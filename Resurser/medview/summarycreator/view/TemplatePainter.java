/*
 * @(#)TemplatePainter.java
 *
 * $Id: TemplatePainter.java,v 1.6 2005/02/24 16:32:57 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import java.awt.*;
import java.awt.font.*;

import javax.swing.*;
import javax.swing.text.*;

import medview.summarycreator.model.*;

import se.chalmers.cs.medview.docgen.template.*;

public class TemplatePainter implements SummaryCreatorUserProperties
{

	public StyledEditorKit getTemplateEditorKit()
	{
		return editorKit;
	}

	private void initSimpleMembers()
	{
		editorKit = new TemplateEditorKit();

		activeSectionTermFillColor = new Color(253, 253, 253, 100);

		activeSectionHighlightColor = new Color(255, 255, 235, 255);

		inactiveSectionHighlightColor = Color.white;

		inactiveSectionFillColor = new Color(255, 255, 255, 200);

		inactiveSideTextColor = Color.lightGray;

		activeSideTextColor = Color.darkGray;

		sideBarFillColor = Color.lightGray;
	}

	public TemplatePainter(TemplateViewWrapper wrapper)
	{
		this.wrapper = wrapper;

		initSimpleMembers();
	}

	private StyledEditorKit editorKit;

	private TemplateViewWrapper wrapper;

	private Color activeSectionTermFillColor;

	private Color activeSectionHighlightColor;

	private Color inactiveSectionHighlightColor;

	private Color inactiveSectionFillColor;

	private Color inactiveSideTextColor;

	private Color activeSideTextColor;

	private Color sideBarFillColor;


	private class TemplateEditorKit extends StyledEditorKit implements ViewFactory
	{
		public ViewFactory getViewFactory()
		{
			return this;
		}

		public View create(Element elem)
		{
			String kind = elem.getName();

			if (kind != null)
			{
				if (kind.equals(AbstractDocument.SectionElementName))
				{
					return new TemplateView(elem);
				}
			}

			return super.getViewFactory().create(elem);
		}
	}

	private class TemplateView extends BoxView
	{
		public TemplateView(Element elem)
		{
			super(elem, View.Y_AXIS);

			sideFont = new Font("SansSerif", Font.PLAIN, SIDE_FONT_HEIGHT);
		}

		public void paint(Graphics g, Shape a)
		{
			try
			{
				// info common to all parts...

				activeSectionIndex = -1;

				model = wrapper.getModel();

				g2d = (Graphics2D)g;

				shapeRect = (Rectangle)a;

				textPane = (JTextPane)getContainer();

				caretPos = textPane.getCaretPosition();

				sectionModels = model.getAllSectionModels();

				rightMostX = shapeRect.x + shapeRect.width;

				sectRects = new int[sectionModels.length * 4];

				context = g2d.getFontRenderContext();

				margin = textPane.getMargin();


				// extract information about sections...

				for (int ctr = 0; ctr < sectionModels.length; ctr++)
				{
					currSectModel = sectionModels[ctr];

					sectionContainsCaret = currSectModel.containsOffset(caretPos);

					if (sectionContainsCaret)
					{
						activeSectionIndex = ctr;
					}

					sectStartOffset = currSectModel.getStartOffset();

					sectEndOffset = currSectModel.getEndOffset();

					sectStartRect = (Rectangle)modelToView(sectStartOffset, a, Position.Bias.Forward);

					sectEndRect = (Rectangle)modelToView(sectEndOffset, a, Position.Bias.Forward);

					sectEndRectY = sectEndRect.y + sectEndRect.height;

					sectRects[(ctr * 4)] = sectStartRect.x;

					sectRects[(ctr * 4) + 1] = sectStartRect.y;

					sectRects[(ctr * 4) + 2] = rightMostX - sectStartRect.x;

					sectRects[(ctr * 4) + 3] = sectEndRectY - sectStartRect.y;
				}


				// draw sidelines and section name text

				sideBarWidth = margin.left - SIDEBAR_CONTENT_GAP;

				sideBarHeight = margin.top + shapeRect.height + margin.bottom;

				g2d.setPaint(sideBarFillColor);

				g2d.fillRect(0, 0, sideBarWidth, sideBarHeight);

				for (int ctr = 0; ctr < sectionModels.length; ctr++)
				{
					whiteX = sideBarWidth - WHITE_EDGE_GAP - RIGHT_FONT_SIDE_GAP - SIDE_FONT_HEIGHT - LEFT_FONT_SIDE_GAP;

					whiteY = sectRects[(ctr * 4) + 1] + WHITE_TOP_GAP;

					whiteWidth = SIDE_FONT_HEIGHT + LEFT_FONT_SIDE_GAP + RIGHT_FONT_SIDE_GAP;

					whiteHeight = sectRects[(ctr * 4) + 3] - WHITE_BOTTOM_GAP - WHITE_TOP_GAP;

					g2d.setPaint((ctr == activeSectionIndex) ? activeSectionHighlightColor : inactiveSectionHighlightColor);

					g2d.fillRect(whiteX, whiteY, whiteWidth, whiteHeight);

					currSectionName = sectionModels[ctr].getName();

					layout = new TextLayout(currSectionName, sideFont, context);

					advance = layout.getAdvance();

					if (advance >= whiteHeight - (REQUIRED_VERTICAL_EXTRA * 2))
					{
						continue;
					}

					descent = layout.getDescent();

					whiteMiddleY = whiteY + (whiteHeight / 2);

					textBaseX = whiteX + whiteWidth - RIGHT_FONT_SIDE_GAP - descent;

					textBaseY = whiteMiddleY + (advance / 2);

					g2d.rotate(( -Math.PI / 2), textBaseX, textBaseY);

					g2d.setPaint((ctr == activeSectionIndex) ? activeSideTextColor : inactiveSideTextColor);

					layout.draw(g2d, textBaseX, textBaseY);

					g2d.rotate((Math.PI / 2), textBaseX, textBaseY);
				}


				// place content text...

				super.paint(g, a);


				if (sectionModels.length != 0)
				{
					// shadow inactive sections...

					shadowWidth = shapeRect.width;

					shadowX = margin.left;

					if (activeSectionIndex != -1)
					{
						if (activeSectionIndex != 0)
						{
							shadowY = margin.top;

							shadowHeight = sectRects[(activeSectionIndex * 4) + 1] - margin.top;

							g2d.setPaint(inactiveSectionFillColor);

							g2d.fillRect(shadowX, shadowY, shadowWidth, shadowHeight);
						}

						if (activeSectionIndex != (sectionModels.length - 1))
						{
							shadowY = sectRects[(activeSectionIndex * 4) + 1] + sectRects[(activeSectionIndex * 4) + 3];

							shadowHeight = margin.top + shapeRect.height - shadowY;

							g2d.setPaint(inactiveSectionFillColor);

							g2d.fillRect(shadowX, shadowY, shadowWidth, shadowHeight);
						}
					}
					else
					{
						shadowY = margin.top;

						int lastSectRectHeight = sectRects[sectRects.length - 1];

						int lastSectRectStartY = sectRects[sectRects.length - 3];

						shadowHeight = lastSectRectStartY + lastSectRectHeight;

						g2d.setPaint(inactiveSectionFillColor);

						g2d.fillRect(shadowX, shadowY, shadowWidth, shadowHeight);
					}


					// shadow active section terms...

					if (activeSectionIndex != -1)
					{
						termModels = sectionModels[activeSectionIndex].getTermModels();

						for (int ctr = 0; ctr < termModels.length; ctr++)
						{
							currTermModel = termModels[ctr];

							termStartOffset = currTermModel.getStartOffset();

							termEndOffset = currTermModel.getEndOffset();

							termStartRect = (Rectangle)modelToView(termStartOffset, a, Position.Bias.Forward);

							termEndRect = (Rectangle)modelToView(termEndOffset + 1, a, Position.Bias.Forward);

							g2d.setPaint(activeSectionTermFillColor);

							termRectHeight = termStartRect.height;

							while (termStartRect.y != termEndRect.y)
							{
								termRectWidth = shapeRect.width - termStartRect.x;

								g2d.fillRect(termStartRect.x, termStartRect.y, termRectWidth, termRectHeight);

								termStartRect.setLocation(shapeRect.x, termStartRect.y + termRectHeight);
							}

							termRectWidth = termEndRect.x - termStartRect.x;

							g2d.fillRect(termStartRect.x, termStartRect.y, termRectWidth, termRectHeight);
						}
					}
				}
			}
			catch (BadLocationException e)
			{
				e.printStackTrace();
			}
		}

		private Insets margin; // (*)

		private int sideBarWidth; // (*)

		private int sideBarHeight; // (*)

		private int shadowWidth; // (*)

		private int shadowHeight; // (*)

		private int shadowX; // (*)

		private int shadowY; // (*)

		private int whiteX; // (*)

		private int whiteY; // (*)

		private int whiteWidth; // (*)

		private int whiteHeight; // (*)

		private int whiteMiddleY; // (*)

		private float textBaseX; // (*)

		private float textBaseY; // (*)

		private float advance; // (*)

		private float descent; // (*)

		private Graphics2D g2d; // (*)

		private int[] sectRects; // (*)

		private Font sideFont; // (*)

		private TextLayout layout; // (*)

		private String currSectionName; // (*)

		private FontRenderContext context; // (*)

		private int activeSectionIndex; // (*)

		private TemplateModel model; // (*)

		private TermModel[] termModels; // (*)

		private TermModel currTermModel; // (*)

		private int caretPos; // (*)

		private int termRectWidth; // (*)

		private int termRectHeight; // (*)

		private int termStartOffset; // (*)

		private int termEndOffset; // (*)

		private Rectangle termStartRect; // (*)

		private Rectangle termEndRect; // (*)

		private JTextPane textPane; // (*)

		private Rectangle shapeRect; // (*)

		private Rectangle sectStartRect; // (*)

		private Rectangle sectEndRect; // (*)

		private int rightMostX; // (*)

		private int sectEndOffset; // (*)

		private int sectStartOffset; // (*)

		private int sectEndRectY; // (*)

		private SectionModel[] sectionModels; // (*)

		private SectionModel currSectModel; // (*)

		private boolean sectionContainsCaret; // (*)

		private static final int REQUIRED_VERTICAL_EXTRA = 3; // (*)

		private static final int SIDEBAR_CONTENT_GAP = 4; // (*)

		private static final int SIDE_FONT_HEIGHT = 10; // (*)

		private static final int WHITE_EDGE_GAP = 4; // (*)

		private static final int WHITE_TOP_GAP = 3; // (*)

		private static final int WHITE_BOTTOM_GAP = 3; // (*)

		private static final int LEFT_FONT_SIDE_GAP = 3; // (*)

		private static final int RIGHT_FONT_SIDE_GAP = 1; // (*)

		/* NOTE: the view's paint() method is
		 * called very frequently, thus it must
		 * be as fast as possible. To avoid
		 * costly allocation procedures each time
		 * the view is painted, I allocate space
		 * for all the variables as member variables
		 * kept between calls, so this does not have
		 * to be done each time the view is painted. */
	}

}
