/*
 * ProgressListener.java
 *
 * Created on June 26, 2002, 3:38 PM
 * 
 * $Id: ProgressListener.java,v 1.2 2002/10/30 15:06:31 zachrisg Exp $
 *
 * $Log: ProgressListener.java,v $
 * Revision 1.2  2002/10/30 15:06:31  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

/**
 *  A listener interface for ProgressEvents.
 *
 * @author  d97nix
 */
public interface ProgressListener extends java.util.EventListener {
    
    /**
     * Called when progress has been made in some activity.
     *
     * @param pe The ProgressEvent describing the progress.
     */
    public void progressMade(ProgressEvent pe);
    
}

