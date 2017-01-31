package medview.medrecords.exceptions;

/**
 *
 * $Id: UserNameNotSetException.java,v 1.2 2008/09/09 07:49:45 oloft Exp $
 *
 */
 
public class UserNameNotSetException extends Exception
{
	public UserNameNotSetException()
	{
		super();
	}

	public UserNameNotSetException(String message)
	{
		super(message);
	}

	public UserNameNotSetException(Throwable cause)
	{
		super(cause);
	}

	public UserNameNotSetException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
