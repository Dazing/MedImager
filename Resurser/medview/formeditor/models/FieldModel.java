/*
 * $Id: FieldModel.java,v 1.1 2003/11/10 23:27:57 oloft Exp $
 *
 */

package medview.formeditor.models;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

import medview.formeditor.data.*;
import medview.formeditor.models.*;
import medview.formeditor.components.*;

/**
 *
 * @author  nils
 * @version
 */
public class FieldModel extends InputModel { 
    
    // Types of fields
    public static final int TYPE_FIRST = 1;
    public static final int TYPE_SINGLE = 1;
    public static final int TYPE_MULTI = 2;
    public static final int TYPE_NOTE = 3;
    public static final int TYPE_INTERVAL = 4;
    public static final int TYPE_IDENTIFICATION = 5;
    public static final int TYPE_QUESTION = 6;
    public static final int TYPE_LAST = 6;     
    
    public static final int DEFAULT_LENGTH = 40;
    
    protected static final boolean debug = false;
    
    /* Fields */
    
    protected int length,fieldType;
    //    private FieldModel nextField;
    // private boolean freeTextAllowed = true;
    
    /** Creates new FieldModel */
    public FieldModel(String initialName, int in_fieldType,PresetModel in_presetModel, String initialDescription, String initialText,int initialLength) {
        
        super(initialName,initialDescription,in_presetModel);       
        length = initialLength;
        //       nextField = null;       
        if ((in_fieldType < TYPE_FIRST) || (in_fieldType > TYPE_LAST)) {
            System.err.println("FieldModel(): Error: in_fieldType out of bounds (" + in_fieldType +"), setting to single");
            fieldType = TYPE_SINGLE;
        } else {
            fieldType = in_fieldType;
        }        
        putValue(initialText);
        
    }
    
    /* Another constructor without length */
    public FieldModel(String initialName, int in_fieldType,PresetModel in_presetModel, String initialDescription, String initialText) {
        this(initialName, in_fieldType, in_presetModel, initialDescription, initialText, DEFAULT_LENGTH);
    }
    
    public int getFieldType() {
        return fieldType;
    }
    
    public int getLength() {
        return length;
    }
    
    public int getLineCount() {
        
        int lines = -1;
        
        switch(fieldType) {
            case TYPE_SINGLE :
            case TYPE_IDENTIFICATION :
                lines = 1;
                break;
            case TYPE_MULTI:               
                lines = getValueCount();
                break;
                
            case TYPE_NOTE:
                lines = 5;
                System.err.println("Error: TYPE_NOT getlinecount not implemented");
                break;
                
            default:
                System.err.println("Error: FieldModel.getLineCount(): Type not recognized (" + fieldType + ")");
                break;               
        }
        return lines;
    }    
    
    /**
     * How many rows should this field be to keep its current contents?
     */
    public int getRowCount() {
        int rows = 1;
        switch(fieldType) {
            case TYPE_SINGLE:
            case TYPE_INTERVAL:
            case TYPE_QUESTION:    
            case TYPE_IDENTIFICATION:
                rows = 1;
                break;
            case TYPE_MULTI:
                rows = getLineCount() ;
                if (rows < 1)
                    rows = 1;
                break;
                
            case TYPE_NOTE:
                // System.err.println("Error: TYPE_NOTE in getRows not implemented");
                rows = 5;
                break;
            default:
                System.err.println("Error: FieldModel.getRows(): Type not recognized (" + fieldType + "): defaulting to 1");
                rows = 1;
                break;
        }
        if (rows == -1) System.err.println("Error: getRows() failed, returning -1");
       //System.out.println("Calculated rows to " + rows);
        return rows;
    }
       
    public int getType() {
        return InputModel.TYPE_TEXTAREA;
    }
    
    public void setLength(int in_length) {
        length = in_length;
    }
    
    
    public void setFieldType(int in_type) {
        if ((in_type >= TYPE_FIRST) && (in_type <= TYPE_LAST)) {
            fieldType = in_type;
        } else {
            System.err.println("Note: FieldModel.setType(invalid fieldtype)");
        }
        
    }
    
    public void putIdCode(String pPcode){ 
        currentValues.clear();
        currentValues.add(pPcode);
        //System.out.println("It is in Field model " + pPcode);
       // fireStateChanged();
       for (int i = 0; i < changeListeners.size(); i++) {
            ChangeListener c = (ChangeListener) changeListeners.get(i);
            if(c instanceof NamedTextArea){
                NamedTextArea nC = (NamedTextArea)c;
               // System.out.println("It is an instance of NamedTextArea " + pPcode);
                nC.updateArea(0); //pPcode);
            }        
           // c.stateChanged(new ChangeEvent(this));
        }
    }
    
    /*
    public void setFreeTextAllowed(boolean free_allowed) {
        freeTextAllowed = free_allowed;
    }
     */
            
    public void putValue(String in_value, boolean fireEvent) {
        /*if (debug) 
           System.out.println("FieldModel " + getName() + ": putting value '" + in_value + "'");
         */
        String[] stringArray = new String[1];
        stringArray[0] = in_value;
        putValues(stringArray, fireEvent);
    }
    
    // inheriten from inputmodel
    //public void putValue(String value)
     
    public void putValues(String[] in_values) {
        putValues(in_values,true);
    }
    
    public void putValues(String[] in_values, boolean fireEvent) {
        if (debug) {
           System.out.println("FieldModel " + getName() + ": putting " + in_values.length + " values: " + Arrays.asList(in_values));           
        }
           
        if (in_values.length < 1) {
            System.out.println("putValues returning since values.length < 1");
            return;
        }
        
        switch(fieldType) {
            // These cases replace the text
            case TYPE_SINGLE:
            case TYPE_INTERVAL:
            case TYPE_QUESTION:
            case TYPE_NOTE:
            case TYPE_IDENTIFICATION:
                currentValues.clear();
                currentValues.add(in_values[0]); // Only add the first                                
                if (fireEvent) {
                    fireStateChanged();
                }// Notify NamedTextField that value has changed
                break;
            case TYPE_MULTI:
                
                for (int i = 0; i < in_values.length; i++) {
                    if (currentValues.contains(in_values[i])) {
                       //System.out.println("This inputmodel already contains ["+in_value+"]");
                    } 
                    else {
                       //System.out.println("This inputmodel did not contain ["+in_value+"]");                                        
                        currentValues.add(in_values[i]);
                    }                                        
                    removeBlankValues();                     
                }   
                
                if (fireEvent)
                    fireStateChanged(); // Notify NamedTextField that value has changed
                break;
            default:
                System.err.println("FieldModel.putValues() error: Unknown field type (" + fieldType + ") !");
                break;                       
        } // end switch
        
        if (debug) {
           System.out.println("Fieldmodel values after update: " + currentValues);
        }
    }
    
   
    public static boolean isSingleValueType(int type) {
        switch(type) {
            case TYPE_SINGLE:
            case TYPE_INTERVAL:
            case TYPE_QUESTION:
            case TYPE_NOTE:
            case TYPE_IDENTIFICATION:
                return true;
                
            default:
                return false;
        }
    }
    
    
    
    
    
}
