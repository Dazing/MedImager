/*
 * TranslatorEvent.java
 *
 * Created on den 25 november 2002, 14:22
 *
 * $Id: TranslatorEvent.java,v 1.1 2002/11/25 13:45:04 zachrisg Exp $
 *
 * $Log: TranslatorEvent.java,v $
 * Revision 1.1  2002/11/25 13:45:04  zachrisg
 * First check in.
 *
 */

package medview.visualizer.event;

import java.util.*;

/**
 * An event for changes of the translator.
 *
 * @author  Göran Zachrisson
 */
public class TranslatorEvent extends EventObject {
    
    /** 
     * Creates a new instance of TranslatorEvent.
     *
     * @param source The source of the event.
     */
    public TranslatorEvent(Object source) {
        super(source);
    }
    
}
