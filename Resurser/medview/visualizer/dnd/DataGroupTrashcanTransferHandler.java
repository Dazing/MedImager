/*
 * DataGroupTrashcanTransferHandler.java
 *
 * Created on August 29, 2002, 11:04 AM
 *
 * $Id: DataGroupTrashcanTransferHandler.java,v 1.5 2004/11/13 10:56:56 erichson Exp $
 *
 * $Log: DataGroupTrashcanTransferHandler.java,v $
 * Revision 1.5  2004/11/13 10:56:56  erichson
 * Thread naming
 *
 * Revision 1.4  2002/12/11 10:23:14  zachrisg
 * Moved the datagroup into a new thread to free up the gui thread.
 *
 * Revision 1.3  2002/11/28 14:49:59  zachrisg
 * Removed debug output.
 *
 * Revision 1.2  2002/10/30 14:31:58  zachrisg
 * Added Id and Log tags and some javadoc.
 *
 */

package medview.visualizer.dnd;

import java.awt.datatransfer.*;
import javax.swing.*;

import medview.visualizer.data.*;
import medview.visualizer.gui.*;

/**
 * A TransferHandler subclass for the trashcan in the data group panel.
 * 
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class DataGroupTrashcanTransferHandler extends TransferHandler {

    /** The data flavors that the transferhandler can handle. */
    private static final DataFlavor[] flavors = { DataGroupVectorFlavor.getInstance() };
            
    /**
     * Indicates whether or not a component can import any of the given data flavors.
     *
     * @param comp The component.
     * @param transferFlavors The flavors.
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
        // a trashcan shouldn't be able to start a drag.. should it?        
        return null;
    }
    
    /**
     * Returns the possible source actions of a component.
     *
     * @param c The component.
     * @return The possible source actions.
     */
    public int getSourceActions(JComponent c) {
        // this shouldn't happen since a trashcan shouldn't start drags
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
        if (t.isDataFlavorSupported(DataGroupVectorFlavor.getInstance())) {
            try {
                DataGroupVector dataGroupVector = (DataGroupVector) t.getTransferData(DataGroupVectorFlavor.getInstance());
                (new ImportThread(dataGroupVector)).start();
                return true;
            } catch (UnsupportedFlavorException e) {
                return false;
            } catch (java.io.IOException e) {
                return false;
            }
        }
        return false;
    }    
    
    private class ImportThread extends Thread {
        private DataGroupVector dataGroupVector;   
        
        public ImportThread(DataGroupVector dataGroupVector) 
        {
            super("DataGroupTrashcanTransferHandler-ImportThread");
            this.dataGroupVector = dataGroupVector;
        }
    
        public void run() {
            DataManager.getInstance().removeDataGroups(dataGroupVector.toArray());
            
            // repaint concerned views
            DataManager.getInstance().validateViews();
        }
    }
}