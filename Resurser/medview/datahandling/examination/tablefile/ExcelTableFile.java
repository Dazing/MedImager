/*
 * ExcelTableFile.java
 *
 * Created on den 26 juli 2004, 17:06
 *
 * $Id: ExcelTableFile.java,v 1.2 2005/05/20 11:14:01 erichson Exp $
 *
 * $Log: ExcelTableFile.java,v $
 * Revision 1.2  2005/05/20 11:14:01  erichson
 * Now removes quotation marks when creating the matrix
 *
 * Revision 1.1  2004/09/09 10:35:20  erichson
 * First check-in.
 *
 */

package medview.datahandling.examination.tablefile;

import java.io.*;
import jxl.*;

import misc.foundation.util.*;

/**
 * TableFile implementation for Excel files.
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class ExcelTableFile extends AbstractTableFile {
    
    /** Excel storage */
    private Workbook workbook;
    private Sheet sheet;
            
    /** Creates a new instance of ExcelTableFile */
    public ExcelTableFile(File excelFile) throws IOException 
    {     
        super(excelFile);
        try {
            workbook = Workbook.getWorkbook(excelFile);        
            sheet = workbook.getSheet(0);        
        } catch (jxl.read.biff.BiffException be)
        {
            throw new IOException("Could not read workbook " + excelFile.getName() + " because of BiffException: " + be.getMessage());
        }
    }
    
    public SparseMatrix getDataMatrix()
    {
        StringSparseMatrix dataMatrix = new StringSparseMatrix();
        for (int i = 0; i < getRowCount(); i++)
        {
            // Add row to matrix
            Cell[] cellArray = sheet.getRow(i);
            
            // Extract string array
            String[] stringArray = new String[cellArray.length];
            for (int c = 0; c < cellArray.length; c++)
            {
                String s = cellArray[c].getContents();
                
                // Remove leading and ending "" marks
                if (s.startsWith("\""))
                    s = s.substring(1); // chop off first
                if (s.endsWith("\""))
                    s = s.substring(0,s.length()-1); // chop off last
                
                stringArray[c] = s;
            }
            
            dataMatrix.addRow(stringArray);
        }
        return dataMatrix;
    } 
    
    
    private int getColumnCount() 
    {
        return sheet.getColumns();
    }
    
    private int getRowCount()
    {
        return sheet.getRows();
    }
                
    public void finalize()
    {
        workbook.close();    
    }    
}
