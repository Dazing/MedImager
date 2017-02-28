package com.MedImager.ExaminationServer;

import java.util.HashMap;

/*
 * Class that converts real terms such as "Medication" & "Disease" to their
 * MedView equivalents
 */
public class TermTranslator {
	private static HashMap<String, String> medviewTerms = new HashMap<>();
	
	public static String getMedviewTerm(String realTerm){
		return medviewTerms.get(realTerm);
	}
}
