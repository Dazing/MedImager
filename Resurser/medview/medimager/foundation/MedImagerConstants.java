/**
 * @(#) MedImagerConstants.java
 */

package medview.medimager.foundation;

import java.awt.*;

import java.util.*;

import javax.sound.sampled.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;

/**
 * Contains constants used in the application.
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public interface MedImagerConstants
{
	// PROPERTY CONSTANTS

	public static final String LAST_ALBUM_DIRECTORY_PROPERTY = "lastAlbumDirectory";

	public static final String LAST_MEDIMAGER_FRAME_DIMENSION_PROPERTY = "lastMedImagerFrameDimension";

	public static final String LAST_DATABASE_SEARCH_PANEL_DIVIDER_LOCATION_PROPERTY = "lastDatabaseSearchPanelDividerLocation";

	public static final String LAST_CHOSEN_BROWSE_PANEL_DIVIDER_LOCATION_PROPERTY = "lastChosenBrowsePanelDividerLocation";

	public static final String LAST_DATABASE_SEARCH_IMAGES_SIZE_SLIDER_VALUE_PROPERTY = "lastDatabaseSearchImagesSizeSliderValue";

	public static final String LOCAL_EXAMINATION_DATA_LOCATION_PROPERTY = "localExaminationDataLocation";

	public static final String LOCAL_GENERATION_TEMPLATE_LOCATION_PROPERTY = "localGenerationTemplateLocation";

	public static final String LOCAL_GENERATION_TERM_DEFINITION_LOCATION_PROPERTY = "localGenerationTermDefinitionLocation";

	public static final String LOCAL_GENERATION_TERM_VALUE_LOCATION_PROPERTY = "localGenerationTermValueLocation";

	public static final String LOCAL_GENERATION_TRANSLATOR_LOCATION_PROPERTY = "localGenerationTranslatorLocation";

	public static final String MAXIMUM_SEARCH_HITS_PROPERTY = "maximumSearchHits";

	public static final String REMOTE_SERVER_LOCATION_PROPERTY = "remoteServerLocation";

	public static final String USE_REMOTE_DATAHANDLING_PROPERTY = "useRemoteDatahandling";


	// IMAGE DIMENSION CONSTANTS

	public static final Dimension THUMB_IMAGE_DIMENSION = new Dimension(64,48);

	public static final Dimension MEDIUM_IMAGE_DIMENSION = new Dimension(320,240);


	// AUDIO CONSTANTS

	public static final AudioFormat AUDIO_FORMAT = new AudioFormat(8000.0F, 8, 1, true, false);


	// STRING CONSTANTS

	public static final String IMPORTED_IMAGE_FILE_STRING = "Importerad bildfil";

	public static final String MEDIMAGER_ALBUM_FILE_EXTENSION = ".mia";

	public static final String NOT_APPLICABLE_STRING = "Ej applicerbar";

	public static final String VERSION_STRING = "3.0rc4";

	public static final String UNKNOWN_STRING = "Okänd";


	// OBJECT CONSTANTS

	public static final PatientIdentifier NOT_APPLICABLE_PID = new PatientIdentifier(NOT_APPLICABLE_STRING, NOT_APPLICABLE_STRING);

	public static final ExaminationIdentifier NOT_APPLICABLE_EID = new MedViewExaminationIdentifier(NOT_APPLICABLE_PID, new Date(0));
}
