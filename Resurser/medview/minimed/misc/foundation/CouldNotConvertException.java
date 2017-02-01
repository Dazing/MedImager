/*
 * @(#) CouldNotConvertException.java
 *
 * $Id: CouldNotConvertException.java,v 1.1 2006/05/29 18:33:02 limpan Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package misc.foundation;

public class CouldNotConvertException extends Exception
{
	public CouldNotConvertException()
	{
		super();
	}

	public CouldNotConvertException(String mess)
	{
		super(mess);
	}
}