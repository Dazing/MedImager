/*
 * @(#)InvalidPrefixException.java
 *
 * $Id: InvalidPrefixException.java,v 1.1 2004/01/20 19:42:16 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

public class InvalidPrefixException extends Exception
{
	public InvalidPrefixException()
	{
		super();
	}

	public InvalidPrefixException(String message)
	{
		super(message);
	}
}