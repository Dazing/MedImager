/*
 * @(#)MedSummaryUserProperties.java
 *
 * $Id: MedSummaryUserProperties.java,v 1.11 2006/05/29 18:32:49 limpan Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.model;

/**
 * The 'model' package is actually a combination of the 'model' part of the
 * MVC design pattern (the model for displaying the medsummary application)
 * and the 'domain' part of the Layers design pattern (the domain of using
 * the medsummary application). Therefore the flag and user properties for
 * the medsummary application should be placed in the model package. The act
 * of 'storing a property' is done by a technical service, currently located
 * in the medview.datahandling package, this package can for the moment be
 * seen as the 'technical services' layer of the Layers design pattern.
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public interface MedSummaryUserProperties
{

	public static final String LANGUAGE_PROPERTY = "Language";

	public static final String LOOK_AND_FEEL_PROPERTY = "LookAndFeel";


	public static final String SERVER_LOCATION_PROPERTY = "ServerLocation";

	public static final String LOCAL_TERM_DEFINITION_LOCATION_PROPERTY = "LocalTermDefinitionLocation";

	public static final String LOCAL_TERM_VALUE_LOCATION_PROPERTY = "LocalTermValueLocation";

	public static final String LOCAL_EXAMINATION_DATA_LOCATION_PROPERTY = "LocalExaminationDataLocation";

	public static final String LOCAL_PDA_DATA_LOCATION_PROPERTY = "LocalPDADataLocation";
	
	public static final String LOCAL_PCODE_GENERATOR_NUMBER_GENERATOR_LOCATION_PROPERTY = "LocalPCodeGeneratorNumberGeneratorLocation";


	public static final String LAST_CHOSEN_SECTION_PROPERTY = "LastChosenSection";

	public static final String LAST_GRAPHIC_TEMPLATE_USED_PROPERTY = "LastGraphicTemplateUsed";

	public static final String LAST_DATA_COMPONENT_PACKAGE_PROPERTY = "LastDataComponentPackage";

	public static final String MAXIMUM_HEIGHT_OF_INSERTED_IMAGES_PROPERTY = "medsummary.model.MaximumHeightOfInsertedImages";

	public static final String PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_VERTICAL_PROPERTY = "medsummary.model.PadEmptySpaceAroundInsertedImagesVertical";

	public static final String PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_HORIZONTAL_PROPERTY = "medsummary.model.PadEmptySpaceAroundInsertedImagesHorizontal";

	public static final String BORDER_TYPE_AROUND_INSERTED_IMAGES_PROPERTY = "medsummary.model.BorderTypeAroundInsertedImages";

	public static final String FASS_URL_PROPERTY = "FassURL";


	// visual component states

	public static final String DIVIDER_LOCATION_PROPERTY = "dividerLocation";

	public static final String MEDSUMMARY_SIZE_PROPERTY = "medsummarySize";

	public static final String MEDSUMMARY_LOCATION_PROPERTY = "medsummaryLocation";

}
