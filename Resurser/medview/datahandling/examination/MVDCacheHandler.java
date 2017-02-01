/**
 * @(#) MVDCacheHandler.java
 */

package medview.datahandling.examination;

import java.io.*;

import java.nio.channels.*;

import java.util.*;

import medview.common.data.*;
import medview.common.filefilter.*;

import medview.datahandling.*;

import misc.foundation.*;

/**
 * This singleton deals with the 'layer' on top of the actual
 * MVD, which provides quick access to commonly accessed data
 * in the MVD without having to dwelve into the actual tree
 * files.<br>
 * <br>
 * Currently, support for pid-pcode mappings as well as quickly
 * obtaining the file names for patients corresponding tree files
 * is implemented. Further caching support can be added in the
 * future.
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MVDCacheHandler implements MedViewDataConstants
{
	// MEMBERS

	private HashMap pidMap = new HashMap();

	private HashMap mvdMap = new HashMap();

	private long pidFileModificationDateAtLastMapConstruct = -1;

	private long mvdFileModificationDateAtLastMapConstruct = -1;

	private static MVDCacheHandler instance = new MVDCacheHandler();

	private static final String DEBUG_PREFIX = "MVDCacheHandler> ";

	private RandomAccessFile pidRandomAccessFile = null;

	private RandomAccessFile mvdRandomAccessFile = null;

	private FileLock pidCacheFileLock = null;

	private FileLock mvdCacheFileLock = null;


	// CONSTANTS

	private static final String MVD_MVD_CACHE_FILE_NAME = "mvdcache.cch";

	private static final String MVD_PID_CACHE_FILE_NAME = "pidcache.cch";

	private static final String MVD_CACHE_SUBDIRECTORY = "cache";

	private static final String TOKEN_DELIMITER = "|";

	private static final String NULL_STRING = "NULL";

	// CONSTRUCTOR

	private MVDCacheHandler() {} // defeat instantiation


	// SINGLETON

	/**
	 * Obtain a reference to the singleton cache handler object.
	 * @return MVDCacheHandler
	 */
	public static MVDCacheHandler instance()
	{
		return instance;
	}


	// REMOVAL

	public void removeFromExaminationCache( PatientIdentifier pid, String treeFileName, File mvdDirectory, ProgressNotifiable not ) throws IOException
	{
		File cacheFile = openAndLockMVDRandomAccessFile(mvdDirectory, not);

		synchronizeInternalMVDMapWithMVDCache(cacheFile, not); // -> IOException

		if (mvdMap.containsKey(pid))
		{
			Vector vect = (Vector) mvdMap.get(pid);

			if (vect.contains(treeFileName))
			{
				vect.remove(treeFileName);

				writeMVDMapIntoMVDMVDCache(not);
			}
			else
			{
				// the tree file name is not in the cache
			}
		}

		releaseLockAndCloseMVDRandomAccessFile();
	}


	// MVD-CACHE

	/**
	 * Adds a tree file name to the vector corresponding to the
	 * specified pid. The mvd directory file specifies which
	 * mvd is currently worked upon, and since this operation might
	 * take a little time, there is the option to provide a
	 * progress notifiable as well.
	 * @param pCode String
	 * @param treeFileName String
	 * @param mvdDirectory File
	 * @param not ProgressNotifiable
	 * @throws IOException
	 */
	public void addToExaminationCache( PatientIdentifier pid, String treeFileName, File mvdDirectory, ProgressNotifiable not ) throws IOException
	{
		if (pid == null)
		{
			return;
		}
		else
		{
			File cacheFile = openAndLockMVDRandomAccessFile(mvdDirectory, not);

			synchronizeInternalMVDMapWithMVDCache(cacheFile, not); // cache-file passed along for last-modification check

			if (mvdMap.containsKey(pid))
			{
				Vector vect = (Vector) mvdMap.get(pid);

				if (!vect.contains(treeFileName))
				{
					vect.add(treeFileName);

					writeMVDMapIntoMVDMVDCache(not);
				}
				else
				{
					// the tree file name is already noted in the vector
				}
			}
			else
			{
				Vector vect = new Vector();

				vect.add(treeFileName);

				mvdMap.put(pid, vect);

				writeMVDMapIntoMVDMVDCache(not);
			}

			releaseLockAndCloseMVDRandomAccessFile();
		}
	}

	/**
	 * Obtain the examination cache, which is a hash map containing
	 * PatientIdentifier objects as keys, which map to Vectors containing
	 * String objects identifying the tree file names corresponding to
	 * the patient identifier.
	 * @param mvdDirectory File
	 * @param not ProgressNotifiable
	 * @return HashMap
	 * @throws IOException
	 */
	public HashMap getExaminationCache(File mvdDirectory, ProgressNotifiable not) throws IOException
	{
		File cacheFile = openAndLockMVDRandomAccessFile(mvdDirectory, not);

		synchronizeInternalMVDMapWithMVDCache(cacheFile, not);

		releaseLockAndCloseMVDRandomAccessFile();

		return mvdMap;
	}


	// PID-CACHE

	/**
	 * Adds the pid-pcode mapping to the cache used in the specified
	 * mvd. The mappings here are not using PatientIdentifier objects,
	 * as in the mvd caching, but instead simple String->String mappings.
	 * @param pid String
	 * @param pCode String
	 * @param mvdDirectory File
	 * @param not ProgressNotifiable
	 * @throws IOException
	 */
	public void addToPIDCache( String pid, String pCode, File mvdDirectory, ProgressNotifiable not  ) throws IOException
	{
		if ((pid == null) || (pCode == null))
		{
			return;
		}
		else
		{
			File cacheFile = openAndLockPIDRandomAccessFile(mvdDirectory, not);

			synchronizeInternalPIDMapWithPIDCache(cacheFile, not);

			pidMap.put(pid, pCode);

			writePIDMapIntoMVDPIDCache(not);

			releaseLockAndClosePIDRandomAccessFile();
		}
	}

	/**
	 * Returns the p-code corresponding to the specified pid, or null
	 * if no such mapping exists.
	 * @param pid String
	 * @param mvdDirectory File
	 * @param not ProgressNotifiable
	 * @return String
	 * @throws IOException
	 */
	public String getCorrespondingPCode( String pid, File mvdDirectory, ProgressNotifiable not ) throws IOException
	{
		if (pid == null)
		{
			return null;
		}
		else
		{
			File cacheFile = openAndLockPIDRandomAccessFile(mvdDirectory, not);

			synchronizeInternalPIDMapWithPIDCache(cacheFile, not);

			releaseLockAndClosePIDRandomAccessFile();

			if (pidMap.containsKey(pid))
			{
				return (String) pidMap.get(pid);
			}
			else
			{
				return null;
			}
		}
	}


	// UTILITIES

	/**
	 * After this method has been called, the internal mvd map member
	 * corresponds to the contents of the specified mvd's mvd cache
	 * file. The method will create such a cache file if no such file
	 * exists for the specified mvd. The internal RAF object for the
	 * mvd cache file is still open after the method returns.
	 *
	 * NOTE: this method assumes that there exists and is open a
	 *       random access 'rw' file object corresponding to the
	 *       cache file. I.e. the corresponding open() method which
	 *       also should lock the file preventing parallell modification
	 *       should have been called prior to this method.
	 *
	 * @param mvdCacheFile File
	 * @param not ProgressNotifiable
	 * @throws IOException
	 */
	private void synchronizeInternalMVDMapWithMVDCache(File mvdCacheFile, ProgressNotifiable not) throws IOException
	{
		// read the cache file

		long currentLastModificationDate = mvdCacheFile.lastModified();

		if (currentLastModificationDate != mvdFileModificationDateAtLastMapConstruct)
		{
			// new things have happened since the last mvd map build

			mvdMap.clear();

			BufferedReader reader = new BufferedReader(new InputStreamReader(

				new ByteArrayInputStream(obtainMVDCacheFileData())));

			StringTokenizer tokenizer = null;

			String line = null, pCode = null, pid = null;

			PatientIdentifier currentPID = null;

			while ((line = reader.readLine()) != null) // -> IOException
			{
				tokenizer = new StringTokenizer(line, TOKEN_DELIMITER);

				pCode = tokenizer.nextToken();

				pid = tokenizer.nextToken();

				if (pid.equals(NULL_STRING))
				{
					currentPID = new PatientIdentifier(pCode);
				}
				else
				{
					currentPID = new PatientIdentifier(pCode, pid);
				}

				Vector vect = new Vector();

				while (tokenizer.hasMoreTokens())
				{
					vect.add(tokenizer.nextToken());
				}

				mvdMap.put(currentPID, vect);
			}

			reader.close();

			mvdFileModificationDateAtLastMapConstruct = currentLastModificationDate;
		}
		else
		{
			// the mvd map is up-to-date, no need to re-read it
		}
	}

	/**
	 * After this method has been called, the internal pid map member
	 * corresponds to the contents of the specified mvd's pid cache
	 * file. The method will create such a cache file if no such file
	 * exists for the specified mvd. The internal RAF object for the
	 * pid cache file is still open after the method returns.
	 *
	 * NOTE: this method assumes that there exists and is open a
	 *       random access 'rw' file object corresponding to the
	 *       cache file. I.e. the corresponding open() method which
	 *       also should lock the file preventing parallell modification
	 *       should have been called prior to this method.

	 * @param pidCacheFile File
	 * @param not ProgressNotifiable
	 * @throws IOException
	 */
	private void synchronizeInternalPIDMapWithPIDCache(File pidCacheFile, ProgressNotifiable not) throws IOException
	{
		// read the cache file

		long currentLastModificationDate = pidCacheFile.lastModified();

		if (currentLastModificationDate != pidFileModificationDateAtLastMapConstruct)
		{
			// new things have happened since the last mvd map build

			pidMap.clear();

			BufferedReader reader = new BufferedReader(new InputStreamReader(

				new ByteArrayInputStream(obtainPIDCacheFileData())));

			StringTokenizer tokenizer = null;

			String line = null;

			/* NOTE: there are only (non-null -> non-null) mappings present
			   in the internal pid map. */

			while ((line = reader.readLine()) != null) // -> IOException
			{
				tokenizer = new StringTokenizer(line, TOKEN_DELIMITER);

				pidMap.put(tokenizer.nextToken(), tokenizer.nextToken());
			}

			reader.close();

			pidFileModificationDateAtLastMapConstruct = currentLastModificationDate;
		}
		else
		{
			// the pid map is up-to-date, no need to re-read it
		}
	}

	/**
	 * This method is only called when the mvd cache file did not exist
	 * and needs to be built based upon the contents of the forest.
	 *
	 * @param mvdDirectory File
	 * @param mvdMVDCacheFile File
	 * @param not ProgressNotifiable
	 * @throws IOException
	 */
	private void createMVDCacheFile(File mvdDirectory, ProgressNotifiable not) throws IOException
	{
		mvdMap.clear();

		File mvdForestDirectory = new File(mvdDirectory, MVD_FOREST_SUBDIRECTORY);

		File[] treeFiles = mvdForestDirectory.listFiles(new TreeFileFilter()); // first build

		PatientIdentifier currentPID = null;

		for (int ctr=0; ctr<treeFiles.length; ctr++)
		{
			MedViewUtilities.PIDPCodePair pair = MedViewUtilities.parsePIDPCode(treeFiles[ctr]);

			currentPID = new PatientIdentifier(pair.getPCode(), pair.getPID());

			if ((currentPID == null) || ( (pair.getPCode() == null) && (pair.getPID() == null)))
			{
			    System.out.println("Warning: Could not get pid for file " + treeFiles[ctr].getPath() + ", skipping.");
			}
			else
			{
			    if (mvdMap.containsKey(currentPID))
			    {
				    Vector vect = (Vector) mvdMap.get(currentPID);

				    vect.add(treeFiles[ctr].getName());
			    }
			    else
			    {
				    Vector vect = new Vector();

				    vect.add(treeFiles[ctr].getName());

				    mvdMap.put(currentPID, vect);
			    }
			}
		}

		writeMVDMapIntoMVDMVDCache(not);
	}

	/**
	 * This method is only called when the pid cache file did not exist,
	 * and needs to be built based upon the contents of the forest.
	 *
	 * @param mvdDirectory File
	 * @param mvdPIDCacheFile File
	 * @param not ProgressNotifiable
	 * @throws IOException
	 */
	private void createPIDCacheFile(File mvdDirectory, ProgressNotifiable not) throws IOException
	{
		pidMap.clear();

		File mvdForestDirectory = new File(mvdDirectory, MVD_FOREST_SUBDIRECTORY);

		File[] treeFiles = mvdForestDirectory.listFiles(new TreeFileFilter()); // first build

		for (int ctr=0; ctr<treeFiles.length; ctr++)
		{
			MedViewUtilities.PIDPCodePair pair = MedViewUtilities.parsePIDPCode(treeFiles[ctr]);

			if ((pair.getPID() != null) && (pair.getPCode() != null))
			{
				pidMap.put(pair.getPID(), pair.getPCode());
			}
		}

		writePIDMapIntoMVDPIDCache(not);
	}

	/**
	 * Writes the internal mvd map into a cache file in the current mvd.
	 *
	 * @param mvdDirectory File
	 * @param not ProgressNotifiable
	 * @throws IOException
	 */
	private void writeMVDMapIntoMVDMVDCache(ProgressNotifiable not) throws IOException
	{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		PrintWriter writer = new PrintWriter(bout);

		Iterator iter = mvdMap.keySet().iterator();

		PatientIdentifier currentPID = null;

		while (iter.hasNext())
		{
			currentPID = (PatientIdentifier) iter.next();

			Vector vect = (Vector) mvdMap.get(currentPID);

			if (currentPID.getPID() == null)
			{
				writer.print(currentPID.getPCode() + TOKEN_DELIMITER +

					NULL_STRING + TOKEN_DELIMITER);
			}
			else
			{
				writer.print(currentPID.getPCode() + TOKEN_DELIMITER +

					currentPID.getPID() + TOKEN_DELIMITER);
			}

			if (vect.size() == 0)
			{
				// the vector contains no tree files (pcode / pid but no tree files with it - happens if remove)

				writer.println();
			}
			else
			{
				Enumeration enm = vect.elements();

				while (enm.hasMoreElements())
				{
					writer.print((String)enm.nextElement());

					if (enm.hasMoreElements())
					{
						writer.print(TOKEN_DELIMITER);
					}
					else
					{
						writer.println();
					}
			}
			}
		}

		writer.flush();

		setMVDCacheFileData(bout.toByteArray());

		writer.close();
	}

	/**
	 * Writes the internal pid map into a cache file in the specified mvd.
	 *
	 * @param mvdDirectory File
	 * @param not ProgressNotifiable
	 * @throws IOException
	 */
	private void writePIDMapIntoMVDPIDCache(ProgressNotifiable not) throws IOException
	{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		PrintWriter writer = new PrintWriter(bout);

		Iterator iter = pidMap.keySet().iterator();

		while (iter.hasNext())
		{
			String pid = (String) iter.next();

			String pCode = (String) pidMap.get(pid);

			writer.println(pid + TOKEN_DELIMITER + pCode); // only (non-null -> non-null) exist in pid map
		}

		writer.flush();

		setPIDCacheFileData(bout.toByteArray());

		writer.close();
	}


	// LOCK HANDLING

	/**
	 * Returns the File object representing the MVD cache file.
	 * Creates the necessary files and directories (cache) if
	 * they are non-existant prior to the call, and does not
	 * return until the cache file exists and is locked so only
	 * this process may alter it.
	 *
	 * After this method has been called, the internal RAF object
	 * and a FileLock exists for the cache file.
	 *
	 * @param mvdDirectory File
	 * @param not ProgressNotifiable
	 * @return File
	 */
	private File openAndLockMVDRandomAccessFile(File mvdDirectory, ProgressNotifiable not)
	{
		try
		{
			// obtain file references

			File mvdCacheDir = new File(mvdDirectory, MVD_CACHE_SUBDIRECTORY);

			if (!mvdCacheDir.exists())
			{
				 mvdCacheDir.mkdirs();
			}

			File mvdMVDCacheFile = new File(mvdCacheDir, MVD_MVD_CACHE_FILE_NAME); // might not exist here

			boolean mvdCacheFileExisted = mvdMVDCacheFile.exists(); // RAF object creation will create file if non-existant

			mvdRandomAccessFile = new RandomAccessFile(mvdMVDCacheFile, "rw"); // might represent non-existing file, creates if not there

			try
			{
				mvdCacheFileLock = mvdRandomAccessFile.getChannel().lock(); // blocks until lock obtained or -> IOException
			}
			catch (IOException exc)
			{
				mvdCacheFileLock = null;

				exc.printStackTrace(); // non-critical error

				System.out.println("WARNING: could not acquire lock on mvd cache file (" + exc.getMessage() + ")");
			}

			if (!mvdCacheFileExisted)
			{
				createMVDCacheFile(mvdDirectory, not); // directory passed for forest data obtaining
			}

			return mvdMVDCacheFile;
		}
		catch (IOException exc)
		{
			exc.printStackTrace();

			return null;
		}
	}

	/**
	 * Returns the File object representing the PID cache file.
	 * Creates the necessary files and directories (cache) if
	 * they are non-existant prior to the call, and does not
	 * return until the cache file exists and is locked so only
	 * this process may alter it.
	 *
	 * After this method has been called, the internal RAF object
	 * and a FileLock exists for the cache file.

	 * @param mvdDirectory File
	 * @param not ProgressNotifiable
	 * @return File
	 */
	private File openAndLockPIDRandomAccessFile(File mvdDirectory, ProgressNotifiable not)
	{
		try
		{
			// obtain file references

			File mvdCacheDir = new File(mvdDirectory, MVD_CACHE_SUBDIRECTORY);

			if (!mvdCacheDir.exists())
			{
				mvdCacheDir.mkdirs();
			}

			File mvdPIDCacheFile = new File(mvdCacheDir, MVD_PID_CACHE_FILE_NAME); // might not exist here

			boolean pidCacheFileExisted = mvdPIDCacheFile.exists(); // RAF object creation will create file if non-existant

			pidRandomAccessFile = new RandomAccessFile(mvdPIDCacheFile, "rw"); // might represent non-existing file, creates if not there

			try
			{
				pidCacheFileLock = pidRandomAccessFile.getChannel().lock(); // blocks until lock obtained or -> IOException
			}
			catch (IOException exc)
			{
				pidCacheFileLock = null;

				System.out.println("WARNING: could not acquire lock on pid cache file (" + exc.getMessage() + ")");
			}

			if (!pidCacheFileExisted)
			{
				createPIDCacheFile(mvdDirectory, not); // does not involve random access file or locks
			}

			return mvdPIDCacheFile;
		}
		catch (IOException exc)
		{
			exc.printStackTrace();

			return null;
		}
	}

	/**
	 * After this method has been called, the internal RAF object
	 * and the FileLock have been set to null again.
	 */
	private void releaseLockAndCloseMVDRandomAccessFile()
	{
		try
		{
			if (mvdCacheFileLock != null)
			{
				/* NOTE: it might be the case that a file lock could
				   not be obtained on the cache file. In this case, a
				   warning message is printed (since it is a non-critical
				   failure) and the file lock is a null object. This has
				   to be tested for here. */

				mvdCacheFileLock.release(); // -> IOException
			}

			if (mvdRandomAccessFile != null)
			{
				mvdRandomAccessFile.close(); // -> IOException
			}

			mvdCacheFileLock = null;

			mvdRandomAccessFile = null;
		}
		catch (IOException exc)
		{
			exc.printStackTrace();
		}
	}

	/**
	 * After this method has been called, the internal RAF object
	 * and the FileLock have been set to null again.
	 */
	private void releaseLockAndClosePIDRandomAccessFile()
	{
		try
		{
			if (pidCacheFileLock != null)
			{
				/* NOTE: it might be the case that a file lock could
				   not be obtained on the cache file. In this case, a
				   warning message is printed (since it is a non-critical
				   failure) and the file lock is a null object. This has
				   to be tested for here. */

				pidCacheFileLock.release(); // -> IOException
			}

			if (pidRandomAccessFile != null)
			{
				pidRandomAccessFile.close(); // -> IOException
			}

			pidCacheFileLock = null;

			pidRandomAccessFile = null;
		}
		catch (IOException exc)
		{
			exc.printStackTrace();
		}
	}


	// BYTE DATA TRANSFER TO AND FROM RAF

	/**
	 * Obtains the data currently contained in the mvd
	 * cache file as a byte array.
	 * @return byte[]
	 */
	private byte[] obtainMVDCacheFileData()
	{
		try
		{
			mvdRandomAccessFile.seek(0L); // -> IOException

			byte[] retArr = new byte[(int)mvdRandomAccessFile.length()]; // -> IOException

			mvdRandomAccessFile.read(retArr); // -> IOException

			return retArr;
		}
		catch (IOException exc)
		{
			exc.printStackTrace();

			return new byte[0];
		}
	}

	/**
	 * Obtains the data currently contained in the pid
	 * cache file as a byte array.
	 * @return byte[]
	 */
	private byte[] obtainPIDCacheFileData()
	{
		try
		{
			pidRandomAccessFile.seek(0L); // -> IOException

			byte[] retArr = new byte[(int)pidRandomAccessFile.length()]; // -> IOException

			pidRandomAccessFile.read(retArr); // -> IOException

			return retArr;
		}
		catch (IOException exc)
		{
			exc.printStackTrace();

			return new byte[0];
		}
	}

	/**
	 * Sets the contents of the mvd cache file to the
	 * specified byte array.
	 * @param data byte[]
	 */
	private void setMVDCacheFileData(byte[] data)
	{
		try
		{
			mvdRandomAccessFile.seek(0L); // -> IOException

			mvdRandomAccessFile.setLength(0L); // -> IOException

			mvdRandomAccessFile.write(data); // -> IOException
		}
		catch (IOException exc)
		{
			exc.printStackTrace();
		}
	}

	/**
	 * Sets the contents of the pid cache file to the
	 * specified byte array.
	 * @param data byte[]
	 */
	private void setPIDCacheFileData(byte[] data)
	{
		try
		{
			pidRandomAccessFile.seek(0L); // -> IOException

			pidRandomAccessFile.setLength(0L); // -> IOException

			pidRandomAccessFile.write(data); // -> IOException
		}
		catch (IOException exc)
		{
			exc.printStackTrace();
		}
	}


	// DEBUGGING TOOLS

	private void debug(String method, String mess)
	{
		System.out.println(DEBUG_PREFIX + method + " - " + mess);
	}


	// UNIT TEST

	public static void main(String[] args)
	{
	}
}
