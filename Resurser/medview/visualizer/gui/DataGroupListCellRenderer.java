/*
 * DataGroupListCellRenderer.java
 *
 * Created on August 8, 2002, 10:46 AM
 *
 * $Id: DataGroupListCellRenderer.java,v 1.2 2002/10/30 15:56:31 zachrisg Exp $
 *
 * $Log: DataGroupListCellRenderer.java,v $
 * Revision 1.2  2002/10/30 15:56:31  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import javax.swing.*;
import medview.visualizer.data.*;

/**
 * A ListCellRenderer for displaying data groups. Used by the DataGroupSelectorDialog.
 * 
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class DataGroupListCellRenderer extends JPanel implements ListCellRenderer {
    
    /** The data group color part of the component. */
    private JPanel colorPanel;
    
    /** The data group name part of the component. */
    private JLabel nameLabel;
    
    /** 
     * Creates a new instance of DataGroupListCellRenderer.
     */
    public DataGroupListCellRenderer() {
        colorPanel = new JPanel();
        colorPanel.setPreferredSize(new Dimension(20,10));
        nameLabel = new JLabel();
        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(colorPanel);
        add(nameLabel);
    }
    
    /**
     * Returns a component that can be used to paint a data group list cell.
     *
     * @param list The list.
     * @param value The data group.
     * @param index The index of the data group in the list.
     * @param isSelected True if the data group is selected in the list.
     * @param cellHasFocus True if the cell has focus.
     * @return A component that can be used to paint a data group list cell.
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        DataGroup group = (DataGroup) value;
        nameLabel.setText(group.getName());
        colorPanel.setBackground(group.getColor());
        if (isSelected) {
            setBackground(Color.black);
            nameLabel.setForeground(Color.white);
        } else {
            setBackground(Color.white);
            nameLabel.setForeground(Color.black);
        }
        return this;
    }
    
}
