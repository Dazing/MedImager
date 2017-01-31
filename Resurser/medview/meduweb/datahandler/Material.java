package medview.meduweb.datahandler;

import java.sql.*;              //JDBC stuff
import java.util.*;             //ArrayList...
import medview.meduweb.datahandler.Datahandler;
import medview.meduweb.datahandler.StringStringId;

/**
 *This class is a part of ths datahandler for the mEduWeb 
 *system. It contains methods used for administration purposes.
 *It extends the class Datahandler which contains methods used
 *to manage the database connection(s). 
 *@author Jenny
 *@version 1.00001
 */
public class Material {

    /**
     *This attribute contains a reference to a new
     *Datahandler object.
     */
    private Datahandler dh = new Datahandler();

    
    /**
     * Returns the filename of the the material having the given id.
     *@param id 
     *@return filename
     */
    public String getName(String id) {
	Statement stmt = null;
	ResultSet rs = null;
	String filename = null;
	try {
	    stmt = dh.connect();
	    rs = stmt.executeQuery("SELECT materiallocation FROM materialtab WHERE id=" + id );
	    while (rs.next()) {
		filename = rs.getString("materiallocation");
	    }
	    dh.disconnect();
	}catch(SQLException sqle){
	}    
	return filename;
    }


    /**
     *Gets all course material with id, location and title.
     *@return allMaterial containing id, location and title.
     */
    public ArrayList getAllMaterial() {
	ArrayList allMaterial = new ArrayList();
	Statement stmt = null;
	ResultSet rs = null;

	try {
	    
	    stmt = dh.connect();
	    rs = stmt.executeQuery("SELECT id,materiallocation,title FROM materialtab ORDER BY senddate");
	    while (rs.next()) {
		String s1 = rs.getString("materiallocation");
		String s2 = rs.getString("title");
		int id = Integer.parseInt(rs.getString("id"));
		allMaterial.add(new StringStringId(s1,s2,id));
	    }
	    dh.disconnect();
	}catch(SQLException sqle){
	}
	return allMaterial;
    }

    /**
     *Gets a list of ids for all new materials since a users last login from the database.
     *@param lastLogin taken from usersession.
     *@return ArrayList, a list of (StringStringId):s for new course material.
     */
    public ArrayList getNewMaterial(Timestamp lastLogin) {
	Statement stmt = null;
	ArrayList tmpMaterial = new ArrayList();
	ResultSet rs = null;
	int i = 0;
	
	try {

	    stmt = dh.connect();
	    rs = stmt.executeQuery("SELECT id,materiallocation,title FROM materialtab WHERE senddate > '" + lastLogin + "' ORDER BY senddate");
	    while (rs.next()) {
		String s1 = rs.getString("materiallocation");
		String s2 = rs.getString("title");
		int id = Integer.parseInt(rs.getString("id"));
		tmpMaterial.add(new StringStringId(s1,s2,id));

	    }
		dh.disconnect();
	}catch(SQLException sqle){
	}

	return tmpMaterial;
    }


    /**
     *Gets a list of id, materiallocation and title of the last inserted material.
     *@return ArrayList, a list of (StringStringId):s for last course material.
     */
    public ArrayList getLastMaterial() {
	Statement stmt = null;
	ArrayList tmpMaterial = new ArrayList();
	ResultSet rs = null;
	int i = 0;
	
	try {

	    stmt = dh.connect();
	    rs = stmt.executeQuery("SELECT id,materiallocation,title FROM materialtab WHERE id = (SELECT max (id) FROM materialtab)");
	    dh.disconnect();
	    while (rs.next()) {
		String s1 = rs.getString("materiallocation");
		String s2 = rs.getString("title");
		int id = Integer.parseInt(rs.getString("id"));
		tmpMaterial.add(new StringStringId(s1,s2,id));

	    }
	}catch(SQLException sqle){
	}

	return tmpMaterial;
    }
	   


    /**
     *Saves a materials title and location in the database.
     *@param matloc URL of the material. 
     *@param titel description of the material.
     */
    public void saveMaterial(String matloc, String title) {
	Statement stmt = null;
	
	try {
	    
	    stmt = dh.connect();
	    stmt.executeUpdate("INSERT INTO materialtab (materiallocation, title) values('" + matloc + "','" + title + "')");
	    dh.disconnect();
	}catch(SQLException sqle){
	}
    }

    /**
     *Deletes specific material in the database.
     *@param id identifies the material to be deleted.
     */
    public void deleteMaterial(String id) {
	Statement stmt = null;
	
	try {
	    
	    stmt = dh.connect();
	    stmt.executeUpdate("DELETE FROM materialtab WHERE id = '" + id + "'");
	    dh.disconnect();

	}catch(SQLException sqle){
	}
    }
}
