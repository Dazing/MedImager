package medview.meduweb.datahandler;

import java.sql.*;              //JDBC stuff
import org.postgresql.Driver;   //PostGreSQL Driver for JDBC
import medview.meduweb.datahandler.Datahandler;
import java.util.*;
import medview.meduweb.datahandler.StringStringId;

/**
 *This class is a part of ths datahandler for the mEduWeb 
 *system. It contains methods used for administration purposes.
 *It extends the class Datahandler which contains methods used
 *to manage the database connection(s). 
 *@author Figge
 *@version 1.2
 */
public class Administration {

    public class LanguageObj {
	public String code;
	public String fullname;
	LanguageObj(String code, String fullname) {
	    this.code = code;
	    this.fullname = fullname;
	}

	public String getCode() {
	    return code;
	}
	public String getFullname() {
	    return fullname;
	}
    }

    public class Visualization {
	public int id;
	public String title;
	Visualization(int id, String title) {
	    this.id = id;
	    this.title = title;
	}
	public int getId() {
	    return id;
	}
	public String getTitle() {
	    return title;
	}
    }

    public class Directory {
	public int dirid = 0;
	public String dir = "";

	public Directory(int id, String d) {
		dirid = id;
		dir = d;
	}
    }

    public class Search {
    	public int id = 0;
    	public String description = "";
    	public String searchstring = "";
    	
    	public Search(int id, String descr, String search) {
    		this.id = id;
    		description = descr;
    		searchstring = search;	
    	}
    	
    	public Search() {}	
    }

    /**
     *This attribute contains a reference to a new
     *Datahandler object.
     */
    private Datahandler dh = new Datahandler();
	public void test() throws SQLException {
		Statement stmt = null;
		stmt = dh.connect();
		dh.disconnect();
	}

	public void addSearch(String username, String description, String search) throws SQLException {
		Statement stmt = dh.connect();
		stmt.executeUpdate("INSERT INTO savedsearchestab (userid, description, searchstring) VALUES('" + 
				username + "', '" + description + "', '" + search + "')");
		dh.disconnect();
	}

	public void deleteSavedSearch(int id) throws SQLException {
		Statement stmt = dh.connect();
		stmt.executeUpdate("DELETE FROM savedsearchestab WHERE id = " + id);
		dh.disconnect();	
	}

	public Search getSearch(int id) throws SQLException {
		Statement stmt = dh.connect();
		ResultSet rs = 	stmt.executeQuery("SELECT * FROM savedsearchestab WHERE id = " + id);
		dh.disconnect();
		Search out = new Search();
		if(rs.next()) {
			out.id = rs.getInt("id");
			out.description = rs.getString("description");
			out.searchstring = rs.getString("searchstring");
			
		}
		return out;
	}
	
	public ArrayList getSearches(String username) throws SQLException {
		Statement stmt = dh.connect();
		ResultSet rs = 	stmt.executeQuery("SELECT * FROM savedsearchestab WHERE userid = '" + username + "'");
		dh.disconnect();
		ArrayList out = new ArrayList();
		Search tmp = null;
		while(rs.next()) {
			tmp = new Search();
			tmp.id = rs.getInt("id");
			tmp.description = rs.getString("description");
			tmp.searchstring = rs.getString("searchstring");
			out.add(tmp);
		}
		return out;
	}

	public int getMaxIdDirs() throws SQLException {
		Statement stmt = null;
		int ret = 0;
		stmt = dh.connect();
		ResultSet rs = stmt.executeQuery("SELECT max(dirid) FROM dirstab");
		if(rs.next())
			ret = rs.getInt(1);
		dh.disconnect();
		return ret;

	}

	public String getDirectory(String name) throws SQLException {
		Statement stmt = null;
		String ret = "";
		stmt = dh.connect();
		ResultSet rs = stmt.executeQuery("SELECT * FROM dirstab WHERE name ='" + name + "'");
		if(rs.next()) {
			ret = rs.getString("dir");
		}	
		dh.disconnect();
		return ret;
	}

	public String getCurrentDirectory(String name) throws SQLException {
		Statement stmt = null;
		String ret = "";
		stmt = dh.connect();
		ResultSet rs = stmt.executeQuery("SELECT * FROM dirstab WHERE name ='" + name + 
						"' AND inuse = true");
		if(rs.next()) {
			ret = rs.getString("dir");
		}	
		dh.disconnect();
		return ret;
	}	

	public ArrayList getDirectories(String name) throws SQLException {
		Statement stmt = null;
		ArrayList dirs = new ArrayList();
		stmt = dh.connect();
		ResultSet rs = stmt.executeQuery("SELECT * FROM dirstab WHERE name ='" + name + 
						"'");
		while(rs.next()) {
			dirs.add(new Directory(rs.getInt("dirid"),
					rs.getString("dir")));
		}
		dh.disconnect();
		return dirs;
	}

	public void addDirectory(String thename, String directory, boolean inUse) 
			throws SQLException {
		Statement stmt = null;
		int newId = getMaxIdDirs() + 1;
		
		

		String updStr = "INSERT INTO dirstab (dirid,name,dir,inuse) VALUES(" + 
				String.valueOf(newId) + 
				",'dbdir','" +
				directory + "'," + (new Boolean(inUse)).toString() + ")";
		
		stmt = dh.connect();		
		stmt.executeUpdate(updStr);
		dh.disconnect();

	}

	public void useDirectory(int id, String name) throws SQLException {
		Statement stmt = null;
		stmt = dh.connect();	
		stmt.executeUpdate("UPDATE dirstab SET inuse=false WHERE name='" +
					name + "'");
		stmt.executeUpdate("UPDATE dirstab SET inuse=true WHERE dirid=" +
					String.valueOf(id));
		dh.disconnect();

	}

    public void addGroup(String name, String profile) throws SQLException{
	Statement stmt = null;
	stmt = dh.connect();
	stmt.executeUpdate("INSERT INTO grouptab VALUES('" + name +
			   "','" + profile +"')");
	dh.disconnect();

    }

    /**
     * Adds a new profile to the database.
     * @param name the new profiles' name.
     * @param admin true if admin rights
     * @param ex allows doing exercises.
     * @param quest allows sending questions.
     * @param mess allows receiving of messages.
     * @throws SQLException has to be caught elsewhere.
     */
    public void addProfile(String name, boolean admin,
			   boolean ex, boolean quest,
			   boolean mess) 
			   throws SQLException {
	Statement stmt = null;
	
	stmt = dh.connect();
	stmt.executeUpdate("INSERT INTO profiletab VALUES('" +
			   name + "'," + 
			   (new Boolean(admin)).toString() + "," +
			   (new Boolean(ex)).toString() + "," +
			   (new Boolean(quest)).toString() + "," +
			   (new Boolean(mess)).toString() +")");
	dh.disconnect();
    }

    /**
     * Adds a user to the database.
     * @param name a user name.
     * @param groupname a group for the new user.
     * @param passw a password for the user.
     * @param lang language to be used for the user.
     * @throws SQLException has to be caught elsewhere.
     */
    public void addUser(String name, String groupname,
			String passw, String lang) 
	                throws SQLException {
	Statement stmt = null;
	
	stmt = dh.connect();
        stmt.executeUpdate("INSERT INTO usertab VALUES('" +
			   name + "','" + passw + "','" +
			   groupname + "','" + lang + "')");
        dh.disconnect();
    }

    /**
     *Deletes a groupname and rights from the grouptab
     *@param groupname The name of the group to delete
     *@throws SQLException has to be caught elsewhere.
     */
    public void deleteGroup(String groupname) throws SQLException {
    	Statement stmt = null;
	
	stmt = dh.connect();
	stmt.executeUpdate("DELETE FROM grouptab WHERE " +
			   "groupname = '" + groupname + "'" );
	
        dh.disconnect();
    }

    /**
     * Deletes a specified profile.
     * @param name the name of the profile to delete.
     */
    public void deleteProfile(String name) throws SQLException {
    	Statement stmt = null;
	
	stmt = dh.connect();
	stmt.executeUpdate("DELETE FROM profiletab WHERE " +
			   "profilename = '" + name + "'" );
	dh.disconnect();
	
    }

    /**
     * Deletes a specified user.
     * @param name the users' username
     */
    public void deleteUser(String name) {
    	Statement stmt = null;
   	try{
	    stmt = dh.connect();
	    stmt.executeUpdate("DELETE FROM usertab WHERE " +
			       "userid = '" + name + "'" );
	    dh.disconnect();
	}catch(SQLException sqle){
	}
    }

    /**
     *Gets the profile for a specified group.
     *@param groupname The name of the group for which to get the profile.
     *@return A String containing the profilename.
     *@throws SQLException
     */
    public String getGroupProfile(String groupname)  throws SQLException{
	ArrayList groups = new ArrayList();
	Statement stmt = null;
	stmt = dh.connect();
	ResultSet rs = null;
	String profile = "";
	rs = stmt.executeQuery("SELECT profilename FROM grouptab" +
			       " WHERE groupname = '" + groupname + "'");
	dh.disconnect();
	if(rs.next()) {
	    profile = rs.getString("profilename");
	}
	return profile;
    }

    /**
     *Gets a list of all present groups.
     *@return An arraylist containing all groupnames.
     *@throws SQLException
     */
    public ArrayList getGroups() throws SQLException{
	ArrayList groups = new ArrayList();
	Statement stmt = null;
	stmt = dh.connect();
	ResultSet rs = null;

	rs = stmt.executeQuery("SELECT groupname FROM grouptab");
	dh.disconnect();
	while(rs.next()) {
	    groups.add(rs.getString("groupname"));
	}
	return groups;
    }


    /**
     *Lists all languages present in the languagetab.
     *@return An ArrayList with LanguageObj.
     */
    public ArrayList getLanguages() throws SQLException{
	ArrayList langs = new ArrayList();
	Statement stmt = null;
	stmt = dh.connect();
	ResultSet rs = null;
	rs = stmt.executeQuery("SELECT * FROM languagetab");
	dh.disconnect();
	while(rs.next()) {
	    
	    langs.add(new LanguageObj(rs.getString("language"), rs.getString("fullname")));
	    
	}
	return langs;
    }

    /**
     * Gets the profile for a user.
     * @param name a users name.
     * @return String the users' profile.
     */
    public String getProfile(String name) throws SQLException {
	String profile = "";
	String group = "";
	Statement stmt = null;

	ResultSet rs = null;
	ResultSet rs2 = null;
	
	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT groupname FROM usertab" +
			       " WHERE userid = '" +
			       name + "'" );
	if(rs.next()) {
	    group = rs.getString("groupname");
	}
	rs2 = stmt.executeQuery("SELECT profilename FROM grouptab" +
				" WHERE groupname = '" +
				group + "'" );
	
	if(rs2.next())
	    profile = rs2.getString("profilename");
	dh.disconnect();
	
	return profile;
    }

    /**
     * Gets a list of all profiles present.
     * @return ArrayList containing Strings, the names of all profiles.
     * @throws SQLExcpetion if a database error uccured.
     */
    public ArrayList getProfiles() throws SQLException {
	ArrayList tmpProfiles = new ArrayList();
	int i = 0;
    	Statement stmt = null;
	ResultSet rs = null;
	
	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT profilename FROM profiletab");
	
	while( rs.next() ) {
	    tmpProfiles.add(rs.getString("profilename"));
	}
	dh.disconnect();

    	return tmpProfiles;
    }


    /**
     *Gets the users for a specified group.
     *@param group The group that users shall be listed for.
     *@return The list of users as an ArrayList.
     *@throws SQLException
     */
    public ArrayList getGroupmembers(String group) throws SQLException {
	ArrayList tmpUsers = new ArrayList();
	int i = 0;
    	Statement stmt = null;
	ResultSet rs = null;
	
	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT userid FROM usertab WHERE " +
			       "groupname = '" + group + "'");
	while( rs.next() ) {
	    tmpUsers.add(rs.getString("userid"));
	}
	
	dh.disconnect();
	
	return tmpUsers;
    }

    /**
     *Tells whether a specific type of permission is set for
     *a specific profile.
     *@param profilename The name of the profile.
     *@param rightname The type of right to return.
     *@return A boolean telling if the right is set or not.
     *@throws SQLException
     */
    public boolean getRights(String profilename, String rightname) throws SQLException {
    	Statement stmt = null;
	ResultSet rs = null;
	boolean ans = false;
	
	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT " + rightname + " FROM profiletab " +
			       "WHERE profilename = '" + profilename + "'");
	if(rs.next()) {
	    ans = rs.getBoolean(rightname);
	    
	}
	dh.disconnect();
	return ans;
    }

    public String getUserGroup(String username) throws SQLException {
    	Statement stmt = null;
	ResultSet rs = null;
	String usergroup = "";
	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT groupname FROM usertab" +
			       " WHERE userid='" + username + "'");
	if(rs.next()) {
	    usergroup = rs.getString("groupname");
	}
	dh.disconnect();
	return usergroup;
    }

    /**
     * Gets a list of all users.
     * @return ArrayList containing Strings, the names of all users.
     * @throws SQLException if a a database errro uccured.
     */
    public ArrayList getUsers() throws SQLException {
	ArrayList tmpUsers = new ArrayList();
	int i = 0;
    	Statement stmt = null;
	ResultSet rs = null;
	
	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT userid FROM usertab");
	while( rs.next() ) {
	    tmpUsers.add(rs.getString("userid"));
	}
	
	dh.disconnect();
	
	return tmpUsers;
    }

    /**
     *Tells whether a groupname is in the database or not.
     *@param group The name to check.
     *@returns True if the name is in the database, false o.w.
     */
    public boolean isExistingGroup(String group) throws SQLException {
	Statement stmt = null;
	ResultSet rs = null;
	boolean isEG = false;
	
	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT * FROM grouptab " +
			       "WHERE groupname='" + group + "'");
	dh.disconnect();
	if( rs.next() ) 
	    isEG = true;
        
	return isEG;
    }

    public boolean isExistingProfile(String name) throws SQLException {
	Statement stmt = null;
	ResultSet rs = null;
	boolean isEP = false;
	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT * FROM profiletab " +
			       "WHERE profilename='" + name + "'");
	dh.disconnect();
	if( rs.next() ) 
	    isEP = true;
        
	return isEP;

    }

    /**
     * Updates the properties for a profile.
     * @param name the profile to be changed.
     * @param admin Sets admin rights.
     * @param ex Sets the right to do exercises.
     * @param quest Sets the rights to send questions.
     * @param mess Sets the rights to read messages.
     * @throws SQLException if a database eror uccured.
     */
    public void updateProfile(String name, boolean admin,
			      boolean ex, boolean quest,
			      boolean mess) throws SQLException {
    	Statement stmt = null;
	stmt = dh.connect();
	stmt.executeUpdate("UPDATE profiletab SET admin=" +
			   (new Boolean(admin)).toString() + 
			   ", exercises=" + (new Boolean(ex)).toString() +
			   ", questions=" + (new Boolean(quest)).toString() + 
			   ", messages=" + (new Boolean(mess)).toString() + 
			   " WHERE profilename='" + name + "'" );
	dh.disconnect();
	
    }

    /**
     * Updates the properties for a user.
     * @param name the users' username.
     * @param profile the new profile to be used.
     * @param passw the new password.
     * @param lang the new language.
     * @throws SQLException if a database error uccured.
     */
    public void updateUser(String name, String profile,
			   String passw, String lang) throws SQLException {
    	Statement stmt = null;

	stmt = dh.connect();
	stmt.executeUpdate("UPDATE usertab SET language='" +
			   lang + "', profilename='" + profile +
			   "', password='" + passw + 
			   "' WHERE userid='" + name + "'");
	dh.disconnect();

    }

    /**
     *Saves a visualization.
     */
    public void saveVisualization(String username, String url,
				  String title, String linkMap) throws SQLException {
	Statement stmt = null;

	stmt = dh.connect();
	stmt.executeUpdate("INSERT INTO savedvisualisationstab " +
			   "(userid, title, url, map) " +
			   " VALUES('" + username + "','" + title +
			   "','" + url + "','" + linkMap + "')" );
	dh.disconnect();
    }

    /**
     * Returns the path to a saved visualization.
     * @param id the id of the visualization.
     * @return String the path.
     */
    public String getVisualization(String id) throws SQLException {
	Statement stmt = null;
	ResultSet rs = null;
	stmt = dh.connect();
	String url = "";
	rs = stmt.executeQuery("SELECT url FROM savedvisualisationstab " +
			       "WHERE id='" + id + "'");
	if(rs.next()) {
	    url = rs.getString("url");
	}
	return url;
    }
  
    /**
     *Gets the linkmap for a saved visualization.
     *@param id The id in the database fpr the specific visualization.
     *@return The linkmap to use for this visualization.
     *@throws SQLException
     */
    public String getLinkMap(String id) throws SQLException {
	Statement stmt = null;
	ResultSet rs = null;
	stmt = dh.connect();
	String map = "";
	rs = stmt.executeQuery("SELECT map FROM savedvisualisationstab " +
			       "WHERE id='" + id + "'");
	dh.disconnect();
	if(rs.next()) {
	    map = rs.getString("map");
	}
	return map;
    }                             

    /**
     * Changes the preferred language for a user.
     * @param name The username for the user to change.
     * @param lang The language that shall be set.
     * @throws SQLException if a database error uccured.
     */
    public void setUserLang(String name, String lang) throws SQLException {
    	Statement stmt = null;
	
	stmt = dh.connect();
	stmt.executeUpdate("UPDATE usertab SET language='" +
			   lang + "' WHERE userid='" + name + "'");
	dh.disconnect();
    }


    /**
     * List all visualizations that belong to a certain user
     * @param username The username for the user
     * @return list of Visualization ibjects
     * @throws SQLException
     */
    public ArrayList listVisualizations(String username) throws SQLException {
	Statement stmt = null;
	ResultSet rs = null;
	stmt = dh.connect();
	ArrayList temp = new ArrayList();
	rs = stmt.executeQuery("SELECT id, title FROM savedvisualisationstab" +
			       " WHERE userid='" + username + "'");
	while(rs.next()) {
	    temp.add(new Visualization(rs.getInt("id"),rs.getString("title")));
	}
	dh.disconnect();
	return temp;
    }

    /**
     *Deletes a saved visualization.
     *@param id of the saved visualization to delete.
     *@param user the user to remove the vis for.
     *@throws SQLException has to be caught elsewhere.
     */
    public void deleteVisualization(String id, String user) throws SQLException {
	String visURL = getVisualization(id);
	String vis = "";
	StringTokenizer st = new StringTokenizer(visURL, "/");
	while (st.hasMoreTokens()) {
	    vis = st.nextToken();
	}

	Statement stmt = null;
	stmt = dh.connect();
       	stmt.executeUpdate("DELETE FROM savedvisualisationstab WHERE id='" + id + "'");
	dh.disconnect();

	//Delete all questions on this visualization
	Questionhandler questionH = new Questionhandler();
	//only remove questions for this specific user
	ArrayList questions = questionH.getQuestionByUser(user, "visual");
	Iterator questI = questions.iterator();
	while (questI.hasNext()) {
	    Question q = (Question)questI.next();
	    //if the question was for this vis, remove
	    if (q.getQuestId().equals(vis)) {
		questionH.deleteQuestion(String.valueOf(q.getId()));
	    }
	}
    }

    /**
     * Changes the password for a user.
     * @param name The username for the user to change.
     * @param passw The new password.
     * @throws SQLException if a database error uccured.
     */
    public void setUserPassword(String name, String passw) throws SQLException {
    	Statement stmt = null;
	stmt = dh.connect();
	stmt.executeUpdate("UPDATE usertab SET password='" +
			   passw + "' WHERE userid='" + name + "'");
	dh.disconnect();
    }

    public String getPassword(String name) throws SQLException {
    	Statement stmt = null;
	stmt = dh.connect();
	ResultSet rs = null;
	String password = "";
	rs = stmt.executeQuery("SELECT password FROM usertab WHERE" +
			       " userid='" + name + "'");

	dh.disconnect();
	if(rs.next()) {
	    password = rs.getString("password");
	}
	return password;

    }

    /**
     * Changes the group for a user.
     * @param name The name of the user.
     * @param group The new group for the user.
     * @throws SQLException if a database error uccured.
     */
    public void setUserGroup(String name, String group) throws SQLException {
    	Statement stmt = null;
	stmt = dh.connect();
	stmt.executeUpdate("UPDATE usertab SET groupname='" +
			   group + "' WHERE userid='" + name + "'");
	dh.disconnect();
    }

    public void setUserProfile(String userid, String profilename) throws SQLException {
    	Statement stmt = null;
	ResultSet rs = null;

	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT groupname FROM grouptab WHERE groupname='" + userid + "'");
	if (rs != null) {
	    stmt.executeUpdate("UPDATE grouptab SET profilename='" + profilename + "' WHERE groupname='" + userid + "'");
	} else {
	    stmt.executeUpdate("INSERT INTO grouptab VALUES('" + userid +
			   "','" + profilename + "')");
	    setUserGroup(userid,userid);
	}
	dh.disconnect();
    }

    public void setGroupProfile(String groupname, String profile) throws SQLException {
    	Statement stmt = null;
	stmt = dh.connect();
	stmt.executeUpdate("UPDATE grouptab SET profilename='" + 
			   profile + "' WHERE groupname='" + groupname + "'"); 

	dh.disconnect();
    }

    /**
     * This method adds an emailaddress to the adminemailtab, in order for
     * question-notifications to be sent to this address.
     * @param email the emailaddress to be used
     * @param userid the user who is assosciated with the address.
     */
    public void saveEmailAddress(String userid, String email) throws SQLException 
    {
	Statement stmt = null;
	stmt = dh.connect();
	int i = stmt.executeUpdate("INSERT INTO adminemailtab (userid, email) VALUES ('"
			   + userid + "', '" + email + "')");
	dh.disconnect();
    }

    /**
     * This method removes an emailaddress from the adminemailtab.
     * @param id the id of the adress to remove
     */
    public void removeEmailAddress(int id) throws SQLException 
    {
	Statement stmt = null;
	stmt = dh.connect();
	int i =stmt.executeUpdate("DELETE FROM adminemailtab WHERE id ='" + id +"'");
	dh.disconnect();
    }

    /**
     * This method returns a list of all emailaddresses in the adminemailtab
     *@return an ArrayList with StringStringId objects - userid, address and id.
     */
    public ArrayList getEmailAddresses() throws SQLException {
	Statement stmt = null;
	ResultSet rs = null;
	ArrayList emailList = new ArrayList();
	
	try {
	    stmt = dh.connect();
	    rs = stmt.executeQuery("SELECT id, userid, email FROM adminemailtab");
	    while (rs.next()) {
		int id = Integer.parseInt(rs.getString("id"));
		String userid = rs.getString("userid");
		String email = rs.getString("email");
		emailList.add(new StringStringId(userid, email, id));
	    }
	    
	    dh.disconnect();
	    
	}catch(SQLException sqle){
	}

	return emailList;
    }

     /**
     * This method returns a list of all emailaddresses assosciated with a user.
     *@return an ArrayList with StringStringId objects - userid, address and id.
     */
    public ArrayList getUserEmails(String userid) throws SQLException {
	Statement stmt = null;
	ResultSet rs = null;
	ArrayList emailList = new ArrayList();
	
	try {
	    stmt = dh.connect();
	    rs = stmt.executeQuery("SELECT id, email FROM adminemailtab WHERE userid='" + userid + "'");
	    while (rs.next()) {
		int id = Integer.parseInt(rs.getString("id"));
		String email = rs.getString("email");
		emailList.add(new StringStringId(userid, email, id));
	    }
	    
	    dh.disconnect();
	    
	}catch(SQLException sqle){
	}

	return emailList;
    }
    
    
}
