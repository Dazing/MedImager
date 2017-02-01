/*
 * DataGroup.java
 *
 * Created on July 19, 2002, 3:17 PM
 *
 * $Id: DataGroup.java,v 1.11 2002/10/17 11:11:22 zachrisg Exp $
 *
 * $Log: DataGroup.java,v $
 * Revision 1.11  2002/10/17 11:11:22  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.data;

import java.awt.*; // Color
import java.util.*;
import medview.visualizer.event.*;

/**
 * A class containing information about a data group. I.e. a set of data elements that is grouped together
 * and thus is represented by the same color in the charts.
 *
 * @author  G?ran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class DataGroup {
    
    /** The name of the group. */
    private String name;
    
    /** The color of the group. */
    private Color color;
    
    /** The data group state listeners. */
    private HashSet stateListeners;
    
    /** The data group member listeners. */
    private HashSet memberListeners;
    
    /** The number of members of this group. */
    private int memberCount = 0;
    
    /** The number of selected members of this group. */
    private int selectedCount = 0;

    /** Locking boolean for thread safety. */
    private boolean available = true;
    
    /** 
     * Creates a new instance of DataGroup.
     *
     * @param groupName The name of the group.
     * @param groupColor The color of the data elements belonging to this group.
     */
    public DataGroup(String groupName, Color groupColor) {
        name = groupName;
        color = groupColor;        
        stateListeners = new HashSet();
        memberListeners = new HashSet();
    }

    /**
     * Adds a data group state listener.
     *
     * @param listener The listener to add.
     */
    public void addDataGroupStateListener(DataGroupStateListener listener) {
        stateListeners.add(listener);
    }
    
    /**
     * Adds a data group member listener.
     *
     * @param listener The listener to add.
     */
    public void addDataGroupMemberListener(DataGroupMemberListener listener) {        
        memberListeners.add(listener);        
    }
    
    /**
     * Returns the color of the data elements belonging to this group.
     *
     * @return The group color.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the name of the group.
     *
     * @return The name of the group.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Notifies the data group state listeners that the color has changed.
     */
    private void fireDataGroupColorChanged() {
        for (Iterator i = stateListeners.iterator(); i.hasNext(); ) {
            DataGroupStateListener listener = (DataGroupStateListener) i.next();
            listener.dataGroupColorChanged(new DataGroupEvent(this));
        }
    }

    /**
     * Notifies the data group state listeners that the name has changed.
     */
    private void fireDataGroupNameChanged() {
        for (Iterator i = stateListeners.iterator(); i.hasNext(); ) {
            DataGroupStateListener listener = (DataGroupStateListener) i.next();
            listener.dataGroupNameChanged(new DataGroupEvent(this));
        }
    }
    
    /**
     * Notifies the data group state listeners that the membercount or selected member count has changed.
     */
    private void fireDataGroupMemberCountChanged() {
        for (Iterator i = memberListeners.iterator(); i.hasNext(); ) {
            DataGroupMemberListener listener = (DataGroupMemberListener) i.next();
            listener.dataGroupMemberCountChanged(new DataGroupEvent(this));
        }
    }
    
    /**
     * Removes a data group state listener.
     *
     * @param listener The listener to remove.
     */
    public void removeDataGroupStateListener(DataGroupStateListener listener) {
        stateListeners.remove(listener);
    }
    
    /**
     * Removes a data group member listener.
     *
     * @param listener The listener to remove.
     */
    public void removeDataGroupMemberListener(DataGroupMemberListener listener) {
        memberListeners.remove(listener);
    }
    
    /**
     * Removes all listeners.
     */
    public void removeAllListeners() {
        stateListeners.clear();
        memberListeners.clear();
    }   

    /**
     * Sets the color of the group.
     *
     * @param groupColor The new color of the group.
     */
    public void setColor(Color groupColor) {
        color = groupColor;
        fireDataGroupColorChanged();
    }

    /**
     * Sets the name of the group.
     *
     * @param groupName The new name of the group.
     */
    public void setName(String groupName) {
        name = groupName;
        fireDataGroupNameChanged();
    }

    /**
     * Returns the number of members of this group.
     *
     * @return The number of group members.
     */
    public synchronized int getMemberCount() {
        return memberCount;
    }

    /**
     * Increases the number of group members by one.
     */
    public synchronized void increaseMemberCount() {
        memberCount++;
        fireDataGroupMemberCountChanged();
    }
    
    /**
     * Decreases the number of group members by one.
     */
    public synchronized void decreaseMemberCount() {
        memberCount--;
        fireDataGroupMemberCountChanged();
    }
    
    /**
     * Returns the number of selected members of this group.
     *
     * @return The number of selected group members.
     */
    public synchronized int getSelectedMemberCount() {
        return selectedCount;
    }
    
    /**
     * Increases the number of selected group members by one.
     */
    public synchronized void increaseSelectedMemberCount() {
        selectedCount++;
        fireDataGroupMemberCountChanged();
    }
    
    /**
     * Decreases the number of selected group members by one.
     */
    public synchronized void decreaseSelectedMemberCount() {
        selectedCount--;
        fireDataGroupMemberCountChanged();
    }
    
    
    /**
     * Returns the name of the group.
     *
     * @return The name of the group.
     */
    public String toString() {
        return name;
    }
}
