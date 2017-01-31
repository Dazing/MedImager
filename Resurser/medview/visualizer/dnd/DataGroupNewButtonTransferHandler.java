/*
 * DataGroupNewButtonTransferHandler.java
 *
 * Created on August 29, 2002, 12:09 PM
 *
 * $Id: DataGroupNewButtonTransferHandler.java,v 1.6 2004/11/13 11:01:50 erichson Exp $
 *
 * $Log: DataGroupNewButtonTransferHandler.java,v $
 * Revision 1.6  2004/11/13 11:01:50  erichson
 * Thread naming
 *
 * Revision 1.5  2002/11/28 14:48:47  zachrisg
 * Removed debug output.
 *
 * Revision 1.4  2002/10/23 12:24:39  zachrisg
 * Changed to use the new MedViewExaminationDataElement constructor.
 *
 */

package medview.visualizer.dnd;

import java.awt.datatransfer.*;
import java.io.*;
import javax.swing.*;

import medview.visualizer.data.*;
import medview.visualizer.gui.*;

/**
 * A transfer handler for the new data group button.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class DataGroupNewButtonTransferHandler extends TransferHandler {

    /** The data flavors that the transferhandler can handle. */
    private static final DataFlavor[] flavors = { ExaminationDataElementVectorFlavor.getInstance() };

    /** A reference to the DataGroupPanel. */
    private DataGroupPanel dataGroupPanel;
    
    /**
     * Creates a new DataGroupNewButtonTransferHandler.
     *
     * @param dataGroupPanel A reference to the DataGroupPanel.
     */
    public DataGroupNewButtonTransferHandler(DataGroupPanel dataGroupPanel) {
        this.dataGroupPanel = dataGroupPanel;
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
                    ApplicationManager.debug("  ..yes");
                    return true;
                }
            }
        }
        ApplicationManager.debug("  ..no");
        return false;
    }
    
    /**
     * Creates a Transferable object from the component.
     *
     * @param c The component to create the object from.
     * @return A Transferable.
     */
    public Transferable createTransferable(JComponent c) {
        // a NewButton shouldn't be able to start a drag.. should it?        
        return null;
    }
    
    /**
     * Returns the possible source actions for a component.
     *
     * @param c The component.
     * @return The possible source actions.
     */
    public int getSourceActions(JComponent c) {
        // this shouldn't happen since a NewButton shouldn't start drags
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

                (new ImportThread(elementVector)).start();
                    
                return true;
            } catch (UnsupportedFlavorException e) {
                return false;
            } catch (java.io.IOException e) {
                return false;
            }
        }
        return false;
    }    

    /**
     * A Thread subclass for transfering examinations to a new data group.
     */
    private class ImportThread extends Thread {
        
        /** The vector of elements to transfer to the new data group. */
        private ExaminationDataElementVector elementVector;
        
        /**
         * Creates a new ImportThread.
         * 
         * @param elementVector The vector of elements to transfer to the new data group.
         */
        public ImportThread(ExaminationDataElementVector elementVector) {
            super("DataGroupNewButtonTransferHandler-ImportThread");
            this.elementVector = elementVector;
        }
    
        /**
         * Starts the transfer.
         */
        public void run() {
            DataGroup dataGroup = dataGroupPanel.newDataGroup();

            if (dataGroup != null) {                
                ExaminationDataElement[] elements = elementVector.toArray();                              

                for (int i = 0; i < elements.length; i++) {
                    try {
                        if (!DataManager.getInstance().examinationAlreadyExists(elements[i].getExaminationIdentifier(),dataGroup)) {
                            ExaminationDataElement element = new MedViewExaminationDataElement(dataGroup,elements[i].getDataSource(), elements[i].getExaminationValueContainer());
                            element.setExaminationIdentifier(elements[i].getExaminationIdentifier());
                            DataManager.getInstance().addDataElement(element);       
                        }
                    } catch (IOException e) {
                        System.err.println("Couldn't get examination identifier.");
                    }
                }
                
                // repaint concerned views
                DataManager.getInstance().validateViews();
            }
        }
    }

}