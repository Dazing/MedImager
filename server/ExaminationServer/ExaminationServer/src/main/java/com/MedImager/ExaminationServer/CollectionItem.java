package com.MedImager.ExaminationServer;

import java.io.IOException;
import java.util.Arrays;

public class CollectionItem {
	public int getCollectionID() {
		return collectionID;
	}

	public void setCollectionID(int collectionID) {
		this.collectionID = collectionID;
	}

	public String getExaminationID() {
		return examinationID;
	}

	public void setExaminationID(String examinationID) {
		this.examinationID = examinationID;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	private int collectionID;
	private int collectionitemID;
	private String examinationID;
	private int index;
	private String note;
	public CollectionItem(){}
	
	public Examination getExamination() throws IOException{
		ExaminationIDParser eidParser = new ExaminationIDParser();
		Examination ex = eidParser.getMoreFromExamination(examinationID);
		ex.setImagePaths(Arrays.asList(ex.getImagePaths().get(index)));
		return ex;
	}

	public int getCollectionitemID() {
		return collectionitemID;
	}

	public void setCollectionitemID(int collectionitemID) {
		this.collectionitemID = collectionitemID;
	}

}
