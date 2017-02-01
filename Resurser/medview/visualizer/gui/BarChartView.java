/*
 * BarChartView.java
 *
 * Created on July 16, 2002, 2:45 PM
 *
 * $Id: BarChartView.java,v 1.28 2005/01/26 13:06:54 erichson Exp $
 *
 * $Log: BarChartView.java,v $
 * Revision 1.28  2005/01/26 13:06:54  erichson
 * Removed junk at end of file
 *
 * Revision 1.26  2002/11/14 16:00:58  zachrisg
 * Added support for session loading/saving of aggregations.
 *
 * Revision 1.25  2002/11/13 14:06:54  zachrisg
 * Added support for session saving.
 *
 * Revision 1.24  2002/10/17 11:10:48  erichson
 * separated stackedBarButton into two separate buttons. This should avoid confusion.
 *
 * Revision 1.23  2002/10/10 14:50:04  erichson
 * added setAggregation
 *
 * Revision 1.22  2002/10/08 10:21:55  erichson
 * Nicer control button layout, cleaned out (removed) the old control code which was commented out
 *
 * Revision 1.21  2002/10/04 13:06:44  erichson
 * Changed to icon-based controls + added tooltips
 *
 * Revision 1.20  2002/10/03 13:44:54  erichson
 * Updated control panel for better visibility
 *
 */

package medview.visualizer.gui;

import medview.datahandling.NoSuchTermException;

import com.jrefinery.data.*; // datasets

import java.awt.*; // FlowLayout, BorderLayout
import java.awt.event.*; // ActionListener
import javax.swing.*; // Components etc
import javax.swing.border.*; // TitledBorder etc

import medview.visualizer.data.*;
import medview.visualizer.dnd.*; // Transferhandler...

/**
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class BarChartView extends View implements ActionListener {
    
    private BarChart barChart;

    private JComboBox termComboBox;
    private JToggleButton horizontalBarButton, verticalBarButton;    
    private JToggleButton horizontalXLabelsButton,verticalXLabelsButton;
    
    private JToggleButton stackedBarsButton, unstackedBarsButton, percentValueButton;
    
    private ButtonGroup alignmentButtonGroup;
    
    /** The scrollbar. */
    private JScrollBar scrollBar;
    
    /** The zoom combobox. */
    private JComboBox zoomComboBox;
    
    /** The zoom in button. */
    private JButton zoomInButton;
   
    /** The zoom out button. */
    private JButton zoomOutButton;

    /** The horizontal scrollpanel. */
    private JPanel horizontalScrollPanel;
    
    /** The vertical scrollpanel. */
    private JPanel verticalScrollPanel;
    
    private String[] terms;
    
    private JPanel contentPanel;
    
    private static final ApplicationManager appManager = ApplicationManager.getInstance();
    
    /** Creates a new instance of BarChartView. Term is set to the first term given by DataManager */
    public BarChartView(ExaminationDataSet dataSet, boolean horizontal, boolean stacked, boolean percentValues) {
        this(dataSet,horizontal,stacked, percentValues, null);
    }
    
    /** Creates a new instance of BarChartView */
    public BarChartView(ExaminationDataSet dataSet, boolean horizontal, boolean stacked, boolean percentValues,  String term) {
        super(dataSet);
        
        terms = DataManager.getInstance().getChosenTerms();
        
        boolean termExist = false;
        for (int i = 0; i < terms.length; i++) {
            if (terms[i].equals(term)) {
                termExist = true;
            }
        }
        if (!termExist) {
            term = DataManager.getInstance().getDefaultTerm();
        }
        
        
        
        CategoryGraphDataSet cDataSet = new CategoryGraphDataSet(dataSet, term, percentValues);
        
        // create the chart
        ViewTransferHandler th = new ViewTransferHandler(this);
        barChart = new BarChart(cDataSet, term, horizontal, stacked);
        barChart.setTransferHandler(th);
        ApplicationManager.getInstance().addToolChangeListener(barChart);

        // the scrollbar
        if (horizontal) {
            scrollBar = new JScrollBar(JScrollBar.VERTICAL);
        } else {
            scrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
        }
        scrollBar.setModel(cDataSet.getRangeModel());
        
        // the scrollpanels
        horizontalScrollPanel = new JPanel(new GridLayout(1,1));
        verticalScrollPanel = new JPanel(new GridLayout(1,1));
        if (horizontal) {
            verticalScrollPanel.add(scrollBar);
        } else {
            horizontalScrollPanel.add(scrollBar);
        }
        
        // the zoom combobox
        Number[] zoomList = { 
            new Integer(1),
            new Integer(5), 
            new Integer(10), 
            new Integer(25), 
            new Integer(50), 
            new Integer(75), 
            new Integer(100) };
        zoomComboBox = new JComboBox(zoomList);
        zoomComboBox.setEditable(true);
        zoomComboBox.setSelectedItem(new Integer(100));
        zoomComboBox.addActionListener(this);
        zoomComboBox.setFont(viewComboBoxFont);
        zoomComboBox.setToolTipText("Zoom factor");
        
        // the zoom buttons
        zoomInButton = ApplicationManager.getInstance().createIconButton("zoomin16.png", "+", viewButtonDimension);
        zoomOutButton = ApplicationManager.getInstance().createIconButton("zoomout16.png", "-", viewButtonDimension);
        zoomInButton.addActionListener(this);
        zoomOutButton.addActionListener(this);
        zoomInButton.setToolTipText("Zoom in");
        zoomOutButton.setToolTipText("Zoom out");
        
        // the zoom buttons panel
        JPanel zoomPanel = new JPanel(new BorderLayout());
        JPanel zoomButtonPanel = new JPanel(new GridLayout(1,2)); 
        zoomButtonPanel.add(zoomInButton);
        zoomButtonPanel.add(zoomOutButton);
        zoomPanel.add(zoomComboBox, BorderLayout.CENTER);
        zoomPanel.add(zoomButtonPanel, BorderLayout.EAST);                        
        
        // percent value button        
        percentValueButton = appManager.createIconToggleButton("percent16.png","%",viewButtonDimension);
        percentValueButton.addActionListener(this);
        percentValueButton.setToolTipText("Values in percent (%)");
        percentValueButton.setSelected(percentValues);
        //percentValueButton.setFont(buttonLabelFont);
                        
        // stacked bars button        
        
          
            //stackedBarButton.setSelectedIcon(appManager.loadIcon("unstackedBars16.png"));
        //} catch (java.io.IOException ioe) { } // inget att g?ra ?t...
        
        stackedBarsButton = appManager.createIconToggleButton("stackedBars16.png","Stacked bars",viewButtonDimension);
        stackedBarsButton.addActionListener(this);
        stackedBarsButton.setToolTipText("Stacked bars");
        stackedBarsButton.setSelected(stacked);
        //stackedBarButton.setFont(buttonLabelFont);                
        
        unstackedBarsButton = appManager.createIconToggleButton("unstackedBars16.png","Unstacked bars",viewButtonDimension);
        unstackedBarsButton.addActionListener(this);
        unstackedBarsButton.setToolTipText("Unstacked bars");
        unstackedBarsButton.setSelected(!stacked);
        
        ButtonGroup stackedBarsGroup = new ButtonGroup();
        stackedBarsGroup.add(stackedBarsButton);
        stackedBarsGroup.add(unstackedBarsButton);
        
        horizontalBarButton = appManager.createIconToggleButton("horizontalBarChart16.png","Horizontal bars",viewButtonDimension);
        horizontalBarButton.addActionListener(this);
        horizontalBarButton.setSelected(horizontal);
        horizontalBarButton.setToolTipText("Horizontal bars");
        //horizontalBarButton.setFont(buttonLabelFont);
                
        verticalBarButton = appManager.createIconToggleButton("verticalBarChart16.png","Vertical bars",viewButtonDimension);
        verticalBarButton.addActionListener(this);
        verticalBarButton.setSelected(!horizontal);
        verticalBarButton.setToolTipText("Vertical bars");
        //verticalBarButton.setFont(buttonLabelFont);
        
        
        // ButtonGroup for horizontal/vertical bar alignment
        ButtonGroup barAlignmentButtonGroup = new ButtonGroup();
        barAlignmentButtonGroup.add(horizontalBarButton);
        barAlignmentButtonGroup.add(verticalBarButton);
                
        // The x axis term panel
        termComboBox = new JComboBox(terms);
        termComboBox.setSelectedItem(term);
        termComboBox.addActionListener(this);
        termComboBox.setFont(viewComboBoxFont);
        termComboBox.setToolTipText("Visualized term");
                
        JPanel xTermPanel = new JPanel();
        xTermPanel.add(termComboBox);
        /*xTermPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
            "Term to visualize",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            smallTitledBorderFont
        ));*/
        
        
        // Vertical / horizontal labels panel        
        horizontalXLabelsButton = appManager.createIconToggleButton("horizontalXLabels16.png","Horizontal X labels",viewButtonDimension);
        //horizontalXLabelsButton.setFont(buttonLabelFont);        
        horizontalXLabelsButton.addActionListener(this);
        horizontalXLabelsButton.setToolTipText("Horizontal X labels");     
        horizontalXLabelsButton.setSelected(! barChart.isXLabelsVertical());
                
        verticalXLabelsButton = appManager.createIconToggleButton("verticalXLabels16.png","Vertical X labels",viewButtonDimension);        
        verticalXLabelsButton.addActionListener(this);
        verticalXLabelsButton.setToolTipText("Verticall X labels");
        verticalXLabelsButton.setSelected(barChart.isXLabelsVertical());
        //verticalXLabelsButton.setFont(buttonLabelFont);
        
        JToolBar toolButtons = new JToolBar();
        toolButtons.setFloatable(false);        
        toolButtons.add(percentValueButton);
        toolButtons.addSeparator();        
        toolButtons.add(stackedBarsButton);
        toolButtons.add(unstackedBarsButton);
        toolButtons.addSeparator();        
        toolButtons.add(horizontalBarButton);
        toolButtons.add(verticalBarButton);
        toolButtons.addSeparator();                
        toolButtons.add(horizontalXLabelsButton);
        toolButtons.add(verticalXLabelsButton);
        
        ButtonGroup labelAlignmentButtonGroup = new ButtonGroup();
        labelAlignmentButtonGroup.add(horizontalXLabelsButton);
        labelAlignmentButtonGroup.add(verticalXLabelsButton);
        
        // Make the south panel which contains the above
        JPanel southPanel = new JPanel(new BorderLayout()); 
        southPanel.setBorder(BorderFactory.createEtchedBorder());
        
        southPanel.add(toolButtons,BorderLayout.WEST);
        southPanel.add(new JPanel(),BorderLayout.CENTER); // Empty panel to fill upp empty (center) space
        southPanel.add(xTermPanel,BorderLayout.EAST);        
        
        // Make the northern panel        
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(horizontalScrollPanel, BorderLayout.CENTER);
        northPanel.add(zoomPanel, BorderLayout.EAST);
        
        // Add all the content to one panel (borderlayout);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        
        contentPanel.add(northPanel, BorderLayout.NORTH);
        contentPanel.add(barChart, BorderLayout.CENTER);
        contentPanel.add(southPanel, BorderLayout.SOUTH);
        contentPanel.add(verticalScrollPanel, BorderLayout.EAST);
        
        this.setViewComponent(contentPanel);
    }
    
    /**
     * Implementation of the ActionListener interface.
     * @param event The event to handle.
     */
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        
        if (source == termComboBox){
            String term = (String)termComboBox.getSelectedItem();
            barChart.setTerm(term);
        } else if (source == verticalXLabelsButton) {
            barChart.setXLabelAlignment(BarChart.ALIGNMENT_VERTICAL);
        } else if (source == horizontalXLabelsButton) {
            barChart.setXLabelAlignment(BarChart.ALIGNMENT_HORIZONTAL);
        } else if (source == verticalBarButton) {
            barChart.setBarAlignment(BarChart.ALIGNMENT_VERTICAL); // Change label alignment to vertical as well. reegenerates
            verticalXLabelsButton.setSelected(true);
            // remove the scrollbar
            verticalScrollPanel.remove(scrollBar);
            horizontalScrollPanel.remove(scrollBar);
            // set the scrollbar orientation
            scrollBar.setOrientation(JScrollBar.HORIZONTAL);
            // add the scrollbar
            horizontalScrollPanel.add(scrollBar);
            validate();
            repaint();
        } else if (source == horizontalBarButton) {
            setBarAlignment(BarChart.ALIGNMENT_HORIZONTAL);
            
            // remove the scrollbar
            verticalScrollPanel.remove(scrollBar);
            horizontalScrollPanel.remove(scrollBar);
            // set the scrollbar orientation
            scrollBar.setOrientation(JScrollBar.VERTICAL);
            // add the scrollbar
            verticalScrollPanel.add(scrollBar);
            validate();
            repaint();
        } else if (source == stackedBarsButton) {
            barChart.setStackedBars(stackedBarsButton.isSelected());
        } else if (source == unstackedBarsButton) {
            barChart.setStackedBars(!unstackedBarsButton.isSelected());
        } else if (source == percentValueButton) {
            barChart.setPercentValues(percentValueButton.isSelected());
        } else if (source == zoomInButton) {
            double newZoom = barChart.getVisibleRange() - 5;
            if (newZoom <= 0.0) {
                newZoom = 1.0;
            }
            zoomComboBox.setSelectedItem(new Double(newZoom));
        } else if (source == zoomOutButton) {
            double newZoom = barChart.getVisibleRange() + 5;
            if (newZoom > 100.0) {
                newZoom = 100.0;
            }
            zoomComboBox.setSelectedItem(new Double(newZoom));
        } else if (source == zoomComboBox) {
            Object selectedZoom = zoomComboBox.getSelectedItem();
            if (selectedZoom instanceof Number) {
                double newZoom = ((Number) selectedZoom).doubleValue();
                if ((newZoom > 0.0) && (newZoom <= 100.0)) {
                    barChart.setVisibleRange(newZoom);
                } else {
                    zoomComboBox.setSelectedItem(new Double(barChart.getVisibleRange()));
                }
            } else {
                zoomComboBox.setSelectedItem(new Double(barChart.getVisibleRange()));
            }
        }
    }
    
    /**
     * Called when a View is about to be disposed. Used for cleaning up and removing listeners.
     */
    public void cleanUp() {
        super.cleanUp();
        ApplicationManager.getInstance().removeToolChangeListener(barChart);
    }
    
    /**
     * Changes the bar alignment. Also updates the choice buttons.
     */
    private void setBarAlignment(int newAlignment) {
        barChart.setBarAlignment(newAlignment);
        switch(newAlignment) {
            case BarChart.ALIGNMENT_HORIZONTAL:        
                horizontalXLabelsButton.setSelected(true);
                verticalXLabelsButton.setSelected(false);
                break;
            case BarChart.ALIGNMENT_VERTICAL:
                verticalXLabelsButton.setSelected(true);
                horizontalXLabelsButton.setSelected(false);
                break;
        }
    }
    
    /**
     * Returns true if the bars are horizontal.
     *
     * @return True if the bars are horizontal.
     */
    public boolean isHorizontal() {
        return barChart.isHorizontal();
    }
    
    /**
     * Returns true if percent values are used.
     *
     * @return True if percent values are used.
     */
    public boolean isPercentValuesUsed() {
        return barChart.isPercentValuesUsed();
    }
    
    /**
     * Returns true if the bars are stacked.
     *
     * @return True if the bars are stacked.
     */
    public boolean isBarsStacked() {
        return barChart.isBarsStacked();
    }
    
    /**
     * Returns the term currently displayed in the barchart.
     *
     * @return The term currently displayed in the barchart.
     */
    public String getTerm() {
        return barChart.getTerm();
    }
    
    /**
     * Invalidate this view, so that it is regenerated in the next repaint
     */
    protected void invalidateView() {
        barChart.invalidateChart();
    }
    
    /**
     * Called after a change in data has been made and checks if the selection for this view has changed.
     * If so is the case then recreate internal data structures and repaint the view.
     */
    public void validateView() {
        super.validateView();
        barChart.validateChart();
    }
    
    /**
     * Returns the name of this type of View.
     *
     * @return The name of this type of View.
     */
    protected String getViewName() {
        return "BarChart";
    }
    
    /**
     * Updates the term choosers.
     */
    public void updateTermChoosers(boolean chosenTermsChanged, boolean allTermsChanged) 
    {
        
        // System.out.println("barchart.updateTermChoosers called");
        
        String[] terms = DataManager.getInstance().getChosenTerms(); // Get active terms
        
        String previousChoice = (String) termComboBox.getSelectedItem();    // Store previous choice
        termComboBox.setModel(new DefaultComboBoxModel(terms));             // Create new ComboBoxModel and set it
        if (previousChoice != null)
            termComboBox.setSelectedItem(previousChoice);                       // Restore previous choice (if it exists)
        
        
        // setTerm is necessary since listeners do not react to setSelectedItem
        barChart.setTerm( (String) termComboBox.getSelectedItem());         // Update graph with current combobox term. This is necessary since previousChoice might not still exist in the box                
    }
    
    protected void updateAggregation(medview.datahandling.aggregation.Aggregation agg) {
        barChart.setAggregation(agg);
    }
    
}
