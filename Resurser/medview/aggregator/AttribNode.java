package medview.aggregator;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 *
 * $Id: AttribNode.java,v 1.2 2004/03/29 16:01:50 erichson Exp $
 *
 * $Log: AttribNode.java,v $
 * Revision 1.2  2004/03/29 16:01:50  erichson
 * Commented out the untyped constructor, since undefined nodes are not handled at all, they just cause a lot of "return null"... grr...
 *
 */

public class AttribNode extends DefaultMutableTreeNode {

	public static int IS_ATTRB   = 1;
	public static int IS_VALUE   = 2;
	public static int IS_ROOT    = 3;
	public static int IS_UNDEF   = 0;

	private ArrayList   mGroups     = new ArrayList();
	private int         mType       = IS_UNDEF ;

    // Untyped nodes are not wanted... // NE
    /*    
    public AttribNode(String aName) {
		super(aName);
		mType = IS_UNDEF;
    }
     */
	public AttribNode(String aName ,int aType) {
		super(aName);
		mType   = aType;
    }
	int getType(){
		return mType;
	}

}