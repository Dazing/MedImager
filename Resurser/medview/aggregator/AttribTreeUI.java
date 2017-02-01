/**
 *
 * $Id: AttribTreeUI.java,v 1.13 2004/10/27 11:20:37 erichson Exp $
 *
 * $Log: AttribTreeUI.java,v $
 * Revision 1.13  2004/10/27 11:20:37  erichson
 * Use AggregationTreeModel.
 *
 * Revision 1.12  2004/10/08 14:05:06  erichson
 * Now gets default aggregation name from Aggregation class
 *
 * Revision 1.11  2004/07/26 12:17:57  erichson
 * Changed from encapsulating a JTree to extending JTree, which makes adding this component
 * elsewhere easier. This made possible some code cleanup since we don't have to keep
 * passing JScrollPanes all over the code. // NE
 *
 * Revision 1.10  2004/05/18 15:54:08  d97nix
 * Fixes to right click handling and popup menu
 *
 * Revision 1.9  2004/05/17 15:40:14  d97nix
 * Work on multiple selection and popup menu handling which was different from GroupTreeUI.
 *
 * Revision 1.8  2004/03/29 16:19:04  erichson
 * Fixed bug where IndexOutOfBoundsException would be thrown when saving, if a filename without a period was entered.
 *
 * Revision 1.7  2004/03/29 16:00:13  erichson
 * Rewrote addChildren, and some cleanup
 *
 * Revision 1.6  2004/03/28 17:50:22  erichson
 * Cleanup and additional methods necessary for adding terms and values from outside (visualizer)
 *
 * Revision 1.5  2003/06/24 14:43:10  erichson
 * Some javadoc and clarifications (better variable names) // NE
 *
 * Revision 1.4  2003/06/24 14:16:27  erichson
 * Removed junk at end of file // NE
 *
 * Revision 1.3  2003/06/24 13:40:32  erichson
 * No code changes, only cosmetic clean-up
 *
 *
 */

package medview.aggregator;

import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.tree.*;
import java.awt.Font;



/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

public class AttribTreeUI extends JTree implements TreeSelectionListener,MouseListener,KeyListener                                   
{
    private     AggregateHashTable      mHashTable;
    private     Vector                  mAttribs;
    private     GroupTreeUI             mGroupTree;
    
    private 	JTree                   mTree;
    // private     JScrollPane             mTreePanel;
    private     SortingMenu             mSortingMenu;    
    
    public AttribTreeUI()
    {
        //mTree.setFont(new Font("Arial",0,10)) ;
        //mTree.putClientProperty("JTree.lineStyle","Horizontal" );
        //mTree.putClientProperty("JTree.lineStyle","Angled" );                
        
        this(new AggregateHashTable());
    }
    
    public AttribTreeUI(AggregateHashTable aTreeModel) {
        this(aTreeModel ,null);
    }
    
    /**
     * Create a attribTreeUI From an existing forest
     *
     * @param aForest the forest to create attribTreeUI from
     */
    //public AttribTreeUI(JScrollPane aPanel, AggregateHashTable aTreeModel,String aForest){
    public AttribTreeUI(AggregateHashTable aTreeModel,String aForest)
    {
        super();
        mTree = this; // I'm doing this so i don't have to change the old code, where the class encapsulated a 
                      // JTree. That made adding etc hard... // NE
        
        //mHashTable  = (AggregateHashTable)aTreeModel;
        mHashTable = aTreeModel;
      
        makeTree(aForest);
                
        // aPanel.getViewport().add(mTree , null);
        mSortingMenu = new SortingMenu("P-code");
    }
    
    
    /** 
     * Gets the root node of the attribute tree // NE
     */
    private AttribNode getAttribRoot() {
        return (AttribNode) mTree.getModel().getRoot(); 
    }            
    
    /**
     * Make tree of Forest.forest. Called from the constructor.
     * @param forestName The name of the root node // NE
     */
    private void makeTree(String forestName){
        
        mAttribs    =  mHashTable.getSortedAttributes();
        
        String rootName = (forestName == null? "theRoot": forestName);
        AttribNode attribRootNode = new AttribNode(rootName,AttribNode.IS_ROOT); // Create root node
        
        AggregationTreeModel aModel = new AggregationTreeModel(attribRootNode);                        
        setModel(aModel);
        
        
        
        mTree.setSelectionModel(new AttribSelectionModel() );
        AttribCellRender aRndr = new  AttribCellRender();
        mTree.setCellRenderer(aRndr);
        AttribCellEditor aCelEdit= new AttribCellEditor(mTree,aRndr);
        mTree.setCellEditor(aCelEdit) ;
        mTree.setShowsRootHandles(true);
        
        mTree.addTreeSelectionListener(this);
        mTree.addMouseListener(this);
        mTree.addKeyListener(this);
        ToolTipManager.sharedInstance().registerComponent(mTree);
        
        AttribNode theRoot = (AttribNode)mTree.getModel().getRoot();
        
        for(int i = 0; i < mAttribs.size() ; i++){
            String nodeName = (String)mAttribs.get(i);
            AttribNode aNode = new AttribNode( nodeName, AttribNode.IS_ATTRB );
            theRoot.add( aNode);
        }
        
        if(theRoot.isLeaf())
            return;
        
        AttribNode aChild = (AttribNode)theRoot.getFirstChild();
        while (aChild != null){
            String chldName = aChild.toString();
            // adds a sorted list of values to each attribute.
            Vector values   = (Vector)mHashTable.get(chldName);
            addChildren(aChild,values);
            
            aChild  = (AttribNode)aChild.getNextSibling();
        }
    }
    
    
    /**
     * Add children (values) to a term. // NE
     */
    private void addChildren(AttribNode aChild, Collection values){
        aChild.setAllowsChildren(true);
        for (Iterator it = values.iterator(); it.hasNext();) 
        {
            String aVal = (String) it.next();
            AttribNode aNode = new AttribNode(aVal,AttribNode.IS_VALUE );
            aChild.add(aNode);
        }
    }
    
    /**
     * Add children (values) to a term. // NE
     */
    public void addChildren(AttribNode aChild, String[] values) {
        aChild.setAllowsChildren(true);
        for (int i = 0 ; i < values.length; i++){
            String aVal = values[i];
            AttribNode aNode = new AttribNode(aVal,AttribNode.IS_VALUE );
            aChild.add(aNode);
        }          
    }
    
    /**
     * String (term) version of addChildren // NE
     */
    public void addChildren(String term, Collection values) {
        // Find attribNode for that term
        AttribNode node = findNode(getAttribRoot(), term);
        if (node == null) {
            // Create new node for that term
            node = new AttribNode(term, AttribNode.IS_ATTRB); // Attribute (term)
            getAttribRoot().add(node);            
        }        
        addChildren(node, values);
    }
    
    void createNewGroup(){
        if(mGroupTree == null) return ;//Warning & save
        mGroupTree.createGroupAtSelectedNode();
    }
    
    
    /**
     * Create new category in the right panel
     */ 
    void newCategory(JScrollPane aPanel){
        mGroupTree = new GroupTreeUI(aPanel,medview.datahandling.aggregation.Aggregation.DEFAULT_AGGREGATION_NAME);
        mGroupTree.setParent(this);
        AttribCellRender atCelRdr = (AttribCellRender)mTree.getCellRenderer();
        atCelRdr.setGroupTree(mGroupTree);
    }
    
    void setCategory(GroupTreeUI aGTr){
        mGroupTree = aGTr;
        mGroupTree.setParent(this);
        AttribCellRender atCelRdr = (AttribCellRender)mTree.getCellRenderer();
        atCelRdr.setGroupTree(aGTr);
    }
    
    
    GroupTreeUI getCategory(){
        return mGroupTree;
    }
    
    void copyPerformed(){
        Ut.prt("attribUI performed copy");
    }
    
    void selectAttribute(String attribName){
        AttribNode theRoot = (AttribNode) mTree.getModel().getRoot();
        selectNode(theRoot,attribName,true );
    }
    
    /**
     * Find node named "nodeName" in tree aParent
     */
    private AttribNode findNode(AttribNode aParent, String nodeName){
        // Get first child of aParent // NE
        AttribNode aChild;
        try {
            aChild = (AttribNode)aParent.getFirstChild();
        } catch (java.util.NoSuchElementException nsee) {
            // "aParent" had no children at all!
            return null;
        }
        
        while (aChild != null) { // while not null
            if(aChild.toString().compareTo(nodeName) == 0) // if child is equal
                return aChild;
            aChild  = (AttribNode) aChild.getNextSibling();
        }
        return null;
        
    }
    void selectNode(AttribNode aParent, String nodeName, boolean expand){
        AttribNode aNode = findNode(aParent,nodeName );
        if (aNode == null )
            return;
        selectNode(aNode,expand);
    }
    
    void selectNode(AttribNode aNode, boolean expand){
        if (aNode == null ) 
            return;
        
        TreeNode[]  aPath   = aNode.getPath() ;
        TreePath    thePath = new TreePath(aPath);
        
        mTree.setSelectionPath(thePath);
        mTree.scrollPathToVisible(thePath);
        if(expand)
            mTree.expandPath(thePath);
    }
    
    /**
     * Called when a character is typed. Changes selection
    */
    void selectNode(char ch){
        String chS = new Character(ch).toString();
        String chL = chS.toLowerCase();
        String chU = chS.toUpperCase();
        AttribNode aNode = (AttribNode)mTree.getLastSelectedPathComponent();
        
        if (aNode == null)
        {
            aNode = (AttribNode)((AttribNode)mTree.getModel().getRoot()).getFirstChild();
        }
        AttribNode prtNode = (AttribNode)aNode.getParent();
        AttribNode nxtNode = (AttribNode)aNode.getNextSibling();
        while(nxtNode != null){
            String nxtStr = nxtNode.toString();
            if(nxtStr.startsWith(chL) || nxtStr.startsWith(chU)){
                selectNode(nxtNode,false);
                return;
            }
            nxtNode = (AttribNode)nxtNode.getNextSibling();
        }
        if (prtNode != null) nxtNode = (AttribNode)prtNode.getFirstChild();
        while(nxtNode != null  && nxtNode != aNode){
            String nxtStr = nxtNode.toString();
            if(nxtStr.startsWith(chL) || nxtStr.startsWith(chU)){
                selectNode(nxtNode,false);
                return;
            }
            nxtNode = (AttribNode)nxtNode.getNextSibling();
        }
    }
    
    /**
     * Save the actual category in a given directory by using a FileWriter object.
     */
    void saveCategory(java.io.File dirName){
        GroupNode  theRoot = mGroupTree.getRoot();
        if (theRoot == null) return;  // should not happen
        
        FileWriter aFw = new FileWriter(theRoot);
        aFw.saveCategory(dirName);
        mGroupTree.setSaved(true);
        
        String fileName = dirName.getName();
        int indx        = fileName.lastIndexOf('.');
        
        String rootName;
        if (indx > -1) {
            rootName = fileName.substring(0,indx);
        } else {
            rootName = new String(fileName);
        }
        
        // If the filename is different than the root name, update the root name
        if(rootName.compareTo(theRoot.toString()) != 0 ) {
            theRoot.changeName(rootName);
            mGroupTree.update();
        }
    }
    
    boolean needSaving(){
        if(mGroupTree == null) return false;
        return !mGroupTree.isSaved();
    }
    
    String getCategoryName(){
        return mGroupTree.getRoot().toString();
    }
        
    /**
     * TreeSelectionListener method
     *  When a node selected in the left side, select the related node on the right side
     */
    public void valueChanged(TreeSelectionEvent e) {
        AttribNode aNode = (AttribNode) mTree.getLastSelectedPathComponent();
        if (aNode == null) 
            return;
        
        String nodeName = aNode.toString();        
        if(mGroupTree == null)
            return;
        
        if(!aNode.isLeaf())
            mGroupTree.selectAttribute(nodeName);
        else  
            mGroupTree.selectAttribute(aNode.getParent().toString());
    }
    
    private void popupTrigger(MouseEvent e) {    
        TreePath pathToClickedNode = mTree.getPathForLocation(e.getX(), e.getY());
        // If it already is selected, do nothing
        if (mTree.isPathSelected(pathToClickedNode))
        {
            // do nothing
        } 
        else {            
            // If it is not selected, clear the old selection and select it
            mTree.setSelectionPath(pathToClickedNode);                
        }
        AttribNode clickedNode = (AttribNode) mTree.getLastSelectedPathComponent();
        showContextMenu(clickedNode,e);        
    }
    
    /**
     * Show context menu // NE
     */
    private void showContextMenu(AttribNode aNode, MouseEvent e){        
        if(aNode != null){
            mSortingMenu.showContextMenu(aNode,this,e);
        } else {
            JOptionPane.showMessageDialog(mTree,"No context!");
        }
    }
                            
    
    /**
     * This method adds selected nodes to the group.
     */
    public void addSelectedNodesToGroup() {
        TreePath[] pathes = mTree.getSelectionPaths();
        if(pathes == null) 
            return;
        if( pathes.length  < 1)
            return;
        
        TreePath    aPath = pathes[0];
        if(aPath == null)
            return;
        
        AttribNode aNode = (AttribNode)aPath.getLastPathComponent() ;
        if(aNode == null)
            return;
        
        String nodeName   = aNode.toString();
        
        if(aNode.getType() == AttribNode.IS_ROOT)
            return; // Double clicked on root node - nothing happens
        
        else if(!aNode.isLeaf()){ // It's an attrib/term Add the attrib to currently selected group
            if (mGroupTree.addAttrib(nodeName)) // addAttrib successful?
            {
                mGroupTree.update();
            }
        }
        else { // Leaf / Value
            // Get the term above this value
            String attribName = aNode.getParent().toString();
            // System.out.println("Trying to add  value " + nodeName + " to " + attribName + " in currently seleced group in tree");
            GroupNode grNd   = mGroupTree.addValue(nodeName,attribName);
            
            if(grNd == null) return;
            for(int i = 1; i < pathes.length; i++) {
                aPath = pathes[i];
                aNode = (AttribNode)aPath.getLastPathComponent() ;
                nodeName   = aNode.toString();
                
                grNd   = mGroupTree.addValue(nodeName,attribName);
                if(grNd == null) return;
            }
            //selectNode(aNode,false);
        }
    }
    
    public void mouseClicked(MouseEvent e)
    { 
        if(e.getClickCount() == 2) // Only respond to double clicks
        {  
            if (e.getButton() == MouseEvent.BUTTON1) // Only left double clicks
            {                
                AttribNode aNode = (AttribNode) mTree.getLastSelectedPathComponent();
                if ((aNode != null) && (aNode.getType() == AttribNode.IS_VALUE))                    
                    addSelectedNodesToGroup(); // Only double clicks on values should add selected nodes
            }
        }
    }
    
    public void mousePressed(MouseEvent e)
    {
        if(e.isPopupTrigger()) // if right button
            popupTrigger(e);                    
    }
    
    public void mouseReleased(MouseEvent e)
    {
        if(e.isPopupTrigger()) // if right button
            popupTrigger(e);    
    }
    
    
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    
    // ORDER IS PRESSED TYPED RELEASED
    public void keyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
        selectNode(ch);
    }
    
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}                    
    
    /*
     public void updateUI() {
        mTree.updateUI();
    } */
    
    /*
    public java.awt.Rectangle getPathBounds(TreePath path) {
        return mTree.getPathBounds(path);
    }
    
    public TreePath getPathForLocation(int x, int y) {
        return mTree.getPathForLocation(x,y);
    }
    */
}
 
/*
        public void mousePressed(MouseEvent e){
                int selRow = mTree.getRowForLocation(e.getX(), e.getY());
                if(selRow < 0) return;
 
                TreePath selPath = mTree.getPathForLocation(e.getX(), e.getY());
                AttribNode aNode =
                                                (AttribNode)selPath.getLastPathComponent();
 
                if (aNode == null) return;
                if(mGroupTree == null) return;
                String nodeName   = aNode.toString();
 
                if(e.getClickCount() == 2) {
                        //Ut.prt("pressed 2 click  " + nodeName);
                        if(aNode.isLeaf()){
                                 String attribName = aNode.getParent().toString();
                                 mGroupTree.addValue(nodeName,attribName);
                        }
                        else mGroupTree.addAttrib(nodeName);
                }
    }
 
 */
