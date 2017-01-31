/*
 * @(#)MedSummaryModel.java
 *
 * $Id: MedSummaryModel.java,v 1.30 2008/11/27 18:36:57 it2aran Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.model;

import medview.common.data.*;
import medview.common.generator.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;
import medview.medsummary.model.exceptions.CouldNotRetrievePatientsException;
import medview.medsummary.model.exceptions.CouldNotSetException;
import medview.medsummary.model.exceptions.CouldNotRefreshExaminationsException;

import misc.foundation.*;

import java.io.*;

import javax.swing.event.*;
import javax.swing.text.*;

import se.chalmers.cs.medview.docgen.template.*;
import se.chalmers.cs.medview.docgen.translator.*;

/**
 * The main model of the MedSummary application.
 *
 * Event listeners can be sure that the state of the model is
 * consistent before the event is fired. This necessitates the
 * need for certain operations to be multi-part, such as setting
 * the current package. This is to avoid event notification
 * occuring in between mutation of the model.
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MedSummaryModel implements MedViewLanguageConstants,
	MedSummaryUserProperties, MedSummaryFlagProperties, MedSummaryConstants
{

	// REMOTE DATAHANDLING FLAG

	public void setUseRemoteDataHandling(boolean flag)
	{
		boolean set = mVDH.getUserBooleanPreference(USE_REMOTE_DATAHANDLING_PROPERTY,

			false, MedSummaryFlagProperties.class);

		if (set != flag) // only change and fire if differs
		{
			mVDH.setUserBooleanPreference(USE_REMOTE_DATAHANDLING_PROPERTY,

				flag, MedSummaryFlagProperties.class); // does not fire

			synchronizeDataAndProperties(); // might fire
		}
	}

	public boolean usesRemoteDataHandling()
	{
		return mVDH.getUserBooleanPreference(USE_REMOTE_DATAHANDLING_PROPERTY,

			false,  MedSummaryFlagProperties.class);
	}


	// SERVER LOCATION

	public void setServerLocation(String loc)
	{
		String set = mVDH.getUserStringPreference(SERVER_LOCATION_PROPERTY,

			"", MedSummaryUserProperties.class);

		if (!set.equalsIgnoreCase(loc)) // only change and fire if differs
		{
			mVDH.setUserStringPreference(SERVER_LOCATION_PROPERTY,

				loc, MedSummaryUserProperties.class); // does not fire

			synchronizeDataAndProperties(); // might fire
		}
	}

	public String getServerLocation()
	{
		return mVDH.getUserStringPreference(SERVER_LOCATION_PROPERTY,

			"", MedSummaryUserProperties.class);
	}


	// LOCAL EXAMINATION DATAHANDLING LOCATION

	public void setLocalExaminationDataLocation(String loc)
	{
		String set = mVDH.getUserStringPreference(LOCAL_EXAMINATION_DATA_LOCATION_PROPERTY,

			"", MedSummaryUserProperties.class);

		if (!set.equalsIgnoreCase(loc)) // only change and fire if differs
		{
			mVDH.setUserStringPreference(LOCAL_EXAMINATION_DATA_LOCATION_PROPERTY,

				loc, MedSummaryUserProperties.class);

			synchronizeDataAndProperties(); // might fire
		}
	}

	public String getLocalExaminationDataLocation() // only local
	{
		return mVDH.getUserStringPreference(LOCAL_EXAMINATION_DATA_LOCATION_PROPERTY,

			"", MedSummaryUserProperties.class);
	}

	public boolean isExaminationDataLocationSet()
	{
		return mVDH.isExaminationDataLocationSet();
	}

	// LOCAL PDA DATA LOCATION

	public void setLocalPDADataLocation(String loc)
	{
		String set = mVDH.getUserStringPreference(LOCAL_PDA_DATA_LOCATION_PROPERTY,

			"", MedSummaryUserProperties.class);

		if (!set.equalsIgnoreCase(loc)) // only change and fire if differs
		{
			mVDH.setUserStringPreference(LOCAL_PDA_DATA_LOCATION_PROPERTY,

				loc, MedSummaryUserProperties.class);

			synchronizeDataAndProperties(); // might fire
		}
	}

	public String getLocalPDADataLocation() // only local
	{
		return mVDH.getUserStringPreference(LOCAL_PDA_DATA_LOCATION_PROPERTY,

			"", MedSummaryUserProperties.class);
	}

	public boolean isPDADataLocationSet()
	{
		return (mVDH.getUserStringPreference(LOCAL_PDA_DATA_LOCATION_PROPERTY, "", MedSummaryUserProperties.class) != null);
	}

	// LOCAL TERM DEFINITION LOCATION

	public void setLocalTermDefinitionLocation(String loc)
	{
		String set = mVDH.getUserStringPreference(LOCAL_TERM_DEFINITION_LOCATION_PROPERTY,

			"", MedSummaryUserProperties.class);

		if (!set.equalsIgnoreCase(loc)) // only change and fire if differs
		{
			mVDH.setUserStringPreference(LOCAL_TERM_DEFINITION_LOCATION_PROPERTY,

				loc, MedSummaryUserProperties.class); // does not fire

			synchronizeDataAndProperties(); // might fire
		}
	}

	public String getLocalTermDefinitionLocation() // only local
	{
		return mVDH.getUserStringPreference(LOCAL_TERM_DEFINITION_LOCATION_PROPERTY,

			"",  MedSummaryUserProperties.class);
	}

	public boolean isTermDefinitionLocationSet()
	{
		return mVDH.isTermDefinitionLocationSet();
	}


	// LOCAL TERM VALUE LOCATION

	public void setLocalTermValueLocation(String loc)
	{
		String set = mVDH.getUserStringPreference(LOCAL_TERM_VALUE_LOCATION_PROPERTY,

			"", MedSummaryUserProperties.class);

		if (!set.equalsIgnoreCase(loc)) // only change and fire if differs
		{
			mVDH.setUserStringPreference(LOCAL_TERM_VALUE_LOCATION_PROPERTY,

				loc, MedSummaryUserProperties.class); // does not fire

			synchronizeDataAndProperties(); // might fire
		}
	}

	public String getLocalTermValueLocation() // only local
	{
		return mVDH.getUserStringPreference(LOCAL_TERM_VALUE_LOCATION_PROPERTY,

			"", MedSummaryUserProperties.class);
	}

	public boolean isTermValueLocationSet()
	{
		return mVDH.isTermValueLocationSet();
	}


	// TREE MODEL

	public TreeModel getTreeModel( )
	{
		return treeModel;
	}

	public void clearTree()
	{
		treeModel.clearPatients();
	}

	private boolean treeContainsPatient(PatientIdentifier pid)
	{
		return (treeModel.contains(pid));
	}


	// PATIENT LISTING

	public PatientIdentifier[] getPatients() throws CouldNotRetrievePatientsException {
		return this.getPatients(null);
	}

	public PatientIdentifier[] getPatients( ProgressNotifiable notifiable ) throws CouldNotRetrievePatientsException
	{
		try
		{
			PatientIdentifier[] retArr = mVDH.getPatients(notifiable); // might take some time

			if (retArr == null)
			{
				return new PatientIdentifier[0];
			}
			else
			{
				return retArr;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();

			throw new CouldNotRetrievePatientsException(e.getMessage());
		}
	}


	// DOCUMENT HANDLING

	public void setDocument(StyledDocument doc)
	{
		if (doc == null)
		{
			clearDocument();
		}
		else
		{
			this.document = doc;

			fireDocumentReplaced();
		}
	}

	public void clearDocument()
	{
		if (containsDocument()) // don't do anything if not contains document
		{
			this.document = null;

			fireDocumentReplaced();
		}
	}

	public boolean containsDocument()
	{
		return (document != null);
	}

	public StyledDocument getDocument()
	{
		return document;
	}


	// SECTIONS

	public String[] getSections()
	{
		return sections;
	}

	public boolean containsSections()
	{
		return (sections != null);
	}


	// TEMPLATE

	public TemplateModelWrapper setTemplate(String filePath) throws CouldNotSetException {
		if (filePath == null)
		{
			clearTemplate();

			throw new CouldNotSetException("Cannot set null template!");
		}
		else
		{
			try
			{
				TemplateModelWrapper wrapper = mVDH.loadTemplateWrapper(filePath); // -> Exception

				TemplateModel templateModel = wrapper.getTemplateModel();

				gEB.buildTemplateModel(templateModel); // -> Exception

				sections = templateModel.getAllContainedSections();

				templateIsSet = true;

				fireTemplateChanged();

				fireTemplateIDChanged();

				fireSectionsChanged();

				return wrapper;

				/* NOTE: the engine builder will
				 * take care of firing events to
				 * the listeners caring about if
				 * the engine is complete or not,
				 * therefore it is not done here. */
			}
			catch (Exception e)
			{
				clearTemplate();

				e.printStackTrace();

				throw new CouldNotSetException(e.getMessage());
			}
		}
	}

	public void clearTemplate()
	{
		boolean templateChangeEventPending = false;

		if (templateIsSet)
		{
			gEB.removeTemplateModel();

			templateIsSet = false;

			templateChangeEventPending = true;
		}

		boolean sectionsChangeEventPending = false;

		if (sections != null)
		{
			sections = null;

			sectionsChangeEventPending = true;
		}

		if (templateChangeEventPending)
		{
			fireTemplateChanged(); // at notification time, state should be consistent

			fireTemplateIDChanged(); // at notification time, state should be consistent
		}

		if (sectionsChangeEventPending)
		{
			fireSectionsChanged(); // at notification time, state should be consistent
		}
	}

	public boolean isTemplateSet()
	{
		return templateIsSet;
	}

	public String getTemplateID()
	{
		if (!isTemplateSet())
		{
			return mVDH.getLanguageString(OTHER_NOT_SET_LS_PROPERTY);
		}
		else
		{
			return mVDH.parseTATDHLocation(currentPackage.getTemplateLocation());
		}
	}

	public String getTemplatePath()
	{
		if (!isTemplateSet())
		{
			return mVDH.getLanguageString(OTHER_NOT_SET_LS_PROPERTY);
		}
		else
		{
			return currentPackage.getTemplateLocation();
		}
	}


	// TRANSLATOR

	public void setTranslator(String filePath) throws CouldNotSetException
	{
		if (filePath == null)
		{
			clearTranslator();

			throw new CouldNotSetException("Cannot set null translator!");
		}
		else
		{
			try
			{
				TranslatorModel translatorModel = mVDH.loadTranslator(filePath); // -> Exception

				gEB.buildTranslatorModel(translatorModel); // -> Exception

				translatorIsSet = true;

				fireTranslatorChanged();

				fireTranslatorIDChanged();

				/* NOTE: the engine builder will
				 * take care of firing events to
				 * the listeners caring about if
				 * the engine is complete or not,
				 * therefore it is not done here. */
			}
			catch (Exception e)
			{
				clearTranslator();

				/* NOTE: we do not want to fire events here,
				   since the only reason we get here is because
				   of error out of our control, and we should
				   keep the previous setting. */

				e.printStackTrace();

				throw new CouldNotSetException(e.getMessage());
			}
		}
	}

	public void clearTranslator()
	{
		if (translatorIsSet)
		{
			gEB.removeTranslatorModel();

			translatorIsSet = false;

			fireTranslatorChanged();

			fireTranslatorIDChanged();
		}
	}

	public boolean containsTranslator()
	{
		return translatorIsSet;
	}

	public String getTranslatorID()
	{
		if (!containsTranslator())
		{
			return mVDH.getLanguageString(OTHER_NOT_SET_LS_PROPERTY);
		}
		else
		{
			return mVDH.parseTATDHLocation(currentPackage.getTranslatorLocation());
		}
	}

	public String getTranslatorPath()
	{
		if (!containsTranslator())
		{
			return mVDH.getLanguageString(OTHER_NOT_SET_LS_PROPERTY);
		}
		else
		{
			return currentPackage.getTranslatorLocation();
		}
	}


	// EXAMINATION DATA REFRESH

	public void refreshExaminations() throws CouldNotRefreshExaminationsException {
		try
		{
			ExaminationIdentifier[] eI = mVDH.refreshExaminations(); // -> IOException

			if ((eI == null) || (eI.length <= 0))
			{
				return;
			}

			for (int ctr=0; ctr<eI.length; ctr++)
			{
				if (treeContainsPatient(eI[ctr].getPID()))
				{
					treeModel.addExaminationToPatient(eI[ctr], eI[ctr].getPID()); // wont add if already there
				}
			}

			firePatientsChanged(); // might not have - PENDING

			/* NOTE: the tree model fires its own events, but we need
			   to fire an event indicating that the patients listing
			   have changed, since this might affect other parts of the
			   application other than the patients in the tree. */
		}
		catch (IOException e)
		{
			throw new CouldNotRefreshExaminationsException(e.getMessage());
		}
	}


	// GENERATOR ENGINE BUILDER

	public MedViewGeneratorEngineBuilder getGeneratorEngineBuilder()
	{
		return gEB;
	}


	// DATA COMPONENT PACKAGES

	public void setCurrentPackage(DataComponentPackage pack)
	{
		if (pack == null)
		{
			clearCurrentPackage();
		}
		else
		{
			// update current package

			mVDH.setUserStringPreference(LAST_DATA_COMPONENT_PACKAGE_PROPERTY,

				pack.getPackageName(), MedSummaryUserProperties.class);

			currentPackage = pack;

			fireCurrentPackageChanged();
		}
	}

	public DataComponentPackage getCurrentPackage()
	{
		return currentPackage;
	}

	public void clearCurrentPackage()
	{
		if (isCurrentPackageSet())
		{
			currentPackage = null;

			clearTemplate();

			clearTranslator();

			fireCurrentPackageChanged();
		}
	}

	public boolean isCurrentPackageSet()
	{
		return (currentPackage != null);
	}

	public DataComponentPackage[] getIncludedPackages()
	{
		return includedPackages;
	}

	private void synchronizePackages() // will also 'refresh' the current package if set
	{
		includedPackages = DataComponentPackageUtilities.obtainIncludedPackages(MedSummaryConstants.class);

		if (isCurrentPackageSet())
		{
			// see if current package is still in array

			boolean foundCurrentPackage = false;

			for (int ctr = 0; ctr < includedPackages.length; ctr++)
			{
				if (includedPackages[ctr].getPackageName().equals(getCurrentPackage().getPackageName()))
				{
					foundCurrentPackage = true;

					try
					{
						setCurrentPackage(includedPackages[ctr]); // replace package (template / translator location might changed)

						setLocalExaminationDataLocation(includedPackages[ctr].getDatabaseLocation());
                        
                        setTemplate(includedPackages[ctr].getTemplateLocation());

						setTranslator(includedPackages[ctr].getTranslatorLocation());
                        
                    }
					catch (Exception exc) // package might not have template / translator set
					{
						exc.printStackTrace();
					}
				}
			}

			if (!foundCurrentPackage)
			{
				clearCurrentPackage(); // <- if we get here, we couldn't find the current package in the included packages
			}
		}

		fireIncludedPackagesUpdated();
	}


	// EVENT FIRING AND HANDLING

	public void removeMedSummaryModelListener( MedSummaryModelListener listener )
	{
		listenerList.remove(MedSummaryModelListener.class, listener);
	}

	public void addMedSummaryModelListener( MedSummaryModelListener listener )
	{
		listenerList.add(MedSummaryModelListener.class, listener);
	}

	private void fireDataLocationChanged( )
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedSummaryModelListener.class)
			{
				((MedSummaryModelListener)listeners[i+1]).dataLocationChanged(sharedEvent);
			}
		}
	}

	private void fireDataLocationIDChanged( )
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedSummaryModelListener.class)
			{
				((MedSummaryModelListener)listeners[i+1]).dataLocationIDChanged(sharedEvent);
			}
		}
	}

	private void firePatientsChanged( )
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedSummaryModelListener.class)
			{
				((MedSummaryModelListener)listeners[i+1]).patientsChanged(sharedEvent);
			}
		}
	}

	private void fireSectionsChanged( )
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedSummaryModelListener.class)
			{
				((MedSummaryModelListener)listeners[i+1]).sectionsChanged(sharedEvent);
			}
		}
	}

	private void fireTemplateChanged( )
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedSummaryModelListener.class)
			{
				((MedSummaryModelListener)listeners[i+1]).templateChanged(sharedEvent);
			}
		}
	}

	private void fireTemplateIDChanged( )
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedSummaryModelListener.class)
			{
				((MedSummaryModelListener)listeners[i+1]).templateIDChanged(sharedEvent);
			}
		}
	}

	private void fireTranslatorChanged( )
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedSummaryModelListener.class)
			{
				((MedSummaryModelListener)listeners[i+1]).translatorChanged(sharedEvent);
			}
		}
	}

	private void fireTranslatorIDChanged( )
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedSummaryModelListener.class)
			{
				((MedSummaryModelListener)listeners[i+1]).translatorIDChanged(sharedEvent);
			}
		}
	}

	private void fireDocumentReplaced()
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedSummaryModelListener.class)
			{
				((MedSummaryModelListener)listeners[i+1]).documentReplaced(sharedEvent);
			}
		}
	}

	private void fireIncludedPackagesUpdated()
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedSummaryModelListener.class)
			{
				((MedSummaryModelListener)listeners[i+1]).includedPackagesUpdated(sharedEvent);
			}
		}
	}

	private void fireCurrentPackageChanged()
	{
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedSummaryModelListener.class)
			{
				((MedSummaryModelListener)listeners[i+1]).currentPackageChanged(sharedEvent);
			}
		}
	}


	// UTILITY METHODS

	public boolean areTermLocationsValid() // might be either local or server
	{
		return (mVDH.isTermDefinitionLocationValid() && mVDH.isTermValueLocationValid());
	}

	public String getDataLocationID()
	{
		return mVDH.getExaminationDataLocationID();
	}

	private void synchronizeDataAndProperties() // sets data layer according to prefs
	{
		syncTermDataHandlerWithPrefs();

		syncExaminationDataHandlerWithPrefs();

		syncPCodeGeneratorWithPrefs();
	}

	private void syncTermDataHandlerWithPrefs()
	{
		if (usesRemoteDataHandling())
		{
			if(!mVDH.getTermDataHandlerInUse().equalsIgnoreCase(REMOTE_TERM_DATAHANDLER_CLASS_NAME)) // is data layer using remote term data handler?
			{
				try
				{
					mVDH.setTermDataHandlerToUse(REMOTE_TERM_DATAHANDLER_CLASS_NAME, true); // true - do not fire loc event
				}
				catch(Exception e)
				{
					e.printStackTrace();

					System.exit(1); // this is a fatal error
				}
			}

			if (mVDH.getUserStringPreference(SERVER_LOCATION_PROPERTY, null, MedSummaryUserProperties.class) != null)
			{
				mVDH.setTermDefinitionLocation(mVDH.getUserStringPreference(SERVER_LOCATION_PROPERTY,

					null, MedSummaryUserProperties.class)); // might fire loc change event

				mVDH.setTermValueLocation(mVDH.getUserStringPreference(SERVER_LOCATION_PROPERTY,

					null, MedSummaryUserProperties.class)); // might fire loc change event
			}
		}
		else
		{
            //don't do anything, all these settings handled in packages
            /*
            if (!mVDH.getTermDataHandlerInUse().equalsIgnoreCase(mVDH.getDefaultTermDataHandler()))
			{
				try
				{
					mVDH.setTermDataHandlerToUse(mVDH.getDefaultTermDataHandler(), true); // true - do not fire loc event
				}
				catch (Exception e)
				{
					e.printStackTrace();

					System.exit(1); // this is a fatal error
				}
			}

			if (mVDH.getUserStringPreference(LOCAL_TERM_DEFINITION_LOCATION_PROPERTY, null, MedSummaryUserProperties.class) != null)
			{
				mVDH.setTermDefinitionLocation(mVDH.getUserStringPreference(LOCAL_TERM_DEFINITION_LOCATION_PROPERTY,

					null, MedSummaryUserProperties.class)); // might fire term location change event
			}

			if (mVDH.getUserStringPreference(LOCAL_TERM_VALUE_LOCATION_PROPERTY, null, MedSummaryUserProperties.class) != null)
			{
				mVDH.setTermValueLocation(mVDH.getUserStringPreference(LOCAL_TERM_VALUE_LOCATION_PROPERTY,

					null, MedSummaryUserProperties.class)); // might fire term location change event
			}*/
		}
	}

	private void syncExaminationDataHandlerWithPrefs()
	{
		if (usesRemoteDataHandling())
		{
			if(!mVDH.getExaminationDataHandlerInUse().equalsIgnoreCase(REMOTE_EXAMINATION_DATAHANDLER_CLASS_NAME)) // is data layer using remote examination data handler?
			{
				try
				{
					mVDH.setExaminationDataHandlerToUse(REMOTE_EXAMINATION_DATAHANDLER_CLASS_NAME, true); // true - do not fire loc event
				}
				catch(Exception e)
				{
					e.printStackTrace();

					System.exit(1); // this is a fatal error
				}
			}

            
            if (mVDH.getUserStringPreference(SERVER_LOCATION_PROPERTY, null, MedSummaryUserProperties.class) != null)
			{
				mVDH.setExaminationDataLocation(mVDH.getUserStringPreference(SERVER_LOCATION_PROPERTY,

					null, MedSummaryUserProperties.class)); // might fire loc change event
			}
		}
		else
		{
            if (!mVDH.getExaminationDataHandlerInUse().equalsIgnoreCase(mVDH.getDefaultExaminationDataHandler()))
			{
				try
				{
					mVDH.setExaminationDataHandlerToUse(mVDH.getDefaultExaminationDataHandler(), true); // true - do not fire loc event
				}
				catch (Exception e)
				{
					e.printStackTrace();

					System.exit(1); // this is a fatal error
				}
			}
            if(currentPackage!=null)
            {
                if (currentPackage.getDatabaseLocation() != null)
                {
                    mVDH.setExaminationDataLocation(currentPackage.getDatabaseLocation()); // might fire examination data location change event
                }
                if (currentPackage.getTermDefinitionsLocation() != null)
                {
                    mVDH.setTermDefinitionLocation(currentPackage.getTermDefinitionsLocation());
                }
                if (currentPackage.getTermValuesLocation() != null)
                {
                    mVDH.setTermValueLocation(currentPackage.getTermValuesLocation());
                }
            }
   
        }
	}

	private void syncPCodeGeneratorWithPrefs()
	{
		if (usesRemoteDataHandling())
		{
			if (!mVDH.getPCodeGeneratorInUse().equalsIgnoreCase(REMOTE_PCODE_GENERATOR_CLASS_NAME))
			{
				try
				{
					mVDH.setPCodeGeneratorToUse(REMOTE_PCODE_GENERATOR_CLASS_NAME);
				}
				catch (Exception e)
				{
					e.printStackTrace();

					System.exit(1); // this is a fatal error
				}
			}

			// set the data layer locations to the set preference location

			if (mVDH.getUserStringPreference(SERVER_LOCATION_PROPERTY, null, MedSummaryUserProperties.class) != null)
			{
				mVDH.setPCodeNRGeneratorLocation(mVDH.getUserStringPreference(SERVER_LOCATION_PROPERTY,

					null, MedSummaryUserProperties.class)); // might fire loc change event
			}
		}
		else
		{
			// replace remote handler with local handler (if remote handler in use)

			if (!mVDH.getPCodeGeneratorInUse().equalsIgnoreCase(mVDH.getDefaultPCodeGenerator()))
			{
				try
				{
					mVDH.setPCodeGeneratorToUse(mVDH.getDefaultPCodeGenerator());
				}
				catch (Exception e)
				{
					e.printStackTrace();

					System.exit(1); // this is a fatal error
				}
			}

			if (mVDH.getUserStringPreference(LOCAL_PCODE_GENERATOR_NUMBER_GENERATOR_LOCATION_PROPERTY, null, MedSummaryUserProperties.class) != null)
			{
				mVDH.setPCodeNRGeneratorLocation(mVDH.getUserStringPreference(LOCAL_PCODE_GENERATOR_NUMBER_GENERATOR_LOCATION_PROPERTY,

					null, MedSummaryUserProperties.class));
			}
		}
	}


	// CONSTRUCTOR

	/**
	 * Constructs the model. Will try to use the
	 * last set template and translator locations.
	 * If preferences for remote datahandling and
	 * various data locations have been set before,
	 * the model will set up the datahandling layer
	 * according to the set values.
	 */
	public MedSummaryModel()
	{
        // set up included data component packages

		synchronizePackages(); // retrieves the packages from prefs into this class's package array
        
		// set up current package (if any)

		if (mVDH.isUserPreferenceSet(LAST_DATA_COMPONENT_PACKAGE_PROPERTY, MedSummaryUserProperties.class))
		{
			String pN = mVDH.getUserStringPreference(LAST_DATA_COMPONENT_PACKAGE_PROPERTY, null, MedSummaryUserProperties.class);

			if (pN != null)
			{
				for (int ctr=0; ctr<includedPackages.length; ctr++)
				{
					if (includedPackages[ctr].getPackageName().equals(pN))
					{
						try
						{
							setCurrentPackage(includedPackages[ctr]);
                            
                            setLocalExaminationDataLocation(includedPackages[ctr].getDatabaseLocation());

                            setTemplate(includedPackages[ctr].getTemplateLocation());

							setTranslator(includedPackages[ctr].getTranslatorLocation());
                            
                            
                        }
						catch (Exception exc)
						{
							exc.printStackTrace();
						}
					}
				}
			}
		}

        // set up datahandlers according to set values

		synchronizeDataAndProperties(); // no listeners attached yet
        
        // add listeners

		mVDH.addMedViewDataListener(dataList);

		mVDH.addMedViewPreferenceListener(prefList);
	}

	// MEMBERS

	private String[] sections = null;

	private StyledDocument document = null;

	private TreeModel treeModel = new TreeModel();


	private boolean templateIsSet = false;

	private boolean translatorIsSet = false;


	private MedViewDataHandler mVDH = MedViewDataHandler.instance();


	private EventListenerList listenerList = new EventListenerList();

	private DataHandlerListener dataList = new DataHandlerListener();

	private MedViewPreferenceListener prefList = new PreferenceListener();

	private MedSummaryModelEvent sharedEvent = new MedSummaryModelEvent(this);


	private MedViewGeneratorEngineBuilder gEB = new MedViewGeneratorEngineBuilder();


	private DataComponentPackage[] includedPackages = null; // set in constructor

	private DataComponentPackage currentPackage = null;


	// DATA LAYER DATA LISTENER

	/**
	 * Listens to the datahandling layer for changes that could
	 * affect the model's registered listeners or the tree model
	 * state.
	 */
	private class DataHandlerListener implements MedViewDataListener
	{
		public void examinationDataLocationChanged(MedViewDataEvent e)
		{
			clearDocument(); // fires

			clearTree(); // tree model will fire

			fireDataLocationChanged(); // ID fire in separate listener method

			firePatientsChanged();
		}

		public void examinationDataLocationIDChanged(MedViewDataEvent e)
		{
			fireDataLocationIDChanged();
		}

		public void termLocationChanged(MedViewDataEvent e)
		{
			clearDocument(); // fires
		}

		public void examinationAdded(MedViewDataEvent e)
		{
			firePatientsChanged(); // might not have - PENDING

			if (treeContainsPatient(e.getIdentifier().getPID()))
			{
				treeModel.addExaminationToPatient(e.getIdentifier(), e.getIdentifier().getPID());
			}
		}

		public void examinationUpdated(MedViewDataEvent e)
		{
			if (treeModel.contains(e.getIdentifier().getPID()))
			{
				treeModel.updateExamination(e.getIdentifier(), e.getIdentifier().getPID());
			}
		}

		public void examinationRemoved(MedViewDataEvent e)
		{
			// TODO: implement this
		}

		public void termDataHandlerChanged(MedViewDataEvent e) { }

		public void examinationDataHandlerChanged(MedViewDataEvent e) { }

		public void templateAndTranslatorDataHandlerChanged(MedViewDataEvent e) { }

		public void termAdded(MedViewDataEvent e) { }

		public void termRemoved(MedViewDataEvent e) { }

		public void valueAdded(MedViewDataEvent e) { }

		public void valueRemoved(MedViewDataEvent e) { }

		public void userIDChanged(MedViewDataEvent e) { }

		public void userNameChanged(MedViewDataEvent e) { }

		/* NOTE: we do not have to listen for datahandler
		 * changes, since each time the data layer changes
		 * a data handling class, it will also fire a data
		 * location change. This goes for both the examination
		 * data handler and the term data handler. The tree
		 * model will notify it's own listeners that the tree
		 * has been cleared, this is not the responsibility of
		 * this medsummary model class. */
	}

	/**
	 * Listens to the datahandling layer for changes that could
	 * affect various resources used in the application.
	 */
	private class PreferenceListener implements MedViewPreferenceListener
	{
		public void userPreferenceChanged(MedViewPreferenceEvent e)
		{
			if (e.getPreferenceName().equals(DataComponentPackageConstants.INCLUDED_PACKAGE_SYNC_FLAG))
			{
				synchronizePackages(); // can also indicate a package update
			}
		}

		public void systemPreferenceChanged(MedViewPreferenceEvent e)
		{
		}
	}
}
