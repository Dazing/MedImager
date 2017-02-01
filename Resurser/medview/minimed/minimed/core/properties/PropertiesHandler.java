package minimed.core.properties;


import java.io.FileNotFoundException;
import java.io.IOException;
import minimed.core.MinimedConstants;
import ch.ubique.inieditor.IniEditor;

/**
 * A class for handling settings/properties throughout the MiniMed application.
 * Settings are saved in the plain-text file <i>settings.conf</i> and language
 * data is stored in <i>lang/strings.txt</i>.
 */
public class PropertiesHandler {
	private String termValuesPath = "";
	private String termDefinitionsPath = "";
	private String formTemplatePath = "";
	private String examSaveDir = "";
	private String examDatabaseDir = "";
	private String helpFilePath = "";
	private String showMucos = "";
	private String minimedRevision = "";
	private String[] terms ;
	private String inboxDir = "";       // Probably never used...
	private String lang;                // Language in use
	private String[] langs;             // Available languages

	private IniEditor settings;
	private IniEditor language;

	/**
	 * Reads all properties from the settings file and loads all needed strings
	 * from the language file for the active language.
	 * 
	 * @param pSettings An instance of IniEditor for accessing the settings file
	 * @param pLanguage An instance of IniEditor for accessing the language file
	 */
	public PropertiesHandler(IniEditor pSettings, IniEditor pLanguage) {
		this.settings = pSettings;
		this.language = pLanguage;
		
		/* Load all settings */
		termValuesPath = settings.get("TermValuesFile", "path");
		termDefinitionsPath = settings.get("TermDefinitionsFile", "path");
		formTemplatePath = settings.get("FormTemplateFile", "path");
		examSaveDir = settings.get("ExamSaveDir", "path");
		examDatabaseDir = settings.get("ExamDatabaseDir", "path");
		helpFilePath = settings.get("HelpFile", "path");
		showMucos = settings.get("Mucos", "show");
		minimedRevision = settings.get("Minimed", "revision");
		lang = settings.get("Minimed", "i18n");
		getTermsFromIni();
		
		/* Load language strings */
		String [] varList;
		varList = (String[])settings.optionNames("Languages").toArray(new String[0]);
		langs = new String[varList.length];
		
		for ( int i = 0; i< varList.length ; i++ ) {
			langs[i] = settings.get("Languages", varList[i]);
		}
	}

	/**
	 * Empty constructor to satisfy:
	 *   minimed.core.datahandling.template.PullXmlMedformTemplateReader
	 */
	public PropertiesHandler() {}
	
	/** 
	 * Gets the path to the term values file.
	 * 
	 * @return The path to the term values file
	 */	
	public String getTermValuesPath() {
		return termValuesPath;
	}
	
	/** 
	 * Gets the path to the term definitions file.
	 * 
	 * @return The path to the term definitions file
	 */	
	public String getTermDefinitionsPath() {
		return termDefinitionsPath;
	}
	
	/** 
	 * Gets the path to the form template file.
	 * 
	 * @return The path to the form template file
	 */	
	public String getFormTemplatePath() {
		return formTemplatePath;
	}
	
	/** 
	 * Gets the path to the examination save directory.
	 * 
	 * @return The path to the exam save dir
	 */
	public String getExamSaveDir() {
		return examSaveDir;
	}
	
	/** 
	 * Gets the path to the examination database directory.
	 * 
	 * @return The path to the exam database dir
	 */	
	public String getExamDatabaseDir() {
		return examDatabaseDir;
	}
	
	/** 
	 * Gets all terms manually specified in the settings file.
	 * 
	 * @return terms The terms specified in the settings file
	 */	
	public String[] getTermsFromIni(){
		String [] varList;
		varList = (String[])settings.optionNames("Terms").toArray(new String[0]);
		terms = new String[varList.length];
		for ( int i = 0; i< varList.length ; i++ ) {
			terms[i] = settings.get("Terms", varList[i]);
		}
		return terms;
	}
	
	/** 
	 * Gets the path to the inbox dir.
	 * 
	 * @return The path to inbox dir
	 */	
	public String getInboxDir() {
		return inboxDir;
	}
	
	/** 
	 * Gets the path to the help file.
	 * 
	 * @return The path to the help file
	 */	
	public String getHelpFilePath() {
		return helpFilePath;
	}

	/** 
	 * Gets the setting for whether or not to show the mucos form in the examination view.
	 * 
	 * @return Whether or not the mucos form should be visible in the exam view
	 */	
	public String getShowMucos() {
		return showMucos;
	}
	
	/** 
	 * Gets the revision of MiniMed being used.
	 * 
	 * @return The revision of MiniMed being used
	 */		
	public String getMinimedRevision() {
		return minimedRevision;
	}
	
	/** 
	 * Gets the language currently being used in MiniMed.
	 * 
	 * @return The language currently being used in MiniMed
	 */	
	public String getLanguage() {
		return lang;
	}
	
	/** 
	 * Gets all the languages available in MiniMed.
	 * 
	 * @return All languages available in MiniMed
	 */
	public String[] getAllLanguages() {
		return langs;
	}
	
	/** 
	 * Sets what language to use in MiniMed.
	 * 
	 * @param pLanguage What language to use in MiniMed
	 */	
	public void setLanguage(String pLanguage) {
		lang = pLanguage;
	}
	
	/**
	 * Provides internationalization support for MiniMed. The method
	 * takes an english character string as argument and uses that to
	 * search the MinimedConstants.LANGUAGE_FILE for a matching section.
	 * If a section with the same name (as the string) is found, it looks for
	 * an entry in that section matching the pattern: 
	 *   <language_being_used> = ...
	 * If an entry for the current language can not be found, it falls back on 
	 * the english string and uses that instead. 
	 * 
	 * @param  String  pStr  The string to translate
	 * @return  String  The translated version of pStr
	 */	
	public String i18n(String pStr) {
		return (language.hasOption(pStr, this.lang) ? language.get(pStr, this.lang) : pStr);
	}
	
	/** 
	 * Sets the path to the form template file.
	 * 
	 * @param pPath The path to the form template file
	 */	
	public void setFormTemplatePath(String pPath) {
		formTemplatePath = pPath;
	}
	
	/** 
	 * Sets the path to the term definitions file.
	 * 
	 * @param  pPath The path to the term definitions file
	 */
	public void setTermDefinitionsPath(String pPath) {
		termDefinitionsPath = pPath;
	}
	
	/** 
	 * Sets the path to the term values file.
	 * 
	 * @param pPath The path to the term values file
	 */
	public void setTermValuesPath(String pPath) {
		termValuesPath = pPath;
	}
	
	/** 
	 * Sets the path to the help file.
	 * 
	 * @param pPath The path to the help file
	 */	
	public void setHelpFilePath(String pPath) {
		helpFilePath = pPath;
	}

	/** 
	 * Sets whether to show the mucos form in the examination view.
	 * 
	 * @param pShow Whether or not to show the mucos form
	 */	
	public void setShowMucos(String pShow) {
		showMucos = pShow;
	}
	
	/** 
	 * Saves all settings to disk.
	 * @throws FileNotFoundException, IOException
	 */	
	public void saveProperties() throws FileNotFoundException, IOException {
	
		/* Set all settings */
		settings.set("TermValuesFile", "path", termValuesPath);
		settings.set("TermDefinitionsFile", "path", termDefinitionsPath);
		settings.set("FormTemplateFile", "path", formTemplatePath);
		settings.set("ExamSaveDir", "path", examSaveDir);
		settings.set("ExamDatabaseDir", "path", examDatabaseDir);
		settings.set("HelpFile", "path", helpFilePath);
		settings.set("Mucos", "show", showMucos);
		settings.set("Minimed", "i18n", lang);
		
		/* Set terms */
		for (int i = 0; i < terms.length; i++) {
			settings.set("Terms", "term"+i, terms[i]);
		}
		
		/* Save everything to the settings file */
		settings.save(MinimedConstants.SETTINGS_FILE);
	}
}
