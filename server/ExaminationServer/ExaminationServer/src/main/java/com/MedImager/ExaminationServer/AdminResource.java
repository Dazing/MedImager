package com.MedImager.ExaminationServer;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/admin")
public class AdminResource{
	@Secured
	@RolesAllowed("admin")
	@GET
	@Path("getuser/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("id") String id){
		return UserHandler.getUser(id);
	}
	
	@Secured
	@RolesAllowed("admin")
	@GET
	@Path("getusers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getUsers(){
		return UserHandler.getUsers();
	}

	@Secured
	@RolesAllowed("admin")
	@PUT
	@Path("updateuserpermission/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response updateUserPermission(@PathParam("id") String id, 
										@HeaderParam("NewUserPermission") String newUserPermission){
		UserHandler.updateUserPermission(id, newUserPermission);
		return Response.ok("User permission updated").build();
	}
	
	@Secured
	@RolesAllowed("admin")
	@DELETE
	@Path("removeuser/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response removeUser(@PathParam("id") String id){
		UserHandler.removeUser(id);
		return Response.ok("User removed").build();
	}
}