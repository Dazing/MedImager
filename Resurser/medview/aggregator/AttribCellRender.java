package medview.aggregator;

import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import javax.swing.JTree;
import java.util.*;

/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

public class AttribCellRender extends DefaultTreeCellRenderer {

	GroupTreeUI     mGroupTree = null;
    public AttribCellRender() {}

	public Component getTreeCellRendererComponent (JTree aTree,Object value,
												    boolean sel,boolean expand,
													boolean leaf,int row,
													boolean hasFocus){
		super.getTreeCellRendererComponent( aTree,value,sel,expand,leaf,row,hasFocus);
		if(mGroupTree == null) return this;

		Font aFont = aTree.getFont();
		Font aFontBold = new Font(aFont.getName() ,Font.BOLD,aFont.getSize() );
		AttribNode aNode  = (AttribNode)value;
		//Ut.prt("render dit called node " + aNode.toString() );
		if(aNode.getType() == aNode.IS_ATTRB ){
			if(mGroupTree.hasAttrib(aNode.toString()) ){
				setFont(aFontBold);
				setToolTipText("Is used");
			}
			else{
				setFont(aFont);
				setToolTipText(null);
			}
		}
		else if(aNode.getType() == aNode.IS_VALUE){
			String attrib = aNode.getParent().toString();

			ArrayList  grps = mGroupTree.findGruops(attrib, aNode.toString());
			if(grps != null && !grps.isEmpty() ){
				setFont(aFontBold);
				String grp = (grps.size() > 1 ? "groups " : "group " );
				setToolTipText("Belongs to " + grp + getGroups(grps));
			}
			else{
				setFont(aFont);
				setToolTipText(null);
			}
		}
		else setFont(aFont);   //setToolTipText(null);
		return this;
	}
	String getGroups(ArrayList  grps){
		if(grps.isEmpty()) return null;
		String aList = (String)grps.get(0);
		for(int i = 1;i < grps.size() ; i++){
			String grp = (String)grps.get(i);
			aList = aList + ", " + grp;
		}
		return aList;
	}

	void setGroupTree(GroupTreeUI aGrTree){
		mGroupTree = aGrTree ;
	}

}











