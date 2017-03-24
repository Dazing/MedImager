package com.MedImager.ExaminationServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;

import javax.swing.tree.DefaultTreeCellEditor.EditorContainer;

import medview.datahandling.InvalidPIDException;
import medview.datahandling.MedViewDataHandler;
import medview.datahandling.NoSuchTermException;
import medview.datahandling.examination.ExaminationIdentifier;
import medview.datahandling.examination.ExaminationValueContainer;
import medview.datahandling.examination.NoSuchExaminationException;

/* 
 * Class that helps in filtering when searching in the database
 */
public class SearchFilter {
	private List<String> values;
	private List<String> terms;
	private int ageLower;
	private int ageUpper;
	private String gender;
	private Boolean smoke;
	private Boolean snuff;
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Boolean getSmoke() {
		return smoke;
	}

	public void setSmoke(Boolean smoke) {
		this.smoke = smoke;
	}

	public Boolean getSnuff() {
		return snuff;
	}

	public void setSnuff(Boolean snuff) {
		this.snuff = snuff;
	}

	private String searchURL;
	
	public SearchFilter(){
	}
	
	public SearchFilter(List<String> values, List<String> terms, String ageLower, String ageUpper, String gender, Boolean smoke, Boolean snuff){
		setValues(values);
		setTerms(terms);
		if(ageLower != null){
			this.ageLower = Integer.parseInt(ageLower);
		}
		if(ageUpper != null){
			this.ageUpper = Integer.parseInt(ageUpper);
		}
		if(smoke != null){
			this.smoke = smoke;
		}
		if(snuff != null){
			this.snuff = snuff;
		}
	}
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	public void addTerm(String term){
		terms.add(term);
	}
	public List<String> getTerms(){
		return terms;
	}
	public void setTerms(List<String> terms) {
		this.terms = terms;
	}
	public int getAgeLower() {
		return ageLower;
	}
	public void setAgeLower(int ageLower) {
		this.ageLower = ageLower;
	}
	public int getAgeUpper() {
		return ageUpper;
	}
	public void setAgeUpper(int ageUpper) {
		this.ageUpper = ageUpper;
	}
	public String getSearchURL() {
		return searchURL;
	}
	public void setSearchURL(String searchURL) {
		this.searchURL = searchURL;
	}
	
	public Boolean filterSatisfied(ExaminationIdentifier eid){
		if(values.size() == 0){
			if(!valueSatisfied(null, eid)){
				return false;
			}
		}else{
			for(String value : values){
				if(!valueSatisfied(value, eid)){
					return false;
				}
			}
		}
		return true;
	}
	
	public Boolean valueSatisfied(String value, ExaminationIdentifier eid){
		MedViewDataHandler handler = MedViewDataHandler.instance();
		handler.setExaminationDataLocation(Constants.EXAMINATION_DATA_LOCATION);
		
		try {
			if(ageLower > 0 && handler.getAge(eid.getPID(), eid.getTime()) < ageLower){
				return false;
			}
			if(ageUpper > 0 && handler.getAge(eid.getPID(), eid.getTime()) > ageUpper){
				return false;
			}
			
			ExaminationValueContainer evc = handler.getExaminationValueContainer(eid);
			
			/*
			 * Gender implementation is preliminary, wait for real database before deploying
			 */
//			if(gender.equals("Female") && !Arrays.asList(evc.getValues("Sex")).contains("Female") ||
//					gender.equals("Male") && !Arrays.asList(evc.getValues("Sex")).contains("Male")){
//				return false;
//			}
			
			if(smoke != null && Arrays.asList(evc.getTermsWithValues()).contains("Smoke")){
				if(smoke == true && Arrays.asList(evc.getValues("Smoke")).contains("Nej") ||
						smoke == false && !Arrays.asList(evc.getValues("Smoke")).contains("Nej")){
					return false;
				}
			}else if(smoke != null && !Arrays.asList(evc.getTermsWithValues()).contains("Smoke")){
				return false;
			}
			
			if(snuff != null && Arrays.asList(evc.getTermsWithValues()).contains("Snuff")){
				if(snuff == true && Arrays.asList(evc.getValues("Snuff")).contains("Nej") ||
						snuff == false && !Arrays.asList(evc.getValues("Snuff")).contains("Nej")){
					return false;
				}
			}else if(snuff != null && !Arrays.asList(evc.getTermsWithValues()).contains("Snuff")){
				return false;
			}
			
			/*
			 * If no values are passed from the user, only the above checks are relevant for the search.
			 * That is, if the examination satisifies the user's requirements of age and whether the patient
			 * smokes and/or snuffs, the examination is deemed eligible to be sent to the user and thus no 
			 * further action needs to be taken.
			 */
			if(values.size() != 0){
				boolean valueInExamination = false;
				
				/*
				 * Search through primary terms for a match
				 */
				for(String term : Arrays.asList(Constants.primaryRelevantTerms)){
					if(Arrays.asList(evc.getTermsWithValues()).contains(term)){
						List<String> termValues = new ArrayList<>(Arrays.asList(evc.getValues(term)));
						if(termValues.contains(value)){
							valueInExamination = true;
						}
					}
				}
				
				/*
				 * Search through extra terms (if any) specified by the user
				 */
				if(valueInExamination == false && !terms.isEmpty()){
					for(String term : terms){
						if(Arrays.asList(evc.getTermsWithValues()).contains(term)){
							List<String> termValues = new ArrayList<>(Arrays.asList(evc.getValues(term)));
							if(termValues.contains(value)){
								valueInExamination = true;
							}
						}
					}
				}
				
				if(valueInExamination == false){
					return false;
				}
			}
			
			
//			/*
//			 * Check for what the user specified about smoking and compare it to
//			 * what information the examination has about smoking
//			 */
//			if(smokes == null){
//				return true;
//			}else{
//				if(!Arrays.asList(evc.getTermsWithValues()).contains("Smoke")){
//					return false;
//				}else{
//					return (Arrays.asList(evc.getValues("Smoke")).contains("Nej") && smokes == false) ||
//					(!Arrays.asList(evc.getValues("Smoke")).contains("Nej") && smokes == true);
//				}
//			}
		} catch (IOException | NoSuchExaminationException | NoSuchTermException | InvalidPIDException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	/*
	 * Old version, kept for now for possible future reference
	 */
	public Boolean valueSatisfiedOld(String value, ExaminationIdentifier eid){
		MedViewDataHandler handler = MedViewDataHandler.instance();
		handler.setExaminationDataLocation(Constants.EXAMINATION_DATA_LOCATION);
		
		try {
			ExaminationValueContainer evc = handler.getExaminationValueContainer(eid);
			if(terms.isEmpty()){
				boolean examinationContainsValue = false;
				for(String term : evc.getTermsWithValues()){
					if(Arrays.asList(evc.getValues(term)).contains(value)){
						examinationContainsValue = true;
					}
				}
				if(examinationContainsValue == false){
					return false;
				}
			}else{
				for(String term : terms){
					if(!Arrays.asList(evc.getTermsWithValues()).contains(term)){
						return false;
					}else{
						List<String> termValues = new ArrayList<>(Arrays.asList(evc.getValues(term)));
						int size = termValues.size();
						String a = termValues.get(0);
						if(!termValues.contains(value)){
							return false;
						}
					}
				}
			}
			if(ageLower > 0 && handler.getAge(eid.getPID(), eid.getTime()) < ageLower){
				return false;
			}
			if(ageUpper > 0 && handler.getAge(eid.getPID(), eid.getTime()) > ageUpper){
				return false;
			}
			
			/*
			 * Check for what the user specified about smoking and compare it to
			 * what information the examination has about smoking
			 */
//			if(smokes == null){
//				return true;
//			}else{
//				if(!Arrays.asList(evc.getTermsWithValues()).contains("Smoke")){
//					return false;
//				}else{
//					return (Arrays.asList(evc.getValues("Smoke")).contains("Nej") && smokes == false) ||
//					(!Arrays.asList(evc.getValues("Smoke")).contains("Nej") && smokes == true);
//				}
//			}
		} catch (IOException | NoSuchExaminationException | NoSuchTermException | InvalidPIDException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
