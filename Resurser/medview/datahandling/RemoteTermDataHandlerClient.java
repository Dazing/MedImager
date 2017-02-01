/**
 * @(#) RemoteTermDataHandlerClient.java
 */

package medview.datahandling;

import java.io.*;

import java.rmi.*;

import java.util.*;

import javax.swing.event.*; // listener list

import medview.medserver.data.*;

/**
 * A remote object (rmi) client implementation of the TermDataHandler
 * interface. Obtains a reference to a remote term datahandling object
 * and acts as a proxy for it.
 *
 * Each time one of the methods requiring a connection to a remote
 * handler is called, the object tries to connect and obtain a remote
 * reference to the remote handler. If this fails, an exception will
 * always be thrown, stating the cause of the failure.
 * @author Fredrik Lindahl
 */
public class RemoteTermDataHandlerClient extends AbstractTermDataHandler
{
	
	// LOCAL SHUT-DOWN NOTIFICATION
	
	public void shuttingDown()
	{		
		disconnect();
	}
	
	
	// METHODS CALLED FROM THE REMOTE LISTENER IMPLEMENTATION

	/**
	 * The connected server is shutting down.
	 */
	public void serverShuttingDown()
	{		
		disconnect();
	}

	/**
	 * This method is called when a value has
	 * been added to a term on the remote server.
	 */
	public void valueWasAddedToServer(String term, Object value)
	{
		try
		{
			if (cacheNeedsUpdate) // only sync thread can set this flag to true
			{
				updateCacheMap(); // -> IOException (after disconnecting)
			}
			else
			{
				TermData data = (TermData) cacheMap.get(term);

				data.addValue(value);
			}

			fireValueAdded(term, value);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			
			disconnect();
		}
	}

	/**
	 * This method is called when a value has
	 * been removed from a term on the remote
	 * server.
	 */
	public void valueWasRemovedFromServer(String term, Object value)
	{
		try
		{
			if (cacheNeedsUpdate) // only sync thread can set this flag to true
			{
				updateCacheMap(); // -> IOException (after disconnecting)
			}
			else
			{
				TermData data = (TermData) cacheMap.get(term);

				data.removeValue(value);
			}

			fireValueRemoved(term, value);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			
			disconnect();
		}
	}

	/**
	 * This method is called when a term has
	 * been added to the remote server.
	 */
	public void termWasAddedToServer(String term, int type)
	{
		try
		{
			if (cacheNeedsUpdate) // only sync thread can set this flag to true
			{
				updateCacheMap(); // -> IOException (after disconnecting)
			}
			else
			{
				TermData newData = new TermData(term);

				newData.setType(type);

				cacheMap.put(term, newData);
			}

			fireTermAdded(term, type);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			
			disconnect();
		}
	}

	/**
	 * This method is called when a term has
	 * been removed from the remote server.
	 */
	public void termWasRemovedFromServer(String term)
	{
		try
		{
			if (cacheNeedsUpdate) // only sync thread can set this flag to true
			{
				updateCacheMap(); // -> IOException (after disconnecting)
			}
			else
			{
				cacheMap.remove(term);
			}

			fireTermRemoved(term);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			
			disconnect();
		}
	}

	
	// CONNECTION
	
	private void connect() throws IOException
	{
		if (isConnected()) // must remove old listener first if connected
		{
			try
			{
				remoteHandler.removeRemoteTermDataHandlerListener(remoteTDHListener);
			}
			catch (RemoteException e) {} // dont care if this happens
		}

		try
		{
			String remName = MedServerDataConstants.REMOTE_TERM_DATAHANDLER_BOUND_NAME;

			String rmiURL = "rmi://" + serverURL + "/" + remName;

			remoteHandler = (RemoteTermDataHandler) Naming.lookup(rmiURL);

			remoteHandler.addRemoteTermDataHandlerListener(remoteTDHListener);
		}
		catch (Exception e) // MalFormedURLException, NotBoundException, RemoteException
		{
			e.printStackTrace();
			
			remoteHandler = null;

			throw new IOException("Could not connect to server!");
		}
	}

	private void disconnect()
	{
		if (isConnected())
		{
			try
			{
				remoteHandler.removeRemoteTermDataHandlerListener(remoteTDHListener);
			}
			catch (RemoteException e) {} // dont care if this happens
		}

		remoteHandler = null;
	}
	
	private boolean isConnected()
	{
		return remoteHandler != null;
	}


	// EVENTS AND LISTENERS
	
	/**
	 * Adds a term datahandler listener.
	 */
	public void addTermDataHandlerListener(TermDataHandlerListener l)
	{
		listenerList.add(TermDataHandlerListener.class, l);
	}

	/**
	 * Removes a term datahandler listener.
	 */
	public void removeTermDataHandlerListener(TermDataHandlerListener l)
	{
		listenerList.remove(TermDataHandlerListener.class, l);
	}

	protected void fireTermAdded(String term, int type)
	{
		Object[] listeners = listenerList.getListenerList();

		TermDataHandlerEvent event = new TermDataHandlerEvent(this);

		event.setTerm(term); event.setType(type);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TermDataHandlerListener.class)
			{
				((TermDataHandlerListener)listeners[i+1]).termAdded(event);
			}
	     }
	}

	protected void fireTermRemoved(String term)
	{
		Object[] listeners = listenerList.getListenerList();

		TermDataHandlerEvent event = new TermDataHandlerEvent(this);

		event.setTerm(term);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TermDataHandlerListener.class)
			{
				((TermDataHandlerListener)listeners[i+1]).termRemoved(event);
			}
	     }
	}

	protected void fireValueAdded(String term, Object value)
	{
		Object[] listeners = listenerList.getListenerList();

		TermDataHandlerEvent event = new TermDataHandlerEvent(this);

		event.setTerm(term); event.setValue(value);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TermDataHandlerListener.class)
			{
				((TermDataHandlerListener)listeners[i+1]).valueAdded(event);
			}
	     }
	}

	protected void fireValueRemoved(String term, Object value)
	{
		Object[] listeners = listenerList.getListenerList();

		TermDataHandlerEvent event = new TermDataHandlerEvent(this);

		event.setTerm(term); event.setValue(value);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TermDataHandlerListener.class)
			{
				((TermDataHandlerListener)listeners[i+1]).valueRemoved(event);
			}
	     }
	}

	protected void fireDefinitionLocationChanged(String loc)
	{
		Object[] listeners = listenerList.getListenerList();

		TermDataHandlerEvent event = new TermDataHandlerEvent(this);

		event.setLocation(loc);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TermDataHandlerListener.class)
			{
				((TermDataHandlerListener)listeners[i+1]).termDefinitionLocationChanged(event);
			}
	     }
	}

	protected void fireValueLocationChanged(String loc)
	{
		Object[] listeners = listenerList.getListenerList();

		TermDataHandlerEvent event = new TermDataHandlerEvent(this);

		event.setLocation(loc);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == TermDataHandlerListener.class)
			{
				((TermDataHandlerListener)listeners[i+1]).termValueLocationChanged(event);
			}
	     }
	}

	
	// TERMDATAHANDLER INTERFACE REGULAR METHODS

	/**
	 * Tries to add the specified term to the remote object.
	 */
	public void addTerm( String term, int type ) throws IOException, InvalidTypeException
	{
		if (!isConnected())
		{
			connect(); // -> IOException (after disconnecting)
		}

		if (cacheNeedsUpdate) // only sync thread can set this flag to true
		{
			updateCacheMap(); // -> IOException (after disconnecting)
		}

		try
		{
			remoteHandler.addTerm(term, type); // -> RemoteException, InvalidTypeException
		}
		catch (RemoteException e) // convert RemoteException -> IOException
		{
			e.printStackTrace();
			
			disconnect();
			
			throw new IOException("Could not communicate with server!");
		}
	}

	/**
	 * Tries to add the specified value to the remote object.
	 */
	public void addValue( String term, Object value ) throws IOException, NoSuchTermException, InvalidTypeException
	{
		if (!isConnected())
		{
			connect(); // -> IOException (after disconnecting)
		}

		if (cacheNeedsUpdate) // only sync thread can set this flag to true
		{
			updateCacheMap(); // -> IOException (after disconnecting)
		}

		try
		{
			remoteHandler.addValue(term, value); // -> RemoteException, NoSuchTermException, InvalidTypeException

			((TermData)cacheMap.get(term)).addValue(value);
		}
		catch (RemoteException e) // convert RemoteException -> IOException
		{
			e.printStackTrace();
			
			disconnect();
			
			throw new IOException("Could not communicate with server!");
		}
	}

	/**
	 * Tries to obtain a list of terms from the remote object.
	 */
	public String[] getTerms( ) throws IOException
	{
		if (!isConnected())
		{
			connect(); // -> IOException (after disconnecting)
		}

		if (cacheNeedsUpdate) // only sync thread can set this flag to true
		{
			updateCacheMap(); // -> IOException (after disconnecting)
		}

		String[] retArr = new String[cacheMap.size()];

		cacheMap.keySet().toArray(retArr);

		return retArr;
	}

	/**
	 * Tries to obtain the type for the specified term from the remote handler.
	 */
	public int getType( String term ) throws IOException, NoSuchTermException, InvalidTypeException
	{
		if (!isConnected())
		{
			connect(); // -> IOException (after disconnecting)
		}

		if (cacheNeedsUpdate) // only sync thread can set this flag to true
		{
			updateCacheMap(); // -> IOException (after disconnecting)
		}

		if (!cacheMap.containsKey(term))
		{
			throw new NoSuchTermException(term + " does not exist");
		}
		else
		{
			TermData info = (TermData)cacheMap.get(term);

			return info.getType();
		}
	}

	/**
	 * Tries to obtain the values for the specified term from the server.
	 */
	public String[] getValues( String term ) throws IOException, NoSuchTermException
	{	
		if (!isConnected())
		{
			connect(); // -> IOException (after disconnecting)
		}

		if (cacheNeedsUpdate) // only sync thread can set this flag to true
		{
			updateCacheMap(); // -> IOException (after disconnecting)
		}

		if (!cacheMap.containsKey(term))
		{
			throw new NoSuchTermException(term + " does not exist");
		}
		else
		{
			TermData info = (TermData)cacheMap.get(term);

			return info.getValues();
		}
	}

	/**
	 * Tries to remove the specified term from the server.
	 */
	public void removeTerm( String term ) throws IOException, NoSuchTermException
	{
		try
		{
			if (!isConnected())
			{
				connect(); // -> IOException (after disconnecting)
			}

			if (cacheNeedsUpdate) // only sync thread can set this flag to true
			{
				updateCacheMap(); // -> IOException (after disconnecting)
			}

			try
			{
				remoteHandler.removeTerm(term); // --> RemoteException, NoSuchTermException

				cacheMap.remove(term);
			}
			catch (RemoteException e) // convert RemoteException -> IOException
			{
				throw new IOException(e.getMessage());
			}
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
			
			disconnect();
			
			throw new IOException("Could not communicate with server!");
		}
	}

	/**
	 * Tries to remove a value from the remote handler.
	 */
	public void removeValue( String term, Object value ) throws IOException, NoSuchTermException,
		InvalidTypeException
	{
		try
		{
			if (!isConnected())
			{
				connect(); // -> IOException (after disconnecting)
			}

			if (cacheNeedsUpdate) // only sync thread can set this flag to true
			{
				updateCacheMap(); // -> IOException (after disconnecting)
			}

			remoteHandler.removeValue(term, value); // --> RemoteException, NoSuchTermException, InvalidTypeException

			((TermData)cacheMap.get(term)).removeValue(value);
		}
		catch (RemoteException e) // convert RemoteException -> IOException
		{
			e.printStackTrace();
			
			disconnect();
			
			throw new IOException("Could not communicate with server!");
		}
	}

	/**
	 * Returns whether or not the specified term exists.
	 */
	public boolean termExists( String term ) throws IOException
	{
		try
		{
			if (!isConnected())
			{
				connect(); // -> IOException (after disconnecting)
			}

			if (cacheNeedsUpdate) // only sync thread can set this flag to true
			{
				updateCacheMap(); // -> IOException (after disconnecting)
			}

			getType(term); // -> IOException (after disconnecting), NoSuchTermException, InvalidTypeException

			return true; // if no exception is thrown, term exists
		}
		catch (RemoteException e) // convert RemoteException -> IOException
		{
			e.printStackTrace();
			
			disconnect();
			
			throw new IOException("Could not communicate with server!");
		}
		catch (NoSuchTermException e)
		{
			return false;
		}
		catch (InvalidTypeException e)
		{
			return true; // term is there but has an invalid type
		}
	}

	public boolean valueExists(String term, Object value) throws IOException
	{
		try
		{
			if (!isConnected())
			{
				connect(); // -> IOException (after disconnecting)
			}

			if (cacheNeedsUpdate) // only sync thread can set this flag to true
			{
				updateCacheMap(); // -> IOException (after disconnecting)
			}

			String[] vals = getValues(term); // -> IOException (after disconnecting), NoSuchTermException

			for (int ctr = 0; ctr < vals.length; ctr++)
			{
				if (vals[ctr].equalsIgnoreCase(value + ""))
				{
					return true; // match
				}
			}

			return false; // no match
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
			
			disconnect();
			
			throw new IOException("Could not communicate with server!");
		}
		catch (NoSuchTermException e)
		{
			return false;
		}
	}


	// URL SETTING METHODS

	/**
	 * Sets the URL of the server containing the remote
	 * term datahandler object. No check is made, it simply
	 * sets the URL. The connection will take place at the
	 * first call to a method that needs information kept
	 * by remote object.
	 */
	public void setTermDefinitionLocation( String loc )
	{
		serverURL = loc;

		if (isConnected())
		{
			disconnect();
		}

		fireDefinitionLocationChanged(loc);

		fireValueLocationChanged(loc); // same for remote
	}
	
	/**
	 * Returns whether or not the term definition
	 * location has been set.
	 * @return boolean
	 */
	public boolean isTermDefinitionLocationSet()
	{
		return (serverURL != null);
	}

	/**
	 * Returns the currently set server URL. This might
	 * be null if it has not yet been set.
	 */
	public String getTermDefinitionLocation( )
	{
		return serverURL;
	}

	/**
	 * Returns whether or not the term definition
	 * location is valid. It is valid if term
	 * definitions can be read from it. In the
	 * remote term datahandler, the definition
	 * location is valid if it is possible to
	 * connect to the currently set server URL.
	 */
	public boolean isTermDefinitionLocationValid( )
	{
		if (serverURL == null)
		{
			return false;
		}

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

	/**
	 * See setTermDefinitionLocation().
	 */
	public void setTermValueLocation( String loc )
	{
		setTermDefinitionLocation(loc);
	}

	/**
	 * See getTermDefinitionLocation().
	 */
	public String getTermValueLocation( )
	{
		return getTermDefinitionLocation();
	}

	/**
	 * See isTermDefinitionLocationValid().
	 */
	public boolean isTermValueLocationValid( )
	{
		return isTermDefinitionLocationValid();
	}
	
	/**
	 * See isTermDefinitionLocationSet().
	 */
	public boolean isTermValueLocationSet()
	{
		return isTermDefinitionLocationSet();
	}


	// UTILITY METHODS
	
	private void updateCacheMap() throws IOException
	{
		if (!isConnected())
		{
			throw new IOException("Not connected!");
		}

		try
		{
			cacheMap = remoteHandler.getTermDataHashMap(); // -> RemoteException
		
			cacheNeedsUpdate = false;
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
			
			disconnect();
			
			throw new IOException("Could not communicate with server!");
		}
	}

	
	// CONSTRUCTOR

	/**
	 * Constructs a remote term datahandler client,
	 * implementing the TermDataHandler interface.
	 */
	public RemoteTermDataHandlerClient()
	{
		try
		{
			remoteTDHListener = new RemoteTermDataHandlerListenerImpl(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();

			System.exit(1); // fatal error
		}

		new Thread(new CacheUpdateRunnable()).start();
	}
	
	// MEMBERS

	private EventListenerList listenerList = new EventListenerList();

	private RemoteTermDataHandlerListener remoteTDHListener;

	private RemoteTermDataHandler remoteHandler = null;

	private HashMap cacheMap = new HashMap();

	private boolean cacheNeedsUpdate = true;

	private String serverURL = null;


	// INTERNAL CLASS - CACHE UPDATE RUNNABLE

	/**
	 * The thread running this runnable has one sole purpose,
	 * namely to make sure that information in the server
	 * is in sync with the information kept in the cache
	 * map. If the system works as it is supposed to (i.e.
	 * term additions / removals, term type changes etc. are
	 * all done via the new MedView Java system, this thread
	 * really should not be necessary. But as a fail-safe, for
	 * those who might used other means of altering the term
	 * information, the thread makes sure that the locally
	 * kept cache is synchronized with the server's every 5
	 * minutes (300 seconds).
	 */
	private class CacheUpdateRunnable implements Runnable
	{
		public void run()
		{
			try
			{
				Thread.sleep(updateTimeout);

				cacheNeedsUpdate = true;
			}
			catch (InterruptedException e)
			{
				e.printStackTrace(); // wont happen (?)
			}
		}

		private int updateTimeout = 180000; // 3 minute update
	}

}
