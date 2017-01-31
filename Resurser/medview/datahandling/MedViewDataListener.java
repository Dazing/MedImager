/*
 * @(#)MedViewDataListener.java
 *
 * $Id: MedViewDataListener.java,v 1.11 2006/04/24 14:17:05 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

import java.util.*;

public interface MedViewDataListener extends EventListener
{

	public abstract void termDataHandlerChanged(MedViewDataEvent e);

	public abstract void examinationDataHandlerChanged(MedViewDataEvent e);

	public abstract void templateAndTranslatorDataHandlerChanged(MedViewDataEvent e);


	public abstract void examinationDataLocationChanged(MedViewDataEvent e);

	public abstract void examinationDataLocationIDChanged(MedViewDataEvent e);

	public abstract void examinationAdded(MedViewDataEvent e);

	public abstract void examinationUpdated(MedViewDataEvent e);

	public abstract void examinationRemoved(MedViewDataEvent e);


	public abstract void termLocationChanged(MedViewDataEvent e);

	public abstract void termAdded(MedViewDataEvent e);

	public abstract void termRemoved(MedViewDataEvent e);

	public abstract void valueAdded(MedViewDataEvent e);

	public abstract void valueRemoved(MedViewDataEvent e);


	public abstract void userIDChanged(MedViewDataEvent e);

	public abstract void userNameChanged(MedViewDataEvent e);

}
