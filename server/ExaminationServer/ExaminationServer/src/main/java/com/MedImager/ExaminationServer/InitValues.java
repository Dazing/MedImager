package com.MedImager.ExaminationServer;

import java.util.List;
import java.util.Map;

public class InitValues {

	private List<String> treatTypes;
	private Map<String, Integer> searchableValues;
	
	public Map<String, Integer> getSearchableValues() {
		return searchableValues;
	}
	public void setSearchableValues(Map<String, Integer> searchableValues) {
		this.searchableValues = searchableValues;
	}
	public List<String> getTreatTypes() {
		return treatTypes;
	}
	public void setTreatTypes(List<String> treatTypes) {
		this.treatTypes = treatTypes;
	}
	public InitValues(){
	}
}
