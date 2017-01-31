/*
 * @(#)ValueLocationNotFoundException.java
 *
 * $Id: ValueLocationNotFoundException.java,v 1.3 2002/10/12 14:10:59 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

public class ValueLocationNotFoundException extends Exception
{
	public ValueLocationNotFoundException()
	{
		super();
	}

	public ValueLocationNotFoundException(String mess)
	{
		super(mess);
	}
}