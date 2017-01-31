/*
 * GraphBrowserComponent.java
 *
 * Created on den 8 juni 2005, 12:04
 *
 * $Id: GraphBrowserComponent.java,v 1.3 2005/06/09 14:29:16 erichson Exp $
 *
 * $Log: GraphBrowserComponent.java,v $
 * Revision 1.3  2005/06/09 14:29:16  erichson
 * Put the graph in a loweredBevelBorder
 *
 * Revision 1.2  2005/06/08 13:55:30  erichson
 * added revalidation to showGraph()
 *
 * Revision 1.1  2005/06/08 12:14:34  erichson
 * First check-in.
 *
 *
 */

package medview.medrecords.components.graph;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


import medview.medrecords.data.graph.*;
import medview.datahandling.examination.ExaminationValueContainer;

/**
 * Component that allows browsing of several graphs (with a combobox to choose between them)
 * 
 * @author Nils Erichson
 */
public class GraphBrowserComponent extends JPanel 
{
    
    private JPanel northPanel, centerPanel;    
    private JComboBox graphSelectorComboBox;
    private GraphSet graphSet;
    private ExaminationValueContainer evc;
    
    /** 
     * Creates a GraphBrowserComponent to browse the graphs described in graphSet
     */
    public GraphBrowserComponent(GraphSet graphSet, ExaminationValueContainer evc)
    {
        super();
        
        this.graphSet = graphSet;
        this.evc = evc;
        
        setLayout(new BorderLayout());
                
        String[] graphs = graphSet.getAvailableGraphs();
        graphSelectorComboBox = new JComboBox(graphs);
        graphSelectorComboBox.addActionListener(new ActionListener()
        {        
            public void actionPerformed(ActionEvent e)
            {            
                String graphName = (String) graphSelectorComboBox.getSelectedItem();
                GraphBrowserComponent.this.showGraph(graphName);
            }
        });
        
        northPanel = new JPanel();
        northPanel.add(graphSelectorComboBox);
        
        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());                
        
        // Add them to 'this' JPanel (which has BorderLayout)
        add(northPanel,BorderLayout.NORTH);
        add(centerPanel,BorderLayout.CENTER);                                
                
        // Show the first graph
        String[] availableGraphs = graphSet.getAvailableGraphs();
        if (availableGraphs.length > 0)
        {                    
            showGraph(availableGraphs[0]);
        }
        else
        {
            // Placeholder if no graphs were available
            centerPanel.add(new JLabel("No graphs available"));
        }
        
    }
    
    private void showGraph(String graphName)
    {
        GraphInfo graphInfo = graphSet.getGraph(graphName);
                
        GraphComponent graphC = new GraphComponent(graphInfo,evc);
        graphC.setBorder(BorderFactory.createLoweredBevelBorder());
        
        centerPanel.removeAll(); 
        centerPanel.add(graphC, BorderLayout.CENTER); 
        revalidate();
    }
    
}
