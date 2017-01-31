/*
 * @(#)MedViewLanguageListener.java
 *
 * $Id: MedViewLanguageListener.java,v 1.4 2002/10/12 14:10:58 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

import java.util.*;

public interface MedViewLanguageListener extends EventListener
{
	public abstract void languageChanged(MedViewLanguageEvent e);
}