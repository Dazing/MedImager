/*
 * @(#)DefaultJournalFirstHeader.java
 *
 * $Id: DefaultJournalFirstHeader.java,v 1.8 2005/03/16 13:50:53 lindahlf Exp $
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

public class DefaultJournalFirstHeader extends JournalPrintObject
	implements MedViewMediaConstants, MedViewPrintProperties
{

	public void print(Graphics2D g2d)
	{
		computePositionAndSize(); // set up draw origin and size

		origin = getDrawingOrigin().getPoint();

		drawWidth = getDrawingSize().getDimension().width;

		drawHeight = getDrawingSize().getDimension().height;

		context = g2d.getFontRenderContext();


		// place image in top left

		logoWidth = (float) folkImage.getWidth(null);

		logoHeight = (float) folkImage.getHeight(null);

		logoRatio = logoWidth / logoHeight;

		scaledLogoHeight = (int) new MmUnit(12).getPoints();

		scaledLogoWidth = (int) (logoRatio * (float)scaledLogoHeight);

		g2d.drawImage(folkImage, origin.x, origin.y, scaledLogoWidth, scaledLogoHeight, null);

		if (!isEnabled())
		{
			g2d.setPaint(new Color(255,255,255,122)); // transparent white

			g2d.fill(new Rectangle(origin.x, origin.y, (int)new MmUnit(47).getPoints(), (int)new MmUnit(12).getPoints()));
		}


		// place clinic information text

		tempLayout = new TextLayout(addrLine5, addrNormFont, context);

		tempLeading = tempLayout.getLeading();

		tempDescent = tempLayout.getDescent();

		tempAscent = tempLayout.getAscent();

		g2d.setStroke(new BasicStroke(1));

		g2d.setPaint((isEnabled()) ? Color.black : Color.gray);

		tempLayout.draw(g2d, origin.x, origin.y+drawHeight-tempLeading-tempDescent-5); // -5 <- space betw. text and line

		tempLayout = new TextLayout(addrLine4, addrNormFont, context);

		tempLayout.draw(g2d, origin.x, origin.y+drawHeight-2*tempLeading-2*tempDescent-tempAscent-5);

		tempLayout = new TextLayout(addrLine3, addrNormFont, context);

		tempLayout.draw(g2d, origin.x, origin.y+drawHeight-3*tempLeading-3*tempDescent-2*tempAscent-5);

		tempLayout = new TextLayout(addrLine2, addrNormFont, context);

		tempLayout.draw(g2d, origin.x, origin.y+drawHeight-4*tempLeading-4*tempDescent-3*tempAscent-5);

		tempLayout = new TextLayout(addrLine1, addrBoldFont, context);

		tempLayout.draw(g2d, origin.x, origin.y+drawHeight-5*tempLeading-5*tempDescent-4*tempAscent-5);


		// draw dividing lines

		g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, new float[] {1,1}, 0.0f));

		g2d.setPaint((isEnabled()) ? Color.black : Color.gray);

		g2d.draw(new Line2D.Double(
			new Point2D.Double(origin.x, origin.y+drawHeight-1),
			new Point2D.Double(origin.x+drawWidth, origin.y+drawHeight-1)));

		g2d.draw(new Line2D.Double(
			new Point2D.Double(origin.x+drawWidth-(new MmUnit(88).getPoints()), origin.y),
			new Point2D.Double(origin.x+drawWidth-(new MmUnit(88).getPoints()), origin.y+drawHeight-1)));
	}

	protected PrintObject subClone()
	{
		return new DefaultJournalFirstHeader();
	}

	/**
	 * Will load the logotype image that is
	 * placed in the top left corner of the
	 * header. If the user property of the
	 * surrounding logotype image path has
	 * not been set, this implementation will
	 * use a built-in default (taken as one
	 * of the image constants of the data layer).
	 * If the property has been set, the static
	 * member defining the image used in the
	 * print method will be set to the loaded
	 * image specified by the path. If the image
	 * for some reason cannot be loaded, the
	 * implementation will fall back to the
	 * default built-in image.
	 */
	private static void staticallyLoadImage()
	{
		if (mVDH.isPropertySet(SURROUNDING_JOURNAL_LOGOTYPE_PATH_PROPERTY))
		{
			currImagePath = mVDH.getUserProperty(SURROUNDING_JOURNAL_LOGOTYPE_PATH_PROPERTY);

			folkImage = Toolkit.getDefaultToolkit().getImage(currImagePath);

			MediaTracker tracker = new MediaTracker(new Component() {});

			tracker.addImage(folkImage, 1);

			try { tracker.waitForID(1); } catch (InterruptedException e) { e.printStackTrace(); }

			if (tracker.isErrorAny()) // if specified image is erroneous - try load built-in image default
			{
				currImagePath = null; // indicates we are using built-in default image, else path of used

				tracker.removeImage(folkImage);

				folkImage = mVDH.getImage(FOLKTANDVARDEN_SIGNATURE_IMAGE_ICON);

				tracker.addImage(folkImage, 1);

				try { tracker.waitForID(1);	} catch (InterruptedException e) { e.printStackTrace(); }
			}
		}
		else // load built-in image default
		{
			currImagePath = null; // indicates we are using built-in default image, else path of used

			folkImage = mVDH.getImage(FOLKTANDVARDEN_SIGNATURE_IMAGE_ICON);

			MediaTracker tracker = new MediaTracker(new Component() {}); tracker.addImage(folkImage, 1);

			try	{ tracker.waitForID(1);	} catch (InterruptedException e) { e.printStackTrace(); }
		}
	}



	/**
	 * Constructs a default journal first
	 * header print object. At construction,
	 * the set print properties in the MedView
	 * context are checked. The address lines
	 * are instance members that are always
	 * set at construction to the value they
	 * contain in the data layer. If they are
	 * not specified in the data layer, the
	 * values are set to the default address.
	 * <br><br>
	 * The logotype image takes some time to
	 * load, and it would be very slow to load
	 * it for each header instance, therefore
	 * it is a static member. Because of this,
	 * some care must be taken to ensure that
	 * the currently set (according to the data
	 * layer property) image is used, but at
	 * the same time that this is not done at
	 * unnecessary times which would slow down
	 * things considerably.
	 */
	public DefaultJournalFirstHeader()
	{
		if (mVDH.isPropertySet(SURROUNDING_JOURNAL_ADDRESS_LINE_1_PROPERTY)) // this is fast
		{
			addrLine1 = mVDH.getUserProperty(SURROUNDING_JOURNAL_ADDRESS_LINE_1_PROPERTY);

			addrLine2 = mVDH.getUserProperty(SURROUNDING_JOURNAL_ADDRESS_LINE_2_PROPERTY);

			addrLine3 = mVDH.getUserProperty(SURROUNDING_JOURNAL_ADDRESS_LINE_3_PROPERTY);

			addrLine4 = mVDH.getUserProperty(SURROUNDING_JOURNAL_ADDRESS_LINE_4_PROPERTY);

			addrLine5 = mVDH.getUserProperty(SURROUNDING_JOURNAL_ADDRESS_LINE_5_PROPERTY);

			if (addrLine1.length() == 0) { addrLine1 = " "; } // zero length strings not allowed during print

			if (addrLine2.length() == 0) { addrLine2 = " "; } // zero length strings not allowed during print

			if (addrLine3.length() == 0) { addrLine3 = " "; } // zero length strings not allowed during print

			if (addrLine4.length() == 0) { addrLine4 = " "; } // zero length strings not allowed during print

			if (addrLine5.length() == 0) { addrLine5 = " "; } // zero length strings not allowed during print
		}

		if (mVDH.isPropertySet(SURROUNDING_JOURNAL_LOGOTYPE_PATH_PROPERTY)) // this is slow
		{
			String dataLoc = mVDH.getUserProperty(SURROUNDING_JOURNAL_LOGOTYPE_PATH_PROPERTY);

			if (currImagePath == null) // we are still using the built-in image
			{
				staticallyLoadImage();
			}
			else if (!currImagePath.equals(dataLoc))
			{
				staticallyLoadImage();
			}
		}
		else // we should use the built-in image
		{
			if (folkImage == null) // first time
			{
				staticallyLoadImage();
			}
			else if (currImagePath != null) // path indicates non-built-in folkImage
			{
				staticallyLoadImage();
			}
		}
	}


	// PERFORMANCE MEMBERS ---------------->

	private Point origin;

	private int drawWidth;

	private int drawHeight;

	private float logoWidth;

	private float logoHeight;

	private float logoRatio;

	private float tempAscent;

	private float tempLeading;

	private float tempDescent;

	private int scaledLogoHeight;

	private int scaledLogoWidth;

	private TextLayout tempLayout;

	private FontRenderContext context;

	//-----------------------------------

	private String addrLine1 = "Kliniken för Oral medicin - Specialisttandvården"; //  built-in default

	private String addrLine2 = "Odontologen, Medicinaregatan 12C, vån 6"; //  built-in default

	private String addrLine3 = "413 90 Göteborg"; //  built-in default

	private String addrLine4 = "Tel: 031-741 34 00"; //  built-in default

	private String addrLine5 = "Fax: 031-741 34 50"; //  built-in default

	private static String currImagePath = null; // null indicates using built-in image

	private static Font addrBoldFont = new Font("SansSerif", Font.BOLD, 10); // constant

	private static Font addrNormFont = new Font("SansSerif", Font.PLAIN, 10); // constant

	private static Image folkImage; // checked at construction

}
