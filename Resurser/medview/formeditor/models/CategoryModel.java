/*
 * $Id: CategoryModel.java,v 1.3 2003/11/18 10:28:15 oloft Exp $
 *
 * Created on June 14, 2001, 10:20 PM
 */

package medview.formeditor.models;

import java.util.*;
import javax.swing.event.*;
import misc.foundation.HashVector;

/**
 *
 * Describes one of the tabs for MedView.
 *
 * @author  nils
 * @version
 */
public class CategoryModel extends java.lang.Object implements ChangeListener{
    // implements ChangeListener att 5/6 2003 to catch change value to inform medrecordFrame
    
    /* Fields */
    
    //private Hashtable inputsTable;
    private HashVector inputsTable; // Maps an input name to an input
    private String title; // Name of this tab
    
    private Vector changeListeners;
    private ExaminationModel mExaminationModel = null;
    
    /** Creates new TabModel */
    public CategoryModel(String in_title) {
        super();
        
        title = new String(in_title);
        inputsTable = new HashVector();
        
        changeListeners = new Vector();
    }
    
    public void addInput(InputModel input) {
        inputsTable.put(input.getName(),input);
        input.addChangeListener(this);
        fireStateChanged();
    }
    
    public boolean containsInput(String catName) {
        return (inputsTable.containsKey(catName));
    }
    
    public InputModel getInput(String name) {
        return (InputModel) inputsTable.get(name);
    }
    
    /**
     * Fetch an array of the field names.
     */
    public String[] getInputNames() {
        
        
        String[] stringArray = new String[0];
        stringArray = (String[]) inputsTable.getKeyArray(stringArray);
        return stringArray; // Return as an array of strings
        
    }
    
    public InputModel[] getInputs() {
        
        InputModel[] inputArray = new InputModel[inputsTable.size()];
        inputArray = (InputModel[]) inputsTable.getValueArray(inputArray);
        return inputArray; // Return as an array of strings
        
        
    }
    
    public String getTitle() {
        return new String(title);
    }
    
    public void inputDown(String name) {
        
        Object o = getInput(name);
        
        if (o != null) {
            inputsTable.moveRelative(o, +1);
        } else {
            System.err.println("inputUp: Could not move input [" + name + "] - could not find it...");
        }
        
    }
    
    public void inputUp(String name) {
        
        Object o = getInput(name);
        
        if (o != null) {
            inputsTable.moveRelative(o, +1);
        } else {
            System.err.println("inputUp: Could not move input [" + name + "] - could not find it...");
        }
        
        
    }
    
    public void moveInput(String name, int relativeAdjustment) {
        // Get the input named name
        InputModel i_model = (InputModel) inputsTable.get(name);
        
        //System.out.println("Moving input [" + i_model.getName() + "]: " + relativeAdjustment);
        
        inputsTable.moveRelative(i_model,relativeAdjustment);
        
        fireStateChanged();
        
    }
    
    
    
    public void removeInput(InputModel input) {
        inputsTable.removeByKey(input.getName());
        input.removeChangeListener(this);
        fireStateChanged();
    }
    
    private FieldModel getField(String key) {
        return (FieldModel) inputsTable.get(key);
    }
    
    /**
     * Get the value of this key
     */
    /*
     public String getValue(String key) {
        return getField(key).getValue();
    }
     */
    /*
    public void storeValue(String key, String value) {
     
        // Fetch field from the table, store the value
        getField(key).setValue(value);
     
    }
     */
    
    public void addChangeListener(ChangeListener cl) {
        if(cl instanceof ExaminationModel)
            mExaminationModel = (ExaminationModel) cl;
        else if (!changeListeners.contains(cl))
            changeListeners.add(cl);
    }
    
    public void removeChangeListener(ChangeListener cl) {
        if(cl instanceof ExaminationModel)
            mExaminationModel = null;
        else
            changeListeners.remove(cl);
    }
    
    public void fireStateChanged() {
        for (int i = 0; i < changeListeners.size(); i++) {
            ChangeListener c = (ChangeListener) changeListeners.get(i);
            c.stateChanged(new ChangeEvent(this));
        }
    }
    
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        if(mExaminationModel != null)
            mExaminationModel.stateChanged(changeEvent);
        
    }
    
    
}
