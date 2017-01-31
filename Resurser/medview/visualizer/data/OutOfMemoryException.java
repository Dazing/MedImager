/*
 * OutOfMemoryException.java
 *
 * $Id: OutOfMemoryException.java,v 1.1 2004/12/21 13:02:11 erichson Exp $
 *
 * $Log: OutOfMemoryException.java,v $
 * Revision 1.1  2004/12/21 13:02:11  erichson
 * First check-in.
 *
 */

package medview.visualizer.data;

/**
 * Exception thrown when OutOfMemoryError is caught
 *
 * @author erichson
 */
public class OutOfMemoryException extends Exception 
{
    
    /** Creates a new instance of OutOfMemoryException */
    public OutOfMemoryException() 
    {
        super();
    }
    
    public OutOfMemoryException(String message)
    {
        super(message);
    }
    
}
