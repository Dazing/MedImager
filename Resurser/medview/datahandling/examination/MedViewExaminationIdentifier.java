/*
 * MedrecordsExaminationIdentifier.java
 *
 * Created on September 9, 2001, 4:49 PM
 *
 * $Id: MedViewExaminationIdentifier.java,v 1.21 2004/11/09 21:13:50 lindahlf Exp $
 *
 * $Log: MedViewExaminationIdentifier.java,v $
 * Revision 1.21  2004/11/09 21:13:50  lindahlf
 * Datalayer upgrade in connection with server development
 *
 * Revision 1.20  2004/10/19 21:40:29  lindahlf
 * Lade tillbaka @deprecation eftersom det faktiskt hjälper om man har en bra IDE
 *
 * Revision 1.19  2004/10/12 15:35:45  erichson
 * Made the hashcode be the hashcode of the string representation.
 *
 * Revision 1.18  2004/10/01 16:39:49  lindahlf
 * no message
 *
 * Revision 1.17  2004/04/12 20:33:57  erichson
 * Added getExaminationIDString().
 * Added javadoc for getExaminationIDString() and getStringRepresentation()
 *   (pasted from ExaminationIdentifier)
 *
 * Revision 1.16  2004/02/28 17:51:26  lindahlf
 * no message
 *
 * Revision 1.15  2004/02/23 13:41:33  erichson
 * Added extra pid checking for equals(). Also tried to clear up confusion between argument variable pcode and member variable pcode.
 *
 * Revision 1.14  2004/01/20 19:42:20  lindahlf
 * Major Upgrade PID support
 *
 * Revision 1.13  2003/08/19 16:03:15  lindahlf
 * See 030819_Release_Notes_DATAHANDLING_MISC_COMMON.pdf
 *
 * Revision 1.12  2002/11/19 00:07:48  lindahlf
 * Added preference functionality. - Fredrik
 *
 * Revision 1.11  2002/11/12 10:48:09  zachrisg
 * Fixed some compilation problems.
 *
 * Revision 1.10  2002/11/12 10:33:50  erichson
 * added createExaminationIdentifierFromStringRepresenation, removed old unused code
 *
 * Revision 1.9  2002/10/22 17:26:00  erichson
 * Removed some very old commented out code
 *
 * Revision 1.8  2002/10/14 09:18:53  erichson
 * 1. Changed constructor argument from seconds to second, to have a consistent syntax.
 * 2. Removed getSeconds() so that all time methods have consistent syntax.
 * 3. Updated equals method to be safe for changes in the Date class.
 *
 * Some of the functionality may be moved to TreeFileHandler in the future, since MedViewExaminationIdentifier should
 * be independent of the PatientIdentifier format.
 *
 */

package medview.datahandling.examination;

import java.io.*;

import java.util.*; // Vector, Calendar
import java.text.*;

import medview.datahandling.*; // PatientIdentifier
import medview.datahandling.examination.*;

/**
 * MedView-specific implementation of the interface ExaminationIdentifier
 * Which provides an an unique identification of an examination
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.1
 */

public class MedViewExaminationIdentifier implements ExaminationIdentifier, Serializable {

    private GregorianCalendar date;
    private Vector imagePaths;

    /** @deprecated use PatientIdentifier concept instead. */
    private String pcode = null;

    private PatientIdentifier pid = null;

    /** Creates new MedrecordsExaminationIdentifier
     * @deprecated Use PatientIdentifier constructor instead.
     * @param in_pcode
     * @param in_date
     */
    public MedViewExaminationIdentifier(String in_pcode) {
	date = new GregorianCalendar();
	imagePaths = new Vector();
	pcode = in_pcode;
    }

	/**
	 * @deprecated Use PatientIdentifier constructor instead
	 */
    public MedViewExaminationIdentifier(String in_pcode, Date in_date) {
	this(in_pcode);
	date.setTime(in_date);
    }

    /**
     * @deprecated Use PatientIdentifier constructor instead.
     * @param in_month month (1-12)
     */
    public MedViewExaminationIdentifier(String in_pcode, int in_year, int in_month, int in_day, int in_hour, int in_minute, int in_second) {
	this(in_pcode);
	date.set(in_year,in_month-1,in_day,in_hour,in_minute,in_second); // -1 because in month is 1-12 and local storage is 0-11
    }



    /**
     * Constructs a MedViewExaminationIdentifier.
     */
    public MedViewExaminationIdentifier(PatientIdentifier pid)
    {
		date = new GregorianCalendar();
		imagePaths = new Vector();
		this.pid = pid;
	}

    /**
     * Constructs a MedViewExaminationIdentifier.
     */
	public MedViewExaminationIdentifier(PatientIdentifier pid, Date in_date)
	{
		this(pid);
		date.setTime(in_date);
	}

    /**
     * Constructs a MedViewExaminationIdentifier.
     */
	public MedViewExaminationIdentifier(PatientIdentifier pid, int in_year, int in_month, int in_day, int in_hour, int in_minute, int in_second)
	{
		this(pid);
		date.set(in_year,in_month-1,in_day,in_hour,in_minute,in_second);
	}

    // This should not exist, we do not want incompatible code...

//    // this constructor will set second to 0
//    public MedViewExaminationIdentifier(String in_pcode, int in_year, int in_month, int in_day, int in_hour, int in_minute) {
//        this(in_pcode,in_year,in_month,in_day,in_hour,in_minute,0);
//    }


    public void setYear(int year) {
	date.set(Calendar.YEAR,year);
    }

    public int getYear() {
	return date.get(Calendar.YEAR);
    }

    /**
     * @param month The month (1-12, as opposed to GregorianCalendar's 0-11)
     */
    public void setMonth(int month) {
	date.set(Calendar.MONTH,month-1);
    }

    /**
     * @return month The month (1-12, as opposed to GregorianCalendar's 0-11)
     */
    public int getMonth() {
	return date.get(Calendar.MONTH) +1;
    }

    public void setDay(int day) {
	date.set(Calendar.DAY_OF_MONTH,day);
    }

    public int getDay() {
	return date.get(Calendar.DAY_OF_MONTH);
    }

    public void setHour(int hour) {
	date.set(Calendar.HOUR_OF_DAY,hour);
    }

    public int getHour() {
	int hour = date.get(Calendar.HOUR_OF_DAY);
	//System.out.println("MedrecordsExaminationData.getHour() ger ut: " + hour);
	return hour;
    }

    public void setMinute(int minute) {
	date.set(Calendar.MINUTE,minute);
    }

    public int getMinute() {
	return date.get(Calendar.MINUTE);
    }

    public void setSecond(int seconds) {
	date.set(Calendar.SECOND,seconds);
    }

    public int getSecond() {
	return date.get(Calendar.SECOND);
    }

	/**
	 * @deprecated use PatientIdentifier methods instead.
	 */
    public void setPcode(String newPcode) {
	if (pid != null)
	{
			setPID(new PatientIdentifier(newPcode));
		}
		else
		{
			pcode = newPcode;
		}
    }

	/**
	 * @deprecated use PatientIdentifier methods instead.
	 */
    public String getPcode() {
		if (pid != null)
		{
			return pid.getPCode();
		}
		else
		{
			return pcode;
		}
    }



	/**
	 * Sets the patient identifier identifying the examination.
	 */
    public void setPID(PatientIdentifier pid)
    {
		this.pid = pid;
	}

	/**
	 * Obtains the patient identifier identifying the examination.
	 */
	public PatientIdentifier getPID()
	{
		return pid;
	}



    // For matching examinations
    public String getReducedTreefileNameDateString() {
	return (new SimpleDateFormat("yyMMddHHmm")).format(date.getTime()); // Remove coupling to TreeFileHandler - Fredrik
    }

    public Date getTime() {
	return date.getTime();
    }


    public String getTreefileNameDateString()
    {
	return (new SimpleDateFormat("yyMMddHHmmss")).format(date.getTime()); // Remove coupling to TreeFileHandler - Fredrik
    }


    /**
     * Show this unique examination as a string.
     *
     * This method should return a string representation which contains both the unique
     * patient id as well as the examination id. (As opposed to getExaminationIDString()
     * which should just return the examination id. // Nils
     */
    public String getStringRepresentation()
    {
		if (pid == null)
		{
			return pcode + "_" + getTreefileNameDateString();
		}
		else
		{
			return pid.getPCode() + "_" + getTreefileNameDateString();
		}
    }

    public int hashCode()
    {
	return getStringRepresentation().hashCode();
    }
    
    /**
     * Get a string representation of the part that differentiates this examination from other examinations
     * for the same patient. Examples of this is the date (in MedView) or löpnummer (for MHC).
     *
     * This is needed for MHC examination handling (in GenericExaminationIdentifier).
     *
     * NOTE: There is now some overlap between this functionality and getStringRepresentation() which should probably be cleaned up in a future meeting. // Nils
     */
    public String getExaminationIDString() {
	return getTreefileNameDateString();
    }

    public static MedViewExaminationIdentifier createExaminationIdentifierFromStringRepresentation(String stringRepresentation)
	throws ParseException
    {
	StringTokenizer tokenizer = new StringTokenizer(stringRepresentation,"_");
	String token_pcode = tokenizer.nextToken();
	if (token_pcode == null)
	    throw new ParseException("createExIdfromString: token_pcode == null", 1);

	String treefileNameDateString = tokenizer.nextToken();
	if (treefileNameDateString == null)
	    throw new ParseException("createExIdfromString: nameDateString == null", 2);

	Date date = (new SimpleDateFormat("yyMMddHHmmss")).parse(treefileNameDateString); // Remove coupling to TreeFileHandler - Fredrik

	return new MedViewExaminationIdentifier(new PatientIdentifier(token_pcode),date);
    }


    public String toString() {
	return getStringRepresentation();
    }


    /**
     * Test if two MedViewExaminationIdentifiers are equal. Equality is defined as having equal P-CODE and equal Date
     */
    public boolean equals(Object otherObject) {

	if (otherObject instanceof ExaminationIdentifier) {
	    ExaminationIdentifier otherIdentifier = (ExaminationIdentifier) otherObject;

	    /* To make sure that we only compare the parts that are interesting (only the date fields) we parse the date to a string containing just date and time.
	       The reason for this is that Date may contain other things like time zone, daylight savings etc that might be undefined */

	    String dateString = (new SimpleDateFormat("yyMMddHHmmss")).format(date.getTime()); // Remove coupling to TreeFileHandler - Fredrik
	    String otherString = (new SimpleDateFormat("yyMMddHHmmss")).format(otherIdentifier.getTime()); // Remove coupling to TreeFileHandler - Fredrik

	    boolean dateEqual = (dateString.equals(otherString)); // String because there may be more to the Date class

	    boolean pcodeEqual;

	    PatientIdentifier otherPid = otherIdentifier.getPID();
	    if ((pid != null) && (otherPid != null)) // Pid exists for both
	    {
		pcodeEqual = pid.equals(otherPid);
	    }
	    else // Pid doesn't exist, compare p-codes instead
	    {
		pcodeEqual = getPcode().equals(otherIdentifier.getPcode());
		//System.out.println("pcode1 = " + pcode + ", otherIdentifier.getPcode = " + other.getPcode());
	    }

	    // Output statements for debugging purposes only
	    // System.out.println("date1 = " + date.getTime() + " date2 = " + otherIdentifier.getTime());
	    // System.out.println("date equal= " + dateEqual + ", pcodeEqual = " + pcodeEqual);

	    return ( dateEqual && pcodeEqual);
	} else {
	    return false;
	}
    }
}
