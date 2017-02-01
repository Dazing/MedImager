/*
 * CategoryGraphDataSet.java
 *
 * Created on July 16, 2002, 2:25 PM
 *
 * $Id: CategoryGraphDataSet.java,v 1.24 2004/07/22 14:51:00 erichson Exp $
 *
 * $Log: CategoryGraphDataSet.java,v $
 * Revision 1.24  2004/07/22 14:51:00  erichson
 * Added value length limiting to getCategories (fixes Bugzilla #383)
 *
 * Revision 1.23  2004/07/15 18:21:14  erichson
 * One line of documentation
 *
 * Revision 1.22  2004/06/24 20:10:26  erichson
 * Added [...] to length-limited fields
 *
 * Revision 1.21  2004/06/24 15:41:00  d97nix
 * Added value length limit checking.
 *
 * Revision 1.20  2004/06/01 10:58:00  erichson
 * Now sorts using TermValueComparator
 *
 * Revision 1.19  2002/10/22 15:59:12  zachrisg
 * MedViewDataHandler.getDefaultValue();
 *
 * Revision 1.18  2002/10/10 14:16:09  erichson
 * Updated with new aggregation handling
 *
 */

package medview.visualizer.data;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import com.jrefinery.data.*; // CategoryDataSet

import medview.datahandling.*; // NoSuchTermException
import medview.datahandling.aggregation.*;
import medview.visualizer.event.*;
import medview.visualizer.gui.Chart;

/**
 * Data set based on categories (each term is a category) with values.
 * Used in BarCharts and StatisticsView.
 *
 * A limit can be set to trim down the length of values, this is mainly used by BarChart to avoid too large values on the x axis.
 * the initial limit is Integer.MAX_VALUE.
 *
 * @author  Goran Zachrisson <zachrisg@mdstud.chalmers.se>, Nils Erichson <erichson@mdstud.chalmers.se>
 */
public class CategoryGraphDataSet implements CategoryDataset, DatasetChangeListener, ChangeListener {
    
    /** True if the values returned should be in percent of the number of examinations in the group. */
    private boolean percentValues = true;
    
    /** True if the dataset needs to be regenerated. */
    private boolean dataSetInvalid = true;
    
    /** The ChangeListeners listening to this dataset */
    private Vector changeListeners;

    private HashMap seriesToDataGroupMap;
    
    /**
     * The length limit for values. Initially set to MAX_INT. Limits getCategories.
     * Can be set to limit the lengths of values (so that axes don't become too large etc) 
     */
    private int categoryLengthLimit = Integer.MAX_VALUE;
    
    /** 
     * Vector containg a HashMap for each series.
     * Maps a DataGroup to a HashMap.
     * The contained HashMaps in turn maps a category to a vector of data elements.
     */
    private HashMap groupToCategoryMapMap;

    /** A hashtable with data groups as keys and the amount of examinations in the data group as values. */
    private Hashtable groupToAmountTable;
    
    /** The dataset with examinations. */
    private ExaminationDataSet examinationDataSet;
    
    /** The term that is to be displayed. */
    private String term;
    
    /** Cached categories. */
    private Vector categories;

    /** Cached visible categories. */
    private Vector visibleCategories;
    
    /** The range model holding the visible window of the dataset. */
    private BoundedRangeModel rangeModel;
    
    /** The percent of the total range that should be visible. */
    private double percentVisible = 100.0;
    
    /** The currently active aggregation */
    private Aggregation aggregation = null;
    
    /** 
     * Creates a new instance of CategoryGraphDataSet
     *
     * @param dataSet the data to include
     * @param term the term we wish to view
     * @param percentValues True if the values should be in percent of the total amount in each data group.
     */ 
    public CategoryGraphDataSet(ExaminationDataSet dataSet, String term, boolean percentValues) {

        // store the arguments
        examinationDataSet = dataSet;
        this.term = term;
        this.percentValues = percentValues;

        // add listeners
        examinationDataSet.addChangeListener(this);        
        
        // initialize variables
        changeListeners = new Vector();
        groupToCategoryMapMap = new HashMap();        
        groupToAmountTable = new Hashtable();
        categories = new Vector();
        visibleCategories = new Vector();
        rangeModel = new DefaultBoundedRangeModel(0,0,0,0);
        rangeModel.addChangeListener(this);
        // generate the data set
        rebuildDataSet();
    }
    
    /**
     * Get the elements for a series and category.
     *
     * @param series The series.
     * @param category The category.
     * @return An array of data elements belonging to the series and category. 
     */
    public ExaminationDataElement[] getElements(int series, Object category) {
        
        // get all elements belonging to the datagroup Series and value Category for the current term        
        DataGroup group = getDataGroup(series);

        HashMap categoryMap = getCategoryMap(group);
        Vector elementVector = (Vector) categoryMap.get(category);
        if (elementVector == null) {
            return new ExaminationDataElement[0];
        } else {
            return (ExaminationDataElement[]) elementVector.toArray(new ExaminationDataElement[elementVector.size()]);
        }
    }
    
    /** 
     * Fetch the correct category HashMap for a certain data group.
     *
     * @param group the DataGroup whose categories we want to look at
     * @return a HashMap mapping DataGroup to a value (Integer)
     */
    private HashMap getCategoryMap(DataGroup group) {
        HashMap categoryMap = (HashMap) groupToCategoryMapMap.get(group);
        
        // If the group did not exist previously, so create one and add it
        if (categoryMap == null) { 
            categoryMap = new HashMap();
            groupToCategoryMapMap.put(group,categoryMap);
        }
        
        return categoryMap;        
    }
    
    /**
     * Rebuilds the data from the ExaminationDataSet and the term.
     */
    private void rebuildDataSet() {
       ApplicationManager.debug("CategoryGraphDataSet: rebuilding the dataset..."); 
        
        groupToCategoryMapMap.clear();
        groupToAmountTable.clear();
        
        ExaminationDataElement[] elements = examinationDataSet.getElements();
        
        for (int i = 0; i < elements.length; i++) { // Loop through all the elements in the examinationdataset
            DataGroup dataGroup = elements[i].getDataGroup();
            Integer totalGroupAmount = (Integer) groupToAmountTable.get(dataGroup);
            if (totalGroupAmount == null) {
                totalGroupAmount = new Integer(1);
            } else {
                totalGroupAmount = new Integer(totalGroupAmount.intValue() + 1);
            }
            groupToAmountTable.put(dataGroup, totalGroupAmount);

            
            /* Get term values from each examination/data element */
            
            String[] termValues = null;
            try {
                termValues = elements[i].getValues(term,aggregation); // Get all values for this term for a patient
                if (termValues.length == 0) {
                    termValues = new String[1];
                    termValues[0] = MedViewDataHandler.instance().getDefaultValue(term);
                }
            } catch (NoSuchTermException e) {
                termValues = new String[1];
                termValues[0] = MedViewDataHandler.instance().getDefaultValue(term);
            }
            
            // Get the correct categoryMap (series)
            HashMap categoryMap = getCategoryMap(dataGroup);                
            
            for (int j = 0; j < termValues.length; j++) { // loop through all the values
                String category = termValues[j];  // Get a value to process (a category)
                Vector elementVector = (Vector) categoryMap.get(category);
                if (elementVector == null) { // It did not exist previously
                    elementVector = new Vector(); // Create new vector)
                    categoryMap.put(category, elementVector);
                }
                // add the element to the vector
                elementVector.add(elements[i]);
            } // end of value loop                                             

        } // Regeneration done

        // generate cache of the categories
        boolean existsNotApplicableValue = false;

        String notApplicableValue = MedViewDataHandler.instance().getDefaultValue(term);
        
        categories.clear(); // Empty the vector        
        for (Iterator it = groupToCategoryMapMap.keySet().iterator(); it.hasNext();) // Loop over all groups
        {
            DataGroup nextKey = (DataGroup) it.next();
            HashMap categoryMap = (HashMap) groupToCategoryMapMap.get(nextKey);                        
            
            for (Iterator i = categoryMap.keySet().iterator(); i.hasNext(); ) {
                Object category = i.next();
                if (category.equals(notApplicableValue)) {
                    existsNotApplicableValue = true;
                } else if (!categories.contains(category)) {
                    categories.add(category);
                }                
            }
        }

        // sort the categories
        Collections.sort(categories, medview.datahandling.termvalues.TermValueComparator.getInstance());
        if (existsNotApplicableValue) {
            categories.add(notApplicableValue);
        }

        ApplicationManager.debug("categories.size(): " + categories.size());
        
        // update the rangemodel
        rangeModel.removeChangeListener(this);
        updateRangeModel();
        updateVisibleCategories();
        rangeModel.addChangeListener(this);
        
        dataSetInvalid = false;
        
        fireDatasetChanged();
    }

    /**
     * Updates the rangemodel.
     */
    private synchronized void updateRangeModel() {
        int max = Math.max(categories.size() - 1, 0);
        int min = 0;
        int value = rangeModel.getValue();
        int extent = (int) (percentVisible / 100.0 * (double)max) ;
        
        // make sure the extent isn't too big
        if ( extent > (max - min) ) {
            value = 0;
            extent = max - min;
        } else if ( (value + extent) > max ) {
            value = max - extent;
        }
      
        ApplicationManager.debug("max: " + max);
        ApplicationManager.debug("min: " + min);
        ApplicationManager.debug("value: " + value);
        ApplicationManager.debug("extent: " + extent);

        rangeModel.setRangeProperties(value, extent, min, max, rangeModel.getValueIsAdjusting());                        
    }
    
    /**
     * Updates the visible categories.
     */
    private void updateVisibleCategories() {

        visibleCategories.clear();
        
        if (categories.size() > 0) {
            int extent = rangeModel.getExtent();
            int startIndex = rangeModel.getValue();
            int endIndex = startIndex + extent;
            
            for (int i = startIndex; i <= endIndex; i++) {
                visibleCategories.add(categories.get(i));
            }
        }
    }

    /**
     * Get a List of all the visible categories (in all the series).
     *
     * @return A list of all the visible categories (term names).
     */
    public java.util.List getCategories() {
        Vector newCategoryVector = new Vector(visibleCategories.size());
        for (Iterator it = visibleCategories.iterator(); it.hasNext();)
        {
            String nextCategory = (String) it.next();
            newCategoryVector.add(Chart.limitString(nextCategory, categoryLengthLimit));
        }
        return newCategoryVector;                
    }
    
    /**
     * Get the total number of categories
     *
     * @return the total number of categories
     */
    public int getCategoryCount() {
        return visibleCategories.size();        
    }
        
    /**
     * Get a category hashmap for a series (maps a category to a value).
     *
     * @param the series whose categories we want
     * @return a HashMap mapping Category name to an Integer of the value count
     */
    private HashMap getCategoryMap(int series) {
        //Object key = groupToCategoryMapMap.keySet().toArray()[series]; // ugly hack!
        DataGroup dataGroup = getDataGroup(series);
        return getCategoryMap(dataGroup);
    }
    
    /**
     * Get the number of items for a category in a series. (In visualizer: Category = Group number, series = element number)
     *
     * @param series The series to look at.
     * @param category The category whose values we're asking for.
     * @return The value (number of items) of the category in the series.
     */              
    public Number getValue(int series, Object category) {
        
        HashMap categoryMap = getCategoryMap(series);                
        Vector elementVector = (Vector) categoryMap.get(category);
        if (elementVector == null) {
            elementVector = new Vector();
        }
        
        Number value = new Integer(elementVector.size());

        if (value == null) {
            value = new Integer(0);
        } else if (! (value instanceof Integer)) {
            value = new Integer(0);
        }
        
        if (percentValues) {
            Integer totalAmount = getExaminationCount(getDataGroup(series));
            if (totalAmount.equals(new Integer(0))) {
                value = new Double(0.0); // should we return 100% ??????                
            } else {
                value = new Double(value.doubleValue() / totalAmount.doubleValue() * 100.0);
            }
        }
        return value;        
    }
    
    /**
     * Returns the number of examinations in a data group.
     *
     * @param dataGroup The data group.
     * @return The number of examinations in the data group.
     */
    public Integer getExaminationCount(DataGroup dataGroup) {
        Integer amount = (Integer) groupToAmountTable.get(dataGroup);
        if (amount == null) {
            amount = new Integer(0);
        }
        return amount;
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
     * Removes a data set change listener.
     *
     * @param listener The listener to remove.
     */
    public void removeChangeListener(com.jrefinery.data.DatasetChangeListener listener) {
        changeListeners.remove(listener);
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
     * Get the number of series.
     *
     * @return The number of series.
     */
    public int getSeriesCount() {               
        return groupToCategoryMapMap.keySet().size();
    }
    
    /**
     * Returns the name of a series.
     *
     * @param series The series.
     * @return The name of the series.
     */
    public String getSeriesName(int series) {
        return ( getDataGroup(series).getName());
    }
    
    /** 
     * Get which DataGroup that corresponds to a series.
     *
     * @param series The series.
     * @return The data group corresponding to the series.
     */
    public DataGroup getDataGroup(int series) {
        return ( (DataGroup) groupToCategoryMapMap.keySet().toArray()[series]); // EXTREMELY ugly hack
    }
    
    /**
     * Returns true if the data set is empty.
     *
     * @return True if the data set is empty.
     */
    public boolean isEmpty() {
        if (groupToCategoryMapMap.size() == 0) {  // NOTE: Is this correct?
            return true;
        } else {
            return false;
        }            
    }
    
    /**
     * Called when the ExaminationDataSet has changed.
     *
     * @param event The object representing the event.
     */    
    public void datasetChanged(DatasetChangeEvent event) {
        dataSetInvalid = true;
    }
    
    public void setAggregation(Aggregation newAggregation) {
        aggregation = newAggregation;
        dataSetInvalid = true;
        fireDatasetChanged(); // Update 
    }

    /**
     * Sets the term of the dataset.
     *
     * @param term The term of the dataset.
     */
    public void setTerm(String term) {
        this.term = term;
        dataSetInvalid = true;
    }
    
    /**
     * Returns the term of the dataset.
     *
     * @return The term.
     */
    public String getTerm() {
        return term;
    }
    
    /**
     * Sets whether or not the values should be in percent of the amount of each data group.
     *
     * @param percentValues True if the values should be in percent.
     */
    public void setPercentValues(boolean percentValues) {
        if (this.percentValues != percentValues) {
            this.percentValues = percentValues;            
        }
        fireDatasetChanged();
    }

    /**
     * Returns whether or not the values are in percent of the amount of each data group.
     *
     * @return True if the values are in percent.
     */
    public boolean isPercentValuesUsed() {
        return percentValues;
    }
    
    /**
     * Rebuilds the dataset if needed.
     */
    public void validateDataSet() {
        if (dataSetInvalid) {
            rebuildDataSet();
        }
    }
        
    /**
     * Called when the range model has changed.
     * 
     * @param event The event.
     */
    public void stateChanged(ChangeEvent event) {
        if (event.getSource() == rangeModel) {
            updateVisibleCategories();
            fireDatasetChanged();
            ApplicationManager.getInstance().validateViews();
        }
    }
    
    /**
     * Sets the size of the visible range. 
     * If the new size is too large, then the size is set to the largest possible size.
     * If the new size is less than 0, then the size is set to 0.
     *
     * @param percentVisible The percentage of the total range that should be visible.
     */
    public void setVisibleRange(double percentVisible) {
        if (percentVisible > 100.0) {
            this.percentVisible = 100.0;
        } else if (percentVisible <= 0.0) {
            this.percentVisible = 1.0;
        } else {
            this.percentVisible = percentVisible;
        }
        
        int newExtent = (int) (this.percentVisible / 100.0 * (double)categories.size());
        
        int extent = rangeModel.getExtent();
        int min = rangeModel.getMinimum();
        int max = rangeModel.getMaximum();
        int value = rangeModel.getValue();

        if (categories.size() == 0) {
            return;
        }
        
        if (newExtent > max) {
            value = 0;
            extent = max;
        } else if (newExtent < 0) {
            extent = 0;
        } else if ((newExtent + value) > max) {
            value = max - newExtent;
            extent = newExtent;
        } else {
            extent = newExtent;
        }
            
        rangeModel.setRangeProperties(value, extent, min, max, rangeModel.getValueIsAdjusting());
    }
    
    /**
     * Return the percent of the total range that is visible.
     * @return The percent of the total range that is visible.
     */
    public double getVisibleRange() {
        return percentVisible;
    }
    
    /**
     * Returns the range model.
     * 
     * @return The range model.
     */
    public BoundedRangeModel getRangeModel() {
        return rangeModel;
    }
    
    /**
     * Sets the new length limit for getCategories().
     */
    
    public void setCategoryLengthLimit(int newLimit)
    {
        if (newLimit > 0)
            categoryLengthLimit = newLimit;
    }
    
    public int getCategoryLengthLimit()
    {
        return categoryLengthLimit;
    }
    
}
