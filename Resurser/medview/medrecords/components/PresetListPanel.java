/*
 * $Id: PresetListPanel.java,v 1.36 2010/07/01 08:05:32 oloft Exp $
 *
 * Created on June 14, 2001, 10:40 PM
 *
 * $Log: PresetListPanel.java,v $
 * Revision 1.36  2010/07/01 08:05:32  oloft
 * Remembers divider location
 *
 * Revision 1.35  2010/06/28 07:12:39  oloft
 * MR 4.5 dev
 *
 * Revision 1.34  2007/10/17 15:17:03  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.33  2005/01/30 15:19:20  lindahlf
 * T4 Integration
 *
 * Revision 1.32  2004/12/08 14:42:52  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.31  2004/11/04 12:04:45  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.30  2003/12/21 21:54:12  oloft
 * Changed settings, removed DataHandlingHandler
 *
 * Revision 1.29  2003/11/11 13:47:18  oloft
 * Switching mainbranch
 *
 * Revision 1.28.2.3  2003/10/13 05:51:54  oloft
 * Added addFirstValue for completion tasks
 *
 * Revision 1.28.2.2  2003/10/08 18:33:37  oloft
 * localization
 *
 * Revision 1.28.2.1  2003/08/07 00:22:56  erichson
 * Added import statement for components.inputs.*
 *
 * Revision 1.28  2003/07/23 14:21:38  erichson
 * Tried to disable the preset lists from gaining focus (which didn't really make a difference)
 *
 * Revision 1.27  2003/07/22 16:50:23  erichson
 * Selection is now always cleared after a left mouse button click.
 *
 * Revision 1.26  2003/07/18 09:43:04  erichson
 * Tog bort "ta bort från lista"-knappen
 *
 * Revision 1.25  2003/07/16 07:54:24  erichson
 * Fixed Bugzilla bug 0005: exception that was thrown when clicking on empty lists
 *
 * Revision 1.24  2003/06/10 13:37:30  erichson
 * Added popup panel when right clicking instead of asking directly whether to remove
 *
 * Revision 1.23  2003/06/06 16:50:16  erichson
 * Undid olof's (accidental) changes (committed as 1.22)
 *
 * Revision 1.21  2003/06/06 13:31:32  erichson
 * Disabled selection in the lists.
 *
 * Revision 1.20  2003/06/06 11:38:12  erichson
 * Removed a debug println which i accidentally left in in last check-in. Also removed empty mouseListener methods.
 *
 * Revision 1.19  2003/06/06 11:18:07  erichson
 * Some changes to avoid "missed" value clicks
 *
 * Revision 1.18  2003/06/05 19:03:56  erichson
 * Fixed right-clicking in value lists
 *
 */
package medview.medrecords.components;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import medview.datahandling.*;
import medview.common.dialogs.*;

import medview.medrecords.components.inputs.*;
import medview.medrecords.models.*;
import medview.medrecords.data.PreferencesModel;

public class PresetListPanel extends JPanel implements ListSelectionListener, ChangeListener, FocusListener {
    // MEMBERS

    private ActionListener actionListener;
    private PresetModel currentPresetModel;
    private PresetModel nextPresetModel;
    private MedViewDataHandler mVDH = MedViewDataHandler.instance();
    private PreferencesModel prefs = PreferencesModel.instance();
    private javax.swing.JLabel currentPresetLabel;
    private javax.swing.JPanel currentPresetPanel;
    private javax.swing.JList currentValueList;
    private javax.swing.JScrollPane upperScrollPane;
    private javax.swing.JScrollPane lowerScrollPane;
    private javax.swing.JLabel nextPresetLabel;
    private javax.swing.JPanel nextPresetPanel;
    private javax.swing.JList nextValueList;
    private javax.swing.JSplitPane valueSplitPane;
    private HashMap currentTermValuesMap;
    private HashMap nextTermValuesMap;

    // CONSTRUCTORS
    /**
     * Creates new form ValuePanel.
     */
    public PresetListPanel() {
        super();

        initComponents();

        setRequestFocusEnabled(false);

        currentPresetModel = new PresetModel("Current");

        nextPresetModel = new PresetModel("Next");

        currentTermValuesMap = new HashMap();

        nextTermValuesMap = new HashMap();

        currentPresetModel.addChangeListener(this); // nader 12/4

        nextPresetModel.addChangeListener(this); // nader 12/4

        currentValueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        nextValueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        currentValueList.addListSelectionListener(this);

        nextValueList.addListSelectionListener(this);

        currentValueList.addFocusListener(this);


        if (prefs.isPresetDividerLocationSet()) {
            valueSplitPane.setResizeWeight(0.0);

            valueSplitPane.setDividerLocation(prefs.getPresetDividerLocation());
        } else {
            valueSplitPane.setResizeWeight(0.5);
        }

        // Borrowed from www.java2s.com
        // Listen to changes of the divider and store location to prefs
        PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent changeEvent) {

                JSplitPane sourceSplitPane = (JSplitPane) changeEvent.getSource();

                String propertyName = changeEvent.getPropertyName();

                if (propertyName.equals(JSplitPane.LAST_DIVIDER_LOCATION_PROPERTY)) {
                    int current = sourceSplitPane.getDividerLocation();

                    //System.out.println(">> Set location to prefs: " + current + ", " + prefs.getPresetDividerLocation());

                    prefs.setPresetDividerLocation(current); // current
                }
            }
        };

        valueSplitPane.addPropertyChangeListener(propertyChangeListener);


    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        // sub components

        valueSplitPane = new javax.swing.JSplitPane();

        upperScrollPane = new javax.swing.JScrollPane();

        currentPresetPanel = new javax.swing.JPanel();

        currentPresetLabel = new javax.swing.JLabel();

        currentValueList = new javax.swing.JList();

        lowerScrollPane = new javax.swing.JScrollPane();

        nextPresetPanel = new javax.swing.JPanel();

        nextPresetLabel = new javax.swing.JLabel();

        nextValueList = new javax.swing.JList();

        // layout

        setLayout(new java.awt.BorderLayout());

        // size and autoscrolling

        setMinimumSize(new java.awt.Dimension(175, 175));

        setPreferredSize(new java.awt.Dimension(175, 175));

        setAutoscrolls(true);

        // value split pane (top bottom)

        valueSplitPane.setBorder(null);

        valueSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        // valueSplitPane.setResizeWeight(0.5);

        valueSplitPane.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                keyPressedOnLists(evt);
            }
        });

        // upper list (current)

        upperScrollPane.setBorder(null);

        //upperScrollPane.setPreferredSize(new Dimension(0,0)); // this forces the divider location to be set to resize weight

        currentPresetPanel.setLayout(new java.awt.BorderLayout());

        currentPresetLabel.setText("Current preset");

        currentPresetLabel.setAlignmentX(0.5F);

        currentPresetPanel.add(currentPresetLabel, java.awt.BorderLayout.NORTH);

        currentValueList.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));

        currentValueList.setRequestFocusEnabled(false);

        currentValueList.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pressOnUpperList(evt);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mouseReleasedOnList(evt);
            }
        });

        currentValueList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                listMouseDragged(evt);
            }
        });

        currentPresetPanel.add(currentValueList, java.awt.BorderLayout.CENTER);

        upperScrollPane.setViewportView(currentPresetPanel);

        valueSplitPane.setTopComponent(upperScrollPane);

        // bottom list (next)

        lowerScrollPane.setBorder(null);

        // lowerScrollPane.setPreferredSize(new Dimension(0,0)); // this forces the divider location to be set to resize weight

        nextPresetPanel.setLayout(new java.awt.BorderLayout());

        nextPresetLabel.setText("Next preset");

        nextPresetPanel.add(nextPresetLabel, java.awt.BorderLayout.NORTH);

        nextValueList.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));

        nextValueList.setRequestFocusEnabled(false);

        nextValueList.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pressOnUpperList(evt);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mouseReleasedOnList(evt);
            }
        });

        nextValueList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {

            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                listMouseDragged(evt);
            }
        });

        nextPresetPanel.add(nextValueList, java.awt.BorderLayout.CENTER);

        lowerScrollPane.setViewportView(nextPresetPanel);

        lowerScrollPane.setMinimumSize(new Dimension(0, 0)); // To make it possible to hide this completely

        valueSplitPane.setRightComponent(lowerScrollPane);

        // add total split pane to main panel

        add(valueSplitPane, java.awt.BorderLayout.CENTER);
    }


    public void focusGained(java.awt.event.FocusEvent p1) {
    }

    public void focusLost(java.awt.event.FocusEvent ev) {
    }

    public void addActionListener(ActionListener newListener) {
        actionListener = AWTEventMulticaster.add(actionListener, newListener);
    }

    public void fireActionEvent(String value) {
        if (actionListener != null) {
            actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, value));
        }
    }

    private void mouseReleasedOnList(java.awt.event.MouseEvent evt) {
        JList aList = (JList) evt.getSource();

        Point p = evt.getPoint();

        if (evt.isPopupTrigger()) {
            new DeleteTermPopupMenu(aList, p).showMe();
        }
    }

    /**
     * When mouse is dragged (with button pushed down) on a list, selection
     * should not occur, so we consume the drag event and clear the selection.
     */
    private void listMouseDragged(java.awt.event.MouseEvent evt) {
        evt.consume(); // Stop this from happening

        Object source = evt.getSource();

        if (source instanceof JList) {
            ((JList) source).clearSelection();
        }
    }

    private void pressOnUpperList(java.awt.event.MouseEvent evt) {
        mouseClickOnList(evt);
    }

    private void keyPressedOnLists(java.awt.event.KeyEvent evt) {
    }

    /**
     * Call this method to detach the panel as a listener from
     * everything it listens to.
     */
    public void detachAsListener() {
        if (currentPresetModel != null) // Fredrik 041207
        {
            currentPresetModel.removeChangeListener(this);
        }

        if (nextPresetModel != null) // Fredrik 041207
        {
            nextPresetModel.removeChangeListener(this);
        }
    }

    /**
     * Sets the new Mask. The Mask is used to determine which
     * presets are visible. Only presets that begin with the
     * mask are shown.
     */
    public void setMask(String in_mask) {
        String mask = new String(in_mask);


        String[] currentPresets = currentPresetModel.getPresets(); // To handle them as an array...

        //The following paragraph is a hack because of a bug I haven't been able to find yet.

        if (mask.startsWith("CURRENT_ROW")) {
            mask = "";
        }

        // This only affects the "current" jlist

        Vector unmasked = new Vector();

        int masklength = mask.length();

        /* implement: adjust this part for ? values later */

        currentTermValuesMap.clear();
        for (int i = 0; i < currentPresets.length; i++) {
            //for values containing || we should only show the last part
            //but save the first part as the value to save in the treefile
            String[] temp;
            temp = currentPresets[i].split("\\|\\|");
            String strValue;
            if (temp.length == 2) {
                strValue = temp[1];
                //add the value to a hashMap
                currentTermValuesMap.put(temp[1], temp[0]);
            } else {
                strValue = temp[0];
            }

            if (masklength == 0) // If mask is zero length, always add
            {
                unmasked.add(strValue);
            } else if (masklength <= (strValue.length())) // If masklength is longer, don't consider it
            {
                // Check if the first part matches

                String firstPart = strValue.substring(0, masklength);

                if (mask.equalsIgnoreCase(firstPart)) {
                    unmasked.add(strValue);
                }
            }
        }

        currentValueList.setListData(unmasked);
    }

    private void setPresets(PresetModel presetModel) {
        if (currentPresetModel != null) // Fredrik 041207
        {
            currentPresetModel.removeChangeListener(this);
        }

        if (presetModel == null) {
            currentPresetModel = new PresetModel("-"); // Empty JList since there are no presets
        } else {
            currentPresetModel = presetModel;
        }

        currentPresetModel.addChangeListener(this);

        currentPresetLabel.setText(currentPresetModel.getTitle());

        setMask(""); // Update presetlist contents

        revalidate();
    }

    private void setNextPresets(PresetModel in_nextPresetModel) {
        if (nextPresetModel != null) // Fredrik 041207
        {
            nextPresetModel.removeChangeListener(this);
        }

        if (in_nextPresetModel == null) {
            nextPresetModel = new PresetModel("-");
        } else {
            nextPresetModel = in_nextPresetModel;
        }

        nextPresetModel.addChangeListener(this);

        nextPresetLabel.setText(nextPresetModel.getTitle());

        //nextValueList.setListData(nextPresetModel.getPresets());

        String[] nextPresets = nextPresetModel.getPresets(); // To handle them as an array...
        Vector stripped = new Vector();
        nextTermValuesMap.clear();
        for (int i = 0; i < nextPresets.length; i++) {
            //for values containing || we should only show the last part
            //but save the first part as the value to save in the treefile
            String[] temp;
            temp = nextPresets[i].split("\\|\\|");
            String strValue;
            if (temp.length == 2) {
                strValue = temp[1];
                //add the value to a hashMap
                nextTermValuesMap.put(temp[1], temp[0]);
            } else {
                strValue = temp[0];
            }
            stripped.add(strValue);
        }
        nextValueList.setListData(stripped);



    }

    public void stateChanged(javax.swing.event.ChangeEvent e) {
        updatePresets(e.getSource());
    }

    public void updatePresets(Object source) {
        if (source instanceof ValueTabbedPane) {
            // This is executed when the tab is changed in ValueTabbedPane

            ValueTabbedPane sourceTabbedPane = (ValueTabbedPane) source;

            TabPanel activeTab = sourceTabbedPane.getSelectedTab();

            if (activeTab == null) {
                setPresets(null); // No presets
            } else {
                ValueInputComponent activeComponent = activeTab.getSelectedInputComponent();

                if (activeComponent == null) {
                    setPresets(new PresetModel(mVDH.getLanguageString(
                            MedViewLanguageConstants.LABEL_PRESET_NONE_LS_PROPERTY)));
                } else {
                    setPresets(activeComponent.getPresetModel());
                }

                ValueInputComponent nextComponent = sourceTabbedPane.getNextInputComponent();

                if (nextComponent == null) {
                    setNextPresets(new PresetModel(mVDH.getLanguageString(
                            MedViewLanguageConstants.LABEL_PRESET_NONE_LS_PROPERTY)));
                } else {
                    setNextPresets(nextComponent.getPresetModel());
                }
            }
        } else if (source instanceof PresetModel) {
        } else {
            System.out.println("ValueListPanel.stateChanged: error: event not from ValueTabbedPane!");

            this.updateUI(); // nader 12/4
        }
    }

    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
    }

    /**
     * Used to add the first selected value to the current input.
     * Simply calls addFromLists to fier the appropriate events.
     */
    public void addFirstValue() {
        addFromList(currentValueList, 0);
    }

    /**
     * Called via the GUI when a mouse is clicked on one of the presetListPanels.
     * If it is a left click, the value will be added to the current field (if it doesn't already exist). The selection is then cleared.
     * If it is a right click, there will be an option to remove that preset value.
     */
    public void mouseClickOnList(java.awt.event.MouseEvent e) {
        Object aSource = e.getSource(); // Get source of the click

        if (aSource == null) {
            return; // If the source is null, stop here (should not happen)
        }

        if (aSource instanceof JList) {
            Point p = e.getPoint();

            JList aList = (JList) aSource;

            if (e.isPopupTrigger()) {
                new DeleteTermPopupMenu(aList, e.getPoint()).showMe();
            } else {
                // Only left clicks should select (necessary since right clicks aren't
                // always popup triggers (depends on platform)) // Nils

                if (e.getButton() == MouseEvent.BUTTON1) {
                    int index = aList.locationToIndex(p);

                    if (index >= 0) // Check that the mouse click gives a valid location, i.e not -1
                    {
                        addFromList(aList, aList.locationToIndex(p)); // Add this value to the current field
                    }

                    aList.clearSelection(); // always clear the selection after a left mouse click.
                }
            }
        }
    }

    /**
     * A popup menu with the option to delete a certain term, used when right clicking on a term
     */
    private class DeleteTermPopupMenu extends JPopupMenu {

        JList termList;
        Point p;
        int termIndex;

        public DeleteTermPopupMenu(JList component, Point position) {
            super();

            termList = component;

            p = position;

            this.termIndex = component.locationToIndex(position);

            String termName = termList.getModel().getElementAt(termIndex).toString();

            setLabel(termName); // Not shown on windows platform

            JMenuItem removeItem = new JMenuItem(mVDH.getLanguageString(
                    MedViewLanguageConstants.MENU_ITEM_REMOVE_VALUE_LS_PROPERTY) + " " + termName);

            removeItem.setMnemonic(mVDH.getLanguageString(
                    MedViewLanguageConstants.MNEMONIC_MENU_ITEM_REMOVE_VALUE_LS_PROPERTY).charAt(0));

            removeItem.addActionListener(new ActionListener() {
                // Action to be taken if removeItem is clicked

                public void actionPerformed(ActionEvent e) {
                    removeFormList(termList, termIndex);

                    setVisible(false);
                }
            });

            add(removeItem);
        }

        public void showMe() // Pop up at the appropriate location
        {
            this.show(termList, p.x, p.y);
        }
    }

    private void addFromList(JList pList, int index) {
        String whichList = "ERROR-LIST_UNKNOWN";

        String value = "ERROR-VALUE_NOT_SET";

        ListModel lm = pList.getModel();

        // Get the value at index

        if (index >= 0 && index < lm.getSize()) {
            Object valueObject = lm.getElementAt(index);

            if (valueObject == null) {
                return; // Do nothing if no selected value
            }

            if (valueObject instanceof String) {
                value = (String) valueObject;

                pList.setSelectedIndex(index); // Not necessary

                String aVal = (String) pList.getSelectedValue(); // Get the selected value from the JList

                String customValue;
                if (pList == currentValueList) {
                    whichList = "THIS";
                    customValue = (String) currentTermValuesMap.get(value);
                } else if (pList == nextValueList) {
                    whichList = "NEXT";
                    customValue = (String) nextTermValuesMap.get(value);
                } else {
                    return; // Unknown source, should not happen.
                }

                // Fire action event


                if (customValue != null) {
                    value = customValue + "||" + value;
                }
                this.fireActionEvent(whichList + "=" + value); // Tell listeners that there was a value selection
            } else {
                System.err.println("PresetListPanel addFromLists error: Clicked object not instance of string");
            }
        } else {
            System.out.println("Ooopss");
        }
    }

    private void removeFormList(JList pList, int index) {
        if (index < 0) {
            return; // Error, end method call here
        }

        pList.setSelectedIndex(index);

        String aVal = (String) pList.getSelectedValue();

        if (aVal == null) {
            return;
        }

        PresetModel aModel = null;

        if (pList == currentValueList) {
            aModel = currentPresetModel;
        } else {
            aModel = nextPresetModel;
        }

        String aTerm = aModel.getTermName();

         int choice = MedViewDialogs.instance().createAndShowQuestionDialog(null, MedViewDialogConstants.YES_NO,
                                                         mVDH.getLanguageString(
                MedViewLanguageConstants.QUESTION_SHOULD_REMOVE_VALUE_LS_PROPERTY) + " " + aVal + "?");

        if (choice == MedViewDialogConstants.YES) {
            try {
                mVDH.removeValue(aTerm, aVal);
            } catch (Exception e) {
                e.printStackTrace();
            }

            aModel.remove(aVal);

            pList.setListData(aModel.getPresets());
        }
    }
}
