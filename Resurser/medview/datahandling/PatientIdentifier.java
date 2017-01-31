package medview.datahandling;

import java.io.*;

import java.util.*;

/**
 * Identifies a patient. A patient is identified by a
 * unique pcode and (optionally) a pid which is used
 * to display the patient to the user. For instance,
 * in Sweden each citizen has a personal number
 * (personnummer), so in Sweden the PID can be the
 * personnummer of the individual represented by the
 * pcode. If there is no pid for a certain individual,
 * the GUI should display the pcode to the user. A
 * patient identifier object always contains a pcode.
 *
 * During construction of the object, the pcode is
 * checked for validity. The constructor will throw
 * an InvalidPIDException if the pcode is invalid and
 * not according to specifications.
 *
 * @author Fredrik Lindahl
 */
public class PatientIdentifier implements Comparable, Serializable
{
	/**
	 * Obtains the pcode of the patient.
	 */
	public String getPCode()
	{
		return pCode;
	}

	/**
	 * Obtains the pid identifier of the
	 * patient, might be null if there is
	 * no such identifier.
	 */
	public String getPID()
	{
		return pid;
	}

	/**
	 * Returns whether or not the patient
	 * identifier object contains a pid.
	 * A patient identifier object must
	 * always contain a pcode, but the pid
	 * is optional and might be null.
	 */
	public boolean containsPID()
	{
		return (pid != null);
	}

	/**
	 * Returns a string
	 */
	public String toString()
	{
		if (pid == null)
		{
			return pCode;
		}
		else
		{
			return pid;
		}
	}

	/**
	 * The hash code of a PatientIdentifier
	 * object is simply the hashcode of the
	 * kept pcode-string.
	 */
	public int hashCode()
	{
		return pCode.hashCode();
	}

	/**
	 * Compares this object to the specified
	 * by the natural order of their pcodes.
	 */
	public int compareTo(Object o)
	{
		return toString().compareToIgnoreCase(o.toString());
	}

	/**
	 * Returns whether or not this identifier is
	 * equal to the specified identifier.
	 */
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		else
		{
			PatientIdentifier other = (PatientIdentifier) obj;
	
			return pCode.equalsIgnoreCase(other.getPCode());	
		}
	}

	/**
	 * Constructs a PatientIdentifier with no pid identifier,
	 * i.e. only containing the unique pcode.
	 */
	public PatientIdentifier(String pCode)
	{
		this(pCode, null);
	}

	/**
	 * Construct a PatientIdentifier with both a pcode and a
	 * pid identifier. If the pid identifier is null, an object
	 * with no pid and only a pcode is created.
	 */
	public PatientIdentifier(String pCode, String pid)
	{
		this.pCode = pCode;

		this.pid = pid;
	}

	private String pCode;

	private String pid;
}
