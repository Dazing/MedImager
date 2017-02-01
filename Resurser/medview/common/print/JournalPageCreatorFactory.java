/*
 * @(#)JournalPageCreatorFactory.java
 *
 * $Id: JournalPageCreatorFactory.java,v 1.5 2003/06/10 00:36:13 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.print;

import java.util.*;

import misc.gui.print.*;

public class JournalPageCreatorFactory extends AbstractPageCreatorFactory
{

	/**
	 * Will traverse all currently set page
	 * creators and set their pcode-value to
	 * the specified one (the default page
	 * creator's pcode will also be modified).
	 * @param pCode the pcode to use in all
	 * contained page creators.
	 */
	public void setPCode(String pCode)
	{
		PageCreator[] pCs = getSupportedPageCreators();

		for (int ctr=0; ctr<pCs.length; ctr++)
		{
			((JournalPageCreator)pCs[ctr]).setPCode(pCode);
		}
	}

	/**
	 * Will traverse all currently set page
	 * creators and set their date-value to
	 * the specified one (the default page
	 * creator's date will also be modified).
	 * @param date the date to use in all
	 * contained page creators.
	 */
	public void setDate(Date date)
	{
		PageCreator[] pCs = getSupportedPageCreators();

		for (int ctr=0; ctr<pCs.length; ctr++)
		{
			((JournalPageCreator)pCs[ctr]).setDate(date);
		}
	}


	/**
	 * Returns the identifier that identifies the one
	 * default journal page creator to use if no other
	 * page creators explicitly have been added to this
	 * factory.
	 * @return the identifier identifying the one default
	 * journal page creator to use if no other page
	 * creators have been set.
	 */
	public String getDefaultPageCreatorIdentifier()
	{
		return defaultPageCreator.getIdentifier();
	}

	/**
	 * Creates and returns the default page creator
	 * to use (which automatically will be added to
	 * the list of page creators currently available
	 * to the user).
	 * @return the default page creator.
	 */
	public PageCreator[] getSupportedPageCreators()
	{
		if (defaultPageCreator == null) // not yet created
		{
			defaultPageCreator = new DefaultJournalPageCreator();

			defaultPageCreator.setDate(date);

			defaultPageCreator.setPCode(pCode);
		}

		if (patientPageCreator == null) // not yet created
		{
			patientPageCreator = new DefaultPatientPageCreator();

			patientPageCreator.setDate(date);

			patientPageCreator.setPCode(pCode);
		}

		return new PageCreator[] { defaultPageCreator, patientPageCreator };
	}


	/**
	 * Creates a journal page creator factory which will
	 * create pages using the text '<pcode>' for the
	 * pcode value and the current time for the date.
	 */
	public JournalPageCreatorFactory()
	{
		this("<pcode>", Calendar.getInstance().getTime());
	}

	/**
	 * Creates a journal page creator factory which will
	 * create pages using the specified pcode and the
	 * specified date.
	 * @param pCode the pcode to use when creating pages.
	 * @param date the data to use when creating pages.
	 */
	public JournalPageCreatorFactory(String pCode, Date date)
	{
		this.pCode = pCode;

		this.date = date;
	}

	private String pCode;

	private Date date;

	private JournalPageCreator defaultPageCreator;

	private JournalPageCreator patientPageCreator;

}