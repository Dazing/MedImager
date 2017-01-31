/*
 * @(#)MeduwebLanguageHandler.java
 *
 * $Id: MeduwebLanguageHandler.java,v 
 *
 * --------------------------------
 * Original author: Fredrik Lindahl modded by Figge
 * --------------------------------
 */

package medview.meduweb.data;

import java.io.*;
import java.net.*;
import java.util.*;
import medview.datahandling.*;
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
 * specified constant, it will return the string "ERRLS".
 *
 * @author Fredrik Lindahl
 */
public class MeduwebLanguageHandler
{

	public String getLanguageString(String id)
	{
		String langString = languageProperties.getProperty(id);

		return ((langString != null) ? langString : "ERRLS");
	}

	public String[] getAvailableLanguages()
	{
		String[] retArr = new String[nFMap.size()];

		nFMap.keySet().toArray(retArr);

		Arrays.sort(retArr);

		return retArr;
	}

	public void changeLanguage(String lang) throws LanguageException
	{
		readLanguageProperties(lang); // throws exception if error...

		String cLP = CURRENT_LANGUAGE_PROPERTY; // ...otherwise we get here...
	}

	public String getCurrentLanguageDescriptor( )
	{
		return currentLanguageDescriptor;
	}

	private void readLanguageProperties(String lang) throws LanguageException
	{
		try
		{
			String relFPath = (String) nFMap.get(lang);

			if (relFPath == null) { throw new LanguageException(lang + " not existant"); }

			ClassLoader cL = ClassLoader.getSystemClassLoader();

			languageProperties.load(cL.getResource(relFPath).openStream());
		}
		catch (IOException e)
		{
			throw new LanguageException(e.getMessage());
		}
	}





	private void initSimpleMembers()
	{
		nFMap = new HashMap();

		languageProperties = new Properties();

		currentLanguageDescriptor = defaultLanguageName;
	}

	private void initNameFileMappings()
	{
		try
		{
			ClassLoader cL = ClassLoader.getSystemClassLoader();

			String avPath = langPath + AVAILABLE_FILE_NAME;
	
			InputStreamReader r = new InputStreamReader(new FileInputStream(avPath));

			BufferedReader buff = new BufferedReader(r);

			boolean isFirstLine = true;

			StringTokenizer t = null;

			String line, name, fName;

			while ((line = buff.readLine()) != null) // while not EOF...
			{
				t = new StringTokenizer(line, "\t" , false);

				name = t.nextToken(); fName = t.nextToken();

				nFMap.put(name, fName);

				if (isFirstLine)
				{
					defaultLanguageName = name;

					defaultLanguageFile = fName;

					isFirstLine = false;
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();

		}
	}

	private void initLanguage( )
	{
		try
		{
			readLanguageProperties(currentLanguageDescriptor);
		}
		catch (LanguageException e) { e.printStackTrace(); }
	}

	public MeduwebLanguageHandler()
	{
		initSimpleMembers();

		initNameFileMappings();

		initLanguage();
	}

	public MeduwebLanguageHandler(String lang)
	{
		this();
		currentLanguageDescriptor = lang;
	}
	
	private HashMap nFMap;

	private String defaultLanguageName;

	private String defaultLanguageFile;
	
	private String currentLanguageDescriptor;
	
	private String langPath = System.getProperty("medview.basedir") + "medview\\datahandling\\resources\\languages\\";

	private Properties languageProperties;

	private static final String LANGUAGE_FILE_SUFFIX = ".lrf";

	private static final String AVAILABLE_FILE_NAME = "available.txt";

	private static final String CURRENT_LANGUAGE_PROPERTY = "currentLanguage";

}
