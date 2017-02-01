
/*
 *
 */

package medview.meduweb.data;

import java.util.*;
import medview.datahandling.*;
import medview.datahandling.examination.tree.*;

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

public class MeduwebExaminationValueTable implements medview.datahandling.examination.ExaminationValueContainer {

    // private static debugcount = 0;

    private static final boolean debug = false; // Set to true to enable debug messages

    private static final boolean INCLUDE_TERMS_WITHOUT_VALUES=false; // new experimental, saves memory. Hopefully will be compatible with fredrik's code.
    //private static final boolean INCLUDE_TERMS_WITHOUT_VALUES=true; // old and safe, but chews a lot of memory.

    private Hashtable hashTable;

    private static MeduwebDataHandler MVDH = MeduwebDataHandler.instance();


    /**
     * Construct a table from a tree of MedView data
     * @param tree the tree of Nodes to process
     * @param includeTermsWithoutValues whether to include terms without values or not.
     */
    private MeduwebExaminationValueTable(Tree tree, boolean includeTermsWithoutValues) {
        this();
        addTree(tree, includeTermsWithoutValues, null); // Initial parentTerm is null - no leaves on top level

        /* Add derived terms */

        try {

            String p_code = getFirstValue("P-code"); // Get p-code
            int birthYear = MVDH.getYearOfBirth(p_code); // Get year of birth

                String datum = getFirstValue("Datum");       // Get the examination date
            try {
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(TreeFileHandler.TREEFILE_DATUM_FIELD_DATE_FORMAT.parse(datum)); // Examination date
                int examinationYear = calendar.get(Calendar.YEAR);
                
                /* Hack to compensate for the 'old' medview format, where year was stored with only two digits */
                if (examinationYear < 100) // 'old' medview format, 98-06-16 (new format prints the entire year)
                    examinationYear = examinationYear + 1900;                
                

                int age = examinationYear - birthYear;
                //System.out.println("Datum = " + datum + ", exYear = " + examinationYear + ", birthyear = " + birthYear);
                // Store Age as a field
                //int age = MedViewDataHandler.instance().get Age(p_code);
                addValue("Age", String.valueOf(age));
            } catch (java.text.ParseException pe) {
                System.err.println("ExaminationValueTable: Could not parse Datum field="+datum);
            }
            int gender = MVDH.getGender(p_code);
            switch (gender) {
                case PCodeParser.MALE:
                    addValue("Gender",PCodeParser.MALE_STRING);
                    break;
                case PCodeParser.FEMALE:
                    addValue("Gender",PCodeParser.FEMALE_STRING);
                    break;
                // default: nothing (will return n/a)
            }

        } catch (InvalidPCodeException ipce) {
            System.err.println("ExaminationvalueTable constructor error: Invalid pcode(" + ipce.getMessage());
        } catch (NoSuchTermException nste) {
            System.err.println("ExaminationvalueTable constructor: p-code term did not exist");
        }


    }

    public MeduwebExaminationValueTable() {
        hashTable = new Hashtable();
    }

    /**
     * Construct a table from a tree of MedView data, defaults to including all the terms.
     * @param tree the tree of Nodes to process
     */
    public MeduwebExaminationValueTable(Tree tree) {
        this(tree,INCLUDE_TERMS_WITHOUT_VALUES);
    }

    /**
     * Add the contents of a tree to this examinationvaluetable
     */
    private void addTree(Tree tree, boolean includeTermsWithoutValues, String parentTerm) {

        // Get the value of this Node

        String value = tree.getValue(); // Node name or leaf value

        if (tree.isLeaf()) { // check root node type
            // System.out.println("Adding value " + value);
            addValue(parentTerm,value); // It's a leaf, add the value
        } else { // It's a branch, process the children
            if (includeTermsWithoutValues)
                addValue(value,null); // If we allow terms without values, store this term (name of the branch), but with empty value

            // Process the child trees of this node
            for (Iterator it = tree.getChildrenIterator(); it.hasNext();) {
                Tree nextTree = (Tree) it.next();
                addTree(nextTree,includeTermsWithoutValues, value); // next parentTerm is this node's value
            }
        }
    }


    /**
     * Change dashes in a String to underscores
     * @param in_str the String to process
     * @return the new String
     */
    private static String fixDashes(String in_str) {
        return in_str.replace('-','_');
    }

    /** Add one value to a Term in this table.
     * @param in_term the name of the term to add the value to.
     * @param value the value to add to this term
     */

    public void addValue(String term, String value) {

        // String term = in_term.to LowerCase();

        // check if the key (term) is in the hashtable
        if (! hashTable.containsKey( term)) {
            // Key(attrib) is not in the hashtable, add it with a new (empty) vector)
            hashTable.put(term, new Vector());
        }

        // Now a vector exists in the table, get a reference it and add the value

        if ((value != null) && (!value.equals(""))) { // Don't store null or ""

            Vector valueVector = (Vector) hashTable.get(term); // Get a reference to the value vector
            valueVector.add(value); // add the value in this vector
        }
    }

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
     */
    public void clearValues(String term) {
        // String term = in_term.to LowerCase();

        if (hashTable.containsKey(term)) // If the term doesn't exist: Don't add an empty vector, that is unnecessary
            hashTable.put(term, new Vector());
    }



    /** Add a Collection of values to a term in this table.
     * @param in_term the name of the attribute to add the values to.
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



    /** Get a subset table containing the terms specified in the array.
     * @param terms an array of the terms to include
     * @return the new table
     */

    public MeduwebExaminationValueTable getSubTable(String[] terms) {

        int termCount = terms.length;
        MeduwebExaminationValueTable newTable = new MeduwebExaminationValueTable();

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
     * @param in_term the name of the term whose value to fetch
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
     * @param in_term the name of the term whose values to fetch.
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

    /** Get the values for this term as an Array.
     * @param in_term the name of the term whose values to fetch.
     * @return an Array of this attribute's values.
     */

    public String[] getValues(String term) throws NoSuchTermException {

        if (term == null)
            throw new NoSuchTermException("Cannot get values, term == null");

        // String term = in_term.to LowerCase();

        if (hashTable.containsKey(term)) {
            Vector valueVector = getValueVector(term);
            if (valueVector == null) { // Key exists but no values
                return new String[0]; // array of length zero
            } else {
                int valueCount = valueVector.size();
                if (valueCount < 1)
                    return new String[0]; // Array returned, but of length zero
                else {
                    String[] valueArray = new String[valueCount];

                    for (int i = 0; i < valueCount; i++) {
                        valueArray[i] = (String) valueVector.get(i);
                    }

                    return valueArray;
                }
            }
        } else // hashTable did not contain the term
            throw new NoSuchTermException(term);
    }

    /** Get the values for this term as one string, with values separated by a dollar sign ('$').
     * @param term the name of the term whose values to fetch.
     * @return A string composed of this term's values.
     */
    public String getDollarizedValueString(String termName) throws NoSuchTermException {
        // String termName = term.to LowerCase();


        Vector valueVector = getValueVector(termName);
        return dollarizeStringVector(valueVector);
    }

    /** Change the name of a term while keeping the values.
     * @param in_oldName the old name of the term
     * @param in_newName the new name for the term
     */

    public void changeTermName(String oldName, String newName) throws NoSuchTermException {

        //String oldName = in_oldName.toL owerCase();
        //String newName = in_newName.to LowerCase();


        Vector valueVector = getValueVector(oldName);   // Find the value of the original attribute

        // If exception is thrown, method call ends here

        hashTable.remove(oldName);                          // Remove the old key from the table
        hashTable.put(newName,valueVector);                 // Reinsert the valuevector with the new attribute name

    }

    /**
     * Create a String view of this table
     * @return a String view of this table
     */

    public String toString() {

        StringBuffer output = new StringBuffer();

        String[] terms = getAllTerms(); // Get all the attributes

        // Loop through all the attributes
        for (int i = 0; i < terms.length; i++) {
            output.append(terms[i]);
            output.append(": ");
            try {
                String valueString = getDollarizedValueString(terms[i]);
                output.append(valueString);
            } catch (NoSuchTermException e) {
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

    public boolean containsTerm(String term) {
        return hashTable.containsKey(term);
    }
}
