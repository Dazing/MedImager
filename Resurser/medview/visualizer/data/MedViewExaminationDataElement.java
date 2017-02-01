/*
 * MedViewExaminationDataElement.java
 *
 * Created on July 3, 2002, 9:51 AM
 *
 * $Id: MedViewExaminationDataElement.java,v 1.24 2004/10/19 12:35:29 erichson Exp $
 *
 * $Log: MedViewExaminationDataElement.java,v $
 * Revision 1.24  2004/10/19 12:35:29  erichson
 * Changed it so that we get (N/A) instead of the "Default value not implemented" from medviewdathandler
 *
 * Revision 1.23  2004/02/24 20:17:11  erichson
 * Added null checking in equals
 *
 * Revision 1.22  2002/10/22 17:33:21  erichson
 * Updated the class to use DataSource
 *
 * Revision 1.21  2002/10/22 15:54:58  zachrisg
 * Now uses MedViewDataHandler.getDefaultValue();
 *
 * Revision 1.20  2002/10/22 15:02:24  zachrisg
 * Updated javadoc a bit.
 *
 * Revision 1.19  2002/10/22 14:50:38  zachrisg
 * DataManager.getNotApplicableValue() is now used instead of MedViewExaminationDataElement.getNotApplicableValue().
 *
 * Revision 1.18  2002/10/10 14:18:14  erichson
 * getValues now gets aggregated values from the passed Aggregation instead of DataManager
 *
 * Revision 1.17  2002/10/10 14:09:44  erichson
 * Added aggregation support
 *
 */

package medview.visualizer.data;

import java.util.*;
import java.io.*;

import medview.datahandling.*;
import medview.datahandling.aggregation.*;
import medview.datahandling.examination.*;
import medview.visualizer.event.*;

/**
 * A data element class representing a MedView examination.
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class MedViewExaminationDataElement implements ExaminationDataElement, DataGroupStateListener {
    
    public static final String NA_VALUE = "(N/A)";
    
    /** The data source for this element.
        Should be initialized once, then never changed*/
    private final DataSource dataSource;
     
    
    /** A list of the selection listeners. */
    private Vector selectionListeners;
     
    /** A list of data group change listeners. */
    private Vector dataGroupChangeListeners;
    
    /** A list of removal listeners. */
    private Vector removalListeners;
    
    /** True if the element is selected. */
    private boolean selected;
    
    private ExaminationValueContainer valueContainer;
    
    private ExaminationIdentifier examinationIdentifier;
    
    /** The data group that the element belongs to. */
    private DataGroup dataGroup = null;
    
    /** 
     * Creates a new instance of MedViewExaminationDataElement.
     *
     * @param dataGroup The data group that the data element should belong to.
     * @param evc The ExaminationValueContainer that the examination data should be extracted from.
     * @param id The examination identifier for this examination.
     */
    public MedViewExaminationDataElement(DataGroup dataGroup, DataSource dSource, ExaminationValueContainer evc, ExaminationIdentifier id) {
        selected = false;
        dataSource = dSource;
        
        selectionListeners = new Vector();
        dataGroupChangeListeners = new Vector();
        removalListeners = new Vector();
        
        valueContainer = evc;
        examinationIdentifier = id;

        this.dataGroup = dataGroup;
        dataGroup.addDataGroupStateListener(this);
        dataGroup.increaseMemberCount();    
    }
    
    /**
     * Creates a new MedViewExaminationDataElement without an examination identifier.
     * The examination identifier will be built from fields on first request.
     *
     * @param dataGroup The data group that the data element should belong to.
     * @param evc The ExaminationValueContainer that the examination data should be extracted from.
     */
    public MedViewExaminationDataElement(DataGroup dataGroup, DataSource dataSource, ExaminationValueContainer evc) { // constructor w/o identifier.
        this(dataGroup,dataSource,evc,null); // id = null means that the identifier will be rebuilt from fields on first request
    }    
    
    /** 
     * Add a SelectionListener to this data element.
     *
     * @param listener tbe SelectionListener to add
     */
    public void addSelectionListener(SelectionListener listener) {
          if (!selectionListeners.contains(listener)) {
            selectionListeners.add(listener);
        }
    }

    /** 
 * Remove a SelectionListener from this data element.
     *
     * @param listener the SelectionListener to remove
     */
    public void removeSelectionListener(SelectionListener listener) {
        selectionListeners.remove(listener);
    }
    
    /** 
     * Notify all SelectionListeners that the selection has changed.
     */
    public void fireSelectionChanged() {
        for (Iterator it = selectionListeners.iterator(); it.hasNext();) {
            SelectionListener sl = (SelectionListener) it.next();
            sl.selectionChanged(new SelectionEvent(this));
        }        
    }
    
    /** 
     * Get a list of applicable attributes.
     *
     * @return An array of attribute names.
     */
    public String[] getTermsWithValues() {
        return valueContainer.getTermsWithValues();
    }
    
    /** 
     * Get a list of values for a term
     *
     * @param term The term.
     * @return The list of values for that term.
     */
    private String[] getValues(String term) throws NoSuchTermException {
        String[] values = valueContainer.getValues(term);
        if (values.length < 1) {
            values = new String[1];
            //values[0] = MedViewDataHandler.instance().getDefaultValue(term);
            values[0] = NA_VALUE;
        }                
        return values;
    }
    
    /**
     * Get a list of (possible aggregated) values for a term.
     *
     * @param term The term.
     * @param aggregation The aggregation to use. Set to null to skip aggregation.
     * @return An array of values.
     * @throws NoSuchTermException If the examination does not have a value for the term.
     */
    public String[] getValues(String term, Aggregation aggregation) throws NoSuchTermException {
        
        if (aggregation == null)
            return getValues(term); // no aggregation
        else {
            String[] values = getValues(term);

            // Aggregate the values        
            LinkedHashSet aggregatedValues = new LinkedHashSet();
            for (int i = 0; i < values.length; i++) {
                String aggregate = aggregation.getAggregatedValue(term,values[i]);
                aggregatedValues.add(aggregate);
            }

            String[] aggregatedArray = new String[aggregatedValues.size()];
            aggregatedArray = (String[]) aggregatedValues.toArray(aggregatedArray);

            return aggregatedArray;
        }
    }
    
    /**
     * Returns the first value of the given term using the specified aggregation.
     *
     * @param term The term.
     * @param aggregation The aggregation to use.
     * @return The first value of the term.
     * @throws NoSuchTermException If the examination does not have a value for the term.
     */
    public String getFirstValue(String term, Aggregation aggregation) throws NoSuchTermException {
        return getValues(term, aggregation)[0];        
    }
    
    /** 
     * Check whether this object is selected or not.
     *
     * @return Whether this object is selected or not.
     */
    public boolean isSelected() {
        return selected;
    }
       
    /** 
     * Set whether this object is selected or not.
     *
     * @param b Whether this object should be selected or not.
     */
    public void setSelected(boolean b) {        
        if (b != selected) {
            if (selected) {
                dataGroup.decreaseSelectedMemberCount();
            } else {
                dataGroup.increaseSelectedMemberCount();
            }
            selected = b;
            fireSelectionChanged();
        }
    }                  

    /**
     * Sets the data group that the data element belongs to.
     *
     * @param dataGroup The data group.
     */
    public void setDataGroup(DataGroup dataGroup) {
        if (this.dataGroup != null) {
            this.dataGroup.removeDataGroupStateListener(this);
            this.dataGroup.decreaseMemberCount();
            if (selected) {
                this.dataGroup.decreaseSelectedMemberCount();
            }
        }
        this.dataGroup = dataGroup;
        dataGroup.addDataGroupStateListener(this);
        dataGroup.increaseMemberCount();
        if (selected) {
            dataGroup.increaseSelectedMemberCount();
        }
        fireDataGroupChanged();
    }
        
    /**
     * Returns the the data group that the data element belongs to.
     *
     * @return The data group.
     */
    public DataGroup getDataGroup() {
        return dataGroup;
    }
    
    /** 
     * Add a data group change listener.
     *
     * @param listener The listener to add.
     */
    public void addDataGroupChangeListener(DataGroupChangeListener listener) {
        if (!dataGroupChangeListeners.contains(listener)) {
            dataGroupChangeListeners.add(listener);
        }
    }
    
    /** 
     * Remove a data group change listener.
     *
     * @param listener The listener to remove.
     */
    public void removeDataGroupChangeListener(DataGroupChangeListener listener) {
        dataGroupChangeListeners.remove(listener);
    }
    
    /** 
     * Notifies all data group change listeners that the data group has changed.
     */
    public void fireDataGroupChanged() {
        for (Iterator i = dataGroupChangeListeners.iterator(); i.hasNext(); ) {
            DataGroupChangeListener listener = (DataGroupChangeListener) i.next();
            listener.dataGroupChanged(new DataGroupEvent(dataGroup));
        }
    }
    
    /** 
     * Called when the color of a data group has changed.
     *
     * @param event The event.
     */
    public void dataGroupColorChanged(DataGroupEvent event) {
        fireDataGroupChanged();
    }
    
    /** 
     * Called when the name of a data group has changed.
     *
     * @param event The event.
     */
    public void dataGroupNameChanged(DataGroupEvent event) {
        fireDataGroupChanged();
    }

    /**
     * Sets the examination identifier.
     *
     * @param id The new examination identifier.
     */
    public void setExaminationIdentifier(ExaminationIdentifier id) {
        examinationIdentifier = id;
    }
    
    /**
     * Returns the examination identifier.
     *
     * @return The examination identifier.
     * @throws IOException If the examination identifier could not be created from the fields.
     */
    public ExaminationIdentifier getExaminationIdentifier() throws IOException {
        if (examinationIdentifier != null) {
            // ApplicationManager.debug("Did not need to rebuild identifier for " + examinationIdentifier); // causes extremely many printouts
            return examinationIdentifier;
        } else { 
            try {
            // Rebuild identifier from fields.
            
            String pcode = getFirstValue("P-code",null);
            String dateString = getFirstValue("Datum",null); // typ 2001-10-30 12:41:01
            
            java.text.DateFormat datumFieldFormat = medview.datahandling.examination.tree.TreeFileHandler.TREEFILE_DATUM_FIELD_DATE_FORMAT;            
            Date date = datumFieldFormat.parse(dateString);
            
            examinationIdentifier = new MedViewExaminationIdentifier(pcode,date);                        
            ApplicationManager.debug("Rebuilt identifier [" + examinationIdentifier + "] from fields...");
            
            return examinationIdentifier;
            } catch (NoSuchTermException nste) {
                throw new java.io.IOException("Could not recreate ExaminationIdentifier because: could not find term: " + nste.getMessage());
            } catch (java.text.ParseException pe) {
                throw new java.io.IOException("Could not recreate ExaminationIdentifier because: could not parse date string: " + pe.getMessage());
                
            }
        }
    }
    
    /** 
     * Add a removal listener.
     *
     * @param listener The listener to add.
     */
    public void addRemovalListener(RemovalListener listener) {
        if (!removalListeners.contains(listener)) {
            removalListeners.add(listener);
        }
    }
    
    /** 
     * Remove a removal listener.
     *
     * @param listener The listener to remove.
     */
    public void removeRemovalListener(RemovalListener listener) {
        removalListeners.remove(listener);
    }
    
    /**
     * Notifies all removal listeners that the element is to be removed.
     */
    protected void fireObjectRemoved() {
        RemovalListener[] listenerArray = new RemovalListener[removalListeners.size()];
        listenerArray = (RemovalListener[]) removalListeners.toArray(listenerArray);
        for (int i = 0; i < listenerArray.length; i++) {
            listenerArray[i].objectRemoved(new RemovalEvent(this,this));
        }
     }
    
     /**
      * Removes the element completely.
      */
     public void removeDataElement() {
        // tell all listeners to remove the element
        fireObjectRemoved();
        
        // remove the element from the dataGroup
        if (dataGroup != null) {
            dataGroup.removeDataGroupStateListener(this);
            dataGroup.decreaseMemberCount();
            if (selected) {
                dataGroup.decreaseSelectedMemberCount();
            }
            dataGroup = null;
        }
        
        // remove all listeners
        removalListeners = null;
        dataGroupChangeListeners = null;
        selectionListeners = null;
     }

     /**
      * Returns true if the data group and examination identifier is equal to that of the other element.
      *
      * @param object The object to compare this object to.
      * @return True if the data group and examination identifier equals that of the compared element.
      */
     public boolean equals(Object object) {
         if (object == null) {
             System.err.println("DataGroup.equals: Warning: other object null");
             return false;
         }
         if (object instanceof ExaminationDataElement) {
            ExaminationDataElement element = (ExaminationDataElement) object;
            try {                
                if (dataGroup.equals(element.getDataGroup()) && 
                    getExaminationIdentifier().equals(element.getExaminationIdentifier())) {
                    return true;
                } else {
                    return false;
                }                    
            } catch (IOException e) {
                return false;
            }
        } else {
            return false;
        }
     }
     
     /** 
      * Returns the ExaminationValueContainer.
      *
      * @return The ExaminationValueContainer.
      */
     public ExaminationValueContainer getExaminationValueContainer() {
        return valueContainer;
     }
     
     /**
      * Get the data source for this examinationDataElement
      * @return the element's data source
      */
     public DataSource getDataSource() {
         return dataSource;
     }
     
}
