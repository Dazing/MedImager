/*
 * @(#)MedViewDataAdapter.java
 *
 * $Id: MedViewDataAdapter.java,v 1.11 2006/04/24 14:17:05 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

/**
 * A simple adapter class that enables subclasses to
 * override only the interesting methods.
 *
 * @author Fredrik Lindahl
 */
public class MedViewDataAdapter implements MedViewDataListener
{

	public void termDataHandlerChanged(MedViewDataEvent e) {}

	public void examinationDataHandlerChanged(MedViewDataEvent e) {}

	public void templateAndTranslatorDataHandlerChanged(MedViewDataEvent e) {}


	public void examinationDataLocationChanged(MedViewDataEvent e)	{}

	public void examinationDataLocationIDChanged(MedViewDataEvent e) {}

	public void examinationAdded(MedViewDataEvent e) {}

	public void examinationUpdated(MedViewDataEvent e) {}

	public void examinationRemoved(MedViewDataEvent e) {}


	public void termLocationChanged(MedViewDataEvent e)	{}

	public void termAdded(MedViewDataEvent e) {}

	public void termRemoved(MedViewDataEvent e) {}

	public void valueAdded(MedViewDataEvent e) {}

	public void valueRemoved(MedViewDataEvent e) {}

	public void userIDChanged(MedViewDataEvent e) {}

	public void userNameChanged(MedViewDataEvent e) {}


}
