/*
 * TemplateEvent.java
 *
 * Created on den 25 november 2002, 14:25
 *
 * $Id: TemplateEvent.java,v 1.1 2002/11/25 13:45:04 zachrisg Exp $
 *
 * $Log: TemplateEvent.java,v $
 * Revision 1.1  2002/11/25 13:45:04  zachrisg
 * First check in.
 *
 */

package medview.visualizer.event;

import java.util.*;

/**
 * An event for changes of the template.
 *
 * @author  Göran Zachrisson
 */
public class TemplateEvent extends EventObject {
    
    /** 
     * Creates a new instance of TemplateEvent.
     *
     * @param source The source of the event.
     */
    public TemplateEvent(Object source) {
        super(source);
    }
    
}
