package com.company;
public class Main {

    public static String path;
    public static void main(String[] args) {
        path = args[0];
        SearchTermParser search = new SearchTermParser("Pollen");
        for(Examination ex : search.getResultList()){
            System.out.println("Ålder: " + ex.getAGE() + " Allergi: " + ex.getALLERGY());
        }
    }
}
