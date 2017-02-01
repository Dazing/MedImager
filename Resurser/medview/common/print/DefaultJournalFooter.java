/*
 * @(#) DefaultJournalFooter.java
 *
 * $Id: DefaultJournalFooter.java,v 1.2 2003/08/19 17:56:35 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.print;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.text.*;
import java.util.*;

import medview.datahandling.*;

import misc.gui.print.*;

public class DefaultJournalFooter extends NumberedFooter implements
	MedViewLanguageConstants
{

	private String createPrintDateString()
	{
		printDate = mVDH.getLanguageString(OTHER_PRINT_DATE_LS_PROPERTY);

		return (printDate + ": " + formatter.format(new Date())); // current date
	}

	private String createPrintPageString()
	{
		printPage = mVDH.getLanguageString(OTHER_PRINT_PAGE_LS_PROPERTY);

		printOf = mVDH.getLanguageString(OTHER_PRINT_OF_LS_PROPERTY);

		return (printPage + " " + getCurrentPage() + " " + printOf + " " + getTotalPages());
	}

	public void print(Graphics2D g2d)
	{
		computePositionAndSize(); // set up draw origin and size

		origin = getDrawingOrigin().getPoint();

		drawWidth = getDrawingSize().getDimension().width;

		drawHeight = getDrawingSize().getDimension().height;

		context = g2d.getFontRenderContext();

		g2d.setPaint((isEnabled()) ? Color.black : Color.gray);


		// draw line (dashed)

		g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, new float[] {1,1}, 0.0f));

		g2d.draw(new Line2D.Double(new Point2D.Double(origin.x, origin.y), new Point2D.Double(origin.x+drawWidth, origin.y)));


		// draw text (solid)

		g2d.setStroke(new BasicStroke(1));

		tempLayout = new TextLayout(createPrintDateString(), footFont, context);

		tempFloat = origin.y+tempLayout.getLeading()+tempLayout.getAscent()+5; // the y coord for baseline

		tempLayout.draw(g2d, origin.x, tempFloat);

		tempLayout = new TextLayout(createPrintPageString(), footFont, context);

		tempLayout.draw(g2d, origin.x+drawWidth-tempLayout.getAdvance(), tempFloat);
	}


	protected PrintObject subClone()
	{
		DefaultJournalFooter clone = new DefaultJournalFooter();

		clone.setCurrentPage(getCurrentPage());

		clone.setTotalPages(getTotalPages());

		return clone;
	}


	public DefaultJournalFooter()
	{
		super();

		mVDH = MedViewDataHandler.instance();

		footFont = new Font("SansSerif", Font.PLAIN, 10);
	}

	// PERFORMANCE MEMBERS ---------------->

	private Point origin;

	private int drawWidth;

	private int drawHeight;

	private String printPage;

	private String printDate;

	private String printOf;

	private float tempFloat;

	private TextLayout tempLayout;

	private FontRenderContext context;

	//-----------------------------------

	private Font footFont;

	private MedViewDataHandler mVDH;

	private static final DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT);

}