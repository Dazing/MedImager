/*
 * XYGraphDataSet.java
 *
 * Created on June 27, 2002, 3:51 PM
 *
 * $Id: XYGraphDataSet.java,v 1.33 2004/07/22 14:56:59 erichson Exp $
 *
 * $Log: XYGraphDataSet.java,v $
 * Revision 1.33  2004/07/22 14:56:59  erichson
 * Removed value length limiting (now done in graph, not in data set)
 *
 * Revision 1.32  2004/07/15 18:12:46  erichson
 * added addSideAxisValues which fixes bugzilla bug 275 (some scatterplot blobs are painted  partially outside graph)
 *
 * Revision 1.31  2004/06/24 20:26:37  erichson
 * forgot to implement setValueLengthLimit in last check-in - fixed.
 *
 * Revision 1.30  2004/06/24 20:21:24  erichson
 * Added length-limiting support.
 *
 * Revision 1.29  2004/06/01 11:33:09  erichson
 * Updated to use TermValueComparator for sorting/ordering
 *
 * Revision 1.28  2002/11/25 16:05:24  zachrisg
 * Removed bug in getSortedValues() that caused a NullPointerException to
 * be thrown.
 *
 * Revision 1.27  2002/10/30 09:55:26  zachrisg
 * Massive javadoc clean up.
 *
 * Revision 1.26  2002/10/22 16:00:35  zachrisg
 * Now uses MedViewDataHandler.getDefaultValue();
 *
 * Revision 1.25  2002/10/22 15:51:08  zachrisg
 * Now uses MedViewDataHandler.getDefaultValue()
 *
 * Revision 1.24  2002/10/10 15:22:30  erichson
 * setAggregates now invalidates and fires datachanged (it did not before, so setAggregates did not affect the graph)
 *
 * Revision 1.23  2002/10/10 14:46:20  erichson
 * Added support for the new aggregation methods
 *
 */

package medview.visualizer.data;

import java.util.*;
import java.awt.*; // Point
import javax.swing.event.*; // ChangeListener
import com.jrefinery.data.*;

import medview.datahandling.*;
import medview.datahandling.aggregation.*;
import medview.datahandling.NoSuchTermException;

/**
 * A data set for scatterplots.
 * XYGraphDataSet observes ExaminationDataSet (for data elements change)
 *
 * @author  d97nix
 */
public class XYGraphDataSet implements SeriesDataset,XYDataset,XisSymbolic,YisSymbolic, DatasetChangeListener {

    /** True if the values returned should be in percent of the number of examinations in the group. */
    private boolean percentValues = true;
                   
    /** The ExaminationDataSet that this data set observes. */
    private ExaminationDataSet examinationDataSet;
    
    /** True if the data set needs to be rebuilt. */
    private boolean dataSetInvalid = true;
    
    /** The ChangeListeners */
    private Vector changeListeners;
    
    private Vector graphItemVector;
    
    /** Values for the x and y terms */
    private String[] xAxisValues,yAxisValues;
    
    /** Value mappings for the x axis */
    private Map xMapping;
    
    /** Value mappings for the y axis */
    private Map yMapping;
    
    /** A hashtable with the data groups as keys and the number of examinations (Integer) as values. */
    private Hashtable groupToAmountTable;
    
    /** Current term for the x axis */
    private String xAxisTerm;
    
    /** Current term for the y axis */
    private String yAxisTerm;
    
    /** Current aggregation */
    private Aggregation aggregation = null;    
    
    /** 
     *  Creates a new XYGraphDataSet.
     *
     * @param dataSet The ExaminationDataSet that the XYGraphDataSet should observe for changes.
     * @param xTerm Term for the x-axis.
     * @param yTerm Term for the y-axis.
     * @param percentValues True if values should be in percent.
     */
    public XYGraphDataSet(ExaminationDataSet dataSet, String xTerm, String yTerm, boolean percentValues) {
        ApplicationManager.debug("Constructing XYGraphdataset with xterm= " + xTerm + ", yterm="+yTerm);
        
        this.percentValues = percentValues;
        
        examinationDataSet = dataSet;
        
        examinationDataSet.addChangeListener(this);
        
        changeListeners = new Vector();        
        graphItemVector = new Vector();        
        
        xMapping = new LinkedHashMap();
        yMapping = new LinkedHashMap();            
        
        xAxisTerm = xTerm;
        yAxisTerm = yTerm;                              
        
        groupToAmountTable = new Hashtable();
        
        rebuildDataSet();
    }        
            
    /** 
     * Regenerates mappings.
     */
    private void rebuildDataSet() {
        ApplicationManager.debug("Regenerating point data");
        createMappings();

        Hashtable yValueTable = new Hashtable();
        groupToAmountTable.clear();
        
        ExaminationDataElement[] examinations = examinationDataSet.getElements();
        
        // Loop through all examinations
        for (int exam = 0; exam < examinations.length; exam++) {
            // generate the groupToAmountTable
            DataGroup dataGroup = examinations[exam].getDataGroup();
            Integer totalGroupAmount = (Integer) groupToAmountTable.get(dataGroup);
            if (totalGroupAmount == null) {
                totalGroupAmount = new Integer(1);
            } else {
                totalGroupAmount = new Integer(totalGroupAmount.intValue() + 1);
            }
            groupToAmountTable.put(dataGroup, totalGroupAmount);
            
            // Make points from this examination
            PointWithElement[] points = makePoints(examinations[exam]); 
            for (int p = 0; p < points.length; p++) {
                PointWithElement point = points[p];
                Hashtable xValueTable = (Hashtable) yValueTable.get(point.getY()); // Store each point in hastable (
                if (xValueTable == null) {
                    xValueTable = new Hashtable();
                    yValueTable.put(point.getY(), xValueTable);
                }
                Hashtable groupTable = (Hashtable) xValueTable.get(point.getX());
                if (groupTable == null) {
                    groupTable = new Hashtable();
                    xValueTable.put(point.getX(), groupTable);
                }
                Vector elementVector = (Vector) groupTable.get(point.getElement().getDataGroup());
                if (elementVector == null) {
                    elementVector = new Vector();
                    groupTable.put(point.getElement().getDataGroup(), elementVector);
                }
                elementVector.add(point.getElement());
            }
        }   
        
        // generate graphItemVector
        graphItemVector.clear();
        
        Set yValueSet = yValueTable.keySet(); // Set of integers
        for (Iterator yIter = yValueSet.iterator(); yIter.hasNext(); ) {
            Integer yValue = (Integer) yIter.next();
            
            Hashtable xValueTable = (Hashtable) yValueTable.get(yValue);
            Set xValueSet = xValueTable.keySet();
            for (Iterator xIter = xValueSet.iterator(); xIter.hasNext(); ) {
                Integer xValue = (Integer) xIter.next();
                
                Hashtable groupTable = (Hashtable) xValueTable.get(xValue);
                Set groupSet = groupTable.keySet();
                
                double percentScale = 100.0;
                
                double totalSize = 0.0;
                for (Iterator groupIter = groupSet.iterator(); groupIter.hasNext(); ) {
                    DataGroup group = (DataGroup) groupIter.next();
                    Vector elementVector = (Vector) groupTable.get(group);
                    if (percentValues) {
                        Integer groupAmount = (Integer) groupToAmountTable.get(group);
                        if (groupAmount.intValue() != 0) {
                            totalSize += percentScale * ((double)elementVector.size()) / groupAmount.doubleValue();
                        }
                    } else {
                        totalSize += elementVector.size();
                    }
                }
                
                double pos = 0.0;
                for (Iterator groupIter = groupSet.iterator(); groupIter.hasNext(); ) {
                    DataGroup group = (DataGroup) groupIter.next();
                    
                    Vector elementVector = (Vector) groupTable.get(group);
                    double size = elementVector.size();
                    if (percentValues) {
                        Integer groupAmount = (Integer) groupToAmountTable.get(group);
                        if (groupAmount.intValue() != 0) {
                            size = percentScale * size / groupAmount.doubleValue();
                        } else {
                            size = 0.0;
                        }
                    }
                    GraphItem graphItem = new GraphItem(pos, size, totalSize, xValue, yValue, group, elementVector);
                    graphItemVector.add(graphItem);
                    pos += size;
                }
            }
        }
        
        
        dataSetInvalid = false;
        fireDatasetChanged();
    }
    
    /**
     * Use the x- and y-mappings to create Point representations of the element derived from the currently selected x and y attributes.
     *
     * @param element The ExaminationDataElement to create Point representations of.
     * @return An array of points representing the ExaminationDataElement according to the x- and y-mappings.
     */
    private PointWithElement[] makePoints(ExaminationDataElement element) {
        
        Vector pointVector;
        String[] xValues;
        String[] yValues;
        
        pointVector = new Vector();
        try {
            xValues = element.getValues(xAxisTerm,aggregation);
            if (xValues.length == 0) {
                xValues = new String[1];
                xValues[0] = MedViewDataHandler.instance().getDefaultValue(xAxisTerm);
            }
        } catch (NoSuchTermException e) {
            xValues = new String[1];
            xValues[0] = MedViewDataHandler.instance().getDefaultValue(xAxisTerm);;
        }
        try {
            yValues = element.getValues(yAxisTerm,aggregation);
            if (yValues.length == 0) {
                yValues = new String[1];
                yValues[0] = MedViewDataHandler.instance().getDefaultValue(yAxisTerm);;
            }
        } catch (NoSuchTermException e) {
            yValues = new String[1];
            yValues[0] = MedViewDataHandler.instance().getDefaultValue(yAxisTerm);;
        }                
        
        if (xValues.length > yValues.length) 
        {
            for (int xCount = 0; xCount < xValues.length; xCount++) {
                for (int yCount = 0; yCount < yValues.length; yCount++) {
                    Integer x = (Integer)xMapping.get(xValues[xCount]); // Get 
                    Integer y = (Integer)yMapping.get(yValues[yCount]); 
                                        
                    pointVector.add(new PointWithElement(element, x, y));
                }
            }
        } else {
            for (int yCount = 0; yCount < yValues.length; yCount++) {
                for (int xCount = 0; xCount < xValues.length; xCount++) {                    
                    Integer x = (Integer)xMapping.get(xValues[xCount]);                    
                    Integer y = (Integer)yMapping.get(yValues[yCount]);
                                        
                    pointVector.add(new PointWithElement(element, x,y));
                    
                }                
            }
        }

        // convert the pointVector to an array that we can return
        PointWithElement[] points = new PointWithElement[pointVector.size()];
        for (int i = 0; i < pointVector.size(); i++) {
            points[i] = (PointWithElement)pointVector.elementAt(i);
        }
        
        return points;
    }
    
        
    /**
     * Used to "fix" jfreeChart 0.9.3 since some dots are painted partly outside the graph. We solve it by adding two empty (dummy) values
     * at the edges.
     */
    private String[] addSideAxisValues(String[] originalAxisValues)
    {
        String[] newAxisValues = new String[originalAxisValues.length + 2];
        for(int i = 0; i < originalAxisValues.length; i++)
        {
            newAxisValues[i+1] = originalAxisValues[i];
        }
        newAxisValues[0] = new String(" ");
        newAxisValues[newAxisValues.length - 1] = new String(" ");
        
        return newAxisValues;
    }
    
    /**
     * Recreate the x- and y-mappings
     */
    private void createMappings() {                
        
        ApplicationManager.debug("Updating mappings");
        
        ExaminationDataElement[] examinations = examinationDataSet.getElements();        
        
        String[] backup;
        
        xAxisValues = getSortedValues(examinations,xAxisTerm); // Get all possible values for the term on the x axis
        xAxisValues = addSideAxisValues(xAxisValues);
        
        
        //limitStrings(xAxisValues);
        
        if (xAxisValues == null) {
            System.err.println("null xAxisValues for " + xAxisTerm);
            xAxisValues = new String[0];
        }
        
        // check for null xAxisValues (weird)
        xMapping = createMapping(xAxisValues);         
                    
        yAxisValues = getSortedValues(examinations,yAxisTerm); // Get all possible values for the term on the y axis                        
        yAxisValues = addSideAxisValues(yAxisValues);
        
        //limitStrings(yAxisValues);
        
        if (yAxisValues == null) {
            System.err.println("null yAxisValues for " + yAxisTerm);
            yAxisValues = new String[0];
        }
        
        yMapping = createMapping(yAxisValues);
        

    }
    

    /**
     * Subfunction of createMappings().
     * Maps the specified values to a unique Integer.
     *
     * @param values The values to map.
     * @return A linked hashmap with the values as the keys to unique Integers.
     */
    private LinkedHashMap createMapping(String[] values) {                
        
        int keyAmount = values.length;
        
        LinkedHashMap mapping = new LinkedHashMap();
        
        for (int i = 0; i < keyAmount; i++) { 
            mapping.put(values[i],new Integer(i));
        }
        
        return mapping;        
    }
             
    /**
     * Returns the x-value of an item.
     *
     * @param series Not used at the moment.
     * @param item The item.
     * @return The x-value of the item.
     */
    public java.lang.Number getXValue(int series, int item) {
        GraphItem graphItem = (GraphItem) graphItemVector.get(item);
        return graphItem.getXValue();
    }

    /**
     * Returns the y-value of an item.
     *
     * @param series Not used at the moment.
     * @param item The item.
     * @return The y-value of the item.
     */
    public java.lang.Number getYValue(int series, int item) {
        GraphItem graphItem = (GraphItem) graphItemVector.get(item);
        return graphItem.getYValue();
    }
    
    /**
     * Returns the symbolic y-value of an item.
     *
     * @param series Not used at the moment.
     * @param item The item.
     * @return The symbolic y-value of the item.
     */
    public java.lang.String getYSymbolicValue(int series, int item) {
        GraphItem graphItem = (GraphItem) graphItemVector.get(item);
        return yAxisValues[graphItem.getYValue().intValue()];
    }
    
    /**
     * Returns the symbolic x-value of an item.
     *
     * @param series Not used at the moment.
     * @param item The item.
     * @return The symbolic x-value of the item.
     */
    public java.lang.String getXSymbolicValue(int series, int item) {
        GraphItem graphItem = (GraphItem) graphItemVector.get(item);
        return xAxisValues[graphItem.getXValue().intValue()];
    }
    
    /**
     * Returns the symbolic value of a y-value.
     *
     * @param val The y-value.
     * @return The symbolic value of the y-value.
     */
    public java.lang.String getYSymbolicValue(java.lang.Integer val) {        
        return yAxisValues[val.intValue()];
    }
    
    /**
     * Returns the symbolic value of an x-value.
     *
     * @param val The x-value.
     * @return The symbolic value of the x-value.
     */
    public java.lang.String getXSymbolicValue(java.lang.Integer val) {
        return xAxisValues[val.intValue()];
    }
    
    /**
     * Get symbolic values of the x-axis values.
     *
     * @return Symbolic values of the x-axis values.
     */
    public java.lang.String[] getXSymbolicValues() {
        return xAxisValues;
    }

    /**
     * Get symbolic values of the y-axis values.
     *
     * @return Symbolic values of the y-axis values.
     */
    public java.lang.String[] getYSymbolicValues() {
        return yAxisValues;
    }
    
    /**
     * Private class for storing a point and an element together
     */
    private class PointWithElement {
        ExaminationDataElement element;
        Integer x;
        Integer y;
        
        /**
         * Creates a new PointWithElement.
         *
         * @param elem The element.
         * @param x The x-coordinate of the point.
         * @param y The y-coordinate of the point.
         */
        public PointWithElement(ExaminationDataElement elem, Integer x, Integer y) {
            this.x = x;
            this.y = y;
            element = elem;
            if ( (x == null) || (y == null) ) {
                System.err.println("Constructor PointWithElement() received null in x or y.");
            }
        }
        
        /**
         * Returns the element.
         *
         * @return The element.
         */
        public ExaminationDataElement getElement() {
            return element;
        }
        
        /**
         * Returns the x-coordinate of the point.
         *
         * @return The x-coordinate.
         */
        public Integer getX() {
            return x;
        }
        
        /**
         * Returns the y-coordinate of the point.
         *
         * @return The y-coordinate.
         */
        public Integer getY() {
            return y;
        }        
    }
    
    /**
     * Private class for information about an item in the graph.
     */
    public class GraphItem {
    
        private double pos;
        private double size;
        private double totalSize;
        private Integer xValue;
        private Integer yValue;
        private DataGroup dataGroup;
        private Vector elementVector;
        
        /**
         * Creates a new GraphItem.
         *
         * @param pos The position of the GraphItem of the totalSize.
         * @param size The size of the GraphItem.
         * @param totalSize The sum of the sizes of all GraphItems with this xValue and yValue.
         * @param xValue The x-value.
         * @param yValue The y-value.
         * @param dataGroup The data group that this GraphItem represents.
         * @param elementVector The elements of this data group with this x-value and y-value.
         */
        public GraphItem(double pos, double size, double totalSize, Integer xValue, Integer yValue, DataGroup dataGroup, Vector elementVector) {
            this.pos = pos;
            this.size = size;
            this.totalSize = totalSize;
            this.xValue = xValue;
            this.yValue = yValue;
            this.dataGroup = dataGroup;
            this.elementVector = elementVector;
        }
        
        /**
         * Returns the position.
         *
         * @return The position.
         */ 
        public double getPos() { return pos; }
        
        /**
         * Returns the size.
         *
         * @return The size.
         */
        public double getSize() { return size; }
        
        /**
         * Returns the sum of the sizes of all GraphItems with this x-value and y-value.
         *
         * @return The sum of all GraphItems' sizes.
         */
        public double getTotalSize() { return totalSize; }
        
        /**
         * Returns the x-value of this GraphItem.
         *
         * @return The x-value.
         */
        public Integer getXValue() { return xValue; }
        
        /**
         * Returns the y-value of this GraphItem.
         *
         * @return The y-value.
         */
        public Integer getYValue() { return yValue; }
        
        /**
         * Returns the data group represented by this GraphItem.
         *
         * @return The data group.
         */
        public DataGroup getDataGroup() { return dataGroup; }
        
        /**
         * Returns a vector of the elements in this data group with this x-value and y-value.
         *
         * @return A vector of the elements of this GraphItem.
         */
        public Vector getElements() { return elementVector; }
    }
    
    /**
     * Get all the values for a term, in an alphabetically sorted order
     *
     * @param examinations The examinations to extract the values from.
     * @param term The term to extract the values for.
     * @return An array of sorted values.
     */
    protected String[] getSortedValues(ExaminationDataElement[] examinations, String term)  {
               
        HashSet valueSet = new HashSet();
        
        // Add values for this term for all examinations
        for (int exam_no = 0; exam_no < examinations.length; exam_no++) {
            // Add all values from this examination (examinations[i])            
                
                try { 
                    String[] values = examinations[exam_no].getValues(term,aggregation);    // Get all values for this term in this examination

                    for (int value_no = 0; value_no < values.length; value_no++) { // for every value
                        valueSet.add(values[value_no]);
                    }
                    
                    if (values.length == 0) {
                        valueSet.add(MedViewDataHandler.instance().getDefaultValue(term));
                    }
                    
                    // System.out.println("Values for " + term + " for this examination: " + Arrays.asList(values));
                    
                } catch (NoSuchTermException nste) { // Term did not exist in examination
                    valueSet.add(MedViewDataHandler.instance().getDefaultValue(term)); // add n/a instead
                }
                
        } // one examination done

        // Now all the values exist as keys in the hashtable. Sort them.
        Vector keyVector = new Vector(valueSet);
        Collections.sort(keyVector, medview.datahandling.termvalues.TermValueComparator.getInstance());
        
        // System.out.println("After sort: " + keyVector);
        
        /*
        if (noValueForTerm) {
            // add the "no-value value"
            keyVector.add(DataManager.getInstance().getNotApplicableValue());
        }
        
         */
         
        String[] values = new String[keyVector.size()];
        values = (String[]) keyVector.toArray(values);
        
        // System.out.println("Returning values: " + Arrays.asList(values));
        return values;
    }
    
    /**
     * Returns the number of items in the data set.
     *
     * @param series Not used at the moment.
     * @return The number of items in the data set.
     */
    public int getItemCount(int series) {
        return graphItemVector.size();
    }
    
    /**
     * Returns true if there are no items in the data set.
     *
     * @return True if there are no items in the data set.
     */
    public boolean isEmpty() {
        if (getItemCount(0) == 0) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Returns the number of series in the data set, currently always 1.
     *
     * @return Always 1.
     */
    public int getSeriesCount() {
        return 1; // Never more than one series, since we are not compatible with JFreechart's series handling anyway (we use our own renderer)
    }
    
    /**
     * Returns the name of a series.
     *
     * @param The series.
     * @return A name of the series, not used anywhere.
     */
    public String getSeriesName(int series) {
        return "XYplot series " + series;
    }
    
    /**
     * Returns the data group of an item. Note that all elements of an item is in the same data group.
     *
     * @param item The item.
     * @return The data group of an item.
     */
    public DataGroup getDataGroup(int item) {
        return ((GraphItem) graphItemVector.get(item)).getDataGroup();
    }
    
    /**
     * Returns a GraphItem for an item.
     *
     * @param item The item.
     * @return A GraphItem for the item.
     */
    public GraphItem getGraphItem(int item) {
        return (GraphItem) graphItemVector.get(item);
    }
    
    /**
     * Returns the elements of the specified item.
     *
     * @param series Not used at the moment.
     * @param item The item.
     * @return The elements of the item.
     */ 
    public ExaminationDataElement[] getElements(int series,int item) {
        Vector elementVector = ((GraphItem) graphItemVector.get(item)).getElements();        
        ExaminationDataElement[] elements = new ExaminationDataElement[elementVector.size()];
        return (ExaminationDataElement[]) elementVector.toArray(elements);
    }

    /**
     * Removes a DatasetChangeListener.
     *
     * @param listener The listener to remove.
     */
    public void removeChangeListener(DatasetChangeListener listener) {        
        changeListeners.remove(listener);        
    }
        
    /**
     * Adds a DatasetChangeListener.
     *
     * @param listener The listener to add.
     */
    public void addChangeListener(DatasetChangeListener listener) {
        if (!changeListeners.contains(listener)) {
            changeListeners.add(listener);
        }
    }
    
    /**
     * Notifies the DatasetChangeListeners that the data set has changed.
     */ 
    protected void fireDatasetChanged() {
        for (Iterator it = changeListeners.iterator(); it.hasNext();) {
            DatasetChangeListener listener = (DatasetChangeListener) it.next();
            listener.datasetChanged(new DatasetChangeEvent(this,this));
            
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

    /**
     * Validates the data set and rebuilds it if needed.
     */
    public void validateDataSet() {
        if (dataSetInvalid) {
            rebuildDataSet();
        }
    }
    
    /**
     * Sets the current aggregation and notifies listeners that
     * the data set has changed.
     *
     * @param newAggregation The new aggregation to use.
     */
    public void setAggregation(Aggregation newAggregation) {
        aggregation = newAggregation;
        dataSetInvalid = true;
        fireDatasetChanged();
    }
    
    /**
     * Sets the x-axis and y-axis terms and regenerates the data set.
     *
     * @param xTerm The term on the x-axis.
     * @param yTerm The term on the y-axis.
     */
    public void setTerms(String xTerm, String yTerm) {
        xAxisTerm = xTerm;
        yAxisTerm = yTerm;
        dataSetInvalid = true;        
    }

    
    /**
     * Sets whether or not the values should be in percent of the amount of each data group.
     *
     * @param percentValues True if the values should be in percent.
     */
    public void setPercentValues(boolean percentValues) {
        if (this.percentValues != percentValues) {
            this.percentValues = percentValues;            
            dataSetInvalid = true;
            fireDatasetChanged();
        }
    }

    /**
     * Returns whether or not the values are in percent of the amount of each data group.
     *
     * @return True if the values are in percent.
     */
    public boolean isPercentValuesUsed() {
        return percentValues;
    }            
}
