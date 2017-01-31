/*
 * @(#)SummaryCreatorConstants.java
 *
 * $Id: SummaryCreatorConstants.java,v 1.8 2008/01/31 13:23:26 it2aran Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.model;

public interface SummaryCreatorConstants
{

// ----------------------------------------------------------------------------------------------------
// ******************************************* VERSION INFO *******************************************
// ----------------------------------------------------------------------------------------------------

	/**
	 * The central spot for placing information about the
	 * current version of the MedSummary software. All
	 * objects in some way displaying the current version
	 * should use this constant.
	 */
	public static final String VERSION_STRING = "1.1.2 Beta 5";

// ----------------------------------------------------------------------------------------------------
// ****************************************************************************************************
// ----------------------------------------------------------------------------------------------------

	/**
	 * The class name of the remote term datahandler to use when the user
	 * wishes to use remote datahandling. This is only used if the user
	 * explicitly has activated this type of datahandling, otherwise the
	 * property will never be used.
	 */
	public static final String REMOTE_TERM_DATAHANDLER_CLASS_NAME = "medview.datahandling.RemoteTermDataHandlerClient";

}
