/*
 * $Id: ExaminationContentFilter.java,v 1.1 2004/11/03 12:34:46 erichson Exp $
 *
 * $Log: ExaminationContentFilter.java,v $
 * Revision 1.1  2004/11/03 12:34:46  erichson
 * First check-in.
 *
 */

package medview.datahandling.examination.filter;

/**
 * Interface to filter contents of examinations. 
 *
 * @author Nils Erichson
 */
public interface ExaminationContentFilter 
{
    /**
     * Whether or not a certain term should be accepted or rejected.
     */
    public boolean acceptTerm(String term);
    
}
