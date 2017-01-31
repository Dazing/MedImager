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
public class CouldNotSetJournalTemplateException extends Exception
{
	public CouldNotSetJournalTemplateException()
	{
		super();
	}

	public CouldNotSetJournalTemplateException(String message)
	{
		super(message);
	}

	public CouldNotSetJournalTemplateException(Throwable cause)
	{
		super(cause);
	}

	public CouldNotSetJournalTemplateException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
