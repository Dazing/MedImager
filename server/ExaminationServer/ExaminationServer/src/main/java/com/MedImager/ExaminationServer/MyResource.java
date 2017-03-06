package com.MedImager.ExaminationServer;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import misc.foundation.MethodNotSupportedException;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("search")
public class MyResource {
	@GET
	@Path("{searchTerm}")
    @Produces(MediaType.APPLICATION_JSON)
    public List <Examination> getJSONResult(@PathParam("searchTerm") String searchURL) throws MethodNotSupportedException{
		SearchFilter filter = SearchParser.createSearchFilter(searchURL);
		SearchTermParser search = new SearchTermParser(filter);
		return search.getResultList();
    }
}