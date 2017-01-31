//
//  WorkbenchFrame.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-11-17.
//
//  $Id: WorkbenchFrame.java,v 1.29 2009/01/15 18:43:11 oloft Exp $
//
package medview.openehr.workbench.view;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.util.*;

import medview.common.components.menu.*;
import medview.common.dialogs.settings.*;
import medview.datahandling.*;

import misc.gui.components.*;

import medview.common.constants.MedViewConstants;
import medview.common.components.toolbar.*;
import medview.common.dialogs.*;

import medview.openehr.workbench.*;
import medview.openehr.workbench.model.*;
import medview.openehr.workbench.model.exceptions.*;
import medview.openehr.workbench.model.tree.ArchetypeNodeInfo;
import medview.openehr.workbench.view.settings.*;

public class WorkbenchFrame extends MainShell implements WorkbenchModelListener {
	
	// Check if we are on Mac OS X.  This is crucial to loading and using the OSXAdapter class.
    public static boolean MAC_OS_X = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));
	
    private WorkbenchModel model;
    private MedViewDataHandler mVDH;
    private Preferences prefs;
    private final static String version = "0.2";
    // Referenced GUI elements
    private JSplitPane mainSplitPane;
    private JTree archetypesTree;
    private JTree templatesTree;
    private JTree archetypeTree;
    private JTabbedPane leftTreeTabbedPane;
    private JTextArea adlTextArea;
    private JTextArea xmlTextArea;
    private JTabbedPane rightTabbedPane;
    private JTextArea resultTextArea;
    private JTextArea messageTextArea;
    private JTextField pathTextField;
    private JTabbedPane lowerTabbedPane;
    private JTable atCodesTable;
    private JTable acCodesTable;
    private JPanel aPathPanel;
    private JTabbedPane middleTabbedPane;
    private JSplitPane mainTopBottomSplitPane;
    private JSplitPane mainLeftRightSplitPane;
	private JToolBar[] toolBars;
	private JComboBox languageCombo;
	private JComboBox viewModeCombo;
	private String[] viewModeLabels;
	private JRadioButtonMenuItem domainModeMenuItem;
	private JRadioButtonMenuItem technicalModeMenuItem;
	
    public WorkbenchFrame(WorkbenchModel m) {
        super();
        model = m;
		model.addWorkbenchModelListener(this);
        mVDH = MedViewDataHandler.instance();
        prefs = Preferences.instance();
		
		//System.getProperties().list(System.out);
		// this.getRootPane().putClientProperty("apple.awt.brushMetalLook", Boolean.TRUE);
		
        // frame size
        String set = prefs.getWindowSize();
		
        StringTokenizer t = new StringTokenizer(set, "x");
		
        try {
            int width = Integer.parseInt(t.nextToken());
            int height = Integer.parseInt(t.nextToken());
			
            this.setSize(new Dimension(width, height));
        } catch (Exception e) {
            e.printStackTrace();	// should never happen
        }
        String loc = prefs.getWindowLocation();
		
		if (loc != null) {
			t = new StringTokenizer(loc, ",");
			
			try {
				int x = Integer.parseInt(t.nextToken());
				int y = Integer.parseInt(t.nextToken());
				
				this.setLocation(x, y);
			} catch (Exception e) {
				e.printStackTrace();	// should never happen
			}
		}
        System.out.println("Setting size");
		
        // frame icon
        setIconImage(mVDH.getImage(MedViewMediaConstants.FRAME_IMAGE_ICON));
		
		// Creates the components referenced in this class
        // without doing any layout.
        createReferencedComponents();
		
        buildMenu();
		
		buildToolBars();
		
        // Layout the center component of the MainShell using the
        // components created above
        layoutCenterComponent();
		
		// Set up our application to respond to the Mac OS X application menu
        registerForMacOSXEvents();
		
    }
	
    // Generic registration with the Mac OS X application menu
    // Checks the platform, then attempts to register with the Apple EAWT
    // See OSXAdapter.java to see how this is done without directly referencing any Apple APIs
    public void registerForMacOSXEvents() {
        if (MAC_OS_X) {
            try {
                // Generate and register the OSXAdapter, passing it a hash of all the methods we wish to
                // use as delegates for various com.apple.eawt.ApplicationListener methods
                OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("quit", (Class[])null));
                OSXAdapter.setAboutHandler(this, getClass().getDeclaredMethod("about", (Class[])null));
                OSXAdapter.setPreferencesHandler(this, getClass().getDeclaredMethod("preferences", (Class[])null));
                // OSXAdapter.setFileHandler(this, getClass().getDeclaredMethod("loadImageFile", new Class[] { String.class }));
            } catch (Exception e) {
                System.err.println("Error while loading the OSXAdapter:");
                e.printStackTrace();
            }
        }
    }
	
    ////////////////////////
    // Layout of GUI stuff
    //////////////////////
    private void layoutCenterComponent() {
		
        layoutLeftTreeTabbedPane();
        layoutRightTabbedPane();
        layoutLowerTabbedPane();
		
		// mainSplitPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        mainSplitPane.setLeftComponent(leftTreeTabbedPane);
		//mainSplitPane.putClientProperty("Quaqua.SplitPane.style","bar");
		
        layoutMiddleTabbedPane();
		
		mainLeftRightSplitPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		
		//mainLeftRightSplitPane.setOneTouchExpandable(true);
        mainLeftRightSplitPane.setLeftComponent(middleTabbedPane);
        mainLeftRightSplitPane.setRightComponent(rightTabbedPane);
		//mainLeftRightSplitPane.putClientProperty("Quaqua.SplitPane.style","bar");
		
        mainLeftRightSplitPane.setDividerLocation(400);
        // mainLeftRightSplitPane.setResizeWeight(0.8);
		
		
        mainTopBottomSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mainTopBottomSplitPane.setOneTouchExpandable(true);
		mainTopBottomSplitPane.putClientProperty("Quaqua.SplitPane.style","bar");
		// mainTopBottomSplitPane.setDividerSize(1);
		
		mainTopBottomSplitPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		
        mainTopBottomSplitPane.setTopComponent(mainLeftRightSplitPane);
        mainTopBottomSplitPane.setBottomComponent(lowerTabbedPane);
		
        mainTopBottomSplitPane.setDividerLocation(550);
        mainTopBottomSplitPane.setResizeWeight(0.8);
		
        mainSplitPane.setRightComponent(mainTopBottomSplitPane);
		
        mainSplitPane.setDividerLocation(200);
		
        setCenterComponent(mainSplitPane);
    }
	
    private void layoutLeftTreeTabbedPane() {
		
        JScrollPane scrollPane1 = new JScrollPane();
        JScrollPane scrollPane2 = new JScrollPane();
		
        scrollPane1.setViewportView(archetypesTree);
        scrollPane2.setViewportView(templatesTree);
		
        leftTreeTabbedPane.addTab(mVDH.getLanguageString(MedViewLanguageConstants.TAB_OEHR_ARCHETYPES_LS_PROPERTY), scrollPane1);
        leftTreeTabbedPane.addTab(mVDH.getLanguageString(MedViewLanguageConstants.TAB_OEHR_TEMPLATES_LS_PROPERTY), scrollPane2);
		
        archetypesTree.setRootVisible(false);
        templatesTree.setRootVisible(false);
		
        archetypesTree.setShowsRootHandles(true);
        templatesTree.setShowsRootHandles(true);
		
        archetypesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        templatesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
        archetypesTree.addTreeSelectionListener(new TreeSelectionListener() {
												
												public void valueChanged(TreeSelectionEvent e) {
												
												treeSelectionChanged(archetypesTree, e);
												}
												});
		
        templatesTree.addTreeSelectionListener(new TreeSelectionListener() {
											   
											   public void valueChanged(TreeSelectionEvent e) {
											   
											   treeSelectionChanged(templatesTree, e);
											   }
											   });
		
        archetypesTree.addMouseListener(new MouseAdapter() {
										
										public void mousePressed(MouseEvent e) {
										int selRow = archetypesTree.getRowForLocation(e.getX(), e.getY());
										TreePath selPath = archetypesTree.getPathForLocation(e.getX(), e.getY());
										if (selRow != -1) {
										if (e.getClickCount() == 1) {
										// mySingleClick(selRow, selPath);
										} else if (e.getClickCount() == 2) {
										showSelectedArchetype();
										}
										}
										}
										});
		
        templatesTree.addMouseListener(new MouseAdapter() {
									   
									   public void mousePressed(MouseEvent e) {
									   int selRow = templatesTree.getRowForLocation(e.getX(), e.getY());
									   TreePath selPath = templatesTree.getPathForLocation(e.getX(), e.getY());
									   if (selRow != -1) {
									   if (e.getClickCount() == 1) {
									   // mySingleClick(selRow, selPath);
									   } else if (e.getClickCount() == 2) {
									   showSelectedTemplate();
									   }
									   }
									   }
									   });
    }
	
    private void layoutMiddleTabbedPane() {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(archetypeTree);
		
        archetypeTree.setRootVisible(false);
        archetypeTree.setShowsRootHandles(true);
		
        middleTabbedPane.add(mVDH.getLanguageString(MedViewLanguageConstants.TAB_OEHR_TREE_LS_PROPERTY), scrollPane);
    }
	
    private void layoutRightTabbedPane() {
		
        JScrollPane scrollPane1 = new JScrollPane();
        JScrollPane scrollPane2 = new JScrollPane();
		
		// adlTextArea.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		// scrollPane1.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		
        scrollPane1.setViewportView(adlTextArea);
        scrollPane2.setViewportView(xmlTextArea);
		
        rightTabbedPane.addTab("ADL", scrollPane1);
        rightTabbedPane.addTab("XML", scrollPane2);
		
    }
	
    private void layoutLowerTabbedPane() {
        JScrollPane pane = new JScrollPane();
        pane.setViewportView(atCodesTable);
		
		atCodesTable.putClientProperty("Quaqua.Table.style", "striped"); // Quaqua trick
		
		lowerTabbedPane.addTab(mVDH.getLanguageString(MedViewLanguageConstants.TAB_OEHR_TERM_DEFINITIONS_LS_PROPERTY), pane);
		
        pane = new JScrollPane();
        pane.setViewportView(acCodesTable);
		
		acCodesTable.putClientProperty("Quaqua.Table.style", "striped"); // Quaqua trick
		
        lowerTabbedPane.addTab(mVDH.getLanguageString(MedViewLanguageConstants.TAB_OEHR_CONSTRAINT_DEFINITIONS_LS_PROPERTY), pane);
		
        layoutAPathPanel();
		
        lowerTabbedPane.addTab(mVDH.getLanguageString(MedViewLanguageConstants.TAB_OEHR_APATH_QUERY_LS_PROPERTY), aPathPanel);
		
        pane = new JScrollPane();
        pane.setViewportView(messageTextArea);
		
        lowerTabbedPane.addTab(mVDH.getLanguageString(MedViewLanguageConstants.TAB_OEHR_MESSAGES_LS_PROPERTY), pane);
    }
	
    private void layoutAPathPanel() {
		
        JPanel pathPanel = new JPanel();
        pathPanel.setLayout(new BorderLayout());
		
        JLabel pathLabel = new JLabel("A-path:");
		
        JButton evaluatePathButton = new JButton("=");
        evaluatePathButton.setToolTipText("Evaluate current expression");
		
        evaluatePathButton.addActionListener(new java.awt.event.ActionListener() {
											 
											 public void actionPerformed(java.awt.event.ActionEvent evt) {
											 evaluateAPathActionPerformed(evt);
											 }
											 });
		
        pathPanel.add(pathLabel, BorderLayout.LINE_START);
        pathPanel.add(pathTextField, BorderLayout.CENTER);
        pathPanel.add(evaluatePathButton, BorderLayout.LINE_END);
		
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(resultTextArea);
		
        aPathPanel.setLayout(new BorderLayout());
        aPathPanel.add(pathPanel, BorderLayout.PAGE_START);
        aPathPanel.add(scrollPane, BorderLayout.CENTER);
		
    }
	
    private void buildMenu() {
		
        // File menu
        JMenu fileMenu = new JMenu();
        fileMenu.setText(mVDH.getLanguageString(MedViewLanguageConstants.MENU_ARCHIVE_LS_PROPERTY));
        fileMenu.setMnemonic(mVDH.getLanguageString(MedViewLanguageConstants.MNEMONIC_MENU_ARCHIVE_LS_PROPERTY).charAt(0));
		
        // file menu - open examination
        
        JMenuItem openArchetypeMenuItem = new javax.swing.JMenuItem(); // open examination
        
        openArchetypeMenuItem.setAccelerator(getAcceleratorKeyStroke(KeyEvent.VK_O));
        
        openArchetypeMenuItem.setText(mVDH.getLanguageString(MedViewLanguageConstants.MENU_ITEM_FILE_OPEN_LS_PROPERTY));
        
        openArchetypeMenuItem.setMnemonic(mVDH.getLanguageString(MedViewLanguageConstants.MNEMONIC_MENU_ITEM_OPEN_LS_PROPERTY).charAt(0));
        
        openArchetypeMenuItem.setToolTipText("Open ad-hoc archetype");
        
        openArchetypeMenuItem.addActionListener(new java.awt.event.ActionListener() {
												public void actionPerformed(java.awt.event.ActionEvent evt) {
												openActionPerformed(evt);
												}
												});
        
        fileMenu.add(openArchetypeMenuItem); // open examination
		
		// file menu - save as xml
        
        JMenuItem saveAsMenuItem = new javax.swing.JMenuItem(); // save as
        
        // saveAsMenuItem.setAccelerator(getAcceleratorKeyStroke(KeyEvent.VK_O));
        
        saveAsMenuItem.setText(mVDH.getLanguageString(MedViewLanguageConstants.MENU_ITEM_FILE_SAVE_AS_XML_LS_PROPERTY));
        
        saveAsMenuItem.setMnemonic(mVDH.getLanguageString(MedViewLanguageConstants.MNEMONIC_MENU_ITEM_SAVE_AS_XML_LS_PROPERTY).charAt(0));
        
        saveAsMenuItem.setToolTipText("Save archetype as xml");
        
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
										 public void actionPerformed(java.awt.event.ActionEvent evt) {
										 saveAsXMLActionPerformed(evt);
										 }
										 });
        
        fileMenu.add(saveAsMenuItem); // open examination
		
		// Prefs only non OS X, otherwise handled by app menu
		if (!(MAC_OS_X && useAppleLAF())) {
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
		}
		
        addToMenuBar(fileMenu);
		
		// Edit menu
		
		JMenu editMenu = new javax.swing.JMenu(); // view menu
        
        editMenu.setText(mVDH.getLanguageString(MedViewLanguageConstants.MENU_EDIT_LS_PROPERTY));
        
        editMenu.setMnemonic(mVDH.getLanguageString(MedViewLanguageConstants.MNEMONIC_MENU_EDIT_LS_PROPERTY).charAt(0));
        
		Action copyAction = new javax.swing.text.DefaultEditorKit.CopyAction();
		
		JMenuItem copyItem = new JMenuItem(copyAction);
		editMenu.add(copyItem);
		// editMenu.add(copyAction);
		copyAction.putValue(Action.NAME, mVDH.getLanguageString(MedViewLanguageConstants.MENU_ITEM_EDIT_COPY_LS_PROPERTY));
		copyAction.putValue(Action.ACCELERATOR_KEY,KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		
        copyItem.setAccelerator(getAcceleratorKeyStroke(KeyEvent.VK_C));
        copyItem.setMnemonic(mVDH.getLanguageString(MedViewLanguageConstants.MNEMONIC_MENU_ITEM_COPY_LS_PROPERTY).charAt(0));
		
        addToMenuBar(editMenu);
		
		// view menu
        
        JMenu viewMenu = new javax.swing.JMenu(); // view menu
        
        viewMenu.setText(mVDH.getLanguageString(MedViewLanguageConstants.MENU_VIEW_LS_PROPERTY));
        
        viewMenu.setMnemonic(mVDH.getLanguageString(MedViewLanguageConstants.MNEMONIC_MENU_VIEW_LS_PROPERTY).charAt(0));
		
		// Display file
		JMenuItem showFileMenuItem = new javax.swing.JMenuItem(); // expand tree
        
        showFileMenuItem.setAccelerator(getAcceleratorKeyStroke(KeyEvent.VK_D));
        
        showFileMenuItem.setText(mVDH.getLanguageString(MedViewLanguageConstants.MENU_ITEM_OEHR_DISPLAY_FILE_LS_PROPERTY));
        
        showFileMenuItem.setMnemonic(mVDH.getLanguageString(MedViewLanguageConstants.MNEMONIC_MENU_ITEM_OEHR_DISPLAY_FILE_LS_PROPERTY).charAt(0));
        
        showFileMenuItem.setToolTipText("Display selected file");
        
        showFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
										   public void actionPerformed(java.awt.event.ActionEvent evt) {
										   showSelectedArchetype();											}
										   });
		
		viewMenu.add(showFileMenuItem);
		
		// Expand tree
		JMenuItem expandTreeMenuItem = new javax.swing.JMenuItem(); // expand tree
        
        expandTreeMenuItem.setAccelerator(getAcceleratorKeyStroke(KeyEvent.VK_E));
        
        expandTreeMenuItem.setText(mVDH.getLanguageString(MedViewLanguageConstants.MENU_ITEM_OEHR_EXPAND_TREE_LS_PROPERTY));
        
        expandTreeMenuItem.setMnemonic(mVDH.getLanguageString(MedViewLanguageConstants.MNEMONIC_MENU_ITEM_OEHR_EXPAND_TREE_LS_PROPERTY).charAt(0));
		
        expandTreeMenuItem.setToolTipText("Expand current tree");
        
        expandTreeMenuItem.addActionListener(new java.awt.event.ActionListener() {
											 public void actionPerformed(java.awt.event.ActionEvent evt) {
											 expandAll(archetypeTree, true);
											 }
											 });
		
		viewMenu.add(expandTreeMenuItem);
		
		// View Mode
		JMenu viewModeSubMenu = new JMenu(mVDH.getLanguageString(MedViewLanguageConstants.MENU_ITEM_OEHR_VIEW_MODE_LS_PROPERTY));
		viewModeSubMenu.setMnemonic(mVDH.getLanguageString(MedViewLanguageConstants.MNEMONIC_MENU_ITEM_OEHR_VIEW_MODE_LS_PROPERTY).charAt(0));
		
        ButtonGroup buttonGroup = new ButtonGroup();
        
		domainModeMenuItem = new JRadioButtonMenuItem(viewModeLabels[0], true);
		
		domainModeMenuItem.addItemListener(new ItemListener() {
										   public void itemStateChanged(ItemEvent e) {
										   if (e.getStateChange() == ItemEvent.SELECTED) {
										   setViewMode(0);                                           }
										   }
										   });
		
		buttonGroup.add(domainModeMenuItem);
		
		viewModeSubMenu.add(domainModeMenuItem);
        
		technicalModeMenuItem = new JRadioButtonMenuItem(viewModeLabels[1], false);
		
		technicalModeMenuItem.addItemListener(new ItemListener() {
											  public void itemStateChanged(ItemEvent e) {
											  if (e.getStateChange() == ItemEvent.SELECTED) {
											  setViewMode(1);                                           }
											  }
											  });
		
		buttonGroup.add(technicalModeMenuItem);
		
		viewModeSubMenu.add(technicalModeMenuItem);
		
        viewMenu.add(viewModeSubMenu);
		
		//
		
		viewMenu.addSeparator();
		
		// choose look-and-feel copy & paste from MedRecords
        
        MedViewMenu lookAndFeelSubMenu = new MedViewMenu(MedViewLanguageConstants.MENU_LOOK_AND_FEEL_LS_PROPERTY, MedViewLanguageConstants.MNEMONIC_MENU_LOOK_AND_FEEL_LS_PROPERTY);
        
        final UIManager.LookAndFeelInfo[] lAFs = UIManager.getInstalledLookAndFeels();
        
        buttonGroup = new ButtonGroup();
        
        for (int ctr = 0; ctr < lAFs.length; ctr++) {
            JRadioButtonMenuItem lAFMenuItem = new JRadioButtonMenuItem(lAFs[ctr].getName(),
																		
																		UIManager.getLookAndFeel().getClass().getName().equals(lAFs[ctr].getClassName()));
            
            final String className = lAFs[ctr].getClassName();
            
            lAFMenuItem.addItemListener(new ItemListener() {
										public void itemStateChanged(ItemEvent e) {
										if (e.getStateChange() == ItemEvent.SELECTED) {
										try {
										UIManager.setLookAndFeel(className);
										
										mVDH.setUserStringPreference(Preferences.LookAndFeel,
																	 
																	 className, Preferences.class);
										} catch (Exception exc) {
										exc.printStackTrace();
										}
										}
										}
										});
            
            buttonGroup.add(lAFMenuItem);
            
            lookAndFeelSubMenu.add(lAFMenuItem);
        }
        
        viewMenu.add(lookAndFeelSubMenu);
		
		addToMenuBar(viewMenu);
		
        // Help menu
        JMenu helpMenu = new JMenu();
        helpMenu.setText(mVDH.getLanguageString(MedViewLanguageConstants.MENU_HELP_LS_PROPERTY));
        helpMenu.setMnemonic(mVDH.getLanguageString(MedViewLanguageConstants.MNEMONIC_MENU_HELP_LS_PROPERTY).charAt(0));
		
		// About only on non OS X
		if (!(MAC_OS_X && useAppleLAF())) {
			JMenuItem aboutItem = new javax.swing.JMenuItem();
			aboutItem.setText(mVDH.getLanguageString(MedViewLanguageConstants.MENU_ITEM_OEHR_HELP_ABOUT_ARCHETYPE_WORKBENCH_LS_PROPERTY));
			aboutItem.setMnemonic(mVDH.getLanguageString(MedViewLanguageConstants.MNEMONIC_MENU_ITEM_ABOUT_LS_PROPERTY).charAt(0));
			aboutItem.addActionListener(new ActionListener() {
										
										public void actionPerformed(ActionEvent evt) {
										aboutActionPerformed(evt);
										}
										});
			helpMenu.add(aboutItem);
		}
        addToMenuBar(helpMenu);
    }
	
	private void buildToolBars() {
		System.out.println("buildToolBars()");
		
        JToolBar toolBar = new javax.swing.JToolBar();
		
        toolBar.setRollover(true);
		
        // create and add 'open archetype' button
		
        JButton toolbarButton = new javax.swing.JButton(); // new examination (toolbar)
		
        toolbarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
																			   "/icons/24x24open.gif")));
		
        toolbarButton.setToolTipText("Open ad-hoc archetype");
		
        toolbarButton.addActionListener(new ActionListener() {
										
										public void actionPerformed(ActionEvent evt) {
										openActionPerformed(evt);
										}
										});
		
        toolBar.add(toolbarButton);
		
        toolbarButton = new javax.swing.JButton(); // saveAs XML
		
        toolbarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
																			   "/icons/24x24saveAs.gif")));
		
        toolbarButton.setToolTipText("Save selected archetype as XML");
		
        toolbarButton.addActionListener(new ActionListener() {
										
										public void actionPerformed(ActionEvent evt) {
										saveAsXMLActionPerformed(evt);
										}
										});
        toolBar.add(toolbarButton);
		
        toolBar.addSeparator();
		
        toolbarButton = new javax.swing.JButton(); // new examination (toolbar)
		
        toolbarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
																			   "/icons/24x24preferences.gif")));
		
        toolbarButton.setToolTipText("Edit preferences");
		
        toolbarButton.addActionListener(new java.awt.event.ActionListener() {
										
										public void actionPerformed(java.awt.event.ActionEvent evt) {
										prefsActionPerformed(evt);
										}
										});
        toolBar.add(toolbarButton);
		
        JToolBar toolBar2 = new javax.swing.JToolBar();
		
        toolBar2.setRollover(true);
		
        toolbarButton = new javax.swing.JButton(); // new examination (toolbar)
		
        toolbarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource(
																			   "/icons/24x24down.gif")));
		
        toolbarButton.setToolTipText("Expand archetype tree");
		
        toolbarButton.addActionListener(new java.awt.event.ActionListener() {
										
										public void actionPerformed(java.awt.event.ActionEvent evt) {
										expandAll(archetypeTree, true);
										}
										});
		
        toolBar2.add(toolbarButton);
		
        toolBar2.addSeparator();
		
		toolBar2.add(new JLabel(mVDH.getLanguageString(MedViewLanguageConstants.LABEL_LANGUAGE_LS_PROPERTY)));
		
		languageCombo.setPreferredSize(new Dimension(COMBOBOX_WIDTH_NORMAL, COMBOBOX_HEIGHT_NORMAL));
		languageCombo.addActionListener(new ActionListener() {
										
										public void actionPerformed(ActionEvent evt) {
										languageComboActionPerformed(evt);
										}
										});
		
		MedViewToolBarComboBoxWrapper wrapper = new MedViewToolBarComboBoxWrapper(languageCombo);
		
		toolBar2.add(wrapper);
		
		// languageCombo.addItemListener(new CheckListener());
		toolBar2.add(new JLabel(mVDH.getLanguageString(MedViewLanguageConstants.LABEL_OEHR_VIEW_MODE_LS_PROPERTY)));
		
		viewModeCombo.setPreferredSize(new Dimension(COMBOBOX_WIDTH_NORMAL, COMBOBOX_HEIGHT_NORMAL));
		viewModeCombo.addActionListener(new ActionListener() {
										
										public void actionPerformed(ActionEvent evt) {
										viewModeComboActionPerformed(evt);
										}
										});
		
		wrapper = new MedViewToolBarComboBoxWrapper(viewModeCombo);
		
		toolBar2.add(wrapper);
		
		toolBars[0] = toolBar;
		toolBars[1] = toolBar2;
		
    }
	
    private KeyStroke getAcceleratorKeyStroke(int keyCode) {
        return KeyStroke.getKeyStroke(keyCode, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    }
	
    private void createReferencedComponents() {
        mainSplitPane = new JSplitPane();
        archetypesTree = new JTree(model.getArchetypesTreeModel());
        archetypesTree.setCellRenderer(new FileTreeCellRenderer());
		
        templatesTree = new JTree(model.getTemplatesTreeModel());
        archetypeTree = new JTree(model.getTreeModel());
        archetypeTree.setCellRenderer(new ArchetypeTreeCellRenderer());
        ToolTipManager.sharedInstance().registerComponent(archetypeTree);
		
        leftTreeTabbedPane = new JTabbedPane();
        adlTextArea = new JTextArea();
        xmlTextArea = new JTextArea();
        rightTabbedPane = new JTabbedPane();
        resultTextArea = new JTextArea();
        messageTextArea = new JTextArea();
        pathTextField = new JTextField();
        aPathPanel = new JPanel();
        atCodesTable = new JTable(model.getAtCodesTableModel());
        acCodesTable = new JTable(model.getAcCodesTableModel());
        lowerTabbedPane = new JTabbedPane();
        middleTabbedPane = new JTabbedPane();
        mainTopBottomSplitPane = new JSplitPane();
        mainLeftRightSplitPane = new JSplitPane();
		toolBars = new JToolBar[2];
		String[]labels = {"en"};
		languageCombo = new JComboBox(labels);
		String combo1 = mVDH.getLanguageString(MedViewLanguageConstants.LABEL_OEHR_DOMAIN_LS_PROPERTY);
		String combo2 = mVDH.getLanguageString(MedViewLanguageConstants.LABEL_OEHR_TECHNICAL_LS_PROPERTY);
		
		viewModeLabels = new String[] {combo1,combo2};
		viewModeCombo = new JComboBox(viewModeLabels);
		
    }
	
    // Action methods
	private void openActionPerformed(java.awt.event.ActionEvent evt) {
        implementationPending();
    }
	
	private void saveAsXMLActionPerformed(java.awt.event.ActionEvent evt) {
        implementationPending();
    }
	
    private void exitActionPerformed(java.awt.event.ActionEvent evt) {
        onClose();
    }
	
	// Mac OS X specific
    // General info dialog; fed to the OSXAdapter as the method to call when 
    // "About OSXAdapter" is selected from the application menu
    public void about() {
		aboutActionPerformed(null);
	}
	
    private void aboutActionPerformed(java.awt.event.ActionEvent evt) {
		
        String titLS = MedViewLanguageConstants.TITLE_OEHR_ABOUT_ARCHETYPE_WORKBENCH_LS_PROPERTY;
        String txtLS = MedViewLanguageConstants.OTHER_OEHR_ABOUT_ARCHETYPE_WORKBENCH_TEXT_LS_PROPERTY;
		
        MedViewDialogs.instance().createAndShowAboutDialog(this, titLS, "Archetype Viewer", version, txtLS);
    }
	
	// Mac OS X specific
    // General preferences dialog; fed to the OSXAdapter as the method to call when
    // "Preferences..." is selected from the application menu
    public void preferences() {
        prefsActionPerformed(null);
    }
	
    private void prefsActionPerformed(java.awt.event.ActionEvent evt) {
        SettingsContentPanel[] settingsContentPanels = new SettingsContentPanel[]{
            new WorkbenchVisualSCP(MedViewDialogs.instance().getSettingsCommandQueue(), this),
            new WorkbenchPathSCP(MedViewDialogs.instance().getSettingsCommandQueue(), this)
        };
		
        MedViewDialogs.instance().createAndShowSettingsDialog(this,
															  MedViewLanguageConstants.TITLE_PREFERENCES_LS_PROPERTY, settingsContentPanels);
    }
	
    private void validateActionPerformed(java.awt.event.ActionEvent evt) {
        // adlTextArea.setText(model.getXMLRepresentation());
        resultTextArea.setText(model.getValidationResult());
    }
	
    private void evaluateAPathActionPerformed(java.awt.event.ActionEvent evt) {
        String expr = pathTextField.getText();
		
        String result = model.evaluateAPathExpression(expr);
		
        resultTextArea.setText(result);
    }
	
	private void languageComboActionPerformed(java.awt.event.ActionEvent evt) {
		String lang = (String)languageCombo.getSelectedItem();
		
		model.setLanguage(lang);
		
	}
	
	private void viewModeComboActionPerformed(java.awt.event.ActionEvent evt) {
		int index = viewModeCombo.getSelectedIndex();
		
		setViewMode(index);
		
	}
	
	private void setViewMode(int index) {
		switch (index) {
			case 0:
				// System.out.println("0");
				model.setDisplayMode(ArchetypeNodeInfo.DisplayMode.DOMAIN);
				break;
			case 1:
				// System.out.println("1");
				
				model.setDisplayMode(ArchetypeNodeInfo.DisplayMode.TECHNICAL);
				break;
			default:
				break;
		}
		
	}
	
    private void treeSelectionChanged(JTree t, TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) t.getLastSelectedPathComponent();
		
        /* if nothing is selected */
        if (node == null) {
            return;
        } else {
            System.out.println("selected " + node.toString());
        }
		
    }
	
    private void showSelectedArchetype() {
        FileTreeNode node = (FileTreeNode) archetypesTree.getLastSelectedPathComponent();
		
        /* if nothing is selected */
        if (node == null) {
            return;
        } else {
			try {
				model.setSelectedArchetypeFile((File) node.getUserObject());
				
				node.setStateParsed();
				
				adlTextArea.setText(model.getTextRepresentation());
				xmlTextArea.setText(model.getXMLRepresentation());
				
				model.validateSelectedArchetype();
				
				node.setStateValidated();
				
				archetypeTree.setModel(model.getTreeModel());
				
				updateLangueComboBox();
				
				messageTextArea.setText(node.toString() + " " + mVDH.getLanguageString(MedViewLanguageConstants.OTHER_OEHR_MESSAGE_PARSED_AND_VALIDATED_OK_LS_PROPERTY));
			}
			catch (ArchetypeParseException e) {
				node.setStateParseError();
				archetypeTree.setModel(null);
				
				adlTextArea.setText("");
				xmlTextArea.setText("");
				MedViewDialogs.instance().createAndShowErrorDialog(this, mVDH.getLanguageString(MedViewLanguageConstants.ERROR_OEHR_COULD_NOT_PARSE_LS_PROPERTY) + " " + node.toString() + ".\n" +
																   mVDH.getLanguageString(MedViewLanguageConstants.OTHER_OEHR_MESSAGE_CHECK_MESSAGES_PANE_LS_PROPERTY));
				displayErrorMessage(e);
			}
			catch (ArchetypeSerializerException e) {
				adlTextArea.setText("");
				xmlTextArea.setText("");
				MedViewDialogs.instance().createAndShowErrorDialog(this, mVDH.getLanguageString(MedViewLanguageConstants.ERROR_OEHR_COULD_NOT_SERIALIZE_LS_PROPERTY) + " " + node.toString() + ".\n" +
																   mVDH.getLanguageString(MedViewLanguageConstants.OTHER_OEHR_MESSAGE_CHECK_MESSAGES_PANE_LS_PROPERTY));
				displayErrorMessage(e);
			}
			catch (ArchetypeValidationException e) {
				updateLangueComboBox();
				archetypeTree.setModel(model.getTreeModel());
				
				MedViewDialogs.instance().createAndShowErrorDialog(this, mVDH.getLanguageString(MedViewLanguageConstants.ERROR_OEHR_COULD_NOT_VALIDATE_LS_PROPERTY) + " " + node.toString() + ".\n" +
																   mVDH.getLanguageString(MedViewLanguageConstants.OTHER_OEHR_MESSAGE_CHECK_MESSAGES_PANE_LS_PROPERTY));
				
				displayErrorMessage(e);
				
			}
        }
    }
	
	private void updateLangueComboBox() {
		java.util.List<String> langs = model.getAvailableLanguages();
		
		Iterator<String> it = langs.iterator();
		
		languageCombo.removeAllItems();
		
		while(it.hasNext()) {
			languageCombo.addItem(it.next());
		}
	}
	
    private void showSelectedTemplate() {
        FileTreeNode node = (FileTreeNode) templatesTree.getLastSelectedPathComponent();
		
        /* if nothing is selected */
        if (node == null) {
            return;
        } else {
            String textRepresentation = model.getTextRepresentation((File) node.getUserObject());
            adlTextArea.setText(textRepresentation);
        }
		
    }
    // overidden methods
	
    protected int getMSHeight() {
        return 700;
    }
	
    protected int getMSWidth() {
        return 950;
    }
	
    protected String getMSTitle() {
        return "MedView Archetype Viewer";
    }
	
    protected JToolBar[] getToolBars() {
		// System.out.println("getToolBars()");
		
		return toolBars;
	}
	
    protected void afterShow() {
		
        super.afterShow();
		
        if (prefs.getMaximizeWindow()) {
            this.setExtendedState(Frame.MAXIMIZED_BOTH);	// maximize
        }
		
        this.addComponentListener(new ComponentAdapter() {
								  
								  public void componentResized(ComponentEvent e) {
								  boolean isMaximized = (getExtendedState() == Frame.MAXIMIZED_BOTH);
								  
								  Preferences.instance().setMaximizeWindow(isMaximized);
								  
								  if (!isMaximized) {
								  Dimension currSize = WorkbenchFrame.this.getSize();
								  
								  int height = currSize.height;
								  
								  int width = currSize.width;
								  
								  Preferences.instance().setWindowSize(width + "x" + height);
								  
								  }
								  }
								  
								  public void componentMoved(ComponentEvent e) {
								  Point location = WorkbenchFrame.this.getLocation();
								  
								  int x = location.x;
								  
								  int y = location.y;
								  
								  Preferences.instance().setWindowLocation(x + "," + y);
								  
								  }
								  });
		
        System.out.println("Setting dividers");
		
        mainSplitPane.setDividerLocation(prefs.getTreeMainViewDividerLocation());
        mainLeftRightSplitPane.setDividerLocation(prefs.getTreeTextViewDividerLocation());
        mainTopBottomSplitPane.setDividerLocation(prefs.getTextAPathViewDividerLocation());
		
    }
	
    protected void onClose() {
		
        if (quit()) {
			
			/* 
			 prefs.setTreeMainViewDividerLocation(mainSplitPane.getDividerLocation());
			 prefs.setTreeTextViewDividerLocation(mainLeftRightSplitPane.getDividerLocation());
			 prefs.setTextAPathViewDividerLocation(mainTopBottomSplitPane.getDividerLocation());
			 */
			
            WorkBenchGUI.shutDown();
        }
    }
	
    public boolean quit() {	
        int type = MedViewDialogConstants.YES_NO;
        int choice = MedViewDialogs.instance().createAndShowQuestionDialog(this, type, mVDH.getLanguageString(MedViewLanguageConstants.LABEL_OEHR_EXIT_WORKBENCH_LS_PROPERTY));
		
		Boolean result =  (choice == MedViewDialogConstants.YES);
		
		if (result) {
			
			// System.out.println(">>>>>>>A");
			
			prefs.setTreeMainViewDividerLocation(mainSplitPane.getDividerLocation());
            prefs.setTreeTextViewDividerLocation(mainLeftRightSplitPane.getDividerLocation());
            prefs.setTextAPathViewDividerLocation(mainTopBottomSplitPane.getDividerLocation());
			
			MedViewDataHandler.instance().shuttingDown();
			WorkBenchGUI.shutDown();
			
		}
		return result;
	}
	
	private void displayErrorMessage(Exception e) {
		messageTextArea.setText(e.getMessage());
	}
	
	private Boolean useAppleLAF() {
		String laf = prefs.getLookAndFeel();
		
		if (laf != null) {
		return laf.startsWith("apple.laf") || laf.startsWith("ch.randelshofer");
		}
		else return false;
	}
	
	private void implementationPending() {
		MedViewDialogs.instance().createAndShowInfoDialog(this, "This feature is not implemented yet");
	}
	
    // If expand is true, expands all nodes in the tree.
    // Otherwise, collapses all nodes in the tree.
    public void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
		
        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }
	
    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
		
        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
	
	// Model listener
	public void archetypesLocationChanged(WorkbenchModelEvent e) {
		archetypesTree.setModel(model.getArchetypesTreeModel());
	}
	
	public void templatesLocationChanged(WorkbenchModelEvent e) {
		templatesTree.setModel(model.getTemplatesTreeModel());
	}
	
	public void displayModeChanged(WorkbenchModelEvent e) {
		if (model.getDisplayMode() == ArchetypeNodeInfo.DisplayMode.DOMAIN) {
			if (viewModeCombo.getSelectedIndex() != 0) {
				viewModeCombo.setSelectedIndex(0);
			}
			domainModeMenuItem.setSelected(true);
		}
		else if (model.getDisplayMode() == ArchetypeNodeInfo.DisplayMode.TECHNICAL) {
			if (viewModeCombo.getSelectedIndex() != 1) {
				viewModeCombo.setSelectedIndex(1);
			}
			technicalModeMenuItem.setSelected(true);
			
		}
	}
	
    private class centerPanel extends javax.swing.JPanel {
		
        /** Creates new form centerPanel */
        public centerPanel() {
            initComponents();
        }
		
        /** This method is called from within the constructor to
         * initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {
			
            jSplitPane1 = new javax.swing.JSplitPane();
            jTabbedPane1 = new javax.swing.JTabbedPane();
            jScrollPane1 = new javax.swing.JScrollPane();
            jTree1 = new javax.swing.JTree();
            jScrollPane2 = new javax.swing.JScrollPane();
            jTree2 = new javax.swing.JTree();
            jPanel1 = new javax.swing.JPanel();
            jPanel2 = new javax.swing.JPanel();
            jTextField1 = new javax.swing.JTextField();
            jLabel1 = new javax.swing.JLabel();
            jButton1 = new javax.swing.JButton();
            jSplitPane2 = new javax.swing.JSplitPane();
            jScrollPane3 = new javax.swing.JScrollPane();
            jTextArea1 = new javax.swing.JTextArea();
            jScrollPane4 = new javax.swing.JScrollPane();
            jTextArea2 = new javax.swing.JTextArea();
			
            jSplitPane1.setDividerLocation(220);
			
            jScrollPane1.setViewportView(jTree1);
			
            jTabbedPane1.addTab("Archetypes", jScrollPane1);
			
            jScrollPane2.setViewportView(jTree2);
			
            jTabbedPane1.addTab("Templates", jScrollPane2);
			
            jSplitPane1.setLeftComponent(jTabbedPane1);
			
            jTextField1.setText("jTextField1");
			
            jLabel1.setText("A-path:");
			
            jButton1.setText("Evaluate");
            jButton1.addActionListener(new java.awt.event.ActionListener() {
									   
									   public void actionPerformed(java.awt.event.ActionEvent evt) {
									   jButton1ActionPerformed(evt);
									   }
									   });
			
            org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
											 jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 511, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(1, 1, 1).add(jButton1).addContainerGap(38, Short.MAX_VALUE)));
            jPanel2Layout.setVerticalGroup(
										   jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jButton1)).addContainerGap(9, Short.MAX_VALUE)));
			
            jSplitPane2.setDividerLocation(330);
            jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
			
            jTextArea1.setColumns(20);
            jTextArea1.setRows(5);
            jScrollPane3.setViewportView(jTextArea1);
			
            jSplitPane2.setTopComponent(jScrollPane3);
			
            jTextArea2.setColumns(20);
            jTextArea2.setRows(5);
            jScrollPane4.setViewportView(jTextArea2);
			
            jSplitPane2.setRightComponent(jScrollPane4);
			
            org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
											 jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jSplitPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 693, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
            jPanel1Layout.setVerticalGroup(
										   jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jSplitPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 595, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
			
            jSplitPane1.setRightComponent(jPanel1);
			
            org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(
									  layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 931, Short.MAX_VALUE));
            layout.setVerticalGroup(
									layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE).addContainerGap()));
        }// </editor-fold>//GEN-END:initComponents
		
        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
            // TODO add your handling code here:
        }//GEN-LAST:event_jButton1ActionPerformed
        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton jButton1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JScrollPane jScrollPane4;
        private javax.swing.JSplitPane jSplitPane1;
        private javax.swing.JSplitPane jSplitPane2;
        private javax.swing.JTabbedPane jTabbedPane1;
        private javax.swing.JTextArea jTextArea1;
        private javax.swing.JTextArea jTextArea2;
        private javax.swing.JTextField jTextField1;
        private javax.swing.JTree jTree1;
        private javax.swing.JTree jTree2;
        // End of variables declaration//GEN-END:variables
    }
}

