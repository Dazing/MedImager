/*
 * InputSelectionEvent.java
 *
 * Created on den 7 augusti 2003, 17:19
 *
 * $Id: InputFocusEvent.java,v 1.3 2003/11/11 14:33:23 oloft Exp $
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
public class InputFocusEvent extends java.util.EventObject {
    
    private final ValueInputComponent input;
    
    /** Creates a new instance of InputSelectionEvent */
    public InputFocusEvent(ValueInputComponent newInput) {
        super(newInput);
        input = newInput;
    }
    
    public ValueInputComponent getNewInput() {
        return input;
    }
    
}
