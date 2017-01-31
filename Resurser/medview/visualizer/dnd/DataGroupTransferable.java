/*
 * DataGroupTransferable.java
 *
 * Created on August 26, 2002, 4:34 PM
 *
 * $Id: DataGroupTransferable.java,v 1.2 2002/10/30 14:29:26 zachrisg Exp $
 *
 * $Log: DataGroupTransferable.java,v $
 * Revision 1.2  2002/10/30 14:29:26  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.dnd;

import java.awt.datatransfer.*;
import java.io.*;

import medview.visualizer.data.*; // ExaminationDataSet

/**
 * A Transferable for transfer of data groups.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class DataGroupTransferable implements Transferable {
    
    /** The supported dataflavors. */
    private static final DataFlavor[] flavors = { DataGroupVectorFlavor.getInstance(), ExaminationDataElementVectorFlavor.getInstance() };
    
    /** The transfered data groups. */
    private DataGroupVector dataGroupVector;
    
    /** 
     * Creates a new instance of DataGroupTransferable.
     *
     * @param dataGroupVector The data groups to be transfered.
     */
    public DataGroupTransferable(DataGroupVector dataGroupVector) {
        this.dataGroupVector = dataGroupVector;
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
        if (dataFlavor.equals( DataGroupVectorFlavor.getInstance())) {
            return dataGroupVector;
        } else if (dataFlavor.equals(ExaminationDataElementVectorFlavor.getInstance())) {                        
            // create an ExaminationDataElementVector containing the elements of the data groups
            DataGroup[] dataGroups = dataGroupVector.toArray();            
            ExaminationDataElementVector elementVector = new ExaminationDataElementVector();
            for (int i = 0; i < dataGroups.length; i++) {
                elementVector.add(DataManager.getInstance().getElementsInDataGroup(dataGroups[i]));
            }
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
