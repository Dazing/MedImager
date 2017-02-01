/**
 * $Id: AggregationTreeModel.java,v 1.1 2004/10/27 11:19:05 erichson Exp $
 *
 * $Log: AggregationTreeModel.java,v $
 * Revision 1.1  2004/10/27 11:19:05  erichson
 * First check-in.
 *
 */



package medview.aggregator;

import java.util.*;
import javax.swing.tree.DefaultTreeModel;



/**
 * AggregationTreeModel: A quick hack on top of DefaultTreeModel to use the TermValueComparator to sort values.
 * This is really a slow implemenation (since it creates a new vector and sorts it for every call to getChild or getIndexOfChild)
 * but it doesn't really matter because of the small amounts of data and the quick computers we have today.
 * 
 * A better way to implement this would be to keep the ordering and resort when any of the "changed" methods are called,
 * but that was more work than this quick solution.
 */

public class AggregationTreeModel extends DefaultTreeModel {

	public AggregationTreeModel(javax.swing.tree.TreeNode aNode) // Both GroupNode and AttribNode inherit from TreeNode. They should probably be merged in the future since they'return mostly the same thing, or at least let one extend the other
        {
		super(aNode);
	}

        private Vector getChildrenVector(Object parent)
        {
            Vector v = new Vector();        
            for (int i = 0; i < getChildCount(parent); i++)
            {
                v.add(super.getChild(parent,i));
            }
            Collections.sort(v, medview.datahandling.termvalues.TermValueComparator.getInstance());
            return v;
        }
        
        public Object getChild(Object parent, int index)        
        {
            return getChildrenVector(parent).get(index);
        }
        
        public int getIndexOfChild(Object parent, Object child)
        {
            return getChildrenVector(parent).indexOf(child, 0);
        }
}