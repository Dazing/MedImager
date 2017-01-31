/*
 * $Id: AggregationChooser.java,v 1.2 2004/10/19 06:33:35 erichson Exp $
 *
 * Created on den 15 oktober 2004, 09:58
 *
 * $Log: AggregationChooser.java,v $
 * Revision 1.2  2004/10/19 06:33:35  erichson
 * added setEnabled to propagate to components
 *
 * Revision 1.1  2004/10/18 10:56:12  erichson
 * First check-in
 *
 */

package medview.common.components.aggregation;

import medview.datahandling.aggregation.*;

import java.io.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Component used to choose aggregations. Also allows popping up an aggregationLibrary.
 * Fires ChangeEvents when the choice changes.
 *
 * @author Nils Erichson 
 */
public class AggregationChooser extends JPanel
{
    protected static final Dimension standardLibraryButtonDimension = new Dimension(24,24);
    
    private JComboBox aggregationComboBox;
    private JButton aggregationLibraryButton;
    private HashSet changeListeners;
    
    private Frame dialogParentFrame;
    private AggregationContainer aggregationContainer;
    
    public AggregationChooser(Frame dialogParentFrame, AggregationContainer aggregationContainer)
    {
        this(dialogParentFrame, aggregationContainer, standardLibraryButtonDimension);
    }    
    
    public AggregationChooser(Frame dialogParentFrame, AggregationContainer aggregationContainer, Dimension libraryButtonDimension)
    {
        super();        
        
        this.aggregationContainer = aggregationContainer;
        this.dialogParentFrame = dialogParentFrame;
        
        BoxLayout bl = new BoxLayout(this,BoxLayout.X_AXIS);
        setLayout(bl);
        
        changeListeners = new HashSet();
        
        aggregationComboBox = new JComboBox();
        updateContents(); // updates the combobox
        aggregationComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                Aggregation agg = getSelectedAggregation();
                if (agg != null) {                
                    fireStateChanged();
                }
            }
        });
        
        // load funnel icon
        try {
            java.net.URL iconURL = getClass().getResource("/medview/common/resources/icons/medview/funnel16.png");            
            java.awt.image.BufferedImage bimage = javax.imageio.ImageIO.read(iconURL);
            if (bimage == null) 
            {
                throw new IOException("No imageReader could read " + iconURL); // Triggers the catch statement below
            }
            else
            {
                ImageIcon icon = new ImageIcon(bimage);            
                aggregationLibraryButton = new JButton(icon);
            }
        } catch (IOException ioe) {
            aggregationLibraryButton = new JButton("Agg.");
        }
        
        aggregationLibraryButton.setPreferredSize(libraryButtonDimension);
        
        aggregationLibraryButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent ev)
            {
                Object selectedItem = aggregationComboBox.getSelectedItem(); // Get current item
                Aggregation initialAgg = null;
                if (selectedItem instanceof Aggregation)
                {
                    initialAgg = (Aggregation) selectedItem;
                }
                AggregationLibraryDialog.showAggregationLibraryDialog(AggregationChooser.this.dialogParentFrame, 
                                                                      AggregationChooser.this.aggregationContainer,
                                                                      initialAgg); 
            }
        });
                                
        add(aggregationLibraryButton);
        add(aggregationComboBox);
    }
    
    public void addChangeListener(ChangeListener listener)
    {
        changeListeners.add(listener);
    }
    public void removeChangeListener(ChangeListener listener)
    {
        changeListeners.remove(listener);
    }
    
    private void fireStateChanged()
    {
        ChangeEvent event = new ChangeEvent(this);
        for (Iterator it = changeListeners.iterator(); it.hasNext();)
        {
            ((ChangeListener) it.next()).stateChanged(event);
        }
    }
    
    public Aggregation getSelectedAggregation()
    {
        Object selectedObject = aggregationComboBox.getSelectedItem();
        if (selectedObject instanceof Aggregation) {
            return (Aggregation) selectedObject;
        } else {
            return null;
        }        
    }
    
    public void setSelectedAggregation(Aggregation aggregation)
    {        
        aggregationComboBox.setSelectedItem(aggregation);
    }
    
    // Update the combobox with the contents from the aggregationcontainer
    public void updateContents()
    {
        
        aggregationComboBox.removeAllItems();
        
        aggregationComboBox.addItem(new Aggregation("No aggregation")); // Create empty aggregation
        Aggregation[] aggs = aggregationContainer.getAggregations();
        for (int i = 0; i < aggs.length; i++) {
            aggregationComboBox.addItem(aggs[i]);
        }        
    }
    
    // override setenabled to disable individual components
    public void setEnabled(boolean enabled) 
    {
        super.setEnabled(enabled);
        aggregationComboBox.setEnabled(enabled);
        aggregationLibraryButton.setEnabled(enabled);
    }
}
