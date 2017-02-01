//
//  ArchetypeTreeCellRenderer.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-11.
//
//  $Id: ArchetypeTreeCellRenderer.java,v 1.3 2008/12/16 09:42:17 oloft Exp $
//

package medview.openehr.workbench.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

import medview.openehr.workbench.model.tree.*;

public class ArchetypeTreeCellRenderer extends DefaultTreeCellRenderer  {

    public Component getTreeCellRendererComponent(
                        JTree tree,
                        Object value,
                        boolean sel,
                        boolean expanded,
                        boolean leaf,
                        int row,
                        boolean hasFocus) {

        super.getTreeCellRendererComponent(
                        tree, value, sel,
                        expanded, leaf, row,
                        hasFocus);
        if (hasImageName(value)) {
			String name = getImageName(value);
			Icon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/" + name));
			
            setIcon(icon);
            // setToolTipText("This book is in the Tutorial series.");*/
         }
        if (hasToolTipText(value)){
            setToolTipText(getToolTipText(value));
        }

        return this;
    }

    protected Boolean hasImageName(Object value) {
        return getImageName(value) != null;
    }
    protected String getImageName(Object value) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		
		if (node.getUserObject() instanceof ArchetypeNodeInfo) {
        ArchetypeNodeInfo info = (ArchetypeNodeInfo)node.getUserObject();
		
				return info.getImageName();

		}
		return null;
     }

     protected Boolean hasToolTipText(Object value) {
        return getToolTipText(value) != null;
     }

     protected String getToolTipText(Object value) {
         DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;

		if (node.getUserObject() instanceof ArchetypeNodeInfo) {
        ArchetypeNodeInfo info = (ArchetypeNodeInfo)node.getUserObject();

				return info.getPath();

		}
		return null;
     }
}

