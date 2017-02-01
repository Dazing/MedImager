/*
 * @(#)MedSummaryModelAdapter.java
 *
 * $Id: MedSummaryModelAdapter.java,v 1.10 2005/01/30 15:30:55 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.model;

public class MedSummaryModelAdapter implements MedSummaryModelListener
{
	public void dataLocationChanged(MedSummaryModelEvent e) {}

	public void dataLocationIDChanged(MedSummaryModelEvent e) {}

	public void patientsChanged(MedSummaryModelEvent e) {}

	public void sectionsChanged(MedSummaryModelEvent e) {}

	public void templateChanged(MedSummaryModelEvent e) {}

	public void templateIDChanged(MedSummaryModelEvent e) {}

	public void translatorChanged(MedSummaryModelEvent e) {}

	public void translatorIDChanged(MedSummaryModelEvent e) {}

	public void documentReplaced(MedSummaryModelEvent e) {}

	public void examinationAdded(MedSummaryModelEvent e) {}

	public void examinationUpdated(MedSummaryModelEvent e) {}

	public void includedPackagesUpdated(MedSummaryModelEvent e) {}

	public void currentPackageChanged(MedSummaryModelEvent e) {}
}
