/*
 * ExaminationDataElementTableModel.java
 *
 * Created on October 3, 2002, 11:23 AM
 *
 * $Id: ExaminationDataElementTableModel.java,v 1.8 2004/12/17 11:46:59 erichson Exp $
 *
 * $Log: ExaminationDataElementTableModel.java,v $
 * Revision 1.8  2004/12/17 11:46:59  erichson
 * added getTerms()
 *
 * Revision 1.7  2004/10/19 12:36:11  erichson
 * Updated so that cells corresponding to terms w/o values are empty, instead of filling them with N/A or Default values not implemented
 *
 * Revision 1.6  2002/10/23 14:57:26  zachrisg
 * Added getAggregation().
 *
 * Revision 1.5  2002/10/23 12:41:39  zachrisg
 * Updated javadoc.
 *
 */

package medview.visualizer.gui;

import java.io.*;
import java.util.*;
import javax.swing.event.*;
import javax.swing.table.*;

import medview.datahandling.*;
import medview.datahandling.aggregation.*;
import medview.datahandling.examination.*;
import medview.visualizer.data.*;

/**
 * A table model for extracting the values of ExaminationDataElements.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class ExaminationDataElementTableModel extends AbstractTableModel {        
    
    /** The data elements. */
    private Vector[] elementVectors;
    
    /** The terms. */
    private String[] terms;
    
    /** The table model listeners. */
    private Vector tableModelListeners;
    
    /** The current aggregation */
    private Aggregation aggregation = null;
    
    /** 
     * Creates a new instance of ExaminationDataElementTableModel.
     *
     * @param terms The terms.
     * @param dataElements The data elements.
     * @param aggregation The aggregation.
     */
    public ExaminationDataElementTableModel(String[] terms, ExaminationDataElement[] dataElements, Aggregation aggregation) {
        tableModelListeners = new Vector();
        this.terms = terms;
        this.aggregation = aggregation;
        setElements(dataElements);
    }
    
    /**
     * Sets the data elements.
     *
     * @param dataElements The data elements.
     */
    public void setElements(ExaminationDataElement[] dataElements) {
        // make sure the examinations are only included once
        Hashtable elementTable = new Hashtable();
        
        ExaminationIdentifier identifier;
        for (int i = 0; i < dataElements.length; i++) {
            try {
                identifier = dataElements[i].getExaminationIdentifier();
                Vector elementVector = (Vector) elementTable.get(identifier);
                if (elementVector == null) {
                    elementVector = new Vector();
                    elementTable.put(identifier, elementVector);
                }
                elementVector.add(dataElements[i]);
            } catch (IOException e) { 
                // do nothing
            }
        }
        
        Collection vectorCol = elementTable.values();
        elementVectors = (Vector[]) vectorCol.toArray(new Vector[vectorCol.size()]);

        fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));       
    }    
    
    /**
     * Sets the terms.
     *
     * @param terms The new terms.
     */
    public void setTerms(String[] terms)
    {
        this.terms = terms;
        fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));       
    }
            
    public String[] getTerms()
    {
        return terms;
    }
    
    /**
     * Returns the number of columns.
     *
     * @return The number of columns.
     */
    public int getColumnCount() {
        return terms.length;
    }
    
    /**
     * Returns the name of a column.
     *
     * @param colum The column.
     * @return The name of a column.
     */
    public String getColumnName(int column) {
        return terms[column];
    }
    
    /**
     * Returns the number of rows.
     *
     * @return The number of rows.
     */
    public int getRowCount() {
        return elementVectors.length;
    }
    
    /**
     * Returns the examination data elements at the specified row.
     *
     * @param row The row.
     * @return The data elements at the specified row.
     */
    public ExaminationDataElement[] getElementsAt(int row) {
        Vector elementVector = elementVectors[row];
        return (ExaminationDataElement[]) elementVector.toArray(new ExaminationDataElement[elementVector.size()]);
    }
    
    /**
     * Returns the value at the specified row and column.
     *
     * @param row The row.
     * @param column The column.
     * @return The value at the specified row and column.
     */
    public Object getValueAt(int row, int column) {
        try {
            String[] values = ((ExaminationDataElement)elementVectors[row].firstElement()).getValues(terms[column],aggregation);
            // create one string of all the values                
            String concatenatedValue = new String();
            for (int v = 0; v < values.length; v++)
            {
                // if (values[v].equals(MedViewDataHandler.DEFAULT_TERM_VALUE))
                if (values[v].equals(MedViewExaminationDataElement.NA_VALUE))
                    values[v] = "";
                if (v > 0) {
                   concatenatedValue += "; " + values[v];
               } else {
                   concatenatedValue += values[v];
               }
            }
            return concatenatedValue;
        } catch (NoSuchTermException e) {
            return new String();
        }
    }            
    
    /**
     * Sets the aggregation.
     *
     * @param agg The new aggregation.
     */
    public void setAggregation(Aggregation agg) {
        aggregation = agg;
        fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));               
    }
    
    /**
     * Returns the aggregation.
     *
     * @return The aggregation.
     */
    public Aggregation getAggregation() {
        return aggregation;
    }
}
