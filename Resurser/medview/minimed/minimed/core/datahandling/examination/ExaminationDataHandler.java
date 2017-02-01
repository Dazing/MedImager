/*
 * @(#)ExaminationDataHandler.java
 *
 * $Id: ExaminationDataHandler.java,v 1.1 2006/05/29 18:32:52 limpan Exp $
 *
 * $Log: ExaminationDataHandler.java,v $
 * Revision 1.1  2006/05/29 18:32:52  limpan
 * Added minimed code
 * ----------------------------------------------------------------------
 *
 * Revision 1.35  2005/09/09 15:40:48  lindahlf
 * Server cachning
 *
 * Revision 1.34  2005/01/30 15:22:07  lindahlf
 * T4 Integration
 *
 * Revision 1.33  2004/11/19 12:32:29  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.32  2004/11/10 13:02:53  erichson
 * added getExaminationCount
 *
 * Revision 1.31  2004/11/09 21:13:49  lindahlf
 * Datalayer upgrade in connection with server development
 *
 * Revision 1.30  2004/11/06 01:10:44  lindahlf
 * Lade till möjlighet att veta om save() överskriver eller lägger till ny
 *
 * Revision 1.29  2004/11/03 12:38:09  erichson
 * Changed exportToMVD method signature.
 *
 * Revision 1.28  2004/10/21 12:16:46  erichson
 * Added a ProgressNotifiable to both exportMVD calls.
 *
 * Revision 1.27  2004/10/19 21:40:22  lindahlf
 * Lade tillbaka @deprecation eftersom det faktiskt hjälper om man har en bra IDE
 *
 * Pågående uppgradering av MedServer
 *
 * Revision 1.26  2004/10/06 14:22:25  erichson
 * Added exportToMVD methods.
 *
 * Revision 1.25  2004/10/01 16:39:48  lindahlf
 * no message
 *
 * Revision 1.24  2004/08/24 18:25:13  lindahlf
 * no message
 *
 * Revision 1.23  2004/01/20 19:42:19  lindahlf
 * Major Upgrade PID support
 *
 * Revision 1.22  2003/09/09 17:17:09  erichson
 * saveTree -> saveExamination
 *
 * Revision 1.21  2003/08/19 16:03:15  lindahlf
 * See 030819_Release_Notes_DATAHANDLING_MISC_COMMON.pdf
 *
 * Revision 1.20  2003/04/10 01:47:21  lindahlf
 * no message
 *
 * Revision 1.19  2002/12/18 11:21:26  lindahlf
 * no message
 *
 * Revision 1.18  2002/12/06 00:31:41  lindahlf
 * Added support for notifying of progress in the data handling layer using the new ProgressNotifiable interface in misc.foundation - a new method was added to the ExaminationDataHandler interface and thus MedViewDataHandler for this purpose. // Fredrik
 *
 * Revision 1.17  2002/12/04 13:53:08  erichson
 * Undid the changes of revision 1.16 // Nils
 *
 * Revision 1.16  2002/12/02 16:56:22  lindahlf
 * Lade till 'various resources' i CVS root, där jag tänkte vi kunde lägga sådant vi vill dela till varandra inom CVS som inte har med kod att göra (koden ligger ju under respektive paket). Detta har inget att göra med hur strukturen ser ut i levererade applikationer etc, det är ju redan löst med preference / user dirs etc. Ni får protestera om ni tycker det är fel att lägga detta här. // Fredrik
 *
 * Revision 1.15  2002/11/21 16:08:06  erichson
 * Changed getImage from returning Image to returning ExaminationImage
 *
 * Revision 1.14  2002/11/19 00:07:48  lindahlf
 * Added preference functionality. - Fredrik
 *
 * Revision 1.13  2002/10/22 14:48:43  erichson
 * changed saveExamination to saveTree // NE
 *
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package minimed.core.datahandling.examination;

import java.io.IOException;

import minimed.core.datahandling.PatientIdentifier;
import minimed.core.datahandling.examination.tree.Tree;
import misc.foundation.ProgressNotifiable;
//import medview.datahandling.examination.filter.*;
//import minimed.core.datahandling.examination.tree.*;
//import medview.datahandling.images.*;

/**
 * Defines methods associated with retrieving information about
 * patients and their associated examinations. This interface should
 * be implemented if you wish to define a special kind of examinaton
 * data handling - the default implementation is the MVDHandler,
 * which implements the methods defined here by processing the
 * structure of a MVD (MedViewDatabase).
 *
 * @author Fredrik Lindahl
 */
public interface ExaminationDataHandler
{

	/**
	 * Constant (returned from the saveExamination() method) that
	 * indicates that the examination did not exist prior to the
	 * call being made.
	 */
	public static final int NEW_EXAMINATION = 1;

	/**
	 * Constant (returned from the saveExamination() method) that
	 * indicates tha the examination existed prior to the call being
	 * made. This might happen, for instance, when a new examination
	 * is being entered using an input tool, and the examination is
	 * saved in several 'passes' before done. Each of these passes
	 * does not add an examination, and therefore the data layer
	 * should not fire an event indicating so. These constants are a
	 * way to know whether an examination was indeed added or not.
	 */
	public static final int PREVIOUS_EXAMINATION = 2;


	/**
	 * Adds an examination datahandler listener. The
	 * listener will be notified of events such as
	 * when an examination has been added or when the
	 * location of examination data has changed.
	 */
	//public void addExaminationDataHandlerListener(ExaminationDataHandlerListener l);

	/**
	 * Removes an examination datahandler listener. The
	 * listener will be notified of events such as
	 * when an examination has been added or when the
	 * location of examination data has changed.
	 */
	//public void removeExaminationDataHandlerListener(ExaminationDataHandlerListener l);

	/**
	 * Allows the datahandler to deal with the system
	 * shutting down. For instance, if the datahandler is
	 * a client to a server, lets it tell the server that it
	 * is shutting down so the server can remove it from its
	 * notification list.
	 */
	public void shuttingDown();

	/**
	 * Returns an array of all the patients listed
	 * at the currently set data location. If the
	 * data location for some reason is inaccesible
	 * or if it contains no patients, an empty array
	 * will be returned. A null value is never to be
	 * returned.
	 */
	public PatientIdentifier[] getPatients() throws IOException;

	/**
	 * Returns an array of all the patients listed
	 * at the currently set data location. If the
	 * data location for some reason is inaccesible
	 * or if it contains no patients, an empty array
	 * will be returned. A null value is never to be
	 * returned. This version of the method accepts a
	 * Notifiable implementation to notify of the
	 * progress of the patient retrieval. If the
	 * argument is null, the method does exactly the
	 * same thing as the normal getPatients() method.
	 * Note that the notifiable will never be notified
	 * if the implementation deems that it takes too
	 * short time to complete the task for anything
	 * useful to be displayed.
	 */
	public PatientIdentifier[] getPatients( ProgressNotifiable notifiable ) throws IOException;


	/**
	 * Returns the total number of examinations available in the data
	 * source handled by this ExaminationDataHandler.
	 * @return the number of examinations.
	 */
	public int getExaminationCount() throws IOException;

	/**
	 * Returns the total number of examinations for the specified patient.
	 * @param pid PatientIdentifier
	 * @return int
	 * @throws IOException
	 */
	public int getExaminationCount(PatientIdentifier pid) throws IOException;


	/**
	 * Returns an array of ExaminationIdentifiers describing
	 * the examinations for the specified pid. An empty
	 * array is returned if no examinations for the specified
	 * patient were found or if the data location for some
	 * reason could not be read. Note that null values are not
	 * allowed to be returned (important for implementation
	 * developers).
	 */
	public ExaminationIdentifier[] getExaminations(PatientIdentifier pid) throws IOException;

	/**
	 * Returns all examinations found in the knowledge base after the
	 * time of last cache construct.
	 *
	 * 1) getPatients().
	 * 2) getExaminations(pid).
	 * 3) getExaminationValueContainer(pid).
	 *
	 * If new examinations are found, each will trigger an examination
	 * added event as well as be included in the returned array. It is
	 * recommended that you use the event notification framework for
	 * local notification and the returned array only in those cases
	 * where this is not applicable, such as when a client needs to
	 * know the added examinations on the server after a certain time.
	 *
	 * @return ExaminationIdentifier[] all examinations found in the
	 * knowledge base after the time of last cache construct.
	 * @throws IOException if something goes wrong.
	 * @deprecated do not use anymore - too complex to be worth the effort
	 */
	//public ExaminationIdentifier[] refreshExaminations() throws IOException;

	/**
	 * Returns all examinations found in the knowledge base after the
	 * specified time.
	 *
	 * If new examinations are found, each will trigger an examination
	 * added event as well as be included in the returned array. It is
	 * recommended that you use the event notification framework for
	 * local notification and the returned array only in those cases
	 * where this is not applicable, such as when a client needs to
	 * know the added examinations on the server after a certain time.
	 *
	 * @param sinceDate the examinations returned are the ones added to
	 * the knowledge base after this time.
	 * @return ExaminationIdentifier[] all examinations found in the
	 * knowledge base after the specified time.
	 * @throws IOException if something goes wrong.
	 * @deprecated do not use anymore - too complex to be worth the effort
	 */
	//public ExaminationIdentifier[] refreshExaminations(long sinceTime) throws IOException;

	/**
	 * Returns an integer representing the total
	 * number of images taken for a patient with
	 * the specified patient identifier. If the data
	 * location for some reason is inaccessible,
	 * or some other error occurs, a value of 0
	 * will be returned.
	 */
	public int getImageCount(PatientIdentifier pid) throws IOException;

	/**
	 * Returns all examination value containers at the currently set
	 * examination data location.
	 * @param not ProgressNotifiable
	 * @return ExaminationValueContainer[]
	 * @throws <any>
	 */
	public ExaminationValueContainer[] getAllExaminationValueContainers(ProgressNotifiable not) throws IOException;

	/**
	 * Returns all examination value containers at the currently set
	 * examination data location. Here you can specify an optimization
	 * hint.
	 * @param hint
	 * @return ExaminationValueContainer[]
	 * @throws IOException
	 */
	public ExaminationValueContainer[] getAllExaminationValueContainers(ProgressNotifiable not, int hint) throws IOException;

	/**
	 * Returns an ExaminationValueContainer containing the
	 * values for the examination identified by the argument.
	 * Will return null if there is no examination value
	 * container for the argument identifier. You can also
	 * specify a hint which indicates whether you want to
	 * have memory-efficient data retrieval or fast and
	 * efficient data retrieval (at the expense of some memory).
	 * The hint constants are defined in the data layer constants
	 * interface.
	 * @param id ExaminationIdentifier
	 * @return ExaminationValueContainer
	 * @throws IOException
	 * @throws NoSuchExaminationException
	 */
	public ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id) throws
		IOException, NoSuchExaminationException;

	/**
	 * Returns an ExaminationValueContainer containing the
	 * values for the examination identified by the argument.
	 * Will return null if there is no examination value
	 * container for the argument identifier. You can also
	 * specify a hint which indicates whether you want to
	 * have memory-efficient data retrieval or fast and
	 * efficient data retrieval (at the expense of some memory).
	 * The hint constants are defined in the data layer constants
	 * interface.
	 * @param id ExaminationIdentifier
	 * @param hint int
	 * @return ExaminationValueContainer
	 * @throws IOException
	 * @throws NoSuchExaminationException
	 */
//	public ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id, int hint) throws
//		IOException, NoSuchExaminationException, InvalidHintException;

	/**
	 * Saves a Tree of examination data to the database. If
	 * any error occurs during the saving (or perhaps if the
	 * data location has not been set prior to the call of
	 * this method) an exception will be thrown.
	 *
	 * @param tree Tree the tree to save.
	 * @return int one of the two constants defined in the
	 * ExaminationDataHandler interface: NEW_EXAMINATION or
	 * PREVIOUS_EXAMINATION.
	 * @throws IOException if something goes wrong.
	 */
	public int saveExamination(Tree tree) throws IOException;

	/**
	 * Saves the specified tree to the specified location.
	 * Observe that this method bypasses the currently set
	 * examination data location. The method is internally
	 * synchronized to avoid retrieval deadlock after an
	 * event has been fired. The intended use of this method
	 * is to be able to store a tree to another location
	 * without having to set the core examination data location,
	 * for instance for storing a tree to a temporary location
	 * if the primary location is inaccessible.
	 *
	 * @param tree Tree the tree to save.
	 * @param imageArray ExaminationImage[] an array of associated
	 * examination images.
	 * @param location the location to store the examination to.
	 * @return int one of the two constants defined in the
	 * ExaminationDataHandler interface: NEW_EXAMINATION or
	 * PREVIOUS_EXAMINATION.
	 * @throws IOException if something goes wrong.
	 */
	//public int saveExamination(Tree tree, ExaminationImage[] imageArray, String location) throws IOException;

	/**
	 * Get the images associated with the specified examination.
	 * If the specified examination is not found in the examination
	 * data location, an exception will be thrown.
	 */
//	public ExaminationImage[] getImages(ExaminationIdentifier id) throws IOException, NoSuchExaminationException;

	/**
	 * Returns the set examination data location
	 * in it's 'raw' form. For example, it could
	 * return 'c:\medview\databases\MSTest.mvd'.
	 * A null value will be returned if the set
	 * location is invalid or for some other reason
	 * cannot be accessed. Also, if the location
	 * has not yet been set, a null value will be
	 * returned.
	 */
	public String getExaminationDataLocation();

	/**
	 * Returns the data location expressed in a
	 * (perhaps) more simple way than the 'raw'
	 * data location. For example, it could
	 * return 'MSTest.mvd' instead of the full
	 * file path to the mvd. This could be used
	 * for displaying the chosen location in a
	 * graphical user interface, for example. A
	 * string specifying that the location is not
	 * valid or not set correctly will be returned
	 * if the location is not set or invalid.
	 */
	public String getExaminationDataLocationID();

	/**
	 * Sets the location of the examination data.
	 * For example - for a MVD handler, the location
	 * could be 'c:\medview\databases\MSTest.mvd',
	 * while for a SQL handler could be something like
	 * 'login:password@server.myip.org:1024'.
	 */
	public void setExaminationDataLocation(String loc);

	/**
	 * Returns whether or not the current implementation
	 * class for the ExaminationDataHandler interface deems
	 * the currently set location to be valid. If the
	 * location has not been set, it is deemed invalid
	 */
	public boolean isExaminationDataLocationValid();

	/* ---- Export methods ---- */

	/**
	* Exports all examinations belonging to the specifiec patients to a new MVD file.
	* @param patientIdentifiers the identifiers of the patients to export
	* @param newMVDlocation the location of the MVD to export to
	* @param notifiable ProgressNotifiable object to keep track of progress.
	* @param filter the examination content filter, which is applied to the data when exporting.
	* @param allowPartialExport whether to allow partial exports or not. If partial export is not allowed,
	* an IOException should be thrown if there is any overlap, no matter how small, between the target MVD and
	* the examinations we're exporting.
	* @return the amount of examinations that were exported.
	*/
//	public int exportToMVD(PatientIdentifier[] patientIdentifiers,
//				String newMVDlocation,
//				ProgressNotifiable notifiable,
//				ExaminationContentFilter filter,
//				boolean allowPartialExport)
//				throws IOException;


	/**
	* Exports a set of examinations to a new MVD file.
	* @param examinationIdentifiers the identifiers of the examinations to export
	* @param newMVDlocation the location of the MVD to export to
	* @param notifiable ProgressNotifiable object to keep track of progress.
	* @param filter the examination content filter, which is applied to the data when exporting.
	* @param allowPartialExport whether to allow partial exports or not. If partial export is not allowed,
	* an IOException should be thrown if there is any overlap, no matter how small, between the target MVD and
	* the examinations we're exporting.
	* @return the amount of examinations that were exported. (Not as useful here as for exportMVD(PatientIdentifier[],
	* but can be useful when allowing partial export
	*/
//	public int exportToMVD(ExaminationIdentifier[] examinationIdentifiers,
//				String newMVDlocation,
//				ProgressNotifiable notifiable,
//				ExaminationContentFilter filter,
//				boolean allowPartialExport)
//				throws IOException;

}
