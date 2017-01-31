/*
 * @(#)DataLocationNotSetException.java
 *
 * $Id: DataLocationNotSetException.java,v 1.1 2004/02/24 18:43:06 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

public class DataLocationNotSetException extends Exception
{
	public DataLocationNotSetException()
	{
		super();
	}

	public DataLocationNotSetException(String message)
	{
		super(message);
	}
}