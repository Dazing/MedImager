/*
 * @(#) MethodNotSupportedException.java
 *
 * $Id: MethodNotSupportedException.java,v 1.1 2006/05/29 18:33:03 limpan Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package misc.foundation;

/**
 * Exception indicating that a called method is not supported in the current
 * implementation.
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Main Project: The MedView Project @ Chalmers University of Technology</p>
 *
 * <p>Project Web: http://www.cs.chalmers.se/proj/medview/website/medview/</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MethodNotSupportedException extends Exception
{
	public MethodNotSupportedException()
	{
		super();
	}

	public MethodNotSupportedException(String mess)
	{
		super(mess);
	}
}
