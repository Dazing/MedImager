package medview.datahandling;

import java.text.*;

import java.util.*;

/**
 * A singleton placed in the data layer, handling derived terms
 * at a data level.
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class DerivedTermHandler
{
	// DERIVED TERMS

	/**
	 * Obtains an array of all the derived terms.
	 * @return String[]
	 */
	public String[] getDerivedTerms()
	{
		int pCL = pCodeDTerms.length;

		int dL = dateDTerms.length;

		String[] retArr = new String[pCL + dL];

		for (int ctr=0; ctr<pCL; ctr++)
		{
			retArr[ctr] = pCodeDTerms[ctr];
		}

		for (int ctr=0; ctr<dL; ctr++)
		{
			retArr[ctr + pCL] = dateDTerms[ctr];
		}

		return retArr;
	}

	/**
	 * Whether or not the specified term is a derived term.
	 * @param term String
	 * @return boolean
	 */
	public boolean isDerivedTerm(String term)
	{
		return (isPCodeDerivedTerm(term) || isDateDerivedTerm(term));
	}

	/**
	 * Obtain the name of the term that the value for the
	 * specified term is derived from.
	 * @param term String
	 * @return String
	 * @throws NotDerivedTermException
	 */
	public String getDerivedTermDerivee(String term) throws NotDerivedTermException
	{
		if (isPCodeDerivedTerm(term))
		{
			return PCODE_TERM_NAME;
		}

		if (isDateDerivedTerm(term))
		{
			return DATE_TERM_NAME;
		}

		throw new NotDerivedTermException(term);
	}

	/**
	 * Obtain the (instance) derived values for the specified
	 * derived term. NOTE: this is not the same as the list of
	 * all possible derived term values for the derived term,
	 * there is another method for this purpose. The returned
	 * array contains the actual derived term values for the
	 * currently set pid, date etc.
	 * @param term String
	 * @return String[]
	 */
	public String[] getDerivedTermValues(String term)
	{
		try
		{
			if (isPCodeDerivedTerm(term))
			{
				if (isPCodeGenderTerm(term))
				{
					switch (mVDH.getGender(examinationPID))
					{
						case PIDParser.FEMALE:
						{
							return new String[] { PCODE_DERIVED_TERM_SHE_VALUE };
						}

						case PIDParser.MALE:
						{
							return new String[] { PCODE_DERIVED_TERM_HE_VALUE };
						}
					}
				}

				if (term.equalsIgnoreCase(PCODE_DERIVED_PID_TERM))
				{
					return new String[] { examinationPID.toString() };
				}

				if (term.equalsIgnoreCase(PCODE_DERIVED_AGE_TERM))
				{
					return new String[] { mVDH.getAge(examinationPID, examinationDate) + "" };
				}

				if (term.equalsIgnoreCase(PCODE_DERIVED_YOB_TERM))
				{
					return new String[] { mVDH.getYearOfBirth(examinationPID) + "" };
				}
			}

			if (isDateDerivedTerm(term))
			{
				DateFormat dF = DateFormat.getDateInstance(DateFormat.SHORT);

				return new String[] { dF.format(examinationDate) };
			}

			return null;
		}
		catch (Exception e)
		{
			System.out.println("Derive: Invalid PID: " + e.getMessage());
                        //e.printStackTrace();

			return null;
		}
	}

	/**
	 * Obtains an array of all possible (known) values that the
	 * derived term can obtain. If the derived term is of such a
	 * type that it only has one value and this value is determined
	 * at run-time after examination pid / date etc. have been set,
	 * this method will return an empty array.
	 * @param term String
	 * @return String[]
	 * @throws NotDerivedTermException
	 */
	public String[] getAllDerivedTermPossibleValues(String term)
	{
		if (isPCodeGenderTerm(term))
		{
			return new String[]
			{
				PCODE_DERIVED_TERM_HE_VALUE,

				PCODE_DERIVED_TERM_SHE_VALUE
			};
		}
		else if (isDateDerivedTerm(term))
		{
			return new String[0];
		}
		else
		{
			return new String[0];
		}
	}

	/**
	 * Returns the (data) type descriptor for the specified term,
	 * i.e. the type descriptors used for system <-> storage
	 * communication, as defined in the data layer's term data
	 * handler classes.
	 * @param term String
	 * @return String
	 * @throws NotDerivedTermException
	 */
	public String getDerivedTermTypeDescriptor(String term) throws NotDerivedTermException
	{
		if (isPCodeDerivedTerm(term) || isDateDerivedTerm(term))
		{
			return TermDataHandler.REGULAR_TYPE_DESCRIPTOR;
		}

		throw new NotDerivedTermException(term);
	}


	// MEDVIEW DERIVED TERM HANDLER IMPLEMENTATIONS

	/**
	 * Sets the examination date used for obtaining derived
	 * term instance values.
	 * @param date Date
	 */
	public void setExaminationDate(Date date)
	{
		this.examinationDate = date;
	}

	/**
	 * Sets the patient identifier used for obtaining derived
	 * term instance values.
	 * @param pid PatientIdentifier
	 */
	public void setPatientIdentifier(PatientIdentifier pid)
	{
		this.examinationPID = pid;
	}


	// UTILITY METHODS

	/**
	 * Whether or not the specified term is derived from
	 * a pcode (pid) or date or not.
	 * @param term String
	 * @return boolean
	 */
	public boolean isOnlyPCodeDateDerived(String term)
	{
		return (isPCodeDerivedTerm(term) || isDateDerivedTerm(term));
	}

	/**
	 * Whether the specified term is of a significant
	 * character, i.e. if it is one that can determine
	 * content inclusion judgement at document generation
	 * time.
	 * @param term String
	 * @return boolean
	 */
	public boolean isSignificantDerivedTerm(String term)
	{
		return (!isOnlyPCodeDateDerived(term));
	}

	private boolean isDateDerivedTerm(String term)
	{
		for (int ctr=0; ctr<dateDTerms.length; ctr++)
		{
			if (term.equalsIgnoreCase(dateDTerms[ctr]))
			{
				return true;
			}
		}

		return false;
	}

	private boolean isPCodeDerivedTerm(String term)
	{
		for (int ctr=0; ctr<pCodeDTerms.length; ctr++)
		{
			if (term.equalsIgnoreCase(pCodeDTerms[ctr]))
			{
				return true;
			}
		}

		return false;
	}

	private boolean isPCodeGenderTerm(String term)
	{
		if (term.equalsIgnoreCase(PCODE_DERIVED_HE_OR_SHE_TERM) ||

			term.equalsIgnoreCase(PCODE_DERIVED_HIS_OR_HERS_TERM) ||

			term.equalsIgnoreCase(PCODE_DERIVED_WOMAN_OR_MAN_TERM) ||

			term.equalsIgnoreCase(PCODE_DERIVED_FEMALE_OR_MALE_TERM))
		{
			return true;
		}

		return false;
	}


	// SINGLETON

	public static DerivedTermHandler instance()
	{
		if (instance == null)
		{
			instance = new DerivedTermHandler();
		}

		return instance;
	}


	// CONSTRUCTOR

	private DerivedTermHandler() { }


	// MEMBER VARIABLES

	private String[] pCodeDTerms = new String[]
	{
		PCODE_DERIVED_PID_TERM, PCODE_DERIVED_AGE_TERM, PCODE_DERIVED_YOB_TERM,

		PCODE_DERIVED_HE_OR_SHE_TERM, PCODE_DERIVED_HIS_OR_HERS_TERM,

		PCODE_DERIVED_WOMAN_OR_MAN_TERM, PCODE_DERIVED_FEMALE_OR_MALE_TERM
	};


	private String[] dateDTerms = new String[]
	{
		DATE_DERIVED_DATE_TERM
	};

	private Date examinationDate = null;

	private PatientIdentifier examinationPID = null;

	private MedViewDataHandler mVDH = MedViewDataHandler.instance();

	public static DerivedTermHandler instance = null;


	// PCODE CONSTANTS

	public static final String PCODE_DERIVED_PID_TERM = "PCode(pid)";

	public static final String PCODE_DERIVED_AGE_TERM = "PCode(age)";

	public static final String PCODE_DERIVED_YOB_TERM = "PCode(year of birth)";

	public static final String PCODE_DERIVED_HE_OR_SHE_TERM = "PCode(he or she)";

	public static final String PCODE_DERIVED_HIS_OR_HERS_TERM = "PCode(his or hers)";

	public static final String PCODE_DERIVED_WOMAN_OR_MAN_TERM = "PCode(woman or man)";

	public static final String PCODE_DERIVED_FEMALE_OR_MALE_TERM = "PCode(female or male)";

	public static final String PCODE_DERIVED_TERM_HE_VALUE = "He";

	public static final String PCODE_DERIVED_TERM_SHE_VALUE = "She";

	public static final String PCODE_TERM_NAME = "P-code";


	// DATE CONSTANTS

	public static final String DATE_DERIVED_DATE_TERM = "Date(date)";

	public static final String DATE_TERM_NAME = "Datum";
}
