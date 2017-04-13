package com.MedImager.ExaminationServer;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

@ApplicationPath("api")
public class MyApp extends ResourceConfig {
	public MyApp() {
		
		super(AuthenticationResource.class);
		/*
		try {
			register(RolesAllowedDynamicFeature.class);
		}
		catch(Exception e){
			System.out.println("Exception: "+e);
		}
		*/
		System.out.println("MyApp Found!");
		register(RolesAllowedDynamicFeature.class);
    }
}