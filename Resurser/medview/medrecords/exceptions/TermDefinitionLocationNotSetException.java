package medview.medrecords.exceptions;

/**
 *
 * $Id: TermDefinitionLocationNotSetException.java,v 1.2 2008/09/09 07:49:45 oloft Exp $
 *
 */

public class TermDefinitionLocationNotSetException extends Exception
{
	public TermDefinitionLocationNotSetException()
	{
		super();
	}

	public TermDefinitionLocationNotSetException(String message)
	{
		super(message);
	}

	public TermDefinitionLocationNotSetException(Throwable cause)
	{
		super(cause);
	}

	public TermDefinitionLocationNotSetException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
