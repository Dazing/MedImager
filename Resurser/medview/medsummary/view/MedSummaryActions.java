/*
 * @(#)MedSummaryActions.java
 *
 * $Id: MedSummaryActions.java,v 1.11 2006/11/15 22:34:55 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.view;

public interface MedSummaryActions
{	
	public static final String ADD_PATIENT_ACTION = "AddPatientAction";
	
	public static final String CHANGE_DATA_LOCATION_ACTION = "ChangeDataLocationAction";
	
	public static final String CLEAR_PATIENTS_ACTION = "ClearPatientsAction";
	
	public static final String CLOSE_GENERATED_PAGE_ACTION = "CloseGeneratedPageAction";
	
	public static final String EXIT_MEDSUMMARY_ACTION = "ExitMedSummaryAction";
	
	public static final String GENERATE_SUMMARY_ACTION = "GenerateSummaryAction";

	public static final String INVOKE_FASS_ACTION = "InvokeFassAction";

	public static final String NEW_DAYNOTE_ACTION = "NewDayNoteAction";

	public static final String NEW_PATIENT_ACTION = "NewPatientAction";

	public static final String PDA_EXPORT_ACTION = "PDAExportAction";
	
	public static final String PRINT_JOURNAL_ACTION = "PrintJournalAction";

	public static final String REFRESH_PATIENTS_ACTION = "RefreshPatientsAction";

	public static final String REMOVE_PATIENT_ACTION = "RemovePatientAction";
	
	public static final String SAVE_AS_HTML_ACTION = "SaveAsHTMLAction";
	
	public static final String SAVE_AS_RTF_ACTION = "SaveAsRTFAction";

	public static final String SHOW_ABOUT_ACTION = "ShowAboutAction";
	
	public static final String SHOW_GRAPH_ACTION = "ShowGraphAction";

	public static final String SHOW_SETTINGS_ACTION = "ShowSettingsAction";

	public static final String VIEW_EXAMINATION_IMAGE_ACTION = "ViewExaminationImageAction";
	
}
