package medview.meduweb.datahandler;
import java.sql.*;
import org.postgresql.*;   //PostGreSQL Driver for JDBC
import java.util.*;        //Properties

/**
 *This class represents a pool of database connections and is used by the datahandler
 *to handle out and return all connections.
 *@author Fredrik Pettersson, mEduWeb/MedView
 *@version 1.0, 030619
 */
public class ConnectionPool {
    /**
     *This attribute contains the hostadress used
     *to connect to the database. Default is "localhost".
     */
    public String host = "localhost";

    /**
     *This attribute contains the port used to connect
     *to the database. Default is "5432".
     */
    public String port = "5432";

    /**
     *This attribute is the database connection.
     */
    protected java.sql.Connection con = null;

    /**
     *This attribute is the username used to get access
     *to the database.
     */
    public String username = "dproj-6";

    /**
     *This attribute is the password used to get access
     *to the database.
     */
    public String password = "";

    /**
     *This attribute is name of the database to be used.
     */
    public String database = "meduweb";

    /** The Initial number of connections in the pool */ 	
    public static int POOL_SIZE = 2;
    public static int POOL_MIN_SIZE = 3;
    
    /** Expansion rate when out of connections */
    public static int POOL_EXPANSION = 2;
    
    private static java.sql.Connection[] thePool;
    private static java.sql.Statement[] stmtPool;
    private static int free = 0;
    private static ConnectionPool theInstance = null; 
	
	private ConnectionPool() {
	    thePool = new java.sql.Connection[POOL_SIZE];
	    stmtPool = new java.sql.Statement[POOL_SIZE];
	    free = POOL_SIZE;
	    try {
	    	Class.forName("org.postgresql.Driver").newInstance();
	    	Properties props = new Properties();
	    	props.put("user",username);
	    	props.put("password",password);
	    	props.put("charSet","SQL_ASCII");
	    	for(int i = 0; i < POOL_SIZE;i++) {
	    	 	thePool[i] = DriverManager.getConnection("jdbc:postgresql://"+ host + 
						      ":" + port + "/" + database,
						      props);
			stmtPool[i] = thePool[i].createStatement();
		}
	    } catch (ClassNotFoundException cnfe) {
	    	System.out.println(cnfe.getMessage());
	    } catch (Exception sqle) {
	    	sqle.printStackTrace();
	    }	
	    System.out.println("Initited Database Connection Pool (of size " + POOL_SIZE + ")");
	}
	
	/**
	 *Returns an instance of the database connection pool.
	 *@return a connection pool
	 */
	public static ConnectionPool getInstance() {
		if(theInstance == null) {
			theInstance = new ConnectionPool();
		}
		return theInstance;	
	}

	/* Expands the pool */
	private void expandPool() {
		java.sql.Connection[] tmpPool = thePool;
		java.sql.Statement[] tmpStmtPool = stmtPool;
		POOL_SIZE += POOL_EXPANSION;
		thePool = new java.sql.Connection[POOL_SIZE];
		stmtPool = new java.sql.Statement[POOL_SIZE];
		Properties props = new Properties();
	    	props.put("user",username);
	    	props.put("password",password);
	    	props.put("charSet","SQL_ASCII");
		for(int i = 0; i < POOL_EXPANSION;i++) {
			try {
			thePool[i] = DriverManager.getConnection("jdbc:postgresql://"+ host + 
						      ":" + port + "/" + database,
						      props);
			stmtPool[i] = thePool[i].createStatement();
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());	
		}	
		}
		for(int j = 0; j < (POOL_SIZE - POOL_EXPANSION); j++) {
			thePool[j + POOL_EXPANSION] = tmpPool[j];
			stmtPool[j + POOL_EXPANSION] = tmpStmtPool[j];	
		}
		free += POOL_EXPANSION;
		System.out.println("Database Connection pool Expanded (new size: " + POOL_SIZE + ")");
	}
	
	private void decreasePool() {
		if((free > 1) && (POOL_SIZE > POOL_MIN_SIZE)) {
			java.sql.Connection[] tmpPool = thePool;
			java.sql.Statement[] tmpStmtPool = stmtPool;	
			POOL_SIZE -= 1;
			thePool = new java.sql.Connection[POOL_SIZE];
			stmtPool = new java.sql.Statement[POOL_SIZE];
			for(int i = 0; i < POOL_SIZE;i++) {
				thePool[i] = tmpPool[i+1];
				stmtPool[i] = tmpStmtPool[i+1];	
			}
			try {
				tmpPool[0].close();
				//tmpPool[1].close();
			} catch (SQLException sqle) {
			}
			tmpStmtPool[0] = null;
			//tmpStmtPool[1] = null;
			tmpPool[0] = null;
			//tmpPool[1] = null;
			free--;
			//free--;
			System.out.println("Size of connection pool decreased. (new size: " +POOL_SIZE + ")");
		}	
	}

	/**
	 *Gets one of the statements in the pool, i.e. a connection to the database.
	 *@return a statement that can be used to access the database.
	 */
	public synchronized java.sql.Statement getStatement() {
		if(free == 0)
			expandPool();
		free--;
		java.sql.Statement tmp = stmtPool[free];
		stmtPool[free] = null;

		return tmp;
	}
	
	/**
	 *Returns a statement/connection to the pool.
	 *@param stmt The Statement that no longer is used and should be returned.
	 */
	public synchronized void returnStatement(java.sql.Statement stmt) {
		if(free != POOL_SIZE) {
			stmtPool[free] = stmt;
			free++;	
		}

		if((free > 2) && (free > (POOL_SIZE - 3)) )
			decreasePool();
	}
	
	/**
	 *Kills the pool instance and closes all connections.
	 */  
	public static void destroyPool() {
		for(int i = 0; i < POOL_SIZE; i++) {
			try {
				if(thePool != null && thePool[i] != null)
					thePool[i].close();
			} catch (Exception e) {
				System.out.println(e.getMessage());	
			}
		}
		theInstance = null;
		System.out.println("Database Connection Pool Destroyed.");	
	}
	
	public int getNOFreeCons() {
		return free;	
	}
}