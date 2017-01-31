/*
 * @(#)LanguageException.java
 *
 * $Id: LanguageException.java,v 1.4 2002/10/12 14:10:58 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

public class LanguageException extends Exception
{
	public LanguageException()
	{
		super();
	}


	public LanguageException(String mess)
	{
		super(mess);
	}
}