/*
 * ExaminationDataElement.java
 *
 * Created on June 26, 2002, 3:44 PM
 *
 * $Id: ExaminationDataElement.java,v 1.12 2004/03/28 16:39:53 erichson Exp $
 *
 * $Log: ExaminationDataElement.java,v $
 * Revision 1.12  2004/03/28 16:39:53  erichson
 * Clearer javadoc for getValues
 *
 * Revision 1.11  2002/10/22 17:21:02  erichson
 * added getDataSource and additional javadoc
 *
 * Revision 1.10  2002/10/22 16:23:09  zachrisg
 * Moved medview.visualizer.data.query to medview.datahandling.query.
 *
 * Revision 1.9  2002/10/11 09:25:29  zachrisg
 * ExaminationDataElement now extends Querible.
 *
 */

package medview.visualizer.data;

/**
 * Interface for classes that model a data element representing an examination.
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.0
 */

import medview.datahandling.*;
import medview.datahandling.aggregation.*; // Aggregation
import medview.datahandling.examination.*; // ExaminationIdentifier
import medview.datahandling.query.*; // Querible
import medview.visualizer.event.*;

public interface ExaminationDataElement extends Querible {
    
    /**
     * Get a list of applicable terms
     * @return an array of term names
     */
    public String[] getTermsWithValues();
    
    /** Check whether this object is selected or not.
     * @return whether this object is selected or not.
     */    
    public boolean isSelected();
    
    /**
     * Get a list of values for a term. Set aggregation to null to skip aggregation.
     * 
     * @param term the term whose values to fetch
     * @param aggregation the aggregation to use. Set to null to skip aggregation.
     * @return the list of values for that term
     */
    public String[] getValues(String term, Aggregation aggregation) throws NoSuchTermException;
 
    /**
     * Get the data source for this examinationDataElement
     * @returns the element's data source
     */
    public DataSource getDataSource();
    
    
    /**
     * Get the first value for a term
     * @return the first value for that term
     */ 
    public String getFirstValue(String term, Aggregation aggregation) throws NoSuchTermException;
    
    /** Set whether this object is selected or not.
     * @param b whether this object should be selected or not.
     */    
    public void setSelected(boolean b);
        
    /** Add a SelectionListener to this data element.
     * @param sl tbe SelectionListener to add
     */    
    public void addSelectionListener(medview.visualizer.event.SelectionListener sl);
    
    /** Remove a SelectionListener from this data element.
     * @param sl the SelectionListener to remove
     */    
    public void removeSelectionListener(medview.visualizer.event.SelectionListener sl);
    
    /** Notify all SelectionListeners that the selection has changed.
     */    
    public void fireSelectionChanged();
       
    /**
     * Sets the data group that the data element belongs to.
     * @param dataGroup The data group.
     */
    public void setDataGroup(DataGroup dataGroup);
        
    /**
     * Returns the the data group that the data element belongs to.
     * @return The data group.
     */
    public DataGroup getDataGroup();

    /**
     * Set the examinationIdentifier
     *
     * @param id the new ExaminationIdentifier
     */
    public void setExaminationIdentifier(ExaminationIdentifier id);
    
    /**
     * Returns the ExaminationIdentifier
     *
     * @return the ExaminationIdentifier
     * @throws IOException when the examinationIdentifier cannot be extracted
     */
    public ExaminationIdentifier getExaminationIdentifier() throws java.io.IOException;
    
    /**
     * Add a data group change listener.
     *
     * @param listener The listener to add.
     */
    public void addDataGroupChangeListener(DataGroupChangeListener listener);
    
    /**
     * Remove a data group change listener.
     *
     * @param listener The listener to remove.
     */
    public void removeDataGroupChangeListener(DataGroupChangeListener listener);
    
    /**
     * Notifies all data group change listeners that the data group has changed.
     */
    public void fireDataGroupChanged();
    
    /**
     * Add a removal listener.
     * @param listener The listener to add.
     */
    public void addRemovalListener(RemovalListener listener);
    
    /**
     * Remove a removal listener.
     * @param listener The listener to remove.
     */
    public void removeRemovalListener(RemovalListener listener);

    /**
     * Removes the element completely.
     */
    public void removeDataElement();
    
    /**
     * Returns the ExaminationValueContainer.
     * @return The ExaminationValueContainer.
     */
    public ExaminationValueContainer getExaminationValueContainer();
}

