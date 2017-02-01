/*
 * @(#)MedSummaryModelListener.java
 *
 * $Id: MedSummaryModelListener.java,v 1.10 2005/01/30 15:30:55 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.model;

import java.util.*;

/**
 */
public interface MedSummaryModelListener extends EventListener
{
	/**
	 * @param e
	 */
	public void dataLocationChanged( MedSummaryModelEvent e );

	/**
	 * @param e
	 */
	public void dataLocationIDChanged( MedSummaryModelEvent e );

	/**
	 * @param e
	 */
	public void patientsChanged( MedSummaryModelEvent e );

	/**
	 * 
	 * @param e
	 */
	public void examinationAdded( MedSummaryModelEvent e );

	/**
	 * @param e
	 */
	public void examinationUpdated( MedSummaryModelEvent e );

	/**
	 * @param e
	 */
	public void sectionsChanged( MedSummaryModelEvent e );

	/**
	 * @param e
	 */
	public void templateChanged( MedSummaryModelEvent e );

	/**
	 * @param e
	 */
	public void templateIDChanged( MedSummaryModelEvent e );

	/**
	 * @param e
	 */
	public void translatorChanged( MedSummaryModelEvent e );

	/**
	 * @param e
	 */
	public void translatorIDChanged( MedSummaryModelEvent e );

	/**
	 * @param e
	 */
	public void documentReplaced( MedSummaryModelEvent e );
	
	/**
	 * @param e
	 */
	public void includedPackagesUpdated( MedSummaryModelEvent e );
	
	/**
	 * @param e MedSummaryModelEvent
	 */
	public void currentPackageChanged( MedSummaryModelEvent e );


}
