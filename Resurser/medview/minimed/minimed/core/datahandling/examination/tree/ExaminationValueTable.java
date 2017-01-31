
/*
 * $Id: ExaminationValueTable.java,v 1.1 2006/05/29 18:32:53 limpan Exp $
 *
 * $Log: ExaminationValueTable.java,v $
 * Revision 1.1  2006/05/29 18:32:53  limpan
 * Added minimed code
 * ----------------------------------------------------------------------
 *
 * Revision 1.26  2005/09/09 15:40:44  lindahlf
 * Server cachning
 *
 * Revision 1.25  2005/05/20 12:28:46  erichson
 * Removed automatic deriving of age and gender
 *
 * Revision 1.24  2005/03/24 16:24:17  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.23  2004/11/15 15:43:41  erichson
 * Replaced to calls to deprecated methods with the new versions
 *
 * Revision 1.22  2004/02/20 16:48:42  erichson
 * Now uses MVDH.getAge(): There is no reason to have its own implementation here...
 *
 * Revision 1.21  2004/02/19 18:21:27  lindahlf
 * Major update patch 1
 *
 * Revision 1.20  2004/01/20 19:42:20  lindahlf
 * Major Upgrade PID support
 *
 * Revision 1.19  2003/08/16 15:16:02  erichson
 * Some cleanup.
 *
 * Revision 1.18  2003/07/07 22:57:19  erichson
 * Added better error checking to the constructor (now throws IOException), and updated the javadoc since it was incorrect
 *
 * Revision 1.17  2003/07/02 14:38:18  d99figge
 * No changes.
 *
 * Revision 1.16  2002/12/06 14:46:21  erichson
 * Removed 'datum' debug output
 *
 * Revision 1.15  2002/12/03 15:42:58  erichson
 * Added hack for 'old' tree file format which gave Age of -1800 etc
 *
 * Revision 1.14  2002/12/03 15:27:46  erichson
 * getPatientIdentifier is no longer part of ExaminationValueContainer, so it is not commented out
 *
 * Revision 1.13  2002/12/03 15:17:31  erichson
 * Added containsTerm implementation, commented out unused method getExaminationDate()
 *
 * Revision 1.12  2002/12/02 17:22:06  lindahlf
 * Added empty containsTerm() method in ExaminationValueTable to make code compilable - should be fixed by someone knowing the code. //Fredrik
 *
 * Revision 1.11  2002/10/22 17:23:29  erichson
 * Commented revision 1.10
 *
 * Revision 1.10  2002/10/11 15:01:34  lindahlf
 * ID-tags added and compilable. // Fredrik
 * // Revision 1.10 made these changes: removed getExaminationDate and getPatientIdentifier() // Nils
 *
 * Revision 1.9  2002/10/02 13:04:31  erichson
 * Fixed age to be examination-relative, and added derived term Gender
 *
 */

package minimed.core.datahandling.examination.tree;

import java.io.IOException;
import java.util.*;

import minimed.core.datahandling.*;
import minimed.core.datahandling.examination.*;

/**
 * A table containing values for a term. The values can be fetched in a variety of ways.
 * Implemented as a Hashtable containing Vectors of values. The keys used in the hashtable are Strings.
 * The table keys are case-insensitive.
 *
 * Note: Formerly known as AttributeShelf
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.2
 */

public class ExaminationValueTable implements minimed.core.datahandling.examination.ExaminationValueContainer
{
	public void internalize() // Added by Fredrik 050906
	{
		// TODO - implement this
	}

	public ExaminationIdentifier getExaminationIdentifier()
	{
		return null; // TODO - implement this
	}

    /**
     * Change dashes in a String to underscores
     * @param in_str the String to process
     * @return the new String
     */
    private static String fixDashes(String in_str) {
	return in_str.replace('-','_');
    }

    /**
     * Add a set of values to one of the terms in this table
     * @param term the term to add the values to
     * @param values the values to add
     */
    public void addValues(String term, String[] values) {

	// String term = in_term.to LowerCase();

	if (!hashTable.containsKey(term)) {
	    // Key(attrib) is not in the hashtable, add it with a new (empty) vector)
	    hashTable.put(term, new Vector());
	}

	// Now a vector exists in the table, get a reference it and add the value

	Vector valueVector = (Vector) hashTable.get(term); // Get a reference to the value vector

	for (int i = 0; i < values.length; i++) {
	    valueVector.add(values[i]); // add the value in this vector
	}

    }

    /**
     * Remove all values from a term
     * @param term the term to remove all values from
     */
    public void clearValues(String term) {
	// String term = in_term.to LowerCase();

	if (hashTable.containsKey(term)) // If the term doesn't exist: Don't add an empty vector, that is unnecessary
	    hashTable.put(term, new Vector());
    }



    /** Add a Collection of values to a term in this table.
     * @param term the name of the term to add the values to.
     * @param values the Collection of values to add to this Term.
     */
    public void addCollection(String term, Collection values) {

	// String term = in_term.to LowerCase();

	// check if the key (attrib) is in the hashtable
	if (!hashTable.containsKey(term)) {
	    hashTable.put(term, new Vector());
	}

	// A vector exists in the table, join it with the values
	Vector valueVector = (Vector) hashTable.get(term);
	valueVector.addAll(values);

    }

    /**
     * Inverse stringtokenizer: Converts a vector of strings to a single string with the former strings separated by a $.
     * @param v the vector (containing strings) to process.
     * @return a String made up of the strings in the vector, separated by $ signs
     */
    private static String dollarizeStringVector(Vector v) {
	StringBuffer buffer = new StringBuffer();

	for (Enumeration e = v.elements(); e.hasMoreElements(); ) {
	    String nextEl = (String) e.nextElement();
	    buffer.append(nextEl);
	    if (e.hasMoreElements()) {
		buffer.append("$");
	    }
	}
	return buffer.toString();
    }


    /**
     * Get all the terms for an examination, whether it has values or not
     * @return an array of terms
     */

    public String[] getAllTerms() {

	/* Basically just fetches the keys of the Hashtable as an array of Strings. */

	Set keySet = hashTable.keySet();
	String[] keyArray = new String[keySet.size()];
	keyArray = (String[]) keySet.toArray(keyArray);
	return keyArray;
    }

    /**
     * Get all terms that have values associated with them
     * @return the terms that have values associated with them
     */
    public String[] getTermsWithValues() {
	Vector termVector = new Vector();

	Set keySet = hashTable.keySet();
	for (Iterator it = keySet.iterator(); it.hasNext();) {
	    String nextTerm = (String) it.next(); // get next key to check
	    Vector valueVector = (Vector) hashTable.get(nextTerm); // get the mapped Vector of that key
	    if (valueVector != null) { // Do not include term if it has no vector mapped to it
		if (valueVector.size() > 0) { // Do not include term if the vector is empty
		    termVector.add(nextTerm); // The vector is not empty, so include this term
		}
	    }
	}

	// Return terms

	String[] termArray = new String[termVector.size()];
	termArray = (String[]) termVector.toArray(termArray);

	return termArray;
    }

    public boolean termHasValues(String term)
    {
	    Vector vect = (Vector) hashTable.get(term);

	    if ((vect != null) && (vect.size() > 0))
	    {
		    return true;
	    }
	    else
	    {
		    return false;
	    }
    }




    /** Get a subset table containing the terms specified in the array.
     * @param terms an array of the terms to include
     * @return the new table
     */

    public ExaminationValueTable getSubTable(String[] terms) {

	int termCount = terms.length;
	ExaminationValueTable newTable = new ExaminationValueTable();

	for (int i = 0; i < termCount; i++) {

	    // Get reference to the vector for this attribute from the old table
	    Vector valueVector;
	    try {
		valueVector = getValueVector(terms[i]);
	    } catch (NoSuchTermException e) {
		valueVector = null;
	    }

	    // If there was no value for this key, add an empty vector
	    if ((valueVector) == null) {
		// System.err.println("Warning! Value of " + terms[i] + " was null!");
		valueVector = new Vector();
	    }

	    // add the old vector to this term
	    newTable.addCollection(terms[i], valueVector);
	}
	if (debug)
	    System.err.println("getSubTable: Resulting subtable has key amount of : " + (newTable.getAllTerms().length));
	return newTable;
    }

    /** Get the first value of a term.
     * @param term the name of the term whose value to fetch
     * @return the first value of the term. If the term did not exist, returns "null"
     */
    public String getFirstValue(String term) throws NoSuchTermException {

	// String term = in_term.toL owerCase();
	if (hashTable.containsKey(term)) {
	    Vector valueVector = (Vector) hashTable.get(term);
	    if (valueVector == null) { // Key exists but no values
		return "null"; // No first value!
	    } else {
		String first = (String) valueVector.firstElement();
		return first;
	    }
	} else
	    throw new NoSuchTermException(term);
    }

    /** Get a Vector of the values of a term
     * @param term the name of the term whose values to fetch.
     * @return a Vector containing this term's values
     */
    public Vector getValueVector(String term) throws NoSuchTermException {
	//String term = in_term.to LowerCase();

	if (hashTable.containsKey(term)) {
	    Vector valueVector = (Vector) hashTable.get(term);
	    if (valueVector == null) {
		return new Vector();
	    } else {
		return valueVector;
	    }
	} else {
	    throw new NoSuchTermException(term);
	}
    }

    /** Get the values for this term as one string, with values separated by a dollar sign ('$').
     * @param termName the name of the term whose values to fetch.
     * @return A string composed of this term's values.
     */
    public String getDollarizedValueString(String termName) throws NoSuchTermException {
	// String termName = term.to LowerCase();


	Vector valueVector = getValueVector(termName);
	return dollarizeStringVector(valueVector);
    }


    /** Change the name of a term while keeping the values.
     * @param oldName the old name of the term
     * @param newName the new name for the term
     */

    public void changeTermName(String oldName, String newName) throws NoSuchTermException {

	//String oldName = in_oldName.toL owerCase();
	//String newName = in_newName.to LowerCase();


	Vector valueVector = getValueVector(oldName);   // Find the value of the original attribute

	// If exception is thrown, method call ends here

	hashTable.remove(oldName);                          // Remove the old key from the table
	hashTable.put(newName,valueVector);                 // Reinsert the valuevector with the new attribute name

    }

// ****************************************************************************
// ------------------------------ CHECKED METHODS -----------------------------
// ****************************************************************************

    /**
     * Get the values for this term as an Array.
     * @param term the name of the term whose values to fetch.
     * @return an Array of this attribute's values.
     */
    public String[] getValues(String term) throws NoSuchTermException
    {
	if (term == null)
	{
	    throw new NoSuchTermException("Cannot get values, term == null");
		}

	if (hashTable.containsKey(term))
	{
	    Vector valueVector = getValueVector(term);

	    if (valueVector == null) 	// key exists but no values
	    {
		return new String[0]; 	// array of length zero
	    }
	    else
	    {
		int valueCount = valueVector.size();

		if (valueCount < 1)
		{
		    return new String[0]; 	// array returned, but of length zero
				}
		else
		{
		    String[] valueArray = new String[valueCount];

		    for (int i = 0; i < valueCount; i++)
		    {
			valueArray[i] = (String) valueVector.get(i);
		    }

		    return valueArray;
		}
	    }
	}
	else 	// hashTable did not contain the term
	{
	    throw new NoSuchTermException(term);
		}
    }

    /**
     * Create a String view of this table
     * @return a String view of this table
     */
    public String toString()
    {
	StringBuffer output = new StringBuffer();

	String[] terms = getAllTerms();

		Arrays.sort(terms);		// Fredrik 040215

	for (int i = 0; i < terms.length; i++)
	{
	    output.append(terms[i]);

	    output.append(": ");

	    try
	    {
		String valueString = getDollarizedValueString(terms[i]);

		output.append(valueString);
	    }
	    catch (NoSuchTermException e)
	    {
		output.append("NoSuchTermException: " + terms[i]);
	    }

	    output.append("\n");
	}

	return output.toString();
    }

    public void setImagePaths(String[] paths) {
	clearValues("Photo");
	addValues("Photo",paths);
    }

    public String[] getImagePaths() {
	try {
	    String[] paths = getValues("Photo");
	    if (paths == null)
		return new String[0];
	    else
		return paths;
	} catch (NoSuchTermException e) {
	    return new String[0]; // No image paths
	}
    }

    /**
     * Gets whether this table contains a given term at all
     * @param term the term to look for
     * @return whether this table contains the term
     */
    public boolean containsTerm(String term)
    {
	return hashTable.containsKey(term);
    }

// ****************************************************************************
// ----------------------------------------------------------------------------
// ****************************************************************************



// ****************************************************************************
// ------------------------------ INITIALIZATION ------------------------------
// ****************************************************************************

    /**
     * Add the contents of a tree to this object
     */
    private void addTree(Tree tree, boolean includeTermsWithoutValues, String parentTerm)
    {
	String value = tree.getValue();

	if (tree.isLeaf())		// check root node type
	{
	    addValue(parentTerm,value); // leaf, add the value
	}
	else 	// branch, process children
	{
	    if (includeTermsWithoutValues)
	    {
		addValue(value,null);
			}

	    for (Iterator it = tree.getChildrenIterator(); it.hasNext();)	// process child trees
	    {
		Tree nextTree = (Tree) it.next();

		addTree(nextTree,includeTermsWithoutValues, value);
	    }
	}
    }

    /**
     * Add one value to a Term in this table.
     */
    public void addValue(String term, String value)
    {
	if (!hashTable.containsKey( term))
	{
	    hashTable.put(term, new Vector());
	}

	if ((value != null) && (!value.equals("")))
	{
	    Vector valueVector = (Vector) hashTable.get(term);

	    valueVector.add(value);
	}
    }

    /**
     * Create a new (empty) ExaminationValueTable
     */
    public ExaminationValueTable()
    {
	hashTable = new Hashtable();
    }

    /**
     * Construct a table from a tree of MedView data,
     * defaults to including all the terms.
     * @param tree the tree of Nodes to process
     * @throws IOException if an error occurs, such as
     * invalid date or p-code or missing mandatory terms
     */
    public ExaminationValueTable(Tree tree) throws IOException
    {
	this(tree,INCLUDE_TERMS_WITHOUT_VALUES);
    }

    /**
     * Construct a table from a tree of MedView data
     * @param tree the tree of Nodes to process
     * @param includeTermsWithoutValues whether to include
     * terms without values or not.
     * @throws IOException if an error occurs, such as
     * invalid date or p-code or missing mandatory terms
     */
    private ExaminationValueTable(Tree tree, boolean includeTermsWithoutValues) throws IOException
    {
	this();

	addTree(tree, includeTermsWithoutValues, null); // initial parentTerm is null - no leaves on top level

	/* Now add the derived terms */

	/*
	try

	 {
	    String p_code = getFirstValue("P-code");

	    int age = -1;
	    String genderString = "Unknown";
	    try {
		PatientIdentifier pid = new PatientIdentifier(p_code);
		age = MVDH.getAge(pid);            // new PatientIdentifier throws InvalidPIDException

		int gender = MVDH.getGender(pid);

		switch (gender)
		{
		    case PIDParser.MALE:
		    {
			genderString = PIDParser.MALE_STRING;
			break;
		    }
		    case PIDParser.FEMALE:
		    {
			genderString = PIDParser.FEMALE_STRING;
			break;
		    }
		}
	    } catch (InvalidPIDException iPIDe)
	    {
		System.err.println("new ExaminationValueTable: Invalid p-code (could not parse age/gender): " + p_code);
		// Do nothing - age will default to -1 and gender to "Unknown" as defined above, before the try statement
	    }

	    addValue("Age", new Integer(age).toString());
	    addValue("Gender",genderString);

	}
	catch (NoSuchTermException nste)
	{
	    throw new IOException("ExaminationvalueTable() error: term did not exist: " + nste.getMessage());
	}

	*/
    }

    private Hashtable hashTable;

    private static final boolean debug = false;

    private static final boolean INCLUDE_TERMS_WITHOUT_VALUES = false;

//    private static MedViewDataHandler MVDH = MedViewDataHandler.instance();

}
