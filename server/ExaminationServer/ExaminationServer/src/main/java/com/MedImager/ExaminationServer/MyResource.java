package com.MedImager.ExaminationServer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import medview.common.data.MedViewUtilities;
import medview.datahandling.examination.MVDHandler;
import medview.datahandling.images.ExaminationImage;
import misc.foundation.MethodNotSupportedException;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
	SearchTermParser search = new SearchTermParser("Pollen");
	//MedViewUtilities util = new MedViewUtilities();
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt(){
		SearchTermParser search = new SearchTermParser("Pollen");
		StringBuilder sb = new StringBuilder();
		for(Examination s : search.getResultList()){
			for (ExaminationImage img:s.getImages()){
				try {
					sb.append(img.getFile() + ", ");
				} catch (MethodNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return sb.toString();
    }
}
