/*
 * $Id: PresetModel.java,v 1.8 2007/10/17 15:17:05 it2aran Exp $
 *
 * Created on June 17, 2001, 11:59 PM
 */

package medview.medrecords.models;

import java.util.Vector;
import javax.swing.event.*;
import medview.medrecords.data.*;


/**
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version
 */

/**
 * Models all presets for a field.
 */
public class PresetModel extends Object {
    
    // Fields for this object
    private Vector presetVector;
    private Vector changeListeners;   // nader 12/4
    private String title;
    private boolean sorted;
    
    /** Creates new PresetModel */
    public PresetModel(String in_title) {
        super();
        sorted=true;
        if (in_title == null)
            title = "Untitled";
        else
            title = in_title;
        presetVector    = new Vector();
        changeListeners = new Vector();// nader 12/4
    }
    
    public PresetModel(String in_title,String[] values) {
        this(in_title);
        addPresets(values);
    }

    public PresetModel(String in_title,String[] values, boolean sorted) {
        this(in_title);
        this.sorted = sorted;
        addPresets(values);
    }

    /**
     * Add a new preset
     */
    public void addPreset(String newPreset) {

        String newPresetReal;
        String[] temp = newPreset.split("\\|\\|");
        newPresetReal=temp[temp.length-1];
        if (! presetVector.contains(newPreset)) {
            if(sorted)
            {
                if(isFirstValue(newPresetReal)){
                    presetVector.add(0,newPreset);
                }
                else{
                    insert(newPreset);
                }
            }
            else
            {
                insert(newPreset);
            }
            fireStateChanged(); // notify all JLists,who are ChangeListeners // nader 12/4
        }
    }
    
    private boolean isFirstValue(String aPreset){
        if(aPreset.compareToIgnoreCase("nej") == 0) return true;
        if(aPreset.compareToIgnoreCase("ja") == 0)  return true;
        return false;
    }
    
    private void insert(String newPreset) {

        String [] temp;
        String newPresetReal;
        temp = newPreset.split("\\|\\|");
        newPresetReal=temp[temp.length-1];
        if(sorted)
        {
            for(int i = 0; i < presetVector.size(); i++){
                String aPreset = (String)presetVector.get(i);

            //if values use custom values using || we have to sort on the second part
            String aPresetReal;
            temp = aPreset.split("\\|\\|");
            aPresetReal=temp[temp.length-1];

                if(!isFirstValue(aPresetReal)){
                    if(aPresetReal.compareToIgnoreCase(newPresetReal) > 0){
                        presetVector.add(i,newPreset);
                        return;
                    }
                }
            }
        }
        presetVector.add(newPreset);
    }
    
    public void remove(String oldPreset) {
        for(int i = 0; i < presetVector.size(); i++){
            String aPreset = (String)presetVector.get(i);
            
            if(aPreset.compareToIgnoreCase(oldPreset) == 0){
                presetVector.remove(aPreset);
                return;
            }
        }  
    }  
    
    public void addChangeListener(ChangeListener cl) {// nader 12/4
       if (! changeListeners.contains(cl))
            changeListeners.add(cl);
    }
    public void removeChangeListener(ChangeListener cl) {// nader 12/4
        changeListeners.remove(cl);
    }
    
    public void fireStateChanged() {// nader 12/4
       // System.out.println(" presetModel Fire");
        for (int i = 0; i < changeListeners.size(); i++) {
            ChangeListener c = (ChangeListener) changeListeners.get(i);
          //  System.out.println(" presetModel in loop i = " + i);
            c.stateChanged(new ChangeEvent(this));
        }
    }
    
    /**
     * Add an array of String presets
     */
    
    public void addPresets(String[] newPresets) {
        // Add all values
        for (int i=0; i < newPresets.length; i++) {
            addPreset(newPresets[i]);
        }
        
    }
    
    public boolean containsPreset(String preset) {
        return presetVector.contains(preset);
    }
    /**
     * Get the presets as an array of Strings
     */
    
    public String[] getPresets() {
        String[] stringArray = new String[presetVector.size()];
        
        stringArray = (String[]) presetVector.toArray(stringArray);
        
        return stringArray;
    }
    
    /**
     * Get the title of the term that these presets apply to
     */
    public String getTitle() {
        return new String(title);
    }
    
    public String getTermName() {
        return new String(title);
    }
    
}
