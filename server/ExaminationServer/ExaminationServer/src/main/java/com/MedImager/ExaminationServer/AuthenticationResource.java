package com.MedImager.ExaminationServer;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
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

@Path("/authentication")
public class AuthenticationResource {
	
	@GET
	@Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(){
		return Response.status(Response.Status.OK).entity("Proceed with login").build();
    }
	
	@POST
	@Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@HeaderParam("Username") String username, @HeaderParam("Password") String password){
        try {
            MockUserDatabase.authenticateUser(username, password);
            String token = MockUserDatabase.issueToken(username);
            return Response.ok(token).build();
        } catch (Exception e){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
	
	// A mocked secure endpoint requiring a valid token
	@Secured
	@GET
	@Path("restricted")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRestricted(){
		return Response.status(Response.Status.OK).entity("Welcome authorized user!").build();
    }
	
	// A mocked secure endpoint requiring token issued to an admin
	@Secured
	@RolesAllowed("admin")
	@GET
	@Path("adminrestricted")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdminRestricted(){
		return Response.status(Response.Status.OK).entity("Welcome authorized user!").build();
    }
}