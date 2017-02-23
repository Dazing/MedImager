package com.MedImager.ExaminationServer;

/**
 * Created by marcus on 2017-02-11.
 */

import medview.common.data.MedViewUtilities;
import medview.datahandling.InvalidPIDException;
import medview.datahandling.MedViewDataHandler;
import medview.datahandling.NoSuchTermException;
import medview.datahandling.PatientIdentifier;
import medview.datahandling.examination.ExaminationIdentifier;
import medview.datahandling.examination.ExaminationValueContainer;
import medview.datahandling.examination.NoSuchExaminationException;
import java.io.IOException;
import java.util.ArrayList;


public class SearchTermParser {
    private String searchTerm;
    ArrayList<String> allTerms = new ArrayList<String>();
    public SearchTermParser(String searchTerm){
        this.searchTerm = searchTerm;
    }

    public ArrayList<Examination> getResultList(){
        return findTerm(searchTerm);
    }

    /*
    NOT COMPLETE
     */
    private ArrayList<Examination> findTerm(String term) {
        MedViewUtilities utilObj = new MedViewUtilities();
        MedViewDataHandler handler = MedViewDataHandler.instance();
        
        handler.setExaminationDataLocation("TestData.mvd");
        ArrayList<Examination> resultList = new ArrayList<Examination>();
        try {
            for (PatientIdentifier pid : handler.getPatients()) {
                for (ExaminationIdentifier eid : handler.getExaminations(pid)) {
                    ExaminationValueContainer container = handler.getExaminationValueContainer(eid);
                    try {
                        Examination examination = new Examination();
                        for (String s : container.getValues("Allergy")) {
                            allTerms.add(s);
                            if(term == s){
                                examination.setAGE(handler.getAge(pid, eid.getTime()));
                                examination.setALLERGY(s);
                                examination.setImages(handler.getImages(eid));
                                resultList.add(examination);
                            }
                        }
                    } catch (NoSuchTermException e) {
                    }
                }
            }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidPIDException e) {
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