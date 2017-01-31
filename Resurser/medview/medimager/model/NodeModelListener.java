/**
 * @(#) NodeModelListener.java
 */

package medview.medimager.model;

import java.util.*;

/**
 */
public interface NodeModelListener extends EventListener
{
	/**
	 * Indicates that a node was added to the source
	 * node's child list.
	 */
	public void nodeAdded( NodeModelEvent event );

	/**
	 * Indicates that a node was removed from the
	 * source node's child list.
	 */
	public void nodeRemoved( NodeModelEvent event );

	/**
	 * Indicates that something in the node's data
	 * has been updated.
	 */
	public void nodeUpdated( NodeModelEvent event );

}
