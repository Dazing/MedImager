/*
 * @(#)CouldNotSetException.java
 *
 * $Id: CouldNotSetException.java,v 1.1 2008/07/29 09:48:19 it2aran Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.model.exceptions;

public class CouldNotSetException extends Exception
{
	public CouldNotSetException()
	{
		super();
	}

	public CouldNotSetException(String message)
	{
		super(message);
	}
}