package medview.medimager.foundation;

import java.awt.image.*;

import java.io.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public interface DatabaseImageSearchResult
{
	/**
	 * Obtain the corresponding patient identifier.
	 *
	 * @return PatientIdentifier
	 */
	PatientIdentifier getPID();

	/**
	 * Obtain the corresponding examination identifier.
	 *
	 * @return ExaminationIdentifier
	 */
	ExaminationIdentifier getEID();

	/**
	 * Obtain a full image.
	 * @return BufferedImage
	 * @throws IOException
	 */
	BufferedImage getFullImage() throws IOException;

	/**
	 * Obtain a thumb version of the full image.
	 *
	 * @return BufferedImage
	 * @throws IOException
	 */
	BufferedImage getThumbImage() throws IOException;

	/**
	 * Obtains the image name. Might be the original
	 * file name of the image, or some other identifying
	 * string.
	 *
	 * @return String
	 */
	String getImageName();
}
