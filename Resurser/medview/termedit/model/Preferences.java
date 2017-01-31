/*
 * $Id: Preferences.java,v 1.1 2003/12/13 23:54:40 oloft Exp $
 *
 * Created on den 2 oktober 2002, 09:58
 *
 */

package medview.termedit.model;

import medview.datahandling.*;

public class Preferences {


    public static final String SelectedLanguage = "SelectedLanguage";
    public static final String LookAndFeel = "LookAndFeel";
    
    public static final String UseRemoteDataHandling = "UseRemoteDataHandling";
    public static final String ServerLocation = "ServerLocation";
    
    public static final String TermDefinitionLocation = "TermDefinitionLocation";
    public static final String TermValuesLocation = "TermValuesLocation";

    private static Preferences instance;

    private MedViewDataHandler mVDH;
    private Class userPropClass;
    
    /** Creates a new instance of DatahandlingHandler */
    private Preferences() {
        mVDH = MedViewDataHandler.instance();
        userPropClass = Preferences.class;
    }
    
    public static Preferences instance() {
        if (instance == null){
            instance = new Preferences();
        }
        return instance;
    }

    public String getTermDefinitionLocation() {
        return getUserStringPref(TermDefinitionLocation, "");
    }
    
    public void setTermDefinitionLocation(String loc) {
        setUserStringPref(TermDefinitionLocation, loc);
    }

    public String getTermValueLocation() {
        return getUserStringPref(TermValuesLocation, "");
    }

    public void setTermValueLocation(String loc) {
        setUserStringPref(TermValuesLocation, loc);
    }

    public String getServerLocation() {
        return getUserStringPref(ServerLocation, "");
    }

    public void setServerLocation(String loc) {
         setUserStringPref(ServerLocation, loc);
    }
    
    public boolean usesRemoteDataHandling() {
        return getUserBoolPref(UseRemoteDataHandling, false);
    }

    public void setUseRemoteDataHandling(boolean flag) {
         setUserBoolPref(UseRemoteDataHandling, flag);
    }

    public String getLookAndFeel() {
        return getUserStringPref(LookAndFeel, null);
    }

    public String getSelectedLanguage() {
        return getUserStringPref(SelectedLanguage, null);
    }    

    // Helpers
    private String getUserStringPref(String key, String defaultVal) {
        return mVDH.getUserStringPreference(key, defaultVal, userPropClass);
    }

    private void setUserStringPref(String key, String value) {
         mVDH.setUserStringPreference(key, value, userPropClass);
    }
    
    private boolean getUserBoolPref(String key, boolean defaultVal) {
        return mVDH.getUserBooleanPreference(key, defaultVal, userPropClass);
    }

    private void setUserBoolPref(String key, boolean defaultVal) {
        mVDH.setUserBooleanPreference(key, defaultVal, userPropClass);
    }

    private int getUserIntPref(String key, int defaultVal) {
        return mVDH.getUserIntPreference(key, defaultVal, userPropClass);
    }

    private void setUserIntPref(String key, int defaultVal) {
        mVDH.setUserIntPreference(key, defaultVal, userPropClass);
    }

}
