/*
 * @(#)DefaultJournalSubHeader.java
 *
 * $Id: DefaultJournalSubHeader.java,v 1.3 2002/10/12 14:10:55 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.print;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.util.*;
import java.text.*;

import misc.gui.print.*;

public class DefaultJournalSubHeader extends JournalPrintObject
{
	public void setEnabled(boolean flag)
	{
		super.setEnabled(flag);

		if (flag)
		{
			textColor = Color.black;
			lineColor = Color.black;
		}
		else
		{
			textColor = Color.gray;
			lineColor = Color.gray;
		}
	}





	public void print(Graphics2D g2d)
	{
		// set up draw origin and size

		computePositionAndSize();


		// common variables

		Point2D origin = getDrawingOrigin().getPoint2D();

		Dimension2D drawSize = getDrawingSize().getDimension2D();

		FontRenderContext context = g2d.getFontRenderContext();


		// draw line

		Point2D lineStartPoint = new Point2D.Double(
			origin.getX(), origin.getY() + drawSize.getHeight() - lineContentGap - contentLineWidth);

		Point2D lineEndPoint = new Point2D.Double(
			lineStartPoint.getX() + drawSize.getWidth(), lineStartPoint.getY());

		Line2D line = new Line2D.Double(lineStartPoint, lineEndPoint);

		g2d.setStroke(new BasicStroke(contentLineWidth));

		g2d.setPaint(lineColor);

		g2d.draw(line);


		// place descriptive text

		Date dateToFormat = getDate();

		if (dateToFormat == null) { dateToFormat = new Date(); }

		String formattedDate = formatter.format(dateToFormat);

		TextLayout descLeftLayout = new TextLayout(pcodeLabel + getPCode(), descFont, context);

		TextLayout descRightLayout = new TextLayout(dateLabel + formattedDate, descFont, context);

		float descLeftAscent = descLeftLayout.getAscent();

		float descRightAscent = descRightLayout.getAscent();

		float descMaxAscent = Math.max(descLeftAscent, descRightAscent);

		float descLeftDescent = descLeftLayout.getDescent();

		float descRightDescent = descRightLayout.getDescent();

		float descMaxDescent = Math.max(descLeftDescent, descRightDescent);

		float descLeftHeight = descLeftAscent + descLeftDescent;

		float descRightHeight = descRightAscent + descRightDescent;

		float descMaxHeight = Math.max(descLeftHeight, descRightHeight);

		float descLeftAdvance = descLeftLayout.getAdvance();

		float descRightAdvance = descRightLayout.getAdvance();

		Point2D descLeftStartPoint = new Point2D.Double(
			origin.getX(), lineStartPoint.getY() - descLineGap - descMaxDescent);

		Point2D descRightStartPoint = new Point2D.Double(
			origin.getX() + drawSize.getWidth() - descRightAdvance, descLeftStartPoint.getY());

		g2d.setStroke(new BasicStroke(1)); g2d.setPaint(textColor);

		descLeftLayout.draw(g2d, (float) descLeftStartPoint.getX(), (float) descLeftStartPoint.getY());

		descRightLayout.draw(g2d, (float) descRightStartPoint.getX(), (float) descRightStartPoint.getY());

	}








	public void setDescriptionLineGap(int g) { this.descLineGap = g; }

	public void setContentLineWidth(int w) { this.contentLineWidth = w; }

	public void setDescriptionFont(Font f) { this.descFont = f; }

	public void setDateLabel(String l) { this.dateLabel = l; }

	public void setPCodeLabel(String l) { this.pcodeLabel = l; }

	public void setLineColor(Color c) { this.lineColor = c; }

	public void setTextColor(Color c) { this.textColor = c; }

	public void setLineContentGap(int g) { this.lineContentGap = g; }





	protected PrintObject subClone()
	{
		DefaultJournalSubHeader clone = new DefaultJournalSubHeader(getPCode(), getDate());

		clone.descLineGap = descLineGap;

		clone.contentLineWidth = contentLineWidth;

		clone.formatter = (DateFormat) formatter.clone();

		clone.descFont = descFont;

		clone.dateLabel = dateLabel;

		clone.pcodeLabel = pcodeLabel;

		clone.lineColor = lineColor;

		clone.textColor = textColor;

		clone.lineContentGap = lineContentGap;

		return clone;
	}





	private void initHeader()
	{
		this.descLineGap = 3;

		this.contentLineWidth = 2;

		this.formatter = DateFormat.getDateInstance(DateFormat.SHORT);

		this.descFont = new Font("serif", Font.PLAIN, 12);

		this.dateLabel = "Undersökningsdatum: ";

		this.pcodeLabel = "Patientkod: ";

		this.lineColor = Color.black;

		this.textColor = Color.black;

		this.lineContentGap = 3;
	}



	public DefaultJournalSubHeader(String pcode, Date date)
	{
		super(pcode, date);

		initHeader();
	}

	private int descLineGap;

	private int contentLineWidth;

	private DateFormat formatter;

	private int lineContentGap;

	private String pcodeLabel;

	private String dateLabel;

	private Color lineColor;

	private Color textColor;

	private Font descFont;
}