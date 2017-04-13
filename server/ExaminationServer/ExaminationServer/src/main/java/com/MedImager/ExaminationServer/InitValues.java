package com.MedImager.ExaminationServer;

import java.util.List;
import java.util.Map;

public class InitValues {
	
	public List<String> getAllergy() {
		return allergy;
	}
	public void setAllergy(List<String> allergy) {
		this.allergy = allergy;
	}
	public List<String> getBiopsySite() {
		return biopsySite;
	}
	public void setBiopsySite(List<String> biopsySite) {
		this.biopsySite = biopsySite;
	}
	public List<String> getDiagDef() {
		return diagDef;
	}
	public void setDiagDef(List<String> diagDef) {
		this.diagDef = diagDef;
	}
	public List<String> getDiagHist() {
		return diagHist;
	}
	public void setDiagHist(List<String> diagHist) {
		this.diagHist = diagHist;
	}
	public List<String> getDiagTent() {
		return diagTent;
	}
	public void setDiagTent(List<String> diagTent) {
		this.diagTent = diagTent;
	}
	public List<String> getDisNow() {
		return disNow;
	}
	public void setDisNow(List<String> disNow) {
		this.disNow = disNow;
	}
	public List<String> getDisPast() {
		return disPast;
	}
	public void setDisPast(List<String> disPast) {
		this.disPast = disPast;
	}
	public List<String> getDrug() {
		return drug;
	}
	public void setDrug(List<String> drug) {
		this.drug = drug;
	}
	public List<String> getFactorNeg() {
		return factorNeg;
	}
	public void setFactorNeg(List<String> factorNeg) {
		this.factorNeg = factorNeg;
	}
	public List<String> getFactorPos() {
		return factorPos;
	}
	public void setFactorPos(List<String> factorPos) {
		this.factorPos = factorPos;
	}
	public List<String> getFamily() {
		return family;
	}
	public void setFamily(List<String> family) {
		this.family = family;
	}
	public List<String> getGender() {
		return gender;
	}
	public void setGender(List<String> gender) {
		this.gender = gender;
	}
	public List<String> getLesnOn() {
		return lesnOn;
	}
	public void setLesnOn(List<String> lesnOn) {
		this.lesnOn = lesnOn;
	}
	public List<String> getLesnSite() {
		return lesnSite;
	}
	public void setLesnSite(List<String> lesnSite) {
		this.lesnSite = lesnSite;
	}
	public List<String> getSkinPbl() {
		return skinPbl;
	}
	public void setSkinPbl(List<String> skinPbl) {
		this.skinPbl = skinPbl;
	}
	public List<String> getSmoke() {
		return smoke;
	}
	public void setSmoke(List<String> smoke) {
		this.smoke = smoke;
	}
	public List<String> getSnuff() {
		return snuff;
	}
	public void setSnuff(List<String> snuff) {
		this.snuff = snuff;
	}
	public List<String> getSymptNow() {
		return symptNow;
	}
	public void setSymptNow(List<String> symptNow) {
		this.symptNow = symptNow;
	}
	public List<String> getSymptSite() {
		return symptSite;
	}
	public void setSymptSite(List<String> symptSite) {
		this.symptSite = symptSite;
	}
	public List<String> getTreatType() {
		return treatType;
	}
	public void setTreatType(List<String> treatType) {
		this.treatType = treatType;
	}
	public List<String> getVasNow() {
		return vasNow;
	}
	public void setVasNow(List<String> vasNow) {
		this.vasNow = vasNow;
	}
	private List<String> allergy;
	private List<String> biopsySite;
	private List<String> diagDef;
	private List<String> diagHist;
	private List<String> diagTent;
	private List<String> disNow;
	private List<String> disPast;
	private List<String> drug;
	private List<String> factorNeg;
	private List<String> factorPos;
	private List<String> family;
	private List<String> gender;
	private List<String> lesnOn;
	private List<String> lesnSite;
	private List<String> skinPbl;
	private List<String> smoke;
	private List<String> snuff;
	private List<String> symptNow;
	private List<String> symptSite;
	private List<String> treatType;
	private List<String> vasNow;
	private Map<String, Integer> searchableValues;
	
	public Map<String, Integer> getSearchableValues() {
		return searchableValues;
	}
	public void setSearchableValues(Map<String, Integer> searchableValues) {
		this.searchableValues = searchableValues;
	}
	public InitValues(){
	}
}
