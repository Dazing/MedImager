/*
 * $Id: MedSummaryConstants.java,v 1.22 2008/11/27 18:36:57 it2aran Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.model;

import misc.gui.print.*;

public interface MedSummaryConstants
{
	// FASS URL

	public static final String DEFAULT_FASS_URL = "http://www.fass.se/LIF/home/index.jsp?UserTypeID=0";


	// DATA HANDLERS

	/**
	 * The class name of the remote examination datahandler to use when the
	 * user wishes to use remote datahandling. This is only used if the user
	 * explicitly has activated this type of datahandling, otherwise the
	 * property will never be used.
	 */
	public static final String REMOTE_EXAMINATION_DATAHANDLER_CLASS_NAME = "medview.datahandling.examination.RemoteExaminationDataHandlerClient";

	/**
	 * The class name of the remote term datahandler to use when the user
	 * wishes to use remote datahandling. This is only used if the user
	 * explicitly has activated this type of datahandling, otherwise the
	 * property will never be used.
	 */
	public static final String REMOTE_TERM_DATAHANDLER_CLASS_NAME = "medview.datahandling.RemoteTermDataHandlerClient";

	public static final String REMOTE_PCODE_GENERATOR_CLASS_NAME = "medview.datahandling.RemotePCodeGeneratorClient";


	// GUI

	public static final int DEFAULT_HEIGHT_OF_INSERTED_IMAGES = 90;

	public static final int DEFAULT_EMPTY_SPACE_AROUND_INSERTED_IMAGES_HORIZONTAL = 5;

	public static final int DEFAULT_EMPTY_SPACE_AROUND_INSERTED_IMAGES_VERTICAL = 5;

	public static final int DEFAULT_BORDER_TYPE_AROUND_INSERTED_IMAGES = PageRenderer.IMAGE_BORDER_SHADOW;

	public static final int DEFAULT_FULL_IMAGE_MAX_SIZE = 768;

	// DATA COMPONENT PACKAGES

	public static final String DATA_COMPONENT_PACKAGE_PREFIX = "dataComponentPackage";

	/**
	 * When listening for changes to the included data component
	 * packages, you should not listen to the individual package
	 * preferences, but instead listen to this flag. When you
	 * receive that this flag's preference has been changed, you
	 * know that the included package information kept in the
	 * preferences are correct and will not be further updated.
	 */
	public static final String INCLUDED_DATA_COMPONENT_PACKAGE_SWITCH_FLAG = "medSummaryIncludedDataComponentPackageSwitchFlag";


	// VERSION

	/**
	 * The central spot for placing information about the
	 * current version of the MedSummary software. All
	 * objects in some way displaying the current version
	 * should use this constant.
	 */
	public static final String VERSION_STRING = "4.3.1";

}
