package medview.medrecords.exceptions;

/**
 *
 * $Id: NoPatientIdentifierTermException.java,v 1.2 2008/09/09 07:49:45 oloft Exp $
 *
 */

public class NoPatientIdentifierTermException extends Exception
{
	public NoPatientIdentifierTermException()
	{
		super();
	}

	public NoPatientIdentifierTermException(String message)
	{
		super(message);
	}

	public NoPatientIdentifierTermException(Throwable cause)
	{
		super(cause);
	}

	public NoPatientIdentifierTermException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
