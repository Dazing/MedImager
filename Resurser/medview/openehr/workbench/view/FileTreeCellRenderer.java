//
//  FileTreeCellRenderer.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-12-16.
//
//  $Id: FileTreeCellRenderer.java,v 1.2 2008/12/26 23:23:30 oloft Exp $
//

package medview.openehr.workbench.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

import medview.openehr.workbench.model.FileTreeNode;

public class FileTreeCellRenderer extends DefaultTreeCellRenderer {

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
						if (leaf) {
        if (hasImageName(value)) {
			String name = getImageName(value);
			Icon icon = new javax.swing.ImageIcon(getClass().getResource("/icons/" + name));
			
            setIcon(icon);
         }
        if (hasToolTipText(value)){
            setToolTipText(getToolTipText(value));
        }
}
        return this;
    }

    protected Boolean hasImageName(Object value) {
       // return getImageName(value) != null;
	   return true;
    }
    protected String getImageName(Object value) {
		if (value instanceof FileTreeNode) {
			FileTreeNode node = (FileTreeNode)value;
			
			return node.getImageName();
		}
		return null;
	}

     protected Boolean hasToolTipText(Object value) {
        // return getImageName(value) != null;
		return false;
     }

     protected String getToolTipText(Object value) {
         /*DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;

		if (node.getUserObject() instanceof ArchetypeNodeInfo) {
        ArchetypeNodeInfo info = (ArchetypeNodeInfo)node.getUserObject();

				return info.getPath();

		}
		return null;
		*/
		return null;
     }

}
