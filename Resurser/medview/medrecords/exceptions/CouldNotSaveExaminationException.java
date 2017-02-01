package medview.medrecords.exceptions;

/**
 *
 * $Id: CouldNotSaveExaminationException.java,v 1.2 2008/09/09 07:49:45 oloft Exp $
 *
 */

public class CouldNotSaveExaminationException extends Exception
{
	public CouldNotSaveExaminationException()
	{
		super();
	}

	public CouldNotSaveExaminationException(String message)
	{
		super(message);
	}

	public CouldNotSaveExaminationException(Throwable cause)
	{
		super(cause);
	}

	public CouldNotSaveExaminationException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
