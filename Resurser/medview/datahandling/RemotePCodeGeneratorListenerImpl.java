package medview.datahandling;

import java.rmi.*;
import java.rmi.server.*;

/**
 * @author Fredrik Lindahl
 * 
 * @version 1.0
 */
public class RemotePCodeGeneratorListenerImpl extends UnicastRemoteObject
	implements RemotePCodeGeneratorListener
{
	// MEMBERS
	
	private RemotePCodeGeneratorClient client;
	
	// CONSTRUCTOR
	
	public RemotePCodeGeneratorListenerImpl(RemotePCodeGeneratorClient client) throws RemoteException
	{
		this.client = client;
	}

	
	// SERVER SHUT-DOWN

	/**
	 * serverShuttingDown
	 *
	 * @param e RemotePCodeGeneratorEvent
	 * @throws RemoteException
	 * @todo Implement this medview.datahandling.RemotePCodeGeneratorListener method
	 */
	public void serverShuttingDown(RemotePCodeGeneratorEvent e) throws RemoteException
	{
		client.serverShuttingDown();
	}
}
