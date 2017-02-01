/*
 * StatisticsView.java
 *
 * Created on October 3, 2002, 3:51 PM
 *
 * $Id: StatisticsView.java,v 1.24 2005/02/16 11:05:48 erichson Exp $
 *
 * $Log: StatisticsView.java,v $
 * Revision 1.24  2005/02/16 11:05:48  erichson
 * Updated exception handling.
 *
 * Revision 1.23  2005/01/26 13:13:45  erichson
 * Fixed junk at end of file
 *
 * Revision 1.21  2004/10/20 12:07:09  erichson
 * Messed up last fix, correcting now
 *
 * Revision 1.20  2004/10/20 12:04:52  erichson
 * Small change since ExtensionFileFilter constructor changed
 *
 * Revision 1.19  2004/06/24 20:29:08  erichson
 * Small cosmetic changes
 *
 * Revision 1.18  2002/11/26 15:16:38  zachrisg
 * Removed the status text from the statusbar.
 *
 * Revision 1.17  2002/11/26 14:50:07  zachrisg
 * Fixed bug that caused the view not to get updated on aggregation changes.
 *
 * Revision 1.16  2002/11/26 14:39:21  zachrisg
 * Fixed bug that caused the view to use a different term than the dataset if the
 * term didn't belong to the set of choosen terms.
 *
 * Revision 1.15  2002/11/18 15:15:09  zachrisg
 * It is now possible to completely remove the statusbar.
 *
 * Revision 1.14  2002/11/14 16:01:02  zachrisg
 * Added support for session loading/saving of aggregations.
 *
 * Revision 1.13  2002/11/13 15:20:40  zachrisg
 * Fixed bug in getTerm().
 *
 * Revision 1.12  2002/11/13 14:54:55  zachrisg
 * Added support for session loading/saving.
 *
 * Revision 1.11  2002/11/01 10:31:34  zachrisg
 * StatisticsView no longer eats drag events.
 *
 * Revision 1.10  2002/10/10 15:05:51  zachrisg
 * Removed shadowed first row.
 *
 * Revision 1.9  2002/10/10 14:41:10  zachrisg
 * Moved export code to TableWriter class.
 *
 * Revision 1.8  2002/10/10 14:24:07  erichson
 * Added setAggregation method
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import jxl.*;
import jxl.write.*;

import medview.visualizer.data.*;
import medview.visualizer.dnd.*;

import misc.gui.ExtensionFileFilter;

/**
 * A View that displays some information about the distribution of
 * elements over the values of a term.
 *
 * @author G?ran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class StatisticsView extends View implements ActionListener 
{
    
    /** The term. */
    private String term;
     
    /** The category graph data set used for calculations. */
    private CategoryGraphDataSet cDataSet;

    /** The table component. */
    private JComponent tableComponent;
    
    /** The term combobox. */
    private JComboBox termComboBox;

    /** The filechooser. */
    private JFileChooser fileChooser;
    
    /** The excel export button. */
    private JButton excelExportButton;
    
    /** The text export button. */
    private JButton txtExportButton;
    
    /** The main panel. */
    private JPanel mainPanel;
    
    /** The transfer handler. */
    private TransferHandler transferHandler;
    
    /** The table. */
    private JTable table;
    
    /** 
     * Creates a new instance of StatisticsView.
     *
     * @param dataSet The data set.
     * @param statisticsTerm The term to do statistics on.
     */
    public StatisticsView(ExaminationDataSet dataSet, String statisticsTerm) {
        super(dataSet, true, true, false);        

        term = null;
        
        // check if term belongs to the choosen terms
        String[] choosenTerms = DataManager.getInstance().getChosenTerms();
        for (int i = 0; i < choosenTerms.length; i++) {
            if (statisticsTerm.equals(choosenTerms[i])) {
                term = statisticsTerm;
            }
        }
        if (term == null) {
            term = DataManager.getInstance().getDefaultTerm();
        }
                
        cDataSet = new CategoryGraphDataSet(dataSet, term, false);

        // create the filechooser
        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);

        // create the transfer handler
        transferHandler = new ViewTransferHandler(this);
        
        // create the table component
        tableComponent = createTableComponent();
        
        // create the term combobox
        termComboBox = new JComboBox(choosenTerms);
        termComboBox.setSelectedItem(term);
        termComboBox.addActionListener(this);

        // create the excel export button
        excelExportButton = new JButton("Export as excel file (.xls)");
        excelExportButton.addActionListener(this);
        
        // create the text export button
        txtExportButton = new JButton("Export as text file (.txt)");
        txtExportButton.addActionListener(this);
        
        // create the toolbar
        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        toolBar.setFloatable(false);
        toolBar.add(excelExportButton);
        toolBar.add(txtExportButton);

        // create the northern panel
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        northPanel.add(new JLabel("Distribution of examinations regarding: "));
        northPanel.add(termComboBox);
        
        // create the main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(tableComponent, BorderLayout.CENTER);
        mainPanel.setTransferHandler(transferHandler);

        // put the main panel and the toolbar in a panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(toolBar, BorderLayout.NORTH);
        panel.add(mainPanel, BorderLayout.CENTER);
       
        setViewComponent(panel);
    }
    
    /**
     * Creates the table with all the statistics.
     * 
     * @return The table component.
     */
    private JComponent createTableComponent() {        
        table = new JTable(new StatisticsTableModel(cDataSet));
        table.setDefaultRenderer((new Object()).getClass(), new DefaultTableCellRenderer() {
            private java.awt.Font plainFont = (new JLabel()).getFont().deriveFont(java.awt.Font.PLAIN);
            private java.awt.Font boldFont = (new JLabel()).getFont().deriveFont(java.awt.Font.BOLD);
            
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table,
                    value, isSelected, hasFocus, row, column);
                if (table.convertColumnIndexToModel(column) == 0) {
                    component.setFont(boldFont);
                    if (isSelected) {
                        component.setBackground(Color.darkGray);
                    } else {
                        component.setBackground(Color.lightGray);
                    }
                } else {
                    component.setFont(plainFont);
                    if (isSelected) {
                        // this case is handled by the default renderer
                    } else {
                        component.setBackground(Color.white);
                    }
                }
                return component;
            }});
        
        // see to that the table will not eat drag events
        table.setTransferHandler(null);
        
        return new JScrollPane(table);
    }
    
    /** 
     * Returns the name of the type of view.
     *
     * @return The view type name.
     */
    protected String getViewName() {
        return "Statistics";
    }
    
    /** 
     * Marks the view as invalid.
     * Called when an element has been selected or deselected.
     */
    protected void invalidateView() {
    }
    
    /** 
     * Updates everything that needs to know which terms exist.
     */
    public void updateTermChoosers(boolean chosenTermsChanged, boolean allTermsChanged)
    {
        // save the old term
        String oldTerm = (String) termComboBox.getSelectedItem();
        
        // create the new combobox model
        String[] chosenTerms = DataManager.getInstance().getChosenTerms();
        DefaultComboBoxModel model = new DefaultComboBoxModel(chosenTerms);
        
        // select the right item
        boolean termExists = false;
        for (int i = 0; i < chosenTerms.length; i++) {
            if (chosenTerms[i].equals(oldTerm)) {
                termExists = true;
            }
        }

        // set the new model of the combobox
        termComboBox.setModel(model);        
        
        if (termExists) {
            model.setSelectedItem(oldTerm);
        } else {
            // select the default term
            model.setSelectedItem(DataManager.getInstance().getDefaultTerm());
            
            // update the view
            cDataSet.setTerm((String)termComboBox.getSelectedItem());
            dataSetHasChanged = true;
            validateView();
        }
        
    }

    /**
     * Called after a change in data has been made and checks if the selection for this view has changed.
     * If so is the case then recreate internal data structures and repaint the view.
     */    
    public void validateView() {
        if (dataSetHasChanged) {
            cDataSet.validateDataSet();
            // make sure the view gets repainted
            validate();
            repaint();
        }
        super.validateView();
    }
    
    /**
     * Handles term changes and export clicks.
     *
     * @param event The event.
     */
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if (source == termComboBox) // Term change event
        {           
            cDataSet.setTerm((String)termComboBox.getSelectedItem()); // Change term
            dataSetHasChanged = true;
            validateView();
        } else if (source == excelExportButton) 
            // Excel export
        {
            fileChooser.resetChoosableFileFilters();
            fileChooser.setFileFilter(new ExtensionFileFilter("xls", "Excel 97 Workbook (.xls)",true));
            int result = fileChooser.showSaveDialog(ApplicationFrame.getInstance());
            if (result == JFileChooser.APPROVE_OPTION) {                
                File file = fileChooser.getSelectedFile();
                if (!file.getPath().toLowerCase().endsWith(".xls")) {
                    file = new File(file.getPath() + ".xls");
                }
                try
                {
                    TableWriter.writeTableToExcelFile(table, file);
                } catch (IOException ioe)
                {
                    JOptionPane.showMessageDialog(this,
                            "Write to excel file failed: " + ioe.getMessage(),
                            "Excel export failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (source == txtExportButton) 
            // Text file export
        {
            fileChooser.resetChoosableFileFilters();
            fileChooser.setFileFilter(new ExtensionFileFilter("txt", "Tab-separated Textfile (.txt)",true));
            int result = fileChooser.showSaveDialog(ApplicationFrame.getInstance());
            if (result == JFileChooser.APPROVE_OPTION) {                
                File file = fileChooser.getSelectedFile();
                if (!file.getPath().toLowerCase().endsWith(".txt")) {
                    file = new File(file.getPath() + ".txt");
                }
                try {
                    TableWriter.writeTableToTextFile(table, file);
                } catch (IOException ioe)
                {
                    JOptionPane.showMessageDialog(this,
                            "Write table to text file failed: " + ioe.getMessage(),
                            "Text file export failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }    

    /**
     * Sets the active aggregation.
     *
     * @param agg The new active aggregation.
     */
    protected void updateAggregation(medview.datahandling.aggregation.Aggregation agg) {
        ((StatisticsTableModel) table.getModel()).setAggregation(agg);
        dataSetHasChanged = true;
        validateView();
    }
    
    /**
     * Returns the displayed term.
     *
     * @return The displayed term.
     */
    public String getTerm() {
        return cDataSet.getTerm();        
    }
}
/*
                                                                                                   
 **/