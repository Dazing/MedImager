package medview.medrecords.exceptions;

/**
 *
 * $Id: TermValueLocationNotSetException.java,v 1.2 2008/09/09 07:49:45 oloft Exp $
 *
 */

public class TermValueLocationNotSetException extends Exception
{
	public TermValueLocationNotSetException()
	{
		super();
	}

	public TermValueLocationNotSetException(String message)
	{
		super(message);
	}

	public TermValueLocationNotSetException(Throwable cause)
	{
		super(cause);
	}

	public TermValueLocationNotSetException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
