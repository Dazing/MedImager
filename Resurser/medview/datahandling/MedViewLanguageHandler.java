/*
 * @(#)MedViewLanguageHandler.java
 *
 * $Id: MedViewLanguageHandler.java,v 1.7 2005/11/13 13:08:19 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * The language handler deals with the language-specific
 * parts of the data handling system. It's responsibilities
 * are: <br><br>
 * <ol>
 *		<li>To set up language property objects.</li>
 *		<li>To provide language-specific strings.</li>
 *		<li>To provide ability to change language.</li>
 * </ol>
 * The available languages are listed in a textfile in the
 * included resource jar-file. This file contains mappings
 * from the name to be displayed to the user for choice and
 * the actual filename containing the language strings. The
 * first mapping on this list becomes the default.<br>
 * <br>
 * The language-specific strings are identified by constants
 * in the MedViewLanguageConstants interface, and these are
 * the constants that need to be provided when obtaining the
 * strings. If the handler cannot find the string for a
 * specified constant, it will return a string identifying that
 * the resource was not found.
 *
 * @author Fredrik Lindahl
 */
public class MedViewLanguageHandler
{
	private HashMap nFMap = new HashMap();

	private Properties languageProperties = new Properties();

	private String currentLanguageDescriptor = "<< not set >>";

	public MedViewLanguageHandler()
	{
		try
		{
			String rP = MedViewDataSettingsHandler.instance().getResourcePrefix();

			ClassLoader cL = ClassLoader.getSystemClassLoader();

			String avPath = rP + "languages/available.txt";

			InputStream aI = cL.getResource(avPath).openStream();

			InputStreamReader r = new InputStreamReader(aI);

			BufferedReader buff = new BufferedReader(r);

			StringTokenizer t = null;

			String line, name, fName;

			boolean isFirstLine = true;

			while ((line = buff.readLine()) != null) // while not EOF...
			{
				t = new StringTokenizer(line, "\t" , false);

				name = t.nextToken();

				fName = t.nextToken();

				nFMap.put(name, fName);

				if (isFirstLine) // first line becomes default language
				{
					currentLanguageDescriptor = name;

					isFirstLine = false;
				}
			}
		}
		catch (Exception e)
		{
			System.err.println("Warning: could not read language properties");
		}

		try
		{
			readLanguageProperties(currentLanguageDescriptor);
		}
		catch (LanguageException e)
		{
			System.err.println("Warning: could not read language properties");
		}
	}

	/**
	 * Obtains the specific language string for the specified abstract id.
	 * @param id String
	 * @return String
	 */
	public String getLanguageString(String id)
	{
		String langString = languageProperties.getProperty(id);

		if (langString == null)
		{
			return "<< no language string found >>";
		}
		else
		{
			return langString;
		}
	}

	/**
	 * Obtains a listing of all available languages.
	 * @return String[]
	 */
	public String[] getAvailableLanguages()
	{
		String[] retArr = new String[nFMap.size()];

		nFMap.keySet().toArray(retArr);

		Arrays.sort(retArr);

		return retArr;
	}

	/**
	 * Tries to change the language to the specified one, and will throw an exception
	 * if this could not be done. More specifically, language properties try to be read
	 * from the location of the language.
	 * @param lang String
	 * @throws LanguageException
	 */
	public void changeLanguage(String lang) throws LanguageException
	{
		currentLanguageDescriptor = lang;

		readLanguageProperties(lang); // -> LanguageException
	}

	/**
	 * Obtain current language descriptor, if used.
	 * @return String
	 */
	public String getCurrentLanguageDescriptor()
	{
		return currentLanguageDescriptor;
	}

	/* UTILITY METHODS */

	private void readLanguageProperties(String lang) throws LanguageException
	{
		try
		{
			String relFPath = (String) nFMap.get(lang);

			if (relFPath == null)
			{
				throw new LanguageException(lang + " not existant");
			}

			ClassLoader cL = ClassLoader.getSystemClassLoader();

			languageProperties.load(cL.getResource(relFPath).openStream());
		}
		catch (Exception e)
		{
			throw new LanguageException(e.getMessage());
		}
	}
}
