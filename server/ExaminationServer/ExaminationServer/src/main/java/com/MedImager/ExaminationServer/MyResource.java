package com.MedImager.ExaminationServer;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.jdt.internal.compiler.lookup.ReductionResult;

import misc.foundation.MethodNotSupportedException;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("api")
public class MyResource {
	@GET
	@Path("{searchTerm}")
    @Produces(MediaType.APPLICATION_JSON)
    public List <Examination> getJSONResult(@PathParam("searchTerm") String searchURL) throws MethodNotSupportedException{
		SearchFilter filter = SearchParser.createSearchFilter(searchURL);
		SearchTermParser search = new SearchTermParser(filter);
		return search.getResultList();
    }
	
	@GET
	@Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public List <Examination> testGet(@QueryParam("Values") List<String> values,
    										 @QueryParam("Term") List<String> terms,
    										 @QueryParam("AgeLower") String ageLower,
    										 @QueryParam("AgeUpper") String ageUpper) throws MethodNotSupportedException{
		SearchTermParser search = new SearchTermParser(new SearchFilter(values, terms, ageLower, ageUpper));
		return search.getResultListWithFilter();
    }
	
//	@GET
//	@Path("/patient/{examinationID")
//    @Produces(MediaType.APPLICATION_JSON)
//	public List <Examination> getPatientJSON(@PathParam("examinationID") String examinationID) throws MethodNotSupportedException{
//		
//		return null;
//	}
}