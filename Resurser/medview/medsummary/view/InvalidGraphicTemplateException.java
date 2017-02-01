/*
 * @(#)CouldNotBuildEngineException.java
 *
 * $Id: InvalidGraphicTemplateException.java,v 1.1 2003/06/10 00:36:14 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.view;

public class InvalidGraphicTemplateException extends Exception
{
	public InvalidGraphicTemplateException()
	{
		super();
	}

	public InvalidGraphicTemplateException(String message)
	{
		super(message);
	}
}