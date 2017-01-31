/*
 * ProgressEvent.java
 *
 * Created on June 26, 2002, 3:42 PM
 *
 * $Id: ProgressEvent.java,v 1.3 2002/11/28 13:25:35 zachrisg Exp $
 *
 * $Log: ProgressEvent.java,v $
 * Revision 1.3  2002/11/28 13:25:35  zachrisg
 * Added message and note to the event to make progress monitoring more flexible.
 *
 * Revision 1.2  2002/10/30 15:06:31  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

/**
 * An event sent when the progress of some activity has changed.
 *
 * @author  d97nix
 */
public class ProgressEvent extends java.util.EventObject {    
    
    /** The minimum of the progress. */
    private int minimum;
    
    /** The current progress. */
    private int progress;
    
    /** The maximum of the progress. */
    private int maximum;
    
    /** The progress message. */
    private String message;
    
    /** A note about the progress. */
    private String note;
    
    /** 
     * Creates new ProgressEvent.
     *
     * @param message The progress message that is used when creating the ProgressMonitor.
     * @param note A note about the progress.
     * @param eventSource The source of the event.
     * @param minimum The progress minimum.
     * @param progress The current progress.
     * @param maximum The progress maximum.
     */
    public ProgressEvent(String message, String note, Object eventSource, int minimum, int progress, int maximum) {
        super(eventSource);
        this.message = message;
        this.note = note;
        this.minimum = minimum;
        this.progress = progress;
        this.maximum = maximum;
        
    }
    
    /**
     * Returns the minimum of the progress.
     *
     * @return The minimum of the progress.
     */
    public int getMinimum() {
        return minimum;
    }
    
    /**
     * Returns the current progress.
     *
     * @return The current progress.
     */
    public int getProgress() {
        return progress;
    }
    
    /**
     * Returns the maximum of the progress.
     * 
     * @return The maximum of the progress.
     */
    public int getMaximum() {
        return maximum;
    }
    
    /**
     * Returns the message of the progress.
     *
     * @return The message of the progress.
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Returns the note about the progress.
     *
     * @return The note about the progress.
     */
    public String getNote() {
        return note;
    }

}
