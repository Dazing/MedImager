package com.MedImager.ExaminationServer;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

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
	public void filter(ContainerRequestContext requestContext){
		String token = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		UserHandler.validateToken(token);
		
		User user = UserHandler.getUserByToken(token);
		
		final String username = user.getUsername();
		final String userPermission = user.getUserPermission();
		
		// Store the user's info so it can easily be retrieved if needed
		storeUserInfo(username, userPermission, requestContext);
	}
	
	/*
	 * Stores the user's name and role for methods needing access to that
	 * information. Storing of name possibly not needed since token should be
	 * tied to user in database.
	 */
	private void storeUserInfo(final String username, final String userPermission,
			ContainerRequestContext requestContext){
		
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
			public boolean isUserInRole(String userPermissionRequired){
				// TODO: Use token to find the user's role in the user database
				return userPermission.equals(userPermissionRequired);
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
