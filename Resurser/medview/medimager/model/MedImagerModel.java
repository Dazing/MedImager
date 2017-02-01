/**
 * @(#) MedImagerModel.java
 */

package medview.medimager.model;

import java.awt.image.*;

import java.io.*;

import javax.swing.event.*;
import javax.swing.text.*;

import medview.datahandling.*;

import medview.medimager.foundation.*;

import misc.foundation.*;
import misc.foundation.text.*;

import se.chalmers.cs.medview.docgen.template.*;
import se.chalmers.cs.medview.docgen.translator.*;

/**
 */
public class MedImagerModel implements MedImagerConstants
{
	// PRIVATE MEMBERS

	private boolean initializing = true; // turned off at end of construction

	private NodeAdder nodeAdder = new NodeAdder();

	private NodeModel libraryRoot, myImagesRoot;

	private ChosenImagesSearchEngine cISearchEngine = new DefaultChosenImagesSearchEngine();

	private DatabaseSearchEngine dbSearchEngine = new DefaultDatabaseSearchEngine();

	private JournalHandler journalHandler = new JournalHandler();

	private NodeStorageHandler nodeStorageHandler = new ZipFileNodeStorageHandler();

	private EventListenerList listenerList = new EventListenerList();

	private UsabilityModel usabilityModel = new UsabilityModel();


	// CONSTRUCTOR(S)

	public MedImagerModel( )
	{
		// set up roots

		libraryRoot = new DefaultNonShareableFolderNodeModel("Mitt Bibliotek");

		myImagesRoot = new DefaultNonShareableFolderNodeModel("Mina Bilder");

		libraryRoot.add(myImagesRoot);

		// turn off 'initializing' flag

		initializing = false;

		// examination data location

		if (MedViewDataHandler.instance().getUserBooleanPreference(USE_REMOTE_DATAHANDLING_PROPERTY, false, MedImagerConstants.class))
		{
			setUseRemoteDataHandling(true); // will set up examination location
		}
		else
		{
			MedViewDataHandler.instance().setExaminationDataLocation( // already using local edh - set up location here

				MedViewDataHandler.instance().getUserStringPreference(

					LOCAL_EXAMINATION_DATA_LOCATION_PROPERTY, "", MedImagerConstants.class));
		}
	}


	// ADDITION TO NODE

	/**
	 * Adds an array of data base search results to the specified
	 * node model. If the specified node model is not of branch
	 * type, nothing will happen.
	 *
	 * @param results DatabaseImageSearchResult[]
	 * @param node NodeModel
	 */
	public void addToNode(final DatabaseImageSearchResult[] results, NodeModel node)
	{
		nodeAdder.addToNode(results, node);
	}

	/**
	 * Adds an array of files (image files) to the specified node
	 * model.
	 *
	 * @param files File[]
	 * @param node NodeModel
	 */
	public void addToNode(File[] files, NodeModel node)
	{
		nodeAdder.addToNode(files, node);
	}

	/**
	 * Adds an array of images to the specified node model.
	 *
	 * @param images BufferedImage[]
	 * @param node NodeModel
	 */
	public void addToNode(BufferedImage[] images, NodeModel node)
	{
		nodeAdder.addToNode(images, node);
	}


	// ALBUM

	/**
	 * Creates a new album with the specified name.
	 * @param name String specifing name of album to create.
	 */
	public void createNewAlbum(String name)
	{
		libraryRoot.add(new DefaultFolderNodeModel(name));
	}

	/**
	 * Closes the specified album.
	 * @param name String specifying name of album to close.
	 */
	public void closeAlbum(String name)
	{
		FolderNodeModel albumNode = getAlbumNode(name);

		if (albumNode != null)
		{
			libraryRoot.remove(albumNode);
		}
	}

	/**
	 * Exports the specified album to the specified filepath.
	 * @param name String indicating album to export.
	 * @param filePath String indicating path to file to export to.
	 * @throws CouldNotExportException if export failed.
	 */
	public void exportAlbum(FolderNodeModel albumNode, String filePath) throws CouldNotExportException
	{
		if (albumNode != null)
		{
			nodeStorageHandler.exportNode(filePath, albumNode);
		}
	}

	/**
	 * Imports the album with the specified filepath into the library.
	 * @param filePath String identifying the path to import from.
	 * @throws CouldNotImportException if the import fails.
	 */
	public void importAlbum(String filePath) throws CouldNotImportException
	{
		FolderNodeModel albumNode = nodeStorageHandler.importNode(filePath);

		libraryRoot.add(albumNode);
	}

	/**
	 * Returns the node model corresponding to the specified album.
	 * @param name String identifying the album you want the node for.
	 * @return FolderNodeModel the node model corresponding to the specified album.
	 */
	private FolderNodeModel getAlbumNode(String name)
	{
		NodeModel[] children = libraryRoot.getChildren();

		for (int ctr=0; ctr<children.length; ctr++)
		{
			if (children[ctr] instanceof FolderNodeModel)
			{
				FolderNodeModel currFolderNode = (FolderNodeModel) children[ctr];

				if (children[ctr].getDescription().equalsIgnoreCase(name))
				{
					return currFolderNode;
				}
			}
		}

		return null;
	}


	// SEARCH AMONGST KNOWLEDGE BASE AND ALBUM

	/**
	 * Search in the database for images matching the
	 * specified search string.
	 *
	 * @param searchText String
	 * @return DatabaseImageSearchResult[]
	 */
	public DatabaseImageSearchResult[] performDatabaseSearch( String searchText, ProgressNotifiable not )
		throws CouldNotParseException, CouldNotSearchException
	{
		return dbSearchEngine.search(searchText, not);
	}

	public int getMaximumHits()
	{
		return ((DefaultDatabaseSearchEngine)dbSearchEngine).getMaximumHits(); // potentially unsafe cast TODO
	}

	public void setMaximumHits(int maximumHits)
	{
		((DefaultDatabaseSearchEngine)dbSearchEngine).setMaximumHits(maximumHits); // potentially unsafe cast TODO
	}


	// ROOTS

	/**
	 * Obtains the library root node model.
	 * @return NodeModel
	 */
	public NodeModel getLibraryRoot( )
	{
		return libraryRoot;
	}

	/**
	 * Obtains the 'My Images' root node model.
	 * @return NodeModel
	 */
	public NodeModel getMyImagesRoot()
	{
		return myImagesRoot;
	}


	// USABILITY

	/**
	 * Obtain a reference to the usability model used
	 * in this application.
	 * @return UsabilityModel
	 */
	public UsabilityModel getUsabilityModel()
	{
		return usabilityModel;
	}


	// JOURNAL OBTAINING

	/**
	 * Generates a journal for the specified patient identifier.
	 * @param pid PatientIdentifier
	 * @param not ProgressNotifiable
	 * @return StyledDocument
	 * @throws CouldNotGenerateJournalException
	 */
	public StyledDocument getJournal(PatientIdentifier pid, ProgressNotifiable not) throws CouldNotGenerateJournalException
	{
		return journalHandler.getJournal(pid, not);
	}


	// JOURNAL GENERATION TEMPLATE

	public void setJournalTemplate(String templateLocation) throws CouldNotSetJournalTemplateException
	{
		journalHandler.setJournalTemplate(templateLocation); // -> CouldNotSetJournalTemplateException

		if (!initializing)
		{
			fireJournalTemplateChanged();
		}
	}

	/**
	 * Obtains the template model in use when generating journals.
	 *
	 * @return TemplateModel
	 * @throws CouldNotObtainTemplateException
	 */
	public TemplateModel getJournalTemplateModel() throws CouldNotObtainJournalTemplateException
	{
		return journalHandler.getJournalTemplateModel(); // -> CouldNotObtainJournalTemplateException
	}

	/**
	 * Obtains the journal template's location.
	 * @return String
	 */
	public String getJournalTemplateLocation()
	{
		return journalHandler.getJournalTemplateLocation();
	}

	/**
	 * Whether or not there is a template set to use
	 * when generating journals.
	 * @return boolean
	 */
	public boolean isJournalTemplateSet()
	{
		try
		{
			return (getJournalTemplateModel() != null);
		}
		catch (CouldNotObtainJournalTemplateException exc)
		{
			return false;
		}
	}


	// JOURNAL GENERATION TRANSLATOR

	public void setJournalTranslator(String translatorLocation) throws CouldNotSetJournalTranslatorException
	{
		journalHandler.setJournalTranslator(translatorLocation); // -> CouldNotSetJournalTranslatorException

		if (!initializing)
		{
			fireJournalTranslatorChanged();
		}
	}

	/**
	 * Obtains the translator to use when generating journals.
	 *
	 * @return TranslatorModel
	 * @throws CouldNotObtainTranslatorException
	 */
	public TranslatorModel getJournalTranslatorModel() throws CouldNotObtainJournalTranslatorException
	{
		return journalHandler.getJournalTranslatorModel(); // -> CouldNotObtainJournalTranslatorException
	}

	/**
	 * Obtain the journal translator's location.
	 * @return String
	 */
	public String getJournalTranslatorLocation()
	{
		return journalHandler.getJournalTranslatorLocation();
	}

	/**
	 * Whether or not there is a translator set to use
	 * when generating journals.
	 * @return boolean
	 */
	public boolean isJournalTranslatorSet()
	{
		try
		{
			return (getJournalTranslatorModel() != null);
		}
		catch (CouldNotObtainJournalTranslatorException exc)
		{
			return false;
		}
	}


	// JOURNAL GENERATION TERM DEFINITION AND VALUE LOCATIONS

	public void setLocalTermDefinitionLocation(String localTermDefinitionLocation)
	{
		journalHandler.setLocalTermDefinitionLocation(localTermDefinitionLocation);
	}

	public String getLocalTermDefinitionLocation()
	{
		return journalHandler.getLocalTermDefinitionLocation();
	}

	public void setLocalTermValueLocation(String localTermValueLocation)
	{
		journalHandler.setLocalTermValueLocation(localTermValueLocation);
	}

	public String getLocalTermValueLocation()
	{
		return journalHandler.getLocalTermValueLocation();
	}


	// EXAMINATION DATA LOCATION (WHERE IMAGES ARE SEARCHED)

	public void setLocalExaminationDataLocation(String location)
	{
		String currentLocation = getLocalExaminationDataLocation();

		if (currentLocation != location)
		{
			MedViewDataHandler.instance().setUserStringPreference(

				LOCAL_EXAMINATION_DATA_LOCATION_PROPERTY, location, MedImagerConstants.class);

			if (!usesRemoteDataHandling())
			{
				MedViewDataHandler.instance().setExaminationDataLocation(location); // change location if using local
			}
		}
	}

	public String getLocalExaminationDataLocation()
	{
		return MedViewDataHandler.instance().getUserStringPreference(

			LOCAL_EXAMINATION_DATA_LOCATION_PROPERTY, "", MedImagerConstants.class);
	}

	public void setRemoteExaminationDataLocation(String location)
	{
		String currentLocation = getRemoteExaminationDataLocation();

		if (currentLocation != location)
		{
			MedViewDataHandler.instance().setUserStringPreference(

				REMOTE_SERVER_LOCATION_PROPERTY, location, MedImagerConstants.class);

			if (usesRemoteDataHandling())
			{
				MedViewDataHandler.instance().setExaminationDataLocation(location); // change location if using remote
			}
		}
	}

	public String getRemoteExaminationDataLocation()
	{
		return MedViewDataHandler.instance().getUserStringPreference(

			REMOTE_SERVER_LOCATION_PROPERTY, "", MedImagerConstants.class);
	}

	public void setUseRemoteDataHandling(boolean useRemote)
	{
		MedViewDataHandler.instance().setUserBooleanPreference(

			USE_REMOTE_DATAHANDLING_PROPERTY, useRemote, MedImagerConstants.class); // remember setting

		if (useRemote)
		{
			if (usesRemoteDataHandling())
			{
				return; // already using remote datahandling
			}
			else
			{
				// examination data handler setup

				MedViewDataHandler.instance().setExaminationDataHandlerToUse(

					new medview.datahandling.examination.RemoteExaminationDataHandlerClient(), true); // set up datahandler

				MedViewDataHandler.instance().setExaminationDataLocation(

					MedViewDataHandler.instance().getUserStringPreference(

						REMOTE_SERVER_LOCATION_PROPERTY, "", MedImagerConstants.class)); // set up remote location

				// term datahandler setup

				journalHandler.setUseRemoteDataHandling(true);
			}
		}
		else
		{
			if (!usesRemoteDataHandling())
			{
				return; // already not using local datahandling
			}
			else
			{
				try
				{
					// examination datahandler setup

					MedViewDataHandler.instance().setExaminationDataHandlerToUse( // -> Exception

						MedViewDataHandler.instance().getDefaultExaminationDataHandler(), true); // set up datahandler

					MedViewDataHandler.instance().setExaminationDataLocation(getLocalExaminationDataLocation()); // set up local location

					// term datahandler setup

					journalHandler.setUseRemoteDataHandling(false);
				}
				catch (Exception exc)
				{
					exc.printStackTrace();

					System.exit(1); // this should never happen - if it does it's programmer error!
				}
			}
		}
	}

	public boolean usesRemoteDataHandling()
	{
		return MedViewDataHandler.instance().getExaminationDataHandlerInUse().equals(

			"medview.datahandling.examination.RemoteExaminationDataHandlerClient");
	}


	// EVENT NOTIFICATION

	public void addModelListener( MedImagerModelListener l )
	{
		listenerList.add(MedImagerModelListener.class, l);
	}

	public void removeModelListener( MedImagerModelListener l )
	{
		listenerList.remove(MedImagerModelListener.class, l);
	}

	private void fireJournalTemplateChanged()
	{
		MedImagerModelEvent event = new MedImagerModelEvent(this);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedImagerModelListener.class)
			{
				((MedImagerModelListener)listeners[i+1]).journalTemplateChanged(event);
			}
		}
	}

	private void fireJournalTranslatorChanged()
	{
		MedImagerModelEvent event = new MedImagerModelEvent(this);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedImagerModelListener.class)
			{
				((MedImagerModelListener)listeners[i+1]).journalTranslatorChanged(event);
			}
		}
	}

}
