/*
 * $Id: CreatorFieldModel.java,v 1.1 2003/11/10 23:27:57 oloft Exp $
 *
 * Created on den 6 juni 2002, 14:28
 */

package medview.formeditor.models;

/**
 *
 * @author  nader
 * @version
 */
public class CreatorFieldModel extends FieldModel {
    
    /** Creates new CreatorFieldModel */
    
    public CreatorFieldModel(String initialName, int in_fieldType,PresetModel in_presetModel, String initialDescription, String initialText,int initialLength) {
        
        super(initialName,in_fieldType,in_presetModel,initialDescription,initialText, initialLength);
        
        //System.out.println("Create a neew creator field mofel" );     
    }
    
    /* Another constructor without length */
    public CreatorFieldModel(String initialName, int in_fieldType,PresetModel in_presetModel, String initialDescription, String initialText) {
        this(initialName, in_fieldType, in_presetModel, initialDescription, initialText, DEFAULT_LENGTH);
    }
    public void putValue(String in_value) {
          //System.out.println("put value in  creator field mofel" );    
        if (debug)
            System.out.println("FieldModel " + getName() + ": putting value " + in_value);        
        if (currentValues.contains(in_value)) {
            return;
        }
        else {
            //System.out.println("This inputmodel did not contain ["+in_value+"]");
            currentValues.add(in_value);
            removeBlankValues();
            fireStateChanged(); // Notify NamedTextField that value has changed
        }
    }
}











