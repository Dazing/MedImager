/*
 * $Id: AggregationContainer.java,v 1.1 2004/10/18 10:52:06 erichson Exp $
 *
 * Created on den 15 oktober 2004, 10:09
 *
 * $Log: AggregationContainer.java,v $
 * Revision 1.1  2004/10/18 10:52:06  erichson
 * First check-in
 *
 */

package medview.datahandling.aggregation;

/**
 * Simple base interface for all classes that contain aggregations, for example the DataManager
 * in MVisualizer.
 * Used to generalize aggregation-related classes, such as AggregationLibrary
 *
 * @author Nils Erichson
 */
public interface AggregationContainer {
    
    public Aggregation[] getAggregations();
    
    public void setAggregations(Aggregation[] aggregations);
    
}
