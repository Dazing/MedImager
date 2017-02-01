/*
 * Preferences.java
 *
 * Created on den 24 juni 2003, 12:38
 *
 * $Id: Preferences.java,v 1.1 2003/06/24 13:33:59 erichson Exp $
 *
 * $Log: Preferences.java,v $
 * Revision 1.1  2003/06/24 13:33:59  erichson
 * First check-in.
 *
 */

package medview.aggregator;

/**
 * Preferences for the Aggregator application.
 * This is a singleton class, so get an instance by calling getInstance()
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class Preferences {

    /* The user Preferences node for Aggregator */
    private java.util.prefs.Preferences prefs;
    private static Preferences instance = null;
    
    /* Preference key name constants */
    public static final String categoryDirPref = "AGGREGATOR_PREFS_CATEGORY_DIR";
    public static final String treefileDirPref = "AGGREGATOR_PREFS_TREEFILE_DIR";
    
    // Fallback dir to use if there is no set directory
    public static final String defaultDir = System.getProperty("user.home");
    
    // Private constructor (singleton use)
    private Preferences() {
         prefs = java.util.prefs.Preferences.userNodeForPackage(getClass());
    }
    
    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
        }        
        return instance;
    }
    
    /* Bean pattern methods go below here */
    
    public String getCategoryDir() {
        return prefs.get(categoryDirPref, defaultDir);
    }
    
    public void setCategoryDir(String dir) {
        prefs.put(categoryDirPref, dir);
    }
    
    public String getTreefileDir() {
        return prefs.get(treefileDirPref, defaultDir);
    }
    
    public void setTreefileDir(String dir) {
        prefs.put(treefileDirPref, dir);
    }
        
}
