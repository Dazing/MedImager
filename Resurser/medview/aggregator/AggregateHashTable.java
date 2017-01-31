package medview.aggregator;

import java.util.*;
import medview.datahandling.termvalues.*;

/**
 *
 * $Id: AggregateHashTable.java,v 1.2 2004/05/17 14:09:22 d97nix Exp $
 *
 * $Log: AggregateHashTable.java,v $
 * Revision 1.2  2004/05/17 14:09:22  d97nix
 * Cleanup; added termValueComparator ordering of elements in aggregator.
 *
 *
 */

/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

public class AggregateHashTable extends Hashtable {

    private final static TermValueComparator termValueComparator = TermValueComparator.getInstance();
    
    public AggregateHashTable()
    {
        super();
    }

    public AggregateHashTable(int aSize)
    {
        super(aSize);
    }

    public AggregateHashTable(Node tree) {
        super();
        addTree(tree);
    }

    /**
     * Return a sorted vector of all attribute in the hash table.
     */
    Vector getSortedAttributes()
    {
        Vector attributes = new Vector();

        for (Iterator keyIt = this.keySet().iterator(); keyIt.hasNext(); ) {
            String key = (String) keyIt.next();
            insertElement(attributes,key);
        }
        return attributes;
    }

    /**
     * Add a complet tree to tha hashtable.
     */
    void addTree(Node aNode){
        Vector leaves = aNode.getLeaves();

        for (Enumeration e = leaves.elements(); e.hasMoreElements();) {
            Node nod = (Node) e.nextElement();
            //if(nod == null) continue;

            String val = nod.getValue();
            if(val == null) continue;

            Node part = (Node)nod.getParent();
            if(part == null)continue;

            String attrib = part.getValue();
            if(attrib == null) continue;

            //attrib.replace('-','_');
            addValue(attrib,val);
        }
    }

    /**
     * Add one value to an attribute.
     */
    private void addValue(String in_attribute, String value) {

        String attribute = in_attribute; //.toLowerCase();

        if (! this.containsKey( attribute)) {
            this.put(attribute, new Vector());
        }

        Vector valVector = (Vector) this.get(attribute);
        if ( !valVector.contains(value)) insertElement(valVector,value);
    }
    
    /**
     * Uses insertion sort to add a new value.
     * Called from getSortedAttributes. Uses termValueComparator to take care of ordering of term values.
     */
    private void insertElement(Vector theList, String anElem){
        
        // Iterate over theList
        for(int i = 0; i < theList.size() ; i++){
            String elem = (String) theList.get(i) ;
            
            // Compare them according to term-value ordering (see TermValueComparator)
            if (termValueComparator.compare(elem,anElem) > 0)
            {
                theList.add(i,anElem);
                return;
            }
        }
        theList.add(anElem);
    }        
}

