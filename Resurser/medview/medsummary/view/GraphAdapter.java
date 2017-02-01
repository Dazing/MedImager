/*
 * @(#)GraphAdapter.java
 *
 * $Id: GraphAdapter.java,v 1.1 2006/11/15 22:34:55 oloft Exp $
 *
 * --------------------------------
 * Original author: Olof Torgersson
 * --------------------------------
 */

package medview.medsummary.view;

import java.awt.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;

public abstract interface GraphAdapter
{
	
	/**
	 * Initiates the graph using the specified
	 * examination identifier. The actual feeder
	 * specifics are dealt with in the various
	 * implementation classes.
	 * @param identifier the examination identifier
	 * that the graph  is for.
	 * @param comp the component over which the
	 * graph should appear (usually centered).
	 */
	public abstract void initiateGraph( ExaminationIdentifier id, Component comp );


}
