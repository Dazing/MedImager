/**
 * @(#) RemoteExaminationDataHandlerListener.java
 */

package medview.datahandling.examination;

import java.rmi.*;

/**
 * Intended as a remote interface being implemented by an
 * object having a stub which receives events from the
 * server.
 * 
 * @author Fredrik Lindahl
 * 
 * <p>Title: </p> 
 * 
 * <p>Description: </p> 
 * 
 * <p>Copyright: Copyright (c) 2004</p> 
 * 
 * <p>Company: </p>
 * 
 * @version 1.0
 */
public interface RemoteExaminationDataHandlerListener extends Remote
{
	/**
	 * Indicates that an examination has been added to the
	 * server.
	 * 
	 * @param e RemoteExaminationDataHandlerEvent an object
	 * carrying with it additional information about the
	 * event.
	 * @throws RemoteException if communication fails.
	 */
	public void examinationAdded( RemoteExaminationDataHandlerEvent e ) throws RemoteException;

	/**
	 * Indicates that an examination has been updated on the
	 * server.
	 * 
	 * @param e RemoteExaminationDataHandlerEvent
	 * @throws RemoteException
	 */
	public void examinationUpdated( RemoteExaminationDataHandlerEvent e ) throws RemoteException;

	/**
	 * Indicates that the server name has been changed
	 * on the server.
	 * 
	 * @param e RemoteExaminationDataHandlerEvent an object
	 * carrying with it additional information about the
	 * event.
	 * @throws RemoteException if communication fails.
	 */
	public void serverNameChanged( RemoteExaminationDataHandlerEvent e) throws RemoteException;

	/**
	 * Indicates that the data location has changed on the
	 * server, and that a re-read of the data might be
	 * necessary on the client.
	 * 
	 * @param e RemoteExaminationDataHandlerEvent an object
	 * carrying with it additional information about the
	 * event.
	 * @throws RemoteException if communication fails.
	 */
	public void locationChanged( RemoteExaminationDataHandlerEvent e) throws RemoteException;
	
	/**
	 * Indicates that the server is about to shut down. The
	 * client should take appropriate steps to remove itself as
	 * a listener to the server and should also be placed in a
	 * 'disconnected' state.
	 * 
	 * @param e RemoteExaminationDataHandlerEvent an object
	 * carrying with it additional information about the event.
	 * @throws RemoteException if communication fails.
	 */
	public void serverShuttingDown( RemoteExaminationDataHandlerEvent e) throws RemoteException;
}
