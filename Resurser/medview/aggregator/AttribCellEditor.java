package medview.aggregator;

import javax.swing.tree.*;
//import javax.swing.JTree.*;
import javax.swing.*;
import java.awt.*;



/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

public class AttribCellEditor extends DefaultTreeCellEditor {

    public AttribCellEditor(JTree aTree,DefaultTreeCellRenderer aRender) {
		super(aTree,aRender );
    }

	public Component getTreeCellEditorComponent(JTree aTree,
												Object aValue,
												boolean isSelected,
												boolean expand,
												boolean leaf,
												int row){

		return super.getTreeCellEditorComponent(aTree,aValue,isSelected,expand,leaf,row);

		//setFont(new Font("Arialo",1,8));
		/*Font aFont = getFont();

		Font af = new Font(aFont.getName() ,Font.BOLD,aFont.getSize() );

		AttribNode aNode  = (AttribNode)aValue;
		Ut.prt("celledit called node " + aNode.toString() );
		if( aNode.getType() == AttribNode.IS_ATTRB ) {
			if(aNode.isUsed())  setFont(af);
		}
		else if( aNode.getType() == AttribNode.IS_VALUE ) {
			if(aNode.isUsed())   setFont(af);
		}

		return editingComponent ;*/
	}



}