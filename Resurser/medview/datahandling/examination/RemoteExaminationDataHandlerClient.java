/**
 * @(#) RemoteExaminationDataHandlerClient.java
 *
 * $Id: RemoteExaminationDataHandlerClient.java,v 1.21 2006/04/24 14:17:38 lindahlf Exp $
 *
 * $Log: RemoteExaminationDataHandlerClient.java,v $
 * Revision 1.21  2006/04/24 14:17:38  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.20  2005/09/09 15:40:48  lindahlf
 * Server cachning
 *
 * Revision 1.19  2005/07/27 13:50:57  erichson
 * Removed a bottleneck: serverAddedExaminationVector was causing long delays due to calling contains() in a loop. Replaced it with a HashSet. // NE
 *
 * Revision 1.18  2005/01/30 15:22:08  lindahlf
 * T4 Integration
 *
 * Revision 1.17  2004/11/24 15:17:41  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.16  2004/11/19 12:32:29  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.15  2004/11/11 22:36:28  lindahlf
 * MedServer Test Pack 2
 *
 * Revision 1.14  2004/11/09 21:13:50  lindahlf
 * Datalayer upgrade in connection with server development
 *
 * Revision 1.13  2004/11/06 01:10:53  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.12  2004/11/05 12:27:21  lindahlf
 * Improved exception handling
 *
 * Revision 1.11  2004/11/04 20:07:42  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.10  2004/11/04 12:04:27  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.9  2004/10/23 15:34:15  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.8  2004/10/21 12:20:45  erichson
 * Added progressNotifiable to exportToMVD methods.
 *
 */

package medview.datahandling.examination;

import java.io.*;

import java.rmi.*;

import javax.swing.event.*; // listener list

import medview.common.data.*;

import medview.datahandling.*;
import medview.datahandling.images.*;
import medview.datahandling.examination.filter.*;
import medview.datahandling.examination.tree.*;

import misc.foundation.*;

/**
 * Note: for now, if a listener of this class receives an examination
 * location changed event with the same location as previously set,
 * this indicates that the location has been changed at the server,
 * and the client should always refresh its data when it receives such
 * an event.
 *
 * Note about refresh:
 *
 * Refresh is about finding out about the examinations having been added
 * outside our control, i.e. the ones that we do not already know about
 * in any way. To keep control of this, the client keeps a vector of
 * examinations having been added via the event notification mechanism
 * after the time of connect to the server. When refresh() is called,
 * all examinations having been added to the server after the client
 * connected is obtained, and then the examinations the client already
 * have received notification about are removed. The remainder is returned
 * from the refresh() method.
 *
 * @author Fredrik Lindahl
 */
public class RemoteExaminationDataHandlerClient implements ExaminationDataHandler
{

	// LOCAL SHUT-DOWN NOTIFICATION

	/**
	 * Allows the datahandler to deal with the system
	 * shutting down. For instance, if the datahandler is
	 * a client to a server, lets it tell the server that it
	 * is shutting down so the server can remove it from its
	 * notification list.
	 */
	public void shuttingDown()
	{
		disconnect();
	}


	// METHODS CALLED FROM THE REMOTE LISTENER IMPLEMENTATION

	/**
	 * This method is called when an examination has
	 * been stored on the server, and the local client
	 * needs to be notified of this.
	 */
	public void examinationWasAddedToServer(ExaminationIdentifier id)
	{
		fireExaminationAdded(id);
	}

	public void examinationWasUpdatedOnServer(ExaminationIdentifier id)
	{
		fireExaminationUpdated(id);
	}

	/**
	 * This method is called when the examination data
	 * location ID has been changed on the server, and
	 * the local client needs to be notified of this.
	 */
	public void locationIDWasChangedAtServer(String locID)
	{
		fireExaminationLocationIDChanged(locID);
	}

	/**
	 * This method is called when the examination data
	 * location has been changed on the server, and the
	 * local client needs to be notified of this. This is
	 * a special case for a remote implementation. For
	 * now, this suffices, but in the future this should
	 * be more clear. I.e. when a local client receives
	 * an event indicating that the location has changed,
	 * it should always refresh its listings and data.
	 */
	public void locationWasChangedAtServer()
	{
		fireExaminationLocationChanged(getExaminationDataLocation());
	}

	/**
	 * This method is called when the server is about to
	 * shut down.
	 */
	public void serverShuttingDown()
	{
		disconnect();
	}


	// CONNECTION

	private void connect() throws IOException
	{
		if (isConnected()) // must remove old listener first if connected
		{
			try
			{
				remoteHandler.removeRemoteExaminationDataHandlerListener(remoteEDHListener);
			}
			catch (RemoteException e) {} // dont care if this happens
		}

		try
		{
			String remName = "remoteExaminationDataHandler";

			String rmiURL = "rmi://" + serverURL + "/" + remName;

			remoteHandler = (RemoteExaminationDataHandler) Naming.lookup(rmiURL);

			remoteHandler.addRemoteExaminationDataHandlerListener(remoteEDHListener);
		}
		catch (Exception e) // RemoteException, NotBoundException, MalformedURLException
		{
			remoteHandler = null;

			e.printStackTrace();

			throw new IOException("Could not connect to server!");
		}
	}

	private void disconnect()
	{
		if (isConnected())
		{
			try
			{
				remoteHandler.removeRemoteExaminationDataHandlerListener(remoteEDHListener);
			}
			catch (RemoteException e) {} // dont care if this happens
		}

		remoteHandler = null;
	}

	private boolean isConnected()
	{
		return (remoteHandler != null);
	}


	// URL SETTING METHODS

	/**
	 * Sets the URL of the server containing the remote
	 * examination datahandler object. No check is made, it
	 * simply sets the URL. The connection will take place at
	 * the first call to a method that needs information kept
	 * by remote object.
	 */
	public void setExaminationDataLocation( String loc )
	{
		serverURL = loc;

		if (isConnected())
		{
			disconnect(); // removes eventual listener
		}

		/* NOTE: the getExaminationDataLocationID() method will return
		   either the server name as obtained from the server (if a
		   connection was made and everything worked), otherwise the
		   server URL will be returned. It does not throw an exception
		   since it handles both the case of connected and disconnected. */

		String id = getExaminationDataLocationID();

		fireExaminationLocationChanged(loc);

		fireExaminationLocationIDChanged(id);
	}

	/**
	 * Returns the currently set server URL. This might be
	 * null if it has not yet been set.
	 */
	public String getExaminationDataLocation( )
	{
		return serverURL;
	}

	/**
	 * Returns whether or not a connection can be
	 * established to the currently set examination
	 * data location. This is done by the client trying
	 * to connect to the server, and if everything is
	 * ok, returns true. If some error occurs while the
	 * client tries to connect to the specified location,
	 * a false value is returned.
	 */
	public boolean isExaminationDataLocationValid( )
	{
		if (serverURL == null)
		{
			return false;
		}

		if (!isConnected())
		{
			try
			{
				connect(); // -> IOException

				return true;
			}
			catch (IOException e)
			{
				return false;
			}
		}
		else
		{
			return true; // we are connected - must be valid
		}
	}

	/**
	 * Returns a location ID as specified from the server. This
	 * could be a string or something more useful than the raw
	 * IP address used to locate the server. If the ID could
	 * not be obtained from the server, this method will return
	 * the ordinary (raw) data location.
	 */
	public String getExaminationDataLocationID()
	{
		try
		{
			if (!isConnected())
			{
				connect(); // -> IOException
			}

			return remoteHandler.getServerName(); // return server name from server
		}
		catch (IOException e)
		{
			return getExaminationDataLocation(); // return url if connection didnt work
		}
	}


	// EVENT AND LISTENERS

	/**
	 * Adds an examination datahandler listener.
	 */
	public void addExaminationDataHandlerListener(ExaminationDataHandlerListener l)
	{
		listenerList.add(ExaminationDataHandlerListener.class, l);
	}

	/**
	 * Removes an examination datahandler listener.
	 */
	public void removeExaminationDataHandlerListener(ExaminationDataHandlerListener l)
	{
		listenerList.remove(ExaminationDataHandlerListener.class, l);
	}

	protected void fireExaminationAdded(ExaminationIdentifier id) // local
	{
		Object[] listeners = listenerList.getListenerList();

		ExaminationDataHandlerEvent event = new ExaminationDataHandlerEvent(this);

		event.setIdentifier(id);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == ExaminationDataHandlerListener.class)
			{
				((ExaminationDataHandlerListener)listeners[i+1]).examinationAdded(event);
			}
		}
	}

	protected void fireExaminationUpdated(ExaminationIdentifier id) // local
	{
		Object[] listeners = listenerList.getListenerList();

		ExaminationDataHandlerEvent event = new ExaminationDataHandlerEvent(this);

		event.setIdentifier(id);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == ExaminationDataHandlerListener.class)
			{
				((ExaminationDataHandlerListener)listeners[i+1]).examinationUpdated(event);
			}
		}
	}

	protected void fireExaminationLocationChanged(String location) // local
	{
		Object[] listeners = listenerList.getListenerList();

		ExaminationDataHandlerEvent event = new ExaminationDataHandlerEvent(this);

		event.setLocation(location);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == ExaminationDataHandlerListener.class)
			{
				((ExaminationDataHandlerListener)listeners[i+1]).examinationLocationChanged(event);
			}
		}
	}

	protected void fireExaminationLocationIDChanged(String locationID) // local
	{
		Object[] listeners = listenerList.getListenerList();

		ExaminationDataHandlerEvent event = new ExaminationDataHandlerEvent(this);

		event.setLocationID(locationID);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == ExaminationDataHandlerListener.class)
			{
				((ExaminationDataHandlerListener)listeners[i+1]).examinationLocationIDChanged(event);
			}
		}
	}


	// EXAMINATION DATA HANDLER INTERFACE REGULAR METHODS

	/**
	 * Returns the total number of examinations available in the data source
	 * handled by this ExaminationDataHandler.
	 *
	 * @return the number of examinations.
	 */
	public int getExaminationCount() throws IOException
	{
		try
		{
			if (!isConnected())
			{
				connect(); // -> IOException (after disconnecting)
			}

			return remoteHandler.getExaminationCount(); // -> RemoteException
		}
		catch (RemoteException e) // convert RemoteException -> IOException
		{
			e.printStackTrace();

			disconnect();

			throw new IOException("Could not communicate with server!");
		}
	}

	/**
	 * Returns the total number of examinations for the specified
	 * patient.
	 * @param pid PatientIdentifier
	 * @return int
	 * @throws IOException
	 */
	public int getExaminationCount(PatientIdentifier pid) throws IOException
	{
		return getExaminations(pid).length; // for now...
	}

	/**
	* Exports all examinations belonging to the specifiec patients to a new MVD file.
	*
	* @param patientIdentifiers the identifiers of the patients to export
	* @param newMVDlocation the location of the MVD to export to
	* @param notifiable ProgressNotifiable object to keep track of progress.
	* @param filter the examination content filter, which is applied to the data when exporting.
	* @param allowPartialExport whether to allow partial exports or not. If partial export is not allowed,
	* an IOException should be thrown if there is any overlap, no matter how small, between the target MVD and
	* the examinations we're exporting.
	* @return the amount of examinations that were exported.
	*/
	public int exportToMVD(PatientIdentifier[] patientIdentifiers,
				String newMVDlocation,
				ProgressNotifiable notifiable,
				ExaminationContentFilter filter,
				boolean allowPartialExport)
		 throws IOException
	{
		 // NOT IMPLEMENTED YET

		 return 0;
	}



	/**
	* Exports a set of examinations to a new MVD file.
	*
	* @param examinationIdentifiers the identifiers of the examinations to export
	* @param newMVDlocation the location of the MVD to export to
	* @param notifiable ProgressNotifiable object to keep track of progress.
	* @param filter the examination content filter, which is applied to the data when exporting.
	* @param allowPartialExport whether to allow partial exports or not. If partial export is not allowed,
	* an IOException should be thrown if there is any overlap, no matter how small, between the target MVD and
	* the examinations we're exporting.
	* @return the amount of examinations that were exported. (Not as useful here as for exportMVD(PatientIdentifier[],
	* but can be useful when allowing partial export
	*/
	public int exportToMVD(ExaminationIdentifier[] examinationIdentifiers,
				String newMVDlocation,
				ProgressNotifiable notifiable,
				ExaminationContentFilter filter,
				boolean allowPartialExport)
				throws IOException
	{
		// NOT IMPLEMENTED YET

		return 0;
	}

	/**
	 * Tries to obtain an array of examination identifiers
	 * for the specified patient code from the remote
	 * server object.
	 * @deprecated Use the corresponding PatientIdentifier method
	 * instead.
	 */
	public ExaminationIdentifier[] getExaminations( String patientCode ) throws IOException
	{
		return getExaminations(new PatientIdentifier(patientCode));
	}

	/**
	 * Tries to obtain an array of examination identifiers
	 * for the specified patient code from the remote
	 * server object.
	 */
	public ExaminationIdentifier[] getExaminations( PatientIdentifier pid ) throws IOException
	{
		try
		{
			if (!isConnected())
			{
				connect(); // -> IOException (after disconnecting)
			}

			ExaminationIdentifier[] rEID = remoteHandler.getExaminations(pid); // -> RemoteException

			return rEID; // -> RemoteException
		}
		catch (RemoteException e) // convert RemoteException -> IOException
		{
			e.printStackTrace();

			disconnect();

			throw new IOException("Could not communicate with server!");
		}
	}

	public final ExaminationValueContainer[] getAllExaminationValueContainers(ProgressNotifiable not) throws IOException
	{
		return getAllExaminationValueContainers(not, MedViewDataConstants.OPTIMIZE_FOR_MEMORY); // hint is ignored here anyway
	}

	public final ExaminationValueContainer[] getAllExaminationValueContainers(ProgressNotifiable not, int hint) throws IOException
	{
		try
		{
			if (!isConnected())
			{
				connect(); // -> IOException (after disconnecting)
			}

			ExaminationValueContainer[] retArr;

			if (not != null)
			{
				retArr = remoteHandler.getAllExaminationValueContainers(new RemoteProgressNotifiableImpl(not)); // -> RemoteException

				not.setIndeterminate(false); // if notification is used - the remote end should set indeterminate prior to transfer
			}
			else
			{
				retArr = remoteHandler.getAllExaminationValueContainers(null); // -> RemoteException
			}

			return retArr;
		}
		catch (RemoteException e) // convert RemoteException -> IOException
		{
			e.printStackTrace();

			disconnect();

			throw new IOException("Could not communicate with server!");
		}
	}

	/**
	 * Tries to obtain an examination value container
	 * from the remote server object.
	 */
	public medview.datahandling.examination.ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id) throws
		IOException, NoSuchExaminationException
	{
		/* NOTE: this method is very tricky when used over RMI. At the server-side, the
		   server's ExaminationValueContainer implementations have their strings internalized,
		   i.e. the strings are placed in the private pool kept by the String class. When
		   marshalled / unmarshalled, the corresponding EVC object being unmarshaled does
		   not seem to re-internalize the strings, thus resulting in huge memory consumption
		   (which is the reason we internalize the strings in the core in the first place).
		   This is why a new EVC object needs to be created and returned from this method,
		   in which we internalize the string at construction. - Fredrik 050906 */

		try
		{
			if (!isConnected())
			{
				connect(); // -> IOException (after disconnecting)
			}

			ExaminationValueContainer evc = remoteHandler.getExaminationValueContainer(id); // -> RemoteException, NoSuchExaminationException

			evc.internalize(); // re-intern after server retrieval

			return evc;
		}
		catch (RemoteException e) // convert RemoteExceptions -> IOExceptions
		{
			e.printStackTrace();

			disconnect();

			throw new IOException("Could not communicate with server!");
		}
	}

	/**
	 * Tries to obtain an examination value container
	 * from the remote server object. The remote client EDH
	 * simply ignores this method, thus it is exactly the same
	 * as calling getExaminationValueContainer(id).
	 */
	public ExaminationValueContainer getExaminationValueContainer( ExaminationIdentifier id, int hint ) throws
		IOException, NoSuchExaminationException, InvalidHintException
	{
		return getExaminationValueContainer(id);
	}

	/**
	 * Tries to obtain an image count for the specified patient
	 * from the remote server object.
	 * @deprecated Use the corresponding PatientIdentifier method
	 * instead.
	 */
	public int getImageCount( String patientIdentifier ) throws IOException
	{
		return getImageCount(new PatientIdentifier(patientIdentifier));
	}

	/**
	 * Tries to obtain an image count for the specified patient
	 * from the remote server object.
	 */
	public int getImageCount( PatientIdentifier pid ) throws IOException
	{
		try
		{
			if (!isConnected())
			{
				connect(); // -> IOException (after disconnecting)
			}

			return remoteHandler.getImageCount(pid); // -> RemoteException
		}
		catch (RemoteException e) // convert RemoteException -> IOException
		{
			e.printStackTrace();

			disconnect();

			throw new IOException("Could not communicate with server!");
		}
	}

	/**
	 * Tries to obtain an array of examination image objects
	 * from the remote server object. This is done by retrieving
	 * the ragged byte array from the server, and reconstructing
	 * the images based on these bytes.
	 */
	public ExaminationImage[] getImages( ExaminationIdentifier id ) throws
		IOException, NoSuchExaminationException
	{
		try
		{
			if (!isConnected())
			{
				connect(); // -> IOException (after disconnecting)
			}

			byte[][] byteArrays = remoteHandler.getImages(id); // ragged arrays

			ExaminationImage[] retArray = new ExaminationImage[byteArrays.length]; // -> RemoteException

			for (int ctr = 0; ctr < byteArrays.length; ctr++)
			{
				retArray[ctr] = new ByteArrayExaminationImage(byteArrays[ctr], id); // tracks
			}

			return retArray;
		}
		catch (RemoteException e) // convert RemoteException -> IOException
		{
			e.printStackTrace();

			disconnect();

			throw new IOException("Could not communicate with server!");
		}
	}

	/**
	 * Tries to obtain all the currently kept patients in
	 * the remote server object. This method does not
	 * notify the user of any progress.
	 */
	public PatientIdentifier[] getPatients( ) throws IOException
	{
		return getPatients(null);
	}

	/**
	 * Tries to obtain all the currently kept patients in
	 * the remote server object, and notifies a notifiable
	 * of progress as this is done. The notifiable is
	 * wrapped in a remote progress notifiable. The server
	 * retrieves a stub to this object, and communicates
	 * progress to it. The object in turn passes on the
	 * notification to the local progress notifiable which
	 * is wrapped within.
	 */
	public PatientIdentifier[] getPatients( ProgressNotifiable not ) throws IOException
	{
		try
		{
			if (!isConnected())
			{
				connect(); // -> IOException (after disconnecting);
			}

			if (not != null)
			{
				return remoteHandler.getPatients(new RemoteProgressNotifiableImpl(not)); // -> RemoteException
			}
			else
			{
				return remoteHandler.getPatients(null); // -> RemoteException
			}
		}
		catch (RemoteException e) // convert RemoteExceptions -> IOExceptions
		{
			e.printStackTrace();

			disconnect();

			throw new IOException("Could not communicate with server!");
		}
	}

	/**
	 * Tries to obtain an array of newly added examinations
	 * from the remote server object.
	 * @deprecated this method is not used anymore.
	 */
	public ExaminationIdentifier[] refreshExaminations( ) throws IOException
	{
		return new ExaminationIdentifier[0];
	}

	/**
	 * Tries to obtain an array of newly added examinations
	 * from the remote server object since the specified time.
	 * @deprecated this method is not used anymore.
	 */
	public ExaminationIdentifier[] refreshExaminations( long sinceTime ) throws IOException
	{
		return new ExaminationIdentifier[0];
	}

	/**
	 * Saves the specified tree along with the specified images
	 * to the remote server.
	 *
	 * @param tree Tree the tree to save.
	 * @param images ExaminationImage[] the images to save.
	 * @throws IOException if some error occurs.
	 */
	public int saveExamination( Tree tree, ExaminationImage[] images ) throws IOException
	{
		try
		{
			if (!isConnected())
			{
				connect(); // -> IOException
			}

			ExaminationIdentifier eid = MedViewUtilities.constructExaminationIdentifier(tree); // -> CouldNotConstructIdentifierException

			byte[][] imageByteArray = null;

			if ((images != null) && (images.length != 0))
			{
				imageByteArray = new byte[images.length][];

				for (int ctr = 0; ctr < images.length; ctr++)
				{
					InputStream iS = images[ctr].getInputStream(); // -> IOException

					imageByteArray[ctr] = new byte[iS.available()];

					iS.read(imageByteArray[ctr]);

					iS.close();
				}
			}
			else
			{
				imageByteArray = new byte[0][];
			}

			return remoteHandler.saveExamination(tree, imageByteArray, eid);
		}
		catch (RemoteException e) // convert RemoteExceptions -> IOExceptions
		{
			e.printStackTrace();

			disconnect();

			throw new IOException("Could not communicate with server!");
		}
		catch (IOException e) // only for obtaining a stack trace if getInputStream() was cause
		{
			e.printStackTrace();

			/* NOTE: don't disconnect here, since if the cause is in the connect()
			   method, it will have disconnected the server. If the cause is in the
			   obtainal of the image inputstream, we should not disconnect the
			   server. */

			throw e;
		}
		catch (CouldNotConstructIdentifierException e)
		{
			e.printStackTrace();

			throw new IOException(e.getMessage());
		}
	}

	/**
	 * Saves the specified tree to the specified location.
	 * Observe that this method bypasses the currently set
	 * examination data location. The method is internally
	 * synchronized to avoid retrieval deadlock after an
	 * event has been fired. The intended use of this method
	 * is to be able to store a tree to another location
	 * without having to set the core examination data location,
	 * for instance for storing a tree to a temporary location
	 * if the primary location is inaccessible.
	 */
	public int saveExamination( Tree tree, ExaminationImage[] imageArray, String location) throws IOException
	{
		return -1; // NOT IMPLEMENTED YET
	}

	public void removeExamination(ExaminationIdentifier eid) throws IOException
	{
		// TODO: implement this
	}


	// CONSTRUCTOR

	/**
	 * Constructs a remote examination datahandler client,
	 * implementing the ExaminationDataHandler interface.
	 */
	public RemoteExaminationDataHandlerClient()
	{
		try
		{
			remoteEDHListener = new RemoteExaminationDataHandlerListenerImpl(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();

			System.exit(1); // fatal error
		}
	}

	// MEMBERS

	private EventListenerList listenerList = new EventListenerList();

	private RemoteExaminationDataHandlerListener remoteEDHListener;

	private RemoteExaminationDataHandler remoteHandler = null;

	private String serverURL = null;
}
