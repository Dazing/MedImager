/*
 * DataGroupComponentSelectButtonTransferHandler.java
 *
 * Created on October 29, 2002, 11:21 AM
 *
 * $Id: DataGroupComponentSelectButtonTransferHandler.java,v 1.3 2002/11/28 14:46:48 zachrisg Exp $
 *
 * $Log: DataGroupComponentSelectButtonTransferHandler.java,v $
 * Revision 1.3  2002/11/28 14:46:48  zachrisg
 * Removed some debug output.
 *
 * Revision 1.2  2002/11/01 10:41:12  zachrisg
 * Now all selected elements in the selected data groups are transfered in a drag operation.
 *
 * Revision 1.1  2002/10/29 14:01:33  zachrisg
 * First check in.
 *
 */

package medview.visualizer.dnd;

import java.awt.datatransfer.*;
import java.io.*;
import javax.swing.*;

import medview.visualizer.data.*;
import medview.visualizer.gui.*;

/**
 * A TransferHandler for handling Drag'n'Drop from and to the select button of a data group component.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class DataGroupComponentSelectButtonTransferHandler extends TransferHandler {
    
    /** The DataGroupComponent that the transferhandler is associated with. */
    private DataGroupComponent dataGroupComponent;
    
    /**
     * Creates a new instance of the transfer handler.
     *
     * @param component The data group component that the transferhandler should be associated with.
     */
    public DataGroupComponentSelectButtonTransferHandler(DataGroupComponent component) {
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
        // this type of transfer handler should not be able to import data
        return false;
    }
    
    /**
     * Creates a Transferable object from the component.
     *
     * @param c The component to create the object from.
     * @return A Transferable.
     */
    public Transferable createTransferable(JComponent c) {
        ExaminationDataElementVector elementVector= new ExaminationDataElementVector();
        DataGroupComponent[] components = dataGroupComponent.getParentDataGroupPanel().getSelectedDataGroupComponents();
        for (int i = 0; i < components.length; i++) {
            ExaminationDataElement[] elements = DataManager.getInstance().getElementsInDataGroup(components[i].getDataGroup());
            for (int e = 0; e < elements.length; e++) {
                if (elements[e].isSelected()) {
                    elementVector.add(elements[e]);
                }   
            }
        }
        return new ExaminationDataTransferable(elementVector);
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
        // this type of transfer handler should not be able to import data
        return false;
    }    
}
