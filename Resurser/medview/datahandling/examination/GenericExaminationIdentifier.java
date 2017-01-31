/*
 * GenericExaminationIdentifier.java
 *
 * Created on den 13 februari 2004, 16:41
 *
 * $Id: GenericExaminationIdentifier.java,v 1.4 2004/10/12 15:34:47 erichson Exp $
 *
 * $Log: GenericExaminationIdentifier.java,v $
 * Revision 1.4  2004/10/12 15:34:47  erichson
 * made hashcode be the hashcode of the string representation.
 *
 * Revision 1.3  2004/04/12 20:34:29  erichson
 * Added getExaminationIDString().
 * Added javadoc for getExaminationIDString() and getStringRepresentation()
 *   (pasted from ExaminationIdentifier)
 *
 * Revision 1.2  2004/04/06 12:21:25  erichson
 * Fixed toString
 *
 * Revision 1.1  2004/04/05 20:57:30  erichson
 * First check-in.
 *
 *
 * 
 */

package medview.datahandling.examination;

import java.util.Date;

import medview.datahandling.*; // PatientIdentifier



/**
 * Generic examination identifier class, where an examination is defined by a pid and 
 * a string. This is appropriate for examinations that do not have a date time (such as
 * MHC examinations, which have a "Löpnummer" instead. 
 *
 * So far only used by MVisualizer.
 *
 * NOTE: Date-methods have dummy implementations since we cannot change ExaminationIdentifier interface right now. Do not use them!!
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class GenericExaminationIdentifier implements medview.datahandling.examination.ExaminationIdentifier {
    
    private PatientIdentifier pid;
    private String examinationId;
    
    /** Creates a new instance of GenericExaminationIdentifier */
    public GenericExaminationIdentifier(PatientIdentifier pid, String examination_id) {
        this.pid = pid;
        this.examinationId = examination_id;
    }
    
    public boolean equals(ExaminationIdentifier other) {
        return false; // See javadoc
    }
    
    public boolean equals(GenericExaminationIdentifier other) {
        if ((pid.equals(other.getPID())) &&
            examinationId.equals(other.getExaminationID()))            

            return true;
        else
            return false;
    }
                     
    private String getExaminationID() {
        return examinationId;
    }
        
    /**
     * Get a string representation of the part that differentiates this examination from other examinations 
     * for the same patient. Examples of this is the date (in MedView) or löpnummer (for MHC).
     *
     * This is needed for MHC examination handling (in GenericExaminationIdentifier).
     *
     * NOTE: There is now some overlap between this functionality and getStringRepresentation() which should probably be cleaned up in a future meeting. // Nils
     */
    public String getExaminationIDString() {
        return getExaminationID();
    }
        
    /** 
     * Show this unique examination as a string.
     *
     * This method should return a string representation which contains both the unique
     * patient id as well as the examination id. (As opposed to getExaminationIDString()
     * which should just return the examination id. // Nils
     */
    public String getStringRepresentation() {
        return (pid.getPID() + '_' + examinationId);
    }    
    
    public int hashCode()
    {
        return getStringRepresentation().hashCode();
    }
    
    /**
     * @deprecated
     */
    public String getPcode() {
        return pid.getPCode();
    }    
    
    public PatientIdentifier getPID() {
        return pid;
    }    
    public void setPID(medview.datahandling.PatientIdentifier pid) {
        this.pid = pid;
    }
    
    /**
     * @deprecated use setPID instead
     */
    public void setPcode(String Pcode) {
        this.pid = new PatientIdentifier(Pcode);
    }            
    
    public int getMinute() {
        return 0; 
    }    
    public void setMinute(int minute) {
    }
    
    public int getSecond() {
        return 0;
    }
    public void setSecond(int seconds) {
    }
    
    public int getHour() {
        return 0;
    }   
    public void setHour(int hour) {
    }
    
    public int getMonth() {
        return 0;
    }
    public void setMonth(int month) {
    }
    
    public int getDay() {
        return 0;
    }
    public void setDay(int day) {        
    }        
    
    public int getYear() {
        return 0;
    }    
    public void setYear(int year) {
    }
    
    public Date getTime() {
        return new Date(0);
    }    
    
    public String toString() {
        return getStringRepresentation();
    }
        
}
