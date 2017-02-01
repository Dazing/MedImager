/**
 * @(#) RemoteTermDataHandler.java
 */

package medview.datahandling;

import java.rmi.*;

import java.util.*;

/**
 * Throw a RemoteException back to the client if there
 * is some problem during communication between the client
 * and the server, or if the server's datahandling layer
 * throws an IOException during the server's local processing
 * of the client's request.
 *
 * Throw the other exceptions (like InvalidTypeException,
 * NoSuchTermException etc) normally (i.e. do not convert
 * the exception from the server's local processing thrown).
 * They will be re-thrown in the client's local process
 * space if they occur.
 *
 * @author Fredrik Lindahl
 */
public interface RemoteTermDataHandler extends Remote
{
	/**
	 * Registers a remote term datahandler listener to the
	 * remote term datahandler.
	 * @param l RemoteTermDataHandlerListener the listener
	 * to add.
	 * @throws RemoteException
	 */
	public void addRemoteTermDataHandlerListener(RemoteTermDataHandlerListener l) throws
	    RemoteException;

	/**
	 * Unregisters a remote term datahandler listener 
	 * from the remote term datahandler.
	 * @param l RemoteTermDataHandlerListener the listener
	 * to remove.
	 * @throws RemoteException
	 */
	public void removeRemoteTermDataHandlerListener(RemoteTermDataHandlerListener l) throws RemoteException;

	/**
	 * Adds a term with the specified type.
	 * @param term String the name of the term to add.
	 * @param type int the type of the added term.
	 * @throws RemoteException
	 * @throws InvalidTypeException if the type is not
	 * of a valid type.
	 */
	public void addTerm( String term, int type ) throws RemoteException, InvalidTypeException;

	/**
	 * Adds a value to the specified term.
	 * @param term String the term which you want to
	 * add a value to.
	 * @param value Object the value that you want to
	 * add to the term.
	 * @throws RemoteException
	 * @throws NoSuchTermException if the specified term
	 * does not exist.
	 * @throws InvalidTypeException if the type of the specified
	 * term is invalid.
	 */
	public void addValue( String term, Object value ) throws RemoteException, NoSuchTermException, InvalidTypeException;

	/**
	 * Obtain an array of all the current terms.
	 * @return String[] an array of all the current
	 * terms.
	 * @throws RemoteException
	 */
	public String[] getTerms( ) throws RemoteException;

	/**
	 * Obtain the type of the specified term.
	 * @param term String the term for which you want
	 * to know the type.
	 * @return int the type of the specified term.
	 * @throws RemoteException
	 * @throws NoSuchTermException if the specified term
	 * does not exist.
	 * @throws InvalidTypeException if the specified term 
	 * has an unrecognized type.
	 */
	public int getType( String term ) throws RemoteException, NoSuchTermException, InvalidTypeException;

	/**
	 * Obtain an array of the values for the specified
	 * term.
	 * @param term String the term for which you want to
	 * see the values.
	 * @return String[] the values contained for the
	 * specified term.
	 * @throws RemoteException
	 * @throws NoSuchTermException if the specified term 
	 * is non-existant.
	 */
	public String[] getValues( String term ) throws RemoteException, NoSuchTermException;

	/**
	 * Removes the specified term.
	 * @param term String the term to remove.
	 * @throws RemoteException
	 * @throws NoSuchTermException if the specified term
	 * does not exist.
	 */
	public void removeTerm( String term ) throws RemoteException, NoSuchTermException;

	/**
	 * Removes the specified value from the specified
	 * terms list of values.
	 * @param term String the term from which you want
	 * to remove a value.
	 * @param value Object the value which you want to remove
	 * from the terms list of values.
	 * @throws RemoteException
	 * @throws NoSuchTermException if the specified term does
	 * not exist.
	 * @throws InvalidTypeException if the specified term does
	 * not have a recognizable type.
	 */
	public void removeValue( String term, Object value ) throws RemoteException, NoSuchTermException, InvalidTypeException;

	/**
	 * Obtain a HashMap of TermData objects.
	 * @return HashMap a hash map containing mappings
	 * between term names -> TermData objects.
	 * @throws RemoteException
	 */
	public HashMap getTermDataHashMap() throws RemoteException;

}
