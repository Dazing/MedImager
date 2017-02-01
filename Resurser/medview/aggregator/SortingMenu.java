package medview.aggregator;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.tree.*;

/**
 * $Id: SortingMenu.java,v 1.4 2004/07/26 12:19:14 erichson Exp $
 *
 * $Log: SortingMenu.java,v $
 * Revision 1.4  2004/07/26 12:19:14  erichson
 * removed JScrollPane parameter in showContextMenu (unnecessary)
 *
 * Revision 1.3  2004/05/18 15:50:22  d97nix
 * Added "add node(s)" to the popup menu.
 *
 * Revision 1.2  2004/05/17 15:38:54  d97nix
 * Bug fixes in popup handling.
 *
 */

/** 
 * 
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

public class SortingMenu implements ActionListener{

	private  final String[]             mPCodeMenu  = { "Sort P-code by genus", "Sort P-code by age",
                                                            "Sort P-code by name" };
	private     JPopupMenu              mPopupPCode;
	private     String []               mCode;
	
        /**
         * Popup menu menuItems
         */
        private JMenuItem addValueItem,
                          pcodeItem0, pcodeItem1, pcodeItem2;
        
        // The clicked pcode node and tree are saved for the actionPerformed method. 
        // Pretty bad style but i won't bother changing it... // NE
        private     DefaultMutableTreeNode  mPcodeNode;	
        private     AttribTreeUI            mAttribTree;
        
    public SortingMenu(String[] aCodes)
    {
		mCode = aCodes;
                buildMenu();    
    }
    
    public SortingMenu(String aCode) {
		mCode = new String[1];
		mCode[0] =  aCode;
		buildMenu() ;
    }
	private void buildMenu(){
		mPopupPCode     = new JPopupMenu();
                
                addValueItem = new JMenuItem("Add to group");
                addValueItem.addActionListener(this);
                
		pcodeItem0 = new JMenuItem(mPCodeMenu[0] );
		pcodeItem1 = new JMenuItem(mPCodeMenu[1] );
		pcodeItem2 = new JMenuItem(mPCodeMenu[2] );
                
		pcodeItem0.addActionListener(this);
		pcodeItem1.addActionListener(this);
		pcodeItem2.addActionListener(this);

                mPopupPCode.add(addValueItem);
                mPopupPCode.add(new JSeparator());
                
                // Unnecessary,changed labels instead
                // mPopupPCode.add(new JLabel("P-Code sorting"));
                // mPopupPCode.add(new JSeparator());
                
		mPopupPCode.add(pcodeItem0);
		mPopupPCode.add(pcodeItem1);
		mPopupPCode.add(pcodeItem2);
	}

        private boolean isValidPCode(String aCode)
        {	
		for(int i = 0; i < mCode.length ; i++)
			if(aCode.compareToIgnoreCase(mCode[i])== 0) return true;

		return false;
	}

        /**
         * Show right click popup menu.
         */
	public void showContextMenu(AttribNode aNode,AttribTreeUI aTree, MouseEvent e){
            mAttribTree = aTree;
            String nodeName = aNode.toString();

            // Get location for clicked node -> popup menu location
            // TreePath clickedNodePath = aTree.getPathForLocation(e.getX(), e.getY());
            // java.awt.Rectangle selectionBounds = aTree.getPathBounds(clickedNodePath);                        
            // int px = (int) selectionBounds.getMaxX();
            // int py = (int) selectionBounds.getMaxY();
            
            // popup menu at mouse click location instead
            int px = e.getX();
            int py = e.getY();
            
            if (aNode.getType() == aNode.IS_ROOT ) 
                return; // Do nothing                          
            else if(aNode.getType() == aNode.IS_VALUE  ) // Is a term such as 'p-code' etc
            {            		
		if(isValidPCode(nodeName)) { 
                    //mPopupPCode.getComponent(0) .setEnabled(false); // addgroup
                    mPcodeNode = aNode;                    
                    setPCodeSortingEnabled(true);                    
                }
                else // Value that is not p-code.
                {
                    setPCodeSortingEnabled(false);
                    // Not p-code, but another value                    
                }
                mPopupPCode.show(e.getComponent(),px,py);
            }
            else if (aNode.getType() == aNode.IS_ATTRB)
            {
                if (nodeName.toLowerCase().equals("p-code")) {
                    setPCodeSortingEnabled(true);
                    mPcodeNode = aNode;                                        
                } else {
                    setPCodeSortingEnabled(false);
                }
                mPopupPCode.show(e.getComponent(),px,py);
            }               
	}

        /**
         * Enable/disable p-code sorting options in right click menu
         */
        private void setPCodeSortingEnabled(boolean enabled) {
            pcodeItem0.setEnabled(enabled);
            pcodeItem1.setEnabled(enabled);
            pcodeItem2.setEnabled(enabled);
        }
        
        // actionPerformed called when a menu item is clicked. clicked tree and node are stored
	public void actionPerformed(ActionEvent e){
		JMenuItem anItem = (JMenuItem) e.getSource();

                if (anItem == addValueItem) {
                    mAttribTree.addSelectedNodesToGroup(); // Ugly, but functional for now...
                }
                
                //if(mPCodeMenu[0].compareTo(anItem.getText()  ) == 0) // sort by genus
                else if ( anItem == pcodeItem0) // sort by genus                		
			SortedValues.reSortPcode(mPcodeNode,SortedValues.PCodeLast );

		//else if  (mPCodeMenu[1].compareTo(anItem.getText() ) == 0) // sort by age
                else if (anItem == pcodeItem1) // sort by age
                    SortedValues.reSortPcode(mPcodeNode,SortedValues.PCodeMiddle);

		//else if  (mPCodeMenu[2].compareTo(anItem.getText() ) == 0)// sort by name
                else if (anItem == pcodeItem2) // sort by name
		    SortedValues.reSortPcode(mPcodeNode,SortedValues.PCodeFirst);

		mAttribTree.updateUI();
	}
}