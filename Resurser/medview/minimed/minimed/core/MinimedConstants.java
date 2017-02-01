package minimed.core;


import java.text.*;

/**
 * An interface for constants used by MiniMed.
 */
public interface MinimedConstants {
	
	/* MiniMed settings file */
	public static final String SETTINGS_FILE = "/SD Card/Minimed/Settings/settings.conf";
	//public static final String SETTINGS_FILE = "settings.conf";
	
	/* MiniMed language file */
	public static final String LANGUAGE_FILE = "/SD Card/Minimed/Lang/strings.txt";
	//public static final String LANGUAGE_FILE = "lang/strings.txt";
	
	/* Input model types */
    public static final int FIELD_TYPE_SINGLE = 1;
    public static final int FIELD_TYPE_MULTI = 2;
    public static final int FIELD_TYPE_NOTE = 3;
    public static final int FIELD_TYPE_INTERVAL = 4;
    public static final int FIELD_TYPE_IDENTIFICATION = 5;
    public static final int FIELD_TYPE_QUESTION = 6;
    public static final int FIELD_TYPE_TEXT = 7;	// Added by Andreas N. (Minimed)
    public static final int FIELD_TYPE_VAS = 8;		// Added by Andreas N. (Minimed)
    public static final int FIELD_TYPE_FIRST = 1;
    public static final int FIELD_TYPE_LAST = 8;

    /* Misc constants */
	public static final String LATIN_ENC = "ISO-8859-1";
	public static final String CONCRETE_ID_TERM_NAME = "Konkret_identifikation";
	public static final String DATE_TERM_NAME = "Datum";
	public final static DateFormat TREEFILENAME_DATE_FORMAT = new SimpleDateFormat("yyMMddHHmmss");
	public final static DateFormat TREEFILE_DATUM_FIELD_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final String MVD_TREE_FILE_ENDER = ".tree";
	public static final String PCODE_TERM_NAME = "P-code";
	public static final String PID_TERM_NAME = "PID";
	public static final String NAME_TERM_NAME = "Ref-name";
	public static final String BORN_TERM_NAME = "Born";
	static final String SAVEFILE_PREFIX = "treeFileNo";

}