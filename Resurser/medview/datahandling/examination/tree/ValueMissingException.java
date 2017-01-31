/*
 * ValueMissingException.java
 *
 * Created on November 1, 2002, 10:55 AM
 *
 * $Id: ValueMissingException.java,v 1.1 2002/11/05 09:47:00 erichson Exp $
 *
 * $Log: ValueMissingException.java,v $
 * Revision 1.1  2002/11/05 09:47:00  erichson
 * First check-in
 *
 */

package medview.datahandling.examination.tree;

/**
 * Exception which is thrown when a value could not be determined for a term
 * 
 * @author Nils Erichson <d97nix@dtek.chalmers.se> 
 */
public class ValueMissingException extends java.lang.Exception {
    
    /**
     * Creates a new instance of <code>ValueMissingException</code> without detail message.
     */
    public ValueMissingException() {
    }
    
    
    /**
     * Constructs an instance of <code>ValueMissingException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ValueMissingException(String msg) {
        super(msg);
    }
}
