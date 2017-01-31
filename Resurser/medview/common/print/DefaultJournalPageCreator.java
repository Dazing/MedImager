/*
 * @(#)DefaultJournalPageCreator.java
 *
 * $Id: DefaultJournalPageCreator.java,v 1.6 2003/08/19 17:56:35 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.print;

import java.util.*;

import misc.gui.print.*;

public class DefaultJournalPageCreator implements JournalPageCreator
{
	public Page createFirstPage(PFPageFormat pageFormat)
	{
		Page page = new Page();

		page.setPFPageFormat(pageFormat);


		DefaultJournalFirstHeader header = new DefaultJournalFirstHeader();

		header.setSize(new UnitSize(new MmUnit(0), new MmUnit(42)));

		header.setBottomMargin(new MmUnit(2));

		header.setHorizontalFill(true); // this is why x above is 0

		page.setHeader(header);


		DefaultJournalFooter footer = new DefaultJournalFooter();

		footer.setSize(new UnitSize(new MmUnit(0), new MmUnit(10)));

		footer.setTopMargin(new MmUnit(2));

		footer.setHorizontalFill(true); // this is why x above is 0

		page.setFooter(footer);


		return page;
	}

	public Page createSubPage(PFPageFormat pageFormat)
	{
		return createFirstPage(pageFormat); // default first and sub the same
	}



	public String getIdentifier()
	{
		return "Default journal template";
	}

	public void setPCode(String pCode)
	{
		this.pCode = pCode;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}



	public DefaultJournalPageCreator()
	{
		this("<< not set >>");
	}

	public DefaultJournalPageCreator(String pCode)
	{
		this(pCode, new Date());
	}

	public DefaultJournalPageCreator(String pCode, Date date)
	{
		this.pCode = pCode;

		this.date = date;
	}

	private String pCode;

	private Date date;
}