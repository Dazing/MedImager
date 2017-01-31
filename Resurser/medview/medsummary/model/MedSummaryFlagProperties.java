/*
 * @(#)MedSummaryFlagProperties.java
 *
 * $Id: MedSummaryFlagProperties.java,v 1.8 2004/02/26 18:47:32 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.model;

/**
 * The 'model' package is actually a
 * combination of the 'model' part of the
 * MVC design pattern (the model for displaying
 * the medsummary application) and the 'domain'
 * part of the Layers design pattern (the domain
 * of using the medsummary application). Therefore
 * the flag and user properties for the medsummary
 * application should be placed in the model package.
 * The act of 'storing a property' is done by a
 * technical service, currently located in the
 * medview.datahandling package, this package can for
 * the moment be seen as the 'technical services' layer
 * of the Layers design pattern.
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public interface MedSummaryFlagProperties
{
	public static final String USE_REMOTE_DATAHANDLING_PROPERTY = "useRemoteDatahandling";

	public static final String USE_LAST_SETTINGS_AT_INIT_PROPERTY = "useLastSettingsAtInit";

	public static final String DISPLAY_DATA_LOCATION_COMBO_IN_TOOLBAR_PROPERTY = "displayDataLocationComboInToolbar";

	public static final String DISPLAY_TEMPLATE_COMBO_IN_TOOLBAR_PROPERTY = "displayTemplateComboInToolbar";

	public static final String DISPLAY_TRANSLATOR_COMBO_IN_TOOLBAR_PROPERTY = "displayTranslatorComboInToolbar";

	public static final String DISPLAY_SECTIONS_COMBO_IN_TOOLBAR_PROPERTY = "displaySectionsComboInToolbar";

	public static final String DISPLAY_GRAPHIC_TEMPLATE_COMBO_IN_TOOLBAR_PROPERTY = "displayGraphicTemplateComboInToolbar";

	public static final String PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_PROPERTY = "padEmptySpaceAroundInsertedImages";

	public static final String USE_BORDER_AROUND_INSERTED_IMAGES_PROPERTY = "useBorderAroundInsertedImages";


	// visual states

	public static final String MEDSUMMARY_MAXIMIZED_PROPERTY = "medsummaryMaximized";
}
