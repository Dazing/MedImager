/*
 * @(#)NoSuchTemplateException.java
 *
 * $Id: NoSuchTemplateException.java,v 1.4 2002/10/12 14:10:59 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

public class NoSuchTemplateException extends Exception
{
	public NoSuchTemplateException()
	{
		super();
	}


	public NoSuchTemplateException(String mess)
	{
		super(mess);
	}
}