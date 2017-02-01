/**
 * @(#) RemotePCodeGeneratorClient.java
 */

package medview.datahandling;

import java.io.*;

import java.rmi.*;

import javax.swing.event.*;

import medview.medserver.data.*;

import misc.foundation.*;

public class RemotePCodeGeneratorClient implements PCodeGenerator
{
	// MEMBERS

	private String prefix = null;

	private String serverURL = null;

	private RemotePCodeGenerator remoteHandler = null;

	private EventListenerList listenerList = new EventListenerList();

	private RemotePCodeGeneratorListener remotePCGListener = null;

	// CONSTRUCTORS

	public RemotePCodeGeneratorClient()
	{
		try
		{
			remotePCGListener = new RemotePCodeGeneratorListenerImpl(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();

			System.exit(1); // fatal error
		}
	}


	// LOCAL SHUT-DOWN NOTIFICATION

	/**
	 * Allows the datahandler to deal with the system
	 * shutting down. For instance, if the datahandler is
	 * a client to a server, lets it tell the server that it
	 * is shutting down so the server can remove it from its
	 * notification list.
	 */
	public void shuttingDown()
	{
		disconnect();
	}


	// SERVER SHUT-DOWN NOTIFICATION

	public void serverShuttingDown()
	{
		disconnect();
	}


	// CONNECT METHODS

	private boolean isConnected()
	{
		return (remoteHandler != null);
	}

	private void connect() throws IOException
	{
		if (isConnected()) // must remove old listener first if connected
		{
			try
			{
				remoteHandler.removeRemotePCodeGeneratorListener(remotePCGListener);
			}
			catch (RemoteException e) {} // dont care if this happens
		}

		try
		{
			String remName = MedServerDataConstants.REMOTE_PCODE_GENERATOR_BOUND_NAME;

			String rmiURL = "rmi://" + serverURL + "/" + remName;

			remoteHandler = (RemotePCodeGenerator) Naming.lookup(rmiURL);

			remoteHandler.addRemotePCodeGeneratorListener(remotePCGListener);
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
				remoteHandler.removeRemotePCodeGeneratorListener(remotePCGListener);
			}
			catch (RemoteException e) {} // dont care if this happens
		}

		remoteHandler = null;
	}


	// LISTENERS

	/**
	 * Adds a PCodeGeneratorListener to the generator.
	 * @param l PCodeGeneratorListener the listener to add.
	 */
	public void addPCodeGeneratorListener( PCodeGeneratorListener l )
	{
		listenerList.add(PCodeGeneratorListener.class, l);
	}

	/**
	 * Removes a PCodeGeneratorListener from the generator.
	 * @param l PCodeGeneratorListener the listener to remove.
	 */
	public void removePCodeGeneratorListener( PCodeGeneratorListener l )
	{
		listenerList.remove(PCodeGeneratorListener.class, l);
	}

	private void firePrefixChanged()
	{
		// NOT IMPLEMENTED YET
	}


	// PCODE GENERATION

	/**
	 * Generates a pcode. If the pid already has a pcode mapped
	 * to it, no generation takes place but the mapped pcode
	 * will be returned.
	 * @param pid String the pid to obtain a pcode for.
	 * @return String the obtained pcode, either generated or retrieved
	 * from a previous generation.
	 */
	public String obtainPCode( String pid ) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException
	{
		return this.obtainPCode(pid, true, null);
	}

	/**
	 * Generates a pcode, you can specify whether or not a number
	 * from the corresponding number generator location is to be
	 * consumed or remain the same. If the pid already has a pcode mapped
	 * to it, no generation takes place but the mapped pcode
	 * will be returned.
	 * @param pid String the pid to obtain a pcode for.
	 * @param consumeNr boolean whether or not to consume a number
	 * from the number generator location.
	 * @return String the obtained pcode, either generated or retrieved
	 * from a previous generation.
	 */
	public String obtainPCode( String pid, boolean consumeNr ) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException
	{
		return this.obtainPCode(pid, consumeNr, null);
	}

	/**
	 * Generates a pcode. Notifies a ProgressNotifiable
	 * of the progress. If the pid already has a pcode mapped
	 * to it, no generation takes place but the mapped pcode
	 * will be returned.
	 * @param pid String the pid to obtain a pcode for.
	 * @param not ProgressNotifiable receives notification of the
	 * progress made.
	 * @return String the obtained pcode, either generated or retrieved
	 * from a previous generation.
	 */
	public String obtainPCode( String pid, ProgressNotifiable not ) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException
	{
		return this.obtainPCode(pid, true, null);
	}

	/**
	 * Generates a pcode, you can specify whether or not a number
	 * from the corresponding number generator location is to be
	 * consumed or remain the same. Notifies a ProgressNotifiable
	 * of the progress. If the pid already has a pcode mapped
	 * to it, no generation takes place but the mapped pcode
	 * will be returned.
	 *
	 * @param pid String the pid to obtain a pcode for.
	 * @param consumeNr boolean whether or not to consume a number
	 * from the number generator location.
	 * @param not ProgressNotifiable receives notification of the
	 * progress made.
	 * @return String the obtained pcode, either generated or retrieved
	 * from a previous generation.
	 */
	public String obtainPCode( String pid, boolean consumeNr, ProgressNotifiable not ) throws
		InvalidRawPIDException, CouldNotGeneratePCodeException
	{
		try
		{
			if (!isConnected())
			{
				connect(); // -> IOException (after disconnecting)
			}

			return remoteHandler.obtainPCode(pid, consumeNr, getGeneratedPCodePrefix());

			// -> InvalidRawPIDException, CouldNotGeneratePCodeException, InvalidUserIDException, RemoteException
		}
		catch (RemoteException e)
		{
			e.printStackTrace();

			disconnect();

			throw new CouldNotGeneratePCodeException("Could not communicate with server!");
		}
		catch (IOException e)
		{
			e.printStackTrace();

			disconnect();

			throw new CouldNotGeneratePCodeException("Could not communicate with server!");
		}
		catch (InvalidUserIDException e)
		{
			e.printStackTrace();

			throw new CouldNotGeneratePCodeException("Set prefix is invalid (Remote Client)!");
		}
	}


	// EXAMINATION LOCATION

	/**
	 * Sets the location from where to fetch information
	 * about examinations.
	 *
	 * @return String the examination data location.
	 */
	public void setExaminationDataLocation( String loc )
	{
		serverURL = loc;

		if (isConnected())
		{
			disconnect();
		}
	}

	/**
	 * Returns the currently set examination data location.
	 *
	 * @return String the currently set examination data
	 * location.
	 */
	public String getExaminationDataLocation( )
	{
		return serverURL;
	}

	/**
	 * Returns whether or not the examination data location
	 * has been set.
	 *
	 * @return boolean whether or not the examination data location
	 * has been set.
	 */
	public boolean isExaminationDataLocationSet( )
	{
		return (serverURL != null);
	}


	// NUMBER GENERATOR

	/**
	 * Sets the PCodeNumberGenerator to use
	 */
	public void setPCodeNumberGenerator(PCodeNumberGenerator numberGenerator) throws
		CouldNotSetPCodeNumberGeneratorException
	{
		throw new CouldNotSetPCodeNumberGeneratorException("You cannot set a pcode number" +

			" generator when using remote pcode generation!");
	}

	/**
	 * Gets the current PCodeNumberGenerator
	 */
        public PCodeNumberGenerator getPCodeNumberGenerator() throws
		CouldNotGetPCodeNumberGeneratorException
	{
		throw new CouldNotGetPCodeNumberGeneratorException("You cannot obtain a pcode " +

			" number generator when using remote pcode generation!");
	}

	/**
	 * This method calls setExaminationDataLocation(), since
	 * they are the same for a remote pcode generator.
	 *
	 * @param loc String the location from which we obtain
	 * the 'löp-nummer' used to generated unique pcodes.
	 */
	public void setNumberGeneratorLocation(String loc)
	{
		setExaminationDataLocation(loc);
	}

	/**
	 * Returns the server URL (examination location) which is
	 * where the numbers are dealt with as well.
	 *
	 * @return String the currently set number generator
	 * location.
	 */
	public String getNumberGeneratorLocation()
	{
		return getExaminationDataLocation();
	}

	/**
	 * Since the number generator location is the same as the
	 * examination data location in the remote client implementation,
	 * this method returns if the examination data location has
	 * been set.
	 *
	 * @return boolean if the number generator location has
	 * been set.
	 */
	public boolean isNumberGeneratorLocationSet()
	{
		return isExaminationDataLocationSet();
	}


	// PCODE PREFIX

	/**
	 * Sets the prefix prepended to generated pcodes. This prefix
	 * is the one sent to the remote pcode generator stub for use
	 * in generating pcodes.
	 *
	 * @param prefix String the prefix prepended to
	 * generated pcodes.
	 */
	public void setGeneratedPCodePrefix( String prefix )
	{
		this.prefix = prefix;
	}

	/**
	 * Obtains the prefix prepended to generated pcodes.
	 *
	 * @return String the prefix prepended to generated
	 * pcodes.
	 */
	public String getGeneratedPCodePrefix( )
	{
		return this.prefix;
	}

	/**
	 * Whether or not the prefix prepended to generated
	 * pcodes has been set.
	 *
	 * @return boolean if the pcode prefix has been set.
	 */
	public boolean isGeneratedPCodePrefixSet( )
	{
		return (this.prefix != null);
	}


	// PID RECOGNITION

	/**
	 * Whether or not the specified pid is recognized by this
	 * pcode generator.
	 *
	 * @param pid String the pid to check if it is recognized.
	 * @return boolean if the specified pid is recognized.
	 * @throws IOException if, due to IO error, the pid could
	 * not be checked for recognition.
	 */
	public boolean recognizes( String pid ) throws IOException
	{
		if (!isConnected())
		{
			connect(); // -> IOException (after disconnecting)
		}

		try
		{
			return remoteHandler.recognizes(pid); // -> RemoteException
		}
		catch (RemoteException e)
		{
			e.printStackTrace();

			disconnect();

			throw new IOException("Could not communicate with server!");
		}
	}
}
