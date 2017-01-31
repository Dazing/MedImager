/*
 * ExaminationDataElementVector.java
 *
 * Created on August 28, 2002, 10:54 AM
 *
 * $Id: ExaminationDataElementVector.java,v 1.2 2002/10/30 14:07:04 zachrisg Exp $
 *
 * $Log: ExaminationDataElementVector.java,v $
 * Revision 1.2  2002/10/30 14:07:04  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.data;

import java.util.*;

/**
 * A wrapper for Vector to create a Vector that only can hold ExaminationDataElements.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class ExaminationDataElementVector {
    
    /** The vector containing the ExaminationDataElements. */
    private Vector elementVector;
    
    /** Creates a new instance of ExaminationDataElementVector */
    public ExaminationDataElementVector() {
        elementVector = new Vector();
    }
    
    /**
     * Adds an ExaminationDataElement.
     *
     * @param element The element to add.
     */
    public void add(ExaminationDataElement element) {
        elementVector.add(element);
    }
    
    /**
     * Adds an array of ExaminationDataElement.
     *
     * @param elements The elements to add.
     */
    public void add(ExaminationDataElement[] elements) {
        for (int i = 0; i < elements.length; i++) {
            elementVector.add(elements[i]);
        }
    }
        
    /**
     * Returns true if the vector contains the ExaminationDataElement.
     *
     * @param element The element.
     * @return True if the vector contains the element.
     */
    public boolean contains(ExaminationDataElement element) {
        return elementVector.contains(element);
    }
    
    /**
     * Returns an array of the ExaminationDataElements.
     *
     * @return An array containing the ExaminationDataElements.
     */
    public ExaminationDataElement[] toArray() {
        ExaminationDataElement[] elements = new ExaminationDataElement[elementVector.size()];
        return (ExaminationDataElement[]) elementVector.toArray(elements);
    }
}
