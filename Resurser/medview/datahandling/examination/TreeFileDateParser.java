/*
 * @(#)TreeFileDateParser.java
 *
 * $Id: TreeFileDateParser.java,v 1.3 2002/10/12 14:11:02 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling.examination;

import java.util.*;

public interface TreeFileDateParser
{
	/**
	 * Extracts the date as a date object from
	 * the specified date string. If the method
	 * returns a null date, this indicates that
	 * the date string could not be parsed and
	 * transformed into a Date object. Note that
	 * the passed-along node date should be stripped
	 * from any node-structure characters (such as
	 * the # and L/N characters), and should only
	 * be a normal date string in some format. An
	 * example: '2001-09-22 10:45' is a valid
	 * parameter to this method, but 'L2001-09-22
	 * 10:45##' is not.
	 */
	public Date extractDate(String nodeDate) throws CouldNotParseDateException;
}