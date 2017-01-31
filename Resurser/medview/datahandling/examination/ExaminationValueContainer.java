/*

 * @(#)ExaminationValueContainer.java

 *

 * $Id: ExaminationValueContainer.java,v 1.15 2005/09/09 15:40:48 lindahlf Exp $

 *

 * $Log: ExaminationValueContainer.java,v $
 * Revision 1.15  2005/09/09 15:40:48  lindahlf
 * Server cachning
 *
 * Revision 1.14  2005/06/03 15:49:04  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.13  2005/03/24 16:25:10  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.12  2005/02/24 16:31:54  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.11  2005/02/16 11:16:16  erichson
 * Added addValue to the interface.
 *
 * Revision 1.10  2004/11/19 12:32:29  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.9  2003/08/19 16:03:15  lindahlf
 * See 030819_Release_Notes_DATAHANDLING_MISC_COMMON.pdf
 *
 * Revision 1.8  2002/11/26 17:14:23  lindahlf
 * Ordnade till generatorn och parse trädet ytterligare - Fredrik
 *
 * Revision 1.7  2002/11/25 15:30:47  lindahlf
 * Removed derived term type 'pcode', added derived term handling // Fredrik
 *
 * Revision 1.6  2002/10/22 17:22:53  erichson
 * Added Log cvs tag
 *
 * // Revision 1.3 made these changes: removed getExaminationDate and getPatientIdentifier() // Nils
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling.examination;

import medview.datahandling.*;

/**
 * A container representing the values entered
 * for the terms in an examination.
 *
 * @author Fredrik Lindahl
 */
public interface ExaminationValueContainer
{
	/**
	 * Retrieve all values entered for a certain
	 * term in a certain examination. Case should be
	 * insignificant.
	 *
	 * @return an array of values entered in the
	 * examination for the specified term. Can return
	 * an empty array if no values have been entered
	 * for the term, a null return value must also be
	 * handled by the using class.<br>
	 * <br>
	 * For instance:<br>
	 * <br>
	 * ["France"]<br>
	 * ["son", "father", "mother"]<br>
	 * ["15"]<br>
	 * <i>null</i><br>
	 * []<br>
	 *
	 * @param term the term for which the values
	 * entered in the examination represented by
	 * the container are to be retrieved.
	 * @throws NoSuchTermException thrown when trying to get
	 * values for a term that does not exist at all in this
	 * examination
	 */
	String[] getValues(String term) throws NoSuchTermException;

	/**
	 * Retrieve all terms that have values in this examination.
	 * @return an array of all terms that have values. The terms
	 * should be returned in lower-case.
	 */
	String[] getTermsWithValues();

	/**
	 * Whether or not the specified term has values in this
	 * value container. Case should be insignificant.
	 * @param term String
	 * @return boolean
	 */
	boolean termHasValues(String term);

	/**
	 * Adds a value to a term. This method should perhaps be placed
	 * in an interface extension to EVC called MutableExaminationValueContainer
	 * or something like that (TODO).
	 * @param term String
	 * @param value String
	 */
	void addValue(String term, String value);

	/**
	 * Call this method to internalize the strings kept in
	 * this examination value container. This is necessary due
	 * to RMI issues, specifically that reconstruction of an
	 * internalized EVC over RMI does not re-internalize the
	 * strings on the client.
	 */
	void internalize(); // -- Added by Fredrik 050906

	/**
	 * Returns the examination identifier of the examination this
	 * value container represents.
	 * @param eid ExaminationIdentifier
	 */
	ExaminationIdentifier getExaminationIdentifier();
}
