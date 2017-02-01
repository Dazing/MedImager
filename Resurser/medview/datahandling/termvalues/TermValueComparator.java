/*
 * TermValueComparator.java
 *
 * Created on May 17, 2004, 3:23 PM
 *
 * $Id: TermValueComparator.java,v 1.5 2008/07/28 06:56:52 it2aran Exp $
 *
 * $Log: TermValueComparator.java,v $
 * Revision 1.5  2008/07/28 06:56:52  it2aran
 * * Package now includes
 * 	termdefinitions
 * 	termvalues
 * 	database
 * 	template
 * 	translator
 * and can be changed withour restarting (both in MSummary and MRecords
 * * removed more termdefinitions checks (the bug that slowed down MRecords) in MedSummary which should make it load faster
 *
 * Revision 1.4  2008/06/12 09:21:22  it2aran
 * Fixed bug:
 * -------------------------------
 * 413: Scrollar till felaktigt textfält om man sparar med felaktigt infyllt textfält.
 * 164: Tabbning mellan inputs scrollar hela formuläret så att den aktuella inputen alltid är synlig
 * Övrigt
 * ------
 * Parametrar -Xms128m -Xmx256m skickas till JVM för att tilldela mer minne så att större bilder kan hanteras
 * Mucositkomponenten helt omgjord. Utseendet passar bättre in samt att inga nollor sparas om inget är ifyllt.
 * Drag'n'drop för bilder fungerade ej och är borttaget tills vidare
 * Text i felmeddelandet vid inmatat värde utan att trycka på enter ändrat
 *
 * Revision 1.3  2004/10/27 11:18:19  erichson
 * Uses toString instead of String casting to avoid classcastexception
 *
 * Revision 1.2  2004/08/30 13:58:59  d97nix
 * Added checking for null and the empty string ("") since they were being (erroneously) sorted as between numerical and non-numerical strings. // NE
 *
 *
 */

package medview.datahandling.termvalues;

/**
 *    
 * Term Value comparator. Orders strings starting with integers numerically, after that alphabetically.
 * This is useful for terms such as age, which would otherwise be ordered as " 1, 10, 11, 2, 21, 3" etc but also 
 * terms such as smoke and alcohol that begin with values like "1 cl/vecka, 10 cl/vecka, 2 cl/vecka".
 *
 * null values and the empty string ("") are defined as less than everything.
 * 
 * As with String, compareTo applied to anything other than a String throws ClassCastException.
 *
 * Currently mainly used by Aggregator and Visualizer.
 *
 * Locked down as Singleton as there is no need to ever allocate more than one - use getInstance().
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.0
 */
 
public class TermValueComparator implements java.util.Comparator {
    
    /**
     * Instance field
     */
    private static TermValueComparator comparator = new TermValueComparator();
    
    /** Creates a new instance of TermValueComparator */
    private TermValueComparator() {
    }
 
    /** Singleton method */
    
    public static TermValueComparator getInstance() {
        return comparator;
    }
    
    /** Implementation of Comparator */
    
    /**
     * Compares two objects. See class documentation of TermvalueComparator.
     */
    public int compare(Object o1, Object o2) throws ClassCastException {        
        return compareTermValues( o1.toString(), o2.toString());
    }   
    
    /**
     * Compares this comparator to another comparator.
     */
    public boolean equals(Object otherComparator) {
        if ( (otherComparator != null) && (otherComparator instanceof TermValueComparator))
            return true;
        else
            return false;
    }
    
    private static int compareTermValues(String element1, String element2) {        
        
        // Handle null
        if (element1 == null && element2 == null)
        {
            return 0; 
        } else if ((element1 == null) || (element1.equals(""))) // Define null less than everything. 
        {
            return -1; 
        } else if ((element2 == null) || (element2.equals("")))
        { 
            return 1; 
        }
        
        // Get lengths of the parts of the string that are integers
        int int1length = getStartingIntegerLength(element1);
        int int2length = getStartingIntegerLength(element2);

        boolean firstBeginsWithInteger = (int1length > 0);
        boolean secondBeginsWithInteger = (int2length > 0);
                
        // If both begin with integers
        if (firstBeginsWithInteger && secondBeginsWithInteger) {            
            return compareStringsBeginningWithIntegers(element1, int1length, element2, int2length);
        }
        
        // First begins with integer but not second: Integer is less than string: Return "less than".
        if (firstBeginsWithInteger) {
            return -1; // Negative = _this_ has a lower value than _other_
        }
        
        // Same case but opposite
        if (secondBeginsWithInteger) {
            return 1; // Positive: _this_ has a higher value than _other_
        }
        
        // No matches so far = both are strings
        return element1.compareToIgnoreCase(element2);
    }       
    
    /**
     * Get the length of the initial segment of a string that makes an integer.
     */
    private static int getStartingIntegerLength(String str) {
        int strlen = str.length();
        if (strlen <= 0)
            return 0;
        
        int i = 0;
        while ( (i < strlen) && (Character.isDigit(str.charAt(i)))) 
        {
            i++;
        }
        
        return i;
    }
    
    /**
     * The actual comparison.
     */
    private static int compareStringsBeginningWithIntegers(String str1, int intlength1, String str2, int intlength2) {
    
        // First, extract integer part
        int int1 = 0;
        int int2 = 0;
        try
        {
            int1 = Integer.parseInt(str1.substring(0,intlength1));
            int2 = Integer.parseInt(str2.substring(0,intlength2));
        }
        catch(Exception e)
        {
            //the number is probably too big for us to handle, so we can't sort it correctly
            //it will be sorted as 0 instead
        }
    
        if (int1 == int2) // Do alphabetic comparison of the rests
        {
            String rest1 = str1.substring(intlength1);
            String rest2 = str2.substring(intlength2);
            return rest1.compareTo(rest2);
        } 
        else { // Compare the integers
            return new Integer(int1).compareTo(new Integer(int2));
        }
    }
}
