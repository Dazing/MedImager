/*
 * @(#)CouldNotGeneratePCodeException.java
 *
 * $Id: CouldNotGeneratePCodeException.java,v 1.1 2004/01/20 19:42:15 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

public class CouldNotGeneratePCodeException extends Exception
{
	public CouldNotGeneratePCodeException()
	{
		super();
	}

	public CouldNotGeneratePCodeException(String message)
	{
		super(message);
	}
}