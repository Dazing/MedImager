/*
 * $Id: TermFilter.java,v 1.1 2004/11/03 12:34:46 erichson Exp $
 *
 * $Log: TermFilter.java,v $
 * Revision 1.1  2004/11/03 12:34:46  erichson
 * First check-in.
 *
 */

package medview.datahandling.examination.filter;

import java.util.*;

/**
 * Filter which determines which terms should be accepted and.
 *
 * @author Nils Erichson
 */
public class TermFilter implements ExaminationContentFilter 
{

    private HashSet rejectedTermsSet;
    
    /** Creates a new instance of TermFilter */
    public TermFilter() 
    {
        rejectedTermsSet = new HashSet();
    }
    
    public TermFilter(String[] termsToReject)
    {
        this();
        for (int i = 0; i < termsToReject.length; i++)
        {
            rejectedTermsSet.add(termsToReject[i]);
        }
    }
    
    public boolean acceptTerm(String term) 
    {
        return !rejectedTermsSet.contains(term);
    }
    
}
