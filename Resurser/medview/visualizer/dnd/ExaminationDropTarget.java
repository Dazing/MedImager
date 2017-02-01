/*
 * $Id: ExaminationDropTarget.java,v 1.1 2004/10/12 09:54:09 erichson Exp $
 *
 * Created on October 12, 2004, 9:51 AM
 *
 * $Log: ExaminationDropTarget.java,v $
 * Revision 1.1  2004/10/12 09:54:09  erichson
 * First check-in.
 *
 */

package medview.visualizer.dnd;

import medview.visualizer.data.*;

/**
 * basic interface for examination drop targets.
 *
 * @author Nils Erichson
 */
public interface ExaminationDropTarget {
    
    public void examinationsDropped(ExaminationDataElementVector elementVector);
    
}
