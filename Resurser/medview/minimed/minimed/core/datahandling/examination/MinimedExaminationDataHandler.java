package minimed.core.datahandling.examination;

import java.io.IOException;

import minimed.core.datahandling.PatientIdentifier;
import minimed.core.datahandling.examination.tree.Tree;
import misc.foundation.ProgressNotifiable;

/*
 * Ska kopiera konceptet med en Facade, från MedviewDataHandler.
 * 
 * Är front för funktioner i:
 * ExaminationDataHandler
 * MedformTemplateReader
 * ParsedTermDataHandler
 */
public class MinimedExaminationDataHandler implements ExaminationDataHandler {

	/*
	 * Constructor
	 */
	public MinimedExaminationDataHandler() {

	}
	
	/*
	 * The following methods implements the ExaminationDataHandler interface
	 */
	
	public void shuttingDown() {
		// TODO Auto-generated method stub
		
	}

	public PatientIdentifier[] getPatients() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public PatientIdentifier[] getPatients(ProgressNotifiable notifiable) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getExaminationCount() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getExaminationCount(PatientIdentifier pid) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public ExaminationIdentifier[] getExaminations(PatientIdentifier pid) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public ExaminationIdentifier[] refreshExaminations() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public ExaminationIdentifier[] refreshExaminations(long sinceTime) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getImageCount(PatientIdentifier pid) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public ExaminationValueContainer[] getAllExaminationValueContainers(ProgressNotifiable not) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public ExaminationValueContainer[] getAllExaminationValueContainers(ProgressNotifiable not, int hint) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id) throws IOException, NoSuchExaminationException {
		// TODO Auto-generated method stub
		return null;
	}

	public ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id, int hint) throws IOException, NoSuchExaminationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getExaminationDataLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getExaminationDataLocationID() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setExaminationDataLocation(String loc) {
		// TODO Auto-generated method stub
		
	}

	public boolean isExaminationDataLocationValid() {
		// TODO Auto-generated method stub
		return false;
	}

	public int saveExamination(Tree tree) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	

}
