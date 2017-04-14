package com.MedImager.ExaminationServer;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/admin")
public class AdminResource{
	@Secured
	@RolesAllowed("admin")
	@GET
	@Path("getuser")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@HeaderParam("ID") String id){
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
}