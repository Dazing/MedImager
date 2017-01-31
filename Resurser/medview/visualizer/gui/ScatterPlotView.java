/*
 * ScatterPlot.java
 *
 * Created on June 26, 2002, 3:03 PM
 *
 * $Id: ScatterPlotView.java,v 1.38 2005/01/26 13:07:59 erichson Exp $
 *
 * $Log: ScatterPlotView.java,v $
 * Revision 1.38  2005/01/26 13:07:59  erichson
 * Removed junk at end of file
 *
 * Revision 1.37  2005/01/26 13:04:47  erichson
 * All views update to new termhandling methods
 *
 * Revision 1.36  2002/11/14 16:01:00  zachrisg
 * Added support for session loading/saving of aggregations.
 *
 * Revision 1.35  2002/11/13 14:34:01  zachrisg
 * Added support for session loading/saving.
 *
 * Revision 1.34  2002/10/10 14:52:23  erichson
 * added setAggregation()
 *
 * Revision 1.33  2002/10/08 10:42:08  erichson
 * Moved term chooser panel to EAST toolbar, removed unused variables
 *
 * Revision 1.32  2002/10/08 10:22:05  erichson
 * Nicer control button layout, cleaned out (removed) the old control code which was commented out
 *
 * Revision 1.31  2002/10/04 13:07:22  erichson
 * Changed to icon-based controls + added tooltips
 *
 * Revision 1.30  2002/10/03 13:45:41  erichson
 * Updated control panel for better visibility
 *
 */

package medview.visualizer.gui;

import medview.datahandling.NoSuchTermException;
import medview.visualizer.data.*;
import medview.visualizer.dnd.*;

import com.jrefinery.chart.*;
import com.jrefinery.data.*;
import com.jrefinery.chart.tooltips.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

/**
 *
 * @author  G?ran Zachrisson <zachrisg@mdstud.chalmers.se>
 * @version 
 */
public class ScatterPlotView extends View implements ActionListener {
    
    private final static ApplicationManager appManager = ApplicationManager.getInstance();
    
    /** The scatterplot component. */
    private ScatterPlot scatterPlot;
    
    /** The combobox to choose the term on the x-axis. */    
    private JComboBox xAxisComboBox;    
    
    /** The combobox to choose the term on the y-axis. */
    private JComboBox yAxisComboBox;
    
    /** The radio buttons for horizontal or vertical labels */
    private JToggleButton verticalLabelsToggleButton,horizontalLabelsToggleButton;
       
    /** The toggle button for selecting whether the values should be in percent or not. */
    private JToggleButton percentToggleButton;
    
    /** The horizontal scrollbar. */
    private JScrollBar horizontalScrollBar;
    
    /** The vertical scrollbar. */
    private JScrollBar verticalScrollBar;
    
    /** The horizontal zoom in button. */
    private JButton horizontalZoomInButton;
    
    /** The horizontal zoom out button. */
    private JButton horizontalZoomOutButton;
    
    /** The horizontal zoom combobox. */
    private JComboBox horizontalZoomComboBox;
    
    /** The vertical zoom in button. */
    private JButton verticalZoomInButton;
    
    /** The vertical zoom out button. */
    private JButton verticalZoomOutButton;
    
    /** The vertical zoom combobox. */
    private JComboBox verticalZoomComboBox;
    
    /** The main panel. */
    private JPanel contentPanel;
    
    
    /** 
     * Creates new ScatterPlot
     * @param dataSet The ExaminationDataSet to plot.
     * @param xTerm The term of the x-axis.
     * @param yTerm The term of the y-axis.
     * @param percentValues True if values should be in percent.
     */
    public ScatterPlotView(ExaminationDataSet dataSet, String xTerm, String yTerm, boolean percentValues) {
        super(dataSet);        
        
        String defaultTerm = DataManager.getInstance().getDefaultTerm();
        // System.out.println("default term = " + defaultTerm);
       
        String xAxisTerm = defaultTerm;
        String yAxisTerm = defaultTerm;
        
        String[] terms = DataManager.getInstance().getChosenTerms();
        
        // If xTerm or yTerm exist in chosen terms, use them instead of defaultTerm
        for (int i = 0; i < terms.length; i++) {
            if (terms[i].equals(xTerm)) {
                xAxisTerm = terms[i];
            }
            if (terms[i].equals(yTerm)) {
                yAxisTerm = terms[i];
            }
        }                    
       
        ApplicationManager.debug("ScatterPlotView(): Creating XYGraphDataSet.");
        XYGraphDataSet xyDataSet = new XYGraphDataSet(dataSet,xAxisTerm,yAxisTerm, percentValues);
                        
        // create the scatterplot
        ViewTransferHandler th = new ViewTransferHandler(this);
        scatterPlot = new ScatterPlot(xyDataSet, xAxisTerm, yAxisTerm);
        scatterPlot.setTransferHandler(th);
        
        ApplicationManager.getInstance().addToolChangeListener(scatterPlot);
        
        // create the comboboxes for the axes
        xAxisComboBox = new JComboBox(terms);        
        xAxisComboBox.setSelectedItem(xAxisTerm);
        xAxisComboBox.setToolTipText("X axis term");
        xAxisComboBox.setFont(viewComboBoxFont);
        xAxisComboBox.addActionListener(this);
        
        yAxisComboBox = new JComboBox(terms);               
        yAxisComboBox.setSelectedItem(yAxisTerm);   
        yAxisComboBox.setToolTipText("Y axis term");
        yAxisComboBox.setFont(viewComboBoxFont);
        yAxisComboBox.addActionListener(this);
                                       
        // create the radio buttons for vertical or horizontal labels
        verticalLabelsToggleButton = appManager.createIconToggleButton("verticalXLabels16.png","Vertical labels",viewButtonDimension);
        horizontalLabelsToggleButton = appManager.createIconToggleButton("horizontalXLabels16.png","Horizontal labels",viewButtonDimension);
        verticalLabelsToggleButton.addActionListener(this);
        horizontalLabelsToggleButton.addActionListener(this);
        verticalLabelsToggleButton.setToolTipText("Vertical X labels");
        horizontalLabelsToggleButton.setToolTipText("Horizontal X labels");

        // ButtonGroup for horizontal/vertical x labels
        ButtonGroup xLabelsGroup = new ButtonGroup();
        xLabelsGroup.add(verticalLabelsToggleButton);
        xLabelsGroup.add(horizontalLabelsToggleButton);        
        verticalLabelsToggleButton.setSelected(scatterPlot.isVerticalXLabelsUsed());        
        horizontalLabelsToggleButton.setSelected(! scatterPlot.isVerticalXLabelsUsed());
        
        // create the percent value toggle button
        percentToggleButton = appManager.createIconToggleButton("percent16.png","%",viewButtonDimension);
        percentToggleButton.addActionListener(this);
        percentToggleButton.setToolTipText("Values in percent");
        percentToggleButton.setSelected(percentValues);
        
        // create the horizontal scrollbar
        horizontalScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
        horizontalScrollBar.setModel(scatterPlot.getHorizontalRangeModel());
        
        // create the vertical scrollbar
        verticalScrollBar = new JScrollBar(JScrollBar.VERTICAL);
        verticalScrollBar.setModel(scatterPlot.getVerticalRangeModel());
        
        // create the zoom buttons
        horizontalZoomInButton = appManager.createIconButton("zoomin16.png", "+", viewButtonDimension);
        horizontalZoomOutButton = appManager.createIconButton("zoomout16.png", "-", viewButtonDimension);
        verticalZoomInButton = appManager.createIconButton("zoomin16.png", "+", viewButtonDimension);
        verticalZoomOutButton = appManager.createIconButton("zoomout16.png", "-", viewButtonDimension);
        horizontalZoomInButton.addActionListener(this);
        horizontalZoomOutButton.addActionListener(this);
        verticalZoomInButton.addActionListener(this);
        verticalZoomOutButton.addActionListener(this);

        horizontalZoomInButton.setToolTipText("Zoom in (X axis)");
        horizontalZoomOutButton.setToolTipText("Zoom out (X axis)");
        verticalZoomInButton.setToolTipText("Zoom in (Y axis)");
        verticalZoomOutButton.setToolTipText("Zoom out (Y axis)");
        
        // create the zoom comboboxes
        Number[] zoomList = { 
            new Integer(1),
            new Integer(5), 
            new Integer(10), 
            new Integer(25), 
            new Integer(50), 
            new Integer(75), 
            new Integer(100) };        
        horizontalZoomComboBox = new JComboBox(zoomList);
        verticalZoomComboBox = new JComboBox(zoomList);
        horizontalZoomComboBox.setEditable(true);
        verticalZoomComboBox.setEditable(true);
        horizontalZoomComboBox.setSelectedItem(new Integer(100));
        verticalZoomComboBox.setSelectedItem(new Integer(100));
        horizontalZoomComboBox.addActionListener(this);
        verticalZoomComboBox.addActionListener(this);        
        horizontalZoomComboBox.setFont(viewComboBoxFont);
        verticalZoomComboBox.setFont(viewComboBoxFont);
        horizontalZoomComboBox.setToolTipText("Horizontal zoom");
        verticalZoomComboBox.setToolTipText("Vertical zoom");
        
        // Toolbar for graph tools (buttons)
        JToolBar graphToolBar = new JToolBar();
        graphToolBar.setFloatable(false); // Don't move it around        
        graphToolBar.add(percentToggleButton);
        graphToolBar.addSeparator();
        graphToolBar.add(horizontalLabelsToggleButton);
        graphToolBar.add(verticalLabelsToggleButton);
        graphToolBar.addSeparator();
        
        // the horizontal zoom panel        
        JPanel horizontalZoomButtonPanel = new JPanel(new GridLayout(1,2));
        horizontalZoomButtonPanel.add(horizontalZoomInButton);
        horizontalZoomButtonPanel.add(horizontalZoomOutButton);
        JPanel horizontalZoomPanel = new JPanel(new BorderLayout());
        horizontalZoomPanel.add(horizontalZoomComboBox, BorderLayout.CENTER);
        horizontalZoomPanel.add(horizontalZoomButtonPanel, BorderLayout.EAST);                

        /* Toolbar for vertical zoom controls */
        JToolBar verticalZoomToolBar = new JToolBar();
        verticalZoomToolBar.add(xAxisComboBox);
        verticalZoomToolBar.addSeparator();                
        verticalZoomToolBar.setFloatable(false);
        verticalZoomToolBar.add(verticalZoomComboBox);
        verticalZoomToolBar.add(verticalZoomInButton);
        verticalZoomToolBar.add(verticalZoomOutButton);                        
        
        // the northern panel
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(yAxisComboBox, BorderLayout.WEST);
        northPanel.add(horizontalScrollBar, BorderLayout.CENTER);
        northPanel.add(horizontalZoomPanel, BorderLayout.EAST);
        
        // the southern panel        
        JPanel southPanel = new JPanel(new BorderLayout());        
        southPanel.setBorder(BorderFactory.createEtchedBorder());
        
        southPanel.add(verticalZoomToolBar,BorderLayout.EAST);
        southPanel.add(new JPanel(),BorderLayout.CENTER); // An empty panel to fill the center (left over) space
        southPanel.add(graphToolBar,BorderLayout.WEST);        
                
        // the eastern panel
        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.add(verticalScrollBar, BorderLayout.CENTER);
        
        // add the panels to the View
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());        
        contentPanel.add(scatterPlot, BorderLayout.CENTER);
        contentPanel.add(southPanel, BorderLayout.SOUTH);
        contentPanel.add(northPanel, BorderLayout.NORTH);
        contentPanel.add(eastPanel, BorderLayout.EAST);
        
        this.setViewComponent(contentPanel);
    }
    
    /**
     * Implementation of the ActionListener interface.
     * @param event The event to handle.
     */
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if ((source == xAxisComboBox) || (source == yAxisComboBox)) {
            String xTerm = (String)xAxisComboBox.getSelectedItem();
            String yTerm = (String)yAxisComboBox.getSelectedItem();                        
            scatterPlot.setTerms(xTerm, yTerm);
        } else if (source == verticalLabelsToggleButton) {
            //scatterPlot.setVerticalXLabels(verticalLabelsCheckBox.isSelected());
            scatterPlot.setVerticalXLabels(true);
        } else if (source == horizontalLabelsToggleButton) {
            scatterPlot.setVerticalXLabels(false);
        } else if (source == percentToggleButton) {
            scatterPlot.setPercentValues(percentToggleButton.isSelected());            
        } else if (source == horizontalZoomInButton) {
            double newZoom = scatterPlot.getHorizontalVisibleRange() - 5;
            if (newZoom <= 0.0) {
                newZoom = 1.0;
            }
            horizontalZoomComboBox.setSelectedItem(new Double(newZoom));
        } else if (source == horizontalZoomOutButton) {
            double newZoom = scatterPlot.getHorizontalVisibleRange() + 5;
            if (newZoom > 100.0) {
                newZoom = 100.0;
            }
            horizontalZoomComboBox.setSelectedItem(new Double(newZoom));
        } else if (source == verticalZoomInButton) {
            double newZoom = scatterPlot.getVerticalVisibleRange() - 5;
            if (newZoom <= 0.0) {
                newZoom = 1.0;
            }
            verticalZoomComboBox.setSelectedItem(new Double(newZoom));
        } else if (source == verticalZoomOutButton) {
            double newZoom = scatterPlot.getVerticalVisibleRange() + 5;
            if (newZoom > 100.0) {
                newZoom = 100.0;
            }
            verticalZoomComboBox.setSelectedItem(new Double(newZoom));
        } else if (source == horizontalZoomComboBox) {
            Object selectedZoom = horizontalZoomComboBox.getSelectedItem();
            if (selectedZoom instanceof Number) {
                double newZoom = ((Number) selectedZoom).doubleValue();
                if ((newZoom > 0.0) && (newZoom <= 100.0)) {
                    scatterPlot.setHorizontalVisibleRange(newZoom);
                } else {
                    horizontalZoomComboBox.setSelectedItem(new Double(scatterPlot.getHorizontalVisibleRange()));
                }
            } else {
                horizontalZoomComboBox.setSelectedItem(new Double(scatterPlot.getHorizontalVisibleRange()));
            }            
        } else if (source == verticalZoomComboBox) {
            Object selectedZoom = verticalZoomComboBox.getSelectedItem();
            if (selectedZoom instanceof Number) {
                double newZoom = ((Number) selectedZoom).doubleValue();
                if ((newZoom > 0.0) && (newZoom <= 100.0)) {
                    scatterPlot.setVerticalVisibleRange(newZoom);
                } else {
                    verticalZoomComboBox.setSelectedItem(new Double(scatterPlot.getVerticalVisibleRange()));
                }
            } else {
                verticalZoomComboBox.setSelectedItem(new Double(scatterPlot.getVerticalVisibleRange()));
            }            
        }
    }
        
     /**
      * Called when a View is about to be disposed. Used for cleaning up and removing listeners.
      */
     public void cleanUp() {
        super.cleanUp(); 
        ApplicationManager.getInstance().removeToolChangeListener(scatterPlot);
     }    
    
     
     /**
      * Returns the name of this type of View.
      * @return The name of this type of View.
      */
     protected String getViewName() {
         return "ScatterPlot";
     }
     
     /**
      * Updates the term choosers.
      */
     public void updateTermChoosers(boolean chosenTermsChanged, boolean allTermsChanged)
     {
        ApplicationManager.debug("updating termchoosers");
         
         String[] terms = DataManager.getInstance().getChosenTerms(); // Get active terms
         
         String xPreviousChoice = (String) xAxisComboBox.getSelectedItem();    // Store previous choice
         String yPreviousChoice = (String) yAxisComboBox.getSelectedItem();
         
         DefaultComboBoxModel xModel = new DefaultComboBoxModel(terms);
         DefaultComboBoxModel yModel = new DefaultComboBoxModel(terms);

         if (xPreviousChoice != null)
             xModel.setSelectedItem(xPreviousChoice);                       // Restore previous choice (if it exists)
         if (yPreviousChoice != null)
             yModel.setSelectedItem(yPreviousChoice);
         
         xAxisComboBox.setModel(xModel);             // Create new ComboBoxModel and set it
         yAxisComboBox.setModel(yModel);                  
         
         
         // setterm is necessary since listeners do not react to setSelectedItem
         scatterPlot.setTerms( (String) xAxisComboBox.getSelectedItem(), (String) yAxisComboBox.getSelectedItem());                                                                                              
     }

     /**
      * Invalidates the view.
      */
     protected void invalidateView() {
        scatterPlot.invalidateChart();
     }

    /**
     * Called after a change in data has been made and checks if the selection for this view has changed.
     * If so is the case then recreate internal data structures and repaint the view.
     */    
    public void validateView() {
        super.validateView();
        scatterPlot.validateChart();
    }          
    
    /** 
     * Set the aggregation for the current view.
     *
     * @param agg The aggregation to use.
     */
    protected void updateAggregation(medview.datahandling.aggregation.Aggregation agg) {
        scatterPlot.setAggregation(agg);
    }
    
    /**
     * Returns the x-axis term.
     *
     * @return The x-axis term.
     */
    public String getXTerm() {
        return scatterPlot.getXTerm();
    }
    
    /**
     * Returns the y-axis term.
     *
     * @return The y-axis term.
     */
    public String getYTerm() {
        return scatterPlot.getYTerm();
    }
    
    /**
     * Returns true if percent values are used.
     *
     * @return True if percent values are used.
     */
    public boolean isPercentValuesUsed() {
        return scatterPlot.isPercentValuesUsed();
    }
}

