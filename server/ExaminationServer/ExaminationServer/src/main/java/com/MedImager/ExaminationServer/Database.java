package com.MedImager.ExaminationServer;

import java.sql.*;

public class Database{
	
	static private final String DB_URL = "jdbc:mysql://localhost/medimager";
	static private final String USER = "root";
	static private final String PASS = "1234";
	
	public static Connection getConnection(){
		Connection connection = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
		}catch(SQLException se){
			// Handle errors for JDBC
			se.printStackTrace();
		}catch(ClassNotFoundException e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}
		return connection;
	}
}