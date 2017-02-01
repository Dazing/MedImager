/*
 * TermContainer.java
 *
 * Created on den 17 december 2004, 10:53
 *
 * $Id: TermContainer.java,v 1.3 2005/05/06 16:28:32 erichson Exp $
 * 
 * $Log: TermContainer.java,v $
 * Revision 1.3  2005/05/06 16:28:32  erichson
 * Added TermsChangeListener (part of bug 445 fix)
 *
 * Revision 1.2  2005/01/26 12:57:04  erichson
 * Second version
 *
 * Revision 1.1  2004/12/17 11:46:31  erichson
 * First check-in.
 *
 */

package medview.visualizer.data;

import medview.visualizer.event.*; 

/**
 * Interface for term containers (to easily link TermChooserDialogs)
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public interface TermContainer extends ChosenTermsContainer
{

    /* Inherited from ChosenTermsContainer: get/set chostenTerms */
    
    /**
     * @return whether the term was added (if not, it already existed)
     */
    public boolean addTerm(String newTerm);
    
    
    /**
     * @return whether the term was added (if not, it already existed)
     */
    public boolean addTerms(String[] newTerms);
    
    
    public String[] getUnchosenTerms(); // there should NOT be a set-method for this
    
    public String[] getAllTerms();
    
    public void setAllTerms(String[] allTerms);
    
    // Move terms to chosen
    public void chooseTerms(String[] terms);
    
    // Move terms to unchosen
    public void unchooseTerms(String[] terms);
    
    public void addTermsChangeListener(TermsChangeListener tce);
    public void removeTermsChangeListener(TermsChangeListener tce);
}
