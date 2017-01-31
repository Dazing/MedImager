/*
 * AggregationTreeNode.java
 *
 * Created on November 4, 2002, 5:12 PM
 *
 * $Id: AggregationTreeNode.java,v 1.1 2002/11/07 16:38:58 erichson Exp $ 
 *
 * $Log: AggregationTreeNode.java,v $
 * Revision 1.1  2002/11/07 16:38:58  erichson
 * First check-in
 *
 */

package medview.common.components.aggregation;

/**
 * Node class for an aggregation tree, which keeps track of whether a node represents a value, term or group
 * @author  Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class AggregationTreeNode extends javax.swing.tree.DefaultMutableTreeNode {
    
    public static final int NODETYPE_FIRST = 1;
    public static final int NODETYPE_VALUE = 1;
    public static final int NODETYPE_TERM = 2;
    public static final int NODETYPE_GROUP = 3;
    public static final int NODETYPE_AGGREGATION = 4;
    public static final int NODETYPE_LAST = 4;
    
    private int aggregationNodeType = 0;
    
    /** Creates a new instance of AggregationTreeNode */
    public AggregationTreeNode(String name, int type) {
        super(name);
        if ((type >= NODETYPE_FIRST) && (type <= NODETYPE_LAST))
            aggregationNodeType = type;
        else
            aggregationNodeType = NODETYPE_VALUE;
    }
        
    public int getType() {
        return aggregationNodeType;
    }
}
