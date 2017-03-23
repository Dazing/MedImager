package com.MedImager.ExaminationServer;

import java.io.IOException;
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

import medview.datahandling.examination.NoSuchExaminationException;
import misc.foundation.MethodNotSupportedException;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("api")
public class MyResource {
	
	@GET
	@Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List <Examination> search(@QueryParam("Value") List<String> values,
    										 @QueryParam("Term") List<String> terms,
    										 @QueryParam("AgeLower") String ageLower,
    										 @QueryParam("AgeUpper") String ageUpper, 
    										 @QueryParam("Gender") String gender,
    										 @QueryParam("Smoke") Boolean smoke,
    										 @QueryParam("Snuff") Boolean snuff) throws MethodNotSupportedException{
		SearchTermParser stp = new SearchTermParser(new SearchFilter(values, terms, ageLower, ageUpper, gender, smoke, snuff));
		return stp.getResultListWithFilter();
    }

	@GET
	@Path("/patient/{examinationID}")
    @Produces(MediaType.APPLICATION_JSON)
	public List<Examination> getPatientJSON(@PathParam("examinationID") String examinationID) throws MethodNotSupportedException, IOException{
		ExaminationIDParser eidParser = new ExaminationIDParser();
		return eidParser.getMoreFromPatient(examinationID);
	}

	@GET
	@Path("/examination/{examinationID}")
    @Produces(MediaType.APPLICATION_JSON)
	public Examination getExaminationJSON(@PathParam("examinationID") String examinationID) throws MethodNotSupportedException, IOException{
		ExaminationIDParser eidParser = new ExaminationIDParser();
		return eidParser.getMoreFromExamination(examinationID);
	}
	
	@GET
	@Path("/initValues")
    @Produces(MediaType.APPLICATION_JSON)
	public InitValues getTreatTypesJSON() throws IOException, NoSuchExaminationException{
		ServerInitializer init = new ServerInitializer();
		return init.initialize();
	}
}