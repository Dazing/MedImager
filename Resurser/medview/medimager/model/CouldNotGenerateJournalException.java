package medview.medimager.model;

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
public class CouldNotGenerateJournalException extends Exception
{
	public CouldNotGenerateJournalException()
	{
		super();
	}

	public CouldNotGenerateJournalException(String message)
	{
		super(message);
	}

	public CouldNotGenerateJournalException(Throwable cause)
	{
		super(cause);
	}

	public CouldNotGenerateJournalException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
