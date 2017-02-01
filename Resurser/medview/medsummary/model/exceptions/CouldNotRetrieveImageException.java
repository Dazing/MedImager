package medview.medsummary.model.exceptions;

/**
 *
 * $Id: CouldNotRetrieveImageException.java,v 1.2 2008/10/23 16:55:05 oloft Exp $
 *
 */

public class CouldNotRetrieveImageException extends Exception
{
	public CouldNotRetrieveImageException()
	{
		super();
	}

	public CouldNotRetrieveImageException(String message)
	{
		super(message);
	}

	public CouldNotRetrieveImageException(Throwable cause)
	{
		super(cause);
	}

	public CouldNotRetrieveImageException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
