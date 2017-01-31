/*
 * TableViewFactory.java
 *
 * Created on October 2, 2002, 4:11 PM
 *
 * $Id: TableViewFactory.java,v 1.2 2002/10/30 15:56:35 zachrisg Exp $
 *
 * $Log: TableViewFactory.java,v $
 * Revision 1.2  2002/10/30 15:56:35  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import medview.visualizer.data.*;

/**
 * A ViewFactory for TableViews.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class TableViewFactory implements ViewFactory {
    
    /** 
     * Creates a new instance of TableViewFactory.
     */
    public TableViewFactory() {
    }
    
    /**
     * Creates a new TableView from an ExaminationDataSet.
     *
     * @param dataSet The data set.
     * @return The TableView created from the data set.
     */
    public View createView(ExaminationDataSet dataSet) {
        return new TableView(dataSet);
    }
    
}
