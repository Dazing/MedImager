/*
 * TermsChangeEvent.java
 *
 * Created on August 26, 2002, 4:13 PM
 *
 * $Id: TermsChangeEvent.java,v 1.3 2005/01/26 13:00:44 erichson Exp $
 *
 * $Log: TermsChangeEvent.java,v $
 * Revision 1.3  2005/01/26 13:00:44  erichson
 * More meta info added
 *
 * Revision 1.2  2002/10/30 15:06:34  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.event;

/**
 * An event for term changes.
 *
 * @author  erichson
 */
public class TermsChangeEvent 
{
    
    private boolean chosenTermsChanged;
    private boolean newTermsAdded;
    
    /** 
     * Creates a new instance of TermsChangeEvent.
     */
    public TermsChangeEvent(boolean chosenTermsChanged, boolean newTermsAdded)
    {
        this.chosenTermsChanged = chosenTermsChanged;
        this.newTermsAdded = newTermsAdded;
    }
    
    public boolean isChosenTermsChanged()
    {
        return chosenTermsChanged;
    }
    
    public boolean isNewTermsAdded()
    {
        return newTermsAdded;
    }
    
}
