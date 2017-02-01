/*
 *
 * Created on den 30 maj 2002, 13:40
 *
 * $Id: CreatorContainerPanel.java,v 1.2 2003/11/14 21:32:47 oloft Exp $
 *
 */

package medview.formeditor.components;

import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import javax.swing.event.*;  // DocumentListener
import javax.swing.text.*;

import medview.formeditor.interfaces.*;
import medview.formeditor.models.*;
import medview.formeditor.data.*;
import medview.formeditor.tools.*;
/**
 *
 * @author  nader
 * @version
 */

public class CreatorContainerPanel extends javax.swing.JPanel {
    
    private TabPanel                mParentTab;
    private ValueInputComponent     mValueInputComponent;
    private JComponent              mJComponent;
    private Color                   mColor;
    
    private JCheckBox editableBox;
    private JCheckBox translateAbleBox;   // Nader 15/4
    private JCheckBox requiredBox;   // Nader 5/8
    
    private JComboBox typeComboBox;
    private JButton   valueButton;
    private JComboBox valueComoBox;
    private DatahandlingHandler mDH = DatahandlingHandler.getInstance();
    
    /** Creates new CreatorContainerPanel */
    public CreatorContainerPanel(ValueInputComponent component) {
        setInputComponent(component);
        mColor = this.getBackground();
    }
    public void setIdentificationState(boolean flag){// nader 30/5
        if(flag){
            editableBox.setSelected(false);
            translateAbleBox.setSelected(true);
            requiredBox.setSelected(true);
            
            editableBox.setEnabled(false);
            translateAbleBox.setEnabled(false);
            requiredBox.setEnabled(false);
        }
        else{
            if(!editableBox.isEnabled()) editableBox.setEnabled(true);
            if(!translateAbleBox.isEnabled()) translateAbleBox.setEnabled(true);
            if(!requiredBox.isEnabled()) requiredBox.setEnabled(true);
            
            editableBox.setSelected(false);
            translateAbleBox.setSelected(false);
            requiredBox.setSelected(false);
        }
        if(mValueInputComponent == null ) return;
        if (mValueInputComponent instanceof NamedTextArea) {
            NamedTextArea textArea = (NamedTextArea) mValueInputComponent;
            if(!textArea.isEnabled()) textArea.setEnabled(true);
            FieldModel f_model = textArea.getFieldModel();
            f_model.setEditable(!editableBox.isSelected());
            f_model.setTranslateAble(!translateAbleBox.isSelected());
            f_model.setRequired(requiredBox.isSelected());
        }
    }
    public void setNoteState(boolean flag){
        if(mValueInputComponent== null ) return;
        if (mValueInputComponent instanceof NamedTextArea) {
            NamedTextArea textArea = (NamedTextArea) mValueInputComponent;
            textArea.setEnabled(!flag);
        }
        
    }
    public InputModel getInputModel() {
        return mValueInputComponent.getInputModel();
        
    }
    public void setTabPanel(TabPanel aTab){
        mParentTab = aTab;
    }
    
    public void setInputComponent(ValueInputComponent component) {
        mValueInputComponent = component;
        
        if (! (component instanceof JComponent)) {
            JOptionPane.showMessageDialog(mParentTab,"FATAL ERROR: Could not cast mValueInputComponent to in_component!");
            return;
        }
        mJComponent = (JComponent) component;
        InputModel model = (InputModel) component.getInputModel();
        this.removeAll();           // Clear out contents
        
        GridBagLayout gbLayout = new GridBagLayout();
        GridBagConstraints gbConstraints = new GridBagConstraints();
        this.setLayout(gbLayout);
        
        JTextField textField = new JTextField(model.getDescription());
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                try {
                    getInputModel().setDescription( e.getDocument().getText(0,e.getDocument().getLength()));
                   // System.out.println("CreatorContPanle  changedUpdate  = " + e.getDocument().getText(0,e.getDocument().getLength()));
                }
                catch (BadLocationException ble){
                    System.out.println("Doh! bad location!");
                }
            }
            public void insertUpdate(DocumentEvent e) {
                try {
                    getInputModel().setDescription( e.getDocument().getText(0,e.getDocument().getLength()));
                    // System.out.println("CreatorContPanle  insertUpdate  = " + e.getDocument().getText(0,e.getDocument().getLength()));
                } catch (BadLocationException ble) {
                    System.out.println("Doh! bad location!");
                }
            }
            public void removeUpdate(DocumentEvent e) {
                try {
                    getInputModel().setDescription( e.getDocument().getText(0,e.getDocument().getLength()));
                    // System.out.println("CreatorContPanle  removeUpdate  = " + e.getDocument().getText(0,e.getDocument().getLength()));
                } catch (BadLocationException ble) {
                    System.out.println("Doh! bad location!");
                }
            }
        });
        textField.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent mouseEvent){
                chooseThisPanel();
            }
        });
        
       /* textField.setHorizontalAlignment(SwingConstants.LEFT); // Left-justify the text in the label
        textField.setAlignmentY(0);
        textField.setAlignmentX(0);*/
        
        JLabel descLabel = new JLabel(mDH.getLanguageString(mDH.LABEL_ATTRIBUTE_PHRASE_LS_PROPERTY));
        //"Description: ");
        descLabel.setHorizontalAlignment(SwingConstants.LEFT);
        descLabel.setAlignmentX(0);
        descLabel.setAlignmentY(0);
        
        gbConstraints.fill = GridBagConstraints.NONE;
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 0;
        gbConstraints.gridheight = 1;
        gbConstraints.gridwidth = 1;
        gbConstraints.weightx = 0;
        gbLayout.setConstraints(descLabel,gbConstraints);
        this.add(descLabel);
        
        gbConstraints.fill = GridBagConstraints.HORIZONTAL; //Fill the entire row with reSizing
        gbConstraints.gridx = 1;
        gbConstraints.gridy = 0;
        gbConstraints.gridheight = 1;
        gbConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gbConstraints.weightx = 1;
        gbLayout.setConstraints(textField,gbConstraints);
        this.add(textField);
        
        String borderText;
        borderText = component.getInputModel().getName();
        this.setBorder(BorderFactory.createTitledBorder(borderText));
        
        // Add another empty panel
        JPanel emptyPanel2 = new JPanel();
        gbConstraints.fill = GridBagConstraints.BOTH;
        gbConstraints.gridheight = 1;
        gbConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gbConstraints.gridx = 0;
        gbConstraints.gridy = 2;
        
        gbLayout.setConstraints(emptyPanel2,gbConstraints);
        this.add(emptyPanel2);
        
        
        
        JPanel designPanel = new JPanel();
        designPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        if (mValueInputComponent instanceof NamedTextArea) {
            FieldModel f_model = ( (NamedTextArea) mValueInputComponent).getFieldModel();
            // Checkbox for editable: true/false
            editableBox = new JCheckBox(mDH.getLanguageString(mDH.CHECKBOX_LOCK_VALUES_LS_PROPERTY));
            // Set the state of the editableBox, from the model
            editableBox.setSelected(!f_model.isEditable());
            editableBox.addActionListener(new java.awt.event.ActionListener() {
                // Listener to update the model with the state of the box
                public void actionPerformed(ActionEvent evt) {
                    NamedTextArea textArea = (NamedTextArea) mValueInputComponent;
                    textArea.getFieldModel().setEditable(!editableBox.isSelected());
                }
                
            });
            //designPanel.add(editableBox);
            
            translateAbleBox = new JCheckBox(mDH.getLanguageString(mDH.CHECKBOX_NO_TRANSLATION_NEEDED_LS_PROPERTY));  // nader 15/4
            translateAbleBox.setSelected(!f_model.isTranslateAble());
            // Listener to update the model with the state of the box
            translateAbleBox.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    NamedTextArea textArea = (NamedTextArea) mValueInputComponent;
                    textArea.getFieldModel().setTranslateAble(!translateAbleBox.isSelected());
                }
            });
            //designPanel.add(translateAbleBox);
            
            
            requiredBox = new JCheckBox(mDH.getLanguageString(mDH.CHECKBOX_REQUIRED_ATTRIBUTE_LS_PROPERTY));  // nader 5/8
            requiredBox.setSelected(f_model.isRequired());
            // Listener to update the model with the state of the box
            requiredBox.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    NamedTextArea textArea = (NamedTextArea) mValueInputComponent;
                    textArea.getFieldModel().setRequired(requiredBox.isSelected());
                }
            });
            //designPanel.add(requiredBox);
            
            // Combobox for type of field
            //{ "Ett v\u00e4rde","Flera v\u00e4rden","Anteckning","Identifikation","Interval"};
            //{ "Ett v\u00e4rde","Flera v\u00e4rden","Anteckning","Intervall","Identifikation","? v\u00e4rde" };
            final String[] typeComboChoices =
            {mDH.getLanguageString(mDH.COMBOBOX_SINGLE_VALUE_ATTRIBUTE_LS_PROPERTY),
             mDH.getLanguageString(mDH.COMBOBOX_MULTIPLE_VALUE_ATTRIBUTE_LS_PROPERTY),
             mDH.getLanguageString(mDH.COMBOBOX_NOTE_VALUE_ATTRIBUTE_LS_PROPERTY),
             mDH.getLanguageString(mDH.COMBOBOX_INTERVAL_VALUE_ATTRIBUTE_LS_PROPERTY),
             mDH.getLanguageString(mDH.COMBOBOX_IDENTIFIER_VALUE_ATTRIBUTE_LS_PROPERTY),
             mDH.getLanguageString(mDH.COMBOBOX_INCOMPLETE_VALUE_ATTRIBUTE_LS_PROPERTY)};
             
             typeComboBox = new JComboBox(typeComboChoices);
             // Set the current choice from the model
             typeComboBox.setSelectedItem( typeComboChoices[f_model.getFieldType() -1]);
             typeComboBox.addActionListener(new java.awt.event.ActionListener() {
                 // What to do when the Field Type combobox is used...
                 public void actionPerformed(ActionEvent evt) {
                     String selectedChoice = (String) typeComboBox.getSelectedItem();
                     NamedTextArea textArea = (NamedTextArea) mValueInputComponent;
                     
                     if (selectedChoice.equals(typeComboChoices[0])) {
                         textArea.getFieldModel().setFieldType(FieldModel.TYPE_SINGLE);
                         setIdentificationState(false);
                     } else if (selectedChoice.equals(typeComboChoices[1])) {
                         textArea.getFieldModel().setFieldType(FieldModel.TYPE_MULTI);
                         setIdentificationState(false);
                     } else if (selectedChoice.equals(typeComboChoices[2])) {  // anteckning
                         textArea.getFieldModel().setFieldType(FieldModel.TYPE_NOTE);
                         setIdentificationState(false);
                         textArea.setText("");
                         textArea.setEnabled(false);
                     } else if (selectedChoice.equals(typeComboChoices[5])) { // OT Must match type in FieldModel!
                         textArea.getFieldModel().setFieldType(FieldModel.TYPE_QUESTION);
                         setIdentificationState(false);
                         //translateAbleBox.setSelected(true);
                     } else if (selectedChoice.equals(typeComboChoices[4])) {
                         if(mParentTab != null){
                             if(mParentTab.checkIdType(textArea)){
                                 textArea.getFieldModel().setFieldType(FieldModel.TYPE_IDENTIFICATION);
                                 setIdentificationState(true);
                                 
                             }else{
                                 String msg = "duplicate Identification type";
                                 JOptionPane.showMessageDialog(null,msg, "MedView",JOptionPane.WARNING_MESSAGE);
                                 typeComboBox.setSelectedIndex(0);
                             }
                         }
                     }
                     else if (selectedChoice.equals(typeComboChoices[3])) { // OT Must match type in FieldModel!
                         textArea.getFieldModel().setFieldType(FieldModel.TYPE_INTERVAL);
                         setIdentificationState(false);
                     }
                     
                 }
             });
             int H = new JLabel().getHeight();
             JLabel vEmpty = new JLabel();
             vEmpty.setPreferredSize(descLabel.getPreferredSize());
             designPanel.add(vEmpty);
             designPanel.add(typeComboBox);
             
             JLabel vEmpty2 = new JLabel();
             vEmpty2.setPreferredSize(new Dimension(30,H));
             designPanel.add(vEmpty2);
             designPanel.add(editableBox);
             designPanel.add(requiredBox);
             designPanel.add(translateAbleBox);
             
             gbConstraints.fill = GridBagConstraints.HORIZONTAL;
             gbConstraints.gridy = 4;
             gbConstraints.gridx = 0;
             gbConstraints.gridheight = 1;
             gbConstraints.gridwidth = GridBagConstraints.REMAINDER;
             
             gbLayout.setConstraints(designPanel,gbConstraints);
             this.add(designPanel);
        }    // End instancof namedtextarea
        
        
        mJComponent.setAlignmentY(0);
        /*
        if (medview.formeditor.data.MRConst.debugColor) { // debugColor has been removed
            this.setBackground(Color.yellow);
            this.setBorder(BorderFactory.createLineBorder(Color.pink,5));
        }
         */
        
    }
    
    private void chooseThisPanel(){
      /*  if(this.getBackground() != mColor){
            this.setBackground(mColor);
            mParentTab.setSelectedInputComponent(null);
        }
        else{
            this.setBackground(Color.lightGray);*/
            mParentTab.setSelectedInputComponent(mValueInputComponent);
    }
    
    
}
