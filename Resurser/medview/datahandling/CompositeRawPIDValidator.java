package medview.datahandling;

import java.util.*;

public class CompositeRawPIDValidator implements RawPIDValidator
{
	public String normalizePID(String pid) throws InvalidRawPIDException
	{
		Enumeration enm = validatorVector.elements();

		while (enm.hasMoreElements())
		{
			RawPIDValidator curr = (RawPIDValidator) enm.nextElement();

			if (curr.validates(pid))
			{
				return curr.normalizePID(pid);
			}
		}

		throw new InvalidRawPIDException(pid);
	}

	public boolean validates(String pid)
	{
		Enumeration enm = validatorVector.elements();

		while (enm.hasMoreElements())
		{
			if (((RawPIDValidator)enm.nextElement()).validates(pid))
			{
				return true;
			}
		}

		return false;
	}

	public CompositeRawPIDValidator()
	{
		validatorVector = new Vector();

		validatorVector.add(new OldPCodeFormatRawPIDValidator());

		validatorVector.add(new NewPCodeFormatRawPIDValidator());

		validatorVector.add(new SwedishPNRRawPIDValidator());
	}

	private Vector validatorVector;


// ----------------------------------------------------------------------------------
// ******************************** UNIT TEST METHOD ********************************
// ----------------------------------------------------------------------------------

	public static void main(String[] args)
	{
		CompositeRawPIDValidator v = new CompositeRawPIDValidator();

		String[] pNrs = new String[]
		{
			"770329-2742", "491123-3823", "750122-2931", "781213-2392",

			"771323-2832", "770335-2932", "778324-28392", "740023-2392",

			"770230-2391", "770229-2391"
		};

		String[] oldPCodes = new String[]
		{
			"X00029300", "X00029401", "X00049801", "AA00030770",

			"XAF00019231"
		};

		String[] newPCodes = new String[]
		{
			"GOT0000019771", "G2T0000019771", "GOT00X0019771",

			"GOT0000019772", "GOT0000010771", "GOT0000012771",

			"GOT00000197X1"
		};

		for (int ctr=0; ctr<pNrs.length; ctr++)
		{
			System.out.println(pNrs[ctr] + " validates? " + v.validates(pNrs[ctr]));
		}

		for (int ctr=0; ctr<oldPCodes.length; ctr++)
		{
			System.out.println(oldPCodes[ctr] + " validates? " + v.validates(oldPCodes[ctr]));
		}

		for (int ctr=0; ctr<newPCodes.length; ctr++)
		{
			System.out.println(newPCodes[ctr] + " validates? " + v.validates(newPCodes[ctr]));
		}
	}

// ----------------------------------------------------------------------------------
// **********************************************************************************
// ----------------------------------------------------------------------------------

}
