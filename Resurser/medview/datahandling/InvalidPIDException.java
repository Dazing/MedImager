/*
 * @(#)InvalidPIDException.java
 *
 * $Id: InvalidPIDException.java,v 1.1 2004/01/20 19:42:16 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

public class InvalidPIDException extends Exception
{
	public InvalidPIDException()
	{
		super();
	}

	public InvalidPIDException(String message)
	{
		super(message);
	}
}