/*
 * SummaryViewFactory.java
 *
 * Created on November 4, 2002, 9:29 AM
 *
 * $Id: SummaryViewFactory.java,v 1.1 2002/11/06 09:25:42 zachrisg Exp $
 *
 * $Log: SummaryViewFactory.java,v $
 * Revision 1.1  2002/11/06 09:25:42  zachrisg
 * First check in.
 *
 */

package medview.visualizer.gui;

import medview.visualizer.data.*;

/**
 * A SummaryView factory class.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class SummaryViewFactory implements ViewFactory {
    
    /** 
     * Creates a new instance of SummaryViewFactory.
     */
    public SummaryViewFactory() {
    }
    
    /** 
     * Creates a new View from an ExaminationDataSet.
     *
     * @param dataSet The ExaminationDataSet.
     * @return The View created from the ExaminationDataSet.
     */
    public View createView(ExaminationDataSet dataSet) {
        return new SummaryView(dataSet);
    }
    
}
