package medview.datahandling;

/**
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Main Project: The MedView Project @ Chalmers University of Technology</p>
 *
 * <p>Sub Project: none</p>
 *
 * <p>Project Web http://www.cs.chalmers.se/proj/medview/website/medview/</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class CouldNotSearchException extends Exception
{
	public CouldNotSearchException()
	{
		super();
	}

	public CouldNotSearchException(String message)
	{
		super(message);
	}

	public CouldNotSearchException(Throwable cause)
	{
		super(cause);
	}

	public CouldNotSearchException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
