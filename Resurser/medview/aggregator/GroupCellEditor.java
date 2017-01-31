package medview.aggregator;

import javax.swing.tree.*; //DefaultTreeCellEditor;
import javax.swing.JTree.*;
import java.util.EventObject;
import javax.swing.*;
import java.awt.event.*;
import java.awt.TextField;

/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

public class GroupCellEditor extends DefaultTreeCellEditor  {
	boolean     mEditableCell = false;
	String      mOldValue;
	GroupNode   mNode;


    public GroupCellEditor(JTree aTree,DefaultTreeCellRenderer aRender) {
		super(aTree,aRender );
    }
	public boolean isCellEditable(EventObject anEvent){
		if(mEditableCell)
			return super.isCellEditable(anEvent);
		else
			return false;
	}
	public void actionPerformed(ActionEvent e){
		mOldValue  = (String) getCellEditorValue();
		//Ut.prt("action preformed");
		super.actionPerformed(e);
	}
	protected void prepareForEditing() {
		//Ut.prt("preparing");
		super.prepareForEditing();
	}
	void setNode(GroupNode aNode){
		mNode = aNode;
	}
	void setCellEditable(boolean flag){
		mEditableCell = flag;
	}
	void undo(){
		mNode.setUserObject(mOldValue );
	}
	String getOldValue(){
		return mOldValue;
	}


}















