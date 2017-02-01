/*
 * AlphabeticalKeysHashMap.java
 *
 * Created on August 22, 2002, 3:55 PM
 *
 * $Id: AlphabeticalHashSet.java,v 1.1 2006/05/29 18:33:02 limpan Exp $
 *
 * $Log: AlphabeticalHashSet.java,v $
 * Revision 1.1  2006/05/29 18:33:02  limpan
 * Added minimed code
 * ----------------------------------------------------------------------
 *
 * Revision 1.7  2005/01/26 12:38:59  erichson
 * Added retainAll, updated other methods
 *
 * Revision 1.6  2004/10/06 14:20:47  erichson
 * Possible to be non-case sensitive. (Fixes bugzilla bug 3)
 *
 * Revision 1.5  2002/11/07 14:52:58  erichson
 * Fixed bug: Using this for non-string objects would throw ClassCastException since Collections.sort(List) needs objects to have a compareTo method.
 * Solved this by adding my own comparator, ToStringComparator, which compares the objects' toString() values
 *
 *
 */

package misc.foundation;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Vector;

/**
 * A LinkedHashSet which maintains alphabetical order efficiently. 
 * Ordering can be case senstitive or not. 
 * NOTE: Equals method is always case sensitive.
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.0, 23 august 2002
 */
public class AlphabeticalHashSet extends LinkedHashSet {
        
    /** Whether the key ordering is valid or needs to be re-built */
    private boolean keysValid = true;
    private boolean caseSensitive = true;
    
    /* Constructors */
    
    /** Creates a new instance of AlphabeticalKeysHashMap */
    public AlphabeticalHashSet(boolean caseSensitive) {
        super();        
        this.caseSensitive = caseSensitive;
    }
    
    public AlphabeticalHashSet(Collection c, boolean caseSensitive) {
        super(c);        
        this.caseSensitive = caseSensitive;
    }
    
    public AlphabeticalHashSet(Object[] objects, boolean caseSensitive) {
        this(caseSensitive);        
        
        for (int i = 0; i < objects.length; i++) {
            add(objects[i]);
        }
    }
    
    public AlphabeticalHashSet(int initialCapacity) {
        super(initialCapacity);
    }
        
    public AlphabeticalHashSet(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }
    
    /* Methods */
    
    public boolean add(Object key) {
        boolean changed = super.add(key);
        if (changed)
            keysValid = false;
        return changed;
    }
    
    public boolean addAll(Collection c)
    {
        boolean changed = super.addAll(c);
        if (changed)
            keysValid = false;
        return changed;
    }
    
    public String[] toStringArray() {
                
        String[] strings = new String[size()];
        strings = (String[]) this.toArray(strings); // Validates order        
        return strings;
    }
    
    public Iterator iterator() {
        validateOrder();
        return super.iterator();
    }
    
    private Iterator unorderedIterator() {
        return super.iterator();
    }
    
    public boolean remove(Object key) {
        boolean changed = super.remove(key);
        if (changed)
            keysValid = false;
        return changed;
    }
    
    public boolean removeAll(Collection c)
    {
        boolean changed = super.removeAll(c);
        if (changed)
            keysValid = false;
        return changed;
    }
    
    public boolean retainAll(Collection c)
    {
        boolean changed = super.retainAll(c);
        if (changed)
            keysValid = false;
        return changed;
    }
    
    public void clear() {
        super.clear();
        keysValid = false;
    }
    
    public String toString() {
        validateOrder();
        return super.toString();
    }
    
    public Object[] toArray() {
        validateOrder();
        return super.toArray();       
    }
     
    public Object[] toArray(Object[] o) {
        validateOrder();
        return super.toArray(o);
    }
    
    /**
     * Validates the order. ordering is done by comparing toString() values
     */
    private synchronized void validateOrder() {        
        if (keysValid == false ) {
            // rebuild the set
            Vector copy = new Vector();
            for (Iterator it = unorderedIterator(); it.hasNext(); ) { // iterate over this set
                copy.add(it.next());
            }
                                                
            Collections.sort(copy, new ToStringComparator(caseSensitive)); // Sort the items alphabetically using our own string-comparator
            
            /* Clear this instance of data and add the items in the sorted copy instead */
            
            this.clear();
            for (Iterator it = copy.iterator(); it.hasNext();) {
                Object o = it.next();
                this.add(o);
            }                        
        }
    }            
    
    /** 
     * Comparator that compares two objects' toString() results
     */
    private class ToStringComparator implements Comparator {        
        private boolean caseSensitive;
        
        public ToStringComparator(boolean caseSensitive)
        {
            this.caseSensitive = caseSensitive;
        }
        
        
        public int compare(Object obj, Object obj1) {
            if (caseSensitive)
                return obj.toString().compareTo(obj1.toString());
            else
                return obj.toString().toUpperCase().compareTo(obj1.toString().toUpperCase());
        }        
    }
    
    
   public boolean equals(Object o) {
       if (o.getClass().equals(this.getClass())) {
           AlphabeticalHashSet other = (AlphabeticalHashSet) o;
           if (other.size() != this.size()) {
               return false;
           } else { // iterate through all elements
               Iterator it1 = iterator();
               Iterator it2 = other.iterator();
               
               while (it1.hasNext() && it2.hasNext()) {
                   Object o1 = it1.next();
                   Object o2 = it2.next();
                   if (! (o1.equals(o2))) {
                       return false;
                   }               
               }
               
               return true;
           }
       } else { // not the same class
           return false;
       }
   }                                  
}
