/*
 * @(#)DefaultTreeFileDateParser.java
 *
 * $Id: DefaultTreeFileDateParser.java,v 1.4 2004/02/28 17:51:26 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling.examination;

import java.text.*;
import java.util.*;

/**
 * The default implementation of the TreeFileDateParser
 * interface. This implementation is capable of parsing
 * dates in the formats '2001-09-22 10:43' and also in
 * the old treefile format '97-03-23 07:39'. This formatter
 * assumes that the dates are in the swedish locale format,
 * so tree file output in other parts of the system needs
 * to take this into consideration. The visual representation
 * of a date can still be modified, but it is important that
 * there is a uniform way of outputting and parsing dates,
 * this is achieved by agreeing that date strings are always
 * handled in the swedish locale.
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class DefaultTreeFileDateParser implements TreeFileDateParser
{
	public Date extractDate(String nodeDate) throws CouldNotParseDateException
	{
		try
		{
			nodeDate = convertOldDateStyle(nodeDate);

			return formatter.parse(nodeDate);
		}
		catch (ParseException e)
		{
			throw new CouldNotParseDateException(e.getMessage());
		}
	}

	private String convertOldDateStyle(String nodeDate)
	{
		StringTokenizer tok = new StringTokenizer(nodeDate, DATE_DELIMS);

		String yearToken = tok.nextToken();

		if (yearToken.length() == 2)
		{
			yearToken = "19" + yearToken; // Y2K!

			nodeDate = yearToken + nodeDate.substring(2);
		}

		return nodeDate;
	}


	public DefaultTreeFileDateParser()
	{
		this(new Locale("sv", "SE"));
	}

	protected DefaultTreeFileDateParser(Locale locale)
	{
		this(locale, DateFormat.SHORT, DateFormat.MEDIUM);
	}

	protected DefaultTreeFileDateParser(Locale locale, int timeStyle, int dateStyle)
	{
		formatter = DateFormat.getDateTimeInstance(timeStyle, dateStyle, locale);
	}

	private DateFormat formatter;

	private static final String DATE_DELIMS = "-/\\";
}