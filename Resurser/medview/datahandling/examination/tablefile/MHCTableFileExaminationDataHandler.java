/*
 * $Id: MHCTableFileExaminationDataHandler.java,v 1.19 2007/01/03 14:15:25 erichson Exp $
 *
 * Created on den 5 juni 2004, 12:49
 *
 * $Log: MHCTableFileExaminationDataHandler.java,v $
 * Revision 1.19  2007/01/03 14:15:25  erichson
 * Fix for mixxing ;
 *
 * Revision 1.18  2007/01/03 14:05:58  erichson
 * Update to pcode generation handling.
 *
 * Revision 1.17  2006/04/24 14:18:41  lindahlf
 * <No Comment Entered>
 * // NE: Fredrik added removeExamination method
 *
 * Revision 1.16  2005/09/09 15:42:01  lindahlf
 * Server cachning
 *
 * Revision 1.15  2005/05/20 11:25:27  erichson
 * Updated methods to be sensitive to lacking pidField (when MHC are running with Löpnummer only)
 *
 * Revision 1.14  2005/01/30 15:28:18  lindahlf
 * T4 Integration
 *
 * Revision 1.13  2004/11/19 12:33:30  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.12  2004/11/15 15:46:27  erichson
 * Pure cosmetic fix
 *
 * Revision 1.11  2004/11/10 13:07:06  erichson
 * added getExaminationCount() impl
 *
 * Revision 1.10  2004/11/09 21:14:21  lindahlf
 * Datalayer upgrade in connection with server development
 *
 * Revision 1.9  2004/11/06 01:11:23  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.8  2004/11/03 12:41:13  erichson
 * Updated arguments of exportToMVD.
 *
 * Revision 1.7  2004/10/21 12:18:02  erichson
 * added progressNotifiable to the exportToMVD calls.
 *
 * Revision 1.6  2004/10/11 14:05:30  erichson
 * Helper methods for session saving.
 *
 * Revision 1.5  2004/10/11 09:46:00  erichson
 * Removed assumptions about personnummer
 *
 * Revision 1.4  2004/10/06 12:34:53  erichson
 * Added dummy export methods to comply with new EDH interface
 *
 * Revision 1.3  2004/10/05 08:49:42  erichson
 * More progress made, still not finished
 *
 * Revision 1.2  2004/09/09 14:05:24  d97nix
 * Added new saveExamination method (...,..., String)
 *
 * Revision 1.1  2004/09/09 10:46:02  erichson
 * First check-in.
 *
 */

package medview.datahandling.examination.tablefile;

import java.io.*;
import java.util.*;

import misc.foundation.*;
import misc.foundation.util.*;

import medview.datahandling.InvalidHintException;
import medview.datahandling.MedViewDataConstants;
import medview.datahandling.PCodeGenerator;
import medview.datahandling.PatientIdentifier;
import medview.datahandling.examination.*;
import medview.datahandling.examination.filter.*;
import medview.datahandling.images.*;

/**
 * Examination data handler for Mun-h center table files (which use Personnummer and Löpnummer)
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class MHCTableFileExaminationDataHandler implements ExaminationDataHandler,
	MedViewDataConstants
{
    /** Constants */
    public static final String LOPNUMMER_FIELD_PARAMETER = "lopnummerfield";
    public static final String PID_FIELD_PARAMETER = "pidfield";

    public static final String DEFAULT_PID = "700101-9996";

    public static final String MHC_USER_ID = "MHC";

    /** Fields */
    private PIDMaker pidMaker;
    private TableFile tableFile;
    private SparseDataTable dataTable;
    private Map columnNameToColumnNumberIntegerMap;
    private int columnCount;
    private int pidColumn, lopnummerColumn;

    private String locationUrl;

    private PatientIdentifier defaultPatientIdentifier;

    // Can't use this handler until these have been explicitly set
    private String pidField = null; // "" if not use pid
    private String lopnummerField = null;

    private static SessionPCodeNRGenerator randomNumberGenerator = new SessionPCodeNRGenerator();
    private static PCodeGenerator pcodeGenerator; 

    /* *** Constructors *** */

    /** Creates a new instance of MHCExcelFileExaminationDataHandler */
    public MHCTableFileExaminationDataHandler(TableFile tableFile)
	throws IOException
    {
        pcodeGenerator = new SessionPCodeGenerator(MHC_USER_ID,
                                                   randomNumberGenerator,
                                                   false); // Don't listen to MVDH for userID updates
        
	pidMaker = new PIDMaker(pcodeGenerator);

	try
	{
	    defaultPatientIdentifier = pidMaker.makePID(DEFAULT_PID);
	}
	catch (medview.datahandling.InvalidPIDException ipide)
	{
	    System.err.println("Warning: Default personnummer invalid!");
	}
	catch (medview.datahandling.CouldNotGeneratePCodeException e)
	{
	    System.err.println("Warning: Could not generate default pode: " + e.getMessage());
	}

	locationUrl = "file://" + tableFile.getPath();
	setFile(tableFile);
    }

    /* Methods */
    
    public PatientIdentifier makePID(String personnummer) throws medview.datahandling.InvalidPIDException, medview.datahandling.CouldNotGeneratePCodeException
    {
	return pidMaker.makePID(personnummer);
    }

    public MHCTableFileExaminationDataHandler(TableFile tableFile, String pidField, String lopnummerField)
	throws IOException
    {
	this(tableFile);
	this.lopnummerField = lopnummerField;
	this.pidField = pidField;
    }

    /**
     * construct from uri: file:///path?lopnummer?...&pid=...
     */
    //public MHCTableFileExaminationDataHandler(URI uri)
    //{



    public MHCTableFileExaminationDataHandler(TableFile tableFile, PCodeGenerator pcodeGenerator)
	throws IOException
    {
	this(tableFile);
	setPCodeGenerator(pcodeGenerator);
    }

    public MHCTableFileExaminationDataHandler(TableFile tableFile, String pidField, String lopnummerField, PCodeGenerator pcodeGenerator)
	throws IOException
    {
	this(tableFile,pidField,lopnummerField);
	setPCodeGenerator(pcodeGenerator);
    }

    /**
     * Allows the datahandler to deal with the system
     * shutting down. For instance, if the datahandler is
     * a client to a server, lets it tell the server that it
     * is shutting down so the server can remove it from its
     * notification list.
     */
    public void shuttingDown(){
    }

    public void setPCodeGenerator (PCodeGenerator pcodeGenerator)
    {
	pidMaker.setPCodeGenerator(pcodeGenerator);
    }

    public TableFile getFile()
    {
	return tableFile;
    }

    /**
     * Read the file and build metadata about the file
     */
    public void setFile(TableFile tableFile) throws IOException
    {
	this.tableFile = tableFile;

	columnNameToColumnNumberIntegerMap = new HashMap();
	SparseMatrix tableMatrix = tableFile.getDataMatrix();
	dataTable = new SparseDataTable(tableMatrix); // Chops off headers etc

	columnCount = dataTable.getColumnCount();
	columnNameToColumnNumberIntegerMap.clear();

	/* Get and store column names from the first row */
	for (int i = 0; i < columnCount; i++)
	{
	    String name = dataTable.getColumnName(i);
	    columnNameToColumnNumberIntegerMap.put(name, new Integer(i));
	}


	/* Get column numbers for quick access */

	// setPidField()

	// pidColumn =)

	// lopnummerColumn =

    }

    public String getPidField()
    {
	return pidField;
    }

    public void setPidField(String pidField) throws IOException
    {
	if (pidField.equals(""))
	{
	    pidColumn = -1;
	    this.pidField = pidField;
	}
	else
	{
	    if (!columnNameToColumnNumberIntegerMap.containsKey(pidField))
		throw new IOException("Table file " + tableFile.getName() + " did not contain pid field column [" + pidField + "]");

	    this.pidField = pidField;

	    pidColumn = ((Integer) columnNameToColumnNumberIntegerMap.get(pidField)).intValue();
	}
    }

    public String getLopnummerField()
    {
	return lopnummerField;
    }

    public void setLopnummerField(String lopnummerField) throws IOException
    {
	if (!columnNameToColumnNumberIntegerMap.containsKey(lopnummerField))
	    throw new IOException("Table file " + tableFile.getName() + " did not contain löpnummer field column [" + lopnummerField + "]");

	this.lopnummerField = lopnummerField;

	lopnummerColumn = ((Integer) columnNameToColumnNumberIntegerMap.get(lopnummerField)).intValue();
    }


    /**
     * Gets the examination rows corresponding to a personnummer
     * @param patient_pid the patient identifier (string)
     *
     */
    private int[] getExaminationRows(String patient_pid)
	throws IOException
    {

	// System.out.println("starting getexaminationrows for " + patient_pid);


	if (pidColumn == -1)
	{
	    // Special case: return all of the rows
	    int rowcount = dataTable.getRowCount();
	    int[] rows = new int[rowcount];

	    for (int i = 0; i < rowcount; i++)
	    {
		rows[i] = i;
	    }

	    return rows;
	}

	Vector v = new Vector();
	String[] pidColumnArray = dataTable.getColumn(pidColumn);


	for (int i = 0; i < pidColumnArray.length; i++)
	{
	    if (pidColumnArray[i].equals(patient_pid))
	    v.add(new Integer(i));
	}

	int[] rows = new int[v.size()];
	for (int i = 0; i < v.size(); i++)
	{
	    rows[i] = ((Integer) v.get(i)).intValue();
	}

	// System.out.println("Getexaminationrows done: Patient PID " + patient_pid + " has " + rows.length + " rows");
	return rows;
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

    public synchronized final ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id) throws
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

    public ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id, int hint)
	throws IOException, NoSuchExaminationException, InvalidHintException
    {
	//System.out.println("GetEVC for id" + id);

	String personnummer = id.getPID().getPID();
	String lopnummer = id.getExaminationIDString();

	// hitta raderna for personnumret

	int row = -1;
	if (pidColumn != -1) // only check rows with the correct Pid
	{
	    int[] examinationRows = examinationRows = getExaminationRows(personnummer);

	    // hoppa till rad går snabbt...

	    for (int r = 0; r < examinationRows.length; r++)
	    {
		String nextLopnummer = dataTable.getCell(examinationRows[r], lopnummerColumn);
		if (nextLopnummer.equals(lopnummer))
		{
		    // Found the correct row!
		    row = r;
		    break;
		}
	    }
	}
	else
	{
	    // We don't use pid, so check all the rows
	    for (int r = 0; r < dataTable.getRowCount(); r++)
	    {
		String nextLopnummer = dataTable.getCell(r, lopnummerColumn);

		if (nextLopnummer.equals(lopnummer))
		{
		    // Found the correct row!
		    row = r;
		    break;
		}
	    }
	}

	if ( row < 0)
	{   // No rows matched - no such examination!
	    //System.out.println("No matches for " + lopnummer);
	    throw new NoSuchExaminationException(id.toString());
	}

	// Create the EVC
	medview.datahandling.examination.tree.ExaminationValueTable evc =
	    new medview.datahandling.examination.tree.ExaminationValueTable();

	String[] rowItems = dataTable.getRow(row);
	for (int i = 0; i < rowItems.length; i++)
	{
	    if (! ((rowItems[i] == null) || (rowItems[i].equals(""))))
		evc.addValue(dataTable.getColumnName(i), rowItems[i]);
	}

	//System.out.println("GetEVC done");
	return evc;

    }

    public ExaminationIdentifier[] getExaminations(String patientCode) throws IOException {
	throw new IOException("getExaminations(String) Not implemented since deprecated");
    }

    public ExaminationIdentifier[] getExaminations(PatientIdentifier pid) throws IOException
    {
	//System.out.println("Getexaminations for " + pid);

	if (pidColumn == -1)
	{
	    // Special case: Return all rows
	    int rowCount = dataTable.getRowCount();
	    ExaminationIdentifier[] examinationIdentifierArray = new ExaminationIdentifier[rowCount];
	    for (int row = 0; row < rowCount; row++)
	    {
		// Store the lopnummer for this row
		String lopnummer = dataTable.getCell(row, lopnummerColumn);
		examinationIdentifierArray[row] = new GenericExaminationIdentifier(defaultPatientIdentifier, lopnummer);
	    }

	    return examinationIdentifierArray;
	}


	String pidString = pid.getPID(); // The personnummer, type 19xxxxxx-xxxx

	Set lopnummerSet = new HashSet();

	// Get column with all patients
	String[] pidCells = dataTable.getColumn(pidColumn);

	for (int row = 0; row < pidCells.length; row++)
	{
	    String nextPid = pidCells[row];
	    //try {
		if (pidString.equals(nextPid))
		{
		   // Store the lopnummer for this row
		   String lopnummer = dataTable.getCell(row, lopnummerColumn);
		   System.out.println("Lopnummer " + lopnummer + " matched pid " + pid.toString());
		   lopnummerSet.add(lopnummer); // Only adds if it does not already exist
		}
	    /*} catch (medview.datahandling.InvalidPIDException iPIDe)
	    {
		throw new IOException("getExaminations: Encountered invalid PID [" + thisPersonnummer + "], aborting.");
	    }*/
	}

	// We now have all lopnummer's
	int idCount = lopnummerSet.size();
	String[] lopnummerArray = new String[idCount];
	lopnummerArray = (String[]) lopnummerSet.toArray(lopnummerArray);

	ExaminationIdentifier[] examinationIdentifierArray = new ExaminationIdentifier[idCount];

	// Create examinationIdentifiers
	for (int i = 0; i < idCount; i++)
	{
	    examinationIdentifierArray[i] = new GenericExaminationIdentifier(pid, lopnummerArray[i]);
	}
	// System.out.println("mhcHandler: getexaminations done, got " + examinationIdentifierArray.length + " examinations for patient " + pid.toString());

	return examinationIdentifierArray;
    }

    public PatientIdentifier[] getPatients() throws IOException {
	//System.out.println("getpatients starting");

	if (pidColumn == -1)
	{
	    // Only one patient - return an array with the default pid
	    PatientIdentifier[] pidArr = { defaultPatientIdentifier };
	    return pidArr;
	}

	HashSet pidSet= new HashSet();

	// Get column with all patients
	String[] pidCells = dataTable.getColumn(pidColumn);
	for (int i = 0; i < pidCells.length; i++)
	{
	    String thisPatient = pidCells[i];
	    pidSet.add(thisPatient);
	}


	// Now we have all unique pids in a Set
	int pidAmount = pidSet.size();
	String[] pidArray = new String[pidAmount];
	pidArray = (String[]) pidSet.toArray(pidArray);

	// Get patientIdentifiers for all these Personnummer's
	PatientIdentifier[] identifierArray = new PatientIdentifier[pidAmount];
	for (int i = 0; i < pidAmount; i++)
	{
	    // Generate pcode for a personnummer.
	    // identifierArray[i] = new PatientIdentifier(generatedpcode, personnummerArray[i]);
	    try {
		identifierArray[i] = pidMaker.makePID(pidArray[i]);
	    } catch (medview.datahandling.InvalidPIDException iPIDe)
	    {
		throw new IOException("Invalid PID Exception for pid [" + pidArray[i] + "]: " + iPIDe.getMessage());
	    }
	    catch (medview.datahandling.CouldNotGeneratePCodeException cngpe)
	    {
		throw new IOException("Could not generate P-code: " + cngpe.getMessage());
	    }
	}

	// System.err.println("MHChandler: Getpatients done, got" + identifierArray.length + " patients");

	return identifierArray;
    }

    public PatientIdentifier[] getPatients(misc.foundation.ProgressNotifiable notifiable) throws IOException
    {
	throw new IOException("getPatients(ProgressNotifiable) Not implemented yet");
    }

    public void addExaminationDataHandlerListener(ExaminationDataHandlerListener l) {
    }


    /**
     * Gets the examination data location URL
     */
    public String getExaminationDataLocation() {
	return locationUrl;
    }

    public String getExaminationDataLocationID() {
	return tableFile.getName();
    }

    public int getImageCount(String patientIdentifier) throws IOException {
	return 0; // Not implemented
    }

    public int getImageCount(PatientIdentifier pid) throws IOException {
	return 0; // Not implemented
    }

    public ExaminationImage[] getImages(ExaminationIdentifier id) throws IOException, NoSuchExaminationException
    {
	return new ExaminationImage[0]; // Not implemented for this handler
    }

    public boolean isExaminationDataLocationValid() {
	System.out.println("Tablefile exists:" + tableFile.exists() + ", pidfield null: " + (pidField == null) + ", lopnummerfield null: " + (lopnummerField == null));

	return (( tableFile.exists()) && (pidField != null) && (lopnummerField != null));
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
	    return new ExaminationIdentifier[0]; // file doesn't change
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
     * @param sinceDate the examinations returned are the ones added to
     * the knowledge base after this time.
     * @return ExaminationIdentifier[] all examinations found in the
     * knowledge base after the specified time.
     * @throws IOException if something goes wrong.
     */
    public ExaminationIdentifier[] refreshExaminations(long sinceTime) throws IOException
    {
	    return new ExaminationIdentifier[0]; // file doesn't change
    }

    public void removeExaminationDataHandlerListener(ExaminationDataHandlerListener l) {
    }


    /* Saving examinations is not implemented */
    public int saveExamination(medview.datahandling.examination.tree.Tree tree, ExaminationImage[] imageArray) throws IOException {
	throw new IOException ("NOT IMPLEMENTED");
    }
    public int saveExamination(medview.datahandling.examination.tree.Tree tree, ExaminationImage[] imageArray, String str) throws IOException {
	throw new IOException ("NOT IMPLEMENTED");
    }

    public void removeExamination(ExaminationIdentifier eid) throws IOException
    {
	throw new IOException("Cannot remove examinations in the Tablefile examination handler.");
    }

    // NOTE: Slow (reloads the excel file)
    public void setExaminationDataLocation(String loc)
    {
	locationUrl = loc;
	File newFile = new File(loc);
	if (newFile.exists())
	{
	    try {
		if (newFile.getName().toUpperCase().endsWith(".XLS"))
		    setFile(new ExcelTableFile(newFile));
		else
		    setFile(new TextTableFile(newFile, TextTableFile.DEFAULT_FIELD_DELIMITER));
	    } catch (IOException ioe)
	    {
		// Do nothing??? Since the interface has no error checking?
	    }
	}
    }

    public String[] getAvailableFields()
    {
	return dataTable.getColumnNames();
    }

    public int exportToMVD(PatientIdentifier[] patientIdentifiers, String newMVDlocation, ProgressNotifiable notifiable, ExaminationContentFilter filter, boolean allowPartialExport) throws IOException {
	// TODO: Implement exporting in MHCTableFile...
	throw new IOException("Exporting not implemented in table file handler");
    }

    public int exportToMVD(ExaminationIdentifier[] examinationIdentifiers, String newMVDlocation, ProgressNotifiable notifiable, ExaminationContentFilter filter, boolean allowPartialExport) throws IOException {
	throw new IOException("Exporting not implemented in table file handler");
    }

    public int getExaminationCount() throws IOException
    {
	String[] lopnummerArray = dataTable.getColumn(lopnummerColumn);
	return lopnummerArray.length;
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

}

