/*
 * DataGroupPanel.java
 *
 * Created on July 19, 2002, 4:46 PM
 *
 * $Id: DataGroupPanel.java,v 1.22 2004/11/13 11:00:09 erichson Exp $
 *
 * $Log: DataGroupPanel.java,v $
 * Revision 1.22  2004/11/13 11:00:09  erichson
 * Thread naming
 *
 * Revision 1.21  2003/07/02 00:26:46  erichson
 * Changed to FloaterComponent and added method getFloaterType().
 * Added super() to constructor.
 *
 * Revision 1.20  2002/12/11 13:40:21  zachrisg
 * Moved the removal of datagroups to a new thread to free up the gui thread.
 *
 * Revision 1.19  2002/11/26 13:43:44  erichson
 * loadIcon -> loadVisualizerIcon()
 *
 * Revision 1.18  2002/10/29 09:59:04  zachrisg
 * Changed language to english and cleaned up javadoc.
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import medview.visualizer.data.*;
import medview.visualizer.event.*;
import medview.visualizer.dnd.*;

/**
 * A panel containing DataGroupComponents and buttons to add, remove and change color of the data groups. 
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class DataGroupPanel extends FloaterComponent implements DataGroupListener, ActionListener, SelectionListener {
    
    /** A vector with the DataGroupComponents. */
    private Vector dataGroupComponents;
    
    /** A component listener for component resizes. */
    private ComponentListener componentListener;
    
    /** The data group panel. */
    private JPanel groupPanel;
    
    /** The toolbar. */
    private JToolBar toolBar;

    /** The new group button. */
    private JButton newButton;
    
    /** The remove group button. */
    private JButton removeButton;
    
    /** The change color button. */
    private JButton colorButton;
    
    /** Creates a new instance of DataGroupPanel */
    public DataGroupPanel(DataGroup[] dataGroups) {
        super();
        // initialize variables
        dataGroupComponents = new Vector();
    
        // create a component listener
        componentListener = new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setSize(getPreferredSize());
            }};        
            
        // add the data groups
        for (int i = 0; i < dataGroups.length; i++) {            
            DataGroupComponent component = new DataGroupComponent(this, dataGroups[i]);
            dataGroups[i].addDataGroupStateListener(component);
            dataGroups[i].addDataGroupMemberListener(component);
            dataGroupComponents.add(component);            
            component.addSelectionListener(this);
            component.addComponentListener(componentListener);
        }
                    
        // create the data group panel
        groupPanel = new JPanel(new GridLayout(dataGroupComponents.size(),1));
        groupPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        
        // create the toolbar buttons
        try {
            ImageIcon newIcon = ApplicationManager.getInstance().loadVisualizerIcon("new16.gif");
            newButton = new JButton(newIcon);                        
        } catch(IOException e) {
            newButton = new JButton("New");
        }
        try {
            ImageIcon trashIcon = ApplicationManager.getInstance().loadVisualizerIcon("trash16.gif");
            removeButton = new JButton(trashIcon);                        
        } catch(IOException e) {
            removeButton = new JButton("Remove");
        }
        try {
            ImageIcon paletteIcon = ApplicationManager.getInstance().loadVisualizerIcon("palette16.gif");
            colorButton = new JButton(paletteIcon);                        
        } catch(IOException e) {
            colorButton = new JButton("Color");
        }
        newButton.addActionListener(this);
        newButton.setTransferHandler(new DataGroupNewButtonTransferHandler(this));
        removeButton.addActionListener(this);
        removeButton.setTransferHandler(new DataGroupTrashcanTransferHandler());
        colorButton.addActionListener(this);
        
        newButton.setToolTipText("New group (you can also drag examinations here).");
        removeButton.setToolTipText("Remove group (you can also drag groups here).");
        colorButton.setToolTipText("Change the color of the selected group.");
        
        // create the toolbar
        toolBar = new JToolBar(JToolBar.HORIZONTAL);        
        toolBar.setFloatable(false);
        toolBar.add(newButton);
        toolBar.add(removeButton);        
        toolBar.add(colorButton);
        
        // add components to the panel
        this.setLayout(new BorderLayout());
        this.add(toolBar, BorderLayout.NORTH);
        this.add(groupPanel, BorderLayout.CENTER);
        
        generatePanel();
    }

    /**
     * Regenerates the panel.
     */
    private void generatePanel() {
        groupPanel.removeAll();
        for (Iterator i = dataGroupComponents.iterator(); i.hasNext(); ) {
            DataGroupComponent component = (DataGroupComponent) i.next();
            groupPanel.add(component);
        }
        this.setSize(getPreferredSize());
    }

    /**
     * Called when a data group was added. Implementation of the DataGroupListener interface.
     *
     * @param event An event containing a reference to the added group.
     */
    public void dataGroupAdded(DataGroupEvent event) {
        ApplicationManager.debug("DataGroupPanel: data group added");
        DataGroup dataGroup = event.getDataGroup();
        boolean exists = false;
        for (Iterator i = dataGroupComponents.iterator(); i.hasNext(); ) {
            DataGroupComponent component = (DataGroupComponent) i.next();
            if (component.getDataGroup() == dataGroup) {
                exists = true;
            }
        }
        if (!exists) {
            DataGroupComponent component = new DataGroupComponent(this, dataGroup);
            dataGroup.addDataGroupStateListener(component);
            dataGroup.addDataGroupMemberListener(component);
            dataGroupComponents.add(component);
            component.addSelectionListener(this);
            component.addComponentListener(componentListener);
            generatePanel();
            component.doClick();
        }
    }
    
    /**
     * Called when a data group was removed. Implementation of the DataGroupListener interface.
     *
     * @param event An event containing a reference to the removed group.
     */
    public void dataGroupRemoved(DataGroupEvent event) {
        ApplicationManager.debug("DataGroupPanel: data group removed");
        DataGroup dataGroup = event.getDataGroup();
        for (Iterator i = dataGroupComponents.iterator(); i.hasNext(); ) {
            DataGroupComponent component = (DataGroupComponent) i.next();
            if (component.getDataGroup() == dataGroup) {
                dataGroup.removeDataGroupStateListener(component);
                dataGroup.removeDataGroupMemberListener(component);
                i.remove();
                component.removeSelectionListener(this);
                component.removeComponentListener(componentListener);
            }
        }    
        generatePanel();
    }
    
    /**
     * For test purposes only.
     *
     * @param args The arguments.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        DataGroupPanel panel = new DataGroupPanel(new DataGroup[0]);
        frame.getContentPane().add(panel);
        DataManager.getInstance().addDataGroupListener(panel);
        DataManager.getInstance().addDataGroup(new DataGroup("GBG",Color.red));
        DataManager.getInstance().addDataGroup(new DataGroup("Stockholm",Color.blue));
        DataManager.getInstance().addDataGroup(new DataGroup("Avesta",Color.green));        
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Called when a button has been clicked.
     *
     * @param event The event.
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == newButton) {
            newDataGroup();
        } else if (event.getSource() == removeButton) {
            Vector selectedGroups = new Vector();
            for (Iterator i = dataGroupComponents.iterator(); i.hasNext(); ) {
                DataGroupComponent component = (DataGroupComponent) i.next();
                if (component.isSelected()) {
                    selectedGroups.add(component.getDataGroup());
                }
            }
            (new RemoveThread((DataGroup[])selectedGroups.toArray(new DataGroup[selectedGroups.size()]))).start();
        } else if (event.getSource() == colorButton) {
            Vector selectedComponents = new Vector();
            for (Iterator i = dataGroupComponents.iterator(); i.hasNext(); ) {
                DataGroupComponent component = (DataGroupComponent) i.next();
                if (component.isSelected()) {
                    selectedComponents.add(component);
                }
            }
            if (selectedComponents.size() > 0) {
                Color startColor = ( (DataGroupComponent)selectedComponents.firstElement() ).getDataGroup().getColor();
                Color color = JColorChooser.showDialog(this, "Choose color", startColor);
                if (color != null) {
                    for (Iterator i = selectedComponents.iterator(); i.hasNext(); ) {
                        DataGroupComponent component = (DataGroupComponent) i.next();
                        component.getDataGroup().setColor(color);
                    }
                    DataManager.getInstance().validateViews();
                }
            }
        }
    }

    /**
     * Creates a new DataGroup and adds it to the DataManger.
     *
     * @return The new data group i created, null otherwise.
     */
    public DataGroup newDataGroup() {
        String name = TextInputDialog.showTextInputDialog(
            ApplicationFrame.getInstance(), 
            this.getLocationOnScreen(), 
            "New group", 
            "The group's name: ", 
            DataManager.getInstance().proposeDataGroupName());
        if (name == null) {
            return null;
        } else if (DataManager.getInstance().existsDataGroupWithName(name) || name.equals("")) {
            return null;
        }
        DataGroup dataGroup = new DataGroup(name, DataManager.getInstance().proposeDataGroupColor());
        DataManager.getInstance().addDataGroup(dataGroup);
        return dataGroup;
    }
    
    /**
     * Called when the selection of a DataGroupComponent has changed.
     *
     * @param e The event.
     */
    public void selectionChanged(SelectionEvent e) {
        DataGroupComponent changedComponent = (DataGroupComponent) e.getSource();
        if (changedComponent.isSelected()) {
            if (!e.isControlDown()) {
                // deselect all other components
                for (Iterator i = dataGroupComponents.iterator(); i.hasNext(); ) {
                    DataGroupComponent component = (DataGroupComponent) i.next();
                    if (!(component == changedComponent)) {
                        component.setSelected(false);
                    }
                }
            }
        }
    }
    
    /**
     * Returns all selected components.
     *
     * @return All selected components.
     */
    public DataGroupComponent[] getSelectedDataGroupComponents() {
        Vector selectedComponents = new Vector();
        for (Iterator i = dataGroupComponents.iterator(); i.hasNext(); ) {
            DataGroupComponent component = (DataGroupComponent) i.next();
            if (component.isSelected()) {
                selectedComponents.add(component);
            }
        }
        DataGroupComponent[] array = new DataGroupComponent[selectedComponents.size()];
        return (DataGroupComponent[]) selectedComponents.toArray(array);        
    }
    
    /**
     * Makes sure that the information displayed in the panel is up to date.
     */
    public void validatePanel() {
        for (Iterator i = dataGroupComponents.iterator(); i.hasNext(); ) {
            DataGroupComponent component = (DataGroupComponent) i.next();
            component.validateComponent();
        }
    }
    
    private class RemoveThread extends Thread {
        private DataGroup[] dataGroups;
        
        public RemoveThread(DataGroup[] dataGroups) 
        {
            super("DataGroupPanel-RemoveThread");
            this.dataGroups = dataGroups;
        }
        
        public void run() {
            for (int i = 0; i < dataGroups.length; i++) {
                DataManager.getInstance().removeDataGroup(dataGroups[i]);
            }
            DataManager.getInstance().validateViews();
        }
    }
    
    public int getFloaterType() {
        return Floater.FLOATER_TYPE_DATAGROUPS;
    }
}
