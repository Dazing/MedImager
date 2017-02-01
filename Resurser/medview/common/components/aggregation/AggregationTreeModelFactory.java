/*
 * AggregationTreeModelFactory.java
 *
 * Created on November 4, 2002, 3:32 PM
 *
 * $Id: AggregationTreeModelFactory.java,v 1.1 2002/11/07 16:38:35 erichson Exp $
 *
 * $Log: AggregationTreeModelFactory.java,v $
 * Revision 1.1  2002/11/07 16:38:35  erichson
 * First check-in
 *
 */

package medview.common.components.aggregation;

import medview.datahandling.aggregation.*;
import javax.swing.*;
import javax.swing.tree.*;

/**
 * Factory for TreeModels 
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class AggregationTreeModelFactory {
        
    
    /**
     * Creates a tree model of the contents of an Aggregation
     */
    public static TreeModel createAggregationTreeModel(Aggregation aggregation) {                        
        return new DefaultTreeModel(createAggregationTreeRootNode(aggregation));
    }
        
   /**
     * Creates the a tree of the contents of an Aggregation
     */ 
    private static TreeNode createAggregationTreeRootNode(Aggregation aggregation) {
        
        DefaultMutableTreeNode rootNode = new AggregationTreeNode(aggregation.getName(),AggregationTreeNode.NODETYPE_AGGREGATION);
        
        // Make nodes of all terms
        String[] terms = aggregation.getTerms();
        
        for (int termCount = 0; termCount < terms.length; termCount++) {
            DefaultMutableTreeNode termNode = new AggregationTreeNode(terms[termCount],AggregationTreeNode.NODETYPE_TERM);
            
            String[] groups = aggregation.getGroups(terms[termCount]);                        
            for (int groupCount = 0; groupCount < groups.length; groupCount++) {
                DefaultMutableTreeNode groupNode = new AggregationTreeNode(groups[groupCount],AggregationTreeNode.NODETYPE_GROUP);
                
                String[] values = aggregation.getValues(terms[termCount],groups[groupCount]);
                
                for (int valueCount = 0; valueCount < values.length; valueCount++) {
                    DefaultMutableTreeNode valueNode = new AggregationTreeNode(values[valueCount],AggregationTreeNode.NODETYPE_VALUE);
                    groupNode.add(valueNode);
                }
                
                termNode.add(groupNode);
            }
            
            rootNode.add(termNode);
        }                
        
        return rootNode;
    }
    
}
