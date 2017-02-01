/**
 * @(#) DataComponentPackagePanel.java
 */

package medview.common.components.datapackage;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import medview.common.components.*;
import medview.common.data.*;
import medview.common.dialogs.*;

import medview.datahandling.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

public class DataComponentPackagePanel extends JPanel implements
	MedViewLanguageConstants, MedViewMediaConstants, GUIConstants
{
	// STATIC MEMBERS

	private static MedViewDataHandler mVDH = MedViewDataHandler.instance();

	private static MedViewDialogs mVD = MedViewDialogs.instance();

	// INSTANCE MEMBERS

	// misc

	private boolean suppressEvents = false;

	// listeners

	private EventListenerList listenerList = new EventListenerList();

	// package edit panel

	private JDialog packageEditDialog;

	private PackageEditPanel packageEditPanel;

	// main panel components

	private JPanel globalPackagePanel;

	private JPanel includedPackagePanel;

	private JButton addToIncludedButton;

	private JButton removeFromIncludedButton;

	private JList globalPackageList;

	private JList includedPackageList;

	private DefaultListModel globalPackageListModel;

	private DefaultListModel includedPackageListModel;

	private JButton addToGlobalPackageButton;

	private JButton removeFromGlobalPackageButton;

	private JButton editGlobalPackageButton;

	private JButton setIncludedPackageDefaultButton;

	// CONSTRUCTORS

	public DataComponentPackagePanel(DataComponentPackage[] globalPacks, DataComponentPackage[] incPacks)
	{
		createSubComponents(); // not including panels

		createPanels();

		layoutGUI();

		setGlobalPackages(globalPacks);

		setIncludedPackages(incPacks);
	}

	private void createSubComponents()
	{
		addToGlobalPackageButton = new JButton(mVDH.getImageIcon(ADD_PACKAGE_IMAGE_ICON_16));

		GUIUtilities.deltaFontSize(addToGlobalPackageButton, -1);

		removeFromGlobalPackageButton = new JButton(mVDH.getImageIcon(REMOVE_PACKAGE_IMAGE_ICON_16));

		removeFromGlobalPackageButton.setEnabled(false);

		GUIUtilities.deltaFontSize(removeFromGlobalPackageButton, -1);

		editGlobalPackageButton = new JButton(mVDH.getImageIcon(EDIT_PACKAGE_IMAGE_ICON_16));

		editGlobalPackageButton.setEnabled(false);

		GUIUtilities.deltaFontSize(editGlobalPackageButton, -1);

		addToIncludedButton = new JButton(mVDH.getImageIcon(RIGHT_ARROW_IMAGE_ICON));

		addToIncludedButton.setEnabled(false);

		addToIncludedButton.setPreferredSize(GUIConstants.BUTTON_SIZE_24x24);

		removeFromIncludedButton = new JButton(mVDH.getImageIcon(LEFT_ARROW_IMAGE_ICON));

		removeFromIncludedButton.setEnabled(false);

		removeFromIncludedButton.setPreferredSize(GUIConstants.BUTTON_SIZE_24x24);

		setIncludedPackageDefaultButton = new JButton(mVDH.getLanguageString(BUTTON_SET_AS_DEFAULT_LS_PROPERTY));

		setIncludedPackageDefaultButton.setEnabled(false);

		GUIUtilities.deltaFontSize(setIncludedPackageDefaultButton, -1);

		globalPackageListModel = new DefaultListModel();

		includedPackageListModel = new DefaultListModel();

		globalPackageList = new JList(globalPackageListModel);

		globalPackageList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		includedPackageList = new JList(includedPackageListModel);

		includedPackageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		addToGlobalPackageButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (packageEditDialog == null)
				{
					createEditFrame();
				}

				packageEditDialog.setLocationRelativeTo(MedViewComponentUtilities.getClosestDialogAncestor(DataComponentPackagePanel.this));

				// set up panel

				packageEditPanel.clearPanel();

				packageEditDialog.setTitle(mVDH.getLanguageString(TITLE_ADD_PACKAGE_LS_PROPERTY));

				packageEditPanel.setApproveButtonText(mVDH.getLanguageString(BUTTON_ADD_TO_LS_PROPERTY));

				packageEditDialog.setVisible(true); // <- blocks

				// add new data component package

				if (!packageEditPanel.wasCancelled())
				{
					DataComponentPackage pack = new DefaultDataComponentPackage();

					pack.setPackageName(packageEditPanel.getPackageName());

					pack.setExaminationLocation(packageEditPanel.getExaminationLocation());

					if (packageEditPanel.usesTemplate())
					{
						pack.setUsesTemplate(true);

						pack.setTemplateLocation(packageEditPanel.getTemplateLocation());
					}

					if (packageEditPanel.usesTranslator())
					{
						pack.setUsesTranslator(true);

						pack.setTranslatorLocation(packageEditPanel.getTranslatorLocation());
					}
					if (packageEditPanel.usesGraph())
					{
						pack.setUsesGraph(true);

						pack.setGraphLocation(packageEditPanel.getGraphLocation());
					}
                    pack.setDatabaseLocation(packageEditPanel.getDatabaseLocation());
                    pack.setTermDefinitionsLocation(packageEditPanel.getTermDefinitionsLocation());
                    pack.setTermValuesLocation(packageEditPanel.getTermValuesLocation());

                    addGlobalPackage(pack);
				}
			}
		});

		removeFromGlobalPackageButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Object[] selValues = globalPackageList.getSelectedValues();

				for (int ctr=0; ctr<selValues.length; ctr++)
				{
					removeGlobalPackage((DataComponentPackage)selValues[ctr]);
				}
			}
		});

		editGlobalPackageButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (packageEditDialog == null)
				{
					createEditFrame();
				}

				DataComponentPackage selectedPackage = (DataComponentPackage) globalPackageList.getSelectedValue();

				packageEditDialog.setLocationRelativeTo(MedViewComponentUtilities.getClosestDialogAncestor(DataComponentPackagePanel.this));

				packageEditDialog.setTitle(mVDH.getLanguageString(TITLE_EDIT_PACKAGE_LS_PROPERTY));

				// set up panel

				packageEditPanel.clearPanel();

				packageEditPanel.setApproveButtonText(mVDH.getLanguageString(BUTTON_OK_LS_PROPERTY));

				packageEditPanel.setPackageName(selectedPackage.getPackageName());

				packageEditPanel.setUseTemplate(selectedPackage.usesTemplate());

				packageEditPanel.setUseTranslator(selectedPackage.usesTranslator());
                
                packageEditPanel.setUseGraph(selectedPackage.usesGraph());
                
                packageEditPanel.setGraphLocation(selectedPackage.getGraphLocation());

                packageEditPanel.setExaminationLocation(selectedPackage.getExaminationLocation());

				packageEditPanel.setTemplateLocation(selectedPackage.getTemplateLocation());

				packageEditPanel.setTranslatorLocation(selectedPackage.getTranslatorLocation());
                
                packageEditPanel.setDatabaseLocation(selectedPackage.getDatabaseLocation());
                
                packageEditPanel.setTermDefinitionsLocation(selectedPackage.getTermDefinitionsLocation());
                
                packageEditPanel.setTermValuesLocation(selectedPackage.getTermValuesLocation());

                packageEditDialog.show(); // blocks

				if (!packageEditPanel.wasCancelled())
				{
					selectedPackage.setPackageName(packageEditPanel.getPackageName());

					selectedPackage.setUsesTemplate(packageEditPanel.usesTemplate());

					selectedPackage.setUsesTranslator(packageEditPanel.usesTranslator());
                    
                    selectedPackage.setUsesGraph(packageEditPanel.usesGraph());

                    selectedPackage.setExaminationLocation(packageEditPanel.getExaminationLocation());

					if (packageEditPanel.usesTemplate())
					{
						selectedPackage.setTemplateLocation(packageEditPanel.getTemplateLocation());
					}
					else
					{
						selectedPackage.setTemplateLocation(null);
					}

					if (packageEditPanel.usesGraph())
					{
						selectedPackage.setGraphLocation(packageEditPanel.getGraphLocation());
					}
					else
					{
						selectedPackage.setGraphLocation(null);
					}
                    
                    if (packageEditPanel.usesTranslator())
					{
						selectedPackage.setTranslatorLocation(packageEditPanel.getTranslatorLocation());
					}
					else
					{
						selectedPackage.setTranslatorLocation(null);
					}

                    selectedPackage.setDatabaseLocation(packageEditPanel.getDatabaseLocation());
                    selectedPackage.setTermDefinitionsLocation(packageEditPanel.getTermDefinitionsLocation());
                    selectedPackage.setTermValuesLocation(packageEditPanel.getTermValuesLocation());
                    
                    fireGlobalPackageEdited(selectedPackage);

					repaint();
				}
			}
		});

		addToIncludedButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Object[] selValues = globalPackageList.getSelectedValues();

				for (int ctr=0; ctr<selValues.length; ctr++)
				{
					addIncludedPackage((DataComponentPackage)selValues[ctr]);
				}
			}
		});

		removeFromIncludedButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Object[] selValues = includedPackageList.getSelectedValues();

				for (int ctr=0; ctr<selValues.length; ctr++)
				{
					removeIncludedPackage((DataComponentPackage)selValues[ctr]);
				}
			}
		});

		setIncludedPackageDefaultButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});

		globalPackageList.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				if (globalPackageList.getSelectedIndices().length == 1)
				{
					// one element selected in system package list

					addToIncludedButton.setEnabled(true);

					removeFromGlobalPackageButton.setEnabled(true);

					editGlobalPackageButton.setEnabled(true);
				}
				else if (globalPackageList.getSelectedIndices().length > 1)
				{
					// more than one element selected in system package list

					addToIncludedButton.setEnabled(true);

					removeFromGlobalPackageButton.setEnabled(true);

					editGlobalPackageButton.setEnabled(false);
				}
				else
				{
					// nothing selected in system package list

					addToIncludedButton.setEnabled(false);

					removeFromGlobalPackageButton.setEnabled(false);

					editGlobalPackageButton.setEnabled(false);
				}
			}
		});

		includedPackageList.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				removeFromIncludedButton.setEnabled(includedPackageList.getSelectedIndices().length == 1);

				setIncludedPackageDefaultButton.setEnabled(includedPackageList.getSelectedIndices().length == 1);
			}
		});
	}

	private void createPanels()
	{
		globalPackagePanel = new JPanel(new BorderLayout());

		globalPackagePanel.setBorder(BorderFactory.createCompoundBorder(

			BorderFactory.createTitledBorder(mVDH.getLanguageString(TITLE_ALL_PACKAGES_LS_PROPERTY)),

			BorderFactory.createEmptyBorder(CCS,CCS,CCS,CCS)));

		globalPackagePanel.setPreferredSize(new Dimension(1,1)); // to force horizontal weights correct

		includedPackagePanel = new JPanel(new BorderLayout());

		includedPackagePanel.setBorder(BorderFactory.createCompoundBorder(

			BorderFactory.createTitledBorder(mVDH.getLanguageString(TITLE_INCLUDED_PACKAGES_LS_PROPERTY)),

			BorderFactory.createEmptyBorder(CCS,CCS,CCS,CCS)));

		includedPackagePanel.setPreferredSize(new Dimension(1,1)); // to force horizontal weights correct
	}

	private void createEditFrame()
	{
		packageEditPanel = new PackageEditPanel();

		packageEditDialog = new JDialog(MedViewComponentUtilities.getClosestDialogAncestor(DataComponentPackagePanel.this), true); // modal

		packageEditDialog.getContentPane().setLayout(new BorderLayout());

		packageEditDialog.getContentPane().add(packageEditPanel, BorderLayout.CENTER);

		packageEditDialog.pack();

		packageEditDialog.setResizable(false);
	}

	private void layoutGUI()
	{
		this.setLayout(new GridBagLayout());

		// system package panel

		JScrollPane globalPackageListScrollPane = new JScrollPane(globalPackageList);

		JPanel globalPackageButtonPanel = new JPanel(new GridLayout(1,3,0,0));

		globalPackageButtonPanel.add(addToGlobalPackageButton);

		globalPackageButtonPanel.add(removeFromGlobalPackageButton);

		globalPackageButtonPanel.add(editGlobalPackageButton);

		globalPackagePanel.add(globalPackageListScrollPane, BorderLayout.CENTER);

		globalPackagePanel.add(globalPackageButtonPanel, BorderLayout.SOUTH);

		// included package panel

		JScrollPane includedPackageListScrollPane = new JScrollPane(includedPackageList);

		JPanel includedPackageButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));

		includedPackageButtonPanel.add(setIncludedPackageDefaultButton);

		includedPackagePanel.add(includedPackageListScrollPane, BorderLayout.CENTER);

		includedPackagePanel.add(includedPackageButtonPanel, BorderLayout.SOUTH);

		// grid bag layout (container, added component, x, y, width, height, hor weight, ver weight, anchor, insets, fill)

		GUIUtilities.gridBagAdd(this, globalPackagePanel, 	0, 0, 1, 2, 1, 0, CENT, 	new Insets(0,0,0,CCS), BOTH);

		GUIUtilities.gridBagAdd(this, addToIncludedButton, 	1, 0, 1, 1, 0, 1, SOUTH, 	new Insets(0,0,CCS/2,CCS), NONE);

		GUIUtilities.gridBagAdd(this, includedPackagePanel, 	2, 0, 1, 2, 1, 0, CENT, 	new Insets(0,0,0,0), BOTH);

		GUIUtilities.gridBagAdd(this, removeFromIncludedButton,	1, 1, 1, 1, 0, 1, NORTH,	new Insets(CCS/2,0,0,CCS), NONE);
	}


	// PUBLIC UTILITY METHODS

	public DataComponentPackage[] getGlobalPackages()
	{
		DataComponentPackage[] retArr = new DataComponentPackage[globalPackageListModel.size()];

		globalPackageListModel.copyInto(retArr);

		return retArr;
	}

	public DataComponentPackage[] getIncludedPackages()
	{
		DataComponentPackage[] retArr = new DataComponentPackage[includedPackageListModel.size()];

		includedPackageListModel.copyInto(retArr);

		return retArr;
	}


	// PACKAGE MUTATOR (PRIVATE)

	private void addGlobalPackage( DataComponentPackage pack )
	{
		if (globalPackageListModel.contains(pack))
		{
			return; // already contains the package - do nothing
		}

		Object[] inList = globalPackageListModel.toArray();

		for (int ctr=0; ctr<inList.length; ctr++)
		{
			if (pack.compareTo(inList[ctr]) <= 0)
			{
				globalPackageListModel.insertElementAt(pack, ctr);

				break;
			}
		}

		if (!globalPackageListModel.contains(pack)) // if we still haven't inserted the package...
		{
			globalPackageListModel.addElement(pack); // ... add to end of list
		}

		fireGlobalPackageAdded(pack);
	}

	private void removeGlobalPackage( DataComponentPackage pack )
	{
		if (globalPackageListModel.contains(pack)) // don't do anything if package not present
		{
			globalPackageListModel.removeElement(pack);

			removeIncludedPackage(pack); // must also remove from included if present there

			fireGlobalPackageRemoved(pack);
		}
	}

	private void addIncludedPackage(DataComponentPackage pack)
	{
		if (includedPackageListModel.contains(pack))
		{
			return; // already contains the package - do nothing
		}

		Object[] inList = includedPackageListModel.toArray();

		for (int ctr=0; ctr<inList.length; ctr++) // insert into correct position
		{
			if (pack.compareTo(inList[ctr]) <= 0)
			{
				includedPackageListModel.insertElementAt(pack, ctr);

				break;
			}
		}

		if (!includedPackageListModel.contains(pack)) // if we still haven't inserted the package...
		{
			includedPackageListModel.addElement(pack); // ... add to end of list
		}

		fireIncludedPackageAdded(pack);
	}

	private void removeIncludedPackage( DataComponentPackage pack )
	{
		if (includedPackageListModel.contains(pack)) // don't do anything if package not present
		{
			includedPackageListModel.removeElement(pack);

			fireIncludedPackageRemoved(pack);
		}
	}


	// LISTENERS

	public void addDataComponentPackagePanelListener( DataComponentPackagePanelListener listener )
	{
		listenerList.add(DataComponentPackagePanelListener.class, listener);
	}

	public void removeDataComponentPackagePanelListener( DataComponentPackagePanelListener listener )
	{
		listenerList.remove(DataComponentPackagePanelListener.class, listener);
	}

	private void fireIncludedPackageAdded(DataComponentPackage pack)
	{
		DataComponentPackagePanelEvent event = new DataComponentPackagePanelEvent(this);

		event.setPackage(pack);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == DataComponentPackagePanelListener.class)
			{
				((DataComponentPackagePanelListener)listeners[i+1]).includedPackageAdded(event);
			}
		}
	}

	private void fireIncludedPackageRemoved(DataComponentPackage pack)
	{
		DataComponentPackagePanelEvent event = new DataComponentPackagePanelEvent(this);

		event.setPackage(pack);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == DataComponentPackagePanelListener.class)
			{
				((DataComponentPackagePanelListener)listeners[i+1]).includedPackageRemoved(event);
			}
		}
	}

	private void fireIncludedPackageNewDefault(DataComponentPackage pack)
	{
		DataComponentPackagePanelEvent event = new DataComponentPackagePanelEvent(this);

		event.setPackage(pack);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == DataComponentPackagePanelListener.class)
			{
				((DataComponentPackagePanelListener)listeners[i+1]).includedPackageNewDefault(event);
			}
		}
	}

	private void fireGlobalPackageAdded(DataComponentPackage pack)
	{
		DataComponentPackagePanelEvent event = new DataComponentPackagePanelEvent(this);

		event.setPackage(pack);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == DataComponentPackagePanelListener.class)
			{
				((DataComponentPackagePanelListener)listeners[i+1]).globalPackageAdded(event);
			}
		}
	}

	private void fireGlobalPackageRemoved(DataComponentPackage pack)
	{
		DataComponentPackagePanelEvent event = new DataComponentPackagePanelEvent(this);

		event.setPackage(pack);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == DataComponentPackagePanelListener.class)
			{
				((DataComponentPackagePanelListener)listeners[i+1]).globalPackageRemoved(event);
			}
		}
	}

	private void fireGlobalPackageEdited(DataComponentPackage pack)
	{
		DataComponentPackagePanelEvent event = new DataComponentPackagePanelEvent(this);

		event.setPackage(pack);

		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i] == DataComponentPackagePanelListener.class)
			{
				((DataComponentPackagePanelListener)listeners[i+1]).globalPackageEdited(event);
			}
		}
	}


	// UTILITY METHODS

	public void setGlobalPackages( DataComponentPackage[] packages )
	{
		Arrays.sort(packages); // DataComponentPackage is comparable

		globalPackageListModel.clear();

		for (int ctr=0; ctr<packages.length; ctr++)
		{
			globalPackageListModel.addElement(packages[ctr]);
		}
	}

	public void setIncludedPackages( DataComponentPackage[] packages )
	{
		Arrays.sort(packages); // DataComponentPackage is comparable

		includedPackageListModel.clear();

		for (int ctr=0; ctr<packages.length; ctr++)
		{
			includedPackageListModel.addElement(packages[ctr]);
		}
	}


	// INTERNAL PACKAGE EDIT PANEL

	private class PackageEditPanel extends JPanel
	{
		// members

		private boolean wasCancelled;


		private JLabel packageNameLabel;

		private JTextField packageNameTextField;

		private JLabel examinationLabel;

		private JTextField examinationTextField;

		private JButton examinationMoreButton;

		private JLabel termValuesLabel;

		private JTextField termValuesTextField;

		private JButton termValuesMoreButton;
        
		private JLabel termDefinitionsLabel;

		private JTextField termDefinitionsTextField;

		private JButton termDefinitionsMoreButton;
        
		private JLabel databaseLabel;

        private JTextField databaseTextField;

		private JButton databaseMoreButton;
        
        private JCheckBox templateCheckBox;

		private JTextField templateTextField;

		private JButton templateMoreButton;

		private JCheckBox translatorCheckBox;

		private JTextField translatorTextField;

		private JButton translatorMoreButton;

		private JTextField graphTextField;

		private JButton graphMoreButton;
        
        private JCheckBox graphCheckBox;
        
        private JButton okButton;

		private JButton cancelButton;

		// constant

		private final int FIELD_WIDTH = 20;

		// constructor

		public PackageEditPanel()
		{
			initializeComponents();

			layoutGUI();

			addComponentListener(new ComponentAdapter()
			{
				public void componentShown(ComponentEvent e)
				{
					packageNameTextField.requestFocus();
				}
			});
		}

		private void initializeComponents()
		{
			// other

			packageNameLabel = new JLabel(mVDH.getLanguageString(LABEL_PACKAGE_NAME_LS_PROPERTY));

			packageNameTextField = new JTextField();

			packageNameTextField.setBackground(Color.WHITE);

			packageNameTextField.setPreferredSize(new Dimension(1, GUIConstants.BUTTON_HEIGHT_NORMAL)); // stretched horizontally

			packageNameTextField.getDocument().addDocumentListener(new DocumentListener()
			{
				public void insertUpdate(DocumentEvent e)
				{
					if (!suppressEvents)
					{
						checkReady();
					}
				}

				public void removeUpdate(DocumentEvent e)
				{
					if (!suppressEvents)
					{
						checkReady();
					}
				}

				public void changedUpdate(DocumentEvent e)
				{

				}
			});

			examinationLabel = new JLabel(mVDH.getLanguageString(CHECKBOX_INCLUDE_FORM_LS_PROPERTY));

			examinationTextField = new JTextField(FIELD_WIDTH);

			examinationTextField.setBackground(Color.WHITE);

			examinationTextField.setEditable(false);

			examinationTextField.setPreferredSize(new Dimension(1, GUIConstants.BUTTON_HEIGHT_NORMAL)); // stretched horizontally

			examinationMoreButton = new MedViewMoreButton();

			examinationMoreButton.setEnabled(true);

			examinationMoreButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					File file = mVD.createAndShowChooseFileDialog(MedViewComponentUtilities.getClosestDialogAncestor(

						DataComponentPackagePanel.this));

					if (file != null)
					{
						examinationTextField.setText(file.getPath());

						checkReady();
					}
				}
			});

            // ------- termvalues
			termValuesLabel = new JLabel(mVDH.getLanguageString(CHECKBOX_INCLUDE_TERMVAL_LS_PROPERTY));



			termValuesTextField = new JTextField(FIELD_WIDTH);

			termValuesTextField.setBackground(Color.WHITE);

			termValuesTextField.setEditable(false);

			termValuesTextField.setPreferredSize(new Dimension(1, GUIConstants.BUTTON_HEIGHT_NORMAL)); // stretched horizontally

			termValuesMoreButton = new MedViewMoreButton();

			termValuesMoreButton.setEnabled(true);

			termValuesMoreButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					File file = mVD.createAndShowChooseFileDialog(MedViewComponentUtilities.getClosestDialogAncestor(

						DataComponentPackagePanel.this));

					if (file != null)
					{
						termValuesTextField.setText(file.getPath());

						checkReady();
					}
				}
			});

            // ------- termdefinitions
			termDefinitionsLabel = new JLabel(mVDH.getLanguageString(CHECKBOX_INCLUDE_TERMDEF_LS_PROPERTY));

			termDefinitionsTextField = new JTextField(FIELD_WIDTH);

			termDefinitionsTextField.setBackground(Color.WHITE);

			termDefinitionsTextField.setEditable(false);

			termDefinitionsTextField.setPreferredSize(new Dimension(1, GUIConstants.BUTTON_HEIGHT_NORMAL)); // stretched horizontally

			termDefinitionsMoreButton = new MedViewMoreButton();

			termDefinitionsMoreButton.setEnabled(true);

			termDefinitionsMoreButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					File file = mVD.createAndShowChooseFileDialog(MedViewComponentUtilities.getClosestDialogAncestor(

						DataComponentPackagePanel.this));

					if (file != null)
					{
						termDefinitionsTextField.setText(file.getPath());

						checkReady();
					}
				}
			});
            
          // ------- database
			databaseLabel = new JLabel(mVDH.getLanguageString(CHECKBOX_INCLUDE_DATABASE_LS_PROPERTY));

			databaseTextField = new JTextField(FIELD_WIDTH);

			databaseTextField.setBackground(Color.WHITE);

			databaseTextField.setEditable(false);

			databaseTextField.setPreferredSize(new Dimension(1, GUIConstants.BUTTON_HEIGHT_NORMAL)); // stretched horizontally

			databaseMoreButton = new MedViewMoreButton();

			databaseMoreButton.setEnabled(true);

			databaseMoreButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					String dirname = mVD.createAndShowLoadMVDDialog(MedViewComponentUtilities.getClosestDialogAncestor(

						DataComponentPackagePanel.this));

					if (dirname != null)
					{
						databaseTextField.setText(dirname);

						checkReady();
					}
				}
			});
            
            // ------- template
            templateCheckBox = new JCheckBox(mVDH.getLanguageString(CHECKBOX_INCLUDE_TEMPLATE_LS_PROPERTY));

			templateCheckBox.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					templateTextField.setEnabled(templateCheckBox.isSelected());

					templateMoreButton.setEnabled(templateCheckBox.isSelected());

					checkReady();
				}
			});

			templateTextField = new JTextField(FIELD_WIDTH);

			templateTextField.setBackground(Color.WHITE);

			templateTextField.setEditable(false);

			templateTextField.setPreferredSize(new Dimension(1, GUIConstants.BUTTON_HEIGHT_NORMAL)); // stretched horizontally

			templateMoreButton = new MedViewMoreButton();

			templateMoreButton.setEnabled(false);

			templateMoreButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					File file = mVD.createAndShowChooseFileDialog(MedViewComponentUtilities.getClosestDialogAncestor(

						DataComponentPackagePanel.this));

					if (file != null)
					{
						templateTextField.setText(file.getPath());

						checkReady();
					}
				}
			});

            
            // translator 
            translatorCheckBox = new JCheckBox(mVDH.getLanguageString(CHECKBOX_INCLUDE_TRANSLATOR_LS_PROPERTY));

			translatorCheckBox.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					translatorTextField.setEnabled(translatorCheckBox.isSelected());

					translatorMoreButton.setEnabled(translatorCheckBox.isSelected());

					checkReady();
				}
			});

			translatorTextField = new JTextField(FIELD_WIDTH);

			translatorTextField.setBackground(Color.WHITE);

			translatorTextField.setEditable(false);

			translatorTextField.setPreferredSize(new Dimension(1, GUIConstants.BUTTON_HEIGHT_NORMAL)); // stretched horizontally

			translatorMoreButton = new MedViewMoreButton();

			translatorMoreButton.setEnabled(false);

			translatorMoreButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					File file = mVD.createAndShowChooseFileDialog(MedViewComponentUtilities.getClosestDialogAncestor(

						DataComponentPackagePanel.this));

					if (file != null)
					{
						translatorTextField.setText(file.getPath());

						checkReady();
					}
				}
			});

            //graph template

            graphCheckBox = new JCheckBox(mVDH.getLanguageString(LABEL_GRAPH_TEMPLATE_LOCATION_LS_PROPERTY));

			graphCheckBox.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					graphTextField.setEnabled(graphCheckBox.isSelected());

					graphMoreButton.setEnabled(graphCheckBox.isSelected());

					checkReady();
				}
			});

			graphTextField = new JTextField(FIELD_WIDTH);

			graphTextField.setBackground(Color.WHITE);

			graphTextField.setEditable(false);

			graphTextField.setPreferredSize(new Dimension(1, GUIConstants.BUTTON_HEIGHT_NORMAL)); // stretched horizontally

			graphMoreButton = new MedViewMoreButton();

			graphMoreButton.setEnabled(false);

			graphMoreButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					File file = mVD.createAndShowChooseFileDialog(MedViewComponentUtilities.getClosestDialogAncestor(

						DataComponentPackagePanel.this));

					if (file != null)
					{
						graphTextField.setText(file.getPath());

						checkReady();
					}
				}
			});
            
            
            okButton = new JButton("NOT SET");

			okButton.setEnabled(false);

			okButton.setPreferredSize(new Dimension(BUTTON_WIDTH_SMALL, BUTTON_HEIGHT_SMALL));

			okButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					wasCancelled = false;

					packageEditDialog.hide();
				}
			});

			cancelButton = new JButton(mVDH.getLanguageString(BUTTON_CANCEL_TEXT_LS_PROPERTY));

			cancelButton.setPreferredSize(new Dimension(BUTTON_WIDTH_SMALL, BUTTON_HEIGHT_SMALL));

			cancelButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					wasCancelled = true;

					packageEditDialog.hide();
				}
			});
		}

		private void layoutGUI()
		{
			setLayout(new GridBagLayout());

			setBorder(BorderFactory.createEmptyBorder(CGS,CGS,CGS,CGS));

			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, CCS, CCS));

			buttonPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

			buttonPanel.add(okButton);

			buttonPanel.add(cancelButton);

			// grid bag layout (container, added component, x, y, width, height, hor weight, ver weight, anchor, insets, fill)

			GUIUtilities.gridBagAdd(this, packageNameLabel,		    0, 0, 1, 1, 0, 1, CENT, 	new Insets(0,0,CGS,CCS), HOR);

			GUIUtilities.gridBagAdd(this, packageNameTextField,	    1, 0, 2, 1, 0, 0, CENT, 	new Insets(0,0,CGS,0), HOR);

			GUIUtilities.gridBagAdd(this, new JSeparator(),		    0, 1, 3, 1, 0, 0, CENT,		new Insets(0,0,CGS,0), BOTH);

			GUIUtilities.gridBagAdd(this, examinationLabel, 	    0, 2, 1, 1, 0, 1, WEST, 	new Insets(0,0,CGS,CCS), NONE);

			GUIUtilities.gridBagAdd(this, examinationTextField, 	1, 2, 1, 1, 1, 0, CENT, 	new Insets(0,0,CGS,CCS), HOR);

			GUIUtilities.gridBagAdd(this, examinationMoreButton,	2, 2, 1, 1, 0, 0, CENT,		new Insets(0,0,CGS,0), NONE);

			GUIUtilities.gridBagAdd(this, termValuesLabel, 	        0, 3, 1, 1, 0, 1, WEST, 	new Insets(0,0,CGS,CCS), NONE);

			GUIUtilities.gridBagAdd(this, termValuesTextField, 	    1, 3, 1, 1, 1, 0, CENT, 	new Insets(0,0,CGS,CCS), HOR);

			GUIUtilities.gridBagAdd(this, termValuesMoreButton,	    2, 3, 1, 1, 0, 0, CENT,		new Insets(0,0,CGS,0), NONE);
            
			GUIUtilities.gridBagAdd(this, termDefinitionsLabel, 	0, 4, 1, 1, 0, 1, WEST, 	new Insets(0,0,CGS,CCS), NONE);

			GUIUtilities.gridBagAdd(this, termDefinitionsTextField, 1, 4, 1, 1, 1, 0, CENT, 	new Insets(0,0,CGS,CCS), HOR);

			GUIUtilities.gridBagAdd(this, termDefinitionsMoreButton,2, 4, 1, 1, 0, 0, CENT,		new Insets(0,0,CGS,0), NONE);
            
 			GUIUtilities.gridBagAdd(this, databaseLabel, 	        0, 5, 1, 1, 0, 1, WEST, 	new Insets(0,0,CGS,CCS), NONE);

			GUIUtilities.gridBagAdd(this, databaseTextField, 	    1, 5, 1, 1, 1, 0, CENT, 	new Insets(0,0,CGS,CCS), HOR);

			GUIUtilities.gridBagAdd(this, databaseMoreButton,	    2, 5, 1, 1, 0, 0, CENT,		new Insets(0,0,CGS,0), NONE);
            
            GUIUtilities.gridBagAdd(this, templateCheckBox, 	    0, 6, 1, 1, 0, 1, WEST, 	new Insets(0,0,CGS,CCS), NONE);

			GUIUtilities.gridBagAdd(this, templateTextField, 	    1, 6, 1, 1, 0, 0, CENT, 	new Insets(0,0,CGS,CCS), HOR);

			GUIUtilities.gridBagAdd(this, templateMoreButton,	    2, 6, 1, 1, 0, 0, CENT,		new Insets(0,0,CGS,0), NONE);

			GUIUtilities.gridBagAdd(this, translatorCheckBox, 	    0, 7, 1, 1, 0, 1, WEST, 	new Insets(0,0,CGS,CCS), NONE);

			GUIUtilities.gridBagAdd(this, translatorTextField, 	    1, 7, 1, 1, 0, 0, CENT, 	new Insets(0,0,CGS,CCS), HOR);

			GUIUtilities.gridBagAdd(this, translatorMoreButton,	    2, 7, 1, 1, 0, 0, CENT,		new Insets(0,0,CGS,0), NONE);

			GUIUtilities.gridBagAdd(this, graphCheckBox, 	        0, 8, 1, 1, 0, 1, WEST, 	new Insets(0,0,CGS,CCS), NONE);

			GUIUtilities.gridBagAdd(this, graphTextField, 	        1, 8, 1, 1, 0, 0, CENT, 	new Insets(0,0,CGS,CCS), HOR);

			GUIUtilities.gridBagAdd(this, graphMoreButton,	        2, 8, 1, 1, 0, 0, CENT,		new Insets(0,0,CGS,0), NONE);
            
            GUIUtilities.gridBagAdd(this, new JSeparator(),		    0, 9, 3, 1, 0, 0, CENT,		new Insets(0,0,CGS,0), BOTH);

			GUIUtilities.gridBagAdd(this, buttonPanel,		        0, 10, 3, 1, 0, 0, CENT,		new Insets(0,0,0,0), BOTH);
		}

		// determine if panel was cancelled

		public boolean wasCancelled()
		{
			return wasCancelled;
		}

		public void clearPanel()
		{
			packageNameTextField.setText("");

			examinationTextField.setText("");

			templateTextField.setText("");

			translatorTextField.setText("");
            
            graphTextField.setText("");

            templateCheckBox.setSelected(false); // disables more button as well

			translatorCheckBox.setSelected(false); // disables more button as well
            
            graphCheckBox.setSelected(false); // disables more button as well

            okButton.setEnabled(false);
		}

		// setters

		public void setPackageName(String name)
		{
			packageNameTextField.setText(name);
		}

		public void setApproveButtonText(String text)
		{
			okButton.setText(text);
		}

		public void setUseTemplate(boolean b)
		{
			templateCheckBox.setSelected(b);
		}

		public void setUseTranslator(boolean b)
		{
			translatorCheckBox.setSelected(b);
		}

		public void setUseGraph(boolean b)
		{
			graphCheckBox.setSelected(b);
		}
        
		public void setGraphLocation(String loc)
		{
			graphTextField.setText(loc);
		}
        
        public void setExaminationLocation(String loc)
		{
			examinationTextField.setText(loc);
		}

		public void setTemplateLocation(String loc)
		{
			templateTextField.setText(loc);
		}

		public void setTranslatorLocation(String loc)
		{
			translatorTextField.setText(loc);
		}

		public void setDatabaseLocation(String loc)
		{
			databaseTextField.setText(loc);
		}
        
		public void setTermDefinitionsLocation(String loc)
		{
			termDefinitionsTextField.setText(loc);
		}
        
        public void setTermValuesLocation(String loc)
		{
			termValuesTextField.setText(loc);
		}
        // getters

		public String getPackageName()
		{
			return packageNameTextField.getText();
		}

		public String getExaminationLocation()
		{
			return examinationTextField.getText();
		}

		public String getTemplateLocation()
		{
			return templateTextField.getText();
		}

		public String getTranslatorLocation()
		{
			return translatorTextField.getText();
		}

		public boolean usesTemplate()
		{
			return templateCheckBox.isSelected();
		}
		
        public boolean usesGraph()
		{
			return graphCheckBox.isSelected();
		}
		public boolean usesTranslator()
		{
			return translatorCheckBox.isSelected();
		}
        
        public String getGraphLocation()
        {
            return graphTextField.getText();
        }
        
        public String getDatabaseLocation()
        {
            return databaseTextField.getText();
        }

        public String getTermValuesLocation()
        {
            return termValuesTextField.getText();
        }

        public String getTermDefinitionsLocation()
        {
            return termDefinitionsTextField.getText();
        }
        // utility method for determining whether 'OK' should be enabled
        //the name of the package can't be empty
        //termdefs, termvalues, database can't be empty
        // also if a checkbox is enabled, the textfield can't be empty

        private void checkReady()
		{
            if (packageNameTextField.getText().length() == 0)
            {
                okButton.setEnabled(false);
                return;
            }
            else
            {
                JTextField[] textFields = new JTextField[]
				{
					examinationTextField, termDefinitionsTextField,
                    termValuesTextField, databaseTextField
                };
                for (JTextField t : textFields)
				{
                    if (t.getText().length() == 0)
                    {
                        okButton.setEnabled(false);
                        return;
                    }
                }
                
                
                if((templateCheckBox.isSelected() && templateTextField.getText().length() == 0) ||
                   (translatorCheckBox.isSelected() && translatorTextField.getText().length() == 0) ||
                   (graphCheckBox.isSelected() && graphTextField.getText().length() == 0)
                        )
                {
                    okButton.setEnabled(false);
                    return;
                }
                
            }
 			okButton.setEnabled(true);
		}
	}
}
