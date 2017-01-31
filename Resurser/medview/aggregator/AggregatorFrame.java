package medview.aggregator;

import java.awt.*;
import java.awt.datatransfer.*;  // clipbord
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.filechooser.*;
import java.io.*;
import javax.swing.event.*;
import javax.swing.border.*;

import ise.java.awt.*; // KappaLayout, LambdaLayout

import misc.event.*; // ProgressListener etc // NE

/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * Original author: Nader Nazari 
 * 
 * Note: Some of the javadoc is by '/NE' (Nils Erichson), it is not to be trusted 100% since I do not understand Nader's code fully
 *
 * $Id: AggregatorFrame.java,v 1.15 2004/10/08 14:06:00 erichson Exp $
 *
 * $Log: AggregatorFrame.java,v $
 * Revision 1.15  2004/10/08 14:06:00  erichson
 *  * Version 1.21
 *  * Now gets default aggregation name from Aggregation class
 *  * Removed references to Category in menu (now Aggregation)
 *
 * Revision 1.14  2004/07/26 12:20:45  erichson
 * version 1.2 with GUI reworking: Changes SplitPanes and ScrollPanes to kappa/lambdalayout.
 *
 * Revision 1.13  2004/05/19 15:49:52  d97nix
 * Added little tree icon.
 *
 * Revision 1.12  2004/05/19 14:43:41  d97nix
 * VERSION changed from private to protected
 *
 * Revision 1.11  2004/05/18 15:52:04  d97nix
 * Cleanup and made sure operations worked on multiple selections.
 *
 * Revision 1.10  2004/03/29 14:28:50  erichson
 * * Changed addValues to use Collection.
 * * Added version number to window title
 *
 * Revision 1.9  2004/03/28 17:52:26  erichson
 * Cleanup and additional methods necessary for adding terms and values from outside (visualizer)
 *
 * Revision 1.8  2003/06/24 17:16:31  erichson
 * Added properly threaded progress handling + some streamlining of code // NE
 *
 * Revision 1.7  2003/06/24 13:30:32  erichson
 * Added main method, moved in fitToScreen() code from StartAggregator, changed variable use to use Preferences
 *
 * Revision 1.6  2002/10/16 22:40:41  oloft
 * Changed menu shortcuts, rearranged file menu
 *
 * Revision 1.5  2002/10/14 10:32:44  erichson
 * Added more javadoc // NE
 *
 * Revision 1.4  2002/10/14 10:11:38  erichson
 * Removed System.exit(), replaced with dispose(). Now terminates cleanly.
 *
 * Revision 1.3  2002/10/09 15:03:10  erichson
 * Updated according to new method calls in groupTreeUI
 *
 */

/**
 * Main Frame of the aggregator application
 * @author Original 1.0 by Nader Nazari, 1.1 by Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.1
 */

public class AggregatorFrame extends JFrame {

    protected static final String VERSION = "v1.21";
    
    /**
     * Whether to pack the frame or not
     */
    private static final boolean packFrame = false;
    
    Preferences prefs = Preferences.getInstance();
            
    private ProgressMonitor progressMonitor = null;
    
    JPanel contentPane;
    JMenuBar jMenuBar1 = new JMenuBar();
    JMenu jMenuFile = new JMenu();
    JMenuItem jMenuFileExit = new JMenuItem();
    JMenu jMenuHelp = new JMenu();
    JMenuItem jMenuHelpAbout = new JMenuItem();
    JToolBar tlbrAggregate = new JToolBar();
    JButton btOpenCat = new JButton();
    JButton btSave = new JButton();
    JButton btHelp = new JButton();
    ImageIcon image1;
    ImageIcon image2;
    ImageIcon image3;
    private ImageIcon treeIcon;
    BorderLayout brlyMainLyout = new BorderLayout();
        
    // Left/right buttons to add/remove
    JButton rightButton; //leftButton;
    
    // JTree trTopAttrib;
    // JTree trBtmGroup;
    JMenuItem jMenuFileOpen = new JMenuItem();
    JMenuItem jMenuFileSave = new JMenuItem();
    JScrollPane ScrPnBack = new JScrollPane();
    JSplitPane spltPnTop = new JSplitPane();
    JScrollPane scrPnTopAttrib = new JScrollPane();
    JScrollPane scrPnTopValue = new JScrollPane();
    
    //User define fields
    //JSeparator      sp = new JSeparator(JSeparator.VERTICAL);
    AttribTreeUI    mAttribTree     = null; // The attribute (term/value) tree.
    GroupTreeUI     mCategoryGroupTree       = null; // The category tree
    File            mDirectoryName  = null;
        
    String          mTitle          = "MedView Aggregator " + VERSION + " - ";
    
    JMenuItem jMenuFileNewCatg = new JMenuItem();
    JMenuItem jMenuFileSaveAs = new JMenuItem();
    JMenuItem jMenuFileOpenCatg = new JMenuItem();
    JMenu jMenuEdit = new JMenu();
    JMenuItem jMenuEditDelete = new JMenuItem();
    JButton btDelete = new JButton();
    JMenuItem jMenuEditChgName = new JMenuItem();
    JMenuItem jMenuEditGroup = new JMenuItem();
    JButton btCopyGrp = new JButton();
    JButton btPasteGrp = new JButton();
    Border border1;
    TitledBorder titledBorder1;
    JButton btNewGroup = new JButton();
    JButton btNew = new JButton();
    JButton btOpenForest = new JButton();
    JPanel jPanel1 = new JPanel();
        
    JLabel statusBar = new JLabel();
    
    
    /**Construct the frame*/
    public AggregatorFrame() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK); // Enables the events defined by the specified event mask parameter
                                                  // to be delivered to this component.
        try {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        mAttribTree  = new AttribTreeUI();
        scrPnTopAttrib.getViewport().add(mAttribTree); // // aPanel.getViewport().add(mTree , null);                
    }
    
    /**Component initialization*/
    private void jbInit() throws Exception  {
        //treeIcon = new ImageIcon(AggregatorFrame.class.getResource("tree.gif")); // Too big
        treeIcon = new ImageIcon(AggregatorFrame.class.getResource("/medview/common/resources/icons/medview/tree16.png"));                
        
        image1 = new ImageIcon(AggregatorFrame.class.getResource("openFile.gif"));
        image2 = new ImageIcon(AggregatorFrame.class.getResource("closeFile.gif"));
        image3 = new ImageIcon(AggregatorFrame.class.getResource("help.gif"));
        border1 = BorderFactory.createLineBorder(Color.black,2);
        titledBorder1 = new TitledBorder("");
        setIconImage(Toolkit.getDefaultToolkit().createImage(AggregatorFrame.
        class.getResource("tree.gif")));
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(brlyMainLyout);
        this.setSize(new Dimension(515, 518));
        this.setTitle(mTitle + "Untitled");
        jMenuFile.setText("File");
        jMenuFile.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuSelected(MenuEvent e) {
                mnFileSelected(e);
            }
            public void menuDeselected(MenuEvent e) {
            }
            public void menuCanceled(MenuEvent e) {
            }
        });
        jMenuFileExit.setText("Exit");
        jMenuFileExit.addActionListener(new ActionListener()  {
            public void actionPerformed(ActionEvent e) {
                jMenuFileExit_actionPerformed(e);
            }
        });
        jMenuHelp.setText("Help");
        jMenuHelpAbout.setText("About Aggregator");
        jMenuHelpAbout.addActionListener(new ActionListener()  {
            public void actionPerformed(ActionEvent e) {
                jMenuHelpAbout_actionPerformed(e);
            }
        });
        btOpenCat.setIcon(new ImageIcon(AggregatorFrame.class.getResource("open.gif")));
        btOpenCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btOpenCategory(e);
            }
        });
        btOpenCat.setMaximumSize(new Dimension(24, 24));
        btOpenCat.setMinimumSize(new Dimension(24, 24));
        btOpenCat.setOpaque(false);
        btOpenCat.setPreferredSize(new Dimension(24, 24));
        btOpenCat.setToolTipText("Open aggregation");
        btOpenCat.setBorderPainted(false);
        btOpenCat.setFocusPainted(false);
        btSave.setIcon(new ImageIcon(AggregatorFrame.class.getResource("save.gif")));
        btSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btSaveCategory(e);
            }
        });
        btSave.setMaximumSize(new Dimension(24, 24));
        btSave.setMinimumSize(new Dimension(24, 24));
        btSave.setOpaque(false);
        btSave.setPreferredSize(new Dimension(24, 24));
        btSave.setToolTipText("Save category");
        btSave.setBorderPainted(false);
        btSave.setFocusPainted(false);
        btHelp.setRolloverEnabled(true);
        btHelp.setRolloverIcon(image3);
        btHelp.setRolloverSelectedIcon(image3);
        btHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btHelp_actionPerformed(e);
            }
        });
        btHelp.setMaximumSize(new Dimension(24, 24));
        btHelp.setMinimumSize(new Dimension(24, 24));
        btHelp.setOpaque(false);
        btHelp.setPreferredSize(new Dimension(24, 24));
        btHelp.setToolTipText("Help");
        btHelp.setBorderPainted(false);
        btHelp.setContentAreaFilled(false);
        btHelp.setFocusPainted(false);
        btHelp.setIcon(new ImageIcon(AggregatorFrame.class.getResource("help.gif")));

        /* Menu item "Open forest" */
        jMenuFileOpen.setActionCommand("Open Forest");
        jMenuFileOpen.setText("Open MVD");
        jMenuFileOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_O,
                                                                      Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        jMenuFileOpen.addActionListener(new ActionListener()  {
            public void actionPerformed(ActionEvent e) {
                mnOpen(e);
            }
        });
        
        /* Menu item "save" (aggregation) */
        jMenuFileSave.setText("Save");
        jMenuFileSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_S,
                                                                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        jMenuFileSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mnSave(e);
            }
        });
        jMenuFileNewCatg.setText("New Aggregation");
        jMenuFileNewCatg.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_N,
                                                                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        jMenuFileNewCatg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mnCreateCategory(e);
            }
        });
        jMenuFileSaveAs.setText("Save As");
        jMenuFileSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mnSaveAs(e);
            }
        });
        jMenuFileOpenCatg.setText("Open Aggregation");
        jMenuFileOpenCatg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mnOpenCategory(e);
            }
        });
        jMenuEdit.setText("Edit");
        jMenuEdit.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuSelected(MenuEvent e) {
                mnEditSelected(e);
            }
            public void menuDeselected(MenuEvent e) {
            }
            public void menuCanceled(MenuEvent e) {
            }
        });
        jMenuEditDelete.setToolTipText("");
        jMenuEditDelete.setText("Delete");
        jMenuEditDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_D,
                                                                           Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        jMenuEditDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mnDelete(e);
            }
        });
        btDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btDeleteNode(e);
            }
        });
        btDelete.setIcon(new ImageIcon(AggregatorFrame.class.getResource("delete.gif")));
        btDelete.setRolloverEnabled(true);
        btDelete.setToolTipText("Delete the selected node");
        btDelete.setBorderPainted(false);
        btDelete.setFocusPainted(false);
        btDelete.setPreferredSize(new Dimension(24, 24));
        btDelete.setMaximumSize(new Dimension(24, 24));
        btDelete.setMinimumSize(new Dimension(24, 24));
        btDelete.setOpaque(false);
        jMenuEditChgName.setText("Change Name");
        jMenuEditChgName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                muChgName(e);
            }
        });
        jMenuEditGroup.setText("New Group");
        jMenuEditGroup.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_G,
                                                                          Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        jMenuEditGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mnNewGroup(e);
            }
        });
        btCopyGrp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btCopyGrp_actionPerformed(e);
            }
        });
        btCopyGrp.setBorderPainted(false);
        btCopyGrp.setIcon(new ImageIcon(AggregatorFrame.class.getResource("copy.gif")));
        btCopyGrp.setToolTipText("Copy");
        btCopyGrp.setPreferredSize(new Dimension(24, 24));
        btCopyGrp.setOpaque(false);
        btCopyGrp.setMinimumSize(new Dimension(24, 24));
        btCopyGrp.setMaximumSize(new Dimension(24, 24));
        btPasteGrp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btPasteGrp_actionPerformed(e);
            }
        });
        btPasteGrp.setBorderPainted(false);
        btPasteGrp.setIcon(new ImageIcon(AggregatorFrame.class.getResource("paste.gif")));
        btPasteGrp.setToolTipText("Paste");
        btPasteGrp.setPreferredSize(new Dimension(24, 24));
        btPasteGrp.setOpaque(false);
        btPasteGrp.setMinimumSize(new Dimension(24, 24));
        btPasteGrp.setMaximumSize(new Dimension(24, 24));
        scrPnTopAttrib.setBorder(null);
        ScrPnBack.setBorder(BorderFactory.createEtchedBorder());
        spltPnTop.setBorder(null);
        scrPnTopValue.setBorder(null);
        tlbrAggregate.setBorder(BorderFactory.createEtchedBorder());
        
        btNewGroup.setOpaque(false);
        btNewGroup.setMinimumSize(new Dimension(24, 24));
        btNewGroup.setMaximumSize(new Dimension(24, 24));
        btNewGroup.setPreferredSize(new Dimension(24, 24));
        btNewGroup.setFocusPainted(false);
        btNewGroup.setBorderPainted(false);
        btNewGroup.setToolTipText("Create group");
        btNewGroup.setRolloverEnabled(true);
        btNewGroup.setIcon(new ImageIcon(AggregatorFrame.class.getResource("bottom.gif")));
        btNewGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btNewGroup(e);
            }
        });
        
        
        btNew.setFocusPainted(false);
        btNew.setBorderPainted(false);
        btNew.setToolTipText("New aggregation");
        btNew.setPreferredSize(new Dimension(24, 24));
        btNew.setOpaque(false);
        btNew.setMinimumSize(new Dimension(24, 24));
        btNew.setMaximumSize(new Dimension(24, 24));
        btNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btNewCategory(e);
            }
        });
        btNew.setIcon(new ImageIcon(AggregatorFrame.class.getResource("new.gif")));
        btOpenForest.setFocusPainted(false);
        btOpenForest.setBorderPainted(false);
        btOpenForest.setToolTipText("Open data base");
        btOpenForest.setPreferredSize(new Dimension(24, 24));
        btOpenForest.setOpaque(false);
        btOpenForest.setMinimumSize(new Dimension(24, 24));
        btOpenForest.setMaximumSize(new Dimension(24, 24));
        btOpenForest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btOpenForest(e);
            }
        });
        btOpenForest.setIcon(new ImageIcon(AggregatorFrame.class.getResource("load.gif")));
        
        statusBar.setText(" ");
        tlbrAggregate.add(btOpenForest, null);
        tlbrAggregate.add(btNew, null);
        tlbrAggregate.add(btOpenCat, null);
        tlbrAggregate.add(btSave);
        
        JSeparator sp = new JSeparator(JSeparator.VERTICAL);
        sp.setMaximumSize(new Dimension(2, 24));
        tlbrAggregate.add(sp);
        
        tlbrAggregate.add(btDelete, null);
        tlbrAggregate.add(btNewGroup, null);
        
        sp = new JSeparator(JSeparator.VERTICAL);
        sp.setMaximumSize(new Dimension(2, 24));
        tlbrAggregate.add(sp);
        
        tlbrAggregate.add(btCopyGrp, null);
        tlbrAggregate.add(btPasteGrp, null);
        
        sp = new JSeparator(JSeparator.VERTICAL);
        sp.setMaximumSize(new Dimension(2, 24));
        tlbrAggregate.add(sp);
        
        tlbrAggregate.add(btHelp, null);
        contentPane.add(jPanel1, BorderLayout.SOUTH);
        
        jPanel1.add(statusBar, null);        
        
        JPanel contentPanel = new JPanel();
        LambdaLayout contentLayout = new LambdaLayout();
        contentPanel.setLayout(contentLayout);
        
        JLabel termValueLabel = new JLabel("Terms and values",SwingConstants.CENTER);
        JLabel aggLabel = new JLabel("Aggregation", SwingConstants.CENTER);
        
        scrPnTopAttrib.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        scrPnTopValue.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        ImageIcon rightArrowIcon = new ImageIcon(AggregatorFrame.class.getResource("icons/FastForward24.gif"));
        rightButton = new JButton(rightArrowIcon);
        //leftButton = new JButton("<-")
        
        
        rightButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ev)
            {
                mAttribTree.addSelectedNodesToGroup();
            }            
        });
        //leftButton.addActionListener(this);
        
        
        contentPanel.add("0,0,,1,,w,2", termValueLabel); // place at 0,0, (w), height 1, align 6, 2 space
        contentPanel.add("2,0,,1,,w,2", aggLabel);
        contentPanel.add("0,1,,5,,wh,5", scrPnTopAttrib); // place at col 0, row 1, height 5, 5 space
        contentPanel.add("2,1,,5,,wh,5", scrPnTopValue);
        contentPanel.add("1,3", rightButton); // place at col 1, row 3 
        //contentPanel.add("1,4", leftButton);
        contentLayout.makeColumnsSameWidth(0,2);
        
        
        contentPane.add(ScrPnBack, BorderLayout.CENTER);
        //ScrPnBack.getViewport().add(spltPnTop, null);
        ScrPnBack.getViewport().add(contentPanel, null);
        
        //spltPnTop.add(scrPnTopAttrib, JSplitPane.LEFT);
        //spltPnTop.add(scrPnTopValue, JSplitPane.RIGHT);
        
        
        jMenuFile.add(jMenuFileOpen);
        jMenuFile.addSeparator();
        jMenuFile.add(jMenuFileNewCatg);
        jMenuFile.add(jMenuFileOpenCatg);
        jMenuFile.addSeparator();
        jMenuFile.add(jMenuFileSave);
        jMenuFile.add(jMenuFileSaveAs);
        jMenuFile.addSeparator();
        jMenuFile.add(jMenuFileExit);
        jMenuHelp.add(jMenuHelpAbout);
        jMenuBar1.add(jMenuFile);
        jMenuBar1.add(jMenuEdit);
        jMenuBar1.add(jMenuHelp);
        
        // Add the tree to the right
        
        jMenuBar1.add(Box.createHorizontalGlue()); // deprecated?        
        JMenu treeMenu = new JMenu();        
        treeMenu.setIcon(treeIcon);  
        //if (treeIcon == null)
        //    treeMenu.setText("tree32.png");        
        jMenuBar1.add(treeMenu);
        
        this.setJMenuBar(jMenuBar1);
        contentPane.add(tlbrAggregate, BorderLayout.NORTH);
        jMenuEdit.add(jMenuEditDelete);
        jMenuEdit.add(jMenuEditGroup);
        jMenuEdit.add(jMenuEditChgName);
        spltPnTop.setDividerLocation(230);
        // scrPnTopAttrib.getViewport().add(jTree1, null);
        
    }
    
    /**
     * Try to close the frame. If there is an active attribTree, ask for close? yes/no first
     */
    public void tryToCloseFrame() {
        if(mAttribTree != null) {
            if(askForSaving()) {
                dispose();
            }
        } else {
            dispose();
        }
                
        dispose(); // Used to be System.exit
    }
    
    /**File | Exit action performed*/
    void jMenuFileExit_actionPerformed(ActionEvent e) {                
        tryToCloseFrame();        
    }
    /** Action performed by Help | About menu item
     * @param e the action event to process
     */
    public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
        AggregatorFrame_AboutBox dlg = new AggregatorFrame_AboutBox(this);
        Dimension dlgSize = dlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point loc = getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.show();
    }
    /** Overridden so we can exit when window is closed
     * @param e the window event to process
     */
    protected void processWindowEvent(WindowEvent e) {
        // super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            jMenuFileExit_actionPerformed(null);
        }
    }
    
    private void mnSave(ActionEvent evt) {
        //if(mAttribTree == null) return;
        if (mDirectoryName == null) mnSaveAs(null);
        else mCategoryGroupTree.saveCategory(mDirectoryName);
    }
    
    private void mnSaveAs(ActionEvent evt) {
        if(mAttribTree == null) return;
        
        JFileChooser chooser;
        chooser = new JFileChooser(prefs.getCategoryDir());        
        CategoryFilter ctgFil =  new CategoryFilter();
        chooser.setFileFilter(ctgFil);  //  extension
        //chooser.addChoosableFileFilter(ctgFil);
        chooser.setFileSelectionMode(chooser.FILES_AND_DIRECTORIES);
        
        // Get file save dialog.
        int res = chooser.showSaveDialog(null);
        if (res ==  JFileChooser.APPROVE_OPTION){
            File aFile = chooser.getSelectedFile();
            prefs.setCategoryDir(aFile.getParent());
            String tmpName = aFile.getPath();
            String sFile;
            if(tmpName.endsWith( ctgFil.getExtention() ) ) sFile = tmpName;
            else  sFile = tmpName + "." + ctgFil.getExtention();
            
            aFile = new File(sFile);
            aFile.mkdir();
            mDirectoryName = aFile;
            mAttribTree.saveCategory(aFile);
            setTitle(mTitle + mDirectoryName.getPath() );
        }
    }
    
    /**
     * Show file chooser to open an MVD database
     */
    void mnOpen(ActionEvent evt) {
        DirectoryChooser chooser;
        DirectoryFileView  fw = new DirectoryFileView("mvd") ;
        chooser = new DirectoryChooser(prefs.getTreefileDir(),fw);
        
        // set file extension filter
        
        MvdFilter aFilter = new MvdFilter();
        chooser.setFileFilter(aFilter);
        
        int res = chooser.showOpenDialog(null);
        if (res ==  JFileChooser.APPROVE_OPTION){
            File aFile = chooser.getSelectedFile();
            String tmpName = aFile.getPath();
            prefs.setTreefileDir(aFile.getParent());
            if(!tmpName.endsWith(aFilter.getExtention()  ) ){
                Ut.warning("It is not a mvd directory");
                return;
            }
            File[] fFiles = aFile.listFiles();
            File forestFile = null;
            for(int i = 0; i < fFiles.length ; i++){
                if(fFiles[i].getName().compareTo("Forest.forest") == 0)
                    forestFile = fFiles[i];
            }
            if(forestFile == null) { // || ! aFile.exists()){
                Ut.warning("No Forest.forest found in this MVD directory ");
                return;
            }
            try {
                int noOfTr = getNoOfTreeFile(forestFile);
                if(noOfTr < 50){ // Don't bother with separate thread if we have a small amount of tree files to read
                    readDataBase(forestFile, aFile, noOfTr, null); // Skip the listener(null) since we won't use a ProgressMonitor
                }
                else{
                    setEnabled(false); // Disable the GUI

                    // Do the reading in a separate thread
                    ReadingThread aRT    = new ReadingThread(forestFile,aFile,noOfTr,this); 
                    
                    // Update the status bar to tell the user that we are reading
                    statusBar.setForeground(Color.red);
                    statusBar.setText("READING: There are " + noOfTr + " files to read. Please wait!!!" ) ;
                    statusBar.updateUI();
                                        
                }                
            }
            catch( Exception e) {
                Ut.warning("Could not read this directory " + e.getMessage() );
                e.printStackTrace()  ;
            }
        }
    }
       
    
    /**
     * Read a a database from "Timmer". Called from ReadingThread in a separate thread. I do not fully understand this // NE
     * @param forestFile the forest file (directory) to read from
     * @param aFile the aggregates file to read from
     * @param noOfTr number of tree files
     * @param aRt the reading thread */    
    
    
    
    public void readDBFromTimmer(File forestFile,File aFile,int noOfTr,ReadingThread aRt){
        
        int max = noOfTr - 1;
        progressMonitor = new ProgressMonitor(this, "", "Reading " + noOfTr + " examinations", 0,  max);
        progressMonitor.setMillisToPopup(250);
        
        ProgressListener progressListener = new ProgressListener() {        
            public void progressMade(ProgressEvent pe) {                
                progressMonitor.setProgress(pe.getProgress());
            }
        };
        
        readDataBase(forestFile, aFile, noOfTr, progressListener);
        aRt.setIsReady() ; // set thread 'ready'? // NE
    }
    
    /**
     * Read a database... /NE
     * @param forestFile the forest file to read from
     * @param aFile the aggregates file to read from
     * @param treeAmount the number of tree files
     * @param changeListener the ChangeListener to be updated with progress, can be null
     */
    private void readDataBase(File forestFile,final File aFile,final int treeAmount, ProgressListener progressListener){
        final TreesReader         aT    = new TreesReader(forestFile);
        
        if (progressListener != null) {
            aT.addProgressListener(progressListener);
        }      
        
        /* Use SwingWorker to update the progress monitor */
        final SwingWorker worker = new SwingWorker() {
            
            
            AggregateHashTable	aTable;
            
            public Object construct()
            {
                aTable = aT.readTreesToHash(); // THIS is where the actual reading happens (In TreesReader) // NE
                return aTable;
            }

            /* When finished, update ui and clean up, enable controls */
            public void finished() 
            {
                if(aTable != null)
                {
                    
                    // Create new UI Tree from the aggregateHashTable in aTable.
                    mAttribTree  = new AttribTreeUI(aTable,aFile.getName());
                    scrPnTopAttrib.getViewport().add(mAttribTree, null); // aPanel.getViewport().add(mTree , null);
                    
                    if(mCategoryGroupTree == null) // If there are no categories, create one
                        mnCreateCategory(null); // Create new category
                    else
                        setCategory(mCategoryGroupTree); // Connect the already existing category mCategoryGroupTree
                                                // to the new AttribTreeUI // NE
                    
                    scrPnTopAttrib.updateUI(); //Scrollpane.updateUI
                    
                }
                
                // Reading is done - update the status bar
                statusBar.setForeground(Color.black);
                statusBar.setText(aFile.getPath() + "   " + aT.getCountFiles() +
                " examinations of " + treeAmount +" Tree files") ;
                statusBar.updateUI();
                
                // Re-enable the UI
                setEnabled(true);
            }
            
            
        };
        worker.start();
        
    }

    int getNoOfTreeFile(File aDir){
        if(aDir == null) return 0;
        if(! aDir.isDirectory()) return 0;
        
        File [] files = aDir.listFiles(new AggregateFileFilter("tree"));
        if(files == null) return 0;
        
        int noF = files.length;
        return noF;
    }
    void mnOpenCategory(ActionEvent e) {
        
        if (mAttribTree != null){
            if( !askForSaving()) return;
        }
        DirectoryChooser chooser;
        DirectoryFileView  fw = new DirectoryFileView("mvg") ;
        chooser = new DirectoryChooser(prefs.getCategoryDir(), fw);            
        CategoryFilter	aFilter = new CategoryFilter();
        chooser.setFileFilter(aFilter);  // set file extension filter
        
        int res = chooser.showOpenDialog(null);
        
        if (res ==  JFileChooser.APPROVE_OPTION){
            File aFile = chooser.getSelectedFile();
            prefs.setCategoryDir(aFile.getParent());
            String aName = aFile.getPath();
            if(!aName.endsWith(aFilter.getExtention() ) ){
                Ut.warning("It is not a mvg directory");
                return;
            }
            
            try {
                CategoryReader aCat = new CategoryReader();
                
                GroupTreeUI category = aCat.readCategory(aFile,scrPnTopValue );
                setCategory(category); // Updates mCategoryGroupTree and UI. Used to be conectCategory // NE
                
                // mCategoryGroupTree = aCat.getCategory(); // returns a GroupTreeUI
                
                category.setSaved(true);
                scrPnTopValue.updateUI();
            }
            catch( Exception ex) {
                Ut.prt(ex.getMessage()) ;
            }
        }
    }
    
    void mnNewGroup(ActionEvent e) {
        if ( mAttribTree == null) return;
        
        mAttribTree.createNewGroup( );
        
    }
    
    /**
     * Ask (in a dialog) whether we should save changes or not. If yes is clicked, saving is done. // NE
     * @return if the user clicked yes (true) or no (false)
     */
    private boolean askForSaving(){
        boolean retVal = true;
        
        if(mAttribTree.needSaving()){
            
            int ans = Ut.yesNoCancelQuestion("Save changes to "
            + mAttribTree.getCategoryName() );
            switch (ans) {
                case Ut.No :
                    retVal = true;
                    break;
                case Ut.Yes :
                    mnSave(null);
                    retVal = true;
                    break;
                case Ut.Cancel :
                    retVal = false;
                    break;
            }
        }
        return retVal;
    }
    
    /**
     * Set mCategoryGroupTree variable and update the AttribTreeUI.
     * Used to be conectCategory(). // NE
     */
    void setCategory(GroupTreeUI newCategory){
        if (mAttribTree == null) 
            return;
        if (newCategory   == null) 
            return;
        
        mCategoryGroupTree = newCategory;
        mAttribTree.setCategory(mCategoryGroupTree );
    }
    
    void mnCreateCategory(ActionEvent e) {
        if (mAttribTree == null) return;
        
        boolean userClickedYesInSaveDialog = askForSaving(); // Ask (in dialog) whether to save or not. 
                                                             // Saves if user clicked yes. // NE
        
        if(userClickedYesInSaveDialog){ 
            mDirectoryName = null;
            mAttribTree.newCategory(scrPnTopValue );
            mCategoryGroupTree = mAttribTree.getCategory();
        }
    }
    
    void btSave_actionPerformed(ActionEvent e) {
        mnSave(null);
    }
    
    // When delete is clicked in the men
    void mnDelete(ActionEvent e)
    {
        if(mAttribTree == null) return;
        
        mCategoryGroupTree.deleteSelectedNodes();
    }
    
    // When delete button is clicked
    void btDelete_actionPerformed(ActionEvent e)
    {
        if(mAttribTree == null) 
            return;
        mCategoryGroupTree.deleteSelectedNodes();
    }
    
    void muChgName(ActionEvent e) {
        if (mCategoryGroupTree == null) return;
        mCategoryGroupTree.changeSelectedNodeName();        
    }
    
    void mnEditSelected(MenuEvent e) {
        
        if(mCategoryGroupTree == null){
            jMenuEditChgName.setEnabled(false);
            jMenuEditDelete.setEnabled(false);
            jMenuEditGroup.setEnabled(false);
        }
        else{
            jMenuEditChgName.setEnabled(true);
            jMenuEditDelete.setEnabled(true);
            jMenuEditGroup.setEnabled(true);
        }
    }
    
    void mnFileSelected(MenuEvent e) {
        if(mAttribTree  == null){
            jMenuFileSave.setEnabled(false) ;
            jMenuFileSaveAs.setEnabled(false);
            jMenuFileNewCatg.setEnabled(false);
        }
        else{
            jMenuFileSave.setEnabled(true) ;
            jMenuFileSaveAs.setEnabled(true);
            jMenuFileNewCatg.setEnabled(true);
        }
    }
    void btCopyGrp_actionPerformed(ActionEvent e) {
        Component aCom = getFocusOwner();
        //Component aCom = getFocusOwner();
        
        Ut.prt("com name = "+ aCom.getName());
        Ut.prt("com clas name = " + aCom.getClass().getName());
        if( aCom == scrPnTopAttrib ) {
            if (mAttribTree != null) 
                mAttribTree.copyPerformed() ;
        }
        else if(aCom == scrPnTopValue ) {
            if(mCategoryGroupTree   == null) mCategoryGroupTree.copyPerformed();
        }
                /*
                try{
                        StringSelection st = new StringSelection("javalllaja");
                 
                        Clipboard clpBrb = new Clipboard (null);
                        clpBrb.setContents(st,null);
                        StringSelection newst = (StringSelection)clpBrb.getContents(null);
                 
                        String froclip = (String)newst.getTransferData(DataFlavor.stringFlavor);
                        Ut.prt("nn = " + froclip);
                         return;
                }
                catch (Exception ex){
                        Ut.prt(ex.getMessage());
                        return;
                }*/
    }

    void btPasteGrp_actionPerformed(ActionEvent e)
    {        
        JOptionPane.showMessageDialog(this,"Paste not implemented!","Not implemented",JOptionPane.ERROR_MESSAGE);
    }
    
    void btNewGroup(ActionEvent e) {
        mnNewGroup(null);
        
    }
    void btHelp_actionPerformed(ActionEvent e) {
        jMenuHelpAbout_actionPerformed(null);
    }
    
    /** 
     * Called when "Open forest" button is clicked // NE
     */
    void btOpenForest(ActionEvent e) {
        mnOpen(e);// data base
    }
    
    void btNewCategory(ActionEvent e) {
        mnCreateCategory(e);
    }
    
    void btOpenCategory(ActionEvent e) {
        mnOpenCategory(e);
    }
    
    void btSaveCategory(ActionEvent e) {
        mnSave(e);
    }
    
    void btDeleteNode(ActionEvent e) {
        mnDelete(e);        
    }
    
    
    
    /**
     * Fits the aggregator to the screen by packing or validating it, and sets the frame in the center of the working area // NE
     */    
    public void fitToScreen() {         
        //Validate frames that have preset sizes
        //Pack frames that have useful preferred size info, e.g. from their layout
        if (packFrame) {
            pack();
        }
        else {
            validate();
        }
        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);             
    }
    
    /**
     * Main method for stand-alone use
     */
    public static void main(String[] args) {
        AggregatorFrame frame = new AggregatorFrame();
        frame.fitToScreen();
        frame.setVisible(true);
    }
            
    /**
     * Add values to a term in the attribute tree. // NE
     */
    public void addTermValues(String term, Collection values) {
        mAttribTree.addChildren(term, values);
    }
}



