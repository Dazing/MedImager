/*
 * $Id: ValueInputComponent.java,v 1.1 2003/11/10 23:29:45 oloft Exp $
 *
 * Created on July 20, 2001, 10:33 PM
 */

package medview.formeditor.interfaces;

import medview.formeditor.models.*;

/**
 *
 * @author  nils
 * @version 
 */
public interface ValueInputComponent  {

    //public String[] getPresets();    
    
    public PresetModel getPresetModel();
    
    public String[] getValues();
    
    public InputModel getInputModel();
       
    public void putValue(String value);
    
    public void requestFocus();
    
    public void resetContents();
    
    public void setEditable(boolean editable);
    
    public void setEnabled(boolean b);
    
    public boolean isEnabled(); 
    
}

