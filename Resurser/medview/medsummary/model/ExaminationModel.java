/*
 * @(#)ExaminationModel.java
 *
 * $Id: ExaminationModel.java,v 1.12 2005/01/30 15:30:55 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.model;

import java.io.*;

import java.util.*;

import java.text.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;
import medview.datahandling.images.*;

import misc.foundation.*;

public class ExaminationModel implements Comparable, MedViewLanguageConstants
{
	
	/**
	 * Updates the model so it represents what
	 * is contained in the data layer.
	 */
	public void update()
	{
		refreshImages();
	}

	/**
	 * Returns the parent patient model
	 * containing this examination model.
	 */
	public PatientModel getPatientModel()
	{
		return patientModel;
	}

	/**
	 * Obtains an array of all image models
	 * kept for this examination model.
	 */
	public ExaminationImageModel[] getImageModels()
	{
		int size = imageModels.size();

		ExaminationImageModel[] ret = new ExaminationImageModel[size];

		imageModels.toArray(ret);

		return ret;
	}

	/**
	 * Adds the specified image to this
	 * examination model.
	 */
	public synchronized void addImage(ExaminationImage image)
	{
		imageModels.add(new ExaminationImageModel(image, this));
	}

	/**
	 * Compares this examination to the
	 * specified examination using the
	 * Java framework for comparisons.
	 */
	public int compareTo(Object model)
	{
		if (!(model instanceof ExaminationModel))
		{
			return 0;
		}
		else
		{
			Date thisDate = this.examinationID.getTime();

			Date otherDate = ((ExaminationModel)model).getDate();

			return (thisDate.compareTo(otherDate));
		}
	}
	
	/**
	 * Returns the corresponding examination identifier.
	 * @return ExaminationIdentifier
	 */
	public ExaminationIdentifier getExaminationIdentifier()
	{
		return this.examinationID;
	}

	/**
	 * Returns the examination date.
	 */
	public Date getDate()
	{
		return examinationID.getTime();
	}

	/**
	 * Returns the examination date as
	 * a formatted date string.
	 */
	public String getDateString()
	{
		int med = DateFormat.MEDIUM;

		int shr = DateFormat.SHORT;

		DateFormat dF = DateFormat.getDateTimeInstance(med, shr);

		return dF.format(examinationID.getTime());
	}


	// UTILITY METHODS
	
	private void refreshImages()
	{
		try
		{			
			imageModels.clear();
			
			ExaminationImage[] images = mVDH.getImages(examinationID);

			for (int ctr=0; ctr<images.length; ctr++)
			{
				imageModels.add(new ExaminationImageModel(images[ctr], this));
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}	
	}


	public ExaminationModel(ExaminationIdentifier id, PatientModel patient)
	{
		this(id, patient, null);
	}

	public ExaminationModel(ExaminationIdentifier id, PatientModel patient, ProgressNotifiable not)
	{
		this.examinationID = id;

		this.patientModel = patient;
		
		// create and add examination image models
		
		refreshImages();
	}

	private MedViewDataHandler mVDH = MedViewDataHandler.instance();

	private HashSet imageModels = new HashSet();

	private ExaminationIdentifier examinationID;

	private PatientModel patientModel;

}
