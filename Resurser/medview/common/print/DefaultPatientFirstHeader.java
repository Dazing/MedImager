/*
 * @(#)DefaultPatientFirstHeader.java
 *
 * $Id: DefaultPatientFirstHeader.java,v 1.3 2005/03/16 13:50:53 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.print;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

import medview.datahandling.*;

import misc.gui.print.*;

/**
 * A PrintObject implementation of a header for displaying
 * patient information. Note that the language specific
 * 'patient information' string is obtained at creation of
 * the header, and the header must be re-created to use any
 * change in language.
 *
 * @author Fredrik Lindahl
 */
public class DefaultPatientFirstHeader extends JournalPrintObject
	implements MedViewMediaConstants, MedViewLanguageConstants
{

	public void print(Graphics2D g2d)
	{
		computePositionAndSize(); // set up draw origin and size

		origin = getDrawingOrigin().getPoint();

		drawWidth = getDrawingSize().getDimension().width;

		drawHeight = getDrawingSize().getDimension().height;

		context = g2d.getFontRenderContext();


		// place image in top left

		folkWidthPoints = (int) new MmUnit(47).getPoints();

		folkHeightPoints = (int) new MmUnit(12).getPoints();

		g2d.drawImage(folkImage, origin.x, origin.y, folkWidthPoints, folkHeightPoints, null);

		if (!isEnabled())
		{
			g2d.setPaint(new Color(255,255,255,122)); // transparent white

			g2d.fill(new Rectangle(origin.x, origin.y, (int)new MmUnit(47).getPoints(), (int)new MmUnit(12).getPoints()));
		}


		// place clinic information text

		tempLayout = new TextLayout(patInfoText, infoFont, context);

		tempAscent = tempLayout.getAscent();

		tempDescent = tempLayout.getDescent();

		tempLeading = tempLayout.getLeading();

		tempAdvance = tempLayout.getAdvance();

		g2d.setStroke(new BasicStroke(1));

		g2d.setPaint((isEnabled()) ? Color.black : Color.gray);

		textHeight = tempAscent + tempDescent;

		textVGap = (drawHeight - 1 - textHeight) / 2;

		textXOrig = origin.x + (drawWidth - folkWidthPoints) / 2 - tempAdvance / 2 + folkWidthPoints;

		textYOrig = origin.y + drawHeight - 1 - textVGap - tempDescent;

		tempLayout.draw(g2d, textXOrig, textYOrig);


		// draw dividing lines

		g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, new float[] {1,1}, 0.0f));

		g2d.setPaint((isEnabled()) ? Color.black : Color.gray);

		g2d.draw(new Line2D.Double(
			new Point2D.Double(origin.x, origin.y+drawHeight-1),
			new Point2D.Double(origin.x+drawWidth, origin.y+drawHeight-1)));
	}



	protected PrintObject subClone()
	{
		return new DefaultPatientFirstHeader();
	}



	private void setupLanguageStrings()
	{
		String lS = OTHER_PATIENT_INFORMATION_LS_PROPERTY;

		patInfoText = mVDH.getLanguageString(lS);
	}

	public DefaultPatientFirstHeader()
	{
		setupLanguageStrings();
	}


	private Point origin;

	private int drawWidth;

	private int drawHeight;

	private float tempAscent;

	private float tempLeading;

	private float tempDescent;

	private float tempAdvance;

	private float textHeight;

	private float textVGap;

	private float textXOrig;

	private float textYOrig;

	private String patInfoText;

	private int folkWidthPoints;

	private int folkHeightPoints;

	private TextLayout tempLayout;

	private static Image folkImage;

	private FontRenderContext context;

	private static Font infoFont = new Font("Serif", Font.BOLD, 15);

	static
	{
		folkImage = MedViewDataHandler.instance().getImage(FOLKTANDVARDEN_SIGNATURE_IMAGE_ICON);

		MediaTracker tracker = new MediaTracker(new Component() {}); tracker.addImage(folkImage, 1);

		try	{ tracker.waitForID(1);	} catch (InterruptedException e) { e.printStackTrace(); }
	}

}
