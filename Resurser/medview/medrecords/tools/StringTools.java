/*
 * $Id: StringTools.java,v 1.2 2002/10/21 10:54:04 nazari Exp $
 *
 * Created on August 11, 2001, 5:39 PM
 */

package medview.medrecords.tools;

/**
 *
 * @author  nils
 * @version 
 */
public class StringTools extends Object {

    /** Creates new StringTools */
    public StringTools() {
    }

    /**
     * Strips the extension from a filename, i.e. remove the final dot and everything after it
     */
    
    public static String stripExtension(String filename) {
     // Show the filename but not the extension
            
        int dotPosition = filename.lastIndexOf('.');
            String result = null;
            if (dotPosition > 0) {
                result = filename.substring(0,dotPosition);
            } else {
                result = filename;
            }
            
        return result;
    }
    
    
    
}
