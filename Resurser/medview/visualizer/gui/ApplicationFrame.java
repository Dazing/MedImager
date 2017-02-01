/*
 * ApplicationFrame.java
 *
 * Created on June 26, 2002, 2:08 PM
 * 
 * $Id: ApplicationFrame.java,v 1.135 2008/09/01 13:18:48 it2aran Exp $
 *  
 * $Log: ApplicationFrame.java,v $
 * Revision 1.135  2008/09/01 13:18:48  it2aran
 * Visualizer: The desktop gets scrollbars if a window is outaide the main window to prevent windows from disappearing (594)
 * Changed version numbers
 *
 * Revision 1.134  2007/10/17 15:20:07  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.133  2006/03/30 20:55:52  erichson
 * Added MedView user ID check
 *
 * Revision 1.132  2005/07/05 14:39:15  erichson
 * Moved preferences to File menu and changed Settings menu to Terms menu
 *
 * Revision 1.131  2005/06/30 10:58:54  erichson
 * Menu/Preferences overhaul + Summary update
 *
 * Revision 1.130  2005/06/30 09:14:07  erichson
 * New preferences dialog added
 *
 * Revision 1.129  2005/06/21 07:12:04  erichson
 * Added medServer loading option, starting added Preferences
 *
 * Revision 1.128  2005/06/09 08:53:38  erichson
 * Updated to interface with the new se.chalmers.docgen package.
 *
 * Revision 1.127  2005/05/20 08:18:25  erichson
 * chooseMHCFileFields updated to return whether OK was clicked or not
 *
 * Revision 1.126  2005/01/26 13:00:38  erichson
 * Small change for using termContainer
 *
 * Revision 1.125  2004/12/17 12:14:45  erichson
 * Updated with global termchooser
 *
 * Revision 1.124  2004/12/16 18:12:44  erichson
 * Plastic LAF
 *
 * Revision 1.123  2004/11/16 08:55:25  erichson
 * Quoting spaces in URI's (fixes BZ #405)
 *
 * Revision 1.122  2004/11/13 11:02:47  erichson
 * Search thread named 
 *
 * Revision 1.121  2004/10/21 12:29:09  erichson
 * Progress fixed for export and searching.
 *
 * Revision 1.120  2004/10/20 12:02:50  erichson
 * added progress to searching
 *
 * Revision 1.119  2004/10/19 13:08:17  erichson
 * Now validates views after performing a search
 *
 * Revision 1.118  2004/10/19 12:46:34  erichson
 * 1. Implementerat sökning grafiskt
 * 2. Fixade språkblandning (ändrade "Alla grafer i ett fönster")
 *
 * Revision 1.117  2004/10/19 06:36:13  erichson
 * Added edit menu and find action.
 *
 * Revision 1.116  2004/10/18 10:55:42  erichson
 * AggregationLibrary code moved out to AggregationLibraryDialog
 *
 * Revision 1.115  2004/10/12 15:40:35  erichson
 * Added the dialog for exporting.
 *
 * Revision 1.114  2004/10/12 10:03:52  erichson
 * Renamed export MVD menu item. Unsure if it should be left in at all now that we have a button for it.
 *
 * Revision 1.113  2004/10/12 10:02:14  erichson
 * Added button to export MVD.
 *
 * Revision 1.112  2004/10/08 15:54:24  erichson
 * MemoryPanel moved to own class in misc package.
 *
 * Revision 1.111  2004/10/06 14:28:37  erichson
 * Added exportMVD action
 *
 * Revision 1.110  2004/10/05 08:43:39  erichson
 * Added menu choices to open table file as well
 *
 * Revision 1.109  2004/06/01 10:36:23  erichson
 * Small fix since AggregatorFrame.addValues had changed to addTermValues
 *
 * Revision 1.108  2004/04/12 20:20:53  erichson
 * More platform-safe file:// URL path handling.
 * Moved data group choice etc to applicationManager in an attempt to generalize somewhat.
 *
 * Revision 1.107  2004/04/06 19:15:03  erichson
 * Cleaned out old DB JOptionPane code (which was commented out)
 *
 * Revision 1.106  2004/04/05 20:47:30  erichson
 * More work on passing term lists to Aggregator.
 *
 * Revision 1.105  2004/03/28 17:53:10  erichson
 * Changed method of starting Aggregator (now adds existing terms and values)
 *
 * Revision 1.104  2004/03/26 17:42:01  erichson
 * * Reinstated DB option for next release
 * * URL --> URI
 *
 * Revision 1.103  2004/02/25 18:55:33  erichson
 * Updated to store termDef and termValue locations in settings, since they are no longer stored by the datahandling
 *
 * Revision 1.102  2004/02/24 21:50:15  erichson
 * Improved translator/template error handling
 *
 * Revision 1.101  2004/02/24 20:13:16  erichson
 * Cleaned up progress dialog handling
 *
 * Revision 1.100  2004/02/23 15:25:38  erichson
 * Added memory display.
 *
 * Revision 1.99  2004/02/23 13:54:14  erichson
 * Hiding DB for Beta 3 release
 *
 * Revision 1.98  2004/02/23 12:11:17  erichson
 * Database GUI, always-quit-bugfix (BZ #335)
 *
 * Revision 1.97  2003/07/08 21:21:55  erichson
 * small fix to setStatusBarProgress which removes the status bar if progress is larger than max as well.
 *
 * Revision 1.96  2003/07/08 20:57:34  erichson
 * Properly closing messageFloater, and added system.exit() when closing app
 *
 * Revision 1.95  2003/07/08 11:14:13  erichson
 * Fixed bug #0008 (filechoosers did not remember directories)
 *
 * Revision 1.94  2003/07/07 22:23:38  erichson
 * Vettiga progressbars vid inladdning av MVD samt spara/ladda session.
 *
 * Revision 1.93  2003/07/04 00:15:10  erichson
 * Less rigorous progress monitor now (updating 2 times / second instead of every loaded examination)
 *
 * Revision 1.92  2003/07/02 16:07:54  erichson
 * added statusMessage method
 *
 * Revision 1.91  2003/07/02 09:08:28  erichson
 * Cleaned out old commented-out floater code
 *
 * Revision 1.90  2003/07/02 09:03:39  erichson
 * updated makeFreeFrame() accordingly, and added closeMDIFloaterAndMakeFreeFloater() method
 *
 * Revision 1.89  2003/07/02 00:39:32  erichson
 * Major cleanup of MakeMDIFrame which had a lot of duplicated code. This was possible due to a total rewrite and
 * generalizeation of all Floater classes. MakeFreeFrame must be done next, as it calls deprecated methods right now.
 * Added errorMessage() method.
 * Added messagePanel and a Floater for it.
 * Removed TOOLBOX_AVAILABLE, now exists as a general method isFloaterTypeAvailable in settings
 *
 * Revision 1.88  2002/12/19 09:27:36  zachrisg
 * Made sure that the statusbar always is updated correctly.
 *
 * Revision 1.87  2002/12/03 14:52:23  erichson
 * Added status bar progress and extra text
 *
 * Revision 1.86  2002/11/29 15:35:07  zachrisg
 * Changed the code for making the ProgressMonitor modal so that the bug that
 * moves the ApplicationFrame to the back is a little bit less severe.
 *
 * Revision 1.85  2002/11/28 13:24:40  zachrisg
 * Improved progress monitoring code to be more flexible.
 *
 * Revision 1.84  2002/11/27 13:48:22  zachrisg
 * Changed hardcoded icon lookup to use the ApplicationManager.loadVisualizerIcon()
 * method instead.
 *
 * Revision 1.83  2002/11/26 15:23:55  zachrisg
 * Changed "Preferences..." menuitem to "Look and feel...".
 *
 * Revision 1.82  2002/11/26 13:16:41  zachrisg
 * Now uses new translator and template handling.
 *
 * Revision 1.81  2002/11/22 16:27:45  zachrisg
 * The session is now saved in <user_home>/medview/visualizer/session.cfg.
 *
 * Revision 1.80  2002/11/20 15:37:20  erichson
 * Changed filechooser since MedViewDataHandler had changed (getDataBaseDirectory -> getUserHomeDirectory)
 *
 * Revision 1.79  2002/11/14 10:45:42  zachrisg
 * Added support for session loading/saving of viewframes' location and size.
 *
 * Revision 1.78  2002/11/11 12:38:21  zachrisg
 * Test code for the session saving/loading.
 *
 * Revision 1.77  2002/11/08 15:48:35  erichson
 * added FloaterListener implementation and added addFloaterListener to makeFreeFrame and makeMDIFrame
 *
 * Revision 1.76  2002/11/07 16:35:01  erichson
 * Modified aggregation menu and showAggregationLibrary dialog
 *
 * Revision 1.75  2002/11/07 15:36:44  erichson
 * Added showAggregationLibraryDialog()
 *
 * Revision 1.74  2002/11/06 09:26:33  zachrisg
 * Added SummaryView chart button.
 *
 * Revision 1.73  2002/11/01 10:41:13  erichson
 * Set "load database" file chooser to be able to select FILES_AND_DIRECTORIES
 *
 * Revision 1.72  2002/11/01 10:29:42  zachrisg
 * Fixed bug that crashed the application when a chart was minimized and the window
 * system was changed.
 *
 * Revision 1.71  2002/11/01 09:49:48  zachrisg
 * Floater locations and visibility are now saved between sessions.
 *
 * Revision 1.70  2002/10/31 14:37:19  zachrisg
 * Added query to the windows menu.
 *
 * Revision 1.69  2002/10/31 08:54:28  erichson
 * Update the loadExaminations file chooser with new FileView
 *
 * Revision 1.68  2002/10/29 10:15:00  zachrisg
 * Changed DataGroupSelectorDialog's title to english.
 *
 * Revision 1.67  2002/10/28 10:48:53  zachrisg
 * Added menuitems for choosing template and translator.
 *
 * Revision 1.66  2002/10/25 14:39:06  zachrisg
 * Added untested basic support for summaryreports.
 *
 * Revision 1.65  2002/10/22 17:42:00  erichson
 * 1. Made the toolbox optional (checks ApplicationManager.TOOLBOX_AVAILABLE)
 * 2. LoadExamination now  takes URL as argument
 *
 * Revision 1.64  2002/10/21 12:48:50  zachrisg
 * Removed progress-monitor-does-not-appear-bug.
 * Cleaned up javadoc.
 *
 * Revision 1.63  2002/10/17 11:42:09  erichson
 * Added imageView chart button
 *
 * Revision 1.62  2002/10/16 12:54:11  zachrisg
 * Added support for the query floater.
 *
 * Revision 1.61  2002/10/15 09:33:54  erichson
 * Removed AboutDialog (it now has its own class)
 *
 * Revision 1.60  2002/10/14 15:18:50  erichson
 * Finished the Window menu (tracking etc)
 * Dynamic application name and version (fetched from ApplicationManager)
 * Now disposes all viewframes when closing (necessary to terminate application properly in non-mdi mode, or ViewFrames would stay after applicationFrame was closed)
 *
 * Revision 1.59  2002/10/14 10:13:25  erichson
 * removed disableAggregates, added startAggregator, changed some menus and options to english
 *
 * Revision 1.58  2002/10/10 14:51:30  erichson
 * Adjusted the file chooser somewhat and removed disable aggregates code
 *
 * Revision 1.57  2002/10/09 13:49:47  zachrisg
 * Removed the trashcan.
 *
 * Revision 1.56  2002/10/08 10:59:10  erichson
 * Fixed proper disposal of datagroup and toolbox when closing application in non-MDI (free) mode
 *
 * Revision 1.55  2002/10/04 13:39:34  erichson
 * Fixed (broken) window system changing
 *
 * Revision 1.54  2002/10/04 09:20:43  zachrisg
 * Added a button for the statistics view.
 *
 * Revision 1.53  2002/10/03 13:40:40  zachrisg
 * Improved handling of chartbuttons.
 *
 * Revision 1.52  2002/09/30 11:59:01  erichson
 * Added exception handling for reading aggregates
 *
 */

package medview.visualizer.gui;

import java.awt.*;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;

import java.util.*;
import java.io.*;

import medview.common.components.aggregation.*;
import medview.common.dialogs.*;
import medview.common.dialogs.settings.SettingsContentPanel;
import medview.common.filefilter.*; // DialogDirectoryFileFilter

import medview.common.generator.MedViewGeneratorUtilities;
//import medview.common.template.*;
//import medview.common.translator.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;
import medview.datahandling.aggregation.*;

import medview.visualizer.data.*;
import medview.visualizer.dnd.*;
import medview.visualizer.event.*;
import medview.visualizer.gui.dialogs.*;
import medview.visualizer.gui.settings.*;

import misc.foundation.*;
import misc.foundation.io.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.template.TemplateModel;
import se.chalmers.cs.medview.docgen.translator.TranslatorModel;

import com.jrefinery.data.*;

/**
 * Main frame for the visualizer application.
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>, G?ran Zachrisson <e8gz@etek.chalmers.se>
 */
public class ApplicationFrame extends JFrame implements WindowListChangeListener, WindowSystemChangeListener, ActionListener, SelectionListener, DatasetChangeListener, ProgressListener, WindowListener, FloaterListener {
        
    private static final Dimension STANDARD_VIEW_DIMENSION = new Dimension(500,350);
    
    private ApplicationFrame applicationFrame;
    
    private JMenuBar menuBar;
    private JMenu fileMenu;    
    
    private JMenuItem openMVDMenuItem; /* Menu item to open an MVD Database */
    private JMenuItem openFileMenuItem; // Open a text or excel file
    
    private String templateFilename = settings.getTemplateFilename();
    private String translatorFilename = settings.getTranslatorFilename();
            
    private SettingsDialog settingsDialog = null; // Will be created on demand    
    
    /** Menu item to open a networked database */
    private JMenuItem openDBMenuItem;
    
    /** Menu item to open examinations from MedServer */
    private JMenuItem openMedServerMenuItem;
    
    private JMenuItem exitMenuItem;
    
    private JMenu termsMenu;    
    private JMenuItem termsMenuItem;    
    private JMenuItem preferencesMenuItem;    
    
    private JMenu windowsMenu;
    private JCheckBoxMenuItem dataGroupsMenuItem;
    private JCheckBoxMenuItem toolBoxMenuItem;
    private JCheckBoxMenuItem queryMenuItem;
    private JCheckBoxMenuItem messageFloaterMenuItem;    
    
    private JMenu helpMenu;
    private JMenuItem aboutMenuItem;
    
    private JMenu aggregatesMenu;
    private JMenuItem loadAggregatesMenuItem;
    private JMenuItem startAggregatorMenuItem;
    private JMenuItem aggregationLibraryMenuItem;
    
    private JToolBar toolBar;
    
    private JDesktopPane desktopPane;
    private JScrollPane scrollPane;
    private HashMap progressSources;
    private misc.gui.components.MemoryPanel memoryPanel;
    
    /** The status bar. */
    //private JPanel statusBarPanel;
    private JToolBar statusBar; // statusBarToolBar
    private JLabel statusBarSelectionLabel;
    private JLabel statusBarTextLabel;
    private JProgressBar statusBarProgressBar;
    private boolean statusBarProgressBarVisible = false;
    
    /** The filechoosers. */
    private JFileChooser fileChooser;

    //private medview.aggregator.DirectoryChooser aggregatesFileChooser = null;
    private JFileChooser aggregatesFileChooser = null;
    private JFileChooser templateChooser;
    private JFileChooser translatorChooser;

    

    
    /** The data group floater panel. */
    private DataGroupPanel dataGroupPanel;
   
    /** The query floater panel. */
    private QueryPanel queryPanel;
    
    /** the message floater panel */
    private MessagePanel messagePanel;
   
    /** the toolbox floater panel */
    private ToolBoxComponent toolBoxPanel;
         
    /** The current floater factory. */
    private FloaterFactory floaterFactory;
    
    /** The only instance of ApplicationFrame. */
    private static ApplicationFrame theInstance = null;
    
    /** True if the statusbar needs to be updated. */
    private boolean statusBarInvalid = true;    
    
    private static final ApplicationManager appManager = ApplicationManager.getInstance();
    private static final Settings settings = Settings.getInstance();
    
    /**
     * The DB connection dialog
     */
    private DatabaseDialog databaseDialog;
    
    /** 
     * Creates a new ApplicationFrame.
     */
    private ApplicationFrame()
    {        
        super(ApplicationManager.APPLICATION_NAME + " " + ApplicationManager.VERSION_STRING);
        try {
            this.setIconImage(ApplicationManager.getInstance().loadVisualizerIcon("medview16.gif").getImage());                                
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this,"ApplicationFrame could not loadVisualizerIcon(medview16.gif). Reason: " + ioe.getMessage(),"Error in ApplicationFrame constructor",JOptionPane.ERROR_MESSAGE);
        }

        applicationFrame = this;        
        
        /* Install Plastic Look&Feel */
        Class plasticClass = com.jgoodies.plaf.plastic.PlasticLookAndFeel.class;
        UIManager.installLookAndFeel("Plastic", plasticClass.getName());
        
        // if (settings.getUseNativeLookAndFeel()) {        
        //    String nativeLAFname = UIManager.getSystemLookAndFeelClassName();
        
        String preferredLAFclass = settings.getLookAndFeelClassName();
        /*
        UIManager.LookAndFeelInfo[] installedLAFs = UIManager.getInstalledLookAndFeels();
        Vector lafs = new Vector();
        for (int i = 0; i < installedLAFs.length; i++ ) {
            lafs.add( installedLAFs[i].getClassName());
        }
        if (lafs.contains(preferredLAFclass)) {         */
        setLookAndFeel(settings.getLookAndFeelClassName());            
        /* } else {
            System.err.println("Could not change LAF: LAF " + preferredLAFclass + " not installed...");
        }*/
        //}
        
        //System.out.println("Creating term chooser");
        //termChooserDialog = new TermChooserDialog(this,true); // Modal
        // DataManager.getInstance().addTermsChangeListener(termChooserDialog);
        //prefsDialog = new PreferencesDialog(this,true); // Modal
        
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // We handle this ourselves        
        ApplicationManager.getInstance().addWindowListChangeListener(this);
        
        // create the hidden (MVD) filechooser
        fileChooser = new JFileChooser(MedViewDataHandler.instance().getUserHomeDirectory());
//        fileChooser.setFileFilter(new DialogDirectoryFileFilter());
        fileChooser.setMultiSelectionEnabled(false);
        //fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // if directories_only, directories that aren't traversable aren't shown
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setFileView(new medview.common.fileview.MedViewFileView());        
        fileChooser.setCurrentDirectory(new File(settings.getMVDFileChooserDir()));
        
        // create the translator filechooser
        translatorChooser = new JFileChooser();
        translatorChooser.setDialogTitle("Choose a translator");
        translatorChooser.setMultiSelectionEnabled(false);
        File translatorFile = new File(settings.getTranslatorFilename());
        if (translatorFile.exists())
            translatorChooser.setSelectedFile(translatorFile);
        
        // create the template filechooser
        templateChooser = new JFileChooser();
        templateChooser.setDialogTitle("Choose a template");
        templateChooser.setMultiSelectionEnabled(false);
        File templateFile = new File(settings.getTemplateFilename());
        if (templateFile.exists())
            templateChooser.setSelectedFile(templateFile);
                
        // Setup term definitions and locations in the MVDH
        String tValueLocation = settings.getTermValueLocation();
        String tDefinitionLocation = settings.getTermDefinitionLocation();
        if (tValueLocation != null) {
            if (new File(tValueLocation).exists())
                MedViewDataHandler.instance().setTermValueLocation(tValueLocation);        
        }
        if (tDefinitionLocation != null) {
            if (new File(tDefinitionLocation).exists())
            MedViewDataHandler.instance().setTermDefinitionLocation(tDefinitionLocation);
        }
        
        progressSources = new HashMap();
        
        // initialize the components
        System.out.print("Initializing components...");
        System.out.flush();
        initComponents();
        System.out.println("Done.");
        if (settings.getWindowSystem() == Settings.WINDOWSYSTEM_MDI) {            
            makeMDIFrame();            
        } else {            
            makeFreeFrame();            
        }
                
        appManager.addWindowSystemChangeListener(this);                
        
        // Listen to the DataManager for selection changes and data changes
        DataManager.getInstance().addSelectionListener(this);
        DataManager.getInstance().addChangeListener(this);
        //DataManager.getInstance().addProgressListener(this); // Timer and getProgress instead
        ApplicationManager.getInstance().addProgressListener(this);                                
        
    }

    /**
     * Returns a reference to the only instance of the application frame.
     * 
     * @return A reference to the application frame.
     */
    public static ApplicationFrame getInstance() {
        if (theInstance == null) {
            theInstance = new ApplicationFrame();
        }
        return theInstance;
    }
    
    /**
     * Initializes the compnents of the frame.
     */
    private void initComponents() {        
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int menuKeyMask = toolkit.getMenuShortcutKeyMask();        
        
        // the file menu
        openMVDMenuItem = new JMenuItem("Open examinations from file");
        openMVDMenuItem.setMnemonic('O');
        openMVDMenuItem.setAccelerator(KeyStroke.getKeyStroke('O',menuKeyMask));
        openMVDMenuItem.addActionListener(this);
        
        openMedServerMenuItem = new JMenuItem("Load from MedServer");
        openMedServerMenuItem.setMnemonic('M');
        openMedServerMenuItem.addActionListener(this);
        openMedServerMenuItem.setAccelerator(KeyStroke.getKeyStroke('M',menuKeyMask));
        
        openDBMenuItem = new JMenuItem("Load from networked database");
        openDBMenuItem.setMnemonic('D');
        openDBMenuItem.addActionListener(this);
        
        JMenuItem exportMVDMenuItem = new JMenuItem(exportSelectionToMVDaction);
        exportMVDMenuItem.setMnemonic('E');        
        
        preferencesMenuItem = new JMenuItem("Preferences...");
        preferencesMenuItem.setMnemonic('P');
        preferencesMenuItem.addActionListener(this);        
                
        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setMnemonic('x');        
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
        exitMenuItem.addActionListener(this);
        
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        
        fileMenu.add(openMVDMenuItem);
        fileMenu.add(openMedServerMenuItem);
        fileMenu.add(openDBMenuItem); 
        
        fileMenu.addSeparator();
        
        fileMenu.add(exportMVDMenuItem);                                
        
        fileMenu.addSeparator();
        
        fileMenu.add(preferencesMenuItem);        
        
        fileMenu.addSeparator();
        
        fileMenu.add(exitMenuItem);
        
        // The Edit menu
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');
        
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("Paste");
        JMenuItem cutItem = new JMenuItem("Cut");
        JMenuItem findItem = new JMenuItem(findAction);
        
        copyItem.setEnabled(false);
        pasteItem.setEnabled(false);
        cutItem.setEnabled(false);
        
        copyItem.setMnemonic('C');
        cutItem.setMnemonic('T');
        pasteItem.setMnemonic('P');
        findItem.setMnemonic('F');
        
        cutItem.setAccelerator(KeyStroke.getKeyStroke('X',menuKeyMask));
        copyItem.setAccelerator(KeyStroke.getKeyStroke('C',menuKeyMask));
        pasteItem.setAccelerator(KeyStroke.getKeyStroke('P',menuKeyMask));
        findItem.setAccelerator(KeyStroke.getKeyStroke('F',menuKeyMask));
        
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.addSeparator();
        editMenu.add(findItem);
        
        // the Terms menu                
        
        termsMenuItem = new JMenuItem("Global Term Chooser...");
        termsMenuItem.setMnemonic('T');
        termsMenuItem.addActionListener(this);                        
        
        termsMenu = new JMenu("Terms");
        termsMenu.setMnemonic('T');                        
        termsMenu.add(termsMenuItem);                
        
        // aggregates menu        
        aggregatesMenu = new JMenu("Aggregations");
        aggregatesMenu.setMnemonic('A');
                
        loadAggregatesMenuItem = new JMenuItem("Load aggregation...");
        loadAggregatesMenuItem.setMnemonic('L');
        loadAggregatesMenuItem.addActionListener(this);
        aggregatesMenu.add(loadAggregatesMenuItem);
        
        aggregatesMenu.addSeparator();
        
        aggregationLibraryMenuItem = new JMenuItem("Aggregation library");
        aggregationLibraryMenuItem.setMnemonic('A');
        aggregationLibraryMenuItem.addActionListener(this);
        aggregatesMenu.add(aggregationLibraryMenuItem);        
        
        aggregatesMenu.addSeparator();
        
        startAggregatorMenuItem = new JMenuItem("Aggregation Editor");
        startAggregatorMenuItem.setMnemonic('E');
        startAggregatorMenuItem.addActionListener(this);
        aggregatesMenu.add(startAggregatorMenuItem);
                        
        // the windows menu
        windowsMenu = new JMenu("Windows");
        windowsMenu.setMnemonic('W');        
        rebuildWindowsMenu(); // Also adds data group item etc
        
        // the help menu
        aboutMenuItem = new JMenuItem("About " + ApplicationManager.APPLICATION_NAME + "...");
        aboutMenuItem.setMnemonic('A');
        aboutMenuItem.addActionListener(this);
        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');       
        helpMenu.add(aboutMenuItem);
        
        // the menu bar
        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(termsMenu);
        menuBar.add(aggregatesMenu);
        menuBar.add(windowsMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        // the toolbar
        toolBar = new JToolBar();
        
        // create the chartbuttons
        Vector v = new Vector();        
        
        v.add(ChartButtonFactory.createChartButton(new ScatterPlotViewFactory("P-code", "Age", false))); // (x-term, y-term, percent)
        v.add(ChartButtonFactory.createChartButton(new BarChartViewFactory("P-code", true, true, false)));
        v.add(ChartButtonFactory.createChartButton(new BarChartViewFactory("P-code", false, true, false)));
        v.add(ChartButtonFactory.createChartButton(new TableViewFactory()));
        v.add(ChartButtonFactory.createChartButton(new StatisticsViewFactory("Age")));
        v.add(ChartButtonFactory.createChartButton(new ImageViewFactory()));
        v.add(ChartButtonFactory.createChartButton(new SummaryViewFactory()));
        v.add(new ExaminationDropTargetButton("Export to MVD","exportMVD24.png") {                
            public void examinationsDropped(ExaminationDataElementVector elementVector)
            {
                chooseTargetAndExportMVD(elementVector.toArray());
            }
            
            public void buttonClicked() 
            {
                int result = JOptionPane.showConfirmDialog(ApplicationFrame.this,
                                                           "Export current selection?",
                                                           "No examinations dragged",
                                                           JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION)
                {
                    chooseTargetAndExportSelectionToMVD();
                }                                          
            }
        });
        
        // add the listeners and transferhandlers to the buttons, and the buttons to the toolbar
        for (Iterator it = v.iterator(); it.hasNext();)
        {
            ExaminationDropTargetButton nextButton = 
                (ExaminationDropTargetButton) it.next();
            
            //nextButton.addActionListener(this);        
            nextButton.setTransferHandler(new ChartButtonTransferHandler());                                        
            toolBar.add(nextButton);
        }
        
        toolBar.addSeparator();
        
        // Memory meter
        
        memoryPanel = new misc.gui.components.MemoryPanel(15); // update with 15 seconds interval
        toolBar.add(memoryPanel);        
        
        // the desktop pane
        desktopPane = new JDesktopPane(); 
        desktopPane.setDragMode(JDesktopPane.LIVE_DRAG_MODE);
        ApplicationManager.getInstance().setDesktop(desktopPane);
        
        // the status bar
        statusBarSelectionLabel = new JLabel();
        statusBarTextLabel = new JLabel();
        
        statusBar = new StatusBar(); // extends JToolBar
        statusBar.setFloatable(false);        
        statusBar.add(statusBarSelectionLabel);        
        statusBar.addSeparator();
        statusBar.add(statusBarTextLabel);
        
        statusBarProgressBar = new JProgressBar();
        
        updateStatusBar();
        
        // change the behaviour of the window's close button
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                closeApplication();
            }
        });
        
        // add components to the content pane        
        Container contentPane = getContentPane();
        contentPane.add(toolBar, BorderLayout.NORTH);
        contentPane.add(statusBar,BorderLayout.SOUTH);
    
        // create the data group floater panel
        dataGroupPanel = new DataGroupPanel(DataManager.getInstance().getDataGroups());
        DataManager.getInstance().addDataGroupListener(dataGroupPanel);
        
        // create the query floater panel
        queryPanel = new QueryPanel();
        
        // create the message panel
        
        // create the tool box panel
        toolBoxPanel = new ToolBoxComponent(ApplicationManager.getInstance()); // ApplicationManager for tools
        
        messagePanel = new MessagePanel();
        messagePanel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ce) {
                appManager.getMessageFloater().setVisible(true);
            }
        });
        
        /* The database dialog component */
        databaseDialog = new DatabaseDialog(this);                                       
    }   

    /**
     * Rebuilds the window menu.
     */
    private void rebuildWindowsMenu() {
        windowsMenu.removeAll();
        
        
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // ActionListener for the windows menu
                Object source = ev.getSource();
                if (source == toolBoxMenuItem) {
                    appManager.getToolBoxFloater().setVisible(toolBoxMenuItem.getState());
                } else if (source == dataGroupsMenuItem) {
                    appManager.getDataGroupFloater().setVisible(dataGroupsMenuItem.getState());
                } else if (source == queryMenuItem) {
                    appManager.getQueryFloater().setVisible(queryMenuItem.getState());
                } else if (source == messageFloaterMenuItem) {
                    appManager.getMessageFloater().setVisible(messageFloaterMenuItem.getState());                   
                } else if (source instanceof ViewFrameMenuItem) { // menu item for a view
                    // Put that view on top and focus it. 
                    ViewFrame viewFrame = ((ViewFrameMenuItem) source).getViewFrame();
                    viewFrame.toFront();
                    viewFrame.requestFocus();
                } else {
                    errorMessage("Error: ApplicationFrame.windowmnu private action listener: unknown source");
                }                  
            }
        };
        
        // Toolbox
        toolBoxMenuItem = new JCheckBoxMenuItem("Toolbox", settings.isFloaterVisible("toolbox"));        
        toolBoxMenuItem.setMnemonic('T');
        toolBoxMenuItem.addActionListener(actionListener);
        if (settings.isFloaterTypeAvailable(Floater.FLOATER_TYPE_TOOLBOX)) {
            windowsMenu.add(toolBoxMenuItem);
        }
        
        // Data groups
        dataGroupsMenuItem = new JCheckBoxMenuItem("Data groups", settings.isFloaterVisible("datagroups"));
        dataGroupsMenuItem.setMnemonic('D');
        dataGroupsMenuItem.addActionListener(actionListener);                
        windowsMenu.add(dataGroupsMenuItem);
        
        // Query
        queryMenuItem = new JCheckBoxMenuItem("Query", settings.isFloaterVisible("query"));
        queryMenuItem.setMnemonic('Q');
        queryMenuItem.addActionListener(actionListener);
        windowsMenu.add(queryMenuItem);
        
        // Messages
        messageFloaterMenuItem = new JCheckBoxMenuItem("Messages", settings.isFloaterVisible("messages"));
        messageFloaterMenuItem.setMnemonic('M');
        messageFloaterMenuItem.addActionListener(actionListener);
        windowsMenu.add(messageFloaterMenuItem);
        
        windowsMenu.add(new JSeparator());
        
        // Add all active windows
        ViewFrame[] viewFrames = ApplicationManager.getInstance().getViewFrames();
        
        // Make menu items for the viewframes
        final int radix = 36;
        for (int i = 0; i < viewFrames.length; i++) {                        
            ViewFrameMenuItem frameItem = null;            
            int windowNumber = i+1; // count from 1
            if (windowNumber <= radix) {               
                char windowDigitCharacter = Character.toUpperCase(Character.forDigit(windowNumber,radix)); // (1-10 + 26 A-Z)                
                frameItem = new ViewFrameMenuItem(windowDigitCharacter,viewFrames[i]);
            } else {
                frameItem = new ViewFrameMenuItem(viewFrames[i]); // No mnemonic etc
            }                                        
            
            frameItem.addActionListener(actionListener);
            windowsMenu.add(frameItem);                        
        }
                
    }
        
    
    /**
     * Turn the frame into an MDI frame.
     */
    private void makeMDIFrame() {
        
        // Store the positioning of the 'free' frame
        settings.setWindowPosition(getLocation());
        
        // Change the floater factory
        floaterFactory = new InternalFloaterFactory();
               
        //getContentPane().add(desktopPane, BorderLayout.CENTER);  // 
        
        scrollPane = new JScrollPane();
        JViewport viewport = new JViewport();
        viewport.setView(desktopPane);
        scrollPane.setViewport(viewport); 

        
        getContentPane().add(scrollPane, BorderLayout.CENTER);  //
        
        
        Vector mdiViewFrames = new Vector();
        
        // create new internal ViewFrames from the free ViewFrames
        
        ViewFrame[] frames = appManager.getViewFrames();
        //for(Iterator i = appManager.getViewFrames().iterator(); i.hasNext(); ) {                
        for (int i = 0; i < frames.length; i++) {
            ViewFrame viewFrame = frames[i];
            View view = viewFrame.getView();
            Dimension size = viewFrame.getSize();
            Point location = viewFrame.getLocationOnScreen();
            Point desktopLocation = desktopPane.getLocationOnScreen();
            location.translate(-desktopLocation.x,-desktopLocation.y);
            viewFrame.closeFrame();
            ViewFrame internalFrame = ViewFrameFactory.createViewJInternalFrame(view);
            internalFrame.setLocation(location);
            internalFrame.setSize(size);
            mdiViewFrames.add(internalFrame);
        }
        
        // remove the free ViewFrames from the ApplicationManager
        appManager.clearViewFrames();
        
        // add the new internal ViewFrames to the JDesktopPane and to the ApplicationManager
        for(Iterator i = mdiViewFrames.iterator(); i.hasNext(); ) {
            ViewFrame viewFrame = (ViewFrame)i.next();
            viewFrame.setVisible(true);
            desktopPane.add((JInternalFrame)viewFrame);
            appManager.addViewFrame(viewFrame);
        }

        // Close old Free floaters and add new MDI ones        
        appManager.setToolBoxFloater(closeFreeFloaterAndMakeMDIFloater(appManager.getToolBoxFloater(),toolBoxPanel));            
        appManager.setDataGroupFloater(closeFreeFloaterAndMakeMDIFloater(appManager.getDataGroupFloater(),dataGroupPanel));        
        appManager.setQueryFloater (closeFreeFloaterAndMakeMDIFloater(appManager.getQueryFloater(),queryPanel) );
        appManager.setMessageFloater (closeFreeFloaterAndMakeMDIFloater(appManager.getMessageFloater(),messagePanel));
        
        // Place the MDI frame        
        setSize(settings.getWindowSize());
        setLocation(settings.getWindowPosition());                        
    }    
      
    /**
     * Close an old Free floater and create a new MDI type floater 
     * @param floater the old floater to destroy
     * @param type the floater type 
     * @return the new floater of the new type
     */
    
     private Floater closeFreeFloaterAndMakeMDIFloater(Floater floater, FloaterComponent component) {
        boolean visible;
        Point location;        
        int floaterType = component.getFloaterType();
        String floaterTypeName = FloaterFactory.getFloaterTypeString(floaterType);
        if (floater != null) {
            location = floater.getLocation();
            location.translate(-getLocation().x, -getLocation().y);
            location.x = Math.max(0, location.x);
            location.y = Math.max(0, location.y);
            
            // get floater visibility
            visible = floater.isVisible() && settings.isFloaterTypeAvailable(floaterType);
 
           // close the old floater
            floater.closeFloater();             
        } else {
            // problem: typ okänd!
            location = settings.getFloaterLocation(floaterTypeName);
            visible = settings.isFloaterVisible(floaterTypeName) && settings.isFloaterTypeAvailable(floaterType);
        }
        
        //floater = floaterFactory.createToolBoxFloater(ApplicationManager.getInstance(),toolBoxLocation);
        floater = floaterFactory.createFloater(component,location);
        desktopPane.add((JInternalFrame)floater);
        
        Dimension size = floater.getSize();
        //resize the desktop pane if needed so that all windows are accessible
        int desktopW = Math.max(desktopPane.getPreferredSize().width,location.x+size.width);
        int desktopH = Math.max(desktopPane.getPreferredSize().height,location.y+size.height);

        //for some reason the data groups window reports a too small size
        if(floater.getName().equals("datagroups"))
        {
            desktopW = Math.max(desktopPane.getPreferredSize().width,location.x+size.width+60);
            desktopH = Math.max(desktopPane.getPreferredSize().height,location.y+size.height+26);
        }
        desktopPane.setPreferredSize(new Dimension(desktopW,desktopH));

        floater.setVisible(visible);
        floater.addFloaterListener(this);
        return floater;
    }
    
    

    
    /**
     * Turns the frame into a free frame.
     */
    private void makeFreeFrame() {
        
        // Store the position of the MDI frame
        settings.setWindowPosition(getLocation());

        // Set the new position
        setLocation(settings.getWindowPosition());        
        
       // Change the floater factory
        floaterFactory = new FreeFloaterFactory();
        
        Vector freeViewFrames = new Vector();        
        
        //for(Iterator i = appManager.getViewFrames().iterator(); i.hasNext(); ) {
        
        ViewFrame[] viewFrames = appManager.getViewFrames();
        for (int i = 0; i < viewFrames.length; i++) {
            //ViewFrame viewFrame = (ViewFrame)i.next();
            ViewFrame viewFrame = viewFrames[i];
            View view = viewFrame.getView();
            Dimension size = viewFrame.getSize();
            // Point location = viewFrame.getLocationOnScreen();
            Point location = viewFrame.getLocation();
            location.translate(desktopPane.getLocationOnScreen().x, getLocationOnScreen().y);
            viewFrame.closeFrame();  
            ViewFrame freeFrame = ViewFrameFactory.createViewJFrame(view);
            freeFrame.setSize(size);
            freeFrame.setLocation(location);
            freeViewFrames.add(freeFrame);
            
        }
        
        // Remove the desktop pane
        getContentPane().remove(desktopPane);
        
        appManager.clearViewFrames();
                
        for(Iterator i = freeViewFrames.iterator(); i.hasNext(); ) {
        
            ViewFrame viewFrame = (ViewFrame)i.next();
            viewFrame.setVisible(true);
            appManager.addViewFrame(viewFrame);
        }
        
        // Close old MDI floaters and add new Free ones
        appManager.setToolBoxFloater(closeMDIFloaterAndMakeFreeFloater(appManager.getToolBoxFloater(),toolBoxPanel));            
        appManager.setDataGroupFloater(closeMDIFloaterAndMakeFreeFloater(appManager.getDataGroupFloater(),dataGroupPanel));        
        appManager.setQueryFloater (closeMDIFloaterAndMakeFreeFloater(appManager.getQueryFloater(),queryPanel) );
        appManager.setMessageFloater (closeMDIFloaterAndMakeFreeFloater(appManager.getMessageFloater(),messagePanel));
        // Place the free frame
        pack();
    }
            
    private Floater closeMDIFloaterAndMakeFreeFloater(Floater floater, FloaterComponent component) {

        Point location; 
        boolean visible;
        int floaterType = component.getFloaterType();
        String floaterTypeString = FloaterFactory.getFloaterTypeString(floaterType);

        // remove the old floater
        if (floater != null) {
            location = getLocation();               
            location.translate(floater.getLocation().x, floater.getLocation().y);
            visible = settings.isFloaterTypeAvailable(floaterType) && floater.isVisible();
            floater.closeFloater();
        } else {
            location = settings.getFloaterLocation( floaterTypeString );
            visible = settings.isFloaterVisible(floaterTypeString) && settings.isFloaterTypeAvailable(floaterType);
        }       

        floater = floaterFactory.createFloater(component, location);
        //toolBoxFloater = floaterFactory.createToolBoxFloater(toolBoxPanel,toolBoxLocation);
        //System.out.println("Free floater " + floaterTypeString + " visible: " + visible);
        floater.setVisible(visible);
        floater.addFloaterListener(this);   

        return floater;
    }
    
    
    /**
     * Displays a ViewFrame with a View.
     *
     * @param view The View to display.
     */
    public void showView(View view) {
        ViewFrame viewFrame = null;
        if(settings.getWindowSystem() == Settings.WINDOWSYSTEM_MDI) {
            viewFrame = ViewFrameFactory.createViewJInternalFrame(view);
            viewFrame.setSize(STANDARD_VIEW_DIMENSION);
            viewFrame.setVisible(true);
            desktopPane.add((JInternalFrame)viewFrame);
            try {
                ((JInternalFrame)viewFrame).setSelected(true);
            } catch (java.beans.PropertyVetoException e) {}
        } else {
            viewFrame = ViewFrameFactory.createViewJFrame(view);            
            viewFrame.setSize(STANDARD_VIEW_DIMENSION);
            viewFrame.setVisible(true);
        }
        
        appManager.addViewFrame(viewFrame);
    }

    /**
     * Displays a ViewFrame with a View at a specified location
     * and with a specified size.
     *
     * @param view The View to display.
     * @param location The location of the ViewFrame.
     * @param size The size of the ViewFrame.
     */
    public void showView(View view, Point location, Dimension size) {
        ViewFrame viewFrame = null;
        if(settings.getWindowSystem() == Settings.WINDOWSYSTEM_MDI) {
            viewFrame = ViewFrameFactory.createViewJInternalFrame(view);
            viewFrame.setLocation(location);
            viewFrame.setSize(size);
            viewFrame.setVisible(true);
            desktopPane.add((JInternalFrame)viewFrame);
            
            //resize the desktop pane if needed so that all windows are accessible
            int desktopW = Math.max(desktopPane.getPreferredSize().width,location.x+size.width);
            int desktopH = Math.max(desktopPane.getPreferredSize().height,location.y+size.height);
            desktopPane.setPreferredSize(new Dimension(desktopW,desktopH));
            
            try {
                ((JInternalFrame)viewFrame).setSelected(true);
            } catch (java.beans.PropertyVetoException e) {}
        } else {
            viewFrame = ViewFrameFactory.createViewJFrame(view);            
            viewFrame.setLocation(location);
            viewFrame.setSize(size);
            viewFrame.setVisible(true);
        }
        
        appManager.addViewFrame(viewFrame);
    }
    
    
    /** 
     * Exit the Application.
     */
    private void closeApplication() {
        int result = JOptionPane.showConfirmDialog(this,"Really quit?","Confirm exit",JOptionPane.YES_NO_OPTION);
        switch (result) {
            case JOptionPane.YES_OPTION:
                
                /* Exit */                                
                settings.setWindowPosition(getLocationOnScreen()); // save window position                
                
                settings.setChosenTerms(DataManager.getInstance().getTermContainer().getChosenTerms()); // Save chosen terms

                // save floaters visibility and locations
                settings.setFloaterVisible(appManager.getQueryFloater().getName(), appManager.getQueryFloater().isVisible());
                settings.setFloaterVisible(appManager.getDataGroupFloater().getName(), appManager.getDataGroupFloater().isVisible());
                settings.setFloaterVisible(appManager.getToolBoxFloater().getName(), appManager.getToolBoxFloater().isVisible());
                settings.setFloaterVisible(appManager.getMessageFloater().getName(), appManager.getMessageFloater().isVisible());
                
                settings.setFloaterLocation(appManager.getQueryFloater().getName(), appManager.getQueryFloater().getLocation());
                settings.setFloaterLocation(appManager.getDataGroupFloater().getName(), appManager.getDataGroupFloater().getLocation());
                settings.setFloaterLocation(appManager.getToolBoxFloater().getName(), appManager.getToolBoxFloater().getLocation());                
                settings.setFloaterLocation(appManager.getMessageFloater().getName(), appManager.getMessageFloater().getLocation());
                
                // save window size if we're in MDI mode
                if (settings.getWindowSystem() == Settings.WINDOWSYSTEM_MDI) {        
                    settings.setWindowSize(getSize());                    
                } else if (settings.getWindowSystem() == Settings.WINDOWSYSTEM_FREE) {
                    // Dispose all windows so that the application exits properly
                    appManager.getToolBoxFloater().closeFloater();
                    appManager.getDataGroupFloater().closeFloater();
                    appManager.getMessageFloater().closeFloater();
                }

                // save the session
                String medviewPath = MedViewDataHandler.instance().getUserHomeDirectory();
                java.io.File visualizerDir = new File(medviewPath + "/visualizer");
                if (!visualizerDir.exists()) {
                    if (!visualizerDir.mkdirs()) {
                        errorMessage("Could not create directory for saving session.");
                    }
                }
                
                
                if (visualizerDir.exists()) {
                    java.io.File sessionFile = new File(visualizerDir.getPath() + "/session.cfg");
                    ProgressObject progress = appManager.saveSession(sessionFile);
                    showProgressDialog(progress, "Saving Session");
                    
                    
                    while (progress.isRunning() && !progress.isCancelled()) {
                        synchronized(this) {
                            try {
                                ApplicationManager.debug("Waiting for saveSession...");
                                wait(1000);
                            } catch (InterruptedException ie) { }
                        }
                    }
                }                        
                
                // Wait for savesession to finish                
                
                // close down all view frames
                appManager.disposeAllViewFrames();                                
                dispose();  // Exit  
                
                synchronized(this) {
                    try {
                        wait(2000); // Wait for 2 seconds to let writes finish
                    } catch(InterruptedException ie) { } // Who cares?
                }
                
                System.exit(0);
                
                break;
            default: // Everything else but YES
                // Do nothing.
                break;
        } // end switch result          
    }
            
    /**
     * Starts the program.
     *
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        ApplicationFrame thisFrame = getInstance();
        thisFrame.show();
        thisFrame.restoreSession();
        thisFrame.checkMedViewUserId(); // Pops up preferences if needed
    }

    /**
     * Checks if a MedViewUserId needs to be set, and if it does pops up the preferences
     */
    private void checkMedViewUserId()
    {
        // Check if we need to start by showing the preferences UI...
        String currentUserID = MedViewDataHandler.instance().getUserID();                                
        
        if ((currentUserID == null) || (currentUserID.equals("")))
        {
            JOptionPane.showMessageDialog(this, "Please set a MedView User ID", "No User ID set", JOptionPane.ERROR_MESSAGE);
            showPreferences(1); // 1 = Data tab            
        }
    }
    
    
    /** 
     * Called when the window system has changed from MDI to
     * free windows or the other way around.
     *
     * @param e An event object containing the source of the event.
     */
    public void windowSystemChanged(WindowSystemChangeEvent e) {
        if (e.getWindowSystem() == Settings.WINDOWSYSTEM_MDI) {
            makeMDIFrame();
            validate();
            repaint();
        } else {
            makeFreeFrame();
            validate();
            repaint();
        }
    }    
    
    /**
     * The implementation of the ActionListener interface.
     *
     * @param e The event to handle.
     */
    public void actionPerformed(ActionEvent e)
    {
        
        Object eventSource = e.getSource();
        
        if (eventSource == openMVDMenuItem)
        {
            chooseAndLoadMVD();
        } 
        else if (eventSource == openMedServerMenuItem)
        {
            loadFromMedServer();
        }
        else if (eventSource == openDBMenuItem)
        {
            chooseAndLoadFromDB();        
        } 
        else if (eventSource == exitMenuItem)
        {
            closeApplication();                    
        } 
        else if (eventSource == termsMenuItem) 
        {
            TermChooserDialog termChooserDialog 
                = new TermChooserDialog(this, 
                                        DataManager.getInstance().getTermContainer(), // term container
                                        "Global active term chooser",
                                        true); // modal
            termChooserDialog.setVisible(true);                
        } else if (eventSource == preferencesMenuItem) { showPreferences(); }
        else if (eventSource == dataGroupsMenuItem) {            
            // empty for now
        } else if (eventSource == aboutMenuItem) 
        {
            AboutDialog aboutDialog = new AboutDialog(this,true); // modal
            aboutDialog.show();                  
        } else if (eventSource == loadAggregatesMenuItem) 
            { chooseAndLoadAggregates(); }
        else if (eventSource == startAggregatorMenuItem) 
            { showAggregator(); }
        else if (eventSource == aggregationLibraryMenuItem)
        {
            AggregationLibraryDialog.showAggregationLibraryDialog(this, // parent frame
                                     DataManager.getInstance(), // aggregation container
                                     null); // initial aggregation                    
        } /*else if (eventSource instanceof ChartButton) { // click on ChartButton
            ExaminationDataElementVector elementVector = new ExaminationDataElementVector();
            ((ChartButton)e.getSource()).examinationsDropped(elementVector);
        }*/
    }

    /**
     * Shows a dialog letting the user choose aggregates to load.
     */
    private void chooseAndLoadAggregates() {
            
        medview.aggregator.DirectoryFileView  fw = new medview.aggregator.DirectoryFileView("mvg") ;
                    
        medview.aggregator.CategoryFilter aFilter = new medview.aggregator.CategoryFilter();
        
        if (aggregatesFileChooser == null) {
            aggregatesFileChooser = new medview.aggregator.DirectoryChooser(MedViewDataHandler.instance().getUserHomeDirectory(),fw);                    
            aggregatesFileChooser.setFileFilter(aFilter);  // set file extension filter
            aggregatesFileChooser.setCurrentDirectory(new File(settings.getAggregationFileChooserDir()));
            // aggregatesFileChooser.setFileView(new medview.common.fileview.MedViewFileView()); // not compatible yet
        }
        
        int res = aggregatesFileChooser.showOpenDialog(this);
        
        if (res ==  JFileChooser.APPROVE_OPTION){
            settings.setAggregationFileChooserDir(aggregatesFileChooser.getCurrentDirectory().getPath());
            File aFile = aggregatesFileChooser.getSelectedFile();
            // mOpenCategDir = aFile.getParent() ;
            String aName = aFile.getPath();
            if(!aName.endsWith(aFilter.getExtention() ) ){                
                JOptionPane.showMessageDialog(this,"Not an mvg directory!","Not MVG",JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                DataManager.getInstance().loadAggregation(aFile);                
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(this,"Aggregation loading failed: " + ioe.getMessage(),"Aggregation loading failed",JOptionPane.ERROR_MESSAGE);
            }                         
        }
    }
  
    /** 
     * Pop up a dialog to load from an SQL database
     */
    private void chooseAndLoadFromDB() {
        databaseDialog.show();                        
    }        
    
    
    /**
     * Show a data group chooser
     * @return the chosen data group
     */
    
    public DataGroup showDataGroupChooser()
    {
        return DataGroupSelectorDialog.showDataGroupSelectorDialog(this,new Point(100,100),"Choose a group for the data");
    }

    
    private void loadFromMedServer()
    {                        
        try 
        {            
            java.net.URI serverURI = new java.net.URI("medserver://" + settings.getMedServerHost());

            ApplicationManager.getInstance().chooseDataGroupThenStartLoading(serverURI);                        
        } 
        catch (java.net.URISyntaxException URIse)
        {
            JOptionPane.showMessageDialog(this,"Failed to load examination data because: URISyntaxException:" + URIse.getMessage());                    
        }   
    }
   
    /**
     * Lets the user choose an archive of examination data and data group and then
     * all data in that archive gets loaded into the data group.     
     */
    private void chooseAndLoadMVD()
    {
        // show a filechooser
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) 
        {
            File dir = fileChooser.getSelectedFile();            
            settings.setMVDFileChooserDir(fileChooser.getCurrentDirectory().getPath());            
                        
            String uriScheme = "file"; 
            String path = dir.getPath();            
            path = path.replace('\\', '/'); // Convert \ to /
            path = "/" + path;
            
            // Do uri-encoding so that we won't run into errors with spaces etc
            
            
            try {                
                
                // Use the long constructor which will quote (convert to %xx) illegal URI chars.
                java.net.URI url = new java.net.URI(uriScheme, 
                                                    "//" + path, // scheme-specific part
                                                    null); // fragment
                
                ApplicationManager.getInstance().chooseDataGroupThenStartLoading(url);            
            } catch (java.net.URISyntaxException URIse) {
                JOptionPane.showMessageDialog(this,"Failed to load examination data because: URISyntaxException:" + URIse.getMessage());                    
            }            
                             
            ApplicationManager.debug("ApplicationFrame.loadData(): exited properly from loadExaminationData()");
            
        }
    }
    
    /**
     * Called by the DataManager when the data has changed.
     *
     * @param event The event.
     */
    public void datasetChanged(DatasetChangeEvent event) {
        statusBarInvalid = true;
    }
    
    /**
     * Called by the DataManager when the selection has changed.
     *
     * @param event The event.
     */
    public void selectionChanged(SelectionEvent event) {
        statusBarInvalid = true;
    }

    /**
     * 
     */
    public void setStatusText(String newText) {
        statusMessage(newText);
    }        
    
    public void setStatusBarProgress(int min, int current, int max) {
        //ApplicationManager.debug("Statusbarprogress: min = " + min + ", curr = " + current + ", max = "+ max); // debug
        statusBarProgressBar.setMinimum(min);
        statusBarProgressBar.setMaximum(max);
        statusBarProgressBar.setValue(current);
        if (current >= max)
            hideStatusBarProgressBar();
        else
            showStatusBarProgressBar();
    }
    
    
    
    private void hideStatusBarProgressBar() {
        statusBar.remove(statusBarProgressBar);
        statusBarProgressBarVisible = false;        
        statusBar.revalidate();
        statusBar.repaint();
    }
    
    private void showStatusBarProgressBar() {
        if (statusBarProgressBarVisible == false) {
            statusBar.add(statusBarProgressBar);
            statusBarProgressBarVisible = true;            
        }
        statusBar.revalidate();
        statusBar.repaint();
    }
        
    /**
     * Updates the statusbar with the current data and selection info.
     */ 
    public void updateStatusBar() {          
        DataManager DM = DataManager.getInstance();
        statusBarSelectionLabel.setText("Selected: " + DM.getSelectedItemCount() + " / " + DM.getTotalItemCount());
        statusBarInvalid = false;
    }
    
    /**
     * Show a progress monitor with the progress of loading data.
     *
     * @param pe The progress event.
     */
    public void progressMade(ProgressEvent pe) {
        ProgressMonitor monitor;
        if (progressSources.containsKey(pe.getSource())) {
            monitor = (ProgressMonitor) progressSources.get(pe.getSource());
        } else {
            setEnabled(false);
            monitor = new ProgressMonitor(this, pe.getMessage(), pe.getNote(), pe.getMinimum(), pe.getMaximum()) {
                public void close() {                    
                    ApplicationFrame.getInstance().setEnabled(true);
                    super.close();
                    // Workaround for the problem that when the user cancels the progressdialog, then
                    // the ApplicationFrame is sent to the back.
                    ApplicationFrame.getInstance().requestFocus();
                }
            };
            progressSources.put(pe.getSource(), monitor);
        }                
        if (monitor.isCanceled()) {
            monitor.close();
            Object source = pe.getSource();
            progressSources.remove(source); // make sure to use a new progress monitor the next time
            if (source instanceof ProgressSubject) {
                ((ProgressSubject)source).cancelProgressOperation();
            }
        } else {                    
            monitor.setNote(pe.getNote());
            monitor.setProgress(pe.getProgress());
            if (pe.getProgress() >= pe.getMaximum()) {
                monitor.close();
                // make sure to use a new progress monitor the next time
                progressSources.remove(pe.getSource());
            }
        }                
        
    }
    
    /**
     * Handles floaterClosing events and updates the menu items accordingly
     */
    public void floaterClosing(FloaterEvent fe) {
        Object source = fe.getSource();
        if (source == appManager.getToolBoxFloater()) {
            toolBoxMenuItem.setState(false);                
        } else if (source == appManager.getDataGroupFloater()) {
            dataGroupsMenuItem.setState(false);
        } else if (source == appManager.getQueryFloater()) {
            queryMenuItem.setState(false);
        } else if (source == messageFloaterMenuItem) {
            messageFloaterMenuItem.setState(false);
        }
    }
    
    
    /**
     * Called when the list of windows has changed.
     * 
     * @param event The event.
     */
    public void windowListChanged(WindowListChangeEvent event) {
        // rebuild the windowsMenu
        rebuildWindowsMenu();        
    }
        
    
    /**
     * Implementation of the WindowListener interface. Does nothing.
     *
     * @param event The event.
     */
    public void windowActivated(java.awt.event.WindowEvent event)
    {
    }
    
    /**
     * Called when a window is closed. Rebuilds the windows menu.
     *
     * @param event The event.
     */
    public void windowClosed(java.awt.event.WindowEvent event) {
        ApplicationManager.debug("A window was disposed");
        rebuildWindowsMenu();
    }
    
    /**
     * Called when a window is closing. Currently does nothing.
     *
     * @param event The event.
     */
    public void windowClosing(java.awt.event.WindowEvent event) {
        ApplicationManager.debug("A window is closing");
    }
    
    /**
     * Empty implementation of the WindowListener interface.
     *
     * @param windowEvent The event.
     */
    public void windowDeactivated(java.awt.event.WindowEvent windowEvent) {}
    
    /**
     * Empty implementation of the WindowListener interface.
     *
     * @param windowEvent The event.
     */
    public void windowDeiconified(java.awt.event.WindowEvent windowEvent) {}    
    
    /**
     * Empty implementation of the WindowListener interface.
     *
     * @param windowEvent The event.
     */
    public void windowIconified(java.awt.event.WindowEvent windowEvent) {}    
    
    /**
     * Empty implementation of the WindowListener interface.
     *
     * @param windowEvent The event.
     */
    public void windowOpened(java.awt.event.WindowEvent windowEvent) {}

    /**
     * Sets the Look And Feel of the application.
     *
     * @param LAFClassName The Look And Feel class name.
     */
    public void setLookAndFeel(String LAFClassName) {
        try {
            UIManager.setLookAndFeel(LAFClassName);// Add your handling code here:
            
        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(this,"Class not found: " + LAFClassName,"ClassNotFoundException", JOptionPane.ERROR_MESSAGE);
        } catch (InstantiationException ie) {
            JOptionPane.showMessageDialog(this,"Could not instantiate class " + LAFClassName +". More info: " + ie.getMessage(),"InstantiationException", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalAccessException iae) {
            JOptionPane.showMessageDialog(this,"IllegalAccessException: " + iae.getMessage(),"IllegalAccessException", JOptionPane.ERROR_MESSAGE);
        } catch (UnsupportedLookAndFeelException ulafe) {
            JOptionPane.showMessageDialog(this,"Look and feel " + LAFClassName + " not supported.","Unsuppoerted Look and feel exception", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Pop up a progress dialog for an object, with the specified title. The dialog closes when the progress is done.
     */
    public void showProgressDialog(ProgressObject progressObject, String title) {
        TimedProgressDialog dialog = new TimedProgressDialog(progressObject, this, title);
        dialog.show();
    }
    
    /**
     * Makes sure that the information displayed in the frame is up to date.
     */
    public void validateFrame() {
        updateStatusBar();
        dataGroupPanel.validatePanel();
    }        
    
    /**
     * Shows a summary dialog. Mainly called from BarChart and ScatterPlot
     *
     * @param examinations The examinations.
     */
    public void showSummaryDialog(ExaminationDataElement[] examinations)
    {
        ApplicationManager.debug("showSummaryDialog() got " + examinations.length + " examinations.");
        
        LinkedHashSet idSet = new LinkedHashSet();
        LinkedHashSet containerSet = new LinkedHashSet();
        
        for (int i = 0; i < examinations.length; i++) {
            try {
                if (!idSet.contains(examinations[i].getExaminationIdentifier())) {
                    idSet.add(examinations[i].getExaminationIdentifier());
                    
                    ExaminationValueContainer evc = ((MedViewExaminationDataElement)examinations[i]).getExaminationValueContainer();
                    ValueContainer valueContainer = MedViewGeneratorUtilities.wrapExaminationValueContainer(evc);                     
                    containerSet.add(valueContainer);
                }
            } catch (IOException exc)
            {
                errorMessage("showSummaryDialog: Could not get examination identifier: " + exc.getMessage());
            }
        }
        
        if (templateFilename == null) 
        {
            JOptionPane.showMessageDialog(this, "Please set summary template", "Summary template missing", JOptionPane.ERROR_MESSAGE);
            showPreferences(SettingsDialog.DATA_TAB); // updates settings
            
            templateFilename = settings.getTemplateFilename();
            if (templateFilename.equals(null)) {
                return;
            }
        }

        if (translatorFilename == null)
        {
            JOptionPane.showMessageDialog(this, "Please set summary translator", "Summary translator missing", JOptionPane.ERROR_MESSAGE);
            showPreferences(SettingsDialog.DATA_TAB); // updates settings            
            translatorFilename = settings.getTranslatorFilename();
            if (translatorFilename.equals(null)) {
                return;
            }
        }
   
        
        try {
        
            ExaminationIdentifier[] ids = new ExaminationIdentifier[idSet.size()];
            ids = (ExaminationIdentifier[]) idSet.toArray(ids);
            
            ValueContainer[] containers = new ValueContainer[containerSet.size()];
            containers = (ValueContainer[]) containerSet.toArray(containers);
            
            TemplateModel templateModel = null;
            TranslatorModel translatorModel = null;
            
            try {
                templateModel = MedViewDataHandler.instance().loadTemplate(templateFilename);
            } catch(se.chalmers.cs.medview.docgen.misc.CouldNotLoadException exc) {
                errorMessage("The template " + templateFilename + " could not be loaded, please make sure to choose a valid template!\nError message: " + exc.getMessage());
                return;
            }
                
            try {
                translatorModel = MedViewDataHandler.instance().loadTranslator(translatorFilename);
            } catch(se.chalmers.cs.medview.docgen.misc.CouldNotLoadException exc) {
                errorMessage("The translator " + translatorFilename + " could not be loaded, please make sure to choose a valid translator! Error message: " + exc.getMessage());                
                return;
            }
                
            String[] sections = templateModel.getAllContainedSections();
            medview.common.generator.MedViewGeneratorEngineBuilder engineBuilder = new medview.common.generator.MedViewGeneratorEngineBuilder();
            engineBuilder.buildIdentifiers( ids ); // orsakar exception!!
            engineBuilder.buildValueContainers( containers );
	    engineBuilder.buildSections( sections );
	    engineBuilder.buildTemplateModel( templateModel );
	    engineBuilder.buildTranslatorModel( translatorModel );
            GeneratorEngine engine = engineBuilder.getEngine();
            StyledDocument document = engine.generateDocument();
            MedViewDialog summaryDialog = new TextDialog( this, // component
                                                          document, // styleddocument
                                                          "Summary", // title
                                                          false); // modal
            summaryDialog.show();
            
        /*} catch (se.chalmers.cs.medview.docgen.misc.CouldNotBuildEngineException exc) {  
            errorMessage("showSummaryDialog: CouldNotBuildEngineException: " + exc.getMessage());*/
        } catch (FurtherElementsRequiredException exc) {
            errorMessage("showSummaryDialog: FurtherElementsRequiredException: " + exc.getMessage());
        } catch (CouldNotGenerateException exc) {
            errorMessage("showSummaryDialog: CouldNotGenerateException: " + exc.getMessage());
        } /*catch (InvalidVersionException exc) {
            errorMessage("showSummaryDialog: InvalidVersionException: " + exc.getMessage());
        } */
    }
    
    /**
     * Shows a dialog allowing the user to select which template file to use.
     */
    /*
    public void showTemplateChooser() {
        int result = templateChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            templateFilename = templateChooser.getSelectedFile().getPath();
            ApplicationManager.getInstance().setTemplate(templateFilename);
            ApplicationManager.debug("Set template path to " + templateFilename);
        }
    }
     */

    /**
     * Shows a dialog allowing the user to select which translator file to use.
     */
    public void showTranslatorChooser() {
        int result = translatorChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            translatorFilename = translatorChooser.getSelectedFile().getPath();
            ApplicationManager.getInstance().setTranslator(translatorFilename);            
            ApplicationManager.debug("Set translator path to " + translatorFilename);
        }
    }

    
    
    /**
     * Restores the last session.
     */
    public void restoreSession() {
        String medviewPath = settings.getUserHomeDirectory();
        File sessionFile = new File(medviewPath + "/visualizer/session.cfg");
        if (sessionFile.exists()) {
            ApplicationManager.debug("getting progress");
            ProgressObject progress = ApplicationManager.getInstance().loadSession(sessionFile);
            ApplicationManager.debug("making dialog and showing it");
            showProgressDialog(progress, "Loading session");            
            ApplicationManager.debug("returned from show");
        }
    }
    
    /**
     * Send an error message to the message panel to be displayed
     * @param message the message
     */
    public void errorMessage(String message) {
        messagePanel.criticalMessage(message);
    }
    
    public void statusMessage(String message) {
        statusBarTextLabel.setText(message);
        messagePanel.message(message);
    }
    
    
    /**
     * Show the Aggregator application
     */
    private void showAggregator() {     
        // Get terms and values
        
        // Very inefficient implementation, but it gets the job done.
        
                        
        HashMap termMap = new HashMap();
        ExaminationDataElement[] elements = DataManager.getInstance().getAllElements();
        for (int i = 0; i < elements.length; i++) { // element loop
            String[] terms = elements[i].getTermsWithValues();
            for (int t = 0; t < terms.length; t++) { // term loop
                Vector valueVector = (Vector) termMap.get(terms[t]);
                if (valueVector == null) {
                    valueVector = new Vector();
                    termMap.put(terms[t], valueVector);                    
                }
                try {
                    String[] values = elements[i].getValues(terms[t], null); // null aggregation -> perform no aggregation
                    for (int k = 0; k < values.length; k++) {
                        valueVector.add(values[k]); // Set ensures only one of each value
                    }
                } catch (medview.datahandling.NoSuchTermException nste) {
                    // Doesn't make sense since getTermsWithValues() gave us that term
                    errorMessage("Strange behaviour in showAggregator(): getTermsWithValues gave term that didn't exist: " + nste.getMessage());
                }                
            }
        }
        
        
        medview.aggregator.AggregatorFrame frame = new medview.aggregator.AggregatorFrame();
        
        // Get all terms in alphabetical order
        Set termsSet = termMap.keySet();
        java.util.List termsList = new LinkedList(termsSet);
        Collections.sort(termsList);
        
        for (Iterator it = termsList.iterator(); it.hasNext();) { // Terms loop
            String nextTerm = (String) it.next();
            Vector valueVector = (Vector) termMap.get(nextTerm);
            Collections.sort(valueVector);
            LinkedHashSet valueSet = new LinkedHashSet(valueVector.size());
            valueSet.addAll(valueVector);
            frame.addTermValues(nextTerm, valueSet);
        }
                
        frame.fitToScreen();
        frame.setVisible(true);
    }
    
    /**
     * Popup a dialog to choose fields for a tableFile. 
     * @return whether it was closed with OK or not
     */
    public boolean chooseMHCFileFields(medview.datahandling.examination.tablefile.MHCTableFileExaminationDataHandler mhcHandler)
    {
        TableFileFieldChooserDialog fieldChooserDialog = new TableFileFieldChooserDialog(mhcHandler,
            this); // parent
        fieldChooserDialog.show(); // blocks
        return fieldChooserDialog.getOkClicked(); 
    }
    
    // Actions
    
    Action findAction = new AbstractAction("Find")
    {
        public void actionPerformed(ActionEvent ae) 
        {
            // show searchdialog
            final SearchDialog searchDialog = new SearchDialog(ApplicationFrame.this, // parentframe
                                                         DataManager.getInstance()); // aggregationcontainer
            int result = searchDialog.showDialog();
            if (result == JOptionPane.OK_OPTION)
            {
                // do the search                
                
                
                final DefaultProgressObject progressObject = new DefaultProgressObject(0,DataManager.getInstance().getTotalItemCount(),0,false); // not indeterminate 
                progressObject.setDescription("Searching");
                
                Thread searchThread = new Thread("SearchThread") 
                {                    
                    public void run()
                    {
                        ExaminationDataElement[] matchElements = 
                            DataManager.getInstance().search(searchDialog.getSearchText(),
                                                             searchDialog.getAggregation(),
                                                             searchDialog.getTerms(),
                                                             progressObject);

                        // Select all matching elements
                        DataManager.getInstance().deselectAllElements();                                
                        for (int i = 0; i < matchElements.length; i++)
                        {
                            matchElements[i].setSelected(true);
                        }

                        // Update all views (ie status bars, data groups etc)
                        DataManager.getInstance().validateViews();
                        statusMessage("Search finished, " + matchElements.length + " elements matched.");
                    }
                }; // end searchThread;
                
                searchThread.start();
                showProgressDialog(progressObject, "Searching");
            }
        }
    };
    
    Action exportSelectionToMVDaction = new AbstractAction("Export selection to MVD") {
        public void actionPerformed(ActionEvent ae)
        {
            chooseTargetAndExportSelectionToMVD();
        }
    };
    
    private void chooseTargetAndExportSelectionToMVD() 
    {
        if (DataManager.getInstance().getSelectedItemCount() <= 0)
        {
            JOptionPane.showMessageDialog(ApplicationFrame.this,
                                    "No examinations selected!",
                                    "Can not export",
                                    JOptionPane.INFORMATION_MESSAGE);
            return;
        } 
        else
        {
            ExaminationDataElement[] selectedElements = DataManager.getInstance().getSelectedElements();
            chooseTargetAndExportMVD(selectedElements);
        }                        
    }    
    
    /** 
     * Show a file selector, and if the user clicks OK, export the MVD
     */
    private void chooseTargetAndExportMVD(ExaminationDataElement[] exportElements)
    {   
        new ExportDialog(this, fileChooser, exportElements).show();
    }        
    
    /* Preferences part - shamelessly stolen from MedRecords */
    
    public void showPreferences() { showPreferences(0); }
    
    public void showPreferences(int tab)
    {                
        if (settingsDialog == null)
        {
            settingsDialog = new SettingsDialog(this);
        }

        // Put it in a ok/cancel dialog and show it
        settingsDialog.show(tab);        
    }

    public static Component getDialogParentComponent()
    {
            return ApplicationFrame.getInstance();
    }
}
