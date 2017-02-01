package medview.medserver.model;

import java.io.*;

import java.rmi.*;

import javax.swing.event.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;

import medview.medserver.data.*;

import misc.foundation.*;
import misc.foundation.io.*;

/**
 * The model can be in one of two states - either in a
 * 'connected' state, when there are registry-bound
 * implementations of a remote term datahandler, a
 * remote examination datahandler, and a remote pcode
 * generator or the model can be in a 'disconnected'
 * state, where these three member variables are not bound
 * in the registry and set to null. The names of the bound
 * remote implementations are defined by the constants
 * in the MedServerDataConstants interface.
 *
 * The server uses the local datahandling implementations
 * to service the clients requests. I.e. there is no switching
 * between data handling implementation classes being done
 * from the server.
 *
 * When it comes to events, it is important to keep the
 * concepts separated. Model events are regarding the
 * local datahandling, while communication events are meant
 * to inform about what the clients are doing to the server.
 * Thus, when a client has, for example, added an examination
 * to the server, the model will receive a data handler event
 * indicating this, and forward this to upper layers by firing
 * a model event indicating that an examination has been added
 * to the local database. Then, the model will also receive an
 * event from the remote examination datahandler implementation
 * indicating that an examination was added remotely. This event
 * carries information about the client host etc, and as stated
 * above it is meant to simply inform. The action to update the
 * local lists or whatever is used to display the local data
 * state is to be taken when the local data event is received.
 * @author Fredrik Lindahl
 */
public class MedServerModel implements MedViewLanguageConstants,
	MedServerModelConstants, MedServerDataConstants, MedServerModelUserProperties
{

	// LOGGING

	public void activateClientCommunicationLogging()
	{
		shouldNotifyOfClientCommunication = true;
	}

	public void deactivateClientCommunicationLogging()
	{
		shouldNotifyOfClientCommunication = false;
	}

	/**
	 * Sets the location of the log file. Delegates
	 * this to the log file handler. Will also set
	 * a preference so that the next time the
	 * application is started, the log file handler
	 * is initiated with this last set location. This
	 * directory will be used both for the global server
	 * log file and the term log file.
	 *
	 * @param path String the path to the log file.
	 */
	public void setLogFileDirectory(String path)
	{
		Class userPropClass = MedServerModelUserProperties.class;

		String pref = LOG_FILE_DIRECTORY_PROPERTY;

		MedViewDataHandler.instance().setUserStringPreference(pref, path, userPropClass);

		logFileHandler.setLogFileDirectory(path);

		termLogFileHandler.setLogFileDirectory(path);
	}

	/**
	 * Returns the currently set log file directory
	 * in use by the log file handler. This might be
	 * the default directory for medview log files.
	 * Note that the term log file and the global
	 * server log file share the same directory.
	 *
	 * @return String the currently set log file directory.
	 */
	public String getLogFileDirectory()
	{
		return logFileHandler.getLogFileDirectory();
	}

	/**
	 * Appends the specified text to the currently
	 * set log file. This will only append to the
	 * global server log file, and not the term log
	 * file.
	 *
	 * @param text String the text to append to the
	 * log file.
	 * @throws IOException if there is some error when
	 * writing to the log file.
	 */
	public void appendToLog(String text) throws IOException
	{
		//logFileHandler.appendToLog(text); // currently disabled (creates huge logfiles <- server crash)
	}

	/**
	 * Appends the specified text to the currently
	 * set term log file. The concept of a term log
	 * file might be removed once the server is used
	 * for template and translator datahandling as
	 * well.
	 *
	 * @param text String the text to append to the
	 * log file.
	 * @throws IOException if there is some error when
	 * writing to the log file.
	 */
	public void appendToTermLog(String text) throws IOException
	{
		// termLogFileHandler.appendToLog(text); // currently disabled (creates huge logfiles <- server crash)
	}


	// SERVER ACTIVATION / DEACTIVATION

	/**
	 * Activates the server, or throws an exception if this
	 * could not be done. In order for the server to be
	 * activated, the term locations, the examination data
	 * location, as well as the pcode number generator location
	 * must have been set. Otherwise, an exception is
	 * thrown right away. Next, the remote implementation
	 * classes try to bind themselves to the rmi registry, if
	 * the registry is not up and running or some other error
	 * occurs during this process, an exception will be thrown
	 * stating the nature of the error.
	 *
	 * @throws CouldNotActivateException if the server could not
	 * be activated for some reason.
	 */
	public void activateServer() throws CouldNotActivateException
	{
		if (!areTermLocationsSet())
		{
			String m = MedViewDataHandler.instance().getLanguageString(ERROR_TERM_LOCATIONS_NOT_SET);

			throw new CouldNotActivateException(m);
		}

		if (!isExaminationDataLocationSet())
		{
			String m = MedViewDataHandler.instance().getLanguageString(ERROR_EXAMINATION_DATA_LOCATION_NOT_SET);

			throw new CouldNotActivateException(m);
		}

		if (!isNumberGeneratorLocationSet())
		{
			String m = MedViewDataHandler.instance().getLanguageString(ERROR_NR_GENERATOR_LOCATION_NOT_SET);

			throw new CouldNotActivateException(m);
		}

		try
		{
			remoteTermDataHandler = new RemoteTermDataHandlerImpl();

			remoteExamDataHandler = new RemoteExaminationDataHandlerImpl();

			remotePCodeGenerator = new RemotePCodeGeneratorImpl();

			remoteTermDataHandler.addRemoteTermDataHandlerImplListener(remoteTDHImplListener);

			remoteExamDataHandler.addRemoteExaminationDataHandlerImplListener(remoteEDHImplListener);

			remotePCodeGenerator.addRemotePCodeGeneratorImplListener(remotePCGImplListener);

			remoteExamDataHandler.setServerNameToClients(getServerNameToClients());

			Naming.rebind(REMOTE_TERM_DATAHANDLER_BOUND_NAME, remoteTermDataHandler); // might throw exception

			Naming.rebind(REMOTE_EXAMINATION_DATAHANDLER_BOUND_NAME, remoteExamDataHandler); // might throw exception

			Naming.rebind(REMOTE_PCODE_GENERATOR_BOUND_NAME, remotePCodeGenerator); // might throw exception

			fireServerActivated();
		}
		catch (Exception e)
		{
			e.printStackTrace();

			// these cannot be null (exception thrown after initiations)

			remoteTermDataHandler.removeRemoteTermDataHandlerImplListener(remoteTDHImplListener);

			remoteExamDataHandler.removeRemoteExaminationDataHandlerImplListener(remoteEDHImplListener);

			remotePCodeGenerator.removeRemotePCodeGeneratorImplListener(remotePCGImplListener);

			remoteTermDataHandler = null;

			remoteExamDataHandler = null;

			remotePCodeGenerator = null;

			throw new CouldNotActivateException(e.getMessage());
		}
	}

	/**
	 * Deactivates the server, or throws an exception if this
	 * could not be done. This will place the model in its
	 * not connected state.
	 *
	 * @throws CouldNotDeactivateException if the server could
	 * not be deactivated for some reason.
	 */
	public void deactivateServer() throws CouldNotDeactivateException
	{
		try
		{
			remoteExamDataHandler.removeRemoteExaminationDataHandlerImplListener(remoteEDHImplListener);

			remoteTermDataHandler.removeRemoteTermDataHandlerImplListener(remoteTDHImplListener);

			remotePCodeGenerator.removeRemotePCodeGeneratorImplListener(remotePCGImplListener);

			remoteExamDataHandler.serverShuttingDown(); // notify client remote stubs of shutdown

			remoteTermDataHandler.serverShuttingDown(); // notify client remote stubs of shutdown

			remotePCodeGenerator.serverShuttingDown(); // notify client remote stubs of shutdown

			remoteExamDataHandler = null;

			remoteTermDataHandler = null;

			remotePCodeGenerator = null;

			Naming.unbind(REMOTE_TERM_DATAHANDLER_BOUND_NAME);

			Naming.unbind(REMOTE_EXAMINATION_DATAHANDLER_BOUND_NAME);

			Naming.unbind(REMOTE_PCODE_GENERATOR_BOUND_NAME);

			fireServerDeactivated();
		}
		catch (Exception e)
		{
			e.printStackTrace();

			throw new CouldNotDeactivateException(e.getMessage());
		}
	}

	/**
	 * Checks whether the server is
	 * in its 'active' or 'inactive'
	 * state.
	 *
	 * @return boolean whether the server
	 * is in its 'active' or 'inactive'
	 * state.
	 */
	public boolean isServerActive()
	{
		return ((remoteTermDataHandler != null) &&

			(remoteExamDataHandler != null) &&

			(remotePCodeGenerator != null));
	}


	// SERVER NAME

	/**
	 * Returns the currently set name that
	 * the server has against the clients
	 * when they want to display a location
	 * to the user. This should not be a too
	 * long name.
	 *
	 * @return String the currently set data
	 * location name (also known as the data
	 * location ID).
	 */
	public String getServerNameToClients()
	{
		/* NOTE: the server might be inactive, when it is
		   it won't have a remote examination data handler
		   implementation. Therefore, we cannot simply query
		   the remote examination data handler implementation
		   for the currently set server name. */

		Class userPropClass = MedServerModelUserProperties.class;

		String prop = SERVER_NAME_TO_CLIENTS_PROPERTY;

		return MedViewDataHandler.instance().getUserStringPreference(prop, "Server", userPropClass);
	}

	/**
	 * Sets the name to which the server is
	 * identified to the client users (this
	 * is not the same as the IP address or
	 * localization information of the server,
	 * it is merely a string shown to the users
	 * of the clients where the data is obtained
	 * from).
	 *
	 * @param name String the name that should be
	 * displayed to clients.
	 */
	public void setServerNameToClients(String name)
	{
		Class userPropClass = MedServerModelUserProperties.class;

		String prop = SERVER_NAME_TO_CLIENTS_PROPERTY;

		MedViewDataHandler.instance().setUserStringPreference(prop, name, userPropClass);

		if (isServerActive()) // if not active, there is no remote exam datahandler yet
		{
			remoteExamDataHandler.setServerNameToClients(name); // will cause event to be fired
		}
	}


	// EXAMINATIONS AND PATIENTS

	/**
	 * Obtains an array of all the patients contained in the
	 * currently set examination data location.
	 *
	 * @param not ProgressNotifiable this operation could take
	 * some time, thus the possibility of registering a
	 * ProgressNotifiable.
	 * @return PatientIdentifier[] the array of all the patients
	 * contained in the currently set examination data location.
	 * @throws CouldNotRetrievePatientsException if some error
	 * occurs during the retrieval of the patient array.
	 */
	public PatientIdentifier[] getPatients(ProgressNotifiable not) throws CouldNotRetrievePatientsException
	{
		if (!MedViewDataHandler.instance().isExaminationDataLocationSet())
		{
			String m = "The examination data location is not set"; // should not happen

			throw new CouldNotRetrievePatientsException(m);
		}
		else
		{
			try
			{
				return MedViewDataHandler.instance().getPatients(not); // can throw IOException
			}
			catch (Exception e)
			{
				throw new CouldNotRetrievePatientsException(e.getMessage());
			}
		}
	}

	/**
	 * Obtains an array of all examinations for the specified
	 * patient identifier.
	 *
	 * @param pid PatientIdentifier the patient for which the
	 * examinations are requested.
	 * @return ExaminationIdentifier[] the array of all
	 * examinations in the currently set examination data location.
	 * @throws CouldNotRetrieveExaminationsException if some error
	 * occurs during the retrieval of the examination array.
	 */
	public ExaminationIdentifier[] getExaminations(PatientIdentifier pid) throws CouldNotRetrieveExaminationsException
	{
		if (!MedViewDataHandler.instance().isExaminationDataLocationSet())
		{
			String m = "The examination data location is not set"; // should not happen

			throw new CouldNotRetrieveExaminationsException(m);
		}
		else
		{
			try
			{
				return MedViewDataHandler.instance().getExaminations(pid); // can throw IOException
			}
			catch (Exception e)
			{
				throw new CouldNotRetrieveExaminationsException(e.getMessage());
			}
		}
	}


	// EXAMINATION DATA LOCATION

	/**
	 * Sets the examination data location.
	 *
	 * @param loc String the examination data
	 * location to set.
	 */
	public void setExaminationDataLocation(String loc) // OBS: the LOCAL examination data location
	{
		MedViewDataHandler.instance().setUserStringPreference(LOCAL_EXAMINATION_DATA_LOCATION_PROPERTY,

			loc, MedServerModelUserProperties.class);

		MedViewDataHandler.instance().setExaminationDataLocation(loc); // the default examination data handler is used
	}

	/**
	 * Returns whether or not the examination data
	 * location has been set.
	 *
	 * @return boolean whether or not the examination
	 * data location has been set.
	 */
	public boolean isExaminationDataLocationSet() // OBS: the LOCAL examination data location
	{
		return MedViewDataHandler.instance().isExaminationDataLocationSet();
	}

	/**
	 * Obtains the currently set examination data
	 * location.
	 *
	 * @return String the currently set examination
	 * data location.
	 */
	public String getExaminationDataLocation() // OBS: the LOCAL examination data location
	{
		return MedViewDataHandler.instance().getExaminationDataLocation();
	}


	// PCODE GENERATION

	/**
	 * Sets the number generator location that the local
	 * pcode number generator uses.
	 *
	 * @param loc String the local pcode number generator
	 * location.
	 */
	public void setNumberGeneratorLocation(String loc)
	{
		Class userPropClass = MedServerModelUserProperties.class;

		String prop = NUMBER_GENERATOR_LOCATION_PROPERTY;

		MedViewDataHandler.instance().setUserStringPreference(prop, loc, userPropClass);

		MedViewDataHandler.instance().setPCodeNRGeneratorLocation(loc); // will fire event (see below)
	}

	/**
	 * Obtains the currently used local pcode number
	 * generator location.
	 *
	 * @return String the local pcode number generator
	 * location currently used.
	 */
	public String getNumberGeneratorLocation()
	{
		return MedViewDataHandler.instance().getPCodeNRGeneratorLocation();
	}

	/**
	 * Whether or not the local pcode number generator
	 * location has been set.
	 *
	 * @return boolean if the local generator location has
	 * been set.
	 */
	public boolean isNumberGeneratorLocationSet()
	{
		return MedViewDataHandler.instance().isPCodeNRGeneratorLocationSet();
	}


	// TERMS

	/**
	 * Obtains an array of all terms currently contained
	 * in the data layer.
	 *
	 * @return String[] an array of all terms currently
	 * handled in the data layer.
	 * @throws CouldNotRetrieveTermsException if the term
	 * array could not be obtained for some reason.
	 */
	public String[] getTerms() throws CouldNotRetrieveTermsException
	{
		if (!MedViewDataHandler.instance().isTermDefinitionLocationSet())
		{
			String m = "The term definition location is not set";

			throw new CouldNotRetrieveTermsException(m);
		}
		else
		{
			try
			{
				return MedViewDataHandler.instance().getTerms();
			}
			catch (Exception e)
			{
				throw new CouldNotRetrieveTermsException(e.getMessage());
			}
		}
	}

	/**
	 * Obtains an array of all values for a specified
	 * term.
	 *
	 * @param term String the term for which you want
	 * the values.
	 * @return Object[] the values for the specified
	 * term.
	 * @throws CouldNotRetrieveValuesException if the
	 * value array could not be obtained for some reason.
	 */
	public Object[] getValues(String term) throws CouldNotRetrieveValuesException
	{
		if (!MedViewDataHandler.instance().isTermValueLocationSet())
		{
			String m = "The term value location is not set";

			throw new CouldNotRetrieveValuesException(m);
		}
		else
		{
			try
			{
				return MedViewDataHandler.instance().getValues(term);
			}
			catch (Exception e)
			{
				throw new CouldNotRetrieveValuesException(e.getMessage());
			}
		}
	}

	/**
	 * Obtain the type description for the specified term.
	 *
	 * @param term String the type description for the
	 * specified term in natural language.
	 * @return String the term for which you want the type.
	 * @throws CouldNotRetrieveTypeDescriptionException if
	 * the type description could not be obtained for some
	 * reason.
	 */
	public String getTypeDescription(String term) throws CouldNotRetrieveTypeDescriptionException
	{
		if (!MedViewDataHandler.instance().isTermDefinitionLocationSet())
		{
			String m = "The term definition location is not set";

			throw new CouldNotRetrieveTypeDescriptionException(m);
		}
		else
		{
			try
			{
				return MedViewDataHandler.instance().getTypeDescriptor(term);
			}
			catch (Exception e)
			{
				throw new CouldNotRetrieveTypeDescriptionException(e.getMessage());
			}
		}
	}

	/**
	 * Set the term definition location.
	 *
	 * @param path String the term definition location.
	 */
	public void setTermDefinitionLocation(String location)
	{
		Class userPropClass = MedServerModelUserProperties.class;

		String prop = LOCAL_TERM_DEFINITIONS_LOCATION_PROPERTY;

		MedViewDataHandler.instance().setUserStringPreference(prop, location, userPropClass);

		MedViewDataHandler.instance().setTermDefinitionLocation(location); // the default term data handler is used
	}

	/**
	 * Set the term value location.
	 *
	 * @param path String the term value location.
	 */
	public void setTermValueLocation(String path)
	{
		Class userPropClass = MedServerModelUserProperties.class;

		String prop = LOCAL_TERM_VALUES_LOCATION_PROPERTY;

		MedViewDataHandler.instance().setUserStringPreference(prop, path, userPropClass);

		MedViewDataHandler.instance().setTermValueLocation(path); // the default term data handler is used
	}

	/**
	 * Obtain the term definition location.
	 *
	 * @return String the term definition location.
	 */
	public String getTermDefinitionLocation()
	{
		return MedViewDataHandler.instance().getTermDefinitionLocation();
	}

	/**
	 * Obtain the term value location.
	 *
	 * @return String the term value location.
	 */
	public String getTermValueLocation()
	{
		return MedViewDataHandler.instance().getTermValueLocation();
	}

	/**
	 * Whether or not the term value location is
	 * set.
	 *
	 * @return boolean whether or not the term value
	 * location is set.
	 */
	public boolean isTermValueLocationSet()
	{
		return MedViewDataHandler.instance().isTermValueLocationSet();
	}

	/**
	 * Whether or not the term definition location
	 * is set.
	 *
	 * @return boolean whether or not the term
	 * definition location is set.
	 */
	public boolean isTermDefinitionLocationSet()
	{
		return MedViewDataHandler.instance().isTermDefinitionLocationSet();
	}

	/**
	 * Whether or not both the term definition and
	 * the term value locations have been set.
	 *
	 * @return boolean whether or not both the term
	 * definition and the term value locations have been set.
	 */
	public boolean areTermLocationsSet()
	{
		return (isTermValueLocationSet() && isTermDefinitionLocationSet());
	}


	// MODEL AND COMMUNICATION EVENTS

	/**
	 * Register a model listener to the model.
	 *
	 * @param l MedServerModelListener the model listener
	 * to register.
	 */
	public void addMedServerModelListener(MedServerModelListener l)
	{
		listenerList.add(MedServerModelListener.class, l);
	}

	/**
	 * Unregister a model listener from the model.
	 *
	 * @param l MedServerModelListener the model listener
	 * to unregister.
	 */
	public void removeMedServerModelListener(MedServerModelListener l)
	{
		listenerList.remove(MedServerModelListener.class, l);
	}

	/**
	 * Register a communication listener to the
	 * model.
	 *
	 * @param l MedServerCommunicationListener the
	 * communication listener to register.
	 */
	public void addMedServerCommunicationListener(MedServerCommunicationListener l)
	{
		listenerList.add(MedServerCommunicationListener.class, l);
	}

	/**
	 * Unregister a communication listener from the
	 * model.
	 *
	 * @param l MedServerCommunicationListener
	 */
	public void removeMedServerCommunicationListener(MedServerCommunicationListener l)
	{
		listenerList.remove(MedServerCommunicationListener.class, l);
	}


	// MODEL LISTENER EVENTS

	protected void fireTermDataHandlerChanged(String className) // local
	{
		clearModelEvent();

		modelEvent.setClassName(className);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerModelListener.class)
			{
				((MedServerModelListener)listeners[i+1]).termDataHandlerChanged(modelEvent);
			}
		}
	}

	protected void fireExaminationDataHandlerChanged(String className) // local
	{
		clearModelEvent();

		modelEvent.setClassName(className);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerModelListener.class)
			{
				((MedServerModelListener)listeners[i+1]).examinationDataHandlerChanged(modelEvent);
			}
		}
	}

	protected void fireTATDataHandlerChanged(String className) // local
	{
		clearModelEvent();

		modelEvent.setClassName(className);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerModelListener.class)
			{
				((MedServerModelListener)listeners[i+1]).tATDataHandlerChanged(modelEvent);
			}
		}
	}

	protected void firePCodeGeneratorChanged(String className) // local
	{
		clearModelEvent();

		modelEvent.setClassName(className);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerModelListener.class)
			{
				((MedServerModelListener)listeners[i+1]).pCodeGeneratorChanged(modelEvent);
			}
		}
	}

	protected void fireExaminationAdded(ExaminationIdentifier id) // local
	{
		clearModelEvent();

		modelEvent.setExamination(id);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerModelListener.class)
			{
				((MedServerModelListener)listeners[i+1]).examinationAdded(modelEvent);
			}
		}
	}

	protected void fireExaminationUpdated(ExaminationIdentifier id) // local
	{
		clearModelEvent();

		modelEvent.setExamination(id);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerModelListener.class)
			{
				((MedServerModelListener)listeners[i+1]).examinationUpdated(modelEvent);
			}
		}
	}

	protected void fireExaminationDataLocationChanged(String loc) // local
	{
		clearModelEvent();

		modelEvent.setLocation(loc);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerModelListener.class)
			{
				((MedServerModelListener)listeners[i+1]).examinationDataLocationChanged(modelEvent);
			}
		}
	}

	protected void fireExaminationDataLocationIDChanged(String locID) // local
	{
		clearModelEvent();

		modelEvent.setLocationID(locID);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerModelListener.class)
			{
				((MedServerModelListener)listeners[i+1]).examinationDataLocationIDChanged(modelEvent);
			}
		}
	}


	protected void fireTermAdded(String term) // local
	{
		clearModelEvent();

		modelEvent.setTerm(term);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerModelListener.class)
			{
				((MedServerModelListener)listeners[i+1]).termAdded(modelEvent);
			}
		}
	}

	protected void fireTermRemoved(String term) // local
	{
		clearModelEvent();

		modelEvent.setTerm(term);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerModelListener.class)
			{
				((MedServerModelListener)listeners[i+1]).termRemoved(modelEvent);
			}
		}
	}

	protected void fireValueAdded(String term, Object val) // local
	{
		clearModelEvent();

		modelEvent.setTerm(term);

		modelEvent.setValue(val);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerModelListener.class)
			{
				((MedServerModelListener)listeners[i+1]).valueAdded(modelEvent);
			}
		}
	}

	protected void fireValueRemoved(String term, Object val) // local
	{
		clearModelEvent();

		modelEvent.setTerm(term);

		modelEvent.setValue(val);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerModelListener.class)
			{
				((MedServerModelListener)listeners[i+1]).valueRemoved(modelEvent);
			}
		}
	}

	protected void fireTermDefinitionLocationChanged(String loc) // local
	{
		clearModelEvent();

		modelEvent.setLocation(loc);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerModelListener.class)
			{
				((MedServerModelListener)listeners[i+1]).termDefinitionLocationChanged(modelEvent);
			}
		}
	}

	protected void fireTermValueLocationChanged(String loc) // local
	{
		clearModelEvent();

		modelEvent.setLocation(loc);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerModelListener.class)
			{
				((MedServerModelListener)listeners[i+1]).termValueLocationChanged(modelEvent);
			}
		}
	}

	protected void firePCodePrefixChanged(String prefix) // local
	{
		clearModelEvent();

		modelEvent.setPrefix(prefix);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerModelListener.class)
			{
				((MedServerModelListener)listeners[i+1]).pCodePrefixChanged(modelEvent);
			}
		}
	}

	protected void fireNumberGeneratorLocationChanged(String loc) // local
	{
		clearModelEvent();

		modelEvent.setLocation(loc);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerModelListener.class)
			{
				((MedServerModelListener)listeners[i+1]).numberGeneratorLocationChanged(modelEvent);
			}
		}
	}


	// COMMUNICATION LISTENER EVENTS

	protected void firePatientListRequestedRemotely(String host) // remote
	{
		clearCommEvent();

		commEvent.setClientHost(host);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).patientListRequested(commEvent);
			}
		}
	}

	protected void fireExaminationCountRequested(String host) // remote
	{
		clearCommEvent();

		commEvent.setClientHost(host);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).examinationCountRequested(commEvent);
			}
		}
	}

	protected void fireExaminationListRequestedRemotely(String host, PatientIdentifier pat) // remote
	{
		clearCommEvent();

		commEvent.setPatient(pat);

		commEvent.setClientHost(host);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).examinationListRequested(commEvent);
			}
		}
	}

	protected void fireAllExaminationValueContainersRequestedRemotely(String host) // remote
	{
		clearCommEvent();

		commEvent.setClientHost(host);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).allExaminationValueContainersRequested(commEvent);
			}
		}
	}

	protected void fireExaminationValueContainerRequestedRemotely(String host, ExaminationIdentifier id) // remote
	{
		clearCommEvent();

		commEvent.setExamination(id);

		commEvent.setClientHost(host);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).examinationValueContainerRequested(commEvent);
			}
		}
	}

	protected void fireImageCountRequestedRemotely(String host, PatientIdentifier pat) // remote
	{
		clearCommEvent();

		commEvent.setPatient(pat);

		commEvent.setClientHost(host);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).imageCountRequested(commEvent);
			}
		}
	}

	protected void fireExaminationAddedRemotely(String host, ExaminationIdentifier id) // remote
	{
		clearCommEvent();

		commEvent.setClientHost(host);

		commEvent.setExamination(id);

		commEvent.setPatient(id.getPID());

		commEvent.setPCode(id.getPID().getPCode());

		commEvent.setPID(id.getPID().getPID());

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).examinationAdded(commEvent);
			}
		}
	}

	protected void fireExaminationUpdatedRemotely(String host, ExaminationIdentifier id) // remote
	{
		clearCommEvent();

		commEvent.setClientHost(host);

		commEvent.setExamination(id);

		commEvent.setPatient(id.getPID());

		commEvent.setPCode(id.getPID().getPCode());

		commEvent.setPID(id.getPID().getPID());

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).examinationUpdated(commEvent);
			}
		}
	}

	protected void fireImagesRequestedRemotely(String host, ExaminationIdentifier id) // remote
	{
		clearCommEvent();

		commEvent.setExamination(id);

		commEvent.setClientHost(host);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).imagesRequested(commEvent);
			}
		}
	}

	protected void fireRefreshPerformedRemotely(String host) // remote
	{
		clearCommEvent();

		commEvent.setClientHost(host);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).refreshPerformed(commEvent);
			}
		}
	}


	protected void fireTermListRequestedRemotely(String host) // remote
	{
		clearCommEvent();

		commEvent.setClientHost(host);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).termListRequested(commEvent);
			}
		}
	}

	protected void fireTermHashMapRequestedRemotely(String host) // remote
	{
		clearCommEvent();

		commEvent.setClientHost(host);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).termHashMapRequested(commEvent);
			}
		}
	}

	protected void fireValuesRequestedRemotely(String host, String term) // remote
	{
		clearCommEvent();

		commEvent.setTerm(term);

		commEvent.setClientHost(host);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).valuesRequested(commEvent);
			}
		}
	}

	protected void fireTypeRequestedRemotely(String host, String term) // remote
	{
		clearCommEvent();

		commEvent.setTerm(term);

		commEvent.setClientHost(host);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).typeRequested(commEvent);
			}
		}
	}

	protected void fireTermExistanceRequestedRemotely(String host, String term) // remote
	{
		clearCommEvent();

		commEvent.setTerm(term);

		commEvent.setClientHost(host);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).termExistanceQueried(commEvent);
			}
		}
	}

	protected void fireTermAddedRemotely(String host, String term) // remote
	{
		clearCommEvent();

		commEvent.setTerm(term);

		commEvent.setClientHost(host);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).termAdded(commEvent);
			}
		}
	}

	protected void fireTermRemovedRemotely(String host, String term) // remote
	{
		clearCommEvent();

		commEvent.setTerm(term);

		commEvent.setClientHost(host);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).termRemoved(commEvent);
			}
		}
	}

	protected void fireValueAddedRemotely(String host, String term, Object value) // remote
	{
		clearCommEvent();

		commEvent.setTerm(term);

		commEvent.setValue(value);

		commEvent.setClientHost(host);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).valueAdded(commEvent);
			}
		}
	}

	protected void fireValueRemovedRemotely(String host, String term, Object value) // remote
	{
		clearCommEvent();

		commEvent.setTerm(term);

		commEvent.setValue(value);

		commEvent.setClientHost(host);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).valueRemoved(commEvent);
			}
		}
	}

	protected void firePCodeRequestedRemotely(String host, String pid, String pCode) // remote
	{
		clearCommEvent();

		commEvent.setClientHost(host);

		commEvent.setPID(pid);

		commEvent.setPCode(pCode);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).pCodeRequested(commEvent);
			}
		}
	}

	protected void fireServerActivated() // local, but fires comm event
	{
		clearCommEvent();

		commEvent.setClientHost("local");

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).serverActivated(commEvent);
			}
		}
	}

	protected void fireServerDeactivated() // local, but fires comm event
	{
		clearCommEvent();

		commEvent.setClientHost("local");

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == MedServerCommunicationListener.class)
			{
				((MedServerCommunicationListener)listeners[i+1]).serverDeactivated(commEvent);
			}
		}
	}


	// UTILITY METHODS

	private void clearModelEvent()
	{
		modelEvent.setTerm(null);

		modelEvent.setValue(null);

		modelEvent.setLocation(null);

		modelEvent.setClassName(null);

		modelEvent.setExamination(null);

		modelEvent.setLocationID(null);

		modelEvent.setPrefix(null);
	}

	private void clearCommEvent()
	{
		commEvent.setPatient(null);

		commEvent.setExamination(null);

		commEvent.setTerm(null);

		commEvent.setValue(null);

		commEvent.setClientHost(null);

		commEvent.setLogLine(null);

		commEvent.setPCode(null);

		commEvent.setPID(null);
	}


	// CONSTRUCTOR

	public MedServerModel()
	{
		// logging

		String logProp = LOG_FILE_DIRECTORY_PROPERTY;

		Class userPropClass = MedServerModelUserProperties.class;

		if (MedViewDataHandler.instance().isUserPreferenceSet(logProp, userPropClass))
		{
			String set = MedViewDataHandler.instance().getUserStringPreference(logProp, null, userPropClass);

			logFileHandler.setLogFileDirectory(set);

			termLogFileHandler.setLogFileDirectory(set);
		}
		else
		{
			logFileHandler.setLogFileDirectory(MedViewDataHandler.instance().getDefaultLogDirectory());

			termLogFileHandler.setLogFileDirectory(MedViewDataHandler.instance().getDefaultLogDirectory());
		}

		logFileHandler.setLogFileName("serverLog.txt");

		termLogFileHandler.setLogFileName("serverTermLog.txt");

		// local data, term, and number generation locations

		String termValLocProp = LOCAL_TERM_VALUES_LOCATION_PROPERTY;

		String termDefLocProp = LOCAL_TERM_DEFINITIONS_LOCATION_PROPERTY;

		String examLocProp = LOCAL_EXAMINATION_DATA_LOCATION_PROPERTY;

		String genProp = NUMBER_GENERATOR_LOCATION_PROPERTY;

		if (MedViewDataHandler.instance().isUserPreferenceSet(examLocProp, userPropClass))
		{
			String set = MedViewDataHandler.instance().getUserStringPreference(examLocProp, null, userPropClass);

			MedViewDataHandler.instance().setExaminationDataLocation(set); // default examination data handler used ( = local)
		}

		if (MedViewDataHandler.instance().isUserPreferenceSet(termValLocProp, userPropClass))
		{
			String set = MedViewDataHandler.instance().getUserStringPreference(termValLocProp, null, userPropClass);

			MedViewDataHandler.instance().setTermValueLocation(set); // default term data handler used ( = local)
		}

		if (MedViewDataHandler.instance().isUserPreferenceSet(termDefLocProp, userPropClass))
		{
			String set = MedViewDataHandler.instance().getUserStringPreference(termDefLocProp, null, userPropClass);

			MedViewDataHandler.instance().setTermDefinitionLocation(set); // default term data handler used ( = local)
		}

		if (MedViewDataHandler.instance().isUserPreferenceSet(genProp, userPropClass))
		{
			String set = MedViewDataHandler.instance().getUserStringPreference(genProp, null, userPropClass);

			MedViewDataHandler.instance().setPCodeNRGeneratorLocation(set); // default pcode number generator used ( = local)
		}

		// attach data listener to data layer

		MedViewDataHandler.instance().addMedViewDataListener(dataHandlerListener);

		// attach pcode listener to data layer

		MedViewDataHandler.instance().addMedViewPCodeListener(pCodeListener);
	}

	// MEMBERS

	// listeners

	private EventListenerList listenerList = new EventListenerList();

	private PCodeListener pCodeListener = new PCodeListener();

	private DataHandlerListener dataHandlerListener = new DataHandlerListener();

	private RemoteTermDataHandlerImplListener remoteTDHImplListener = new RemoteTDHImplListener();

	private RemoteExaminationDataHandlerImplListener remoteEDHImplListener = new RemoteEDHImplListener();

	private RemotePCodeGeneratorImplListener remotePCGImplListener = new RemotePCGImplListener();

	// remote implementations

	private RemoteTermDataHandlerImpl remoteTermDataHandler = null;

	private RemoteExaminationDataHandlerImpl remoteExamDataHandler = null;

	private RemotePCodeGeneratorImpl remotePCodeGenerator = null;

	// shared events

	private MedServerCommunicationEvent commEvent = new MedServerCommunicationEvent(this);

	private MedServerModelEvent modelEvent = new MedServerModelEvent(this);

	// logging

	private LogFileHandler logFileHandler = new LogFileHandler();

	private LogFileHandler termLogFileHandler = new LogFileHandler();

	private boolean shouldNotifyOfClientCommunication = false;


	// DATA LAYER DATA LISTENER

	private class DataHandlerListener implements MedViewDataListener
	{
		public void termDataHandlerChanged(MedViewDataEvent e)
		{
			fireTermDataHandlerChanged(e.getClassName());
		}

		public void examinationDataHandlerChanged(MedViewDataEvent e)
		{
			fireExaminationDataHandlerChanged(e.getClassName());
		}

		public void templateAndTranslatorDataHandlerChanged(MedViewDataEvent e)
		{
			fireTATDataHandlerChanged(e.getClassName());
		}

		public void examinationAdded(MedViewDataEvent e)
		{
			fireExaminationAdded(e.getIdentifier());
		}

		public void examinationUpdated(MedViewDataEvent e)
		{
			fireExaminationUpdated(e.getIdentifier());
		}

		public void examinationRemoved(MedViewDataEvent e)
		{
			// TODO: implement this
		}

		public void examinationDataLocationChanged(MedViewDataEvent e)
		{
			fireExaminationDataLocationChanged(e.getLocation());
		}

		public void examinationDataLocationIDChanged(MedViewDataEvent e)
		{
			fireExaminationDataLocationIDChanged(e.getLocationID());
		}

		public void termLocationChanged(MedViewDataEvent e)
		{
			fireTermValueLocationChanged(e.getLocation());

			fireTermDefinitionLocationChanged(e.getLocation());
		}

		public void termAdded(MedViewDataEvent e)
		{
			fireTermAdded(e.getTerm());
		}

		public void termRemoved(MedViewDataEvent e)
		{
			fireTermRemoved(e.getTerm());
		}

		public void valueAdded(MedViewDataEvent e)
		{
			fireValueAdded(e.getTerm(), e.getValue());
		}

		public void valueRemoved(MedViewDataEvent e)
		{
			fireValueRemoved(e.getTerm(), e.getValue());
		}

		public void userIDChanged(MedViewDataEvent e)
		{
			// add notification later
		}

		public void userNameChanged(MedViewDataEvent e)
		{
			// add notification later
		}
	}

	// DATA LAYER PCODE LISTENER

	private class PCodeListener implements MedViewPCodeListener
	{
		public void generatedPCodePrefixChanged(MedViewPCodeEvent e)
		{
			firePCodePrefixChanged(e.getGeneratedPCodePrefix());
		}

		public void nrGeneratorLocationChanged(MedViewPCodeEvent e)
		{
			fireNumberGeneratorLocationChanged(e.getNRGeneratorLocation());
		}

		public void pCodeGeneratorChanged(MedViewPCodeEvent e)
		{
			// add notification later
		}
	}

	// REMOTE PCODE GENERATOR IMPLEMENTATION LISTENER

	private class RemotePCGImplListener implements RemotePCodeGeneratorImplListener
	{
		public void pCodeRequested(RemotePCodeGeneratorEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				firePCodeRequestedRemotely(e.getClientHost(), e.getPID(), e.getPCode());
			}
		}
	}

	// REMOTE TERM DATAHANDLER IMPLEMENTATION LISTENER

	private class RemoteTDHImplListener implements RemoteTermDataHandlerImplListener
	{
		public void valueAdded(RemoteTermDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				fireValueAddedRemotely(e.getClientHost(), e.getTerm(), e.getValue());
			}
		}

		public void valueRemoved(RemoteTermDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				fireValueRemovedRemotely(e.getClientHost(), e.getTerm(), e.getValue());
			}
		}

		public void termAdded(RemoteTermDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				fireTermAddedRemotely(e.getClientHost(), e.getTerm());
			}
		}

		public void termRemoved(RemoteTermDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				fireTermRemovedRemotely(e.getClientHost(), e.getTerm());
			}
		}

		public void termListRequested(RemoteTermDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				fireTermListRequestedRemotely(e.getClientHost());
			}
		}

		public void termHashMapRequested(RemoteTermDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				fireTermHashMapRequestedRemotely(e.getClientHost());
			}
		}

		public void typeRequested(RemoteTermDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				fireTypeRequestedRemotely(e.getClientHost(), e.getTerm());
			}
		}

		public void valuesRequested(RemoteTermDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				fireValuesRequestedRemotely(e.getClientHost(), e.getTerm());
			}
		}
	}

	// REMOTE EXAMINATION DATAHANDLER IMPLEMENTATION LISTENER

	private class RemoteEDHImplListener implements RemoteExaminationDataHandlerImplListener
	{
		public void serverNameChanged(RemoteExaminationDataHandlerEvent e)
		{
			// not interesting here
		}

		public void locationChanged(RemoteExaminationDataHandlerEvent e)
		{
			// not interesting here
		}

		public void examinationAdded(RemoteExaminationDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				fireExaminationAddedRemotely(e.getClientHost(), e.getIdentifier());
			}
		}

		public void examinationUpdated(RemoteExaminationDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				fireExaminationUpdatedRemotely(e.getClientHost(), e.getIdentifier());
			}
		}

		public void patientListRequested(RemoteExaminationDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				firePatientListRequestedRemotely(e.getClientHost());
			}
		}

		public void examinationCountRequested(RemoteExaminationDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				fireExaminationCountRequested(e.getClientHost());
			}
		}

		public void examinationListRequested(RemoteExaminationDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				fireExaminationListRequestedRemotely(e.getClientHost(), e.getPatient());
			}
		}

		public void allExaminationValueContainersRequested(RemoteExaminationDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				fireAllExaminationValueContainersRequestedRemotely(e.getClientHost());
			}
		}

		public void examinationValueContainerRequested(RemoteExaminationDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				fireExaminationValueContainerRequestedRemotely(e.getClientHost(), e.getIdentifier());
			}
		}

		public void imageCountRequested(RemoteExaminationDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				fireImageCountRequestedRemotely(e.getClientHost(), e.getPatient());
			}
		}

		public void imagesRequested(RemoteExaminationDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				fireImagesRequestedRemotely(e.getClientHost(), e.getIdentifier());
			}
		}

		public void refreshPerformed(RemoteExaminationDataHandlerEvent e)
		{
			if (shouldNotifyOfClientCommunication)
			{
				fireRefreshPerformedRemotely(e.getClientHost());
			}
		}
	}
}
