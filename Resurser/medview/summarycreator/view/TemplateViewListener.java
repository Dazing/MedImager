/*
 * @(#)TemplateViewListener.java
 *
 * $Id: TemplateViewListener.java,v 1.3 2002/10/12 14:11:06 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import java.util.*;

public interface TemplateViewListener extends EventListener
{
	public abstract void viewSwitched( TemplateViewEvent e );
}
