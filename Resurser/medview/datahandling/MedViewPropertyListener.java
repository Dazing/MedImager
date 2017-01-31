/*
 * @(#)MedViewPropertyListener.java
 *
 * $Id: MedViewPropertyListener.java,v 1.4 2002/10/12 14:10:59 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

import java.util.*;

public interface MedViewPropertyListener extends EventListener
{
	public abstract void userPropertyChanged(MedViewPropertyEvent e);

	public abstract void flagPropertyChanged(MedViewPropertyEvent e);
}