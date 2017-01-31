/*
 * @(#)InvalidDataLocationException.java
 *
 * $Id: InvalidDataLocationException.java,v 1.3 2002/10/12 14:11:00 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling.examination;

public class InvalidDataLocationException extends Exception
{
	public InvalidDataLocationException()
	{
		super();
	}

	public InvalidDataLocationException(String message)
	{
		super(message);
	}
}