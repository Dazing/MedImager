/*
 * Tree.java
 *
 * $Id: Tree.java,v 1.1 2006/05/29 18:32:53 limpan Exp $
 *
 * $Log: Tree.java,v $
 * Revision 1.1  2006/05/29 18:32:53  limpan
 * Added minimed code
 * ----------------------------------------------------------------------
 *
 * Revision 1.20  2004/11/15 15:41:47  erichson
 * Removed getParent() error message, added null documentation to @return
 *
 * Revision 1.19  2004/10/19 21:40:35  lindahlf
 * Lade tillbaka @deprecation eftersom det faktiskt hjälper om man har en bra IDE
 *
 * Revision 1.18  2004/10/01 16:39:49  lindahlf
 * no message
 *
 * Revision 1.17  2004/03/08 23:58:28  lindahlf
 * no message
 *
 * Revision 1.16  2004/02/20 15:45:18  erichson
 * Updated an error message // NE
 *
 * Revision 1.15  2004/01/30 15:28:59  lindahlf
 * no message
 * // Added hasChildren method // NE
 *
 * Revision 1.14  2003/09/09 17:58:12  erichson
 * Bug 175 (Empty leaves) var inte ordentligt fixad, men nu är den det.
 *
 * Revision 1.13  2003/09/09 17:24:45  erichson
 * Added methods setName, setValue, deleteNode, removeChild, removeAllChildren
 *
 * Revision 1.12  2003/09/08 16:35:14  erichson
 * Fixed printing algorithm: Fixes bugzilla bug 173
 *
 * Revision 1.11  2003/09/08 13:48:59  erichson
 * Changed leafToString so that an empty string is returned if the value is null or "".
 * Fixes bugzilla bug #175.
 *
 * Revision 1.10  2003/09/08 13:25:47  erichson
 * Added method getValuesOfNodesNamed
 *
 * Revision 1.9  2003/08/19 18:16:09  lindahlf
 * Måste finnas en tom konstruktor för att MRTree ska kunna serialiseras
 *
 * Revision 1.8  2003/08/19 16:03:15  lindahlf
 * See 030819_Release_Notes_DATAHANDLING_MISC_COMMON.pdf
 *
 * Revision 1.7  2003/08/18 21:44:23  erichson
 * Updated javadoc, deprecated getFirstChild method. Changed getLeaves method since it was unclear whether it was recursive or not. I'm not sure if anyone is using this method, hopefully it can be removed in the future.
 *
 * Revision 1.6  2003/08/16 15:07:16  erichson
 * added toString(), useful when writing Trees to file or streams
 *
 * Revision 1.5  2003/08/07 00:13:14  erichson
 * added setParent call to addChild
 *
 * Revision 1.4  2003/01/21 12:07:08  nazari
 * Change instance variables state from prive to protected
 *
 * Revision 1.3  2002/10/23 08:46:04  nazari
 * Add 2 new methods to find the file node
 *
 * Revision 1.2  2002/09/30 12:04:31  erichson
 * Added some javadoc
 *
 */

package minimed.core.datahandling.examination.tree;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

/**
 * The class representing a node in a Tree of MedView data.
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.2
 *
 * Replaces the old class medview.datahandling.examination.Tree.Node.
 */


public abstract class Tree implements Serializable {

    /**
     * The parent node
     */
    protected Tree parent;

    /**
     * Children of this node
     */
    protected Vector children;

    /**
     * The value or node name
     */
    protected String value;

    /**
     * The node type (TYPE_BRANCH or TYPE_LEAF).
     */
    protected int type;

    /**
     * The possible values for the type field of the Node
     */

    /** The node type for 'type missing' */
    public static final int TYPE_NONE = 0;
    /** The node type for a branch */
    public static final int TYPE_BRANCH = 1;
    /** The node type for a leaf */
    public static final int TYPE_LEAF = 2;

	protected Tree()
	{
		// added for serialization support (Fredrik 030819)
	}

    /** Construct a new Tree object
     * @param in_value The value stored in the node (for leaves) or node name (for branches)
     * @param in_type The type of node (TYPE_BRANCH, TYPE_LEAF, or in worst case TYPE_NONE)
     */
    protected Tree(String in_value, int in_type) {
        children = new Vector();
        value = in_value;
        type = in_type;
    }

    /**
     * Deletes this node (removes it from the parent);
     */
    public void deleteNode() {
        parent.removeChild(this);
    }

    /**
     * Removes one of the child nodes
     */
    public void removeChild(Tree child) {
        children.remove(child);
    }

    /** Add a child to this node. Updates the child's parent reference.
     * @param child The tree to add as a child
     */
    public void addChild(Tree child)
    {
        if (child != null) {
            children.add(child);
            child.setParent(this);
        } else {
            System.err.println("Tree.addChild error: Tried to add NULL child");
        }
    }

    /** 
     * Fetch the parent Node of this Node.
     * @return The parent Node of this Node. Note that for the top node, the parent is <code>null</code>.
     */
    public Tree getParent()
    {    
        return parent; // null if top.
    }

    /**
     * Change the parent Tree of this node. This does not change the parent (i.e. does not update the parents' set of children, that is done when the parent's addChild method is called)
     * @param newParent The Tree that should be the new parent.
     */
    protected final void setParent(Tree newParent) {
        parent = newParent;
    }

	/**
	 * Returns whether or not the node has children.
	 */
	public boolean hasChildren()	// Added by Fredrik 040130
	{
		return (children.size() != 0);
	}

    /**
     * Removes all children from this Tree node
     */
    public void removeAllChildren() {
        children.removeAllElements();
    }

    /**
     * Fetch an Enumeration of the child trees of this node.
     * @return an Enumeration of the child trees of this tode
     */
    public Enumeration getChildrenEnumeration() {
        return children.elements();
    }

    /**
     * Fetch an Iterator of the child Trees of this node
     * @return an Iterator of the childr Trees of this node
     */
    public Iterator getChildrenIterator() {
        return children.iterator();
    }

    /**
     * Returns a vector containing all nodes in this tree that are TYPE_LEAF. Can be recursive or not.
     * @param recursive whether the search should be recursive (go deeper in the tree) or not (only return the leaf children of this node)
     * @return A vector containing all the leaves in this tree
     *
     *
     */
    private Vector getLeaves(boolean recursive) {
        Vector v = new Vector(); // Create vector to hold the nodes

        // Loop through all children and add them to this vector
        for (Enumeration ce = children.elements(); ce.hasMoreElements(); ) {
            Tree child = (Tree) ce.nextElement();

            if (child.type == TYPE_LEAF) {
                // System.out.println("Stored a leaf! " + child.getValue());
                v.add(child); // save this
            } else {
                if (recursive) {
                    // keep doing recursion
                    v.addAll(child.getLeaves(recursive)); // recursive
                }
            }
        }

        return v;
    }

    /**
     * Gets a a node with a certain name is in this tree. This is a recursive search.
     * @param pNodeName the name of the node to fetch.
     * @return the node if it exists, or null if it doesn't
     */
    public Tree getNode(String pNodeName){

        // If this node's name is it, we have found it
        if (pNodeName.equals(value))
            return this;

        // Else, keep searching recursively by iterating through this node's children
        for (Enumeration ce = children.elements(); ce.hasMoreElements(); ) {
            Tree aNode = ((Tree) ce.nextElement()).getNode(pNodeName);

            if(aNode != null) // If not null, we have found it, so return it
                return aNode;
        }
        return null;
    }
    /**
     * Fetch this node's first child.
     * @return The value of the child
     * @deprecated This was a stupid convenience method. You shouldn't use this, since in the future
     *  assumptions about value order might change.
     */
    public Tree getFirstChild(){
        Enumeration ce = children.elements();
        if(ce.hasMoreElements()){
            return ((Tree) ce.nextElement());
        }
        return null;
    }

    public String[] getValuesOfNodesNamed(String nodeName)
    {
        Vector v = new Vector();

		if (getValue() == null)
		{
			String m1 = "Tree: WARNING: when obtaining values for nodes";

			String m2 = "named '" + nodeName + "', a node with a null value was ";

			String m3 = "encountered";

			return new String[0];	// return empty array
		}

        if (getValue().equals(nodeName))
        {
            for(Iterator it = getChildrenIterator(); it.hasNext();)
            {
                Tree nextChild = (Tree) it.next();

                v.add(nextChild.getValue());
            }
        }
        else 	// get all the children
        {
            for(Iterator it = getChildrenIterator(); it.hasNext();)
            {
                Tree nextChild = (Tree) it.next();

                String[] subTreeValues = nextChild.getValuesOfNodesNamed(nodeName);

                for (int i = 0; i < subTreeValues.length; i++)
                {
                    v.add(subTreeValues[i]);
                }
            }
        }

        String[] array = new String[v.size()];

        array = (String[]) v.toArray(array);

        return array;
    }



    /**
     * Fetch the value of this node.
     * @return The value of this node
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the name/value of this node. Same as setName
     */
    public void setValue(String newValue) {
        value = newValue;
    }

    /**
     * Sets the name/value of this node. Same as setValue
     */
    public void setName(String newName) {
        setValue(newName);
    }

    /**
     * Check whether this node is a leaf or not
     * @return whether this node is a leaf or not
     */
    public boolean isLeaf() {
        return (this.type == TYPE_LEAF);
    }


    // Ugly solution. see toString() for why // NE
    private String leafToString() {
        if (value != null) {
            if (!value.trim().equals("")) {
                return ( value + "#");
            }
        }
        return ""; // Don't write this leaf out since it is empty.
    }

    // Ugly solution. see toString() for why // NE
    private String branchToString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append( value );
        for (java.util.Iterator it = getChildrenIterator(); it.hasNext();) {
            Tree nextChild = (Tree) it.next();
            if (nextChild.isLeaf()) {
                String leafString = nextChild.toString(); // "" or "Value#"
                if (!leafString.trim().equals("")) { // not ""
                    buffer.append("\nL"); // Leaf marking
                    buffer.append(leafString); // "Value"
                }
            } else {
                buffer.append("\nN"); // Node marking
                buffer.append( (nextChild.toString()));
            }
        }
        buffer.append("#");
        return buffer.toString();
    }


    /**
     * Creates a String representation of a Tree.
     * @return the String representation of this Tree.
     */
    public String toString() {

        // Ugly solution. Had do do it this way to not break compatibility with Nader's class MRTree.
        // In the future, toString() should be abstract, and leafToString() should be TreeLeaf.toString(), and branchToString() should be TreeBranch.toString()
        // Nils E 03-08-15

        if (type == TYPE_LEAF)
            return leafToString();
        else
            return branchToString();
    }
}


