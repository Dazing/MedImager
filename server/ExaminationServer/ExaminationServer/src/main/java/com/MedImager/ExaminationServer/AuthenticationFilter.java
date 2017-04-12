package com.MedImager.ExaminationServer;

import java.security.Principal;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
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
		User user = UserHandler.getUser(token);
		
		// Store relevant user info in case resources require them
		final String username = user.getUsername();
		final String userPermission = user.getUserPermission();
		storeUserInfo(username, userPermission, requestContext);
	}
	
	private void storeUserInfo(final String username, final String userPermission,
			ContainerRequestContext requestContext){
		final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
		requestContext.setSecurityContext(new SecurityContext(){
			@Override
			public Principal getUserPrincipal(){
				return new Principal(){
					@Override
					public String getName(){
						return username;
					}
				};
			}
			@Override
			public boolean isUserInRole(String userPermissionRequired){
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
