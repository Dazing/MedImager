package medview.datahandling;

import java.io.*;

import medview.datahandling.examination.*;
import medview.datahandling.images.*;

/**
 * Wraps a result from a search in the examination data.
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public interface ExaminationDataSearchResult
{
	/**
	 * Obtain the examination identifier associated with
	 * the search result.
	 *
	 * @return ExaminationIdentifier
	 */
	ExaminationIdentifier getExaminationIdentifier();

	/**
	 * Obtain the examination value container associated
	 * with the search result. This is considered a utility
	 * method, since the value container could just as
	 * easily be retrieved via the central datahandler.
	 *
	 * @return ExaminationValueContainer
	 */
	ExaminationValueContainer getExaminationValueContainer()  throws IOException, NoSuchExaminationException;

	/**
	 * Obtain an array of all the images associated with the
	 * search result. This is considered a utility method,
	 * since the image could just as easily be retrieved via
	 * the central datahandler.
	 *
	 * @return ExaminationImage[]
	 */
	ExaminationImage[] getExaminationImages() throws IOException;
}
