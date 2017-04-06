package com.MedImager.ExaminationServer;

import java.security.Key;
import java.sql.*;
import java.util.Map;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.WebApplicationException;

import org.eclipse.jdt.internal.compiler.util.Sorting;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;

//TODO: Thread safety - Close connections, connection pooling etc.
public class UserHandler{
	
//	private static long TOKEN_TTL_MILLIS = 3600000;
	private static long TOKEN_TTL_MILLIS = 20000;
	
	// TODO: Possibly swap for key stored on server
	private static final Key key = MacProvider.generateKey();
	
	public static void authenticateUser(String username, String password){
		Connection connection = Database.getConnection();
		String query = "SELECT * FROM users WHERE " + "Username = '" + username + "' AND Password = '" + password
				+ "'";
				
		try{
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if(!resultSet.isBeforeFirst()) {
				throw new NotAuthorizedException("Credentials not valid");
			}
		}catch(SQLException e){
			throw new WebApplicationException();
		}
	}
	
	public static String issueToken(String username){
		Connection connection = Database.getConnection();
		String query = "SELECT * FROM users WHERE Username = '" + username + "'";
		
		try{
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
			resultSet.next();
			
			String userPermission = resultSet.getString("UserPermission");
			Date exp = new Date(System.currentTimeMillis() + TOKEN_TTL_MILLIS);
			
			String compactJws = Jwts.builder().claim("username", username)
					.claim("userpermission", "testpermission").setExpiration(exp)
					.signWith(SignatureAlgorithm.HS512, key).compact();
					
			return compactJws;
			
		}catch(SQLException e){
			throw new WebApplicationException();
		}
	}
	
	public static void validateToken(String token){
		if(token == null) {
			throw new NotAuthorizedException("Authorization header must be provided");
		}
		
		// TODO:
		// Check if it was issued by the server and if it's not expired.
		
		// This line will throw an exception if it is not a signed JWS (as
		// expected)
		try{
			Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token)
					.getBody();
					
			System.out.println("username: " + claims.get("username").toString());
			System.out.println("Permission: " + claims.get("userpermission").toString());
			System.out.println("Expiration: " + claims.getExpiration().toString());
		}catch(SignatureException e){
			throw new NotAuthorizedException("Token not valid");
		}catch(ExpiredJwtException e){
			throw new NotAuthorizedException("Token expired");
		}
	}
	
	public static User getUserByToken(String token){
		// TODO: Extract user info using token and querying database
		
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
		String username = claims.get("username").toString();
		
		Connection connection = Database.getConnection();
		String query = "SELECT * FROM users WHERE Username = '" + username + "'";
		
		try{
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
			resultSet.next();
			String id = resultSet.getString("ID");
			String userPermission = resultSet.getString("UserPermission");
			String email = resultSet.getString("Email");
			String firstName = resultSet.getString("FirstName");
			String lastName = resultSet.getString("LastName");
			
			return new User(id, username, userPermission, email, firstName, lastName);
			
		}catch(SQLException e){
			throw new WebApplicationException();
		}
	}
}




















