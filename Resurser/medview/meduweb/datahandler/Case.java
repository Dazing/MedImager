package medview.meduweb.datahandler;

import java.sql.*;              //JDBC stuff
import org.postgresql.Driver;   //PostGreSQL Driver for JDBC
import medview.meduweb.datahandler.Datahandler;
import medview.meduweb.data.DataLayer;
import java.util.*;             //ArrayList

/**
 *This class is a part of ths datahandler for the mEduWeb 
 *system. It contains methods used when retrieving cases
 *from the MedView database.
 *It uses the class Datahandler which contains methods used
 *to manage the database connection(s). 
 *@author Figge
 *@version 1.0
 */
public class Case {

    private static Boolean updated;

    public Case() {
	if (updated == null ||
	    updated.booleanValue() == false) {
	    DataLayer.destroyMe();
	    dl = DataLayer.getInstance();
	    //reinsert categories
	    Category category = new Category();
	    category.remakeCategories();
	    //remove attributes
	    category.remakeIgnoredAttributes();
	    //set flag to indicate that update has been done
	    updated = new Boolean(true);
	} else {
	    dl = DataLayer.getInstance();
	}
    }

    /**
     * This method sets a flag so that the next time a new Case is
     * instansiated, the DataLayer will be rebuilt from scratch.
     */
    public static void flush() {
	updated = new Boolean(false);
    }

    /**
     *This class is used to return a saved case.
     *@author Figge
     *@version 1.0
     */
    public class SavedCase {
	/**
	 *This attribute holds the PCode for the saved case.
	 */
	public String pcode = "";
	/**
	 *This attribute holds the title for the saved case.
	 */
	public String title = "";

	/**
	 *Returns a reference to a new object of type SavedCase.
	 *@param pc a PCode
	 *@param ti a title
	 *@return SavedCase
	 */
	public SavedCase(String pc, String ti) {
	    this.pcode = pc;
	    this.title = ti; 
	}

    }

    /**
     *This attribute contains a reference to a new
     *Datahandler object.
     */
    private Datahandler dh = new Datahandler();

    /**
     *This attribute contains a reference to the 
     *DataLayer Object.
     */
    private DataLayer dl;

    //bygg om sen!
    /**
     * Gets all values for a category.
     */
    public ArrayList getCategoryValues(String category) {
	String str = "lichen planus";
	ArrayList list = new ArrayList();
	list.add(str);
	return list;
    }

    /**
     * gets a random patient from the category;
     */
    public ArrayList getRandomPatientFromDiagnose(String diagnose, int i) {
	ArrayList list = dl.searchAttribute("Diag-tent", diagnose);
	ArrayList out = new ArrayList();
	if (list != null && list.size() > 0) {
	    for (int j=0; j<i; j++) {
		int index = (int)(Math.random()*list.size());
		if (list.size() > 0) {
		    out.add((String)list.remove(index));
		}
		else {break;}
	    }
	}
	return out;
    }

    /**
     *Deletes a saved case from the mEduWeb database.
     *@param pcode The PCode of the Saved Case to be deleted.
     *@param name The username for the user.
     *@return void
     */
    public void deleteCase(String pcode, String name) {
	Statement stmt = null;
	try{
	    stmt = dh.connect();
	    
	    stmt.executeUpdate("DELETE FROM savedcasestab " +
			       "WHERE pcode='" + pcode + "' AND " +
			       "userid='" + name + "'");
	    dh.disconnect();
	}
	catch(SQLException sqle) {
	}
    }

    /**
     *Tells whether a specific PCode exists or not.
     *@param pcode The PCode to check.
     *@return boolean True if the PCode is in the
     *MedView database, False otherwise.
     */
    public boolean existPCode(String pcode) {
	return dl.existingPCode(pcode);
    }

    /**
     *Lists all searcheable attributes present in 
     *the MedView database.
     *@return String[] List of present attributes.
     */
    public ArrayList getAttributes() {
	return dl.listAttributes();
    }

    /**
     *Lists all present values for the given attribute.
     *@param attribute The attr. to list values for.
     *@return String[] A list of values.
     */
    public ArrayList getAttributeValues(String attribute) {
	return dl.listValues(attribute);
    }

	/**
	*Lists all present values for the given attribute and removes 
	*values that aren't present under categories.
	*@param attribute The attr. to list values for.
     	*@return ArrayList A list of values.
     	*/
	public ArrayList getAttributeValuesClean(String attribute) {
		ArrayList result = new ArrayList();
		Iterator tempVals = dl.listValues(attribute).iterator();
		ArrayList valsWithoutCats = dl.listValuesWithoutCategories(attribute);
		while(tempVals.hasNext()) {
			String tempValue = (String)tempVals.next();
			if(tempValue.startsWith("-")) {
				if(contains(tempValue.substring(1,tempValue.length()), valsWithoutCats)) {
					result.add(tempValue);
				}
			} else {
				result.add(tempValue);	
			}	
		}
		return result;	
	}
		
	private boolean contains(String value, ArrayList al) {
		Iterator theI = al.iterator();
		while(theI.hasNext()) {
			String tempValue = ((String)theI.next());

			if(tempValue.equals(value)) {
				return true;
			}
		}
		return false;
	}		
    /**
     * Lists all values for an attribute, without using categories.
     * @param attribute The Attribute for which to list the values.
     * @return ArrayList of strings containing the values.
     */
    public ArrayList getAttributeValuesWithoutCategories(String attribute) {
	return dl.listValuesWithoutCategories(attribute);
    }

    public ArrayList getAttributeCategories(String attribute) {
    	return dl.listCategories(attribute);	
    }
    
    /**
     *Gets a case from the MedView database.
     */
    // public getCase(String pcode) {

    //}

    /**
     *Gets the formatted text for a case in the
     *specified language.
     *@param pcode A valid PCode.
     *@param template A present xml-template.
     *@param transl A valid translator.
     *@return String The formatted text. 
     */
    public String getPatientText(String pcode,
				 String template,
				 String transl) {
	return dl.getPatientText(pcode,template,transl);
    }

    /**
     *Gets a list with urls to the images associated
     *with a specific case.
     *@param pcode A valid pcode identifying the case.
     *@return A list of urls.
     */
    public ArrayList getPatientImages(String pcode) {
	//Get paths for images
	return dl.getPatientImages(pcode);
    }

    /**
     *Gets a list of all PCodes present in the MedView db.
     *@return ArrayList containing Strings of PCodes.
     */
    public ArrayList getPcodes() {
	return dl.getPcodes();
    }

    /**
     *Gets PCodes and titles for all saved cases for
     *a specific user.
     *@param userid A valid userid.
     *@return ArrayList containing SavedCase objects, all saved cases.
     */
    public ArrayList getSavedCases(String userid) {
	ArrayList tmpCases = new ArrayList();
	int i = 0;
	ResultSet rs = null;
	Statement stmt = null;
	try {
	    stmt = dh.connect();
	    rs = stmt.executeQuery("SELECT * FROM savedcasestab " +
				   "WHERE userid='" + userid + "'");
	    while(rs.next()){
		tmpCases.add(new SavedCase(rs.getString("pcode"),
					   rs.getString("title")));
	    }
	    dh.disconnect();
	}catch(SQLException sqle) {
	}

	return tmpCases;
    }

    /**
     *Gets PCodes and titles for x saved cases for
     *a specific user.
     *@param userid A valid userid.
     *@param x The number of saved cases to get
     *@return ArrayList containing SavedCase objects, x saved cases.
     */
    public ArrayList getSavedCases(String userid, int x) {
	ArrayList tmpCases = new ArrayList();
	ResultSet rs = null;
	Statement stmt = null;
	try {
	    stmt = dh.connect();
	    rs = stmt.executeQuery("SELECT * FROM savedcasestab " +
				   "WHERE userid='" + userid + "'");
	    while(rs.next() && x > 0){
                x--;
		tmpCases.add(new SavedCase(rs.getString("pcode"),
					   rs.getString("title")));
	    }
	    dh.disconnect();
	}catch(SQLException sqle) {
	}

	return tmpCases;
    }


    /**
     *Saves a case to the meduweb database.
     *@param pcode The pcode for the case to be saved.
     *@param title A title for the saved case.   
     *@param userid The id for the user saving the case.
     *@return void
     */
    public void saveCase(String pcode, String title,
			 String userid) {
    	Statement stmt = null;
	try{
	    stmt = dh.connect();
	    stmt.executeUpdate("INSERT INTO savedcasestab " + 
			      "VALUES ('" + userid + "','" +
			       title + "','" + pcode + "')");
	    dh.disconnect();
	}
	catch(SQLException sqle) {
	}
    }

    /**
     *Saves an edited case to the meduweb database.
     *@param pcode The pcode for the case that have been edited.
     *@param casetext The text to display for this edited case.
     *@param editor The userid of the editor.
     *@param inuse Tells whether to use this edition or the one in the MedView db.
     *@param imagelocations The relative locations of the images to display.
     *@throws SQLException 
     */
    public void saveEditedCase(String pcode, String casetext1, String casetext2,
				String editor, boolean inuse,
				String language1, String language2,
				ArrayList imagelocations) throws SQLException {
	Statement stmt = dh.connect();
	ResultSet rs = stmt.executeQuery("SELECT * FROM editedcasetab WHERE " +
						"pcode='" + pcode +"'");
	if(rs.next()) {
		dh.disconnect();
		throw new SQLException("There is already a saved case with this pcode.");
	} else {
		stmt.executeUpdate("INSERT INTO editedcasetab(pcode,editor,inuse) VALUES ('" +
					pcode + "','" +
					editor + "'," + (new Boolean(inuse)).toString() +
					")" );
		stmt.executeUpdate("INSERT INTO editedcasetexttab VALUES('" +
					pcode + "','" + language1 + "','" +
					casetext1 + "')");
		stmt.executeUpdate("INSERT INTO editedcasetexttab VALUES('" +
					pcode + "','" + language2 + "','" +
					casetext2 + "')");
		Iterator imageI = imagelocations.iterator();
		while(imageI.hasNext()) {
			stmt.executeUpdate("INSERT INTO editedcaseimagestab VALUES ('" +
						pcode + "','" + (String)imageI.next() +
						"')" );
		}
		dh.disconnect();
	}
	
    }

    /**
     *Deletes the edited version of the specified case and all of its components.
     *@param pcode The pcode of the case to delete edited version of.
     *@throws SQLException
     */
    public void deleteEditedCase(String pcode) throws SQLException {
	Statement stmt = dh.connect();
	stmt.executeUpdate("DELETE FROM editedcasetexttab WHERE pcode='" + pcode + "'");
	stmt.executeUpdate("DELETE FROM editedcaseimagestab WHERE pcode='" + pcode + "'");
	stmt.executeUpdate("DELETE FROM editedcasetab WHERE pcode='" + pcode + "'");
	dh.disconnect();

    }

    /**
     *Lists the pcodes of all edited cases currently in the database.
     *@return A list of strings which are the pcodes.
     *@throws SQLException
     */
    public ArrayList listEditedCases() throws SQLException {
	ArrayList editedCases = new ArrayList();
	Statement stmt = dh.connect();
	ResultSet rs = stmt.executeQuery("SELECT pcode FROM editedcasetab ORDER BY (pcode)");
	dh.disconnect();
	while(rs.next()) {
		editedCases.add(rs.getString("pcode"));
	}
	return editedCases;
    }

    /**
     *Tells whether this case is already edited or not.
     *@param pcode The pcode of the case to check.	
     *@return True if there is an edited version of this case, else false.
     *@throws SQLException
     */
    public boolean isEdited(String pcode) throws SQLException {
	boolean isedited = false;
	Statement stmt = dh.connect();
	ResultSet rs = stmt.executeQuery("SELECT * FROM editedcasetab WHERE " +
						"pcode='" + pcode + "'");
	dh.disconnect();
	if(rs.next()) {
		isedited = true;
	}
	return isedited;
    }

    /**
     *Tells if the edited version of this case should be used or not.
     *@param pcode The pcode of the case to check.
     *@return True if it should be used, false if shouldn't.
     *@throws SQLException
     */
    public boolean inUse(String pcode) throws SQLException {
	boolean isinuse = false;
	Statement stmt = dh.connect();
	ResultSet rs = stmt.executeQuery("SELECT inuse FROM editedcasetab WHERE " +
						"pcode='" + pcode + "'");
	dh.disconnect();
	if(rs.next()) {
		isinuse = rs.getBoolean("inuse");
	}
	return isinuse;
    }

	/**
	*Sets the edited case to be in use, that means that the edited version
	*should be displayed to the users instaead of the original version.
	*@param pcode The PCode of the case.
	*@param inuse true or false
	*/
    public void setInUse(String pcode, boolean inuse) throws SQLException {
	Statement stmt = dh.connect();
	stmt.executeUpdate("UPDATE editedcasetab SET inuse=" + (new Boolean(inuse)).toString() +
				" WHERE pcode='" + pcode + "'");
	dh.disconnect(); 
    }

    /**
     *Gets the text for an edited case from the meduweb database.
     *@param pcode The pcode of the case to get the text for.
     *@return The text if there is one, else an empty String.
     *@throws SQLException
     */
    public String getEditedCaseText(String pcode, String language) throws SQLException {
	String text = "";
	Statement stmt = dh.connect();
	ResultSet rs = stmt.executeQuery("SELECT casetext FROM editedcasetexttab WHERE " +
						"pcode='" + pcode + "' AND language='" + language + "'");
	if(rs.next()) {
		text = rs.getString("casetext");
	}
	dh.disconnect();
	return text;

    }

    /**
     *Gets the locations of images to a specified edited case.
     *@param pcode The pcode of the case for which to get image locations.
     *@return A list of image locations (Strings).
     *@throws SQLException
     */
    public ArrayList getEditedCaseImages(String pcode) throws SQLException {
	ArrayList images = new ArrayList();
	Statement stmt = dh.connect();
	ResultSet rs = stmt.executeQuery("SELECT imagelocation FROM editedcaseimagestab WHERE " +
						"pcode='" + pcode + "' ORDER BY (imagelocation)");
	dh.disconnect();
	while(rs.next()) {
		images.add(rs.getString("imagelocation"));
	}
	return images;
    }

    /**
     *Gets the editor of a edited case from the meduweb database.
     *@param pcode The pcode of the case.
     *@return the userid of the last editor of this case.
     *@throws SQLException
     */
     public String getEditor(String pcode) throws SQLException {
	String editor = "";
	Statement stmt = dh.connect();
	ResultSet rs = stmt.executeQuery("SELECT editor FROM editedcasetab WHERE " +
						"pcode='" + pcode + "'");
	if(rs.next()) {
		editor = rs.getString("editor");
	}
	dh.disconnect();
	return editor;
     }

    /**
     *Gets the date when this case was last edited from the meduweb db.
     *@param pcode The pcode of the case to get the editdate for.
     *@return The editdate of the specified case, null if case not found.
     *@throws SQLException
     */
     public java.sql.Date getEditDate(String pcode) throws SQLException {
	java.sql.Date date = null;
	Statement stmt = dh.connect();
	ResultSet rs = stmt.executeQuery("SELECT editdate FROM editedcasetab WHERE " +
						"pcode='" + pcode + "'");
	if(rs.next()) {
		date = rs.getDate("editdate");
	}
	dh.disconnect();
	return date;

     }

    /**
     *Lists the PCodes of the cases with the given value
     *of the given attribute.
     *@param attribute The attribute to serch on.
     *@param value The value to search for.
     *@return ArrayList  List of PCodes.
     */
    public ArrayList searchAttribute(String attribute,
				    String value) {
	return dl.searchAttribute(attribute, value);
    }
}
