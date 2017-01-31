/*
 * @(#)DefaultPatientPageCreator.java
 *
 * $Id: DefaultPatientPageCreator.java,v 1.2 2003/08/19 17:56:35 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.print;

import java.util.*;

import misc.gui.print.*;

/**
 * Creates patient pages (using a patient-specific header
 * and the default journal footer for the graphic surrounding
 * the actual text).
 */
public class DefaultPatientPageCreator implements JournalPageCreator
{
	public Page createFirstPage(PFPageFormat pageFormat)
	{
		Page page = new Page();

		page.setPFPageFormat(pageFormat);


		DefaultPatientFirstHeader header = new DefaultPatientFirstHeader();

		header.setSize(new UnitSize(new MmUnit(0), new MmUnit(15))); // w,h

		header.setBottomMargin(new MmUnit(2));

		header.setHorizontalFill(true); // this is why width above is 0

		page.setHeader(header);


		DefaultJournalFooter footer = new DefaultJournalFooter();

		footer.setSize(new UnitSize(new MmUnit(0), new MmUnit(10))); // w,h

		footer.setTopMargin(new MmUnit(2));

		footer.setHorizontalFill(true); // this is why width above is 0

		page.setFooter(footer);


		return page;
	}

	public Page createSubPage(PFPageFormat pageFormat)
	{
		return createFirstPage(pageFormat); // default first and sub the same
	}



	public String getIdentifier()
	{
		return "Default patient template";
	}

	public void setPCode(String pCode)
	{
		this.pCode = pCode;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}



	public DefaultPatientPageCreator()
	{
		this("<< not set >>", new Date());
	}

	public DefaultPatientPageCreator(String pCode)
	{
		this(pCode, new Date());
	}

	public DefaultPatientPageCreator(String pCode, Date date)
	{
		this.pCode = pCode;

		this.date = date;
	}

	private String pCode;

	private Date date;
}