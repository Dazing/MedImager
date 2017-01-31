/*
 * @(#)MedViewLanguageConstants.java
 *
 * $Id: MedViewLanguageConstants.java,v 1.77 2010/06/28 07:12:39 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

public interface MedViewLanguageConstants
{

	/**************************************** ACTIONS ****************************************/

	public static final String ACTION_ABOUT_MEDSUMMARY_LS_PROPERTY = "AboutMedSummary";

	public static final String ACTION_ABOUT_SUMMARYCREATOR_LS_PROPERTY = "AboutSummaryCreator";

	public static final String ACTION_ABOUT_MEDRECORD_LS_PROPERTY = "AboutMedRecord";

	public static final String ACTION_ABOUT_MEDSERVER_LS_PROPERTY = "AboutMedServer";

	public static final String ACTION_ACTIVATE_LS_PROPERTY = "Activate";

	public static final String ACTION_ADD_NEW_VALUE_LS_PROPERTY = "AddNewValue";

	public static final String ACTION_ADD_PATIENT_LS_PROPERTY = "AddPatient";

	public static final String ACTION_ASSOCIATE_TRANSLATOR_LS_PROPERTY = "AssociateTranslator";

	public static final String ACTION_BOLD_LS_PROPERTY = "Bold";

	public static final String ACTION_CENTER_JUSTIFY_LS_PROPERTY = "CenterJustify";

	public static final String ACTION_CHANGE_DATA_LOCATION_LS_PROPERTY = "ChangeDataLocation";

	public static final String ACTION_CHANGE_INPUT_DATA_LOCATION_LS_PROPERTY = "ChangeInputDataLocation";

	public static final String ACTION_CHANGE_INPUT_IMAGE_LOCATION_LS_PROPERTY = "ChangeInputImageLocation";

	public static final String ACTION_CHANGE_INPUT_GRAPH_TEMPLATE_LOCATION_LS_PROPERTY = "ChangeInputGraphTemplateLocation";

	public static final String ACTION_CHANGE_JOURNAL_LOGOTYPE_LOCATION = "ChangeJournalLogotype";

	public static final String ACTION_CHANGE_LOCK_FILE_LOCATION = "ChangeLockFileLocation";

    public static final String ACTION_CHANGE_CARIES_FILE_LOCATION = "ChangeCariesFileLocation";

    public static final String ACTION_CHANGE_PDA_DATA_LOCATION_LS_PROPERTY = "ChangePDADataLocation";

	public static final String ACTION_CHANGE_PROTOCOL_LS_PROPERTY = "ChangeProtocol";

	public static final String ACTION_CHANGE_TEMPLATE_LS_PROPERTY = "ChangeTemplate";

	public static final String ACTION_CHANGE_TERM_DEFINITION_LOCATION = "ChangeTermDefinitionLocation";

	public static final String ACTION_CHANGE_TERM_VALUE_LOCATION = "ChangeTermValueLocation";

	public static final String ACTION_CHANGE_TRANSLATOR_LS_PROPERTY = "ChangeTranslator";

	public static final String ACTION_CHOOSE_COLOR_LS_PROPERTY = "ChooseColor";

	public static final String ACTION_CLEAR_PATIENTS_LS_PROPERTY = "ClearPatients";

	public static final String ACTION_CLOSE_GENERATED_PAGE_LS_PROPERTY = "CloseGeneratedPage";

	public static final String ACTION_CLOSE_TEMPLATE_LS_PROPERTY = "CloseTemplate";

	public static final String ACTION_CLOSE_TRANSLATOR_LS_PROPERTY = "CloseTranslator";

	public static final String ACTION_COPY_LS_PROPERTY = "Copy";

	public static final String ACTION_COPY_ENTIRE_TEXT_TO_CLIPBOARD_LS_PROPERTY = "CopyEntireTextToClipboard";

	public static final String ACTION_COPY_EXAMINATION_IMAGE_LS_PROPERTY = "CopyExaminationImage";

	public static final String ACTION_CUT_LS_PROPERTY = "Cut";

	public static final String ACTION_DEACTIVATE_LS_PROPERTY = "Deactivate";

	public static final String ACTION_EXIT_LS_PROPERTY = "Exit";

	public static final String ACTION_FONT_SIZE_10_LS_PROPERTY = "FontSize10";

	public static final String ACTION_FONT_SIZE_11_LS_PROPERTY = "FontSize11";

	public static final String ACTION_FONT_SIZE_12_LS_PROPERTY = "FontSize12";

	public static final String ACTION_FONT_SIZE_14_LS_PROPERTY = "FontSize14";

	public static final String ACTION_FONT_SIZE_16_LS_PROPERTY = "FontSize16";

	public static final String ACTION_FONT_SIZE_18_LS_PROPERTY = "FontSize18";

	public static final String ACTION_FONT_SIZE_24_LS_PROPERTY = "FontSize24";

	public static final String ACTION_FONT_SIZE_36_LS_PROPERTY = "FontSize36";

	public static final String ACTION_FONT_SIZE_48_LS_PROPERTY = "FontSize48";

	public static final String ACTION_FONT_SIZE_8_LS_PROPERTY = "FontSize8";

	public static final String ACTION_GENERATE_PREVIEW_LS_PROPERTY = "GeneratePreview";

	public static final String ACTION_GENERATE_SUMMARY_LS_PROPERTY = "GenerateSummary";

	public static final String ACTION_INVOKE_FASS_LS_PROPERTY = "InvokeFASS";

	public static final String ACTION_ITALIC_LS_PROPERTY = "Italic";

	public static final String ACTION_LEFT_JUSTIFY_LS_PROPERTY = "LeftJustify";

	public static final String ACTION_LOAD_TEMPLATE_LS_PROPERTY = "LoadTemplate";

	public static final String ACTION_LOAD_TRANSLATOR_LS_PROPERTY = "LoadTranslator";

	public static final String ACTION_MONOSPACED_LS_PROPERTY = "MonoSpaced";

	public static final String ACTION_NAME_PREFIX_LS_PROPERTY = "Action.Name.";

	public static final String ACTION_NEW_DAYNOTE_LS_PROPERTY = "NewDaynote";

	public static final String ACTION_NEW_PATIENT_LS_PROPERTY = "NewPatient";

	public static final String ACTION_NEW_SECTION_LS_PROPERTY = "NewSection";

	public static final String ACTION_NEW_TEMPLATE_LS_PROPERTY = "NewTemplate";

	public static final String ACTION_NEW_TRANSLATOR_LS_PROPERTY = "NewTranslator";

	public static final String ACTION_PASTE_LS_PROPERTY = "Paste";

	public static final String ACTION_PDA_EXPORT_LS_PROPERTY = "PDAExport";

	public static final String ACTION_PREVIEW_JOURNAL_LS_PROPERTY = "PreviewJournal";

	public static final String ACTION_PRINT_TEMPLATE_LS_PROPERTY = "PrintTemplate";

	public static final String ACTION_PRINT_JOURNAL_LS_PROPERTY = "PrintJournal";

	public static final String ACTION_REFRESH_PATIENT_LIST_LS_PROPERTY = "RefreshPatientList";

	public static final String ACTION_REMOVE_HEADER_IMAGE_LS_PROPERTY = "RemoveHeaderImage";

	public static final String ACTION_REMOVE_PATIENT_LS_PROPERTY = "RemovePatient";

	public static final String ACTION_REMOVE_PREVIEW_VALUE_LS_PROPERTY = "RemovePreviewValue";

	public static final String ACTION_REMOVE_SECTION_LS_PROPERTY = "RemoveSection";

	public static final String ACTION_REMOVE_VALUE_LS_PROPERTY = "RemoveValue";

	public static final String ACTION_RENAME_SECTION_LS_PROPERTY = "RenameSection";

	public static final String ACTION_RIGHT_JUSTIFY_LS_PROPERTY = "RightJustify";

	public static final String ACTION_SANSSERIF_LS_PROPERTY = "SansSerif";

	public static final String ACTION_SAVE_AS_HTML_LS_PROPERTY = "SaveAsHTML";

	public static final String ACTION_SAVE_AS_RTF_LS_PROPERTY = "SaveAsRTF";

	public static final String ACTION_SAVE_TEMPLATE_AS_LS_PROPERTY = "SaveTemplateAs";

	public static final String ACTION_SAVE_TEMPLATE_LS_PROPERTY = "SaveTemplate";

	public static final String ACTION_SAVE_TRANSLATOR_AS_LS_PROPERTY = "SaveTranslatorAs";

	public static final String ACTION_SAVE_TRANSLATOR_LS_PROPERTY = "SaveTranslator";
	
	public static final String ACTION_SELECT_ALL_LS_PROPERTY = "SelectAll";

	public static final String ACTION_SERIF_LS_PROPERTY = "Serif";

	public static final String ACTION_SETTINGS_LS_PROPERTY = "Settings";

	public static final String ACTION_SET_PAGE_VIEW_LS_PROPERTY = "SetPageView";

	public static final String ACTION_SET_PANE_VIEW_LS_PROPERTY = "SetPaneView";

	public static final String ACTION_SET_PREVIEW_VALUE_LS_PROPERTY = "SetPreviewValue";

	public static final String ACTION_SET_PREVIEW_PCODE_LS_PROPERTY = "SetPreviewPCode";

	public static final String ACTION_SHORT_DESCRIPTION_PREFIX_LS_PROPERTY = "Action.ShortDescription.";
	
	public static final String ACTION_SHOW_GRAPH_LS_PROPERTY = "ShowGraph";

	public static final String ACTION_TEXT_STRIKETHROUGH_LS_PROPERTY = "TextStrikethrough";

	public static final String ACTION_TEXT_SUBSCRIPT_LS_PROPERTY = "TextSubscript";

	public static final String ACTION_TEXT_SUPERSCRIPT_LS_PROPERTY = "TextSuperscript";

	public static final String ACTION_UNDERLINE_LS_PROPERTY = "Underline";

	public static final String ACTION_VIEW_EXAMINATION_IMAGE_LS_PROPERTY = "ViewExaminationImage";

	/************************************** END ACTIONS **************************************/





	/**************************************** BUTTONS ****************************************/

	public static final String BUTTON_ADD_TO_LS_PROPERTY = "Button.AddTo";
	
	public static final String BUTTON_ADD_LOCALLY_LS_PROPERTY = "Button.AddLocally";

	public static final String BUTTON_APPLY_LS_PROPERTY = "Button.Apply";

    public static final String BUTTON_BROWSE = "Button.Browse";

    public static final String BUTTON_CANCEL_PREVIEW_LS_PROPERTY = "Button.CancelPreview";

	public static final String BUTTON_CANCEL_TEXT_LS_PROPERTY = "Button.CancelText";

	public static final String BUTTON_CHANGE_COLOR_LS_PROPERTY = "Button.ChangeColor";

	public static final String BUTTON_CHANGE_LS_PROPERTY = "Button.Change";

	public static final String BUTTON_CHANGE_TRANSLATOR_OVERRIDE_LS_PROPERTY = "Button.ChangeTranslatorOverride";

	public static final String BUTTON_CLEAR_TREE_LS_PROPERTY = "Button.ClearTree";

	public static final String BUTTON_CLOSE_LS_PROPERTY = "Button.Close";

	public static final String BUTTON_DELETE_LS_PROPERTY = "Button.Delete";

	public static final String BUTTON_EDIT_LS_PROPERTY = "Button.Edit";

	public static final String BUTTON_END_PREVIEW_LS_PROPERTY = "Button.EndPreview";

	public static final String BUTTON_END_SETTINGS_LS_PROPERTY = "Button.EndSettings";
    
    public static final String BUTTON_EROSION_ALL_OK = "Button.ErosionAllOK";

    public static final String BUTTON_EXPORT_LS_PROPERTY = "Button.Export";

	public static final String BUTTON_LOAD_TEXT_LS_PROPERTY = "Button.LoadText";

	public static final String BUTTON_NEW_PCODE_LS_PROPERTY = "Button.NewPCode";

	public static final String BUTTON_NEW_TRANSLATOR_OVERRIDE_LS_PROPERTY = "Button.NewTranslatorOverride";

	public static final String BUTTON_NEXT_IMAGE_SET = "Button.NextImageSet";

    public static final String BUTTON_NO_LS_PROPERTY = "Button.No";

	public static final String BUTTON_OK_LS_PROPERTY = "Button.Ok";

	public static final String BUTTON_OPEN_LS_PROPERTY = "Button.Open";

	public static final String BUTTON_PREVIOUS_IMAGE_SET = "Button.PreviousImageSet";

	public static final String BUTTON_REFRESH_LS_PROPERTY = "Button.Refresh";

	public static final String BUTTON_REFRESH_PATIENTS_LS_PROPERTY = "Button.RefreshPatients";

	public static final String BUTTON_REMOVE_FROM_LS_PROPERTY = "Button.RemoveFrom";

	public static final String BUTTON_RENAME_LS_PROPERTY = "Button.Rename";

	public static final String BUTTON_SAVE_LS_PROPERTY = "Button.Save";

	public static final String BUTTON_SAVE_TEXT_LS_PROPERTY = "Button.SaveText";

	public static final String BUTTON_SAVE_TRANSLATOR_OVERRIDE_LS_PROPERTY = "Button.SaveTranslatorOverride";

	public static final String BUTTON_SET_AS_DEFAULT_LS_PROPERTY = "Button.SetAsDefault";

	public static final String BUTTON_YES_LS_PROPERTY = "Button.Yes";

	/************************************** END BUTTONS **************************************/





	/************************************** CHECK BOXES **************************************/

	public static final String CHECKBOX_ASK_BEFORE_SECTION_REMOVAL_LS_PROPERTY = "CheckBox.AskBeforeSectionRemoval";

	public static final String CHECKBOX_ENABLE_DOCUMENT_MODE_LS_PROPERTY = "CheckBox.EnableDocumentMode";

	public static final String CHECKBOX_GEMEN_LS_PROPERTY = "CheckBox.Gemen";

	public static final String CHECKBOX_INCLUDE_TERMDEF_LS_PROPERTY = "CheckBox.IncludeTermDefinitions";
    
    public static final String CHECKBOX_INCLUDE_TERMVAL_LS_PROPERTY = "CheckBox.IncludeTermValues";
    
    public static final String CHECKBOX_INCLUDE_DATABASE_LS_PROPERTY = "CheckBox.IncludeDatabase";
    
    public static final String CHECKBOX_INCLUDE_FORM_LS_PROPERTY = "CheckBox.IncludeForm";

    public static final String CHECKBOX_INCLUDE_TEMPLATE_LS_PROPERTY = "CheckBox.IncludeTemplate";

	public static final String CHECKBOX_INCLUDE_TRANSLATOR_LS_PROPERTY = "CheckBox.IncludeTranslator";

	public static final String CHECKBOX_KEEP_SELECTION_PREFIX_LS_PROPERTY = "CheckBox.KeepSelectionPrefix";

	public static final String CHECKBOX_LOCK_VALUES_LS_PROPERTY = "CheckBox.LockValues";

	public static final String CHECKBOX_MEDSUMMARY_DISPLAY_DATA_LOCATION_LS_PROPERTY = "CheckBox.MedSummaryDisplayDataLocation";

	public static final String CHECKBOX_MEDSUMMARY_DISPLAY_TEMPLATE_LS_PROPERTY = "CheckBox.MedSummaryDisplayTemplate";

	public static final String CHECKBOX_MEDSUMMARY_DISPLAY_TRANSLATOR_LS_PROPERTY = "CheckBox.MedSummaryDisplayTranslator";

	public static final String CHECKBOX_MEDSUMMARY_DISPLAY_SECTIONS_LS_PROPERTY = "CheckBox.MedSummaryDisplaySections";

	public static final String CHECKBOX_NO_TRANSLATION_NEEDED_LS_PROPERTY = "CheckBox.NoTranslationNeeded";

	public static final String CHECKBOX_PAD_EMPTY_AROUND_PICTURE_LS_PROPERTY = "CheckBox.PadEmptyAroundPicture";

	public static final String CHECKBOX_PERFORM_AUTOMATIC_VG_LS_PROPERTY = "CheckBox.PerformAutomaticVG";

	public static final String CHECKBOX_REMOVE_VALUE_LS_PROPERTY = "CheckBox.RemoveValue";

	public static final String CHECKBOX_REQUIRED_ATTRIBUTE_LS_PROPERTY = "CheckBox.RequiredAttribute";

	public static final String CHECKBOX_SHOULD_DISPLAY_INACTIVE_SECTIONS_LS_PROPERTY = "CheckBox.ShouldDisplayInactiveSections";

	public static final String CHECKBOX_SHOULD_DISPLAY_MEDSUMMARY_SPLASH_LS_PROPERTY = "CheckBox.ShouldDisplayMedSummarySplash";

	public static final String CHECKBOX_SHOULD_DISPLAY_SUMMARYCREATOR_SPLASH_LS_PROPERTY = "CheckBox.ShouldDisplaySummaryCreatorSplash";

	public static final String CHECKBOX_SHOULD_DISPLAY_TILE_LS_PROPERTY = "CheckBox.ShouldDisplayTile";

	public static final String CHECKBOX_SHOW_TRANSLATOR_AT_NEW_VALUE_LS_PROPERTY = "CheckBox.ShowTranslatorAtNewValue";
    
    public static final String CHECKBOX_START_MEDSUMMARY_LS_PROPERTY = "CheckBox.StartMedSummary";

    public static final String CHECKBOX_USE_BORDER_AROUND_PICTURE_LS_PROPERTY = "CheckBox.UseBorderAroundPicture";

	public static final String CHECKBOX_USE_LAST_SETTINGS_AT_INIT_LS_PROPERTY = "CheckBox.UseLastSettingsAtInit";

	public static final String CHECKBOX_USE_MEDFORM_TOUCHSCREEN_DATA_LS_PROPERTY = "CheckBox.UseMedFormTouchscreenData";

	public static final String CHECKBOX_USE_MUCOS_LS_PROPERTY = "CheckBox.UseMucos";

    public static final String CHECKBOX_USE_PLAQUE_INDEX_LS_PROPERTY = "CheckBox.UsePlaqueIndex";

	public static final String CHECKBOX_USE_SMART_PARSE_LS_PROPERTY = "CheckBox.UseSmartParse";

	public static final String CHECKBOX_VERSAL_LS_PROPERTY = "CheckBox.Versal";

	/************************************ END CHECK BOXES ************************************/





	/********************************** CHECK BOX MENU ITEMS *********************************/

	public static final String CHECKBOX_MENU_ITEM_SHOW_DATA_LOCATION_LS_PROPERTY = "CheckBoxMenuItem.ShowDataLocation";

	public static final String CHECKBOX_MENU_ITEM_SHOW_GRAPHIC_TEMPLATE_LS_PROPERTY = "CheckBoxMenuItem.ShowGraphicTemplate";

	public static final String CHECKBOX_MENU_ITEM_SHOW_TEMPLATE_LS_PROPERTY = "CheckBoxMenuItem.ShowTemplate";

	public static final String CHECKBOX_MENU_ITEM_SHOW_TRANSLATOR_LS_PROPERTY = "CheckBoxMenuItem.ShowTranslator";

	public static final String CHECKBOX_MENU_ITEM_SHOW_SECTIONS_LS_PROPERTY = "CheckBoxMenuItem.ShowSections";

	/******************************** END CHECK BOX MENU ITEMS *******************************/





	/************************************** COMBO BOXES **************************************/

	public static final String COMBOBOX_ALL_SECTIONS_COMBO_CHOICE_LS_PROPERTY = "ComboBox.AllSectionsComboChoice";

	public static final String COMBOBOX_DEFAULT_SECTION_COMBO_CHOICE_LS_PROPERTY = "ComboBox.DefaultSectionComboChoice";

	public static final String COMBOBOX_IDENTIFIER_VALUE_ATTRIBUTE_LS_PROPERTY = "ComboBox.IdentifierValueAttributePhrase";

	public static final String COMBOBOX_INCOMPLETE_VALUE_ATTRIBUTE_LS_PROPERTY = "ComboBox.IncompleteValueAttributePhrase";

	public static final String COMBOBOX_INTERVAL_VALUE_ATTRIBUTE_LS_PROPERTY = "ComboBox.IntervalValueAttributePhrase";

	public static final String COMBOBOX_NOTE_VALUE_ATTRIBUTE_LS_PROPERTY = "ComboBox.NoteValueAttributePhrase";

	public static final String COMBOBOX_MULTIPLE_VALUE_ATTRIBUTE_LS_PROPERTY = "ComboBox.MultipleValueAttributePhrase";

	public static final String COMBOBOX_SINGLE_VALUE_ATTRIBUTE_LS_PROPERTY = "ComboBox.SingleValueAttributePhrase";


	/************************************ END COMBO BOXES ************************************/





	/************************************** CONTEXT MENUES **************************************/

	public static final String CONTEXT_MENU_REMOVE_IMAGE_LS_PROPERTY = "ContextMenu.RemoveImage";

	public static final String CONTEXT_MENU_VIEW_FULL_SIZE_LS_PROPERTY = "ContextMenu.ViewImageFullSize";

	/************************************** END CONTEXT MENUES **************************************/





	/************************************* ERROR MESSAGES ************************************/

	public static final String ERROR_COULD_NOT_READ_BACKGROUND_DATA_LS_PROPERTY = "ErrorMessage.CouldNotReadBackgroundData";

	public static final String ERROR_COULD_NOT_READ_PDA_DATA_LS_PROPERTY = "ErrorMessage.CouldNotReadPDAData";

	public static final String ERROR_COULD_NOT_RETRIEVE_DATA_LOCATION_ID_LS_PROPERTY = "ErrorMessage.CoultNotRetrieveDataLocationID";

	public static final String ERROR_COULD_NOT_SAVE_NORMALLY_BUT_SAVED_LOCALLY = "ErrorMessage.CouldNotSaveNormallyButSavedLocally";

	public static final String ERROR_COULD_NOT_SAVE_NORMALLY_AND_LOCALLY = "ErrorMessage.CouldNotSaveNormallyAndLocally";

	public static final String ERROR_COULD_NOT_SET_DATA_COMPONENT_PACKAGE_LS_PROPERTY = "ErrorMessage.CouldNotSetDataComponentPackage";

	public static final String ERROR_COULD_NOT_USE_REMOTE_DATAHANDLING = "ErrorMessage.CouldNotUseRemoteDataHandling";

	public static final String ERROR_EXAMINATION_DATA_LOCATION_NOT_SET = "ErrorMessage.ExaminationDataLocationNotSet";

	public static final String ERROR_FIELD_ALREADY_EXISTS = "ErrorMessage.FieldAlreadyExists";

	public static final String ERROR_FIELD_INVALID_NAME = "ErrorMessage.FieldInvalidName";
	
	public static final String ERROR_INVALID_PCODE_PREFIX_LS_PROPERTY = "ErrorMessage.InvalidPcodePrefix";

	public static final String ERROR_INVALID_PCODE_SUFFIX_LS_PROPERTY = "ErrorMessage.InvalidPcodeSuffix";

	public static final String ERROR_MESSAGE_SPECIFIED_DIRECTORY_NON_EXISTANT_LS_PROPERTY =	"ErrorMessage.SpecifiedDirectoryNonExistant";

	public static final String ERROR_MESSAGE_SPECIFIED_FILE_NON_EXISTANT_LS_PROPERTY = "ErrorMessage.SpecifiedFileNonExistant";

	public static final String ERROR_MISSING_PCODE_LS_PROPERTY = "ErrorMessage.MissingPCode";

	public static final String ERROR_MISSING_REQUIRED_INPUT_VALUE = "ErrorMessage.MissingRequiredInputValue";

	public static final String ERROR_MISSING_VALUE_LS_PROPERTY = "ErrorMessage.MissingValue";

	public static final String ERROR_NO_PATIENT_IDENTIFIER_TERM_FOUND = "ErrorMessage.NoPatientIdentifierTermFound";

	public static final String ERROR_NO_TEMPLATE_SET = "ErrorMessage.NoTemplateSet";

	public static final String ERROR_NO_TRANSLATOR_SET = "ErrorMessage.NoTranslatorSet";

	public static final String ERROR_NR_GENERATOR_LOCATION_NOT_SET = "ErrorMessage.NRGeneratorLocationNotSet";
	
	public static final String ERROR_OEHR_COULD_NOT_PARSE_LS_PROPERTY = "ErrorMessage.OEHRCouldNotParse";
	
	public static final String ERROR_OEHR_COULD_NOT_SERIALIZE_LS_PROPERTY = "ErrorMessage.OEHRCouldNotSerialize";

	public static final String ERROR_OEHR_COULD_NOT_VALIDATE_LS_PROPERTY = "ErrorMessage.OEHRCouldNotValidate";
	
	public static final String ERROR_ONLY_PRESET_VALUES_ALLOWED_LS_PROPERTY = "ErrorMessage.OnlyPresetValuesAllowed";

	public static final String ERROR_PACKAGE_DOES_NOT_CONTAIN_EXAMINATION = "ErrorMessage.PackageDoesNotContainExamination";

	public static final String ERROR_TAB_ALREADY_EXISTS = "ErrorMessage.TabAlreadyExists";

	public static final String ERROR_TAB_INVALID_NAME = "ErrorMessage.TabInvalidName";

	public static final String ERROR_TERM_LOCATIONS_INVALID = "ErrorMessage.TermLocationsInvalid";

	public static final String ERROR_TERM_LOCATIONS_NOT_SET = "ErrorMessage.TermLocationsNotSet";

	public static final String ERROR_TERM_NOT_FOUND_IN_TRANSLATOR = "ErrorMessage.TermNotFoundInTranslator";

	public static final String ERROR_USER_ID_NOT_SET = "ErrorMessage.UserIDNotSet";

	public static final String ERROR_USER_ID_NOT_OF_LENGTH_3 = "ErrorMessage.UserIDNotOfLength3";

	public static final String ERROR_USER_NAME_NOT_SET = "ErrorMessage.UserNameNotSet";

	public static final String ERROR_NO_REMOTE_EXAMINATION_DATA_HANDLER_IN_USE = "ErrorMessage.NoRemoteExaminationDataHandlerInUse";

	public static final String ERROR_WHILE_TRANSLATING = "ErrorMessage.ErrorWhileTranslating";

	public static final String ERROR_WHILE_PREVIEWING = "ErrorMessage.ErrorWhilePreviewing";

	public static final String ERROR_WHILE_GRAPHING = "ErrorMessage.ErrorWhileGraphing";

	public static final String ERROR_WHILE_PARSING_GRAPH_TEMPLATE = "ErrorMessage.ErrorWhileParsingGraphTemplate";

	public static final String ERROR_WHILE_SAVING = "ErrorMessage.ErrorWhileSaving";

	public static final String ERROR_WRITE_HTML_LS_PROPERTY = "ErrorMessage.WriteHTML";

	public static final String ERROR_WRITE_PROTOCOL_LS_PROPERTY = "ErrorMessage.WriteProtocol";

	public static final String ERROR_WRITE_PROTOCOL_PERMISSION_LS_PROPERTY = "ErrorMessage.WriteProtocolPermission";

	public static final String ERROR_WRITE_RTF_LS_PROPERTY = "ErrorMessage.WriteRTF";

	/*********************************** END ERROR MESSAGES **********************************/





	/************************************* FILE FILTERS **************************************/

	public static final String FILEFILTER_DIRECTORY_LS_PROPERTY = "FileFilter.Directory";

	public static final String FILEFILTER_HTML_FILES_LS_PROPERTY = "FileFilter.HTMLFiles";

	public static final String FILEFILTER_IMAGE_FILES_LS_PROPERTY = "FileFilter.ImageFiles";

	public static final String FILEFILTER_MVD_DIRECTORIES_LS_PROPERTY = "FileFilter.MVDDirectories";

	public static final String FILEFILTER_RTF_FILES_LS_PROPERTY = "FileFilter.RTFFiles";
	
	public static final String FILEFILTER_TREE_FILES_LS_PROPERTY = "FileFilter.TreeFiles";

	public static final String FILEFILTER_TXT_FILES_LS_PROPERTY = "FileFilter.TXTFiles";

	public static final String FILEFILTER_XML_FILES_LS_PROPERTY = "FileFilter.XMLFiles";

	public static final String FILEFILTER_LOCK_FILES_LS_PROPERTY = "FileFilter.LockFiles";

	/*********************************** END FILE FILTERS ************************************/





	/**************************************** LABELS *****************************************/

	public static final String LABEL_ADDING_EXAMINATION_IMAGES_LS_PROPERTY = "Label.AddingExaminationImages";

	public static final String LABEL_ADDING_PATIENTS_LS_PROPERTY = "Label.AddingPatients";

	public static final String LABEL_ADDRESS_LINE_1_LS_PROPERTY = "Label.AddressLine1";

	public static final String LABEL_ADDRESS_LINE_2_LS_PROPERTY = "Label.AddressLine2";

	public static final String LABEL_ADDRESS_LINE_3_LS_PROPERTY = "Label.AddressLine3";

	public static final String LABEL_ADDRESS_LINE_4_LS_PROPERTY = "Label.AddressLine4";

	public static final String LABEL_ADDRESS_LINE_5_LS_PROPERTY = "Label.AddressLine5";

	public static final String LABEL_AFTER_LS_PROPERTY = "Label.After";

	public static final String LABEL_ATTRIBUTE_PHRASE_LS_PROPERTY = "Label.AttributePhrase";

	public static final String LABEL_BACKGROUND_OF_INPUT_VALUES = "Label.BackgroundOfInputValues";

	public static final String LABEL_BEFORE_LS_PROPERTY = "Label.Before";

	public static final String LABEL_BUILDING_PARSE_TREES_LS_PROPERTY = "Label.BuildingParseTrees";

	public static final String LABEL_CHANGE_SERVER_NAME_DESCRIPTION_LS_PROPERTY = "Label.ChangeServerNameDescription";

	public static final String LABEL_CHANGE_TRANSLATOR_TO_LS_PROPERTY = "Label.ChangeTranslatorTo";

	public static final String LABEL_CHANGES_TAKE_EFFECT_LS_PROPERTY = "Label.ChangesTakeEffect";

	public static final String LABEL_CURRENT_LANGUAGE_LS_PROPERTY = "Label.CurrentLanguage";

	public static final String LABEL_CURRENT_PLATFORM_LS_PROPERTY = "Label.CurrentPlatform";

	public static final String LABEL_CURRENT_TEMPLATES_LS_PROPERTY = "Label.CurrentTemplates";

	public static final String LABEL_CURRENT_TRANSLATORS_LS_PROPERTY = "Label.CurrentTranslators";

	public static final String LABEL_CURRENT_TRANSLATOR_LS_PROPERTY = "Label.CurrentTranslator";

	public static final String LABEL_DATA_IMPORTED_OK_LS_PROPERTY = "Label.DataImportedOK";

	public static final String LABEL_DATA_IMPORTED_OK_PDA_LS_PROPERTY = "Label.DataImportedOKPDA";

	public static final String LABEL_DATA_LOCATION_LS_PROPERTY = "Label.DataLocation";

	public static final String LABEL_DEFAULT_DATABASE_LS_PROPERTY = "Label.DefaultDatabase"; // kind of same as above

	public static final String LABEL_DEFAULT_PROTOCOL_LS_PROPERTY = "Label.DefaultProtocol";

	public static final String LABEL_DISTRIBUTED_EXAMINATIONS_LS_PROPERTY = "Label.DistributedExaminations";

	public static final String LABEL_DISTRIBUTED_TERM_DEFINITION_LOCATION_LS_PROPERTY = "Label.DistributedTermDefinitionLocation";

	public static final String LABEL_DISTRIBUTED_TERM_VALUE_LOCATION_LS_PROPERTY = "Label.DistributedTermValueLocation";

	public static final String LABEL_EARLIER_SECTION_NAME_LS_PROPERTY = "Label.EarlierSectionName";

	public static final String LABEL_EXAMINATION_LOCATION_LS_PROPERTY = "Label.ExaminationLocation";

	public static final String LABEL_EXAMINATION_SAVED_OK_LS_PROPERTY = "Label.ExaminationSavedOK";

    public static final String LABEL_COULDNT_GET_CARIES_DATA = "Label.CouldntGetCariesData";

    public static final String LABEL_CARIES_FILE_MISSING = "Label.CariesFileMissing";

    public static final String LABEL_CARIES_FILE_INVALID = "Label.CariesFileInvalid";

    public static final String LABEL_EXAMPLE_TEXT = "Label.ExampleText";

	public static final String LABEL_EXIT_MEDSERVER_LS_PROPERTY = "Label.ExitMedServer";

	public static final String LABEL_EXIT_MEDSUMMARY_LS_PROPERTY = "Label.ExitMedSummary";

	public static final String LABEL_EXIT_SUMMARYCREATOR_LS_PROPERTY = "Label.ExitSummaryCreator";

	public static final String LABEL_FASS_URL_LS_PROPERTY = "Label.FASSURL";

	public static final String LABEL_FILTER_ON_LS_PROPERTY = "Label.FilterOn";

	public static final String LABEL_FREETEXT_LS_PROPERTY = "Label.FreeText";

	public static final String LABEL_GRAPHIC_TEMPLATES_LS_PROPERTY = "Label.GraphicTemplates";

	public static final String LABEL_INTERVAL_EXISTED_LS_PROPERTY = "Label.IntervalExisted";

	public static final String LABEL_IMAGE_CATEGORY_NAME_LS_PROPERTY = "Label.ImageCategory";

	public static final String LABEL_IMAGE_DIR_EMPTY_LS_PROPERTY = "Label.ImageDirEmpty";

	public static final String LABEL_IMAGE_DIR_NOT_FOUND_LS_PROPERTY = "Label.ImageDirNotFound";

	public static final String LABEL_IMAGE_DURATION_LS_PROPERTY = "Label.ImageDuration";

	public static final String LABEL_IMAGE_INPUT_DIR_LS_PROPERTY = "Label.ImageInputDir";

	public static final String LABEL_IMAGE_MAX_THUMBNAILS_LS_PROPERTY = "Label.ImageMaxThumbnails";

	public static final String LABEL_GRAPH_TEMPLATE_LOCATION_LS_PROPERTY = "Label.GraphTemplateLocation";

	public static final String LABEL_IMAGE_TERM_NAME_LS_PROPERTY = "Label.ImageTermName";

	public static final String LABEL_IMPORT_NO_DATA_EXISTS_LS_PROPERTY = "Label.ImportNoDataExists";

	public static final String LABEL_INFORMAL_SERVER_NAME_LS_PROPERTY = "Label.InformalServerName";

	public static final String LABEL_KEEP_SEARCH_STRING_LS_PROPERTY = "Label.KeepSearchString";
	
	public static final String LABEL_LANGUAGE_LS_PROPERTY = "Label.Language";

	public static final String LABEL_LOCAL_LOCATION_LS_PROPERTY = "Label.LocalLocation";

	public static final String LABEL_LOCKED_INPUT_FIELD_LS_PROPERTY = "Label.LockedInputField";

	public static final String LABEL_LOCKFILE_LOCATION_LS_PROPERTY = "Label.LockFileLocation";

    public static final String LABEL_CARIESFILE_LOCATION_LS_PROPERTY = "Label.CariesFileLocation";

    public static final String LABEL_LOGOTYPE_PATH_LS_PROPERTY = "Label.LogotypePath";

	public static final String LABEL_MAX_HEIGHT_FOR_INSERTED_PICTURE_LS_PROPERTY = "Label.MaxHeightForInsertedPicture";

	public static final String LABEL_MEDFORM_URL_LS_PROPERTY = "Label.MedFormURL";

	public static final String LABEL_MEDFORM_USER_LS_PROPERTY = "Label.MedFormUser";

	public static final String LABEL_MEDFORM_MVD_LS_PROPERTY = "Label.MedFormMVD";
    
    public static final String LABEL_MEDSUMMARY_LS_PROPERTY = "Label.MedSummary";

    public static final String LABEL_MR_LANGUAGE_LS_PROPERTY = "Label.MRLanguage";

	public static final String LABEL_NAME_PROMPT_LS_PROPERTY = "Label.NamePrompt";

	public static final String LABEL_NEW_FIELD_NAME_LS_PROPERTY = "Label.NewFieldName";

	public static final String LABEL_NEW_INTERVAL_PROMPT_LS_PROPERTY = "Label.NewIntervalPrompt";

	public static final String LABEL_NEW_PCODE_LS_PROPERTY = "Label.NewPCode";

	public static final String LABEL_NEW_SECTION_NAME_LS_PROPERTY = "Label.NewSectionName";

	public static final String LABEL_NEW_TAB_NAME_LS_PROPERTY = "Label.NewTabName";

	public static final String LABEL_NEW_TRANSLATOR_PROMPT_LS_PROPERTY = "Label.NewTranslatorPrompt";

	public static final String LABEL_NEW_VALUE_PROMPT_LS_PROPERTY = "Label.NewValuePrompt";

	public static final String LABEL_NO_FIELD_SELECTED_LS_PROPERTY = "Label.NoFieldSelected";

	public static final String LABEL_NO_TAB_SELECTED_LS_PROPERTY = "Label.NoTabSelected";

	public static final String LABEL_NONE_LS_PROPERTY = "Label.None";

	public static final String LABEL_NTL_SEPARATOR_LS_PROPERTY = "Label.NTLSeparator";

	public static final String LABEL_OBSERVANDUM_PICTURE_TEXT_LS_PROPERTY = "Label.ObservandumPictureText";

	public static final String LABEL_OEHR_ARCHETYPES_LOCATION_LS_PROPERTY = "Label.OEHRArchetypesLocation";
	
	public static final String LABEL_OEHR_DOMAIN_LS_PROPERTY = "Label.OEHRDomain";
	
	public static final String LABEL_OEHR_EXIT_WORKBENCH_LS_PROPERTY = "Label.OEHRExitWorkbench";
	
	public static final String LABEL_OEHR_TECHNICAL_LS_PROPERTY = "Label.OEHRTechnical";

	public static final String LABEL_OEHR_TEMPLATES_LOCATION_LS_PROPERTY = "Label.OEHRTemplatesLocation";
	
	public static final String LABEL_OEHR_VIEW_MODE_LS_PROPERTY = "Label.OEHRViewMode";

	public static final String LABEL_OPEN_TEMPLATE_DEMAND_LS_PROPERTY = "Label.OpenTemplateDemand";

	public static final String LABEL_PACKAGE_NAME_LS_PROPERTY = "Label.PackageName";

	public static final String LABEL_PACKAGES_LS_PROPERTY = "Label.Packages";

	public static final String LABEL_PARSING_EXAMINATIONS_LS_PROPERTY = "Label.ParsingExaminations";

	public static final String LABEL_PARSING_TREES_LS_PROPERTY = "Label.ParsingTrees";

	public static final String LABEL_PATIENT_LOCATION_LS_PROPERTY = "Label.PatientLocation";

	public static final String LABEL_PDA_LOCATION_LS_PROPERTY = "Label.PDALocation";

	public static final String LABEL_PIXELS_LS_PROPERTY = "Label.Pixels";

	public static final String LABEL_PLACE_SECTION_LS_PROPERTY = "Label.PlaceSection";

	public static final String LABEL_PRESET_NONE_LS_PROPERTY = "Label.PresetNone";

	public static final String LABEL_PREVIEW_PCODE_LS_PROPERTY = "Label.PreviewPCode";

	public static final String LABEL_PREVIEW_TRANSLATOR_LS_PROPERTY = "Label.PreviewTranslator";

	public static final String LABEL_PREVIEW_TEMPLATE_LS_PROPERTY = "Label.PreviewTemplate";

	public static final String LABEL_REQUIRED_INPUT_FIELD_LS_PROPERTY = "Label.RequiredInputField";

	public static final String LABEL_SAVE_TEMPLATE_AS_LS_PROPERTY = "Label.SaveTemplateAs";

	public static final String LABEL_SAVE_TRANSLATOR_AS_LS_PROPERTY = "Label.SaveTranslatorAs";

	public static final String LABEL_SEARCH_FIELD_LS_PROPERTY = "Label.SearchField";

	public static final String LABEL_SECTION_LS_PROPERTY = "Label.Section";

	public static final String LABEL_SECTION_NAME_LS_PROPERTY = "Label.SectionName";

	public static final String LABEL_SELECT_EXAMINATION_IMAGES_LS_PROPERTY = "Label.SelectExaminationImages";

	public static final String LABEL_SEPARATOR_LS_PROPERTY = "Label.Separator";

	public static final String LABEL_SERVER_LOCATION_LS_PROPERTY = "Label.ServerLocation";

	public static final String LABEL_SERVER_NAME_TO_CLIENTS_LS_PROPERTY = "Label.ServerNameToClients";

	public static final String LABEL_SHOULD_SAVE_PROTOCOL_LS_PROPERTY = "Label.ShouldSaveProtocol";

	public static final String LABEL_SHOULD_SAVE_EXAMINATION_LS_PROPERTY = "Label.ShouldSaveExamination";

	public static final String LABEL_SHOULD_SAVE_TEMPLATE_LS_PROPERTY = "Label.ShouldSaveTemplate";

	public static final String LABEL_SHOULD_SAVE_TRANSLATOR_LS_PROPERTY = "Label.ShouldSaveTranslator";

	public static final String LABEL_SHOW_TRANSLATOR_AT_NEW_VALUE_LS_PROPERTY = "Label.ShowTranslatorAtNewValue";
    
    public static final String LABEL_START_MEDSUMMARY_LS_PROPERTY = "Label.StartMedSummary";

    public static final String LABEL_SHOWING_ALL_LS_PROPERTY = "Label.ShowingAll";

	public static final String LABEL_SPAWNING_CONTENT_LS_PROPERTY = "Label.SpawningContent";

	public static final String LABEL_TEMPLATE_LOCATION_LS_PROPERTY = "Label.TemplateLocation";

	public static final String LABEL_TEMPLATE_LS_PROPERTY = "Label.Template";

	public static final String LABEL_TERM_DEFINITION_LOCATION_LS_PROPERTY = "Label.TermDefinitionLocation";

	public static final String LABEL_TERM_DESCRIPTOR_LS_PROPERTY = "Label.TermDescriptor";
	
	public static final String LABEL_TERM_VALUE_LOCATION_LS_PROPERTY = "Label.TermValueLocation";
	 	
	public static final String LABEL_TERMS_LOCKED_LS_PROPERTY = "Label.TermsLocked";

	public static final String LABEL_TOTAL_NUMBER_OF_PATIENTS_LS_PROPERTY = "Label.TotalNumberOfPatients";

	public static final String LABEL_TOTAL_NUMBER_OF_EXAMINATIONS_LS_PROPERTY = "Label.TotalNumberOfExaminations";

	public static final String LABEL_TOTAL_NUMBER_OF_TERMS_LS_PROPERTY = "Label.TotalNumberOfTerms";

	public static final String LABEL_TOTAL_NUMBER_OF_VALUES_LS_PROPERTY = "Label.TotalNumberOfValues";

	public static final String LABEL_TRANSLATOR_LOCATION_LS_PROPERTY = "Label.TranslatorLocation";

	public static final String LABEL_TRANSLATOR_LS_PROPERTY = "Label.Translator";

	public static final String LABEL_TYPE_DESCRIPTOR_LS_PROPERTY = "Label.TypeDescriptor";

	public static final String LABEL_USE_LS_PROPERTY = "Label.Use";

	public static final String LABEL_USE_MEDFORM_TOUCHSCREEN_DATA_LS_PROPERTY = "Label.UseMedFormTouchscreenData";

	public static final String LABEL_USE_TEMPLATE_LS_PROPERTY = "Label.UseTemplate";

	public static final String LABEL_USE_PLAQUE_INDEX_LS_PROPERTY = "Label.UsePlaqueIndex";

	public static final String LABEL_USE_MUCOS_LS_PROPERTY = "Label.UseMucos";

    public static final String LABEL_USER_ID_LS_PROPERTY = "Label.UserID";

	public static final String LABEL_USER_NAME_LS_PROPERTY = "Label.UserName";

	public static final String LABEL_VALUE_EXISTED_LS_PROPERTY = "Label.ValueExisted";

	public static final String LABEL_VALUES_DESCRIPTOR_LS_PROPERTY = "Label.ValuesDescriptor";

	public static final String LABEL_VERTICAL_LS_PROPERTY = "Label.Vertical";

	public static final String LABEL_WINDOW_MANAGEMENT_LS_PROPERTY = "Label.WindowManagement";

	public static final String LABEL_HORIZONTAL_LS_PROPERTY = "Label.Horizontal";

	/************************************** END LABELS ***************************************/





	/****************************************** MENU *****************************************/

	public static final String MENU_ALIGNMENT_LS_PROPERTY = "Menu.Alignment";

	public static final String MENU_ARCHIVE_LS_PROPERTY = "Menu.Archive";

	public static final String MENU_EDIT_LS_PROPERTY = "Menu.Edit";

	public static final String MENU_FIELD_LS_PROPERTY = "Menu.Field";

	public static final String MENU_FONT_FAMILY_LS_PROPERTY = "Menu.FontFamily";

	public static final String MENU_FONT_SIZE_LS_PROPERTY = "Menu.FontSize";

	public static final String MENU_FONT_STYLE_LS_PROPERTY = "Menu.FontStyle";

	public static final String MENU_FORMAT_LS_PROPERTY = "Menu.Format";

	public static final String MENU_HELP_LS_PROPERTY = "Menu.Help";

	public static final String MENU_LOOK_AND_FEEL_LS_PROPERTY = "Menu.LookAndFeel";

	public static final String MENU_TABS_LS_PROPERTY = "Menu.Tabs";

	public static final String MENU_VIEW_LS_PROPERTY = "Menu.View";

	public static final String MENU_ITEM_FIELD_DOWN_LS_PROPERTY = "MenuItem.FieldDownwards";

	public static final String MENU_ITEM_FIELD_NEW_LS_PROPERTY = "MenuItem.FieldNew";

	public static final String MENU_ITEM_FIELD_REMOVE_LS_PROPERTY = "MenuItem.FieldRemove";

	public static final String MENU_ITEM_FIELD_UP_LS_PROPERTY = "MenuItem.FieldUpwards";

	public static final String MENU_ITEM_FILE_CHANGE_PROTOCOL_LS_PROPERTY = "MenuItem.FileChangeProtocol";

	public static final String MENU_ITEM_FILE_CLOSE_EXAMINATION_LS_PROPERTY = "MenuItem.FileCloseExamination";

	public static final String MENU_ITEM_FILE_EXIT_LS_PROPERTY = "MenuItem.FileExit";

	public static final String MENU_ITEM_FILE_IMPORT_DATA_LS_PROPERTY = "MenuItem.FileImportData";
	
	public static final String MENU_ITEM_FILE_IMPORT_LS_PROPERTY = "MenuItem.FileImport";

	public static final String MENU_ITEM_FILE_NEW_EXAMINATION_LS_PROPERTY = "MenuItem.FileNewExamination";

	public static final String MENU_ITEM_FILE_NEW_FORM_LS_PROPERTY = "MenuItem.FileNewForm";

	public static final String MENU_ITEM_FILE_OPEN_LS_PROPERTY = "MenuItem.FileOpen";

	public static final String MENU_ITEM_FILE_PREFERENCES_LS_PROPERTY = "MenuItem.FilePreferences";

	public static final String MENU_ITEM_FILE_PREVIEW_LS_PROPERTY = "MenuItem.FilePreview";

	public static final String MENU_ITEM_FILE_SAVE_LS_PROPERTY = "MenuItem.FileSave";
	
	public static final String MENU_ITEM_FILE_SAVE_AND_CLOSE_LS_PROPERTY = "MenuItem.FileSaveAndClose";
	
	public static final String MENU_ITEM_FILE_SAVE_AS_LS_PROPERTY = "MenuItem.FileSaveAs";

	public static final String MENU_ITEM_FILE_SAVE_AS_XML_LS_PROPERTY = "MenuItem.FileSaveAsXML";
			
	public static final String MENU_ITEM_EDIT_SELECT_ALL_LS_PROPERTY = "MenuItem.SelectAll";
		
	public static final String MENU_ITEM_GRAPH_PREVIEW_LS_PROPERTY = "MenuItem.GraphPreview";

	public static final String MENU_ITEM_HELP_ABOUT_FORMEDITOR_LS_PROPERTY = "MenuItem.HelpAboutFormEditor";

	public static final String MENU_ITEM_HELP_ABOUT_MEDRECORDS_LS_PROPERTY = "MenuItem.HelpAboutMedRecords";
	
	public static final String MENU_ITEM_OEHR_DISPLAY_FILE_LS_PROPERTY = "MenuItem.OEHRDisplayFile";
	
	public static final String MENU_ITEM_OEHR_HELP_ABOUT_ARCHETYPE_WORKBENCH_LS_PROPERTY = "MenuItem.OEHRHelpAboutArchetypeWorkbench";
	
	public static final String MENU_ITEM_OEHR_VIEW_MODE_LS_PROPERTY = "MenuItem.OEHRViewMode";
	
	public static final String MENU_ITEM_OEHR_EXPAND_TREE_LS_PROPERTY = "MenuItem.OEHRExpandTree";

	public static final String MENU_ITEM_OPEN_FASS_LS_PROPERTY = "MenuItem.OpenFASS";

	public static final String MENU_ITEM_IMPORT_PDA_LS_PROPERTY = "MenuItem.ImportPDA";

	public static final String MENU_ITEM_REMOVE_VALUE_LS_PROPERTY = "MenuItem.RemoveValue";

	public static final String MENU_ITEM_TAB_DOWN_LS_PROPERTY = "MenuItem.TabDownwards";

	public static final String MENU_ITEM_TAB_NEW_LS_PROPERTY = "MenuItem.TabNew";

	public static final String MENU_ITEM_TAB_REMOVE_LS_PROPERTY = "MenuItem.TabRemove";

	public static final String MENU_ITEM_TAB_UP_LS_PROPERTY = "MenuItem.TabUpwards";


	public static final String MNEMONIC_MENU_ALIGNMENT_LS_PROPERTY = "Mnemonic.MenuAlignment";

	public static final String MNEMONIC_MENU_ARCHIVE_LS_PROPERTY = "Mnemonic.MenuArchive";

	public static final String MNEMONIC_MENU_EDIT_LS_PROPERTY = "Mnemonic.MenuEdit";

	public static final String MNEMONIC_MENU_FIELD_LS_PROPERTY = "Mnemonic.MenuField";

	public static final String MNEMONIC_MENU_FAMILY_LS_PROPERTY = "Mnemonic.MenuFamily";

	public static final String MNEMONIC_MENU_FORMAT_LS_PROPERTY = "Mnemonic.MenuFormat";

	public static final String MNEMONIC_MENU_HELP_LS_PROPERTY = "Mnemonic.MenuHelp";

	public static final String MNEMONIC_MENU_LOOK_AND_FEEL_LS_PROPERTY = "Mnemonic.MenuLookAndFeel";

	public static final String MNEMONIC_MENU_ITEM_ABOUT_LS_PROPERTY = "Mnemonic.MenuItemAbout";

	public static final String MNEMONIC_MENU_ITEM_ACTIVATE_LS_PROPERTY = "Mnemonic.MenuItemActivate";

	public static final String MNEMONIC_MENU_ITEM_ASSOCIATE_TRANSLATOR_LS_PROPERTY = "Mnemonic.MenuItemAssociateTranslator";

	public static final String MNEMONIC_MENU_ITEM_BOLD_LS_PROPERTY = "Mnemonic.MenuItemBold";

	public static final String MNEMONIC_MENU_ITEM_CENTER_LS_PROPERTY = "Mnemonic.MenuItemCenter";

	public static final String MNEMONIC_MENU_ITEM_CHANGE_DATA_LOCATION_LS_PROPERTY = "Mnemonic.MenuItemChangeDataLocation";

	public static final String MNEMONIC_MENU_ITEM_CHANGE_PROTOCOL_LS_PROPERTY = "Mnemonic.MenuItemChangeProtocol";

	public static final String MNEMONIC_MENU_ITEM_CHANGE_TEMPLATE_LS_PROPERTY = "Mnemonic.MenuItemChangeTemplate";

	public static final String MNEMONIC_MENU_ITEM_CHANGE_TRANSLATOR_LS_PROPERTY = "Mnemonic.MenuItemChangeTranslator";

	public static final String MNEMONIC_MENU_ITEM_CHOOSE_COLOR_LS_PROPERTY = "Mnemonic.MenuItemChooseColor";

	public static final String MNEMONIC_MENU_ITEM_CLEAR_LS_PROPERTY = "Mnemonic.MenuItemClear";

	public static final String MNEMONIC_MENU_ITEM_CLOSE_EXAMINATION_LS_PROPERTY = "Mnemonic.MenuItemCloseExamination";

	public static final String MNEMONIC_MENU_ITEM_CLOSE_TEMPLATE_LS_PROPERTY = "Mnemonic.MenuItemCloseTemplate";

	public static final String MNEMONIC_MENU_ITEM_CLOSE_TRANSLATOR_LS_PROPERTY = "Mnemonic.MenuItemCloseTranslator";

	public static final String MNEMONIC_MENU_ITEM_CLOSE_GENERATED_PAGE_LS_PROPERTY = "Mnemonic.MenuItemCloseGeneratedPage";

	public static final String MNEMONIC_MENU_ITEM_COPY_LS_PROPERTY = "Mnemonic.MenuItemCopy";

	public static final String MNEMONIC_MENU_ITEM_CUT_LS_PROPERTY = "Mnemonic.MenuItemCut";

	public static final String MNEMONIC_MENU_ITEM_DEACTIVATE_LS_PROPERTY = "Mnemonic.MenuItemDeactivate";

	public static final String MNEMONIC_MENU_ITEM_DOWNWARDS_LS_PROPERTY = "Mnemonic.MenuItemDownwards";

	public static final String MNEMONIC_MENU_ITEM_EXIT_LS_PROPERTY = "Mnemonic.MenuItemExit";

	public static final String MNEMONIC_MENU_ITEM_GENERATE_SUMMARY_LS_PROPERTY = "Mnemonic.MenuItemGenerateSummary";

	public static final String MNEMONIC_MENU_ITEM_GRAPH_PREVIEW_LS_PROPERTY = "Mnemonic.MenuItemGraphPreview";

	public static final String MNEMONIC_MENU_ITEM_IMPORT_LS_PROPERTY = "Mnemonic.MenuItemImport";
	
	public static final String MNEMONIC_MENU_ITEM_IMPORT_DATA_LS_PROPERTY = "Mnemonic.MenuItemImportData";
	
	public static final String MNEMONIC_MENU_ITEM_IMPORT_PDA_LS_PROPERTY = "Mnemonic.MenuItemImportPDA";

	public static final String MNEMONIC_MENU_ITEM_ITALIC_LS_PROPERTY = "Mnemonic.MenuItemItalic";

	public static final String MNEMONIC_MENU_ITEM_LEFT_LS_PROPERTY = "Mnemonic.MenuItemLeft";

	public static final String MNEMONIC_MENU_ITEM_MONOSPACED_LS_PROPERTY = "Mnemonic.MenuItemMonospaced";

	public static final String MNEMONIC_MENU_ITEM_NEW_DAYNOTE_LS_PROPERTY = "Mnemonic.MenuItemNewDaynote";

	public static final String MNEMONIC_MENU_ITEM_NEW_EXAMINATION_LS_PROPERTY = "Mnemonic.MenuItemNewExamination";

	public static final String MNEMONIC_MENU_ITEM_NEW_FIELD_LS_PROPERTY = "Mnemonic.MenuItemNewField";

	public static final String MNEMONIC_MENU_ITEM_NEW_PATIENT_LS_PROPERTY = "Mnemonic.MenuItemNewPatient";

	public static final String MNEMONIC_MENU_ITEM_NEW_PROTOCOL_LS_PROPERTY = "Mnemonic.MenuItemNewProtocol";

	public static final String MNEMONIC_MENU_ITEM_NEW_SECTION_LS_PROPERTY = "Mnemonic.MenuItemNewSection";

	public static final String MNEMONIC_MENU_ITEM_NEW_TAB_LS_PROPERTY = "Mnemonic.MenuItemNewTab";

	public static final String MNEMONIC_MENU_ITEM_NEW_TEMPLATE_LS_PROPERTY = "Mnemonic.MenuItemNewTemplate";

	public static final String MNEMONIC_MENU_ITEM_NEW_TRANSLATOR_LS_PROPERTY = "Mnemonic.MenuItemNewTranslator";
	
	public static final String MNEMONIC_MENU_ITEM_OEHR_DISPLAY_FILE_LS_PROPERTY="Mnemonic.MenuItemOEHRDisplayFile";
	
	public static final String MNEMONIC_MENU_ITEM_OEHR_EXPAND_TREE_LS_PROPERTY="Mnemonic.MenuItemOEHRExpandTree";
	
	public static final String MNEMONIC_MENU_ITEM_OEHR_VIEW_MODE_LS_PROPERTY = "Mnemonic.MenuItemOEHRViewMode";

	public static final String MNEMONIC_MENU_ITEM_OPEN_LS_PROPERTY = "Mnemonic.MenuItemOpen";

	public static final String MNEMONIC_MENU_ITEM_OPEN_FASS_LS_PROPERTY = "Mnemonic.MenuItemOpenFASS";

	public static final String MNEMONIC_MENU_ITEM_OPEN_TEMPLATE_LS_PROPERTY = "Mnemonic.MenuItemOpenTemplate";

	public static final String MNEMONIC_MENU_ITEM_PASTE_LS_PROPERTY = "Mnemonic.MenuItemPaste";

	public static final String MNEMONIC_MENU_ITEM_PDA_EXPORT_LS_PROPERTY = "Mnemonic.MenuItemPDAExport";

	public static final String MNEMONIC_MENU_ITEM_PREFERENCES_LS_PROPERTY = "Mnemonic.MenuItemPreferences";

	public static final String MNEMONIC_MENU_ITEM_PRINT_LS_PROPERTY = "Mnemonic.MenuItemPrint";

	public static final String MNEMONIC_MENU_ITEM_PREVIEW_LS_PROPERTY = "Mnemonic.MenuItemPreview";

	public static final String MNEMONIC_MENU_ITEM_REFRESH_LS_PROPERTY = "Mnemonic.MenuItemRefresh";

	public static final String MNEMONIC_MENU_ITEM_REMOVE_FIELD_LS_PROPERTY = "Mnemonic.MenuItemRemoveField";

	public static final String MNEMONIC_MENU_ITEM_REMOVE_LOGOTYPE_LS_PROPERTY = "Mnemonic.MenuItemRemoveLogotype";

	public static final String MNEMONIC_MENU_ITEM_REMOVE_PATIENT_LS_PROPERTY = "Mnemonic.MenuItemRemovePatient";

	public static final String MNEMONIC_MENU_ITEM_REMOVE_SECTION_LS_PROPERTY = "Mnemonic.MenuItemRemoveSection";

	public static final String MNEMONIC_MENU_ITEM_REMOVE_VALUE_LS_PROPERTY = "Mnemonic.MenuItemRemoveValue";

	public static final String MNEMONIC_MENU_ITEM_RENAME_SECTION_LS_PROPERTY = "Mnemonic.MenuItemRenameSection";

	public static final String MNEMONIC_MENU_ITEM_REMOVE_TAB_LS_PROPERTY = "Mnemonic.MenuItemRemoveTab";

	public static final String MNEMONIC_MENU_ITEM_RIGHT_LS_PROPERTY = "Mnemonic.MenuItemRight";

	public static final String MNEMONIC_MENU_ITEM_SANSSERIF_LS_PROPERTY = "Mnemonic.MenuItemSansSerif";

	public static final String MNEMONIC_MENU_ITEM_SAVE_AS_HTML_LS_PROPERTY = "Mnemonic.MenuItemSaveAsHTML";

	public static final String MNEMONIC_MENU_ITEM_SAVE_LS_PROPERTY = "Mnemonic.MenuItemSave";

	public static final String MNEMONIC_MENU_ITEM_SAVE_AND_CLOSE_LS_PROPERTY = "Mnemonic.SaveAndClose";
	
	public static final String MNEMONIC_MENU_ITEM_SAVE_AS_LS_PROPERTY = "Mnemonic.MenuItemSaveAs";

	public static final String MNEMONIC_MENU_ITEM_SAVE_AS_RTF_LS_PROPERTY = "Mnemonic.MenuItemSaveAsRTF";
	
	public static final String MNEMONIC_MENU_ITEM_SAVE_AS_XML_LS_PROPERTY = "Mnemonic.MenuItemSaveAsXML";

	public static final String MNEMONIC_MENU_ITEM_SAVE_TEMPLATE_AS_LS_PROPERTY = "Mnemonic.MenuItemSaveTemplateAs";

	public static final String MNEMONIC_MENU_ITEM_SAVE_TEMPLATE_LS_PROPERTY = "Mnemonic.MenuItemSaveTemplate";

	public static final String MNEMONIC_MENU_ITEM_SAVE_TRANSLATOR_AS_LS_PROPERTY = "Mnemonic.MenuItemSaveTranslatorAs";

	public static final String MNEMONIC_MENU_ITEM_SAVE_TRANSLATOR_LS_PROPERTY = "Mnemonic.MenuItemSaveTranslator";
	
	public static final String MNEMONIC_MENU_ITEM_SELECT_ALL_LS_PROPERTY = "Mnemonic.MenuItemSelectAll";

	public static final String MNEMONIC_MENU_ITEM_SERIF_LS_PROPERTY = "Mnemonic.MenuItemSerif";

	public static final String MNEMONIC_MENU_ITEM_SET_PAGE_VIEW_LS_PROPERTY = "Mnemonic.MenuItemSetPage";

	public static final String MNEMONIC_MENU_ITEM_SET_PANE_VIEW_LS_PROPERTY = "Mnemonic.MenuItemSetPane";
	
	public static final String MNEMONIC_MENU_ITEM_SHOW_GRAPH_LS_PROPERTY = "Mnemonic.MenuItemShowGraph";

	public static final String MNEMONIC_MENU_ITEM_STRIKETHROUGH_LS_PROPERTY = "Mnemonic.MenuItemStrikethrough";

	public static final String MNEMONIC_MENU_ITEM_SUBSCRIPT_LS_PROPERTY = "Mnemonic.MenuItemSubscript";

	public static final String MNEMONIC_MENU_ITEM_SUPERSCRIPT_LS_PROPERTY = "Mnemonic.MenuItemSuperscript";

	public static final String MNEMONIC_MENU_ITEM_UNDERLINE_LS_PROPERTY = "Mnemonic.MenuItemUnderline";

	public static final String MNEMONIC_MENU_ITEM_UPWARDS_LS_PROPERTY = "Mnemonic.MenuItemUpwards";

	public static final String MNEMONIC_MENU_SIZE_LS_PROPERTY = "Mnemonic.MenuSize";

	public static final String MNEMONIC_MENU_STYLE_LS_PROPERTY = "Mnemonic.MenuStyle";

	public static final String MNEMONIC_MENU_TABS_LS_PROPERTY = "Mnemonic.MenuTabs";

	public static final String MNEMONIC_MENU_VIEW_LS_PROPERTY = "Mnemonic.MenuView";

	/**************************************** END MENU ***************************************/





	/***************************************** OTHER *****************************************/

	public static final String OTHER_ABOUT_LS_PROPERTY = "Other.About";

	public static final String OTHER_ABOUT_FORMEDITOR_TEXT_LS_PROPERTY = "Other.AboutFormEditorText";

	public static final String OTHER_ABOUT_MEDIMAGER_TEXT_LS_PROPERTY = "Other.AboutMedImagerText";

	public static final String OTHER_ABOUT_MEDRECORDS_TEXT_LS_PROPERTY = "Other.AboutMedRecordsText";

	public static final String OTHER_ABOUT_MEDSERVER_TEXT_LS_PROPERTY = "Other.AboutMedServerText";

	public static final String OTHER_ABOUT_MEDSUMMARY_TEXT_LS_PROPERTY = "Other.AboutMedSummaryText";

	public static final String OTHER_ABOUT_SUMMARYCREATOR_TEXT_LS_PROPERTY = "Other.AboutSummaryCreatorText";

	public static final String OTHER_ABOUT_TERMEDIT_TEXT_LS_PROPERTY = "Other.AboutTermEditText";

	public static final String OTHER_ABOUT_MEDVIEW_VENTURE_TEXT_LS_PROPERTY = "Other.AboutMedViewVentureText";

	public static final String OTHER_ACTIVATED_LS_PROPERTY = "Other.Activated";

	public static final String OTHER_BROWSE_LS_PROPERTY = "Other.Browse";

	public static final String OTHER_BUILDING_PID_CACHE_LS_PROPERTY = "Other.BuildingPIDCache";

	public static final String OTHER_CHECKING_TERM_VALIDITY_LS_PROPERTY = "Other.CheckingTermValidity";

	public static final String OTHER_CREATION_TIME_LS_PROPTERY = "Other.CreationTime";

	public static final String OTHER_DEACTIVATED_LS_PROPERTY = "Other.Deactivated";

	public static final String OTHER_INITIALIZING_COMPONENTS_LS_PROPERTY = "Other.InitializingComponents";

	public static final String OTHER_INITIALIZING_LIST_OF_TERMS_LS_PROPERTY = "Other.InitializingListOfTerms";

	public static final String OTHER_INITIALIZING_SETTINGS_LS_PROPERTY = "Other.InitializingSettings";

	public static final String OTHER_INITIALIZING_TOOLBARS_AND_MENUS_LS_PROPERTY = "Other.InitializingToolbarsAndMenus";

	public static final String OTHER_INVALID_TYPE_LS_PROPERTY = "Other.InvalidType";

	public static final String OTHER_INTERVAL_TRANSLATION_DESCRIPTION_LS_PROPERTY = "Other.IntervalTranslationDescription";

	public static final String OTHER_INTERVAL_VALUE_DESCRIPTION_LS_PROPERTY = "Other.IntervalValueDescription";

	public static final String OTHER_LANGUAGE_CHANGE_WILL_TAKE_EFFECT_WHEN_RESTARTED_LS_PROPERTY = "Other.LanguageChangeWillTakeEffectWhenRestarted";

	public static final String OTHER_LOADING_MEDRECORDS_TEXT_LS_PROPERTY = "Other.LoadingMedRecordsText";

	public static final String OTHER_LOAD_LS_PROPERTY = "Other.Load";
	
	public static final String OTHER_OEHR_ABOUT_ARCHETYPE_WORKBENCH_TEXT_LS_PROPERTY = "Other.OEHRAboutArchetypeWorkbenchText";
	
	public static final String OTHER_OEHR_MESSAGE_CHECK_MESSAGES_PANE_LS_PROPERTY = "Other.OEHCheckMessagesPane";
	
	public static final String OTHER_OEHR_MESSAGE_PARSED_AND_VALIDATED_OK_LS_PROPERTY = "Other.OEHRMessageParsedAndValidatedOK";

	public static final String OTHER_OEHR_RUBRIC_FOR_LS_PROPERTY = "Other.OEHRRubricFor";
	
	public static final String OTHER_OEHR_USE_LS_PROPERTY = "Other.OEHRUse";

	public static final String OTHER_OPEN_LS_PROPERTY = "Other.Open";

	public static final String OTHER_MULTIPLE_TRANSLATION_DESCRIPTION_LS_PROPERTY = "Other.MultipleTranslationDescription";

	public static final String OTHER_MULTIPLE_VALUE_DESCRIPTION_LS_PROPERTY = "Other.MultipleValueDescription";

	public static final String OTHER_NEW_LS_PROPERTY = "Other.New";

	public static final String OTHER_NEW_TRANSLATOR_LS_PROPERTY = "Other.NewTranslator";

	public static final String OTHER_NOT_SET_LS_PROPERTY = "Other.NotSet";

	public static final String OTHER_OBTAINING_PATIENTS_LS_PROPERTY = "Other.ObtainingPatients";

	public static final String OTHER_SETTINGS_TAKE_EFFECT_NEXT_TIME_LS_PROPERTY = "Other.SettingsTakeEffectNextTime";

	public static final String OTHER_STATUS_LS_PROPERTY = "Other.Status";

	public static final String OTHER_SURROUNDING_TEMPLATE_LOGO_OBSERVANDUM_LS_PROPERTY = "Other.SurroundingTemplateLogoObservandum";

	public static final String OTHER_PATIENT_INFORMATION_LS_PROPERTY = "Other.PatientInformation";

	public static final String OTHER_PREVIEW_DESCRIPTION_LS_PROPERTY = "Other.PreviewDescription";

	public static final String OTHER_PRINT_DATE_LS_PROPERTY = "Other.PrintDate";

	public static final String OTHER_PRINT_PAGE_LS_PROPERTY = "Other.PrintPage";

	public static final String OTHER_PRINT_OF_LS_PROPERTY = "Other.PrintOf";

	public static final String OTHER_PROGRESS_ADD_IMAGES_LS_PROPERTY = "Other.ProgressAddImages";

	public static final String OTHER_REGULAR_TRANSLATION_DESCRIPTION_LS_PROPERTY = "Other.RegularTranslationDescription";

	public static final String OTHER_REGULAR_VALUE_DESCRIPTION_LS_PROPERTY = "Other.RegularValueDescription";

	public static final String OTHER_RETRIEVING_TREE_FILE_LISTING_LS_PROPERTY = "Other.RetrievingTreeFileListing";

	public static final String OTHER_SAVE_LS_PROPERTY = "Other.Save";

	public static final String OTHER_SAVE_AS_LS_PROPERTY = "Other.SaveAs";

	public static final String OTHER_UNKNOWN = "Other.Unknown";

	public static final String OTHER_VERSION_LS_PROPERTY = "Other.Version";

	/*************************************** END OTHER ***************************************/





	/*************************************** QUESTIONS ***************************************/

	public static final String QUESTION_CONFIRM_DELETE_FILE_LS_PROPERTY = "Question.ConfirmDeleteFile";

	public static final String QUESTION_SHOULD_ADD_VALUE_LS_PROPERTY = "Question.ShouldAddValue";

	public static final String QUESTION_SHOULD_REMOVE_FIELD_LS_PROPERTY = "Question.ShouldRemoveField";

	public static final String QUESTION_SHOULD_REMOVE_SECTION_LS_PROPERTY = "Question.ShouldRemoveSection";

	public static final String QUESTION_SHOULD_REMOVE_TAB_LS_PROPERTY = "Question.ShouldRemoveTab";

	public static final String QUESTION_SHOULD_REMOVE_VALUE_LS_PROPERTY = "Question.ShouldRemoveValue";

	public static final String QUESTION_SHOULD_ASSOCIATE_TRANSLATOR_LS_PROPERTY = "Question.ShouldAssociateTranslator";

	public static final String QUESTION_LOAD_ASSOCIATED_TRANSLATOR_LS_PROPERTY = "Question.LoadAssociatedTranslator";

	/************************************* END QUESTIONS **************************************
	   /*************************************** RADIO BUTTONS ***************************************/

	 public static final String RADIO_BUTTON_BLACK_LS_PROPERTY = "RadioButton.Black";

	public static final String RADIO_BUTTON_USE_BUILT_IN_ADDRESS_LS_PROPERTY = "RadioButton.UseBuiltInAddress";

	public static final String RADIO_BUTTON_USE_CUSTOM_ADDRESS_LS_PROPERTY = "RadioButton.UseCustomAddress";

	public static final String RADIO_BUTTON_USE_BUILT_IN_LOGOTYPE_LS_PROPERTY = "RadioButton.UseBuiltInLogotype";

	public static final String RADIO_BUTTON_USE_CUSTOM_LOGOTYPE_LS_PROPERTY = "RadioButton.UseCustomLogotype";

	public static final String RADIO_BUTTON_USE_LOCAL_DATAHANDLING_LS_PROPERTY = "RadioButton.UseLocalDataHandling";

	public static final String RADIO_BUTTON_USE_REMOTE_DATAHANDLING_LS_PROPERTY = "RadioButton.UseRemoteDataHandling";

	public static final String RADIO_BUTTON_SHADOW_LS_PROPERTY = "RadioButton.Shadow";

	/************************************* END RADIO BUTTONS *************************************/





	/****************************************** TABS *****************************************/

	public static final String TAB_COMPONENT_PACKAGES_LS_PROPERTY = "Tab.ComponentPackages";

	public static final String TAB_COMPONENT_PACKAGES_DESCRIPTION_LS_PROPERTY = "Tab.ComponentPackagesDescription";

	public static final String TAB_DATA_LOCATIONS_LS_PROPERTY = "Tab.DataLocations";

	public static final String TAB_DATA_LOCATIONS_DESCRIPTION_LS_PROPERTY = "Tab.DataLocationsDescription";

	public static final String TAB_EXAMINATION_DATA_LS_PROPERTY = "Tab.ExaminationData";

	public static final String TAB_GLOBAL_DESCRIPTION_LS_PROPERTY = "Tab.GlobalDescription";

	public static final String TAB_GLOBAL_LS_PROPERTY = "Tab.Global";

	public static final String TAB_INSERTED_IMAGES_IN_TEXT_LS_PROPERTY = "Tab.InsertedImagesInText";

	public static final String TAB_INSERTED_IMAGES_IN_TEXT_DESCRIPTION_LS_PROPERTY = "Tab.InsertedImagesInTextDescription";

	public static final String TAB_INTERFACECREATOR_DESCRIPTION_LS_PROPERTY = "Tab.InterfaceCreatorDescription";

	public static final String TAB_INTERFACECREATOR_LS_PROPERTY = "Tab.InterfaceCreator";

	public static final String TAB_MEDIMAGER_JOURNAL_DESCRIPTION_LS_PROPERTY = "Tab.MedImagerJournalDescription";

	public static final String TAB_MEDIMAGER_JOURNAL_LS_PROPERTY = "Tab.MedImagerJournal";

	public static final String TAB_MEDIMAGER_SEARCH_DESCRIPTION_LS_PROPERTY = "Tab.MedImagerSearchDescription";

	public static final String TAB_MEDIMAGER_SEARCH_LS_PROPERTY = "Tab.MedImagerSearch";

	public static final String TAB_MEDRECORDS_DESCRIPTION_LS_PROPERTY = "Tab.MedRecordsDescription";

	public static final String TAB_MEDRECORDS_LS_PROPERTY = "Tab.MedRecords";

	public static final String TAB_MEDRECORDS_EXPERT_DESCRIPTION_LS_PROPERTY = "Tab.MedRecordsExpertDescription";

	public static final String TAB_MEDRECORDS_EXPERT_LS_PROPERTY = "Tab.MedRecordsExpert";

	public static final String TAB_MEDRECORDS_INPUT_DESCRIPTION_LS_PROPERTY = "Tab.MedrecordsInputsDescription";

	public static final String TAB_MEDRECORDS_INPUT_LS_PROPERTY = "Tab.MedrecordsInputs";

	public static final String TAB_MEDRECORDS_VISUAL_DESCRIPTION_LS_PROPERTY = "Tab.MedRecordsVisualDescription";

	public static final String TAB_MEDRECORDS_VISUAL_LS_PROPERTY = "Tab.MedRecordsVisual";

	public static final String TAB_MEDSERVER_SERVER_LS_PROPERTY = "Tab.MedServerServer";

	public static final String TAB_MEDSERVER_SERVER_DESCRIPTION_LS_PROPERTY = "Tab.MedServerServerDescription";

	public static final String TAB_MEDSERVER_VISUAL_LS_PROPERTY = "Tab.MedServerVisual";

	public static final String TAB_MEDSERVER_VISUAL_DESCRIPTION_LS_PROPERTY = "Tab.MedServerVisualDescription";

	public static final String TAB_MEDSUMMARY_DATAHANDLING_LS_PROPERTY = "Tab.MedSummaryDataHandling";

	public static final String TAB_MEDSUMMARY_DATAHANDLING_DESCRIPTION_LS_PROPERTY = "Tab.MedSummaryDataHandlingDescription";

	public static final String TAB_MEDSUMMARY_DESCRIPTION_LS_PROPERTY = "Tab.MedSummaryDescription";

	public static final String TAB_MEDSUMMARY_EXPERT_LS_PROPERTY = "Tab.MedSummaryExpert";

	public static final String TAB_MEDSUMMARY_EXPERT_DESCRIPTION_LS_PROPERTY = "Tab.MedSummaryExpertDescription";

	public static final String TAB_MEDSUMMARY_LS_PROPERTY = "Tab.MedSummary";

	public static final String TAB_MEDSUMMARY_JOURNAL_LS_PROPERTY = "Tab.MedSummaryJournal";

	public static final String TAB_MEDSUMMARY_JOURNAL_DESCRIPTION_LS_PROPERTY = "Tab.MedSummaryJournalDescription";

	public static final String TAB_MEDSUMMARY_VISUAL_LS_PROPERTY = "Tab.MedSummaryVisual";

	public static final String TAB_MEDSUMMARY_VISUAL_DESCRIPTION_LS_PROPERTY = "Tab.MedSummaryVisualDescription";

    public static final String TAB_MUCOS_LS_PROPERTY = "Tab.Mucos";

	public static final String TAB_PLAQUE_INDEX_LS_PROPERTY = "Tab.PlaqueIndex";
	
	public static final String TAB_OEHR_APATH_QUERY_LS_PROPERTY = "Tab.OEHRAPathQuery";

	public static final String TAB_OEHR_ARCHETYPES_LS_PROPERTY = "Tab.OEHRArchetypes";
	
	public static final String TAB_OEHR_CONSTRAINT_DEFINITIONS_LS_PROPERTY = "Tab.OEHRConstraintDefinitions";

	public static final String TAB_OEHR_MESSAGES_LS_PROPERTY = "Tab.OEHRMessages";
	
	public static final String TAB_OEHR_REPOSITORY_LS_PROPERTY = "Tab.OEHRRepository";

	public static final String TAB_OEHR_REPOSITORY_DESCRIPTION_LS_PROPERTY = "Tab.OEHRRepositoryDescription";

	public static final String TAB_OEHR_TEMPLATES_LS_PROPERTY = "Tab.OEHRTemplates";
	
	public static final String TAB_OEHR_TERM_DEFINITIONS_LS_PROPERTY = "Tab.OEHRTermDefinitions";
	
	public static final String TAB_OEHR_TREE_LS_PROPERTY = "Tab.OEHRTree";

	public static final String TAB_OEHR_VISUAL_LS_PROPERTY = "Tab.OEHRVisual";

	public static final String TAB_OEHR_VISUAL_DESCRIPTION_LS_PROPERTY = "Tab.OEHRVisualDescription";

	public static final String TAB_SERVER_LS_PROPERTY = "Tab.Server";

	public static final String TAB_SUMMARYCREATOR_DESCRIPTION_LS_PROPERTY = "Tab.SummaryCreatorDescription";

	public static final String TAB_SUMMARYCREATOR_LS_PROPERTY = "Tab.SummaryCreator";

	public static final String TAB_SUMMARYCREATOR_TEMPLATE_LS_PROPERTY = "Tab.SummaryCreatorTemplate";

	public static final String TAB_SUMMARYCREATOR_TEMPLATE_DESCRIPTION_LS_PROPERTY = "Tab.SummaryCreatorTemplateDescription";

	public static final String TAB_SUMMARYCREATOR_VISUAL_LS_PROPERTY = "Tab.SummaryCreatorVisual";

	public static final String TAB_SUMMARYCREATOR_VISUAL_DESCRIPTION_LS_PROPERTY = "Tab.SummaryCreatorVisualDescription";

	public static final String TAB_TERMS_LS_PROPERTY = "Tab.Terms";

	/**************************************** END TABS ***************************************/





	/***************************************** TITLES ****************************************/

	public static final String TITLE_ABOUT_FORMEDITOR_LS_PROPERTY = "Title.AboutFormEditor";

	public static final String TITLE_ABOUT_MEDIMAGER_LS_PROPERTY = "Title.AboutMedImager";

	public static final String TITLE_ABOUT_MEDRECORDS_LS_PROPERTY = "Title.AboutMedRecords";

	public static final String TITLE_ABOUT_MEDSERVER_LS_PROPERTY = "Title.AboutMedServer";

	public static final String TITLE_ABOUT_MEDSUMMARY_LS_PROPERTY = "Title.AboutMedSummary";

	public static final String TITLE_ABOUT_SUMMARYCREATOR_LS_PROPERTY = "Title.AboutSummaryCreator";

	public static final String TITLE_ABOUT_TERMEDIT_LS_PROPERTY = "Title.AboutTermEdit";

	public static final String TITLE_ADD_NEW_INTERVAL_LS_PROPERTY = "Title.AddNewInterval";

	public static final String TITLE_ADD_NEW_SECTION_LS_PROPERTY = "Title.AddNewSection";

	public static final String TITLE_ADD_NEW_VALUE_LS_PROPERTY = "Title.AddNewValue";

	public static final String TITLE_ADD_PACKAGE_LS_PROPERTY = "Title.AddPackage";

	public static final String TITLE_ALL_PACKAGES_LS_PROPERTY = "Title.AllPackages";

	public static final String TITLE_AVAILABLE_PATIENTS_LS_PROPERTY = "Title.AvailablePatients";

	public static final String TITLE_AVAILABLE_TERMS_LS_PROPERTY = "Title.AvailableTerms";

	public static final String TITLE_CHANGE_EXAMINATION_LOCATION_LS_PROPERTY = "Title.ChangeExaminationLocation";

	public static final String TITLE_CHANGE_NAME_LS_PROPERTY = "Title.ChangeName";

	public static final String TITLE_CHANGE_PATIENT_LOCATION_LS_PROPERTY = "Title.ChangePatientLocation";

	public static final String TITLE_CHANGE_PREVIEW_PCODE_LS_PROPERTY = "Title.ChangePreviewPCode";

	public static final String TITLE_CHANGE_PROTOCOL_LS_PROPERTY = "Title.ChangeProtocol";

	public static final String TITLE_CHANGE_TERM_DEFINITIONS_LS_PROPERTY = "Title.ChangeTermDefinitions";

	public static final String TITLE_CHANGE_TERM_VALUES_LS_PROPERTY = "Title.ChangeTermValues";

	public static final String TITLE_CHANGE_TRANSLATOR_LOCATION_LS_PROPERTY = "Title.ChangeTranslatorLocation";

	public static final String TITLE_CHANGE_TRANSLATOR_LS_PROPERTY = "Title.ChangeTranslator";

	public static final String TITLE_CHOOSE_DIRECTORY_LS_PROPERTY = "Title.ChooseDirectory";

	public static final String TITLE_CHOOSE_FILE_LS_PROPERTY = "Title.ChooseFile";

	public static final String TITLE_CHOOSE_IMAGE_LS_PROPERTY = "Title.ChooseImage";

	public static final String TITLE_COLOR_CHOOSER_LS_PROPERTY = "Title.ColorChooser";

	public static final String TITLE_CLOSE_LS_PROPERTY = "Title.Close";

	public static final String TITLE_CLOSE_PROTOCOL_LS_PROPERTY = "Title.CloseProtocol";

	public static final String TITLE_DISTRIBUTED_SETTINGS_LS_PROPERTY = "Title.DistributedSettings";

	public static final String TITLE_EDIT_PACKAGE_LS_PROPERTY = "Title.EditPackage";

	public static final String TITLE_ERROR_LS_PROPERTY = "Title.Error";

	public static final String TITLE_EXIT_MEDSUMMARY_LS_PROPERTY = "Title.ExitMedSummary";

	public static final String TITLE_EXIT_SUMMARYCREATOR_LS_PROPERTY = "Title.ExitSummaryCreator";

	public static final String TITLE_FORMEDITOR_UNTITLED_LS_PROPERTY = "Title.FormEditorUntitled";

	public static final String TITLE_GENERATOR_TOOLBAR_LS_PROPERTY = "Title.GeneratorToolBar";

	public static final String TITLE_GRAPH_VIEWER_LS_PROPERTY = "Title.GraphViewer";
	
	public static final String TITLE_GRAPHICAL_TOOLBAR_LS_PROPERTY = "Title.GraphicalToolBar";

	public static final String TITLE_INCLUDED_PACKAGES_LS_PROPERTY = "Title.IncludedPackages";

	public static final String TITLE_IMPORT_DATA_LS_PROPERTY = "Title.ImportData";

	public static final String TITLE_IMPORT_IMAGE_LS_PROPERTY = "Title.ImportImage";

	public static final String TITLE_INFO_LS_PROPERTY = "Title.Info";

	public static final String TITLE_INTERVAL_EXISTED_LS_PROPERTY = "Title.IntervalExisted";
	
	public static final String TITLE_INVALID_INPUT_LS_PROPERTY = "Title.InvalidInput";

	public static final String TITLE_LOAD_GRAPH_TEMPLATE_LS_PROPERTY = "Title.LoadGraphTemplate";

	public static final String TITLE_LOAD_INPUT_FORM_LS_PROPERTY = "Title.LoadInputForm";

	public static final String TITLE_LOAD_MVD_LS_PROPERTY = "Title.LoadMVD";

	public static final String TITLE_LOAD_TEMPLATE_LS_PROPERTY = "Title.LoadTemplate";

	public static final String TITLE_LOCAL_SETTINGS_LS_PROPERTY = "Title.LocalSettings";

	public static final String TITLE_MEDIMAGER_SETTINGS_LS_PROPERTY = "Title.MedImagerSettings";

	public static final String TITLE_MEDRECORDS_WHEN_SAVED_LS_PROPERTY = "Title.MedRecordsWhenSaved";

	public static final String TITLE_MEDSERVER_SETTINGS_LS_PROPERTY = "Title.MedServerSettings";

	public static final String TITLE_MEDSUMMARY_SETTINGS_LS_PROPERTY = "Title.MedSummarySettings";

	public static final String TITLE_NEW_EXAMINATION_LS_PROPERTY = "Title.NewExamination";

	public static final String TITLE_NEW_FIELD_DIALOG_LS_PROPERTY = "Title.NewFieldDialog";

	public static final String TITLE_NEW_TAB_DIALOG_LS_PROPERTY = "Title.NewTabDialog";
	
	public static final String TITLE_NEW_TEMPLATE_LS_PROPERTY = "Title.NewTemplate";

	public static final String TITLE_NEW_TRANSLATOR_DIALOG_LS_PROPERTY = "Title.NewTranslatorDialog";

	public static final String TITLE_NEW_VALUE_ALERT_LS_PROPERTY = "Title.NewValueAlert";

	public static final String TITLE_NO_FIELD_SELECTED_LS_PROPERTY = "Title.NoFieldSelected";

	public static final String TITLE_NO_TAB_SELECTED_LS_PROPERTY = "Title.NoTabSelected";

	public static final String TITLE_OBTAINING_INFORMATION_LS_PROPERTY = "Title.ObtainingInformation";
	
	public static final String TITLE_OEHR_ABOUT_ARCHETYPE_WORKBENCH_LS_PROPERTY = "Title.OEHRAboutArchetypeWorkbench";

	public static final String TITLE_OPEN_TEMPLATE_LS_PROPERTY = "Title.OpenTemplate";

	public static final String TITLE_PATIENT_TOOLBAR_LS_PROPERTY = "Title.PatientToolbar";

	public static final String TITLE_PDADIALOG_LS_PROPERTY = "Title.PDADialog";

	public static final String TITLE_PDAEXPORTDIALOG_LS_PROPERTY = "Title.PDAExportDialog";

	public static final String TITLE_PDA_SETTINGS_LS_PROPERTY = "Title.PDASettings";

	public static final String TITLE_PREFERENCES_LS_PROPERTY = "Title.Preferences";

	public static final String TITLE_PREVIEW_LS_PROPERTY = "Title.Preview";

	public static final String TITLE_PREVIEW_TEMPLATE_LS_PROPERTY = "Title.PreviewTemplate";

	public static final String TITLE_QUESTION_LS_PROPERTY = "Title.Question";

	public static final String TITLE_RENAME_SECTION_LS_PROPERTY = "Title.RenameSection";

	public static final String TITLE_SAVE_LS_PROPERTY = "Title.Save";

	public static final String TITLE_SAVE_HTML_LS_PROPERTY = "Title.SaveHTML";

	public static final String TITLE_SAVE_RTF_LS_PROPERTY = "Title.SaveRTF";

	public static final String TITLE_SAVE_TEMPLATE_AS_LS_PROPERTY = "Title.SaveTemplateAs";

	public static final String TITLE_SAVE_TRANSLATOR_AS_LS_PROPERTY = "Title.SaveTranslatorAs";

	public static final String TITLE_SELECTED_PATIENT_LS_PROPERTY = "Title.SelectedPatient";

	public static final String TITLE_SELECTED_TERM_LS_PROPERTY = "Title.SelectedTerm";

	public static final String TITLE_SERVER_TOOLBAR_LS_PROPERTY = "Title.ServerToolbar";

	public static final String TITLE_SERVER_TRAFFIC_LS_PROPERTY = "Title.ServerTraffic";

	public static final String TITLE_SETTINGS_LS_PROPERTY = "Title.Settings";

	public static final String TITLE_SHOULD_SAVE_TEMPLATE_LS_PROPERTY = "Title.ShouldSaveTemplate";

	public static final String TITLE_SHOULD_SAVE_TRANSLATOR_LS_PROPERTY = "Title.ShouldSaveTranslator";

	public static final String TITLE_SUMMARYCREATOR_SETTINGS_LS_PROPERTY = "Title.SummaryCreatorSettings";

	public static final String TITLE_TEMPLATE_CHOOSER_TOOLBAR_LS_PROPERTY = "Title.TemplateChooserToolbar";

	public static final String TITLE_TEMPLATE_TOOLBAR_LS_PROPERTY = "Title.TemplateToolbar";

	public static final String TITLE_TERM_EDITOR_LS_PROPERTY = "Title.TermEditor";

	public static final String TITLE_TEXT_TOOLBAR_LS_PROPERTY = "Title.TextToolBar";

	public static final String TITLE_TRANSLATOR_LS_PROPERTY = "Title.Translator";

	public static final String TITLE_VALUE_EXISTED_LS_PROPERTY = "Title.ValueExisted";

	/*************************************** END TITLES **************************************/

	/***************************************** TOOL TIPS ****************************************/

	public static final String TOOLTIP_IMPORT_DATA_LS_PROPERTY = "Tooltip.ImportData";

	public static final String TOOLTIP_SHOW_GRAPH_LS_PROPERTY = "Tooltip.ShowGraph";

	public static final String TOOLTIP_NEW_EXAMINATION_LS_PROPERTY = "Tooltip.NewExamination";

	public static final String TOOLTIP_NEW_FIELD_LS_PROPERTY = "Tooltip.NewField";

	public static final String TOOLTIP_NEW_FORM_LS_PROPERTY = "Tooltip.NewForm";

	public static final String TOOLTIP_NEW_TAB_LS_PROPERTY = "Tooltip.NewTab";

	public static final String TOOLTIP_OPEN_FORM_LS_PROPERTY = "Tooltip.OpenForm";

	public static final String TOOLTIP_PREVIEW_LS_PROPERTY = "Tooltip.Preview";

	public static final String TOOLTIP_PDA_LS_PROPERTY = "Tooltip.PDA";

	public static final String TOOLTIP_REMOVE_VALUE_LS_PROPERTY = "Tooltip.RemoveValue";

	public static final String TOOLTIP_SAVE_EXAMINATION_LS_PROPERTY = "Tooltip.SaveExamination";

	public static final String TOOLTIP_SAVE_AND_CLOSE_EXAMINATION_LS_PROPERTY = "Tooltip.SaveAndCloseExamination";

	public static final String TOOLTIP_SAVE_FORM_LS_PROPERTY = "Tooltip.SaveForm";


	/*************************************** END TOOL TIPS **************************************/



	/**************************************** UNIVERSAL **************************************/

	/************************************** END UNIVERSAL ************************************/

}
