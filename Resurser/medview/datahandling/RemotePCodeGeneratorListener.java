package medview.datahandling;

import java.rmi.*;

/**
 * @author Fredrik Lindahl
 * 
 * @version 1.0
 */
public interface RemotePCodeGeneratorListener extends Remote
{
	public void serverShuttingDown(RemotePCodeGeneratorEvent e) throws RemoteException;
}
