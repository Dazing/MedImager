/*
 * ExaminationDataElementVectorFlavor.java
 *
 * Created on August 28, 2002, 11:16 AM
 *
 * $Id: ExaminationDataElementVectorFlavor.java,v 1.2 2002/10/30 14:37:26 zachrisg Exp $
 *
 * $Log: ExaminationDataElementVectorFlavor.java,v $
 * Revision 1.2  2002/10/30 14:37:26  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.dnd;

import java.awt.datatransfer.*;
import medview.visualizer.data.*;

/**
 * A DataFlavor for ExaminationDataElementVector. Uses the Singleton-pattern.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class ExaminationDataElementVectorFlavor extends DataFlavor {
    
    /** The reference to the only instance of the class. */
    private static ExaminationDataElementVectorFlavor theInstance = null;
    
    /** 
     * Creates a new instance of ExaminationDataElementVectorFlavor. 
     *
     * @throws ClassNotFoundException If medview.visualizer.data.ExaminationDataElementVector can not be found.
     */
    private ExaminationDataElementVectorFlavor() throws ClassNotFoundException {
        super(Class.forName("medview.visualizer.data.ExaminationDataElementVector"),"MedView ExaminationDataElementVector");
    }
    
    /**
     * Returns a reference to the only instance of the class.
     * If the class isn't instantiated yet it is instantiated.
     *
     * @return A reference to the only instance of the class.
     */
    public static ExaminationDataElementVectorFlavor getInstance() {
        if (theInstance == null) {
            try {
                theInstance = new ExaminationDataElementVectorFlavor();
            } catch (ClassNotFoundException e) {
                System.err.println("ExaminationDataElementVectorFlavor.getInstance(): Error: Class ExaminationDataElementVectorFlavor not found!");
                theInstance = null;
            }
        }
        return theInstance;
    }
}