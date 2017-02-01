package medview.aggregator;

import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import javax.swing.JTree;
import javax.swing.Icon;
import javax.swing.ImageIcon;


/**
 * $Id: GroupTreeCellRenderer.java,v 1.1 2004/05/19 14:43:08 d97nix Exp $
 *
 * $Log: GroupTreeCellRenderer.java,v $
 * Revision 1.1  2004/05/19 14:43:08  d97nix
 * Rewrote GroupCellRender and renamed it to this.
 *
 */

/**
 * TreeCellRenderer for cells in the group tree.
 * 
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

public class GroupTreeCellRenderer extends DefaultTreeCellRenderer {

    private final Icon funnelIcon, groupIcon, valueIcon;
    
    /**
     *
     */
    public GroupTreeCellRenderer()
    {
        super();
        funnelIcon = loadIcon("medview/funnel16.png"); // For aggregation root node
        groupIcon = loadIcon("openide/methods.gif");
        // openFolderIcon = loadIcon("openide/defaultFolderOpen.gif");
        // defaultFolderIcon = loadIcon("openide/defaultFolder.gif");
        valueIcon = loadIcon("openide/methodPublic.gif");    
    }

    public Component getTreeCellRendererComponent (JTree aTree,
                                                        Object value,
                                                        boolean sel,
                                                        boolean expanded,
                                                        boolean leaf,
                                                        int row,
                                                        boolean hasFocus)
    {
        super.getTreeCellRendererComponent( aTree,value,sel,expanded,leaf,row,hasFocus);

        //setFont(new Font("Arialo",1,8));

        GroupNode aNode  = (GroupNode) value;        
        
        switch(aNode.getType())
        {
            case GroupNode.IS_VALUE:
                //if(aNode.isLeaf())
                    setIcon(valueIcon);                
                break;
            case GroupNode.IS_ROOT:
                setIcon(funnelIcon);
                break;
            case GroupNode.IS_ATTRB:
                // super.getTreeCellRendererComponent( aTree,value,sel,expanded,false,row,hasFocus);
                if (expanded)
                    setIcon(openIcon);                
                else
                    setIcon(closedIcon);                                                
                break;
            case GroupNode.IS_GROUP:
                setIcon(groupIcon);
                break;            
            default:
                System.err.println("AggregationTreeCellRenderer: Warning: got unknown nodetype (" + aNode.getType() + ")");
                break;
        }
        
        return this;
    }

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