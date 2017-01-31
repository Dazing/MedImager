/*
 * PlaqueTablePanel.java
 *
 * Created on den 30 oktober 2002, 14:01
 */

package medview.medrecords.plaqueIndex.plaqueIndexS;

/**
 *
 * @author  nader
 */

import medview.medrecords.data.*;
import medview.medrecords.tools.*;
import medview.medrecords.models.*;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;


public class PlaqueTablePanel extends javax.swing.JPanel {
    
    
    private PlaqueIndexTableModel[] mModels = new PlaqueIndexTableModel[4];
    private PlaqueIndexTable[] mTables      = new PlaqueIndexTable[4];
    private PlaquePanel mParent;
    
    
    /** Creates a new instance of PlaqueTablePanel */
    public PlaqueTablePanel(PlaquePanel pPanel) {
        mParent = pPanel;
        initComponents();
    }
    
    private void initComponents() {
        
        this.setLayout(new GridLayout(2,2,3,3));
        for (int j = 0; j < 4; j++){
            boolean startCol = false;
            if(j % 2 == 0) startCol = true;
            PlaqueIndexTableModel aMd = new PlaqueIndexTableModel(j,startCol);
            
            for (int i = 0; i < 4; i++){
                String rn = getRowName(i);
                if (rn != null && startCol){
                    aMd.addRow(rn);
                }
                else{
                    aMd.addRow();
                }
            }
            PlaqueIndexTable aTb = new PlaqueIndexTable(aMd);
            aTb.addMouseListener(mParent);
            
            mModels[j] = aMd;
            mTables[j] = aTb;
            
            JScrollPane scrollPane = new JScrollPane(aTb);
            Dimension aDim = aTb.getPreferredSize();
            int aH = aDim.height * 2;
            int aW = aDim.width / 2;
            scrollPane.setPreferredSize(new Dimension(aW,aH));
            
            this.add(scrollPane);
        }
    }
    private String getRowName(int idx){
        if(idx == 0) return "m";
        if(idx == 1) return "b";
        if(idx == 2) return "d";
        if(idx == 3) return "l";
        return null;
    }
    
    private int getRowNumber(String aChar){
        if(aChar.equalsIgnoreCase("m")) return 0;
        if(aChar.equalsIgnoreCase("b")) return 1;
        if(aChar.equalsIgnoreCase("d")) return 2;
        if(aChar.equalsIgnoreCase("l")) return 3;
        return -1;
    }
    
    public PlaqueIndexTable getFirstTable(){
        return mTables[0];
    }
    
    public PlaqueIndexTable getLastTable(){
        return mTables[3];
    }
    
    public PlaqueIndexTable getTableAt(int i){
        return mTables[i];
    }
    
    public PlaqueIndexTable getNextTable(PlaqueIndexTable pTable){
        int find = -1;
        
        for(int i = 0; i < mTables.length; i++){
            if(pTable == mTables[i]) find = i;
        }
        if(find >= 3) find = 0;
        else find++;
        return mTables[find];
    }
    
    public PlaqueIndexTable getPrvTable(PlaqueIndexTable pTable){
        int find = -1;
        
        for(int i = 0; i < mTables.length; i++){
            if(pTable == mTables[i]) find = i;
        }
        if(find <= 0) find = 3;
        else find--;
        return mTables[find];
    }
    
    String getValueFor(String pTooth,String pStatus){
        int aLine = this.getRowNumber(pStatus);
        Object found;
        for(int i = 0; i < mModels.length; i++){
            PlaqueIndexTableModel    aModel  = mModels[i];
            for(int j = 0; j < aModel.getColumnCount(); j++){
                String colName = aModel.getColumnName(j);
                if(colName.compareTo(pTooth) == 0){
                    found = aModel.getValueAt(aLine,j);
                    return found.toString();
                }
            }
        }
        return null;
        
    }
    
    // may be the PlaqueCategoryModel must be build accoring to the input values
    // it means if you don't have a value for a ter do,t generata the term.
    public void writeToCategory(PlaqueCategoryModel pPlaqueCatModel){
        
        InputModel[] inputs = pPlaqueCatModel.getInputs();
        
        for(int i = 0; i < inputs.length; i++){
            PlaqueInputModel    aModel  = (PlaqueInputModel)inputs[i];
            String              aTooth  = aModel.getTooth();
            String              aStatus = aModel.getStatus();
            String              aValue  = getValueFor(aTooth,aStatus);
            if(aValue != null && aValue.length() > 0 && !aValue.equalsIgnoreCase("-") )
                aModel.putValue(aValue);
        }
    }
    
    
    
    
}