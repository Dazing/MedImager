package com.MedImager.ExaminationServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import medview.datahandling.InvalidPIDException;
import medview.datahandling.MedViewDataHandler;
import medview.datahandling.NoSuchTermException;
import medview.datahandling.examination.ExaminationIdentifier;
import medview.datahandling.examination.ExaminationValueContainer;
import medview.datahandling.examination.NoSuchExaminationException;
import medview.datahandling.images.ExaminationImage;
import misc.foundation.MethodNotSupportedException;

public class ExaminationBuilder {
	Examination ex;
    MedViewDataHandler handler;
    
	public ExaminationBuilder(){
	}
	
	public Examination getExamination(ExaminationIdentifier eid){
        handler = MedViewDataHandler.instance();
        handler.setExaminationDataLocation("TestData.mvd");
		Examination ex = new Examination();
		String [] noVal = {};
		try {
			ExaminationValueContainer container = handler.getExaminationValueContainer(eid);
			try {
				String[] ageStringArr = {Integer.toString(handler.getAge(eid.getPID(), eid.getTime()))};
				ex.setAge(ageStringArr);
			} catch (InvalidPIDException e) {
				ex.setAge(noVal);
			}
			try {
				ex.setAllergy(container.getValues("Allergy"));
			} catch (NoSuchTermException e) {
				ex.setAllergy(noVal);	
			}
			try {
				ex.setBiopsySite(container.getValues("Biopsy-site"));
			} catch (NoSuchTermException e) {
				ex.setBiopsySite(noVal);	
			}
			try {
				ex.setDiagDef(container.getValues("Diag-def"));
			} catch (NoSuchTermException e) {
				ex.setDiagDef(noVal);	
			}
			try {
				ex.setDiagHist(container.getValues("Diag-hist"));
			} catch (NoSuchTermException e) {
				ex.setDiagHist(noVal);	
			}
			try {
				ex.setDiagTent(container.getValues("Diag-Tent"));
			} catch (NoSuchTermException e) {
				ex.setDiagTent(noVal);	
			}
			try {
				ex.setDisNow(container.getValues("Dis-now"));
			} catch (NoSuchTermException e) {
				ex.setDisNow(noVal);	
			}
			try {
				ex.setDisPast(container.getValues("Dis-past"));
			} catch (NoSuchTermException e) {
				ex.setDisPast(noVal);	
			}
			try {
				ex.setDrug(container.getValues("Drug"));
			} catch (NoSuchTermException e) {
				ex.setDrug(noVal);	
			}
			try {
				ex.setFactorNeg(container.getValues("Factor-neg"));
			} catch (NoSuchTermException e) {
				ex.setFactorNeg(noVal);	
			}
			try {
				ex.setFactorPos(container.getValues("Factor-pos"));
			} catch (NoSuchTermException e) {
				ex.setFactorPos(noVal);	
			}
			try {
				ex.setFamily(container.getValues("Family"));
			} catch (NoSuchTermException e) {
				ex.setFamily(noVal);	
			}
			try {
				String [] gender = {Integer.toString(handler.getGender(eid.getPID()))};
				ex.setGender(gender);
			} catch (InvalidPIDException e) {
				ex.setGender(noVal);
			}
			try {
				ex.setLesnOn(container.getValues("Lesn-on"));
			} catch (NoSuchTermException e) {
				ex.setLesnOn(noVal);	
			}
			try {
				ex.setLesnSite(container.getValues("Lesn-site"));
			} catch (NoSuchTermException e) {
				ex.setLesnSite(noVal);	
			}
			try {
				ex.setSkinPbl(container.getValues("Skin-pbl"));
			} catch (NoSuchTermException e) {
				ex.setSkinPbl(noVal);	
			}
			try {
				ex.setSmoke(container.getValues("Smoke"));
			} catch (NoSuchTermException e) {
				ex.setSmoke(noVal);	
			}
			try {
				ex.setSnuff(container.getValues("Snuff"));
			} catch (NoSuchTermException e) {
				ex.setSnuff(noVal);	
			}
			try {
				ex.setSymptNow(container.getValues("Sympt-now"));
			} catch (NoSuchTermException e) {
				ex.setSymptNow(noVal);	
			}
			try {
				ex.setSymptSite(container.getValues("Sympt-site"));
			} catch (NoSuchTermException e) {
				ex.setSymptSite(noVal);	
			}
			try {
				ex.setTreatType(container.getValues("Treat-type"));
			} catch (NoSuchTermException e) {
				ex.setTreatType(noVal);	
			}
			try {
				ex.setVasNow(container.getValues("Vas-now"));
			} catch (NoSuchTermException e) {
				ex.setVasNow(noVal);	
			}
            List<String> imagePaths = new ArrayList<String>();
			for(ExaminationImage img :handler.getImages(eid)){
            	try {
					imagePaths.add(img.getFile().toString());
				} catch (MethodNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            };
            ex.setImagePaths(imagePaths);
            ex.setExaminationID(eid.getExaminationIDString());			
		} catch (IOException e) {
		} catch (NoSuchExaminationException e) {
			
		}
		
		return ex;
	}
}