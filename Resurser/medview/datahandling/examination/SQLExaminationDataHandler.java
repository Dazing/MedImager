/*
 * SQLExaminationDataHandler.java
 *
 * $Id: SQLExaminationDataHandler.java,v 1.23 2007/01/03 14:15:38 erichson Exp $
 *
 * $Log: SQLExaminationDataHandler.java,v $
 * Revision 1.23  2007/01/03 14:15:38  erichson
 * Misspelled IOException
 *
 * Revision 1.22  2007/01/03 14:11:00  erichson
 * Cosmetic update
 *
 * Revision 1.21  2006/04/24 14:17:38  lindahlf
 * <No Comment Entered>
 * // removeExamination was added // NE
 *
 * Revision 1.20  2005/09/09 15:40:48  lindahlf
 * Server cachning
 *
 * Revision 1.19  2005/05/20 12:05:54  erichson
 * Cleaned up debug messages
 *
 * Revision 1.18  2005/01/30 15:22:08  lindahlf
 * T4 Integration
 *
 * Revision 1.17  2004/11/19 12:32:30  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.16  2004/11/10 13:07:44  erichson
 * added getExaminationCount() impl
 *
 * Revision 1.15  2004/11/09 21:13:50  lindahlf
 * Datalayer upgrade in connection with server development
 *
 * Revision 1.14  2004/11/06 01:10:55  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.13  2004/11/03 12:38:09  erichson
 * Changed exportToMVD method signature.
 *
 * Revision 1.12  2004/10/21 12:17:44  erichson
 * added progressNotifiable to the exportToMVD calls.
 *
 * Revision 1.11  2004/10/06 14:25:55  erichson
 * added dummy exportToMVD methods.
 *
 * Revision 1.10  2004/10/05 08:56:59  erichson
 * added setPcodeGenerator and createDBUri
 *
 * Revision 1.9  2004/10/01 16:39:49  lindahlf
 * no message
 *
 * Revision 1.8  2004/09/09 11:02:09  erichson
 * Added windows authentication (experimental)
 * Pid generating code moved to PIDmaker (for use in other data handlers)
 *
 * Revision 1.7  2004/08/24 18:25:13  lindahlf
 * no message
 * // Fredrik added second saveExamination() call i think // NE
 *
 * Revision 1.6  2004/06/24 15:43:10  d97nix
 * Updated since names in datahandling had changed (RawPID* classes etc)
 *
 * Revision 1.5  2004/04/12 20:11:32  erichson
 * * caches Pcodes generated from PIDs in a hashmap
 * * added finalize() to close down database connectivity when garbage collecting
 * * Quick fix for converting PID with years with only 2 numbers (this needs to be generalized later)
 *
 * Revision 1.4  2004/04/06 19:11:48  erichson
 * Revision 1.3 commit should have read: Column loading now works.
 *
 * Revision 1.3  2004/04/06 19:05:59  erichson
 * Added getExaminationIDString()
 *
 * Revision 1.2  2004/04/05 20:49:48  erichson
 * Finally fetches examinations properly. However, it does not get all terms.
 *
 * Revision 1.1  2004/03/26 17:26:32  erichson
 * First check-in: semi-functional version.
 *
 */

package medview.datahandling.examination;

import java.io.IOException;
import java.net.URI;
import java.sql.*;
import java.util.*;

import misc.foundation.ProgressNotifiable;

import medview.datahandling.InvalidHintException;
import medview.datahandling.MedViewDataConstants;
import medview.datahandling.PCodeGenerator;
import medview.datahandling.examination.tree.ExaminationValueTable;
import medview.datahandling.examination.filter.*;
import medview.datahandling.InvalidPIDException;
import medview.datahandling.MedViewDataHandler;
import medview.datahandling.PatientIdentifier;


/**
 * ExaminationDataHandler implementation for SQL databases.
 *
 * Implementation notes:
 * ExaminationDataLocation string is implemented as the JDBC url.
 * Saving examinations is not supported in this implementation.
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 *
 */
public class SQLExaminationDataHandler 
    implements medview.datahandling.examination.ExaminationDataHandler, 
               MedViewDataConstants
{

    /* ----- Constants ----- */

    /**
     * ExaminationIdentifier field types
     */
    public static final int EXAMINATIONIDENTIFIER_DATE = 1; // For regular MedView which identifies examinations by date. Uses MedViewExaminationIdentifier
    public static final int EXAMINATIONIDENTIFIER_STRING = 2; // For MHC and others, which identify examinations by a string

    /**
     * Authentication field types
     */
    public static final int AUTHENTICATION_SQL = 1;
    public static final int AUTHENTICATION_WINDOWS = 2;

    /* ----- Fields ----- */

    private medview.datahandling.examination.tablefile.PIDMaker pidMaker;     // Visualizer will set this to force it's own pcodeGenerator. Otherwise MVDH is used.

    private static final MedViewDataHandler MVDH = MedViewDataHandler.instance();

    /**
     * For use in refreshExaminations() - time of last refresh. Updated by:
     * getExaminations(), getPatients(), getImageCount() getExaminationValueContainer()
     * and refreshExaminations() itself.
     */
    private java.util.Date lastRefresh;

    /**
     * URL Address of the SQL server
     */
    private String serverURL;

    private String serverPassword = null;
    private String serverUserName = null;

    /**
     * The name/catalog of the database to use. c f mysql "USE" command
     */
    private String dbCatalog;

    /**
     * The name of the table/view to access
     */
    private String tableName;

    /**
     * Names of the fields to access
     */
    private String patientIdentifierField;
    //private String examinationDateField;
    private String examinationIdentifierField;

    // Database stuff
    private Connection connection;
    private PreparedStatement getAllExaminationsPreparedStatement;
    private PreparedStatement getExaminationsPreparedStatement;
    private PreparedStatement getPatientsPreparedStatement;
    private PreparedStatement getEVCPreparedStatement;
    private String jdbcDriverClass;

    private int examinationIdentifierType; // EXAMINATIONIDENTIFIER_DATE or EXAMINATIONIDENTIFIER_STRING


    /* Constructors and destructors */

    /**
     * Create new SQLExaminationDataHandler from an URL, which includes username and password
     *
     */
    public SQLExaminationDataHandler(URI serverURL,
				     String username,
				     String password,
				     String catalogName, // catalog
				     String tableName,
				     String patientIdentifierField,
				     String examinationIdentifierField,
				     int examinationIdentifierType)
	 throws SQLException, ClassNotFoundException
    {
	this.serverURL = serverURL.toString();
	serverUserName = username; // may be null
	serverPassword = password; // May be null
	dbCatalog = catalogName;
	this.tableName = tableName;
	this.patientIdentifierField = patientIdentifierField;
	this.examinationIdentifierField = examinationIdentifierField;
	this.examinationIdentifierType = examinationIdentifierType;
	pidMaker = new medview.datahandling.examination.tablefile.PIDMaker();
	initDB();
    }

    /**
     * Creates a new SQLExaminationDataHandler without user name and password (they will be provided in the call to getConnection later)
     *
     */
    public SQLExaminationDataHandler(URI serverURL,
				     String dbName,
				     String tableName,
				     String patientIdentifierField,
				     String examinationIdentifierField,
				     int examinationIdentifierType)

      throws SQLException, ClassNotFoundException
    {
	this(serverURL,
	    null, // user
	    null, // password
	    dbName, tableName, patientIdentifierField,
	    examinationIdentifierField,examinationIdentifierType
	);
    }

    /**
     * Allows the datahandler to deal with the system
     * shutting down. For instance, if the datahandler is
     * a client to a server, lets it tell the server that it
     * is shutting down so the server can remove it from its
     * notification list.
     */
    public void shuttingDown()
    {    }

    /**
     * Finalizer method will clean up our footprints on the SQL server and close the connection.
     */
    public void finalize()
    {
	try {
	    getAllExaminationsPreparedStatement.close();
	    getExaminationsPreparedStatement.close();
	    getPatientsPreparedStatement.close();
	    getEVCPreparedStatement.close();
	    connection.close();
	}
	catch (SQLException sqle)
	{
	    // Well, what can you do?
	}
    }

    /* ----- Methods ----- */

    /**
     * Get the appropriate JDBC driver class name for a jdbc URI
     */
    private String getJDBCDriverClassName(String uriString) throws java.net.URISyntaxException
    {

	java.net.URI uri = new java.net.URI(uriString);
	StringTokenizer tokenizer = new StringTokenizer(uri.getSchemeSpecificPart(),":");
	String jdbcType = tokenizer.nextToken();
	if (jdbcType.equals("jtds"))
	    return "net.sourceforge.jtds.jdbc.Driver";
	else
	    throw new java.net.URISyntaxException(uriString,"Unknown JDBC protocol type: " + jdbcType);
    }

    /**
     * Set up the DB for processing - connect and install prepared statements etc
     */
    private void initDB() throws ClassNotFoundException, SQLException {

	// Make sure correct JDBC driver is installed
	try {
	    Class.forName(getJDBCDriverClassName(serverURL));
	} catch (java.net.URISyntaxException URIse) {
	    throw new SQLException("Could not determine a JDBC Driver: " + URIse.getMessage());
	}

	/* Connect to the database */

	// If user name and password are set, use them
	if ((serverUserName != null) && (serverPassword != null))
	    connection = DriverManager.getConnection(serverURL.toString(), serverUserName, serverPassword);
	else // Otherwise, assume they are in the URL
	    connection = DriverManager.getConnection(serverURL.toString());

	getPatientsPreparedStatement = connection.prepareStatement(
	    "SELECT DISTINCT " + patientIdentifierField +
	    " FROM " + tableName
	);

	getAllExaminationsPreparedStatement = connection.prepareStatement(
	    "SELECT DISTINCT " +
		examinationIdentifierField + ", " + patientIdentifierField +
	    " FROM " + tableName
	);

	getExaminationsPreparedStatement = connection.prepareStatement(
	    "SELECT " + examinationIdentifierField + "," + patientIdentifierField +
	    " FROM " + tableName +
	    " WHERE " + patientIdentifierField +
	    " = ?"
	   //" LIKE ?"
	);

	// Get column names
	DatabaseMetaData databaseMetaData = connection.getMetaData();

	// dumpMetaData(databaseMetaData); // debug

	ResultSet rs = databaseMetaData.getColumns(dbCatalog, // catalog
						   null, // schemaPattern // should not be used to narrow the search
						   tableName, // tableNamePattern
						   "%"); // columnNamePattern // match all columns
	// Each row corresponds to a column
	StringBuffer columnNameBuffer = new StringBuffer();
	boolean nextRowExists = rs.next();
	while (nextRowExists) {
	    String name = rs.getString("COLUMN_NAME"); // See DatabaseMetaData documentation

	    columnNameBuffer.append("["); // In case of column names with spaces, they must be enclosed within [ ]
	    columnNameBuffer.append(name);
	    columnNameBuffer.append("]");

	    nextRowExists = rs.next();
	    if (nextRowExists)
		columnNameBuffer.append(", ");
	}

	String allColumnsString = columnNameBuffer.toString();
	// System.out.println("all columns = " + allColumnsString);

	String getEVCStatementString =
	    "SELECT " + allColumnsString +
	    " FROM " + tableName +
	    " WHERE " + patientIdentifierField +
	    " = ?" +
	    " AND " + examinationIdentifierField +
	    " = ?";

	getEVCPreparedStatement = connection.prepareStatement(getEVCStatementString);
    }

    public void addExaminationDataHandlerListener(ExaminationDataHandlerListener l) {
    }

    /**
     * Returns the data location expressed in a
     * (perhaps) more simple way than the 'raw'
     * data location. For example, it could
     * return 'MSTest.mvd' instead of the full
     * file path to the mvd. This could be used
     * for displaying the chosen location in a
     * graphical user interface, for example. A
     * string specifying that the location is not
     * valid or not set correctly will be returned
     * if the location is not set or invalid.
     */
    public String getExaminationDataLocationID() {
	try {
	    java.net.URI url = new java.net.URI(serverURL);
	    return url.getHost();
	} catch (java.net.URISyntaxException URIse) {
	    return serverURL; // Fallback case
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

	public ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id) throws // Fredrik 041119
		IOException, NoSuchExaminationException
	{
		try
		{
			return this.getExaminationValueContainer(id, OPTIMIZE_FOR_MEMORY);
		}
		catch (InvalidHintException exc)
		{
			throw new IOException(exc.getMessage());
		}
	}

    public ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id, int hint)
	throws IOException, NoSuchExaminationException, InvalidHintException
    {

	// System.out.println("Getting examinationvaluecontainer for " + id.toString());
	updateRefreshTime(); // Should be done according to refreshExaminations() javadoc
	try
	{
	    String pidString = id.getPID().toString();
	    if (pidString.length() > 11)
		pidString = pidString.substring(2); // Cut off first two letters of year

	    String examinationIdString = id.getExaminationIDString();
	    // System.out.println("Debug: pidString == " + pidString + ", examinationIdString = " + examinationIdString);
	    getEVCPreparedStatement.setString(1,pidString ); // Set PID in prepared statement
	    getEVCPreparedStatement.setString(2,examinationIdString);

	    // Execute the query
	    ResultSet rs = getEVCPreparedStatement.executeQuery();

	    // Get the column names
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int numberOfColumns = rsmd.getColumnCount();

	    //System.out.println("Number of columns for this EVC target = " + numberOfColumns);

	    String[] columnNames = new String[numberOfColumns];
	    for (int i = 0; i < numberOfColumns; i++)
	    {
		columnNames[i] = rsmd.getColumnName(i+1); // i+1 because SQL columns are indexed starting from 1, not 0...
	    }

	    // Process the resultSet
	    ExaminationValueTable evc = new ExaminationValueTable();

	    boolean nextExists = rs.next(); // Try to go to first row. The result set should only have one row
	    if (!nextExists)
	    {
		throw new NoSuchExaminationException("Resultset empty for id " + id.toString());
	    }

	    for( int i = 0; i < numberOfColumns; i++) {
		evc.addValue(columnNames[i], rs.getString(i+1)); // Once again i+1 because SQL comuns are indexed from 1, not 0
	    }

	    // System.out.println("Got examinationvaluecontainer for " + id.toString());

	    return evc;
	} catch (SQLException sqle) {
	    throw new IOException("SQLException: " + sqle.getMessage());
	}
    }

    /**
     * @deprecated use getExaminations(PatientIdentifier) instead
     */
    public ExaminationIdentifier[] getExaminations(String patientCode) throws IOException {
	throw new IOException("Please use getExaminations(PatientIdentifier) instead!");
	/*
	try {
	    return doGetExaminationsQuery(patientCode);
	} catch (SQLException sqle) {
	    throw new IOException("SQLException: " + sqle.getMessage());
	}
	 */
    }

    /**
     * This should later be generalized so we can use different DB formats
     */
    private String convertPIDtoDBformat(PatientIdentifier pid) {
	String pidString = pid.getPID(); // Returns pid like "199910313288"; ie length 12
	if (pidString.length() > 11) // remove 2 first digits
	{
	    // System.out.println("convert: original pid " + pidString);
	    // pidString = pidString.substring(2,8) + "-" + pidString.substring(8);
	    pidString = pidString.substring(2);
	}
	return pidString;
    }

    public ExaminationIdentifier[] getExaminations(medview.datahandling.PatientIdentifier pid) throws IOException {
	//System.out.println("getExaminations for " + pid);
	updateRefreshTime(); // Should be done according to refreshExaminations() javadoc

	try {
	    return doGetExaminationsQuery(convertPIDtoDBformat(pid));
	} catch (IOException sqle) {
	    throw new IOException("doGetExaminationsQuery failed with Exception: " + sqle.getMessage());
	}
    }

    private String debugSQLException(SQLException sqle) {
	StringBuffer sb = new StringBuffer();
	sb.append("Message = " + sqle.getMessage() + "\n");
	sb.append("SQLState = " + sqle.getSQLState() +"\n");

	SQLException next = sqle.getNextException();
	if (next != null) {
	    sb.append("Next: \n");
	    sb.append(debugSQLException(next));
	}
	return sb.toString();
    }

    private ExaminationIdentifier[] doGetExaminationsQuery(String patientID) throws IOException  {

	ResultSet rs;
	try {
	    //System.out.println("Doing getExaminations for " + patientID);
	    getExaminationsPreparedStatement.setString(1,patientID);

	    // System.out.println("statement = " + getExaminationsPreparedStatement.toString() +". Executing query");
	    getExaminationsPreparedStatement.executeQuery();
	    //System.out.println("Getting result set");
	    rs = getExaminationsPreparedStatement.getResultSet();
	    //System.out.println("Got a result set");
	} catch (SQLException sqle) {
	    throw new IOException("Could not get examination list because of SQLException: " + debugSQLException(sqle));
	}
	// Process result
	Vector identifierVector = new Vector();
	try {
	    while (rs.next()) {
		String patientIdentifierString;
		try {
		    patientIdentifierString = rs.getString(patientIdentifierField);
		} catch (SQLException sqle) {
		    throw new IOException("Could not get PIDfield for patient because of SQLException: " + debugSQLException(sqle));
		}

		// String pcode = "A00019111"; // DUMMMY pcode, we only use pid.
					    // Had to do it this way since PatientIdentifier requires a pcode.

		// PatientIdentifier pid = new PatientIdentifier(pcode, patientIdentifierString);

		PatientIdentifier pid;
		try {
		    pid = pidMaker.makePID(patientIdentifierString);
		}
		catch (InvalidPIDException iPIDe)
		{
		    throw new IOException("Could not get PCode from pid " + patientIdentifierString + ": " + iPIDe.getMessage());
		}
		catch (medview.datahandling.CouldNotGeneratePCodeException cngPCe)
		{
		    throw new IOException("Could not generate PCode for pid " + patientIdentifierString + ": " + cngPCe.getMessage());
		}

		ExaminationIdentifier identifier;

		switch (examinationIdentifierType) {
		    case EXAMINATIONIDENTIFIER_DATE:
			java.sql.Date examinationDate = rs.getDate(examinationIdentifierField);
			identifier = new MedViewExaminationIdentifier(pid, examinationDate);
			break;
		    case EXAMINATIONIDENTIFIER_STRING:
		    default:
			String examinationID = rs.getString(examinationIdentifierField);
			identifier = new GenericExaminationIdentifier(pid, examinationID);
			break;
		}

		identifierVector.add(identifier);
	    }
	} catch (SQLException sqle) {
	    throw new IOException("Failed to get patient info for " + patientID + " because of SQLException: " + debugSQLException(sqle));
	}
	ExaminationIdentifier[] identifierArray = new ExaminationIdentifier[identifierVector.size()];
	identifierArray = (ExaminationIdentifier[]) identifierVector.toArray(identifierArray);

	// System.out.println("Done - " + identifierArray.length + " examinations found for patient " + patientID);
	return identifierArray;

    }


    /**
     * @deprecated use getImageCount(PatientIdentifier) instead
     */
    public int getImageCount(String patientIdentifier) {
	updateRefreshTime(); // Should be done according to refreshExaminations() javadoc

	return 0; // No standard for images via SQL server exists yet
    }

    public int getImageCount(medview.datahandling.PatientIdentifier pid) {
	updateRefreshTime(); // Should be done according to refreshExaminations() javadoc

	return 0; // No standard for images via SQL server exists yet
    }

    public medview.datahandling.images.ExaminationImage[] getImages(ExaminationIdentifier id) {
	return new medview.datahandling.images.ExaminationImage[0]; // No standard for images via SQL server exists yet
    }

    public medview.datahandling.PatientIdentifier[] getPatients() throws IOException {
	// Fetch all entries in pid-field row, without progress handling
	return getPatients((misc.foundation.ProgressNotifiable) null);
    }

    public medview.datahandling.PatientIdentifier[] getPatients(misc.foundation.ProgressNotifiable notifiable) throws IOException {
	// System.out.println("Listing patients in SQL db!");
	// TODO: update to use progressnotifiable...
	// also check if progressnotifiable is null (from getPatients())
	try {
	    ResultSet rs = getPatientsPreparedStatement.executeQuery();
	    updateRefreshTime(); // Should be done according to refreshExaminations() javadoc


	    Vector v = new Vector();

	    while (rs.next()) // has another row
	    {
		String pidString = rs.getString(patientIdentifierField);

		try {
		    PatientIdentifier pid = pidMaker.makePID(pidString);
		    v.add(pid);
		} catch (InvalidPIDException iPIDe) {
		    throw new IOException("MakePid threw InvalidPIDException  p-code for PID [" + pidString + "]: " + iPIDe.getMessage());
		} catch (medview.datahandling.CouldNotGeneratePCodeException cngpce) {
		    throw new IOException("Could generate p-code for PID [" + pidString + "]: " + cngpce.getMessage());
		}
	    }

	    // Convert vector to array
	    PatientIdentifier[] pidArray = new PatientIdentifier[v.size()];
	    pidArray = (PatientIdentifier[]) v.toArray(pidArray);

	    //System.out.println("Listed patients OK (" + pidArray.length + " patients)");

	    return pidArray;
	} catch (SQLException sqle) {
	    throw new IOException("getPatients: SQLException: " + debugSQLException(sqle));
	}
    }

    /**
     * Returns whether or not the current implementation
     * class for the ExaminationDataHandler interface deems
     * the currently set location to be valid. If the
     * location has not been set, it is deemed invalid
     */
    public boolean isExaminationDataLocationValid()
    {
	try {
	    java.net.URI url = new java.net.URI(serverURL);
	} catch (java.net.URISyntaxException URIse) {
	    System.err.println("Warning: examination data location not valid: URISyntaxException:" + URIse.getMessage());
	    return false;
	}

	// Check that there is a proper connection
	if (connection == null)
	    return false;

	// check db exists
	// check table exists

	return true;
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
	    updateRefreshTime();

	    throw new IOException("refreshExaminations not implemented in SQLExaminationDataHandler due to lack of time // Nils");
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
	    updateRefreshTime();

	   throw new IOException("refreshExaminations not implemented in SQLExaminationDataHandler due to lack of time // Nils");
    }

    public void removeExaminationDataHandlerListener(ExaminationDataHandlerListener l) {
    }

    public int saveExamination(medview.datahandling.examination.tree.Tree tree, medview.datahandling.images.ExaminationImage[] imageArray) throws IOException {
	throw new IOException("SaveExamination not implemented in SQLExaminationDataHandler");
    }

    public int saveExamination(medview.datahandling.examination.tree.Tree root, medview.datahandling.images.ExaminationImage[] imageArray, String location) throws IOException {
	throw new IOException("SaveExamination not implemented in SQLExaminationDataHandler");
    }

    public void removeExamination(ExaminationIdentifier eid) throws IOException
    {
        throw new IOException("SQLExaminationDataHandler does not support removing examinations");
    }

    // ExaminationDataLocation string is interpreted as the JDBC url for this implementation.
    public String getExaminationDataLocation()
    {
	// return serverUserName + ":" + serverPassword + "@" + serverAddress + ":" + serverPort + "/" + activeDatabase + "/" + tableName;
	return serverURL.toString();
    }
    public void setExaminationDataLocation(String loc)
    {
	serverURL = loc;
    }

    /**
     * Updates the "last refresh" time to now
     */
    private void updateRefreshTime()
    {
	lastRefresh = new java.util.Date(); // new Date() sets time to now (accurate to millisecond)
    }

    /**
     * Visualizer will set this to force it's own pcodeGenerator. Otherwise MVDH is used.
     */
    public void setPCodeGenerator(PCodeGenerator generator)
    {
	pidMaker.setPCodeGenerator(generator);
    }

    public static java.net.URI createDBURI(String host,
				    int port,
				    String catalog,
				    String username,
				    String password,
				    String appName,
				    String windowsDomain) throws java.net.URISyntaxException
    {
	String urlString = ("jdbc:jtds:sqlserver://" + host
					+ ":" + port
					+ "/" + catalog
					+ ";user=" + username
					+ ";password=" + password
					+ ";progName=" + appName);
	if (! ((windowsDomain == null) || (windowsDomain.equals(""))))
	{
	    urlString = urlString + ";domain=" + windowsDomain;
	}
	return new java.net.URI(urlString);
    }

    /**
     * Mainly test method
     */
    private void dumpMetaData(DatabaseMetaData metaData) {

	try {
	    System.out.println("Catalog term = " + metaData.getCatalogTerm());
	    System.out.println("Schema term = " + metaData.getSchemaTerm());

	    // Catalogs
	    ResultSet rs = metaData.getCatalogs();
	    while (rs.next()) {
		System.out.println("Found catalog " + rs.getString("TABLE_CAT"));
	    }

	    rs = metaData.getSchemas();
	    while (rs.next()) {
		System.out.println("Found schema " + rs.getString("TABLE_SCHEM"));
		// System.out.println(" with catalog name " + rs.getString("TABLE_CATALOG"));
	    }
	} catch (SQLException sqle) {
	    System.err.println("dumpMetaData failed: SQLException: " + sqle.getMessage());
	}
    }

    public int exportToMVD(PatientIdentifier[] patientIdentifiers, String newMVDlocation, ProgressNotifiable notifiable, ExaminationContentFilter filter, boolean allowPartialExport) throws IOException {
	// TODO: Implement export in SQLExaminationDataHandler
	throw new IOException("Export to MVD not yet implemented for SQL databases.");
    }

    public int exportToMVD(ExaminationIdentifier[] examinationIdentifiers, String newMVDlocation, ProgressNotifiable notifiable, ExaminationContentFilter filter, boolean allowPartialExport) throws IOException {
	throw new IOException("Export to MVD not yet implemented for SQL databases.");
    }

    public int getExaminationCount() throws IOException
    {
	try
	{
	    getAllExaminationsPreparedStatement.executeQuery();

	    //System.out.println("Getting result set");

	    ResultSet rs = getExaminationsPreparedStatement.getResultSet();
	    rs.last(); // Move to last row
	    int count = rs.getRow();// Rows are numbered from 1 (0 is for "no current row") so row number = amount
	    rs.close();
	    return count;

	} catch (SQLException sqle)
	{
	    throw new IOException("SQL exception: " + sqle.getMessage());
	}
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
