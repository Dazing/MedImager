/*
 * @(#)OldPCodeParser.java
 *
 * $Id: OldPCodeFormatPIDParser.java,v 1.3 2004/11/15 15:40:40 erichson Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 *
 * $Log: OldPCodeFormatPIDParser.java,v $
 * Revision 1.3  2004/11/15 15:40:40  erichson
 * Added a warning message if negative age appears
 *
 */

package medview.datahandling;

import java.util.*;

/**
 * A PID parser recognizing identifier strings in the
 * old format of a pcode, namely A[A]NNNN(0|9)YY(0|1).
 *
 * @author Fredrik Lindahl
 */
public class OldPCodeFormatPIDParser implements PIDParser
{
    public int getYearOfBirth(PatientIdentifier pid) throws InvalidPIDException
    {
		if (recognizes(pid))
		{
			String year;

			char centChar;

			if (pid.getPCode().length() == 9)
			{
				centChar = pid.getPCode().charAt(5); // G00019490

				year = pid.getPCode().substring(6,8);
			}
			else
			{
				centChar = pid.getPCode().charAt(6); // GG00019490

				year = pid.getPCode().substring(7,9);
			}

			int centInt;

			if (centChar == '9')
			{
				centInt = 1900;
			}
			else
			{
				centInt = 2000;
			}

			return (centInt + Integer.parseInt(year));
		}
		else
		{
			throw new InvalidPIDException(pid.toString());
		}
    }

    public int getAge(PatientIdentifier pid) throws InvalidPIDException
    {
        return getAge(pid, new Date());
    }

    public int getAge(PatientIdentifier pid, Date atDate) throws InvalidPIDException
    {
		if (recognizes(pid))
		{
			GregorianCalendar cal = new GregorianCalendar();

			cal.setTime(atDate);

			int year = cal.get(Calendar.YEAR);

			int age = year - getYearOfBirth(pid);
                        if (age < 0)
                            System.err.println("Warning: OldPCodeFormatPIDParser: Age for " + pid.toString() + " is " + age + "!");
                        return age;
		}
		else
		{
			throw new InvalidPIDException(pid.toString());
		}
    }

    public int getGender(PatientIdentifier pid) throws InvalidPIDException
    {
        if (recognizes(pid))
        {
			char genderChar;

            if (pid.getPCode().length() == 9)
            {
				genderChar = pid.getPCode().charAt(8); // G00019490
			}
            else
            {
                genderChar = pid.getPCode().charAt(9); // GG00019490
            }

            return ((genderChar == '0') ? PIDParser.FEMALE : PIDParser.MALE);
        }
        else
        {
			throw new InvalidPIDException(pid.toString());
        }
    }

    public boolean recognizes(PatientIdentifier pid)
    {
		return validator.validates(pid.getPCode());
    }


    public OldPCodeFormatPIDParser()
    {
		validator = new OldPCodeFormatRawPIDValidator();
	}

    private RawPIDValidator validator;
}