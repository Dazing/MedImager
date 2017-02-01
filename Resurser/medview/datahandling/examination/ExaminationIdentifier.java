/**

 * @(#)ExaminationData.java -> ExaminationIdentifier.java

 *

 * $Id: ExaminationIdentifier.java,v 1.21 2006/04/24 14:17:38 lindahlf Exp $

 *

 * $Log: ExaminationIdentifier.java,v $
 * Revision 1.21  2006/04/24 14:17:38  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.20  2004/11/04 20:07:42  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.19  2004/10/19 21:40:24  lindahlf
 * Lade tillbaka @deprecation eftersom det faktiskt hjälper om man har en bra IDE
 *
 * Revision 1.18  2004/10/01 16:39:49  lindahlf
 * no message
 *
 * Revision 1.17  2004/04/12 20:27:02  erichson
 * Wrote some additional javadoc to clear up difference between getExaminationID and getStringRepresentation.
 *
 * Revision 1.16  2004/04/12 20:24:59  erichson
 * Fixed a spelling error (d'oh)
 *
 * Revision 1.15  2004/04/12 20:24:28  erichson
 * Added getExaminationIDString() method, which is needed for MHC examination handling (in GenericExaminationIdentifier).
 * There is now some overlap between this functionality and getStringRepresentation() which should probably be cleaned up in a future meeting. // Nils
 *
 * Revision 1.14  2004/02/20 14:32:55  erichson
 * Rollback of commit 1.13 which was made by mistake, sorry. (The comment was regarding Aggregator) // NE
 *
 * Revision 1.12  2004/01/20 19:42:20  lindahlf
 * Major Upgrade PID support
 *
 * Revision 1.11  2002/10/14 09:15:47  erichson
 * getSeconds() -> getSecond(), added Log tag
 *
 */

package medview.datahandling.examination;

import java.io.*;

import java.util.*;

import medview.datahandling.*;

public interface ExaminationIdentifier extends Serializable
{
	/**
	 * Set the p-code.
	 * @deprecated Use setPID() instead.
	 */
	public void setPcode(String Pcode);

	/**
	 * Get the P-code
	 * @deprecated Use getPID() instead.
	 */
	public String getPcode();

	/**
	 * Set the patient identifier for which the
	 * examination applies.
	 */
	public void setPID(PatientIdentifier pid);

	/**
	 * Retrieve the patient identifier for which
	 * the examination applies.
	 */
	public PatientIdentifier getPID();

	public void setYear(int year);

	public int getYear();

	public void setMonth(int month);

	public int getMonth();

	public void setDay(int day);

	public int getDay();

	public void setHour(int hour);

	public int getHour();

	public void setMinute(int minute);

	public int getMinute();

	public void setSecond(int seconds);

	public int getSecond();

	public Date getTime();

	/**
	 * Show this unique examination as a string.
	 *
	 * This method should return a string representation which contains both the unique
	 * patient id as well as the examination id. (As opposed to getExaminationIDString()
	 * which should just return the examination id. // Nils
	 */

	public String getStringRepresentation();

	/**
	 * Get a string representation of the part that differentiates this examination from other examinations
	 * for the same patient. Examples of this is the date (in MedView) or löpnummer (for MHC).
	 *
	 * This is needed for MHC examination handling (in GenericExaminationIdentifier).
	 *
	 * NOTE: There is now some overlap between this functionality and getStringRepresentation() which should probably be cleaned up in a future meeting. // Nils
	 */
	public String getExaminationIDString();
}
