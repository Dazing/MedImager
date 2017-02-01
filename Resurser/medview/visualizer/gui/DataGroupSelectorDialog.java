/*
 * DataGroupSelectorDialog.java
 *
 * Created on August 6, 2002, 3:00 PM
 *
 * $Id: DataGroupSelectorDialog.java,v 1.7 2004/07/23 13:27:20 erichson Exp $
 *
 * $Log: DataGroupSelectorDialog.java,v $
 * Revision 1.7  2004/07/23 13:27:20  erichson
 * Ok --> OK (BZ #137)
 *
 * Revision 1.6  2002/10/29 10:06:41  zachrisg
 * Changed so that only a single group can be selected in the list.
 * Changed language to english.
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import medview.visualizer.data.*;
import medview.visualizer.event.*;

/**
 * A class for displaying a dialog allowing the user to choose a data group.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class DataGroupSelectorDialog extends JDialog implements ActionListener, DataGroupListener, DataGroupStateListener, ListSelectionListener {

    /** The DataGroup to return. */
    private DataGroup dataGroup = null;
    
    /** The list of datagroups. */
    private JList groupList;
    
    /** The ok button. */
    private JButton okButton;
    
    /** The cancel button. */
    private JButton cancelButton;
    
    /** The new button. */
    private JButton newButton;
    
    /** The remove button. */
    private JButton removeButton;
    
    /** The rename button. */
    private JButton renameButton;

    /** The change color button. */
    private JButton colorButton;
    
    /** The datagroups. */
    private DataGroup[] dataGroups;
    
    /** A self reference. */
    private DataGroupSelectorDialog selfReference;
    
    /** 
     * Creates a modal DataGroupSelectorDialog. 
     *
     * @param owner The owner frame of the dialog.
     * @param title The title of the dialog.
     */
    public DataGroupSelectorDialog(Frame owner, String title) {
        super(owner, title, true);
        selfReference = this;        
        
        // create the data group list
        groupList = new JList();
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupList.setBorder(new BevelBorder(BevelBorder.LOWERED));
        groupList.setCellRenderer(new DataGroupListCellRenderer());
        generateDataGroupList();
        
        // add the data group list to a panel
        JPanel groupListPanel = new JPanel(new BorderLayout());
        groupListPanel.setBorder(new TitledBorder("Datagroups"));
        groupListPanel.add(new JScrollPane(groupList), BorderLayout.CENTER);
        
        // create the new, remove, rename and change color buttons
        newButton = new JButton("New datagroup");
        removeButton = new JButton("Remove");
        renameButton = new JButton("Change name");
        colorButton = new JButton("Change color");
        
        // add actionlisteners to the buttons
        newButton.addActionListener(this);
        removeButton.addActionListener(this);
        renameButton.addActionListener(this);
        colorButton.addActionListener(this);
        
        // add the buttons to a panel
        JPanel eastPanel = new JPanel(new BorderLayout());
        JPanel eastInnerPanel = new JPanel(new GridLayout(4,1));
        JPanel newButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel removeButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel renameButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel colorButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        newButtonPanel.add(newButton);
        removeButtonPanel.add(removeButton);
        renameButtonPanel.add(renameButton);
        colorButtonPanel.add(colorButton);
        eastInnerPanel.add(newButtonPanel);
        eastInnerPanel.add(removeButtonPanel);
        eastInnerPanel.add(renameButtonPanel);
        eastInnerPanel.add(colorButtonPanel);
        eastPanel.add(eastInnerPanel, BorderLayout.NORTH);
        
        // create the ok and cancel buttons
        okButton = new JButton("OK");
        okButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        
        // add the buttons to a panel
        JPanel southPanel = new JPanel(new FlowLayout());
        southPanel.add(okButton);
        southPanel.add(cancelButton);
        
        // add the panels to the dialog
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(groupListPanel,BorderLayout.CENTER);
        getContentPane().add(eastPanel,BorderLayout.EAST);
        getContentPane().add(southPanel,BorderLayout.SOUTH);

        // set the default button
        getRootPane().setDefaultButton(okButton);
    
        // add listeners
        groupList.addListSelectionListener(this);
        DataManager.getInstance().addDataGroupListener(this);

        // select the first group or if list is empty, disable some buttons
        if (dataGroups.length > 0) {
            groupList.setSelectedIndex(0);
        } else {
            removeButton.setEnabled(false);
            renameButton.setEnabled(false);
            colorButton.setEnabled(false);
            okButton.setEnabled(false);
        }        
        
        // make sure to remove all listeners when closing
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                groupList.removeListSelectionListener(selfReference);
                DataManager.getInstance().removeDataGroupListener(selfReference);
                removeDataGroupStateListeners();
            }
        });
        
        // set the right size of the dialog
        pack();
        Dimension size = getSize();
        if (size.height < 300) {
            size.height = 300;
        }
        if (size.width < 300) {
            size.width = 300;
        }
        setSize(size);
    }
        
    /**
     * Handles action events.
     *
     * @param event The event to handle.
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == okButton) {
            // set the return value
            dataGroup = (DataGroup) groupList.getSelectedValue();
            // clean up
            groupList.removeListSelectionListener(this);
            DataManager.getInstance().removeDataGroupListener(this);
            removeDataGroupStateListeners();            
            // hide the dialog
            setVisible(false);
        } else if (event.getSource() == cancelButton) {
            // set the return value
            dataGroup = null;
            // clean up
            groupList.removeListSelectionListener(this);
            DataManager.getInstance().removeDataGroupListener(this);
            removeDataGroupStateListeners();
            // hide the dialog
            setVisible(false);
        } else if (event.getSource() == newButton) {
            String name = TextInputDialog.showTextInputDialog(
                ApplicationFrame.getInstance(), 
                this.getLocationOnScreen(), 
                "New datagroup", 
                "The datagroup's name: ", 
                DataManager.getInstance().proposeDataGroupName());
            if (name == null) {
                return;
            } else if (DataManager.getInstance().existsDataGroupWithName(name) || name.equals("")) {
                return;
            }
            DataManager.getInstance().addDataGroup(new DataGroup(name, DataManager.getInstance().proposeDataGroupColor()));        
            
        } else if (event.getSource() == removeButton) {
            DataGroup group = (DataGroup) groupList.getSelectedValue();
            if (group != null) {
                DataManager.getInstance().removeDataGroup(group);
                DataManager.getInstance().validateViews();
            }
        } else if (event.getSource() == renameButton) {
            DataGroup group = (DataGroup) groupList.getSelectedValue();
            if (group != null) {
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
        } else if (event.getSource() == colorButton) {
            DataGroup group = (DataGroup) groupList.getSelectedValue();
            if (group != null) {
                Color color = JColorChooser.showDialog(this,"Choose color",group.getColor());
                if (color != null) {
                    group.setColor(color);
                    DataManager.getInstance().validateViews();
                }
            }
        }
    }
    
    /**
     * Displays a modal datagroup selector dialog.
     *
     * @param owner The owner frame of the dialog.
     * @param location The location on screen to display the dialog.
     * @param title The title of the dialog.
     *
     * @return The selected data group if "ok" is pressed, null if "cancel" or the close button is pressed.
     */
    public static DataGroup showDataGroupSelectorDialog(Frame owner, Point location, String title) {
        DataGroupSelectorDialog dialog = new DataGroupSelectorDialog(owner,title);
        dialog.setLocation(location);
        dialog.setVisible(true);
        return dialog.dataGroup;
    }
     
    /**
     * Generates the list of data groups.
     */
    private void generateDataGroupList() {
        removeDataGroupStateListeners();
        dataGroups = DataManager.getInstance().getDataGroups();
        groupList.setListData(dataGroups);
        addDataGroupStateListeners();
    }

    /**
     * Removes all datagroup state listeners.
     */
    private void removeDataGroupStateListeners() {
        if (dataGroups != null) {
            for (int i = 0; i < dataGroups.length; i++) {
                dataGroups[i].removeDataGroupStateListener(this);
            }
        }
    }
    
    /**
     * Adds datagroup state listeners.
     */
    private void addDataGroupStateListeners() {
        if (dataGroups != null) {
            for (int i = 0; i < dataGroups.length; i++) {
                dataGroups[i].addDataGroupStateListener(this);
            }
        }
    }    
    
    /** 
     * Called when a data group was added.
     *
     * @param event An event containing a reference to the added group.
     */
    public void dataGroupAdded(DataGroupEvent event) {
        generateDataGroupList();
        groupList.setSelectedValue(event.getDataGroup(), true);
    }
    
    /** 
     * Called when the color of a data group has changed.
     *
     * @param event The event.
     */
    public void dataGroupColorChanged(DataGroupEvent event) {
        groupList.repaint();
    }
    
    /** 
     * Called when the member count or selected member count of the data group has changed.
     *
     * @param event The event.
     */
    public void dataGroupMemberCountChanged(DataGroupEvent event) {}
    
    /** 
     * Called when the name of a data group has changed.
     *
     * @param event The event.
     */
    public void dataGroupNameChanged(DataGroupEvent event) {
        groupList.repaint();
    }
    
    /** 
     * Called when a data group was removed.
     *
     * @param event An event containing a reference to the removed group.
     */
    public void dataGroupRemoved(DataGroupEvent event) {
        generateDataGroupList();
    }
    
    /**
     * Process selection changes in the data group list.
     *
     * @param event The object describing the event.
     */
    public void valueChanged(ListSelectionEvent event) {
        if (groupList.isSelectionEmpty()) {
            removeButton.setEnabled(false);
            renameButton.setEnabled(false);
            colorButton.setEnabled(false);
            okButton.setEnabled(false);
        } else {
            removeButton.setEnabled(true);
            renameButton.setEnabled(true);
            colorButton.setEnabled(true);
            okButton.setEnabled(true);
        }
    }
    
}
