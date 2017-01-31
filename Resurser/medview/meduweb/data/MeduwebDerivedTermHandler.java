/**
 * @(#) MeduwebDerivedTermHandler.java
 */

package medview.meduweb.data;

import java.text.*;

import java.util.*;

import medview.datahandling.*;
import medview.common.translator.*;

/**
 * Derived terms are not defined in the
 * term definition or value files, nor are
 * they defined in any examination record.
 * The terms can be found, for instance, in
 * a template, and assume values for a certain
 * examination based on another term's value.
 *
 * @author Fredrik Lindahl
 */
public class MeduwebDerivedTermHandler
{

	/**
	 * Returns the term that the specified term
	 * is derived from (if it is derived from
	 * any term at all). If the term is not a
	 * derived term, this method will return a
	 * null value. For instance, if called for
	 * the term 'PCode(age)', the method would
	 * return the string "P-code".
	 */
	public String getDerivedTermDerivee(String term)
	{
		if (isPCodeDerivedTerm(term))
		{
			return PCODE_TERM_NAME;
		}

		if (isDateDerivedTerm(term))
		{
			return DATE_TERM_NAME;
		}

		return null;
	}



	/**
	 * Returns an array of all derived terms that
	 * this handler handles.
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
	 * Returns the derived term values for the
	 * specified derived term. If the term is
	 * not a derived term, or if it is derived
	 * but contains no values, the method returns
	 * an empty string array.
	 */
	public Object[] getDerivedTermValues(String term)
	{
		if (isPCodeGenderTerm(term))
		{
			return new String[]
			{
				PCODE_DERIVED_TERM_HE_VALUE,

				PCODE_DERIVED_TERM_SHE_VALUE
			};
		}

		return new String[0];
	}



	/**
	 * Retrieve the type that the derived term
	 * should have. If the term is not recognized
	 * (it could be that the term is not a derived
	 * term), a value of -1 is returned from this
	 * method.
	 */
	public int getDerivedTermType(String term)
	{
		if (isPCodeDerivedTerm(term))
		{
			return TermDataHandler.REGULAR_TYPE;
		}

		if (isDateDerivedTerm(term))
		{
			return TermDataHandler.REGULAR_TYPE;
		}

		return -1;
	}



	/**
	 * Returns the handler instance. The handler is
	 * a Singleton instance, that will be instantiated
	 * at the first instance() call (lazily).
	 */
	public static MeduwebDerivedTermHandler instance()
	{
		if (instance == null) { instance = new MeduwebDerivedTermHandler(); }

		return instance;
	}



	/**
	 * Returns whether or not the specified term
	 * is a derived term that can be handled by
	 * this handler.
	 */
	public boolean isDerivedTerm(String term)
	{
		if (isPCodeDerivedTerm(term)) { return true; }

		if (isDateDerivedTerm(term)) { return true; }

		return false;
	}



	/**
	 * Returns whether or not the specified derived
	 * term requires knowledge of the examination
	 * date.
	 */
	public boolean requiresExaminationDate(String term)
	{
		if (isPCodeDerivedTerm(term)) { return true; }

		if (isDateDerivedTerm(term)) { return true; }

		return false;
	}



	/**
	 * Returns whether or not the specified derived
	 * term requires knowledge of the patient identifier.
	 */
	public boolean requiresPatientIdentifier(String term)
	{
		if (isPCodeDerivedTerm(term)) { return true; }

		return false;
	}



	/**
	 * Obtains the derived term values for the
	 * specified depended-on value. Some derived
	 * terms may need information about the
	 * generated examination date or the patient
	 * identifier associated with the currently
	 * generated examination. This can be checked
	 * by calling the requiresXXX() methods in the
	 * handler prior to obtaining the derived values,
	 * notifying the caller whether or not it needs
	 * to provide these arguments, otherwise they
	 * can be specified as null. A null value
	 * will be returned if some error occurs or
	 * if the term is not a derived term recognized
	 * by this handler.
	 */
	public String[] getDerivedValues(String term, String dVal, Date eD, String pID)
	{
		try
		{
			if (isPCodeDerivedTerm(term))
			{
				if (isPCodeGenderTerm(term))
				{
					switch (mVDH.getGender(pID))
					{
						case PCodeParser.FEMALE:
						{
							return new String[] { PCODE_DERIVED_TERM_SHE_VALUE };
						}

						case PCodeParser.MALE:
						{
							return new String[] { PCODE_DERIVED_TERM_HE_VALUE };
						}
					}
				}

				if (term.equalsIgnoreCase(PCODE_DERIVED_PID_TERM))
				{
					return new String[] { mVDH.getPatientIdentifier(pID) };
				}

				if (term.equalsIgnoreCase(PCODE_DERIVED_AGE_TERM))
				{
					return new String[] { mVDH.getAge(pID, eD) + "" };
				}

				if (term.equalsIgnoreCase(PCODE_DERIVED_YOB_TERM))
				{
					return new String[] { mVDH.getYearOfBirth(pID) + "" };
				}
			}

			if (isDateDerivedTerm(term))
			{
				DateFormat dF = DateFormat.getDateInstance(DateFormat.SHORT);

				return new String[] { dF.format(eD) };
			}

			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return null;
		}
	}





	/**
	 * Returns whether or not the specified term
	 * requires a derivee value in order to be
	 * derived. If the term is only dependent on
	 * the examination date and/or pid of the
	 * associated examination, a null value of the
	 * derivee value can be specified when obtaining
	 * the derived values using the getDerivedValues()
	 * method. Otherwise, the value for the term is
	 * dependent upon some other value found in the
	 * examination record. Note that if the term then
	 * <i>is</i> only pcode or date derived, the value
	 * will not be found and the term will translate to
	 * a no line.<br>
	 * <br>
	 * All derived terms will have a patient identifier
	 * and examination date associated with them at the
	 * time of generation.
	 */
	public boolean isOnlyPCodeDateDerived(String term)
	{
		if (isPCodeDerivedTerm(term)) { return true; }

		if (isDateDerivedTerm(term)) { return true; }

		return false;
	}





	/**
	 * Returns whether or not the specified derived
	 * term's value is considered enough significant
	 * to affect the corresponding node's return value
	 * when it is asked to parse itself in the parse
	 * tree mechanism. A derived term is considered to
	 * be significant if its derivee is a significant
	 * term (currently, this is all non-derived terms
	 * except the 'pcode' and 'date' terms).
	 */
	public boolean isSignificantDerivedTerm(String term)
	{
		return (!isOnlyPCodeDateDerived(term));
	}





// -------------------------------------------------------------
// ****************** PRIVATE UTILITY METHODS ******************
// -------------------------------------------------------------

	private boolean isDateDerivedTerm(String term)
	{
		for (int ctr=0; ctr<dateDTerms.length; ctr++)
		{
			if (term.equalsIgnoreCase(dateDTerms[ctr])) { return true; }
		}

		return false;
	}

	private boolean isPCodeDerivedTerm(String term)
	{
		for (int ctr=0; ctr<pCodeDTerms.length; ctr++)
		{
			if (term.equalsIgnoreCase(pCodeDTerms[ctr])) { return true; }
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

// -------------------------------------------------------------
// *************************************************************
// -------------------------------------------------------------





	private void initSimpleMembers()
	{
		pCodeDTerms = new String[]
		{
			PCODE_DERIVED_PID_TERM,

			PCODE_DERIVED_AGE_TERM,

			PCODE_DERIVED_YOB_TERM,

			PCODE_DERIVED_HE_OR_SHE_TERM,

			PCODE_DERIVED_HIS_OR_HERS_TERM,

			PCODE_DERIVED_WOMAN_OR_MAN_TERM,

			PCODE_DERIVED_FEMALE_OR_MALE_TERM
		};

		dateDTerms = new String[]
		{
			DATE_DERIVED_DATE_TERM
		};

		mVDH = MeduwebDataHandler.instance();
	}

	private MeduwebDerivedTermHandler()
	{
		initSimpleMembers();
	}

	private String[] dateDTerms;

	private String[] pCodeDTerms;

	private MeduwebDataHandler mVDH;

	private static MeduwebDerivedTermHandler instance;


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


	public static final String DATE_DERIVED_DATE_TERM = "Date(date)";

	public static final String DATE_TERM_NAME = "Datum";

}
