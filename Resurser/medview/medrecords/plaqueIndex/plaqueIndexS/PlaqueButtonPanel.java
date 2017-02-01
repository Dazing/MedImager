
package medview.medrecords.plaqueIndex.plaqueIndexS;

/**
 *
 * @author  nader
 */

import medview.medrecords.data.*;
import medview.medrecords.tools.*;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;


public class PlaqueButtonPanel extends javax.swing.JPanel implements ActionListener{
    
    
    private ArrayList mButtons;
    private ArrayList mPlaqueValues;
    
    private PlaquePanel    mParentPanel;
    
    
    /** Creates new form PlaqueButtonPanel */
    public PlaqueButtonPanel(PlaquePanel pParent) {
        mParentPanel   = pParent;
        initComponents();
    }
    
    private void initComponents() {
        mButtons    = new ArrayList();         
        Font aFont  = new Font(new JButton().getFont().getName(),Font.BOLD,10);
        
        this.setLayout(new GridLayout(4,4,3,3)); 
        for(int i = PqConst.PlqIdxMin ; i <=  PqConst.PlqIdxMax; i++){
            JButton aButton = new JButton("" + i);
            aButton.setFont(aFont);
            aButton.addActionListener(this);
            mButtons.add(aButton);
            this.add(aButton);
        }
    }
    
     public void actionPerformed(ActionEvent e) { 
        Object source = e.getSource();
        
        if (source instanceof JButton) {
            JButton aButton     = (JButton) source;
            String  aValue      = aButton.getText();
            try{
                Integer anInt   = new Integer(aValue);
                fireButtonClick(anInt.intValue());
            }
            catch (NumberFormatException ex){
                return;
            }
            //mInfoLabel.setText(aValue);
            
        }
    }
    private void fireButtonClick(int pValue){
        mParentPanel.fireButtonClick(pValue);
    }
        
    public int getPlaque(String pTooth) {
        for(int i = 0; i < mButtons.size(); i++){           
            JButton aButton = (JButton)mButtons.get(i);
            String  aTitle  = aButton.getText();
            if( aTitle.compareTo(pTooth) == 0)
                return 5;
        }
        return -1;
    }
    
    
}