/*
 * TemplateChangeListener.java
 *
 * Created on den 25 november 2002, 14:27
 *
 * $Id: TemplateChangeListener.java,v 1.1 2002/11/25 13:45:04 zachrisg Exp $
 *
 * $Log: TemplateChangeListener.java,v $
 * Revision 1.1  2002/11/25 13:45:04  zachrisg
 * First check in.
 *
 */

package medview.visualizer.event;

/**
 * An interface for classes listening to changes of the template.
 *
 * @author  Göran Zachrisson
 */
public interface TemplateChangeListener {
    
    /**
     * Called when the template has been changed.
     *
     * @param event The object describing the event.
     */
    public void templateChanged(TemplateEvent event);
    
}
