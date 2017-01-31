/*
 *
 * Created on den 6 juni 2002, 14:46
 *
 * $Id: CreatorNamedTextArea.java,v 1.1 2003/11/11 00:18:15 oloft Exp $
 *
 */

package medview.formeditor.components;

import java.util.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import medview.formeditor.data.*;
import medview.formeditor.tools.*;
import medview.formeditor.models.*;

import medview.formeditor.interfaces.*;
/**
 *
 * @author  nader
 * @version
 */
public class CreatorNamedTextArea extends NamedTextArea implements ValueInputComponent,ChangeListener, DocumentListener, CaretListener, FocusListener, KeyListener{

    /** Creates new CreatorNamedTextArea */

    public CreatorNamedTextArea(FieldModel in_model, TabPanel in_parentTab){
        super(in_model,in_parentTab ); // Constructor wants number of rows > 1
    }
    /**
     * The method for putting a value into the field. Calls from presentList
     */

    public void putValue(String in_value) {
        if (debug)
            System.out.println("NamedTextField " + this + ": putting value " + in_value);

        mModel.putValue(in_value);           // Update the mModel with the value
        updateArea();                       // Update the area from the mModel
    }



    public void rebuild() {
        //System.out.println(getName() + ": rebuild() called");
        if (mModel == null) {
            JOptionPane.showMessageDialog( this,"NamedTextArea.rebuild: Error: mModel == null","Fatal mModel error",JOptionPane.ERROR_MESSAGE);
        } else {
            this.setText(mModel.getValueText());
            this.setRows( mModel.getRowCount() ); // Rows = 0 gives 1 row
            this.setColumns(mModel.getLength()); // Sets new length and invalidates
            this.updateRows();
            this.getDocument().addDocumentListener(this);
            setLineWrap(false);
        }
        setBorder(BorderFactory.createLoweredBevelBorder());

    }

    /**
     * Update the Area with the contents of the mModel
     */
    public void updateArea(){
        // this.setMask(""); //nader 14/5
        setText("\n" + mModel.getValueText());
        setCaretPosition(0);
        //System.out.println("Creator update area");
        //setText( mModel.getValueText());
    }
    /**
     * update the preset list mask with the contents of the current row
     */

    protected void updateMask() {
        //System.out.println("Creator update mask");
        // Get what's on the currently edited Row
        // String currentRow = getCurrentRow();
        // this.setMask(currentRow);
    }

    protected void setMask(String newMask) {
        //System.out.println("Creator set mask");
        return;
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

   /*
    *Check to see if there is an unreturned value
    */
    public boolean checkInput(){
        if ( mModel.getFieldType() == FieldModel.TYPE_NOTE ) return true;

        String[] rows   = RowTools.getRows(getText());
        String   fRow   = rows[0];
        return true;
    }

    public void keyPressed(java.awt.event.KeyEvent evt) {
        //System.out.println(getName() + ": keyPressed called");
        // If a tab is typed, we should move the cursor to the next input
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            //System.out.println("Got a tab, moving focus");
            ValueInputComponent nextComponent = mParentTab.getInputAfter(this);
            if (nextComponent != null) {
                nextComponent.requestFocus();
            }
            evt.consume(); // Do not let tab propagate
        }
        else {
            if  (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                if ( mModel.getFieldType() ==FieldModel.TYPE_NOTE){
                    updateModel();
                }
                else{
                    evt.consume();
                    addNewValue();   // ask to add new value to translater
                    updateModel();  // update the mModel with the contents of text field
                    updateArea();   // update the textField with the the mModel and add a new line.
                }
            }
            else if  (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                evt.consume();
                updateModel();
                //updateArea();
            }
            else {
                //evt.consume();
            }
        }
    }

    /*
     * Add a new value to a given term. 14/5 Nader
     */
    protected void addNewValue(){           // 14/5 Nader

        String[] rows   = RowTools.getRows(getText());
        String content  = rows[0];

        if (( content.trim().equals("")) || (getPresetModel().containsPreset(content))) {
        }
        else if(!mModel.isTranslateAble()){ // Do nothing
        }

        else { // ask to add new value to translator
        /*    String[] optionArray = { "Visa TermEditor", "Avst\u00E5" };
            String defaultOption = optionArray[0];

            int result = JOptionPane.showOptionDialog(this,"V\u00E4rdet \"" + content +
            "\" ej tidigare registrerat. Registrera nytt v\u00E4rde?",
            "Ok\u00E4nt v\u00E4rde",JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,null,optionArray,defaultOption);

            switch(result) {
                case JOptionPane.YES_OPTION: // Visa termeditor
                    String termName = mModel.getName();
                    String newValue = content;
                    new medview.formeditor.components.TranslatorHandler(getPresetModel(),newValue).show();
                    break;

                case JOptionPane.NO_OPTION:
                case JOptionPane.CLOSED_OPTION:
                default:
                    break;
            }*/
        }
    }
    /**
     *
     * Update the mModel with the contents of the NamedTextArea
     */

    private void updateModel() {
        StringTokenizer tokenizer = new StringTokenizer(getText(),"\n");
        switch(mModel.getFieldType()) {

            case FieldModel.TYPE_NOTE:
                // System.out.println("updateModel called! test = " + getText() );
                mModel.putValue(this.getText());
                break;

            case FieldModel.TYPE_MULTI:
            case FieldModel.TYPE_SINGLE:
            case FieldModel.TYPE_INTERVAL:
            case FieldModel.TYPE_QUESTION:
            case FieldModel.TYPE_IDENTIFICATION:
                mModel.clear();
                // Read lines from the text String and add them as values
                while (tokenizer.hasMoreTokens()) {
                    mModel.putValue(tokenizer.nextToken());
                }
                break;


            default:
                System.err.println("NamedTextArea.updateModel error: Unknown FieldType");
                break;
        }
        if (debug) System.out.println("NamedTextArea " + getName() + " got event, updated mModel to " + mModel.getValueText());
    }







}
