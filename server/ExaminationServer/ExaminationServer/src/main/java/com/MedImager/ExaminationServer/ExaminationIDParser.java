package com.MedImager.ExaminationServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import medview.datahandling.MedViewDataHandler;
import medview.datahandling.PatientIdentifier;
import medview.datahandling.examination.ExaminationIdentifier;

public class ExaminationIDParser {

	MedViewDataHandler handler;
	
	public ExaminationIDParser(){
		handler = MedViewDataHandler.instance();
        handler.setExaminationDataLocation("TestData.mvd");
	}
	
	public List<Examination> getMoreFromPatient(String examinationID) throws IOException{
		for(PatientIdentifier pid : handler.getPatients()){
			for(ExaminationIdentifier eid : handler.getExaminations(pid)){
				if(eid.getExaminationIDString().equals(examinationID)){
					return allFromPID(pid);
				}
			}
		}
		return null;
		
	}
	public ExaminationIdentifier getExaminationIdentifier(String examinationID) throws IOException{
		for(PatientIdentifier pid : handler.getPatients()){
			for(ExaminationIdentifier eid : handler.getExaminations(pid)){
				if(eid.getExaminationIDString().equals(examinationID)){
					return eid;
				}
			}
		}
		return null;
		
	}
	public Examination getMoreFromExamination(String examinationID) throws IOException{
		ExaminationBuilder eidBuilder = new ExaminationBuilder();
		for(PatientIdentifier pid : handler.getPatients()){
			for(ExaminationIdentifier eid : handler.getExaminations(pid)){
				if(eid.getExaminationIDString().equals(examinationID)){
					return eidBuilder.getExamination(eid);
				}
			}
		}
		return null;
		
	}
	private List<Examination> allFromPID(PatientIdentifier pid) throws IOException{
		List<Examination> result = new ArrayList<Examination>();
		ExaminationBuilder eidBuilder = new ExaminationBuilder();
		for(ExaminationIdentifier eid: handler.getExaminations(pid)){
			result.add(eidBuilder.getExamination(eid));
		}
		return result;
	}
}