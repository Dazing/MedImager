/*
 * @(#)NoSuchTranslatorException.java
 *
 * $Id: NoSuchTranslatorException.java,v 1.4 2002/10/12 14:10:59 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

public class NoSuchTranslatorException extends Exception
{
	public NoSuchTranslatorException()
	{
		super();
	}


	public NoSuchTranslatorException(String mess)
	{
		super(mess);
	}
}