/*
 * TableWriter.java
 *
 * Created on October 10, 2002, 4:30 PM
 *
 * $Id: TableWriter.java,v 1.10 2005/02/16 11:04:46 erichson Exp $
 *
 * $Log: TableWriter.java,v $
 * Revision 1.10  2005/02/16 11:04:46  erichson
 * Exception handling improved.
 *
 * Revision 1.9  2004/12/17 11:45:45  erichson
 * Exception handling updated for new JXL (2.5.1)
 *
 * Revision 1.8  2004/11/13 10:49:35  erichson
 * Thread naming
 *
 * Revision 1.7  2004/10/21 08:36:33  erichson
 * ConcreteProgressObject -> DefaultProgressObject
 *
 * Revision 1.6  2004/02/24 21:17:09  erichson
 * Removed debug output
 *
 * Revision 1.5  2004/02/24 20:19:24  erichson
 * Made excel saving be performed in a separate thread so we don't lock up the gui
 *
 * Revision 1.4  2002/10/25 08:31:30  zachrisg
 * Added writeArrayToExcelFile().
 *
 * Revision 1.3  2002/10/23 14:47:45  zachrisg
 * Added method writeArrayToTextFile().
 *
 * Revision 1.2  2002/10/11 07:57:05  zachrisg
 * Added Id and Log tags.
 *
 */

package medview.visualizer.data;

import java.io.*;
import javax.swing.*;

import jxl.*;
import jxl.write.*;

/**
 * A tool class for writing JTables to excel- or text-files.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class TableWriter {
    
    private static TableWriter instance = new TableWriter();
    
    /** Private constructor. Only static methods. */
    private TableWriter() {}
    
    public static TableWriter getInstance() {
        return instance;
    }
    
    /**
     * Writes the table to an excel file, including the headers.
     *
     * @param table The table.
     * @param file The file to write the table to.
     */
    public static void writeTableToExcelFile(JTable table, File file) 
        throws IOException
    {
        if (file == null) {
            throw new IOException("writeTableToExcelFile: file == null");
        }
        
        System.out.println("writeTableToExcelFile");
        try {
            
            // create a new workbook
            // System.out.println("Creating workbook");
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            // System.out.println("Created workbook, creating sheet");
            
            // create a sheet
            WritableSheet sheet = workbook.createSheet("Examinations", 0);
            


            int columnCount = table.getColumnCount();
            int rowCount = table.getRowCount();

            // System.out.println("Columns = " + columnCount + ", rowCOunt = " + rowCount);

            // write the headers to the sheet
            for (int col = 0; col < columnCount; col++) {
                jxl.write.Label label = new jxl.write.Label(col, 0, table.getColumnName(col));
                sheet.addCell(label);
               // System.out.println("Added col " + col);
            }




            // write the cells to the sheet
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < columnCount; col++) {
                    jxl.write.Label label = new jxl.write.Label(col, row + 1, (String) table.getValueAt(row, col));
                    sheet.addCell(label);
                }
                //System.out.println("Row " + row + " done");
            }
            
            //System.out.println("End of for loop");
            // flush the workbook to disk and close it
            workbook.write();
            workbook.close();
            
        } catch (IOException e) {
            throw new IOException("writeTableToExcelFile: Could not write to file: " + e.getMessage());
        } 
        catch (jxl.write.WriteException we)
        {
            throw new IOException("JXL could not write to excel file: " + we.getMessage());
        }
    }
    
    
    /**
     * Writes the table to a tab-separated textfile, including the headers.
     *
     * @param table The table.
     * @param file The file to write the table to.
     */
    public static void writeTableToTextFile(JTable table, File file) throws IOException
    {
        if (file == null) {
            throw new IOException("writeTableToTextFile: file == null!");
        }
        
        try {                        
            
            // open streams
            FileOutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream, "ISO-8859-1");
        
            int columnCount = table.getColumnCount();
            int rowCount = table.getRowCount();
            
            // write the headers to the stream
            for (int col = 0; col < columnCount; col++) {
                streamWriter.write(table.getColumnName(col));
                
                // write tab-separator
                if (col != columnCount - 1) {
                    streamWriter.write("\t");
                }
            }
                    
            // write the cells to the stream
            for (int row = 0; row < rowCount; row++) {

                // write newline-separator
                streamWriter.write("\n");
                
                for (int col = 0; col < columnCount; col++) {
                    streamWriter.write((String) table.getValueAt(row, col));

                    // write tab-separator
                    if (col != columnCount - 1) {
                        streamWriter.write("\t");
                    }
                }
            }
            
            // close the streams
            streamWriter.close();
            
        } catch (IOException e) {
            throw new IOException("writeTableToTextFile: Could not write to file:" + e.getMessage());
        }        
    }
    
    /**
     * Writes a two-dimensional array to a tab-separated textfile.
     *
     * @param array The array.
     * @param file The file to write the array to.
     */
    public ProgressObject writeArrayToTextFile(String[][] array, File file)
        throws IOException
    {
        if (file == null) {
            //return new DefaultProgressObject(0, 1,0 , false);
            throw new IOException("writeArrrayToTextFile: file == null!");
        }
        
        try {
            
            // open streams
            FileOutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream, "ISO-8859-1");
        
            int rowCount = array.length;
            int columnCount;
            
            if (rowCount == 0) {
                columnCount = 0;
            } else {         
                columnCount = array[0].length;
            }
                        
            // write the cells to the stream
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < columnCount; col++) {
                    streamWriter.write(array[row][col]);

                    // write tab-separator
                    if (col != columnCount - 1) {
                        streamWriter.write("\t");
                    }
                }
                
                // write the line-separator
                if (row != rowCount - 1) {
                    streamWriter.write("\n");
                }
            }
            
            // close the streams
            streamWriter.close();
            
        } catch (IOException e) {
            throw new IOException("writeArrayToTextFile: Could not write to file: " + e.getMessage());
        }
        // TODO: We should implement Threading of this in the future, but it doesn't matter right now since it's so fast
        return new DefaultProgressObject(0,30,500,true); 
    }
    
    /**
     * Writes a two-dimensional array to an Excel97 file.
     *
     * @param array The array.
     * @param file The file to write the array to.
     */
    public ProgressObject writeArrayToExcelFile(String[][] p_array, File p_file) 
        throws IOException
    {
        if (p_file == null) {
            // return new DefaultProgressObject(0,1,1,true);
            throw new IOException("writeArrayToExcelFile: file == null!");
        }                       

        DefaultProgressObject m_progress = new DefaultProgressObject(0,200,1, false); // Bogus values
        WriteArrayToExcelThread writeThread = new WriteArrayToExcelThread(p_array,p_file,m_progress);
        writeThread.start();
        return m_progress;
    }
    
    public class WriteArrayToExcelThread extends Thread {
        private String[][] m_Array;
        private File m_File;
        private DefaultProgressObject m_Progress;
        
        public WriteArrayToExcelThread(String[][] array, File file, DefaultProgressObject progress) {        
            super("WriteArrayToExcelThread");
            m_Array = array;
            m_File = file;
            m_Progress = progress;
        }
        
        public void run() {
            try {
                        
                int rowCount = m_Array.length;
                int columnCount;

                // Get column count
                if (rowCount == 0) {
                    columnCount = 0;
                } else {         
                    columnCount = m_Array[0].length;
                }
                m_Progress.setProgressMin(0);
                // System.out.println("max = " + (rowCount -1) );
                m_Progress.setProgressMax(rowCount-1);
                
                // create a new workbook                
                WritableWorkbook workbook = Workbook.createWorkbook(m_File);

                // create a sheet                
                WritableSheet sheet = workbook.createSheet("Examinations", 0);

        
                // write the cells to the sheet
                for (int row = 0; row < rowCount; row++) {
                    for (int col = 0; col < columnCount; col++) {
                        jxl.write.Label label = new jxl.write.Label(col, row, m_Array[row][col]);                            
                        sheet.addCell(label);                            
                    }
                    // System.out.println("Progressing row " + row);
                    m_Progress.setProgress(row);
                }

                
                // flush the workbook to disk and close it            
                workbook.write();                
                workbook.close();
                System.out.println("Writing to excel file " + m_File.getName() + " done.");

            } catch (IOException e) {
                System.out.println("Could not write to file: " + e.getMessage());
            } catch (jxl.write.WriteException we) {
                System.out.println("Could not write to excel file: " + we.getMessage());
            }
        }
    }                        
}
