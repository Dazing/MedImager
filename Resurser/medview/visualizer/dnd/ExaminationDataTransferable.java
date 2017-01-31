/*
 * ExaminationDataTransferable.java
 *
 * Created on July 11, 2002, 10:41 AM
 *
 * $Id: ExaminationDataTransferable.java,v 1.2 2002/10/30 14:41:26 zachrisg Exp $
 *
 * $Log: ExaminationDataTransferable.java,v $
 * Revision 1.2  2002/10/30 14:41:26  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.dnd;

import java.awt.datatransfer.*;
import java.io.*;

import medview.visualizer.data.*; // ExaminationDataSet

/**
 * A Transferable for transfer of examination data.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class ExaminationDataTransferable implements Transferable {
    
    /** The supported dataflavors. */
    private static final DataFlavor[] flavors = { ExaminationDataElementVectorFlavor.getInstance() };
    
    /** The transfered examination data. */
    private ExaminationDataElementVector elementVector;
    
    /**
     * Creates a new instance of ExaminationDataTransferable.
     *
     * @param elementVector The ExaminationDataElementVector to transfer.
     */
    public ExaminationDataTransferable(ExaminationDataElementVector elementVector) {
        this.elementVector = elementVector;
    }

    /**
     * Returns an Object with the transfered data in the specified data flavor.
     *
     * @param dataFlavor The requested data flavor of the transfered data.
     * @return An Object with the transfered data.
     * @throws UnsupportedFlavorException If the flavor was not supported.
     * @throws IOException This is not thrown by this Transferable, but is part of the interface.
     */
    public synchronized Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {
        if (dataFlavor.equals(ExaminationDataElementVectorFlavor.getInstance())) {
            return elementVector;
        } else {
            throw new UnsupportedFlavorException(dataFlavor);
        }
    }
    
    /**
     * Returns an array of the data flavors that the Transferable can deliver the data in.
     *
     * @return An array of the supported data flavors.
     */
    public synchronized DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }
    
    /**
     * Checks if a data flavor is supported by the Transferable.
     *
     * @param dataFlavor The data flavor to check if it is supported.
     * @return True if it is supported, false otherwise.
     */
    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        for (int i = 0; i < flavors.length; i++) {
            if (dataFlavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }
    
}
