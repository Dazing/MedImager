/*
 * @(#)SummaryCreatorActions.java
 *
 * $Id: SummaryCreatorActions.java,v 1.8 2003/06/04 20:33:14 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

public interface SummaryCreatorActions
{
	public static final String NEW_TRANSLATOR_ACTION = "newTranslator";

	public static final String LOAD_TRANSLATOR_ACTION = "loadTranslator";

	public static final String SAVE_TRANSLATOR_ACTION = "saveTranslator";

	public static final String SAVE_TRANSLATOR_AS_ACTION = "saveTranslatorAs";

	public static final String CLOSE_TRANSLATOR_ACTION = "closeTranslator";

	public static final String CLOSE_TEMPLATE_ACTION = "closeTemplate";

	public static final String SHOW_ABOUT_ACTION = "showAbout";

	public static final String SHOW_SETTINGS_ACTION = "showSettings";

	public static final String EXIT_SUMMARYCREATOR_ACTION = "exitSummaryCreator";

	public static final String ADD_SECTION_ACTION = "addSection";

	public static final String REMOVE_SECTION_ACTION = "removeSection";

	public static final String RENAME_SECTION_ACTION = "renameSection";

	public static final String NEW_TEMPLATE_ACTION = "newTemplate";

	public static final String LOAD_TEMPLATE_ACTION = "loadTemplate";

	public static final String SAVE_TEMPLATE_ACTION = "saveTemplate";

	public static final String SAVE_TEMPLATE_AS_ACTION = "saveTemplateAs";

	public static final String PREVIEW_JOURNAL_ACTION = "previewJournal";

	public static final String ASSOCIATE_TRANSLATOR_ACTION = "associateTranslator";

	public static final String SET_PREVIEW_PCODE_ACTION = "setPreviewPCode";
}
