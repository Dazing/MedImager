/*
 * AggregationTreeCellRenderer.java
 *
 * Created on November 4, 2002, 5:17 PM
 *
 * $Id: AggregationTreeCellRenderer.java,v 1.4 2004/10/08 14:53:11 erichson Exp $
 *
 * $Log: AggregationTreeCellRenderer.java,v $
 * Revision 1.4  2004/10/08 14:53:11  erichson
 * Fixed loop bug in loadIcon which broke AggregationPanel
 *
 * Revision 1.3  2004/05/19 14:41:22  d97nix
 * Rewrote somewhat.
 *
 * Revision 1.2  2002/11/07 16:36:38  erichson
 * Added support for NODETYPE_AGGREATION, fixed wrong filename (method -> methodPublic)
 *
 * Revision 1.1  2002/11/04 16:53:17  erichson
 * First check-in
 *
 */

package medview.common.components.aggregation;

import java.io.*;

import javax.swing.*;
import javax.swing.tree.*;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import medview.datahandling.aggregation.*;

/**
 * Cell renderer for items in an AggregationTree
 *
 * @author  Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class AggregationTreeCellRenderer extends javax.swing.tree.DefaultTreeCellRenderer {
    
    private final Icon funnelIcon, groupIcon, valueIcon;
    
    /** Creates a new instance of AggregationTreeCellRenderer */
    public AggregationTreeCellRenderer() {        
        super();
        
        funnelIcon = loadIcon("medview/funnel16.png"); // For aggregation root node
        groupIcon = loadIcon("openide/methods.gif");
        //openFolderIcon = loadIcon("openide/defaultFolderOpen.gif");
        //defaultFolderIcon = loadIcon("openide/defaultFolder.gif");
        valueIcon = loadIcon("openide/methodPublic.gif");    

    }
    
    public java.awt.Component getTreeCellRendererComponent(JTree tree, Object object, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) 
    {
                
        super.getTreeCellRendererComponent(tree,object,selected,expanded,leaf,row,hasFocus);                
                
        if (object instanceof AggregationTreeNode)
        {
            AggregationTreeNode aggNode = (AggregationTreeNode) object;
            int type = aggNode.getType();

            switch(type) {
                case AggregationTreeNode.NODETYPE_AGGREGATION:
                    setIcon(funnelIcon);
                    break;                    
                case AggregationTreeNode.NODETYPE_GROUP:
                    setIcon(groupIcon);
                    break;                    
                case AggregationTreeNode.NODETYPE_TERM:                    
                    if (expanded)
                        setIcon(openIcon);
                    else
                        setIcon(closedIcon);                                        
                    break;                                                             
                case AggregationTreeNode.NODETYPE_VALUE:
                    setIcon(valueIcon);
                    break;
                default:
                    System.err.println("AggregationTreeCellRenderer: Warning: got unknown nodetype (" + type + ") - reverting to super method");
                    break;                    
            }            
        } else {    
            System.err.println("AggregationTreeCellRenderer: Warning: got non-AggregationTreeNode (" + object.getClass() + "), reverting to super method");            
        }
        return this;
    }
                       
     /**
     * Load and create icon
     * @param name the icon image's file name
     * @return the new icon
     */
    
    private ImageIcon loadIcon(String name) {
        String resourceName = "/medview/common/resources/icons/" + name;
        //System.out.println("Trying to load: " + resourceName);
        java.net.URL resource = getClass().getResource(resourceName);
        if (resource == null)
        {
            System.err.println("Warning: Could not load resource " + resourceName +"!");
            // throw new java.io.IOException("Resource not found (" + resourceName + ")!");            
            return null;
        } else {
            return new javax.swing.ImageIcon(resource);
        }
    }    
}


 