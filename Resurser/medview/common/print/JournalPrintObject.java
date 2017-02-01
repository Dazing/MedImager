/*
 * @(#)JournalPrintObject.java
 *
 * $Id: JournalPrintObject.java,v 1.5 2003/06/10 00:36:14 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.print;

import java.util.*;

import medview.datahandling.*;

import misc.gui.print.*;

public abstract class JournalPrintObject extends PrintObject
{
	public void setPCode(String pcode)
	{
		this.pcode = pcode;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}


	public String getPCode()
	{
		return pcode;
	}

	public Date getDate()
	{
		return date;
	}


	public JournalPrintObject()
	{
		this(null, null);
	}

	public JournalPrintObject(String pCode)
	{
		this(pCode, null);
	}

	public JournalPrintObject(Date date)
	{
		this(null, date);
	}

	public JournalPrintObject(String pcode, Date date)
	{
		this.pcode = pcode;

		this.date = date;
	}


	protected static MedViewDataHandler mVDH;

	private String pcode;

	private Date date;

	static
	{
		mVDH = MedViewDataHandler.instance();
	}
}