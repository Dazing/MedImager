/*
 * $Id: RowTools.java,v 1.1 2003/11/10 23:23:55 oloft Exp $
 *
 * Created on July 9, 2001, 8:25 PM
 */

package medview.formeditor.tools;

import java.io.*;
import java.util.*;

/**
 *
 * Package for handling "rows" in multi-line strings
 *
 * @author  Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1,0
 */
public class RowTools extends Object {

    public static int countRows(String text) {
        return getRows(text).length;
    }

    /**
     * Get all rows, including non-empty ones
     */
        
    public static String[] getRows(String text) {

        Vector v = new Vector();
        StringBuffer buffer = new StringBuffer();
        
        char[] chars = text.toCharArray();
        
        for (int i = 0; i < text.length(); i++) {
            if ((chars[i] == '\n') || (chars[i] == '\r')) {
                v.add(buffer.toString());
                
                buffer = new StringBuffer();
            } else {
            
                buffer.append(chars[i]);
                
            }
        }
        
        // Add the final (unfinished) row
        
        v.add(buffer.toString());
        
        String[] result = new String[v.size()];
        
        result = (String[]) v.toArray(result);
       
        return result;
        
    }
    
    /*
    public static String[] getRows(String text) {
    
        Vector v = new Vector();
        
        BufferedReader br = new BufferedReader(new StringReader(text));
        String row = "glafs";
        
        do {
            try {
                row = br.readLine();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            
            if (row != null) {
                v.add(row);
                // System.out.println("Got row: " + row);
            }
        } while (row != null);
            
        String[] result = new String[v.size()];
        result = (String[]) v.toArray(result);
        
        return result;
        
    }
      */  
    
      /** 
       * Get only non-empty rows
       */
     public static String[] getContentRows(String text) {
        String row;
        
        Vector v = new Vector();
        
        
        StringTokenizer st = new StringTokenizer(text,"\n");
        
        while (st.hasMoreTokens()) {
            // System.out.println("A token!");
            row = st.nextToken();
            if (row == null) row = "";
            
               // if (! (row.equals(""))) {
                    v.add(row);
                    //System.out.println("Got a row: [" + row + "]");
              //  }
                
            
        }
        
        String[] result = new String[v.size()];
        
        result = (String[]) v.toArray(result);
        
        return result;
    }

    
    /**
     * Fetch a row inside the string
     * @param rowNumber the row number to fetch (1 is the first row)
     */
    public static String getRow(String text, int rowNumber) throws NoSuchRowException {

       // System.out.println("getRow: find number " + rowNumber + " in text [" + text + "]");
        /*
        int thisIndex = 0;
        int nextIndex = 0;

        // Find positions of line returns rowNumber times
        for (int i=0; i < rowNumber; i++) {
            thisIndex = text.indexOf('\n',thisIndex+1);
            // If there aren't enough rows, throw exception
            if (thisIndex == -1) throw new NoSuchRowException("Row " + thisIndex + " does not exist");
        }

        
        
        // Find the position of an eventuel next linebreak
        nextIndex = text.indexOf('\n',thisIndex+1);

        // If there is no next linebreak, just return the rest
        if (nextIndex == -1) {
            if (thisIndex >= (text.length() -1)) {
                return "";
            }
            else {
                return text.substring(thisIndex+1);
            }
        } else { 
            return text.substring(thisIndex+1,nextIndex); // Return the substring between the two linebreaks
        }
         */
        
        String[] rows = getRows(text);
        //System.out.println("Rows.length = " + rows.length);
        if (rowNumber >= rows.length) throw new NoSuchRowException("No such row: " + rowNumber);
        
        return rows[rowNumber]; // array is indexed at 0  was -1
        
    }
    
    /** 
     * Which row is at position pos? I.e. How many linebreaks before pos in string str?
     * Returns -1 if illegal
     */
    public static int getRowNumber(String str, int pos) throws IndexOutOfBoundsException {        

        int number = countRows(str.substring(0,pos));
        if (number == -1) number = 0;
        
        return number;
        
        /*
        int matches = 0;        
        int lastMatch = 0;
        
        System.out.println("getRowNumber: got Debugging: Pos was " + pos + " and Text was " + str + " (" + str.length() + " characters)");
       
        
        // If the pos is after the last position, throw exception
        if (pos > (str.length() ) ) throw new IndexOutOfBoundsException(pos + " > " + (str.length() ));
        if (pos > 0) {
            String subString = str.substring(0,pos); // Get the substring before pos                
        
            // Count all the linebreaks in the suubstring by looping until indexOf returns -1
         while ( (lastMatch != -1) && (lastMatch < subString.length())) { 
             System.out.println("in while loop");
             lastMatch = subString.indexOf('\n',lastMatch+1);
                if (lastMatch != -1) { 
                matches = matches + 1; // Count the amount of linebreaks passed to get here
             }
            }
        }
        System.out.println("End of getrownumber");
        return matches; // The amount of linebreaks passed is the row number
         */
    }
    
    
    public static void main(String[] args) {
    
       String teststring = "Alltså\nDet\n\nVar\nSåhär\n\nDåva\n";
       System.out.println(teststring+"\nThat was " + countRows(teststring) +".");

       System.out.println("row at pos 27 = " + getRowNumber(teststring,20));
       System.out.println("row at pos 28 = " + getRowNumber(teststring,28));
       
       
    }
    
}
