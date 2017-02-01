package medview.datahandling;

import java.util.*;

public class CompositePIDParser implements PIDParser
{
	public int getAge(PatientIdentifier pid) throws InvalidPIDException
	{
		Enumeration enm = parserVector.elements();

		while (enm.hasMoreElements())
		{
			PIDParser curr = (PIDParser) enm.nextElement();

			if (curr.recognizes(pid))
			{
				return (curr.getAge(pid));
			}
		}

		throw new InvalidPIDException(pid.toString());
	}

	public int getAge(PatientIdentifier pid, Date atDate) throws InvalidPIDException
	{
		Enumeration enm = parserVector.elements();

		while (enm.hasMoreElements())
		{
			PIDParser curr = (PIDParser) enm.nextElement();

			if (curr.recognizes(pid))
			{
				return (curr.getAge(pid, atDate));
			}
		}

		throw new InvalidPIDException(pid.toString());
	}

	public int getYearOfBirth(PatientIdentifier pid) throws InvalidPIDException
	{
		Enumeration enm = parserVector.elements();

		while (enm.hasMoreElements())
		{
			PIDParser curr = (PIDParser) enm.nextElement();

			if (curr.recognizes(pid))
			{
				return (curr.getYearOfBirth(pid));
			}
		}

		throw new InvalidPIDException(pid.toString());
	}

	public int getGender(PatientIdentifier pid) throws InvalidPIDException
	{
		Enumeration enm = parserVector.elements();

		while (enm.hasMoreElements())
		{
			PIDParser curr = (PIDParser) enm.nextElement();

			if (curr.recognizes(pid))
			{
				return (curr.getGender(pid));
			}
		}

		throw new InvalidPIDException(pid.toString());
	}

	public boolean recognizes(PatientIdentifier pid)
	{
		Enumeration enm = parserVector.elements();

		while (enm.hasMoreElements())
		{
			PIDParser curr = (PIDParser) enm.nextElement();

			if (curr.recognizes(pid))
			{
				return true;
			}
		}

		return false;
	}

	public CompositePIDParser()
	{
		parserVector = new Vector();

		parserVector.add(new OldPCodeFormatPIDParser());

		parserVector.add(new NewPCodeFormatPIDParser());
	}

	private Vector parserVector;



// ----------------------------------------------------------------------------------
// ******************************** UNIT TEST METHOD ********************************
// ----------------------------------------------------------------------------------

	public static void main(String[] args)
	{
		try
		{
			CompositePIDParser p = new CompositePIDParser();

			PatientIdentifier[] pids = new PatientIdentifier[]
			{
				new PatientIdentifier("X00029300"),

				new PatientIdentifier("X00029401"),

				new PatientIdentifier("X00049801"),

				new PatientIdentifier("AA00030770"),

				//new PatientIdentifier("XAF00019231"), // invalid length

				new PatientIdentifier("GOT0000019771"),

				//new PatientIdentifier("G2T0000019771"), // invalid prefix

				//new PatientIdentifier("GOT00X0019771"), // invalid running nr

				//new PatientIdentifier("GOT0000019772"), // invalid gender

				new PatientIdentifier("GOT0000010771"),

				//new PatientIdentifier("GOT0000012771"), // invalid century

				//new PatientIdentifier("GOT00000197X1"), // invalid year of birth
			};

			for (int ctr=0; ctr<pids.length; ctr++)
			{
				System.out.println("Age (" + pids[ctr] + ") = " + p.getAge(pids[ctr]));
			}

			for (int ctr=0; ctr<pids.length; ctr++)
			{
				System.out.println("Year of birth (" + pids[ctr] + ") = " + p.getYearOfBirth(pids[ctr]));
			}

			for (int ctr=0; ctr<pids.length; ctr++)
			{
				System.out.println("Gender (1=MALE, 2=FEMALE) (" + pids[ctr] + ") = " + p.getGender(pids[ctr]));
			}
		 }
		 catch (InvalidPIDException e)
		 {
			 e.printStackTrace();
		 }
	}

// ----------------------------------------------------------------------------------
// **********************************************************************************
// ----------------------------------------------------------------------------------

}
