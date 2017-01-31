package medview.meduweb.datahandler;

import java.sql.*;              //JDBC stuff
import java.util.*;             //ArrayList...
//import org.postgresql.Driver;   //PostGreSQL Driver for JDBC
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
public class Messages {

    /**
     *This attribute contains a reference to a new
     *Datahandler object.
     */
    private Datahandler dh = new Datahandler();

    /**
     *Gets all messages and ids from the database.
     *@return ArrayList containing StringStringIds with date, message and id.
     */
    public ArrayList getAllMess() {
	ArrayList messages = new ArrayList();
	Statement stmt = null;
	ResultSet rs = null;
	
	try {
	    
	    stmt = dh.connect();
	    rs = stmt.executeQuery("SELECT senddate, id,message FROM messagetab ORDER BY senddate DESC");
	    while (rs.next()) {
		int id =  Integer.parseInt(rs.getString("id"));
		String date = rs.getString("senddate");
		String msg = rs.getString("message");
		messages.add(new StringStringId(date,msg, id));
	    }
	    dh.disconnect();

	}catch(SQLException sqle){
	}

	return messages;
    }

    /**
     *Gets the latest message from the database.
     *@return String containing the last saved message.
     */
    public String getLastMess() {
	String lastMessage = "";
	Statement stmt = null;
	ResultSet rs = null;
	
	try {
	    
	    stmt = dh.connect();
	    rs = stmt.executeQuery("SELECT message FROM messagetab WHERE id = (SELECT max (id) FROM messagetab)");
	    while (rs.next()) {
		lastMessage = rs.getString("message");
	    }
	    dh.disconnect();

	}catch(SQLException sqle){
	}

	return lastMessage;
    }
    
    /**
     *Saves a new message.
     *@param message contains the message text.
     */
    public void saveMess(String message) {
	
	Statement stmt = null;
	
	try {
	    
	    stmt = dh.connect();
	    stmt.executeUpdate("INSERT INTO messagetab (message) VALUES('" + message + "')");
	    dh.disconnect();
	}catch(SQLException sqle){
	}
    }
	    
    /**
     *Deletes all messages.
     */
    public void deleteMessages() {

	Statement stmt = null;

	try {

	    stmt = dh.connect();
	    stmt.executeUpdate("DELETE FROM messagetab");
	    dh.disconnect();

	}catch(SQLException sqle){
	}
    }



    /**
     *Deletes a specific message.
     *@param id containing the message-id to be deleted.
     */
    public void deleteMess(int id) {

	Statement stmt = null;

	try {

	    stmt = dh.connect();
	    stmt.executeUpdate("DELETE FROM messagetab WHERE id = '" + id + "'");
	    dh.disconnect();

	}catch(SQLException sqle){
	}
    }

}
