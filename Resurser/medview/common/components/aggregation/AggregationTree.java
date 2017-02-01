/*
 * AggregationTree.java
 *
 * Created on November 4, 2002, 4:58 PM
 *
 * $Id: AggregationTree.java,v 1.3 2002/11/27 16:09:15 erichson Exp $
 *
 * $Log: AggregationTree.java,v $
 * Revision 1.3  2002/11/27 16:09:15  erichson
 * Small javadoc addition
 *
 * Revision 1.2  2002/11/07 15:43:58  erichson
 * Small fix: setCellRenderer instead of setTreeCellRenderer
 *
 * Revision 1.1  2002/11/04 16:53:17  erichson
 * First check-in
 *
 */

package medview.common.components.aggregation;

import javax.swing.*;

import medview.datahandling.aggregation.*;

/**
 * Component that visualizess an aggregation (term->grouping->value) as a JTree.
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class AggregationTree extends javax.swing.JTree {
    
    /** 
     * Creates a new instance of AggregationTree 
     * @param aggregation the aggregation to visualize
     */
    public AggregationTree(Aggregation aggregation) {
        super(AggregationTreeModelFactory.createAggregationTreeModel(aggregation));
        this.setCellRenderer(new AggregationTreeCellRenderer());
    }
    
}
