/**
 * @(#) RemoteExaminationDataHandlerListenerImpl.java
 */

package medview.datahandling.examination;

import java.rmi.*;
import java.rmi.server.*;

/**
 */
public class RemoteExaminationDataHandlerListenerImpl extends UnicastRemoteObject
	implements RemoteExaminationDataHandlerListener
{
	public void examinationAdded( RemoteExaminationDataHandlerEvent e ) throws RemoteException
	{
		client.examinationWasAddedToServer(e.getIdentifier());
	}
	
	public void examinationUpdated( RemoteExaminationDataHandlerEvent e ) throws RemoteException
	{
		client.examinationWasUpdatedOnServer(e.getIdentifier());
	}

	public void serverNameChanged(RemoteExaminationDataHandlerEvent e) throws RemoteException
	{
		client.locationIDWasChangedAtServer(e.getServerName());
	}

	public void locationChanged( RemoteExaminationDataHandlerEvent e) throws RemoteException
	{
		client.locationWasChangedAtServer();
	}
	
	public void serverShuttingDown( RemoteExaminationDataHandlerEvent e) throws RemoteException
	{
		client.serverShuttingDown();
	}

	public RemoteExaminationDataHandlerListenerImpl( RemoteExaminationDataHandlerClient client ) throws RemoteException
	{
		this.client = client;
	}

	private RemoteExaminationDataHandlerClient client;
}
