package com.MedImager.ExaminationServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import medview.datahandling.MedViewDataHandler;
import medview.datahandling.NoSuchTermException;
import medview.datahandling.PatientIdentifier;
import medview.datahandling.examination.ExaminationIdentifier;
import medview.datahandling.examination.ExaminationValueContainer;
import medview.datahandling.examination.NoSuchExaminationException;

public class ServerInitializer {
	MedViewDataHandler handler;
	InitValues initValues;
	public ServerInitializer(){
		handler = MedViewDataHandler.instance();
        handler.setExaminationDataLocation("TestData.mvd");
		initValues = new InitValues();
	}
	public InitValues initialize() throws IOException, NoSuchExaminationException{
		setTreatTypes();
		return initValues;
	}
	private void setTreatTypes() throws IOException, NoSuchExaminationException{
		List<String> results = new ArrayList<String>();
		for(PatientIdentifier pid : handler.getPatients()){
			for(ExaminationIdentifier eid : handler.getExaminations(pid)){
            	ExaminationValueContainer container;
					container = handler.getExaminationValueContainer(eid);
	                    try {
							for (String s : container.getValues("Treat-type")) {
								if(!results.contains(s)){
									results.add(s);
								}
							}
						} catch (NoSuchTermException e) {
						}
	                }
				}
			
		
		initValues.setTreatTypes(results);
	}
}
