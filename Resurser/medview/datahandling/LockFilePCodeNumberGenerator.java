// $Id: LockFilePCodeNumberGenerator.java,v 1.3 2005/12/22 19:10:25 lindahlf Exp $

package medview.datahandling;

import java.io.*;

import java.nio.channels.*;

import misc.foundation.io.*;

public class LockFilePCodeNumberGenerator implements PCodeNumberGenerator
{
	/**
	 * Sets the location from which the number
	 * generator obtains the numbers. In this
	 * case the location is a path to a lock file.
	 * @param loc String the number generator
	 * location.
	 */
	public void setNumberGeneratorLocation(String loc)
	{
		this.lockFileLocation = loc;
	}

	/**
	 * Obtains the currently set number generator
	 * location. In this case the location is a
	 * path to a lock file.
	 * @return String the currently set number generator
	 * location.
	 */
	public String getNumberGeneratorLocation()
	{
		return this.lockFileLocation;
	}

	/**
	 * Whether or not the number generator location has
	 * been set.
	 * @return boolean if the number generator location has
	 * been set.
	 */
	public boolean isNumberGeneratorLocationSet()
	{
		return (lockFileLocation != null);
	}

	/**
	 * Generates the next counter number to
	 * use for the next generated pcode. The
	 * negative number -1 is returned if the
	 * number could not be obtained for some
	 * reason.
	 */
	public int getNextNumber() throws CouldNotObtainNumberException
	{
		return this.getNextNumber(true); // consume a number
	}

	/**
	 * Generates the next counter number to
	 * use for the next generated pcode. This
	 * version of the method allows you to
	 * specify whether or not a number will be
	 * consumed by the call. If not, the next
	 * call will return the same number.
	 */
	public int getNextNumber(boolean consumeNr) throws CouldNotObtainNumberException
	{
		try
		{
			File file = new File(lockFileLocation);

			if (!file.exists())
			{
				throw new CouldNotObtainNumberException("Lock file doesn't exist!");
			}

			String enc = IOConstants.ISO_LATIN_1_CHARACTER_ENCODING;

			FileInputStream input = new FileInputStream(file); // -> Exceptions

			InputStreamReader streamReader = new InputStreamReader(input, enc); // -> Exceptions

			BufferedReader reader = new BufferedReader(streamReader);

			int nr = Integer.parseInt(reader.readLine()); // -> Exceptions

			input.close();

			if (consumeNr)
			{
				FileOutputStream output = new FileOutputStream(file); // -> Exceptions

				FileChannel channel = output.getChannel(); // get the file channel

				FileLock lock = null;

				try
				{
					lock = channel.lock(); // GET LOCK (-> IOException if underlying system doesn't support locks)
				}
				catch (IOException exc)
				{
					System.out.println("WARNING: could not obtain lock on lockfile (" + exc.getMessage() + ")"); // non-critical
				}

				OutputStreamWriter streamWriter = new OutputStreamWriter(output, enc);

				PrintWriter writer = new PrintWriter(streamWriter, true); // autoflush

				writer.print(nr + 1);

				writer.flush(); // but the autoflush doesnt seem to work so this is needed

				if (lock != null)
				{
					/* NOTE: the underlying file system might not support file locks,
					   this code should account for that. If the lock object is null
					   here, this must be the case. */

					lock.release(); // RELEASE LOCK
				}

				output.close();
			}

			return nr;
		}
		catch (FileNotFoundException e)
		{
			throw new CouldNotObtainNumberException("Lock file doesn't exist!");
		}
		catch (UnsupportedEncodingException e)
		{
			throw new CouldNotObtainNumberException("Unsupported encoding when reading lockfile!");
		}
		catch (IOException e)
		{
			throw new CouldNotObtainNumberException("IO Error when reading lockfile!");
		}
	}

	public LockFilePCodeNumberGenerator()
	{
	}

	private String lockFileLocation;
}
