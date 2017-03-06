package com.MedImager.ExaminationServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;

import medview.datahandling.MedViewDataHandler;
import medview.datahandling.NoSuchTermException;
import medview.datahandling.examination.ExaminationIdentifier;
import medview.datahandling.examination.ExaminationValueContainer;
import medview.datahandling.examination.NoSuchExaminationException;

/* 
 * Class that helps in filtering when searching in the database
 */
public class SearchFilter {
	
	MedViewDataHandler handler;
	
	private int ageLower;
	private int ageUpper;
	private Boolean smokes;
	private List<String> terms;
	private String searchURL;
	
	
	public SearchFilter(){
		terms = new ArrayList<>();
		handler = MedViewDataHandler.instance();
		handler.setExaminationDataLocation(Constants.EXAMINATION_DATA_LOCATION);
	}
	public void addTerm(String term){
		terms.add(term);
	}
	public List<String> getTerms(){
		return terms;
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
	public boolean isSmokes() {
		return smokes;
	}
	public void setSmokes(boolean smokes) {
		this.smokes = smokes;
	}
	public String getSearchURL() {
		return searchURL;
	}
	public void setSearchURL(String searchURL) {
		this.searchURL = searchURL;
	}
	
	public Boolean filterSatisfied(ExaminationIdentifier eid){
		try {
			ExaminationValueContainer evc = handler.getExaminationValueContainer(eid);
			
			/*
			 * Make sure that the examination has values for all the terms that the
			 * user filtered for
			 */
			for(String term : terms){
				if(!Arrays.asList(evc.getTermsWithValues()).contains(term)){
					return false;
				}
			}
			
			/*
			 * Check for what the user specified about smoking and compare it to
			 * what information the examination has about smoking
			 */
			if(smokes == null){
				return true;
			}else{
				if(!Arrays.asList(evc.getTermsWithValues()).contains("Smoke")){
					return false;
				}else{
					return (Arrays.asList(evc.getValues("Smoke")).contains("Nej") && smokes == false) ||
					(!Arrays.asList(evc.getValues("Smoke")).contains("Nej") && smokes == true);
				}
			}
			
		} catch (IOException | NoSuchExaminationException | NoSuchTermException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
