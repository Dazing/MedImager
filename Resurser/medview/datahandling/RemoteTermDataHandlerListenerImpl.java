package medview.datahandling;

import java.rmi.*;
import java.rmi.server.*;

import medview.medserver.data.*;

/**
 * A concrete implementation of the RemoteTermDataHandler listener interface
 * that the clients register with the server. This implementation simply
 * passes forward the happenings to the term datahandler client, which in turn
 * deals with them appropriately.
 * @author Fredrik Lindahl
 */
public class RemoteTermDataHandlerListenerImpl extends UnicastRemoteObject
	implements RemoteTermDataHandlerListener
{
	public void valueAdded(RemoteTermDataHandlerEvent e) throws RemoteException
	{
		client.valueWasAddedToServer(e.getTerm(), e.getValue());
	}

	public void valueRemoved(RemoteTermDataHandlerEvent e) throws RemoteException
	{
		client.valueWasRemovedFromServer(e.getTerm(), e.getValue());
	}

	public void termAdded(RemoteTermDataHandlerEvent e) throws RemoteException
	{
		client.termWasAddedToServer(e.getTerm(), e.getType());
	}

	public void termRemoved(RemoteTermDataHandlerEvent e) throws RemoteException
	{
		client.termWasRemovedFromServer(e.getTerm());
	}
	
	public void serverShuttingDown(RemoteTermDataHandlerEvent e) throws RemoteException
	{
		client.serverShuttingDown();
	}

	public RemoteTermDataHandlerListenerImpl(RemoteTermDataHandlerClient client) throws RemoteException
	{
		this.client = client;
	}

	private RemoteTermDataHandlerClient client;
}
