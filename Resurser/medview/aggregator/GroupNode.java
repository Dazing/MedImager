package medview.aggregator;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 *
 * $Id: GroupNode.java,v 1.3 2004/05/19 13:42:04 d97nix Exp $
 *
 * $Log: GroupNode.java,v $
 * Revision 1.3  2004/05/19 13:42:04  d97nix
 * Changed constants to final
 *
 * Revision 1.2  2004/03/29 15:43:13  erichson
 * Commented out constructor without node type, since "UNDEFINED" nodes are hardly useful...
 *
 */

public class GroupNode extends DefaultMutableTreeNode {

	public static final int IS_UNDEF = 0;
	public static final int IS_ROOT  = 1;
	public static final int IS_ATTRB = 2;
	public static final int IS_GROUP = 3;
	public static final int IS_VALUE = 4;

	int mType = IS_UNDEF;

   /* public GroupNode(String aName) {
		super(aName);
		mType = IS_UNDEF;
    }*/
	public GroupNode(String aName ,int aType) {
		super(aName);
		mType = aType;
    }
	int getType(){
		return mType;
	}
	void changeName(String aStr){
		setUserObject(aStr);
	}

}