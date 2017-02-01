/*
 * AggregationLibraryPanel.java
 *
 * Created on November 5, 2002, 10:35 AM
 *
 * $Id: AggregationLibraryPanel.java,v 1.7 2004/10/08 14:55:09 erichson Exp $
 *
 * $Log: AggregationLibraryPanel.java,v $
 * Revision 1.7  2004/10/08 14:55:09  erichson
 * Optional dialog error message
 *
 * Revision 1.6  2002/11/27 15:46:45  erichson
 * Disabled floating of the toolbar
 *
 * Revision 1.5  2002/11/27 15:30:19  erichson
 * Put the aggregation view inside a JScrollPane
 *
 * Revision 1.4  2002/11/27 15:23:05  erichson
 * Fixed aggregation viewing, set preferred size, added "remove aggregation" button
 *
 * Revision 1.3  2002/11/07 16:32:44  erichson
 * Added borderlayouts so components fill space better
 *
 * Revision 1.2  2002/11/07 15:32:26  erichson
 * Added borders for a less barren look
 *
 * Revision 1.1  2002/11/07 15:26:08  erichson
 * First check-in
 *
 */

package medview.common.components.aggregation;

import java.util.*;

import java.awt.*; // Container
import java.awt.event.*; // ActionListener
import javax.swing.*;
import javax.swing.border.*; // BevelBorder etc
import javax.swing.event.*;

import medview.datahandling.aggregation.*;
import medview.common.components.aggregation.*;

/**
 * GUI component to visualize and manipulate the library of loaded aggregations.
 *
 * Used like this: Create with a set of aggregations. When done editing, call getAggregations() to get the modified aggregate set.
 * This component is meant to be used with a dialog, so that a click on OK commits the changes, and cancel discards them 
 *
 * @author  Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.0
 */
public class AggregationLibraryPanel extends JPanel implements ActionListener  {
    
    private JPanel westPanel,eastPanel;
    private JSplitPane splitPane;
    private JList aggregationsList;
    private AggregationTree aggregationTree;
    private HashSet aggregationsSet;   
    
    private JButton removeAggregationButton;
    
    private static final String aggregationListTitle = "Loaded aggregations";

    /** Creates a new instance of AggregationLibrary */
    public AggregationLibraryPanel() {        
                
        aggregationsSet = new HashSet();                
                                        
        aggregationsList = new JList();
        aggregationsList.setPrototypeCellValue(aggregationListTitle);
        ListSelectionModel selectionModel = new SingleSelectionModel() {
            public void updateSingleSelection(int oldIndex, int newIndex) {
                System.out.println("SingleSelection updated!"); // debug
                viewAggregation((Aggregation) aggregationsList.getModel().getElementAt(newIndex));
            }
        };        
        
        aggregationsList.setSelectionModel(selectionModel);
        //aggregationsList.addListSelectionListener(this);
        aggregationsList.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
        westPanel = new JPanel();
        westPanel.setLayout(new BorderLayout());
        westPanel.add(aggregationsList,BorderLayout.CENTER);        
        westPanel.setBorder(BorderFactory.createTitledBorder(aggregationListTitle));
        
        eastPanel = new JPanel();
        eastPanel.setLayout(new BorderLayout());
        eastPanel.setBorder(BorderFactory.createTitledBorder("Aggregation view"));
        
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); // top to bottom
        splitPane.add(westPanel);
        splitPane.add(eastPanel);
                
        setLayout(new BorderLayout());
        add(splitPane,BorderLayout.CENTER);
        
        
        Image trashImage = java.awt.Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("/medview/common/resources/icons/tools/trash24.gif"));
        if (trashImage == null)
            removeAggregationButton = new JButton("Remove aggregation");
        else
            removeAggregationButton = new JButton(new ImageIcon(trashImage));                                       
        removeAggregationButton.addActionListener(this);
        
        JToolBar toolBar = new JToolBar();
        toolBar.add(removeAggregationButton);
        toolBar.setFloatable(false);
        
        add(toolBar,BorderLayout.NORTH);
        
        setPreferredSize(new Dimension(600,400));
    }    
    
    public AggregationLibraryPanel(Aggregation[] aggregations) {
        this();
        setAggregations(aggregations);
    }
    
    /**
     * Create new AggregationLibrary containing a set of aggregation, and a certain aggregation to view from the beginning
     */
    public AggregationLibraryPanel(Aggregation[] aggregations, Aggregation aggregationToViewFirst) {
        this(aggregations);
        viewAggregation(aggregationToViewFirst);
    }
        
    /**
     * Gets the current set of aggregations
     */
    public Aggregation[] getAggregations() {
        Aggregation[] array = new Aggregation[aggregationsSet.size()];        
        array = (Aggregation[]) aggregationsSet.toArray(array);
        return array;
    }
    
    public void setAggregations(Aggregation[] aggregations) {
        aggregationsSet.clear();
        
        for (int i = 0; i < aggregations.length; i++) {
            aggregationsSet.add(aggregations[i]);
        }
        
        updateGUI();
    }

    public void removeAggregation(Aggregation aggregation) {
        aggregationsSet.remove(aggregation);
        updateGUI();
    }
    
    private void updateGUI() {        
        Object[] aggs = aggregationsSet.toArray();        
        aggregationsList.setListData(aggs);
    }
    
    /**
     * View an aggregation in the right panel
     */ 
    public void viewAggregation(Aggregation agg) {       
        if (agg != null) {
            
            eastPanel.removeAll();            

            eastPanel.add(new JScrollPane(new AggregationTree(agg))); 
            
            //eastPanel.invalidate();
            eastPanel.revalidate();
            
        } else {
            /*JOptionPane.showMessageDialog(this, 
                                        "AggregationLibrary.viewAggregation error: null aggregation",
                                        "Null aggregation",
                                        JOptionPane.ERROR_MESSAGE);*/
        }
    }
    
    /** inner class stolen from 
     * http://java.sun.com/products/jfc/tsc/tech_topics/jlist_1/jlist.html
     */
    
    private abstract class SingleSelectionModel extends DefaultListSelectionModel {
        public SingleSelectionModel() {
            setSelectionMode(SINGLE_SELECTION);
        }

        public void setSelectionInterval(int index0, int index1) {
            int oldIndex = getMinSelectionIndex();
            super.setSelectionInterval(index0, index1);
            int newIndex = getMinSelectionIndex();
            if (oldIndex != newIndex) {
                updateSingleSelection(oldIndex, newIndex);
            }
        }

        // Extend this to define what happens when selection is updated
        public abstract void updateSingleSelection(int oldIndex, int newIndex);
        
    } 

    private Aggregation getSelectedAggregation() {
        Object aggObj = aggregationsList.getSelectedValue();
        if ((aggObj == null) || !(aggObj instanceof Aggregation)) {
            return null;
        } else {
            return (Aggregation) aggObj;
        }                            
    }
    
    public void actionPerformed(ActionEvent ev) {
        Object source = ev.getSource();
        if (source == removeAggregationButton) {
            removeAggregation(getSelectedAggregation());
        }
    }
    
}

