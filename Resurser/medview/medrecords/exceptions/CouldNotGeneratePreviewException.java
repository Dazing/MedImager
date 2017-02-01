package medview.medrecords.exceptions;

/**
 *
 * $Id: CouldNotGeneratePreviewException.java,v 1.2 2008/09/09 07:49:45 oloft Exp $
 *
 */

public class CouldNotGeneratePreviewException extends Exception
{
	public CouldNotGeneratePreviewException()
	{
		super();
	}

	public CouldNotGeneratePreviewException(String message)
	{
		super(message);
	}

	public CouldNotGeneratePreviewException(Throwable cause)
	{
		super(cause);
	}

	public CouldNotGeneratePreviewException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
