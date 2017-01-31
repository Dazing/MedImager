/**
 * @(#) NodeModel.java
 */

package medview.medimager.model;

import java.util.*;

import javax.swing.event.*;

/**
 * Every node model has a description, which is the one used by
 * the toString() and is generally considered the describing
 * string. Subclasses may override the getDescription() method to
 * return other descriptions (perhaps composed of various subclass-
 * specific node data).
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public abstract class NodeModel implements Cloneable
{
	// MEMBERS

	private String description = null;

	private NodeModel parent = null;

	private Vector children = new Vector();

	protected EventListenerList listenerList = new EventListenerList();


	// CONSTRUCTOR(S)

	/**
	 * Creates a node model with no parent, described
	 * by the specified description string.
	 * @param description String
	 */
	public NodeModel(String description)
	{
		this.description = description;
	}


	// CLONING

	/**
	 * A cloned node model does not share the same parent as the
	 * original, it is an orphan. Otherwise, it is exactly the
	 * same as the original. Also, it does not share the listeners
	 * that the original has.
	 * @return Object
	 */
	public Object clone()
	{
		NodeModel clonedNodeModel = null;

		try
		{
			clonedNodeModel = (NodeModel) this.getClass().newInstance();
		}
		catch (InstantiationException exc)
		{
			exc.printStackTrace();
		}
		catch (IllegalAccessException exc)
		{
			exc.printStackTrace();
		}

		clonedNodeModel.description = this.description;

		NodeModel[] children = getChildren();

		for (int ctr=0; ctr<children.length; ctr++)
		{
			clonedNodeModel.add((NodeModel)children[ctr].clone());
		}

		return clonedNodeModel;
	}


	// ABSTRACT

	/**
	 * Whether or not the node is a branch.
	 * @return boolean
	 */
	public abstract boolean isBranch();

	/**
	 * Whether or not the node is a leaf.
	 * @return boolean
	 */
	public abstract boolean isLeaf();


	// NODE NAME AND TOSTRING()

	/**
	 * Returns the node's description.
	 * @return String
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * Sets the node's description.
	 * @param description String
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Returns a string representation of the node.
	 */
	public String toString()
	{
		return getDescription();
	}


	// NODE STRUCTURE

	/**
	 * Adds the specified node model to the list
	 * of children kept by the branch node. If the
	 * node is not of branch type, this method does
	 * nothing. If the specified node is already a
	 * child of this node, does nothing.
	 */
	public void add( NodeModel node )
	{
		if (isBranch())
		{
			if (!children.contains(node))
			{
				if (node.hasParent())
				{
					node.getParent().remove(node); // remove the node from its parent
				}

				int index = getInsertIndex(node); // obtain where to insert the node in child list

				children.insertElementAt(node, index); // insert the node

				node.setParent(this); // set the parent

				fireNodeAdded(node); // notify that node was added to parent
			}
		}
	}

	private int getInsertIndex(NodeModel node)
	{
		if (getChildCount() == 0)
		{
			return 0; // if there are no children, insert first
		}
		else
		{
			if (node.isBranch())
			{
				NodeModel[] children = getChildren();

				for (int ctr=0; ctr<children.length; ctr++)
				{
					if (children[ctr].isBranch())
					{
						String currentFolderName = children[ctr].getDescription();

						String nodeFolderName = node.getDescription();

						if (nodeFolderName.compareTo(currentFolderName) < 0)
						{
							return ctr; // insert alphabetically
						}
					}
					else
					{
						return ctr; // we have traversed all folders and now encountered a leaf
					}
				}

				return getChildCount(); // only occurs if all nodes are folders pre insert node
			}
			else
			{
				return getChildCount(); // if node is a leaf, insert last
			}
		}
	}

	/**
	 * Removes the specified node model from the list
	 * of children kept by the branch node. If the
	 * node is not of branch type, this method does
	 * nothing. If the specified node is not a child
	 * of this node, does nothing.
	 */
	public void remove( NodeModel node )
	{
		if (isBranch())
		{
			if (containsChild(node))
			{
				children.remove(node);			// remove the node from the child list

				node.setParent(null);			// set the removed node's parent to null

				fireNodeRemoved(node);			// notify that node was removed
			}
		}
	}

	/**
	 * Obtains an enmeration of the children kept by
	 * this branch node.
	 */
	public NodeModel[] getChildren( )
	{
		NodeModel[] retArr = new NodeModel[children.size()];

		children.toArray(retArr);

		return retArr;
	}

	/**
	 * Returns the number of children of this node. Will
	 * always be 0 if the node is not a branch.
	 */
	public int getChildCount()
	{
		return children.size();
	}

	/**
	 * Returns whether or not this node contains the
	 * specified node as a child.
	 */
	public boolean containsChild(NodeModel child)
	{
		NodeModel[] children = getChildren();

		for (int ctr=0; ctr<children.length; ctr++)
		{
			if (children[ctr] == child)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Gets the index of the specified child. Will return a
	 * value of -1 if the node is not a leaf or whether the
	 * specified child is not a child of the node.
	 */
	public int getIndexOfChild(NodeModel child)
	{
		if (!isBranch())
		{
			return -1;
		}
		else
		{
			NodeModel[] children = getChildren();

			for (int ctr=0; ctr<children.length; ctr++)
			{
				if (children[ctr] == child)
				{
					return ctr;
				}
			}

			return -1;
		}
	}

	/**
	 * Returns the index of this node in its parent's
	 * child list, or -1 ir this node is not a child of
	 * any parent.
	 */
	public int getNodeIndex()
	{
		if (!hasParent())
		{
			return -1;
		}
		else
		{
			return getParent().getIndexOfChild(this);
		}
	}

	/**
	 * Returns the path to the current node, where each
	 * step is separated by a forward slash character. If
	 * the node is a root (i.e. it has no parent), the method
	 * will return an empty string. Thus, all paths will begin
	 * with the root node containing the node in question, for
	 * instance 'root/branch/branch/leaf'. Another example is
	 * 'root/branch/branch/".
	 */
	public String getPathToNode()
	{
		if (hasParent())
		{
			return getParent().getPathToNode() + getParent() + "/";
		}
		else
		{
			return "";
		}
	}

	/**
	 * Obtains the node's parent. Will return null if the
	 * node has no parent
	 */
	public NodeModel getParent( )
	{
		if (!hasParent())
		{
			return null;
		}
		else
		{
			return parent;
		}
	}

	/**
	 * Sets the parent of the node. Will also add the node
	 * to the parent's child array if it is not already there.
	 */
	public void setParent( NodeModel parent )
	{
		this.parent = parent;

		if (parent != null)
		{
			if (!parent.containsChild(this))
			{
				parent.add(this);
			}
		}
	}

	/**
	 * Returns whether or not the node has a parent. The
	 * only time this method returns a false value if it
	 * is a root node (i.e. has a null parent).
	 */
	public boolean hasParent()
	{
		return parent != null;
	}


	// EVENT HANDLING

	/**
	 * Registers a node model listener to the node.
	 */
	public void addNodeModelListener( NodeModelListener l )
	{
		listenerList.add(NodeModelListener.class, l);
	}

	/**
	 * Removes a registered node model listener from the
	 * node. If the node does not have the specified listener
	 * as a registered listener - nothing happens.
	 */
	public void removeNodeModelListener( NodeModelListener l )
	{
		listenerList.remove(NodeModelListener.class, l);
	}

	protected void fireNodeAdded(NodeModel node)
	{
		Object[] listeners = listenerList.getListenerList();

		NodeModelEvent e = new NodeModelEvent(this);

		e.setNode(node);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == NodeModelListener.class)
			{
				((NodeModelListener)listeners[i+1]).nodeAdded(e);
			}
		}
	}

	protected void fireNodeRemoved(NodeModel node)
	{
		Object[] listeners = listenerList.getListenerList();

		NodeModelEvent e = new NodeModelEvent(this);

		e.setNode(node);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == NodeModelListener.class)
			{
				((NodeModelListener)listeners[i+1]).nodeRemoved(e);
			}
		}
	}

	protected void fireNodeUpdated()
	{
		Object[] listeners = listenerList.getListenerList();

		NodeModelEvent e = new NodeModelEvent(this);

		e.setNode(this);

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == NodeModelListener.class)
			{
				((NodeModelListener)listeners[i+1]).nodeUpdated(e);
			}
		}
	}


	// DEBUGGING TOOLS

	/**
	 * Prints the node and all its children to the
	 * standard output stream.
	 */
	public void printTree(int offs)
	{
		for (int ctr=0; ctr<offs; ctr++)
		{
			System.out.print(" ");
		}

		System.out.print(toString() + " [" + getClass().getName() + "]\n");

		NodeModel[] children = getChildren();

		for (int ctr=0; ctr<children.length; ctr++)
		{
			children[ctr].printTree(offs+1);
		}
	}

}
