/*
 * @(#)NewPCodeFormatPIDParser.java
 *
 * $Id: NewPCodeFormatPIDParser.java,v 1.3 2004/11/15 15:40:48 erichson Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 *
 * $Log: NewPCodeFormatPIDParser.java,v $
 * Revision 1.3  2004/11/15 15:40:48  erichson
 * Added a warning message if negative age appears
 *
 */

package medview.datahandling;

import java.util.*;

/**
 * A PID parser recognizing identifier strings in the
 * new format of a pcode, namely AAANNNNNN(0|9)YY(0|1).
 *
 * @author Fredrik Lindahl
 */
public class NewPCodeFormatPIDParser implements PIDParser
{
    public int getYearOfBirth(PatientIdentifier pid) throws InvalidPIDException
    {
		if (recognizes(pid))
		{
			char centChar = pid.getPCode().charAt(9);

			String year = pid.getPCode().substring(10,12); // GOT0000019490

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
                        int age = (year - getYearOfBirth(pid));
                        
                        if (age < 0)
                            System.err.println("Warning: NewPCodeFormatPIDParser: Age for " + pid.toString() + " is " + age + "!");
                        
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
			char genderChar = pid.getPCode().charAt(12); // GOT0000019490

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


    public NewPCodeFormatPIDParser()
    {
		validator = new NewPCodeFormatRawPIDValidator();
	}

    private RawPIDValidator validator;
}