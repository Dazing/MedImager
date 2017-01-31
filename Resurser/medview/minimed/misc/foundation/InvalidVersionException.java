/*
 * @(#) InvalidVersionException.java
 *
 * $Id: InvalidVersionException.java,v 1.1 2006/05/29 18:33:02 limpan Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package misc.foundation;

/**
 * Exception indicating that something is of an inproper version.
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
public class InvalidVersionException extends Exception
{
	public InvalidVersionException()
	{
		super();
	}

	public InvalidVersionException(String mess)
	{
		super(mess);
	}
}
