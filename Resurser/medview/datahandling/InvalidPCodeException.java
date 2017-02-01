/*
 * @(#)InvalidPCodeException.java
 *
 * $Id: InvalidPCodeException.java,v 1.4 2002/10/12 14:10:57 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

public class InvalidPCodeException extends Exception
{
	public InvalidPCodeException()
	{
		super();
	}

	public InvalidPCodeException(String mess)
	{
		super(mess);
	}
}