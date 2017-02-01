package medview.datahandling;

/**
 * A PID validator recognizing identifier strings in the
 * new format of a pcode, namely AAANNNNNN(0|9)YY(0|1),
 * example GOT0000019770.
 *
 * @author Fredrik Lindahl
 */
public class NewPCodeFormatRawPIDValidator implements RawPIDValidator
{
	/**
	 */
	public String normalizePID(String pid) throws
		InvalidRawPIDException
	{
		if (!validates(pid))
		{
			throw new InvalidRawPIDException(pid);
		}
		else
		{
			return pid; // no modification
		}
	}

	public boolean validates(String pid)
	{
		if (! (pid.length() == 13))
		{
			return false; // check that length is 13 characters
		}

		char genderChar = pid.charAt(12);

		if (! ( (genderChar == '0') || (genderChar == '1')))
		{
			return false; // check that gender int is 0 or 1
		}

		String yearString = pid.substring(10, 12);

		try
		{
			Integer.parseInt(yearString);
		}
		catch (NumberFormatException e)
		{
			return false; // check that year is a number
		}

		char centuryChar = pid.charAt(9);

		if (! ( (centuryChar == '0') || (centuryChar == '9')))
		{
			return false;
		}

		String letters = pid.substring(0, 3);

		String number = pid.substring(3, 9);

		for (int ctr = 0; ctr < letters.length(); ctr++)
		{
			if (! (Character.getType(letters.charAt(ctr)) == Character.UPPERCASE_LETTER))
			{
				return false; // check that initial three letters are uc characters
			}
		}

		try
		{
			Integer.parseInt(number); // check that running number is a number
		}
		catch (NumberFormatException e)
		{
			return false;
		}

		return true;
	}
}
