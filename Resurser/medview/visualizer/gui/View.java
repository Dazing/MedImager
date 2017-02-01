/*
 * ViewComponent.java
 *
 * Created on June 26, 2002, 2:27 PM
 *
 * $Id: View.java,v 1.42 2005/01/26 13:06:55 erichson Exp $
 *
 * $Log: View.java,v $
 * Revision 1.42  2005/01/26 13:06:55  erichson
 * Removed junk at end of file
 *
 * Revision 1.40  2004/11/16 09:25:32  erichson
 * Added null checking since sometimes there is no aggregationChooser now
 *
 * Revision 1.39  2004/10/18 10:57:12  erichson
 * Moved aggregation chooser gui out to own class, AggregationChooser in common
 *
 * Revision 1.38  2004/10/11 11:41:41  erichson
 * Moved view constants here
 *
 * Revision 1.37  2004/10/08 15:57:58  erichson
 * Added functionality to allow for a more dynamic status bar (to be used by for example ImageView
 * when examinations are filtered out)
 *
 * Revision 1.36  2002/12/05 12:21:04  zachrisg
 * Added a constructor that makes the trashcan optional.
 *
 * Revision 1.35  2002/11/26 15:28:26  zachrisg
 * Changed the remaining swedish strings to english.
 *
 * Revision 1.34  2002/11/26 15:14:03  zachrisg
 * Added constructor that allows for the status text to be removed.
 *
 * Revision 1.33  2002/11/26 13:45:19  erichson
 * loadIcon() -> loadVisualizerIcon()
 *
 * Revision 1.32  2002/11/21 15:50:25  erichson
 * Just one additional line of javadoc
 *
 * Revision 1.31  2002/11/18 15:15:08  zachrisg
 * It is now possible to completely remove the statusbar.
 *
 * Revision 1.30  2002/11/14 16:01:04  zachrisg
 * Added support for session loading/saving of aggregations.
 *
 * Revision 1.29  2002/11/11 12:39:36  zachrisg
 * Added getAggregation().
 *
 * Revision 1.28  2002/11/07 16:34:10  erichson
 * Added aggregation icon button to the statusbar, changed constructor so that aggregation controls is optional
 *
 * Revision 1.27  2002/10/10 15:11:03  erichson
 * Added aggregation combo box to the status bar
 *
 * Revision 1.26  2002/10/10 14:45:01  erichson
 * Made View listen to DataManager for changes to the aggregates set. When the aggregates set in datamanager is changed, the combobox is updated.
 *
 * Revision 1.25  2002/10/04 13:05:48  erichson
 * added ViewButtonDimension
 *
 * Revision 1.24  2002/10/03 13:44:00  erichson
 * Moved font constants here to be shared by all views
 *
 * Revision 1.23  2002/10/03 13:42:52  zachrisg
 * Changed javadoc and  moved updateTermChoosers().
 *
 * Revision 1.22  2002/09/27 15:37:37  erichson
 * Now adds aggregationlisteners to datasets
 *
 */

package medview.visualizer.gui;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;

import medview.common.components.aggregation.*;

import medview.visualizer.data.*;
import medview.visualizer.event.*;
import medview.visualizer.dnd.*;

import medview.datahandling.aggregation.*;

import com.jrefinery.data.*;

/**
 * A graphical view component visualizing a dataset in some way. A View can be placed in a ViewFrame.
 *
 * @author  G?ran Zachrisson <zachrisg@mdstud.chalmers.se>
 * @version 
 */
public abstract class View extends JPanel implements SelectionListener, DatasetChangeListener, TermsChangeListener, AggregationListener {

    /* View type constants */
    public static final String BARCHART_VIEW_TYPE = "barchartview";
    public static final String SCATTERPLOT_VIEW_TYPE = "scatterplotview";    
    public static final String IMAGE_VIEW_TYPE = "imageview";
    public static final String STATISTICS_VIEW_TYPE = "statisticsview";    
    public static final String TABLE_VIEW_TYPE = "tableview";
    public static final String SUMMARY_VIEW_TYPE = "summaryview";
    public static final String UNKNOWN_VIEW_TYPE = "unknownview";
    
    private final static NumberFormat nf;    
    static {        
            nf = NumberFormat.getPercentInstance();
            nf.setMinimumFractionDigits(1);
            nf.setMaximumFractionDigits(1);            
    }
    
    /** True if the selection in the dataset that is monitored by the view has changed. */
    protected boolean selectionHasChanged = false;
    
    /** True if the data in the dataset has changed. */
    protected boolean dataSetHasChanged = false;
    
    /** Standard dimensions for control buttons in View panels */
    protected static final Dimension viewButtonDimension = new Dimension(24,24);

    
    /** True if some elements has changed groups. */
    //protected boolean dataGroupHasChanged = false;
    
    /** The ExaminationDataSet associated with the View. */
    protected ExaminationDataSet examinationDataSet = null;
    
    /** The view's status bar. */
    private JPanel statusBar;
    
    /** The status label. */
    private JLabel statusLabel;
    
    /** The trashcan button. */
    private JButton trashcanButton;
    
 
    
    /** The panel containing the graph component. */
    private JPanel contentPanel;
    
  
    /** The aggregation chooser for this view */
    private AggregationChooser aggregationChooser = null;
 
    
    protected final Font smallTitledBorderFont = UIManager.getLookAndFeelDefaults().getFont("TitledBorder.font").deriveFont( 9.0f);        
    protected final Font buttonLabelFont = smallTitledBorderFont.deriveFont( 10.0f);                
    protected final Font viewComboBoxFont = buttonLabelFont;

    // protected Chart plot;    

    /** The title change listeners. */
    private Vector titleChangeListeners;
    
    /** The title. */
    private String title;
        
    private static final DataManager DM = DataManager.getInstance();
    
    /** 
     * Creates new View with a statusbar and aggregation controls.
     *
     * @param dataSet The dataset to use for the View.
     */
    public View(ExaminationDataSet dataSet) {        
        this(dataSet,true);
    }
    
    /**
     * Creates a new View with a statusbar and with optional aggregation controls.
     *
     * @param dataSet The dataset to use for the View.
     * @param includeAggregationControls If true, then the statusbar has aggregation controls.
     */
    public View(ExaminationDataSet dataSet, boolean includeAggregationControls) {
        this(dataSet, includeAggregationControls, true);
    }

    /**
     * Creates a new View with an optional statusbar and optional aggregation controls.
     *
     * @param dataSet The dataset to use for the View.
     * @param includeAggregationControls If true, then the statusbar has aggregation controls.
     * @param includeStatusBar If true, then the View will have a statusbar.
     */    
    public View(ExaminationDataSet dataSet, boolean includeAggregationControls, boolean includeStatusBar) {
        this(dataSet, includeAggregationControls, includeStatusBar, true);
    }

    /**
     * Creates a new View with an optional statusbar and optional aggregation controls.
     *
     * @param dataSet The dataset to use for the View.
     * @param includeAggregationControls If true, then the statusbar has aggregation controls.
     * @param includeStatusBar If true, then the View will have a statusbar.
     * @param includeStatusText If true, then the status text will be displayed in the statusbar.
     */    
    public View(ExaminationDataSet dataSet, boolean includeAggregationControls, boolean includeStatusBar, boolean includeStatusText) {
        this(dataSet, includeAggregationControls, includeStatusBar, includeStatusText, true);
    }
        
    /**
     * Creates a new View with an optional statusbar and optional aggregation controls and an optional
     * trashcan.
     *
     * @param dataSet The dataset to use for the View.
     * @param includeAggregationControls If true, then the statusbar has aggregation controls.
     * @param includeStatusBar If true, then the View will have a statusbar.
     * @param includeStatusText If true, then the status text will be displayed in the statusbar.
     * @param includeTrashcan If true, then the trashcan will be displayed in the statusbar.
     */    
    public View(ExaminationDataSet dataSet, boolean includeAggregationControls, boolean includeStatusBar, boolean includeStatusText, boolean includeTrashcan) {
                
        // initialize variables
        title = "";
        titleChangeListeners = new Vector();
        
        DataManager.getInstance().addTermsChangeListener(this);              
        
        this.setLayout(new BorderLayout());

        // the status bar
        statusLabel = new JLabel("No dataset loaded");        
        try {
            ImageIcon trashcanIcon = ApplicationManager.getInstance().loadVisualizerIcon("trash16.gif");
            trashcanButton = new JButton(trashcanIcon);                        
        } catch(IOException e) {
            trashcanButton = new JButton("Remove");
        }
        trashcanButton.setTransferHandler(new ViewTrashcanTransferHandler(this));
        trashcanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String[] buttons = {"All examinations", "Selected examinations", "Cancel" };
                int result = OptionDialog.showOptionDialog(ApplicationFrame.getInstance(), new Point(100,100), "Remove?", "What do you want to remove?", buttons);
                if (result == 0) {
                    examinationDataSet.removeAllDataElements();
                    DataManager.getInstance().validateViews();
                } else if (result == 1) {
                    examinationDataSet.removeAllSelectedDataElements();
                    DataManager.getInstance().validateViews();
                }
            }});
        
        
        JToolBar trashAndLabelToolBar = new JToolBar();
        trashAndLabelToolBar.setFloatable(false);        

        if (includeTrashcan) {
            trashAndLabelToolBar.add(trashcanButton);
        }
        
        if (includeStatusText) {
            trashAndLabelToolBar.addSeparator();
            trashAndLabelToolBar.add(statusLabel);
        }
            
        statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.add(trashAndLabelToolBar,BorderLayout.WEST);
        statusBar.add(new JPanel()); // Empty jpanel which fills out the rest of the space
        
        if (includeAggregationControls) 
        {
            aggregationChooser = new AggregationChooser(ApplicationFrame.getInstance(),
                                                        DataManager.getInstance());
            aggregationChooser.addChangeListener(new javax.swing.event.ChangeListener()
            {
                public void stateChanged(javax.swing.event.ChangeEvent ev)
                {
                    AggregationChooser changedChooser = (AggregationChooser) ev.getSource();
                    updateAggregation(changedChooser.getSelectedAggregation());
                }
            });
            statusBar.add(aggregationChooser,BorderLayout.EAST);
        }
                    
        if (includeStatusBar) 
            this.add(statusBar,BorderLayout.SOUTH);

        contentPanel = new JPanel();
        this.add(contentPanel,BorderLayout.CENTER);                            
    
        setExaminationDataSet(dataSet);    
    }
    
    /**
     * Returns the View's ExaminationDataSet.
     * @return The View's ExaminationDataSet.
     */
    public ExaminationDataSet getExaminationDataSet() {
        return examinationDataSet;
    }
    
    /**
     * Sets the view's component. (DEPRECATED??)
     * @param component The new component.
     */
    public void setViewComponent(JComponent component) {        
        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.add(component,BorderLayout.CENTER);
    }
    
    /**
     * Sets the View's ExaminationDataSet.
     * @param newExaminationDataSet The View's new ExaminationDataSet.
     */
     public void setExaminationDataSet(ExaminationDataSet newExaminationDataSet) {

         // remove the old listeners
         if (examinationDataSet != null) {                  
            examinationDataSet.removeChangeListener(this);
            examinationDataSet.removeSelectionListener(this);
         }
         examinationDataSet = newExaminationDataSet;
         
         // add new listeners
         examinationDataSet.addChangeListener(this);
         examinationDataSet.addSelectionListener(this);
         DataManager.getInstance().addAggregationListener(this); // Listen to DataManager for aggregation changes.
         
         // update the view
         generateTitle();
         updateStatusBar();
     }

     /**
      * Implementation of the SelectionListener interface. Invoked when the selection of an ExaminationDataElement has changed.
      * @param e The object containing information about the event.
      */
     public void selectionChanged(SelectionEvent e) {
        selectionHasChanged = true;
        invalidateView();
     }
     
     
     public void aggregationChanged(AggregationEvent e) {
        updateAggregationChoosers();                
     }
     
     /** 
      * Handles dataset change notifications.
      * @param datasetChangeEvent The object containing information about the event.
      */
     public void datasetChanged(DatasetChangeEvent event) {
         dataSetHasChanged = true;
     }
        
    /**
     * Update the statusbar with the current data and selection info
     */ 
    public void updateStatusBar() {                   
                
        String labelText = "Selected: ";
        DataGroup[] dataGroups = examinationDataSet.getDataGroups();
        for (int i = 0; i < dataGroups.length; i++) {
            int count = examinationDataSet.getElementCountInDataGroup(dataGroups[i]);
            int selectedCount = examinationDataSet.getSelectedElementCountInDataGroup(dataGroups[i]);
            labelText += dataGroups[i].getName() + ": " 
                + selectedCount + "/" 
                + count;
            if (count == 0) {
                // this shouldn't happen
                labelText += ", ";
            } else {
                labelText += " (" + nf.format((double)selectedCount / (double)count) + "), ";
            }
        }
                
        labelText += "Total: " + getTotalSelectionString(true); // with percent
        setStatusBarText(labelText);        
    }
              
    public String getTotalSelectionString(boolean withPercent)
    {
        String labelText = new String();
        int totalSelectedCount = examinationDataSet.getSelectedElementCount();
        int totalCount = examinationDataSet.getElementCount();
        labelText += totalSelectedCount + "/" + totalCount;
        if (withPercent)
        {
            if (totalCount == 0) {
                labelText += " (0%)";
            } else {
                labelText += " (" + nf.format((double)totalSelectedCount / (double)totalCount) + ")";
            }        
        }
        return labelText;
    }
    
    public void setStatusBarText(String text)
    {
        statusLabel.setText(text);
    }
    
    /**
     * Called after a change in data has been made and checks if the selection for this view has changed.
     * If so is the case then recreate internal data structures and repaint the view.
     */    
    public void validateView() {
        if (dataSetHasChanged || selectionHasChanged) {
            if (dataSetHasChanged) {
                generateTitle();
            }
            updateStatusBar();
            dataSetHasChanged = false;
            selectionHasChanged = false;           
        }
    }
    
    /**
     * Adds a title change listener.
     * @param listener The listener to add.
     */
    public void addTitleChangeListener(TitleChangeListener listener) {
        if (!titleChangeListeners.contains(listener)) {
            titleChangeListeners.add(listener);
        }
    }
    
    /**
     * Removes a title change listener.
     * @param listener The listener to remove.
     */
    public void removeTitleChangeListener(TitleChangeListener listener) {
        titleChangeListeners.remove(listener);
    }
    
    /**
     * Notifies the title change listeners that the title has changed.
     */
    private void fireTitleChanged() {
        for (Iterator i = titleChangeListeners.iterator(); i.hasNext(); ) {
            TitleChangeListener listener = (TitleChangeListener) i.next();
            listener.titleChanged(new TitleChangeEvent(title));
        }
    }
    
    /**
     * Sets the window title.
     * @param newTitle The new window title.
     */
    public void setTitle(String newTitle) {
        title = newTitle;
        fireTitleChanged();
    }

    /**
     * Returns the desired window title.
     * @return The desired window title.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Regenerates the window's title and sets it.
     */
    public void generateTitle() {
        DataGroup[] dataGroups = examinationDataSet.getDataGroups();
        
        String newTitle = new String();
        for (int i = 0; i < dataGroups.length; i++) {
            if (!newTitle.equals("")) {
                newTitle += " + ";
            }
            newTitle += dataGroups[i].getName();
            if (examinationDataSet.getElementCountInDataGroup(dataGroups[i]) != dataGroups[i].getMemberCount()) {
                newTitle += " (partly)";
            }
        }
        newTitle = getViewName() + ": " + newTitle;
        setTitle(newTitle);
    }
    
    /**
     * Marks the view as invalid. 
     * Called when an element has been selected or deselected.
     */
    protected abstract void invalidateView();    
    
    /**
     * Returns the name of the type of view.
     *
     * @return The view type name.
     */
    protected abstract String getViewName();
    
     /**
      * Called when a View is about to be disposed. Used for cleaning up and removing listeners.
      */
     public void cleanUp() {
         // remove listeners
         if (examinationDataSet != null) {                  
            examinationDataSet.removeChangeListener(this); 
            examinationDataSet.removeSelectionListener(this);            
         }
         DataManager DM = DataManager.getInstance();
         DM.removeTermsChangeListener(this);         
     }
      
     public void termsChanged(TermsChangeEvent tce) 
     {
         updateTermChoosers(tce.isChosenTermsChanged(), tce.isNewTermsAdded());
     }

     /**
     * Updates everything that needs to know which terms exist.
     */
    public abstract void updateTermChoosers(boolean chosenTermsChanged, boolean allTermsChanged);
     
    /**
     * Updates the aggregation choosers with the aggregations in the data manager
     */
    public void updateAggregationChoosers() 
    {
        if (aggregationChooser != null)
            aggregationChooser.updateContents();
    }
    
    /**
     * Sets the current aggregation both in the View's aggregation chooser
     * and then calls updateAggregation() to make sure that subclasses get
     * notified of the aggregation change.
     *
     * @param agg The new aggregation.
     */
    public void setAggregation(Aggregation agg) {
        if (aggregationChooser != null)
            aggregationChooser.setSelectedAggregation(agg);
        updateAggregation(agg);
    }
    
    /**
     * Called when the contents of a View needs to be updated because
     * of a change of the active aggregation. This method should be
     * implemented by subclasses of View that want to be informed of
     * aggregation changes.
     *
     * @param agg the new aggregation
     */
    protected void updateAggregation(Aggregation agg) {
        // Do nothing. This should be implemented by subclasses.
    }

    /**
     * Returns the current aggregation.
     *
     * @return The currently active aggregation. Returns <code>null</code> if there is no aggregationChoooser.
     */
    public Aggregation getAggregation() 
    {
        if (aggregationChooser != null)
            return aggregationChooser.getSelectedAggregation();
        else
            return null;
    }    
}
