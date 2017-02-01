/*
 * @(#)MedViewPreferenceListener.java
 *
 * $Id: MedViewPreferenceListener.java,v 1.1 2003/08/19 16:03:14 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

import java.util.*;

public interface MedViewPreferenceListener extends EventListener
{
	public abstract void userPreferenceChanged(MedViewPreferenceEvent e);

	public abstract void systemPreferenceChanged(MedViewPreferenceEvent e);
}