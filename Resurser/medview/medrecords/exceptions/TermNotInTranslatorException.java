package medview.medrecords.exceptions;

/**
 *
 * $Id: TermNotInTranslatorException.java,v 1.2 2008/09/09 07:49:45 oloft Exp $
 *
 */

public class TermNotInTranslatorException extends Exception
{
	public TermNotInTranslatorException()
	{
		super();
	}

	public TermNotInTranslatorException(String message)
	{
		super(message);
	}

	public TermNotInTranslatorException(Throwable cause)
	{
		super(cause);
	}

	public TermNotInTranslatorException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
