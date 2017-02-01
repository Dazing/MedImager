/*
 * @(#)NoSuchTermException.java
 *
 * $Id: NoSuchTermException.java,v 1.6 2002/10/12 14:10:59 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

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