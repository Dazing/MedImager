/**
 * @(#) DataComponentPackageUtilities.java
 */

package medview.common.data;

import java.util.*;

import medview.datahandling.*;

/**
 * Utility class relieving the application classes from the details of
 * storing and retrieving data component package data.
 * 
 * The data component package information is stored in the preferences,
 * using the preferences tree (see Java API). Global packages are defined
 * as packages that can be used (and should be visible / selectable) in 
 * all applications, while included packages are application-specific. When
 * dealing with the included package methods, you need to specify a class
 * which will be used to find the preference node in which to store the
 * included package data.
 * 
 * For listening to changes in the package data, an application class must
 * register as a preference listener, and react when the sync flag preferences
 * have been changed. A sync flag preference is changed when the underlying
 * preference data has been completely updated, and is always called after all
 * updating has been done. Individual preference listening should not be done
 * since it might be in the middle of a complete preference update, which may
 * involve several nodes. The sync flags are placed in the constants interface
 * found in this package.
 * 
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class DataComponentPackageUtilities implements DataComponentPackageConstants
{
	// PREFIX (placed here and not in constants since they are internal)

	private static final String GLOBAL_PACKAGE_PREFIX =  "globalPackage";

	private static final String DATA_COMPONENT_PACKAGE_PREFIX = "dataComponentPackage";


	// SUFFIX (placed here and not in constants since they are internal)

	private static final String PACKAGE_NAME_SUFFIX = "Name";

	private static final String USES_EXAMINATION_SUFFIX = "UsesExamination";

	private static final String USES_TEMPLATE_SUFFIX = "UsesTemplate";

	private static final String USES_TRANSLATOR_SUFFIX = "UsesTranslator";
    
    private static final String USES_GRAPH_SUFFIX = "UsesGraph";

    private static final String EXAMINATION_LOCATION_SUFFIX = "ExaminationLocation";

	private static final String TEMPLATE_LOCATION_SUFFIX = "TemplateLocation";

	private static final String TRANSLATOR_LOCATION_SUFFIX = "TranslatorLocation";
	
	private static final String DATABASE_LOCATION_SUFFIX = "DatabaseLocation";

	private static final String TERMVALUES_LOCATION_SUFFIX = "TermValuesLocation";
    
    private static final String GRAPH_LOCATION_SUFFIX = "GraphLocation";

    private static final String TERMDEFINITIONS_LOCATION_SUFFIX = "TermDefinitionsLocation";
    
    private static MedViewDataHandler mVDH = MedViewDataHandler.instance();
	

	/**
	 * Obtain an array of all current global packages.
	 * @return DataComponentPackage[]
	 */
	public static DataComponentPackage[] getGlobalPackages( )
	{
		String packageName = null;
		
		Vector retVect = new Vector();
		
		int ctr = 0;
		
		while (true)
		{
			packageName = mVDH.getUserStringPreference(GLOBAL_PACKAGE_PREFIX + ctr +
			
				PACKAGE_NAME_SUFFIX, null, DataComponentPackageConstants.class);
				
			if (packageName == null)
			{
				break; // no more global packages in preferences	
			}
			else
			{
				DataComponentPackage pack = new DefaultDataComponentPackage();
				
				pack.setPackageName(packageName);
				
				pack.setUsesTemplate(mVDH.getUserBooleanPreference(GLOBAL_PACKAGE_PREFIX + 

					ctr + USES_TEMPLATE_SUFFIX, false, DataComponentPackageConstants.class));
					
				pack.setUsesTranslator(mVDH.getUserBooleanPreference(GLOBAL_PACKAGE_PREFIX + 

					ctr + USES_TRANSLATOR_SUFFIX, false, DataComponentPackageConstants.class));
					
				pack.setUsesGraph(mVDH.getUserBooleanPreference(GLOBAL_PACKAGE_PREFIX + 

					ctr + USES_GRAPH_SUFFIX, false, DataComponentPackageConstants.class));
                
                pack.setExaminationLocation(mVDH.getUserStringPreference(GLOBAL_PACKAGE_PREFIX +
				
					ctr + EXAMINATION_LOCATION_SUFFIX, null, DataComponentPackageConstants.class)); // ok to set null (not used)
					
				pack.setDatabaseLocation(mVDH.getUserStringPreference(GLOBAL_PACKAGE_PREFIX +
				
					ctr + DATABASE_LOCATION_SUFFIX, null, DataComponentPackageConstants.class));
                
				pack.setTermDefinitionsLocation(mVDH.getUserStringPreference(GLOBAL_PACKAGE_PREFIX +
				
					ctr + TERMDEFINITIONS_LOCATION_SUFFIX, null, DataComponentPackageConstants.class));
                
				pack.setTermValuesLocation(mVDH.getUserStringPreference(GLOBAL_PACKAGE_PREFIX +
				
					ctr + TERMVALUES_LOCATION_SUFFIX, null, DataComponentPackageConstants.class));
                
				pack.setGraphLocation(mVDH.getUserStringPreference(GLOBAL_PACKAGE_PREFIX +
				
					ctr + GRAPH_LOCATION_SUFFIX, null, DataComponentPackageConstants.class));
                pack.setTemplateLocation(mVDH.getUserStringPreference(GLOBAL_PACKAGE_PREFIX +

					ctr + TEMPLATE_LOCATION_SUFFIX, null, DataComponentPackageConstants.class)); // ok to set null (not used)
					
				pack.setTranslatorLocation(mVDH.getUserStringPreference(GLOBAL_PACKAGE_PREFIX +

					ctr + TRANSLATOR_LOCATION_SUFFIX, null, DataComponentPackageConstants.class)); // ok to set null (not used)
					
				retVect.add(pack);
				
				ctr++;
			}
		}
		
		if (retVect.size() != 0)
		{
			DataComponentPackage[] retArr = new DataComponentPackage[retVect.size()];
			
			retVect.toArray(retArr);
			
			return retArr;
		}
		else
		{
			return new DataComponentPackage[0];
		}
	}
	
	/**
	 * Sets the global packages to the specified ones. Will cause
	 * the global sync flag to be triggered.
	 * 
	 * @param packages DataComponentPackage[]
	 */
	public static void synchronizeGlobalPackages(DataComponentPackage[] packages)
	{
		// store package info
		
		for (int ctr=0; ctr<packages.length; ctr++)
		{		
			mVDH.setUserStringPreference(GLOBAL_PACKAGE_PREFIX + ctr + PACKAGE_NAME_SUFFIX,
			
				packages[ctr].getPackageName(), DataComponentPackageConstants.class);
				
			mVDH.setUserBooleanPreference(GLOBAL_PACKAGE_PREFIX + ctr + USES_TEMPLATE_SUFFIX,

				packages[ctr].usesTemplate(), DataComponentPackageConstants.class);
            
			mVDH.setUserBooleanPreference(GLOBAL_PACKAGE_PREFIX + ctr + USES_GRAPH_SUFFIX,

				packages[ctr].usesGraph(), DataComponentPackageConstants.class);
				
            mVDH.setUserBooleanPreference(GLOBAL_PACKAGE_PREFIX + ctr + USES_TRANSLATOR_SUFFIX,

				packages[ctr].usesTranslator(), DataComponentPackageConstants.class);
				
			mVDH.setUserStringPreference(GLOBAL_PACKAGE_PREFIX + ctr + EXAMINATION_LOCATION_SUFFIX,
	
					packages[ctr].getExaminationLocation(), DataComponentPackageConstants.class);			
			
			if (packages[ctr].usesTemplate())
			{
				mVDH.setUserStringPreference(GLOBAL_PACKAGE_PREFIX + ctr + TEMPLATE_LOCATION_SUFFIX,
	
					packages[ctr].getTemplateLocation(), DataComponentPackageConstants.class);
			}
			else
			{
				mVDH.clearUserPreference(GLOBAL_PACKAGE_PREFIX + ctr + TEMPLATE_LOCATION_SUFFIX,

					DataComponentPackageConstants.class);
			}
			
			if (packages[ctr].usesTranslator())
			{
				mVDH.setUserStringPreference(GLOBAL_PACKAGE_PREFIX + ctr + TRANSLATOR_LOCATION_SUFFIX,
	
					packages[ctr].getTranslatorLocation(), DataComponentPackageConstants.class);				
			}
			else
			{
				mVDH.clearUserPreference(GLOBAL_PACKAGE_PREFIX + ctr + TRANSLATOR_LOCATION_SUFFIX,

					DataComponentPackageConstants.class);
			}
            
			if (packages[ctr].usesGraph())
			{
				mVDH.setUserStringPreference(GLOBAL_PACKAGE_PREFIX + ctr + GRAPH_LOCATION_SUFFIX,
	
					packages[ctr].getGraphLocation(), DataComponentPackageConstants.class);				
			}
			else
			{
				mVDH.clearUserPreference(GLOBAL_PACKAGE_PREFIX + ctr + GRAPH_LOCATION_SUFFIX,

					DataComponentPackageConstants.class);
			}
            
            if(packages[ctr].getDatabaseLocation()!=null)
            {
            mVDH.setUserStringPreference(GLOBAL_PACKAGE_PREFIX + ctr + DATABASE_LOCATION_SUFFIX,	
					packages[ctr].getDatabaseLocation(), DataComponentPackageConstants.class);
            }
			
            if(packages[ctr].getTermDefinitionsLocation()!=null)
            {
            mVDH.setUserStringPreference(GLOBAL_PACKAGE_PREFIX + ctr + TERMDEFINITIONS_LOCATION_SUFFIX,	
					packages[ctr].getTermDefinitionsLocation(), DataComponentPackageConstants.class);
            }
            
            if(packages[ctr].getTermValuesLocation()!=null)
            {
            mVDH.setUserStringPreference(GLOBAL_PACKAGE_PREFIX + ctr + TERMVALUES_LOCATION_SUFFIX,	
					packages[ctr].getTermValuesLocation(), DataComponentPackageConstants.class);
            }
        }
		
		// strip trailing prefs (if any)

		int ctr = packages.length;

		while (true)
		{
			if (mVDH.isUserPreferenceSet(GLOBAL_PACKAGE_PREFIX + ctr + PACKAGE_NAME_SUFFIX, DataComponentPackageConstants.class))
			{
				mVDH.clearUserPreference(GLOBAL_PACKAGE_PREFIX + ctr + PACKAGE_NAME_SUFFIX, DataComponentPackageConstants.class);
				
				mVDH.clearUserPreference(GLOBAL_PACKAGE_PREFIX + ctr + USES_EXAMINATION_SUFFIX, DataComponentPackageConstants.class);
				
				mVDH.clearUserPreference(GLOBAL_PACKAGE_PREFIX + ctr + USES_TEMPLATE_SUFFIX, DataComponentPackageConstants.class);
                
                mVDH.clearUserPreference(GLOBAL_PACKAGE_PREFIX + ctr + USES_GRAPH_SUFFIX, DataComponentPackageConstants.class);
				
                mVDH.clearUserPreference(GLOBAL_PACKAGE_PREFIX + ctr + USES_TRANSLATOR_SUFFIX, DataComponentPackageConstants.class);
				
				mVDH.clearUserPreference(GLOBAL_PACKAGE_PREFIX + ctr + EXAMINATION_LOCATION_SUFFIX, DataComponentPackageConstants.class);
                
                mVDH.clearUserPreference(GLOBAL_PACKAGE_PREFIX + ctr + GRAPH_LOCATION_SUFFIX, DataComponentPackageConstants.class);
				
                mVDH.clearUserPreference(GLOBAL_PACKAGE_PREFIX + ctr + TEMPLATE_LOCATION_SUFFIX, DataComponentPackageConstants.class);
				
				mVDH.clearUserPreference(GLOBAL_PACKAGE_PREFIX + ctr + TRANSLATOR_LOCATION_SUFFIX, DataComponentPackageConstants.class);
				
				ctr++;
			}
			else
			{
				break; // exit while loop
			}
		}
		
		// switch notify flag -> indicate to listeners that the package info has changed
		
		boolean currentFlag = mVDH.getUserBooleanPreference(GLOBAL_PACKAGE_SYNC_FLAG, 
		
			false, DataComponentPackageConstants.class);
		
		mVDH.setUserBooleanPreference(GLOBAL_PACKAGE_SYNC_FLAG, !currentFlag, 
		
			DataComponentPackageConstants.class);
	}
	
	/**
	 * Obtain a global package with the specified name.
	 * @param packageName String
	 * @return DataComponentPackage
	 * @throws NoSuchPackageException
	 */
	public static DataComponentPackage getGlobalPackage(String packageName) throws NoSuchPackageException
	{
		DataComponentPackage[] all = getGlobalPackages();
		
		for (int ctr=0; ctr<all.length; ctr++)
		{
			if (all[ctr].getPackageName().equals(packageName))
			{
				return all[ctr];
			}
		}
		
		throw new NoSuchPackageException(packageName + " does not exist!");
	}
	
	/**
	 * Use this method to obtain the corresponding DataComponentPackages
	 * that have been mapped (by name) in the preferences, using the
	 * specified preference class.
	 * 
	 * Example:
	 * 
	 * <PREFERENCE_CLASS_NODE>
	 *    dataComponentPackage0 = Klinikens paket
	 *    dataComponentPackage1 = Specialist paket
	 * 
	 * The method will return the corresponding DataComponentPackage
	 * objects with the names 'Klinikens paket' and 'Specialist paket'.
	 */
	public static DataComponentPackage[] obtainIncludedPackages(Class prefClass)
	{
		int ctr = 0;

		Vector vector = new Vector();

		while (true) // we don't know how many there are
		{	
			String name = mVDH.getUserStringPreference(DATA_COMPONENT_PACKAGE_PREFIX + ctr++, 

				null, prefClass);

			if (name == null)
			{
				break; // no more data packages to include
			}
			else
			{
				try
				{
					vector.add(DataComponentPackageUtilities.getGlobalPackage(name));
				}
				catch (NoSuchPackageException exc)
				{
					exc.printStackTrace();

					continue;
				}
			}
		}

		DataComponentPackage[] retArr = new DataComponentPackage[vector.size()];
		
		vector.toArray(retArr);
		
		Arrays.sort(retArr);
		
		return retArr;
	}

	/**
	 * Sets the global packages to the specified ones. Will cause
	 * the global sync flag to be triggered.
	 * @param includedPackages DataComponentPackage[]
	 * @param prefClass Class
	 */
	public static void synchronizeIncludedPackages(DataComponentPackage[] includedPackages, Class prefClass)
	{
		// set the names
		
		for (int ctr=0; ctr<includedPackages.length; ctr++)
		{
			mVDH.setUserStringPreference(DATA_COMPONENT_PACKAGE_PREFIX + ctr,

				includedPackages[ctr].getPackageName(), prefClass);
		}

		// strip trailing included prefs (if any)

		int ctr = includedPackages.length;

		while (true)
		{
			if (mVDH.isUserPreferenceSet(DATA_COMPONENT_PACKAGE_PREFIX + ctr, prefClass))
			{
				mVDH.clearUserPreference(DATA_COMPONENT_PACKAGE_PREFIX + ctr++, prefClass);
			}
			else
			{
				break; // exit while loop
			}
		}

		// switch notify flag -> indicate to listeners that included package info has changed

		boolean currentFlag = mVDH.getUserBooleanPreference(INCLUDED_PACKAGE_SYNC_FLAG, 

			false, DataComponentPackageConstants.class);

		mVDH.setUserBooleanPreference(INCLUDED_PACKAGE_SYNC_FLAG, !currentFlag, 

			DataComponentPackageConstants.class);
	}
}
