package medview.datahandling;

/**
 * A PID validator recognizing identifier strings in the
 * format of a swedish personal number (personnummer). A
 * swedish personnummer is recognized by this validator
 * if it occurs in one of the following forms:
 * YYXXXXXX-XXXX or YYXXXXXXXXXX, where each 'X' is a number
 * between 0-9 and YY is either 19 or 20 (century). Furthermore,
 * the validator checks that the numbers represent valid
 * months and days.
 *
 * @author Fredrik Lindahl
 */
public class SwedishPNRRawPIDValidator implements RawPIDValidator
{
	public String normalizePID(String pid) throws InvalidRawPIDException
	{
		if (!validates(pid))
		{
			throw new InvalidRawPIDException(pid);
		}
		else
		{
			if (pid.length() == 12)	// 197703298482 (missing a '-')
			{
				return pid.substring(0,8) + "-" + pid.substring(8);
			}
			else if ((pid.length() == 10) || (pid.length() == 11))	// 7703298482 or 770329-8482
			{
				String preFix = null;

				if (pid.charAt(0) == '0')
				{
					preFix = "20" + pid.substring(0,6);
				}
				else
				{
					preFix = "19" + pid.substring(0,6);
				}

				if (pid.length() == 10)
				{
					return preFix + "-" + pid.substring(6);
				}
				else
				{
					return preFix + "-" + pid.substring(7);
				}
			}
			else
			{
				return pid; // already properly formatted
			}
		}
	}

	public boolean validates(String pid)
	{
		/* NOTE: The variants are:
		 *
		 * 7703298242
		 * 770329-8242
		 * 197703298242
		 * 19770329-8242
		 */

		if (!((pid.length() >= 10) && (pid.length() <= 13)))
		{
			return false;
		}

		if (pid.length() == 13)
		{
			if (!(pid.charAt(8) == '-'))
			{
				return false; // checks that '-' is divider
			}
		}

		if (pid.length() == 11)
		{
			if (!(pid.charAt(6) == '-'))
			{
				return false;	// checks that '-' is divider
			}
		}

		if ((pid.length() == 13) || (pid.length() == 12))
		{
			String centString = pid.substring(0,2);

			try
			{
				int cent = Integer.parseInt(centString);

				if (!((cent == 19) || (cent == 20)))
				{
					return false; // checks century is 19 or 20
				}
			}
			catch (NumberFormatException e)
			{
				return false; // checks that cent is a number
			}
		}

		String monthString = null;

		if ((pid.length() == 13) || (pid.length() == 12))
		{
			monthString = pid.substring(4,6);
		}
		else	// length is 10 or 11
		{
			monthString = pid.substring(2,4);
		}

		int month = -1;

		try
		{
			month = Integer.parseInt(monthString);

			if ((month <= 0) || (month > 12))
			{
				return false; // checks that month is 1-12
			}
		}
		catch (NumberFormatException e)
		{
			return false; // checks that month is a number
		}

		String dayString = null;

		if ((pid.length() == 13) || (pid.length() == 12))
		{
			dayString = pid.substring(6,8);
		}
		else	// length is 10 or 11
		{
			dayString = pid.substring(4,6);
		}

		try
		{
			int day = Integer.parseInt(dayString);

			if ((day <= 0) || (day > monthDays[month-1]))
			{
				return false; // checks that day is valid
			}
		}
		catch (NumberFormatException e)
		{
			return false; // checks that day is a number
		}

		return true;
	}

	private int[] monthDays = new int[] {31,29,31,30,31,30,31,31,30,31,30,31};


// ----------------------------------------------------------------------------------
// ******************************** UNIT TEST METHOD ********************************
// ----------------------------------------------------------------------------------

	public static void main(String[] args)
	{
		SwedishPNRRawPIDValidator v = new SwedishPNRRawPIDValidator();

		String[] pNrs = new String[]
		{
			"19770329-2742", "19491123-3823", "19750122-2931", "19781213-2392",

			"19771323-2832", "19770335-2932", "19778324-28392", "19740023-2392",

			"19770230-2391", "19770229-2391", "19770329-2425", "20031123-2572",

			"197703292425"
		};

		for (int ctr=0; ctr<pNrs.length; ctr++)
		{
			System.out.println(pNrs[ctr] + " validates? " + v.validates(pNrs[ctr]));
		}
	}

// ----------------------------------------------------------------------------------
// **********************************************************************************
// ----------------------------------------------------------------------------------

}