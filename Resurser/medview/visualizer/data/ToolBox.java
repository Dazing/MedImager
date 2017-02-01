/*
 * ToolBox.java
 *
 * Created on July 1, 2002, 4:29 PM
 *
 * $Id: ToolBox.java,v 1.2 2002/10/30 14:16:43 zachrisg Exp $
 *
 * $Log: ToolBox.java,v $
 * Revision 1.2  2002/10/30 14:16:43  zachrisg
 * Added javadoc and Id and Log tags.
 *
 */

package medview.visualizer.data;
import medview.visualizer.event.*;

/**
 * The interface for a toolbox that keeps track of the current tool.
 *
 * @author  d97nix
 * @version 
 */
public interface ToolBox {

    /** Point selection tool. */
    public static final int TOOL_POINTER = 1;
    
    /** Rectangle selection tool. */
    public static final int TOOL_RECTANGLE_SELECT = 2;
    
    /**
     * Sets which tool to use.
     *
     * @param newTool The tool to use, either TOOL_POINTER or TOOL_RECTANGLE_SELECT.
     */
    public void setTool(int newTool);
    
    /**
     * Returns the currently used tool.
     *
     * @return The tool currently in use, either TOOL_POINTER or TOOL_RECTANGLE_SELECT.
     */
    public int getTool();
    
    /**
     * Adds a ToolChangeListener that listens for tool changes.
     *
     * @param tcl The listener to add.
     */
    public void addToolChangeListener(ToolChangeListener tcl);
    
    /**
     * Removes a ToolChangeListener.
     *
     * @param tcl The listener to remove.
     */
    public void removeToolChangeListener(ToolChangeListener tcl);
}

