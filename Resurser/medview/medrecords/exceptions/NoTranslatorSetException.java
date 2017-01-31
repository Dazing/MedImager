package medview.medrecords.exceptions;

/**
 *
 * $Id: NoTranslatorSetException.java,v 1.2 2008/09/09 07:49:45 oloft Exp $
 *
 */

public class NoTranslatorSetException extends Exception
{
	public NoTranslatorSetException()
	{
		super();
	}

	public NoTranslatorSetException(String message)
	{
		super(message);
	}

	public NoTranslatorSetException(Throwable cause)
	{
		super(cause);
	}

	public NoTranslatorSetException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
