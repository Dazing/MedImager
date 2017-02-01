/*
 * AbstractProgressObject.java
 *
 * $Id: AbstractProgressObject.java,v 1.2 2004/10/21 12:22:46 erichson Exp $
 *
 * $Log: AbstractProgressObject.java,v $
 * Revision 1.2  2004/10/21 12:22:46  erichson
 * Added ProgressNotifiable compatibility.
 *
 * Revision 1.1  2004/02/24 20:12:06  erichson
 * First check-in.
 *
 */

package medview.visualizer.data;

/**
 * Abstract helper implementation of the ProgressObject interface
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public abstract class AbstractProgressObject implements ProgressObject, misc.foundation.ProgressNotifiable {
    
    private String description = new String();
    
    protected int min, value, max;
    private boolean indeterminate = false;
    
    /** Creates a new instance of AbstractProgressObject */
    public AbstractProgressObject(boolean indeterminate) {        
        this(0,0,0,indeterminate);
    }
    
    public AbstractProgressObject(int min, int max, int now, boolean indeterminate) {        
        this.min = min;
        this.max = max;
        this.value = now;
        this.indeterminate = indeterminate;
    }
    
    public abstract void cancel();
        
    public int getProgress() {
        return value;
    }    
    public void setProgress(int progress) {
        value = progress;
    }
    
    public int getProgressMax() {
        return max;
    }
    public void setProgressMax(int newmax) {
        max = newmax;
    }    
    
    public int getProgressMin() {
        return min;
    }
    public void setProgressMin(int newmin) {
        min = newmin;
    }
    
    public abstract boolean isCancelled();
        
    public boolean isIndeterminate() {
        return indeterminate;    
    }
    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
    }
        
    public abstract boolean isRunning();
    
    /* misc.foundation.ProgressNotifiable compatibility */    
    public void setTotal(int total) { setProgressMax(total); }
    public int getTotal() { return getProgressMax(); }
    public void setCurrent(int current) { setProgress(current); }
    public int getCurrent() { return getProgress(); }
    public void setDescription(String desc) { description = desc; }
    public String getDescription() { return description; }
}
