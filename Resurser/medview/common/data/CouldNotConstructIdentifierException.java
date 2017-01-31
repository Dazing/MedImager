package medview.common.data;

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
public class CouldNotConstructIdentifierException extends Exception
{
	public CouldNotConstructIdentifierException()
	{
		super();
	}

	public CouldNotConstructIdentifierException(String message)
	{
		super(message);
	}

	public CouldNotConstructIdentifierException(Throwable cause)
	{
		super(cause);
	}

	public CouldNotConstructIdentifierException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
