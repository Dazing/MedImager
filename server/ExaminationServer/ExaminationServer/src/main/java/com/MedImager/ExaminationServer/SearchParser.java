package com.MedImager.ExaminationServer;

/* 
 * Class that takes the resource url sent from the client and parses
 * out the values contained within to create a SearchFilter
 */
public class SearchParser {
	public static SearchFilter createSearchFilter(String searchURL) {
		SearchFilter filter = new SearchFilter();
		filter.setSearchURL(searchURL);
		
		/* 
		 * Next parse the resourceUrl. If it contains e.g. 
		 * "Term=Allergy#AgeLower=20" then do:
		 * 
		 * filter.addTerm("Allergy"); 
		 * filter.setAgeLower(20);
		 */
		
		return filter;
	}
	
}
