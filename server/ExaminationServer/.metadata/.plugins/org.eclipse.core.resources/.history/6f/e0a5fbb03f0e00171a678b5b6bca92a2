package com.MedImager.ExaminationServer;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import misc.foundation.MethodNotSupportedException;

public class Router {
	@GET
	@Path("{searchTerm}")
    public getJSONResult(@PathParam("searchTerm") String searchURL) throws MethodNotSupportedException{

		return search.getResultList();
    }
}
