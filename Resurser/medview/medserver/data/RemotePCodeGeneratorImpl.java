/**
 * @(#) RemotePCodeGeneratorImpl.java
 */

package medview.medserver.data;

import java.rmi.*;
import java.rmi.server.*;

import javax.swing.event.*;

import java.util.*;

import medview.datahandling.*;

public class RemotePCodeGeneratorImpl extends UnicastRemoteObject implements RemotePCodeGenerator
{
	// MEMBERS
	
	private MedViewDataHandler mVDH = MedViewDataHandler.instance();
	
	private EventListenerList listenerList = new EventListenerList();
	
	private Vector remoteListeners = new Vector();
		
	// CONSTRUCTOR
	
	/**
	 * Constructs an implementation of the PCodeGenerator 
	 * remote interface.
	 * 
	 * @throws RemoteException if the construction of the
	 * remote object cannot be made.
	 */
	public RemotePCodeGeneratorImpl() throws RemoteException {}
	
	
	// SERVER SHUTTONG DOWN

	public void serverShuttingDown()
	{		
		fireServerShuttingDown();
	}
	
	
	// LISTENERS
	
	/**
	 * Adds a remote listener stub.
	 * 
	 * @param l RemotePCodeGeneratorListener
	 * @throws RemoteException
	 */
	public void addRemotePCodeGeneratorListener(RemotePCodeGeneratorListener l) throws RemoteException
	{
		remoteListeners.add(l);
	}

	/**
	 * Removes a remote listener stub.
	 * 
	 * @param l RemotePCodeGeneratorListener
	 * @throws RemoteException
	 */
	public void removeRemotePCodeGeneratorListener(RemotePCodeGeneratorListener l) throws RemoteException
	{
		remoteListeners.remove(l);
	}
	
	/**
	 * Adds a (local) implementation listener.
	 * 
	 * @param l RemotePCodeGeneratorImplListener the 
	 * listener to add.
	 */
	public void addRemotePCodeGeneratorImplListener(RemotePCodeGeneratorImplListener l)
	{
		listenerList.add(RemotePCodeGeneratorImplListener.class, l); // local listener
	}
	
	/**
	 * Removes a (local) implementation listener.
	 * 
	 * @param l RemotePCodeGeneratorImplListener the
	 * listener to remove.
	 */
	public void removeRemotePCodeGeneratorImplListener(RemotePCodeGeneratorImplListener l) 
	{
		listenerList.remove(RemotePCodeGeneratorImplListener.class, l); // local listener
	}
	
	/**
	 * Fires an event to all registered remote listeners that
	 * the server is shutting down.
	 */
	private void fireServerShuttingDown() // only notifies remote stubs
	{
		// fire to registered remote listener stubs
		
		if (remoteListeners.size() != 0)
		{
			RemotePCodeGeneratorEvent event = new RemotePCodeGeneratorEvent();
			
			Vector removeVector = new Vector();

			Enumeration enm = remoteListeners.elements();

			while (enm.hasMoreElements()) // for each of the stubs
			{
				RemotePCodeGeneratorListener l = (RemotePCodeGeneratorListener) enm.nextElement();

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
				RemotePCodeGeneratorListener l = (RemotePCodeGeneratorListener) enm.nextElement();

				remoteListeners.remove(l); // client is down, remove the listener stub
			  }			
		}
	}
	
	/**
	 * Fires the (local) event indicating that a pcode has
	 * been requested from a client.
	 * 
	 * @param pid String the pid for which a pcode was
	 * requested.
	 * @param pCode String the pcode which is returned to
	 * the client.
	 */
	private void firePCodeRequested(String pid, String pCode) // only notifies local listeners
	{
		try
		{
			RemotePCodeGeneratorEvent event = new RemotePCodeGeneratorEvent();
			
			event.setClientHost(RemoteServer.getClientHost()); // throws ServerNotActiveException
			
			event.setPID(pid); 
			
			event.setPCode(pCode);
			
			Object[] listeners = listenerList.getListenerList();
			
			for (int i = listeners.length-2; i>=0; i-=2) 
			{
				if (listeners[i] == RemotePCodeGeneratorImplListener.class) 
				{
					((RemotePCodeGeneratorImplListener)listeners[i+1]).pCodeRequested(event);
				}
			}
		}
		catch (ServerNotActiveException e)
		{
			e.printStackTrace(); // useful for debugging
		}
	}
	
	
	// PCODE GENERATION
	
	/**
	 * Generates a pcode (server-side) based on the specified
	 * pid, whether or not a number should be consumed at
	 * generation, and with the specified prefix.
	 * 
	 * @param pid String the pid (ex. 19450402-2344).
	 * @param consumeNr boolean if should 'swallow' a number.
	 * @param prefix String care provider prefix.
	 * @return String the server-generated pcode.
	 * @throws InvalidRawPIDException if the specified pid is
	 * not valid (not recognized by the server).
	 * @throws CouldNotGeneratePCodeException if the server
	 * cannot generate the pcode for some reason.
	 * @throws InvalidUserIDException if the specified prefix
	 * is an invalid user ID.
	 * @throws RemoteException if the remote communication system
	 * breaks down for some reason.
	 */
	public String obtainPCode( String pid, boolean consumeNr, String prefix ) throws 
		InvalidRawPIDException, CouldNotGeneratePCodeException, InvalidUserIDException, RemoteException
	{
		mVDH.setUserID(prefix);
	
		String pCode = mVDH.obtainPCode(pid, consumeNr);
		
		firePCodeRequested(pid, pCode);
		
		return pCode;
	}
	
	
	// PID RECOGNITION
	
	/**
	 * Returns whether or not the server recognizes the specified
	 * pid.
	 * 
	 * @param pid String the pid to check.
	 * @return boolean if the server recognizes the pid.
	 * @throws RemoteException if the remote communicatino system
	 * breaks down for some reason.
	 */
	public boolean recognizes( String pid ) throws RemoteException
	{
		return mVDH.validates(pid);
	}

}
