//
//  TermEditFrame.java
//  TETest
//
//  Created by Olof Torgersson on Wed Nov 26 2003.
//  $Id: TermEditFrame.java,v 1.7 2008/11/13 20:38:19 oloft Exp $.
//

package medview.termedit.view;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;
import medview.datahandling.*;

import misc.domain.*;
import misc.gui.components.*;

import medview.termedit.model.*;
import medview.termedit.view.settings.*;

public class TermEditFrame extends MainShell implements TermEditDocumentListener {

    private Document document; // Model

    private MedViewDataHandler mVDH;
    private MedViewDialogs mVD;

    private final static String version = "0.1.1";
	
	// Corresponds to labels in type pop-up
	private final static int FREE_TYPE_INDEX = 3;
	private final static int REGULAR_TYPE_INDEX = 0;
	private final static int MULTIPLE_TYPE_INDEX = 1;
	private final static int INTERVAL_TYPE_INDEX = 2;

    // Referenced GUI elements
    private SearchableListPanel termListPanel;
    private JButton addTermButton;
    private JButton removeTermButton;
    private JTextField termTextField;
    private JComboBox termTypeComboBox;
    private TermEditTableModel valuesTableModel;
    private JTable valuesTable;
    private JScrollPane valuesTablePane;
    private JButton addValueButton;
    private JButton removeValueButton;
    // End referenced GUI elements

    public TermEditFrame(Document d) {
        super();
        document = d;
        document.addDocumentListener(this);

        buildMenu();

        // Creates the components referenced in this class
        // without doing any layout.
        createReferencedComponents();

        // Layout the center component of the MainShell using the
        // components created above
        layoutCenterComponent();

        if (mVDH.isTermDefinitionLocationValid()) {
                setStatusText("Loaded " + document.getTermCount() + " terms.");
        }
        else {
                setStatusText("No term definitions found.");
        }
    }

    ////////////////////////
    // Layout of GUI stuff
    //////////////////////
    private void layoutCenterComponent() {
        JPanel centerComponent = new JPanel();

        centerComponent.setLayout(new BorderLayout());

        // The left part containg term list and a couple of buttons
        JPanel termListView = new JPanel();
        layoutTermList(termListView);

        // Put the term list to the left in the centercomponent
        centerComponent.add(termListView, BorderLayout.WEST);

        // The right part containg a view with info and a list of values
        JPanel termDisplay = new JPanel();
        layoutTermDisplay(termDisplay);

        // MAke the term display the main part of the window
        centerComponent.add(termDisplay, BorderLayout.CENTER);

        setCenterComponent(centerComponent);
    }

    private void layoutTermList(JPanel termListView) {
        // The entire "column"
        termListView.setLayout(new BorderLayout());
        termListView.add(termListPanel, BorderLayout.CENTER);

        // Two buttons below it
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addTermButton);
        buttonPanel.add(removeTermButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.WEST);
        // Iden här var att tvinga knapparna åt höger. Detta verkar inte funka såm man får göra på nåt annat sätt
        // Inte riktigt sant, förefaller ha effekt då metal används. Kanske det bara är knapparnas storlek
        // som gör att det inte har effekt med aqua
        // I princip kan väl knapparnas storlek sättas explicit

        termListView.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void layoutTermDisplay(JPanel termDisplay) {
        JPanel topPanel = new JPanel();

        termDisplay.setLayout(new BorderLayout());

        // What kind of Layout should be used?
        topPanel.add(new JLabel("Term:"));
        topPanel.add(termTextField);
        topPanel.add(new JLabel("Type:"));
        topPanel.add(termTypeComboBox);

        termDisplay.add(topPanel, BorderLayout.NORTH);

        // Tabbellen är huvudgrejen
        termDisplay.add(valuesTablePane, BorderLayout.CENTER);

        // Ett par kannapr längst ner
        // Two buttons below it
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addValueButton);
        buttonPanel.add(removeValueButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        termDisplay.add(bottomPanel, BorderLayout.SOUTH);
    }
    ////////////////////////
    // End layout of GUI stuff
    //////////////////////

    // Listening
    public void termSelectionChanged(TermEditDocumentEvent e) {
        valuesTableModel.reloadData();
        //System.out.println("termSelectionChanged: " + document.getSelectedTerm());
        if (document.isTermSelected()) {
            int popIndex;
            String selTerm = document.getSelectedTerm();
            termTextField.setText(selTerm);
            try {
				switch (document.getBasicType(selTerm)) {
					case TermDataHandler.FREE_TYPE:
						popIndex = FREE_TYPE_INDEX;
						break;
					case TermDataHandler.REGULAR_TYPE:
						popIndex = 0;
						break;
					case TermDataHandler.MULTIPLE_TYPE:
						popIndex = 1;
						break;
					case TermDataHandler.INTERVAL_TYPE:
						popIndex = 2;
						break;
					default:
						popIndex = 0;
				}
            }
            catch (Exception ex) {
                popIndex = 0;
            }
            //System.out.println("termTypeComboBox.setSelectedIndex: " + popIndex);
            termTypeComboBox.setSelectedIndex(popIndex);
        }
		
    }

    public void termAdded(TermEditDocumentEvent evt) {
        refreshTermList();
        //document.setSelectedTerm(evt.getTerm());
        termListPanel.selectEntry(evt.getTerm());
    }

    public void termRemoved(TermEditDocumentEvent evt) {
        refreshTermList();
    }

    public void termTypeChanged(TermEditDocumentEvent evt) {}

    public void valueAdded(TermEditDocumentEvent evt) {
        System.out.println("value added " + evt.getValue());
        valuesTableModel.reloadData();
        int index = valuesTableModel.indexOf(evt.getValue());

        scrollTableToRow(index);
        valuesTable.editCellAt(index, 0);
    }

    public void valueRemoved(TermEditDocumentEvent evt) {
        valuesTableModel.reloadData();
    }

    public void valueRenamed(TermEditDocumentEvent evt) {
        String val = evt.getValue();

        valuesTableModel.reloadData();
        int index = valuesTableModel.indexOf(val);

        System.out.println("valueRenamed: " + val + " index: " + index);
        valuesTable.setRowSelectionInterval(index, index);
        scrollTableToRow(index);
       /*  ListSelectionModel selModel = valuesTable.getSelectionModel();
        selModel.setSelectionInterval(index, index); */

    }


    // Action methods
    private void exitActionPerformed(java.awt.event.ActionEvent evt) {
        onClose();
    }

    private void aboutActionPerformed(java.awt.event.ActionEvent evt) {

        String titLS = MedViewLanguageConstants.TITLE_ABOUT_TERMEDIT_LS_PROPERTY;
        String txtLS = MedViewLanguageConstants.OTHER_ABOUT_TERMEDIT_TEXT_LS_PROPERTY;

        mVD.createAndShowAboutDialog(this, titLS, "TermEdit", version, txtLS);
    }

    private void prefsActionPerformed(java.awt.event.ActionEvent evt)
    {
	    SettingsContentPanel[] settingsContentPanels = new SettingsContentPanel[]
	    {
	    	new TermEditVisualSCP(mVD.getSettingsCommandQueue(), this),

		new TermEditDataHandlingSCP(mVD.getSettingsCommandQueue(), this)
	    };

	    MedViewDialogs.instance().createAndShowSettingsDialog(this,

		    "Termedit Settings", settingsContentPanels);
	}


    private void selectTerm(ListSelectionEvent e) {
        String st = (String) termListPanel.getSelectedEntry();
        //System.out.println("selectTerm klonk: " + st);
        document.setSelectedTerm(st);
    }

    private void addTerm(ActionEvent evt) {
        //System.out.println("addTerm");

        String newTerm = mVD.createAndShowChangeNameDialog(this, document.getFreshTermName(),
                                                           "Enter the name of the term to be added.", 20);

        if (newTerm != null) {
            System.out.println("addTerm - " + newTerm);
            document.createTerm(newTerm);
        }

    }

    private void removeTerm(ActionEvent evt) {
        if (document.isTermSelected()) {
            String selTerm = document.getSelectedTerm();

            int choice = mVD.createAndShowQuestionDialog(this, MedViewDialogConstants.YES_NO,
                                                         "Do you really want to remove the term \"" + selTerm + "\"?");

            if (choice == MedViewDialogConstants.YES) {
                System.out.println("removeTerm - Yes");
                try {
                    document.removeTerm(selTerm);
                }
                catch (Exception  e) {
                    e.printStackTrace(); // Should not happen
                    mVD.createAndShowErrorDialog(this, "The selected term could not be removed.");
                }
            }
        }
    }

    private void setTermType(ActionEvent evt) {
        if (document.isTermSelected()) {
			int termType;
			
            switch (termTypeComboBox.getSelectedIndex()) {
				case FREE_TYPE_INDEX:
					termType = TermDataHandler.FREE_TYPE;
					break;
				case REGULAR_TYPE_INDEX:
					termType = TermDataHandler.REGULAR_TYPE;
					break;
				case MULTIPLE_TYPE_INDEX:
					termType = TermDataHandler.MULTIPLE_TYPE;
					break;
				case INTERVAL_TYPE_INDEX:
					termType = TermDataHandler.INTERVAL_TYPE;
					break;
				default:
					termType = -1; // Should not happen
			}
            //System.out.println("setTermType: " + index);
            document.setBasicType(document.getSelectedTerm(), termType); 
        }
    }

    private void addValue(ActionEvent evt) {
        System.out.println("addValue");
        if (document.isTermSelected()) {
            String newVal = document.getFreshValue();
            try {
                document.addValue(document.getSelectedTerm(), newVal);
            }
            catch (Exception e) {
                e.printStackTrace();
                mVD.createAndShowErrorDialog(this, "Could not add a new value.");
            }
        }
    }

    private void removeValue(ActionEvent evt) {
        if (document.isTermSelected()) {
            int selIndex = valuesTable.getSelectedRow();

            if (selIndex > -1) {
                String selTerm = document.getSelectedTerm();
                String selVal = (String)valuesTable.getValueAt(selIndex, 0);

                System.out.println("removeValue: " + selTerm + " " + selVal);
                int type = MedViewDialogConstants.YES_NO;
                int choice = mVD.createAndShowQuestionDialog(this, type, "Do you really want to remove the value \"" + selVal + "\"?");

                if (choice == MedViewDialogConstants.YES) {
                    System.out.println("removeValue - Yes");

                    try {
                        document.removeValue(selTerm, selVal);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        mVD.createAndShowErrorDialog(this, "Could not remove the selected value.");

                    }
                }
            }
        }
    }


    // overidden methods
    protected int getMSHeight() {
        return 570;
    }

    protected int getMSWidth() {
    return 796;
    }

    protected String getMSTitle() {
        return "MedView TermEdit";
    }

    // Not needed
    protected boolean usesToolBars() {
        return false;
    }

    protected void subclassInit() {
        mVD = MedViewDialogs.instance();
        mVDH = MedViewDataHandler.instance();
    }

    protected void onClose() {
        //String message = mVDH.getLanguageString(lS);

        int type = MedViewDialogConstants.YES_NO;
        int choice = mVD.createAndShowQuestionDialog(this, type, "Do you really want to quit TermEdit?");

        if (choice == MedViewDialogConstants.YES) { System.exit(0); }
    }

    // misc helpers
    private void refreshTermList() {
        //System.out.println("refreshTermList");
        // Create term list
        String[] terms;
        try {
            terms = document.getTerms();
        }
        catch (Exception e) {
            System.out.println("refreshTermList failed: " + e.toString());
            terms = new String[0];
        }
        Arrays.sort(terms);
        SearchableListModel termListModel = termListPanel.getModel();
        termListModel.setEntries(terms);
    }

    private void scrollTableToRow(int row) {
        valuesTable.scrollRectToVisible(valuesTable.getCellRect(row, 0, true));
    }

    private void createReferencedComponents() {
        SearchableListModel termListModel = new SearchableListModel(new String[0]);

        termListPanel = new SearchableListPanel(termListModel, "Search:");
        termListPanel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                selectTerm(evt);
            }
        });
        refreshTermList();

        // Create add and delete term buttons
        addTermButton = new JButton("+");
        addTermButton.setToolTipText("Add a new term");
        addTermButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                addTerm(evt);
            }
        });

        removeTermButton = new JButton("-");
        removeTermButton.setToolTipText("Delete selected term");
        removeTermButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                removeTerm(evt);
            }
        });

        // Term text-field
        termTextField = new JTextField(16);

        // Term type combobox
        String[] termTypes = {"Single Value", "One or More Values", "Numerical Values", "Free Text Entry"};
        termTypeComboBox = new JComboBox(termTypes);
        termTypeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setTermType(evt);
            }
        });

        // Creation of table here, now in createTable
        valuesTableModel = new TermEditTableModel();
        valuesTable = new JTable(valuesTableModel);
        valuesTablePane = new JScrollPane(valuesTable);

        // Create add and delete value buttons
        addValueButton = new JButton("+");
        addValueButton.setToolTipText("Add a new value");
        addValueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                addValue(evt);
            }
        });

        removeValueButton = new JButton("-");
        removeValueButton.setToolTipText("Delete selected value");
        removeValueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                removeValue(evt);
            }
        });
    }


    private void buildMenu() {
        // File menu
        JMenu fileMenu = new JMenu();
        fileMenu.setText(mVDH.getLanguageString(MedViewLanguageConstants.MENU_ARCHIVE_LS_PROPERTY));
        fileMenu.setMnemonic(mVDH.getLanguageString(MedViewLanguageConstants.MNEMONIC_MENU_ARCHIVE_LS_PROPERTY).charAt(0));

        JMenuItem prefsItem = new javax.swing.JMenuItem();
        prefsItem.setText(mVDH.getLanguageString(MedViewLanguageConstants.MENU_ITEM_FILE_PREFERENCES_LS_PROPERTY));
        prefsItem.setMnemonic(mVDH.getLanguageString(MedViewLanguageConstants.MNEMONIC_MENU_ITEM_PREFERENCES_LS_PROPERTY).charAt(0));
        prefsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                prefsActionPerformed(evt);
            }
        });
        fileMenu.add(prefsItem);
        fileMenu.add(new JSeparator());

        JMenuItem exitItem = new javax.swing.JMenuItem();
        exitItem.setText(mVDH.getLanguageString(MedViewLanguageConstants.MENU_ITEM_FILE_EXIT_LS_PROPERTY));
        exitItem.setMnemonic(mVDH.getLanguageString(MedViewLanguageConstants.MNEMONIC_MENU_ITEM_EXIT_LS_PROPERTY).charAt(0));
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        fileMenu.add(exitItem);

        addToMenuBar(fileMenu);

        // Help menu
        JMenu helpMenu = new JMenu();
        helpMenu.setText(mVDH.getLanguageString(MedViewLanguageConstants.MENU_HELP_LS_PROPERTY));
        helpMenu.setMnemonic(mVDH.getLanguageString(MedViewLanguageConstants.MNEMONIC_MENU_HELP_LS_PROPERTY).charAt(0));

        JMenuItem aboutItem = new javax.swing.JMenuItem();
        aboutItem.setText("About TermEdit...");
        aboutItem.setMnemonic("A".charAt(0));
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                aboutActionPerformed(evt);
            }
        });
        helpMenu.add(aboutItem);

        addToMenuBar(helpMenu);
    }

    private class TermEditTableModel extends AbstractTableModel {

        private String[] loadedValues;
        private int rowCount;

        public TermEditTableModel() {
            super();
            reloadData();
        }

        public int getColumnCount() {
            return 1;
        }

        public String getColumnName(int col) {
            return "Known values";
        }
        public int getRowCount() {
            return rowCount;
         }

        public boolean isCellEditable(int row, int col) {
            return true;
        }

        public void setValueAt(Object value, int row, int col) {
            System.out.println("setValueAt: " + value);
            if (document.isTermSelected()) {
                String trm = document.getSelectedTerm();
                String newVal = value.toString();
                try {
                    String oldVal = loadedValues[row];
                    document.renameValue(trm, oldVal, newVal);
                    //fireTableCellUpdated(row, col);
                    reloadData();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        // Returns index of val or -1 if not found
        public int indexOf(String val) {
            int valIndex = -1;

            for (int i=0; i<rowCount; i++) {
                if (loadedValues[i].equals(val)) {
                    valIndex = i;
                    break;
                }
            }
            return valIndex;
        }

        public Object getValueAt(int row, int col) {
            if (document.isTermSelected()) {
                try {
                    return loadedValues[row];
                }
                catch (Exception e) {
                    return "Internal error trying to get value";
                }
            }
            return null;
        }

        public void reloadData() {
            //System.out.println("reloadData");

            try {
                loadedValues = document.getValues(document.getSelectedTerm());
                Arrays.sort(loadedValues);
                rowCount = loadedValues.length;
            }
            catch (NoSuchTermException nse) {
                loadedValues = new String[0];
                rowCount = 0;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            fireTableChanged(new TableModelEvent(this));
        }
    }
}
