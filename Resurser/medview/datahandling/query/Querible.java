/*
 * Querible.java
 *
 * Created on October 11, 2002, 10:09 AM
 * 
 * $Id: Querible.java,v 1.1 2002/10/22 16:15:27 zachrisg Exp $
 *
 * $Log: Querible.java,v $
 * Revision 1.1  2002/10/22 16:15:27  zachrisg
 * Moved from medview.visualizer.data.query.
 *
 * Revision 1.2  2002/10/22 14:26:19  zachrisg
 * Added some javadoc.
 *
 * Revision 1.1  2002/10/11 09:00:59  zachrisg
 * First check in.
 *
 */

package medview.datahandling.query;

import medview.datahandling.NoSuchTermException;
import medview.datahandling.aggregation.Aggregation;

/**
 * Interface to be implemented by classes that wish to be queried by the Query class.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public interface Querible {
    
    /**
     * Returns the values of the Querible.
     *
     * @param term The term.
     * @param agg The aggregation.
     * @return An array containing the values of the Querible for the term.
     * @throws NoSuchTermException If the term is not defined for the instance of the Querible.
     */
    public String[] getValues(String term, Aggregation agg) throws NoSuchTermException;
}
