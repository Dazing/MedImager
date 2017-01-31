/*
 * @(#)CouldNotConstructDateException.java
 *
 * $Id: CouldNotConstructDateException.java,v 1.1 2006/09/13 21:48:53 oloft Exp $
 *
 * --------------------------------
 * Original author: Olof  Torgersson
 * --------------------------------
 */
//

package medview.common.data;

public class CouldNotConstructDateException extends Exception
{
	public CouldNotConstructDateException()
	{
		super();
	}
	
	public CouldNotConstructDateException(String message)
	{
		super(message);
	}
	
	public CouldNotConstructDateException(Throwable cause)
	{
		super(cause);
	}
	
	public CouldNotConstructDateException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
