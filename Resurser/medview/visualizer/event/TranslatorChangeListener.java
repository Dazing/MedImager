/*
 * TranslatorChangeListener.java
 *
 * Created on den 25 november 2002, 14:33
 *
 * $Id: TranslatorChangeListener.java,v 1.1 2002/11/25 13:45:04 zachrisg Exp $
 *
 * $Log: TranslatorChangeListener.java,v $
 * Revision 1.1  2002/11/25 13:45:04  zachrisg
 * First check in.
 *
 */

package medview.visualizer.event;

/**
 * An interface for classes listening to changes of the translator.
 * 
 * @author  Göran Zachrisson
 */
public interface TranslatorChangeListener {
    
    /**
     * Called when the translator has been changed.
     * 
     * @param event The object describing the event.
     */
    public void translatorChanged(TranslatorEvent event);       
    
}
