/*
 * @(#)InvalidRawPIDException.java
 *
 * $Id: InvalidRawPIDException.java,v 1.1 2004/04/08 13:19:41 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

public class InvalidRawPIDException extends Exception
{
	public InvalidRawPIDException()
	{
		super();
	}

	public InvalidRawPIDException(String message)
	{
		super(message);
	}
}