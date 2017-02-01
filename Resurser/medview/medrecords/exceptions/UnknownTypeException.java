/*
 * UnknownTypeException.java
 *
 * Created on den 5 augusti 2003, 01:26
 *
 * $Id: UnknownTypeException.java,v 1.2 2003/11/11 14:40:37 oloft Exp $
 *
 * $Log: UnknownTypeException.java,v $
 * Revision 1.2  2003/11/11 14:40:37  oloft
 * Switching main-branch
 *
 * Revision 1.1.2.1  2003/08/16 14:48:08  erichson
 * First check-in.
 *
 */

package medview.medrecords.exceptions;

/**
 * General exception for type-checking situations
 * @author  nix
 */
public class UnknownTypeException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>UnknownTypeException</code> without detail message.
     */
    public UnknownTypeException() {
    }    
    
    /**
     * Constructs an instance of <code>UnknownTypeException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public UnknownTypeException(String msg) {
        super(msg);
    }
}
