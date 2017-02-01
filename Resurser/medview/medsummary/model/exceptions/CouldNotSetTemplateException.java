package medview.medsummary.model.exceptions;

/**
 *
 * $Id: CouldNotSetTemplateException.java,v 1.2 2008/10/23 16:55:05 oloft Exp $
 *
 */

public class CouldNotSetTemplateException extends Exception
{
	public CouldNotSetTemplateException()
	{
		super();
	}

	public CouldNotSetTemplateException(String message)
	{
		super(message);
	}

	public CouldNotSetTemplateException(Throwable cause)
	{
		super(cause);
	}

	public CouldNotSetTemplateException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
