import java.sql.*;

public class Database {


    // JDBC driver name and database URL
    static private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static private final String DB_URL = "jdbc:mysql://localhost/medimager";

    static private Connection connection = null;

    // Database credentials
    static private final String USER = "root";
    static private final String PASS = "1234";

    public Database () {
        try{
            Class.forName("com.mysql.jdbc.Driver");

            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL,USER,PASS);

        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }

    public Connection getConnection () {
        return connection;
    }

}


