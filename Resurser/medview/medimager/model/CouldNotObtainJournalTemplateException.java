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
public class CouldNotObtainJournalTemplateException extends Exception
{
	public CouldNotObtainJournalTemplateException()
	{
		super();
	}

	public CouldNotObtainJournalTemplateException(String message)
	{
		super(message);
	}

	public CouldNotObtainJournalTemplateException(Throwable cause)
	{
		super(cause);
	}

	public CouldNotObtainJournalTemplateException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
