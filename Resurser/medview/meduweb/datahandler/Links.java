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
public class Links {

    /**
     *This attribute contains a reference to a new
     *Datahandler object.
     */
    private Datahandler dh = new Datahandler();

    /**
     *Gets all links, title and ids from the database.
     *@return ArrayList containing StingStringIds with links, titles and ids.
     */
    public ArrayList getAllLinks() {
	ArrayList linkList = new ArrayList();
	Statement stmt = null;
	ResultSet rs = null;
	
	try {
	    
	    stmt = dh.connect();
	    rs = stmt.executeQuery("SELECT id,link,title FROM linktab");
	    while (rs.next()) {
		int id =  Integer.parseInt(rs.getString("id"));
		String link = rs.getString("link");
		String title = rs.getString("title");
		linkList.add(new StringStringId(link, title, id));
	    }
	    dh.disconnect();

	}catch(SQLException sqle){
	}

	return linkList;
    }

    /**
     *Gets all new links from the database.
     *@param lastLogin containing information about the users last login.
     *@return ArrayList containing ids (String) of all new links since a users last login.
     */
    public ArrayList getNewLinks(Timestamp lastLogin) {
	ArrayList linkList = new ArrayList();
	Statement stmt = null;
	ResultSet rs = null;
	int i = 0;
	
	try {
	    
	    stmt = dh.connect();
	    rs = stmt.executeQuery("SELECT id FROM linktab WHERE senddate > '" + lastLogin + "'");
	    while (rs.next()) {
		linkList.add(rs.getString("id"));
	    }
	    dh.disconnect();

	}catch(SQLException sqle){
	}

	return linkList;
    }
    
    /**
     *Saves a link in the database.
     *@param link the URL adress.
     *@param title describes the link.
     */
    public void saveLink(String link, String title) {
	Statement stmt = null;
	
	try {

	    stmt = dh.connect();
	    stmt.executeUpdate("INSERT INTO linktab (link,title) values('" + link + "','" + title + "')");
	    dh.disconnect();
	}catch(SQLException sqle){
	}
    }

    /**
     *Deletes a specified link from the database.
     *@param id specifies the link to delete.
     */
    public void deleteLink(String id) {
	Statement stmt = null;
	
	try {

	    stmt = dh.connect();
	    stmt.executeUpdate("DELETE FROM linktab WHERE id = '" + id + "'");
	    dh.disconnect();
	}catch(SQLException sqle){
	}
    }

}
