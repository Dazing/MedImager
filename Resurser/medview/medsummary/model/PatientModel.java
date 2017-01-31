/*
 * @(#)PatientModel.java
 *
 * $Id: PatientModel.java,v 1.13 2005/01/30 15:30:55 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.model;

import medview.datahandling.*;
import medview.datahandling.examination.*;

import misc.foundation.*;

import java.io.*;

import java.text.*;

import java.util.*;

public class PatientModel implements Comparable
{

	/**
	 * Compares this model to the specified
	 * model using the Java framework for
	 * comparison. If the patient ids are
	 * the same, they are considered equal.
	 * Otherwise, they are compared lexico-
	 * graphically.
	 */
	public int compareTo(Object obj)
	{
		if (obj instanceof PatientModel)
		{
			PatientModel other = (PatientModel) obj;

			return pid.compareTo(other.getPID());
		}
		else
		{
			return -1;
		}
	}

	/**
	 * Checks whether this patientmodel is
	 * equal to the specified using the
	 * Java framework for comparison. They
	 * are deemed equal if the patient ids
	 * are the same (regardless of case).
	 */
	public boolean equals(Object obj)
	{
		if (!(obj instanceof PatientModel))
		{
			return false;
		}
		else
		{
			PatientModel other = (PatientModel) obj;

			return other.getPID().equals(pid);
		}
	}

	/**
	 * Returns whether or not the patient model contains an 
	 * examination model corresponding to the specified id.
	 * @param examinationID ExaminationIdentifier
	 * @return boolean
	 */
	public boolean containsModelForExamination(ExaminationIdentifier examinationID)
	{
		return (getModel(examinationID) != null);
	}

	/**
	 * Obtains an array of all examination models
	 * kept in this patient model.
	 */
	public ExaminationModel[] getModels()
	{
		ExaminationModel[] ret = new ExaminationModel[models.size()];

		models.toArray(ret);

		return ret;
	}

	/**
	 * If this patient model keeps an examination
	 * model for the specified identifier, this
	 * method will return this examination model.
	 * Otherwise, a null value will be returned.
	 */
	public ExaminationModel getModel(ExaminationIdentifier examinationID)
	{
		Iterator iter = models.iterator();

		while (iter.hasNext())
		{
			ExaminationModel curr = (ExaminationModel) iter.next();

			if (curr.getDate().equals(examinationID.getTime()))
			{
				return curr; // CHANGE THIS IN FUTURE - COMPARISON BASED ON OBJECTS EQUALS() IMPLEMENTATION INSTEAD! // FL
			}
		}

		return null;
	}

	/**
	 * Adds a new examination model to represent
	 * the specified examination identifier to
	 * this patient model without any notification
	 * of progress.
	 */
	public synchronized ExaminationModel addExamination(ExaminationIdentifier examinationID)
	{
		return this.addExamination(examinationID, null);
	}

	/**
	 * Adds a new examination model to represent
	 * the specified examination identifier to
	 * this patient model with notification of
	 * progress. Will not add the examination if
	 * it already in the model. In both cases,
	 * the examination model representing the
	 * specified ID is returned from the method.
	 */
	public synchronized ExaminationModel addExamination(ExaminationIdentifier examinationID, ProgressNotifiable not)
	{
		if (!containsModelForExamination(examinationID))
		{
			ExaminationModel ret = new ExaminationModel(examinationID, this, not);
	
			models.add(ret);
	
			return ret;	
		}
		else
		{
			return getModel(examinationID);
		}
	}

	/**
	 * Removes the specified examination model from
	 * the kept examination model listing. Nothing
	 * happens if it is not in the listing.
	 */
	public synchronized void removeExamination(ExaminationModel model)
	{
		models.remove(model);
	}

	/**
	 * Tells the examination model that it should check
	 * with the data layer for updated information.
	 * @param id ExaminationIdentifier
	 */
	public synchronized void updateExamination(ExaminationIdentifier id)
	{
		ExaminationModel model = getModel(id);
		
		if (model != null)
		{
			model.update();
		}
	}

	/**
	 * Obtains the patient identifier for the
	 * represented patient.
	 */
	public PatientIdentifier getPID()
	{
		return pid;
	}



	public PatientModel(PatientIdentifier pid, ExaminationIdentifier[] e)
	{
		this(pid, e, null);
	}

	public PatientModel(PatientIdentifier pid, ExaminationIdentifier[] e, ProgressNotifiable n)
	{
		this.pid = pid;

		models = new TreeSet();

		for (int ctr=0; ctr<e.length; ctr++)
		{
			addExamination(e[ctr], n);
		}
	}

	private PatientIdentifier pid;

	private TreeSet models;

	private MedViewDataHandler mVDH = MedViewDataHandler.instance();

}
