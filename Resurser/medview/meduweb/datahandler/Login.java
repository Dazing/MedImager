package medview.meduweb.datahandler;

import java.sql.*;              //JDBC stuff

import javax.servlet.http.*;        //Session stuff  
//import org.apache.catalina.*;      //Session stuff  
import java.util.*;             //ArrayList stuff
import medview.meduweb.datahandler.*;
/**
 *This class is a part of the datahandler for the mEduWeb 
 *system. It contains methods used for administration purposes.
 *It extends the class Datahandler which contains methods used
 *to manage the database connection(s). 
 *@author Jenny
 *@version 1.00001
 */
public class Login {

    /**
     *This attribute contains a reference to a new
     *Datahandler object.
     */
    private Datahandler dh = new Datahandler();
    
    /**
     *Checks if given username and password are correct and returns a boolean.
     *Login updates userstatisticstab and systemstatisticstab.
     *Login also updates the session with the values:
     *logged boolean,
     *username String,
     *language String,
     *previouslogin String,
     *profile String,
     *adminrights boolean,
     *exercisesrights boolean,
     *questionsrights boolean,
     *messagesrights boolean.
     *@param username containing the username from the usernamefield in the loginpage.
     *@param password containing the password from the passwordfield in the loginpage.
     *@param session a HttpSession. 
     *@return a boolean logged weather username and password was correct.
     */
    public boolean login(String username, String password, HttpSession session) {
	Statement stmt = null;	
	boolean logged = false;
        ResultSet rs = null;
	String previousLogin = "";
	String language = "";
	String profile = "";
	boolean adminrights = false;
	boolean exercisesrights = false;
	boolean questionsrights = false;
	boolean messagesrights = false;

	try {

	    stmt = dh.connect();
	    rs = stmt.executeQuery("SELECT userid FROM usertab WHERE userid = '" + username + "' AND password = '" + password + "'");
	    if (rs.next()) {
		logged = true;
		rs = stmt.executeQuery("SELECT lastlogin FROM userstatisticstab WHERE userid = '" + username + "'");
		
		if (rs.next()) {
		    rs.next();
		    previousLogin = rs.getString("lastlogin");
		    stmt.executeUpdate("UPDATE userstatisticstab SET lastlogin = current_timestamp, numlogins = numlogins + '1' WHERE userid='" + username + "'");
		    stmt.executeUpdate("UPDATE userstatisticstab SET totalloggedtime = (totalloggedtime + loggedtime) WHERE userid='" + username + "'");
		    stmt.executeUpdate("UPDATE systemstatisticstab SET numlogins = numlogins + '1'");
		    stmt.executeUpdate("UPDATE systemstatisticstab SET loggedtime = loggedtime + (SELECT loggedtime FROM userstatisticstab WHERE userid = '" + username + "')");
		} else { //If first-time user
		    previousLogin = "1979-01-01 00:00:00.0000000-00";
		    stmt.executeUpdate("INSERT INTO userstatisticstab VALUES ('" + 
				       username + "', CURRENT_TIMESTAMP, '0 00:00:00', '0 00:00:00', '1', '0', '0', '0')"); 
		    stmt.executeUpdate("UPDATE systemstatisticstab SET numlogins = numlogins + '1'");
		    stmt.executeUpdate("UPDATE systemstatisticstab SET loggedtime = loggedtime + (SELECT loggedtime FROM userstatisticstab WHERE userid = '" + username + "')");
		};

		rs = stmt.executeQuery("SELECT language FROM usertab WHERE userid = '" + username +"'");
		rs.next();
		language = rs.getString("language");
		rs = stmt.executeQuery("SELECT profilename FROM grouptab WHERE groupname = (SELECT groupname FROM usertab WHERE userid = '" + username +"')");
		rs.next();
		profile = rs.getString("profilename");
		rs = stmt.executeQuery("SELECT admin, exercises, questions, messages FROM profiletab WHERE profilename = '" + profile + "'");
		if (rs != null) {
		    rs.next();
		    adminrights = rs.getBoolean("admin");
		    exercisesrights = rs.getBoolean("exercises");
		    questionsrights = rs.getBoolean("questions");
		    messagesrights = rs.getBoolean("messages");
		} else {
		    messagesrights = false;
		}
		session.setAttribute("logged", new Boolean(logged));
		session.setAttribute("username", username);
		session.setAttribute("previouslogin", previousLogin);
		session.setAttribute("language", language);
		session.setAttribute("profile", profile);
		session.setAttribute("adminrights", new Boolean(adminrights));
		session.setAttribute("exercisesrights", new Boolean(exercisesrights));
		session.setAttribute("questionsrights", new Boolean(questionsrights));
		session.setAttribute("messagesrights", new Boolean(messagesrights));
	    }
	    dh.disconnect();
	    
	}catch(SQLException sqle) {
	}
	return logged;
    }
    /**
     *Updates userstatisticstab "loggedtime".
     *@param username containing user to logout.
     */
    public void updateLoggedTime(String username) {
	Statement stmt = null;
	
	try {

	    stmt = dh.connect();
	    stmt.executeUpdate("UPDATE userstatisticstab SET loggedtime = (current_timestamp - lastlogin) WHERE userid = '" + username + "'");
	    dh.disconnect();
	}catch(SQLException sqle){
	}
    }
}
	
	

