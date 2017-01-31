/*
 * DataGroupComponent.java
 *
 * Created on July 19, 2002, 4:29 PM
 *
 * $Id: DataGroupComponent.java,v 1.21 2003/07/02 00:31:28 erichson Exp $
 *
 * $Log: DataGroupComponent.java,v $
 * Revision 1.21  2003/07/02 00:31:28  erichson
 * Changed to FloaterComponent and added method getFloaterType().
 * Added super() to constructor.
 *
 * Revision 1.20  2002/11/26 13:35:28  erichson
 * small update: loadIcon -> loadVisualizerIcon()
 *
 * Revision 1.19  2002/11/13 14:52:32  zachrisg
 * Changed the color of the selection button.
 *
 * Revision 1.18  2002/11/01 10:40:21  zachrisg
 * It is now possible to drag the selected elements from all selected groups.
 * Added control selection of elements in groups.
 *
 * Revision 1.17  2002/10/30 10:01:51  zachrisg
 * Added a button (actually a JLabel) for selecting examinations in the data groups and
 * to allow dragging only the selected examinations.
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import medview.visualizer.data.*;
import medview.visualizer.dnd.*;
import medview.visualizer.event.*;

/**
 * A component that displays the color and name of a data group.
 * To the right of the data group name is a button which can be
 * clicked to select the elements in the group, or used to drag
 * only the selected elements from the group.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class DataGroupComponent extends FloaterComponent implements ActionListener, DataGroupStateListener, DataGroupMemberListener {
   
    /** The parent panel containing this component. */
    private DataGroupPanel parent;
    
    /** The data group that the component represents. */
    private DataGroup group;

    /** True if the data group component is selected. */
    private boolean selected = false;

    /** The selection listeners. */
    private Vector selectionListeners;
    
    /** The panel displaying the color of the data group. */
    private JPanel groupColorPanel;
    
    /** The label displaying the name of the data group. */
    private JLabel groupNameLabel;

    /** The label displaying the selection tool. */
    private JLabel selectLabel;
    
    /** The mouse input listner responsible for detecting mouse events. */
    private MouseInputListener mouseInputListener;

    /** The popup menu that becomes visible after a right click on the group name label. */
    private JPopupMenu popupMenu;
    
    /** The new group menu item. */
    private JMenuItem newGroupMenuItem;
      
    /** The rename group menu item. */
    private JMenuItem renameGroupMenuItem;
    
    /** True if the component needs to be regenerated. */
    private boolean componentInvalid = true;
    
    /** 
     * Creates a new instance of DataGroupComponent.
     *
     * @param parent The parent panel containing this component.
     * @param dataGroup The data group to associate the component with.
     */
    public DataGroupComponent(DataGroupPanel parent, DataGroup dataGroup) {
        super();
        // save the parent
        this.parent = parent;
        
        // save the data group
        group = dataGroup;
        
        // initialize variables
        selectionListeners = new Vector();
        
        // create the color button
        groupColorPanel = new JPanel();
        groupColorPanel.setPreferredSize(new Dimension(20,10));
        groupColorPanel.setBackground(group.getColor());

        // create the name label
        groupNameLabel = new JLabel();
        setLabelText();
        
        // create and attach the data group component transferhandler
        TransferHandler th = new DataGroupComponentTransferHandler(this);
        this.setTransferHandler(th);
        groupNameLabel.setTransferHandler(th);
        groupColorPanel.setTransferHandler(th);
                
        // create the popup menu items
        newGroupMenuItem = new JMenuItem("New group...");
        newGroupMenuItem.addActionListener(this);
        renameGroupMenuItem = new JMenuItem("Change the group's name...");
        renameGroupMenuItem.addActionListener(this);

        // create the popupmenu
        popupMenu = new JPopupMenu();
        popupMenu.add(newGroupMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(renameGroupMenuItem);

        // create the selection tool
        try {
            ImageIcon icon = ApplicationManager.getInstance().loadVisualizerIcon("selectedElements16.png");
            selectLabel = new JLabel(icon); 
            selectLabel.setPreferredSize(new Dimension(24,24));
            selectLabel.setBorder(new BevelBorder(BevelBorder.RAISED));
        } catch(java.io.IOException e) {
            selectLabel = new JLabel("Select");
        }
        selectLabel.setOpaque(true);
        selectLabel.setToolTipText("Click to select examinations, drag to drag only the selected examinations.");                
        selectLabel.setTransferHandler(new DataGroupComponentSelectButtonTransferHandler(this));
        
        // create the panel with the color and the name label
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(groupColorPanel);
        leftPanel.add(groupNameLabel);
        
        // create the glue between the color-name-panel and the selection button
        JPanel glue = new JPanel();
        glue.setOpaque(false);
        
        // add the components to the main component
        setLayout(new BorderLayout());        
        add(leftPanel, BorderLayout.WEST);
        add(glue, BorderLayout.CENTER);
        add(selectLabel, BorderLayout.EAST);
                        
        // create the mouse input listener for the selection tool
        MouseInputAdapter selectionAdapter = new MouseInputAdapter() {
            /** True if a mousebutton has been pressed. */
            private boolean pressed = false;            
            /** True if the Control key was pressed while the mouse button was pressed. */
            private boolean controlDown = false;   
            /** True if the Shift key was pressed while the mouse button was pressed. */
            private boolean shiftDown = false;   
            /** True if the component needs to fire a SelectionEvent. */
            private boolean fireSelectionEventNeeded = false;    
            
            public void mousePressed(MouseEvent e) {
                fireSelectionEventNeeded = false;
                if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
                    // make the label look pressed down
                    selectLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
    
                    controlDown = e.isControlDown();
                    shiftDown = e.isShiftDown();
                    if (e.isControlDown()) {
                        setSelected(!selected);
                    } else {
                        if (!selected) {                        
                            setSelected(true);
                            fireSelectionChanged(controlDown, shiftDown);
                        } else {
                            fireSelectionEventNeeded = true;
                        }
                    }
                    pressed = true;
                }
            }    
            
            public void mouseDragged(MouseEvent e) {
                if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
                    if (pressed == true) {
                        // make the label look raised
                        selectLabel.setBorder(new BevelBorder(BevelBorder.RAISED));                                        
                        
                        // prevent further drag starts
                        pressed = false;
                        
                        // start the drag
                        selectLabel.getTransferHandler().exportAsDrag(selectLabel, e, TransferHandler.COPY);
                    }
                }
            }
            
            public void mouseClicked(MouseEvent e) {
                int modifiers = e.getModifiers();
                if ((modifiers & MouseEvent.BUTTON1_MASK) != 0) {                    
                    // raise the "button"
                    selectLabel.setBorder(new BevelBorder(BevelBorder.RAISED));

                    // select the elements in the datagroup
                    if (controlDown) {
                        DataManager.getInstance().selectDataGroup(group);
                    } else {
                        DataManager.getInstance().selectDataGroupExclusively(group);
                    }
                    ApplicationManager.getInstance().validateViews();                    
                    
                    if (fireSelectionEventNeeded) {
                        fireSelectionChanged(controlDown, shiftDown);
                        fireSelectionEventNeeded = false;
                    }
                } else if ((modifiers & MouseEvent.BUTTON3_MASK) != 0) {
                    if (e.getSource() instanceof Component) {
                        popupMenu.show((Component)e.getSource(), e.getPoint().x, e.getPoint().y);
                    }
                }
            }

            public void mouseExited(MouseEvent e) {
                if (pressed) {
                    // make the label look raised
                    selectLabel.setBorder(new BevelBorder(BevelBorder.RAISED));                
                    pressed = false;
                }
            }
            
        };


        
        
/*        MouseInputAdapter selectionAdapter = new MouseInputAdapter() {
            private boolean pressed = false;
            
            public void mousePressed(MouseEvent e) {
                selectLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
                pressed = true;
            }
            
            public void mouseClicked(MouseEvent e) {
                // select the elements in the datagroup
                DataManager.getInstance().selectDataGroup(group);
                ApplicationManager.getInstance().validateViews();
                
                // raise the "button"
                selectLabel.setBorder(new BevelBorder(BevelBorder.RAISED));
                pressed = false;
            }
            
            public void mouseDragged(MouseEvent e) {
                if (pressed) {
                    selectLabel.setBorder(new BevelBorder(BevelBorder.RAISED));                
                    pressed = false;
                    selectLabel.getTransferHandler().exportAsDrag(selectLabel, e, TransferHandler.COPY);
                }
            }
            
            public void mouseExited(MouseEvent e) {
                if (pressed) {
                    selectLabel.setBorder(new BevelBorder(BevelBorder.RAISED));                
                    pressed = false;
                }
            }
        };
*/        
        selectLabel.addMouseListener(selectionAdapter);
        selectLabel.addMouseMotionListener(selectionAdapter);        
        
        // create and attach the mouse input listener
        mouseInputListener = new MouseInputAdapter() {
            /** True if a mousebutton has been pressed. */
            private boolean pressed = false;            
            /** True if the Control key was pressed while the mouse button was pressed. */
            private boolean controlDown = false;   
            /** True if the Shift key was pressed while the mouse button was pressed. */
            private boolean shiftDown = false;   
            /** True if the component needs to fire a SelectionEvent. */
            private boolean fireSelectionEventNeeded = false;    
            
            public void mousePressed(MouseEvent e) {
                fireSelectionEventNeeded = false;
                if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
                    controlDown = e.isControlDown();
                    shiftDown = e.isShiftDown();
                    if (e.isControlDown()) {
                        setSelected(!selected);
                    } else {
                        if (!selected) {                        
                            setSelected(true);
                            fireSelectionChanged(controlDown, shiftDown);
                        } else {
                            fireSelectionEventNeeded = true;
                        }
                    }
                    pressed = true;
                }
            }    
            
            public void mouseDragged(MouseEvent e) {
                if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
                    if (pressed == true) {
                        // prevent further drag starts
                        pressed = false;
                        // start the drag
                        TransferHandler th = groupNameLabel.getTransferHandler();
                        th.exportAsDrag(groupNameLabel,e,TransferHandler.COPY);
                    }
                }
            }
            
            public void mouseClicked(MouseEvent e) {
                int modifiers = e.getModifiers();
                if ((modifiers & MouseEvent.BUTTON1_MASK) != 0) {
                    if (fireSelectionEventNeeded) {
                        fireSelectionChanged(controlDown, shiftDown);
                        fireSelectionEventNeeded = false;
                    }
                } else if ((modifiers & MouseEvent.BUTTON3_MASK) != 0) {
                    if (e.getSource() instanceof Component) {
                        popupMenu.show((Component)e.getSource(), e.getPoint().x, e.getPoint().y);
                    }
                }
            }
        };
        this.addMouseMotionListener(mouseInputListener);
        this.addMouseListener(mouseInputListener);
    
        // set the component not selected
        setSelected(false);
    }
    
    /**
     * Returns the data group represented by this component.
     *
     * @return The data group represented by this component.
     */
    public DataGroup getDataGroup() {
        return group;
    }    
    
    /**
     * Handles clicks on the colorbutton.
     *
     * @param event The event.
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == newGroupMenuItem) {            
            String name = TextInputDialog.showTextInputDialog(
                ApplicationFrame.getInstance(), 
                this.getLocationOnScreen(), 
                "New group", 
                "The group's name: ", 
                DataManager.getInstance().proposeDataGroupName());
            if (name == null) {
                return;
            } else if (DataManager.getInstance().existsDataGroupWithName(name) || name.equals("")) {
                return;
            }
            DataManager.getInstance().addDataGroup(new DataGroup(name, DataManager.getInstance().proposeDataGroupColor()));        
        } else if (event.getSource() == renameGroupMenuItem) {
            String name = TextInputDialog.showTextInputDialog(
                ApplicationFrame.getInstance(), 
                this.getLocationOnScreen(), 
                "Change the name of the group", 
                "The group's name: ", 
                group.getName());
            if (name == null) {
                return;
            } else if (DataManager.getInstance().existsDataGroupWithName(name) || name.equals("")) {
                return;
            }
            group.setName(name);
            DataManager.getInstance().validateViews();
        }
    }
    
    /**
     * Called when the color of a data group has changed.
     *
     * @param event The event.
     */
    public void dataGroupColorChanged(DataGroupEvent event) {
        groupColorPanel.setBackground(event.getDataGroup().getColor());        
    }
    
    /**
     * Called when the name of a data group has changed.
     *
     * @param event The event.
     */
    public void dataGroupNameChanged(DataGroupEvent event) {
        setLabelText();
    }

    /**
     * Sets the text of the label.
     */
    private void setLabelText() {
        String count = " (" + group.getSelectedMemberCount() + "/" + group.getMemberCount() + ")";
        groupNameLabel.setText(group.getName() + count);
        // make sure the parent gets resized
        setSize(getPreferredSize());
        componentInvalid = false;
    }
    
    /** 
     * Called when the member count or selected member count of the data group has changed.
     *
     * @param event The event.
     */
    public void dataGroupMemberCountChanged(DataGroupEvent event) {
        componentInvalid = true;
    }
    
    /**
     * Sets the selection of the data group component.
     *
     * @param selected True if the data group should be selected.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            this.setBackground(Color.black);
            groupNameLabel.setForeground(Color.white);
        } else {
            this.setBackground(Color.white);
            groupNameLabel.setForeground(Color.black);
        }
    }
    
    /**
     * Simulates a click on the component. Fires a SelectionEvent.
     */
     public void doClick() {
         setSelected(true);
         fireSelectionChanged(false,false);
     }
    
    /**
     * Returns true if the data group component is selected.
     *
     * @return True if the data group component is selected.
     */
     public boolean isSelected() {
         return selected;
     }
    
    /** 
     * Adds a selection listener.
     *
     * @param listener The selection listener to add.
     */
    public void addSelectionListener(SelectionListener listener) {
        if (!selectionListeners.contains(listener)) {
            selectionListeners.add(listener);
        }
    }
    
    /**
     * Removes a selection listener.
     *
     * @param listener The selection listener to remove.
     */
    public void removeSelectionListener(SelectionListener listener) {
        selectionListeners.remove(listener);
    }
    
    /**
     * Notifies the selection listeners that the selection has changed.
     *
     * @param controlDown True if Control was pressed when the selection changed.
     * @param shiftDown True if Shift was pressed when the selection changed.
     */
    private void fireSelectionChanged(boolean controlDown, boolean shiftDown) {
        for (Iterator i = selectionListeners.iterator(); i.hasNext(); ) {
            SelectionListener listener = (SelectionListener) i.next();
            listener.selectionChanged(new SelectionEvent(this,controlDown,shiftDown));
        }
    }

    /**
     * Returns the parent panel.
     *
     * @return The parent panel.
     */
     public DataGroupPanel getParentDataGroupPanel() {
         return parent;
     }

     /**
      * Makes sure that the data shown in the component is up to date.
      */
     public void validateComponent() {
         if (componentInvalid) {
             setLabelText();
         }
     }
     
     public int getFloaterType() {
         return Floater.FLOATER_TYPE_DATAGROUPS;
     }
     
}
