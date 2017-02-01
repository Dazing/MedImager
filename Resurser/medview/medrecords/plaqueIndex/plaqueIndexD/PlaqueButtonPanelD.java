/**
 * $Id: PlaqueButtonPanelD.java,v 1.4 2005/04/26 12:57:12 erichson Exp $
 *
 * $Log: PlaqueButtonPanelD.java,v $
 * Revision 1.4  2005/04/26 12:57:12  erichson
 * Merging COPENHAGEN_DEVELOPMENT branch.
 *
 * Revision 1.3.2.2  2005/03/25 13:33:49  erichson
 * Added super call in constructor
 *
 * Revision 1.3.2.1  2005/03/04 08:35:52  erichson
 * disableButtons replaced by setEnabledButtons
 *
 */

package medview.medrecords.plaqueIndex.plaqueIndexD;

/**
 * The panel containing the buttons 0-15, at the bottom of the plaque input // NE
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

import medview.medrecords.plaqueIndex.plaqueIndexD.TabHandler.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.ThreeColTab.*;



public class PlaqueButtonPanelD extends javax.swing.JPanel implements ActionListener{
    
    
    private ArrayList       mButtons;
    private ArrayList       mPlaqueValues;
    
    private PlaquePanelD    mPlaquePanel; // Reference to parent panel // NE
    private boolean         mVertical       = false;
    private String          mRadioComH      = "Horizontal";
    private String          mRadioComV      = "Vertical";
    
    
    /** Creates new form PlaqueButtonPanel */
    public PlaqueButtonPanelD(PlaquePanelD pParent)
    {
        super();
        mPlaquePanel   = pParent;
        initComponents();
    }
    
    private void initComponents() {
        mButtons    = new ArrayList();
        Font aFont  = new Font(new JButton().getFont().getName(),Font.BOLD,12);
        
        this.setLayout(new GridLayout(4,5,3,3));
        for(int i = PqDFunConst.PlqIdxMin ; i <=  PqDFunConst.PlqIdxMax; i++){ 
            JButton aButton = new JButton("" + i);
            aButton.setFont(aFont);
            aButton.addActionListener(this);
            mButtons.add(aButton);
            this.add(aButton);
        }
        this.add(new JLabel());
        
        JRadioButton hButton = new JRadioButton(mRadioComH);
        hButton.setFont(aFont);
        hButton.addActionListener(this);
        hButton.setActionCommand(mRadioComH);
        hButton.setSelected(true);
        this.add(hButton);
        
        JRadioButton vButton = new JRadioButton(mRadioComV);
        vButton.setFont(aFont);
        vButton.addActionListener(this);
        vButton.setActionCommand(mRadioComV);
        this.add(vButton);
                
        this.add(new JLabel());
           
        ButtonGroup group = new ButtonGroup();
        group.add(vButton);
        group.add(hButton);
            
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
        if (source instanceof JRadioButton) {
            JRadioButton aButton    = (JRadioButton) source;
            String com = aButton.getActionCommand();
            if(com.compareToIgnoreCase(mRadioComH)  == 0 ) mVertical =  false;
            else mVertical = true;
            mPlaquePanel.setVerticalLoop(mVertical);
        }
    }
    private void fireButtonClick(int pValue){
        mPlaquePanel.fireButtonClick(pValue);
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
    
    /**
     * Set which buttons are enabled/disabled for this type of input.     
     */    
    public void setEnabledButtons(int enabledMax)
    {
        for(int i = 0; i < mButtons.size(); i++){
            JButton aButton = (JButton)mButtons.get(i);
            aButton.setEnabled( (i <= enabledMax)  );
        }         
    }
    
    
}