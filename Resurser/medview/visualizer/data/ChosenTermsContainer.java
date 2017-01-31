/*
 * ChosenTermsContainer.java
 *
 * Created on den 22 december 2004, 14:26
 *
 * $Id: ChosenTermsContainer.java,v 1.1 2005/01/26 12:56:33 erichson Exp $
 *
 * $Log: ChosenTermsContainer.java,v $
 * Revision 1.1  2005/01/26 12:56:33  erichson
 * First check-in.
 *
 *
 */

package medview.visualizer.data;

/**
 * Simple base interface for handling chosen terms
 *
 * @author erichson
 */
public interface ChosenTermsContainer
{    
    
    public String[] getChosenTerms();
    
    public void setChosenTerms(String[] terms);
}
