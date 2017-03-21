package com.MedImager.ExaminationServer;

import medview.datahandling.examination.ExaminationValueContainer;
import medview.datahandling.examination.MVDHandler;

public class ExaminationBuilder {
	Examination ex = new Examination();
	
	public ExaminationBuilder(){
		
	}
	
	public Examination getExamination(ExaminationIdentifier eid){
		MVDHandler handler = new MVDHandler();
		ExaminationValueContainer container = handler.getExaminationValueContainer(eid);
		//ex.setAge(container.getValues("Age"))
		return ex;
	}
}
