/*
 * QueryPanel.java
 *
 * Created on October 14, 2002, 10:58 AM
 *
 * $Id: QueryPanel.java,v 1.12 2003/07/02 00:25:27 erichson Exp $
 *
 * $Log: QueryPanel.java,v $
 * Revision 1.12  2003/07/02 00:25:27  erichson
 * changed to FloaterComponent and added method getFloaterType()
 *
 * Revision 1.11  2002/12/04 15:50:17  zachrisg
 * Now all values of a term can be moved.
 *
 * Revision 1.10  2002/12/04 14:11:55  zachrisg
 * Added functionality to arrow buttons.
 * Cleaned up GUI with LambdaLayout.
 *
 * Revision 1.9  2002/12/03 15:48:58  zachrisg
 * Added two nice big buttons.
 *
 * Revision 1.8  2002/10/22 16:23:38  zachrisg
 * Moved medview.visualizer.data.query to medview.datahandling.query.
 *
 * Revision 1.7  2002/10/21 14:32:16  zachrisg
 * Added checkbox for live query.
 * Added sorting of terms and values.
 *
 * Revision 1.6  2002/10/21 12:50:06  zachrisg
 * The QueryPanel now listens for data changes in the DataManager.
 *
 * Revision 1.5  2002/10/17 15:52:53  zachrisg
 * Added aggregations.
 *
 * Revision 1.4  2002/10/17 15:21:33  zachrisg
 * The root of the tree is now hidden and the "handles" for the terms are shown.
 *
 * Revision 1.3  2002/10/17 11:06:57  zachrisg
 * Changed the text of the apply-query-button.
 *
 * Revision 1.2  2002/10/16 14:47:14  zachrisg
 * Now the query is applied only when something gets added/removed to/from the query
 * when you double click.
 *
 * Revision 1.1  2002/10/16 12:52:23  zachrisg
 * First check in.
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;

import com.jrefinery.data.*;
import ise.java.awt.*;

import medview.datahandling.*;
import medview.datahandling.aggregation.*;
import medview.datahandling.query.*;
import medview.visualizer.data.*;
import medview.visualizer.event.*;

/**
 * A query panel.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class QueryPanel extends FloaterComponent implements ActionListener, TermsChangeListener, AggregationListener, DatasetChangeListener {
    
    /** The current aggregation. */
    private Aggregation aggregation = null;
    
    /** The term-value-tree. */
    private JTree termValueTree;
    
    /** The query-tree. */
    private JTree queryTree;
    
    /** The apply query button. */
    private JButton applyQueryButton;
    
    /** The aggregation combobox. */
    private JComboBox aggregationComboBox;
    
    /** The live query checkbox. */
    private JCheckBox liveQueryCheckBox;
    
    /** The move to query button. */
    private JButton moveToQueryButton;
    
    /** The remove from query button. */
    private JButton removeFromQueryButton;
    
    /** 
     * Creates a new instance of QueryPanel.
     */
    public QueryPanel() {                
        DataManager.getInstance().addTermsChangeListener(this);
        DataManager.getInstance().addAggregationListener(this);
        DataManager.getInstance().addChangeListener(this);

        // create the live query checkbox
        liveQueryCheckBox = new JCheckBox("Immediate selection", true);
        
        // create the live query panel
        JPanel liveQueryPanel = new JPanel(new GridLayout(1,1));
        liveQueryPanel.setBorder(new TitledBorder("Live query"));
        liveQueryPanel.add(liveQueryCheckBox);
        
        // create the aggregation combobox
        aggregationComboBox = new JComboBox();
        updateAggregationChoosers(); // updates the combobox
        aggregationComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                Aggregation agg = (Aggregation) aggregationComboBox.getSelectedItem();
                if (agg != null) {                
                    setAggregation(agg);
                }
            }
        });        
        
        // create the aggregation panel
        JPanel aggregationPanel = new JPanel(new GridLayout(1,1));
        aggregationPanel.setBorder(new TitledBorder("Aggregation"));
        aggregationPanel.add(aggregationComboBox);
        
        // create the term-value-tree
        DefaultMutableTreeNode topNode = createTermValueNodes();        
        termValueTree = new JTree(topNode);
        termValueTree.setRootVisible(false);
        termValueTree.setShowsRootHandles(true);
        termValueTree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    if (moveSelectedNodes(termValueTree, queryTree)) {
                        if (liveQueryCheckBox.isSelected()) {
                            DataManager.getInstance().querySelectElements(createQuery(queryTree), aggregation);
                            DataManager.getInstance().validateViews();
                        }
                    }
                }
            }}
        );
        
        // create the query-tree
        DefaultMutableTreeNode queryTopNode = createQueryNodes();
        queryTree = new JTree(queryTopNode);
        queryTree.setRootVisible(false);
        queryTree.setShowsRootHandles(true);
        queryTree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    if (moveSelectedNodes(queryTree, termValueTree)) {
                        if (liveQueryCheckBox.isSelected()) {
                            DataManager.getInstance().querySelectElements(createQuery(queryTree), aggregation);
                            DataManager.getInstance().validateViews();
                        }
                    }
                }
            }}
        );

        // create the move to query button
        try {
            moveToQueryButton = new JButton(ApplicationManager.getInstance().loadCommonIcon("generic/rightArrowImage.gif"));
        } catch (IOException exc) {
            moveToQueryButton = new JButton("->");
        }
        moveToQueryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (moveSelectedNodes(termValueTree, queryTree)) {
                    if (liveQueryCheckBox.isSelected()) {
                        DataManager.getInstance().querySelectElements(createQuery(queryTree), aggregation);
                        DataManager.getInstance().validateViews();
                    }
                }
            }
        });
        
        // create the remove from query button
        try {
            removeFromQueryButton = new JButton(ApplicationManager.getInstance().loadCommonIcon("generic/leftArrowImage.gif"));        
        } catch (IOException exc) {
            removeFromQueryButton = new JButton("<-");
        }
        removeFromQueryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (moveSelectedNodes(queryTree, termValueTree)) {
                    if (liveQueryCheckBox.isSelected()) {
                        DataManager.getInstance().querySelectElements(createQuery(queryTree), aggregation);
                        DataManager.getInstance().validateViews();
                    }
                }
            }
        });

        // create the term-value-panel        
        JPanel termValuePanel = new JPanel(new BorderLayout());
        termValuePanel.setBorder(new TitledBorder("Values"));
        termValuePanel.add(new JScrollPane(termValueTree), BorderLayout.CENTER);
        
        // create the query-panel
        JPanel queryPanel = new JPanel(new BorderLayout());
        queryPanel.setBorder(new TitledBorder("Query"));
        queryPanel.add(new JScrollPane(queryTree), BorderLayout.CENTER);
            
        // the apply query button
        applyQueryButton = new JButton("Select examinations");
        applyQueryButton.addActionListener(this);

        // add all components to the main panel
        this.setLayout(new LambdaLayout());
        // "x, y, width, height, alignment, stretch, padding"
        this.add(aggregationPanel, "0,0,3,1,7,0,0");
        this.add(liveQueryPanel, "4,0,3,1,3,0,0");
        this.add(termValuePanel, "0,1,3,4,0,wh,0");
        this.add(queryPanel, "4,1,3,4,0,wh,0");
        this.add(moveToQueryButton, "3,2,1,1,5,0,0");
        this.add(removeFromQueryButton, "3,3,1,1,1,0,0");
        this.add(applyQueryButton, "2,5,3,1,0,0,12");

//  AAA LLL
//  TTT QQQ
//  TTTMQQQ
//  TTTRQQQ
//  TTT QQQ
//    SSS
        
        this.setSize(getPreferredSize());
    }
    
    /**
     * Moves the selected nodes from the sourceTree to the destTree.
     *
     * @param sourceTree The source tree.
     * @param destTree The destination tree.
     * @return True if any nodes were moved.
     */
    public boolean moveSelectedNodes(JTree sourceTree, JTree destTree) {
        TreePath[] treePaths = sourceTree.getSelectionPaths();

        if (treePaths == null) {
            return false;
        }

        boolean nodesMoved = false;
               
        for (int p = 0; p < treePaths.length; p++) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePaths[p].getLastPathComponent();
            boolean nodeMoved = moveNode(sourceTree, destTree, node);
            nodesMoved = nodesMoved || nodeMoved;
        }

        return nodesMoved;
    }
    
    /**
     * Moves a node from the sourceTree to the destTree.
     *
     * @param sourceTree The source tree.
     * @param destTree The destination tree.
     * @param node The node to move.
     */
    private boolean moveNode(JTree sourceTree, JTree destTree, DefaultMutableTreeNode node) {
        DefaultTreeModel sourceTreeModel = (DefaultTreeModel) sourceTree.getModel();
        DefaultTreeModel destTreeModel = (DefaultTreeModel) destTree.getModel();

        if (node == null) {
            return false;
        }
                
        Object userObject = node.getUserObject();
        
        if (userObject instanceof TermValuePair) {
            String term = ((TermValuePair) userObject).getTerm();
            String value = ((TermValuePair) userObject).getValue();

            // if the node isn't part of the source tree then return
            if (getTermNode(sourceTree,  term) == null) {
                return false;
            }
            
            // remove the value node from the source tree
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
            sourceTreeModel.removeNodeFromParent(node);
            node.setParent(null);
            
            // if the parent node has no children then remove it
            if (parentNode.getChildCount() == 0) {
                sourceTreeModel.removeNodeFromParent(parentNode);
            }
            
            DefaultMutableTreeNode destTermNode = getTermNode(destTree, term);
            
            // if no term node exists for the value then create it first
            if (destTermNode == null) {
                destTermNode = new DefaultMutableTreeNode(term);
                DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) destTreeModel.getRoot();
                destTreeModel.insertNodeInto(destTermNode, rootNode, getSuitableIndexForChild(rootNode, destTermNode));
            }
            
            // add the value node
            destTreeModel.insertNodeInto(node, destTermNode, getSuitableIndexForChild(destTermNode, node));
            destTree.makeVisible(getPathOfNode(node));
            
            return true;
        } else {
            boolean nodesMoved = false;
            while (node.getChildCount() > 0) {
                boolean nodeMoved = moveNode(sourceTree, destTree, (DefaultMutableTreeNode) node.getFirstChild());
                nodesMoved = nodesMoved || nodeMoved;
            }
            return nodesMoved;
        }
    }
    
    /**
     * Returns the TreePath of a TreeNode.
     *
     * @param node The node.
     * @return treePath The TreePath from the root to the node.
     */
    private TreePath getPathOfNode(TreeNode node) {
        if (node == null) {
            return null;
        }
        
        Vector objects = new Vector();
        
        TreeNode currentNode = node;
        while (currentNode != null) {
            objects.add(0, currentNode);
            currentNode = currentNode.getParent();
        }
        
        return new TreePath(objects.toArray());
    }
    
    /**
     * Returns an index suitable for insertion of a node based on sorting of the toString().
     *
     * @param parent The node that the child should be inserted into
     * @param newChild The child that should be inserted
     * @return A suitable index for the child to be inserted at.
     */
    private int getSuitableIndexForChild(DefaultMutableTreeNode parent, DefaultMutableTreeNode newChild) {
        int index = 0;
        
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (newChild.toString().compareTo(parent.getChildAt(i).toString()) <= 0) {
                return index;
            } else {
                index++;
            }
        }

        return index;        
    }    
    
    /**
     * Creates nodes for all the terms and their values and returns the top node.
     *
     * @return The top node.
     */ 
    private DefaultMutableTreeNode createTermValueNodes() {
        DefaultMutableTreeNode topNode;
        DefaultMutableTreeNode termNode;
        DefaultMutableTreeNode valueNode;
        
        Hashtable valueSetTable = DataManager.getInstance().getChosenTermsAndValues(aggregation);
        
        topNode = new DefaultMutableTreeNode("Terms", true);
        
        for (Enumeration termEnum = valueSetTable.keys(); termEnum.hasMoreElements(); ) {
            String term = (String) termEnum.nextElement();
            
            termNode = new DefaultMutableTreeNode(term);
         
            LinkedHashSet valueSet = (LinkedHashSet) valueSetTable.get(term);
            
            if (valueSet != null) {
                for (Iterator valueIter = valueSet.iterator(); valueIter.hasNext(); ) {
                    String value = (String) valueIter.next();

                    // find out if the query contains the value
                    boolean queryContainsValue = (getValueNode(queryTree, term, value) != null);
                    
                    // only add the value to the term-value-tree if it isn't in the Query
                    if (!queryContainsValue) {
                        valueNode = new DefaultMutableTreeNode(new TermValuePair(term, value));
                        termNode.insert(valueNode, getSuitableIndexForChild(termNode, valueNode));                       
                    }
                }
            }
            
            topNode.insert(termNode, getSuitableIndexForChild(topNode, termNode));
        }
        
        return topNode;
    
    }
    
    /**
     * Creates the tree nodes of the query and returns the top node.
     *
     * @return The top node of the query nodes.
     */
    public DefaultMutableTreeNode createQueryNodes() {
        return  new DefaultMutableTreeNode("Terms", true);
    }
    
    /**
     * Returns the node in a tree of a specific term and a value.
     *
     * @param tree The tree.
     * @param term The term.
     * @param value The value.
     * @return The node in the tree with the value of the term.
     */
    private DefaultMutableTreeNode getValueNode(JTree tree, String term, String value) {
        DefaultMutableTreeNode topNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        
        for (Enumeration termEnum = topNode.children(); termEnum.hasMoreElements(); ) {
            DefaultMutableTreeNode termNode = (DefaultMutableTreeNode) termEnum.nextElement();
            if (termNode.getUserObject().equals(term)) {
                for (Enumeration valueEnum = termNode.children(); valueEnum.hasMoreElements(); ) {
                    DefaultMutableTreeNode valueNode = (DefaultMutableTreeNode) valueEnum.nextElement();
                    if (((TermValuePair) valueNode.getUserObject()).getValue().equals(value)) {
                        return valueNode;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Finds the node for a term in a tree.
     * 
     * @param tree The tree.
     * @param term The term.
     * @return The node representing the term in the tree.
     */
    private DefaultMutableTreeNode getTermNode(JTree tree, String term) {
        DefaultMutableTreeNode topNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        
        for (Enumeration termEnum = topNode.children(); termEnum.hasMoreElements(); ) {
            DefaultMutableTreeNode termNode = (DefaultMutableTreeNode) termEnum.nextElement();
            if (termNode.getUserObject().equals(term)) {
                return termNode;
            }
        }
        return null;
    }

    /** 
     * Creates and returns a query from the data in the tree.
     *
     * @param tree The tree.
     * @return A query.
     */
    private Query createQuery(JTree tree) {
        Query query = new Query();
        
        DefaultMutableTreeNode topNode = (DefaultMutableTreeNode) tree.getModel().getRoot();
        
        for (Enumeration termEnum = topNode.children(); termEnum.hasMoreElements(); ) {
            DefaultMutableTreeNode termNode = (DefaultMutableTreeNode) termEnum.nextElement();
            String term = (String) termNode.getUserObject();
            Vector values = new Vector();
            for (Enumeration valueEnum = termNode.children(); valueEnum.hasMoreElements(); ) {
                DefaultMutableTreeNode valueNode = (DefaultMutableTreeNode) valueEnum.nextElement();
                TermValuePair termValuePair = (TermValuePair) valueNode.getUserObject();
                values.add(termValuePair.getValue());
            }
            query.addConstraint(term, (String[])values.toArray(new String[values.size()]));
        }
        
        return query;
    }
    
    /**
     * Called when a button is clicked.
     *
     * @param event The event.
     */
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == applyQueryButton) {
            DataManager.getInstance().querySelectElements(createQuery(queryTree), aggregation);
            DataManager.getInstance().validateViews();
        }
    }

    /**
     * Updates everything that handles which aggregations exist
     */
    public void updateAggregationChoosers() {
        aggregationComboBox.removeAllItems();
        
        aggregationComboBox.addItem(new Aggregation("No aggregation")); // Create empty aggregation
        Aggregation[] aggs = DataManager.getInstance().getAggregations();
        for (int i = 0; i < aggs.length; i++) {
            aggregationComboBox.addItem(aggs[i]);
        }        
    }    
    
    /**
     * Set the aggregation.
     *
     * @param agg the new aggregation
     */
    public void setAggregation(Aggregation agg) {
        aggregation = agg;
        
        // clear the query
        queryTree.setModel(new DefaultTreeModel(createQueryNodes()));
        
        // create the new term-value-tree
        termValueTree.setModel(new DefaultTreeModel(createTermValueNodes()));        
    }
    
    /** 
     * Called when the aggregation has changed.
     *
     * @param event The object representing the event.
     */
    public void aggregationChanged(AggregationEvent event) {
        updateAggregationChoosers();
    }

    /**
     * Called when the terms has changed in the datamanager.
     *
     * @param event The event.
     */
    public void termsChanged(TermsChangeEvent event) {
        DefaultMutableTreeNode topNode = createTermValueNodes();        
        termValueTree.setModel(new DefaultTreeModel(topNode));
    }
    
    /**
     * Called when elements has been added to or removed from the DataManager.
     *
     * @param event The event.
     */    
    public void datasetChanged(DatasetChangeEvent event) {
        DefaultMutableTreeNode topNode = createTermValueNodes();        
        termValueTree.setModel(new DefaultTreeModel(topNode));        
    }
    
    /**
     * A private class for packaging a term and a value together.
     */
    private class TermValuePair {
        
        /** The term. */
        private String term;
        
        /** The value. */
        private String value;
        
        /**
         * Creates a new term-value-pair.
         *
         * @param term The term.
         * @param value The value.
         */
        public TermValuePair(String term, String value) {
            this.term = term;
            this.value = value;
        }
        
        /**
         * Returns the term.
         * 
         * @return The term.
         */
        public String getTerm() {
            return term;
        }
        
        /**
         * Returns the value.
         *
         * @return The value.
         */
        public String getValue() {
            return value;
        }
        
        /**
         * Returns the value.
         *
         * @return The value.
         */
        public String toString() {
            return value;
        }
    }
    
    public int getFloaterType() {
        return Floater.FLOATER_TYPE_QUERY;
    }
}
