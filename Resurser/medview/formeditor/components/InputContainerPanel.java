/*
 * $Id: InputContainerPanel.java,v 1.1 2003/11/11 00:18:16 oloft Exp $
 *
 * Created on August 1, 2001, 12:56 PM
 *
 */

package medview.formeditor.components;

import java.awt.*;
import java.awt.event.*;
import java.awt.Font.*;
import javax.swing.*;
import javax.swing.border.*;


import medview.formeditor.data.*;
import medview.formeditor.exceptions.*;
import medview.formeditor.interfaces.*;
import medview.formeditor.models.*;
import medview.formeditor.tools.*;

/**
 * @author  nils
 * @version
 */
public class InputContainerPanel extends JPanel  {
    
    private TabPanel parentTab;
    private ValueInputComponent valueInputComponent;
    private JComponent mInputComponent;
    private JComponent mComLabel;
    private String  mBorderText;
    private DatahandlingHandler mDH = DatahandlingHandler.getInstance();

    public InputContainerPanel(ValueInputComponent component) {
        setInputComponent(component);
    }
    
    /**
     * Verifies the inputted values in this inputcontainerpanel. Throws an exception which 
     * describes what is wrong if there is something that is not valid, otherwise returns normally. // Nils
     */
    
    // I have commented this out for now, since it does not seem to be used anywhere?? // Nils
    
    /*
    public void verifyInputValues() throws ValueInputException {// nader
        if(valueInputComponent instanceof NamedTextArea){
            NamedTextArea nt = (NamedTextArea) valueInputComponent;
            nt.verifyInput();                        
        }
        return;
    }
     */
    
    public InputModel getInputModel() {
        return valueInputComponent.getInputModel();
    }
    public void setTabPanel(TabPanel aTab){
        parentTab = aTab;
    }
    
    public void setInputComponent(ValueInputComponent component) {
        
        // valueInputComponent = component;
        
        if (! (component instanceof JComponent)) {
            JOptionPane.showMessageDialog(parentTab,"FATAL ERROR: Could not cast ValueInputComponent to in_component!");
            return;
        }
        mInputComponent     = (JComponent) component;
        InputModel model    = (InputModel) component.getInputModel();
        this.removeAll();           // Clear out contents
        
        BorderLayout gbLayout = new BorderLayout();
        this.setLayout(gbLayout);
        
        // JLabel descLabel = new JLabel(model.getDescription()) ;
        
        
        JTextArea descLabel = new JTextArea(model.getDescription()) ;
        mComLabel = descLabel;
        
        //descLabel.setHorizontalAlignment(SwingConstants.LEFT); // Left-justify the text in the label
        descLabel.setAlignmentY(0);
        descLabel.setAlignmentX(0);
        descLabel.setAutoscrolls(true);
        
        
        // descLabel.setEnabled(false);
        descLabel.setEditable(false);
        descLabel.setWrapStyleWord(true);
        descLabel.setBackground(new JLabel().getBackground());
        descLabel.setFont(new JLabel().getFont());
        
        int aH = descLabel.getHeight();
        
        
        if(mInputComponent instanceof NamedTextArea){
            NamedTextArea   nta = (NamedTextArea) mInputComponent;
            String          str = descLabel.getText();
            if(!nta.isEditable())
                str = str + " (" +mDH.getLanguageString(mDH.LABEL_LOCKED_INPUT_FIELD_LS_PROPERTY) + ")";

            FieldModel aModel = nta.getFieldModel();
            if(aModel.isRequired())
                str = str + " (" +  mDH.getLanguageString(mDH.LABEL_REQUIRED_INPUT_FIELD_LS_PROPERTY) + ")";
            
            descLabel.setText(str);
            String borderText = component.getInputModel().getName();  // attrib name
            mBorderText = borderText;
            
            //this.setBorder(BorderFactory.createTitledBorder(borderText));
            
            this.setBorder(borderText);
            nta.setParentPanel(this);
            
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    mouseclicked(evt);
                }
            });
        }
        // descLabel.setPreferredSize(new Dimension(150,20));
        this.add(descLabel,BorderLayout.NORTH);  // usually a question
        this.add(mInputComponent,BorderLayout.CENTER);  // it can be a picture or textField
    }
   
    public void selectComponent(){
       /* if (mComLabel instanceof JTextArea){
            JTextArea ta = (JTextArea) mComLabel;
            ta.requestFocus();
            ta.setCaretPosition(0);
            if(ta.getText().length() > 2)
                ta.moveCaretPosition(3);
        }*/
        if(mInputComponent instanceof NamedTextArea){
            JTextArea ta = (JTextArea) mInputComponent;
            ta.requestFocus();
            ta.setCaretPosition(0);
            //if(ta.getText().length() > 2)
            //ta.moveCaretPosition(3);
        }
        
    }
    /*
     *Calls when the title of a container of a nameTextField is clicked
     */
    public void mouseclicked(java.awt.event.MouseEvent evt){
       // Ut.prt("mouseclick in input containe panle");
        parentTab.scrollToPanel(this);
        
    }
    private void setBorder(String pTitle){
        Font aFont  = new JLabel().getFont();  
        aFont       = aFont.deriveFont(Font.BOLD);
        this.setBorder(BorderFactory.createTitledBorder(this.getBorder(),mBorderText,
        TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION ,aFont));
        
    }
    
}

//implements FocusListener
  //Invoked when a component gains the keyboard focus.
  /*  public void focusGained(FocusEvent e){
        Font aFont     = new JLabel().getFont();  
        aFont = aFont.deriveFont(Font.BOLD);
        this.setBorder(BorderFactory.createTitledBorder(this.getBorder(),mBorderText,
        TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION ,aFont));
       
    }
    
    //Invoked when a component loses the keyboard focus.
    public void focusLost(FocusEvent e){
         this.setBorder(BorderFactory.createTitledBorder(this.getBorder(),mBorderText)); 
    }*/
    