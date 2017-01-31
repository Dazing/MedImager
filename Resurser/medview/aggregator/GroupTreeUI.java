/**
 * $Id: GroupTreeUI.java,v 1.9 2004/10/27 11:19:34 erichson Exp $
 *
 * $Log: GroupTreeUI.java,v $
 * Revision 1.9  2004/10/27 11:19:34  erichson
 * Use AggregationTreeModel.
 *
 */

package medview.aggregator;

import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.tree.*;


/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 *
 * $Id: GroupTreeUI.java,v 1.9 2004/10/27 11:19:34 erichson Exp $
 *
 * $Log: GroupTreeUI.java,v $
 * Revision 1.9  2004/10/27 11:19:34  erichson
 * Use AggregationTreeModel.
 *
 * Revision 1.8  2004/05/19 14:42:06  d97nix
 * GroupCellRender -> GroupTreeCellRenderer update
 *
 * Revision 1.7  2004/05/18 15:53:05  d97nix
 * Fixed the right-click menu (didn't show up in the right place, etc)
 *
 * Revision 1.6  2004/05/17 16:24:42  d97nix
 * Enabled multiple selection, and updated right click menu behaviour accordingly.
 *
 * Revision 1.5  2004/05/17 15:49:15  d97nix
 * Fixed non-standard right click behaviour (with regard to multiple selection etc)
 *
 * Revision 1.4  2004/05/17 15:03:05  d97nix
 * One-row cleanup
 *
 * Revision 1.3  2004/03/29 16:03:34  erichson
 * Updated addValue. Also cleaned up the horrible right-click menu.
 *
 * Revision 1.2  2002/10/09 15:04:47  erichson
 * New method names, better constructors
 *
 */


/**
 * somehow an UI component (JTree) which contains the category group data
 */
public class GroupTreeUI implements TreeSelectionListener,MouseListener,
                                    ActionListener,CellEditorListener {

        private     AttribTreeUI    mParent;

	private	    JTree           mTree;
	private     JScrollPane     mTreeScrollPane;
	private     boolean         mIsSaved;
		
	private     boolean         isValueAdded;

        // Context (right click popup) menu

        private     JPopupMenu      mPopup;
        
        JMenuItem addGroupPopupMenuItem,
                  deleteNodePopupMenuItem,
                  renameNodePopupMenuItem;
        
        
        
    // Added by Nils
    public TreeModel getTreeModel() {
        return mTree.getModel();
    }
    
    /**
     * Create a new GroupTreeUI for a category 
     */
    public GroupTreeUI(String categoryName){
        GroupNode         aNode  = new GroupNode(categoryName,GroupNode.IS_ROOT);
        AggregationTreeModel    aModel = new AggregationTreeModel(aNode);
        mTree  = new JTree(aModel);
        
        GroupTreeCellRenderer aGr = new GroupTreeCellRenderer();
        mTree.setCellRenderer(aGr);
        GroupCellEditor aGe = new GroupCellEditor(mTree,aGr );
        mTree.setCellEditor(aGe) ;
        aGe.addCellEditorListener(this);
        
        TreeSelectionModel tsM = (TreeSelectionModel) mTree.getSelectionModel();
        tsM.setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        
        mIsSaved     = true;
        isValueAdded = false;
    }
    
    
    public GroupTreeUI(JScrollPane aPanel, String aTreeModel) {
        this(aTreeModel); // creates mTree
        setPane(aPanel);
    }

    /**
     * Add this component to a JScrollpane
     */
    public void setPane(JScrollPane aPanel) {
        mTreeScrollPane = aPanel;
        mTreeScrollPane.getViewport().add(mTree , null); // add mTree to the viewport
        //GroupCellEditor grp =
        
        mTree.addTreeSelectionListener(this);   // Listen to the JTree for tree selections
        mTree.addMouseListener(this);           // Listen to the JTree for mouse events
        mTree.setEditable(true);                // Make JTree editable
                               
        addGroupPopupMenuItem = new JMenuItem(); // Text is set when showpopup method is called
        deleteNodePopupMenuItem = new JMenuItem();
        renameNodePopupMenuItem = new JMenuItem();
        
        addGroupPopupMenuItem.setMnemonic('A');
        deleteNodePopupMenuItem.setMnemonic('D');
        renameNodePopupMenuItem.setMnemonic('R');
        
        
        // add actionlisteners to popup menuitems
        addGroupPopupMenuItem.addActionListener(this);
        deleteNodePopupMenuItem.addActionListener(this);
        renameNodePopupMenuItem.addActionListener(this);
        
        // set the popupmenu
        mPopup = new JPopupMenu();
        mPopup.add(addGroupPopupMenuItem);
        mPopup.add(deleteNodePopupMenuItem);
        mPopup.add(renameNodePopupMenuItem);
        
        mTree.updateUI();
    }

	/**
	 * Assign the mParent filed to the left side tree.
	 */
	void setParent(AttribTreeUI aTree){
		mParent = aTree ;
	}

	void copyPerformed(){
		Ut.prt("GROUP   UI performed");
	}

	GroupNode getRoot(){
		return (GroupNode)(mTree.getPathForRow(0).getPathComponent(0) );
	}
	void setSaved(boolean flag){
		mIsSaved = flag;
	}
	boolean isSaved(){
		return mIsSaved;
	}
	void update(){
		mTree.updateUI();
	}

        /**
         * Save this category to an MVG file
         */ 
	void saveCategory(java.io.File dirName){
		GroupNode  theRoot = getRoot();
		if (theRoot == null) return;  // should not happen

		FileWriter aFw = new FileWriter(theRoot);
		aFw.saveCategory(dirName);
		setSaved(true);

		String fileName = dirName.getName();
		int indx        = fileName.lastIndexOf('.');
		String rootName = fileName.substring(0,indx);
		if(rootName.compareTo(theRoot.toString()) != 0 )   {
		    theRoot.changeName(rootName);
			update();
		}
	}
	
	/**
	 * Given a group name and an attribute Node. make a group and
	 * add it to the attribute.
	 */
	private boolean addGroup(GroupNode aNode,String groupName){

		if (aNode == null) return false;
		if(aNode.getType() != GroupNode.IS_ATTRB ) return false;

		addChild(aNode,groupName,GroupNode.IS_GROUP );

		mTree.updateUI();
		selectNode(aNode,true);
		return true;
	}
        
        /**
	 * Given a group and an attribute name. make a group and
	 * add it to the attribute.
	 */
	boolean addGroup(String attribName,String groupName){
		GroupNode aNode = findChild(getRoot() ,attribName);
		if (aNode == null) return false;
		if(aNode.getType() != GroupNode.IS_ATTRB ) return false;

		addChild(aNode,groupName,GroupNode.IS_GROUP ); // selects etc
		return true;
	}

        /**
	 * Given a value add it to the selected group in the GroupTree..
	 */
	GroupNode addValue(String valueName, String attribName) {
            // Get the currently selected group in the JTree
		GroupNode aNode =(GroupNode) mTree.getLastSelectedPathComponent();
                // System.out.println("GroupTreeUI.add(" + valueName + " to " + attribName + ". Groupnode got: " + aNode.toString() + " with parent " + aNode.getParent().toString() );
		if( aNode == null) {
                    // System.out.println("aNode null");
                    return null;
                }		
                
                if(aNode.getType() != GroupNode.IS_GROUP ) { 
                    // System.out.println("aNode.getType() != IS_GROUP (type was " + aNode.getType() + ")");
                    JOptionPane.showMessageDialog(mTree, "Select a group first!", "No group selected", JOptionPane.INFORMATION_MESSAGE);
                    return null;
                }
                
		if(! aNode.getParent().toString().equals(attribName)) {                        
                    System.out.println("aNode.getParent != attribName (" + attribName + ")");
                    return null;
                }

		addChild(aNode,valueName,GroupNode.IS_VALUE );
		mTree.updateUI();
		isValueAdded  =  true;
                // System.out.println("Add_ok i think");
		selectNode(aNode,true) ;
		return aNode;
	}
        
	/**
	 * Given a the names of an attribute and a group. make a value with the
	 * given valueName and add it to its group.
	 */
	boolean addValue(String attribName, String grpNname, String valueName)
        {
                System.out.println("GroupTreeUI.add(" + valueName + " to " + attribName + " in group " + grpNname);
		GroupNode aRoot = getRoot();
		GroupNode aNode = findChild(aRoot,attribName);
		if( aNode == null) {
                    System.out.println("findChild returned null");
                    return false;
                }

		GroupNode gNode = findChild(aNode,grpNname);
		if( gNode == null) {
                    System.err.println("gNode null");
                    return false;
                }
		
                if(gNode.getType() != GroupNode.IS_GROUP )  {
                    System.out.println("gnode.getType() != IS_GROUP");
                    return false;
                }
		addChild(gNode,valueName,GroupNode.IS_VALUE );
                System.out.println("Add_ok i think");
		return true;
	}
	
	/**
	 * Given an attribute name add it to the main tree.
	 */
	boolean addAttrib(String attribName){
		GroupNode aRoot = (GroupNode)mTree.getModel().getRoot();
		return addChild(aRoot,attribName,GroupNode.IS_ATTRB);
	}
	/**
	 * Given a Node and its child's name, make a child and add it to the node.
	 */
	private boolean addChild(GroupNode parentNode,String childName,int aType){
            //System.out.println("addChild called: parent " + parentNode + ", child " + childName + ", type " + aType);
            //if ( parentNode == null) return false;
            if( findChild(parentNode,childName) != null)
                return false;

            GroupNode aNode = new GroupNode(childName,aType);
            parentNode.add(aNode);
            mIsSaved = false;
            return true;
        }
        
        /**
         * Try to create a group at the currently selected tree node. Will show an input dialog and ask for name.
         */
	void createGroupAtSelectedNode(){
	    GroupNode aNode = (GroupNode)mTree.getLastSelectedPathComponent(); // Get the last selected path
		if( aNode == null) // abort if there is no selected place to create the group
                    return ; 
		if(aNode.getType() != GroupNode.IS_ATTRB ) 
                    return ; // abort if the currently selected node isn't attrib

		String aStr = Ut.showInput("Create a New Group","Write the group name:"); // Ask for group name in an input dialog
		if (aStr == null || aStr.length() < 1) return; // abort if the name is invalid
		addGroup(aNode,aStr);
	 }
        
         /**
          * Try to enable changing the name of a node. 
          */
	 void changeSelectedNodeName()
         {             
	    GroupNode aNode = (GroupNode)mTree.getLastSelectedPathComponent(); // Get last selected node in the tree
		if( aNode == null) 
                    return ; // abort if there was no selected node
		if(aNode.getType() != GroupNode.IS_GROUP){
			Ut.message(" Only the group's name can be chnaged. ");
			return ;
		}
		/*String newName = Ut.showInput("Chang the group name", "Write the new name");
		if(newName != null && newName.length() > 0 ){
			aNode.setUserObject(newName);
		}*/

		GroupCellEditor agp = (GroupCellEditor)mTree.getCellEditor();
		agp.setCellEditable(true);
		agp.setNode(aNode);
		agp.actionPerformed(null);
	 }

         /**
          * Delete the currently selected nodes
          */
         public void deleteSelectedNodes() {
             // Get selected components
             TreePath[] selectedPaths = mTree.getSelectionPaths();
             
             if (selectedPaths.length == 1) {
                 String nodeName = selectedPaths[0].getLastPathComponent().toString();
                 int ans = Ut.yesNoQuestion("Do you want to delete the node <" + nodeName + ">?") ;
                 if (ans ==  Ut.No )
                     return; // Abort
             } else // Not 1
             {
                 int ans = Ut.yesNoQuestion("Do you want to delete " + selectedPaths.length + " selected nodes?") ;
                 if (ans ==  Ut.No )
                     return; // Abort
             }
             
             mIsSaved    = false;             
             
             for (int i = 0; i < selectedPaths.length; i++) {
                 GroupNode aNode = (GroupNode) selectedPaths[i].getLastPathComponent();
                 if(aNode.getType() != GroupNode.IS_ROOT ) // Can't delete the root
                 {
                     aNode.removeFromParent();
                     deleteNode(aNode);
                 }
             }
             // Clear selection after deleting
             mTree.getSelectionModel().clearSelection();
         }

	 /**
	  * Try to delete a node. Does NOT update the selection afterwards, but calls jtree.updateUI().
          * @param aNode the node to delete 
          * @return the deleted groupnode if successful??
	  */
	private GroupNode deleteNode(GroupNode aNode){
		// if( aNode == null) return null; // Can't delete a null node		

		/*if(mParent != null){
		if(aNode.getType() == aNode.IS_ATTRB)
		//  the attrib and each value of it
		else if(aNode.getType() == aNode.IS_VALUE)
			//  the Value geoup name delete
		else if(aNode.getType() == aNode.IS_GROUP)
			//the group name from each value in that attrib
		// same problem with change name of group
		}*/
		mTree.updateUI() ;
		return aNode;
	}
	boolean hasAttrib(String attribName){
		GroupNode root = getRoot();
		if(findChild(root,attribName) == null)  return false;
		return true;
	}
	ArrayList findGruops(String attrib,String aValue){
		GroupNode root = getRoot();
		if(root.isLeaf() ) return null;

		GroupNode attribNode = findChild(root,attrib);
		if(attribNode  == null)  return null;
		if(attribNode.isLeaf())  return null;

		ArrayList aList = new ArrayList();
		GroupNode group = (GroupNode)attribNode.getFirstChild();
		while(group != null){
			if(group.isLeaf()){
				group = (GroupNode)group.getNextSibling();
				continue;
			}
			GroupNode value = (GroupNode)group.getFirstChild();
			while(value != null){
				if(value.toString().compareTo(aValue) == 0){
					aList.add(group.toString());
					break;
				}
				else
					value = (GroupNode)value.getNextSibling();
			}
			group = (GroupNode)group.getNextSibling();
		}
		return aList;
	}

	 /**
	 * Given a node and one of its child's name, find and return the child.
	 */
	private GroupNode findChild(GroupNode aNode, String childName){
		if (aNode == null) return null;
		if(aNode.isLeaf() ) return null;

		GroupNode aChild = (GroupNode)aNode.getFirstChild();
		while (aChild != null){
			if(aChild.toString().compareTo(childName) == 0)  return aChild;
			aChild  = (GroupNode)aChild.getNextSibling();
		}
		return null;
	}
	/**
	 * Select the named attribute.
	 */
	void selectAttribute (String attribName){
		GroupNode attribNode = findChild(getRoot() ,attribName);
		if(attribNode == null) return;

		GroupNode aNode = (GroupNode)mTree.getLastSelectedPathComponent();
		if(aNode != null){
			if (aNode == attribNode ) return;
			if (aNode.getParent()  == attribNode) return;
		}
		selectNode (attribNode,true);
	}

	/**
	 * Given a node, select it on the tree.
	 */
	private void selectNode (GroupNode aNode,boolean expand){
		if (aNode == null ) return;

		TreeNode[]  aPath   = aNode.getPath() ;
		TreePath    thePath = new TreePath(aPath);

		mTree.setSelectionPath(thePath);
		mTree.scrollPathToVisible(thePath);
		if(expand)
			mTree.expandPath(thePath);
	}
	/**
	 * Show right click (context) menu
         * @param aNode the clicked node that triggered this right menu.
	 */
	 private void showRightMenu(GroupNode aNode, MouseEvent e)
         {

             TreePath[] selectedPaths = mTree.getSelectionPaths();
             if ((selectedPaths == null) || (selectedPaths.length <= 0))
                 return; // Do nothing             
             
             int selectionSize = selectedPaths.length;            

            

            // Don't show popup menu for root node
            if (aNode.getType() == aNode.IS_ROOT ) 
                return;      

            if(aNode.getType() != aNode.IS_ATTRB  )
            { 
                // Not attribute(term) - disable "add group", enable rename                                            
                addGroupPopupMenuItem.setEnabled(false);
                addGroupPopupMenuItem.setText("Add group"); // Disabled

                if (selectionSize == 1)
                {
                    String nodeName = aNode.toString();                
                    deleteNodePopupMenuItem.setText("Delete node " + nodeName);
                    renameNodePopupMenuItem.setEnabled(true);
                    renameNodePopupMenuItem.setText("Rename node " + nodeName);                        
                } 
                else // Selection size > 1 (0 should hopefully never happen)
                {
                    deleteNodePopupMenuItem.setText("Delete " + selectionSize + " nodes");
                    renameNodePopupMenuItem.setEnabled(false);
                    renameNodePopupMenuItem.setText("Rename node");                        
                }
            }
            else
            {   // Is a term/attribute - disable rename, enable add group
                String nodeName = aNode.toString();   
                addGroupPopupMenuItem.setEnabled(true);
                addGroupPopupMenuItem.setText("Add group to " + nodeName);

                deleteNodePopupMenuItem.setText("Delete node " + nodeName);

                renameNodePopupMenuItem.setEnabled(false);
                renameNodePopupMenuItem.setText("Rename node");

            }
             
            // Get location for the popup menu
             
            // Get location for clicked node -> popup menu location
            // TreePath clickedNodePath = mTree.getPathForLocation(e.getX(), e.getY());
            // java.awt.Rectangle selectionBounds = mTree.getPathBounds(clickedNodePath);                        
            //int px = (int) selectionBounds.getMaxX();
            //int py = (int) selectionBounds.getMaxY();
            
            // Popup menu at mouse click location 
            int px = e.getX();           
            int py = e.getY();
            mPopup.show(e.getComponent(),px,py);

	 }

         // Actions called by clicking one of the choices in the popup menu // NE
	 public void actionPerformed(ActionEvent e){
            JMenuItem anItem = (JMenuItem) e.getSource();

            //if(mRightMenu[0].compareTo(anItem.getText()  ) == 0) // new group
            if (anItem == addGroupPopupMenuItem)
                createGroupAtSelectedNode();

            //else if  (mRightMenu[1].compareTo(anItem.getText() ) == 0) // delete
            else if (anItem == deleteNodePopupMenuItem)
                deleteSelectedNodes();

            //else if  (mRightMenu[2].compareTo(anItem.getText() ) == 0)// change name
            else if (anItem == renameNodePopupMenuItem)
                changeSelectedNodeName();
	 }
	/**
	 * The method of the ItemListener interface.
	 */
	 public void itemStateChanged(ItemEvent e) {
	 }

	/**
	 * The method of the TreeSelectionListener interface. Called with
	 * selecting a node on the tree.
	 */
	public void valueChanged(TreeSelectionEvent e) {
        GroupNode aNode = (GroupNode) mTree.getLastSelectedPathComponent();

        if (aNode == null) return;
		if(mParent == null) return;
		if (aNode.mType == GroupNode.IS_ATTRB )
			mParent.selectAttribute(aNode.toString() );

		if (aNode.mType == GroupNode.IS_GROUP ){
			if(isValueAdded)
				isValueAdded = false;
			else
				 mParent.selectAttribute(aNode.getParent(). toString() );
		}
		if (aNode.mType == GroupNode.IS_VALUE )
			mParent.selectAttribute(aNode.getParent().getParent().toString() );
    }
	/**
	 * The methods of the CellEditorListener interface.
	 */
// called when return is pressed
	public void editingStopped(ChangeEvent e) {
		GroupCellEditor agr = (GroupCellEditor)mTree.getCellEditor() ;
		String aVal = (String)agr.getCellEditorValue();
		agr.setCellEditable(false);
		if (aVal == null || aVal.length() < 1){
			Ut.warning("Input something" );
			agr.undo() ;
			mTree.updateUI() ;
		}
	}
// called when the user click somewhere else
	public void editingCanceled(ChangeEvent e) {
		GroupCellEditor agr = (GroupCellEditor)mTree.getCellEditor() ;
		agr.setCellEditable(false);
		Ut.prt("canceled");
	}
	
        /**
	 * The methods of the MoseListener interface.
	 */                
	public void mouseClicked(MouseEvent e){
		
                // Formerly a mouse click would operate on last left-selected tree item.
                // This is not how standard guis operate, so I have changed it.
                // NE                                                                           
		                
                // Alt-doubleclick deletes node??
		if(e.getModifiers()== MouseEvent.BUTTON1_MASK + MouseEvent.ALT_MASK ) // if alt + left button
                {
                    if(e.getClickCount() == 2)
                    {
                        TreePath pathToClickedNode = mTree.getPathForLocation(e.getX(), e.getY());           
                        GroupNode clickedNode = (GroupNode) mTree.getLastSelectedPathComponent();
                        if(clickedNode == null)
                            return ;
                        deleteNode(clickedNode);
                        mTree.getSelectionModel().clearSelection();
                    }
                }
	}

        public void mousePressed(MouseEvent e) {        
            if(e.isPopupTrigger()) // if right button
            { 	
                popupTrigger(e);			
            }            
        }
	
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popupTrigger(e);
            }
        }

        public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	        
        private void popupTrigger(MouseEvent e) {
            TreePath pathToClickedNode = mTree.getPathForLocation(e.getX(), e.getY());
            
            // IF it is already selected, do nothing
            if (mTree.isPathSelected(pathToClickedNode)) {
                
            }
            // If it is not selected, forget the old selection and select the new one
            else 
            {
                mTree.setSelectionPath(pathToClickedNode);                
            }
            GroupNode clickedNode = (GroupNode) mTree.getLastSelectedPathComponent();
            showRightMenu(clickedNode,e);    
        }

}