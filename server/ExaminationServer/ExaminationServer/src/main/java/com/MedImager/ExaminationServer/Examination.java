package com.MedImager.ExaminationServer;

import java.util.List;

import medview.datahandling.images.ExaminationImage;

/**
 * Created by marcus on 2017-02-10.
 */
public class Examination {
	public String[] getAge() {
		return age;
	}

	public void setAge(String[] age) {
		this.age = age;
	}

	public String[] getAllergy() {
		return allergy;
	}

	public void setAllergy(String[] allergy) {
		this.allergy = allergy;
	}

	public String[] getBiopsySite() {
		return biopsySite;
	}

	public void setBiopsySite(String[] biopsySite) {
		this.biopsySite = biopsySite;
	}

	public String[] getDiagDef() {
		return diagDef;
	}

	public void setDiagDef(String[] diagDef) {
		this.diagDef = diagDef;
	}

	public String[] getDiagHist() {
		return diagHist;
	}

	public void setDiagHist(String[] diagHist) {
		this.diagHist = diagHist;
	}

	public String[] getDiagTent() {
		return diagTent;
	}

	public void setDiagTent(String[] diagTent) {
		this.diagTent = diagTent;
	}

	public String[] getDisNow() {
		return disNow;
	}

	public void setDisNow(String[] disNow) {
		this.disNow = disNow;
	}

	public String[] getDisPast() {
		return disPast;
	}

	public void setDisPast(String[] disPast) {
		this.disPast = disPast;
	}

	public String[] getDrug() {
		return drug;
	}

	public void setDrug(String[] drug) {
		this.drug = drug;
	}

	public String[] getFactorNeg() {
		return factorNeg;
	}

	public void setFactorNeg(String[] factorNeg) {
		this.factorNeg = factorNeg;
	}

	public String[] getFactorPos() {
		return factorPos;
	}

	public void setFactorPos(String[] factorPos) {
		this.factorPos = factorPos;
	}

	public String[] getFamily() {
		return family;
	}

	public void setFamily(String[] family) {
		this.family = family;
	}

	public String[] getGender() {
		return gender;
	}

	public void setGender(String[] gender) {
		this.gender = gender;
	}

	public String[] getLesnOn() {
		return lesnOn;
	}

	public void setLesnOn(String[] lesnOn) {
		this.lesnOn = lesnOn;
	}

	public String[] getLesnSite() {
		return lesnSite;
	}

	public void setLesnSite(String[] lesnSite) {
		this.lesnSite = lesnSite;
	}

	public String[] getSkinPbl() {
		return skinPbl;
	}

	public void setSkinPbl(String[] skinPbl) {
		this.skinPbl = skinPbl;
	}

	public String[] getSmoke() {
		return smoke;
	}

	public void setSmoke(String[] smoke) {
		this.smoke = smoke;
	}

	public String[] getSnuff() {
		return snuff;
	}

	public void setSnuff(String[] snuff) {
		this.snuff = snuff;
	}

	public String[] getSymptNow() {
		return symptNow;
	}

	public void setSymptNow(String[] symptNow) {
		this.symptNow = symptNow;
	}

	public String[] getSymptSite() {
		return symptSite;
	}

	public void setSymptSite(String[] symptSite) {
		this.symptSite = symptSite;
	}

	public String[] getTreatType() {
		return treatType;
	}

	public void setTreatType(String[] treatType) {
		this.treatType = treatType;
	}

	public String[] getVasNow() {
		return vasNow;
	}

	public void setVasNow(String[] vasNow) {
		this.vasNow = vasNow;
	}

	public List<String> getImagePaths() {
		return imagePaths;
	}

	public void setImagePaths(List<String> imagePaths) {
		this.imagePaths = imagePaths;
	}

    public String getExaminationID() {
		return examinationID;
	}

	public void setExaminationID(String examinationID) {
		this.examinationID = examinationID;
	}
	
	private String [] age;
	private String [] allergy;
	private String [] biopsySite;
	private String [] diagDef;
	private String [] diagHist;
	private String [] diagTent;
	private String [] disNow;
	private String [] disPast;
	private String [] drug;
	private String [] factorNeg;
	private String [] factorPos;
	private String [] family;
	private String [] gender;
	private String [] lesnOn;
	private String [] lesnSite;
	private String [] skinPbl;
	private String [] smoke;
	private String [] snuff;
	private String [] symptNow;
	private String [] symptSite;
	private String [] treatType;
	private String [] vasNow;
	private List<String> imagePaths;
	private String examinationID;


	public Examination(){}
    

	
	
}