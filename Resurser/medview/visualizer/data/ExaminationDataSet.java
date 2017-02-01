/*
 * ExaminationDataset.java
 *
 * Created on June 26, 2002, 3:31 PM
 *
 * $Id: ExaminationDataSet.java,v 1.22 2004/10/11 14:01:56 erichson Exp $
 * 
 * $Log: ExaminationDataSet.java,v $
 * Revision 1.22  2004/10/11 14:01:56  erichson
 * Small commit with some dummy testing code
 *
 * Revision 1.21  2002/10/30 14:10:12  zachrisg
 * Updated javadoc.
 *
 * Revision 1.20  2002/10/10 14:48:22  erichson
 * No longer listens for aggregation changes in datamanager (this is now done by View)
 *
 * Revision 1.19  2002/09/27 15:39:29  erichson
 * Now implements aggregationlistener
 *
 */

package medview.visualizer.data;

import java.util.*; // Vector
import javax.swing.event.*; // ChangeListener
import com.jrefinery.data.*; // Dataset etc

import medview.datahandling.NoSuchTermException;
import medview.visualizer.event.*;

/** 
 * A class for handling a set of ExaminationDataElement:s.
 */
public class ExaminationDataSet extends AbstractDataset implements SelectionListener, DataGroupChangeListener, RemovalListener {

    /** The name of this dataset */
    private String name;
    
    /** DatasetChangeListeners */
    private Vector changeListeners;            
    
    /** SelectionListeners */
    private Vector selectionListeners;
    
    /** Vector containing the individual data elements */
    private LinkedHashSet dataVector;
    
    /** A table with data groups as keys and the number of examinations in the group as value. */
    private Hashtable groupToAmountTable;
    
    /** A table with data groups as keys and the number of selected examinations in the group as value. */
    private Hashtable groupToSelectedAmountTable;
    
    /** 
     * Creates new ExaminationDataset.
     */
    public ExaminationDataSet() {
        dataVector = new LinkedHashSet();
        changeListeners = new Vector();
        selectionListeners = new Vector();
        groupToAmountTable = new Hashtable();
        groupToSelectedAmountTable = new Hashtable();                
    }
    
    /** 
     * Creates new ExaminationDataSet with some examination data elements.
     *
     * @param elements The elements to fill the new data set with.
     */
    public ExaminationDataSet(ExaminationDataElement[] elements) {
        this();        
        addDataElements(elements);
    }
           
    /**
     * Adds data elements to the data set and then fires a ChangeEvent.
     *
     * @param newElements The elements to add.
     */
    public synchronized void addDataElements(ExaminationDataElement[] newElements) {
        for (int i = 0; i < newElements.length; i++) {
            this.addDataElement(newElements[i],false); // this one is synchronized. Do not fire event (we do this last)
        }
        fireDatasetChanged();           
    }
    
    /**
     * Removes data elements from the data set and then fires a ChangeEvent.
     *
     * @param elements The data elements to remove.
     */
    public synchronized void removeDataElements(ExaminationDataElement[] elements) {
        for (int i = 0; i < elements.length; i++) {
            removeDataElement(elements[i],false);
        }
        fireDatasetChanged();
    }
    
    /**
     * Removes all data elements from the data set and then fires a ChangeEvent.
     */
    public synchronized void removeAllDataElements() {
        removeDataElements(getElements());
    }

    /**
     * Removes all selected data elements from the data set.
     */
    public synchronized void removeAllSelectedDataElements() {
        ExaminationDataElement[] elements = getElements();
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].isSelected()) {
                removeDataElement(elements[i], false);
            }
        }
        fireDatasetChanged();
    }
    
    /**
     * Removes a data element from the data set.
     *
     * @param element The element to remove.
     * @param fireEvent Whether to fire a ChangeEvent or not.
     */
    protected synchronized void removeDataElement(ExaminationDataElement element, boolean fireEvent) {
        if (dataVector.contains(element)) {
            
            // remove listeners from the element
            element.removeSelectionListener(this);
            element.removeDataGroupChangeListener(this);
            element.removeRemovalListener(this);
            
            // remove the element from the data set
            dataVector.remove(element);

            // remove the count of the element
            DataGroup dataGroup = element.getDataGroup();
            
            Integer amount = (Integer) groupToAmountTable.get(dataGroup);
            if (amount == null) {
                System.out.println("ExaminationDataSet.removeDataElement(): this shouldn't happen...");
                amount = new Integer(0);
            } else {
                amount = new Integer(amount.intValue() - 1);
            }
            if (amount.intValue() == 0) {
                groupToAmountTable.remove(dataGroup);
            } else {
                groupToAmountTable.put(dataGroup, amount);
            }
            
            // if the element is selected, remove the selection count
            if (element.isSelected()) {
                Integer selectedAmount = (Integer) groupToSelectedAmountTable.get(dataGroup);
                if (selectedAmount == null) {
                    System.out.println("ExaminationDataSet.removeDataElement(): this shouldn't happen...");
                    selectedAmount = new Integer(0);
                } else {
                    selectedAmount = new Integer(selectedAmount.intValue() - 1);
                }
                if (selectedAmount.intValue() == 0) {
                    groupToSelectedAmountTable.remove(dataGroup);
                } else {
                    groupToSelectedAmountTable.put(dataGroup, selectedAmount);                
                }
            }            
                        
            if (fireEvent) {
                fireDatasetChanged();
            }
        }
    }
    
    /** 
     * Add a data element to this set.     
     *
     * @param element The element to add
     * @param fireEvent whether to fire a changeEvent or not. It's useful to skip this for batch adds for example     
     */    
    protected synchronized void addDataElement(ExaminationDataElement element, boolean fireEvent) {
        if (dataVector.contains(element)) {
            // Do nothing
        } else {
            // add the element to the element vector            
            dataVector.add(element);
            
            // count the element
            DataGroup dataGroup = element.getDataGroup();
            
            Integer amount = (Integer) groupToAmountTable.get(dataGroup);
            if (amount == null) {
                amount = new Integer(1);
            } else {
                amount = new Integer(amount.intValue() + 1);
            }
            groupToAmountTable.put(dataGroup, amount);
            
            // if the element is selected, count the selection
            if (element.isSelected()) {
                Integer selectedAmount = (Integer) groupToSelectedAmountTable.get(dataGroup);
                if (selectedAmount == null) {
                    selectedAmount = new Integer(1);
                } else {
                    selectedAmount = new Integer(selectedAmount.intValue() + 1);
                }
                groupToSelectedAmountTable.put(dataGroup, selectedAmount);                
            }            
            
            // add listeners to the element
            element.addSelectionListener(this);
            element.addDataGroupChangeListener(this);
            element.addRemovalListener(this);
            
            if (fireEvent) {
                fireDatasetChanged();
            }
        }
    }
    
    /**
     * Adds a data element to the data set and then fires a ChangeEvent.
     *
     * @param element The element to add.
     */    
    public void addDataElement(ExaminationDataElement element) {
        addDataElement(element,true); // synchronized, Always fires event
    }    
    
    /** 
     * Export this data set to a file. NOT YET IMPLEMENTED
     *
     * @param f The File to export the data to.
     * @throws IOException When there is an IO error when exporting the data to file.
     */    
    public void exportData(java.io.File f) throws java.io.IOException {
        throw new java.io.IOException("ExaminationDataSet.exportData(File) not implemented!");
    }
    
    
    /** 
     * Get the amount of elements in this data set.
     *
     * @return The amount of elements in this data set.
     */    
    public synchronized int getElementCount() {
        return dataVector.size();
    }
    
    /**
     * Get the amount of elements in this data set in the specified data group.
     *
     * @param dataGroup The data group.
     * @return The amount of elements in the data group.
     */
    public synchronized int getElementCountInDataGroup(DataGroup dataGroup) {
        Integer count = (Integer) groupToAmountTable.get(dataGroup);
        if (count == null) {
            return 0;
        } else {
            return count.intValue();
        }
    }
    
    /**
     * Get the amount of elements of this data set that are currently selected.
     *
     * @return the amount of currently selected data elements
     */
    public synchronized int getSelectedElementCount() {
        int count = 0;
        for (Enumeration e = groupToSelectedAmountTable.elements(); e.hasMoreElements(); ) {
            Integer selectedAmount = (Integer) e.nextElement();
            count += selectedAmount.intValue();
        }
        return count;        
    }

    /**
     * Get the amount of elements of this data set in the specified data group that are currently selected.
     *
     * @param dataGroup The data group.
     * @return The amount of currently selected data elements.
     */
    public synchronized int getSelectedElementCountInDataGroup(DataGroup dataGroup) {
        Integer count = (Integer) groupToSelectedAmountTable.get(dataGroup);
        if (count == null) {
            return 0;
        } else {
            return count.intValue();
        }
    }
        
    /**
     * Returns the data groups in this data set.
     *
     * @return An array of all the data groups in this data set.
     */
    public synchronized DataGroup[] getDataGroups() {
        Set groupSet = groupToAmountTable.keySet();
        return (DataGroup[]) groupSet.toArray(new DataGroup[groupSet.size()]);
    }
    
    /**
     * Returns a new ExaminationDataElementVector containing the selected ExaminationDataElements in this dataset.
     *
     * @return An ExaminationDataElementVector containing the selected ExaminationDataElements.
     */
    public synchronized ExaminationDataElementVector getSelectedElements() {
        ExaminationDataElementVector elementVector = new ExaminationDataElementVector();
        for (Iterator i = dataVector.iterator(); i.hasNext(); ) {
            ExaminationDataElement element = (ExaminationDataElement)i.next();
            if (element.isSelected()) {
                elementVector.add(element);
            }
        }
        
        return elementVector;
    }
        
    /** 
     * Get all the elements in this data set.
     *
     * @return An array of all the elements in this data set.
     */    
    public synchronized ExaminationDataElement[] getElements()
    {
        ExaminationDataElement[] elementArray = new ExaminationDataElement[getElementCount()];        
        /*
         if (sortElements) {
            LinkedList list = new LinkedList(dataVector); // Put data in a list            
            sortExaminationDataElementList(list); // Sort the list
            elementArray = (ExaminationDataElement[]) list.toArray(elementArray);
        } else {
         */
            elementArray = (ExaminationDataElement[]) dataVector.toArray(elementArray);            
        // }
                
        return elementArray;        
    }
    
    
    /**
     * Removes a data set change listener.
     *
     * @param listener The listener to remove.
     */
    public void removeChangeListener(com.jrefinery.data.DatasetChangeListener listener) {        
        changeListeners.remove(listener);        
    }
        
    /**
     * Adds a data set change listener.
     *
     * @param listener The listener to add.
     */
    public void addChangeListener(com.jrefinery.data.DatasetChangeListener listener) {
        if (!changeListeners.contains(listener)) {
            changeListeners.add(listener);
        }
    }
    
    /**
     * Fires a DatasetChangeEvent.
     */
    protected void fireDatasetChanged() {
        for (Iterator it = changeListeners.iterator(); it.hasNext();) {
            DatasetChangeListener listener = (DatasetChangeListener) it.next();
            listener.datasetChanged(new DatasetChangeEvent(this,this));
            
        }
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
     * Adds a selection listener.
     *
     * @param listener The listener to add.
     */    
    public void addSelectionListener(SelectionListener listener) {
        if (!selectionListeners.contains(listener)) {
            selectionListeners.add(listener);
        }
    }
    
    /**
     * Fires a SelectionEvent.
     */
    protected void fireSelectionChanged() {
        for (Iterator it = selectionListeners.iterator(); it.hasNext();) {
            SelectionListener listener = (SelectionListener) it.next();
            listener.selectionChanged(new SelectionEvent(this));
            
        }
    }
    
    /**
     * Called when the selection has changed.
     *
     * @param event The object representing the event.
     */
    public void selectionChanged(SelectionEvent event) {
        ExaminationDataElement element = (ExaminationDataElement) event.getSource();
        DataGroup dataGroup = element.getDataGroup();
        
        // if the element wasn't selected, increase the selection count
        if (element.isSelected()) {
            Integer selectedAmount = (Integer) groupToSelectedAmountTable.get(dataGroup);
            if (selectedAmount == null) {
                selectedAmount = new Integer(1);
            } else {
                selectedAmount = new Integer(selectedAmount.intValue() + 1);
            }
            groupToSelectedAmountTable.put(dataGroup, selectedAmount);                
        } else { // else, decrease the selection count
            Integer selectedAmount = (Integer) groupToSelectedAmountTable.get(dataGroup);
            if (selectedAmount == null) {
                System.out.println("ExaminationDataSet.removeDataElement(): this shouldn't happen...");
                selectedAmount = new Integer(0);
            } else {
                selectedAmount = new Integer(selectedAmount.intValue() - 1);
            }
            if (selectedAmount.intValue() == 0) {
                groupToSelectedAmountTable.remove(dataGroup);
            } else {
                groupToSelectedAmountTable.put(dataGroup, selectedAmount);                
            }
        }            
        fireSelectionChanged();
    }
    
    /**
     * Called when an element has changed the data group that it belongs to.
     *
     * @param event The object representing the event.
     */    
    public void dataGroupChanged(DataGroupEvent event) {
        fireDatasetChanged();
    }

    /**
     * Called when an object is removed.
     *
     * @param event The event containing a reference to the source and the removed object.
     */
    public void objectRemoved(RemovalEvent event) {
        ExaminationDataElement element = (ExaminationDataElement) event.getRemovedObject();
        
        if (dataVector.contains(element)) {

            // remove the element from the data set
            // dataVector.removeElement(element);

            removeDataElement(element, false);
            
            // notify listeners of the element removal
            fireDatasetChanged();
        }
    }
        
}
