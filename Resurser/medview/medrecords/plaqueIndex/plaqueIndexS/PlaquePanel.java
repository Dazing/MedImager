/*
 * PlaquePanel.java
 *
 * Created on den 1 november 2002, 10:49
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

public class PlaquePanel extends javax.swing.JPanel implements MouseListener {//{
    
    private PlaqueIndexTable    mChoosenTable;
    private PlaqueTablePanel    mTablePanel;
    private PlaqueButtonPanel   mButtonPanel;
    private JScrollPane         mScrollPane;
    private PlaqueCategoryModel mModel;
    
    /** Creates a new instance of PlaquePanel */
    public PlaquePanel(PlaqueCategoryModel pPlaqueCatModel) {
        this();
        mModel= pPlaqueCatModel;
    }
    
    public PlaquePanel() {
        initComponents();
    }
    
    public void writeToCategory(){
        mTablePanel.writeToCategory(mModel);
    }
    
    /*
     * Called if the mouse is clicked on a table. 
     */
    public void mouseClicked(MouseEvent e) {
        Object aTab = e.getSource();
       // System.out.println("mouse cliclk on table Listener PlaquePanel");
        if (aTab instanceof PlaqueIndexTable){
            setChoosenTable((PlaqueIndexTable) aTab);
        }
    }
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    
    
    
    private void initComponents() {
        this.setLayout(new java.awt.BorderLayout());
        
        JPanel          fieldPanel  = new JPanel();
        GridBagLayout   gbLayout    = new GridBagLayout(); 
            
        fieldPanel.setAlignmentX(0); 
        fieldPanel.setAlignmentY(0);
        fieldPanel.setLayout(gbLayout);
        
        GridBagConstraints layOutCstrn  = new GridBagConstraints();
        layOutCstrn.anchor              = GridBagConstraints.WEST;
        layOutCstrn.gridheight          = 1 ;
        layOutCstrn.gridwidth           = GridBagConstraints.REMAINDER;
        layOutCstrn.fill                = GridBagConstraints.HORIZONTAL;
        layOutCstrn.weightx             = 1;
        layOutCstrn.weighty             = 0;
        layOutCstrn.gridx               = 0;
        layOutCstrn.gridy               = 0;
                      
        mTablePanel                     = new PlaqueTablePanel(this);
        gbLayout.setConstraints(mTablePanel,layOutCstrn);
        fieldPanel.add(mTablePanel);
        
        layOutCstrn.gridy       = 1;
        layOutCstrn.gridheight  = GridBagConstraints.RELATIVE;
        mButtonPanel            = new PlaqueButtonPanel(this);
        gbLayout.setConstraints(mButtonPanel,layOutCstrn);
        fieldPanel.add(mButtonPanel);
        
        JLabel aFillRest        = new JLabel();     
        layOutCstrn.fill        = GridBagConstraints.BOTH;
        layOutCstrn.weighty     = 1;
        layOutCstrn.gridheight  = GridBagConstraints.REMAINDER;
        layOutCstrn.gridy       = 2;
               
        gbLayout.setConstraints(aFillRest,layOutCstrn);
        aFillRest.setAlignmentY(0);
        fieldPanel.add(aFillRest);     
        
        mScrollPane = new JScrollPane(fieldPanel);
        mScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        this.add(mScrollPane,BorderLayout.CENTER);     
        
       // mTablePanel = new PlaqueTablePanel(this);   
       // this.add(mTablePanel, java.awt.BorderLayout.CENTER);
        //this.add(new PlaqueButtonPanel(this), java.awt.BorderLayout.SOUTH);
        
        
    }
    public void setChoosenTable(PlaqueIndexTable pTable){
       // mChoosenTable.getS
        mChoosenTable = pTable;
        int aCol = mChoosenTable.getSelectedColumn();
        int aRow = mChoosenTable.getSelectedRow();
        
        if(aRow < 0) aRow = 0;
        if(aCol < 0) aCol = 0;
           
       changeSelection(aRow,aCol,true);
        
    }
    
    public void  fireButtonClick(int pValue){
        if(mChoosenTable == null){
            mChoosenTable = mTablePanel.getFirstTable();
            mChoosenTable.changeSelection(0,1,false,false);
        }
        int aRow = mChoosenTable.getSelectedRow();
        int aCol = mChoosenTable.getSelectedColumn();
        
        if(aRow < 0) aRow = 0;
        if(aCol < 0) aCol = 0;
        Integer newValue = new Integer(pValue);
        
        Object aIndx = mChoosenTable.getModel().getValueAt(aRow,aCol);
        
        if(mChoosenTable.getModel().isCellEditable(aRow,aCol))
            mChoosenTable.getModel().setValueAt(newValue,aRow,aCol);
        else{
            aCol++;
            aRow--;
        }
         //   mChoosenTable.getModel().setValueAt(newValue,aRow,aCol+1);
       /* if(aIndx instanceof Integer){
            mChoosenTable.getModel().setValueAt(newValue,aRow,aCol);
        }
        if(aIndx instanceof String){   
            aCol++;
            mChoosenTable.getModel().setValueAt(newValue,aRow,aCol);
        }*/
        changeSelection(aRow,aCol,true);
        
    }
    private void changeSelection(int pRow,int pCol){
        if(pCol < mChoosenTable.getColumnCount() -1){
            pCol++;
        }
        else if(pRow < mChoosenTable.getRowCount() -1){
            pCol = 0;
            pRow++;
        }
        else{
            mChoosenTable = mTablePanel.getNextTable(mChoosenTable);
            pRow = 0;
            pCol = 0;
        }
        mChoosenTable.changeSelection(pRow,pCol,false,false);
        
    }
     private void changeSelection(int pRow,int pCol,boolean isVertical){
        if(! isVertical){
            changeSelection(pRow,pCol);    
            return;
        }
        
        if(pRow < mChoosenTable.getRowCount() -1){
            pRow++;
        }
        else if(pCol < mChoosenTable.getColumnCount() -1){
            pRow = 0;
            pCol++;
        }
        else{
            mChoosenTable = mTablePanel.getNextTable(mChoosenTable);
            pRow = 0;
            pCol = 0;
        }
        mChoosenTable.changeSelection(pRow,pCol,false,false);
        
    }
    
}
