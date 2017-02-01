package medview.medsummary.model.exceptions;

/**
 *
 * $Id: CouldNotAddPatientException.java,v 1.2 2008/10/23 16:55:05 oloft Exp $
 *
 */

public class CouldNotAddPatientException extends Exception
{
	public CouldNotAddPatientException()
	{
		super();
	}

	public CouldNotAddPatientException(String message)
	{
		super(message);
	}

	public CouldNotAddPatientException(Throwable cause)
	{
		super(cause);
	}

	public CouldNotAddPatientException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
