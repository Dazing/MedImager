/*
 * InvalidExaminationModelException.java
 *
 * Created on den 9 september 2003, 14:57
 *
 * $Id: InvalidExaminationModelException.java,v 1.2 2003/11/11 14:40:37 oloft Exp $
 *
 * $Log: InvalidExaminationModelException.java,v $
 * Revision 1.2  2003/11/11 14:40:37  oloft
 * Switching main-branch
 *
 * Revision 1.1.2.1  2003/09/09 13:57:47  erichson
 * First check-in.
 *
 */

package medview.medrecords.exceptions;

/**
 * Exception throws when an ExaminationModel is invalid
 * @author Nils Erichson
 */
public class InvalidExaminationModelException extends java.lang.Exception {        
    
    /**
     * Constructs an instance of <code>InvalidExaminationModelException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public InvalidExaminationModelException(String msg) {
        super(msg);
    }
}
