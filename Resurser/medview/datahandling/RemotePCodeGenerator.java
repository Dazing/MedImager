/**
 * @(#) RemotePCodeGenerator.java
 */

package medview.datahandling;

import java.rmi.*;

/**
 * The Remote interface specifying a remote pcode
 * generator. The implementation provides a stub to
 * which clients can communicate.
 * 
 * @author Fredrik Lindahl
 * 
 * @version 1.0
 */
public interface RemotePCodeGenerator extends Remote
{
	public void addRemotePCodeGeneratorListener(RemotePCodeGeneratorListener l) throws RemoteException;
	
	public void removeRemotePCodeGeneratorListener(RemotePCodeGeneratorListener l) throws RemoteException;
	
	/**
	 * Generates a pcode (server-side) based on the specified
	 * pid, whether or not a number should be consumed at
	 * generation, and with the specified prefix.
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
		InvalidRawPIDException, CouldNotGeneratePCodeException, InvalidUserIDException, RemoteException;

	/**
	 * Returns whether or not the server recognizes the specified
	 * pid.
	 * 
	 * @param pid String the pid to check.
	 * @return boolean if the server recognizes the pid.
	 * @throws RemoteException if the remote communicatino system
	 * breaks down for some reason.
	 */
	public boolean recognizes( String pid ) throws RemoteException;

}
