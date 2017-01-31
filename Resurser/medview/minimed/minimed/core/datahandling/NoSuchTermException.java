/*
 * @(#)NoSuchTermException.java
 *
 * $Id: NoSuchTermException.java,v 1.1 2006/05/29 18:32:51 limpan Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package minimed.core.datahandling;

public class NoSuchTermException extends Exception
{
	public NoSuchTermException()
	{
		super();
	}


	public NoSuchTermException(String mess)
	{
		super(mess);
	}
}