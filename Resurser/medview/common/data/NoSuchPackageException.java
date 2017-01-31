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
public class NoSuchPackageException extends Exception
{
	public NoSuchPackageException()
	{
		super();
	}

	public NoSuchPackageException(String message)
	{
		super(message);
	}

	public NoSuchPackageException(Throwable cause)
	{
		super(cause);
	}

	public NoSuchPackageException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
