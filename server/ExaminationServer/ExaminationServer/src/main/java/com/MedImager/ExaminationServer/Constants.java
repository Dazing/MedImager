package com.MedImager.ExaminationServer;

public class Constants {
	public static final String EXAMINATION_DATA_LOCATION = "TestData.mvd";
	public static final String[] relevantTerms = {
			"Age", "Allergy", "Biopsy-site", "Diag-def", "Diag-hist", 
			"Diag-Tent", "Dis-now", "Dis-past", "Drug", "Factor-neg", 
			"Factor-pos", "Family", "Gender", "Lesn-on", "Lesn-site", 
			"Skin-pbl", "Smoke", "Snuff", "Sympt-now", "Sympt-site", 
			"Treat-type", "Vas-now"
			};
	public static final String[] primaryRelevantTerms = {
			"Diag-def", "Dis-now", "Sympt-now", "Sympt-site"
			};
}
