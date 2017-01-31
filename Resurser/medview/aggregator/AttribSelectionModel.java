package medview.aggregator;

import javax.swing.tree.*;
import javax.swing.tree.DefaultTreeSelectionModel;

/**
 * $Id: AttribSelectionModel.java,v 1.3 2004/05/18 15:51:15 d97nix Exp $
 *
 * $Log: AttribSelectionModel.java,v $
 * Revision 1.3  2004/05/18 15:51:15  d97nix
 * Fixed bug that prevented shift-selection. Did that ever work?
 *
 * Revision 1.2  2004/05/17 15:44:45  d97nix
 * Minor clean-up for readability, no functionality changes
 *
 */


/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

 /**
  * Selection model for the AttribTreeUI. Imposes restrictions on what multiple selections are 
  * allowed in the Attrib tree.
  */

public class AttribSelectionModel extends DefaultTreeSelectionModel {

    public AttribSelectionModel()
    {
        super();
        setSelectionMode(DISCONTIGUOUS_TREE_SELECTION );
    }

        // Restrictions for adding a single path to the selection
	public void addSelectionPath(TreePath aPath) {
		//Ut.prt("add called");
		if(getSelectionCount() > 0)  // more than 0 node already selected
                {
                    /** 
                     * Restriction: You can only do multiple selection at depth 3
                     * I.E only select multiple values, from the same attribute
                     */
                        TreePath selectionPath = getSelectionPath(); // Selected path before selection
			if( aPath.getPathCount() != 3) // Multiple selection only ok at depth 3
                            return; 
			
                        // Only OK if they belong to the same term!
                        if ( selectionPath.getPathComponent(1) != aPath.getPathComponent(1) )
                           return;		}
		super.addSelectionPath(aPath);
	}
        
	/**
	 * Restriction for adding multiple paths (shift-click)
	 */
	public void setSelectionPaths(TreePath[] paths) {            
		//Ut.prt("set SS called");
            if ((paths == null) || (paths.length < 1))
                return;
            
            if( paths.length > 1)
            {   // more than one node selected
                    TreePath   aPath   = paths[0];
                    int        depthOfFirstNode    =  aPath.getPathCount();
                    
                    // if the first path in the selection has a depth less than 3, the selection is not allowed
                    if(depthOfFirstNode < 3 ) 
                        return;  // the Deep of leaves is 3.

                    // Only allow the same depth for the other selected nodes
                    for(int i = 1; i < paths.length; i++){
                             int nodeDepth = paths[i].getPathCount();                             
                             if( depthOfFirstNode != nodeDepth)
                                 return;
                    }
            }
            
            super.setSelectionPaths(paths);            
	}
}