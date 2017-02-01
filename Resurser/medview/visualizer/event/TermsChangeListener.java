/*
 * TermsChangeListener.java
 *
 * Created on August 26, 2002, 4:14 PM
 *
 * $Id: TermsChangeListener.java,v 1.2 2002/10/30 15:06:34 zachrisg Exp $
 *
 * $Log: TermsChangeListener.java,v $
 * Revision 1.2  2002/10/30 15:06:34  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

/**
 * A listener interface for TermChangeEvents.
 *
 * @author  erichson
 */
public interface TermsChangeListener {
    
    /**
     * Called when the terms have changed.
     *
     * @param tce The event object.
     */
    public void termsChanged(TermsChangeEvent tce);
    
}
