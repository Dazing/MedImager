/*
 * ConcreteProgressObject.java
 *
 * Created on den 23 februari 2004, 17:44
 *
 * $Id: DefaultProgressObject.java,v 1.1 2004/10/21 08:25:24 erichson Exp $
 *
 * $Log: DefaultProgressObject.java,v $
 * Revision 1.1  2004/10/21 08:25:24  erichson
 * Renamed from ConcreteProgressObject
 *
 * Revision 1.1  2004/02/24 20:23:57  erichson
 * First check-in.
 *
 */

package medview.visualizer.data;

/**
 * Full implementation of ProgressObject. isRunning() is implemented to return true as long as current
 * progress is less than max. cancel() does nothing except make isCancelled() return true, you must override
 * it to add more functionality.
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class DefaultProgressObject extends AbstractProgressObject {
            
    private boolean cancelled = false;
    /** Creates a new instance of ConcreteProgressObject */
    public DefaultProgressObject(int min, int max, int current, boolean indeterminate) {
        super(min,max,current,indeterminate);        
    }
    
    public DefaultProgressObject(boolean indeterminate) {
        super(indeterminate);        
    }
    
    public void cancel() {
        cancelled = true;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public boolean isRunning() {
        /*if (thread == null)
            return false;
        else
            return thread.isAlive();*/
        return (value < max);
    }    
}
