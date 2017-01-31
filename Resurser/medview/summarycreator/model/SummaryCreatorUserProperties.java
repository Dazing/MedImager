/*
 * @(#)SummaryCreatorUserProperties.java
 *
 * $Id: SummaryCreatorUserProperties.java,v 1.6 2004/02/28 17:51:27 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.model;

public interface SummaryCreatorUserProperties
{
	public static final String SERVER_LOCATION_PROPERTY = "serverLocation";

	public static final String LOCAL_TERM_DEFINITION_LOCATION_PROPERTY = "localTermDefinitionLocation";

	public static final String LOCAL_TERM_VALUE_LOCATION_PROPERTY = "localTermValueLocation";

	public static final String LANGUAGE_PROPERTY = "languageProperty";

	public static final String LOOK_AND_FEEL_PROPERTY = "lookAndFeelClassName";

	public static final String PREVIEW_PCODE_PROPERTY = "previewPCode";

	public static final String LAST_TEMPLATE_VIEW_PROPERTY = "lastTemplateView";


	// visual component states

	public static final String LAST_TERM_TRANS_SPLIT_DIV_LOC_PROPERTY = "lastTermTransSplitDivLoc";

	public static final String LAST_TEMP_TERM_SPLIT_DIV_LOC_PROPERTY = "lastTempTermSplitDivLoc";

	public static final String DIVIDER_LOCATION_PROPERTY = "dividerLocation";

	public static final String SUMMARYCREATOR_SIZE_PROPERTY = "summaryCreatorSize";

	public static final String SUMMARYCREATOR_LOCATION_PROPERTY = "summaryCreatorLocation";
}
