package medview.datahandling;

import java.rmi.*;

import java.util.*;

public interface RemoteTermDataHandlerListener extends Remote
{
	/**
	 * Notifies the client that a value has been added.
	 * @param e RemoteTermDataHandlerEvent
	 * @throws RemoteException
	 */
	public void valueAdded(RemoteTermDataHandlerEvent e) throws RemoteException;

	/**
	 * Notifies the client that a value has been removed.
	 * @param e RemoteTermDataHandlerEvent
	 * @throws RemoteException
	 */
	public void valueRemoved(RemoteTermDataHandlerEvent e) throws RemoteException;

	/**
	 * Notifies a client that a term has been added.
	 * @param e RemoteTermDataHandlerEvent
	 * @throws RemoteException
	 */
	public void termAdded(RemoteTermDataHandlerEvent e) throws RemoteException;
	
	/**
	 * Notifies a client that a term has been removed.
	 * @param e RemoteTermDataHandlerEvent
	 * @throws RemoteException
	 */
	public void termRemoved(RemoteTermDataHandlerEvent e) throws RemoteException;

	/**
	 * Indicates that the server is about to shut down. The
	 * client should take appropriate steps to remove itself as
	 * a listener to the server and should also be placed in a
	 * 'disconnected' state.
	 * 
	 * @param e RemoteExaminationDataHandlerEvent
	 * @throws RemoteException
	 */	
	public void serverShuttingDown(RemoteTermDataHandlerEvent e) throws RemoteException;
}
