/*
 * ProgressObject.java
 *
 * Created on den 3 juli 2003, 22:07
 *
 * $Id: ProgressObject.java,v 1.3 2004/10/21 12:19:36 erichson Exp $
 *
 * $Log: ProgressObject.java,v $
 * Revision 1.3  2004/10/21 12:19:36  erichson
 * added getDescription.
 *
 * Revision 1.2  2003/07/07 22:27:33  erichson
 * Corrected spelling (isCancelled), added methods cancel() and isIndeterminate
 *
 * Revision 1.1  2003/07/03 23:59:14  erichson
 * First check-in.
 *
 */

package medview.visualizer.data;

/**
 * Common interface for running jobs that other parts of the code want to ask for progress.
 * @author Nils Erichson <d97nix@dtek.chalmers.se
 */
public interface ProgressObject {
    
    public int getProgressMin();
    
    public int getProgress();
    
    public int getProgressMax();
    
    public boolean isRunning();
    
    public boolean isCancelled();
    
    public void cancel();
    
    public boolean isIndeterminate();
    
    public String getDescription();
    
}
