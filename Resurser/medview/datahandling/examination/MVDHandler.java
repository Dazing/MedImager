/*
 * @(#)MVDHandler.java
 *
 * $Id: MVDHandler.java,v 1.68 2006/05/29 18:32:48 limpan Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 *
 * $Log: MVDHandler.java,v $
 * Revision 1.68  2006/05/29 18:32:48  limpan
 * Added minimed code
 * ----------------------------------------------------------------------
 *
 * Revision 1.67  2006/04/24 14:17:38  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.66  2006/03/21 13:43:04  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.65  2005/12/22 19:10:23  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.64  2005/09/09 15:40:48  lindahlf
 * Server cachning
 *
 * Revision 1.63  2005/07/27 14:08:15  erichson
 * Modified an error message + some comments
 *
 * Revision 1.62  2005/06/09 08:56:09  erichson
 * Additional info in the error message when exporting fails.
 *
 * Revision 1.61  2005/03/16 13:56:09  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.60  2005/01/30 15:22:07  lindahlf
 * T4 Integration
 *
 * Revision 1.59  2004/12/08 14:47:48  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.58  2004/12/02 13:41:07  erichson
 * Fix to refreshExaminations since it would crash if p-code or date couldn't be extracted.
 *
 * Revision 1.57  2004/11/24 16:25:53  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.56  2004/11/23 10:21:22  erichson
 * Fix to constructFileMap to avoid hanging if a p-code extraction fails (null p-code is encountered).
 *
 * Revision 1.55  2004/11/19 12:32:29  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.54  2004/11/16 07:58:56  erichson
 * Cleaned up some code I had commented out, also removed another try {} catch () { return null}
 *
 * Revision 1.53  2004/11/13 12:17:20  erichson
 * Fix to close buffered readers instead of fileinputstreams
 *
 * Revision 1.52  2004/11/11 22:36:28  lindahlf
 * MedServer Test Pack 2
 *
 * Revision 1.51  2004/11/10 13:01:54  erichson
 * added getExaminationCount()
 *
 * Revision 1.50  2004/11/09 21:13:49  lindahlf
 * Datalayer upgrade in connection with server development
 *
 * Revision 1.49  2004/11/06 01:10:48  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.48  2004/11/04 20:07:42  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.47  2004/11/03 12:40:07  erichson
 * updated export with new arguments, and rewrote it to be "safe" i.e never overwrite tree files or pictures.
 *
 * Revision 1.46  2004/10/29 12:13:26  erichson
 * Bug fix for case when export would fail if a picture could not be found. Now just skips it and prints an error message.
 *
 * Revision 1.45  2004/10/21 12:25:45  erichson
 * added a ProgressNotifiable to exportToMVD.
 *
 * Revision 1.44  2004/10/19 21:40:26  lindahlf
 * Lade tillbaka @deprecation eftersom det faktiskt hjälper om man har en bra IDE
 *
 * Revision 1.43  2004/10/11 18:56:36  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.42  2004/10/06 14:32:00  erichson
 * * Separated out getRelativePaths from getImages
 * * Added exportToMVD methods
 *
 * Revision 1.41  2004/10/05 14:11:10  erichson
 * Separated getExaminationFile out of getExaminationValueContainer (to be able to re-use the code in the export methods)
 *
 * Revision 1.40  2004/10/01 16:39:49  lindahlf
 * no message
 *
 * Revision 1.39  2004/08/24 18:25:13  lindahlf
 * no message
 *
 * Revision 1.38  2004/08/24 17:02:48  lindahlf
 * no message
 *
 * Revision 1.37  2004/05/18 18:21:05  lindahlf
 * Åtgärdade fel med bild-filnamn skapade utan datum
 *
 * Revision 1.36  2004/03/08 23:58:27  lindahlf
 * no message
 *
 * Revision 1.35  2004/02/28 17:51:26  lindahlf
 * no message
 *
 * Revision 1.34  2004/02/26 12:14:05  lindahlf
 * Added Cache support to MVDHandler, and proper patient id support when invoking MR from MS
 *

 * Revision 1.33  2004/02/20 17:29:51  lindahlf
 * no message
 *
 * Revision 1.32  2004/02/19 18:21:27  lindahlf
 * Major update patch 1
 *
 * Revision 1.31  2004/01/20 19:42:20  lindahlf
 * Major Upgrade PID support
 *
 * Revision 1.30  2003/11/13 08:53:40  lindahlf
 * -
 *
 * Revision 1.29  2003/10/19 19:48:59  oloft
 * saveExaminationImages cuts any path preceeding filename
 *
 * Revision 1.28  2003/10/08 22:33:20  oloft
 * commented out code in saveExamination
 *
 * Revision 1.27  2003/10/01 07:48:01  oloft
 * Reversed logic in SaveExaminationImages: if an image file already exists it is not overwritten, previously an exception was thrown
 *
 * Revision 1.26  2003/09/30 10:37:04  oloft
 * Hack line 371, saveExaminationImages, added extra  Pictures to newPicturePath
 *
 * Revision 1.25  2003/09/09 20:59:04  lindahlf
 * Kan nu läsa löv med flera rader
 *
 * Revision 1.24  2003/09/09 17:36:56  erichson
 * * saveTree -> saveExamination
 * * lade till privat metod saveExaminationImages() som anropas av
 * saveExamination
 * * lade till statisk metod getExaminationIdentifier(Tree). Den är
 * osnygg och bör dessutom flyttas till bättre ställe i framtiden,
 * men jag var tvungen att slänga in den där för att få ihop
 * MedRecords till i morgon...
 *
 * Revision 1.23  2003/09/07 21:39:53  erichson
 * Updated saveTree method to use Tree.toString().
 * Removed writeNode method since it was no longer used.
 *
 * Revision 1.22  2003/08/20 16:29:08  lindahlf
 * Lade till stöd för multitrådat
 *
 * Revision 1.21  2003/08/19 16:03:15  lindahlf
 * See 030819_Release_Notes_DATAHANDLING_MISC_COMMON.pdf
 *
 * Revision 1.19  2003/04/10 01:47:21  lindahlf
 * no message
 *
 * Revision 1.18  2003/03/13 11:22:05  lindahlf
 * no message
 *
 * Revision 1.17  2003/03/13 01:21:46  lindahlf
 * Ordnat textbuggen för SC i page-vyn och diverse annat
 *
 * Revision 1.16  2002/12/18 11:21:26  lindahlf
 * no message
 *
 * Revision 1.15  2002/12/06 00:31:29  lindahlf
 * Added support for notifying of progress in the data handling layer using the new ProgressNotifiable interface in misc.foundation - a new method was added to the ExaminationDataHandler interface and thus MedViewDataHandler for this purpose. // Fredrik
 *
 * Revision 1.14  2002/12/04 12:04:11  nazari
 * no message
 *
 * Revision 1.13  2002/12/03 12:24:39  oloft
 * Added dummy getImages method
 *
 * Revision 1.12  2002/11/26 17:14:24  lindahlf
 * Ordnade till generatorn och parse trädet ytterligare - Fredrik
 *
 * Revision 1.11  2002/11/19 00:07:48  lindahlf
 * Added preference functionality. - Fredrik
 *
 * Revision 1.10  2002/11/11 17:53:02  lindahlf
 * LATIN_1 kodning
 *
 * Revision 1.9  2002/11/04 19:24:55  lindahlf
 * Ordnade så att termer nu blir exakt såsom de är definierade i termDefinitions- och termValues filerna. Fick ordna till MVDHandler och DefaultGeneratorEngine. // Fredrik
 *
 * Revision 1.8  2002/10/22 16:45:56  zachrisg
 * Changed saveExamination -> saveTree for compability with changes to ExaminationDataHandler interface
 *
 */

package medview.datahandling.examination;

import java.io.*;

import java.util.*;

import javax.swing.event.*; // listener list only

import medview.common.data.*;

import medview.datahandling.*;
import medview.datahandling.images.*;
import medview.datahandling.examination.filter.*;
import medview.datahandling.examination.tree.*;

import misc.foundation.*;
import misc.foundation.io.*;

/**
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MVDHandler	implements ExaminationDataHandler, MedViewLanguageConstants,
	MedViewDataConstants
{
	// MEMBERS

	private File mvdDirectory = null;

	private EventListenerList listenerList = new EventListenerList();

	private TreeFileDateParser dateParser = new DefaultTreeFileDateParser();

	private TreeFilePhotoParser photoParser = new DefaultTreeFilePhotoParser();


	private static final String DATUM_NODE_NAME = MVD_NODE_PREFIX + DATE_TERM_NAME;

	private static final String PCODE_NODE_NAME = MVD_NODE_PREFIX + PCODE_TERM_NAME;

	private static final String PHOTO_NODE_NAME = MVD_NODE_PREFIX + PHOTO_TERM_NAME;

	private static final String PID_NODE_NAME = MVD_NODE_PREFIX + PID_TERM_NAME;

	private static final String INVALID_DATA_LOCATION_ID_STRING = "Invalid data location";

	private static final String LATIN_ENC = IOConstants.ISO_LATIN_1_CHARACTER_ENCODING;

	public static final String NULL_PID_STRING = "NULL";


	// EVENTS AND LISTENERS

	/**
	 * Adds an examination data handler listener to this implementation.
	 * @param l ExaminationDataHandlerListener
	 */
	public void addExaminationDataHandlerListener(ExaminationDataHandlerListener l)
	{
		listenerList.add(ExaminationDataHandlerListener.class, l);
	}

	/**
	 * Removes an examination data handler listener from this implementation.
	 * @param l ExaminationDataHandlerListener
	 */
	public void removeExaminationDataHandlerListener(ExaminationDataHandlerListener l)
	{
		listenerList.remove(ExaminationDataHandlerListener.class, l);
	}

	/**
	 * Fires an event indicating that an examination was added to the
	 * currently set examination data location. The event will have with
	 * it information about the examination that was added in form of an
	 * examination identifier.
	 * @param id ExaminationIdentifier
	 */
	private void fireExaminationAdded(ExaminationIdentifier id)
	{
		final Object[] listeners = listenerList.getListenerList();

		final ExaminationDataHandlerEvent event = new ExaminationDataHandlerEvent(this);

		event.setIdentifier(id);

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == ExaminationDataHandlerListener.class)
			{
				((ExaminationDataHandlerListener)listeners[i + 1]).examinationAdded(event);
			}
		}
	}

	/**
	 * Fires an event indicating that an examination was updated in the
	 * currently set examination data location. The event will have with
	 * it information about the examination that was updated in form of an
	 * examination identifier.
	 * @param id ExaminationIdentifier
	 */
	private void fireExaminationUpdated(ExaminationIdentifier id)
	{
		final Object[] listeners = listenerList.getListenerList();

		final ExaminationDataHandlerEvent event = new ExaminationDataHandlerEvent(this);

		event.setIdentifier(id);

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == ExaminationDataHandlerListener.class)
			{
				((ExaminationDataHandlerListener)listeners[i + 1]).examinationUpdated(event);
			}
		}
	}

	/**
	 * Fires an event indicating that an examination was removed from the
	 * currently set examination data location. The event will have with it
	 * information about the examination that was removed in form of an
	 * examination identifier.
	 * @param id ExaminationIdentifier
	 */
	private void fireExaminationRemoved(ExaminationIdentifier id)
	{
		final Object[] listeners = listenerList.getListenerList();

		final ExaminationDataHandlerEvent event = new ExaminationDataHandlerEvent(this);

		event.setIdentifier(id);

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == ExaminationDataHandlerListener.class)
			{
				((ExaminationDataHandlerListener)listeners[i + 1]).examinationRemoved(event);
			}
		}
	}

	/**
	 * Fires an event indicating that the examination data location
	 * was changed. The event will have with it information about the
	 * new examination data location.
	 */
	private void fireExaminationLocationChanged()
	{
		Object[] listeners = listenerList.getListenerList();

		ExaminationDataHandlerEvent event = new ExaminationDataHandlerEvent(this);

		event.setLocation(mvdDirectory.getPath());

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == ExaminationDataHandlerListener.class)
			{
				((ExaminationDataHandlerListener)listeners[i + 1]).examinationLocationChanged(event);
			}
		}
	}

	/**
	 * Fires an event indicating that the examination data location
	 * ID (usually a descriptive string of the location) was changed.
	 * The event will have with it information about the new data
	 * location ID.
	 */
	private void fireExaminationLocationIDChanged()
	{
		Object[] listeners = listenerList.getListenerList();

		ExaminationDataHandlerEvent event = new ExaminationDataHandlerEvent(this);

		event.setLocationID(getExaminationDataLocationID());

		for (int i = listeners.length - 2; i >= 0; i -= 2)
		{
			if (listeners[i] == ExaminationDataHandlerListener.class)
			{
				((ExaminationDataHandlerListener)listeners[i + 1]).examinationLocationIDChanged(event);
			}
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
	}


	// STORING EXAMINATION DATA

	/**
	 * Saves the specified tree to the currently set
	 * MVD location. The locations have to be valid
	 * before the method is called, otherwise an
	 * exception will be thrown. The locations are made
	 * valid by setting the examination data location
	 * to a valid MVD database. The method is internally
	 * synchronized to avoid retrieval deadlock after an
	 * event has been fired.
	 */
	public final int saveExamination(Tree root, ExaminationImage[] imageArray) throws IOException
	{
		if (!locationsAreValid())
		{
			throw new IOException("Locations not valid");
		}
		else
		{
			return this.saveExamination(root, imageArray, mvdDirectory.getPath());
		}
	}

	/**
	 * Saves the specified tree to the specified location.
	 * Observe that this method may bypass the currently set
	 * examination data location. The method is internally
	 * synchronized to avoid retrieval deadlock after an
	 * event has been fired. Also observe that the necessary
	 * directories will be created if the specified location
	 * is non-existant. Will return an integer indicating if
	 * the save resulted in a new file being created, or if
	 * it simply updated a previous tree file.
	 *
	 * @param root Tree
	 * @param imageArray ExaminationImage[]
	 * @param location String
	 * @return int
	 * @throws IOException
	 */
	public final int saveExamination(Tree root, ExaminationImage[] imageArray, String location) throws IOException
	{
		int retVal = NEW_EXAMINATION; // indicates that the examination did not exist in the MVD prior to the call

		// create MVD-directory if non-existant

		File mvdDirectory = new File(location);

		if (!mvdDirectory.exists())
		{
			mvdDirectory.mkdirs(); // create mvd directory if non-existant
		}

		// create forest directory if non-existant

		File mvdForestDirectory = new File(mvdDirectory, MVD_FOREST_SUBDIRECTORY);

		if (!mvdForestDirectory.exists())
		{
			mvdForestDirectory.mkdirs(); // create the forest directory if not existant
		}

		// store examinations and their pictures

		try
		{
			ExaminationIdentifier examinationIdentifier = MedViewUtilities.constructExaminationIdentifier(root);

			synchronized (this)
			{
				// obtain the file by examining the concrete node's path

				Tree concreteNode = root.getNode(CONCRETE_ID_TERM_NAME);

				Tree concreteValNode = concreteNode.getFirstChild();

				String concretePath = concreteValNode.getValue();

				String fileName = IOUtilities.extractFileWithoutPath(concretePath); // strip the path part of the concrete path

				File writeFile = new File(mvdForestDirectory, fileName);

				// check if the file exists (determines return value from method)

				if (writeFile.exists())
				{
					retVal = PREVIOUS_EXAMINATION; // indicates that the examination existed prior to call
				}

				FileOutputStream fos = new FileOutputStream(writeFile);

				OutputStreamWriter osw = new OutputStreamWriter(fos, LATIN_ENC);

				// write the tree to the output file

				osw.write(root.toString()); // Replaces old writeNode functionality. // NE 03-0907

				osw.flush();

				osw.close();

				// save the examination images (will create pictures dir if non-existant)

				saveExaminationImages(imageArray, location);

				// make sure that the cache structure in the mvd is consistent with the MVD contents

				MedViewUtilities.PIDPCodePair pair = MedViewUtilities.parsePIDPCode(root);

				MVDCacheHandler.instance().addToExaminationCache(new PatientIdentifier(

					pair.getPCode(), pair.getPID()), fileName, mvdDirectory, null);

				MVDCacheHandler.instance().addToPIDCache(pair.getPID(), pair.getPCode(), mvdDirectory, null);
			}

			/* NOTE: we cannot place the fire() functionality in the filemap
			   check since we do not always do the filemap check, depending
			   on whether the filemap has been constructed or not. */

			if (retVal == NEW_EXAMINATION)
			{
				fireExaminationAdded(examinationIdentifier); // only notify of addition if new examination
			}
			else
			{
				fireExaminationUpdated(examinationIdentifier);
			}

			return retVal;
		}
		catch (Exception e) // NullPointerException etc..
		{
			e.printStackTrace(); // for debug info

			throw new IOException(e.getMessage());
		}
	}

	/**
	 * Removes the specified examination from the mvd, along with the
	 * images.
	 *
	 * @param eid ExaminationIdentifier
	 * @throws IOException
	 */
	public void removeExamination(ExaminationIdentifier eid) throws IOException
	{
		try
		{
			// remove the images

			ExaminationImage[] images = getImages(eid);

			for (int ctr = 0; ctr < images.length; ctr++)
			{
				images[ctr].getFile().delete(); // -> MethodNotSupportedException
			}

			// remove the tree file

			File examinationFile = getExaminationFile(eid);

			examinationFile.delete(); // -> IOException

			// remove from cache

			MVDCacheHandler.instance().removeFromExaminationCache(eid.getPID(),

				examinationFile.getName(), mvdDirectory, null);

			// notify listeners

			fireExaminationRemoved(eid);
		}
		catch (MethodNotSupportedException exc)
		{
			exc.printStackTrace(); // will not happen here, if it does it's programmer error
		}
		catch (NoSuchExaminationException exc)
		{
			exc.printStackTrace();
		}
	}

	/**
	 * Stores an array of examination images to the specified
	 * examination data location. Will create the pictures
	 * subdirectories in the specified location if they are non-
	 * existant.
	 * @param imageArray ExaminationImage[]
	 * @param location String
	 * @throws IOException
	 */
	private void saveExaminationImages(ExaminationImage[] imageArray, String location) throws IOException
	{
		// obtain reference to the pictures folder

		File mvdDirectory = new File(location);

		File picturesSubdirectory = new File(mvdDirectory, MVD_PICTURES_SUBDIRECTORY);

		File picturesPicturesSubdirectory = new File(picturesSubdirectory, MVD_PICTURES_SUBDIRECTORY);

		if (!picturesPicturesSubdirectory.exists())
		{
			picturesPicturesSubdirectory.mkdirs(); // create the pictures directory if not existant
		}

		// for each image, write it to the folder

		for (int i = 0; i < imageArray.length; i++)
		{
			// obtain image data

			InputStream is = imageArray[i].getInputStream();

			int numberOfBytes = is.available();

			byte[] buffer = new byte[numberOfBytes];

			int sizeRead = is.read(buffer, 0, numberOfBytes);

			if (sizeRead < numberOfBytes)
			{
				String a = "Read too few bytes from ";

				String b = "examinationImage ";

				String c = imageArray[i].getName() + " (";

				String d = sizeRead + " < " + numberOfBytes;

				throw new IOException(a + b + c + d);
			}

			is.close();

			// construct image file name

			String imageFileName = imageArray[i].getName();

			String imageFileNameUpperCase = imageFileName.toUpperCase();

			if ((!imageFileNameUpperCase.endsWith(".JPG")) && // check if the image file name ends with

			    (!imageFileNameUpperCase.endsWith(".JPEG"))) // .jpg or .jpeg. If not, append .jpg
			{
				imageFileName = imageFileName + ".jpg";
			}

			// write the actual image file

			File newPictureFile = new File(picturesPicturesSubdirectory,

				IOUtilities.extractFileWithoutPath(imageFileName));

			if (!newPictureFile.exists()) // do not create file if already exists
			{
				newPictureFile.createNewFile();

				FileOutputStream fos = new FileOutputStream(newPictureFile);

				fos.write(buffer, 0, numberOfBytes);

				fos.close();
			}
			else
			{
				System.out.println("Warning: the following image: " +

					IOUtilities.extractFileWithoutPath(imageFileName) +

						" already existed!");
			}
		}
	}


	// OBTAINING EXAMINATION DATA

	/**
	 * Returns the total number of examinations available in the data
	 * source handled by this ExaminationDataHandler.
	 * @return the number of examinations.
	 */
	public synchronized int getExaminationCount() throws IOException
	{
		return this.getExaminationCount((ProgressNotifiable)null);
	}

	/**
	 * Returns the total number of examinations for the specified
	 * patient.
	 * @param pid PatientIdentifier
	 * @return int
	 * @throws IOException
	 */
	public int getExaminationCount(PatientIdentifier pid) throws IOException
	{
		return this.getExaminations(pid).length; // for now...
	}

	/**
	 * Returns the total number of examinations available in the data
	 * source handled by this ExaminationDataHandler. This version of
	 * the method takes a progress notifiable as this operation might
	 * take some time in some cases.
	 * @param not ProgressNotifiable
	 * @return int
	 * @throws IOException
	 */
	private synchronized int getExaminationCount(ProgressNotifiable not) throws IOException
	{
		if (!locationsAreValid())
		{
			throw new IOException("Locations not valid");
		}

		HashMap examinationMap = MVDCacheHandler.instance().getExaminationCache(mvdDirectory, not);

		int count = 0;

		Set patients = examinationMap.keySet();

		Iterator iter = patients.iterator();

		while (iter.hasNext())
		{
			count += ((Vector)examinationMap.get(iter.next())).size();
		}

		return count;
	}


	/**
	 * Retrieves an array of examination identifiers for
	 * the specified patient id. The locations have to be
	 * valid before the method is called, otherwise an
	 * exception will be thrown. The locations are made
	 * valid by setting the examination data location
	 * to a valid MVD database.
	 * @param pid PatientIdentifier
	 * @return ExaminationIdentifier[]
	 * @throws IOException
	 */
	public synchronized final ExaminationIdentifier[] getExaminations(PatientIdentifier pid) throws IOException
	{
		if (!locationsAreValid())
		{
			throw new IOException("MVDHandler - locations are invalid");
		}

		int invalidDateCtr = 0;

		// Get examination files for this PID (from the cache)
		File[] files = getPatientFiles(pid);

		ExaminationIdentifier[] idRetArr = new ExaminationIdentifier[files.length];

		for (int ctr = 0; ctr < files.length; ctr++)
		{
			String currLine = null;

			/* in order to construct ExaminationIdentifier, we need the date from
			   INSIDE the tree file, not the date in the file name, so we have to
			   read the file.

			   TODO: Caching this data instead of reading it every time would increase performance.
			   */

			FileInputStream fileInputStream = new FileInputStream(files[ctr]);

			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, LATIN_ENC);

			BufferedReader currBufferedReader = new BufferedReader(inputStreamReader);

			while ((currLine = currBufferedReader.readLine()) != null)
			{
				if (currLine.startsWith(DATUM_NODE_NAME))
				{
					// we found the date node

					String dateNode = MedViewUtilities.extractNodeValue(currBufferedReader.readLine(),

						currBufferedReader);

					try
					{
						Date currDate = dateParser.extractDate(dateNode);

						idRetArr[ctr] = new MedViewExaminationIdentifier(pid, currDate);
					}
					catch (CouldNotParseDateException e)
					{
						invalidDateCtr++;
					}

					break ; // no need to continue while loop

				}
			}

			currBufferedReader.close(); // NE 0401112
		}

		if (invalidDateCtr != 0)
		{
			ExaminationIdentifier[] old = idRetArr;

			idRetArr = new ExaminationIdentifier[files.length - invalidDateCtr];

			for (int ctr = 0; ctr < idRetArr.length; ctr++)
			{
				idRetArr[ctr] = old[ctr];
			}

			System.out.println("Warning: invalid dates found (" + invalidDateCtr + ")");
		}

		return idRetArr;
	}


	/**
	 * Not used anymore.
	 */
	public synchronized final ExaminationIdentifier[] refreshExaminations() throws IOException
	{
		return new ExaminationIdentifier[0];
	}

	/**
	 * Not used anymore
	 */
	public ExaminationIdentifier[] refreshExaminations(long sinceTime) throws IOException
	{
		return new ExaminationIdentifier[0];
	}


	// OBTAINING IMAGES

	/**
	 * Returns an array of examination images that were taken
	 * during the specified examination. The locations have to
	 * be valid before the method is called, otherwise an
	 * exception will be thrown. The locations are made valid
	 * by setting the examination data location to a valid MVD
	 * database. Will return an empty ExaminationImage array if
	 * there are no images taken for the specified examination.
	 *
	 * @param id ExaminationIdentifier
	 * @return ExaminationImage[]
	 * @throws IOException
	 */
	public synchronized final ExaminationImage[] getImages(ExaminationIdentifier id) throws IOException
	{
		String[] relativePaths = getRelativeImagePaths(id);

		ExaminationImage[] retArr = new ExaminationImage[relativePaths.length];

		for (int i = 0; i < relativePaths.length; i++)
		{
			File file = new File(mvdDirectory.getPath() + File.separator + relativePaths[i]);

			retArr[i] = new FileExaminationImage(id, file);
		}

		return retArr;
	}

	/**
	 * Help method which gets the relative image paths for all
	 * images belonging to examination id.
	 *
	 * @param id ExaminationIdentifier
	 * @return String[]
	 * @throws IOException
	 */
	private synchronized final String[] getRelativeImagePaths(ExaminationIdentifier id) throws IOException
	{
		if (!locationsAreValid())
		{
			throw new IOException("Locations not valid");
		}
		else
		{
			try
			{
				ExaminationValueContainer cont = getExaminationValueContainer(id, OPTIMIZE_FOR_EFFICIENCY);

				String[] rawImagePaths = null;

				try
				{
					rawImagePaths = cont.getValues(PHOTO_TERM_NAME);
				}
				catch (NoSuchTermException exc)
				{
					// there are no photos in this examination

					return new String[0];
				}

				// photo node contained values

				String[] retArr = new String[rawImagePaths.length];

				for (int ctr = 0; ctr < rawImagePaths.length; ctr++)
				{
					String curr = rawImagePaths[ctr];

					String rel = photoParser.extractRelativeLocation(curr);

					retArr[ctr] = rel;
				}

				return retArr;
			}
			catch (Exception e)
			{
				e.printStackTrace();

				throw new IOException(e.getMessage());
			}
		}
	}

	/**
	 * Returns the number of images that have been taken for
	 * the specified patient. The locations have to be valid
	 * before the method is called, otherwise an exception
	 * will be thrown. The locations are made valid by setting
	 * the examination data location to a valid MVD database.
	 *
	 * @param pid PatientIdentifier
	 * @return int
	 * @throws IOException
	 */
	public synchronized final int getImageCount(PatientIdentifier pid) throws IOException
	{
		if (!locationsAreValid())
		{
			throw new IOException("Locations not valid");
		}
		else
		{
			try
			{
				int imageCtr = 0;

				ExaminationIdentifier[] ids = getExaminations(pid);

				for (int ctr = 0; ctr < ids.length; ctr++)
				{
					ExaminationValueContainer cont = getExaminationValueContainer(ids[ctr], OPTIMIZE_FOR_EFFICIENCY);

					try
					{
						String[] rawImagePaths = cont.getValues(PHOTO_TERM_NAME);

						imageCtr += rawImagePaths.length;
					}
					catch (NoSuchTermException exc)
					{
						// do not increase counter if no photo node existant
					}
				}

				return imageCtr;
			}
			catch (Exception e)
			{
				throw new IOException(e.getMessage());
			}
		}
	}


	// OBTAINING EXAMINATION VALUES

	/**
	 * Obtain the tree file corresponding to the specified identifier.
	 * @param id ExaminationIdentifier
	 * @return File
	 * @throws IOException
	 * @throws NoSuchExaminationException
	 */
	public synchronized final File getExaminationFile(ExaminationIdentifier id) throws IOException, NoSuchExaminationException
	{
		if (!locationsAreValid())
		{
			throw new IOException("Locations not valid");
		}

		File[] files = getPatientFiles(id.getPID());

		if (files.length == 0)
		{
			String message = "No examinations for '" + id.getPID() + "'";

			throw new NoSuchExaminationException(message);
		}

		GregorianCalendar calendar = new GregorianCalendar();

		patientFileLoop:
		for (int ctr = 0; ctr < files.length; ctr++)
		{
			FileInputStream fileInputStream = new FileInputStream(files[ctr]);

			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, LATIN_ENC);

			BufferedReader currBufferedReader = new BufferedReader(inputStreamReader);

			String currLine = null;

			while ((currLine = currBufferedReader.readLine()) != null)
			{
				if (currLine.startsWith(DATUM_NODE_NAME))
				{
					String dateNode = MedViewUtilities.extractNodeValue(currBufferedReader.readLine(), currBufferedReader);

					try
					{
						calendar.setTime(dateParser.extractDate(dateNode));
					}
					catch (CouldNotParseDateException e)
					{
						e.printStackTrace();

						currBufferedReader.close(); // NE 041112

						continue patientFileLoop; // skip to next patient file
					}

					if ((calendar.get(Calendar.YEAR) == id.getYear()) &&

					    (calendar.get(Calendar.MONTH) == id.getMonth() - 1) &&

					    (calendar.get(Calendar.DAY_OF_MONTH) == id.getDay()) &&

					    (calendar.get(Calendar.HOUR_OF_DAY) == id.getHour()) &&

					    (calendar.get(Calendar.MINUTE) == id.getMinute()) &&

					    (calendar.get(Calendar.SECOND) == id.getSecond()))
					{
						// we found the tree file corresponding to the examination identifier

						currBufferedReader.close();

						return files[ctr];
					}
					else
					{
						currBufferedReader.close();

						continue patientFileLoop;
					}
				}
			}

			// we shouldn't get here, since all tree files should have a date node!

			currBufferedReader.close();
		}

		// if we reach this statement, the tree file wasn't found, which is very weird

		String message = "None of the examinations for '" + id.getPID() + "' matched '" + id.getExaminationIDString() + "'";

		throw new NoSuchExaminationException(message);
	}

	/**
	 * Major method (called from many methods above) that
	 * returns an array of the tree files for the
	 * specified patient. Returns an empty array if the
	 * specified patient did not have any corresponding
	 * tree files.
	 *
	 * @param pid PatientIdentifier
	 * @return File[]
	 * @throws IOException
	 */
	private File[] getPatientFiles(PatientIdentifier pid) throws IOException // returns paths to patient tree files
	{
		HashMap cacheMap = MVDCacheHandler.instance().getExaminationCache(mvdDirectory, null);

		// check if file map contains the patient, if not return empty array

		if (!cacheMap.containsKey(pid))
		{
			return new File[0];
		}

		// return patient's tree files

		Vector vect = (Vector) cacheMap.get(pid); // if contains key -> then safe cast...

		File[] retArr = new File[vect.size()];

		Enumeration enm = vect.elements();

		int ctr = 0;

		File mvdForestDirectory = new File(mvdDirectory, MVD_FOREST_SUBDIRECTORY);

		while (enm.hasMoreElements())
		{
			retArr[ctr++] = new File(mvdForestDirectory, (String) enm.nextElement());
		}

		return retArr;
	}

	public synchronized final ExaminationValueContainer[] getAllExaminationValueContainers(ProgressNotifiable not) throws IOException
	{
		return getAllExaminationValueContainers(not, MedViewDataConstants.OPTIMIZE_FOR_MEMORY);
	}

	public synchronized final ExaminationValueContainer[] getAllExaminationValueContainers(ProgressNotifiable not, int hint) throws IOException
	{
		try
		{
			PatientIdentifier[] pids = getPatients(not);

			if (not != null)
			{
				not.setTotal(pids.length);

				not.setDescription("Obtaining value containers"); // TODO - language
			}

			Vector evcVector = new Vector();

			for (int ctr = 0; ctr < pids.length; ctr++)
			{
				if (not != null)
				{
					not.setCurrent(ctr);
				}

				ExaminationIdentifier[] eids = getExaminations(pids[ctr]);

				for (int ctr2 = 0; ctr2 < eids.length; ctr2++)
				{
					evcVector.add(getExaminationValueContainer(eids[ctr2], hint)); // -> NoSuchExaminationException
				}
			}

			ExaminationValueContainer[] retArr = new ExaminationValueContainer[evcVector.size()];

			evcVector.toArray(retArr);

			return retArr;
		}
		catch (InvalidHintException exc)
		{
			exc.printStackTrace();

			System.exit(1); // critical error

			return null; // unreachable but compiler don't know this
		}
		catch (NoSuchExaminationException exc)
		{
			exc.printStackTrace();

			System.exit(1); // critical error

			return null; // unreachable but compiler don't know this
		}
	}

	/**
	 * Obtain a container containing all values for the specified
	 * examination. If the examination is non-existant in the currently
	 * set location, an exception will be thrown. The locations have to
	 * be valid before the method is called, otherwise an exception will
	 * be thrown. The locations are made valid by setting the examination
	 * data location to a valid MVD database.
	 * @param id ExaminationIdentifier
	 * @return ExaminationValueContainer
	 * @throws IOException
	 * @throws NoSuchExaminationException
	 * @throws InvalidHintException
	 */
	public synchronized final ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id) throws
		IOException, NoSuchExaminationException
	{
		try
		{
			return getExaminationValueContainer(id, MedViewDataConstants.OPTIMIZE_FOR_EFFICIENCY);
		}
		catch (InvalidHintException exc)
		{
			exc.printStackTrace();

			throw new IOException("invalid hint");
		}
	}

	/**
	 * Obtain a container containing all values for the specified
	 * examination. If the examination is non-existant in the currently
	 * set location, an exception will be thrown. The locations have to
	 * be valid before the method is called, otherwise an exception will
	 * be thrown. The locations are made valid by setting the examination
	 * data location to a valid MVD database.
	 * @param id ExaminationIdentifier
	 * @param hint int
	 * @return ExaminationValueContainer
	 * @throws IOException
	 * @throws NoSuchExaminationException
	 * @throws InvalidHintException
	 */
	public synchronized final ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id, int hint) throws
		IOException, NoSuchExaminationException, InvalidHintException
	{
		File file = getExaminationFile(id);

		FileInputStream fileInputStream = new FileInputStream(file);

		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, LATIN_ENC);

		BufferedReader currBufferedReader = new BufferedReader(inputStreamReader);

		return MedViewUtilities.constructExaminationValueContainer(id, currBufferedReader, hint);
	}


	// OBTAINING PATIENT LISTINGS

	/**
	 * Obtains an array of all the patients found in the
	 * currently set data location. The locations have to
	 * be valid before the method is called, otherwise an
	 * exception will be thrown. The locations are made valid
	 * by setting the examination data location to a valid MVD
	 * database.
	 */
	public synchronized final PatientIdentifier[] getPatients() throws IOException
	{
		return getPatients(null);
	}

	/**
	 * Obtains an array of all the patients found in the
	 * currently set data location. The locations have to
	 * be valid before the method is called, otherwise an
	 * exception will be thrown. The locations are made valid
	 * by setting the examination data location to a valid MVD
	 * database. This method also takes a progress notifiable
	 * to deliver notification of progress to.
	 *
	 * @param notifiable ProgressNotifiable
	 * @return PatientIdentifier[]
	 * @throws IOException
	 */
	public synchronized final PatientIdentifier[] getPatients(ProgressNotifiable notifiable) throws IOException
	{
		if (!locationsAreValid())
		{
			throw new IOException("Locations are invalid");
		}

		HashMap cacheMap = MVDCacheHandler.instance().getExaminationCache(mvdDirectory, notifiable);

		Set kS = cacheMap.keySet();

		if (notifiable != null)
		{
			notifiable.setTotal(kS.size());

			String lS = OTHER_OBTAINING_PATIENTS_LS_PROPERTY;

			notifiable.setDescription(MedViewDataHandler.instance().getLanguageString(lS));
		}

		PatientIdentifier[] retArr = new PatientIdentifier[kS.size()];

		if (notifiable != null)
		{
			notifiable.setTotal(retArr.length);
		}

		Iterator iter = kS.iterator();

		int ctr = 0;

		while (iter.hasNext())
		{
			if (notifiable != null)
			{
				notifiable.setCurrent(ctr + 1);
			}

			retArr[ctr++] = (PatientIdentifier)iter.next();
		}

		return retArr;
	}


	// SETTING AND GETTING DATA LOCATIONS

	/**
	 * Obtain the currently set examination data
	 * location, or a null value if is has not
	 * yet been set.
	 *
	 * @return String
	 */
	public synchronized String getExaminationDataLocation()
	{
		if (mvdDirectory != null)
		{
			return mvdDirectory.getPath();
		}
		else
		{
			return null;
		}
	}

	/**
	 * Sets the current examination data location to
	 * the specified location. Will try to construct
	 * the pathnames etc within the location's MVD
	 * structure. If this cannot be done, subsequent
	 * calls to methods will throw IOExceptions stating
	 * that the location was invalid. The method is
	 * internally synchronized to avoid retrieval
	 * deadlock on event notification. Will cause events
	 * to be fired notifying of examination location
	 * change as well as examination location ID change.
	 *
	 * @param loc String
	 */
	public void setExaminationDataLocation(String loc)
	{
		if (loc != null) // check so not setting to already set loc
		{
			if (new File(loc).equals(mvdDirectory))
			{
				return; // trying to set a location that is already set
			}
		}

		synchronized (this)
		{
			trySetMVD(loc, false);
		}

		fireExaminationLocationChanged();

		fireExaminationLocationIDChanged();
	}

	/**
	 * Tries to obtain the ID of the currently set
	 * data location. This ID should be something
	 * more descriptive to a user of the application
	 * than the 'raw' location. For instance, a file
	 * path might be concatenated to display to the
	 * user only the last component of the path.
	 *
	 * @return String
	 */
	public synchronized String getExaminationDataLocationID()
	{
		String setPath = getExaminationDataLocation();

		if (setPath != null)
		{
			if (setPath.endsWith(File.separator))
			{
				setPath = setPath.substring(0, setPath.length() - 1);
			}

			return setPath.substring(setPath.lastIndexOf(File.separator) + 1);
		}
		else
		{
			return "Not Set";
		}
	}

	/**
	 * Returns whether or not the currently set
	 * examination data location is valid. If the
	 * location is not set, or if an MVD database
	 * structure was not recognized in it, this
	 * method will return false.
	 *
	 * @return boolean
	 */
	public synchronized boolean isExaminationDataLocationValid()
	{
		return locationsAreValid();
	}

	/**
	 * Returns whether or not the locations set for
	 * the various MVD paths are valid.
	 * @return boolean
	 */
	private boolean locationsAreValid()
	{
		return ((mvdDirectory != null) && mvdDirectory.exists());
	}

	/**
	 * Tries to set the MVD to the specified path,
	 * if the warning flag is set, a warning message
	 * is printed if the path was found to be invalid.
	 * The MVD directory will be created (along with its
	 * subdirectories) if it is non-existant.
	 *
	 * @param path String
	 * @param warning boolean
	 */
	private void trySetMVD(String path, boolean warning)
	{
		if (path != null)
		{
			// set (and create if non-existant) mvd location

			mvdDirectory = new File(path);

			if (!mvdDirectory.exists())
			{
				mvdDirectory.mkdir(); // create mvd directory if non-existant
			}

			// set (and create if non-existant) forest location

			File forestLocationFile = new File(mvdDirectory, MVD_FOREST_SUBDIRECTORY);

			if (!forestLocationFile.exists())
			{
				forestLocationFile.mkdir(); // create forest directory if non-existant
			}

			// set (and create if non-existant) pictures location

			File mvdPicturesDirectory = new File(mvdDirectory, MVD_PICTURES_SUBDIRECTORY);

			File mvdPicturesPicturesDirectory = new File(mvdPicturesDirectory, MVD_PICTURES_SUBDIRECTORY);

			if (!mvdPicturesPicturesDirectory.exists())
			{
				mvdPicturesPicturesDirectory.mkdir(); // create pictures directory if non-existant
			}

			return; // <- if we get here, then everything's ok, we return
		}

		if (warning) // <- if we get here, we couldn't set the mvd
		{
			System.out.println("Warning: '" + path + "' has an invalid MVD structure");
		}

		mvdDirectory = null;
	}


	// EXPORTING OF MVD

	/**
	 * Exports all examinations belonging to the specified patients
	 * to a new MVD structure.
	 *
	 * @param patientIdentifiers the identifiers of the patients
	 * to export.
	 * @param newMVDlocation the location of the MVD to export to.
	 * @param filter the examination content filter, which is applied
	 * to the data when exporting.
	 * @param allowPartialExport whether to allow partial exports or not.
	 * If partial export is not allowed, an IOException should be thrown
	 * if there is any overlap, no matter how small, between the target
	 * MVD and the examinations we're exporting.
	 */
	public int exportToMVD(PatientIdentifier[] patientIdentifiers, String newMVDlocation, ProgressNotifiable notifiable,

		ExaminationContentFilter filter, boolean allowPartialExport) throws IOException
	{
		// collect the examination identifiers to export

		Vector v = new Vector();

		notifiable.setIndeterminate(true);

		notifiable.setDescription("Export: Collecting examinations");

		for (int p = 0; p < patientIdentifiers.length; p++)
		{
			ExaminationIdentifier[] identifiers = getExaminations(patientIdentifiers[p]);

			for (int e = 0; e < identifiers.length; e++)
			{
				v.add(identifiers[e]);
			}
		}

		ExaminationIdentifier[] examinationIdentifierArray = new ExaminationIdentifier[v.size()];

		examinationIdentifierArray = (ExaminationIdentifier[])v.toArray(examinationIdentifierArray);

		return exportToMVD(examinationIdentifierArray, newMVDlocation, notifiable, filter, allowPartialExport);
	}

	/**
	 * Exports a set of examinations to a new MVD file.
	 * @param examinationIdentifiers the identifiers of the
	 * examinations to export.
	 *
	 * @param newMVDlocation the location of the MVD to export to.
	 * @param filter the examination content filter, which is applied
	 * to the data when exporting.
	 * @param allowPartialExport whether to allow partial exports or
	 * not. If partial export is not allowed, an IOException should be
	 * thrown if there is any overlap, no matter how small, between the
	 * target MVD and the examinations we're exporting.
	 */
	public int exportToMVD(ExaminationIdentifier[] examinationIdentifiers, String newMVDlocation, ProgressNotifiable notifiable,

		ExaminationContentFilter filter, boolean allowPartialExport) throws IOException
	{
		notifiable.setIndeterminate(true);

		if (!newMVDlocation.endsWith(File.separator))
		{
			newMVDlocation += File.separator;
		}

		/*
		 * If partial export is not allowed, the first thing we must
		 * do is make sure that none of the tree files or images exist
		 * in the destination MVD at all. If this is the case, no export
		 * will take place. (IOException will be thrown before the export
		 * starts). */

		File[] treeFiles = new File[examinationIdentifiers.length];

		for (int i = 0; i < treeFiles.length; i++)
		{
			try
			{
				treeFiles[i] = getExaminationFile(examinationIdentifiers[i]);
			}
			catch (NoSuchExaminationException nsee)
			{
				throw new IOException("exportMVD failed: Could not get tree file for examination " +

					examinationIdentifiers[i].toString());
			}
		}

		// create the destination paths

		String[] destinationPaths = new String[treeFiles.length];

		String forestPath = newMVDlocation + MVD_FOREST_SUBDIRECTORY + File.separator;

		for (int i = 0; i < destinationPaths.length; i++)
		{
			destinationPaths[i] = forestPath + treeFiles[i].getName();

			// throw exception if one of the destination files already exists and we do not allow partial export

			if (!allowPartialExport)
			{
				File destFile = new File(destinationPaths[i]);

				if (destFile.exists())
				{
					throw new IOException("Export failed: Examination [" + destFile.getName() +

						"] exists in target MVD, and partial export is not allowed.");
				}
			}
		}

		// get the photo source paths for all the examinations, and check them

		Vector relativeImagePathsVector = new Vector();

		for (int e = 0; e < examinationIdentifiers.length; e++)
		{
			String[] relativeImagePaths = getRelativeImagePaths(examinationIdentifiers[e]);

			for (int i = 0; i < relativeImagePaths.length; i++)
			{
				relativeImagePathsVector.add(relativeImagePaths[i]);
			}
		}

		String[] relativeImageSourcePaths = new String[relativeImagePathsVector.size()];

		relativeImageSourcePaths = (String[])relativeImagePathsVector.toArray(relativeImageSourcePaths);

		// create the corresponding destination paths, and check them if we don't allow partial exports

		String[] destinationImagePaths = new String[relativeImageSourcePaths.length];

		for (int i = 0; i < destinationImagePaths.length; i++)
		{
			destinationImagePaths[i] = newMVDlocation + relativeImageSourcePaths[i];

			if (!allowPartialExport)
			{
				if (new File(destinationImagePaths[i]).exists())
				{
					throw new IOException("Export failed: Destination image [" + destinationImagePaths[i] +

						"] exists, and partial export is not allowed.");
				}
			}
		}

		// --- ACTUAL EXPORTING STARTS HERE ---

		// now we have checked all destination tree file paths and destination image paths, so export should be OK

		// create the new MVD

		File forestDirFile = new File(forestPath);

		if (!forestDirFile.exists())
		{
			forestDirFile.mkdirs(); // create the forest directory if not existant
		}

		// create the Pictures dir in the new MVD

		String picturesPath = newMVDlocation + MVD_PICTURES_SUBDIRECTORY + File.separator +

			MVD_PICTURES_SUBDIRECTORY + File.separator; // Fredrik 041115

		File picturesFile = new File(picturesPath);

		if (!picturesFile.exists())
		{
			picturesFile.mkdirs();
		}

		// copy all tree files to the forest dir

		notifiable.setIndeterminate(false);

		notifiable.setTotal(treeFiles.length);

		notifiable.setDescription("Export: Copying tree files");

		int exportedExaminations = 0; // Export count

		for (int i = 0; i < treeFiles.length; i++)
		{
			notifiable.setCurrent(i);

			String newTreeFilePath = destinationPaths[i];

			File destinationTreeFile = new File(newTreeFilePath);

			if (destinationTreeFile.exists())
			{
				if (!allowPartialExport) // partial export forbidden
				{
					// this should NEVER happen, due to the check above

					throw new IOException("ExportMVD: MAJOR ERROR (this should never happen):" +

						"Partial export not allowed, and export destination file existed, " +

							"even though we checked before");
				}
			}
			else // target file does not exist, copy OK
			{
				BufferedReader reader = new BufferedReader(new FileReader(treeFiles[i]));

				BufferedWriter writer = new BufferedWriter(new FileWriter(destinationTreeFile));

				exportNode(reader, writer, filter); // Works recursively

				writer.flush();

				reader.close();

				writer.close();

				exportedExaminations++;
			}
		}

		// copy the images included in the vector

		notifiable.setDescription("Export: Copying pictures");

		notifiable.setCurrent(0);

		notifiable.setTotal(relativeImageSourcePaths.length);

		int progress = 0;

		for (int i = 0; i < relativeImageSourcePaths.length; i++)
		{
			String nextRelativeImagePath = relativeImageSourcePaths[i];

			String sourceImagePath = mvdDirectory.getPath() + File.separator + nextRelativeImagePath;

			String destinationImagePath = destinationImagePaths[i];

			File sourceImageFile = new File(sourceImagePath);

			if (sourceImageFile.exists())
			{
				// check the destination file

				File destinationImageFile = new File(destinationImagePath);

				if (destinationImageFile.exists())
				{
					// destination image exists

					if (!allowPartialExport)
					{
						// this should NEVER happen, due to the check above

						throw new IOException("ExportMVD: MAJOR ERROR (this should never happen):" +

							"Partial export not allowed, and export destination image file existed, " +

							"even though we checked before.\n" +

							"Source image file: " + sourceImagePath + "\n" +

							"Destination image file: " + destinationImageFile.getPath()

							);
					}
					else
					{
						// since partial export is allowed, Do nothing except skip the image file
					}
				}
				else // destination image file does NOT exist
				{
					destinationImageFile.getParentFile().mkdirs(); // make sure the dir going to contain it exists

					InputStream istream = new FileInputStream(sourceImageFile);

					FileOutputStream ostream = new FileOutputStream(destinationImageFile);

					misc.foundation.io.IOUtilities.copy(istream, ostream); // copy the image (via the streams)

					istream.close();

					ostream.close();
				}
			}
			else
			{
				System.err.println("Warning: Tried to export image file " + sourceImageFile.getPath() +

					", which didn't exist");
			}

			progress++;

			notifiable.setCurrent(progress);
		}

		return exportedExaminations; // done
	}

	/**
	 * Recursive method used for exporting a tree file while filtering it.
	 * Calling this with a <code>null</code>BufferedWriter skips the node completely.
	 * @param the content filter. Passing <code>null</code> disables filtering.
	 * @param writer the output writer. Passing <code>null</code> disables exporting.
	 * (This is used to process parts of the tree recursively but not actually writing
	 * them (i.e if it is filtered)).
	 */
	private int exportNode(BufferedReader reader, BufferedWriter writer, ExaminationContentFilter filter) throws IOException
	{
		if (!reader.ready()) // No more to read
		{
			return 0;
		}
		else
		{
			String nextLine = reader.readLine();

			if (((writer != null) && (MedViewUtilities.isNode(nextLine)) && (filter != null))) // then check if allowed node
			{
				String nodeName = MedViewUtilities.extractNodeValue(nextLine, reader);

				if (!filter.acceptTerm(nodeName))
				{
					System.out.println("Note: term " + nodeName + " was filtered out");

					writer = null; // do not export anything here
				}
			}

			// process "this node"

			if (writer != null)
			{
				writer.write(nextLine + "\n");
			}

			int depth = 1 - MedViewUtilities.extractTerminates(nextLine);

			while ((depth > 0) && (reader.ready()))
			{
				int depthChange = exportNode(reader, writer, filter);

				depth = depth + depthChange;
			}

			return depth;
		}
	}
}
