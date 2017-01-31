/*
 * TableView.java
 *
 * Created on October 2, 2002, 3:49 PM
 *
 * $Id: TableView.java,v 1.23 2005/02/16 11:09:02 erichson Exp $
 *
 * $Log: TableView.java,v $
 * Revision 1.23  2005/02/16 11:09:02  erichson
 * Improved exception handling when exporting
 *
 * Revision 1.22  2005/01/26 13:59:59  erichson
 * Bugfix: Export skrev alltid över fil utan att fråga
 *
 * Revision 1.21  2005/01/26 13:13:46  erichson
 * Fixed junk at end of file
 *
 * Revision 1.20  2005/01/26 13:04:47  erichson
 * All views update to new termhandling methods
 *
 * Revision 1.19  2004/12/21 13:02:13  erichson
 * Added excel limits and out of memory checking
 *
 * Revision 1.18  2004/12/17 11:47:38  erichson
 * Several improvements: Own term chooser, status bar, spss export or not
 *
 * Revision 1.17  2004/10/20 12:07:08  erichson
 * Messed up last fix, correcting now
 *
 * Revision 1.16  2004/10/20 12:04:43  erichson
 * Small change since ExtensionFileFilter constructor changed
 *
 * Revision 1.15  2004/08/30 14:00:56  d97nix
 * Now passes TermValueComparator to TableSorter, fixes bug #334
 *
 * Revision 1.14  2004/02/24 20:10:28  erichson
 * Updated to reflect how TableWriter is now called
 *
 * Revision 1.13  2002/12/19 09:13:50  zachrisg
 * Added horizontal scrollbar instead of the columns getting resized.
 *
 * Revision 1.12  2002/12/05 12:26:43  zachrisg
 * Made the View _not_ to use coupled selection.
 * Added a "Select examinations in other views" button.
 *
 * Revision 1.11  2002/11/14 16:01:03  zachrisg
 * Added support for session loading/saving of aggregations.
 *
 * Revision 1.10  2002/10/25 08:32:04  zachrisg
 * Made export to excel file more SPSS compatible.
 *
 * Revision 1.9  2002/10/23 15:00:00  zachrisg
 * Changed "-" between terms and values to "." for easier import to SPSS.
 *
 * Revision 1.8  2002/10/23 14:55:13  zachrisg
 * Adapted text export to SPSS.
 *
 * Revision 1.7  2002/10/23 12:46:45  zachrisg
 * Added a little piece of javadoc.
 *
 * Revision 1.6  2002/10/10 14:41:22  zachrisg
 * Moved export code to TableWriter class.
 *
 * Revision 1.5  2002/10/10 14:25:15  erichson
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
import javax.swing.event.*;
import javax.swing.table.*;

import jxl.*;
import jxl.write.*;

import medview.datahandling.*;
import medview.datahandling.aggregation.*;
import medview.datahandling.examination.*;
import medview.visualizer.data.*;
import medview.visualizer.dnd.*;

import misc.gui.ExtensionFileFilter;
import misc.gui.TableSorter;

/**
 * A table view displaying the choosen terms of the examinations. 
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class TableView extends View implements ActionListener, ChosenTermsContainer
{
    
    /** The actual table. */
    private JTable table;
    
    /** The excel export button. */
    private JButton excelExportButton;
    
    /** The text export button. */
    private JButton txtExportButton;
    
    /** The select examinations button. */
    private JButton selectButton;
    
    /** The table model. */
    private ExaminationDataElementTableModel tableModel;
    
    /** The filechooser. */
    private JFileChooser fileChooser;
    
    /** The main panel. */
    private JPanel mainPanel;
    
    /** The transfer handler. */
    private TransferHandler transferHandler;
    
    private boolean spssTypeExport = false;
    
    /** 
     * Creates a new instance of TableView.
     * 
     * @param dataSet The dataset.
     */
    public TableView(ExaminationDataSet dataSet) {
        super(dataSet, 
            true, // aggregation controls
            true,  // status bar
            true, // status text
            false); // trashcan
        
        // create the filechooser
        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        
        // get initial chosen terms and elements
        String[] terms = DataManager.getInstance().getTermContainer().getChosenTerms();        
        ExaminationDataElement[] elements = dataSet.getElements();
        
        // create the table
        tableModel = new ExaminationDataElementTableModel(terms, elements, null);
        TableSorter model = new TableSorter(tableModel, 
                                            medview.datahandling.termvalues.TermValueComparator.getInstance());
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        model.addMouseListenerToHeaderInTable(table);
        
        transferHandler = new ViewTransferHandler(this);
        table.setTransferHandler(null);
        table.setColumnSelectionAllowed(false);       
        
        // create the excel export button
        excelExportButton = new JButton("Export as excel file (.xls)");
        excelExportButton.addActionListener(this);
        
        // create the text export button
        txtExportButton = new JButton("Export as text file (.txt)");
        txtExportButton.addActionListener(this);
        
        // create the select examinations button
        selectButton = new JButton("Select examinations in other views");
        selectButton.addActionListener(this);
        
        // create the panel for the select examinations button
        JPanel selectButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectButtonPanel.add(selectButton);
        
        // create the export button panel
        JPanel exportButtonPanel = new JPanel(new GridLayout(1,2));        
        exportButtonPanel.add(excelExportButton);
        exportButtonPanel.add(txtExportButton);
        
        // export radio buttons
        JPanel exportRadioButtonPanel = new JPanel(new FlowLayout());
        JRadioButton regularExportTypeRadioButton = new JRadioButton("As-is (WYSIWYG)");
        regularExportTypeRadioButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent ae) { spssTypeExport = false; }            
        });        
        JRadioButton spssExportTypeRadioButton = new JRadioButton("Recode variables (for SPSS)");
        
        spssExportTypeRadioButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent ae) { spssTypeExport = true; }
        });        
        
        exportRadioButtonPanel.add(regularExportTypeRadioButton);
        exportRadioButtonPanel.add(spssExportTypeRadioButton);        
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(regularExportTypeRadioButton);
        bg.add(spssExportTypeRadioButton);
        regularExportTypeRadioButton.setSelected(true);
        spssTypeExport = false;
        
        
        // Entire export panel (buttons and radiobuttons)
        JPanel exportPanel = new JPanel();
        exportPanel.setLayout(new BorderLayout());
        exportPanel.setBorder(new TitledBorder("Export"));
        exportPanel.add(exportButtonPanel,BorderLayout.CENTER);
        exportPanel.add(exportRadioButtonPanel, BorderLayout.SOUTH);
        
        
        // Choose local terms-button
        JButton chooseLocalTermsButton = new JButton("Choose terms for this table");
        chooseLocalTermsButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                TermChooserDialog dialog = new TermChooserDialog(ApplicationFrame.getInstance(), 
                                                                 TableView.this, // termContainer
                                                                 "Term chooser for \"" + getTitle() + "\"", // title
                                                                 true); // modal
                dialog.show();
                
            }
        });
        
        // create the term button panel
        JPanel termButtonPanel = new JPanel();
        termButtonPanel.setBorder(new TitledBorder("Terms"));
        termButtonPanel.add(chooseLocalTermsButton);
        
        // create the northern panel
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel,BoxLayout.X_AXIS)); //(new FlowLayout(FlowLayout.LEFT));
        northPanel.add(exportPanel);
        northPanel.add(termButtonPanel);
        
        // create the main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setTransferHandler(transferHandler);
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(selectButtonPanel, BorderLayout.SOUTH);
        setViewComponent(mainPanel);
    }
    
    /**
     * Returns the name of this type of view.
     *
     * @return The view type name.
     */
    protected String getViewName() {
        return "Table";
    }
    
    /**
     * Marks the view as invalid. 
     * Called when an element has been selected or deselected.
     */
    protected void invalidateView() {     }
    
    /**
     * Updates everything that needs to know which terms exist. (When global terms are changed)
     */
    public void updateTermChoosers(boolean chosenTermsChanged, boolean allTermsChanged) 
    {
        if (chosenTermsChanged)
        {
            int result = JOptionPane.showConfirmDialog(this,
                                            "Global chosen terms changed. Update chosen terms of " + getTitle() +"?",
                                            "Terms changed",
                                            JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION)
            {
                String[] terms = DataManager.getInstance().getTermContainer().getChosenTerms();
                setChosenTerms(terms);
            }
        }
        
        if (allTermsChanged)
        {
            // do nothing.
            
            // ((TermContainer) this).setAllTerms(DataManager.getInstance().getTermContainer().getAllTerms());
        }        
    }
    
    public void setChosenTerms(String[] terms)
    {
        tableModel.setTerms(terms);
    }
    
    public String[] getChosenTerms()
    {
        return tableModel.getTerms();
    }
        
    /**
     * Regenerates the table.
     */
    private void regenerateTable() {
        ExaminationDataElement[] elements = getExaminationDataSet().getElements();
        tableModel.setElements(elements);
    }
        
    /**
     * Called when the table needs to be regenerated.
     */
    public void validateView()
    {
        if (super.dataSetHasChanged)
        {
            regenerateTable();
        }        
        super.validateView();
    }
        
    /**
     * Called when a toolbar button has been clicked.
     *
     * @param event The event.
     */
    public void actionPerformed(ActionEvent event)
    {                
        
        try
        {
            Object source = event.getSource();                
            
            /* If we export to excel, we need to warn if more than 256 columns
               are going to be exported */
            if (source == excelExportButton) 
            {
                String[][] spssArray = null;
                int columns;

                if (spssTypeExport)
                {
                    spssArray = convertTableModelToSPSSArray(tableModel);
                    String[] firstRow = spssArray[0];
                    columns = firstRow.length;
                } 
                else
                {
                    columns = table.getColumnCount();
                }

                if (columns > 256)
                {
                    String errorMessage;
                    if (spssTypeExport)
                    {
                        errorMessage = "You have too many SPSS variables (" + columns + "), Excel only allows a maximum of 256.\n" +
                                       "Use aggregation to reduce the value domain so that the SPSS encoding yields 256 variables or less.";
                    }
                    else
                    {
                        errorMessage = "Too many terms for Excel export (" + columns + " terms, Excel allows a maximum of 256 columns).";
                    }
                    
                    String[] options = { "Export anyway", "Cancel" };
                    int result = JOptionPane.showOptionDialog(this,
                                                    errorMessage,
                                                    "Too many variables",
                                                    0, // optionType,
                                                    JOptionPane.ERROR_MESSAGE, // messageType
                                                    null, // icon
                                                    options,                                                                                                        
                                                    options[1]); // initialValue, options
                                                    
                    if (result != 0)
                        return;
                    
                    // if result is 0 (export anyway), keep going
                }

                fileChooser.resetChoosableFileFilters();
                fileChooser.setFileFilter(new ExtensionFileFilter("xls", "Excel 97 Workbook (.xls)",true));
                int result = fileChooser.showSaveDialog(ApplicationFrame.getInstance());
                if (result == JFileChooser.APPROVE_OPTION) 
                {                
                    File file = fileChooser.getSelectedFile();
                    if (!file.getPath().toLowerCase().endsWith(".xls")) 
                    {
                        file = new File(file.getPath() + ".xls");
                    }
                    
                    if (file.exists())
                    {
                        int ok_to_overwrite = JOptionPane.showConfirmDialog(this,
                                                      "File " + file.getName() + " exists! Overwrite?",
                                                      "File exists",
                                                      JOptionPane.YES_NO_OPTION,
                                                      JOptionPane.ERROR_MESSAGE);
                        if (ok_to_overwrite == JOptionPane.NO_OPTION)
                            return;
                        
                        // if not "No" option, continue saving (and overwrite!);
                                                      
                                                    
                    }
                    
                    if (spssTypeExport)
                    {

                        try {
                            ProgressObject progress = TableWriter.getInstance().writeArrayToExcelFile(spssArray, file);
                            ApplicationFrame.getInstance().showProgressDialog(progress, "Exporting to Excel (SPSS type)");
                        } catch (IOException ioe)
                        {
                            JOptionPane.showMessageDialog(this,
                                    "Write to excel file failed: " + ioe.getMessage(),
                                    "Excel export failed",
                                    JOptionPane.ERROR_MESSAGE);
                        }                      
                    } 
                    else {
                        try {
                            TableWriter.getInstance().writeTableToExcelFile(table,  file);                        
                            ApplicationManager.getInstance().infoMessage("Exported table as-is to Excel file " + file.getPath() + ".");
                        } catch (IOException ioe)
                        {
                            JOptionPane.showMessageDialog(this,
                                    "Write to excel file failed: " + ioe.getMessage(),
                                    "Excel export failed",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } 
            else if (source == txtExportButton) 
            {
                fileChooser.resetChoosableFileFilters();
                fileChooser.setFileFilter(new ExtensionFileFilter("txt", "Tab-separated Textfile (.txt)", true));
                int result = fileChooser.showSaveDialog(ApplicationFrame.getInstance());
                if (result == JFileChooser.APPROVE_OPTION)
                {                
                    File file = fileChooser.getSelectedFile();
                    if (!file.getPath().toLowerCase().endsWith(".txt"))
                    {
                        file = new File(file.getPath() + ".txt");
                    }
                                                            
                    if (file.exists())
                    {
                        int ok_to_overwrite = JOptionPane.showConfirmDialog(this,
                                                      "File " + file.getName() + " exists! Overwrite?",
                                                      "File exists",
                                                      JOptionPane.YES_NO_OPTION,
                                                      JOptionPane.ERROR_MESSAGE);
                        if (ok_to_overwrite == JOptionPane.NO_OPTION)
                            return;
                        
                        // if not "No" option, continue saving (and overwrite!);                                                                                                          
                    }                                        
                    
                    if (spssTypeExport)
                    {                                    
                        try {
                            ProgressObject progress = TableWriter.getInstance().writeArrayToTextFile(convertTableModelToSPSSArray(tableModel), file);                        
                            ApplicationFrame.getInstance().showProgressDialog(progress, "Exporting to Text File (SPSS type)");
                        } catch (IOException ioe)
                        {
                            JOptionPane.showMessageDialog(this,
                                    "Write to text file failed: " + ioe.getMessage(),
                                    "Text file export failed",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else {
                        try {
                            TableWriter.getInstance().writeTableToTextFile(table, file);                        
                            ApplicationManager.getInstance().infoMessage("Exported table as-is to text file " + file.getPath() + ".");
                        } catch (IOException ioe)
                        {
                            JOptionPane.showMessageDialog(this,
                                    "Write to text file failed: " + ioe.getMessage(),
                                    "Text file export failed",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } 
            else if (source == selectButton)
            {
                DataManager.getInstance().deselectAllElements();
                int[] selectedRows = table.getSelectedRows();
                if (selectedRows == null) {
                    return;
                }
                for (int i = 0; i < selectedRows.length; i++) {
                    ExaminationDataElement[] elements = tableModel.getElementsAt(i);
                    if (elements.length > 0)
                    {
                        try 
                        {
                            DataManager.getInstance().selectExaminations(elements[0].getExaminationIdentifier());
                        } catch (IOException exc) 
                        {
                            // do nothing
                        }
                    }
                }
                DataManager.getInstance().validateViews();
            }
        } catch (OutOfMemoryException oome)
        {
            JOptionPane.showMessageDialog(this, "Not enough memory to complete operation.","OutOfMemoryException", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sets the aggregation to use.
     *
     * @param agg The aggregation to use.
     */
    protected void updateAggregation(Aggregation agg) {
        tableModel.setAggregation(agg);
    }                                  
    
    /**
     * Converts a table model to a two-dimensional array converting terms with multiple values to multiple terms.
     * Note that the first row in the returned array is the headers.
     *
     * @param tableModel The table model.
     * @return A two-dimensional array of the table model including the headers.
     */
    private String[][] convertTableModelToSPSSArray(ExaminationDataElementTableModel tableModel) 
        throws OutOfMemoryException
    {
        try 
        {
            int columnCount = tableModel.getColumnCount();
            int rowCount = tableModel.getRowCount();
            Aggregation agg = tableModel.getAggregation();

            LinkedHashSet columnInfoSet = new LinkedHashSet();

            for (int c = 0; c < columnCount; c++) {
                String columnName = tableModel.getColumnName(c);

                LinkedHashSet valueSet = new LinkedHashSet();

                // find out if the column is a multiple value column and thus should be splitted into several columns
                boolean multipleValueColumn = false;
                for (int r = 0; r < rowCount; r++) {
                    ExaminationDataElement[] elements = tableModel.getElementsAt(r);
                    String[] values;
                    try {
                        values = elements[0].getValues(columnName, agg);
                    } catch (NoSuchTermException exc) {
                        values = new String[0];
                    }

                    for (int v = 0; v < values.length; v++) {
                        valueSet.add(values[v]);
                    }

                    if (values.length > 1) {
                        multipleValueColumn = true;
                    }
                }

                if (multipleValueColumn) {
                    for (Iterator i = valueSet.iterator(); i.hasNext(); ) {
                        String value = (String) i.next();
                        columnInfoSet.add(new ColumnInfo(columnName, value));
                    }
                } else {                
                    columnInfoSet.add(new ColumnInfo(columnName));
                }            
            }

            // create the array. Add an extra row for the headers.
            String[][] tableArray = new String[rowCount + 1][columnInfoSet.size()];

            // generate the header row of the array
            int col = 0;
            for (Iterator i = columnInfoSet.iterator(); i.hasNext(); ) 
            {
                ColumnInfo columnInfo = (ColumnInfo) i.next();

                if (columnInfo.isMultipleValueColumn()) {
                    tableArray[0][col] = columnInfo.getTerm() + "." + columnInfo.getValue();
                } else {
                    tableArray[0][col] = columnInfo.getTerm();
                }
                col++;
            }

            // generate the rest of the array
            for (int row = 0; row < rowCount; row++) {
                ExaminationDataElement[] elements = tableModel.getElementsAt(row);

                col = 0;
                for (Iterator i = columnInfoSet.iterator(); i.hasNext(); ) {
                    ColumnInfo columnInfo = (ColumnInfo) i.next();
                    String[] values;
                    try {
                        values = elements[0].getValues(columnInfo.getTerm(), agg);
                    } catch (NoSuchTermException exc) {
                        values = new String[0];
                    }

                    if (columnInfo.isMultipleValueColumn()) {
                        boolean valueExists = false;
                        for (int v = 0; v < values.length; v++) {
                            if (columnInfo.getValue().equals(values[v])) {
                                valueExists = true;
                            }
                        }
                        if (valueExists) {
                            tableArray[row + 1][col] = "Yes"; // UGLY hardcoded
                        } else {
                            tableArray[row + 1][col] = "No"; // UGLY hardcoded
                        }
                    } else {
                        if (values.length == 0) {
                            tableArray[row + 1][col] = new String("");
                        } else {
                            tableArray[row + 1][col] = values[0];
                        }
                    }
                    col++;
                }
            }

            return tableArray;
        } 
        catch (OutOfMemoryError oome)
        {
            throw new OutOfMemoryException(oome.getMessage());
        }
    }
    
    /** 
     * Internal class to keep track of column information when exporting the table.
     */
    private class ColumnInfo {
        
        /** True if this column is part of a multiple value column. */
        private boolean multipleValueColumn = false;
        
        /** The term represented by this column. */
        private String term;
        
        /** The value of the term represented by this column. Only used if multipleValueColumn is true. */
        private String value;
        
        /**
         * Constructs a multiple value column.
         *
         * @param term The term.
         * @param value The value.
         */
        public ColumnInfo(String term, String value) {
            multipleValueColumn = true;
            this.term = term;
            this.value = value;
        }
        
        /**
         * Constructs a single value column.
         *
         * @param term The term.
         */
        public ColumnInfo(String term) {
            this.term = term;
        }
        
        /**
         * Returns true if this column is part of a multiple value column.
         *
         * @return Tru if part of a multiple value column.
         */
        public boolean isMultipleValueColumn() {
            return multipleValueColumn;
        }
        
        /**
         * Returns the term represented by this column.
         *
         * @return The term represented by this column.
         */
        public String getTerm() {
            return term;
        }
        
        /**
         * Returns the value represent this column. Only used if the column is part of a multiple value column.
         *
         * @return The value represented by this column.
         */
        public String getValue() {
            return value;
        }
        
        /**
         * Returns true if this ColumnInfo object is equal to the specified object.
         *
         * @param o The specified object.
         */ 
        public boolean equals(Object o) {
            if (o instanceof ColumnInfo) {
                ColumnInfo columnInfo = (ColumnInfo) o;
                if (multipleValueColumn == columnInfo.isMultipleValueColumn()) {
                    return term.equals(columnInfo.getTerm()) && value.equals(columnInfo.getValue());
                }
            }
            return false;
        }
    }    
    
    /** Override status bar for the table view */
    
    public void updateStatusBar()
    {
        int examinationCount = examinationDataSet.getElementCount();
        String labelText = (examinationCount + " examinations.");
        if (examinationCount > 0)
        {
            labelText += " (";        
            DataGroup[] dataGroups = examinationDataSet.getDataGroups();
            for (int i = 0; i < dataGroups.length; i++)
            {
                int count = examinationDataSet.getElementCountInDataGroup(dataGroups[i]);            
                labelText += dataGroups[i].getName() + ": " + count;
                if (i < (dataGroups.length - 1)) 
                   labelText += ", ";                            
            }

            labelText += ")";
        }
        setStatusBarText(labelText);        
    }
    
    /** Test method for the gui */
    
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("TableViewTest");        
        
        TableView view = new TableView(new ExaminationDataSet());
        frame.getContentPane().add(view);
        frame.pack();
        frame.show();
        
    }
}

