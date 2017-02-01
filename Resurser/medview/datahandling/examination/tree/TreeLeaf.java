/*
 * TreeLeaf.java
 *
 * Created on den 14 augusti 2003, 14:34
 *
 * $Id: TreeLeaf.java,v 1.1 2003/08/18 21:24:44 erichson Exp $
 *
 * $Log: TreeLeaf.java,v $
 * Revision 1.1  2003/08/18 21:24:44  erichson
 * First check-in.
 *
 */

package medview.datahandling.examination.tree;

/**
 * Class for laves  in Tree structures.
 * 
 * Right now this class doesn't do very much. In the future, functionality from Tree which differs depending on
 * whether the Tree is a Branch or not (like toString()) will be moved here, that is not possible right now 
 * because of the MRTree class.
 * @author Nils Erichson
 */
public class TreeLeaf extends Tree {
    
    /**
     * Creates a new leaf-node Tree.
     * @param value the value/name of this node
     */
    public TreeLeaf(String value) {
        super(value,Tree.TYPE_LEAF);
    }
    
    // overrides addChild in tree
    public void addChild(Tree child) {
        System.err.println("ERROR: tried to add a child to a leaf node!");
    }     
    
}
