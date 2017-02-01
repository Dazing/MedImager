/*
 * DataSourceFactory.java
 *
 * Created on October 21, 2002, 4:16 PM
 *
 * $Id: DataSourceFactory.java,v 1.18 2007/01/03 14:01:07 erichson Exp $
 *
 * $Log: DataSourceFactory.java,v $
 * Revision 1.18  2007/01/03 14:01:07  erichson
 * Updates to pcode generator handling.
 *
 * Revision 1.17  2005/06/21 07:15:29  erichson
 * supports medserver:// url
 *
 * Revision 1.16  2005/05/20 11:17:42  erichson
 * Update to recognize when pid(personnummer) is saved to session, not p-code
 *
 * Revision 1.15  2004/11/30 21:00:42  erichson
 * Enabling MVDhandler for beta 7 release
 *
 * Revision 1.14  2004/11/16 08:54:16  erichson
 * checking for null result from url.getPath
 *
 * Revision 1.13  2004/11/16 07:59:54  erichson
 * beta 6 will use SafeMVDhandler since MVDhandler takes too much mem.
 *
 * Revision 1.12  2004/11/10 13:06:24  erichson
 * Updated PCode generation since datahandling was updated
 *
 * Revision 1.11  2004/10/20 11:55:37  erichson
 * Back to safeMVDhandler since MVDHandler crashed on OM.MVD
 *
 * Revision 1.10  2004/10/11 14:03:18  erichson
 * Better support for MHC files (lopnummer etc)
 *
 * Revision 1.9  2004/10/06 14:24:13  erichson
 * Now using MVDHandler...
 *
 * Revision 1.8  2004/10/05 08:51:33  erichson
 * File-based handling now supports TableFile
 *
 * Revision 1.7  2004/06/24 15:43:44  d97nix
 * Small fix since SwedishPNRPCodeGenerator constructor had changed.
 *
 * Revision 1.6  2004/04/05 20:52:31  erichson
 * Now uses SwedishPNRPCodeGenerator to generate p-codes from personnummer.
 *
 * Revision 1.5  2004/03/28 17:59:15  erichson
 * jdbc:jtds support.
 *
 * Revision 1.4  2003/07/08 14:08:11  erichson
 * Added some disabled code to test MVDHandler compatibility
 *
 * Revision 1.3  2002/10/31 14:33:38  erichson
 * Now uses SafeMVDHandler instead of MVDHandler, since MVDHandler isn't compatible...
 *
 * Revision 1.2  2002/10/31 10:05:20  erichson
 * Added MVDhandler support
 *
 * Revision 1.1  2002/10/22 16:38:46  erichson
 * First check-in
 *
 */

package medview.visualizer.data;

import java.io.*; // File etc
import java.net.*; // URL / URI
import java.sql.SQLException;

import medview.datahandling.*;
import medview.datahandling.examination.*;
import medview.datahandling.examination.tablefile.*;
import medview.datahandling.examination.tree.*; // TreeFileHandler

/**
 * Factory class for data sources
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class DataSourceFactory 
{

    /* Classes to use when creating remote datahandler */
    /*
    private static final String REMOTE_TERM_DATAHANDLER_CLASS = "medview.datahandling.RemoteTermDataHandlerClient";
    private static final String REMOTE_EXAMINATION_DATAHANDLER_CLASS = "medview.datahandling.examination.RemoteExaminationDataHandlerClient";
    private static final String REMOTE_PCODE_GENERATOR_CLASS = "medview.datahandling.RemotePCodeGeneratorClient";
    */
    
    // For own pcode generator
    
    // private static String pcodePrefix = MedViewDataHandler.instance().getUserID();
    private static SessionPCodeNRGenerator randomNumberGenerator = new SessionPCodeNRGenerator();           
    
    
    private static PCodeGenerator sessionPcodeGenerator =  
        new SessionPCodeGenerator(randomNumberGenerator,
                                  true); // Accept MVDH updates
    
    private final static Settings settings = Settings.getInstance();
    
    /** Create a new DataSource, using ExaminationDataHandler determined by the URL protocol
     * @return a new DataSource
     * @param url the url for the data source. 
     *
     * file:// loads from a file (MVD file or Table file)
     * jdbc: loads from a database server
     * medserver: loads from a MedServer host.
     *
     * @throws IOException when a proper examination data handler could not be instantiated for the URL
     */
    public static DataSource createDataSource(URI url) throws IOException, InvalidDataLocationException, SQLException {
        
        System.out.println("Creating data source for " + url.toString());
        String protocol = url.getScheme();
        //System.out.println("Protocol = " + protocol);
        
        
        if (protocol.equals("file")) 
        {
            return createFileDataSource(url);            // may be null!
        } 
        else if (protocol.equals("medserver"))
        {
            return createMedServerDataSource(url);
        }
        else if (protocol.equals("jdbc"))
        {
            System.out.println("Scheme-specific-part:" + url.getSchemeSpecificPart());
            try
            {
                return createJTDSDataSource(url);
            //} catch (SQLException sqle) {
            //    throw new IOException("Could not create JTDS data source: SQLException: " + sqle.getMessage());
            } catch (ClassNotFoundException cnfe) {
                throw new IOException("Could not load the JDBC driver! " + cnfe.getMessage());
            }
        } else { // not file        
            // If this statement is reached, no datasource has been returned
            throw new IOException("Could not find proper examination data handler for URL " + url);
        }
    }
    
    /**
     * Creates file data source 
     * @param url a file://... -type URL
     * @return datasource, or null if there should be no loading (for example if cancel is clicked)
     */
    private static DataSource createFileDataSource(URI url) throws IOException, InvalidDataLocationException
    {                
        try {
            String path = url.getPath();
            if (path == null)
            {
                throw new InvalidDataLocationException("path in url " + url.toString() + " null!");
            }
            
            File directory = new File(path); 
                        
            if (directory.isDirectory()) 
            {                

                if (directory.getName().toUpperCase().endsWith(".MVD")) { // Directory that ends with MVD
                    
                    ExaminationDataHandler mvdh;

                    //mvdh = new SafeMVDHandler(directory.getPath());

                    //MVDHandler will not work for some reason, since it is so fond of returning null...
                    mvdh = new MVDHandler();
                    mvdh.setExaminationDataLocation(directory.getPath());                    

                    DataSource dataSource = new DataSource(mvdh,url);
                    return dataSource;
                } else { // Directory that doesn't end with MVD - treat as directory of tree files
                    File[] treeFiles = directory.listFiles(TreeFileHandler.treeFileFilter);            
                    if (treeFiles.length > 0) { // .tree files exist
                        TreeFileHandler tfh = new CachingTreeFileHandler();
                        tfh.setExaminationDataLocation(directory.getPath());                        
                        return new DataSource(tfh,url);                                                                        
                    } else {
                        // No tree files existed
                        throw new IOException("Tried to create data source for directory that didn't contain any tree files!");
                    }
                }
            } 
            else // is not directory
            { 
                 
                String query = url.getQuery();
                File openFile = directory;
                
                TableFile tableFile;
                if (openFile.getName().toUpperCase().endsWith(".XLS")) // Excel file
                {
                    // Create excel file data source                    
                    tableFile = new ExcelTableFile(openFile);
                } else {
                    tableFile = new TextTableFile(openFile, TextTableFile.DEFAULT_FIELD_DELIMITER); // TODO: Make choosable
                }                        
        
        // get lopnummer and etc from the properties
                java.util.Properties props = misc.foundation.net.URIParameters.getParameters(query);
                String lopnrField = (String) props.get(MHCTableFileExaminationDataHandler.LOPNUMMER_FIELD_PARAMETER);
                String pidField = (String) props.get(MHCTableFileExaminationDataHandler.PID_FIELD_PARAMETER);
      
                System.out.println("From url properties: lopnrfield = " + lopnrField + ", " + pidField);
                
                MHCTableFileExaminationDataHandler handler =
                    new MHCTableFileExaminationDataHandler(tableFile);
                                   
                // handler.setPCodeGenerator(sessionPcodeGenerator); // Done in MHCTFedh constructor - does not accept MVDH userid updates
                
                if (lopnrField == null) 
                {
                    // Choose pid and lopnummer fields. Method will update handler     
                    boolean okClicked = medview.visualizer.gui.ApplicationFrame.getInstance().chooseMHCFileFields(handler); // TODO: gui referenced from data class                
                    if (!okClicked)
                        return null;
                }
                else // Löpnummer field exists, but not pid field
                {
                    handler.setLopnummerField(lopnrField);
                    
                    if (pidField == null)
                    {
                        handler.setPidField("");  // Don't use pid!
                    }
                }
                return new DataSource(handler, url);                                                        
            }                                
        } 
        catch (java.lang.IllegalArgumentException iae)
        {
            throw new IOException("Could not open File [" + url + "]: IllegalArgumentException: " + iae.getMessage());
        }
    } // end createFileDataSource method
    
    
    /**
     * Create a MedServer dataSource based on an url of the type medserver:host
     */ 
    public static DataSource createMedServerDataSource(URI url)
    {
        ExaminationDataHandler handler = new medview.datahandling.examination.RemoteExaminationDataHandlerClient();
        handler.setExaminationDataLocation(url.getHost());        
        return new DataSource(handler, url);
    }
        
    
    /**
     * Create a JTDSDataSource.
     *
     * @throws SQLException if an SQL error occurs
     * @throws ClassNotFoundException if the JDBC driver class could not be found
     */
    private static DataSource createJTDSDataSource(URI url) 
        throws SQLException, ClassNotFoundException
    {
        // Username and password exist in the URL so don't pass them to SQLExaminationDataHandler
        SQLExaminationDataHandler dataHandler = new SQLExaminationDataHandler(url,                                             
                                            //settings.getDatabaseUser(), 
                                            // settings.getDatabasePassword(),
                                            settings.getDatabaseCatalog(), // name
                                            settings.getDatabaseTable(),
                                            settings.getDatabasePIDField(),
                                            settings.getDatabaseExaminationIdentifierField(),
                                            settings.getDatabaseExaminationIdentifierType()                                            
                                            );                
          
        
        dataHandler.setPCodeGenerator(sessionPcodeGenerator); // Default is use MVDH. Note: This one accept userID updates from MVDH.
        
        DataSource dataSource = new DataSource(dataHandler,url);
        return dataSource;
    }
        
}
