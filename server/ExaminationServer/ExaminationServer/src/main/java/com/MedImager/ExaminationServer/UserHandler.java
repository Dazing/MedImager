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
	
	private static long TOKEN_TTL_MILLIS = 3600000;
	
	// TODO: Possibly swap for key stored on server
	private static final Key key = MacProvider.generateKey();
	
	public static void authenticateUser(String username, String password){
		try(Connection con = Database.getConnection();){
			
			//Check if username is in database
			String query = "SELECT * FROM users WHERE Username = ?";
			try(PreparedStatement ps = con.prepareStatement(query);){
				ps.setString(1, username);
				try(ResultSet rs = ps.executeQuery();){
					if(!rs.isBeforeFirst()) {
						throw new NotAuthorizedException("Username not valid");
					}
				}
			}
			
			//Confirmed that username is in database, check now if password is correct
			query = "SELECT * FROM users WHERE Username = ? AND Password = ?";
			try(PreparedStatement ps = con.prepareStatement(query);){
				ps.setString(1, username);
				ps.setString(2, password);
				try(ResultSet rs = ps.executeQuery();){
					if(!rs.isBeforeFirst()) {
						throw new NotAuthorizedException("Password not valid");
					}
				}
			}
		}catch(SQLException e){
			throw new WebApplicationException();
		}
	}
	
	public static String issueToken(String username){
		String query = "SELECT * FROM users WHERE Username = ?";
		
		try(Connection con = Database.getConnection();
				PreparedStatement ps = con.prepareStatement(query);){
			ps.setString(1, username);
			
			try(ResultSet rs = ps.executeQuery();){
				rs.next();
				
				String userPermission = rs.getString("UserPermission");
				Date exp = new Date(System.currentTimeMillis() + TOKEN_TTL_MILLIS);
				String compactJws = Jwts.builder().claim("username", username)
						.claim("userpermission", userPermission).setExpiration(exp)
						.signWith(SignatureAlgorithm.HS512, key).compact();
						
				return compactJws;
			}
		}catch(SQLException e){
			throw new WebApplicationException();
		}
	}
	
	public static void validateToken(String token){
		if(token == null) {
			throw new NotAuthorizedException("Authorization header must be provided");
		}
		
		try{
			Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
			
			System.out.println("username: " + claims.get("username").toString());
			System.out.println("Permission: " + claims.get("userpermission").toString());
			System.out.println("Expiration: " + claims.getExpiration().toString());
		}catch(SignatureException e){
			throw new NotAuthorizedException("Token not valid");
		}catch(ExpiredJwtException e){
			throw new NotAuthorizedException("Token expired");
		}
	}
	
	/*
	 * Assumes that token has been verified
	 */
	public static User getUserByToken(String token){
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
		String username = claims.get("username").toString();
		
		String query = "SELECT * FROM users WHERE Username = ?";
		
		try(Connection con = Database.getConnection();
				PreparedStatement ps = con.prepareStatement(query);){
			
			ps.setString(1, username);
			
			try(ResultSet rs = ps.executeQuery();){
				rs.next();
				
				String id = rs.getString("ID");
				String userPermission = rs.getString("UserPermission");
				String email = rs.getString("Email");
				String firstName = rs.getString("FirstName");
				String lastName = rs.getString("LastName");
				
				return new User(id, username, userPermission, email, firstName, lastName);
			}
		}catch(SQLException e){
			throw new WebApplicationException();
		}
	}
}
