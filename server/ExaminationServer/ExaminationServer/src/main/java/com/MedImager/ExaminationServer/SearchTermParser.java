package com.MedImager.ExaminationServer;

/**
 * Created by marcus on 2017-02-11.
 */

import medview.datahandling.MedViewDataHandler;
import medview.datahandling.NoSuchTermException;
import medview.datahandling.PatientIdentifier;
import medview.datahandling.examination.ExaminationIdentifier;
import medview.datahandling.examination.ExaminationValueContainer;
import medview.datahandling.examination.NoSuchExaminationException;
import misc.foundation.MethodNotSupportedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SearchTermParser {
    private String searchTerm;
    ArrayList<String> allTerms = new ArrayList<String>();
    MedViewDataHandler handler;
    SearchFilter filter;
    
    //Hashmap with translations from variable names in Examination class -> term names in MedView
    public static Map<String, String> translations;
    
    public SearchTermParser(SearchFilter filter){
        this.searchTerm = filter.getSearchURL();
        this.filter = filter;
        
        handler = MedViewDataHandler.instance();
        handler.setExaminationDataLocation("TestData.mvd");
        
        //Incomplete at the moment
        translations = new HashMap<>();
        translations.put("allergy", "Allergy");
        translations.put("biopsySite", "Biopsy-site");
        translations.put("diagDef", "Diag-def");
        translations.put("diagHist", "diagHist");
        translations.put("diagTent", "diagTent");
        translations.put("disNow", "Dis-now");
        translations.put("disPast", "Dis-past");
    }

    public ArrayList<Examination> getResultList() throws MethodNotSupportedException{
        return findTerm(searchTerm);
    }
    
    public ArrayList<Examination> getResultListWithFilter() throws MethodNotSupportedException{
        return findTerm(filter);
    }
    
    public ArrayList<Examination> getResultListWithFilter2() throws MethodNotSupportedException{
        return findTerm2(filter);
    }

    /*
    NOT COMPLETE
     */
    private ArrayList<Examination> findTerm(String term) throws MethodNotSupportedException {
        ArrayList<Examination> resultList = new ArrayList<Examination>();
        try {
            for (PatientIdentifier pid : handler.getPatients()) {
                for (ExaminationIdentifier eid : handler.getExaminations(pid)) {
                	ExaminationValueContainer container = handler.getExaminationValueContainer(eid);
                    try {
                        for (String s : container.getValues("Allergy")) {
                            allTerms.add(s);
                            if(term.equals(s)){
                        		ExaminationBuilder ex = new ExaminationBuilder();
                        		resultList.add(ex.getExamination(eid));
                            }
                        }
                    } catch (NoSuchTermException e) {
                    }
                }
            }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchExaminationException e) {
                e.printStackTrace();
            }
        if(resultList.isEmpty()) {
            for (String match : closeMatch(term)) {
                System.out.println("Inga resultat, menade du: " + match + ", ");
            }
        }
        return resultList;

    }
    
    private ArrayList<Examination> findTerm(SearchFilter filter) throws MethodNotSupportedException {
        ArrayList<Examination> resultList = new ArrayList<Examination>();
        try {
            for (PatientIdentifier pid : handler.getPatients()) {
                for (ExaminationIdentifier eid : handler.getExaminations(pid)) {
                	if(filter.filterSatisfied(eid)){
                		ExaminationBuilder exBuilder = new ExaminationBuilder();
                		Examination ex = exBuilder.getExamination(eid);
                		if(ex.getImagePaths().size()!=0){
                			resultList.add(ex);
                		}
                	}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }
    
    private ArrayList<Examination> findTerm2(SearchFilter filter) throws MethodNotSupportedException {
        ArrayList<Examination> resultList = new ArrayList<Examination>();
        try {
            for (PatientIdentifier pid : handler.getPatients()) {
                for (ExaminationIdentifier eid : handler.getExaminations(pid)) {
                	if(filter.filterSatisfied2(eid)){
                		ExaminationBuilder exBuilder = new ExaminationBuilder();
                		Examination ex = exBuilder.getExamination(eid);
                		if(ex.getImagePaths().size()!=0){
                			resultList.add(ex);
                		}
                	}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }
    
    /*
    * Calculates Edit Distance.
    * Is called when no results were found in findTerm(String)
    *
    * An array with matches of equal distances are returned.
    *
    * CAN BE IMPROVED UPON!
    * */
    private ArrayList<String> closeMatch(String s){
        int lowestDistance = s.length();
        ArrayList<String> closestMatches = new ArrayList<String>();
        for(String word:allTerms){
            int distance = Math.abs(s.length()-word.length());
            for(int i = 0; i<s.length()&&i<word.length();i++){
                if(word.charAt(i)!=s.charAt(i)){
                    distance++;
                }
            }
            if (distance<lowestDistance){
                lowestDistance = distance;
                closestMatches = new ArrayList<String>();
                closestMatches.add(word);
            }else if(distance==lowestDistance && !closestMatches.contains(word)){
                closestMatches.add(word);
            }

        }
        return closestMatches;
    }
}