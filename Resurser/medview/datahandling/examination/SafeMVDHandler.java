/*
 * SafeMVDHandler.java
 *
 * Created on October 31, 2002, 2:32 PM
 *
 * $Id: SafeMVDHandler.java,v 1.23 2006/04/24 14:17:38 lindahlf Exp $
 *
 * $Log: SafeMVDHandler.java,v $
 * Revision 1.23  2006/04/24 14:17:38  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.22  2005/09/09 15:40:49  lindahlf
 * Server cachning
 *
 * Revision 1.21  2005/01/30 15:22:08  lindahlf
 * T4 Integration
 *
 * Revision 1.20  2004/11/19 12:32:30  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.19  2004/11/10 13:08:14  erichson
 * added getExaminationCount() impl
 *
 * Revision 1.18  2004/11/09 21:13:50  lindahlf
 * Datalayer upgrade in connection with server development
 *
 * Revision 1.17  2004/11/06 01:10:59  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.16  2004/11/03 12:38:10  erichson
 * Changed exportToMVD method signature.
 *
 * Revision 1.15  2004/10/23 15:34:15  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.14  2004/10/21 12:26:28  erichson
 * added progressNotifiable to exportToMVD.
 *
 * Revision 1.13  2004/10/06 13:56:25  erichson
 * Updated with exportToMVD calls
 *
 * Revision 1.12  2004/08/24 18:25:13  lindahlf
 * no message
 *
 * Revision 1.11  2004/01/21 13:44:07  erichson
 * Updated to use new DataHandling version (with pid support)
 *
 * Revision 1.10  2003/09/09 18:17:33  erichson
 * Fixed small bug which would happen if an image lacks extension
 *
 * Revision 1.9  2003/07/02 16:10:00  erichson
 * Removed debug output
 *
 * Revision 1.8  2003/07/02 10:36:51  erichson
 * Removed unneccesary throwing of IOException when photo term does not exist (which seems to be common with older tree files)
 *
 * Revision 1.7  2003/04/12 13:08:06  oloft
 * Added dummy refreshExaminations method.
 *
 * Revision 1.6  2002/12/06 14:35:28  erichson
 * Small optimizations
 *
 * Revision 1.5  2002/12/06 01:03:31  lindahlf
 * Added notifiable support
 *
 * Revision 1.4  2002/11/22 16:51:13  erichson
 * Small fix to fixImagePaths(..) since it did not treat \ and / the same
 *
 * Revision 1.3  2002/11/21 16:11:17  erichson
 * Added image handling support
 *
 * Revision 1.2  2002/10/31 15:12:49  erichson
 * Now uses CachingTreeFileHandler rather than regular TreeFileHandler
 *
 * Revision 1.1  2002/10/31 14:37:32  erichson
 * First check-in
 *
 */

package medview.datahandling.examination;

import java.io.*;
import java.util.*; // StringTokenizer
import java.awt.Image;
import java.awt.image.*;

import javax.imageio.*;

import medview.datahandling.*; // NoSuchTermException etc...
import medview.datahandling.examination.filter.*;
import medview.datahandling.examination.tree.*;
import medview.datahandling.images.*; // ImageIdentifier

import misc.foundation.*; // Fredrik 021205 (notifiable support)...

/**
 * ExaminationDataHandler implementation for an MVD directory.
 * This MVD handler is implemented on top of a TreeFileHandler which handles the Forest.forest directory.
 * This implementation was created because MVDHandler lacks necessary error handling (exceptions).
 * Thus the name 'Safe' MVDHandler.
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.0
 */
public class SafeMVDHandler implements ExaminationDataHandler,
	MedViewDataConstants
{

	/* Fields */

	private String MVDPath;

	private final ExaminationDataHandler treeFileHandler;

	// private Vector examinationDataHandlerListeners = new Vector();

	/* Constants */



	private final static String FOREST_DIRECTORY_NAME = "Forest.forest";

	private final static String PICTURES_DIRECTORY_NAME = "Pictures";

	private final static String PHOTO_TERM_NAME = "Photo";

	private final static String fileSep = System.getProperty("file.separator");

	/* Constructors */

	/** Creates a new instance of SafeMVDHandler */
	public SafeMVDHandler(String path) throws InvalidDataLocationException
	{
		treeFileHandler = new CachingTreeFileHandler();
		//System.out.println("Creating new SafeMVDHandler with location " + path);
		setExaminationDataLocation(path);
	}


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


	/* Methods */

	/** Returns the set examination data location
	 * in it's 'raw' form. For example, it could
	 * return 'c:\medview\databases\MSTest.mvd'.
	 */
	public String getExaminationDataLocation()
	{
		return MVDPath;
	}

	/** Returns the data location expressed in a
	 * (perhaps) more simple way than the 'raw'
	 * data location. For example, it could
	 * return 'MSTest.mvd' instead of the full
	 * file path to the mvd.
	 */
	public String getExaminationDataLocationID()
	{
		return new File(MVDPath).getName();
	}

	public synchronized final ExaminationValueContainer[] getAllExaminationValueContainers(ProgressNotifiable not) throws
		IOException
	{
		return getAllExaminationValueContainers(not, MedViewDataConstants.OPTIMIZE_FOR_MEMORY);
	}

	public synchronized final ExaminationValueContainer[] getAllExaminationValueContainers(ProgressNotifiable not, int hint) throws
		IOException
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

	/** Creates an instance of an ExaminationValueContainer
	 * for a specified examination. Will return null if
	 * there is no examination value container for the
	 * specified parameters.
	 */
	public medview.datahandling.examination.ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id) throws
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

	/** Creates an instance of an ExaminationValueContainer
	 * for a specified examination. Will return null if
	 * there is no examination value container for the
	 * specified parameters.
	 */
	public medview.datahandling.examination.ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id, int hint) throws
		IOException, NoSuchExaminationException, InvalidHintException
	{
		return treeFileHandler.getExaminationValueContainer(id, hint);
	}

	public ExaminationIdentifier[] getExaminations(PatientIdentifier patientCode) throws IOException
	{
		return treeFileHandler.getExaminations(patientCode);
	}


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
	 */
	public ExaminationIdentifier[] refreshExaminations() throws IOException
	{
		return null; // NOT IMPLEMENTED HERE
	}

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
	 */
	public ExaminationIdentifier[] refreshExaminations(long sinceTime) throws IOException
	{
		return null; // NOT IMPLEMENTED HERE
	}


	public int getImageCount(PatientIdentifier patientIdentifier) throws IOException
	{
		return treeFileHandler.getImageCount(patientIdentifier);
	}

	/**
	 * Returns an array of all the patients listed at some location.
	 */
	public PatientIdentifier[] getPatients() throws IOException
	{
		return treeFileHandler.getPatients();
	}

	public PatientIdentifier[] getPatients(ProgressNotifiable notifiable) throws IOException
	{
		return this.getPatients(); // Fredrik 021205 (notifiable support)...
	}

	/**
	 * Saves a Tree of examination data to the database.
	 *  @param tree a Tree of examination data to be saved to the database
	 *  @throws IOException if some error occurs during saving.
	 */
	public void saveTree(medview.datahandling.examination.tree.Tree tree) throws IOException
	{
		//treeFileHandler.saveTree(tree);
		throw new IOException("saveTree so far not implemented in safeMVDhandler");
	}

	/** Sets the location of the examination data.
	 * For example - for a MVD handler, the location
	 * could be 'c:\medview\databases\MSTest.mvd',
	 * while for a SQL handler could be something like
	 * 'login:password@server.myip.org:1024'.
	 */
	public void setExaminationDataLocation(String newLocation)
	{

		if (newLocation.equals(MVDPath))
		{
			System.out.println("SafeMVDhandler was already at set location");
		}
		else
		{

			System.out.println("SafeMVDhandler changing location...");


			// moved checks to isExaminationDataLocationValid


			MVDPath = newLocation;

			String treeFileDirPath = newLocation + fileSep + FOREST_DIRECTORY_NAME;

			System.out.println("Tree file dir path = " + treeFileDirPath);
			treeFileHandler.setExaminationDataLocation(treeFileDirPath);
			//fireExaminationDataLocationChanged();
		}
	}

	public boolean isExaminationDataLocationValid()
	{
		try
		{
			checkExaminationPathValidity();
		}
		catch (InvalidDataLocationException idle)
		{
			return false;
		}
		// Otherwise OK
		return true;
	}

	private void checkExaminationPathValidity() throws InvalidDataLocationException
	{

		File mvdDir = new File(MVDPath);

		if (!mvdDir.exists())
		{
			throw new InvalidDataLocationException("MVD directory [" + MVDPath + "] does not exist");
		}

		if (!mvdDir.isDirectory())
		{
			throw new InvalidDataLocationException("[" + MVDPath + "] is not a directory!");
		}

		// Check files in the directory
		File[] files = mvdDir.listFiles();

		boolean forestExists = false;
		boolean picturesExists = false;
		for (int i = 0; i < files.length; i++)
		{
			String fileName = files[i].getName();
			if (fileName.equals(FOREST_DIRECTORY_NAME))
			{
				if (files[i].isDirectory())
				{
					forestExists = true;
				}
				else
				{
					throw new InvalidDataLocationException(FOREST_DIRECTORY_NAME + " in the MVD is not a directory!");
				}
			}
			else if (fileName.equals(PICTURES_DIRECTORY_NAME))
			{
				if (files[i].isDirectory())
				{
					picturesExists = true;
				}
				else
				{
					throw new InvalidDataLocationException(PICTURES_DIRECTORY_NAME + " in the MVD is not a directory!");
				}

			}
		}

		if (!forestExists)
		{
			throw new InvalidDataLocationException("Error: Could not find forest directory (" + FOREST_DIRECTORY_NAME +
				") in MVD directory " + MVDPath);
		}
		else if (!picturesExists)
		{
			throw new InvalidDataLocationException("Error: Could not find pictures directory (" + PICTURES_DIRECTORY_NAME +
				") in MVD directory " + MVDPath);
		}

		// All checks OK
	}

	/**
	 * Get images for all available images associated with an examination
	 * @param id the Examination whose images to get
	 * @return an array of ExaminationImages
	 */
	public ExaminationImage[] getImages(ExaminationIdentifier id) throws IOException, NoSuchExaminationException
	{
		// fetch image paths and fix them
		try
		{
			ExaminationValueContainer evc = getExaminationValueContainer(id, OPTIMIZE_FOR_EFFICIENCY);

			String[] imagePaths = evc.getValues(PHOTO_TERM_NAME); // get from photo term
			fixImagePaths(imagePaths);

			FileExaminationImage[] images = new FileExaminationImage[imagePaths.length];

			for (int i = 0; i < imagePaths.length; i++)
			{
				images[i] = new FileExaminationImage(id, new File(imagePaths[i]));
			}

			return images;

		}
		catch (NoSuchTermException nste)
		{
			// throw new IOException("Could not locate photo term [" + PHOTO_TERM_NAME + "]: " + nste.getMessage());
			// Don't worry about it
			return new ExaminationImage[0];
		}
		catch (InvalidHintException exc)
		{
			exc.printStackTrace();

			return new ExaminationImage[0];

			// does not happen
		}
	}


	/**
	 * Fixes image paths to be relative to this datasource
	 */
	public void fixImagePaths(String[] imagePaths)
	{ // SHOULD BE PRIVATE
		for (int i = 0; i < imagePaths.length; i++)
		{

			char fileSepChar = fileSep.toCharArray()[0];

			imagePaths[i] = imagePaths[i].replace('\\', fileSepChar);
			imagePaths[i] = imagePaths[i].replace('/', fileSepChar);

			String picturesPath = getPicturesPath();

			//System.out.println("Fix image Paths: Old image path=" + imagePaths[i]);

			String relativePath = getPictureRelativeImagePath(imagePaths[i]);
			//System.out.println("relative part of current image: " + relativePath);
			imagePaths[i] = picturesPath + fileSep + relativePath;

			//System.out.println("Fix image Paths: new (relative) image path=" + imagePaths[i]);
		}
		// Get the datasource of
		// multiple elements may give the same pictures again...
	}

	/**
	 * Returns the part of a picture path relative to the "Pictures" part
	 * for example, "c:/mvd/pictures/g01/g01.jpg" returns "/g01/g01.jpg"
	 */
	private String getPictureRelativeImagePath(String oldPath)
	{

		/*
		  int index = -1;
		  System.out.println("oldpath=" + oldPath);

		  if (oldpath.toLowerCase().contains("pictures")) {
		    index = oldPath.toLowerCase().indexOf("pictures");
		  } else if (oldpath.toLowerCase().contains("bilder")) {


		  String fromFirstPictures = oldPath.substring(picturesPartIndex);
		  int firstSlashIndex = fromFirstPictures.indexOf('/');
		  String afterPictures = fromFirstPictures.substring(firstSlashIndex);
		  //String imagePath = dataSource.createPicturePath(afterPictures);
		  System.out.println("getPictureRelativeImagePath: old = " + oldPath);
		  System.out.println("getPictureRelativeImagePath: new = " + afterPictures);
		  return afterPictures;
		 */

		// Separate out one or two final dirs
		StringTokenizer tok = new StringTokenizer(oldPath, fileSep);

		String nextToLast = null;
		String last = null;


		while (tok.hasMoreTokens())
		{
			nextToLast = last;
			last = tok.nextToken();
		}

		String fileName = last;
		String directory = nextToLast;

		if (directory == null)
		{
			return fileName; // no path before it
		}

		// remove extension of the file name

		String fileNameWithoutExtension;
		int dotPosition = last.lastIndexOf('.');
		if (dotPosition < 0)
		{
			fileNameWithoutExtension = last;
		}
		else
		{
			fileNameWithoutExtension = last.substring(0, dotPosition); // will not include the dot
		}

		// System.out.println("directory = " + directory + ", fileNameWithoutExtension = " + fileNameWithoutExtension);

		/*
		  String relativePart;

		  if (directory.toUpperCase().equals(fileNameWithoutExtension.toUpperCase())) {
		    System.out.println("equal, keeping directory part");
		    relativePart = directory + fileSep + fileName;
		  } else { // not equal
		    relativePart =  fileName;
		  }
		  System.out.println("relativePart = " + relativePart);
		  return relativePart;
		 */

		String relativePart = directory + fileSep + fileName;

		String testPath = getPicturesPath() + fileSep + fileName;
		if (new java.io.File(testPath).exists())
		{
			return fileName;
		}

		testPath = getPicturesPath() + fileSep + relativePart;
		if (new java.io.File(testPath).exists())
		{
			return relativePart;
		}

		// If we reach this part, we could not find the image...
		System.out.println("DataSource: Could not find a matching image for " + relativePart);

		return relativePart;
	}

	private String getPicturesPath()
	{
		String picturePath = MVDPath + fileSep + "Pictures";
		//System.out.println("getPicturesPath() gave = " + path);
		return picturePath;
	}


	public int saveExamination(Tree tree, ExaminationImage[] imageArray) throws IOException
	{
		// Fire examination added

		return -1; // NOT IMPLEMENTED HERE
	}

	public int saveExamination(Tree tree, ExaminationImage[] imageArray, String location) throws IOException
	{

		return -1; // NOT IMPLEMENTED HERE
	}

	public void removeExamination(ExaminationIdentifier eid) throws IOException
	{
		// TODO: implement this
	}

	public void addExaminationDataHandlerListener(ExaminationDataHandlerListener edhl)
	{
		//examinationDataHandlerListeners.add(edhl);
		treeFileHandler.addExaminationDataHandlerListener(edhl);
	}

	public void removeExaminationDataHandlerListener(ExaminationDataHandlerListener edhl)
	{
		// examinationDataHandlerListeners.remove(edhl);
		treeFileHandler.removeExaminationDataHandlerListener(edhl);
	}

	public int exportToMVD(PatientIdentifier[] patientIdentifiers, String newMVDlocation, ProgressNotifiable notifiable,
			       ExaminationContentFilter filter, boolean allowPartialExport) throws IOException
	{
		return treeFileHandler.exportToMVD(patientIdentifiers, newMVDlocation, notifiable, filter, allowPartialExport);
	}

	public int exportToMVD(ExaminationIdentifier[] examinationIdentifiers, String newMVDlocation, ProgressNotifiable notifiable,
			       ExaminationContentFilter filter, boolean allowPartialExport) throws IOException
	{
		return treeFileHandler.exportToMVD(examinationIdentifiers, newMVDlocation, notifiable, filter, allowPartialExport);
	}

	public int getExaminationCount() throws IOException
	{
	    return treeFileHandler.getExaminationCount();
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
		return treeFileHandler.getExaminations(pid).length; // for now...
	}

	/*
	     private void fireExaminationDataLocationChanged() {
	  ExaminationDataHandlerEvent event = new ExaminationDataHandlerEvent(this);
	  event.setLocation( MVDPath);

	  for (Iterator it = examinationDataHandlerListeners.iterator(); it.hasNext();) {
	 ExaminationDataHandlerListener nextListener = (ExaminationDataHandlerListener) it.next();
	 nextListener.examinationLocationChanged(event);
	  }
	     }*/

}
