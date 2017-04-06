package com.MedImager.ExaminationServer;

import java.security.Key;
import java.sql.*;
import java.util.List;
import java.util.Map;

import javax.ws.rs.NotAuthorizedException;

import com.mysql.fabric.proto.xmlrpc.AuthenticatedXmlRpcMethodCaller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

//TODO: Thread safety - Close connections, connection pooling etc.
public class UserHandler{

	private static long TOKEN_TTL_MILLIS = 3600000;
	private static final Key key = MacProvider.generateKey();

	public static void authenticateUser(String username, String password) throws Exception{
		Connection connection = Database.getConnection();
		String query = "SELECT * FROM users WHERE Username = " + username + " AND Password = " + password;

		try{
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if(!resultSet.isBeforeFirst()) {
				throw new Exception();
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	public static String issueToken(String username){
		String token = null;

		Connection connection = Database.getConnection();
		String query = "SELECT * FROM users WHERE Username = 'rune'";

		try{
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);

			String userPermission = null;
			if(resultSet.isBeforeFirst()) {
				resultSet.next();
				userPermission = resultSet.getString("UserPermission");
			}

			// TODO: Issue JWT token based on user info

			long nowMillis = System.currentTimeMillis();
			Date exp = new Date(System.currentTimeMillis() + TOKEN_TTL_MILLIS);

			String compactJws = Jwts.builder().setSubject(username).setExpiration(exp)
					.claim("userpermission", "testpermission").signWith(SignatureAlgorithm.HS512, key).compact();

			return compactJws;

		}catch(SQLException e){
			e.printStackTrace();
		}

		return token;
	}

	public static void validateToken(String token){
		if(token == null) {
			throw new NotAuthorizedException("Authorization header must be provided");
		}

		// TODO:
		// Check if it was issued by the server and if it's not expired.
		// If token is invalid: throw new NotAuthorizedException("Token not
		// valid");

		// This line will throw an exception if it is not a signed JWS (as
		// expected)
		Claims claims = Jwts.parser().setSigningKey(key)
				.parseClaimsJws(token).getBody();
		System.out.println("Subject: " + claims.getSubject());
		System.out.println("Expiration: " + claims.getExpiration().toString());

	}

	public static Map<String, String> getUserInfo(String token){
		// TODO: Extract user info using token and querying database

		return null;
	}
}