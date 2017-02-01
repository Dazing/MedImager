/*
 * InputSelectionListener.java
 *
 * Created on den 7 augusti 2003, 17:22
 *
 * $Id: InputFocusListener.java,v 1.3 2003/11/11 14:33:23 oloft Exp $
 *
 */

package medview.medrecords.events;

/**
 *
 * @author  nix
 */
public interface InputFocusListener extends java.util.EventListener {
    
    public void inputFocusGained(InputFocusEvent ev);
    
}
