/**
 * @(#) NodeModelEvent.java
 */

package medview.medimager.model;

import java.util.*;

/**
 * An event related to something happening in a
 * NodeModel implementation object. There can be
 * three different events:
 *
 * 1) a child was added to a node.
 * 2) a child was removed from a node.
 * 3) a node's data was updated in some way.
 *
 * If a child was added or removed from a node, a
 * corresponding add() or remove() firing method is
 * called, but not the update() method. If something
 * in a node has changed (besides the children of the
 * node), update() fires but not the add() or remove().
 *
 * @author Fredrik Lindahl
 */
public class NodeModelEvent extends EventObject
{
	// PRIVATE MEMBERS

	private NodeModel node;


	// CONSTRUCTOR(S)

	/**
	 * Constructs an event without specifying the causing
	 * node.
	 */
	public NodeModelEvent( Object source )
	{
		this(source, null);
	}

	/**
	 * Constructs an event, specifying the node that caused
	 * the event to be fired. For child addition or removal,
	 * the node specified should be the child added or removed.
	 * For update, it should be the source node.
	 */
	public NodeModelEvent( Object source, NodeModel node )
	{
		super(source);

		this.node = node;
	}


	// CONCRETE NODE METHODS

	/**
	 * Returns the node that was the cause of
	 * this event. For child addition or removal,
	 * the node returned from this method is the
	 * child added or removed. For update, it is
	 * the source node.
	 */
	public NodeModel getNode( )
	{
		return node;
	}

	/**
	 * Sets the node that was the cause of this
	 * event. For child addition or removal,
	 * the node returned from this method is the
	 * child added or removed. For update, it is
	 * the source node.
	 */
	public void setNode( NodeModel node )
	{
		this.node = node;
	}

}
