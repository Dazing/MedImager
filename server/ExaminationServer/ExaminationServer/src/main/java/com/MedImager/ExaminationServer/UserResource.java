package com.MedImager.ExaminationServer;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import java.util.List;

@Path("/user")
public class UserResource{
	@GET
	@Path("login")
	@Produces(MediaType.TEXT_PLAIN)
	public Response login(){
		List<User> users = UserHandler.getUsers();
		
		String str = "";
		
		for	(int i = 0; i < users.size(); i++) {
			str += ("username: "+users.get(i).getUsername()+", ");
		}
		
		return Response.ok(str).build();
	}
	
	@POST
	@Path("login")
	@Produces(MediaType.TEXT_PLAIN)
	public Response login(@HeaderParam("Username") String username, @HeaderParam("Password") String password){
		UserHandler.authenticateUser(username, password);
		String token = UserHandler.issueToken(username);
		return Response.ok(token).build();
	}
	
	@POST
	@Path("register")
	@Produces(MediaType.TEXT_PLAIN)
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
	@Produces(MediaType.TEXT_PLAIN)
	public Response unregister(@Context SecurityContext securityContext){
		String id = securityContext.getUserPrincipal().getName();
		UserHandler.unregisterUser(id);
		return Response.ok("User unregistered").build();
	}
	
	@Secured
	@GET
	@Path("getuser")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@Context SecurityContext securityContext){
		String id = securityContext.getUserPrincipal().getName();
		return UserHandler.getUser(id);
	}
	
	@Secured
	@PUT
	@Path("updatepassword")
	@Produces(MediaType.TEXT_PLAIN)
	public Response updatePassword(@HeaderParam("NewPassword") String newPassword,
									@Context SecurityContext securityContext){
		String id = securityContext.getUserPrincipal().getName();
		UserHandler.updatePassword(id, newPassword);
		return Response.ok("Password updated").build();
	}
	
	// A mocked secure resource requiring a valid token
	@Secured
	@GET
	@Path("restricted")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getRestricted(){
		return Response.ok("Welcome authorized user!").build();
	}
	
	// A mocked secure resource requiring token issued to an admin
	@Secured
	@RolesAllowed("admin")
	@GET
	@Path("adminrestricted")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getAdminRestricted(){
		return Response.ok("Welcome admin!").build();
	}
}