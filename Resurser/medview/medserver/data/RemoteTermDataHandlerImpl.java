package medview.medserver.data;

import java.io.*;

import java.rmi.*;
import java.rmi.server.*;

import java.util.*;

import medview.datahandling.*;

/**
 * The server implementation of the remote term datahandler.
 * It is important to realize the events being fired from
 * this implementation are in regard to what is done from
 * a <i>client</i>. For example, if the server locally adds
 * a term, no event will be fired from this object. If a
 * client adds a term to the server, this object fires an
 * event, providing information about the term and the client
 * host that added the term. Locally, an event will also be
 * fired <i>from the datahandling layer</i> indicating that an
 * event has been added to the database. They indicate two
 * different things.
 *
 * All methods that mutate the database are synchronized,
 * while information retrieval methods are not. Thus, when
 * a client is notified of an update, it can safely retrieve
 * information from the server without deadlock occuring.
 * 
 * Also note that the event used in the object cannot be shared,
 * since this would lead to synchronization problems. For instance,
 * if thread A (spawned by a remote client call) does something
 * triggering a firing to remote clients, and one of those clients
 * returns to the server spawning a new thread B, and thread B
 * does something firing something, then it might be the case that
 * thread B will enter a fire() method and clear the event before
 * thread A has finished it's firing method. Then thread A will
 * fire an event containing inproper (perhaps null) values.
 * 
 * @author Fredrik Lindahl
 */
public class RemoteTermDataHandlerImpl extends UnicastRemoteObject implements RemoteTermDataHandler
{
	// MEMBERS

	private MedViewDataHandler mVDH = MedViewDataHandler.instance();

	private Vector registeredClientListeners = new Vector();

	private Vector registeredLocalListeners = new Vector();
	
	// CONSTRUCTOR

	public RemoteTermDataHandlerImpl() throws RemoteException {}

	
	// SERVER SHUTTING DOWN

	public void serverShuttingDown()
	{		
		fireServerShuttingDown();
	}
	
	
	// OBTAINING TERMS
	
	/**
	 * Obtains an array of all terms kept in the server.
	 * 
	 * @throws RemoteException if communication fails.
	 * @return String[] the array of server terms.
	 */
	public String[] getTerms( ) throws RemoteException
	{
		try
		{
			fireTermListRequested(); // event sent to local server listeners only

			return mVDH.getTerms();
		}
		catch (IOException e) // convert local IOException -> RemoteException
		{
			throw new RemoteException(e.getMessage());
		}
	}
	
	/**
	 * Obtain a hashmap of TermData objects.
	 * 
	 * @return HashMap hashmap of TermData objects.
	 * @throws RemoteException if the communication doesnt work.
	 */
	public HashMap getTermDataHashMap() throws RemoteException
	{
		try
		{
			String[] terms = mVDH.getTerms();

			HashMap retMap = new HashMap(terms.length);

			for (int ctr = 0; ctr < terms.length; ctr++)
			{
				TermData data = new TermData(terms[ctr]);

				data.setType(mVDH.getType(terms[ctr]));

				data.setValues(mVDH.getValues(terms[ctr]));

				retMap.put(terms[ctr], data);
			}

			fireTermHashMapRequested(); // event sent to local server listeners only

			return retMap;
		}
		catch (Exception e)
		{
			throw new RemoteException(e.getMessage());
		}
	}
	
	
	// OBTAINING VALUES
	
	/**
	 * Obtain the values for the specified term.
	 * 
	 * @param term String the term to obtain values for.
	 * @throws RemoteException if communication fails.
	 * @throws NoSuchTermException if term non-existant.
	 * @return String[] values for specified term.
	 */
	public String[] getValues( String term ) throws RemoteException,
		NoSuchTermException
	{
		try
		{
			fireValuesRequested(term); // event sent only to local server listeners

			return mVDH.getValues(term);
		}
		catch (IOException e) // convert local IOException -> RemoteException
		{
			throw new RemoteException(e.getMessage());
		}
	}	


	// TERM ADDITION / REMOVAL

	/**
	 * Adds the specified term if it does not already exist.
	 * 
	 * @param term String the term to add.
	 * @param type int the type of the term.
	 * @throws RemoteException if communication fails.
	 * @throws InvalidTypeException if type is invalid.
	 */
	public synchronized void addTerm( String term, int type ) throws RemoteException,
		InvalidTypeException
	{
		try
		{
			if (!mVDH.termExists(term)) // if term already exists - do nothing
			{
				mVDH.addTerm(term, type); // data layer will fire event

				fireTermAdded(term, type); // event sent to clients and local server listeners
			}
		}
		catch (IOException e) // convert local IOException -> RemoteException
		{
			throw new RemoteException(e.getMessage());
		}
	}
	
	/**
	 * Removes the specified term, but only if the term exists.
	 * 
	 * OBS: this method will call the data layer to remove the
	 * term, which in turn will fire a regular data event upwards.
	 * Thus, the server should not try to remove the term again
	 * when receiving the local event via the implementation 
	 * listener.
	 * 
	 * @param term String term to remove.
	 * @throws RemoteException if communication fails.
	 * @throws NoSuchTermException if term doesn't exist.
	 */
	public synchronized void removeTerm( String term ) throws RemoteException,
		NoSuchTermException
	{
		try
		{
			if (mVDH.termExists(term))
			{
				mVDH.removeTerm(term); // data layer will fire event

				fireTermRemoved(term); // event sent to clients and local server listeners
			}
		}
		catch (IOException e) // convert local IOException -> RemoteException
		{
			throw new RemoteException(e.getMessage());
		}
	}
	
	
	// VALUE ADDITION / REMOVAL
	
	/**
	 * Adds the specified value to the specified term if it does not already exist
	 * for the term.
	 * 
	 * @param term String the term which you are adding the value to.
	 * @param value Object the value to add.
	 * @throws RemoteException if communication fails.
	 * @throws NoSuchTermException if the term does not exist.
	 * @throws InvalidTypeException if the type of the term is invalid.
	 */
	public synchronized void addValue( String term, Object value ) throws RemoteException,
		NoSuchTermException, InvalidTypeException
	{
		try
		{
			if (!mVDH.valueExists(term, value))
			{
				mVDH.addValue(term, value); // data layer will fire event

				fireValueAdded(term, value); // event sent to clients and local server listeners
			}
		}
		catch (IOException e) // convert local IOException -> RemoteException
		{
			throw new RemoteException(e.getMessage());
		}
	}
	
	/**
	 * Removes the specified value, only if the term and value are existant.
	 * 
	 * OBS: this method will call the data layer to remove the
	 * values, which in turn will fire a regular data event upwards.
	 * Thus, the server should not try to remove the values again
	 * when receiving the local event via the implementation 
	 * listener.
	 * 
	 * @param term String term containing value to remove.
	 * @param value Object value to remove.
	 * @throws RemoteException if communication fails.
	 * @throws NoSuchTermException if term non-existant.
	 * @throws InvalidTypeException if type of term invalid.
	 */
	public synchronized void removeValue( String term, Object value ) throws RemoteException,
		NoSuchTermException, InvalidTypeException
	{
		try
		{
			if (mVDH.valueExists(term, value))
			{
				mVDH.removeValue(term, value); // data layer will fire event

				fireValueRemoved(term, value); // event sent to clients and local server listeners
			}
		}
		catch (IOException e) // convert local IOException -> RemoteException
		{
			throw new RemoteException(e.getMessage());
		}
	}
	
	
	// TERM TYPE

	/**
	 * Obtain the type of the specified term.
	 * 
	 * @param term String the term to obtain type for.
	 * @throws RemoteException if communication fails.
	 * @throws NoSuchTermException if term non-existant.
	 * @throws InvalidTypeException if type of term invalid.
	 * @return int type of the specified term.
	 */
	public int getType( String term ) throws RemoteException,
		NoSuchTermException, InvalidTypeException
	{
		try
		{
			fireTypeRequested(term); // event sent only to local server listeners

			return mVDH.getType(term);
		}
		catch (IOException e) // convert local IOException -> RemoteException
		{
			throw new RemoteException(e.getMessage());
		}
	}


	// LOCAL AND REMOTE CLIENT LISTENERS (STUBS)

	/**
	 * Adds a remote term datahandler listener to the list of such listeners kept
	 * by this implementation class. This method is visible to clients, who may add
	 * their own implementations of the RemoteTermDataHandlerListener to the
	 * implementation class of the server. The implementation stub class (template)
	 * must be found on the server though.
	 * 
	 * @param l RemoteTermDataHandlerListener remote listener (stub) to add.
	 */
	public synchronized void addRemoteTermDataHandlerListener(RemoteTermDataHandlerListener l)
	{
		registeredClientListeners.add(l);
	}

	/**
	 * Removes a remote term datahandler listener from the list of such listeners
	 * kept by this implementation class. This method is visible to clients, who
	 * may add their own implementations of the RemoteTermDataHandlerListener to
	 * the implementation class of the server. The implementation stub class
	 * (template) must be found on the server though.
	 * 
	 * @param l RemoteTermDataHandlerListener remote listener (stub) to remove.
	 */
	public synchronized void removeRemoteTermDataHandlerListener(RemoteTermDataHandlerListener l)
	{
		registeredClientListeners.remove(l);
	}
	
	/**
	 * Notify clients that a server shutdown is imminent.
	 */
	private void fireServerShuttingDown()
	{
		RemoteTermDataHandlerEvent event = new RemoteTermDataHandlerEvent();
		
		// fire to registered remote listener stubs

		if (registeredClientListeners.size() != 0)
		{
			Vector removeVector = new Vector();

			Enumeration enm = registeredClientListeners.elements();

			while (enm.hasMoreElements()) // for each of the stubs
			{
				RemoteTermDataHandlerListener l = (RemoteTermDataHandlerListener) enm.nextElement();

				try
				{
					l.serverShuttingDown(event); // event is serialized and sent to client implementation
				}
				catch (RemoteException e) // remove from listenerlist if communication failure with client stub
				{
					removeVector.add(l); // OBS: we cant remove immediately (enmeration will screw up)
				}
			}

			enm = removeVector.elements(); // remove vector contains the ones we couldnt fire to

			while (enm.hasMoreElements())
			{
				RemoteTermDataHandlerListener l = (RemoteTermDataHandlerListener) enm.nextElement();

				registeredClientListeners.remove(l); // client is down, remove the listener stub
			}
		}		
	}
	
	/**
	 * Will fire an event indicating that the specified term has been added to the
	 * database. This event is fired to all registered remote client listeners, as
	 * well as all registered local server listeners.
	 * 
	 * @param term String the term that has been added.
	 * @param type int the type of the added term.
	 */
	protected void fireTermAdded(String term, int type)
	{
		try
		{
			// fire to registered client listeners (stubs)

			String cl = RemoteServer.getClientHost(); // obtain calling client info

			RemoteTermDataHandlerEvent event = new RemoteTermDataHandlerEvent();
			
			event.setTerm(term);
			
			event.setType(type);
			
			event.setClientHost(cl);

			if (registeredClientListeners.size() != 0)
			{
				Enumeration enm = registeredClientListeners.elements();

				while (enm.hasMoreElements())
				{
					RemoteTermDataHandlerListener l = (RemoteTermDataHandlerListener) enm.nextElement();

					try
					{
						l.termAdded(event);
					}
					catch (RemoteException e) // remove from listenerlist if communication failure with client stub
					{
						registeredClientListeners.remove(l);
					}
				}
			}

			// fire to local server listeners

			if (registeredLocalListeners.size() != 0)
			{
				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					RemoteTermDataHandlerImplListener l = (RemoteTermDataHandlerImplListener) enm.nextElement();

					l.termAdded(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Will fire an event indicating that the specified term has been removed from
	 * the database. This event is fired to all registered remote client listeners,
	 * as well as all registered local server listeners.
	 * 
	 * @param term String the term that has been removed.
	 */
	protected void fireTermRemoved(String term)
	{
		try
		{
			// fire to registered client listeners (stubs)

			String cl = RemoteServer.getClientHost(); // obtain calling client info

			RemoteTermDataHandlerEvent event = new RemoteTermDataHandlerEvent();

			event.setTerm(term);

			event.setClientHost(cl);

			if (registeredClientListeners.size() != 0)
			{
				Enumeration enm = registeredClientListeners.elements();

				while (enm.hasMoreElements())
				{
					RemoteTermDataHandlerListener l = (RemoteTermDataHandlerListener) enm.nextElement();

					try
					{
						l.termRemoved(event);
					}
					catch (RemoteException e) // remove from listenerlist if communication failure with client stub
					{
						registeredClientListeners.remove(l);
					}
				}
			}

			// fire to local server listeners

			if (registeredLocalListeners.size() != 0)
			{
				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					RemoteTermDataHandlerImplListener l = (RemoteTermDataHandlerImplListener) enm.nextElement();

					l.termRemoved(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Will fire an event indicating that the specified value has been added to the
	 * database. This event is fired to all registered remote client listeners, as
	 * well as all registered local server listeners.
	 * 
	 * @param term String the term containing the added value.
	 * @param value Object the value that was added.
	 */
	protected void fireValueAdded(String term, Object value)
	{
		try
		{
			// fire to registered client listeners (stubs)

			String cl = RemoteServer.getClientHost(); // obtain calling client info
			
			RemoteTermDataHandlerEvent event = new RemoteTermDataHandlerEvent();

			event.setTerm(term);

			event.setValue(value);

			event.setClientHost(cl);

			if (registeredClientListeners.size() != 0)
			{
				Enumeration enm = registeredClientListeners.elements();

				while (enm.hasMoreElements())
				{
					RemoteTermDataHandlerListener l = (RemoteTermDataHandlerListener) enm.nextElement();

					try
					{
						l.valueAdded(event);
					}
					catch (RemoteException e) // remove from listenerlist if communication failure with client stub
					{
						registeredClientListeners.remove(l);
					}
				}
			}

			// fire to local server listeners

			if (registeredLocalListeners.size() != 0)
			{
				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					RemoteTermDataHandlerImplListener l = (RemoteTermDataHandlerImplListener) enm.nextElement();

					l.valueAdded(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Will fire an event indicating that the specified value has been removed from
	 * the database. This event is fired to all registered remote client listeners,
	 * as well as all registered local server listeners.
	 * @param term String
	 * @param value Object
	 */
	protected void fireValueRemoved(String term, Object value)
	{
		try
		{
			// fire to registered client listeners (stubs)

			String cl = RemoteServer.getClientHost(); // obtain calling client info

			RemoteTermDataHandlerEvent event = new RemoteTermDataHandlerEvent();
			
			event.setTerm(term);
			
			event.setValue(value);
			
			event.setClientHost(cl);

			if (registeredClientListeners.size() != 0)
			{
				Enumeration enm = registeredClientListeners.elements();

				while (enm.hasMoreElements())
				{
					RemoteTermDataHandlerListener l = (RemoteTermDataHandlerListener) enm.nextElement();

					try
					{
						l.valueRemoved(event);
					}
					catch (RemoteException e) // remove from listenerlist if communication failure with client stub
					{
						registeredClientListeners.remove(l);
					}
				}
			}

			// fire to local server listeners

			if (registeredLocalListeners.size() != 0)
			{
				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					RemoteTermDataHandlerImplListener l = (RemoteTermDataHandlerImplListener) enm.nextElement();

					l.valueRemoved(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}


	// LOCAL LISTENERS ONLY

	/**
	 * This method is not visible to the clients, who only see the methods defined
	 * by the RemoteTermDataHandler interface. These methods can be used by the
	 * server classes to listen to events that only are of interest to the local
	 * server.
	 * 
	 * @param l RemoteTermDataHandlerImplListener local listener to add.
	 */
	public void addRemoteTermDataHandlerImplListener(RemoteTermDataHandlerImplListener l)
	{
		registeredLocalListeners.add(l);
	}

	/**
	 * This method is not visible to the clients, who only see the methods defined
	 * by the RemoteTermDataHandler interface. These methods can be used by the
	 * server classes to listen to events that only are of interest to the local
	 * server.
	 * 
	 * @param l RemoteTermDataHandlerImplListener local listener to remove.
	 */
	public void removeRemoteTermDataHandlerImplListener(RemoteTermDataHandlerImplListener l)
	{
		registeredLocalListeners.remove(l);
	}

	/**
	 * Will fire an event to the local server listeners
	 * that a hash map of TermData objects have been
	 * requested by a client. This event will not be fired
	 * to any client listeners, since it is of interest
	 * only to the server.
	 */
	protected void fireTermHashMapRequested()
	{		
		try
		{
			// fire to local server listeners
			
			String cl = RemoteServer.getClientHost(); // obtain calling client info
			
			RemoteTermDataHandlerEvent event = new RemoteTermDataHandlerEvent();

			event.setClientHost(cl);

			if (registeredLocalListeners.size() != 0)
			{
				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					RemoteTermDataHandlerImplListener l = (RemoteTermDataHandlerImplListener) enm.nextElement();

					l.termHashMapRequested(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Will fire an event to the local server listeners
	 * that the list of terms have been requested. This
	 * event will not be fired to any client listeners,
	 * since it is of interest only to the server.
	 */
	protected void fireTermListRequested()
	{
		try
		{
			// fire to local server listeners
			
			String cl = RemoteServer.getClientHost(); // obtain calling client info
			
			RemoteTermDataHandlerEvent event = new RemoteTermDataHandlerEvent();

			event.setClientHost(cl);

			if (registeredLocalListeners.size() != 0)
			{
				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					RemoteTermDataHandlerImplListener l = (RemoteTermDataHandlerImplListener) enm.nextElement();

					l.termListRequested(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Will fire an event to the local server listeners that the type of a term has
	 * been requested. This event will not be fired to any client listeners, since
	 * it is of interest only to the server.
	 * 
	 * @param term String the term which type was requested.
	 */
	protected void fireTypeRequested(String term)
	{
		try
		{
			// fire to local server listeners
			
			String cl = RemoteServer.getClientHost(); // obtain calling client info

			RemoteTermDataHandlerEvent event = new RemoteTermDataHandlerEvent();

			event.setTerm(term);

			event.setClientHost(cl);

			if (registeredLocalListeners.size() != 0)
			{
				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					RemoteTermDataHandlerImplListener l = (RemoteTermDataHandlerImplListener) enm.nextElement();

					l.typeRequested(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Will fire an event to the local server listeners that the list of values for
	 * a term have been requested. This event will not be fired to any client
	 * listeners, since it is of interest only to the server.
	 * 
	 * @param term String the term for which the values was requested.
	 */
	protected void fireValuesRequested(String term)
	{
		try
		{
			// fire to local server listeners
			
			String cl = RemoteServer.getClientHost(); // obtain calling client info

			RemoteTermDataHandlerEvent event = new RemoteTermDataHandlerEvent();

			event.setClientHost(cl);

			if (registeredLocalListeners.size() != 0)
			{
				Enumeration enm = registeredLocalListeners.elements();

				while (enm.hasMoreElements())
				{
					RemoteTermDataHandlerImplListener l = (RemoteTermDataHandlerImplListener) enm.nextElement();

					l.valuesRequested(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace();
		}
	}

}
