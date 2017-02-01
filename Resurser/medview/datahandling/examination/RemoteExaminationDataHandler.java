/**
 * @(#) RemoteExaminationDataHandler.java
 */

package medview.datahandling.examination;

import misc.foundation.*;

import java.rmi.*;

import medview.datahandling.*;
import medview.datahandling.examination.tree.*;

public interface RemoteExaminationDataHandler extends Remote
{
	// REMOTE CLIENT LISTENERS (STUBS)

	/**
	 * Adds a remote listener (stub) to the server implementation.
	 *
	 * @param l RemoteExaminationDataHandlerListener the remote
	 * listener (stub) to add.
	 * @throws RemoteException if the communication fails.
	 */
	public void addRemoteExaminationDataHandlerListener(RemoteExaminationDataHandlerListener l) throws RemoteException;

	/**
	 * Removes a remote listener (stub) from the server implementation.
	 *
	 * @param l RemoteExaminationDataHandlerListener the remote
	 * listener (stub) to remove.
	 * @throws RemoteException if the communication fails.
	 */
	public void removeRemoteExaminationDataHandlerListener(RemoteExaminationDataHandlerListener l) throws RemoteException;


	// SERVER NAME

	/**
	 * Obtains the name of the server.
	 *
	 * @return String the id of the server.
	 * @throws RemoteException if communication fails.
	 */
	public String getServerName() throws RemoteException;


	// OBTAINING EXAMINATION DATA

	/**
	 * Returns the total number of examinations available in
	 * the data source handled by the server.
	 *
	 * @return the number of examinations.
	 */
	public int getExaminationCount() throws RemoteException;

	/**
	 * Obtain the examinations for the specified patient.
	 *
	 * @param pid PatientIdentifier the patient for which
	 * you want to obtain examinations.
	 * @return ExaminationIdentifier[] the examinations.
	 * @throws RemoteException if communication fails.
	 */
	public ExaminationIdentifier[] getExaminations( PatientIdentifier pid ) throws RemoteException;

	/**
	 * Obtains all examination value containers.
	 * @param notifiable RemoteProgressNotifiable
	 * @return ExaminationValueContainer[]
	 * @throws RemoteException
	 */
	public ExaminationValueContainer[] getAllExaminationValueContainers(RemoteProgressNotifiable notifiable) throws RemoteException;

	/**
	 * Obtains a value container for the specified examination.
	 *
	 * @param id ExaminationIdentifier the examination that you
	 * want to obtain the values for.
	 * @return ExaminationValueContainer the value container.
	 * @throws RemoteException if communication fails.
	 * @throws NoSuchExaminationException if there is no such
	 * examination on the server.
	 */
	public ExaminationValueContainer getExaminationValueContainer( ExaminationIdentifier id ) throws RemoteException, NoSuchExaminationException;

	/**
	 * *** THINK ABOUT THIS ***
	 *
	 * @return ExaminationIdentifier[]
	 * @throws RemoteException
	 */
	public ExaminationIdentifier[] refreshExaminations( long sinceTime ) throws RemoteException;


	// OBTAINING IMAGE DATA

	/**
	 * Obtain the number of images associated with the
	 * specified patient.
	 *
	 * @param pid PatientIdentifier the patient.
	 * @return int the number of images associated with
	 * the patient.
	 * @throws RemoteException if communication fails.
	 */
	public int getImageCount( PatientIdentifier pid ) throws RemoteException;

	/**
	 * Return a byte array of bytes representing each
	 * image associated with the specified examination.
	 *
	 * @param id ExaminationIdentifier the examination for
	 * which you want to obtain the images.
	 * @return byte[][] the image byte array of bytes.
	 * @throws RemoteException if communication fails.
	 */
	public byte[][] getImages( ExaminationIdentifier id ) throws RemoteException;


	// OBTAINING PATIENT DATA

	/**
	 * Obtain an array of all patients kept on the server.
	 * Might take some time, therefore there is the opportunity
	 * to pass along a remote progress notifiable.
	 *
	 * @param notifiable RemoteProgressNotifiable for notification
	 * of progress.
	 * @return PatientIdentifier[] the patients kept on the server.
	 * @throws RemoteException if communication fails.
	 */
	public PatientIdentifier[] getPatients( RemoteProgressNotifiable notifiable ) throws RemoteException;


	// STORING EXAMINATION DATA

	/**
	 * Stores the specified tree, along with the images and an
	 * examination identifier identifying the examination associated
	 * with the tree, into the servers knowledge base.
	 *
	 * @param tree Tree the tree to store.
	 * @param images byte[][] the images associated with the tree.
	 * @param eid ExaminationIdentifier the examination the tree
	 * represents.
	 * @return whether the examination existed previously in the
	 * database, or if the save resulted in a new examination being
	 * added.
	 * @throws RemoteException if communication fails.
	 */
	public int saveExamination(Tree tree, byte[][] images, ExaminationIdentifier eid) throws RemoteException;
}
