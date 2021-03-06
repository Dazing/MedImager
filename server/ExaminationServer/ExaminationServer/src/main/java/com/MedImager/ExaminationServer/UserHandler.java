package com.MedImager.ExaminationServer;

import java.security.Key;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.mindrot.jbcrypt.BCrypt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class UserHandler{
	private static long TOKEN_TTL_MILLIS = 3600000;
	
	// TODO: Possibly swap for key stored on server
	private static final Key key = MacProvider.generateKey();
	
	public static void authenticateUser(String username, String inputPassword){
		try(Connection con = Database.getConnection();){
			
			//Check if username is in database
			String query = "SELECT * FROM users WHERE username = ?";
			try(PreparedStatement ps = con.prepareStatement(query);){
				ps.setString(1, username);
				try(ResultSet rs = ps.executeQuery();){
					if(!rs.isBeforeFirst()) {
						throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
								.header("WWW-Authenticate", "Username not valid")
								.entity("Username not valid: "+username).build());
					}
					rs.next();
					if(rs.getString("user_permission").equals("0")) {
						throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
								.header("WWW-Authenticate", "Not yet approved by admin")
								.entity("Not yet approved by admin").build());
					}
				}
			}
			
			//Confirmed that username is in database, check if password is correct
			query = "SELECT * FROM users WHERE username = ?";
			try(PreparedStatement ps = con.prepareStatement(query);){
				ps.setString(1, username);
				try(ResultSet rs = ps.executeQuery();){
					rs.next();
					String storedPassword = rs.getString("password");
					if(!isPasswordValid(inputPassword, storedPassword)) {
						throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
								.header("WWW-Authenticate", "Password not valid")
								.entity("Password not valid").build());
					}
				}
			}
		}catch(SQLException e){
			throw new WebApplicationException();
		}
	}
	
	public static String issueToken(String username){
		String query = "SELECT * FROM users WHERE username = ?";
		
		try(Connection con = Database.getConnection();
				PreparedStatement ps = con.prepareStatement(query);){
			ps.setString(1, username);
			
			try(ResultSet rs = ps.executeQuery();){
				rs.next();
				String id = rs.getString("id");
				String userPermission = rs.getString("user_permission");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				Date exp = new Date(System.currentTimeMillis() + TOKEN_TTL_MILLIS);
				
				String compactJws = Jwts.builder()
						.claim("id", id)
						.claim("username", username)
						.claim("user_permission", userPermission)
						.claim("first_name", firstName)
						.claim("last_name", lastName)
						.setExpiration(exp).signWith(SignatureAlgorithm.HS512, key).compact();
						
				return compactJws;
			}
		}catch(SQLException e){
			throw new WebApplicationException();
		}
	}
	
	public static void validateToken(String token){
		if(token == null) {
			throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
					.header("WWW-Authenticate", "Token must be provided")
					.entity("Token must be provided").build());
		}
		
		try{
			Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
			
			String oldUserPermission = claims.get("user_permission").toString();
			String id = claims.get("id").toString();
			
			String query = "SELECT * FROM users WHERE id = ?";
			try(Connection con = Database.getConnection();
					PreparedStatement ps = con.prepareStatement(query);){
				
				ps.setString(1, id);
				
				try(ResultSet rs = ps.executeQuery();){
					rs.next();
					String newUserPermission = rs.getString("user_permission");
					
					if(!newUserPermission.equals(oldUserPermission)){
						throw new WebApplicationException(
								Response.status(Response.Status.CONFLICT)
								.entity("User permission updated, please login again").build());
					}
				}
			}catch(SQLException e){
				throw new WebApplicationException();
			}
			
		}catch(SignatureException | MalformedJwtException | UnsupportedJwtException e){
			throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
					.header("WWW-Authenticate", "Token not valid")
					.entity("Token not valid").build());
		}catch(ExpiredJwtException e){
			throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
					.header("WWW-Authenticate", "Token expired")
					.entity("Token expired").build());
		}
	}
	
	public static User getUser(String id){
		if(id == null){
			throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
					.entity("No user ID provided").build());
		}
		
		
		String query = "SELECT * FROM users WHERE id = ?";
		
		try(Connection con = Database.getConnection();
				PreparedStatement ps = con.prepareStatement(query);){
			ps.setString(1, id);
			
			try(ResultSet rs = ps.executeQuery();){
				if(!rs.isBeforeFirst()) {
					throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
							.entity("User ID not valid").build());
				}
				
				rs.next();
				
				String username = rs.getString("username");
				String userPermission = rs.getString("user_permission");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				
				return new User(id, username, userPermission, firstName, lastName);
			}
		}catch(SQLException e){
			throw new WebApplicationException();
		}
	}
	
	public static List<User> getUsers(){
		String query = "SELECT * FROM users";
		
		try(Connection con = Database.getConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();){
			
			List<User> userList = new ArrayList<>();
			
			while(rs.next()){
				String id = rs.getString("id");
				String username = rs.getString("username");
				String userPermission = rs.getString("user_permission");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				
				userList.add(new User(id, username, userPermission, firstName, lastName));
			}
			
			return userList;
		}catch(SQLException e){
			throw new WebApplicationException();
		}
	}
	
	/*
	 * Assumes that token has been verified
	 */
	public static User getUserByToken(String token){
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
		String id = claims.get("id").toString();
		String username = claims.get("username").toString();
		String userPermission = claims.get("user_permission").toString();
		String firstName = claims.get("first_name").toString();
		String lastName = claims.get("last_name").toString();
		
		return new User(id, username, userPermission, firstName, lastName);
	}
	
	public static void registerUser(String username, String password, String firstName, String lastName){
		if(username == null || password == null || firstName == null || lastName == null){
			throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
					.entity("Required user info missing").build());
		}
		
		try(Connection con = Database.getConnection();){
			String query = "SELECT * FROM users WHERE username = ?";
			
			try(PreparedStatement ps = con.prepareStatement(query);){
				ps.setString(1, username);
				try(ResultSet rs = ps.executeQuery();){
					if(rs.isBeforeFirst()) {
						throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
								.entity("Account already registered").build());
					}
				}
			}
			
			query = "INSERT INTO users (username, password, first_name, last_name) VALUES (?, ?, ?, ?)";
			try(PreparedStatement ps = con.prepareStatement(query);){
				ps.setString(1, username);
				ps.setString(2, generateHashedPassword(password));
				ps.setString(3, firstName);
				ps.setString(4, lastName);
				ps.executeUpdate();
			}
		}catch(SQLException e){
			throw new WebApplicationException();
		}
	}
	
	public static void unregisterUser(String id){
		String query = "DELETE FROM users WHERE id = ?";
		
		try(Connection con = Database.getConnection();
				PreparedStatement ps = con.prepareStatement(query);){
			ps.setString(1, id);
			ps.executeUpdate();
		}catch(SQLException e){
			throw new WebApplicationException();
		}
	}
	
	public static void updatePassword(String id, String newPassword){
		if(newPassword == null){
			throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
					.entity("No new password provided").build());
		}
		
		String query = "UPDATE users SET password = ? WHERE id = ?";
		try(Connection con = Database.getConnection();
				PreparedStatement ps = con.prepareStatement(query);){
			
			ps.setString(1, generateHashedPassword(newPassword));
			ps.setString(2, id);
			ps.executeUpdate();
		}catch(SQLException e){
			throw new WebApplicationException();
		}
	}
	
	private static String generateHashedPassword(String password){
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}
	
	private static boolean isPasswordValid(String inputPassword, String storedPassword){
		return BCrypt.checkpw(inputPassword, storedPassword);
	}
	
	public static void updateUserPermission(String id, String newUserPermission){
		if(id == null){
			throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
					.entity("No user ID provided").build());
		}
		
		if(newUserPermission == null){
			throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
					.entity("No new user permission provided").build());
		}
		
		String query = "UPDATE users SET user_permission = ? WHERE id = ?";
		
		try(Connection con = Database.getConnection();
				PreparedStatement ps = con.prepareStatement(query);){
			ps.setString(1, newUserPermission);
			ps.setString(2, id);
			int rowsUpdated = ps.executeUpdate();
			if(rowsUpdated < 1){
				throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
						.entity("User ID not valid").build());
			}
		}catch(SQLException e){
			throw new WebApplicationException();
		}
	}
	
	public static void removeUser(String id){
		if(id == null){
			throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
					.entity("No user ID provided").build());
		}
				
		String query = "DELETE FROM users WHERE id = ?";
		
		try(Connection con = Database.getConnection();
				PreparedStatement ps = con.prepareStatement(query);){
			ps.setString(1, id);
			int deletedRows = ps.executeUpdate();
			if(deletedRows < 1){
				throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
						.entity("User ID not valid").build());
			}
		}catch(SQLException e){
			throw new WebApplicationException();
		}
	}
}





















