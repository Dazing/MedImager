/*
 * NamedTextField.java
 *
 * Created on June 21, 2001, 8:08 PM
 *
 * $Id: NamedTextArea.java,v 1.3 2003/11/14 21:32:48 oloft Exp $
 * 
 */


package medview.formeditor.components;

import java.util.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import medview.formeditor.data.*;
import medview.formeditor.exceptions.*;
import medview.formeditor.interfaces.*;
import medview.formeditor.models.*;
import medview.formeditor.tools.*;


/**
 *
 * Extension of JTextField that allows fetching of the field name this JTextField represents
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version
 */

public class NamedTextArea extends JTextArea implements ValueInputComponent, ChangeListener, DocumentListener, CaretListener, FocusListener, KeyListener{
    
    protected static final boolean debug = false;
    
    protected int           mRowNumber = 0;
    protected FieldModel    mModel;
    protected TabPanel      mParentTab   = null;
    protected JPanel        mParentPanel = null;
    protected boolean       mEditor_mode;
    private   boolean       mUpateFlag   = false;
    private DatahandlingHandler mDH = DatahandlingHandler.getInstance();
    
    
    /** Creates new NamedTextField */
    
    public NamedTextArea(FieldModel in_model, TabPanel in_parentTab) {
        //super(2,3);
        super(in_model.getLength(),in_model.getRowCount() ); // Constructor wants number of rows > 1
        
        //if (MRConst.debug) System.out.println("Constructed textarea named " + in_model.getName() + " with length " + in_model.getRowCount());
        
        
        setModel(in_model); // assigns mModel, calls rebuild(), adds change listener
        mParentTab = in_parentTab;                                
        
        addCaretListener(this);
        addFocusListener(this);                        
        addKeyListener(this);
        // this.setRows(1);
    }
    
    public void clearValues() {
        setText("");
    }
    
    public InputModel getInputModel() {
        return mModel;
    }
    
    public FieldModel getFieldModel() {
        return mModel;
    }
    
    public String getName() {
        return mModel.getName();
    }
    
    public TabPanel getParentTab() {
        return mParentTab;
    }
    
    public JPanel getParentPanel() {
        return mParentPanel;
    }
    
    public void setParentPanel(JPanel pPanel) {
        mParentPanel = pPanel;
    }

    public void stateChanged(ChangeEvent ce) {
        /* DO NOTHING
           The only reason this is still here is to maintain compatibility with formeditor since CreatorNamedTextArea extends this class
         */
    }
    
    public void requestFocus() {
        requestFocusInWindow();
    }
    
    /**
     * The method for putting a value into the field. Calls from presetLists. Should not be called by the key-handling part since that is not always a "new" value
     */

    public void putValue(String in_value) {        
        if (debug)
            System.out.println("NamedTextArea.putValue: [" + in_value + "]");
        
        /*
        //mModel.putValue(in_value, true);           // Update the mModel with the value                
        String currText = getText();
        
        if (Arrays.asList(getValues()).contains(in_value)) {
            if (debug)
                System.out.println("Value '" + in_value + "' already existed, so skipping it");
        } else {                            
            //setText("\n" + currText + "\n" + in_value);
            setText(currText + "\n" + in_value);
            adjustField(getText(), 0);
            updateModel(); // updates mask
            //updateArea(0);                       // Update the area from the mModel. not necessary if we fire event
        }
        */
        mModel.putValue(in_value);
        updateArea(getCurrentRowNumber()); // Since we don't listen to the model anymore
    }
    
    
    
    public void rebuild() {
        System.out.println(getName() + ": rebuild() called");
        if (mModel == null) {
            JOptionPane.showMessageDialog( this,"NamedTextArea.rebuild: Error: mModel == null","Fatal mModel error",JOptionPane.ERROR_MESSAGE);
        } else {
            switch(mModel.getFieldType()) {
                case FieldModel.TYPE_SINGLE:
                case FieldModel.TYPE_MULTI:
                case FieldModel.TYPE_INTERVAL:
                case FieldModel.TYPE_QUESTION:
                case FieldModel.TYPE_IDENTIFICATION:
                    this.updateArea(0);
                    this.setRows( mModel.getRowCount() ); // Rows = 0 gives 1 row
                    this.setColumns(mModel.getLength()); // Sets new length and invalidates
                    this.updateRows();
                    //this.getDocument().addDocumentListener(this);
                    setLineWrap(false);
                    break;
                case FieldModel.TYPE_NOTE:
                    this.setText(mModel.getValueText());
                    this.setRows( mModel.getRowCount() ); // Rows = 0 gives 1 row
                    this.setColumns(mModel.getLength()); // Sets new length and invalidates
                    this.updateRows();
                    //this.getDocument().addDocumentListener(this);
                    setLineWrap(true);
                    setWrapStyleWord(true);
                    break;
                default:
                    System.err.println("NamedTextArea.rebuild error: Unknown fieldType");
                    break;
            }
            
        }
        setBorder(BorderFactory.createLoweredBevelBorder());
        
    }
    
    public void setModel(FieldModel in_model) {
        System.out.println("setModel called");
        // remove old change listener
        
        //if (mModel != null)
        //    mModel.removeChangeListener(this);
        
        mModel = in_model;
        //mModel.addChangeListener(this); // don't listen anymore
        rebuild();
    }               
    
    /**
     * Changelistener:
     */
    /*public void stateChanged(ChangeEvent e) {
        System.out.println("NamedTextField received ChangeEvent that mModel was updated = " + mModel.getValueText());
        //if (FieldModel.isSingleValueType(mModel.getFieldType())) {
        //    setText(mModel.getValueText());
        //} else {
        // setText("\n" + mModel.getValueText()); // Append empty line at start
        //}
        updateArea(getCurrentRowNumber());
    }*/
    
    /**
     * Update the Area with the contents of the mModel
     */
    public void updateArea(int rowNumber){
        System.out.println("Updating area with text from model");
        String str = mModel.getValueText();
        adjustField(str, rowNumber);
    }
    
    /**
     * Update the contents of the field. Remove empty lines, then add empty line at top
     * @param str the text for the field
     */
    public void adjustField(String str, int rowNumber) {        
        // Remove empty lines
        /*
        StringTokenizer tokenizer; tokenizer = new StringTokenizer(str,"\n");
        StringBuffer trimmedStringBuffer = new StringBuffer();
        
        if (tokenizer.hasMoreTokens()) {
        
            switch(mModel.getFieldType()) {
                case FieldModel.TYPE_SINGLE:
                case FieldModel.TYPE_INTERVAL:
                case FieldModel.TYPE_QUESTION:
                case FieldModel.TYPE_NOTE:
                case FieldModel.TYPE_IDENTIFICATION:
                    
                    String gotString = null;
                    tokenizer = new StringTokenizer(str,"\n");
                    while (tokenizer.hasMoreTokens()) {
                        String nextString = tokenizer.nextToken().trim(); // remove whitespace around it
                        if (!nextString.equals("")) { // If this line has content
                            gotString = nextString;                           
                        }
                    }
                    
                    if (gotString != null)
                        str = gotString; // use it
                    
//                   //if (str.startsWith("\n")) {
//                        tokenizer.nextToken(); // Skip empty line
//                        System.out.println("single-stringtokenizer skipped empty first line");
//                    }
//                    if (tokenizer.hasMoreTokens()) {                        
//                        str = tokenizer.nextToken(); // Only get first token if we have a single type
//                        System.out.println("single-stringtokenizer choosing token " + str);
//                    }
                    break;
                default: // multiple types
                    tokenizer = new StringTokenizer(str,"\n");
                    // else get all tokens for multiple-types
                    while (tokenizer.hasMoreTokens()) {
                        String nextToken = tokenizer.nextToken();
                        if (!nextToken.trim().equals("")) {
                            trimmedStringBuffer.append(nextToken);
                            if (tokenizer.hasMoreTokens()) {
                                trimmedStringBuffer.append("\n");
                            }
                        }
                    }
            } // end switch
        } // end if
            
        str = trimmedStringBuffer.toString();                
        System.out.println("str ended as " + str.replaceAll("\n",","));
        */
        
        // If we are on any row other than the first, no problem
        if( rowNumber > 0 ){            
            setText("\n" + str);
            return;
        } else {
        
            // Otherwise, we are on the first row, so we need to do some special stuff
            if(str != null) {
                if (str.length() > 0) {
                    if(mModel.getFieldType() != FieldModel.TYPE_QUESTION){
                        setText("\n" + str);  //nader 7/5
                        setCaretPosition(0);
                    }
                }
                else{
                    // Special case for "?" values on first line                
                    if(str.trim().startsWith("?")){
                        int qIndex = str.indexOf("?");
                        String vStr = str.substring(qIndex + 1);
                        setText(vStr);
                        this.requestFocus();
                        // this.setCaretPosition(qIndex);
                        //this.moveCaretPosition(qIndex +1);
                    }
                    else{ // if we are on first line, but not "?" value
                        setText("\n" + str);
                        // System.out.println("It is in NamedTextArea =  \n" + str);
                    }
                }
            }
        }
    }
    
    public void setText(String newText) {                
        System.out.println("Setting text to [" + newText.replaceAll("\n",",") + "] - e we called setText in NamedTextArea.");        
        super.setText(newText);                        
    }
    
    // Set rows to the rowcount
    public void updateRows() {
        this.setRows(mModel.getRowCount() );
    }
    
    public String getCurrentRow() {        
        try {
            return RowTools.getRow(getText(),getCurrentRowNumber());
        } catch (medview.formeditor.tools.NoSuchRowException e) {
            return (    ("CURRENT_ROW_DOES_NOT_EXIST: " + e.getMessage() ) );
            // //System.out.println("Note: ToDo: Implement better getcurrentrow!");
            // return ""; // THIS IS A HACK!!!
        }
    }
    
    public int getCurrentRowNumber() {
        /*String text = getText();
        System.out.println("Debugging: Text was " + text + " (" + text.length() + " characters)");
        //return RowTools.getRowNumber(text, getCaretPosition()); */
        int retVal = mRowNumber -1;  // Note: mRowNumber is real number (1 is the first row)
        if (retVal < 0) retVal = 0;
        
        return retVal;
    }
    
    public void documentChanged() {        
        //updateModel();
        //updateArea();
        //updateMask();        
    }
    /**
     * update the preset mask with the contents of the current row
     */
    protected void updateMask() {
        
        // Get what's on the currently edited Row
        String currentRow = getCurrentRow();
        System.out.println("updating mask with current row (" + getCurrentRowNumber() + "): [" + currentRow + "]");
        this.setMask(currentRow);
    }
    /**
     * update the preset list mask with the contents of the current row
     */
    protected void setMask(String newMask) {
        // Tell PresetListPanel to update the list of possible choices
        
        TabPanel mParentTab = getParentTab();
        ValueTabbedPane parentPane = mParentTab.getParentPane();
        PresetListPanel plPanel = parentPane.getPresetListPanel();
        if (plPanel == null) {
            System.err.println("Error: Could not set mask: presetListPanel == null");
        } else {  // Filter the value List  Nader
            plPanel.setMask(newMask); // Can handle a null value
        }
    }
    
    /**
     * Documentlistener
     */
    
    public void removeUpdate(javax.swing.event.DocumentEvent p1) {
        System.out.println("RemoveUpdate run");
        documentChanged();
    }
    
    // NOT run when characters is inserted or deleted
    public void changedUpdate(javax.swing.event.DocumentEvent p1) {
        System.out.println("ChangedUpdate run");
        documentChanged();
    }
    public void insertUpdate(javax.swing.event.DocumentEvent p1) {
        System.out.println("InsertUpdate run");
        documentChanged();
    }
    
    
    public String toString() {
        return mModel.getName() + "=" + mModel.getValueText();
    }
    
    public void caretUpdate(javax.swing.event.CaretEvent e) {
        int pos = e.getDot();
        //System.out.println("caretUpdate called! Pos: " + pos);
        
        
        int newRow = RowTools.getRowNumber(getText(),pos);
        if (newRow != mRowNumber) {
            mRowNumber = newRow;
            //System.out.println("New row, updating mask");
            updateMaskForRowChange();
        } else {
            mRowNumber = newRow;
        }
        //System.out.println("caretUpdate: set current row to " + mRowNumber);
    }
    
    public void resetContents() {
        setText("");
    }
    
    public String[] getValues() {
        return RowTools.getContentRows(getText()); // Get all non-null rows and return as values
    }
    
    public PresetModel getPresetModel() {
        return mModel.getPresetModel();
    }
    
   /* public void setEditable(boolean in_editable) {
        //mEditor_mode = in_editable;
    }
    */
    public void focusGained(java.awt.event.FocusEvent p1) {
        if (debug)
            System.out.println("Textfield " + getName() + " got focus");
        // mParentTab.setSelectedInputComponent(this);
    }
    /*
     * Call the input tool to add new valur to the translator.
     */
    public void focusLost(java.awt.event.FocusEvent ev) {
        
        /* Reclaim focus if the JList steals it */
        
        java.awt.Component oppositeComponent = ev.getOppositeComponent();
        
        if (debug) {
            String oppositeComponentName = "null";
            if (oppositeComponent != null) {
                oppositeComponentName = oppositeComponent.getClass().toString();
            }
            System.out.println("Textfield " + getName() + " lost focus to " + oppositeComponentName);
        }
        
        if (oppositeComponent instanceof JList) {
            if (debug) {
                String firstval = "null";
                try {
                    Object firstElement = ((JList) oppositeComponent).getModel().getElementAt(0);
                if (firstElement != null)
                    firstval = (String) firstElement;
                } catch (ArrayIndexOutOfBoundsException aioobe) { } // do nothing, firstval will stay as "null"
                System.out.println("target jlists first element was " + firstval);
            }

            // reclaim it
            if (debug)
                System.out.println("reclaiming focus from jlist");
            requestFocus();
            if (debug)
                System.out.println("reclaiming attempt done");
        }                        
                            
    }
    
   /**
    * Verify that the input in this text area is valid (according to the model)
    * Returns true if the input is valid, otherwise throws an exception with an explanation
    * @throws ValueInputException if the input is invalid
    */
    public void verifyInput() throws ValueInputException {
        if ( mModel.getFieldType() == FieldModel.TYPE_NOTE )
            return; // Any input is valid for NOTE type fields
        
        String[] rows   = RowTools.getRows(getText());
        
        
        /* There must be at least one row */
        if (rows == null || rows.length == 0) {
            throw new ValueInputException("Input empty (null or zero rows)");
        }
            
        String   fRow   = rows[0];
        
        /* Value can not be "" */
        
        // Check that first line is empty! If it isn't, we have made some input but not confirmed it.
        // Should the program work like this? It seems confusing. // NE
        if(fRow != null){
            // frow is not null, so check it
            if ( !fRow.trim().equals("") ) 
                throw new ValueInputException("Value has been input, but not confirmed. Press return, or delete the first line");
        }
        
        /* If the field is an identification field, verify the p_code */
        if ( mModel.getFieldType() == FieldModel.TYPE_IDENTIFICATION) { // P_code?
            if ( rows.length != 2 ) {
                //throw new ValueInputException("P-code type field had rows != 2 (rows = " + rows.length + ")");
                throw new ValueInputException("Patient identifier field is not filled in");
            }
            String   aRow   = rows[1];
            //System.out.println("pcode = " + aRow + "md = " + mModel.getName());
            if(aRow == null) 
                throw new ValueInputException("Program error (aRow == null, which should never happen. Case: First check)");
            
            if(! mDH.checkPCode(aRow))  
                throw new ValueInputException("Patient identifier '" + aRow + "' has an invalid format");
        }
        
        /* If this field is "required" to be filled in, check that a value is entered */
        if ( mModel.isRequired()){
            if ( rows.length < 2 )
                throw new ValueInputException("The template requires this field to be filled in");
            String   aRow   = rows[1];
            if(aRow == null) 
                throw new ValueInputException("Program error (aRow == null, which should never happen. Case: isRequired)");
            if(aRow.length() < 1)  
                throw new ValueInputException("The template requires this field to be filled in");
        }
        return; // Detected nothing wrong with the input
    }
    
    /*
     * Add a new Preset value to a given term. 14/5 Nader
     */
    protected void addNewPreset(String newVal){           // 14/5 Nader
        //String[] rows = RowTools.getRows(getText());
        //String newVal = rows[nr];
        PresetModel PresetMd = getPresetModel();
        
        if (( newVal.trim().equals("")) || (PresetMd.containsPreset(newVal))) {
        }
        else if(!mModel.isTranslateAble()){ // Do nothing
        }
        else if( mModel.getFieldType() == FieldModel.TYPE_QUESTION){
        }
        else {  // ask to add new value to translator
            String[]    optionArray     = {mDH.getLanguageString(mDH.BUTTON_ADD_TO_LS_PROPERTY),
            mDH.getLanguageString(mDH.BUTTON_CANCEL_TEXT_LS_PROPERTY)};
            String      defaultOption   = optionArray[0];
            
            int result = JOptionPane.showOptionDialog(this,
            newVal + " " +  mDH.getLanguageString(mDH.QUESTION_SHOULD_ADD_VALUE_LS_PROPERTY),
            mDH.getLanguageString(mDH.TITLE_NEW_VALUE_ALERT_LS_PROPERTY),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            optionArray,
            defaultOption);
            
            switch(result) {
                case JOptionPane.YES_OPTION: // Visa termeditor
                    String termName = mModel.getName();
                    String newValue = newVal;
                    new TranslatorHandler(getPresetModel(),newValue).show();
                    DatahandlingHandler.getInstance().writeValue(termName,newValue);
                    PresetMd.addPreset(newValue);
                    PresetMd.fireStateChanged();
                    
                    break;
                    
                case JOptionPane.NO_OPTION:
                case JOptionPane.CLOSED_OPTION:
                default:
                    break;
            }
        }
    }
    
    
    
    /**
     * What should be done to the mask when we change to another row in the NamedTextArea
     */
    public void updateMaskForRowChange() {
        setMask("");
        // The alternative is to set the mask to the current row contents...
    }
    
    public void keyReleased(java.awt.event.KeyEvent evt) {
        keyEventFired(evt);
        //evt.consume();
        /*
        if(mUpateFlag){
            updateModel();
            mUpateFlag = false;
        }*/
    }
    
    // has to be done this way, because if we ran updatemodel immediatly, it would be run before the text field was updated -> 1-letter lag
    public void keyTyped(java.awt.event.KeyEvent evt) {
        /*
        if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE)
            System.out.println("Backspace!");
        if (evt.getKeyCode() == KeyEvent.VK_DELETE)
            System.out.println("Delete!");
       */
        
        try {
            char typedChar = evt.getKeyChar();
            if (typedChar != KeyEvent.CHAR_UNDEFINED) {
                Character unicodeChar = new Character(typedChar);
                if (Character.isLetterOrDigit(typedChar)) {
                    getDocument().insertString(getCaretPosition(), unicodeChar.toString(), javax.swing.text.SimpleAttributeSet.EMPTY); 
                    evt.consume(); // If we don't consume it there will be double letters
                    updateModel();
                    updateMask();
                }
            }
        } catch (javax.swing.text.BadLocationException ble) {
            System.err.println("error: badlocationexception");
        }
    }
    
    public void keyPressed(java.awt.event.KeyEvent evt) {
        //keyEventFired(evt);
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_ENTER: // If this is not here, an enter press is inserted into the textfield
            case KeyEvent.VK_TAB: // If this is not here, a tab press is inserted into the textfield
                evt.consume();
        }
        
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_COPY:
            case KeyEvent.VK_CUT:
            case KeyEvent.VK_PASTE:
                System.out.println("That was a copy/cut/paste key!");
        }
        
        //evt.consume();
    }
    
    private void  keyEventFired(java.awt.event.KeyEvent evt) {
        
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_BACK_SPACE:
            case KeyEvent.VK_DELETE:
            case KeyEvent.VK_COPY:
            case KeyEvent.VK_CUT:
            case KeyEvent.VK_PASTE:
                updateModel();
                updateMask();
        }
         
        
        
        
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            ValueInputComponent nextComponent = mParentTab.getInputAfter(this);
            if (nextComponent != null) {
                nextComponent.requestFocus();
            }
            evt.consume(); // Do not let tab propagate
        }
        else if (! mModel.isEditable() ) {
            //System.out.println("Not editable: Consuming");
            if  (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE){
                this.setText("");
                updateModel();
                evt.consume();
            }
            
        }
        else if  (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if ( mModel.getFieldType() ==FieldModel.TYPE_NOTE){
                updateModel();
            }
            else{
                int currentRowNumber = this.getCurrentRowNumber();
                evt.consume(); // consume enter
                
                
                addNewPreset(getCurrentRow());    // ask to add new value to translater
                /*if (getCurrentRowNumber() == 0)
                    setText("\n" + getText());*/
                
                updateModel();      // update the mModel with the contents of text field                
                //putValue(getCurrentRow());
                updateArea(currentRowNumber);     // update the textField with the mModel and add a new line.
                setCaretPosition(0);
                
                //adjustField(getText(),currentRowNumber); // instead of updateArea()
                
                //updateModel(); // Note that the order is reverse from the above
            }
        }
        
         // The below was disabling delete, backspace and cut/copy/paste, so I removed it // Nils
        /*
        else{
            //if  (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            // I Don' t Notice any deifference
            //updateModel();  // Cause backspace to not delete the selecteded text and delete char one by one
            String aText = this.getSelectedText();
            if(aText!= null){
                evt.consume();
                mUpateFlag = true;
            }
            evt.consume();
        }
         */
    }
    
    
    /**
     * Update the mModel with the contents of the NamedTextArea
     */
    private void updateModel() {
        String text = getText();
        System.out.println("Updating model with text = " + text.replaceAll("\n",","));
        
        StringTokenizer tokenizer = new StringTokenizer(text,"\n");
        
        // Skip the first token, unless the text starts with an empty line ("\n");
        
                                    
        switch(mModel.getFieldType()) {
            
            case FieldModel.TYPE_SINGLE:
            case FieldModel.TYPE_INTERVAL:
            case FieldModel.TYPE_QUESTION:
            case FieldModel.TYPE_IDENTIFICATION:                
                System.out.println("Singletype updatemodel");
                
                // These put the first token
                
                //mModel.putValue(""); // clear out the value first. This is because if we have no tokens, putvalue is never calles
                String value = null;
                
                while (tokenizer.hasMoreTokens()) {
                    String nextToken = tokenizer.nextToken().trim(); // remove whitespace
                    if (! nextToken.equals("") && (value == null)) { // if there is content, keep the value
                        value = nextToken;
                    }
                }
                
                if (value == null)
                    value = "";
                
                mModel.putValue(value,false); // do not send event
                        /*
                    while (tokenizer.hasMoreTokens()) {
                        value = tokenizer.nextToken();
                    }
                    if (value != null) {
                        mModel.putValue(value); // Put the value from line 2
                    }
                }*/
                break;
                
            case FieldModel.TYPE_NOTE:                
                // System.out.println("updateModel called! test = " + getText() );
                mModel.putValue(this.getText(), false);
                break;
                
            case FieldModel.TYPE_MULTI:
                mModel.clear(false);
                // Read lines from the text String and add them as values
                Vector values = new Vector();
                                                                
                while (tokenizer.hasMoreTokens()) {
                    values.add(tokenizer.nextToken());
                }
                String[] valueArray = new String[values.size()];
                valueArray = (String[]) values.toArray(valueArray);
                mModel.putValues(valueArray,false );
                break;
                
                
            default:
                System.err.println("NamedTextArea.updateModel error: Unknown FieldType");
                break;
        }
        //if (debug) System.out.println("NamedTextArea " + getName() + " got event, updated mModel to " + mModel.getValueText());
    }
    
    
    
}


 /*public void focusLost(java.awt.event.FocusEvent ev) {
           if (ev.isTemporary()) {
            //System.out.println("Textfield " + getName() + " lost focus temporarily");
        }
        else {
            String[] rows = RowTools.getRows(getText());
            for (int i = 0; i < rows.length; i++) {
                String content = rows[i];
                if (( content.trim().equals("")) || (getPresetModel().containsPreset(content))) {
                    //System.out.println("Content = [" + content + "] was ok, no trigger");// Do nothing
                }
                else if(!mModel.isTranslateAble()){
                }
                else {  // ask to add new value to translator
                    String[] optionArray = { "Visa TermEditor", "Avst\u00E5" };
                    String defaultOption = optionArray[0];
                    int result = JOptionPane.showOptionDialog(this,"V\u00E4rdet \"" + content +
                                        "\" ej tidigare registrerat. Registrera nytt v\u00E4rde?",
                                        "Ok\u00E4nt v\u00E4rde",JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE,null,optionArray,defaultOption);
                    switch(result) {
                        case JOptionPane.YES_OPTION: // Visa termeditor
                            String termName = mModel.getName();
                            String newValue = content;
                            new medview.formeditor.components.TermEditorFrame(
                                                               getPresetModel(),newValue).show();
                            break;
                        case JOptionPane.NO_OPTION:
                        case JOptionPane.CLOSED_OPTION:
                        default:
                            break;
                    }
                }
            }
        }
    }*/