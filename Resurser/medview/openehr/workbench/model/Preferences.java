//
//  Preferences.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-11-18.
//
//  $Id: Preferences.java,v 1.3 2008/12/23 07:36:07 oloft Exp $
//

package medview.openehr.workbench.model;

import medview.datahandling.*;

public class Preferences {


    public static final String SelectedLanguage = "SelectedLanguage";
    public static final String LookAndFeel = "LookAndFeel";
        
    public static final String ArchetypesLocation = "ArchetypesLocation";
    public static final String TemplatesLocation = "TemplatesLocation";

	public static final String MaximizeWindow = "MaximizeWindow";
	public static final String WindowSize = "WindowSize";
	public static final String WindowLocation = "WindowLocation";
	
	public static final String TreeMainViewDividerLocation = "TreeMainViewDividerLocation";
	public static final String TreeTextViewDividerLocation = "TreeTextViewDividerLocation";
	public static final String TreeTableViewDividerLocation = "TreeTableViewDividerLocation";
	public static final String TextAPathViewDividerLocation = "TextAPathViewDividerLocation";
	
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

    public String getLookAndFeel() {
        return getUserStringPref(LookAndFeel, null);
    }

    public String getSelectedLanguage() {
        return getUserStringPref(SelectedLanguage, null);
    }    

    public String getArchetypesLocation() {
        return getUserStringPref(ArchetypesLocation, mVDH.getUserHomeDirectory());
		// return "/Users/oloft/Work/openEHR/SANDBOX-zilics-models/zilics-models/test/src/main/resources/adl";
    }

    public String getTemplatesLocation() {
        return getUserStringPref(TemplatesLocation, mVDH.getUserHomeDirectory());
		// return "/Users/oloft/Work/openEHR/SANDBOX-zilics-models/zilics-models/test/src/main/resources/xml/templates";
    }    

	public Boolean getMaximizeWindow() {
		return getUserBoolPref(MaximizeWindow, false);
	}
	
	public String getWindowSize() {
		return getUserStringPref(WindowSize, "950x700");
	}
	
	public String getWindowLocation() {
		return getUserStringPref(WindowLocation, null);
	}
	
	public int getTreeMainViewDividerLocation() {
		return getUserIntPref(TreeMainViewDividerLocation, 200);
	}
	
	public int getTreeTextViewDividerLocation() {
		return getUserIntPref(TreeTextViewDividerLocation, 300);
	}
	
	public int getTreeTableViewDividerLocation() {
		return getUserIntPref(TreeTextViewDividerLocation, 100);
	}
	
	public int getTextAPathViewDividerLocation() {
		return getUserIntPref(TextAPathViewDividerLocation, 380);
	}
	
	public void setLookAndFeel(String lf)
	{
		setUserStringPref(LookAndFeel, lf);
	}

	public void setSelectedLanguage(String lang)
	{
		setUserStringPref(SelectedLanguage, lang);
	}

	public void setArchetypesLocation(String lf)
	{
		setUserStringPref(ArchetypesLocation, lf);
	}

	public void setTemplatesLocation(String lang)
	{
		setUserStringPref(TemplatesLocation, lang);
	}
	
	public void setMaximizeWindow(Boolean flag) {
		setUserBoolPref(MaximizeWindow, flag);
	}

	public void setWindowSize(String s) {
		setUserStringPref(WindowSize, s);
	}
	
	public void setWindowLocation(String s) {
		setUserStringPref(WindowLocation, s);
	}
	
	public void setTreeMainViewDividerLocation(int n) {
		setUserIntPref(TreeMainViewDividerLocation, n);
	}
	
	public void setTreeTextViewDividerLocation(int n) {
		setUserIntPref(TreeTextViewDividerLocation, n);
	}

	public void setTreeTableViewDividerLocation(int n) {
		setUserIntPref(TreeTableViewDividerLocation, n);
	}
	
	public void setTextAPathViewDividerLocation(int n) {
		setUserIntPref(TextAPathViewDividerLocation, n);
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

