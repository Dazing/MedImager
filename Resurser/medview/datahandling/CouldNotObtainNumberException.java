package medview.datahandling;

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
public class CouldNotObtainNumberException extends Exception
{
	public CouldNotObtainNumberException()
	{
		super();
	}

	public CouldNotObtainNumberException(String message)
	{
		super(message);
	}

	public CouldNotObtainNumberException(Throwable cause)
	{
		super(cause);
	}

	public CouldNotObtainNumberException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
