/*
 * $Id: ValueInputException.java,v 1.1 2003/11/12 23:26:52 oloft Exp $
 *
 */

package medview.formeditor.exceptions;

import medview.formeditor.interfaces.*;
import medview.formeditor.components.*;
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


