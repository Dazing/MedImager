/*
 * Aggregation.java
 *
 * Created on September 30, 2002, 10:34 AM
 *
 * $Id: Aggregation.java,v 1.9 2005/10/21 08:36:28 erichson Exp $
 *
 * $Log: Aggregation.java,v $
 * Revision 1.9  2005/10/21 08:36:28  erichson
 * Minor cleanup.
 *
 * Revision 1.8  2004/10/08 14:04:00  erichson
 * Added DEFAULT_AGGREGATION_NAME constant
 *
 * Revision 1.7  2002/11/27 14:55:32  erichson
 * Fixed bug: getValues would return group instead of value
 *
 * Revision 1.6  2002/11/08 15:35:01  erichson
 * added getMVGfile, changed loadMVGfile to setMVGfile to complete the bean pattern
 *
 * Revision 1.5  2002/11/07 15:07:21  erichson
 * disallowed termToAggregateMap = null, since this is too error-prone
 *
 * Revision 1.4  2002/11/04 16:54:47  erichson
 * Added documentation, tree/groups fetching methods (these are slow)
 *
 * Revision 1.3  2002/10/10 15:21:52  erichson
 * More constructors now available
 *
 * Revision 1.2  2002/10/10 14:49:11  erichson
 * added name field (and methods) and toString
 *
 * Revision 1.1  2002/09/30 12:01:22  erichson
 * First check-in
 *
 */

package medview.datahandling.aggregation;

import java.util.*; // HashMap etc
import java.io.*; // File etc

import medview.aggregator.GroupNode;

/**
 * A class for easy access of aggregations. You can load an MVG file and then ask for aggregations of (term,value).
 *
 * An aggregation is a hierarchical structure: Aggregation name -> term -> grouping name -> values that map to this grouping
 *
 * Internally, the aggregation is represented as two layers of HashMaps: The first (termToAggregateMap) maps a term to another HashMap. This second hashmap
 * maps values to their groupings (aggregations).
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */

public class Aggregation {
    
    /* Constants */
    
    public static final String DEFAULT_AGGREGATION_NAME = "New aggregation";
     
    
    /* Fields */
    
    /** The name of this aggregation */
    private String name;
    
    /** HashMap which maps a term to a hashmap (which maps a value to a grouping) */
    private HashMap termToAggregateMap;
    
    /** Creates a new instance of Aggregation */
    public Aggregation(String name) {        
        this.name = name;
        //termToAggregateMap = null; // change in funcionality 02-11-05
        termToAggregateMap = new HashMap();
    }
    
    private File sourceMVGFile = null;
    
    /**
     * constructs a new Aggregation
     */
    public Aggregation() {
        this("Untitled");        
    }
    
    /**
     * Create a new Aggregation object using information from an MVG File
     */
    public Aggregation(File MVGFile) throws IOException {
        this();
        setMVGFile(MVGFile);        
    }
    
    /**
     * Get the aggregation of a value
     * @param term the term whose aggregation to fetch
     * @param value the value whose aggregation to fetch
     */
    public String getAggregatedValue(String term, String value) 
    {
        if ((termToAggregateMap) == null)
            return value;
        else 
        {
            
            // Get HashMap for that term
            HashMap valueToGroupMap = (HashMap) termToAggregateMap.get(term);
            if (valueToGroupMap == null) { // no valueToGroupMap for that term
                // System.out.println("no mappings for " + term);
                return value;
            } else {
                             
                // get grouping for that value
                String aggregatedValue = (String) valueToGroupMap.get(value);
                
                if (aggregatedValue == null) 
                { 
                    // no grouping for that value
                    aggregatedValue = value;
                } /*else {
                    // System.out.println(term+","+value+" aggregated to " + aggregatedValue); // TODO: Debug
                }*/
                return aggregatedValue;
            }
        }
    }
    
    /**
     * Gets the terms in this aggregation.
     */ 
    public String[] getTerms() {
        Set terms = termToAggregateMap.keySet();
        if (terms == null) {
            return new String[0];
        }
        
        String[] termStringArray = new String[terms.size()];
        termStringArray = (String[]) terms.toArray(termStringArray);
        
        return termStringArray;
    }
    
    /**
     * Gets the groups for a term 
     * This is slow since this is the reverse of how this data structure works, but that's OK since this is not meant to be done frequently.
     */
    public String[] getGroups(String term) {
        HashMap valueToGroupMap = (HashMap) termToAggregateMap.get(term);
        
        Set values = (Set) valueToGroupMap.keySet();
        HashSet groups = new HashSet();
        for (Iterator it = values.iterator(); it.hasNext(); ) {
            String value = (String) it.next();
            String group = (String) valueToGroupMap.get(value);
            groups.add(group);
        }
        
        String[] stringArray = new String[groups.size()];
        
        stringArray = (String[]) groups.toArray(stringArray);
        return stringArray;
        
    }
    
    /**
     * Gets the values for a term and a group.
     * This is slow since this is the reverse of how this data structure works, but that's OK since this is not meant to be done frequently.
     */
    public String[] getValues(String term, String group) {
        HashMap valueToGroupMap = (HashMap) termToAggregateMap.get(term);
        
        Set values = (Set) valueToGroupMap.keySet(); // get the set of values
        HashSet matchingValues = new HashSet();
        for (Iterator it = values.iterator(); it.hasNext(); ) {            
            String value = (String) it.next();                      
            String valueGroup = (String) valueToGroupMap.get(value); 
            if (valueGroup.equals(group))
                matchingValues.add(value);
        }
        
        String[] stringArray = new String[matchingValues.size()];
        
        stringArray = (String[]) matchingValues.toArray(stringArray);
        return stringArray;
        
    }
    
    /**
     * Gets the name of this aggregation
     */
    public String getName() {
        return name;
    }
    
    public void setName(String newName) {
        name = newName;
    }
    
    /**
     * Gets the source MVGfile for this Aggregation
     */
    public File getMVGFile() {
        return sourceMVGFile;
    }
           
    /**
     * Load an MVG file into this Aggregation. This will replace the current aggregation.
     */
    private void setMVGFile(File MVGFile) throws IOException 
    {                
        
        termToAggregateMap = new HashMap();
        
        // Use nader's code to load a GroupTreeUI
        medview.aggregator.CategoryReader categoryReader = new medview.aggregator.CategoryReader();
        // categoryReader.readCategory(aFile,new JScrollPane() );
        medview.aggregator.GroupTreeUI categoryTree = categoryReader.readCategory(MVGFile); // getCategory(); // returns a GroupTreeUI
        
        // get tree model and extract aggregates info from it
        javax.swing.tree.TreeModel model = categoryTree.getTreeModel();
        Object rootNode = model.getRoot();
        for (int term = 0; term < model.getChildCount(rootNode); term++) {
            medview.aggregator.GroupNode termNode = (medview.aggregator.GroupNode) model.getChild(rootNode,term); // get term'th term node
            String termName = (String) termNode.getUserObject();
            // Now we have a term, get the value-to-group mapping
            
            HashMap valueToGroupMap = (HashMap) termToAggregateMap.get(termName);
            
            // Create map and store it if it doesn't already exist
            if (valueToGroupMap == null) {
                valueToGroupMap = new HashMap();
                termToAggregateMap.put(termName,valueToGroupMap);
            }
            
            for (int group = 0; group < model.getChildCount(termNode); group++) {
                GroupNode groupNode = (GroupNode) model.getChild(termNode,group); // get group node
                String groupName = (String) groupNode.getUserObject();
                // System.out.println("Processing group " + groupName);
                for (int value = 0; value < model.getChildCount(groupNode); value++) {
                    GroupNode valueNode = (GroupNode) model.getChild(groupNode,value); // get value node
                    String valueName = (String) valueNode.getUserObject();
                    valueToGroupMap.put(valueName,groupName);
                    // System.out.println("New aggregate! " + termName + ", " + valueName + " to " + groupName);
                }
            }            
        } // end term loop
        
        
        // Debug info
        /*
        Set terms = termToAggregateMap.keySet();
        for (Iterator it = terms.iterator(); it.hasNext();) {
            String term = (String) it.next(); // next term
            System.out.println("Term: " + term);
            // Get map for this term
         
            HashMap valueToGroupMap = (HashMap) termToAggregateMap.get(term);
         
            Set values = valueToGroupMap.keySet();
            for (Iterator it2 = values.iterator(); it2.hasNext();) {
                String value = (String) it2.next();
                System.out.println(value + " = " + valueToGroupMap.get(value));
            }
        }*/
        sourceMVGFile = MVGFile;
        name = new StringTokenizer(MVGFile.getName(),".").nextToken(); // Set name to the part before the first period
        System.out.println("Loaded MVG file: " + MVGFile.getPath());
    } // end load
    
    public String toString() {
        return getName();
    }
    
} // end class




