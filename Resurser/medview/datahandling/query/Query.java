/*
 * Query.java
 *
 * Created on October 11, 2002, 10:27 AM
 *
 * $Id: Query.java,v 1.1 2002/10/22 16:15:27 zachrisg Exp $
 *
 * $Log: Query.java,v $
 * Revision 1.1  2002/10/22 16:15:27  zachrisg
 * Moved from medview.visualizer.data.query.
 *
 * Revision 1.4  2002/10/15 07:54:48  zachrisg
 * Added setQueryIsAdjusting() and getQueryIsAdjusting().
 *
 * Revision 1.3  2002/10/11 13:47:14  zachrisg
 * Added method getConstraintValues().
 *
 * Revision 1.2  2002/10/11 09:16:39  zachrisg
 * Added support for listeners.
 *
 * Revision 1.1  2002/10/11 09:01:00  zachrisg
 * First check in.
 *
 */

package medview.datahandling.query;

import java.util.*;

import medview.datahandling.aggregation.Aggregation;
import medview.visualizer.event.*;

/**
 * A Query class used to determine if a Querible fulfills a number of QueryConstraints.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class Query {

    /** The query change listeners. */
    private LinkedHashSet changeListeners;
    
    /** The constraints. */
    private Hashtable constraints;
    
    /** True if the query is undergoing a series of changes. */
    private boolean queryIsAdjusting = false;
    
    /** Creates a new instance of Query */
    public Query() {
        constraints = new Hashtable();
        changeListeners = new LinkedHashSet();
    }
    
    /**
     * Adds a query change listener.
     *
     * @param listener The listener to add.
     */
    public void addChangeListener(QueryChangeListener listener) {
        if (!changeListeners.contains(listener)) {
            changeListeners.add(listener);
        }
    }
    
    /**
     * Removes a query change listener.
     *
     * @param listener The listener to remove.
     */
    public void removeChangeListener(QueryChangeListener listener) {
        changeListeners.remove(listener);
    }
    
    /**
     * Notifies all query change listeners that the query has changed.
     */
    private void fireQueryChanged() {
        for (Iterator i = changeListeners.iterator(); i.hasNext(); ) {
            QueryChangeListener listener = (QueryChangeListener) i.next();
            listener.queryChanged(new QueryEvent(this));
        }
    }        
    
    /**
     * Adds a new constraint.
     *
     * @param term The term to add the constraint to.
     * @param values The term values to add to the constraint.
     */
    public void addConstraint(String term, String[] values) {
        QueryConstraint constraint = (QueryConstraint) constraints.get(term);
        if (constraint == null) {
            constraint = new QueryConstraint(term);
            constraints.put(term, constraint);
        }
        for (int i = 0; i < values.length; i++) {
            constraint.addValue(values[i]);
        }
        fireQueryChanged();
    }
    
    /**
     * Removes all constraints of the specified term.
     *
     * @param term The term.
     */
    public void removeConstraint(String term) {
        constraints.remove(term);
        fireQueryChanged();
    }
   
    /**
     * Returns the possible values of a term for a constraint.
     *
     * @param term The term.
     * @return The possible values for that term.
     */
    public String[] getConstraintValues(String term) {
        QueryConstraint constraint = (QueryConstraint) constraints.get(term);
        if (constraint == null) {
            return new String[0];
        }
        return constraint.getValues();
    }
    
    /**
     * Returns the terms of the constraints.
     *
     * @return The terms of the constraints.
     */
    public String[] getConstraintTerms() {
        Set termSet = constraints.keySet();
        return (String[]) termSet.toArray(new String[termSet.size()]);
    }
    
    /**
     * Removes the specified constraints.
     *
     * @param term The term.
     * @param values The values.
     */
    public void removeConstraint(String term, String[] values) {
        QueryConstraint constraint = (QueryConstraint) constraints.get(term);
        if (constraint != null) {
            // remove the values from the constraint
            for (int i = 0; i < values.length; i++)  {
                constraint.removeValue(values[i]);
            }
            
            // if the constraint now is empty then remove the constraint
            if (constraint.getValueCount() == 0) {
                constraints.remove(term);
            }
        }
        fireQueryChanged();
    }
    
    /**
     * Returns true if the Querible fulfills the Query.
     *
     * @param querible The Querible.
     * @param agg The aggregation to use.
     */
    public boolean isFulfilledBy(Querible querible, Aggregation agg) {
        for (Enumeration e = constraints.elements(); e.hasMoreElements(); ) {
            QueryConstraint constraint = (QueryConstraint) e.nextElement();
            if (!constraint.isFulfilledBy(querible, agg)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * This attribute indicates that any coming changes should be considered as a single event.
     *
     * @param isAdjusting True if the coming changes should be considered a single event.
     */
    public void setQueryIsAdjusting(boolean isAdjusting) {
        queryIsAdjusting = isAdjusting;
    }
    
    /**
     * Returns true if the current changes to the query are part of a series of changes.
     *
     * @return True if the changes are part of a series of changes.
     */
    public boolean getQueryIsAdjusting() {
        return queryIsAdjusting;
    }
    
}
