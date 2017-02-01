/*
 * $Id: HashVector.java,v 1.1 2006/05/29 18:33:02 limpan Exp $
 *
 * Created on August 12, 2001, 4:39 PM
 */

package misc.foundation;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 * A Vector with support for hashing.
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.1, 02-07-03
 */

public class HashVector extends Object {

    private Hashtable hashtable;
    private Vector vector;

    /** Creates new HashVector */
    public HashVector() {
        hashtable = new Hashtable();
        vector = new Vector();
    }

    /** Creates a new HashVector, with an initial hashtable capacity
     * @param size initial capacity
     */
    public HashVector(int size) {
        hashtable = new Hashtable(size);
        vector = new Vector();
    }

    /** Put an object into the table
     * @param key The key
     * @param value The value
     */
    public void put(Object key, Object value) {
        // System.out.println("Did put: " + key + ", " + value);
        vector.add(value);
        hashtable.put(key,value);
    }

    /** Get an object from the HashVector according to its index
     * @param index The index of the object to get
     * @return The object indexed by <CODE>index</CODE>
     */
    public Object get(int index) {
        return vector.get(index);
    }

    /** Get an object from the HashVector according to a key
     * @param key The key
     * @return The object mapped by <CODE>key</CODE>
     */
    public Object get(Object key) {
        // System.out.println("Did get: " + key);
        return hashtable.get(key);
    }

    /** Check if this HashVector contains an object
     * @param object The object to search for
     * @return Whether the HashVector contains the object or not
     */
    public boolean contains(Object object) {
        return ((vector.contains(object) && hashtable.contains(object)));
    }


    /** Get an (ordered) iterator view of the HashVector
     * @return Ordered iterator for the elements in the HashVector
     */
    public Iterator iterator() {
        return vector.iterator();
    }

    /** Check whether the HashVector contains a key
     * @param key The key to look for
     * @return True or false, whether the HashVector contains the key or not
     */
    public boolean containsKey(Object key) {
        return hashtable.containsKey(key);
    }

    /** Remove an object from the HashVector
     * @param key The key for the object to remove
     */
    public void removeByKey(Object key) {
        Object o = hashtable.get(key);
        vector.remove(o);
        hashtable.remove(key);
    }

    /** Move an object in the ordering
     * @param object The object to move
     * @param relativeMovement The relative movement (can be positive or negative)
     */
    public void moveRelative(Object object, int relativeMovement) {
        int startingPosition = vector.indexOf(object);

        int newIndex = startingPosition + relativeMovement;

        //System.out.println("Starting pos: " + startingPosition);
        //System.out.println("Removing and re-adding at " + newIndex);

        if (startingPosition >= 0) {
            // Remove the object from the vector
            vector.remove(object);
            // add it at the new position
            try {
                vector.add(newIndex,object);
            } catch(ArrayIndexOutOfBoundsException e) {
                // Adding failed, restore to old position
                vector.add(startingPosition,object);
            }

        }

        // System.out.println("Ending pos: " + vector.indexOf(o));

    }

    /** Get the amount of elements in the HashVector
     * @return The amount of elements
     */
    public int size() {

        int vs = vector.size();
        int hts = hashtable.size();

        if (vs != hts) {
            System.err.println("HashVector integrity error: vectorsize(" + vs+") != hashtable size (" + hts + ")...");
        }

        return vs;
    }



    /** Get all the values as an array
     * @return The values as an array
     */
     public Object[] getValueArray() {
        return vector.toArray();
    }

    public Object[] getValueArray(Object[] o) {
        return vector.toArray(o);
    }
    
     public Object[] getKeyArray() {

         return hashtable.keySet().toArray();

     }

    public Object[] getKeyArray(Object[] o) {

        return hashtable.keySet().toArray(o);

    }
    
}
