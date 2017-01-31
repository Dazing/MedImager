package medview.common.generator;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class SynchronizingException extends Exception
{
	public SynchronizingException()
	{
		super();
	}

	public SynchronizingException(String message)
	{
		super(message);
	}

	public SynchronizingException(Throwable cause)
	{
		super(cause);
	}

	public SynchronizingException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
