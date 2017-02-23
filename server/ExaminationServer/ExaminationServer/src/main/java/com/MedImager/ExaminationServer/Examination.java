package com.MedImager.ExaminationServer;

import medview.datahandling.images.ExaminationImage;

/**
 * Created by marcus on 2017-02-10.
 */
public class Examination {
    private int AGE;
    private String ALLERGY;
    private ExaminationImage[] images;

    public Examination(){}
    
    public ExaminationImage[] getImages(){
    	return images;
    }
    
    public void setImages(ExaminationImage[] images){
    	this.images = images;
    }

    public int getAGE() {
        return AGE;
    }

    public void setAGE(int AGE) {
        this.AGE = AGE;
    }

    public String getALLERGY() {
        return ALLERGY;
    }

    public void setALLERGY(String ALLERGY) {
        this.ALLERGY = ALLERGY;
    }
}