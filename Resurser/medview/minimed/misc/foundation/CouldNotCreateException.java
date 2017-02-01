/*
 * @(#) CouldNotCreateException.java
 *
 * $Id: CouldNotCreateException.java,v 1.1 2006/05/29 18:33:02 limpan Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package misc.foundation;

public class CouldNotCreateException extends Exception
{
	public CouldNotCreateException()
	{
		super();
	}

	public CouldNotCreateException(String message)
	{
		super(message);
	}
}