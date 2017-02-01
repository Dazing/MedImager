/*
 * InputValueChangedEvent.java
 *
 * Created on den 13 augusti 2003
 *
 * $Id: InputValueChangeEvent.java,v 1.2 2003/11/11 14:28:19 oloft Exp $
 *
 * $Log: InputValueChangeEvent.java,v $
 * Revision 1.2  2003/11/11 14:28:19  oloft
 * Switching main-branch
 *
 * Revision 1.1.2.1  2003/08/16 14:44:11  erichson
 * New events for MedRecords package.
 *
 */

package medview.medrecords.events;

import medview.medrecords.components.*;
import medview.medrecords.components.inputs.*;
import medview.medrecords.interfaces.*;

/**
 *
 * @author  nix
 */
public class InputValueChangeEvent extends java.util.EventObject {
    
    private final ValueInput input;
        
    public InputValueChangeEvent(ValueInput newInput) {
        super(newInput);
        input = newInput;
    }
    
    public ValueInput getInput() {
        return input;
    }
    
}
