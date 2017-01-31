/*
 * $Id: InputModel.java,v 1.2 2003/11/14 21:32:48 oloft Exp $
 *
 * Created on August 1, 2001, 12:58 PM
 * 
 */

package medview.formeditor.models;


import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

import medview.formeditor.data.*;

/**
 * The basic class for InputComponent models.
 *
 * @author Nils Erichson
 * @version
 */
public abstract class InputModel extends Object { 

    public static final int TYPE_FIRST = 1;
    public static final int TYPE_TEXTAREA = 1;
    public static final int TYPE_PICTURECHOOSER = 2;
    public static final int TYPE_LAST = 2;

    private boolean editable = true; // Can you edit the contents manually?
    private String name;
    private String description;
    private PresetModel presetModel;

    protected int type;
    protected Vector currentValues;

    protected Vector changeListeners;
    private  boolean translateAble = true; //nader 4/15
    private  boolean identificationTerm = false; // nader 30/5
    private  boolean required = false; // nader 5/8
    /** Creates new InputModel */


    public InputModel(String initialName,String initialDescription,  PresetModel in_presetModel) {

        changeListeners = new Vector();
        currentValues = new Vector();

        setName(initialName);
        setDescription(initialDescription);

        if (in_presetModel == null) {
            setPresetModel(new PresetModel(initialName));
        } else {
            setPresetModel(in_presetModel);
        }
    }

    public void addChangeListener(ChangeListener cl) {
        if (!changeListeners.contains(cl))
            changeListeners.add(cl);
    }
    /**
     * Clear out the currentValues vector
     */
    public void clear() {
        clear(true);
    }
    
    public void clear(boolean fireEvent) {
        // System.out.println("FieldModel.clear() called");
        currentValues.clear();
        if (fireEvent)
            fireStateChanged();
    }
    
    public void fireStateChanged() {
        System.out.println("InputModel.FireStateChanged run");
        for (Iterator it = changeListeners.iterator(); it.hasNext();) {
            ChangeListener c = (ChangeListener) it.next();
            c.stateChanged(new ChangeEvent(this));
        }
    }

    public String getDescription() {
        return new String(description);
    }

    public String getName() {
        return new String(name);
    }

    public PresetModel getPresetModel() {
        return presetModel;
    }

    public int getType() {
        return type;
    }

    public int getValueCount() {
        return currentValues.size();
    }

    public String[] getValues() {

        String[] values = new String[currentValues.size()];
        values = (String[]) currentValues.toArray(values);
        return values;

    }

    public String getValueText() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < currentValues.size(); i++) {
            sb.append((String) currentValues.get(i));
            if (i < (currentValues.size() -1)) // Don't append enter to the last, because that will create a new line!
                sb.append("\n");
        }
        String result = sb.toString();
        return result;
    }
    /**
     * Remove values that are all whitespace
     */
    public void removeBlankValues() {
        for (Iterator it = currentValues.iterator(); it.hasNext();) {
            Object o = it.next();
            if (o instanceof String) {
                String s = (String) o;
                if (s.trim().equals("")) { // If value can be reduced to empty
                    //if (MRConst.debug) System.out.println("stripBlankValues: Removed [" + s + "]");
                    it.remove();
                }
            }
        }
    }

    public boolean isEditable() {
        return editable;
    }

    public void removeChangeListener(ChangeListener cl) {
        changeListeners.remove(cl);
    }

    public void setEditable(boolean isEditable) {
        editable = isEditable;
    }

    public void setName(String new_name) {
        name = new_name;
        fireStateChanged();
    }

    public void setDescription(String in_description) {
        description = new String(in_description);
        fireStateChanged();
    }

    public void setPresetModel(PresetModel in_presetmodel) {
        presetModel = in_presetmodel;
    }

    /**
     * The method for putting a value into the field model.
     * This will fire a stateChanged event.
     * @param in_value the value to put
     * 
     */
    public void putValue(String in_value) {
        putValue(in_value,true);
    }
        
    public void putValue(String in_value, boolean fireEvent) {

        if (currentValues.contains(in_value)) {
            //System.out.println("This inputmodel already contains ["+in_value+"]");
        } else {
            //System.out.println("This inputmodel did not contain ["+in_value+"]");
            currentValues.add(in_value);
            
            if (fireEvent)
                fireStateChanged();
        }
    }      

    public boolean isTranslateAble(){ //nader 4/16
        return translateAble;
    }
    public void setTranslateAble(boolean flag){ //nader 4/16
        translateAble = flag;
    }
     public boolean isRequired(){ //nader 4/16
        return required;
    }
    public void setRequired(boolean flag){ //nader 4/16
        required = flag;
    }
    public boolean isIdentification(){ //nader 30/5
        return identificationTerm;
    }
    public void setIdentification(boolean flag){ //nader 30/5
        identificationTerm = flag;
    }
    public String toString() {
        return ( getName() + "(" + currentValues.size() + "values)");
    }

}
