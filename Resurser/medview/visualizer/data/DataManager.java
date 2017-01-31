/*
 * DataManager.java
 *
 * Created on June 26, 2002, 3:23 PM
 *
 * $Id: DataManager.java,v 1.100 2005/06/21 07:13:25 erichson Exp $
 *
 * $Log: DataManager.java,v $
 * Revision 1.100  2005/06/21 07:13:25  erichson
 * Minor cleanups
 *
 * Revision 1.99  2005/05/20 11:57:50  erichson
 * Updated progress handling in loadExaminations/LoadExaminationsThread for better visual feeback during loading (split up into listing and actual loading)
 *
 * Revision 1.98  2005/05/20 11:22:32  erichson
 * New termsChanged handling (bug 445) + sensitive to null datasource (when mhcFieldChooser is cancelled)
 *
 * Revision 1.97  2005/03/23 14:15:00  erichson
 * Made search() a little more readable.
 *
 * Revision 1.96  2005/02/16 11:06:57  erichson
 * Term handling update.
 *
 * Revision 1.95  2005/01/26 12:51:49  erichson
 * Term handling overhaul
 *
 * Revision 1.94  2004/12/17 12:00:14  erichson
 * implements TermContainer
 *
 * Revision 1.93  2004/11/16 07:07:36  erichson
 * Debug output removed
 *
 * Revision 1.92  2004/11/13 10:55:58  erichson
 * Thread naming
 *
 * Revision 1.91  2004/11/03 13:51:20  erichson
 * export methods now return exported amount.
 *
 * Revision 1.90  2004/10/21 12:29:59  erichson
 * Progress handling update to search and export.
 *
 * Revision 1.89  2004/10/20 12:03:17  erichson
 * Added progress to search
 *
 * Revision 1.88  2004/10/19 12:45:27  erichson
 * Fixat språkblandning (nya grupper hette Grupp, nu Group)
 *
 * Revision 1.87  2004/10/18 10:53:17  erichson
 * 1. Added search method
 * 2. implements AggregationContainer
 *
 * Revision 1.86  2004/10/12 15:47:33  erichson
 * Large update consisting of help methods for exporting to MVD.
 *
 * Revision 1.85  2004/10/12 13:40:28  erichson
 * Committing slow version of getCompletePatientExaminationSet before reworking it
 *
 * Revision 1.84  2004/10/12 10:00:23  erichson
 * Some reworking of the export methods.
 *
 * Revision 1.83  2004/10/06 14:27:47  erichson
 * * Added support for exporting
 * * Updated to use new  case-insensitive AlphabeticalHashSet
 *
 * Revision 1.82  2004/07/26 14:55:22  erichson
 * Updated createDBUri to allow windows domain.
 *
 * Revision 1.81  2004/04/08 07:14:12  erichson
 * Bug fix: getDefaultTerm would throw exception if no terms were chosen
 *
 * Revision 1.80  2004/03/28 17:54:46  erichson
 * small loadExaminations bugfix
 *
 * Revision 1.79  2004/03/26 17:40:48  erichson
 * URL --> URI
 * Added createDBURI()
 *
 * Revision 1.78  2004/02/23 14:10:34  erichson
 * Removed "removing element" println
 *
 * Revision 1.77  2004/02/23 13:44:43  erichson
 * Cosmetic change.
 *
 * Revision 1.76  2003/07/08 21:23:45  erichson
 * Had forgotten to start the timer that updates the progress bar when removing elements, fixed now.
 *
 * Revision 1.75  2003/07/08 13:52:39  erichson
 * Fixes bugzilla bug #0022 (Some of the data loading was outside the thread)
 *
 * Revision 1.74  2003/07/07 22:26:27  erichson
 * Fixade problem med att inladdning ej avslutades när man valde cancel.
 *
 * Revision 1.73  2003/07/07 13:54:11  erichson
 * Fix: Bugzilla #0016 (progressMonitorns cancel-knapp fungerade ej vid inläsning av mvd)
 *
 * Revision 1.72  2003/07/04 00:16:21  erichson
 * Removed progress listeners, replaced with ProgressObject support instead
 *
 * Revision 1.71  2002/12/11 10:26:17  zachrisg
 * Added progress monitoring to the removal of elements plus made the
 * validateViews() method synchronized.
 *
 * Revision 1.70  2002/12/05 12:27:20  zachrisg
 * Added method selectExaminations().
 *
 * Revision 1.69  2002/11/29 15:38:45  zachrisg
 * Moved the modality handling of the ProgressMonitor to ApplicationFrame.
 *
 * Revision 1.68  2002/11/28 14:26:27  zachrisg
 * Added workaround to bug that moves the ApplicationFrame to the back when
 * examination data loading is finished.
 *
 * Revision 1.67  2002/11/28 13:27:05  zachrisg
 * Now uses the more flexible progress monitoring.
 *
 * Revision 1.66  2002/11/14 14:24:14  zachrisg
 * Made addAggregation() public.
 *
 * Revision 1.65  2002/11/07 16:33:10  erichson
 * added setAggregations
 *
 * Revision 1.64  2002/11/04 15:28:34  zachrisg
 * Changed method getPatientIdentifierTerm() to return "P-code" and not "Pcode".
 *
 * Revision 1.63  2002/11/04 14:53:42  zachrisg
 * Added getPatientIdentifierTerm().
 *
 * Revision 1.62  2002/10/31 09:57:42  zachrisg
 * Change the sematics of selectDataGroup() and added method selectDataGroupExclusively().
 *
 * Revision 1.61  2002/10/31 08:52:53  erichson
 * added getPhotoTerm, loadExaminationData now throws invalidDataLocationException
 *
 * Revision 1.60  2002/10/29 09:26:23  zachrisg
 * Removed validateViews() from selectDataGroup().
 *
 * Revision 1.59  2002/10/22 17:27:20  erichson
 * Changed data loading to use URL and DataSource
 *
 * Revision 1.58  2002/10/22 16:23:09  zachrisg
 * Moved medview.visualizer.data.query to medview.datahandling.query.
 *
 * Revision 1.57  2002/10/22 16:01:31  zachrisg
 * Removed getNotApplicableValue().
 *
 * Revision 1.56  2002/10/21 14:30:29  zachrisg
 * Added support for <n/a> values in getChosenTermsAndValue().
 *
 * Revision 1.55  2002/10/21 09:13:38  zachrisg
 * DatasetChangeListeners now gets notified only when all data is loaded.
 * General javadoc clean up.
 *
 * Revision 1.54  2002/10/17 11:05:10  zachrisg
 * Changes from Vectors to HashSets.
 *
 * Revision 1.53  2002/10/16 12:51:37  zachrisg
 * The data manager now adds terms only when the data is finished loading (to avoid multiple
 * updates during loading).
 *
 * Revision 1.52  2002/10/14 15:16:09  erichson
 * Minor javadoc addition to fireTermsChanged()
 *
 * Revision 1.51  2002/10/14 13:38:24  zachrisg
 * Changed getTermsAndValues() to getChosenTermsAndValues().
 *
 * Revision 1.50  2002/10/14 10:28:08  zachrisg
 * Added getTermsAndValues().
 *
 * Revision 1.49  2002/10/11 09:28:05  zachrisg
 * Added querySelectElements().
 *
 * Revision 1.48  2002/10/11 07:55:30  zachrisg
 * Catch InvalidDataLocationException.
 *
 * Revision 1.47  2002/10/10 14:19:17  erichson
 * Replaced old aggregates handling (one global) with new version (several loaded at once, and you choose which one to use per view)
 *
 * Revision 1.46  2002/10/10 13:53:39  zachrisg
 * Changed the totals string.
 *
 * Revision 1.45  2002/10/07 11:31:45  zachrisg
 * Added get totals string.
 *
 * Revision 1.44  2002/10/02 14:20:05  erichson
 * getChartTerms() -> getAllTerms, javadoc added
 *
 * Revision 1.43  2002/09/30 11:56:11  erichson
 * Moved aggregation handling to separate class
 *
 * Revision 1.42  2002/09/27 15:41:22  erichson
 * Added cvs log field
 *
 */

package medview.visualizer.data;

import java.util.*; // Vector
import java.awt.*; // Color
import java.io.*; // File
import java.net.URI;

import com.jrefinery.data.*; // Dataset, DatasetChangeEvent etc

import misc.foundation.AlphabeticalHashSet;

import medview.datahandling.*;
import medview.datahandling.aggregation.*;
import medview.datahandling.examination.*;
import medview.datahandling.examination.filter.*;
import medview.datahandling.query.*;
import medview.visualizer.event.*;
import medview.visualizer.gui.*;

/** 
 * Manages all examination data currently loaded in the application.
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.0
 */
public class DataManager 
   implements com.jrefinery.data.Dataset, // No longer ProgressSubject
              SelectionListener, AggregationContainer
              
{ 
            
    /* Constants */
    private static final String GROUP_NAME_PREFIX = "Group";
    
    /* Terms */
    // private AlphabeticalHashSet allTermsSet,chosenTermsSet;    
    private TermContainer termContainer = new DefaultTermContainer();
    
    /* Data */
    private LinkedHashSet dataElements;
    private LinkedHashSet dataGroups;
    private HashMap examinationsMap;
    private AlphabeticalHashSet aggregationsSet;

    /* Listeners */
    private HashSet dataGroupListeners;
    private HashSet selectionListeners;
    
    private HashSet aggregationListeners;
    private HashSet dataListeners;
    //private HashSet progressListeners;
            
    /** Set this boolean to false to stop loading threads */
//    private boolean keepLoading = false;    
    
    /** The amount of selected elements */
    private int selectedElementCount;
    
    /** Settings shortcut */
    private final static Settings settings = Settings.getInstance();
    
    /** Instance field (for the singleton pattern) */
    private static DataManager instance = null;
    
    /** Creates new DataManager */
    private DataManager() 
    {
                
        termContainer = new DefaultTermContainer();
        
        //allTermsSet = new AlphabeticalHashSet(false); // Ordering not case sensitive
        
        // Load chosen terms from preferences
        
        //chosenTermsSet = new AlphabeticalHashSet(settings.getChosenTerms(), 
        //                                        false); // Ordering not case sensitive
        
        String[] chosenTerms = settings.getChosenTerms();
        System.out.println("Loaded settings chosenterms = " + Arrays.asList(chosenTerms));
        
        termContainer.setAllTerms(chosenTerms); 
        termContainer.setChosenTerms(chosenTerms);
        
        aggregationsSet = new AlphabeticalHashSet(false);  // Ordering not case sensitive
        dataElements = new LinkedHashSet();
        dataGroups = new LinkedHashSet();
        dataGroupListeners = new HashSet();
        selectionListeners = new HashSet();
        
        aggregationListeners = new HashSet();
        dataListeners = new HashSet();
        //progressListeners = new HashSet();
        examinationsMap = new HashMap();
    }
    
    /**
     * Returns a reference to the only instance of DataManager.
     *
     * @return A reference to the only instance of DataManager.
     */
    public static DataManager getInstance() {
        if (instance == null)
            instance = new DataManager();
        
        return instance;
    }

    /**
     * Gets unique examinationIdentifiers from a set of ExaminationDataElements.
     * This is because an examination can have several corresponding elements (in different data groups).
     * Help method for export.
     */
    public static int getUniqueExaminationIdentifiers(ExaminationDataElement[] dataElements)
        throws IOException
    {
        
        HashSet uniqueIdentifiers = new HashSet();
        for (int i = 0; i < dataElements.length; i++)
        {
            ExaminationIdentifier id = dataElements[i].getExaminationIdentifier();
            // DEBUG: 
            // System.out.println("Unique examinationidentifier: " + id + " with hashcode " + id.hashCode());
            uniqueIdentifiers.add(dataElements[i].getExaminationIdentifier());
        }
        return uniqueIdentifiers.size();
    }
    
    /**
     * Gets unique ExaminationIdentifiers from a set of ExaminationDataElements
     * (due to grouping, examinationdataelements can match an examination more than once)
     */
    public static PatientIdentifier[] getPatients(ExaminationDataElement[] elements) throws IOException
    {
        HashSet uniquePatients = new HashSet();
        for (int i = 0; i < elements.length; i++)
        {
            uniquePatients.add(elements[i].getExaminationIdentifier().getPID());
        }
        PatientIdentifier[] array = new PatientIdentifier[uniquePatients.size()];
        array = (PatientIdentifier[]) uniquePatients.toArray(array);
        return array;
    }
    
    /**
     * For export: Gets ALL examinationdataelements associated with the patients that have elements
     */
    public int getCompletePatientExaminationCount(PatientIdentifier[] patients)
        throws IOException
    {        
        return getUniqueExaminationIdentifiers(getElementsForPatients(patients));
    }
    
    public ExaminationDataElement[] getElementsForPatients(PatientIdentifier[] patients) throws IOException
    {     
        // put all patients in a hashset
        HashSet patientSet = new HashSet(patients.length);
        for (int p = 0; p < patients.length; p++)
        {
            patientSet.add(patients[p]);
        }
        
        HashSet countedElements = new HashSet();
        
        /* Go through all elements, but count each examination only once since one
           examination can be part of more than one element */
        for (Iterator it = dataElements.iterator(); it.hasNext();)
        {
            ExaminationDataElement nextElement = (ExaminationDataElement) it.next();
            
            ExaminationIdentifier examination = nextElement.getExaminationIdentifier();
                        
            if (patientSet.contains(examination.getPID())) 
            { // If this is one of our patients...
                if (! countedElements.contains(nextElement))
                { // If this examinationIdentifier isn't already counted       
                    countedElements.add(nextElement);
                }
            }
        }
        // return countedElements;
        ExaminationDataElement[] elements = new ExaminationDataElement[countedElements.size()];
        elements = (ExaminationDataElement[]) countedElements.toArray(elements);
        return elements;
    }    
    
    /**
     * Adds a data element to the data manager.
     *
     * @param dataElement The element.
     * @return True if the addition was successfully performed.
     */
    public synchronized boolean addDataElement(ExaminationDataElement dataElement) {
        
        try {
            ExaminationIdentifier identifier = dataElement.getExaminationIdentifier();
            
            
            if (examinationAlreadyExists(identifier, dataElement.getDataGroup())) {
                ApplicationManager.debug("DataManager.addDataElement: skipped " + identifier.getStringRepresentation() + " since it was already in there");// do nothing
                return false;
            } else { // it didn't exist!
                dataElements.add(dataElement);
                dataElement.addSelectionListener(this);
                
                LinkedHashSet groupSet = (LinkedHashSet) examinationsMap.get(identifier.getStringRepresentation());
                if (groupSet == null) {
                    groupSet = new LinkedHashSet();
                    groupSet.add(dataElement.getDataGroup());
                    examinationsMap.put(identifier.getStringRepresentation(),groupSet); // Store a reference to keep it from being added again
                } else {
                    groupSet.add(dataElement.getDataGroup());
                }
                return true;
            }
        } catch (java.io.IOException ioe) {
            System.err.println("Could not extract examinationidentifier from element, skipping..");
            return false;
        }
    }
    
    /**
     * Removes an element without firing an event.
     *
     * @param dataElement The element to remove.
     */
    protected synchronized void removeDataElement(ExaminationDataElement dataElement) {
        try {
            ExaminationIdentifier identifier = dataElement.getExaminationIdentifier();
            
            // does the element exist in that group?
            if (examinationAlreadyExists(identifier, dataElement.getDataGroup())) { 
                dataElement.removeSelectionListener(this);
                dataElement.removeDataElement();
                dataElements.remove(dataElement);
                
                LinkedHashSet groupSet = (LinkedHashSet) examinationsMap.get(identifier.getStringRepresentation());
                groupSet.remove(dataElement.getDataGroup());
                if (groupSet.size() == 0) {
                    examinationsMap.remove(identifier.getStringRepresentation());
                }
            } else {
                // element did not exist in that group
                ApplicationManager.getInstance().errorMessage("Could not remove examination data element " + identifier + "in group " + dataElement.getDataGroup());
            }
        } catch (java.io.IOException ioe) {
            ApplicationManager.getInstance().errorMessage("Could not get examinationidentifier from element, skipping. Reason : " + ioe.getMessage());
        }
    }
    
     private int removalCount = 0;
    /**
     * Removes a couple of elements and the updates the views.
     *
     * @param elements The elements to remove.
     */
   
    public synchronized void removeDataElements(final ExaminationDataElement[] elements) {
        
        ApplicationManager.getInstance().setStatusBarText("Removing " + elements.length + " examinations...");        
        
        // Do the removal in a separate thread
        Thread removalThread = new Thread("ElementRemovalThread_" + elements.length) 
        {
            public void run() {
                for (removalCount = 0; removalCount < elements.length; removalCount++) {            
                    // System.out.println("removing element " + removalCount); // Just to slow down
                    removeDataElement(elements[removalCount]);                    
                }   
                fireDataChanged();
                validateViews();
                ApplicationManager.getInstance().setStatusBarText("Finished removing examinations.");
            }            
        };
        
        final javax.swing.Timer timer = new javax.swing.Timer(150,null);
        timer.addActionListener( new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ev) {
                ApplicationManager.getInstance().setStatusBarProgress(1, removalCount+1, elements.length);
                if ( (removalCount+1) >= elements.length) {
                    timer.stop();
                }
            }
        });                               
        timer.start();
        removalThread.start();                  
    }
    
    /** 
     * Get all available terms
     *
     * @return all available terms
     */
    public String[] getAllTerms()
    {
        //return allTermsSet.toStringArray();
        return termContainer.getAllTerms();
    }
    
    /** 
     * Get the currently active (chosen) terms
     *
     * @return the currently active (chosen) terms
     */
    public String[] getChosenTerms() 
    {
        //return chosenTermsSet.toStringArray();
        return termContainer.getChosenTerms();
    }
    
    /**
     * Get the currently inactive (unchosen) terms.
     *
     * @return An array of the inactive terms.
     */ 
    /*public String[] getUnchosenTerms() {
        // Set operation: Full set - chosen terms = resulting terms
        AlphabeticalHashSet copySet = new AlphabeticalHashSet(allTermsSet, 
                                                               false); // Ordering not case sensitive
        copySet.removeAll(chosenTermsSet);
        return copySet.toStringArray();
    }
    */
    
    /**
     * Returns a hashtable with the chosen terms as keys and LinkedHashSets of the term values as values.
     * 
     * @param agg The aggregation to use.
     * @return A hashtable with the terms as keys and the term values as values.
     */
    public Hashtable getChosenTermsAndValues(Aggregation agg) {
        // create hashtable of terms and values
        Hashtable valueSetTable = new Hashtable();
        
        ExaminationDataElement[] elements = getAllElements();
        String[] terms = termContainer.getChosenTerms();
        
        for (int t = 0; t < terms.length; t++) {
            LinkedHashSet valueSet = new LinkedHashSet();
            
            for (int e = 0; e < elements.length; e++) {
                String[] values;
                try {
                    values = elements[e].getValues(terms[t], agg);                    
                    if (values.length == 0) {
                        values = new String[1];
                        values[0] = MedViewDataHandler.instance().getDefaultValue(terms[t]);
                    }
                } catch (NoSuchTermException exc) {
                    values = new String[1];
                    values[0] = MedViewDataHandler.instance().getDefaultValue(terms[t]);
                }
                for (int v = 0; v < values.length; v++) {
                    valueSet.add(values[v]);
                }
            }
            if (valueSet.size() > 0) {
                valueSetTable.put(terms[t], valueSet);
            }        
        }
        
        return valueSetTable;
    }
    
    
    /**
     * Case insensitive search.
     * @param searchValue the text to search for
     * @param aggregation the aggregation to use. can be <code>null</code> for no aggregation.
     * @param terms the terms to search.
     * @return the elements that match the search criteraia.
     */ 
    public ExaminationDataElement[] search(String searchValue, Aggregation aggregation, String[] terms, AbstractProgressObject progressObject)
    {
        int progress = 1;
        progressObject.setProgressMin(progress);
        progressObject.setProgress(progress);
        progressObject.setProgressMax(dataElements.size());
        
        searchValue = searchValue.toLowerCase();
        
        // Put terms in HashSet
        HashSet searchTermsSet = new HashSet();
        for (int i = 0; i < terms.length; i++)
        {
            searchTermsSet.add(terms[i]);
        }
        
        Vector matches = new Vector();

        // iterate over all data elements
    elementLoop:
        for (Iterator it = dataElements.iterator(); it.hasNext();)
        {
            progressObject.setProgress(progress);
            ExaminationDataElement nextElement = (ExaminationDataElement) it.next();
            ExaminationValueContainer evc = nextElement.getExaminationValueContainer();
            String[] termsWithValues = evc.getTermsWithValues();

        termLoop: 
            for (int i = 0; i < termsWithValues.length; i++)
            {
                if (searchTermsSet.contains(termsWithValues[i]))
                {
                    // Check this value
                    try {
                        String[] values = evc.getValues(termsWithValues[i]);
                        for (int v = 0; v < values.length; v++)
                        {
                            if (aggregation != null)
                            {
                                values[v] = aggregation.getAggregatedValue(termsWithValues[i], values[v]);
                            }

                            // Search this value for the search value
                            if (values[v].toLowerCase().indexOf(searchValue) != -1) // values[v] contains the searchValue
                            {
                                matches.add(nextElement);                              
                                continue elementLoop;   // go to next examination
                            }
                        }
                    } catch (NoSuchTermException nste)
                    {
                        ApplicationManager.errorMessage("APPLICATION ERROR: Skipping term: getTermsWithValues gave non-existant-term: " + termsWithValues[i]);
                    }                    
                }
            }
            progress++;
        } // end of "all elements" loop
        
        progressObject.setProgress(progressObject.getProgressMax()); // TODO: Ugly hack, remove this 

        ExaminationDataElement[] array = new ExaminationDataElement[matches.size()];
        array = (ExaminationDataElement[]) matches.toArray(array);
        return array;
    }
    
    /**
     * Adds a term to the set of available terms.
     *
     * @param newTerm The new term.
     */
    public void addTerm(String newTerm) 
    {
        boolean term_was_added = termContainer.addTerm(newTerm); // Fires termsChanged
                      
        System.out.println("DM.addTerm: " + term_was_added + " that we added term " + newTerm);
    }
    
    /**
     * Adds a number of terms and fires an event after all have been added if any.
     *
     * @param terms The terms to add.
     */
    public void addTerms(String[] terms) 
    {        
        /*System.out.println("Addterms before: all="+termContainer.getAllTerms().length +","
                            + "chos=" + termContainer.getChosenTerms().length +","
                            + "unchos= " + termContainer.getUnchosenTerms().length);*/
        
        boolean termAdded = termContainer.addTerms(terms);   // fires termsChanged
        
        /*
        if (termAdded) 
        {
            System.out.println("DM.addTerms: we added " + Arrays.asList(terms));            
        }
        
        System.out.println("Addterms after: all="+termContainer.getAllTerms().length +","
                            + "chos=" + termContainer.getChosenTerms().length +","
                            + "unchos= " + termContainer.getUnchosenTerms().length);
        */
    }
    
    public TermContainer getTermContainer()
    { return termContainer; }
            
    
    /**      
     * Get the default term for opening new graphs.
     *
     * The default term should of course be one that exists in at least some of the examinations
     * @return the default term
     */
    public String getDefaultTerm() 
    {
        String[] chosenTerms = termContainer.getChosenTerms();
        if (chosenTerms.length <= 0) { // No chosen term exists
            return "NoTerm";            
        }
        else {
            String defaultTerm = termContainer.getChosenTerms()[0];        
            return defaultTerm;
        }
    }
    
    /**
     * Add a {@link TermsChangeListener TermsChangeListener} to the set of terms.
     *
     * @param tce the TermsChangeListener to add
     */
    public void addTermsChangeListener(TermsChangeListener tce) {
        termContainer.addTermsChangeListener(tce);
    }
     
    public void removeTermsChangeListener(TermsChangeListener tce) {
        termContainer.removeTermsChangeListener(tce);
    }
    
    /**
     * Returns the string to be displayed when displaying a sum (totals) of something.
     *
     * @return The totals string.
     */
    public String getTotalsString() {
        // UNIMPLEMENTED: add localization
        return "# of examinations";
    }
    
    /**
     * Add a term to the set of chosen terms.
     *
     * @param newTerm the term to add to the set of chosen terms
     */
    /*public void addChosenTerm(String newTerm) {
        chosenTermsSet.add(newTerm);
        fireTermsChanged();
    }*/
    
    /**
     * Remove a term from the set of chosen terms.
     *
     * @param term the term to remove from the set of chosen terms
     */
    /*public void removeChosenTerm(String term) {
        chosenTermsSet.remove(term);
        fireTermsChanged();
    }*/
    
    /**
     * sets the set of chosen terms.
     *
     * @param terms the new set of chosen terms
     */
    public void setChosenTerms(String[] terms) 
    {
        System.out.println("DM.SetChosenTerms called");
        termContainer.setChosenTerms(terms); // fires termsChanged
        
        /*
        fireTermsChanged(true, // chosen terms changed
                         false); // all terms changed*/
    }
    
    /**
     * Check whether a certain examination already exists in a data group.
     *
     * @param identifier the examination identifier
     * @param dataGroup the dataGroup to check
     * @return whether that examination already exists in that data group
     */
    public boolean examinationAlreadyExists(ExaminationIdentifier identifier, DataGroup dataGroup) {
        LinkedHashSet groupSet = (LinkedHashSet) examinationsMap.get(identifier.getStringRepresentation());
        if (groupSet == null) {
            return false;
        } else {
            return groupSet.contains(dataGroup);
        }
    }
    
    /**
     * Get the total amount of data items loaded.
     *
     * @return the total amount of data items loaded
     */
    public int getTotalItemCount() {
        return dataElements.size();
    }
    
    /**
     * Get the amount of selected items out of all data elements.
     *
     * @return the amount of selected items out of all data elements
     */
    public int getSelectedItemCount() {
        return selectedElementCount;
    }
    
    /**
     * Get all loaded data elements.
     *
     * @return an array of all the loaded data elements
     */
    public synchronized ExaminationDataElement[] getAllElements() {
        ExaminationDataElement[] array = new ExaminationDataElement[dataElements.size()];
        array = (ExaminationDataElement[]) dataElements.toArray(array);
        return array;        
    }
    
    /**
     * Get all selected data elements
     *
     * @return an array of all the selected data elements
     */
    public synchronized ExaminationDataElement[] getSelectedElements() 
    {
        Vector v = new Vector();
        for (Iterator it = dataElements.iterator(); it.hasNext();)
        {
            ExaminationDataElement nextElement =
                (ExaminationDataElement) it.next();
            if (nextElement.isSelected())
                v.add(nextElement);
        }
        
        ExaminationDataElement[] array = new ExaminationDataElement[v.size()];
        array = (ExaminationDataElement[]) v.toArray(array);
        return array;        
        
    }    
    
    /**
     * Exports selected examinations to a file.
     *
     * @return the amount of examinations exported (since partial export can be allowed)
     */    
    public synchronized int exportSelectedExaminations(File targetMVD, AbstractProgressObject progressObject, ExaminationContentFilter filter, boolean allowPartialExport) throws IOException
    {
        progressObject.setIndeterminate(true);
        ExaminationDataElement[] selectedElements = getSelectedElements();
        return exportExaminations(selectedElements, targetMVD, progressObject, filter, allowPartialExport);
    }
    
    /**
     * Export all examinations that match these patients
     * @return the amount of examinations exported (since partial export can be allowed)
     */
    public synchronized int exportPatientExaminations(PatientIdentifier[] patientIdentifiers,
                                   File targetMVD,
                                   AbstractProgressObject progressObject,
                                   ExaminationContentFilter filter,
                                   boolean allowPartialExport)
                                   throws IOException
    {
        progressObject.setIndeterminate(true);
        progressObject.setDescription("Export: Finding examinations for patients");
        
        ExaminationDataElement[] elements = getElementsForPatients(patientIdentifiers);
        System.out.println("exportPatientExaminations: " + elements.length + " elements");
        return exportExaminations(elements, targetMVD, progressObject, filter, allowPartialExport);
    }
    
    
    /**
     * Exports the examinations corresponding to a set of elements.
     * Each examination is only exported once (since more than one element can correspond to the same
     * examination)
     * @return the amount of examinations exported (since partial export can be allowed)
     */
    public synchronized int exportExaminations(ExaminationDataElement[] exportElements,
                                   File targetMVD,
                                   AbstractProgressObject progressObject,
                                   ExaminationContentFilter filter,
                                   boolean allowPartialExport)
                                   throws IOException
    {
        /* Loop through them and select unique examinations
         * as examinations can appear twice 
         */        
        // Sort examinations per datasource to make this more efficient        
        HashMap dataSourceToVectorMap = new HashMap();        
        
        progressObject.setIndeterminate(false);
        progressObject.setProgressMin(0);
        progressObject.setProgressMax(exportElements.length); // will never max out this way
        progressObject.setDescription("Export: Preparing to export...");
        
        HashSet exportedExaminations = new HashSet(); // Keep track of which examinations have already been exported
        for (int i = 0; i < exportElements.length; i++)
        {            
            progressObject.setProgress(i); // Will never reach max
            ExaminationDataElement nextElement = exportElements[i];
            ExaminationIdentifier nextElementIdentifier = nextElement.getExaminationIdentifier();
            if (! exportedExaminations.contains(nextElementIdentifier))
            {
                // This examination has not been exported yet, so add it to the list of examinations to export
                DataSource nextSource = nextElement.getDataSource();
                Vector v = (Vector) dataSourceToVectorMap.get(nextSource);
                if (v == null) {
                    v = new Vector();
                    dataSourceToVectorMap.put(nextSource,v);
                }
                
                v.add(nextElementIdentifier);
                exportedExaminations.add(nextElementIdentifier);
            }
            
        }       
        
        //go through all datasources and export
        int totalExported = 0;
        for (Iterator dataSourceIterator = dataSourceToVectorMap.keySet().iterator(); dataSourceIterator.hasNext();)
        {
            DataSource nextSource = (DataSource) dataSourceIterator.next();
            progressObject.setDescription("Export: Exporting dataSource + " + nextSource.getURI());            
            
            Vector v = (Vector) dataSourceToVectorMap.get(nextSource);
            
            ExaminationIdentifier[] identifiers = new ExaminationIdentifier[v.size()];
            identifiers = (ExaminationIdentifier[]) v.toArray(identifiers);
                        
            int exportedAmount = nextSource.exportToMVD(identifiers, targetMVD, progressObject, filter, allowPartialExport);
            totalExported += exportedAmount;
        } 
        // Not really clean to do this here (would rather do it after the call to export), but this is the only
        // place where the total of examinations is available when exporting based on patients
        ApplicationManager.infoMessage("Exported " + totalExported + " examinations out of " + exportedExaminations.size() + ". (Skipped " + (exportedExaminations.size() - totalExported) + ")");
        return totalExported;        
    }
    
    /**
     * Get all data elements in a data group.
     *
     * @param dataGroup The data group from which to take the elements.
     * @return The elements of the given data group.
     */
    public ExaminationDataElement[] getElementsInDataGroup(DataGroup dataGroup) {
        Vector elements = new Vector();
        for (Iterator i = dataElements.iterator(); i.hasNext(); ) {
            ExaminationDataElement element = (ExaminationDataElement) i.next();
            if (element.getDataGroup().equals(dataGroup)) {
                elements.add(element);
            }
        }
        ExaminationDataElement[] array = new ExaminationDataElement[elements.size()];
        array = (ExaminationDataElement[]) elements.toArray(array);
        return array;
    }
    
    
    /**
     * Load examination data.
     *
     * @param path The path to the data directory.
     * @param dataGroup The data group to put the loaded data into.
     *
     * @return LoadExaminationsThread that loads examinations. call getProgress on it to update progress
     */
    public LoadExaminationsThread loadAllExaminations(java.net.URI url, DataGroup dataGroup) throws java.io.IOException, InvalidDataLocationException {
        
        LoadExaminationsThread loadThread = new LoadExaminationsThread(url, dataGroup);
        loadThread.start();
        return loadThread;
    }
    
    
    /**
     * Creates and runs a new thread which loads all examinations from a datasource
     */
    public LoadExaminationsThread loadAllExaminations(DataSource dataSource, DataGroup dataGroup) throws java.io.IOException {
        LoadExaminationsThread loadThread = new LoadExaminationsThread(dataSource, dataGroup);
        loadThread.start();
        return loadThread;
    }
    
    /**
     * Creates a new thread to load a set of examinations from a datasource.
     */
    public LoadExaminationsThread loadExaminations(DataSource dataSource, DataGroup dataGroup, ExaminationIdentifier[] examinations) {
        ApplicationManager.debug("Now really Loading examination data (running loadExaminations)");                                
        ApplicationManager.debug("DataGroup: " + dataGroup);
        LoadExaminationsThread loadThread = new LoadExaminationsThread(dataSource, dataGroup, examinations);
        ApplicationManager.debug("Starting loading thread");
        loadThread.start();
        //javax.swing.SwingUtilities.invokeLater(loadThread);
        ApplicationManager.debug("Now the loading thread has started, returning from loadExaminations");
        return loadThread;        
    }
        
        
    /**
     * Update selection lists, and the GUI.
     */
    public synchronized void validateViews() {
        ApplicationManager.getInstance().validateViews(); // Tell views to update
        fireSelectionChanged(); // Tell everyone who listens to the 'global selection' (selection out of all elements) to update
    }
    
    /**
     * Adds a progress listener.
     *
     * @param listener The listener to add.
     */
    /*public void addProgressListener(ProgressListener listener) {
        progressListeners.add(listener);
    }*/
    
    /**
     * Removes a progress listener.
     *
     * @param listener The listener to remove.
     */
    /*public void removeProgressListener(ProgressListener listener) {
        progressListeners.remove(listener);
    }*/
    
    /**
     * Notifies progress listeners that progress in loading the data has been made.
     *
     * @param minimum The minimum of loaded patients.
     * @param now The current amount of loaded patients.
     * @param max The maximum number of patients to be loaded.
     */
    /*
    private void fireProgressMade(int minimum, int now, int max) {
        for (Iterator it = progressListeners.iterator(); it.hasNext();) {
            ProgressListener nextListener = (ProgressListener) it.next();
            nextListener.progressMade(new ProgressEvent(
                "Loading examinations for " + max + " patients...", 
                "Loaded " + now + "/" + max + " patients", 
                this, minimum, now, max));
        }
    }
    */
    
    /** 
     * Adds a selection listener.
     *
     * @param listener The listener to add.
     */
    public void addSelectionListener(SelectionListener listener) {
        selectionListeners.add(listener);
    }
    
    /**
     * Removes a selection listener.
     *
     * @param listener The listener to remove.
     */
    public void removeSelectionListener(SelectionListener listener) {
        selectionListeners.remove(listener);
    }
    
    /**
     * Notifies selection listeners that the selection has changed.
     */    
    private void fireSelectionChanged() {
        for (Iterator it = selectionListeners.iterator(); it.hasNext();) {
            SelectionListener nextListener = (SelectionListener) it.next();
            nextListener.selectionChanged(new SelectionEvent(this));
        }
    }

    /**
     * Adds a data set change listener.
     *
     * @param listener The listener.
     */
    public void addChangeListener(DatasetChangeListener listener) {
        dataListeners.add(listener);
    }
    
    /**
     * Removes a data set change listener.
     *
     * @param listener The listener to remove.
     */
    public void removeChangeListener(DatasetChangeListener listener) {
        dataListeners.remove(listener);
    }
    
    /**
     * Notifies data set change listeners that the data in the DataManager has changed.
     */
    private void fireDataChanged() {
        for (Iterator it = dataListeners.iterator(); it.hasNext();) {
            DatasetChangeListener nextListener = (DatasetChangeListener) it.next();
            // ApplicationManager.debug("Firing a data changed event");
            nextListener.datasetChanged(new DatasetChangeEvent(this,this));
        }
    }
    
    /**
     * Deselect all data elements without firing an event.
     */
    public void deselectAllElements() {
        for (Iterator it = dataElements.iterator(); it.hasNext();) {
            ExaminationDataElement nextElement = (ExaminationDataElement) it.next();
            nextElement.setSelected(false);
        }
    }
    
    /**
     * Select all data elements that fulfills the query, deselect the rest.
     *
     * @param query The query.
     * @param agg The aggregation.
     */
    public void querySelectElements(Query query, Aggregation agg) {
        for (Iterator i = dataElements.iterator(); i.hasNext(); ) {
            ExaminationDataElement element = (ExaminationDataElement) i.next();
            element.setSelected(query.isFulfilledBy(element, agg));
        }
    }
    
    /**
     * Selects the examinations with the given identifier but does not deselect
     * the examinations that does not match the given identifier.
     * This method does not fire an event.
     *
     * @param identifier The ExaminationIdentifier.
     */
    public void selectExaminations(ExaminationIdentifier identifier) {
        for (Iterator i = dataElements.iterator(); i.hasNext(); ) {
            ExaminationDataElement element = (ExaminationDataElement) i.next();
            try {
                if (element.getExaminationIdentifier().equals(identifier)) {
                    element.setSelected(true);
                }
            } catch (IOException exc) { }
        }
    }
    
    
    /**
     * Adds a new data group to the DataManager.
     *
     * @param dataGroup The data group to add.
     */
    public void addDataGroup(DataGroup dataGroup) {
        if (dataGroups.add(dataGroup)) {
            fireDataGroupAdded(dataGroup);
        }
    }
    
    /**
     * Adds a data group listener.
     *
     * @param listener The listener to add.
     */
    public void addDataGroupListener(DataGroupListener listener) {
        dataGroupListeners.add(listener);
    }
    
    /**
     * Removes a data group from the datamanager.
     *
     * @param dataGroup The data group to remove.
     */
    public void removeDataGroup(DataGroup dataGroup) {
        // remove all elements in the data group
        removeDataElements(getElementsInDataGroup(dataGroup));
        dataGroups.remove(dataGroup);
        fireDataGroupRemoved(dataGroup);
    }
    
    /**
     * Removes data groups.
     *
     * @param dataGroups The data groups to remove.
     */
    public void removeDataGroups(DataGroup[] dataGroups) {
        for (int i = 0; i < dataGroups.length; i++) {
            removeDataGroup(dataGroups[i]);
        }
    }
    
    /**
     * Removes a data group listener.
     *
     * @param listener The listener to remove.
     */
    public void removeDataGroupListener(DataGroupListener listener) {
        dataGroupListeners.remove(listener);
    }
    
    /**
     * Notifies the data group listeners that a data group has been added.
     *
     * @param dataGroup The data group that has been added.
     */
    private void fireDataGroupAdded(DataGroup dataGroup) {
        for (Iterator i = dataGroupListeners.iterator(); i.hasNext(); ) {
            DataGroupListener listener = (DataGroupListener) i.next();
            listener.dataGroupAdded(new DataGroupEvent(dataGroup));
        }
    }
    
    /**
     * Notifies the data group listeners that a data group has been removed.
     *
     * @param dataGroup The data group that has been removed.
     */
    private void fireDataGroupRemoved(DataGroup dataGroup) {
        for (Iterator i = dataGroupListeners.iterator(); i.hasNext(); ) {
            DataGroupListener listener = (DataGroupListener) i.next();
            listener.dataGroupRemoved(new DataGroupEvent(dataGroup));
        }
    }
    
    /**
     * Returns an array of all the data groups available.
     *
     * @return An array of all the data groups.
     */
    public DataGroup[] getDataGroups() {
        DataGroup[] groups = new DataGroup[dataGroups.size()];
        groups = (DataGroup[])dataGroups.toArray(groups);
        return groups;
    }
    
    /**
     * Selects the elements in the given datagroup.
     *
     * @param dataGroup The data group to be selected.
     */
    public void selectDataGroup(DataGroup dataGroup) {
        for (Iterator i = dataElements.iterator(); i.hasNext(); ) {
            ExaminationDataElement element = (ExaminationDataElement) i.next();
            if (element.getDataGroup().equals(dataGroup)) {
                element.setSelected(true);
            }
        }
    }

    /**
     * Selects the elements in the given datagroup and deselects all
     * other elements.
     *
     * @param dataGroup The data group to be selected.
     */
    public void selectDataGroupExclusively(DataGroup dataGroup) {
        for (Iterator i = dataElements.iterator(); i.hasNext(); ) {
            ExaminationDataElement element = (ExaminationDataElement) i.next();
            if (element.getDataGroup().equals(dataGroup)) {
                element.setSelected(true);
            } else {
                element.setSelected(false);
            }
        }
    }    
    
    /**
     * Returns true if the data group name already exists.
     *
     * @param name The name.
     * @return True if the data group name already exists.
     */
    public boolean existsDataGroupWithName(String name) {
        for (Iterator i = dataGroups.iterator(); i.hasNext(); ) {
            DataGroup group = (DataGroup) i.next();
            if (group.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns true if a data group with the given color already exists.
     *
     * @param Color The color.
     * @return True if a data group with the given color exists.
     */
    public boolean existsDataGroupWithColor(Color color) {
        for (Iterator i = dataGroups.iterator(); i.hasNext(); ) {
            DataGroup group = (DataGroup) i.next();
            if (group.getColor().equals(color)) {
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * Returns a proposal of a data group name not yet taken.
     *
     * @return A unique data group name.
     */
    public String proposeDataGroupName() {
        String namePrefix = GROUP_NAME_PREFIX + " ";
        
        int groupNumber = 0;
        boolean nameExists = true;
        while (nameExists) {
            groupNumber++;
            nameExists = existsDataGroupWithName(namePrefix + groupNumber);
        }
        return namePrefix + groupNumber;        
    }
    
    /**
     * Returns a proposal of a data group color not yet taken.
     *
     * @return The proposed data group color.
     */
    public Color proposeDataGroupColor() {
        for (int granularity = 48; granularity > 0; granularity -= 4) {
            for (int intensity = 255; intensity >= 0; intensity -= granularity) {
                Color color = new Color(intensity, 255 - intensity, 255 - intensity);
                if (!existsDataGroupWithColor(color)) {
                    return color;
                }
                color = new Color(255 - intensity, intensity, 255 - intensity);
                if (!existsDataGroupWithColor(color)) {
                    return color;
                }
                color = new Color(255 - intensity, 255 - intensity, intensity);
                if (!existsDataGroupWithColor(color)) {
                    return color;
                }
                color = new Color(intensity, intensity, 255 - intensity);
                if (!existsDataGroupWithColor(color)) {
                    return color;
                }
                color = new Color(intensity, 255 - intensity, intensity);
                if (!existsDataGroupWithColor(color)) {
                    return color;
                }
                color = new Color(255 - intensity, intensity, intensity);
                if (!existsDataGroupWithColor(color)) {
                    return color;
                }
            }
        }
        return Color.black;        
    }
    
    /** 
     * Called when the selection has changed.
     *
     * @param event The object representing the event.
     */
    public synchronized void selectionChanged(SelectionEvent event) {
        ExaminationDataElement element = (ExaminationDataElement) event.getSource();
        if (element.isSelected()) {
            selectedElementCount++;
        } else {
            selectedElementCount--;
        }
    }
        
    /**
     * Class for a {@see java.util#Thread Thread} that loads examination data.
     */
    public class LoadExaminationsThread extends Thread implements ProgressObject
    {
        private boolean running = false;
        private boolean cancelled = false;
    
        /** The data group to which the data is to be inserted. */
        private DataSource dataSource;
        private DataGroup dataGroup;
        //private String[] patients;
        
        private DefaultProgressObject progressObject;
        private ExaminationIdentifier[] examinationIdentifiers; // The examinations to load when run
        
        private URI url = null;
        
        
        /**
         * Creates a new LoadExaminationsThread which loads selected examinations.
         *
         * @param path the path to the directory of examination data to load
         * @param dataGroup the data group to load the data into
         * @param examinationIdentifier the examinations to load. If it is null, all examinations are loaded.
         */        
        public LoadExaminationsThread(DataSource dataSource, DataGroup dataGroup, ExaminationIdentifier[] examinationIdentifiers)
        {
            super("LoadExaminationsThread");
            this.dataSource = dataSource;
            this.dataGroup = dataGroup;
            this.examinationIdentifiers = examinationIdentifiers;
            
            //ApplicationManager.debug("Getting patients from data source");
            //patients = dataSource.getPatientIdentifiers();
            //ApplicationManager.debug("got " + patients.length + " patients.");
            
            
            progressObject = new DefaultProgressObject(true); // start as indeterminate
            progressObject.setProgressMin(0);
        }
        
        public LoadExaminationsThread(DataSource dataSource, DataGroup dataGroup) {
            this(dataSource, dataGroup, null); // null loads all
        }
            
        public LoadExaminationsThread(URI url, DataGroup dataGroup) {
            this(null,  dataGroup, null); // Create datasource, and load all examinations
            this.url = url;            
        }
        
        /**
         * Starts to load the examinations.
         */
        private void loadExaminations()
        {
        
            progressObject.setIndeterminate(true);           
            progressObject.setDescription("Listing patients");
            
            // Create the datasource if it is null
            if (dataSource == null)
            {
                // Create new datasource
                ApplicationManager.statusMessage("Creating new datasource");
                try {
                    dataSource = DataSourceFactory.createDataSource(url); // throws IOException. May also return null
                } catch (java.sql.SQLException sqle)
                {
                    String errorMessage = "Could not log into SQL Server: " + sqle.getMessage();
                    ApplicationManager.errorMessage(errorMessage);
                    javax.swing.JOptionPane.showMessageDialog(ApplicationFrame.getInstance(),errorMessage,"Could not log into SQL server", javax.swing.JOptionPane.ERROR_MESSAGE);
                } catch (IOException ioe) { 
                    ApplicationManager.errorMessage("DataManager.loadExaminationData: Loading failed, could not create data source for " + url + ": " + ioe.getMessage());
                    return;
                } catch (InvalidDataLocationException idle) {
                    ApplicationManager.errorMessage("DataManager.loadExaminationData: Loading failed, invalid data location: " + idle.getMessage());
                    return;
                }
            }                
            
            if (dataSource != null)
            {
                if (examinationIdentifiers == null) { // Load all examinations
                    ApplicationManager.statusMessage("Listing examinations belonging to patients");
                    try {
                        examinationIdentifiers = dataSource.getAllExaminations(progressObject); // Pass progressObject along!
                    } catch (IOException ioe) {
                        ApplicationManager.errorMessage("DataManager.loadExaminationData: Could not get examination list for datasource: " + ioe.getMessage());
                        return; // Bail out (stop loading)
                    }
                }

                //private static final String DEFAULT_EDH_CLASS_NAME = "medview.datahandling.examination.tree.CachingTreeFileHandler";
                //final String cachingHandlerClass = "medview.datahandling.examination.tree.CachingTreeFileHandler";
                //MedViewDataSettingsHandler MVDSH = MedViewDataSettingsHandler.instance();
                //MVDSH.setProperty(medview.datahandling.DataHandlerFactory.CURRENT_EDH_CLASS_PROPERTY,cachingHandlerClass);               
                //MedViewDataHandler MVDH = MedViewDataHandler.instance();
                //MVDH.setExaminationDataLocation(treeFileLocation);                                               

                ApplicationManager.startTimer();                
                HashSet loadedTerms = new HashSet();                                

                progressObject.setIndeterminate(false);
                progressObject.setProgressMax(examinationIdentifiers.length);

                //System.out.println("going into loadLoop");

                progressObject.setDescription("Loading " + examinationIdentifiers.length + " examinations");
                
                loadLoop: 
                for(int examinationCount = 0; !cancelled && (examinationCount < examinationIdentifiers.length); examinationCount++)
                {
                    progressObject.setProgress(examinationCount+1);
                    
                    //ApplicationManager.debug("reading exam " + examinationCount);

                    // Skip this examination if it already exists
                    if (examinationAlreadyExists(examinationIdentifiers[examinationCount],dataGroup))
                    {
                        ApplicationManager.debug("DataManager.LoadExaminationsThread: Skipped [" + examinationIdentifiers[examinationCount] + "] since it already exists.");
                    } else
                    {
                        try
                        {
                            ApplicationManager.debug("Creating element for " + examinationIdentifiers[examinationCount]);
                            ExaminationDataElement dataElement = dataSource.createExaminationDataElement(dataGroup,
                                                                            examinationIdentifiers[examinationCount],
                                                                            true); // add derived terms?

                            // Add all terms that have values to the visualizer's term set
                            //ApplicationManager.debug("Getting terms with values");
                            String[] terms = dataElement.getTermsWithValues();
                            for (int i = 0; i < terms.length; i++)
                            {
                                /* too slow to do addTerm(terms[i]); every time. 
                                   Instead, store loaded terms in a vector, and addTerm all of it when loading finishes */
                                loadedTerms.add(terms[i]);
                            }

                            // dataElement.setExaminationIdentifier(examinations[exam]); // store the custom identifier. this is because getExaminations for a catalog will give examinationidentifier based on the file name, not on the Datum field

                            boolean elementAdded = addDataElement(dataElement);

                            // The data element must remove itself from the data group, since DataGroup does not know which elements belong to it
                            if(!elementAdded)
                            {
                                dataElement.removeDataElement();
                            }

                        } catch (NoSuchExaminationException e) {
                            ApplicationManager.getInstance().errorMessage("DataManager.loadExaminationData: Strange, getPatients gave non-existant Examination ("+examinationIdentifiers[examinationCount].toString()+"). Skipped");
                        } catch (IOException ioe) {
                            ApplicationManager.getInstance().errorMessage("Did not load examination " + examinationIdentifiers[examinationCount] + " because: " + ioe.getMessage());
                        }
                    }
                } // one examination done
                ApplicationManager.debug("Examination loading in loadExaminations done, finishing up");
                    // ApplicationManager.debug("Patient " + p + " loaded (or skipped). (" + examinations.length + " examinations) " + ApplicationManager.stopTimer());                                                                
        //            fireProgressMade(0,p+1,patients.length); // Update the progress monitor                    
                    //DataManager.getInstance().validateViews(); // Update counters in views for every patient that is loaded
                //} // one patient done (end of for patients)

                // add the loaded terms
                System.out.println("Adding terms...");
                addTerms((String[])loadedTerms.toArray(new String[loadedTerms.size()]));

                ApplicationManager.infoMessage("All patients loaded. " + ApplicationManager.stopTimer());


                // notify listeners that the data in the datamanager has changed            
                System.out.println("Announcing new data...");
                fireDataChanged();
                System.out.println("Validating views...");
                DataManager.getInstance().validateViews();            
                System.out.println("Done loading examinations.");
            } // end if datasource != null
        }
 
        public int getProgressMin() {
            return 1;
        }
        
        public int getProgress()
        {
            return progressObject.getProgress();
        }
        
        public int getProgressMax()
        {
            return progressObject.getProgressMax();
        }
        
        /**
         * Starts the execution of the thread.
         */
        public void run() {            
            setRunning(true);
            ApplicationManager.debug("run() Calling loadExaminations");
            loadExaminations();
            /*} catch (IOException e) {
                canceled = true;
                throw e;
            }*/
            ApplicationManager.debug("setrunning false");
            setRunning(false);
            ApplicationManager.debug("Loading thread terminates now");
        } // end of run()
        
        public synchronized boolean isRunning() {
            return running;
        }
        
        private synchronized void setRunning(boolean run) {
            running = run;
        }
        
        public boolean isCancelled() {;
            return cancelled;
        }
        
        public void cancel() {
            cancelled = true;
            //keepLoading = false;
            ApplicationManager.debug("Cancelled load examinations ");
        }        
        
        public boolean isIndeterminate() {
            return progressObject.isIndeterminate();
        }
        
        public String getDescription() 
        {
            return progressObject.getDescription();
        }
        
    } // end of class loadthread
    
    // Aggregates handling
    
    /**
     * Load and install a new aggregation from an MVG file.
     *
     * @param MVGFile The file to load the aggregation from.
     */
    public void loadAggregation(File MVGFile) throws IOException {        
        Aggregation newAggregation = new Aggregation(MVGFile);
        addAggregation(newAggregation);
    }
        
    /**
     * Adds an aggregation to the DataManager.
     *
     * @param agg The aggregation to add.
     */
    public void addAggregation(Aggregation agg) {
        aggregationsSet.add(agg);
        fireAggregationChanged();
    }
    
    /**
     * Returns an array of all loaded aggregations.
     *
     * @return An array of all loaded aggregations.
     */
    public Aggregation[] getAggregations() {
        Aggregation[] aggs = new Aggregation[aggregationsSet.size()];
        aggs = (Aggregation[]) aggregationsSet.toArray(aggs);
        return aggs;
    }
    
    public void setAggregations(Aggregation[] aggs) {
        aggregationsSet.clear();
        for (int i = 0; i < aggs.length; i++) {
            aggregationsSet.add(aggs[i]);
        }
        fireAggregationChanged();
    }
    
    /*
    private void setAggregation(Aggregation agg) {
        aggregation = agg;
        ApplicationManager.debug("firing... want to repaint all views now");
        fireAggregationChanged();
        //fireSelectionChanged(); // will repaint views
        validateViews();
    }
    */
    
    /**
     * Adds an aggregation listener.
     *
     * @param listener The listener to add.
     */    
    public void addAggregationListener(AggregationListener listener) {
        aggregationListeners.add(listener);
    }
    
    /**
     * Removes an aggregation listener.
     *
     * @param listener The listener to remove.
     */
    public void removeAggregationListener(AggregationListener listener) {
        aggregationListeners.remove(listener);
    }        
    
    /**
     * Notifies aggregation listeners that the loaded aggregations has somehow changed.
     */
    private void fireAggregationChanged() {
        for (Iterator it = aggregationListeners.iterator(); it.hasNext();) {
            AggregationListener nextListener = (AggregationListener) it.next();
            nextListener.aggregationChanged(new AggregationEvent(this));
        }
    }
    
    
    /**
     * Gets which term contains the photo paths.
     *
     * @return the photo term
     * NOTE: This should later be replaced when Fredrik finishes the term handling
     */
    public String getPhotoTerm() {
        return "Photo";
    }

    /**
     * Gets which term contains the patient identifier.
     *
     * @return The patient identifier term.
     * NOTE: This should later be replaced when Fredrik finishes the term handling
     */
    public String getPatientIdentifierTerm() 
    {
        return "P-code";
    }
    
    /** 
     * Cancels the operation that generates ProgressEvents.
     */
    /*
     public void cancelProgressOperation() {
        stopLoadThreads();
    }*/

    /**
     * Stops the loading threads.
     */
    /*
    private void stopLoadThreads() {
        keepLoading = false;
    }
    */
    
    /**
     * Creates an URI for the database from the currently stored settings.
     */
    public static java.net.URI createDBURI() throws java.net.URISyntaxException {                
        // Assume MS-SQL for now
        /* The URL format for jTDS is:
         jdbc:jtds:<server_type>://<server>[:<port>][/<database>][;<property>=<value>[;...]]*/
        
        String windowsDomain;
        if (settings.getDatabaseAuthenticationType() == SQLExaminationDataHandler.AUTHENTICATION_WINDOWS)
            windowsDomain = settings.getDatabaseWindowsDomain();
        else
            windowsDomain = null;            

        return SQLExaminationDataHandler.createDBURI(settings.getDatabaseServerAddress(),
                                        settings.getDatabaseServerPort(),
                                        settings.getDatabaseCatalog(),
                                        settings.getDatabaseUser(),
                                        settings.getDatabasePassword(), 
                                        ApplicationManager.APPLICATION_NAME,
                                        windowsDomain);        
    }    
}
