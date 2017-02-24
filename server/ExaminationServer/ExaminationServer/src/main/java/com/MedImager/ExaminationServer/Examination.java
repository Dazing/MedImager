package com.MedImager.ExaminationServer;

import java.util.List;

import medview.datahandling.images.ExaminationImage;

/**
 * Created by marcus on 2017-02-10.
 */
public class Examination {
    private int AGE;
    private String ALLERGY;
    private List<String> imagePaths;

    public Examination(){}
    

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


	public List<String> getImagePaths() {
		return imagePaths;
	}


	public void setImagePaths(List<String> imagePaths) {
		this.imagePaths = imagePaths;
	}
}