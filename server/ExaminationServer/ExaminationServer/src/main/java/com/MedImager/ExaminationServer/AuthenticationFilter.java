package com.MedImager.ExaminationServer;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter{

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException{
		
		String token = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		validateToken(token);
		
		String tempUsername = "";
		String tempRole = "";
		
		// Get the user's username and role
		for(User user : MockUserDatabase.users){
    		if(user.getToken() != null && user.getToken().equals(token)){
    			tempUsername = user.getUsername();
    			tempRole = user.getRole();
    		}
    	}
		
		final String username = tempUsername;
		final String role = tempRole;
		
		// Store the user's info so it can be retrieved by methods needing this info
		storeUserInfo(username, role, requestContext);
	}
	
	private void validateToken(String token){
		if (token == null) {
			throw new NotAuthorizedException("Authorization header must be provided");
		}
		
		// TODO:
		// Check if it was issued by the server and if it's not expired.
		// Throw an Exception if the token is invalid.
		
		// Mock
		for(User user : MockUserDatabase.users){
    		if(user.getToken() != null && user.getToken().equals(token)){
    			return;
    		}
    	}
		
		throw new NotAuthorizedException("Token not valid");
	}
	
	/*
	 * Stores the user's name and role for methods needing access to that information.
	 * Storing of name possibly not needed since token should be tied to user in database.
	 */
	private void storeUserInfo(final String username, final String role, ContainerRequestContext requestContext){
		
		final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
		requestContext.setSecurityContext(new SecurityContext(){
			
			@Override
			public Principal getUserPrincipal(){
				return new Principal(){
					@Override
					public String getName(){
						// Mock
						return username;
					}
				};
			}
			
			@Override
			public boolean isUserInRole(String roleToCheck){
				// TODO: Use token to find the user's role in the user database
				return role.equals(roleToCheck);
			}
			
			@Override
			public boolean isSecure(){
				return currentSecurityContext.isSecure();
			}
			
			@Override
			public String getAuthenticationScheme(){
				return currentSecurityContext.getAuthenticationScheme();
			}
		});
		
	}
}
