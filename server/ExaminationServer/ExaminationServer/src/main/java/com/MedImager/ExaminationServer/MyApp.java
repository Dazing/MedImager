package com.MedImager.ExaminationServer;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

@ApplicationPath("api")
public class MyApp extends ResourceConfig {
	public MyApp() {
		super(AuthenticationResource.class);
		register(RolesAllowedDynamicFeature.class);
    }
}
