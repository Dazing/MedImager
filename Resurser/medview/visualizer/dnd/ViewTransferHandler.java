/*
 * ExaminationDataSetTransferHandler.java
 *
 * Created on July 11, 2002, 1:07 PM
 *
 * $Id: ViewTransferHandler.java,v 1.9 2002/11/28 14:51:37 zachrisg Exp $
 *
 * $Log: ViewTransferHandler.java,v $
 * Revision 1.9  2002/11/28 14:51:37  zachrisg
 * Removed debug output.
 *
 * Revision 1.8  2002/10/30 14:43:50  zachrisg
 * Added missing javadoc.
 *
 * Revision 1.7  2002/10/30 14:38:52  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.dnd;

import java.awt.datatransfer.*;
import javax.swing.*;

import medview.visualizer.data.*;
import medview.visualizer.gui.*;

/**
 * A TransferHandler for handling Drag'n'Drop of ExaminationDataSets.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class ViewTransferHandler extends TransferHandler {
    
    /** The data flavors that the transferhandler can handle. */
    private static final DataFlavor[] flavors = { ExaminationDataElementVectorFlavor.getInstance() };
    
    /** The View where the examination data is located. */
    private View view;
    
    /**
     * Creates a new instance of ViewTransferHandler.
     *
     * @param view The view where the examination data is located to use in DnD operations.
     */
    public ViewTransferHandler(View view) {
        this.view = view;
    }
    
    /**
     * Indicates whether or not a component can import any of the given data flavors.
     *
     * @param comp The component.
     * @param transferFlavors The data flavors.
     * @return True if any of the data flavors can be imported.
     */
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        for (int i = 0; i < transferFlavors.length; i++) {
            for (int j = 0; j < flavors.length; j++) {
                if (transferFlavors[i].equals(flavors[j])) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Creates a Transferable object from the component.
     *
     * @param c The component to create the object from.
     * @return A Transferable.
     */
    public Transferable createTransferable(JComponent c) {
        ExaminationDataElementVector elementVector = view.getExaminationDataSet().getSelectedElements();
        ExaminationDataTransferable t = new ExaminationDataTransferable(elementVector);
        return t;
    }
    
    /**
     * Returns the possible source actions of a component.
     *
     * @param c The component.
     * @return The possible source actions.
     */
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY;        
    }
    
    /**
     * Imports The data in the Transferable object passed to the method.
     *
     * @param comp The component that will receive the data.
     * @param t The Transferable object that is sent to the component.
     * @return True if the operation succeeded.
     */
    public boolean importData(JComponent comp, Transferable t) {
        if (t.isDataFlavorSupported(ExaminationDataElementVectorFlavor.getInstance())) {
            try {                
                ExaminationDataElementVector elementVector = (ExaminationDataElementVector) t.getTransferData(ExaminationDataElementVectorFlavor.getInstance());                
                view.getExaminationDataSet().addDataElements(elementVector.toArray());     
                // repaint concerned views
                DataManager.getInstance().validateViews();
                
                return true;
            } catch (UnsupportedFlavorException e) {
                return false;
            } catch (java.io.IOException e) {
                return false;
            }
        }
        return false;
    }
    
}
