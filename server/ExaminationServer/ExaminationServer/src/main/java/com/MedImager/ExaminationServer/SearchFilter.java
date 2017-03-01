package com.MedImager.ExaminationServer;

import java.util.ArrayList;
import java.util.List;

/* 
 * Class that helps in filtering when searching in the database
 */
public class SearchFilter {
	private int ageLower;
	private int ageUpper;
	private boolean smokes;
	private List<String> terms;
	
	public SearchFilter(){
		terms = new ArrayList<>();
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
	
}
