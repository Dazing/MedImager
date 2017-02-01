/*
 * DataGroupVectorFlavor.java
 *
 * Created on August 28, 2002, 11:15 AM
 *
 * $Id: DataGroupVectorFlavor.java,v 1.2 2002/10/30 14:34:52 zachrisg Exp $
 *
 * $Log: DataGroupVectorFlavor.java,v $
 * Revision 1.2  2002/10/30 14:34:52  zachrisg
 * Added Id and Log tags.
 *
 */

package medview.visualizer.dnd;

import java.awt.datatransfer.*;
import medview.visualizer.data.*;

/**
 * A DataFlavor for DataGroupVector. Uses the Singleton-pattern.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class DataGroupVectorFlavor extends java.awt.datatransfer.DataFlavor {
    
    /** The reference to the only instance of the class. */
    private static DataGroupVectorFlavor theInstance = null;
    
    /** Creates a new instance of DataGroupVectorFlavor */
    private DataGroupVectorFlavor() throws ClassNotFoundException {
        super(Class.forName("medview.visualizer.data.DataGroupVector"),"MedView DataGroupVector");
    }
    
    /**
     * Returns a reference to the only instance of the class.
     * If the class isn't instantiated yet it is instatciated.
     *
     * @return A reference to the only instance of the class.
     */
    public static DataGroupVectorFlavor getInstance() {
        if (theInstance == null) {
            try {
                theInstance = new DataGroupVectorFlavor();
            } catch (ClassNotFoundException e) {
                System.err.println("DataGroupVectorFlavor.getInstance(): Error: Class DataGroupVectorFlavor not found!");
                theInstance = null;
            }
        }
        return theInstance;
    }
}