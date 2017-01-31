package medview.medrecords.exceptions;

/**
 *
 * $Id: CouldNotObtainTemplateException.java,v 1.2 2008/09/09 07:49:45 oloft Exp $
 *
 */

public class CouldNotObtainTemplateException extends Exception
{
	public CouldNotObtainTemplateException()
	{
		super();
	}

	public CouldNotObtainTemplateException(String message)
	{
		super(message);
	}

	public CouldNotObtainTemplateException(Throwable cause)
	{
		super(cause);
	}

	public CouldNotObtainTemplateException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
