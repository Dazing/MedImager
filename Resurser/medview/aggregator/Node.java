package medview.aggregator;

import java.util.*;

/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

/**
 * The class representing a node in a tree of MedView data.
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.1
 */
public class Node {
    private Node parent;
    private Vector children;
    private String value;
    private int type;

    /**
     * The possible values for the type field of the Node
     */

    public static final int TYPE_NONE = 0;
    public static final int TYPE_BRANCH = 1;
    public static final int TYPE_LEAF = 2;

    /** Construct a new Node object
    * @param in_value The value stored in the node
    * @param in_type The type of node (branch, leaf or none)
    */
    public Node(String in_value, int in_type) {
        children = new Vector();
        value = in_value;
        type = in_type;
    }

    /** Add a child to this node.
    * @param n The node to add as a child
    */
    public void addChild(Node n) {
        children.add(n);
    }

    /** Fetch the parent Node of this Node.
    * @return The parent Node of this Node.
    */
    public Node getParent() {
        if (parent == null) {
            System.err.println("Error: Going too far back (node has no parent)  " + value );
        }
        return parent;
    }

    /**
    * Change the parent of this Node. Note: Does not update the parents' lists of children!
    * @param n The Node that should be the new parent.
    */
    public void setParent(Node n) {
        parent = n;
    }

    /**
    * Fetch an Enumeration of the children of this Node.
    * @return an Enumeration of the children of this Node
    */
    public Enumeration getChildrenEnumeration() {
        return children.elements();
    }

    /** Return a vector containing all nodes in this tree that are TYPE_LEAF
     * @return A vector containing all the leaves in this tree
    */
    public Vector getLeaves() {
        Vector v = new Vector(); // Create vector to hold the nodes

        // Loop through all children and add them to this vector

        for (Enumeration ce = children.elements(); ce.hasMoreElements(); ) {
            Node child = (Node) ce.nextElement();

            if (child.type == Node.TYPE_LEAF) {
                // System.out.println("Got a leaf! " + child.getValue());
                v.add(child); // save this
            } else {
                // keep doing recursion
                v.addAll(child.getLeaves()); // recursive
            }
        }
        return v;
    }

    /** Fetch the value of this node.
    * @return The value of this node
    */
    public String getValue() {
        return value;
    }
}
