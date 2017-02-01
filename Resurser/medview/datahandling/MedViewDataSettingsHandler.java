/*
 * @(#)MedViewDataSettingsHandler.java
 *
 * $Id: MedViewDataSettingsHandler.java,v 1.19 2005/03/16 13:52:42 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

import java.io.*;

import java.util.prefs.*;

/**
 *
 * Some important methods and their
 * explanations:<br>
 * <br>
 *
 * <i> getUserHomeDirectory() </i>:
 * Returns the users home directory appended
 * with the medview specific subfolder. For
 * instance, on a Windows system, this could be
 * 'c:\documents and settings\fredrik\medview\'.<br>
 * <br>
 *
 * <i> getResourcePrefix() </i>:
 * Returns the prefix that should be
 * used for grouping resources. For
 * instance, it the application is
 * loaded with a -cp setting specifying
 * a jar file containing resources, the
 * resources in the jar file should be
 * contained under this prefix. Ex:
 * 'resources/images/newIcon.gif'.
 */
public class MedViewDataSettingsHandler
{

// ----------------------------------------------------------------------------
// ***************************** USER PREFERENCES *****************************
// ----------------------------------------------------------------------------

	public void clearUserPreference(String key)
	{
		clearUserPreference(key, null); // the non class node is assumed
	}

	public void clearUserPreference(String key, Class c)
	{
		pref = (c == null) ? userNonClassNode : Preferences.userNodeForPackage(c);

		pref.remove(key);
	}


	public String getUserStringPreference(String key, String def, Class c)
	{
		pref = (c == null) ? userNonClassNode : Preferences.userNodeForPackage(c);

		return pref.get(key, def);
	}

	public boolean getUserBooleanPreference(String key, boolean def, Class c)
	{
		pref = (c == null) ? userNonClassNode : Preferences.userNodeForPackage(c);

		return pref.getBoolean(key, def);
	}

	public int getUserIntPreference(String key, int def, Class c)
	{
		pref = (c == null) ? userNonClassNode : Preferences.userNodeForPackage(c);

		return pref.getInt(key, def);
	}

	public double getUserDoublePreference(String key, double def, Class c)
	{
		pref = (c == null) ? userNonClassNode : Preferences.userNodeForPackage(c);

		return pref.getDouble(key, def);
	}

	public boolean isUserPreferenceSet(String key, Class c)
	{
		pref = (c == null) ? userNonClassNode : Preferences.userNodeForPackage(c);

		return (pref.get(key, null) != null);
	}


	public void setUserStringPreference(String key, String val, Class c)
	{
		pref = (c == null) ? userNonClassNode : Preferences.userNodeForPackage(c);

		pref.put(key, val);
	}

	public void setUserBooleanPreference(String key, boolean val, Class c)
	{
		pref = (c == null) ? userNonClassNode : Preferences.userNodeForPackage(c);

		pref.putBoolean(key, val);
	}

	public void setUserIntPreference(String key, int val, Class c)
	{
		pref = (c == null) ? userNonClassNode : Preferences.userNodeForPackage(c);

		pref.putInt(key, val);
	}

	public void setUserDoublePreference(String key, double val, Class c)
	{
		pref = (c == null) ? userNonClassNode : Preferences.userNodeForPackage(c);

		pref.putDouble(key, val);
	}

// ----------------------------------------------------------------------------
// ****************************************************************************
// ----------------------------------------------------------------------------





// ----------------------------------------------------------------------------
// **************************** SYSTEM PREFERENCES ****************************
// ----------------------------------------------------------------------------

	public void clearSystemPreference(String key)
	{
		clearSystemPreference(key, null); // the non class node is assumed
	}

	public void clearSystemPreference(String key, Class c)
	{
		pref = (c == null) ? systemNonClassNode : Preferences.systemNodeForPackage(c) ;

		pref.remove(key);
	}

	public boolean isSystemPreferenceSet(String key, Class c)
	{
		pref = (c == null) ? systemNonClassNode : Preferences.systemNodeForPackage(c);

		return (pref.get(key, null) != null);
	}


	public String getSystemStringPreference(String key, String def, Class c)
	{
		pref = (c == null) ? systemNonClassNode : Preferences.systemNodeForPackage(c);

		return pref.get(key, def);
	}

	public boolean getSystemBooleanPreference(String key, boolean def, Class c)
	{
		pref = (c == null) ? systemNonClassNode : Preferences.systemNodeForPackage(c);

		return pref.getBoolean(key, def);
	}

	public int getSystemIntPreference(String key, int def, Class c)
	{
		pref = (c == null) ? systemNonClassNode : Preferences.systemNodeForPackage(c);

		return pref.getInt(key, def);
	}


	public void setSystemStringPreference(String key, String val, Class c)
	{
		pref = (c == null) ? systemNonClassNode : Preferences.systemNodeForPackage(c);

		pref.put(key, val);
	}

	public void setSystemBooleanPreference(String key, boolean val, Class c)
	{
		pref = (c == null) ? systemNonClassNode : Preferences.systemNodeForPackage(c);

		pref.putBoolean(key, val);
	}

	public void setSystemIntPreference(String key, int val, Class c)
	{
		pref = (c == null) ? systemNonClassNode : Preferences.systemNodeForPackage(c);

		pref.putInt(key, val);
	}

// ----------------------------------------------------------------------------
// ****************************************************************************
// ----------------------------------------------------------------------------





	public String getResourcePrefix()
	{
		return "medview/datahandling/resources/";
	}

	public String getUserHomeDirectory()
	{
		String fS = System.getProperty("file.separator");

		return System.getProperty("user.home") + fS + "medview" + fS;
	}

	public String getUserCacheDirectory()
	{
		String fS = System.getProperty("file.separator");

		return System.getProperty("user.home") + fS + "medview" + fS + "cache";
	}

        // Added by Nils 2004-03-29
        /**
         * Gets a temporary directory for use by MedView.
         * Implemented as {system property <code>java.io.tmpdir</code>}/medview.
         * On UNIX systems the default value of <code>java.io.tmpdir</code> is typically "/tmp" or "/var/tmp"; on Microsoft Windows systems it is typically "c:\\temp".
         *
         * @return the path to the medview temp directory.
         * @see java.io.File#createTempFile(java.lang.String, java.lang.String, java.io.File)
         */
        public String getMedViewTempDirectory() {

                String fS = System.getProperty("file.separator");

                String tempDir = System.getProperty("java.io.tmpdir");

                return tempDir + fS + "medview";
        }

	public static MedViewDataSettingsHandler instance( )
	{
		if (instance == null) { instance = new MedViewDataSettingsHandler(); }

		return instance;
	}





	private void initNonClassNodes()
	{
		userNonClassNode = Preferences.userRoot().node("/medview/defaultPreferenceNode");

		systemNonClassNode = Preferences.systemRoot().node("/medview/defaultPreferenceNode");
	}

	private void checkDirectoryExistances()
	{
		File homeFile = new File(getUserHomeDirectory());

		File cacheFile = new File(getUserCacheDirectory());

		if (!homeFile.exists()) { homeFile.mkdirs(); }

		if (!cacheFile.exists()) { cacheFile.mkdirs(); }
	}

	private MedViewDataSettingsHandler()
	{
		initNonClassNodes();

		checkDirectoryExistances();
	}

	private static MedViewDataSettingsHandler instance;

	private Preferences systemNonClassNode;

	private Preferences userNonClassNode;

	private Preferences pref;

	/* NOTE: the 'non class nodes' are the ones used when the caller
	 * specifies a null class argument. In general, this should be
	 * avoided, but it is included for backwards compatibility reasons.
	 * Note that it is then important that the keys are not identical. */

}
