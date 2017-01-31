package medview.datahandling;

import java.io.*;

import medview.datahandling.examination.*;
import medview.datahandling.images.*;

/**
 * An object containing a search result as obtained from the datahandling
 * search methods. This implementation stores in memory the examination
 * identifier as well as the examination value container, but retrieves
 * the image on demand since otherwise it would require enormous amounts
 * of memory.
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class DefaultExaminationDataSearchResult implements ExaminationDataSearchResult
{
	private ExaminationValueContainer evc;

	/**
	 * Construct a search result.
	 * @param eid ExaminationIdentifier
	 * @param evc ExaminationValueContainer
	 */
	public DefaultExaminationDataSearchResult(ExaminationValueContainer evc)
	{
		this.evc = evc;
	}

	/**
	 * Obtain the examination identifier associated with the search
	 * result.
	 *
	 * @return ExaminationIdentifier
	 * @todo Implement this
	 *   medview.datahandling.ExaminationDataSearchResult method
	 */
	public ExaminationIdentifier getExaminationIdentifier()
	{
		return evc.getExaminationIdentifier();
	}

	/**
	 * Obtain the examination value container associated with the
	 * search result. Note that this implementation does not throw
	 * the exceptions specified in the interface. For other implementations,
	 * the obtainal of the evc might occur on-demand and thus can cause
	 * errors to occur.
	 *
	 * @return ExaminationValueContainer
	 * @todo Implement this
	 *   medview.datahandling.ExaminationDataSearchResult method
	 */
	public ExaminationValueContainer getExaminationValueContainer() throws IOException, NoSuchExaminationException
	{
		return evc;
	}

	/**
	 * Obtains an array of all the images associated with the
	 * search result.
	 *
	 * @return ExaminationImage[]
	 */
	public ExaminationImage[] getExaminationImages() throws IOException
	{
		return MedViewDataHandler.instance().getImages(getExaminationIdentifier()); // -> IOException
	}
}
