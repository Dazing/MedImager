/*
 * @(#)SummaryCreatorModel.java
 *
 * $Id: SummaryCreatorModel.java,v 1.19 2005/06/03 15:45:43 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.model;

import java.io.*;

import java.util.*;

import javax.swing.event.*;
import javax.swing.text.*;

import medview.common.generator.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;

import misc.foundation.*;
import misc.foundation.io.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.template.*;
import se.chalmers.cs.medview.docgen.translator.*;

/**
 * The model for the summary creator application.
 * Contains a reference to the current template
 * model in use, as well as a reference to the
 * current translator model in use. If we were to
 * place the responsibilities of performing the
 * template actions, and the translator actions, in
 * this class the class would fast become bloated -
 * thus we let the template model and the translator
 * model act as 'secondary' facade controllers that
 * are accessed through this 'primary' facade
 * controller. The responsibility of keeping the models
 * is another thing - it is upheld by this class, which
 * is indicated by the class implementing the keeper
 * interfaces. By placing all the logic necessary to
 * load, save, close, new etc. regarding the models
 * in this class (in the domain layer), we increase the
 * application reusability since new GUI's can easily be
 * plugged in on top of this domain layer.
 *
 * @author Fredrik Lindahl
 */
public class SummaryCreatorModel implements TranslatorModelKeeper, TemplateModelKeeper,
	SummaryCreatorUserProperties, SummaryCreatorFlagProperties, SummaryCreatorConstants
{

// -------------------------------------------------------------------------------------
// ****************************** SETTING UP DATA HANDLING *****************************
// -------------------------------------------------------------------------------------

	/**
	 * Sets whether or not to use remote datahandling
	 * in the SummaryCreator application.
	 */
	public void setUseRemoteDataHandling(boolean flag)
	{
		String prefName = USE_REMOTE_DATAHANDLING_PROPERTY;

		Class flagPrefClass = SummaryCreatorFlagProperties.class;

		boolean set = mVDH.getUserBooleanPreference(prefName, false, flagPrefClass);

		if (set != flag) // only change if differs
		{
			mVDH.setUserBooleanPreference(prefName, flag, flagPrefClass); // does not fire
		}

		synchronizeDataAndProperties(); // might fire
	}

	/**
	 * Sets the server location used if remote datahandling
	 * is used in SummaryCreator.
	 */
	public void setServerLocation(String loc)
	{
		String prefName = SERVER_LOCATION_PROPERTY;

		Class userPrefClass = SummaryCreatorUserProperties.class;

		String set = mVDH.getUserStringPreference(prefName, "", userPrefClass);

		if (!set.equalsIgnoreCase(loc)) // only change if differs
		{
			mVDH.setUserStringPreference(prefName, loc, userPrefClass); // does not fire
		}

		synchronizeDataAndProperties(); // might fire
	}

	/**
	 * Sets the local term definition location to use if
	 * local datahandling is used in SummaryCreator.
	 */
	public void setLocalTermDefinitionLocation(String loc)
	{
		String prefName = LOCAL_TERM_DEFINITION_LOCATION_PROPERTY;

		Class userPrefClass = SummaryCreatorUserProperties.class;

		String set = mVDH.getUserStringPreference(prefName, "", userPrefClass);

		if (!set.equalsIgnoreCase(loc)) // only change if differs
		{
			mVDH.setUserStringPreference(prefName, loc, userPrefClass); // does not fire
		}

		synchronizeDataAndProperties(); // might fire
	}

	/**
	 * Sets the local term value location to use if
	 * local datahandling is used in SummaryCreator.
	 */
	public void setLocalTermValueLocation(String loc)
	{
		String prefName = LOCAL_TERM_VALUE_LOCATION_PROPERTY;

		Class userPrefClass = SummaryCreatorUserProperties.class;

		String set = mVDH.getUserStringPreference(prefName, "", userPrefClass);

		if (!set.equalsIgnoreCase(loc)) // only change if differs
		{
			mVDH.setUserStringPreference(prefName, loc, userPrefClass); // does not fire
		}

		synchronizeDataAndProperties(); // might fire
	}

	/**
	 * Synchronizes the set values for the various
	 * datahandling properties with the current state
	 * of the datahandling layer. For instance, if the
	 * flag stating that we are to use remote datahandling
	 * in SummaryCreator is set to true, and the data handling
	 * layer uses local datahandling, this method will set
	 * the datahandling layer state to use a remote data
	 * handler and set up its locations according to the
	 * location preferences used in SummaryCreator.
	 */
	private void synchronizeDataAndProperties()
	{
		String rTDHC = REMOTE_TERM_DATAHANDLER_CLASS_NAME;

		String prefName = USE_REMOTE_DATAHANDLING_PROPERTY;

		Class flagPrefClass = SummaryCreatorFlagProperties.class;

		Class userPrefClass = SummaryCreatorUserProperties.class;

		boolean remFlag = mVDH.getUserBooleanPreference(prefName, false, flagPrefClass); // false - default

		boolean dataLayerUsesRemote = mVDH.getTermDataHandlerInUse().equalsIgnoreCase(rTDHC);

		if (remFlag)
		{
			if (!dataLayerUsesRemote)
			{
				try
				{
					mVDH.setTermDataHandlerToUse(rTDHC, true); // true - do not fire term location changed event
				}
				catch (Exception e)
				{
					e.printStackTrace();

					System.exit(1); // this is a fatal error
				}
			}

			String remLocPref = SERVER_LOCATION_PROPERTY;

			String currSetLoc = mVDH.getTermDefinitionLocation(); // might be null (not set)

			String prefSetLoc = mVDH.getUserStringPreference(remLocPref, null, userPrefClass);

			if (prefSetLoc != null)
			{
				if (currSetLoc == null)
				{
					mVDH.setTermDefinitionLocation(prefSetLoc); // fires term location change event
				}
				else
				{
					if (!currSetLoc.equalsIgnoreCase(prefSetLoc))
					{
						mVDH.setTermDefinitionLocation(prefSetLoc); // fires term location change event
					}
				}
			}
		}
		else
		{
			if (dataLayerUsesRemote)
			{
				String dTDHC = mVDH.getDefaultTermDataHandler(); // returns class name of default

				try
				{
					mVDH.setTermDataHandlerToUse(dTDHC, true); // true - do not fire term location change event
				}
				catch (Exception e)
				{
					e.printStackTrace();

					System.exit(1); // this is a fatal error
				}
			}

			String localDefLocPref = LOCAL_TERM_DEFINITION_LOCATION_PROPERTY;

			String localValLocPref = LOCAL_TERM_VALUE_LOCATION_PROPERTY;

			String currSetDefLoc = mVDH.getTermDefinitionLocation();

			String currSetValLoc = mVDH.getTermValueLocation();

			String prefSetDefLoc = mVDH.getUserStringPreference(localDefLocPref, null, userPrefClass);

			String prefSetValLoc = mVDH.getUserStringPreference(localValLocPref, null, userPrefClass);

			if (prefSetDefLoc != null)
			{
				if ((currSetDefLoc == null) || (!currSetDefLoc.equalsIgnoreCase(prefSetDefLoc)))
				{
					mVDH.setTermDefinitionLocation(prefSetDefLoc); // fires term location change event
				}
			}

			if (prefSetValLoc != null)
			{
				if ((currSetValLoc == null) || (!currSetValLoc.equalsIgnoreCase(prefSetValLoc)))
				{
					mVDH.setTermValueLocation(prefSetValLoc); // fires term location change event
				}
			}
		}
	}

	/**
	 * Returns whether or not the SummaryCreator
	 * application uses remote datahandling (i.e.
	 * if the preference indicating this is set to
	 * a true value). If the preference is not set,
	 * a false value is returned (default).
	 */
	public boolean usesRemoteDataHandling()
	{
		String prefName = USE_REMOTE_DATAHANDLING_PROPERTY;

		Class flagPrefClass = SummaryCreatorFlagProperties.class;

		return mVDH.getUserBooleanPreference(prefName, false, flagPrefClass);
	}

	/**
	 * Returns the currently set server location
	 * used if remote datahandling is used. Will
	 * return an empty string if it is not set
	 * (the default).
	 */
	public String getServerLocation()
	{
		String prefName = SERVER_LOCATION_PROPERTY;

		Class userPrefClass = SummaryCreatorUserProperties.class;

		return mVDH.getUserStringPreference(prefName, "", userPrefClass);
	}

	/**
	 * Returns the currently set local term definition
	 * location used if local datahandling is in use.
	 * Will return an empty string if is is not set
	 * yet.
	 */
	public String getTermDefinitionLocation()
	{
		String prefName = LOCAL_TERM_DEFINITION_LOCATION_PROPERTY;

		Class userPrefClass = SummaryCreatorUserProperties.class;

		return mVDH.getUserStringPreference(prefName, "", userPrefClass);
	}

	/**
	 * Returns the currently set local term value
	 * location used if local datahandling is in use.
	 * Will return an empty string if it is not set
	 * yet.
	 */
	public String getTermValueLocation()
	{
		String prefName = LOCAL_TERM_VALUE_LOCATION_PROPERTY;

		Class userPrefClass = SummaryCreatorUserProperties.class;

		return mVDH.getUserStringPreference(prefName, "", userPrefClass);
	}

	/**
	 * Returns whether or not the term locations are
	 * valid. This method queries the data layer for
	 * this information by individually checking with
	 * the data layer if the definition and value
	 * locations are valid.
	 */
	public boolean areTermLocationsValid()
	{
		boolean b1 = mVDH.isTermDefinitionLocationValid();

		boolean b2 = mVDH.isTermValueLocationValid();

		return b1 && b2;
	}

// -------------------------------------------------------------------------------------
// *************************************************************************************
// -------------------------------------------------------------------------------------





	/**
	 * Obtains an array of all the terms found in the
	 * specified locations. There is no way that the
	 * summarycreator application can mutate the terms,
	 * (which is why push-from-below in event notification
	 * is done, i.e. the model listens to the data layer
	 * for changes in the term listings (or values)).
	 */
	public String[] getTerms() throws CouldNotRetrieveTermsException
	{
		try
		{
			String[] datTerms = mVDH.getTerms(); // obtain all global terms

			String[] derTerms = DerivedTermHandlerFactory.getDerivedTermHandler().getDerivedTerms(); // obtain all derived terms

			String[] retArr = new String[datTerms.length + derTerms.length];

			for (int ctr=0; ctr<datTerms.length; ctr++) // add all global terms to set
			{
				retArr[ctr] = datTerms[ctr];
			}

			for (int ctr = 0; ctr<derTerms.length; ctr++) // add all derived terms to set
			{
				retArr[datTerms.length + ctr] = derTerms[ctr];
			}

			return retArr;
		}
		catch (IOException e)
		{
			throw new CouldNotRetrieveTermsException(e.getMessage());
		}
	}

	/**
	 * Tries to generate a preview document, based on
	 * the currently set template and translator. In
	 * order for the method not to throw an exception,
	 * a template and a translator must have been set.
	 */
	public StyledDocument generatePreviewDocument() throws CouldNotGenerateException
	{
		if (!containsTranslatorModel())
		{
			throw new CouldNotGenerateException("No translator set");
		}

		if (!containsTemplateModel())
		{
			throw new CouldNotGenerateException("No template set");
		}

		try
		{

			String[] allSections = templateModel.getAllContainedSections();

			PatientIdentifier pid = mVDH.generateExamplePID();

			ValueContainer vC = GeneratorUtilities.createPreviewContainer(translatorModel);

			ExaminationIdentifier eI = new MedViewExaminationIdentifier(pid, new Date());

			ValueContainer[] vCs = new ValueContainer[] { vC };

			ExaminationIdentifier[] eIs = new ExaminationIdentifier[] { eI };

			gEB.removeAllBuilt();

			gEB.buildIdentifiers(eIs);

			gEB.buildValueContainers(vCs);

			gEB.buildSections(allSections);

			gEB.buildTemplateModel(templateModel);

			gEB.buildTranslatorModel(translatorModel);

			return gEB.getEngine().generateDocument();
		}
		catch (Exception e)
		{
			throw new CouldNotGenerateException(e.getMessage());
		}
	}

// -------------------------------------------------------------------------------------------
// ********************************* TRANSLATOR MODEL KEEPER *********************************
// -------------------------------------------------------------------------------------------

	/**
	 * Removes the kept translator model from this
	 * model, and notifies all listeners of the fact
	 * that it has been removed.
	 */
	public void clearTranslatorModel()
	{
		if (containsTranslatorModel())
		{
			translatorModel.removeAllListeners();
		}

		translatorModel = null;

		currTranslatorLocation = null;

		fireTranslatorModelChanged();

		fireTranslatorModelLocationChanged();
	}

	/**
	 * Returns whether or not the model keeps a
	 * translator model.
	 */
	public boolean containsTranslatorModel( )
	{
		return (translatorModel != null);
	}

	/**
	 * Returns the currently kept translator model.
	 */
	public TranslatorModel getTranslatorModel( )
	{
		return translatorModel;
	}

	/**
	 * Returns the location (if the translator has
	 * been saved or loaded) of the currently kept
	 * translator model
	 */
	public String getTranslatorModelLocation()
	{
		return currTranslatorLocation;
	}

	/**
	 * Returns whether or not the model currently
	 * keeps a translator model that has not yet
	 * been saved.
	 */
	public boolean containsNewTranslatorModel( ) // if translator model not null and location null -> new model
	{
		return ((translatorModel != null) && (currTranslatorLocation == null));
	}

	/**
	 * Sets the currently kept translator model to the one
	 * loaded from the specified filepath. Will also synchronize
	 * the loaded translator model's derived and global terms
	 * and add any terms not existant in the model but existant
	 * in the global / derived.
	 */
	public void loadTranslatorModel(String filePath) throws se.chalmers.cs.medview.docgen.misc.CouldNotLoadException
	{
		try
		{
			if (containsTranslatorModel())
			{
				translatorModel.removeAllListeners();
			}

			TranslatorModel model = mVDH.loadTranslator(filePath);

			MedViewGeneratorUtilities.synchronizeTranslatorModel(model); // synchronizes translator model with global and derived terms

			currTranslatorLocation = filePath;

			translatorModel = model;

			fireTranslatorModelChanged();

			fireTranslatorModelLocationChanged();
		}
		catch (Exception e)
		{
			throw new se.chalmers.cs.medview.docgen.misc.CouldNotLoadException(e.getMessage());
		}
	}

	/**
	 * Sets the currently kept translator model to a new
	 * one, synchronizes the terms (i.e. adds all global
	 * and derived terms to the model), and notifies all
	 * listeners of the fact.
	 */
	public void newTranslatorModel() throws se.chalmers.cs.medview.docgen.translator.CouldNotCreateException
	{
		try
		{
			if (containsTranslatorModel())
			{
				translatorModel.removeAllListeners();
			}

			TranslatorModel model = new TranslatorModel();

			MedViewGeneratorUtilities.synchronizeTranslatorModel(model); // adds global and derived terms & values to translator model

			currTranslatorLocation = null;

			translatorModel = model;

			fireTranslatorModelChanged();

			fireTranslatorModelLocationChanged();
		}
		catch (Exception e)
		{
			throw new se.chalmers.cs.medview.docgen.translator.CouldNotCreateException(e.getMessage());
		}
	}

	/**
	 * Saves the kept translator model to the specified
	 * filepath. If the filepath differs from the previous
	 * one, a notification is sent to listeners.
	 */
	public void saveTranslatorModel(String filePath) throws se.chalmers.cs.medview.docgen.misc.CouldNotSaveException
	{
		try
		{
			mVDH.saveTranslator(translatorModel, filePath);

			if ((currTranslatorLocation == null) || (!currTranslatorLocation.equals(filePath)))
			{
				currTranslatorLocation = filePath;

				fireTranslatorModelLocationChanged();
			}
		}
		catch (Exception e)
		{
			throw new se.chalmers.cs.medview.docgen.misc.CouldNotSaveException(e.getMessage());
		}
	}

// -------------------------------------------------------------------------------------------
// *******************************************************************************************
// -------------------------------------------------------------------------------------------



// -------------------------------------------------------------------------------------------
// ********************************** TEMPLATE MODEL KEEPER **********************************
// -------------------------------------------------------------------------------------------

	/**
	 * This method will 'close' the currently kept
	 * template model and the reference to the model
	 * will point subsequently point to null. It will
	 * also reset the value of the current template
	 * location member and any eventual associated
	 * translator information. Listeners will be
	 * notified of 'template model changed' and
	 * 'template model location changed' events.
	 */
	public void clearTemplateModel()
	{
		if (containsTemplateModel())
		{
			templateModel.removeAllListeners();
		}

		templateAssLocation = null; // associated translator location

		currTemplateLocation = null;

		templateModel = null;

		fireTemplateModelChanged();

		fireTemplateModelLocationChanged();
	}

	/**
	 * This method will create a blank and new
	 * template model and associate the SC model
	 * with it. It will clear any eventual
	 * association to a translator as well as set
	 * the current template location to a null
	 * value.
	 */
	public void newTemplateModel()
	{
		if (templateModel != null)
		{
			templateModel.removeAllTemplateModelListeners();
		}

		templateAssLocation = null; // associated translator location

		currTemplateLocation = null;

		templateModel = new TemplateModel();

		fireTemplateModelChanged();

		fireTemplateModelLocationChanged();
	}

	/**
	 * Returns whether or not the template model
	 * keeper contains a template model.
	 */
	public boolean containsTemplateModel()
	{
		return (templateModel != null);
	}

	/**
	 * Returns the currently kept template model.
	 */
	public TemplateModel getTemplateModel( )
	{
		return templateModel;
	}

	/**
	 * Returns the current template model location
	 * (i.e.the location where the template model kept
	 * has been loaded from or stored to).
	 */
	public String getTemplateModelLocation()
	{
		return currTemplateLocation;
	}

	/**
	 * Returns whether or not the template keeper contains
	 * a new template model.
	 */
	public boolean containsNewTemplateModel( )
	{
		return ((templateModel != null) && (currTemplateLocation == null));
	}

	/**
	 * Saves the currently kept template model to the specified
	 * filepath. If the currently kept template model has an
	 * associated translator model linked to it, this link will
	 * be rewritten into the new template model with the save,
	 * otherwise the template model will be written without any
	 * information about a linked-to translator model. Linkage
	 * information exists if a template model has been loaded
	 * that contained linkage information. This information is
	 * kept by the model no matter what the user decides to do
	 * with the linked-to translator (he/she may decide not to
	 * use it at all). Linkage information also exists after a
	 * used explicitly has requested to link the model with the
	 * translator (in which case the other version of the save
	 * template method will be called from the upper layer).
	 */
	public void saveTemplateModel(String filePath) throws se.chalmers.cs.medview.docgen.misc.CouldNotSaveException
	{
		this.saveTemplateModel(filePath, templateAssLocation);
	}

	/**
	 * Saves the currently kept template model to the specified
	 * filepath. Also, the specified translator location is linked
	 * with the template. If the version of the method is used
	 * without the tLoc parameter (only the filepath of the template
	 * model to save) the model is written with linkage-information
	 * only if a prior operation has shown that a linkage exists
	 * for the kept model (a load or explicit link using this version
	 * of the method).
	 * @param filePath the fully specified filepath of the file that
	 * the template model is to be written to.
	 * @param tLoc the fully specified location of the associated
	 * translator that should be written with the template model
	 * (can be null - in this case no linkage information is written
	 * with the model).
	 */
	public void saveTemplateModel(String filePath, String tLoc) throws se.chalmers.cs.medview.docgen.misc.CouldNotSaveException
	{
		try
		{
			TemplateModelWrapper wrapper = new TemplateModelWrapper(templateModel, filePath);

			if (tLoc != null)
			{
				templateAssLocation = tLoc;

				wrapper.setAssociatedTranslatorLocation(tLoc);
			}

			mVDH.saveTemplate(wrapper);

			String cTL = currTemplateLocation;

			if ((cTL == null) || !(cTL.equals(filePath))) // if path is different from earlier...
			{
				currTemplateLocation = filePath;

				fireTemplateModelLocationChanged();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new se.chalmers.cs.medview.docgen.misc.CouldNotSaveException(e.getMessage());
		}
	}

	/* NOTE: the current template location will not be set if the saveTemplateModel()
	 * method throws an exception. This will make sure that the previous template
	 * location remains intact, which is what it should if the specified path is invalid
	 * in some way. Also, when saving the template, the template model does not change
	 * in any way, so no 'template model changed' event is fired, the template location
	 * may change, though, so a 'template model location changed' event is fired. */

	public TemplateModelWrapper loadTemplateModel(String filePath) throws se.chalmers.cs.medview.docgen.misc.CouldNotLoadException
	{
		TemplateModelWrapper wrapper = mVDH.loadTemplateWrapper(filePath);

		templateModel = wrapper.getTemplateModel();

		if (wrapper.isAssociatedWithTranslator())
		{
			templateAssLocation = wrapper.getAssociatedTranslatorLocation();
		}
		else
		{
			templateAssLocation = null; // indicates no association exists
		}

		currTemplateLocation = filePath;

		fireTemplateModelLocationChanged();

		fireTemplateModelChanged();

		return wrapper; // pass to user (gui) for choosing if use associated translator
	}

// -------------------------------------------------------------------------------------------
// *******************************************************************************************
// -------------------------------------------------------------------------------------------



// -------------------------------------------------------------------------------------------
// *************************************** EVENT FIRING **************************************
// -------------------------------------------------------------------------------------------

	public void addTranslatorModelKeeperListener( TranslatorModelKeeperListener listener )
	{
		if (listenerList == null) { listenerList = new EventListenerList(); }

		listenerList.add(TranslatorModelKeeperListener.class, listener);
	}

	public void removeTranslatorModelKeeperListener( TranslatorModelKeeperListener listener )
	{
		if (listenerList == null) { return; }

		listenerList.remove(TranslatorModelKeeperListener.class, listener);
	}

	public void addTemplateModelKeeperListener( TemplateModelKeeperListener listener )
	{
		if (listenerList == null) { listenerList = new EventListenerList(); }

		listenerList.add(TemplateModelKeeperListener.class, listener);
	}

	public void removeTemplateModelKeeperListener( TemplateModelKeeperListener listener )
	{
		if (listenerList == null) { return; }

		listenerList.remove(TemplateModelKeeperListener.class, listener);
	}

	public void addSummaryCreatorModelListener(SummaryCreatorModelListener listener)
	{
		if (listenerList == null) { listenerList = new EventListenerList(); }

		listenerList.add(SummaryCreatorModelListener.class, listener);
	}

	public void removeSummaryCreatorModelListener(SummaryCreatorModelListener listener)
	{
		if (listenerList == null) { return; }

		listenerList.remove(SummaryCreatorModelListener.class, listener);
	}

	private void fireTranslatorModelChanged()
	{
		if (listenerList == null) { return; }

		Object[] listeners = listenerList.getListenerList();

		TranslatorModelKeeperEvent event = null;

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TranslatorModelKeeperListener.class)
			{
				if (event == null) { event = new TranslatorModelKeeperEvent(this); }

				((TranslatorModelKeeperListener)listeners[i+1]).translatorModelChanged(event);
			}
		}
	}

	private void fireTranslatorModelLocationChanged()
	{
		if (listenerList == null) { return; }

		Object[] listeners = listenerList.getListenerList();

		TranslatorModelKeeperEvent event = null;

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TranslatorModelKeeperListener.class)
			{
				if (event == null) { event = new TranslatorModelKeeperEvent(this); }

				((TranslatorModelKeeperListener)listeners[i+1]).translatorModelLocationChanged(event);
			}
		}
	}

	private void fireTemplateModelChanged()
	{
		if (listenerList == null) { return; }

		Object[] listeners = listenerList.getListenerList();

		TemplateModelKeeperEvent event = null;

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TemplateModelKeeperListener.class)
			{
				if (event == null) { event = new TemplateModelKeeperEvent(this); }

				((TemplateModelKeeperListener)listeners[i+1]).templateModelChanged(event);
			}
		}
	}

	private void fireTemplateModelLocationChanged()
	{
		if (listenerList == null) { return; }

		Object[] listeners = listenerList.getListenerList();

		TemplateModelKeeperEvent event = null;

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TemplateModelKeeperListener.class)
			{
				if (event == null) { event = new TemplateModelKeeperEvent(this); }

				((TemplateModelKeeperListener)listeners[i+1]).templateModelLocationChanged(event);
			}
		}
	}

	private void fireTermListingChanged()
	{
		if (listenerList == null) { return; }

		Object[] listeners = listenerList.getListenerList();

		SummaryCreatorModelEvent event = null;

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == SummaryCreatorModelListener.class)
			{
				if (event == null) { event = new SummaryCreatorModelEvent(this); }

				((SummaryCreatorModelListener)listeners[i+1]).termListingChanged(event);
			}
		}
	}

// -------------------------------------------------------------------------------------------
// *******************************************************************************************
// -------------------------------------------------------------------------------------------





	public SummaryCreatorModel()
	{
		// members

		templateAssLocation = null;

		mVDH = MedViewDataHandler.instance();

		gEB = new MedViewGeneratorEngineBuilder();

		// terms

		synchronizeDataAndProperties();

		// listeners

		mVDH.addMedViewDataListener(new DataListener());
	}

	private MedViewDataHandler mVDH;

	private MedViewGeneratorEngineBuilder gEB;

	private EventListenerList listenerList;

	private String currTemplateLocation = null;

	private String currTranslatorLocation = null;

	private TemplateModel templateModel = null;

	private TranslatorModel translatorModel = null;

	private String templateAssLocation; // location of template's associated translator




	private class DataListener extends MedViewDataAdapter
	{
		public void termAdded(MedViewDataEvent e)
		{
			fireTermListingChanged();

			if (containsTranslatorModel())
			{
				try
				{
					String typeDesc = TermHandlerFactory.getTermHandler().getTypeDescriptor(e.getTerm());

					translatorModel.addTranslationModel(TranslationModelFactoryCreator.getTranslationModelFactory().

						createTranslationModel(e.getTerm(), typeDesc)); // translator fires its own events
				}
				catch (Exception exc)
				{
					exc.printStackTrace();

					translatorModel.removeTranslationModel(e.getTerm());
				}
			}
		}

		public void termRemoved(MedViewDataEvent e)
		{
			fireTermListingChanged();

			if (containsTranslatorModel())
			{
				translatorModel.removeTranslationModel(e.getTerm()); // translator fires its own events
			}
		}

		public void valueAdded(MedViewDataEvent e)
		{
			if (containsTranslatorModel())
			{
				translatorModel.addValue(e.getTerm(), e.getValue()); // translator fires its own events
			}
		}

		public void valueRemoved(MedViewDataEvent e)
		{
			if (containsTranslatorModel())
			{
				translatorModel.removeValue(e.getTerm(), e.getValue()); // translator fires its own events
			}
		}

		public void termLocationChanged(MedViewDataEvent e)
		{
			fireTermListingChanged();

			if (containsTranslatorModel())
			{
				clearTranslatorModel();
			}
		}
	}

}
