/*
 * DataGroupVector.java
 *
 * Created on August 28, 2002, 11:06 AM
 *
 * $Id: DataGroupVector.java,v 1.2 2002/10/30 14:05:31 zachrisg Exp $
 *
 * $Log: DataGroupVector.java,v $
 * Revision 1.2  2002/10/30 14:05:31  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.data;

import java.util.*;

/**
 * A wrapper for Vector to create a Vector that only can hold DataGroups.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class DataGroupVector {
    
    /** The vector containing the DataGroups. */
    private Vector dataGroupVector;
    
    /** Creates a new instance of DataGroupVector */
    public DataGroupVector() {
        dataGroupVector = new Vector();
    }
    
    /**
     * Adds a DataGroup.
     *
     * @param dataGroup The data group to add.
     */
    public void add(DataGroup dataGroup) {
        dataGroupVector.add(dataGroup);
    }
    
    /**
     * Adds an array of DataGroups.
     *
     * @param dataGroups The data groups to add.
     */
    public void add(DataGroup[] dataGroups) {
        for (int i = 0; i < dataGroups.length; i++) {
            dataGroupVector.add(dataGroups[i]);
        }
    }
        
    /**
     * Returns true if the vector contains the DataGroup.
     *
     * @param dataGroup The data group.
     * @return True if the vector contains the data group.
     */
    public boolean contains(DataGroup dataGroup) {
        return dataGroupVector.contains(dataGroup);
    }
    
    /**
     * Returns an array of the DataGroups.
     *
     * @return An array containing the DataGroups.
     */
    public DataGroup[] toArray() {
        DataGroup[] dataGroups = new DataGroup[dataGroupVector.size()];
        return (DataGroup[]) dataGroupVector.toArray(dataGroups);
    }
}
 