/*
 * TreeFileHandler.java
 *
 * Created on July 24, 2001, 4:52 PM
 *
 * $Id: TreeFileHandler.java,v 1.1 2006/05/29 18:32:53 limpan Exp $
 *
 * $Log: TreeFileHandler.java,v $
 * Revision 1.1  2006/05/29 18:32:53  limpan
 * Added minimed code
 * ----------------------------------------------------------------------
 *
 * Revision 1.77  2005/09/09 15:40:44  lindahlf
 * Server cachning
 *
 * Revision 1.76  2005/06/15 13:30:39  erichson
 * Committed an old comment
 *
 * Revision 1.75  2005/06/14 15:16:04  erichson
 * Generalized tree file loading to better support medForm import. // NE
 *
 * Revision 1.74  2005/01/30 15:21:12  lindahlf
 * T4 Integration
 *
 * Revision 1.73  2004/11/19 12:32:25  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.72  2004/11/15 15:45:13  erichson
 * 1. Added support for the new 'long' pcode format with three letters
 * 2. Error checking if going too far back is now done in the correct way
 * 3. Deprecation fix
 *
 * Revision 1.71  2004/11/13 11:16:10  erichson
 * fix after PerOlof visit: LineReader was not closed properly
 *
 * Revision 1.70  2004/11/11 22:36:24  lindahlf
 * MedServer Test Pack 2
 *
 * Revision 1.69  2004/11/10 13:08:37  erichson
 * added getExaminationCount() impl
 *
 * Revision 1.68  2004/11/09 21:14:27  lindahlf
 * Datalayer upgrade in connection with server development
 *
 * Revision 1.67  2004/11/06 01:11:01  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.66  2004/11/03 12:42:51  erichson
 * Updated arguments of exportToMVD methods.
 *
 * Revision 1.65  2004/10/23 15:34:17  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.64  2004/10/20 11:54:26  erichson
 * Quick fix since ExtensionFileFilter now includes directories, which confused this handler
 *
 * Revision 1.63  2004/10/11 18:57:03  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.62  2004/10/06 14:25:15  erichson
 * Moved some code to getTreeFile + added dummy exportToMVD methods
 *
 * Revision 1.61  2004/08/24 18:25:13  lindahlf
 * no message
 *
 * Revision 1.60  2004/02/24 18:41:36  lindahlf
 * Same pcode generated for existant pid
 *
 * Revision 1.59  2004/02/23 13:43:08  erichson
 * Completed migration to PID, now no deprecated methods are used
 *
 * Revision 1.58  2004/01/20 19:42:20  lindahlf
 * Major Upgrade PID support
 *
 * Revision 1.57  2003/09/09 17:11:36  erichson
 * Added PREFERRED_HEIGHT field
 *
 * Revision 1.56  2003/08/19 16:03:15  lindahlf
 * See 030819_Release_Notes_DATAHANDLING_MISC_COMMON.pdf
 *
 * Revision 1.55  2003/08/16 15:14:57  erichson
 * Forgot to recompile before checking in; fixed the constant name error
 *
 * Revision 1.54  2003/08/16 15:12:34  erichson
 * Some cleanup and more javadoc
 *
 * Revision 1.53  2003/08/16 15:09:14  erichson
 * Changed names of term constants
 *
 * Revision 1.52  2003/08/16 15:05:25  erichson
 * Mainly cleanup
 *
 * Revision 1.51  2003/08/07 10:55:38  erichson
 * removed setParent call since that is now private (and done in addChild)
 *
 * Revision 1.50  2003/07/03 23:57:06  erichson
 * Added support for tree files that have two A-Z letters in the p_code
 * Better error handling (exceptions etc)
 * Removed a listFiles call which was totally unnecessary and slowed things down
 *
 * Revision 1.49  2003/07/02 14:38:18  d99figge
 * No changes.
 *
 * Revision 1.48  2003/07/02 10:34:39  erichson
 * Removed debug message
 *
 * Revision 1.47  2003/07/02 00:23:45  erichson
 * Better at throwing exceptions now. Also removed some debug println's.
 *
 * Revision 1.46  2003/07/01 14:03:40  erichson
 * Fix for bugzilla bug 7 (tree file reading would crash when there were no lines to read)
 *
 * Revision 1.45  2003/04/10 01:47:21  lindahlf
 * no message
 * // Revision 1.45 added refreshExaminations method // NE
 *
 * Revision 1.44  2002/12/06 01:03:17  lindahlf
 * Added notifiable support
 *
 * Revision 1.43  2002/11/21 15:44:40  erichson
 * Updated TreeFileHandler with regards to Fredrik's properties changes and the addition of imagehandling to ExaminationDataHandler
 *
 * Revision 1.42  2002/11/05 09:43:05  erichson
 * Made exception handling easier (ValueMissingException added)
 *
 * Revision 1.41  2002/10/31 15:14:39  erichson
 * removed "forcibly extracting"... etc output
 *
 * Revision 1.40  2002/10/23 10:58:50  nazari
 * Use new treeFileHandler
 *
 * Revision 1.39  2002/10/23 08:46:29  nazari
 * Get the file name from the tree
 *
 * Revision 1.38  2002/10/22 15:03:44  erichson
 * changed saveTree to the new format. added "Naders_test_treefile". filename to make it compilable. Nader needs to add the correct implementation.
 *
 * Revision 1.37  2002/10/21 09:36:36  nazari
 * no message
 * // Revision 1.37 made these changes: added saveTree method // Nils
 *
 * Revision 1.36  2002/10/11 15:01:34  lindahlf
 * ID-tags added and compilable. // Fredrik
 * // Revision 1.36 made these changes: removed getExaminationDate and getPatientIdentifier() // Nils
 *
 * Revision 1.35  2002/10/02 13:05:57  erichson
 * Removed debug output
 *
 * Revision 1.34  2002/09/30 07:16:22  nazari
 * no message
 *
 * Revision 1.33  2002/09/27 15:38:43  erichson
 * Mainly cleanup, now defaults to iso-8559-1 (via misc.foundation.io.IOConstants)
 *
 * Revision 1.32  2002/09/26 20:04:45  erichson
 * Made it case sensitive
 *
 * Revision 1.31  2002/09/25 12:58:02  nazari
 * no message
 *
 * Revision 1.30  2002/09/23 09:20:08  erichson
 * Revised again. Should handle all known valid tree files now. // NE
 *
 *
 * Modified by Fredrik Lindahl 22 march 2002
 * -----------------------------------------
 * The following modifications was made:
 *
 * 1) restructured code for easier understanding (for me)
 * 2) removed 'debug' printouts
 * 3) added a few methods
 * 4) made some 'public' methods 'protected' instead
 * 5) made the class represent a generic component that
 *    can be extended for further possibilities of pcode
 *    variants, this was made with the introduction of
 *    some protected methods
 */

package minimed.core.datahandling.examination.tree;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import java.io.*;

// 1.4 only
// import java.nio.charset.*;
import minimed.core.MedViewUtilities;
import minimed.core.datahandling.*;
import minimed.core.datahandling.examination.*;
//import minimed.core.datahandling.examination.filter.*;
//import minimed.core.properties.*;

// import medview.medrecords.models.*; // ExaminationModel, CategoryModel etc

// import medview.summarycreator.generator.*;

import misc.foundation.*; // Fredrik 021205 (notifiable support)...

/**
 * Implementation of ExaminationDataHandler for operating on Tree Files.
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.3, 23 August 2002
 */

public class TreeFileHandler implements MedViewDataConstants
{
	
    /**
     * Constant for the tree file name type of form treeFileNoXXXXXX
     */
    private static final int TREEFILENAMETYPE_TREEFILENO = 1;

    /**
     * Constant for the tree file name type of form PCODE_DATE where pcode is one letter
     */
    private static final int TREEFILENAMETYPE_PCODE_FIRST_WITH_ONE_LETTER = 2;

    /**
     * Constant for the tree file name type of form PCODE_DATE where pcode is two letters
     */
    private static final int TREEFILENAMETYPE_PCODE_FIRST_WITH_TWO_LETTERS = 3;

    /**
     * Constant for the tree file name type of form PCODEDATE_ (underscore last in file name)
     */
    private static final int TREEFILENAMETYPE_UNDERSCORE_LAST = 4;


    /**
     * Constant for the tree file name type of form PCODE_DATE where pcode is three letters
     */
    private static final int TREEFILENAMETYPE_PCODE_LONG_THREE_LETTERS = 5;

    /**
     * The constant for the "Konkret_identifikation" term
     */
    public static final String CONCRETE_ID_TERM_NAME = "Konkret_identifikation";

    /**
     * The constant for the date term
     */
    public static final String DATE_TERM_NAME = "Datum";

    /**
     * The current tree file path
     */
    private String treeFilePath = null;

    /**
     * Possible tree file extensions, for use with FileFilters
     */
    private static final String[] treeFileExtensions = { "tree","TREE","Tree" };

    /**
     * File filter for files that end with any of the extensions in treeFileExtensions
     */
/*    public static final misc.gui.ExtensionFileFilter treeFileFilter = new misc.gui.ExtensionFileFilter(treeFileExtensions,
										    "Tree files (.tree", // description
										    false); // Don't accept directories
*/
    MiniFileFilter treeFileFilter = new MiniFileFilter();
    // 1.4 only
    // private Charset characterSet;

    //private MedViewDataSettingsHandler mVDSH = MedViewDataSettingsHandler.instance();
    private final static String fileSep = System.getProperty("file.separator");

    /**
     * Whether to use the date in the filename of the treefile (speeds things up).
     * If set to false, always checks the date term
     */
    private final boolean USE_DATE_IN_TREEFILENAME = true;

    /**
     * Dateformat for the way dates are stored inside tree files
     */
    public final static DateFormat TREEFILE_DATUM_FIELD_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Dateformat for the way date and time is stored in treefile file names
     */
    public final static DateFormat TREEFILENAME_DATE_FORMAT = new SimpleDateFormat("yyMMddHHmmss");

    /**
     * Dateformat for the way date and time is stored in treefile file names, reduced version (no seconds)
     */
    public final static DateFormat TREEFILENAME_DATE_FORMAT_WITHOUT_SECONDS = new SimpleDateFormat("yyMMddHHmm");

    /**
     * Default subdirectory for ExaminationDataHandler
     */
    private static final String TREE_EDH_DEFAULT_SUBDIRECTORY = "examples";
    private static final String TREE_EDH_EXAMINATION_DATA_LOCATION_PROPERTY = "treeEDHExaminationDataLocation";

    /**
     * Construct a new TreeFileHandler
     */
/*
    public TreeFileHandler() {
	treeFilePath = getDefaultDataLocation();
    treeFilePath = getExamSaveDir(); //get the default path from the properties module
	this.mVDSH = MedViewDataSettingsHandler.instance();
	1.4 only
	 setCharacterSet(Charset.forName("ISO-8859-1"));
	System.out.println("charsets: " + Charset.availableCharsets());
	cachedPath = null;
    }
*/
    public TreeFileHandler(String path ) {
	//this();
	treeFilePath = path;

    }

	public void shuttingDown() // Fredrik 041108
	{
	}

	/**
	 * Adds an examination datahandler listener. The
	 * listener will be notified of events such as
	 * when an examination has been added or when the
	 * location of examination data has changed.
	 */
/*	public void addExaminationDataHandlerListener(ExaminationDataHandlerListener l) // Fredrik 030818
	{
	}*/

	/**
	 * Removes an examination datahandler listener. The
	 * listener will be notified of events such as
	 * when an examination has been added or when the
	 * location of examination data has changed.
	 */
/*	public void removeExaminationDataHandlerListener(ExaminationDataHandlerListener l) // Fredrik 030818
	{
	}*/

	/**
	 * Returns whether or not the current implementation
	 * class for the ExaminationDataHandler interface deems
	 * the currently set location to be valid. If the
	 * location has not been set, it is deemed invalid
	 */
	public boolean isExaminationDataLocationValid() // Fredrik 030818
	{
		return true;
	}

    //1.4 only methods
    /*
    public void setCharacterSet(Charset newCharSet) {
	if (newCharSet != null) {
	    characterSet = newCharSet;
	} else {
	    System.err.println("Error: TreeFileHandler.setCharacterSet got null charset");
	}
    }


    public Charset getCharacterSet() {
	return characterSet;
    }
    */

    /**
     * Get the type of filename used for a tree file.
     *
     * There are three kinds of formats.
     *
     * 1: treeFileNo990523115320.tree
     *    1-10 text, 11-16 date, 17-22 time. No p-code!
     *    (sub 0,10)  (sub 10,16)  (sub 16,22)
     * 2: B00299570_011211071314.tree
     *    1-9 p-code, 10 dash, 11-16 date, 17-22 time
     *    (sub 0,9)   (pos 9)  (sub 10,16) (sub 16,22)
     * 3: G02749460990223074651_.tree
     *    1-9 p-code, 10-15 date, 16-21 time, 22 dash.
     *    (sub 0,9)   (sub 9,15)  (sub 15,21) (pos 21)
     */
    protected int getTreefileNameType(File file) {
	String filename=file.getName();
	int filenameType = 0;

	// System.out.println("sub09: " + filename.substring(0,9) + ", indexOf_: " + filename.indexOf('_')); // debug

	// type 1: treeFileNo....
	String firstTenLetters = filename.substring(0,10);
	if (firstTenLetters.equalsIgnoreCase("treeFileNo")) {
	    filenameType = TREEFILENAMETYPE_TREEFILENO;
	} else {
	    int underscoreIndex = filename.indexOf('_');
	    if (underscoreIndex == 9) // 10th letter
		filenameType = TREEFILENAMETYPE_PCODE_FIRST_WITH_ONE_LETTER;
	    else if (underscoreIndex == 10) // 11th letter
		filenameType = TREEFILENAMETYPE_PCODE_FIRST_WITH_TWO_LETTERS;
	    else if (underscoreIndex == 13) // 14th letter, example: YXX0017789400_041102143417.tree
		filenameType = TREEFILENAMETYPE_PCODE_LONG_THREE_LETTERS;
	    else if (underscoreIndex == 21) // 22th letter
		filenameType = TREEFILENAMETYPE_UNDERSCORE_LAST;
	}
	return filenameType;
    }

    /**
     * Get the p-code for a treeFile
     * @see getTreefileNameType
     * @throws ValueMissingException if a p-code could not be located
     */
    protected String getPCode(File file) throws ValueMissingException {
	String filename = file.getName();

	int filenameType = getTreefileNameType(file);
	switch (filenameType) {
	    case TREEFILENAMETYPE_PCODE_FIRST_WITH_ONE_LETTER:
	    case TREEFILENAMETYPE_UNDERSCORE_LAST:
		return filename.substring(0,9); // pcode has 1 letter, is 9 letters long
	    case TREEFILENAMETYPE_PCODE_FIRST_WITH_TWO_LETTERS:
		return filename.substring(0,10); // pcode has 2 letters, is 10 letters long
	    case TREEFILENAMETYPE_PCODE_LONG_THREE_LETTERS: // example: YXX0017789400_041102143417.tree
		return filename.substring(0,13); // pcode has 3 letters, is 13 letters long
	    case TREEFILENAMETYPE_TREEFILENO:
		// not in filename, have to use manual extraction from fields
		return extractPCodeFromFields(file);
	    default:
		System.err.println("Warning: Filename type unknown for filename " + filename + ". Extracting pcode from fields.");
		return extractPCodeFromFields(file);
	}
    }
   
    /**
     * Extract a particular value for a term in a tree file by line-by-line reading
     */
    private String[] extractTermValuesFromFile(File file, String term) throws ValueMissingException, NoSuchTermException {
	String filepath = file.getPath();

      //  System.out.println("Trying manual value extraction for term="+term+", file: " + filepath);
	String lineToLookFor = "N" + term; // for example 'NP-code'

	// We read directly from the file, since making an attributeshelf takes an unnecessary amount of time
	try {
	    // Read file line by line

	    FileInputStream fis = new FileInputStream(file);

	    // 1.4
	    // BufferedReader lineReader = new BufferedReader(new InputStreamReader(fis,characterSet));

	    // 1.3 hardcoded
	    BufferedReader lineReader = new BufferedReader(new InputStreamReader(fis,misc.foundation.io.IOConstants.ISO_LATIN_1_CHARACTER_ENCODING));

	    Vector valuesVector = new Vector();
	    while (lineReader.ready()) { // Keep reading while there are more lines to read (not EOF)
	    	
	    	String nextLine = lineReader.readLine();
	    	
	    	//System.out.println("looking for " + lineToLookFor + " in line " + nextLine); // debug
	    	int index = nextLine.indexOf(lineToLookFor); // Look for line
	    	if (index != -1) {
	    		//System.out.println("Found it!");
	    		// Next line contains the value
	    		nextLine = lineReader.readLine();
	    		// System.out.println("Nextline = " + nextLine);
	    		while (nextLine.startsWith("L")) {
	    			String value = nextLine.substring(1); // Cut off the 'l';
	    			int firstHashPosition = value.indexOf('#');
	    			value = value.substring(0,firstHashPosition); // cut off ending #'s
	    			valuesVector.add(value);
	    			// System.out.println("Forcibly Extracted " + term + "=" + value + " from file " + filepath);
	    			// System.out.println("Storing that.");
	    			
	    			nextLine = lineReader.readLine();
	    			if(nextLine == null){
	    				break;
	    			}
	    		}
	    		String[] returnString = new String[valuesVector.size()];
	    		returnString = (String[]) valuesVector.toArray(returnString);
	    		// System.out.println("getTermValue Failsafe: ValuesVector = " + valuesVector);
	    		lineReader.close();
	    		return returnString;
		}
	    } // If we come here we reached EOF
	    lineReader.close();
	    //System.out.println("Extraction failed");
	    throw new NoSuchTermException("Could not extract term [" + term + "] from file " + filepath + ": could not parse filename, and did not find " + lineToLookFor + " in file");
	} catch (FileNotFoundException fnfe) {
	    throw new ValueMissingException("Could not read treeFile " + filepath +": not found");
	} catch (IOException ioe) {
	    throw new ValueMissingException("Could not extract term " + term + " from file " + filepath + ": IOException: " + ioe.getMessage());
	}

    }

    /** Extract the p-code part from the fields in a treeFile
     * @return A p-code
     * @param filename the filename to parse
     * @throws ParseException thrown when the p-code extraction fails
     */
    private String extractPCodeFromFields(File treeFile) throws ValueMissingException {
	try {
	    String[] pcodes = extractTermValuesFromFile(treeFile,"P-code");
	    if (pcodes.length < 1)
		throw new ValueMissingException("extractPcodeFromFields didn't return anything");
	    else
		return pcodes[0];
	} catch (NoSuchTermException nste) {
	    throw new ValueMissingException("No such term exception: " + nste.getMessage());
	}
    }

    private String extractDatePartFromFields(File treeFile) throws ValueMissingException {
	try {
	    return extractTermValuesFromFile(treeFile,"Datum")[0];
	} catch (NoSuchTermException nste) {
	    throw new ValueMissingException("No such term exception: " + nste.getMessage());
	}
    }
/*
    private String getDefaultDataLocation() {
	return MedViewDataSettingsHandler.instance().getUserHomeDirectory() + TREE_EDH_DEFAULT_SUBDIRECTORY; // Create the setting
    }*/

    /** Get a list of unique patients (in the form of p_codes)
     * @return an array of p_codes
     * @throws IOException thrown when accessing of the tree files fails
     */
    public PatientIdentifier[] getPatients() throws IOException {
    MedViewUtilities.PIDPCodePair pair;
    	
	File[] files = listTreeFiles();
	
	
	//Vector p_codes = new Vector();
	
	PatientIdentifier[] patients = new PatientIdentifier[files.length];
	
		for(int i = 0; i< files.length;i++) {
			pair = MedViewUtilities.parsePIDPCode(files[i]);
			
			if(	pair.getPCode() == null && pair.getPID() == null) {
				throw new IOException("No PCode or PID present");
			}
			else{
				patients[i]=new PatientIdentifier(pair.getPCode(),pair.getPID());
			}
			
		}
		return patients;
    }
	/*
	for (int i = 0; i < files.length; i++) {
	    try {
		String p_code = getPCode(files[i]); // extract or cached...

		if (!(p_codes.contains(p_code))) {
		    p_codes.add(p_code);
		}
	    } catch (ValueMissingException vme) {
		// System.err.println("Note: Could not extract p_code from file " + files[i].getName() + " because: " + vme.getMessage());

		// Throw exception, since an examination without a P_CODE is useless
		throw new IOException("Could not get a valid P_code for file " + files[i].getName() +". Reason: " + vme.getMessage());
	    }
	} */


		// ADDED BY FREDRIK 040120 --------------------------------------->

	/*	PatientIdentifier[] retArr = new PatientIdentifier[p_codes.size()];

		for (int ctr=0; ctr<p_codes.size(); ctr++)
		{
			retArr[ctr] = new PatientIdentifier((String)p_codes.elementAt(ctr));
		}

		return retArr;

		// <---------------------------------------------------------------
    }*/

	// ADDED BY FREDRIK 030404 ---------------------------------------------------------------------->

    public PatientIdentifier[] getPatients(ProgressNotifiable notifiable) throws IOException
    {
		return this.getPatients();
	}

	/**
	 * Returns all examinations found in the knowledge base after the
	 * time of last cache construct.
	 *
	 * 1) getPatients().
	 * 2) getExaminations(pid).
	 * 3) getExaminationValueContainer(pid).
	 *
	 * If new examinations are found, each will trigger an examination
	 * added event as well as be included in the returned array. It is
	 * recommended that you use the event notification framework for
	 * local notification and the returned array only in those cases
	 * where this is not applicable, such as when a client needs to
	 * know the added examinations on the server after a certain time.
	 *
	 * @return ExaminationIdentifier[] all examinations found in the
	 * knowledge base after the time of last cache construct.
	 * @throws IOException if something goes wrong.
	 */
	public ExaminationIdentifier[] refreshExaminations() throws IOException
	{
		throw new IOException("Error: refreshExaminations() not implemented in TreeFileHandler class");
	}

	/**
	 * Returns all examinations found in the knowledge base after the
	 * specified time.
	 *
	 * If new examinations are found, each will trigger an examination
	 * added event as well as be included in the returned array. It is
	 * recommended that you use the event notification framework for
	 * local notification and the returned array only in those cases
	 * where this is not applicable, such as when a client needs to
	 * know the added examinations on the server after a certain time.
	 *
	 * @param sinceTime the examinations returned are the ones added to
	 * the knowledge base after this time.
	 * @return ExaminationIdentifier[] all examinations found in the
	 * knowledge base after the specified time.
	 * @throws IOException if something goes wrong.
	 */
	public ExaminationIdentifier[] refreshExaminations(long sinceTime) throws IOException
	{
		throw new IOException("Error: refreshExaminations() not implemented in TreeFileHandler class");
	}

	// <----------------------------------------------------------------------------------------------

	// ADDED BY FREDRIK 040120 --------------------------------------->

	public ExaminationIdentifier[] getExaminations(PatientIdentifier pid) throws IOException
	{
		//added pcode check /jonas 
		if(pid.getPCode() != null)
			return this.getExaminations(pid.getPCode());
		else
			return this.getExaminations(pid.getPID());
		
	}

	public int getImageCount(PatientIdentifier pid) throws IOException
	{
		return this.getImageCount(pid.getPCode());
	}

	// <---------------------------------------------------------------



    /** Get the total amount of images associated with a patient
     * @param patientCode The p_code for the patient whose images to count
     * @throws IOException thrown when access to the tree files fails
     * @return the total amount of images associated with the patient
     * @deprecated has been deprecated, use PatientIdentifier version instead
     */
    public int getImageCount(String patientCode) throws IOException {
	/**
	 * Modified by Fredrik Lindahl 22 march 2002
	 *
	 * Modified by Nils Erichson 02 July 2002
	 */

	int count = 0;

	File[] treeFiles = getPatientTreeFiles(patientCode);

	for (int i = 0; i < treeFiles.length; i++) {
	    try {
		String[] photos = extractTermValuesFromFile(treeFiles[i],"Photo"); // quicker than going via EVC
		count += photos.length;
	    } catch (NoSuchTermException e) {
		// Do nothing - There simply aren't any photos for that examination
	    } catch (ValueMissingException vme) {
		// System.err.println("getImagecount: Not counting photos for " + treeFiles[i].getPath() + " because: " + vme.getMessage());
		throw new IOException("Could not count images for " + treeFiles[i].getPath() + " because: " + vme.getMessage());
	    }
	}
	return count;
    }

    /** Get a list of tree files for a certain patient
     * @return an array of Files
     * @param patient_pcode the p_code of the patient
     * @throws IOException when access to the tree files fails
     */
    protected File[] getPatientTreeFiles(String patient_pcode) throws IOException {
	try {
	    File[] files = listTreeFiles();

	    Vector matching = new Vector();
	    
	    for (int i = 0; i < files.length; i++) {

		try {
		    String file_pcode = MedViewUtilities.parsePIDPCode(files[i]).getPCode();

		    if (file_pcode.equals(patient_pcode)) {
			matching.add(files[i]);
		    }
		} catch (Exception vme) {
		    //System.err.println("getPatientTreeFiles: Skipping file " + files[i].getPath() +" because: " + vme.getMessage());
		    throw new IOException("Could not get a valid P_code for file " + files[i].getPath());
		}
	 }


	    // Convert vector to File[] and return
	    files = new File[matching.size()];
	    files = (File[]) matching.toArray(files);
	    return files;
	} catch (IOException ioe) {
	    throw new IOException("GetpatientTreeFiles failed because: " + ioe.getMessage());
	}
    }
    /**
     * 
     * @param pid The pid
     * @return
     * @throws IOException
     */
    protected File[] getPatientTreeFilesWithPID(String pid) throws IOException {
    	try {
    	    File[] files = listTreeFiles();

    	    Vector matching = new Vector();
    	    
    	    for (int i = 0; i < files.length; i++) {

	    		try {
	    		    String file_pcode = MedViewUtilities.parsePIDPCode(files[i]).getPID();
	
	    		    if (file_pcode.equals(pid)) {
	    			matching.add(files[i]);
	    		    }
	    		} catch (Exception vme) {
	    		    //System.err.println("getPatientTreeFiles: Skipping file " + files[i].getPath() +" because: " + vme.getMessage());
	    		    throw new IOException("Could not get a valid PID for file " + files[i].getPath());
	    		}
    	    }


    	    // Convert vector to File[] and return
    	    files = new File[matching.size()];
    	    files = (File[]) matching.toArray(files);
    	    return files;
    	} catch (IOException ioe) {
    	    throw new IOException("GetpatientTreeFiles failed because: " + ioe.getMessage());
    	}
     }


    /** List the tree files in the current datafile path
     * @return an array of all the tree files in the current data location
     * @throws IOException when access to the tree files fails
     */
    protected File[] listTreeFiles() throws IOException {
	/**
	 * Modified by Fredrik Lindahl 22 march 2002
	 */

	// Get the tree file location property from medviewDataSettingsHandler
	//String treeFilePath = mVDSH.getProperty(TREE_EDH_EXAMINATION_DATA_LOCATION_PROPERTY);

	if (treeFilePath == null) {
	    //treeFilePath = getDefaultDataLocation();
		throw new IOException("No treefile path set");
	}
	File[] files = (new File(treeFilePath)).listFiles(treeFileFilter);
	if (files == null)
	    throw new IOException("I/O error, or " + treeFilePath + "is not a directory!");
	return files;
    }

    /**
     * Construct a Node Tree from a treeFile
     * @param treeFile the file to parse
     * @return the root Node of the tree
     */
    public Tree loadTreeFile(File treeFile) throws IOException
    {
	// System.out.println("TreeFile.makeTree: Making tree of " + treeFile.getName());

	FileInputStream fis = new FileInputStream(treeFile);            // Get inputstream for file

	// requires 1.4
	//InputStreamReader isr = new InputStreamReader(fis,characterSet);// Build a reader
	InputStreamReader isr = new InputStreamReader(fis,misc.foundation.io.IOConstants.ISO_LATIN_1_CHARACTER_ENCODING);// Build a reader
	// System.out.println("Encoding = " + isr.getEncoding());
	try
	{
	    return loadTree(isr);
	}
	catch (IOException ioe)
	{
	    // Add additional information
	    throw new IOException("Failed to load tree file " + treeFile.getPath() + ": " + ioe.getMessage());
	}
    }

    public Tree loadTree(String str) throws IOException
    {
	StringReader reader = new StringReader(str);
	return loadTree(reader);
    }

    public Tree loadTree(Reader in_reader) throws IOException
    {

	// FIRST, read all lines of a tree file into a vector!
	Vector lines = new Vector();

	BufferedReader reader = new BufferedReader(in_reader);                // Convert to a bufferedreader so we can read lines

	//System.out.println("readertype: " + in_reader.getClass());

	String nextLine;
	do
	{                                        // Now read lines until we get to the end
	    nextLine = reader.readLine();
	    if (nextLine != null)
	    {
		//System.out.println("read: " + line);
		lines.add(nextLine);                               // Read lines and process them
	    }
	} while (nextLine != null); // line == null indicates end of stream reached

	// System.out.println("Read " + lines.size() + " lines");

	// Check so that the tree file was not empty
	if (lines.size() == 0)
	{
	    throw new IOException("No lines to read!");
	}

	// Loop through vector of lines, build tree

	Iterator lineIterator = lines.iterator();

	// int lineNo = 0;

	try {
	    String line = (String) lineIterator.next();
	    //System.out.println("checking first line " + line);

	    Tree rootNode = new TreeBranch(line);                    // build root node
	    Tree parent = rootNode;

	    for ( ; lineIterator.hasNext(); )
	    {                                 /* lineiterator is already defined */

		// lineNo = lineNo + 1;

		line = (String) lineIterator.next();
		if (line.length() > 0) // Just ignore empty lines
		{
		    //	System.out.println("Read a line");

		    String fences, value;

		    //System.out.println("debug line = " + line);

		    char firstChar = line.charAt(0);                                // Get the first character (L or N for leaf or node)
		    int type = Tree.TYPE_NONE;

		    if (firstChar == 'N')
		    {
			type = Tree.TYPE_BRANCH;                                    // Node: A newline leads to a child
		    }
		    else if (firstChar == 'L')
		    {
			type = Tree.TYPE_LEAF;                                      // Slightly different for Leaves:
			while (line.indexOf("#") == -1) {
			    line = line + (String) lineIterator.next();             // Read more lines until we find fences ('#')
			}
		    }

		    if (type != Tree.TYPE_NONE) {
			// Got Node or Leaf, process

			int firstFence = line.indexOf('#'); // Find the location of the first '#' (fence) character on the line
			if (firstFence >= 0 ) {
			    // There are fences on the line. Separate line into 'value' part and 'fences' part
			    value = line.substring(1,firstFence);                   // get the substring that's not fences
			    fences = line.substring(firstFence,line.length());      // get the substring that are the fences
			} else {
			    // There are no fences on this line
			    value = line.substring(1,line.length()); // The value is the entire line except the first character ('L' or 'N')
			    fences = "";
			}

			// System.out.println("Processing type " + firstChar + ", value = " + value + ", fences = " + fences);

			/*
			if (type == Tree.TYPE_BRANCH) {
			    // Non-leaves (attributes) should be lower case
			    value = value.to ower Case();
			}
			*/

			/*if (type == Node.TYPE_BRANCH)
			    System.out.println("Making new BRANCH with value " + value);
			else if (type == Node.TYPE_LEAF)
			    System.out.println("Making new LEAF with value " + value); */

			Tree n;
			if (type == Tree.TYPE_LEAF)
			    n = new TreeLeaf(value);
			else
			    n = new TreeBranch(value);

			parent.addChild(n); // Also sets the parent of the child
			parent = n;

			/* Process the fences (#)! I.e. back up in the hierarchy s many times as there are fences */

			int fenceCount = fences.length();
			while (fenceCount > 0)
			{
			    if (parent == null)
			    {
				System.err.println("Malformed tree data! Tried to back up in a tree without parent, remaining '#'s: " + fenceCount);
			    }
			    else
			    {
				parent = parent.getParent();
			    }
			    fenceCount--;
			}

		    } else { // Nodetype is not Leaf or Branch
			System.out.println("Error: Neither Leaf or Node line detected!");
		    }
		} // end if line.length() > 0
	    }

	    // Done making node structure
	    return rootNode;

	} catch (java.util.NoSuchElementException nsee)
	{
	    throw new IOException("Tree parsing failed due to NoSuchElementException: " + nsee.getMessage());
	}
    }


    /** Get unique identifiers for all examinations associated with a patient
     * @return an array of ExaminationIdentifiers
     * @param patientCode the p_code for the patient
     * @throws IOException When access to the tree files fails
     * @deprecated has been deprecated, use PatientIdentifier version instead
     */
    public ExaminationIdentifier[] getExaminations(String patientCode) throws IOException {
	/**
	 * Modified by Fredrik Lindahl 22 march 2002
	 *
	 * Modified by Nils Erichson 02 July 2002
	 */

    Vector examinationIdentifiers = new Vector();
    
	File[] examFiles = getPatientTreeFiles(patientCode);
	
	for (int i = 0; i < examFiles.length; i++) {
	    // get attributes from tree file
	    // ExaminationValueTable valueTable = new ExaminationValueTable(loadTreeFile(examFiles[i]));

	    // set date by extracting it from the file
	    try {
	
		Date date = getDate(examFiles[i]);
	
		// create new examinationIdentifier object
		PatientIdentifier pid = new PatientIdentifier(patientCode);
		MedViewExaminationIdentifier examinationIdentifier = new MedViewExaminationIdentifier(pid,date);
		examinationIdentifiers.add(examinationIdentifier);

	    } catch (ValueMissingException vme) {
		System.err.println("Warning: TreeFileHandler skipped " + examFiles[i].getPath() + " since Date could not be extracted: " + vme.getMessage());
	    }
	}

	// convert the Vector conents to to array
	MedViewExaminationIdentifier[] dataArray = new MedViewExaminationIdentifier[examinationIdentifiers.size()];
	dataArray = (MedViewExaminationIdentifier[]) examinationIdentifiers.toArray(dataArray);
	return dataArray;
    }

    /**
     * Extract the date (when the examination was made) from a certain treefile
     * @return the extracted date
     * @param treeFile the file to extract the date from
     * @throws ParseException thrown if the date extraction fails
     */
    protected Date getDate(File treeFile) throws ValueMissingException {

	/**
	 * The Date from the "datum" field of the TreeFile is not
	 * compatible with the date in the file name, so instead
	 * we just use the date in the file name as the date used
	 * for a basis in the ExaminationData object returned above.
	 */

	String filename = treeFile.getName();

	int filenameType = getTreefileNameType(treeFile);

	String datePart = null;
	if (USE_DATE_IN_TREEFILENAME) {
	    try {
		switch (filenameType) {
		    case TREEFILENAMETYPE_PCODE_FIRST_WITH_ONE_LETTER:

		    case TREEFILENAMETYPE_UNDERSCORE_LAST:
			datePart = filename.substring(10,22); // date length: 12
			return TREEFILENAME_DATE_FORMAT.parse(datePart);
			// System.out.println("treefileno parsing: Parsed " + datePart + " into " + cal.toString());

		    case TREEFILENAMETYPE_PCODE_FIRST_WITH_TWO_LETTERS:
			datePart = filename.substring(11,23); // date length: 12
			return TREEFILENAME_DATE_FORMAT.parse(datePart);

		    case TREEFILENAMETYPE_PCODE_LONG_THREE_LETTERS: // "New" format with 3-letter pcode
			datePart = filename.substring(14,26); // date length: 12
			return TREEFILENAME_DATE_FORMAT.parse(datePart);

		    case TREEFILENAMETYPE_TREEFILENO:
			datePart = filename.substring(10,22); // date length: 12
			return TREEFILENAME_DATE_FORMAT.parse(datePart);
		    default:
			// System.out.println("BAD BAD BAD: had to extract from datum field from file " + treeFile.getName());
			datePart = extractDatePartFromFields(treeFile);
			return TREEFILE_DATUM_FIELD_DATE_FORMAT.parse(datePart);
		}
	    } catch (ParseException pe) {
		throw new ValueMissingException("getDate(): Could not parse date from filename: " + pe.getMessage());
	    }
	}else { // not USE_DATE_IN_TREEFILENAME;
	    datePart = extractDatePartFromFields(treeFile);
	    try {
		return TREEFILE_DATUM_FIELD_DATE_FORMAT.parse(datePart);
	    } catch (ParseException pe) {
		throw new ValueMissingException("Could not parse date from datePart [" + datePart + "] :" + pe.getMessage());
	    }
	}
    }

    public synchronized final ExaminationValueContainer[] getAllExaminationValueContainers(ProgressNotifiable not) throws
	    IOException
    {
	    return getAllExaminationValueContainers(not, MedViewDataConstants.OPTIMIZE_FOR_MEMORY);
    }

    public synchronized final ExaminationValueContainer[] getAllExaminationValueContainers(ProgressNotifiable not, int hint) throws
	    IOException
    {
	    try
	    {
		    PatientIdentifier[] pids = getPatients(not);

		    if (not != null)
		    {
			    not.setTotal(pids.length);

			    not.setDescription("Obtaining value containers"); // TODO - language
		    }

		    Vector evcVector = new Vector();

		    for (int ctr = 0; ctr < pids.length; ctr++)
		    {
			    if (not != null)
			    {
				    not.setCurrent(ctr);
			    }

			    ExaminationIdentifier[] eids = getExaminations(pids[ctr]);

			    for (int ctr2 = 0; ctr2 < eids.length; ctr2++)
			    {
				    evcVector.add(getExaminationValueContainer(eids[ctr2], hint)); // -> NoSuchExaminationException
			    }
		    }

		    ExaminationValueContainer[] retArr = new ExaminationValueContainer[evcVector.size()];

		    evcVector.toArray(retArr);

		    return retArr;
	    }
	    catch (InvalidHintException exc)
	    {
		    exc.printStackTrace();

		    System.exit(1); // critical error

		    return null; // unreachable but compiler don't know this
	    }
	    catch (NoSuchExaminationException exc)
	    {
		    exc.printStackTrace();

		    System.exit(1); // critical error

		    return null; // unreachable but compiler don't know this
	    }
    }

    /** Creates an instance of an ExaminationValueContainer
     * for a specified examination. Will return null if
     * there is no examination value container for the
     * specified parameters.
     */
    public minimed.core.datahandling.examination.ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id) throws
	    IOException, NoSuchExaminationException
    {
	    try
	    {
		    return getExaminationValueContainer(id, MedViewDataConstants.OPTIMIZE_FOR_EFFICIENCY);
	    }
	    catch (InvalidHintException exc)
	    {
		    exc.printStackTrace();

		    throw new IOException("invalid hint");
	    }
	}

   /**
    * Gets a ExaminationValueContainer for an examination.
    * Note: Right now, this implementation does not respecxt the hints.
    */
   public minimed.core.datahandling.examination.ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id, int hint)
	throws NoSuchExaminationException, IOException, InvalidHintException
   {
	   File  treeFileToRead = getTreeFile(id);
	   
	// System.out.println("Loading tree file " + treeFileToRead + " to Tree");
	Tree tree = loadTreeFile(treeFileToRead);
	// System.out.print("Making ExaminationValueTable...");
	ExaminationValueTable table = new ExaminationValueTable(tree);
	// System.out.println("Done.");
	return table;
   }

   /**
    * Get a file handle for the tree file corresponding to an examination identifier
    */
   public File getTreeFile(ExaminationIdentifier matchID)
	throws NoSuchExaminationException, IOException
   {
	   // get all treefiles for this patient
	//System.out.println("Looking for treefiles for patient " + matchID.getPcode());
	   try {
		   if(matchID.getPID().getPCode()== null){
			   String match_pid = matchID.getPID().getPID();
			   File[] examFiles = getPatientTreeFilesWithPID(match_pid);
			   if (examFiles.length < 0)
					throw new NoSuchExaminationException("Patient " + match_pid + " has no examinations!");
			   //no testing rigth now since all the functions needs a pcode
			   //and the final result is anyway equal to (but with pcode used):
			   return examFiles[0];
			
		   }
		   else{

				String match_pcode = matchID.getPID().getPCode();
			    //assert (match_pcode != null);
		
			    File[] examFiles = getPatientTreeFiles(match_pcode);
			    if (examFiles.length < 0)
				throw new NoSuchExaminationException("Patient " + match_pcode + " has no examinations!");
		
			    // Check if our date matches one of the examinations for this patient
		
			    Vector matches = new Vector();
			    try {
				for (int i = 0; i < examFiles.length; i++) {
				    //System.out.println("Checking if treefile " + examFiles[i].getName() + " belongs to " + matchString);
		
				    String other_pcode = getPCode(examFiles[i]);
				    if (other_pcode == null)
					System.err.println("Err: getPCode[ + examFiles[i] + ]" == null);
		
				    MedViewExaminationIdentifier otherExamID = new MedViewExaminationIdentifier(new PatientIdentifier(other_pcode),getDate(examFiles[i]));
		
				    // if (matchID.getPcode() == null)
				    //    System.out.println("Matchid pcode null");
				    // if (otherExamID.getPcode() == null)
				    // System.out.println("Otherexam pcode null");
		
		
				    if (matchID.equals(otherExamID)) // Compares p-code and date
				    {
					//System.out.println("Match! (" + matchID + " == " + otherExamID + ")");
					matches.add(examFiles[i]);
				    } else {
					 //System.out.println("Did not match (" + matchID + " != " + otherExamID + ")");
				    }
				}
			    }  catch (ValueMissingException vme) {
				throw new IOException("Could not get date from examination " + matchID + ": " + vme.getMessage());
			    }
			    if (matches.size() < 1) { /* debug info block */
				System.err.println("Warning: TreeFileHandler.getExaminationValueContainer error: NO examination dates matched " + matchID.getTime());
				System.err.print("Tried: [" );
				for (int i = 0; i < examFiles.length; i++ ) {
				    //System.out.print(examFiles[i].getName() + ", ");
				    try {
					PatientIdentifier pid = new PatientIdentifier(getPCode(examFiles[i]));
					MedViewExaminationIdentifier id = (new MedViewExaminationIdentifier(pid, getDate(examFiles[i])));
					System.out.print(id + "(" +  id.getTime() + "), ");
				    }  catch (ValueMissingException vme) {
					throw new IOException("Could not get date from examination " + matchID);
				    }
				}
				System.err.println("]");
				throw new NoSuchExaminationException(matchID);
			    } else { // matches >= 0
				if (matches.size() > 1) {
				    // debug info - will still work (take the first one)
				    String[] matchnames = new String[matches.size()];
				    for (int i = 0; i < matches.size(); i++) {
					matchnames[i] = ((File) matches.get(i)).getName();
				    }
				    throw new IOException("Multiple examinations matched: " + Arrays.asList(matchnames));
				}
				// Make value table of the first file in matches
		
				return (File) matches.get(0);
			    }
		 }
	   } catch (IOException ioe) {
		    throw new IOException("Could not get valueContainer for examination " + matchID + ": " + ioe.getMessage());
		}
    }

    public ExaminationValueContainer getExaminationValueContainer(String p_code, Date date) throws IOException, NoSuchExaminationException
    {
	    try
	    {
		    return getExaminationValueContainer(new MedViewExaminationIdentifier(new PatientIdentifier(p_code), date),
			    OPTIMIZE_FOR_EFFICIENCY);
	    }
	    catch (InvalidHintException exc)
	    {
		    exc.printStackTrace(); // never happens

		    return null; // <- ok since never happens and we get stack trace
	    }
    }

    /** Fetches an ExaminationValueContainer for the specified examination
     * @param p_code the p_code for the patient whose examination to acess
     * @param year the year the examination was made
     * @param day the day the examination was made
     * @param hour the hour the examination was made
     * @param minute the minute the examination was made
     * @param month the month the examination was made. Note: Has value 1-12 (not 0-11 as in GregorianCalendar!)
     * @throws NoSuchExaminationException thrown when trying to access an examination that doesn't exist in the current tree file database
     * @throws IOException thrown when accessing the tree file database fails
     * @return an ExaminationValueContainer for the specified examination
     */
    public ExaminationValueContainer getExaminationValueContainer(String p_code, int year, int month, int day, int hour, int minute, int second) throws
	    NoSuchExaminationException, IOException
	{
		try
		{
			return getExaminationValueContainer(new MedViewExaminationIdentifier(new PatientIdentifier(p_code),
				year,
				month,
				day,
				hour,
				minute,
				second), OPTIMIZE_FOR_EFFICIENCY);
		}
		catch (InvalidHintException exc)
		{
			exc.printStackTrace(); // never happens

			    return null; // <- ok since never happens and we get stack trace
		}
    }

    /**
     * Sets the location of the examination data (the tree files)
     * @param loc the location of the examination data (the tree files)
     */
    public void setExaminationDataLocation(String loc) {
	//properties.setProperty(TREE_EDH_EXAMINATION_DATA_LOCATION_PROPERTY, loc);
	treeFilePath = loc;
    }

    /**
     * Get the current location of the examination data (the tree files)
     * @return the current location of the examination data (the tree files)
     */
    public String getExaminationDataLocation() throws IOException{
	/**
	 * Added by Fredrik Lindahl 22 march 2002
	 */

	if (treeFilePath == null) {
	    //return getDefaultDataLocation(); // Get the current examination data location from the medview data settings handler
	    throw new IOException("No treefile path set");
		//return "/local"; /* Get the default datalocation from the properties module */
	}
	else {
	    File treeDir = new File(treeFilePath);

	    if (treeDir.exists() && treeDir.isDirectory()) {
		return treeDir.getPath();
	    }
	    else {
		/* return getDefaultDataLocation();*/
	    	throw new IOException("No treefile path set");
	    	//return "/local"; /* get the default data location from teh properties moduel */
	    }
	}
    }

    public String getExaminationDataLocationID()
    {
		/* Added by Fredrik 11 october 2002. */

		return "Tree file handling";
    }

    /**
     * Set up the default data location, fetching from the medview settings datahandler
     * @return the default data location
     */
    //private String setupDefaultDataLocation() {
	//String propSetting = mVDSH.getDataLocation() + TREE_EDH_DEFAULT_SUBDIRECTORY; // Create the setting
	//mVDSH.setProperty(TREE_EDH_EXAMINATION_DATA_LOCATION_PROPERTY, propSetting); // Set it
	//return propSetting;
    //}


     /**
     * Saves a Tree of examination data to the tree file database.
     * @param pTree a Tree of examination data to be saved to the database
     * @throws IOException if some error occurs during saving.
     */
/*    public int saveExamination(Tree pTree, ExaminationImage[] imageArray) throws IOException {

	// If you want you can create an ExaminationIdentifier first and then do this...
	// String fileName = identifier.getPcode() + "_" + TREEFILENAME_DATE_FORMAT.format(identifier.getTime());

	// Get concrete_identification node in the tree
	Tree fileNode   = pTree.getNode(CONCRETE_ID_TERM_NAME);
	if(fileNode == null)
	    throw new IOException("TreeFileHandler: Could not get the file node from the Tree ");

	// The leaf is the basis of the file name
	Enumeration childrenEnumerator = fileNode.getChildrenEnumeration();
	Tree fileNameNode = (Tree) childrenEnumerator.nextElement();

	if(fileNameNode == null)
	    throw new IOException("TreeFileHandler: Could not get the file value node from the file node ");

	String fileName = fileNameNode.getValue();
	if(fileName == null)
	    throw new IOException("Could not get the file name from the file value node ");

	File pTreeFile  = new File(fileName);

	FileOutputStream    fos = new FileOutputStream(pTreeFile);
	OutputStreamWriter  osw = new OutputStreamWriter(fos,"ISO-8859-1");

	osw.write(pTree.getValue());

	Enumeration enm  = pTree.getChildrenEnumeration();
	while(enm.hasMoreElements()){
	    Tree aChild = (Tree)enm.nextElement();
	    writeChild(aChild,osw);
	    writeChildren(aChild,osw);
	}
	osw.flush();
	osw.close();

	// Since MedRecords does not use TreeFileHandler anymore, i won't bother to implement
	// image handling here for now. // NE
	throw new IOException("Error: Image handling not implemented in TreeFileHandler! Your tree file was saved OK, however. Contact Nils Erichson if you get this error.");
    }
*/

 /*   public int saveExamination(Tree pTree, ExaminationImage[] imageArray, String location) throws IOException {
		throw new IOException("This method is not implemented yet in TreeFileHandler");
	}*/ 


    private void writeChild(Tree pTree,OutputStreamWriter pOsw) throws IOException {
	if(pTree.isLeaf()) pOsw.write("\nL" + pTree.getValue() );
	else pOsw.write("\nN" + pTree.getValue() );
    }

    private void writeChildren(Tree pTree,OutputStreamWriter pOsw)throws IOException
    {
	Enumeration enm  = pTree.getChildrenEnumeration();
	while(enm.hasMoreElements())
	{
	    Tree aChild = (Tree)enm.nextElement();
	    writeChild(aChild,pOsw);
	    writeChildren(aChild,pOsw);
	}
	pOsw.write("#");
    }


    /**
     * Get the images associated with an examination
     */
  /*  public ExaminationImage[] getImages(ExaminationIdentifier id) throws IOException, NoSuchExaminationException {
	throw new IOException("getImages not implemented in TreeFileHandler class");
    } 

    public int exportToMVD(PatientIdentifier[] patientIdentifiers, String newMVDlocation, ProgressNotifiable notifiable, ExaminationContentFilter filter, boolean allowPartialExport) throws IOException {
	throw new IOException("Export to MVD not implemented yet for treefilehandler");
    }

    public int exportToMVD(ExaminationIdentifier[] examinationIdentifiers, String newMVDlocation, ProgressNotifiable notifiable, ExaminationContentFilter filter, boolean allowPartialExport) throws IOException {
	throw new IOException("Export to MVD not implemented yet for treefilehandler");
    }*/

    public int getExaminationCount() throws IOException
    {

	if (treeFilePath == null) {
	    /*treeFilePath = getDefaultDataLocation();*/
		throw new IOException("No treefile path set");
	}

	return ((new File(treeFilePath)).listFiles(treeFileFilter)).length;
    }

    /**
     * Returns the total number of examinations for the specified
     * patient.
     * @param pid PatientIdentifier
     * @return int
     * @throws IOException
     */
    public int getExaminationCount(PatientIdentifier pid) throws IOException
    {
	    return this.getExaminations(pid).length; // for now...
	}

    public class MiniFileFilter implements FilenameFilter {
    	public boolean accept(File dir,String name) {
    		String a = "tree";
    		return name.toLowerCase().endsWith(a.toLowerCase());
    	}
    }
    /**
     * Returns the value of a child (leaf).
     * 
     * @param pTree
     * @param pTerm
     * @return the value of a child.
     * @throws IOException
     */
	public String getChildValue( Tree pTree, String pTerm ) throws IOException{
		String retval = "";
		Tree retNode = pTree.getNode(pTerm);
		Iterator it = retNode.getChildrenIterator();
		if (it.hasNext()) {
			Tree child = (Tree)it.next();
			retval = child.getValue();
		}
		return retval;
	}

    
}
