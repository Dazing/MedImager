/*
 * @(#)CouldNotParseDateException.java
 *
 * $Id: CouldNotParseDateException.java,v 1.3 2002/10/12 14:10:59 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling.examination;

public class CouldNotParseDateException extends Exception
{
	public CouldNotParseDateException()
	{
		super();
	}

	public CouldNotParseDateException(String message)
	{
		super(message);
	}
}