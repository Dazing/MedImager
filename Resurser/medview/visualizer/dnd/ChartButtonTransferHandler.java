/*
 * ChartButtonTransferHandler.java
 *
 * Created on July 29, 2002, 10:37 AM
 *
 * $Id: ChartButtonTransferHandler.java,v 1.8 2004/11/13 11:01:41 erichson Exp $
 *
 * $Log: ChartButtonTransferHandler.java,v $
 * Revision 1.8  2004/11/13 11:01:41  erichson
 * Thread naming
 *
 * Revision 1.7  2004/10/12 09:59:37  erichson
 * Updated to use ExaminationDropTargetButton instead of ChartButton. Should probably rename this class later.
 *
 * Revision 1.6  2002/11/28 14:46:07  zachrisg
 * Removed some debug output.
 *
 * Revision 1.5  2002/10/30 14:23:10  zachrisg
 * Added Id and Log tags and javadoc.
 *
 */

package medview.visualizer.dnd;

import java.awt.datatransfer.*;
import javax.swing.*;
import medview.visualizer.data.*; // ExaminationDataSet
import medview.visualizer.gui.*; // ChartButton

/**
 * A TransferHandler subclass for the chartbuttons in the toolbar.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class ChartButtonTransferHandler extends TransferHandler {
        
    /** The data flavors that the transferhandler can handle. */
    private static final DataFlavor[] flavors = { ExaminationDataElementVectorFlavor.getInstance() };
    
    /**
     * Creates a new instance of ChartButtonTransferHandler.
     */
    public ChartButtonTransferHandler() {
    }
    
    /**
     * Indicates whether or not a component can import any of the given data flavors.
     *
     * @param comp The component.
     * @param transferFlavors The data flavors to be examined.
     * @return True if any of the data flavors can be imported.
     */
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        if (comp instanceof ExaminationDropTargetButton) {
            for (int i = 0; i < transferFlavors.length; i++) {
                for (int j = 0; j < flavors.length; j++) {
                    if (transferFlavors[i].equals(flavors[j])) {
                        return true;
                    }
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
        // a ChartButton can't start drags yet
        return null;
    }
    
    /**
     * Returns the actions that the component can give rise to.
     *
     * @param c The component.
     * @return The actions that the component can give rise to.
     */
    public int getSourceActions(JComponent c) {
        // this shouldn't be called as a ChartButton can't start drags yet
        return TransferHandler.COPY;        
    }
    
    /**
     * Makes all the data in the Transferable object belong to the data group.
     *
     * @param comp The component that received the data.
     * @param t The Transferable object that is sent to the component.
     * @return True if the operation succeeded.
     */
    public boolean importData(JComponent comp, Transferable t) {
        if (t.isDataFlavorSupported(ExaminationDataElementVectorFlavor.getInstance())) {
            if (comp instanceof ExaminationDropTargetButton) {
                try {
                    ExaminationDataElementVector importedElementVector = (ExaminationDataElementVector)t.getTransferData(ExaminationDataElementVectorFlavor.getInstance());
                    (new ImportThread((ExaminationDropTargetButton)comp, importedElementVector)).start();
                    return true;
                } catch (UnsupportedFlavorException e) {
                    return false;
                } catch (java.io.IOException e) {
                    return false;
                }
            }
        }
        return false;
    }
    
    /**
     * A private subclass of Thread to import the data from an ExaminationDataElementVector and
     * create a chart from it. 
     */
    private class ImportThread extends Thread {
        
        /** The element vector to create the chart from. */
        private ExaminationDataElementVector elementVector;
        
        /** The chart button that the elements were dropped on. */
        private ExaminationDropTarget button;
        
        /**
         * Creates a new ImportThread.
         *
         * @param button The chart button that the elements were dropped on.
         * @param elementVector The element vector with the elements to create the chart from.
         */
        public ImportThread(ExaminationDropTarget button, ExaminationDataElementVector elementVector) 
        {
            super("ChartButtonTransferHandler-ImportThread");
            this.elementVector = elementVector;
            this.button = button;
        }
        
        /**
         * Starts the importing.
         */
        public void run() {
            button.examinationsDropped(elementVector);                                                            
        }
    }
    
}
