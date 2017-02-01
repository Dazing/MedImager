/*
 * ProgressSubject.java
 *
 * Created on den 28 november 2002, 13:14
 *
 * $Id: ProgressSubject.java,v 1.2 2002/11/28 13:32:24 zachrisg Exp $
 *
 * $Log: ProgressSubject.java,v $
 * Revision 1.2  2002/11/28 13:32:24  zachrisg
 * Added methods addProgressListener() and removeProgressListener().
 *
 * Revision 1.1  2002/11/28 12:23:28  zachrisg
 * First check in.
 *
 */

package medview.visualizer.event;

/**
 * An interface for classes that generates ProgressEvents and in which
 * the progress can be cancelled.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public interface ProgressSubject {
    
    /**
     * Cancels the operation that generates ProgressEvents.
     */
    public void cancelProgressOperation();

    /**
     * Adds a ProgressListener to the subject.
     *
     * @param listener The listener to add.
     */
    public void addProgressListener(ProgressListener listener);
    
    /**
     * Removes a ProgressListener to the subject.
     *
     * @param listener The listener to remove.
     */
    public void removeProgressListener(ProgressListener listener);
    
}
