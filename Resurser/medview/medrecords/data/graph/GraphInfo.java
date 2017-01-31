/*
 * GraphInfo.java
 *
 * Created on den 7 juni 2005, 12:05
 *
 * $Id: GraphInfo.java,v 1.2 2005/06/08 09:47:59 erichson Exp $
 *
 * $Log: GraphInfo.java,v $
 * Revision 1.2  2005/06/08 09:47:59  erichson
 * Added vector for proper ordering of attributes.
 *
 * Revision 1.1  2005/06/07 17:54:22  erichson
 * First check-in.
 *
 */

package medview.medrecords.data.graph;

/**
 *
 * Data class describing the contents of a graph.
 *
 * @author Nils Erichson
 */

import java.util.*;

public class GraphInfo
{       
    private HashMap attributeLabelsMap; // Maps attribute name to Label
    private Vector attributes;
    public String graphTitle;
    public int maxValue;
    public boolean usesFixMaxValue;

    public GraphInfo()
    {
        attributeLabelsMap = new HashMap();
        graphTitle = "Untitled";
        maxValue = 1;
        usesFixMaxValue = true;
        attributes = new Vector();
    }
    
    public void putAttributeLabel(String attribute, String label)
    {        
        attributeLabelsMap.put(attribute,label);
    }
    
    public void addAttribute(String attribute)
    {
        attributes.add(attribute);
    }
    
    public String[] getAttributes()
    {
        String[] attrs = new String[attributes.size()];        
        attrs = (String[]) attributes.toArray(attrs);
        return attrs;
    }
    
    public String getAttributeLabel(String attribute)
    {
        String label = (String) attributeLabelsMap.get(attribute);
        if (label != null)
            return label;
        else                       
            return "";
    }
    
}
            
    

