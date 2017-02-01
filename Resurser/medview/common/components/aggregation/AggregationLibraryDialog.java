/*
 * $Id: AggregationLibraryDialog.java,v 1.2 2004/10/21 08:19:52 erichson Exp $
 *
 * Created on den 15 oktober 2004, 10:42
 *
 * $Log: AggregationLibraryDialog.java,v $
 * Revision 1.2  2004/10/21 08:19:52  erichson
 * Removed TODO: text for already fixed problem
 *
 * Revision 1.1  2004/10/18 10:48:56  erichson
 * First check-in.
 *
 */

package medview.common.components.aggregation;

import medview.datahandling.aggregation.*;
import java.awt.Frame;
import javax.swing.JOptionPane;
/**
 * Dialog to show an Aggregation Library
 * @author Nils Erichson
 */
public class AggregationLibraryDialog {
        
    /**
     * Show a dialog with an aggregationLibrary and OK/CANCEL options
     *
     * @param agg the aggregation to view first (or null)
     */ 
    public static void showAggregationLibraryDialog(Frame parentFrame,
                                                    AggregationContainer aggregationContainer, 
                                                    Aggregation agg) 
    {       
       
       AggregationLibraryPanel aggregationLibrary = new AggregationLibraryPanel(aggregationContainer.getAggregations(),agg);
       
       String[] options = { "OK", "Cancel" };
       
       // Bad dependency on Visualizer class, fix later
       /*int result = medview.visualizer.gui.OptionDialog.showOptionDialog(parentFrame,"Aggregation Library", aggregationLibrary, options);
       if (result == 0) { // index of OK string            
          aggregationContainer.setAggregations(aggregationLibrary.getAggregations());
       }*/
       int result = JOptionPane.showConfirmDialog(parentFrame,
                                                  aggregationLibrary, // message
                                                  "Aggregation Library", // title
                                                  JOptionPane.OK_CANCEL_OPTION, // option type
                                                  JOptionPane.PLAIN_MESSAGE); // message type, decides icon
       if (result == JOptionPane.OK_OPTION)
           aggregationContainer.setAggregations(aggregationLibrary.getAggregations());
    }
}
