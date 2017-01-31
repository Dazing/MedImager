/*
 * TreeBranch.java
 *
 * Created on den 14 augusti 2003, 14:32
 *
 * $Id: TreeBranch.java,v 1.1 2003/08/18 21:22:42 erichson Exp $
 *
 * $Log: TreeBranch.java,v $
 * Revision 1.1  2003/08/18 21:22:42  erichson
 * First check-in.
 *
 */

package medview.datahandling.examination.tree;

/**
 * Class for branches in Tree structures.
 * Right now this class doesn't do very much. In the future, functionality from Tree which differs depending on
 * whether the Tree is a Branch or not (like toString()) will be moved here, that is not possible right now 
 * because of the MRTree class.
 *
 * @author Nils Erichson
 */
public class TreeBranch extends Tree {
    
    /**
     * Creates a new Branch node 
     * @param value The name/value of this node.
     */
    public TreeBranch(String value) {
        super(value,Tree.TYPE_BRANCH);
    }
    
}
