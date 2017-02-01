/*
 * DataGroupTransferHandler.java
 *
 * Created on July 22, 2002, 2:57 PM
 *
 * $Id: DataGroupComponentTransferHandler.java,v 1.6 2004/11/13 10:57:41 erichson Exp $
 *
 * $Log: DataGroupComponentTransferHandler.java,v $
 * Revision 1.6  2004/11/13 10:57:41  erichson
 * Thread naming
 *
 * Revision 1.5  2002/11/28 14:47:48  zachrisg
 * Removed some debug output.
 *
 * Revision 1.4  2002/10/23 12:24:38  zachrisg
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
 * A TransferHandler for handling Drag'n'Drop from and to data group components.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class DataGroupComponentTransferHandler extends TransferHandler {
    
    /** The data flavors that the transferhandler can handle. */
    private static final DataFlavor[] flavors = {ExaminationDataElementVectorFlavor.getInstance() };
    
    /** The DataGroupComponent that the transferhandler is associated with. */
    private DataGroupComponent dataGroupComponent;
    
    /**
     * Creates a new instance of DataGroupTransferHandler.
     *
     * @param component The data group component that the transferhandler should be associated with.
     */
    public DataGroupComponentTransferHandler(DataGroupComponent component) {
        dataGroupComponent = component;
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
        DataGroupVector dataGroupVector = new DataGroupVector();
        DataGroupComponent[] components = dataGroupComponent.getParentDataGroupPanel().getSelectedDataGroupComponents();
        for (int i = 0; i < components.length; i++) {
            dataGroupVector.add(components[i].getDataGroup());
        }
        return new DataGroupTransferable(dataGroupVector);
    }
    
    /**
     * Returns the possible source actions for a component.
     *
     * @param c The component.
     * @return The possible source actions.
     */    
    public int getSourceActions(JComponent c) {
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
            try {
                ExaminationDataElementVector elementVector = (ExaminationDataElementVector) t.getTransferData(ExaminationDataElementVectorFlavor.getInstance());
                
                (new ImportThread(elementVector)).start();
                return true;
            } catch (UnsupportedFlavorException e) {
                return false;
            } catch (java.io.IOException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * A Thread subclass that copies examinations into a data group.
     */
    private class ImportThread extends Thread {
        
        /** The vector of examinations to transfer into the data group. */
        private ExaminationDataElementVector elementVector;
        
        /**
         * Creates a new instance of ImportThread.
         *
         * @param elementVector The vector of examinations to transfer into the data group.
         */
        public ImportThread(ExaminationDataElementVector elementVector) 
        {
            super("DataGroupComponentTransferHandler-ImportThread");
            this.elementVector = elementVector;
        }
    
        /**
         * Starts the transfer.
         */
        public void run() {
            DataGroup dataGroup = dataGroupComponent.getDataGroup();
            ExaminationDataElement[] elements = elementVector.toArray();                              

            for (int i = 0; i < elements.length; i++) {
                try {
                    if (!DataManager.getInstance().examinationAlreadyExists(elements[i].getExaminationIdentifier(),dataGroup)) {
                        ExaminationDataElement element = new MedViewExaminationDataElement(dataGroup, elements[i].getDataSource(), elements[i].getExaminationValueContainer());
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
