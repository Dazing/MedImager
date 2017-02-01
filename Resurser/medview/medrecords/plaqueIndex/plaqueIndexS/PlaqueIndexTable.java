package medview.medrecords.plaqueIndex.plaqueIndexS;

import javax.swing.JTable;
import javax.swing.table.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.DefaultCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JComboBox;
import java.awt.event.*;
import java.awt.Font;
import java.awt.*;

import medview.medrecords.models.*;
import medview.medrecords.data.*;
import medview.medrecords.components.*;



/**
 * Title:
 * Description:
 * Copyright:
 * Company:
 * @author Nader Nazari
 * @version
 */


 public class PlaqueIndexTable extends JTable implements ActionListener {
    
    public PlaqueIndexTable(PlaqueIndexTableModel aMd) {
        super(aMd);
        
        this.setRowSelectionAllowed(false);
        this.setColumnSelectionAllowed(false);
        this.setCellSelectionEnabled(true);
        
        JTableHeader aHeader = this.getTableHeader();
        Font  aFont = aHeader.getFont().deriveFont(java.awt.Font.BOLD,12);
        aHeader.setFont(aFont);
       
        aFont = this.getFont().deriveFont(java.awt.Font.BOLD,10);
        this.setFont(aFont);
        
        //this.setSelectionBackground(java.awt.Color.green);
        //setIntegerEditor();
        
        int fstIntCol = 0;
        if(aMd.hasStartCol()){
            fstIntCol = 1;
            //TableColumn fstCol = this.getColumnModel().getColumn(0);
           // fstCol.setPreferredWidth(50);
            //fstCol.setWidth(50);
        }
            
        for (int i = fstIntCol; i < aMd.getColumnCount(); i++){
            TableColumn aCol = this.getColumnModel().getColumn(i);
            setListedColumn(aCol);
          // aCol.setPreferredWidth(15);
            //aCol.setWidth(15);
        }
        
       /* this.setBorder(
        this.setCounds(r
        this.setPreferredScrollableViewportSize(size
        this.setShowVerticalLines(showVerticalLines*/      
    }
    
    /**
     * Set a NumberField for an Integer based column.
     */
    private void setIntegerEditor() {
        //Set up the editor for the integer cells.
        final NumberField nrField = new NumberField(0, 5);
        nrField.setHorizontalAlignment(NumberField.RIGHT);
        
        DefaultCellEditor nrEditor = new DefaultCellEditor(nrField) {
            //Override DefaultCellEditor's getCellEditorValue method
            //to return an Integer, not a String:
            public Object getCellEditorValue() {
                return new Integer(nrField.getValue());
            }
        };
        
        setDefaultEditor(Integer.class, nrEditor);
    }
    
    /**
     * Set the combo box for speed on a given column
     */
    public void setListedColumn(TableColumn aColumn) {
        JComboBox comboBox = new JComboBox();
        
        comboBox.addItem("-");
        Integer aCell;
        for(int i = PqConst.PlqIdxMin ; i <= PqConst.PlqIdxMax;  i++){
            aCell          = new Integer(i);
            comboBox.addItem(aCell);
        }
        comboBox.setMaximumRowCount(2 + PqConst.PlqIdxMax - PqConst.PlqIdxMin);
        aColumn.setCellEditor(new DefaultCellEditor(comboBox));
        
        //Set up tool tips for the sport cells.
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click for combo box");
        aColumn.setCellRenderer(renderer);
        
        //Set up tool tip for the sport column header.
        TableCellRenderer headerRenderer = getTableHeader().
        getDefaultRenderer();
        
        if (headerRenderer instanceof DefaultTableCellRenderer) {
            ((DefaultTableCellRenderer)headerRenderer).setToolTipText(
            "Click the cell to see a list of choices");
        }
        comboBox.addActionListener(this);
    }
    
    public void setPlaquePanel(PlaquePanel aListener){   
           // mListener = aListener;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object anObj = actionEvent.getSource();
        
         if (anObj instanceof JComboBox){
            
            MouseListener[] mls = (MouseListener[])(this.getListeners(MouseListener.class));
            for(int i = 0; i < mls.length; i++){
                MouseListener ml = mls[i];
                if(ml instanceof PlaquePanel){
                    ((PlaquePanel)ml).setChoosenTable(this);
                    break;
                }
            }
        }
    }
    
    /**
     * Remove the selected row from the table.
     */
    
    public void removeRows(){
        int row = getSelectedRow();
        //Ut.prt("row " + row);
        if(row < getRowCount()  &&  row > -1){
            removeRowSelectionInterval(row,row);
            PlaqueIndexTableModel aMd =(PlaqueIndexTableModel) getModel();
            aMd.removeRow(row);
            //updateUI();
        }
    }
    
    
    
}