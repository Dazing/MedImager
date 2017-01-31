/*
 * @(#)SummaryCreatorFlagProperties.java
 *
 * $Id: SummaryCreatorFlagProperties.java,v 1.6 2004/02/28 17:51:27 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.model;

public interface SummaryCreatorFlagProperties
{
	public static final String USE_REMOTE_DATAHANDLING_PROPERTY = "useRemoteDataHandling";

	public static final String ASSOCIATE_TRANSLATOR_WITH_TEMPLATE_PROPERTY = "associateTranslatorWithTemplate";

	public static final String LOAD_ASSOCIATED_TRANSLATOR_WITH_TEMPLATE_PROPERTY = "loadAssociatedTranslatorWithTemplate";

	public static final String ASK_BEFORE_REMOVE_SECTION_PROPERTY = "askBeforeRemoveSection";


	// visual states

	public static final String SUMMARYCREATOR_MAXIMIZED_PROPERTY = "summaryCreatorMaximized";
}
