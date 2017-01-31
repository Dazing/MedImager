//
//  Preferences.java
//
//  Created by Olof Torgersson on Fri Dec 05 2003.
//
//  $Id: PreferencesModel.java,v 1.35 2010/07/01 17:26:45 oloft Exp $
//

package medview.medrecords.data;

import java.awt.Dimension;

import medview.datahandling.*;

/**
 * @author Olof Torgersson, modified by Fredrik Lindahl
 * @version 1.1
 */
public class PreferencesModel
{
	// ACCESS BASED ON KEYS

	/**
	 * Returns null if key non-existant.
	 * @param key String
	 * @return String
	 */
	public static String getStringForKey(String key)
	{
		return PreferencesModel.instance().getUserStringPref(key, null);
	}

	/**
	 * Returns false if key non-existant.
	 * @param key String
	 * @return boolean
	 */
	public static boolean getBoolForKey(String key)
	{
		return PreferencesModel.instance().getUserBoolPref(key, false);
	}

	/**
	 * Returns -1 if key non-existant.
	 * @param key String
	 * @return int
	 */
	public static int getIntForKey(String key)
	{
		return PreferencesModel.instance().getUserIntPref(key, -1);
	}


	// SET VERIFIERS (FOR THOSE THAT DONT HAVE MEANINGFUL DEFAULT VALUES)

	public boolean isPCodeNRGeneratorLocationSet()
	{
		return (getPCodeNRGeneratorLocation() != null);
	}

	public boolean isTermDefinitionLocationSet()
	{
		return (getTermDefinitionLocation() != null);
	}

	public boolean isTermValueLocationSet()
	{
		return (getTermValueLocation() != null);
	}

	public boolean isLocalDatabaseLocationSet()
	{
		return (getLocalDatabaseLocation() != null);
	}

	public boolean isServerLocationSet()
	{
		return (getServerLocation() != null);
	}

	public boolean isPDADatabaseLocationSet()
	{
		return (getPDADatabaseLocation() != null);
	}

	public boolean isLookAndFeelSet()
	{
		return (getLookAndFeel() != null);
	}

	public boolean isPresetDividerLocationSet() {
		return (getPresetDividerLocation() != -1);
	}

	// GETTERS FOR SPECIAL KEYS

	public String getPCodeNRGeneratorLocation()
	{
		return getUserStringPref(PCodeNRGeneratorLocation, null);
	}

	public String getTermDefinitionLocation()
	{
		return getUserStringPref(TermDefinitionLocation, null);
	}

	public String getTermValueLocation()
	{
		return getUserStringPref(TermValuesLocation, null);
	}

	public String getLocalDatabaseLocation()
	{
		return getUserStringPref(LocalDatabaseLocation, null);
	}

	public String getServerLocation()
	{
		return getUserStringPref(ServerLocation, null);
	}

	public String getPDADatabaseLocation()
	{
		return getUserStringPref(PDADatabaseLocation, null);
	}

	public String getFASSURL()
	{
		return getUserStringPref(fassURL, "http://www.fass.se/LIF/home/index.jsp?UserTypeID=0");
	}

	public boolean usesRemoteDataHandling()
	{
		return getUserBoolPref(UseRemoteDataHandling, false);
	}

	public String getLookAndFeel()
	{
		return getUserStringPref(LookAndFeel, null);
	}

	public String getSelectedLanguage()
	{
		return getUserStringPref(SelectedLanguage, null);
	}

	public String getGraphTemplateLocation()
	{
		return getUserStringPref(GraphTemplateLocation, "");
	}

    public String getCariesFileLocation()
	{
		return getUserStringPref(CariesFileLocation, "");
	}
	public String getImageInputLocation()
	{
		return getUserStringPref(ImageInputLocation, mVDH.getUserHomeDirectory());
	}

	public String getImageCategoryName()
	{
		return getUserStringPref(ImageCategoryName, "BILDER");
	}

	public String getImageTermName()
	{
		return getUserStringPref(ImageTermName, "Photo");
	}

	public int getImageSelectorCount()
	{
		return getUserIntPref(ImageSelectorCount, 8);
	}

	public void setUsePlaqueIndex(boolean flag)
	{
		setUserBoolPref(UsePlaqueIndex, flag);
	}

	public boolean getUseMucos()
	{
	     return getUserBoolPref(UseMucos, false);
	}

    public boolean getStartMedSummary()
	{
	     return getUserBoolPref(StartMedSummary, true);
	}
    public boolean getShowTranslatorAtNewValue()
	{
		return getUserBoolPref(ShowTranslatorAtNewValue, false);
	}

	public boolean getKeepPrefix()
	{
		return getUserBoolPref(KeepPrefix, false);
	}

	public boolean getUseDocumentMode()
	{
		return getUserBoolPref(UseDocumentMode, true);
	}

	public int getPresetDividerLocation() {
		return getIntForKey(PresetDividerLocation);
	}

        public boolean getShowPDAButton() {
            return getUserBoolPref(ShowPDAButton, false);
        }

        public boolean getShowEditButtons() {
            return getUserBoolPref(ShowEditButtons, true);
        }

	// SETTERS FOR SPECIAL KEYS

	public void setPCodeNRGeneratorLocation(String loc)
	{
		setUserStringPref(PCodeNRGeneratorLocation, loc);
	}

	public void setTermDefinitionLocation(String loc)
	{
		setUserStringPref(TermDefinitionLocation, loc);
	}

	public void setTermValueLocation(String loc)
	{
		setUserStringPref(TermValuesLocation, loc);
	}

	public void setLocalDatabaseLocation(String loc)
	{
		setUserStringPref(LocalDatabaseLocation, loc);
	}

	public void setCariesDatabaseLocation(String loc)
	{
		setUserStringPref(CariesFileLocation, loc);
	}

    public void setServerLocation(String loc)
	{
		setUserStringPref(ServerLocation, loc);
	}

	public void setPDADatabaseLocation(String loc)
	{
		setUserStringPref(PDADatabaseLocation, loc);
	}

	public void setFASSURL(String url)
	{
		setUserStringPref(fassURL, url);
	}

	public void setUseRemoteDataHandling(boolean flag)
	{
		setUserBoolPref(UseRemoteDataHandling, flag);
	}

	public void setLookAndFeel(String lf)
	{
		setUserStringPref(LookAndFeel, lf);
	}

	public void setSelectedLanguage(String lang)
	{
		setUserStringPref(SelectedLanguage, lang);
	}

	public void setGraphTemplateLocation(String loc)
	{
		setUserStringPref(GraphTemplateLocation, loc);
	}

	public void setImageInputLocation(String loc)
	{
		setUserStringPref(ImageInputLocation, loc);
	}

	public void setImageCategoryName(String name)
	{
		setUserStringPref(ImageCategoryName, name);
	}

	public void setImageTermName(String name)
	{
		setUserStringPref(ImageTermName, name);
	}

	public void setImageSelectorCount(int n)
	{
		setUserIntPref(ImageSelectorCount, n);
	}

	public boolean getUsePlaqueIndex()
	{
		return getUserBoolPref(UsePlaqueIndex, false);
	}

	public void setUseMucos(boolean flag)
	{
	    setUserBoolPref(UseMucos, flag);
	}

    public void setShowTranslatorAtNewValue(boolean flag)
	{
		setUserBoolPref(ShowTranslatorAtNewValue, flag);
	}

	public void setKeepPrefix(boolean flag)
	{
		setUserBoolPref(KeepPrefix, flag);
	}

	public void setUseDocumentMode(boolean flag)
	{
		setUserBoolPref(UseDocumentMode, flag);
	}

	public void setPresetDividerLocation(int n) {
		setUserIntPref(PresetDividerLocation, n);
	}

        public void setShowPDAButtton(boolean flag) {
            setUserBoolPref(ShowPDAButton, flag);
        }

        public void setShowEditButtons(boolean flag) {
            setUserBoolPref(ShowEditButtons, flag);
        }

	/** MedForm-related */

	public boolean getTouchScreenDataFromMedForm()
	{ return getUserBoolPref(GetTouchScreenDataFromMedForm, false); }

	public void setTouchScreenDataFromMedForm(boolean getDataFromMedForm)
	{ setUserBoolPref(GetTouchScreenDataFromMedForm, getDataFromMedForm); }

	public String getMedFormURL()
	{ return getUserStringPref(MedFormURL, "http://"); }

	public void setMedFormURL(String url)
	{ setUserStringPref(MedFormURL, url); }

	public String getMedFormUser()
	{ return getUserStringPref(MedFormUser, ""); }

	public void setMedFormUser(String user)
	{ setUserStringPref(MedFormUser, user); }

	public String getMedFormMVD()
	{ return getUserStringPref(MedFormMVD, ""); }

	public void setMedFormMVD(String mvd)
	{ setUserStringPref(MedFormMVD, mvd); }


	// SPECIAL NON-KEY SETTINGS

	public void setGraphWindowSize(Dimension dim)
	{
		setUserIntPref(GraphWindowWidth, (int)dim.getWidth());

		setUserIntPref(GraphWindowHeight, (int)dim.getHeight());
	}

	public Dimension getGraphWindowSize()
	{
		int w = getUserIntPref(GraphWindowWidth, GRAPH_WINDOW_WIDTH_DEFAULT);

		int h = getUserIntPref(GraphWindowHeight, GRAPH_WINDOW_HEIGHT_DEFAULT);

		Dimension dim = new Dimension(w, h);

		return dim;
	}

	public void setPreviewWindowSize(Dimension dim)
	{
		setUserIntPref(PreviewWindowWidth, (int)dim.getWidth());

		setUserIntPref(PreviewWindowHeight, (int)dim.getHeight());
	}

	public Dimension getPreviewWindowSize()
	{
		int w = getUserIntPref(PreviewWindowWidth, PREVIEW_WINDOW_WIDTH_DEFAULT);

		int h = getUserIntPref(PreviewWindowHeight, PREVIEW_WINDOW_HEIGHT_DEFAULT);

		Dimension dim = new Dimension(w, h);

		return dim;
	}

	public int getImageWindowWidth()
	{
		return getUserIntPref(ImageWindowWidth, IMAGE_WINDOW_WIDTH_DEFAULT);
	}

	public void setImageWindowWidth(int n)
	{
		setUserIntPref(ImageWindowWidth, n);
	}

	// UTILITY METHODS (ACT AS A LAYER ON TOP OF DATA LAYER PREFS HANDLING)

	private String getUserStringPref(String key, String defaultVal)
	{
		return mVDH.getUserStringPreference(key, defaultVal, PreferencesModel.class);
	}

	private void setUserStringPref(String key, String value)
	{
		mVDH.setUserStringPreference(key, value, PreferencesModel.class);
	}

	private boolean getUserBoolPref(String key, boolean defaultVal)
	{
		return mVDH.getUserBooleanPreference(key, defaultVal, PreferencesModel.class);
	}

	private void setUserBoolPref(String key, boolean defaultVal)
	{
		mVDH.setUserBooleanPreference(key, defaultVal, PreferencesModel.class);
	}

	private int getUserIntPref(String key, int defaultVal)
	{
		return mVDH.getUserIntPreference(key, defaultVal, PreferencesModel.class);
	}

	private void setUserIntPref(String key, int defaultVal)
	{
		mVDH.setUserIntPreference(key, defaultVal, PreferencesModel.class);
	}


	// SINGLETON

	public static PreferencesModel instance()
	{
		if (instance == null)
		{
			instance = new PreferencesModel();
		}

		return instance;
	}


	// CONSTRUCTOR

	private PreferencesModel()
	{
		mVDH = MedViewDataHandler.instance();
	}

	// MEMBERS (MR names are for backwards compability)

	// version

	public static final String MEDRECORDS_VERSION_STRING = "4.5 Beta 2";

	// packages

	public static final String DATA_COMPONENT_PACKAGE_PREFIX = "dataComponentPackage";

	public static final String INCLUDED_DATA_COMPONENT_PACKAGE_SWITCH_FLAG = "medRecordsIncludedDataComponentPackageSwitchFlag";

	public static final String LAST_DATA_COMPONENT_PACKAGE_PROPERTY = "lastDataComponentPackage";

	// mixed

	public static final String PCodeNRGeneratorLocation = "PCodeNRGeneratorLocation";

	public static final String TermDefinitionLocation = "MRtermLocation";

	public static final String TermValuesLocation = "MRvalueLocation";

	public static final String LocalDatabaseLocation = "MRmvdpath";

	public static final String PDADatabaseLocation = "MRpdamvdpath";

	public static final String ServerLocation = "ServerLocation";

    public static final String CariesFileLocation = "CariesFileLocation";

    public static final String fassURL = "FassURL";

	public static final String UseRemoteDataHandling = "UseRemoteDataHandling";

	public static final String LookAndFeel = "LookAndFeel";

	public static final String SelectedLanguage = "MRLanguage";

	public static final String InputListBackgroundColor = "listBackgroundColorPref";

	public static final String DefaultInputListBackgroundColorString = "220,220,220";

	public static final String GraphTemplateLocation = "MRgraphtemplatepath";

	/* Medform-related */

	public static final String GetTouchScreenDataFromMedForm = "GetTouchScreenDataFromMedForm";

	public static final String MedFormURL = "MedFormURL";

	public static final String MedFormUser = "MedFormUser";

	public static final String MedFormMVD = "MedFormMVD";

	/* --------------- */

	public static final String ImageInputLocation = "MRimagepath";

	public static final String ImageCategoryName = "MRpicturecategory";

	public static final String ImageTermName = "ImageTermName";

	public static final String ImageSelectorCount = "MRmaxthumbnails"; // Num of pics/group

	public static final String UsePlaqueIndex = "MRIsPlaqueUsed";

	public static final String UseMucos = "MRIsMucosUsed";

    public static final String ShowTranslatorAtNewValue = "MRShowTranslatorAtNewValue";
    
    public static final String StartMedSummary = "StartMedSummary";

    public static final String KeepPrefix = "MRKeepPrefix"; //

	public static final String UseDocumentMode = "MRIsDocumentModeOn"; //

	public static final String PCodePrefix = "GeneratedPCodePrefix"; //

	public static final String PreviewWindowWidth = "PreviewWindowWidth";

	public static final String PreviewWindowHeight = "PreviewWindowHeight";

	public static final String GraphWindowWidth = "GraphWindowWidth";

	public static final String GraphWindowHeight = "GraphWindowHeight";
	
	public static final String ImageWindowWidth = "ImageWindowWidth";
	
	public static final String PresetDividerLocation = "PresetDividerLocation";

        public static final String ShowPDAButton = "ShowPDAButton";

        public static final String ShowEditButtons = "ShowEditButtons";

	// visual component states

	private static final int GRAPH_WINDOW_WIDTH_DEFAULT = 640;

	private static final int GRAPH_WINDOW_HEIGHT_DEFAULT = 480;

	private static final int PREVIEW_WINDOW_WIDTH_DEFAULT = 350;

	private static final int PREVIEW_WINDOW_HEIGHT_DEFAULT = 500;
	
	private static final int IMAGE_WINDOW_WIDTH_DEFAULT = 768;
	
	public static final String MEDRECORDS_SIZE_PROPERTY = "medrecordsSize";

	public static final String MEDRECORDS_LOCATION_PROPERTY = "medrecordsLocation";

	public static final String MEDRECORDS_MAXIMIZED_PROPERTY = "medrecordsMaximized";

        
	// some other members

	private static PreferencesModel instance;

	private MedViewDataHandler mVDH;

	/* NOTE: the previous "MRPictureTermName" is deprecated, always
	 * uses default value currently, thus new name used instead of old,
	 * effect is that actually stored settings are discared and default
	 * value "Photo" is always used. */

}
