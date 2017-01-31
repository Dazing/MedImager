/*
 * TextTableFile.java
 *
 * Created on den 29 juli 2004, 18:44
 *
 * $Id: TextTableFile.java,v 1.2 2005/05/20 11:14:29 erichson Exp $
 *
 * $Log: TextTableFile.java,v $
 * Revision 1.2  2005/05/20 11:14:29  erichson
 * Now removes quotation marks when creating the matrix
 *
 * Revision 1.1  2004/09/09 10:33:08  erichson
 * First check-in.
 *
 */

package medview.datahandling.examination.tablefile;

import java.io.*;
import java.util.*;

import misc.foundation.util.*;

/**
 * Table file implementation for text files where columns are separated by a delimiter, typically tab or a semicolon.
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class TextTableFile extends AbstractTableFile {
    
    public static final String DEFAULT_FIELD_DELIMITER = ";";
    
    private String fieldDelimiter;
    
    /** Creates a new instance of TextTableFile */
    public TextTableFile(File file, String delimiter) 
    {
        super(file);
        this.fieldDelimiter = delimiter;
    }
    
    public SparseMatrix getDataMatrix() throws IOException 
    {
        StringSparseMatrix dataMatrix = new StringSparseMatrix();
        
        // Open file for reading
        // Navigate to correct column
        BufferedReader reader = new BufferedReader(new FileReader(theFile));
        Vector v = new Vector();
        
        // boolean isFirstLine = true;
        while (reader.ready()) // Has more lines to read
        {                    
            v.clear();
  
            String nextLine = reader.readLine();
            
            // Chop up the line into fields
            StringTokenizer tokenizer = new StringTokenizer(nextLine, fieldDelimiter);
            
            while (tokenizer.hasMoreTokens())
            {
                String s = tokenizer.nextToken();
                
                // Remove leading and ending "" marks
                if (s.startsWith("\""))
                    s = s.substring(1); // chop off first
                if (s.endsWith("\""))
                    s = s.substring(0,s.length()-1); // chop off last                                
                
                v.add(s);
            }
            
            // Convert vector to string for addition into matrix
            String[] rowArray = new String[v.size()];
            rowArray = (String[]) v.toArray(rowArray);                        
            dataMatrix.addRow(rowArray);
        }
        return dataMatrix;
    }    
}
