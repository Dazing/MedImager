package com.MedImager.ExaminationServer;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/user")
public class AuthenticationResource{
	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(){
		return Response.ok("Proceed with login").build();
	}
	
	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@HeaderParam("Username") String username, @HeaderParam("Password") String password){
		UserHandler.authenticateUser(username, password);
		String token = UserHandler.issueToken(username);
		return Response.ok(token).build();
	}
	
	@POST
	@Path("register")
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(@HeaderParam("Username") String username, 
							@HeaderParam("Password") String password, 
							@HeaderParam("FirstName") String firstName,
							@HeaderParam("LastName") String lastName){
		UserHandler.registerUser(username, password, firstName, lastName);
		return Response.ok("Preliminary registration complete, awaiting approval").build();
	}
	
	@Secured
	@DELETE
	@Path("unregister")
	@Produces(MediaType.APPLICATION_JSON)
	public Response unregister(@Context SecurityContext securityContext){
		String username = securityContext.getUserPrincipal().getName();
		UserHandler.unregisterUser(username);
		return Response.ok("User unregistered").build();
	}
	
	// A mocked secure resource requiring a valid token
	@Secured
	@GET
	@Path("restricted")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRestricted(){
		return Response.ok("Welcome authorized user!").build();
	}
	
	// A mocked secure resource requiring token issued to an admin
	@Secured
	@RolesAllowed("admin")
	@GET
	@Path("adminrestricted")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAdminRestricted(){
		return Response.ok("Welcome admin!").build();
	}
}