/*
 * @(#)TreeModel.java
 *
 * $Id: TreeModel.java,v 1.15 2008/07/29 09:31:58 it2aran Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.model;

import java.io.*;
import java.util.*;

import javax.swing.event.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;
import medview.medsummary.model.exceptions.CouldNotAddPatientException;

import misc.foundation.*;

/**
 */
public class TreeModel implements MedViewLanguageConstants
{

	/**
	 * Same as the other add patient method, besides that
	 * this method does not deliver any notifications to
	 * a notifiable. Will not do anything if the patient is
	 * already in the tree.
	 */
	public synchronized void addPatient( PatientIdentifier pid ) throws CouldNotAddPatientException {
		this.addPatient(pid, null);
	}

	/**
	 * Adds a new patient model representing a patient
	 * with the specified patient identifier to the tree.
	 * If the tree already contains a patient model with
	 * that identifier, nothing happens. The method will
	 * also query the data handling layer for all the
	 * examinations associated with the added patient, and
	 * add all such examinations as models to the added
	 * patient model. Notification of progress will be
	 * delivered to the specified notifiable. Will not do
	 * anything if the patient is already in the tree.
	 */
	public synchronized void addPatient( PatientIdentifier pid, ProgressNotifiable not ) throws
		CouldNotAddPatientException
	{
		if (contains(pid))
		{
			return; // the patient is already in the tree - dont add
		}
		else
		{
			try
			{
				ExaminationIdentifier[] eD = mVDH.getExaminations(pid); // -> IOException

				PatientModel pM = new PatientModel(pid, eD, not);

				this.models.add(pM);

				firePatientAdded(pM);
			}
			catch (IOException e)
			{
				e.printStackTrace();

				throw new CouldNotAddPatientException(e.getMessage());
			}
		}
	}

	/**
	 * If the tree contains a patient model representing
	 * a patient with the specified patient identifier,
	 * this method removes that patient model from the
	 * tree. Otherwise, nothing will happen.
	 */
	public synchronized void removePatient( PatientIdentifier pid)
	{
		if (!contains(pid))
		{
			return;
		}
		else
		{
			PatientModel pM = getModel(pid);

			this.models.remove(pM);

			firePatientRemoved(pM);
		}
	}

	/**
	 * Clears the tree from all patient models.
	 */
	public synchronized void clearPatients()
	{
		models.clear();

		firePatientsCleared();
	}


	/**
	 * Adds a model representing an examination to the
	 * patient model representing the patient with the
	 * specified patient identifier. If no such patient
	 * model exists in the tree, nothing happens. If
	 * the examination is already in the patients node,
	 * nothing will happen.
	 */
	public synchronized void addExaminationToPatient(ExaminationIdentifier examinationID, PatientIdentifier pid)
	{
		PatientModel model = getModel(pid);

		if ((model != null) && (!model.containsModelForExamination(examinationID)))
		{
			ExaminationModel eModel = model.addExamination(examinationID);

			fireExaminationAdded(eModel);
		}
	}

	public synchronized void updateExamination(ExaminationIdentifier examinationID, PatientIdentifier pid)
	{
		PatientModel model = getModel(pid);

		if ((model != null) && (model.containsModelForExamination(examinationID)))
		{
			model.updateExamination(examinationID);

			fireExaminationUpdated(model, model.getModel(examinationID));
		}
	}

	/**
	 * If the tree contains a patient model representing
	 * a patient with the specified patient identifier,
	 * this patient model is returned. Otherwise, a null
	 * value is returned.
	 */
	public PatientModel getModel(PatientIdentifier pid)
	{
		Iterator iter = models.iterator();

		while (iter.hasNext())
		{
			PatientModel curr = (PatientModel) iter.next();

			if (curr.getPID().equals(pid))
			{
				return curr;
			}
		}

		return null;
	}

	/**
	 * Returns an array of all patient models that
	 * are currently in the tree. May return an empty
	 * array.
	 */
	public PatientModel[] getModels()
	{
		PatientModel[] ret = new PatientModel[models.size()];

		models.toArray(ret);

		return ret;
	}

	/**
	 * Checks whether the tree contains a patient
	 * model representing a patient with the
	 * specified patient identifier.
	 */
	public boolean contains(PatientIdentifier pid)
	{
		Iterator iter = models.iterator();

		while (iter.hasNext())
		{
			PatientModel curr = (PatientModel) iter.next();

			if (curr.getPID().equals(pid))
			{
				return true;
			}
		}

		return false;
	}



// -------------------------------------------------------------------------
// ***************************** EVENT METHODS *****************************
// -------------------------------------------------------------------------

	public void addTreeModelListener( TreeModelListener listener )
	{
		listenerList.add(TreeModelListener.class, listener);
	}

	public void removeTreeModelListener( TreeModelListener listener )
	{
		listenerList.remove(TreeModelListener.class, listener);
	}

	private void firePatientAdded( PatientModel model )
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TreeModelListener.class)
			{
				TreeModelEvent event = new TreeModelEvent(this, model, null);

				((TreeModelListener)listeners[i+1]).patientAdded(event);
			}
		}
	}

	private void firePatientRemoved( PatientModel model )
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TreeModelListener.class)
			{
				TreeModelEvent event = new TreeModelEvent(this, model, null);

				((TreeModelListener)listeners[i+1]).patientRemoved(event);
			}
		}
	}

	private void firePatientsCleared( )
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TreeModelListener.class)
			{
				TreeModelEvent event = new TreeModelEvent(this);

				((TreeModelListener)listeners[i+1]).patientsCleared(event);
			}
		}
	}

	private void fireExaminationAdded( ExaminationModel model )
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TreeModelListener.class)
			{
				TreeModelEvent event = new TreeModelEvent(this, null, model);

				((TreeModelListener)listeners[i+1]).examinationAdded(event);
			}
		}
	}

	private void fireExaminationUpdated( PatientModel pModel, ExaminationModel model )
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TreeModelListener.class)
			{
				TreeModelEvent event = new TreeModelEvent(this, pModel, model);

				((TreeModelListener)listeners[i+1]).examinationUpdated(event);
			}
		}
	}

// -------------------------------------------------------------------------
// *************************************************************************
// -------------------------------------------------------------------------





	public TreeModel() { }

	private TreeSet models = new TreeSet();

	private MedViewDataHandler mVDH = MedViewDataHandler.instance();

	private EventListenerList listenerList = new EventListenerList();

}
