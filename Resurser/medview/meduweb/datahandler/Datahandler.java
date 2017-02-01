package medview.meduweb.datahandler;

import medview.meduweb.data.DataLayer;
import java.sql.*;
import org.postgresql.*;   //PostGreSQL Driver for JDBC
import java.util.*;        //Properties


/**
 *This class is used to manage the database connection
 *used in other datahandler classes.
 *From version 2.0 this class is only a facade to the class ConnectionPool.
 *@author Figge, mEduWeb
 *@version 2.0, 030619
 */
public class Datahandler {


    /**
     *This attribute contains the hostadress used
     *to connect to the database. Default is "localhost".
     */
    private String host = "localhost";

    /**
     *This attribute contains the port used to connect
     *to the database. Default is "5432".
     */
    private String port = "5432";

    /**
     *This attribute is the database connection.
     */
    protected java.sql.Statement con = null;

    /**
     *This attribute is the username used to get access
     *to the database.
     */
    private String username = "dproj-6";

    /**
     *This attribute is the password used to get access
     *to the database.
     */
    private String password = "";

    /**
     *This attribute is name of the database to be used.
     */
    private String database = "meduweb";

    private ConnectionPool conPool = null;
    /**
     *Returns a reference to a new instance of the
     *Datahandler.
     *@return Datahandler
     */
    public Datahandler() {
    	conPool = ConnectionPool.getInstance();
    }

    


    /**
     *Connects to the postGreSQL database.
     *Host, port, database, username and password are 
     *set separately.
     *@return Statement.
     */
     public java.sql.Statement connect() {
     	if(con != null)
     		this.disconnect();
     		
	con = conPool.getStatement();
	return con;
     }
    

    /**
     *Connects to the postGreSQL database.
     *@param hostS The host of the database.
     *@param portS The port used by the database.
     *@param databaseS The name of the database to connect to.
     *@param usernameS The username to use when connecting.
     *@param passwordS The password that grants access to the database.
     *@return Statement
     */
    /*public java.sql.Statement connect(String hostS, String portS,
			     String databaseS,
			     String usernameS,
			     String passwordS) {
	if(con != null)
	    disconnect();
	java.sql.Statement stmt = null;
	try {
	    Class.forName("org.postgresql.Driver").newInstance();
	    con = DriverManager.getConnection("jdbc:postgresql://"+ hostS + 
					      ":" + portS + "/" + databaseS +
					      "?user=" + username);
	    //usernameS,passwordS);
	    stmt = con.createStatement();
	}
	catch(ClassNotFoundException cnfe) {
	    System.out.println(cnfe.getMessage());
	} catch (Exception sqle) {
	    sqle.printStackTrace();
	}    
	return stmt;
    }*/

    /**
     *Closes the connection to the database.
     *@return void
     */
    public void disconnect() {
	    if (con != null)
		conPool.returnStatement(con);
	con = null;
    }

    /**
     *Destroys the connection pool and creates a new one.
     *Should be used to refresh connections and/or when 
     *database settings have changed.
     */
    public void reinitiateConnectionPool() {
    	ConnectionPool.destroyPool();
    	conPool = ConnectionPool.getInstance();
    	con = null;	
    }
    
    /**
     *Gets the name of the database.
     *@return String the databasename.
     */
    public String getDatabase() {
	return database;
    }

    /**
     *Gets the database host.
     *@return String the hostname.
     */
    public String getHost() {
	return host;
    }

    /**
     *Returns the number of free connections in the connection pool used.
     *@return number of free connections.
     */
    public int getNumberOfFreeConnections() {
    	return conPool.getNOFreeCons();	
    }
    
    /**
     *Gets the database password used.
     *@return String the password.
     */
    public String getPassword() {
	return password;
    }

    /**
     *Gets the database port.
     *@return String the port.
     */
    public String getPort() {
	return port;
    }

    /**
     *Gets the database username used.
     *@return String the username.
     */
    public String getUsername() {
	return username;
    }

    /**
     *Tells whether a connection to the db is set up
     *or not.
     *@return boolean true if connected false o.w.
     */
    public boolean isConnected() {
	if(con == null) {
	    return false;
	}else{
	    return true;
	}
    }

    /**
     *Sets the name of the database to be used.
     *@param dbn the name.
     *@return void
     */
    public void setDatabase(String dbn) {
	this.database = dbn;
	conPool.database = dbn;
    }

    /**
     *Sets the hostname to be used when connecting to 
     *the database.
     *@param hostn the name.
     *@return void
     */
    public void setHost(String hostn) {
	this.host = hostn;
	conPool.host = hostn;
    }

    /**
     *Sets the password that gives access to the 
     *database.
     *@param passwn the password.
     *@return void
     */
    public void setPassword(String passwn) {
	this.password = passwn;
	conPool.password = passwn;
    }

    /**
     *Sets the port to be used when connecting to 
     *the database.
     *@param portn the port.
     *@return void
     */
    public void setPort(String portn) {
	this.port = portn;
	conPool.port = portn;
    }

    /**
     *Sets the username that gives access to the 
     *database.
     *@param usern the name.
     *@return void
     */
    public void setUsername(String usern) {
	this.username = usern;
	conPool.username = usern;
    }


}
