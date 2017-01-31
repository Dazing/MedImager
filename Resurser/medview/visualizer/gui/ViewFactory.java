/*
 * ViewFactory.java
 *
 * Created on July 25, 2002, 4:18 PM
 *
 * $Id: ViewFactory.java,v 1.2 2002/10/30 15:56:39 zachrisg Exp $
 *
 * $Log: ViewFactory.java,v $
 * Revision 1.2  2002/10/30 15:56:39  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import medview.visualizer.data.*;

/**
 * An interface for factory classes that creates Views.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public interface ViewFactory {
    
    /**
     * Creates a new View from an ExaminationDataSet.
     *
     * @param dataSet The ExaminationDataSet.
     * @return The View created from the ExaminationDataSet.
     */
    public View createView(ExaminationDataSet dataSet);
    
}
