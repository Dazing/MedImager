/*
 * DefaultTermConatiner.java
 *
 * Created on den 22 december 2004, 12:34
 *
 * $Id: DefaultTermContainer.java,v 1.6 2008/08/20 12:20:04 it2aran Exp $
 *
 * $Log: DefaultTermContainer.java,v $
 * Revision 1.6  2008/08/20 12:20:04  it2aran
 * Terms doesn't have to be sorted
 *
 * Revision 1.5  2008/08/20 12:19:04  it2aran
 * Terms doesn't have to be sorted
 *
 * Revision 1.4  2005/05/20 12:13:23  erichson
 * removed debug "firing terms" message
 *
 * Revision 1.3  2005/05/06 16:29:45  erichson
 * termsChanged event firing update (Fixes bug 445)
 *
 * Revision 1.2  2005/02/16 11:08:21  erichson
 * Small update, removed mass println's
 *
 * Revision 1.1  2005/01/26 12:54:44  erichson
 * First check-in.
 *
 *
 */



package medview.visualizer.data;

import java.util.*;

import misc.foundation.AlphabeticalHashSet;

import medview.visualizer.event.*;

/**
 * Default implementation of TermContainer
 *
 * @author erichson
 */
public class DefaultTermContainer implements TermContainer
{
    private AlphabeticalHashSet unchosenTermsSet; // , allTermsSet;
    private ArrayList<String> chosenTermsSet;
    private static final boolean CASE_SENSITIVE = false;
    private HashSet termsChangeListeners = new HashSet();
    
    /** Creates a new instance of DefaultTermConatiner */
    public DefaultTermContainer() 
    {        
        chosenTermsSet = new ArrayList<String>();
        unchosenTermsSet = new AlphabeticalHashSet(CASE_SENSITIVE);        
    }   
        
    
    public DefaultTermContainer(String[] chosenTerms, String[] unchosenTerms)
    {
        chosenTermsSet = new ArrayList<String>(Arrays.asList(chosenTerms));
        unchosenTermsSet = new AlphabeticalHashSet(unchosenTerms, CASE_SENSITIVE);        
    }
    
    public DefaultTermContainer(Collection chosenTerms, Collection unchosenTerms)
    {
        chosenTermsSet = new ArrayList(chosenTerms);
        unchosenTermsSet = new AlphabeticalHashSet(unchosenTerms, CASE_SENSITIVE);        
    }    
    
    private AlphabeticalHashSet getAllTermsSet() 
    {
        AlphabeticalHashSet allTermsSet = new AlphabeticalHashSet(CASE_SENSITIVE);
        allTermsSet.addAll(chosenTermsSet);
        allTermsSet.addAll(unchosenTermsSet);
        //System.out.println("getAllTermsSet(): size " + allTermsSet.size());
        return allTermsSet;        
        //return allTermsSet.toStringArray();
    }
    
    public String[] getAllTerms()
    {
        String[] allTerms = getAllTermsSet().toStringArray();
        //System.out.println("allterms = " + Arrays.asList(allTerms));
        return allTerms;        
    }

    public void setAllTerms(String[] allTerms) 
    {
        AlphabeticalHashSet allTermsSet = new AlphabeticalHashSet(allTerms, CASE_SENSITIVE);
        setAllTerms(allTermsSet); // fires termsChanged
        
    }
    
    private void setAllTerms(AlphabeticalHashSet allTermsSet)
    {
        //System.out.println("setAllterms: chosenterms before: " + chosenTermsSet.size());
        //System.out.println("setAllterms: allterms are " + allTermsSet.size());
        // remove all chosen that do not exist in new ALLTERMS
        chosenTermsSet.retainAll(allTermsSet);
        //System.out.println("setAllTerms: chosenterms after retain: " + chosenTermsSet.size());
        
        
        // unchosen are updated to be all that are not chosen
        unchosenTermsSet = new AlphabeticalHashSet(CASE_SENSITIVE);
        unchosenTermsSet.addAll(allTermsSet);
        //System.out.println("setAllTerms: unchosenterms after addall: " + unchosenTermsSet.size());
        unchosenTermsSet.removeAll(chosenTermsSet);        
        //System.out.println("setAllTerms: unchosenterms after removeall: " + unchosenTermsSet.size());
        
        //updateUnchosen();
        fireTermsChanged(false, // chosen
                         true); // all terms
    }
    
    public String[] getChosenTerms() { 
        String[] chosenTerms = chosenTermsSet.toArray((new String [chosenTermsSet.size ()]));
        //System.out.println("ChosenTerms: " + Arrays.asList(chosenTerms));
        return chosenTerms;
    }    

    public String[] getUnchosenTerms() 
    { 
        //System.out.println("getunchosenterms: unchosentermsset: size " + unchosenTermsSet.size());
        String[] unchosenTerms = unchosenTermsSet.toStringArray();
        //System.out.println("UnchosenTerms: " + Arrays.asList(unchosenTerms));
        return unchosenTerms;
    }    

    
    public void setChosenTerms(String[] terms) 
    {
        //System.out.println("Pre setChosen chosen terms: " + Arrays.asList(getChosenTerms()));
        
        // make "all terms"
        AlphabeticalHashSet allTermsSet = getAllTermsSet();
        
        chosenTermsSet = new ArrayList(Arrays.asList(terms));
        
        setAllTerms(allTermsSet); // Sets "all", then removes "chosen" to update unchosen
        
        //unchosenTermsSet = new AlphabeticalHashSet
        
        //updateUnchosen();
        
        //System.out.println("Post setChosen chosen terms: " + Arrays.asList(getChosenTerms()));
        
        fireTermsChanged(true, // chosen
                         false); // all terms
    }
    /*
    private void updateUnchosen()
    {
        unchosenTermsSet.clear();                
        
        for (java.util.Iterator it = allTermsSet.iterator(); it.hasNext();)
        {
            String nextTerm = (String) it.next();
            if (chosenTermsSet.contains(nextTerm))
            {
                // nothing
            }
            else
            {
                unchosenTermsSet.add(nextTerm);
            }
        }            
    }*/
    
    public void chooseTerms(String[] terms) {
        for (int i = 0; i < terms.length; i++) {
            chosenTermsSet.add(terms[i]);
            unchosenTermsSet.remove(terms[i]);
        }        
        fireTermsChanged(true, // chosen
                         false); // all terms
    }
    
    
    public void unchooseTerms(String[] terms) {
        for (int i = 0; i < terms.length; i++) {
            chosenTermsSet.remove(terms[i]);
            unchosenTermsSet.add(terms[i]);
        }        
        fireTermsChanged(true, // chosen
                         false); // all terms
    }
    
    public boolean addTerm(String term)
    {
        AlphabeticalHashSet allTermsSet = getAllTermsSet();
        boolean changed = allTermsSet.add(term);
        if (changed)
            setAllTerms(allTermsSet); // Fires termsChanged
        return changed;
    }
    
    public boolean addTerms(String[] terms)
    {
        AlphabeticalHashSet allTermsSet = getAllTermsSet();
        
        boolean termAdded = false;
        for (int i = 0; i < terms.length; i++) 
        {
            boolean oneAdded = allTermsSet.add(terms[i]);
            if (oneAdded)
                termAdded = true;            
        }
        if (termAdded)
            setAllTerms(allTermsSet); // fires termsChanged
        return termAdded;
    }
    
    /**
     * Add a {@link TermsChangeListener TermsChangeListener} to the set of terms.
     *
     * @param tce the TermsChangeListener to add
     */
    public void addTermsChangeListener(TermsChangeListener tce) {
        termsChangeListeners.add(tce);
    }
    
    /**
     * Remove a {@link TermsChangeListener TermsChangeListener} from the set of terms.
     *
     * @param tce the TermsChangeListener to remove
     */
    public void removeTermsChangeListener(TermsChangeListener tce) {
        termsChangeListeners.remove(tce);
    }
    
    
    /**
     * Tells all {@link #TermsChangeListener TermsChangeListeners} that the terms have changed. 
     * Fired: * when the TermChooserPanel has been closed with OK (update chosen terms)
     *        * when addTerms has been run (examinations added) (update available terms)
     */ 
    private void fireTermsChanged(boolean chosenTermsChanged, boolean newTermsAdded) 
    {
        // System.out.println("Firing terms changed - chosen:" + chosenTermsChanged + ", newTerms: " + newTermsAdded);
        
        TermsChangeEvent event = new TermsChangeEvent(chosenTermsChanged, newTermsAdded);
        
        for (Iterator it = termsChangeListeners.iterator(); it.hasNext();)
        {
            ( (TermsChangeListener) it.next()).termsChanged(event);
        }
    }
    
}
