/*
 * @(#)DefinitionLocationNotFoundException.java
 *
 * $Id: DefinitionLocationNotFoundException.java,v 1.3 2002/10/12 14:10:57 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

public class DefinitionLocationNotFoundException extends Exception
{
	public DefinitionLocationNotFoundException()
	{
		super();
	}

	public DefinitionLocationNotFoundException(String mess)
	{
		super(mess);
	}
}