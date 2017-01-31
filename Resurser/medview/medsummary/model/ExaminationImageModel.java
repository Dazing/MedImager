/*
 * @(#)ExaminationImageModel.java
 *
 * $Id: ExaminationImageModel.java,v 1.12 2008/07/29 09:31:58 it2aran Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.model;

import java.awt.image.*;

import java.io.*;

import javax.swing.*;

import medview.datahandling.*;
import medview.datahandling.images.*;
import medview.medsummary.model.exceptions.CouldNotRetrieveImageException;

/**
 * Model for an examination image to be presented in the MedSummary
 * application domain.
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Main Project: The MedView Project @ Chalmers University of Technology</p>
 *
 * <p>Sub Project: none</p>
 *
 * <p>Project Web http://www.cs.chalmers.se/proj/medview/website/medview/</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class ExaminationImageModel
{

	// MEMBERS

	private ExaminationImage examinationImage;

	private ExaminationModel examinationModel;

	public static final int ICON_WIDTH = ExaminationImage.THUMBNAIL_WIDTH;

	public static final int ICON_HEIGHT = ExaminationImage.THUMBNAIL_HEIGHT;

	// CONSTRUCTOR(S)

	public ExaminationImageModel( ExaminationImage image, ExaminationModel parent )
	{
		this.examinationImage = image;

		this.examinationModel = parent;
	}


	// IMAGE OBTAINING

	/**
	 * Obtains the full image.
	 * @return BufferedImage
	 * @throws medview.medsummary.model.exceptions.CouldNotRetrieveImageException
     */
	public BufferedImage getFullImage() throws CouldNotRetrieveImageException {
		try
		{
			return examinationImage.getFullImage(); // -> IOException
		}
		catch (IOException exc)
		{
			throw new CouldNotRetrieveImageException(exc);
		}
	}

	/**
	 * Will always return an image icon, even if the image
	 * could not be obtained (in this case, an empty image
	 * icon is returned).
	 * @return ImageIcon
	 */
	public ImageIcon getImageIcon()
	{
		try
		{
			return new ImageIcon(examinationImage.getThumbnail()); // -> IOException
		}
		catch (IOException exc)
		{
			System.err.println("Could not obtain thumbnail for image '" + examinationImage.getName() + "'");

			return new ImageIcon(MedViewDataHandler.instance().getImage(

				MedViewMediaConstants.NO_THUMBNAIL_IMAGE_ICON));
		}
	}


	// IMAGE DATA MODEL AND MODEL OBTAINING

	/**
	 * Returns the underlying data model for the image.
	 * @return ExaminationImage
	 */
	public ExaminationImage getExaminationImage()
	{
		return examinationImage;
	}

	/**
	 * Returns the model for the corresponding examination.
	 * @return ExaminationModel
	 */
	public ExaminationModel getExaminationModel()
	{
		return examinationModel;
	}


	// MISC METHODS

	/**
	 * Returns a string version of the image.
	 * @return String
	 */
	public String toString()
	{
		return (examinationImage.getName() + "\r");
	}

}
