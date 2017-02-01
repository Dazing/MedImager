package medview.datahandling;

/**
 * A PID validator recognizing identifier strings in the
 * old format of a pcode, namely A[A]NNNN(0|9)YY(0|1).
 *
 * @author Fredrik Lindahl
 */
public class OldPCodeFormatRawPIDValidator implements RawPIDValidator
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
		if (! ( (pid.length() == 9) || (pid.length() == 10)))
		{
			return false;
		}

		String genderString = pid.substring(pid.length() - 1, pid.length());

		if (! (genderString.equalsIgnoreCase("0") || genderString.equalsIgnoreCase("1")))
		{
			return false;
		}

		String yearString = pid.substring(pid.length() - 3, pid.length() - 1);

		try
		{
			Integer.parseInt(yearString);
		}
		catch (NumberFormatException e)
		{
			return false;
		}

		String centuryString = pid.substring(pid.length() - 4, pid.length() - 3);

		if (! (centuryString.equalsIgnoreCase("0") || centuryString.equalsIgnoreCase("9")))
		{
			return false;
		}

		String numbers = null;

		String letters = null;

		if (pid.length() == 9)
		{
			numbers = pid.substring(1, 5);

			letters = pid.substring(0, 1);
		}
		else if (pid.length() == 10)
		{
			numbers = pid.substring(2, 6);

			letters = pid.substring(0, 2);
		}

		try
		{
			Integer.parseInt(numbers);
		}
		catch (NumberFormatException e)
		{
			return false;
		}

                /* Removed uppercase-only checking as mentioned in e-mail with Olof and Fredrik. // Nils 2005-11-10
		for (int ctr = 0; ctr < letters.length(); ctr++)
		{
			if (! (Character.getType(letters.charAt(ctr)) == Character.UPPERCASE_LETTER))
			{
				return false;
			}
		}
                */
		return true;
	}
}
