package medview.datahandling;

import java.util.*;

public interface PIDParser
{
	/**
	 * Returns the age of the specified patient at
	 * the current date. Will return a value of -1
	 * if the pid format could not be recognized.
	 */
	public int getAge(PatientIdentifier pid) throws InvalidPIDException;

	/**
	 * Returns the age of the specified patient at
	 * the specified date. Will return a value of -1
	 * if the pid format could not be recognized.
	 */
	public int getAge(PatientIdentifier pid, Date atDate) throws InvalidPIDException;

	/**
	 * Returns the year of birth of the specified
	 * patient. Will return -1 if the pid format
	 * could not be recognized.
	 */
	public int getYearOfBirth(PatientIdentifier pid) throws InvalidPIDException;

	/**
	 * Returns the gender of the specified patient
	 * as one of the integer constants found in the
	 * PIDParser interface (MALE or FEMALE). Will
	 * return a value of -1 is the pid format could
	 * not be recognized.
	 */
	public int getGender(PatientIdentifier pid) throws InvalidPIDException;

	/**
	 * Returns whether or not the PIDParser recognizes
	 * the format of the specified patient identifier.
	 */
	public boolean recognizes(PatientIdentifier pid);

	public static final int MALE = 1;

	public static final int FEMALE = 2;

	public static final String MALE_STRING = "Male";

	public static final String FEMALE_STRING = "Female";
}