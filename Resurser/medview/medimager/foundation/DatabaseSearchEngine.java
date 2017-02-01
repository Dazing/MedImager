/**
 * @(#) DatabaseSearchEngine.java
 */

package medview.medimager.foundation;

import medview.datahandling.*;

import misc.foundation.*;
import misc.foundation.text.*;

/**
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
public interface DatabaseSearchEngine
{
	/**
	 * Performs a search against the currently set database
	 * location, and returns an array of result objects.
	 * @param searchText String
	 * @return DatabaseImageSearchResult[]
	 */
	DatabaseImageSearchResult[] search( String searchText, ProgressNotifiable not )
		throws CouldNotParseException, CouldNotSearchException;
}
