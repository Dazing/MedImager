package medview.datahandling;

import misc.foundation.*;
import misc.foundation.text.*;

/**
 * Defines a simple search engine interface that is used by
 * the data handling layer for searching in the currently
 * set data location. Implementations parse the search string,
 * followed by performing the actual search.
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
public interface ExaminationDataSearchEngine
{
	/**
	 * Performs a search in the current examination data location. The
	 * search string is parsed, if for some reason the search engine
	 * implementation cannot parse the search string, an exception is
	 * thrown.
	 * @param searchString String
	 * @param not ProgressNotifiable
	 * @return ExaminationDataSearchResult[]
	 * @throws CouldNotParseException
	 * @throws CouldNotSearchException
	 */
	ExaminationDataSearchResult[] searchExaminationData(String searchString, ProgressNotifiable not)
		throws CouldNotParseException, CouldNotSearchException;

	/**
	 * Implementations may use a caching-technique to speed up subsequent
	 * searches. If this method is called, eventual cached information is
	 * to be cleared from the implementation search cache. Typically called
	 * when the location the search is performed on changes.
	 */
	void clearCache();
}
