/*
 * QueryConstraint.java
 *
 * Created on October 11, 2002, 10:00 AM
 *
 * $Id: QueryConstraint.java,v 1.1 2002/10/22 16:15:28 zachrisg Exp $
 *
 * $Log: QueryConstraint.java,v $
 * Revision 1.1  2002/10/22 16:15:28  zachrisg
 * Moved from medview.visualizer.data.query.
 *
 * Revision 1.4  2002/10/22 15:51:47  zachrisg
 * Now uses MedViewDataHandler.getDefaultValue().
 *
 * Revision 1.3  2002/10/21 14:11:28  zachrisg
 * Added support for <n/a> values.
 *
 * Revision 1.2  2002/10/11 13:43:11  zachrisg
 * Added getValues().
 *
 * Revision 1.1  2002/10/11 09:01:00  zachrisg
 * First check in.
 *
 */

package medview.datahandling.query;

import java.util.*;

import medview.datahandling.MedViewDataHandler;
import medview.datahandling.NoSuchTermException;
import medview.datahandling.aggregation.Aggregation;

/**
 * A constraint for the values of a term.
 * Use addValue() to add values for the term. Then use 
 * isFulfilledBy() to find out if any of the values of the
 * Querible for the term matches any of thoose in the constraint.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class QueryConstraint {
    
    /** The term that the values are constraining. */
    private String term;
    
    /** A hashset of possible values for the term. */
    private LinkedHashSet values;
   
    /** 
     * Creates a new instance of QueryConstraint.
     *
     * @param term The term to put the constraint on.
     */
    public QueryConstraint(String term) {
        this.term = term;
        values = new LinkedHashSet();
    }
    
    /**
     * Adds a possible value for the term.
     *
     * @param value The value to add.
     */
    public void addValue(String value) {
        if (!values.contains(value)) {
            values.add(value);
        }
    }
    
    /**
     * Removes a possible value for the term.
     *
     * @param value The value to remove.
     */
    public void removeValue(String value) {
        values.remove(value);
    }
    
    /**
     * Returns the term.
     *
     * @return The term.
     */
    public String getTerm() {
        return term;
    }
    
    /**
     * Sets the term.
     * Note that it is not much logic in changing the term since the values
     * of the constraint are specific for the term, and thus they would not be
     * valid if you change the term.
     *
     * @param newTerm The new term.
     */
     public void setTerm(String newTerm) {
         term = newTerm;
     }
    
    /**
     * Returns the values for the term specified by this constraint.
     *
     * @return An array of the values for the term.
     */
    public String[] getValues() {
        return (String[])values.toArray(new String[values.size()]);
    }
    
    /**
     * Returns the number of values in this constraint.
     *
     * @return The number of values in this constraint.
     */
    public int getValueCount() {
        return values.size();
    }
    
    /**
     * Returns true if the constraint is fulfilled by the Querible.
     *
     * @param querible The Querible to examine.
     * @param agg The aggregation to use. If you pass null then no aggragation will be used.
     * @return True if the constraint is fulfilled by the Querible.
     */
    public boolean isFulfilledBy(Querible querible, Aggregation agg) {
        String[] qValues;
        try {
            qValues = querible.getValues(term, agg);
            if (qValues.length == 0) {
                qValues = new String[1];
                qValues[0] = MedViewDataHandler.instance().getDefaultValue(term);
            }
        } catch (NoSuchTermException e) {
            qValues = new String[1];
            qValues[0] = MedViewDataHandler.instance().getDefaultValue(term);
        }
        
        // if any of the Quierible's values matches the constraint's, then return true
        for (int i = 0; i < qValues.length; i++) {
            for (Iterator j = values.iterator(); j.hasNext(); ) {
                String value = (String) j.next();
                if (value.equals(qValues[i])) {
                    return true;
                }
            }
        }
        
        // no values matched
        return false;        
    }
}
