/*
 * @(#)TreeModelListener.java
 *
 * $Id: TreeModelListener.java,v 1.7 2004/11/19 12:34:00 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.model;

import java.util.*;

public interface TreeModelListener extends EventListener
{
	public void patientAdded( TreeModelEvent e );

	public void patientRemoved( TreeModelEvent e );

	public void patientsCleared( TreeModelEvent e );

	public void examinationAdded( TreeModelEvent e);

	public void examinationRemoved( TreeModelEvent e);
	
	public void examinationUpdated( TreeModelEvent e);
}
