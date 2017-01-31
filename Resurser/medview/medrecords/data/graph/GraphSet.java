/*
 * GraphCollection.java
 *
 * Created on den 7 juni 2005, 12:16
 *
 * $Id: GraphSet.java,v 1.2 2005/06/08 09:47:36 erichson Exp $
 *
 * $Log: GraphSet.java,v $
 * Revision 1.2  2005/06/08 09:47:36  erichson
 * Updated dump after adding ordered attributes.
 *
 * Revision 1.1  2005/06/07 17:54:23  erichson
 * First check-in.
 *
 */

package medview.medrecords.data.graph;

/**
 * Set of several graphs.
 *
 * @author Nils Erichson
 */

import java.util.*;

public class GraphSet 
{
    private HashMap availableGraphsMap; // Maps name to Graph

    /** Creates a new instance of GraphCollection */
    {
        availableGraphsMap = new HashMap();
    }
    
    /**
     * Gets names of available graphs
     */
    public String[] getAvailableGraphs()
    {
        Set keys = availableGraphsMap.keySet();
        String[] strings = new String[keys.size()];
        strings = (String[]) keys.toArray(strings);
        return strings;
    }
    
    public GraphInfo getGraph(String name)
    {
        return (GraphInfo) availableGraphsMap.get(name);
    }
    
    /**
     * Returns whether a graph named 'name' has been stored as availableGraph or not
     */
    public boolean graphExists(String name)
    {
        return availableGraphsMap.containsKey(name);
    }
    
    public void put(String name, GraphInfo graph)
    {
        availableGraphsMap.put(name,graph);
    }        
    
    public void putGraph(String name, GraphInfo graph) { put(name,graph); }
    
    // Debug method
    public void dump()
    {
        String[] availableGraphs = getAvailableGraphs();
        for (int g = 0; g < availableGraphs.length; g++)
        {
            System.out.println("Graph: " + availableGraphs[g]);
            
            GraphInfo info = getGraph(availableGraphs[g]);
            System.out.println("Title: " + info.graphTitle + 
                               ", maxvalue: " + info.maxValue +
                               ", fixMaxValue: " + info.usesFixMaxValue);
            
            System.out.println("Attributes: ");
            
            String[] attrs = info.getAttributes();
            
            for(int i = 0; i < attrs.length; i++)
            {
                String nextKey = attrs[i];
                String nextLabel = info.getAttributeLabel(nextKey);
                System.out.println("\t" + nextKey + " => " + nextLabel);
            }                        
        }
    }
}
