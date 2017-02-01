package medview.medserver.data;

import java.io.*;

import java.rmi.*;
import java.rmi.server.*;

import java.util.*;

import javax.swing.event.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;
import medview.datahandling.examination.tree.*;
import medview.datahandling.images.*;

import misc.foundation.*;

/**
 * The implementation class for the remote object that
 * provides examination data services to all clients.
 *
 * @author Fredrik Lindahl
 */
public class RemoteExaminationDataHandlerImpl extends UnicastRemoteObject
	implements RemoteExaminationDataHandler, MedViewDataConstants
{
	// MEMBERS

	private EventListenerList listenerList = new EventListenerList();

	private MedViewDataHandler mVDH = MedViewDataHandler.instance();

	private Vector registeredClientListeners = new Vector();

	private Vector registeredLocalListeners = new Vector();

	private String serverNameToClients = null;


	// CONSTRUCTOR

	/**
	 * Constructs an implementation of the RemoteExaminationDataHandler
	 * remote interface. Will attach listener to the server data layer,
	 * ensuring that data location and data location ID change events in
	 * the server will be propagated to the clients (provided they have
	 * attached remote stub listeners).
	 *
	 * @throws RemoteException if the implementation cannot be instantiated.
	 */
	public RemoteExaminationDataHandlerImpl() throws RemoteException
	{
		mVDH.addMedViewDataListener(new MedViewDataAdapter()
		{
			public void examinationDataLocationChanged(MedViewDataEvent e)
			{
				fireExaminationDataLocationChanged(); // notify clients of change
			}
		});
	}


	// SERVER SHUTTONG DOWN

	public void serverShuttingDown()
	{
		fireServerShuttingDown();
	}

	// OBTAINING PATIENT DATA

	/**
	 * Obtain an array of all patients kept on the server.
	 * Might take some time, therefore there is the opportunity
	 * to pass along a remote progress notifiable.
	 *
	 * @param notifiable RemoteProgressNotifiable for notification
	 * of progress.
	 * @return PatientIdentifier[] the patients kept on the server.
	 * @throws RemoteException if communication fails.
	 */
	public PatientIdentifier[] getPatients(final RemoteProgressNotifiable notifiable)
		throws RemoteException
	{
		try
		{
			firePatientListRequested();

			if (notifiable == null)
			{
				return mVDH.getPatients();
			}
			else
			{
				return mVDH.getPatients(new DefaultProgressNotifiable() // wrapper
				{
					public void setCurrent(int c)
					{
						super.setCurrent(c);

						try
						{
							if ((c % 50) == 0)
							{
								notifiable.setCurrent(c);
							}
						}
						catch (RemoteException e) { e.printStackTrace(); }
					}

					public void setDescription(String d)
					{
						super.setDescription(d);

						try
						{
							notifiable.setDescription(d);
						}
						catch (RemoteException e) { e.printStackTrace(); }
					}

					public void setTotal(int t)
					{
						super.setTotal(t);

						try
						{
							notifiable.setTotal(t);
						}
						catch (RemoteException e) { e.printStackTrace(); }
					}
				});

			}
		}
		catch (IOException e)
		{
			throw new RemoteException(e.getMessage());
		}
	}


	// STORING EXAMINATION DATA

	/**
	 * Stores the specified tree, along with the images and an
	 * examination identifier identifying the examination associated
	 * with the tree, into the servers knowledge base.
	 *
	 * @param tree Tree the tree to store.
	 * @param images byte[][] the images associated with the tree.
	 * @param eid ExaminationIdentifier the examination the tree
	 * represents.
	 * @throws RemoteException if communication fails.
	 */
	public synchronized int saveExamination(Tree tree, byte[][] images, ExaminationIdentifier eid)
		throws RemoteException
	{
		try
		{
			// construct new relative paths of images (server mvd)

			ExaminationImage[] eImages = new ExaminationImage[images.length];

			for (int ctr=0; ctr<images.length; ctr++)
			{
				String imageName = eid + "_" + (ctr+1) + MVD_IMAGE_FORMAT_FILE_ENDER;

				eImages[ctr] = new ByteArrayExaminationImage(images[ctr], eid, imageName);
			}

			// remove old paths in tree and insert new relative paths based on examination id

			Tree photoNode = tree.getNode(PHOTO_TERM_NAME);

			photoNode.removeAllChildren();

			for (int ctr=0; ctr<eImages.length; ctr++)
			{
				photoNode.addChild(new TreeLeaf(MVD_PICTURES_SUBDIRECTORY + File.separator +

					MVD_PICTURES_SUBDIRECTORY + File.separator + eImages[ctr].getName())); // the pic dir constant does not end in filesep (doc)
			}

			// call the data layer to save the examination

			int retVal = mVDH.saveExamination(tree, eImages); // data layer will fire event

			if (retVal == ExaminationDataHandler.NEW_EXAMINATION) // save resulted in a new examination being added
			{
				fireExaminationAdded(eid); // only fire if new examination has been added
			}
			else
			{
				fireExaminationUpdated(eid);
			}

			/* NOTE: it might also be the case that a save results in a
			   previous examination being updated (for instance when you
			   are using input and save it in multiple passes before you
			   are done with the examination). In this case, we don't want
			   to fire an event indicating that an examination has been
			   added, since this would not be true. An alternative method
			   of doing this would be to fire some sort of 'examination
			   updated' event instead. But this works fine as it is, and
			   there's no need to do so right now. */

			return retVal;
		}
		catch (IOException e)
		{
			throw new RemoteException(e.getMessage());
		}
	}


	// IMAGE DATA

	/**
	 * Obtain the number of images associated with the
	 * specified patient.
	 *
	 * @param pid PatientIdentifier the patient.
	 * @return int the number of images associated with
	 * the patient.
	 * @throws RemoteException if communication fails.
	 */
	public int getImageCount(PatientIdentifier pid)	throws RemoteException
	{
		try
		{
			fireImageCountRequested(pid);

			return mVDH.getImageCount(pid);
		}
		catch (IOException e)
		{
			throw new RemoteException(e.getMessage());
		}
	}

	/**
	 * Return a byte array of bytes representing each
	 * image associated with the specified examination.
	 *
	 * @param id ExaminationIdentifier the examination for
	 * which you want to obtain the images.
	 * @return byte[][] the image byte array of bytes.
	 * @throws RemoteException if communication fails.
	 */
	public byte[][] getImages(ExaminationIdentifier id) throws RemoteException
	{
		try
		{
			fireImagesRequested(id);

			ExaminationImage[] images = mVDH.getImages(id);

			byte[][] retArray = new byte[images.length][];

			for (int ctr=0; ctr<images.length; ctr++)
			{
				InputStream iS = images[ctr].getInputStream();

				retArray[ctr] = new byte[iS.available()];

				iS.read(retArray[ctr]);

				iS.close();
			}

			return retArray;
		}
		catch (IOException e)
		{
			throw new RemoteException(e.getMessage());
		}
	}


	// OBTAINING EXAMINATION DATA

	/**
	 * Returns the total number of examinations available in
	 * the data source handled by the server.
	 *
	 * @return the number of examinations.
	 */
	public int getExaminationCount() throws RemoteException
	{
		try
		{
			fireExaminationCountRequested();

			return mVDH.getExaminationCount();
		}
		catch (IOException e)
		{
			throw new RemoteException(e.getMessage());
		}
	}

	/**
	 * Returns all examinations found in the knowledge base after the
	 * specified time.
	 *
	 * @param sinceDate the examinations returned are the ones added to
	 * the knowledge base after this time.
	 * @return ExaminationIdentifier[] all examinations found in the
	 * knowledge base after the specified time.
	 * @throws IOException if something goes wrong.
	 */
	public ExaminationIdentifier[] refreshExaminations( long sinceTime ) throws RemoteException
	{
		try
		{
			fireRefreshPerformed();

			return mVDH.refreshExaminations(sinceTime);
		}
		catch (IOException e)
		{
			throw new RemoteException(e.getMessage());
		}
	}

	/**
	 * Obtain the examinations for the specified patient.
	 *
	 * @param pid PatientIdentifier the patient for which
	 * you want to obtain examinations.
	 * @return ExaminationIdentifier[] the examinations.
	 * @throws RemoteException if communication fails.
	 */
	public ExaminationIdentifier[] getExaminations(PatientIdentifier pid)
		throws RemoteException
	{
		try
		{
			fireExaminationListRequested(pid);

			return mVDH.getExaminations(pid);
		}
		catch (IOException e)
		{
			throw new RemoteException(e.getMessage());
		}
	}

	/**
	 * Obtains all examination value containers.
	 * @param notifiable RemoteProgressNotifiable
	 * @return ExaminationValueContainer[]
	 * @throws RemoteException
	 */
	public ExaminationValueContainer[] getAllExaminationValueContainers(final RemoteProgressNotifiable notifiable)
		throws RemoteException
	{
		try
		{
			fireAllExaminationValueContainersRequested();

			ExaminationValueContainer[] retArr;

			if (notifiable == null)
			{
				retArr = mVDH.getAllExaminationValueContainers(null); // -> IOException
			}
			else
			{
				retArr = mVDH.getAllExaminationValueContainers(new DefaultProgressNotifiable() // -> IOException
				{
					public void setCurrent(int c)
					{
						super.setCurrent(c);

						try
						{
							if (c % 100 == 0 || c >= getTotal()-1) // are we consistent with 'current'?
							{
								notifiable.setCurrent(c);
							}
						}
						catch (RemoteException e) { e.printStackTrace(); }
					}

					public void setDescription(String d)
					{
						super.setDescription(d);

						try
						{
							notifiable.setDescription(d);
						}
						catch (RemoteException e) { e.printStackTrace(); }
					}

					public void setTotal(int t)
					{
						super.setTotal(t);

						try
						{
							notifiable.setTotal(t);
						}
						catch (RemoteException e) { e.printStackTrace(); }
					}
				});

			}

			try
			{
				notifiable.setDescription("Transferring value container array"); // -> RemoteException, TODO - language

				notifiable.setIndeterminate(true);
			}
			catch (RemoteException exc)
			{
				exc.printStackTrace();
			}

			return retArr;
		}
		catch (IOException e)
		{
			throw new RemoteException(e.getMessage());
		}
	}

	/**
	 * Obtains a value container for the specified examination.
	 *
	 * @param id ExaminationIdentifier the examination that you
	 * want to obtain the values for.
	 * @return ExaminationValueContainer the value container.
	 * @throws RemoteException if communication fails.
	 * @throws NoSuchExaminationException if there is no such
	 * examination on the server.
	 */
	public ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id)
		throws RemoteException
	{
		try
		{
			fireExaminationValueContainerRequested(id);

			return mVDH.getExaminationValueContainer(id, OPTIMIZE_FOR_EFFICIENCY);
		}
		catch (IOException e)
		{
			throw new RemoteException(e.getMessage());
		}
		catch (NoSuchExaminationException e)
		{
			throw new RemoteException(e.getMessage());
		}
		catch (InvalidHintException e)
		{
			throw new RemoteException(e.getMessage());
		}
	}


	// SERVER NAME TO CLIENTS

	/**
	 * Obtains the server name as specified by the server.
	 *
	 * @return String the server name.
	 * @throws RemoteException if communication fails.
	 */
	public String getServerName() throws RemoteException
	{
		return this.serverNameToClients;
	}

	/**
	 * Local method setting the server name to clients.
	 *
	 * @param name String the name of the server shown
	 * to the clients.
	 */
	public void setServerNameToClients(String name)
	{
		this.serverNameToClients = name;

		fireServerNameChanged();
	}


	// LISTENERS AND EVENTS

	/**
	 * Adds a remote examination datahandler listener to the list of
	 * such listeners kept by this implementation class. This
	 * method is visible to clients, who may add their own
	 * implementations of the RemoteExaminationDataHandlerListener to
	 * the implementation class of the server. The implementation
	 * stub class (template) must be found on the server though.
	 *
	 * @param l RemoteExaminationDataHandlerListener the stub to add
	 * to the list of remote listeners.
	 */
	public synchronized void addRemoteExaminationDataHandlerListener(RemoteExaminationDataHandlerListener l)
	{
		registeredClientListeners.add(l);
	}

	/**
	 * Removes a remote examination datahandler listener from the list of
	 * such listeners kept by this implementation class. This
	 * method is visible to clients, who may add their own
	 * implementations of the RemoteExaminationDataHandlerListener to
	 * the implementation class of the server. The implementation
	 * stub class (template) must be found on the server though.
	 *
	 * @param l RemoteExaminationDataHandlerListener the stub to remove
	 * from the list of remote listeners.
	 */
	public synchronized void removeRemoteExaminationDataHandlerListener(RemoteExaminationDataHandlerListener l)
	{
		registeredClientListeners.remove(l);
	}

	/**
	 * This method is not visible to the clients, who only see
	 * the methods defined by the RemoteExaminationDataHandler interface.
	 * These methods can be used by the server classes to listen
	 * to events that only are of interest to the local server.
	 *
	 * @param l RemoteExaminationDataHandlerListener the listener to add
	 * to the list of local listeners.
	 */
	public void addRemoteExaminationDataHandlerImplListener(RemoteExaminationDataHandlerImplListener l)
	{
		registeredLocalListeners.add(l);
	}

	/**
	 * This method is not visible to the clients, who only see
	 * the methods defined by the RemoteExaminationDataHandler interface.
	 * These methods can be used by the server classes to listen
	 * to events that only are of interest to the local server.
	 *
	 * @param l RemoteExaminationDataHandlerListener the listener to remove
	 * from the list of local listeners.
	 */
	public void removeRemoteExaminationDataHandlerImplListener(RemoteExaminationDataHandlerImplListener l)
	{
		registeredLocalListeners.remove(l);
	}

	/**
	 * Will fire an event indicating that the specified
	 * examination has been added to the database. This event
	 * is fired to all registered remote client stubs,
	 * as well as all registered local server listeners.
	 *
	 * @param id ExaminationIdentifier identifies the examination
	 * that has been added to the server's knowledge base.
	 */
	private void fireExaminationAdded(ExaminationIdentifier id)
	{
		// construct event

		RemoteExaminationDataHandlerEvent event = new RemoteExaminationDataHandlerEvent();

		event.setIdentifier(id);

		// fire to registered remote listener stubs

		if (registeredClientListeners.size() != 0)
		{
			Vector removeVector = new Vector();

			Enumeration enm = registeredClientListeners.elements();

			while (enm.hasMoreElements()) // for each of the stubs
			{
				RemoteExaminationDataHandlerListener l = (RemoteExaminationDataHandlerListener) enm.nextElement();

				try
				{
					l.examinationAdded(event); // event is serialized and sent to client implementation
				}
				catch (RemoteException e) // remove from listenerlist if communication failure with client stub
				{
					e.printStackTrace();

					removeVector.add(l); // OBS: we cant remove immediately (enmeration will screw up)
				}
			}

			enm = removeVector.elements(); // remove vector contains the ones we couldnt fire to

			while (enm.hasMoreElements())
			{
				RemoteExaminationDataHandlerListener l = (RemoteExaminationDataHandlerListener) enm.nextElement();

				removeRemoteExaminationDataHandlerListener(l); // client is down, remove the listener stub
			}
		}

		/* NOTE: we need to fire an 'examination added' event upwards to be able
		   to know which client added an examination. Note that we will also receive
		   an event from the data layer stating that an examination has been added,
		   but it does not carry any information about the client adding the
		   examination since this is pure 'server' information. */

		// fire to local server listeners (RemoteTermDataHandlerImplListener implementations)

		try
		{
			if (registeredLocalListeners.size() != 0)
			{
				event.setClientHost(RemoteServer.getClientHost());

				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					((RemoteExaminationDataHandlerImplListener)enm.nextElement()).examinationAdded(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Will fire an event indicating that the specified
	 * examination has been updated in the database. This event
	 * is fired to all registered remote client stubs,
	 * as well as all registered local server listeners.
	 *
	 * @param id ExaminationIdentifier identifies the examination
	 * that has been updated in the server's knowledge base.
	 */
	private void fireExaminationUpdated(ExaminationIdentifier id)
	{
		// construct event

		RemoteExaminationDataHandlerEvent event = new RemoteExaminationDataHandlerEvent();

		event.setIdentifier(id);

		// fire to registered remote listener stubs

		if (registeredClientListeners.size() != 0)
		{
			Vector removeVector = new Vector();

			Enumeration enm = registeredClientListeners.elements();

			while (enm.hasMoreElements()) // for each of the stubs
			{
				RemoteExaminationDataHandlerListener l = (RemoteExaminationDataHandlerListener) enm.nextElement();

				try
				{
					l.examinationUpdated(event); // event is serialized and sent to client implementation
				}
				catch (RemoteException e) // remove from listenerlist if communication failure with client stub
				{
					e.printStackTrace();

					removeVector.add(l); // OBS: we cant remove immediately (enmeration will screw up)
				}
			}

			enm = removeVector.elements(); // remove vector contains the ones we couldnt fire to

			while (enm.hasMoreElements())
			{
				RemoteExaminationDataHandlerListener l = (RemoteExaminationDataHandlerListener) enm.nextElement();

				removeRemoteExaminationDataHandlerListener(l); // client is down, remove the listener stub
			}
		}

		/* NOTE: we need to fire an 'examination updated' event upwards to be able
		   to know which client added an examination. Note that we will also receive
		   an event from the data layer stating that an examination has been updated,
		   but it does not carry any information about the client updating the
		   examination since this is pure 'server' information. */

		// fire to local server listeners (RemoteTermDataHandlerImplListener implementations)

		try
		{
			if (registeredLocalListeners.size() != 0)
			{
				event.setClientHost(RemoteServer.getClientHost());

				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					((RemoteExaminationDataHandlerImplListener)enm.nextElement()).examinationUpdated(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Will fire an event indicating that the data location
	 * ID has been changed on the server. This event will
	 * be fired to all registered remote client listeners,
	 * as well as all registered local server listeners.
	 */
	private void fireServerNameChanged()
	{
		// construct event

		RemoteExaminationDataHandlerEvent event = new RemoteExaminationDataHandlerEvent();

		event.setServerName(serverNameToClients);

		// fire to registered remote listener stubs

		if (registeredClientListeners.size() != 0)
		{
			Vector removeVector = new Vector();

			Enumeration enm = registeredClientListeners.elements();

			while (enm.hasMoreElements()) // for each of the stubs
			{
				RemoteExaminationDataHandlerListener l = (RemoteExaminationDataHandlerListener) enm.nextElement();

				try
				{
					l.serverNameChanged(event); // event is serialized and sent to client implementation
				}
				catch (RemoteException e) // remove from listenerlist if communication failure with client stub
				{
					e.printStackTrace();

					removeVector.add(l); // OBS: we cant remove immediately (enmeration will screw up)
				}
			}

			enm = removeVector.elements(); // remove vector contains the ones we couldnt fire to

			while (enm.hasMoreElements())
			{
				RemoteExaminationDataHandlerListener l = (RemoteExaminationDataHandlerListener) enm.nextElement();

				removeRemoteExaminationDataHandlerListener(l);; // client is down, remove the listener stub
			}
		}


		// fire to local server listeners (RemoteTermDataHandlerImplListener implementations)

		if (registeredLocalListeners.size() != 0)
		{
			Enumeration enm = registeredLocalListeners.elements();

			while (enm.hasMoreElements())
			{
				((RemoteExaminationDataHandlerImplListener)enm.nextElement()).serverNameChanged(event);
			}
		}
	}

	/**
	 * The firing of an examination data location changed event
	 * from the server indicates that the server's local examination
	 * location has changed. The server listens to the data layer
	 * for changes, and will fire this event if the data layer says
	 * that the data location has been changed.
	 */
	private void fireExaminationDataLocationChanged()
	{
		// construct event

		RemoteExaminationDataHandlerEvent event = new RemoteExaminationDataHandlerEvent();

		// fire to registered remote listener stubs

		if (registeredClientListeners.size() != 0)
		{
			Vector removeVector = new Vector();

			Enumeration enm = registeredClientListeners.elements();

			while (enm.hasMoreElements()) // for each of the stubs
			{
				RemoteExaminationDataHandlerListener l = (RemoteExaminationDataHandlerListener) enm.nextElement();

				try
				{
					l.locationChanged(event); // event is serialized and sent to client implementation
				}
				catch (RemoteException e) // remove from listenerlist if communication failure with client stub
				{
					e.printStackTrace();

					removeVector.add(l); // OBS: we cant remove immediately (enmeration will screw up)
				}
			}

			enm = removeVector.elements(); // remove vector contains the ones we couldnt fire to

			while (enm.hasMoreElements())
			{
				RemoteExaminationDataHandlerListener l = (RemoteExaminationDataHandlerListener) enm.nextElement();

				removeRemoteExaminationDataHandlerListener(l);; // client is down, remove the listener stub
			}
		}


		// fire to local server listeners (RemoteTermDataHandlerImplListener implementations)

		if (registeredLocalListeners.size() != 0)
		{
			Enumeration enm = registeredLocalListeners.elements();

			while (enm.hasMoreElements())
			{
				((RemoteExaminationDataHandlerImplListener)enm.nextElement()).locationChanged(event);
			}
		}
	}

	private void fireServerShuttingDown()
	{
		// construct event

		RemoteExaminationDataHandlerEvent event = new RemoteExaminationDataHandlerEvent();

		// fire to registered remote listener stubs

		if (registeredClientListeners.size() != 0)
		{
			Vector removeVector = new Vector();

			Enumeration enm = registeredClientListeners.elements();

			while (enm.hasMoreElements()) // for each of the stubs
			{
				RemoteExaminationDataHandlerListener l = (RemoteExaminationDataHandlerListener) enm.nextElement();

				try
				{
					l.serverShuttingDown(event); // event is serialized and sent to client implementation
									}
				catch (RemoteException e) // remove from listenerlist if communication failure with client stub
				{
					e.printStackTrace();

					removeVector.add(l); // OBS: we cant remove immediately (enmeration will screw up)
				}
			}

			enm = removeVector.elements(); // remove vector contains the ones we couldnt fire to

			while (enm.hasMoreElements())
			{
				RemoteExaminationDataHandlerListener l = (RemoteExaminationDataHandlerListener) enm.nextElement();

				removeRemoteExaminationDataHandlerListener(l);; // client is down, remove the listener stub
			}
		}
	}

	/**
	 * Fires an event to all locally registered local
	 * server listeners. Nothing is fired remotely.
	 */
	private void firePatientListRequested()
	{
		try
		{
			if (registeredLocalListeners.size() != 0)
			{
				RemoteExaminationDataHandlerEvent event = new RemoteExaminationDataHandlerEvent();

				event.setClientHost(RemoteServer.getClientHost());

				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					((RemoteExaminationDataHandlerImplListener)enm.nextElement()).patientListRequested(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}

	private void fireExaminationCountRequested()
	{
		try
		{
			if (registeredLocalListeners.size() != 0)
			{
				RemoteExaminationDataHandlerEvent event = new RemoteExaminationDataHandlerEvent();

				event.setClientHost(RemoteServer.getClientHost());

				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					((RemoteExaminationDataHandlerImplListener)enm.nextElement()).examinationCountRequested(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Fires an event to all locally registered local
	 * server listeners. Nothing is fired remotely.
	 *
	 * @param patient PatientIdentifier the patient for
	 * which the examination list was requested.
	 */
	private void fireExaminationListRequested(PatientIdentifier patient)
	{
		try
		{
			if (registeredLocalListeners.size() != 0)
			{
				RemoteExaminationDataHandlerEvent event = new RemoteExaminationDataHandlerEvent();

				event.setClientHost(RemoteServer.getClientHost());

				event.setPatient(patient);

				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					((RemoteExaminationDataHandlerImplListener)enm.nextElement()).examinationListRequested(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Fires an event to all locally registered local
	 * server listeners. Nothing is fired remotely.
	 *
	 * @param id ExaminationIdentifier the examination for
	 * which the value container was requested.
	 */
	private void fireAllExaminationValueContainersRequested()
	{
		try
		{
			if (registeredLocalListeners.size() != 0)
			{
				RemoteExaminationDataHandlerEvent event = new RemoteExaminationDataHandlerEvent();

				event.setClientHost(RemoteServer.getClientHost());

				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					((RemoteExaminationDataHandlerImplListener)enm.nextElement()).allExaminationValueContainersRequested(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Fires an event to all locally registered local
	 * server listeners. Nothing is fired remotely.
	 *
	 * @param id ExaminationIdentifier the examination for
	 * which the value container was requested.
	 */
	private void fireExaminationValueContainerRequested(ExaminationIdentifier id)
	{
		try
		{
			if (registeredLocalListeners.size() != 0)
			{
				RemoteExaminationDataHandlerEvent event = new RemoteExaminationDataHandlerEvent();

				event.setClientHost(RemoteServer.getClientHost());

				event.setIdentifier(id);

				event.setPatient(id.getPID());

				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					((RemoteExaminationDataHandlerImplListener)enm.nextElement()).examinationValueContainerRequested(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Fires an event to all locally registered local
	 * server listeners. Nothing is fired remotely.
	 *
	 * @param patient PatientIdentifier the patient for
	 * which an image count was requested.
	 */
	private void fireImageCountRequested(PatientIdentifier patient)
	{
		try
		{
			if (registeredLocalListeners.size() != 0)
			{
				RemoteExaminationDataHandlerEvent event = new RemoteExaminationDataHandlerEvent();

				event.setClientHost(RemoteServer.getClientHost());

				event.setPatient(patient);

				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					((RemoteExaminationDataHandlerImplListener)enm.nextElement()).imageCountRequested(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Fires an event to all locally registered local
	 * server listeners. Nothing is fired remotely.
	 *
	 * @param id ExaminationIdentifier the examination for
	 * which the images were requested.
	 */
	private void fireImagesRequested(ExaminationIdentifier id)
	{
		try
		{
			if (registeredLocalListeners.size() != 0)
			{
				RemoteExaminationDataHandlerEvent event = new RemoteExaminationDataHandlerEvent();

				event.setClientHost(RemoteServer.getClientHost());

				event.setIdentifier(id);

				event.setPatient(id.getPID());

				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					((RemoteExaminationDataHandlerImplListener)enm.nextElement()).imagesRequested(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Fires an event to all locally registered local
	 * server listeners. Nothing is fired remotely.
	 */
	private void fireRefreshPerformed()
	{
		try
		{
			if (registeredLocalListeners.size() != 0)
			{
				RemoteExaminationDataHandlerEvent event = new RemoteExaminationDataHandlerEvent();

				event.setClientHost(RemoteServer.getClientHost());

				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					((RemoteExaminationDataHandlerImplListener)enm.nextElement()).refreshPerformed(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}

}
