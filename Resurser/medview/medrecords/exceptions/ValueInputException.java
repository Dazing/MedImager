/*
 * $Id: ValueInputException.java,v 1.4 2003/11/11 14:40:37 oloft Exp $
 *
 * Created on den 24 maj 2002, 10:56
 *
 * $Log: ValueInputException.java,v $
 * Revision 1.4  2003/11/11 14:40:37  oloft
 * Switching main-branch
 *
 * Revision 1.3.2.1  2003/08/07 21:03:45  erichson
 * Added import statement for components.inputs.*;
 *
 * Revision 1.3  2003/07/22 16:57:49  erichson
 * Removed empty constructor
 *
 */

package medview.medrecords.exceptions;

import medview.medrecords.interfaces.*;
import medview.medrecords.components.*;
import medview.medrecords.components.inputs.*;

/**
 *
 * @author  nader
 * @version 
 */
public class ValueInputException extends java.lang.Exception {

    private ValueInputComponent inputField  = null;
    private TabPanel            tabPanel    = null;       

    /**
     * Constructs an <code>ValueInputException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ValueInputException(String msg) {
        super(msg);
    }
    
    public ValueInputException(String msg,ValueInputComponent aField,TabPanel aTab) {
        super(msg);
        inputField  = aField;
        tabPanel    = aTab;
    }
    
    public ValueInputComponent getInputField() {
        return inputField;
    }
    
    public TabPanel getTabPanel() {
        return tabPanel;
    }
    
}


