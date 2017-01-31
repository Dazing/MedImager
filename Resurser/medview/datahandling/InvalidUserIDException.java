/*
 * @(#)InvalidUserIDException.java
 *
 * $Id: InvalidUserIDException.java,v 1.1 2004/02/02 17:20:42 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

public class InvalidUserIDException extends Exception
{
	public InvalidUserIDException()
	{
		super();
	}

	public InvalidUserIDException(String message)
	{
		super(message);
	}
}