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
	public void filter(ContainerRequestContext requestContext) throws IOException{
		String token = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		UserHandler.validateToken(token);
		
//		Map<String, String> userInfo = UserHandler.getUserInfo(token);
//		
//		final String username = userInfo.get("username");
//		final String role = userInfo.get("userPermission");
		
		final String username = "rune";
		final String role = "normal";
		
		// Store the user's info so it can easily be retrieved if needed
		storeUserInfo(username, role, requestContext);
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
