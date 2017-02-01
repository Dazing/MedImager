/*
 * DataSource.java
 *
 * Created on October 21, 2002, 11:20 AM
 *
 * $Id: DataSource.java,v 1.31 2005/10/21 10:02:54 erichson Exp $
 *
 * $Log: DataSource.java,v $
 * Revision 1.31  2005/10/21 10:02:54  erichson
 * Changed derivation messages from error to info
 *
 * Revision 1.30  2005/10/21 09:08:10  erichson
 * Disabled debugging section for patients
 *
 * Revision 1.29  2005/10/21 09:03:04  erichson
 * Updated derivation of Age and Gender: Now only derives if they do not already exist.
 *
 * Revision 1.28  2005/06/28 09:16:34  erichson
 * Fixed medserver datalocation
 *
 * Revision 1.27  2005/06/15 13:29:03  erichson
 * Fixed problems with invalid data locations passed to MVDhandler
 *
 * Revision 1.26  2005/06/15 10:54:52  erichson
 * Updated since DerivedTermHandler API had changed.
 *
 * Revision 1.25  2005/05/20 12:23:54  erichson
 * Only derive terms and values if valid pcode or pid
 *
 * Revision 1.24  2005/05/20 12:00:44  erichson
 * Shorter 'listing' message to fit in progress box
 *
 * Revision 1.23  2005/05/20 11:57:50  erichson
 * Updated progress handling in loadExaminations/LoadExaminationsThread for better visual feeback during loading (split up into listing and actual loading)
 *
 * Revision 1.22  2005/05/20 11:19:28  erichson
 * Updated to recognized when pids are saved instead of p-codes.
 *
 * Revision 1.21  2005/02/16 11:15:10  erichson
 * Introduced use of DerivedTermHandler
 *
 * Revision 1.20  2004/11/19 15:22:48  erichson
 * Fix for new ExaminationDataHandler (added hint to getExaminationValueContainer method)
 *
 * Revision 1.19  2004/11/16 07:09:20  erichson
 * Removed some really old code that was commented out
 *
 * Revision 1.18  2004/11/03 12:43:27  erichson
 * updated exportToMVD call with new arguments.
 *
 * Revision 1.17  2004/10/21 12:23:21  erichson
 * added progressNotifiable to exportToMVD.
 *
 * Revision 1.16  2004/10/11 14:04:32  erichson
 * Support for recreating GenericExaminationIdentifier and URI for MHCTableFileExaminationHandler
 *
 * Revision 1.15  2004/10/06 14:21:57  erichson
 * Added exportToMVD
 *
 * Revision 1.14  2004/10/05 08:46:07  erichson
 * Debug code to be able to disable resetExaminationDataHandler()
 *
 * Revision 1.13  2004/06/24 15:42:39  d97nix
 * Fix for error message when isDataLocationValid fails
 *
 * Revision 1.12  2004/03/26 17:34:30  erichson
 * Changed from URL to URI
 *
 * Revision 1.11  2004/02/23 12:14:54  erichson
 * Cosmetic fix
 *
 * Revision 1.10  2004/01/21 13:44:57  erichson
 * Updated to use new DataHandling version (with pid support)
 *
 * Revision 1.9  2003/07/03 23:45:23  erichson
 * added getAllExaminations() method
 *
 * Revision 1.8  2002/12/06 14:40:44  erichson
 * Small fix in the caching to compensate for different file separators on different platforms
 *
 * Revision 1.7  2002/11/21 15:17:09  erichson
 * Removed picture handling methods (moved to ExaminationImage handling)
 *
 * Revision 1.6  2002/11/05 09:50:23  erichson
 * getImagePaths disabled
 *
 * Revision 1.5  2002/10/31 15:59:41  erichson
 * getRelativePath and fixImagePaths now actually work
 *
 * Revision 1.4  2002/10/31 15:12:06  erichson
 * added a check to resetExaminationDataHandler so that location isn't reset if it's unnecessary
 *
 * Revision 1.3  2002/10/31 14:32:56  erichson
 * Added code for photo path handling, and added method getExaminationValueContainer
 *
 * Revision 1.2  2002/10/22 17:40:12  erichson
 * Made examinationDataHandler final
 *
 * Revision 1.1  2002/10/22 17:38:54  erichson
 * First check-in
 *
 */

package medview.visualizer.data;

import java.util.*; // StringTokenizer

import java.io.IOException;
import java.net.*; // URL / URI
import medview.datahandling.*;
import medview.datahandling.examination.*;
import medview.datahandling.examination.filter.*;
import medview.datahandling.examination.tablefile.*;
import medview.datahandling.images.*;

// import medview.common.translator.DerivedTermHandler; // Moved to datahandling

/**
 * A wrapper class for an ExaminationDataHandler. This wrapper class exists because some dataHandlers might be locked to one instance,
 * which means that you use the same datahandler instance but change the data path between uses. The datasource class compensates for this,
 * allowing you to create different DataSource instances for different paths but which use the same datahandler.
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.0
 */
public class DataSource
{
    // For deriving terms
    private static final String AGE_TERM = "Age";
    private static final String GENDER_TERM = "Gender";
    
    private static final boolean DISABLE_RESET_EXAMINATION_DATAHANDLER = false; // Debug
    
    private static final String fileSep = System.getProperty("file.separator");
    private static final char fileSepChar = fileSep.charAt(0);
    
    private static final String PHOTO_TERM = DataManager.getInstance().getPhotoTerm();
    
    private final ExaminationDataHandler examinationDataHandler;
    private final URI url; // Should only be assigned once, at creation
    
    /** 
     * Creates a new instance of DataSource
     *
     * @param edh The ExaminationDataHandler to use
     * @param uri The URI for the actual data source
     */
    public DataSource(ExaminationDataHandler edh, URI url) 
    {
       examinationDataHandler = edh;
       this.url = url;               
       System.out.println("Created datasource for url " + url + " with handler class " + edh.getClass().getName());
    }    
    
    /**
     * Ensure that the handler points to this dataset's location. This must be done in case the handler has been redirected
     *
     * @throws IOException when examinationDataHandler cannot be reset to the url location
     */
    private void resetExaminationDataHandler() throws IOException
    {        

        if (url == null)
            throw new IOException("resetExaminationDataHandler: location is null!");
        
        /*System.out.println("URL = " + url.toString());
        System.out.println("Authority = " + url.getAuthority());
        System.out.println("Scheme specific = " + url.getSchemeSpecificPart());       
        System.out.println("Getpath = " + location);
        */
               
        String location = url.toString();
        /*if (location != null)
        {
            if (fileSepChar != '/') 
            { 
                // Ensure conversion from url '/' to local file system separator
                location = location.replace('/',fileSepChar);
            }
        }
        else
        {
            // location null, which happens with SQL url            
            location = url.toString();
            if (location == null)
                throw new IOException("resetExaminationDataHandler: location still null after grabbing from url.toString!");
        }*/
        
        
                    
        
        // --- Check if the location has changed, which will prompt a resetExaminationHandler() call
        
        String edhLocation = examinationDataHandler.getExaminationDataLocation();

        
        // Remove medserver:// part for the actual EDH
        if (location.startsWith("medserver://"))
        {
            location = location.substring("medserver://".length());
        }
                
        /*System.out.println("Comparing location " + location);
        System.out.println("to Current loc = " + edhLocation;
        */       
        
        if (! (locationEquals(location, edhLocation)))
        {
            System.out.println("ExaminationDataHandler reseting location from " + examinationDataHandler.getExaminationDataLocation() + " to " + location);           
            
            // if location is file://, strip off that part and just pass on the path to the underlying EDH
            
            if (location.startsWith("file:/"))
            {
                java.io.File f = new java.io.File(url);
                if (f.exists())
                {
                    location = f.getPath();
                }
                else
                {
                    throw new IOException("DataSource: ResetExaminationDataHandler failed, new location " + url + " points to a file that doesn't exist");                   
                }
            }                        
            
            examinationDataHandler.setExaminationDataLocation(location);
            
            if (! examinationDataHandler.isExaminationDataLocationValid())
            {
                throw new IOException("DataSource: ResetExaminationDataHandler failed: " +
                   "Invalid location [" + location + "]" + " according to " + examinationDataHandler.getClass().getName());
            }
        }
    }
    
    
    /**
     * Check whether two EDHlocations are equal. 
     * If locations are file://, it's checked if they point to the same file. If not, the urls are just compared.
     */ 
    private boolean locationEquals(String location1, String location2)
    {
        // System.out.println("locationEquals testing " + location1 + " to " + location2);
        
        if (location1.startsWith("file:"))
        {
            try 
            {
                location1 = new java.io.File(new URI(location1)).getPath();
            } catch (java.net.URISyntaxException e)
            {
                System.out.println("Note: URISyntaxException for " + location1);
            }
        }
        
        if (location2.startsWith("file:"))
        {
            try 
            {
                location2 = new java.io.File(new URI(location2)).getPath();
            } catch (java.net.URISyntaxException e)
            {
                System.out.println("Note: URISyntaxException for " + location2);
            }
        }
        
        return (location1.equals(location2));
        
    }
    
    /** Get the available patient identifiers in this data source
     * @throws IOException if the examinationDataHandler fails
     * @return the patient identifiers
     */
    public medview.datahandling.PatientIdentifier[] getPatientIdentifiers() throws IOException {
        if (!DISABLE_RESET_EXAMINATION_DATAHANDLER) // Debug
            resetExaminationDataHandler();
        return examinationDataHandler.getPatients();
    }
    
    public ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id) throws IOException, NoSuchExaminationException {
        if (!DISABLE_RESET_EXAMINATION_DATAHANDLER) // Debug
            resetExaminationDataHandler();
        try {
            return examinationDataHandler.getExaminationValueContainer(id, medview.datahandling.MedViewDataConstants.OPTIMIZE_FOR_MEMORY);                                
        } catch (InvalidHintException ihe)
        {
            throw new IOException("InvalidHintException: " + ihe.getMessage());
        }
    }
    
    /**
     * Creates a new ExaminationDataElement
     *
     * @param dataGroup the data group to assign to this element
     * @param id the examinationIdentifier for the element
     * @throws IOException if the examinationDataHandler could not be reset, or if it fails to fetch an examinationValueContainer
     * @throws NoSuchExaminationException if the examination specified by id does not exist
     * @return the new ExaminationDataElement
     *
     */
    public ExaminationDataElement createExaminationDataElement(DataGroup dataGroup, ExaminationIdentifier id, boolean shouldAddDerivedTerms)
        throws IOException, NoSuchExaminationException
    {
        //System.out.println("Creating element for id " + id);

        if (!DISABLE_RESET_EXAMINATION_DATAHANDLER) // Debug
            resetExaminationDataHandler();        
   
        try {
            ExaminationValueContainer valueContainer = examinationDataHandler.getExaminationValueContainer(id, medview.datahandling.MedViewDataConstants.OPTIMIZE_FOR_MEMORY);                                
            
            if (shouldAddDerivedTerms)
                addDerivedTerms(valueContainer, id);
            
            MedViewExaminationDataElement dataElement = new MedViewExaminationDataElement(dataGroup, this, valueContainer, id);
            
            //System.out.println("Created element");
            return dataElement;        
        } 
        catch (InvalidHintException ihe)
        {
            throw new IOException("InvalidHintException: " + ihe.getMessage());
        }
                
    }                                                 
    
    private void addDerivedTerms(ExaminationValueContainer evc, ExaminationIdentifier id)
    {       
        PatientIdentifier pid = id.getPID();
        
        /* Only derive terms and values if valid pcode or pid */
        if (MedViewDataHandler.instance().validates(pid.toString())) // pcode or pid
        {
            DerivedTermHandler dTH = DerivedTermHandler.instance();

            /*
            String[] terms = dTH.getDerivedTerms();

            System.out.println("Derived terms to add: " + Arrays.asList(terms));

            for (int i = 0; i < terms.length; i++)
            {            
                String[] vals = dTH.getDerivedValues(terms[i], 
                                                  null, // default val
                                                  id.getTime(), // examination date
                                                  pid);
                for (int j = 0; j < vals.length; j++)
                {
                    System.out.println("Adding value " + vals[j] + " to term " + terms[i]);
                    evc.addValue(terms[i], vals[j]);
                }
            } */


            // Age
            
            
            /* String[] vals = dTH.getDerivedValues(DerivedTermHandler.PCODE_DERIVED_AGE_TERM, 
                                            null, 
                                            id.getTime(),
                                            pid);
             */
            
            dTH.setExaminationDate(id.getTime());
            dTH.setPatientIdentifier(pid);
            
            if (evc.termHasValues(AGE_TERM))
            {
                ApplicationManager.infoMessage("Examination " + id.toString() + " already contains " + AGE_TERM + ". Will not derive from p-code.");
            }
            else
            {
                String[] vals = dTH.getDerivedTermValues(DerivedTermHandler.PCODE_DERIVED_AGE_TERM);

                if (vals == null)
                {
                    ApplicationManager.errorMessage("Warning: Could not derive Age for examination " + id.toString());
                } else {
                    for (int j = 0; j < vals.length; j++)
                    {
                        // System.out.println("Adding value " + vals[j] + " to term " + terms[i]);
                        evc.addValue(AGE_TERM, vals[j]);
                    }
                }
            }

            // Gender
            
            if (evc.termHasValues(GENDER_TERM))
            {
                ApplicationManager.infoMessage("Examination " + id.toString() + " already contains " + GENDER_TERM + ". Will not derive from p-code.");
            }
            else
            {            
                String[] vals = dTH.getDerivedTermValues(DerivedTermHandler.PCODE_DERIVED_FEMALE_OR_MALE_TERM);
                if (vals == null)
                {
                    ApplicationManager.errorMessage("Warning: Could not derive Gender for examination " + id.toString());
                } else {        
                    for (int j = 0; j < vals.length; j++)
                    {
                        // System.out.println("Adding value " + vals[j] + " to term " + terms[i]);
                        evc.addValue(GENDER_TERM, vals[j]);
                    }
                }
            }
        }
    }
    
    /** 
     * Gets the examinationidentifiers for all the examinations for a patient
     *
     * @return the examinationidentifiers
     * @param patientIdentifier the patient whose examinations to get
     * @throws IOException if the ExaminationDataHandler fails to reset, or if it fails to fetch the examinations
     */
    public ExaminationIdentifier[] getExaminations(PatientIdentifier patientIdentifier) throws IOException {
        if (!DISABLE_RESET_EXAMINATION_DATAHANDLER) // Debug
            resetExaminationDataHandler();
        
        return examinationDataHandler.getExaminations(patientIdentifier);
    }
    
    /**
     * Get all available examinations in this DataSource
     */
    public ExaminationIdentifier[] getAllExaminations(DefaultProgressObject progressObject) throws IOException
    {        
        
        PatientIdentifier[] patients = getPatientIdentifiers();
        
        // Debugging section for checking which patients were loaded        
        /*
        String[] patientStrings = new String[patients.length];
        for (int i = 0; i < patientStrings.length; i++)
        {
            patientStrings[i] = patients[i].getPCode().toUpperCase();
        }
        Arrays.sort(patientStrings);
        System.out.println(Arrays.asList(patientStrings));
        */
        
        progressObject.setIndeterminate(false);
        progressObject.setProgressMin(0);
        progressObject.setProgressMax(patients.length);
        progressObject.setDescription("Examining " + patients.length + " patients");
        
        int examinationCount = 0;
        
        ExaminationIdentifier[][] arrayArray = new ExaminationIdentifier[patients.length][];
        
        for (int p = 0; p < patients.length; p++) 
        {
            progressObject.setProgress(p);            
            arrayArray[p] = getExaminations(patients[p]);                        
            examinationCount += arrayArray[p].length;
        }
     
        ExaminationIdentifier[] identifiers = new ExaminationIdentifier[examinationCount];
        
        int e = 0;
        for (int p = 0; p < patients.length; p++)
        {
            ExaminationIdentifier[] examinations = arrayArray[p];
            for(int i = 0; i < examinations.length; i++)
            {
                identifiers[e++] = examinations[i];
            }
        }
        
        assert (e == examinationCount);
                
        return identifiers;
    }
        
    /**
     *
     * @return the amount of examinations that were exported
     * @see ExaminationDataHandler#exportToMVD
     */
    public int exportToMVD(ExaminationIdentifier[] examinations, java.io.File targetMVD, AbstractProgressObject progressObject, ExaminationContentFilter filter, boolean allowPartialExport) throws IOException
    {
        return examinationDataHandler.exportToMVD(examinations, targetMVD.getPath(), progressObject, filter, allowPartialExport);
    }       
    
    // mediate the following three methods to examinationdatahandler
    public ExaminationImage[] getImages(ExaminationIdentifier id) throws IOException, NoSuchExaminationException {
        return examinationDataHandler.getImages(id);
    }
  
    /**
     * Gets the URI for this datasource. Used by saveSession etc
     * @return the URI for this datasource
     */
    public URI getURI() 
    {               
       if (examinationDataHandler instanceof MHCTableFileExaminationDataHandler)
       {
           try {
               MHCTableFileExaminationDataHandler mhcEDH =                
                  (MHCTableFileExaminationDataHandler) examinationDataHandler;
               URI newURI = new URI("file://" + url.getPath()
                        + "?"
                        + MHCTableFileExaminationDataHandler.LOPNUMMER_FIELD_PARAMETER
                        + "=" 
                        + mhcEDH.getLopnummerField()
                        + "&"
                        + MHCTableFileExaminationDataHandler.PID_FIELD_PARAMETER
                        + "="
                        + mhcEDH.getPidField());
               return newURI;
           } catch (java.net.URISyntaxException urise)           
           {
               ApplicationManager.errorMessage("Note: getURI gave URISyntaxException:" + urise.getMessage() + ", reverting to old url");
               return url;
           }
       }        
       else
           return url;
    }
    
    public ExaminationIdentifier createExaminationIdentifierFromStringRepresentation(String stringRepresentation)
        throws java.text.ParseException
    {
        if (examinationDataHandler instanceof MHCTableFileExaminationDataHandler)
        {
            try {
                // Format: PID_LOPNUMMER (pid= pcode or personnummer)
                
                StringTokenizer tok = new StringTokenizer(stringRepresentation,"_");
                String pid = tok.nextToken();
                String lopnummer = tok.nextToken();
                
                
                PatientIdentifier patId = null;
                
                if ( new SwedishPNRRawPIDValidator().validates(pid))
                {
                    // pid is personnummer
                    
                    // Hack time! Todo: Make a better (long term) solution...
                    if (examinationDataHandler instanceof MHCTableFileExaminationDataHandler)
                    {
                        MHCTableFileExaminationDataHandler mhcHandler = (MHCTableFileExaminationDataHandler) examinationDataHandler;
                        try {
                            patId = mhcHandler.makePID(pid);                            
                        } catch (InvalidPIDException e)
                            // Can't happen, since this is caused by validates() failing, and validates() already accepted the pid above
                        {
                        
                        } catch (CouldNotGeneratePCodeException e)
                        {   
                            throw new java.text.ParseException("Could not generate p-code for " + pid, -1);
                        }
                    }
                   
                } 
                
                if (patId == null)
                    // pid is p-code
                    patId = new PatientIdentifier(pid);
                
                
                GenericExaminationIdentifier identifier = new GenericExaminationIdentifier(patId, lopnummer);
                return identifier;
            } catch (NoSuchElementException nsee)
            {
                throw new java.text.ParseException("Could not parse generic examination identifier string [" + stringRepresentation + "]",-1);
            }
        }
        else 
        {
            return MedViewExaminationIdentifier.createExaminationIdentifierFromStringRepresentation(stringRepresentation);
        }
    }
}
