/*
 * $Id: CachingTreeFileHandler.java,v 1.5 2004/01/27 08:13:50 erichson Exp $
 *
 * Created on September 10, 2002, 11:04 AM
 *
 * $Log: CachingTreeFileHandler.java,v $
 * Revision 1.5  2004/01/27 08:13:50  erichson
 * Updated to new datahandling (use PatientIdentifier)
 *
 */

package medview.datahandling.examination.tree;

import java.util.*; // Hashtable, date
import java.io.*; // File, ioexception
import java.text.*; // Parseexception

import medview.datahandling.PatientIdentifier;
import medview.datahandling.examination.*; // Examinationidentifier

/**
 * This extension of TreeFileHandler works using the assumption that the tree file database will not change during
 * a session. Thus it will load everything only once and keep it cached.
 * It caches everything except individual attributeshelfs (which would consume too much memory)
 * 
 * @author  erichson
 */
public class CachingTreeFileHandler extends medview.datahandling.examination.tree.TreeFileHandler {
    
    
    private Hashtable treefilepathToDateTable = new Hashtable();
    private Hashtable treefilepathToPcodeTable = new Hashtable();
    private Hashtable pcodeToExaminationVectorTable = new Hashtable();
    private Hashtable pcodeToImageCountTable = new Hashtable();
    private Hashtable pcodeToTreefilesVectorTable = new Hashtable();
    
    private PatientIdentifier[] patients = null;
    
    private File[] treeFiles = null;
    
    /** Creates a new instance of CachingTreeFileHandler */
    public CachingTreeFileHandler() {
        super();
    }
    
    public String getPCode(File treeFile) throws ValueMissingException {
        String treeFilePath = treeFile.getPath();
        String pcode = (String) treefilepathToPcodeTable.get(treeFilePath);
        if (pcode == null) {
            pcode = super.getPCode(treeFile);
            treefilepathToPcodeTable.put(treeFilePath,pcode);
        }
        return pcode;
    }
    
    /** Extract the date (when the examination was made) from a certain treefile
     * @return the extracted date
     * @param treeFile the file to extract the date from
     * @throws ParseException thrown if the date extraction fails
     */
    protected Date getDate(File treeFile) throws ValueMissingException {
        String treeFilePath = treeFile.getPath();
        Date date = (Date) treefilepathToDateTable.get(treeFilePath);
        if (date == null) {
            date = super.getDate(treeFile);
            treefilepathToDateTable.put(treeFilePath,date);
        }        
        return date;        
    }
    
    /** Get the current location of the examination data (the tree files)
     * @return the current location of the examination data (the tree files)
     */
    //public String getExaminationDataLocation() {
    //}
    
    /** Fetches an ExaminationValueContainer for the specified examination
     * @param id The identifier of the examination to fetch
     * @throws NoSuchExaminationException thrown when trying to access an examination that doesn't exist in the current tree file database
     * @throws IOException thrown when accessing the tree file database fails
     * @return an ExaminationValueContainer for the specified examination
     */
  //  public medview.datahandling.examination.ExaminationValueContainer getExaminationValueContainer(ExaminationIdentifier id) throws NoSuchExaminationException, IOException {
//        return super(id);
    //}
    
    /** Fetches an ExaminationValueContainer for the specified examination
     * @param p_code the p_code for the patient whose examination to acess
     * @param year the year the examination was made
     * @param day the day the examination was made
     * @param hour the hour the examination was made
     * @param minute the minute the examination was made
     * @param month the month the examination was made. Note: Has value 1-12 (not 0-11 as in GregorianCalendar!)
     * @throws NoSuchExaminationException thrown when trying to access an examination that doesn't exist in the current tree file database
     * @throws IOException thrown when accessing the tree file database fails
     * @return an ExaminationValueContainer for the specified examination
     */
    //public medview.datahandling.examination.ExaminationValueContainer getExaminationValueContainer(String p_code, int year, int month, int day, int hour, int minute) throws NoSuchExaminationException, IOException {
   // }
    
    /** Get unique identifiers for all examinations associated with a patient
     * @return an array of ExaminationIdentifiers
     * @param patientCode the p_code for the patient
     * @throws IOException When access to the tree files fails
     */
    public ExaminationIdentifier[] getExaminations(String patientCode) throws IOException {
        Vector v = (Vector) pcodeToExaminationVectorTable.get(patientCode);
        ExaminationIdentifier[] examinationIDs;
        if (v == null) {
            v = new Vector();
            examinationIDs = super.getExaminations(patientCode);
            for (int i = 0; i < examinationIDs.length; i++) {
                v.add(examinationIDs[i]);
            }
            pcodeToExaminationVectorTable.put(patientCode,v);    
            return examinationIDs;
        } else {
            examinationIDs = new ExaminationIdentifier[v.size()];
            examinationIDs = (ExaminationIdentifier[]) v.toArray(examinationIDs);
            return examinationIDs;
        }                
    }
    
    /** Get the total amount of images associated with a patient
     * @param patientCode The p_code for the patient whose images to count
     * @throws IOException thrown when access to the tree files fails
     * @return the total amount of images associated with the patient
     */
    public int getImageCount(String patientCode) throws IOException {
        Integer count = (Integer) pcodeToImageCountTable.get(patientCode);
        if (count == null) {
            int intCount = super.getImageCount(patientCode);
            count = new Integer(intCount);
            pcodeToImageCountTable.put(patientCode,count);
            return intCount;
        } else {
            return count.intValue();
        }
    }
    
    /** Get a list of tree files for a certain patient
     * @return an array of Files
     * @param patient_pcode the p_code of the patient
     * @throws IOException when access to the tree files fails
     */
    protected File[] getPatientTreeFiles(String patient_pcode) throws IOException {
        Vector v = (Vector) pcodeToTreefilesVectorTable.get(patient_pcode);
        File[] files;
        if (v == null) {
            v = new Vector();
            files = super.getPatientTreeFiles(patient_pcode);
            for (int i = 0; i < files.length; i++) {
                v.add(files[i]);
            }
            pcodeToTreefilesVectorTable.put(patient_pcode,v);
            return files;
        } else {
            files = new File[v.size()];
            files = (File[]) v.toArray(files);
            return files;
        }
            
    }
    
    /** 
     * Get a list of unique patients (patient identifiers)
     * @return an array of patient identifiers
     * @throws IOException thrown when accessing of the tree files fails
     */
    public PatientIdentifier[] getPatients() throws IOException {
        System.out.println("you are using caching treefile handler");
        if (patients == null) {
            patients = super.getPatients();
        }
        return patients;
    
    }
    
    /** List the tree files in the current datafile path
     * @return an array of all the tree files in the current data location
     * @throws IOException when access to the tree files fails
     */
    protected File[] listTreeFiles() throws IOException {
        if (treeFiles == null) {
            treeFiles = super.listTreeFiles();
        }
        return treeFiles;
    }
    
    public void setExaminationDataLocation(String loc) {        
        super.setExaminationDataLocation(loc);
        
        // Invalidate these cached items
        treeFiles = null;
        patients = null;        
    }
    
    /** Create a new ExaminationValueTable from a tree file
     * @param treeFile the tree file to turn into an ExaminationValueTable
     * @throws IOException thrown when access to the tree files fails
     * @return the new ExaminationValueTable
     */
    //protected ExaminationValueTable makeValueTable(File treeFile) throws IOException {
    //}
    
    /** Save an examination to the tree file database
     * @param examination the examination to save
     * @throws IOException when storing the new tree file fails
     */
    //public void saveExamination(ExaminationModel examination) throws IOException {
    //}
    
    /** Sets the location of the examination data (the tree files)
     * @param loc the location of the examination data (the tree files)
     */
    //public void setExaminationDataLocation(String loc) {
    //}
    
}
