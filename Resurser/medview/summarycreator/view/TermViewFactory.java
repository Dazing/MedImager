/*
 * @(#)TermViewFactory.java
 *
 * $Id: TermViewFactory.java,v 1.9 2005/03/16 13:49:35 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import se.chalmers.cs.medview.docgen.*;

/**
 *
 */
public class TermViewFactory
{

	public AbstractTermView createEmptyTermView( )
	{
		return new EmptyTermView();
	}

	public AbstractTermView createTermView(String typeDesc)
	{
		if (typeDesc.equals(TermHandler.NON_TRANSLATED_TYPE_DESCRIPTOR))
		{
			return new FreeTextTermView();
		}
		else if (typeDesc.equals(BasicTermHandler.INTERVAL_TYPE_DESCRIPTOR))
		{
			return new IntervalTranslationModelView();
		}
		else if (typeDesc.equals(BasicTermHandler.MULTIPLE_TYPE_DESCRIPTOR))
		{
			return new MultipleTranslationModelView();
		}
		else if (typeDesc.equals(BasicTermHandler.REGULAR_TYPE_DESCRIPTOR))
		{
			return new RegularTranslationModelView();
		}
		else
		{
			System.out.print("WARNING: TermViewFactory : ");

			System.out.print("the type '" + typeDesc + "' was not ");

			System.out.print("recognized when trying to obtain ");

			System.out.print("a view for it, will return the ");

			System.out.print("empty view...");

			return createEmptyTermView();
		}
	}

	// SINGLETON

	public static TermViewFactory instance( )
	{
		if (instance == null)
		{
			instance = new TermViewFactory();
		}

		return instance;
	}

	private TermViewFactory() {} // defeat instantiation

	private static TermViewFactory instance;

}
