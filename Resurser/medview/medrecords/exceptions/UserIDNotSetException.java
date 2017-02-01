package medview.medrecords.exceptions;

/**
 *
 * $Id: UserIDNotSetException.java,v 1.2 2008/09/09 07:49:45 oloft Exp $
 *
 */
 
public class UserIDNotSetException extends Exception
{
	public UserIDNotSetException()
	{
		super();
	}

	public UserIDNotSetException(String message)
	{
		super(message);
	}

	public UserIDNotSetException(Throwable cause)
	{
		super(cause);
	}

	public UserIDNotSetException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
