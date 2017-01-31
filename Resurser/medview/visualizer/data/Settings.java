/*
 * Settings.java
 *
 * Created on September 4, 2002, 9:46 AM
 *
 * $Id: Settings.java,v 1.18 2008/08/27 07:11:21 it2aran Exp $
 *
 * $Log: Settings.java,v $
 * Revision 1.18  2008/08/27 07:11:21  it2aran
 * Removed default terms
 *
 * Revision 1.17  2008/08/25 09:23:17  it2aran
 * T4 Server updates so it loads the mvdlocation from the package
 * Visualizer: Chosen terms doesn't have to be sorted alpabetically
 * Visualizer: Can load and save chosen terms
 * Updated the release notes
 *
 * Revision 1.16  2005/06/21 07:13:51  erichson
 * Added medServer host settings.
 *
 * Revision 1.15  2004/07/26 14:53:16  erichson
 * Added authentication type settings
 *
 * Revision 1.14  2004/03/26 17:57:39  erichson
 * Fixed spelling error in setDatabaseExaminationIdentifierType method
 *
 * Revision 1.13  2004/03/26 17:29:04  erichson
 * Added default address localhost, set port default to 1433 (sqlserver) instead of 3316 (mysql)
 *
 * Revision 1.12  2004/03/25 21:14:41  erichson
 * More database keys added
 *
 * Revision 1.11  2004/02/25 18:53:45  erichson
 * Added termDefinitions and termValue settings since they are no longer stored by the datahandling
 *
 * Revision 1.10  2004/02/23 12:23:06  erichson
 * Added keys for database support
 *
 * Revision 1.9  2003/07/08 11:17:38  erichson
 * Added keys and methods for remembering filechooser directories
 *
 * Revision 1.8  2003/07/02 00:16:54  erichson
 * added isFloaterTypeAvailable
 *
 * Revision 1.7  2002/11/01 09:49:04  zachrisg
 * Changed the API for floater prefs to use the floater name instead of a reference to the
 * floater itself.
 *
 * Revision 1.6  2002/10/31 15:13:22  zachrisg
 * Added floater visibility and location prefs.
 *
 * Revision 1.5  2002/10/28 10:12:18  zachrisg
 * Added get and set methods for template and translator filenames.
 * Added a lot of javadoc.
 *
 */

package medview.visualizer.data;


import java.util.*; // Vector, StringTokenizer
import java.util.prefs.*;

import java.awt.Point;
import java.awt.Dimension;

import medview.datahandling.examination.SQLExaminationDataHandler;
import medview.visualizer.gui.*;

/**
 * Class which tracks all settings for the visualizer app.
 *
 * @author Nils Erichsom <d97nix@dtek.chalmers.se>
 *
 * $Id: Settings.java,v 1.18 2008/08/27 07:11:21 it2aran Exp $
 */

public class Settings  
{
    
    private final static String TERM_DELIMITER = "|"; //for chosen terms
    // Preference keys
    private final static String KEY_LOOK_AND_FEEL = "LOOK_AND_FEEL"; // boolean
    private final static String KEY_WINDOW_SYSTEM = "WINDOW_SYSTEM"; // integer
    // private final static String KEY_KEEP_SELECTION_AFTER_DND = "DND_KEEP_SELECTION";
    private final static String KEY_WINDOWPOS_X = "WINDOWPOS_X";
    private final static String KEY_WINDOWPOS_Y = "WINDOWPOS_Y";
    private final static String KEY_WINDOWSIZE_X = "WINDOWSIZE_X";
    private final static String KEY_WINDOWSIZE_Y = "WINDOWSIZE_Y";
    private final static String KEY_LOAD_ONLY_PRIMARY_EXAMINATIONS = "LOAD_ONLY_PRIMARY_EXAMINATIONS";
    private final static String KEY_CHOSEN_TERMS = "CHOSEN_TERMS";    
    
    private final static String KEY_TEMPLATE_FILENAME = "TEMPLATE_FILENAME";
    private final static String KEY_TRANSLATOR_FILENAME = "TRANSLATOR_FILENAME";
    private final static String KEY_TERMDEFINITION_LOCATION = "TERMDEFINITIONS_LOCATION";
    private final static String KEY_TERMVALUE_LOCATION = "TERMDEFINITION_LOCATION";
    
    private final static String KEY_FLOATER_VISIBILITY = "FLOATER_VISIBILITY";
    private final static String KEY_FLOATER_X_LOCATION = "FLOATER_X_LOCATION";
    private final static String KEY_FLOATER_Y_LOCATION = "FLOATER_Y_LOCATION";
    private final static String KEY_MVD_FILECHOOSER_DIR = "MVD_FILECHOOSER_DIR";
    private final static String KEY_AGGREGATION_FILECHOOSER_DIR = "AGGREGATION_FILECHOOSER_DIR";
    
    /** Window system constants */
    public static final int WINDOWSYSTEM_FIRST = 1;
    public static final int WINDOWSYSTEM_MDI = 1;    
    public static final int WINDOWSYSTEM_FREE = 2;
    public static final int WINDOWSYSTEM_LAST = 2;    
    
    /** Default settings */
    
    // Window system
    private final static int DEFAULT_WINDOWSYSTEM = WINDOWSYSTEM_MDI;    
    private final static String DEFAULT_LOOK_AND_FEEL = javax.swing.UIManager.getCrossPlatformLookAndFeelClassName();
    // private final static boolean DEFAULT_KEEP_SELECTION_AFTER_DND = true;
    private final static java.awt.Dimension DEFAULT_WINDOWSIZE = new java.awt.Dimension(800,600);
    private final static java.awt.Point DEFAULT_WINDOWPOSITION = new java.awt.Point(0,0);
    
    private final static boolean DEFAULT_LOAD_ONLY_PRIMARY_EXAMINATIONS = false;
    private final static String DEFAULT_CHOSEN_TERMS = "";
    
    // Templates/translator
    private final static String DEFAULT_TEMPLATE_FILENAME = "";
    private final static String DEFAULT_TRANSLATOR_FILENAME = "";
    private final static String DEFAULT_TERMDEFINITION_LOCATION = "";
    private final static String DEFAULT_TERMVALUE_LOCATION = "";
    
    private final static boolean DEFAULT_FLOATER_VISIBILITY = true;
    private final static String DEFAULT_AGGREGATION_FILECHOOSER_DIR = getUserHomeDirectory() + "/aggregations/";
    private final static String DEFAULT_MVD_FILECHOOSER_DIR = getUserHomeDirectory();
    
    
    /* Member variables */
    
    private Preferences prefs;
    
    /** A reference to the one and only instance of this class. */
    private static Settings instance = null;
    
    /** 
     * Creates a new instance of Settings.
     */
    private Settings() {        
        prefs = Preferences.userNodeForPackage(this.getClass());                        
    }
    
    /**
     * Returns a reference to the one and only instance of the Settings class.
     *
     * @return A reference to the instance of the Settings class.
     */
    public static Settings getInstance() {
        if (instance == null) 
            instance = new Settings();
        
        return instance;
    }
    
    /**
     * Get the class name of the look and feel.
     *
     * @return The class name of the look and feel.
     */    
    public String getLookAndFeelClassName() {
        return prefs.get(KEY_LOOK_AND_FEEL,DEFAULT_LOOK_AND_FEEL);
    }
    
    /**
     * Set the look and feel (class name).
     *
     * @param lookAndFeelClass The look and feel class name.
     */
    public void setLookAndFeelClassName(String lookAndFeelClass) {
        prefs.put(KEY_LOOK_AND_FEEL,lookAndFeelClass);
    }
    
    /**
     * Returns true if only primary examinations should be loaded.
     *
     * @return True if only primary examinations should be loaded.
     */
    public boolean isLoadOnlyPrimaryExaminations() {
        return prefs.getBoolean(KEY_LOAD_ONLY_PRIMARY_EXAMINATIONS,DEFAULT_LOAD_ONLY_PRIMARY_EXAMINATIONS);
    }
    
    /**
     * Sets whether only primary examinations should be loaded.
     *
     * @param onlyPrimary True if only primary examinations should be loaded.
     */
    public void setLoadOnlyPrimaryExaminations(boolean onlyPrimary) {
        prefs.putBoolean(KEY_LOAD_ONLY_PRIMARY_EXAMINATIONS,onlyPrimary);
    }
    
    /**
     * Returns the window system.
     *
     * @return The window system, either WINDOWSYSTEM_FREE or WINDOWSYSTEM_MDI.
     */
    public int getWindowSystem() {        
        return prefs.getInt(KEY_WINDOW_SYSTEM,DEFAULT_WINDOWSYSTEM); 
    }        
    
    /**
     * Sets the window system.
     *
     * @param windowSystem The window system, either WINDOWSYSTEM_FREE or WINDOWSYSTEM_MDI.
     */
    public void setWindowSystem(int windowSystem) {
        if ((windowSystem >= WINDOWSYSTEM_FIRST) && (windowSystem <= WINDOWSYSTEM_LAST)) {
            prefs.putInt(KEY_WINDOW_SYSTEM,windowSystem);
        } else {
            System.err.println("Settings: ERROR: bad window system");
        }
    }
    
    /**
     * Returns the size of the main window.
     *
     * @return The size of the main window.
     */
    public java.awt.Dimension getWindowSize() {
        return new java.awt.Dimension(prefs.getInt(KEY_WINDOWSIZE_X,DEFAULT_WINDOWSIZE.width),prefs.getInt(KEY_WINDOWSIZE_Y,DEFAULT_WINDOWSIZE.height));
    }
    
    /**
     * Sets the size of the main window.
     *
     * @param d The size of the main window.
     */
    public void setWindowSize(java.awt.Dimension d) {
        prefs.putInt(KEY_WINDOWSIZE_X,d.width);
        prefs.putInt(KEY_WINDOWSIZE_Y,d.height);
    }
    
    /**
     * Returns the main window's position.
     *
     * @return The position of the main window.
     */
    public Point getWindowPosition() {
        return new Point(prefs.getInt(KEY_WINDOWPOS_X,DEFAULT_WINDOWPOSITION.x),prefs.getInt(KEY_WINDOWPOS_Y,DEFAULT_WINDOWPOSITION.y));        
    }
    
    /**
     * Sets the main window's position.
     *
     * @param p The position.
     */
    public void setWindowPosition(Point p) {
        prefs.putInt(KEY_WINDOWPOS_X,p.x);
        prefs.putInt(KEY_WINDOWPOS_Y,p.y);
    }    
    
    /**
     * Sets the filname of the template to use when displaying patient data.
     *
     * @param templateFilename The filename of the template to use.
     */
    public void setTemplateFilename(String templateFilename) {
        prefs.put(KEY_TEMPLATE_FILENAME, templateFilename);
    }    
    /**
     * Returns the filename of the template to use when displaying patient data.
     *
     * @return The filename of the template to use when displaying patient data.
     */
    public String getTemplateFilename() {
        return prefs.get(KEY_TEMPLATE_FILENAME, DEFAULT_TEMPLATE_FILENAME);
    }
    
    /**
     * Sets the filename of the translator to use when displaying patient data.
     *
     * @param translatorFilename The filename of the translator to use.
     */
    public void setTranslatorFilename(String translatorFilename) {
        prefs.put(KEY_TRANSLATOR_FILENAME, translatorFilename);
    }
    
    public void setTermDefinitionLocation(String newPath) {
        prefs.put(KEY_TERMDEFINITION_LOCATION, newPath);
    }
    public String getTermDefinitionLocation() {
        return prefs.get(KEY_TERMDEFINITION_LOCATION, DEFAULT_TERMDEFINITION_LOCATION);
    }
    public void setTermValueLocation(String newPath) {
        prefs.put(KEY_TERMVALUE_LOCATION, newPath);
    }
    public String getTermValueLocation() {
        return prefs.get(KEY_TERMVALUE_LOCATION, DEFAULT_TERMVALUE_LOCATION);
    }
    
    /**
     * Returns the filename of the translator to use when displaying patient data.
     *
     * @return The filename of the translator to use.
     */
    public String getTranslatorFilename() {
        return prefs.get(KEY_TRANSLATOR_FILENAME, DEFAULT_TRANSLATOR_FILENAME);
    }
    
    /**
     * Returns the chosen terms.
     *
     * @return An array of the chosen terms.
     */
    public String[] getChosenTerms() {
        String chosenTermsString = prefs.get(KEY_CHOSEN_TERMS,DEFAULT_CHOSEN_TERMS);
        StringTokenizer tokenizer = new StringTokenizer(chosenTermsString,TERM_DELIMITER);
        Vector termVector = new Vector();
        while (tokenizer.hasMoreTokens()) {
            termVector.add(tokenizer.nextToken());
        }
        
        String[] terms = new String[termVector.size()];
        terms = (String[]) termVector.toArray(terms);
        return terms;
    }     
        
    /**
     * Sets the chosen terms.
     *
     * @param terms An array of the chosen terms.
     */
    public void setChosenTerms(String[] terms) {
        StringBuffer buffer = new StringBuffer();
        List termList = Arrays.asList(terms);
        for (Iterator it = termList.iterator(); it.hasNext();) {
            String s = (String) it.next();
            buffer.append(s);
            if (it.hasNext()) {
                buffer.append(TERM_DELIMITER);
            }
        }        
        prefs.put(KEY_CHOSEN_TERMS,buffer.toString());
    }
    
    /**
     * Sets the visibility of the floater.
     *
     * @param floaterName The floater name.
     * @param visible True if the floater is visible.
     */
    public void setFloaterVisible(String floaterName, boolean visible) {
        prefs.putBoolean(KEY_FLOATER_VISIBILITY + "_" + floaterName, visible);        
    }
    
    /**
     * Returns true if the floater should be visible.
     *
     * @param floaterName The floater name.
     * @return True if the floater should be visible.
     */
    public boolean isFloaterVisible(String floaterName) {
        return prefs.getBoolean(KEY_FLOATER_VISIBILITY + "_" + floaterName, DEFAULT_FLOATER_VISIBILITY);
    }
    
    /**
     * Sets the location of the floater.
     *
     * @param floaterName The floater name.
     * @param location The location of the floater.
     */
    public void setFloaterLocation(String floaterName, Point location) {        
        prefs.putInt(KEY_FLOATER_X_LOCATION + "_" + floaterName, location.x);
        prefs.putInt(KEY_FLOATER_Y_LOCATION + "_" + floaterName, location.y);
    }
    
    /**
     * Returns the location that the floater should have.
     *
     * @param floaterName The floater name.
     * @return The location that the floater should have.
     */
    public Point getFloaterLocation(String floaterName) {
        int defaultXLocation = 0;
        int defaultYLocation = 50;
        if (floaterName.equals("datagroups")) {
            defaultXLocation = 150;
        } else if (floaterName.equals("query")) {
            defaultXLocation = 300;
        } else if (floaterName.equals("toolbox")) {
            defaultXLocation = 10;
        }
        
        int xLocation = prefs.getInt(KEY_FLOATER_X_LOCATION + "_" + floaterName, defaultXLocation);
        int yLocation = prefs.getInt(KEY_FLOATER_Y_LOCATION + "_" + floaterName, defaultYLocation);
        
        return new Point(xLocation, yLocation);
    }
    
    public boolean isFloaterTypeAvailable(int floater_type) {
        switch(floater_type) {            
            case Floater.FLOATER_TYPE_TOOLBOX:
                return false;        
            case Floater.FLOATER_TYPE_DATAGROUPS:
            case Floater.FLOATER_TYPE_MESSAGES:
            case Floater.FLOATER_TYPE_QUERY:
            default:
                return true;
        }
    }
     
    // Not really settings right now, putting it since it might be useful to be able to change it in the future
    public static String getUserHomeDirectory() {
        return medview.datahandling.MedViewDataHandler.instance().getUserHomeDirectory();        
    }
    
    public String getMVDFileChooserDir() {
        return prefs.get(KEY_MVD_FILECHOOSER_DIR, DEFAULT_MVD_FILECHOOSER_DIR);
    }
                
    public void setMVDFileChooserDir(String newDir) {
        prefs.put(KEY_MVD_FILECHOOSER_DIR, newDir);
    }

    // Aggregation file chooser
    
    public String getAggregationFileChooserDir() {
        return prefs.get(KEY_AGGREGATION_FILECHOOSER_DIR, DEFAULT_AGGREGATION_FILECHOOSER_DIR);
    }
                
    public void setAggregationFileChooserDir(String newDir) {
        prefs.put(KEY_AGGREGATION_FILECHOOSER_DIR, newDir);
    }
    
    // ---
    // - MedServer section 
    // ---
    
    private final static String KEY_MEDSERVER_HOST = "MEDSERVER_HOST";
    private final static String DEFAULT_MEDSERVER_HOST= "localhost";
    
    public void setMedServerHost(String host)
    {
        prefs.put(KEY_MEDSERVER_HOST, host);
    }
    
    public String getMedServerHost()
    {
        return prefs.get(KEY_MEDSERVER_HOST, DEFAULT_MEDSERVER_HOST);
    }
    
    // ------------------------------------------------------------------------
    // Database section
    // ------------------------------------------------------------------------
    
    private final static String KEY_DATABASE_SERVER_ADDRESS = "DATABASE_SERVER_ADDRESS";
    private final static String KEY_DATABASE_SERVER_PORT = "DATABASE_SERVER_PORT";
    private final static String KEY_DATABASE_USERNAME = "DATABASE_USERNAME";
    private final static String KEY_DATABASE_PASSWORD = "DATABASE_PASSWORD";
    private final static String KEY_DATABASE_CATALOG = "DATABASE_CATALOG";        
    
    private final static String KEY_AUTHENTICATION_TYPE = "AUTHENTICATION_TYPE";
    private final static String KEY_WINDOWS_DOMAIN = "WINDOWS_DOMAIN";
    
    private static final String KEY_DATABASE_EXAMINATIONIDENTIFIER_FIELD = "DATABASE_EXAMINATIONIDENTIFIER_FIELD";
    private static final String KEY_DATABASE_EXAMINATIONIDENTIFIER_TYPE = "DATABASE_EXAMINATIONIDENTIFIER_TYPE";
    private static final String KEY_DATABASE_TABLE = "DATABASE_TABLE";
    private static final String KEY_DATABASE_PIDFIELD = "DATABASE_PIDFIELD";
    
    private final static int DEFAULT_DATABASE_SERVER_PORT = 1433; // 1433 for SQL server, 3316 for mySQL
    private final static String DEFAULT_DATABASE_SERVER_ADDRESS = "localhost";
    
    public void setDatabaseServerAddress(String address) {
        prefs.put(KEY_DATABASE_SERVER_ADDRESS, address);
    }
    
    public String getDatabaseServerAddress() {
        return prefs.get(KEY_DATABASE_SERVER_ADDRESS, DEFAULT_DATABASE_SERVER_ADDRESS);
    }
    
    public void setDatabaseServerPort(int port) {
        prefs.putInt(KEY_DATABASE_SERVER_PORT, port);
    }
    
    public int getDatabaseServerPort() {
        return prefs.getInt(KEY_DATABASE_SERVER_PORT, DEFAULT_DATABASE_SERVER_PORT);
    }
    
    public void setDatabaseUser(String username) {
        prefs.put(KEY_DATABASE_USERNAME, username);
    }
    
    public String getDatabaseUser() {
        return prefs.get(KEY_DATABASE_USERNAME, "");
    }
    
    public void setDatabasePassword(String password) {
        prefs.put(KEY_DATABASE_PASSWORD, password);
    }
    
    public String getDatabasePassword() {
        return prefs.get(KEY_DATABASE_PASSWORD, "");
    }
                        
    public void setDatabaseCatalog(String catalog) {
        prefs.put(KEY_DATABASE_CATALOG, catalog);
    }
    
    public String getDatabaseCatalog() {
        return prefs.get(KEY_DATABASE_CATALOG, "");
    }
    
    public String getDatabasePIDField() {
        return prefs.get(KEY_DATABASE_PIDFIELD,"");
    }
    public void setDatabasePIDField(String PIDfield) {
        prefs.put(KEY_DATABASE_PIDFIELD, PIDfield);
    }
    
    public String getDatabaseTable() {
        return prefs.get(KEY_DATABASE_TABLE, "");
    }
    
    public void setDatabaseTable(String table) {
        prefs.put(KEY_DATABASE_TABLE,table);
    }
    
    public String getDatabaseExaminationIdentifierField() {
        return prefs.get(KEY_DATABASE_EXAMINATIONIDENTIFIER_FIELD, "");
    }
    
    public void setDatabaseExaminationIdentifierField(String field) {
        prefs.put(KEY_DATABASE_EXAMINATIONIDENTIFIER_FIELD, field);
    }
     
    public int getDatabaseExaminationIdentifierType() {
        return prefs.getInt(KEY_DATABASE_EXAMINATIONIDENTIFIER_TYPE, SQLExaminationDataHandler.EXAMINATIONIDENTIFIER_DATE);
    }
    
    public void setDatabaseExaminationIdentifierType(int type) {
        prefs.putInt(KEY_DATABASE_EXAMINATIONIDENTIFIER_TYPE, type);
    }
    
    public String getDatabaseWindowsDomain()
    {
        return prefs.get(KEY_WINDOWS_DOMAIN, "");
    }
    
    public void setDatabaseWindowsDomain(String newDomain)
    {
        prefs.put(KEY_WINDOWS_DOMAIN, newDomain);
    }
    
    public int getDatabaseAuthenticationType()
    {
        return prefs.getInt(KEY_AUTHENTICATION_TYPE, SQLExaminationDataHandler.AUTHENTICATION_SQL);
    }
    
    public void setDatabaseAuthenticationType(int authType)
    {
        prefs.putInt(KEY_AUTHENTICATION_TYPE, authType);
    }
        
}
