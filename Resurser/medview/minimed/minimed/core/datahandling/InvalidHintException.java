package minimed.core.datahandling;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class InvalidHintException extends Exception
{
	public InvalidHintException()
	{
		super();
	}

	public InvalidHintException(String message)
	{
		super(message);
	}

	public InvalidHintException(Throwable cause)
	{
		super(cause);
	}

	public InvalidHintException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
