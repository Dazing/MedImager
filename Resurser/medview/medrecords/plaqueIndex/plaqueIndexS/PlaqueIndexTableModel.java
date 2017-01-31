
package medview.medrecords.plaqueIndex.plaqueIndexS;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

import medview.medrecords.components.*;
import medview.medrecords.data.*;

/**
 * Title:
 * Description:
 * Copyright:
 * Company:
 * @author 			Nader Nazari
 * @version
 */

/**
 * The table model used in signal aspect table (AspectTable).
 */
public class PlaqueIndexTableModel extends AbstractTableModel {
    
    private  ArrayList  mValues;        // The list of rows 
    private  String[]   mColumnNames;
    private  int        mTeethGroup;
    private  boolean    mHasStartCol;
    
    
    public PlaqueIndexTableModel(int pSerial, boolean pStartCol) {
        mTeethGroup    = pSerial;
        mValues        = new ArrayList();
        mHasStartCol   = pStartCol;
        
        if(mHasStartCol){
            mColumnNames    = new String[PqConst.NuOfPlqTabCol +1];
            mColumnNames[0] = "Tand " ;
            buildTableHeader(1);
        }
        else {
            mColumnNames    = new String[PqConst.NuOfPlqTabCol];
            buildTableHeader(0);
        }
    }
    
    private void buildTableHeader(int pStart){
        int j = 0;
        for(int i = pStart; i < mColumnNames.length; i++){
            if(mTeethGroup ==  PqConst.First_Group){
                mColumnNames[i] = "" + (18 - j) ;
            }
            
            else if(mTeethGroup ==  PqConst.Second_Group ){
                mColumnNames[i] =  "" + (21  + j);
            }
            
            else if(mTeethGroup ==  PqConst.Third_Group ){
                mColumnNames[i] = "" + (48 - j);
            }
            
            else if(mTeethGroup ==  PqConst.Fourth_Group ){
                mColumnNames[i] = "" + (31  + j) ;
            }
            j++;
        }
    }
    
    public ArrayList getValues(){
        return mValues;
    }
    
    public boolean hasStartCol(){
        return mHasStartCol;
    }
    
   public void addRow(){   
        PlaqueIndex aRow = new PlaqueIndex(mTeethGroup);
        int sZ = mValues.size();
        mValues.add(aRow);
        fireTableRowsInserted(sZ,sZ);
    }
    
    public void addRow(String pRowName){
        PlaqueIndex aRow = new PlaqueIndex(mTeethGroup,pRowName);
        int sZ = mValues.size();
        mValues.add(aRow);
        fireTableRowsInserted(sZ,sZ);
    }
    
    public void removeRow(int aRow){
        if(aRow < mValues.size() && aRow > -1){
            mValues.remove(aRow);
            fireTableRowsDeleted(aRow,aRow);
        }
    }
    
    public String getColumnName(int col) {
        //System.out.println("call col name nr =  " + col );
        return mColumnNames[col];
    }
    
    
    public Class getColumnClass(int c) {
        PlaqueIndex aRow;
        if(mHasStartCol){
            aRow = new PlaqueIndex(mTeethGroup,"");
        }
        else {
            aRow = new PlaqueIndex(mTeethGroup);
            //System.out.println("call this"); 
        }
        return (aRow.getCell(c)).getClass();
    }
    
    /**
     * If a cell can be edited.
     */
    public boolean isCellEditable(int row, int col) {
        if(mHasStartCol){
            if (col == 0) return false;
        }
        return true;
    }
    
    /**
     * Set a value at the given indexes.
     */ 
    public void setValueAt(Object value, int row, int col) {
        if (row >= mValues.size() ) return;
        PlaqueIndex aRow = (PlaqueIndex)mValues.get(row);
        if(aRow != null){
            aRow.setCell(col,value);
            fireTableCellUpdated(row, col);
        }
    }
    
    public int getColumnCount() {
        return mColumnNames.length;
    }
    
    public Object getValueAt(int row, int col) {
        PlaqueIndex aRow = (PlaqueIndex)mValues.get(row);
        if(aRow != null) return aRow.getCell(col) ;
        
        return null;
    }
    
    public int getRowCount() {
        return mValues.size();
    }
    
    
}

