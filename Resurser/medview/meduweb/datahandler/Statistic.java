package medview.meduweb.datahandler;

import java.sql.*;                 //JDBC stuff  
//import org.apache.catalina.*;      //Database stuff 
import java.util.*;                //ArrayList stuff
import medview.meduweb.datahandler.*;
/**
 *This class is a part of ths datahandler for the mEduWeb 
 *system. It contains methods used for statistic purposes.
 *It extends the class Datahandler which contains methods used
 *to manage the database connection(s). 
 *@author Jenny
 *@version 1.00001
 */
public class Statistic {

    /**
     *This attribute contains a reference to a new
     *Datahandler object.
     */
    private Datahandler dh = new Datahandler();
     

    /**
     *getGroups gets all groups from grouptab in the database.
     *@return ArrayList of string with all groupnames in grouptab.
     */
    public ArrayList getGroups() {
	Statement stmt = null;
	ArrayList tmpGroups = new ArrayList();
	int i = 0;
	ResultSet rs = null;
	
	try {
	    
	    stmt = dh.connect();
	    rs = stmt.executeQuery("SELECT groupname FROM grouptab");
	    
	    while (rs.next()) {
		tmpGroups.add(rs.getString("groupname"));
	    }
	    dh.disconnect();
	}catch(SQLException sqle) {
	}

	return tmpGroups;
    }
    
    /**
     *gets all statistics for a specified group.
     *@param groupname containing a groupname to get statistics for.
     *@return StatClass groupstatistics with all information about the group.
     */
    public StatClass getGroupStat(String groupname) {
	Statement stmt = null;
	StatClass groupstatistics = new StatClass();
	ResultSet rs = null;
	//String[] usernames = null;
	ArrayList usernames = new ArrayList();
	int i = 0;
	String grouptotalloggedtime = "";
	int groupnumlogins = 0;
	int groupnumsearches = 0;
	int groupnumadminexercises = 0;
	int groupnumownexercises = 0;
	
	try {

	    
	    stmt = dh.connect();
	    /*rs = stmt.executeQuery("SELECT userid FROM usertab WHERE groupname = '" + groupname + "'");
   
	    while (rs.next()) {
		usernames.add(rs.getString("userid"));
	    }
	    */
	    rs = stmt.executeQuery("SELECT sum(numlogins) as loginsum, sum(numsearches) as searchsum, " +
				   "sum(numadminexercises) as adminexsum, sum(numownexercises) as ownexsum, " +
				   "sum(totalloggedtime) as loggedtimesum FROM userstatisticstab WHERE userid in " +
				   "(SELECT userid FROM usertab where groupname  = '" + groupname + "')" );
	    if (rs.next()) {
		grouptotalloggedtime = rs.getString("loggedtimesum");
		groupnumlogins = rs.getInt("loginsum");
		groupnumsearches = rs.getInt("searchsum");
		groupnumadminexercises = rs.getInt("adminexsum");
		groupnumownexercises = rs.getInt("ownexsum");
	    }
	    
	    groupstatistics.loggedtime = grouptotalloggedtime;
	    groupstatistics.numlogins = groupnumlogins;
	    groupstatistics.numsearches = groupnumsearches;
	    groupstatistics.numadminexercises = groupnumadminexercises;
	    groupstatistics.numownexercises = groupnumownexercises;
	    
	    dh.disconnect();
	}catch(SQLException sqle){
	}
	return groupstatistics;
    }

    /**
     *gets all statistics for the system.
     *@return StatClass systemstatistics containing all statistics for the system.
     */
    public StatClass getSystemStat() {
	Statement stmt = null;
	StatClass systemstatistics = new StatClass();
	ResultSet rs = null;
	
	try {

	    stmt = dh.connect();
	    rs = stmt.executeQuery("SELECT * FROM systemstatisticstab");
	    rs.next();
	    systemstatistics.loggedtime = rs.getString("loggedtime");
	    systemstatistics.numlogins = rs.getInt("numlogins");
	    systemstatistics.numsearches = rs.getInt("numsearches");
	    systemstatistics.numadminexercises = rs.getInt("numadminexercises");
	    systemstatistics.numownexercises = rs.getInt("numownexercises");
	 
	    dh.disconnect();

	}catch(SQLException sqle){
	}
	return systemstatistics;
    }

    public void updateAdminExercises(String username) throws SQLException {
	Statement stmt = null;
	stmt = dh.connect();
	stmt.executeUpdate("UPDATE userstatisticstab SET numadminexercises = " +
			   "numadminexercises + '1' WHERE userid='" + username + "'");
	stmt.executeUpdate("UPDATE systemstatisticstab SET numadminexercises = " +
			   "numadminexercises + '1'");
	dh.disconnect();
    }

    public void updateOwnExercises(String username) throws SQLException {
	Statement stmt = null;
	stmt = dh.connect();
	stmt.executeUpdate("UPDATE userstatisticstab SET numownexercises = " +
			   "numownexercises + '1' WHERE userid='" + username + "'");
	stmt.executeUpdate("UPDATE systemstatisticstab SET numownexercises = " +
			   "numownexercises + '1'");
	dh.disconnect();
    }

    /**
     *Used to inkrement the number of searches with 1.
     *@param username The name of the user doing the search.
     */
    public void updateSearches(String username) throws SQLException {
	Statement stmt = null;
	stmt = dh.connect();
	stmt.executeUpdate("UPDATE userstatisticstab SET numsearches = numsearches + '1'" +
			   " WHERE userid='" + username + "'");
	stmt.executeUpdate("UPDATE systemstatisticstab SET numsearches = numsearches + '1'");
	dh.disconnect();
    }

    /**
     *gets the average statistics for all the users excluding admins in the system.
     *@return StatClass useravgstatistics containing all average statistics.
     */
    public StatClass getUserAvgStat() {
	Statement stmt = null;
	StatClass useravgstatistics = new StatClass();
	ResultSet rs = null;
	int i = 0;
	
	try {
	    stmt = dh.connect();
	    String query = "SELECT AVG(totalloggedtime) as loggedavg, AVG(numlogins) as numloginsavg, AVG(numsearches) as numsearchesavg, AVG(numadminexercises) as numadminexercisesavg, AVG(numownexercises) as numownexercisesavg FROM userstatisticstab WHERE userid in (SELECT userid from usertab WHERE groupname in (SELECT groupname FROM grouptab WHERE profilename != 'admin'))";
	    rs = stmt.executeQuery(query);
	    rs.next();
	    String loggedtime = rs.getString("loggedavg");
	    String numlogins = rs.getString("numloginsavg");
	    String numsearch =  rs.getString("numsearchesavg");
	    String numadmin = rs.getString("numadminexercisesavg");
	    String numown = rs.getString("numownexercisesavg");

	    useravgstatistics.loggedtime = loggedtime; 
	    useravgstatistics.numlogins = Integer.parseInt(numlogins.substring(0, numlogins.lastIndexOf('.')));
	    useravgstatistics.numsearches = Integer.parseInt(numsearch.substring(0, numsearch.lastIndexOf('.')));
	    useravgstatistics.numadminexercises = Integer.parseInt(numadmin.substring(0, numadmin.lastIndexOf('.')));
	    useravgstatistics.numownexercises = Integer.parseInt(numown.substring(0, numown.lastIndexOf('.')));
	    dh.disconnect();
	    
	}catch(SQLException sqle){}
	
	return useravgstatistics;
    }
}

