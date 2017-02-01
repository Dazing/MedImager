/*
 * ApplicationManager.java
 *
 * Created on July 1, 2002, 4:04 PM
 *
 * $Id: ApplicationManager.java,v 1.69 2008/09/01 13:18:48 it2aran Exp $
 *
 * $Log: ApplicationManager.java,v $
 * Revision 1.69  2008/09/01 13:18:48  it2aran
 * Visualizer: The desktop gets scrollbars if a window is outaide the main window to prevent windows from disappearing (594)
 * Changed version numbers
 *
 * Revision 1.68  2008/06/12 09:21:22  it2aran
 * Fixed bug:
 * -------------------------------
 * 413: Scrollar till felaktigt textfält om man sparar med felaktigt infyllt textfält.
 * 164: Tabbning mellan inputs scrollar hela formuläret så att den aktuella inputen alltid är synlig
 * Övrigt
 * ------
 * Parametrar -Xms128m -Xmx256m skickas till JVM för att tilldela mer minne så att större bilder kan hanteras
 * Mucositkomponenten helt omgjord. Utseendet passar bättre in samt att inga nollor sparas om inget är ifyllt.
 * Drag'n'drop för bilder fungerade ej och är borttaget tills vidare
 * Text i felmeddelandet vid inmatat värde utan att trycka på enter ändrat
 *
 * Revision 1.67  2008/04/24 10:07:22  it2aran
 * My CVS client is strange, so a lot of files are reported as changed in docgen, when they aren't
 *
 * Revision 1.66  2007/01/05 11:38:33  erichson
 * Beta 12
 *
 * Revision 1.65  2006/03/30 20:58:30  erichson
 * Updated version number.
 *
 * Revision 1.64  2005/10/21 09:51:52  erichson
 * Beta 10
 *
 * Revision 1.63  2005/06/30 10:57:51  erichson
 * Summary generation update
 *
 * Revision 1.62  2005/06/09 08:54:51  erichson
 * Updated to interface with the new se.chalmers.docgen package.
 *
 * Revision 1.61  2005/05/20 12:32:13  erichson
 * Beta 10
 *
 * Revision 1.60  2005/02/16 11:04:03  erichson
 * Beta 9
 *
 * Revision 1.59  2005/01/26 14:00:07  erichson
 * Beta 8
 *
 * Revision 1.58  2004/11/30 21:04:20  erichson
 * Beta 7
 *
 * Revision 1.57  2004/11/16 17:33:44  erichson
 * Upped version a bit for bugfix version
 *
 * Revision 1.56  2004/11/03 12:32:55  erichson
 * Beta 6 + some more comments
 *
 * Revision 1.55  2004/10/11 11:43:04  erichson
 * Separated out session handling to its own package
 *
 * Revision 1.54  2004/10/06 14:26:35  erichson
 * added infoMessage and errorDialog, updated version to beta 5
 *
 * Revision 1.53  2004/04/12 20:08:51  erichson
 * Moved chooseDataGroupThenStartLoading here
 *
 * Revision 1.52  2004/04/05 12:46:56  erichson
 * Added MVDH init, medview user id for visualizer
 *
 * Revision 1.51  2004/03/26 17:37:41  erichson
 * * URL --> URI
 * 
 *
 * Revision 1.50  2004/03/26 17:30:41  erichson
 * * New beta version
 * * Changed Datasource: URL --> URI
 *
 * Revision 1.49  2004/02/24 21:49:39  erichson
 * Improved translator/template error handling
 *
 * Revision 1.48  2004/02/23 14:00:42  erichson
 * Updating version to beta 3.
 *
 * Revision 1.47  2003/07/08 21:08:29  erichson
 * Updated version (now beta 2)
 *
 * Revision 1.46  2003/07/08 11:16:00  erichson
 * Bug-related changes (saveSession etc)
 *
 * Revision 1.45  2003/07/03 23:44:31  erichson
 * Changed println's to use the errorMessage() method
 *
 * Revision 1.44  2003/07/02 16:08:32  erichson
 * added statusMessage method.
 *
 * Revision 1.43  2003/07/02 00:34:52  erichson
 * New version, added errorMessage() method
 *
 * Revision 1.42  2002/12/20 13:49:07  erichson
 * Updated version
 *
 * Revision 1.41  2002/12/11 15:29:49  zachrisg
 * Fixed exception at session loading when the database is moved.
 *
 * Revision 1.40  2002/12/06 15:16:24  erichson
 * Updated prototype field to prototype 5
 *
 * Revision 1.39  2002/12/06 14:47:26  erichson
 * Fixed error in Session loading aggregation: output ((i +1)) instead of i+1)
 *
 * Revision 1.38  2002/12/05 11:55:52  erichson
 * Spelling error in loading views fixed
 *
 * Revision 1.37  2002/12/04 14:20:37  zachrisg
 * Removed typo.
 *
 * Revision 1.36  2002/12/03 15:26:25  erichson
 * Image loading was broken when using a JAR file since an absolute file system path was used instead of an URL. Also added some statusbar stuff.
 *
 * Revision 1.35  2002/11/29 15:38:27  zachrisg
 * Moved the modality handling of the ProgressMonitor to ApplicationFrame.
 *
 * Revision 1.34  2002/11/28 14:24:53  zachrisg
 * Removed bug that caused GUI to remain locked if now view was loaded in session.
 * Added workaround for bug that moves ApplicationFrame to the back after
 * session loading is finished.
 *
 * Revision 1.33  2002/11/28 13:56:40  zachrisg
 * Made the progressmonitor modal while loading the session by disabling
 * ApplicationFrame.
 *
 * Revision 1.32  2002/11/28 13:26:35  zachrisg
 * Added progress monitoring to session loading.
 *
 * Revision 1.31  2002/11/26 13:15:01  erichson
 * Updated icon loading, added method loadVisualizerIcon
 *
 * Revision 1.30  2002/11/25 15:25:08  zachrisg
 * Added better template and translator handling.
 *
 * Revision 1.29  2002/11/18 14:15:44  zachrisg
 * Now terms get loaded when the session is loaded.
 *
 * Revision 1.28  2002/11/14 16:01:48  zachrisg
 * Added session loading/saving of aggregations.
 *
 * Revision 1.27  2002/11/14 10:45:06  zachrisg
 * Added session loading/saving of viewframes' location and size.
 *
 * Revision 1.26  2002/11/13 14:56:35  zachrisg
 * Added statisticsview specific session saving/loading.
 *
 * Revision 1.25  2002/11/13 14:33:25  zachrisg
 * Added scatterplot specific session loading/saving stuff.
 *
 * Revision 1.24  2002/11/13 14:09:20  zachrisg
 * Added support in session loading/saving for barchart specific data.
 *
 * Revision 1.23  2002/11/12 16:02:07  zachrisg
 * Added basic session loading of views.
 *
 * Revision 1.22  2002/11/12 15:12:20  zachrisg
 * Added session loading of datasources and elements.
 *
 * Revision 1.21  2002/11/11 12:37:53  zachrisg
 * A start of the session saving.
 *
 * Revision 1.20  2002/11/07 15:25:28  erichson
 * added loadCommonIcon to load icons from the common/resources/icons repository, and added some javadoc
 *
 * Revision 1.19  2002/10/22 16:41:50  erichson
 * Added variable for whether the toolbox should be available or not
 *
 * Revision 1.18  2002/10/14 15:20:14  erichson
 * Fixed removeViewFrame (it didn't fire a window list event)
 * added disposeAllViewFrames
 * Added application name and version as constants
 *
 * Revision 1.17  2002/10/04 13:08:09  erichson
 * added createIconToggleButton and createIconRadioButton
 *
 * Revision 1.16  2002/09/27 15:41:03  erichson
 * Added cvs log and id fields
 *
 */

package medview.visualizer.data;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.swing.*; // UIManager
import javax.imageio.*;

import misc.foundation.*;
import misc.foundation.io.*;

import medview.datahandling.*;
import medview.datahandling.aggregation.*;
import medview.datahandling.examination.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.template.TemplateModel;
import se.chalmers.cs.medview.docgen.translator.TranslatorModel;
import se.chalmers.cs.medview.docgen.translator.TranslationModelFactoryCreator;

import medview.visualizer.data.session.*;
import medview.visualizer.event.*;
import medview.visualizer.gui.*;



/**
 * The class controling the current window system and tool. Uses the Singleton-pattern.
 *
 * @author  Nils Erichson <erichson@mdstud.chalmers.se>
 * @version 1.0
 */
public class ApplicationManager implements ToolBox
{
    
    /** If this constant is true then debug outputs are generated. */
    //private static final boolean debug = true;
    private static final boolean debug = false;                
    
    /* Constants */
    public static final String APPLICATION_NAME = "mVisualizer";    
    public static final String VERSION_STRING = "Version 1.1";

    private JDesktopPane desktop = null;

    public static final int NUMBER_OF_TOOLS = 4;
    
    /* MedView User ID, used when generating p-codes. */
    public static final String DEFAULT_MEDVIEW_USER_ID = "VIS"; 
    
    /** The WindowSystemChangeListeners. */
    protected Vector windowSystemChangeListeners;        
    
    /** The WindowListChangeListeners. */
    private Vector windowListChangeListeners;
    
    /** The ToolChangeListeners. */
    protected Vector toolChangeListeners;
        
    /** The TemplateChangeListeners. */
    protected Vector templateChangeListeners;
    
    /** The TranslatorChangeListeners. */
    protected Vector translatorChangeListeners;        
        
    /** The ProgressListeners for listening to the session loading. */
    protected Vector progressListeners;
    
    /** The ViewFrames. */
    protected Vector viewFrames;
        
    /** The toolbox floater. */
    protected Floater toolBoxFloater = null;

    /** The data group floater. */
    protected Floater dataGroupFloater = null;

     protected Floater messageFloater = null;
    
    /** The query floater. */
    protected Floater queryFloater = null;

    /** The current translator. */
    protected TranslatorModel translatorModel = null;
    
    /** The current template. */
    protected TemplateModel templateModel = null;
    
    private static ApplicationManager theInstance;        
    
    private int activeTool;    
    
    private static Settings settings;
        
    private static Vector startDates;        // For timer use
    
    /** If this flag is set to false while loading a session, then the operation i cancelled. */
    // private boolean keepLoading = false;
    
    /** Creates new ApplicationManager */
    private ApplicationManager()
    {
        windowSystemChangeListeners = new Vector();
        windowListChangeListeners = new Vector();
        toolChangeListeners = new Vector();
        templateChangeListeners = new Vector();
        translatorChangeListeners = new Vector();        
        progressListeners = new Vector();
        
        viewFrames = new Vector();
        startDates = new Vector();           // Vector storing times, for timer use
        settings = Settings.getInstance();
        activeTool = ToolBox.TOOL_POINTER;                
        
        // Init the MVDH
        MedViewDataHandler MVDH = MedViewDataHandler.instance();
        
        MVDH.setPCodeNRGeneratorLocation(MVDH.getMedViewTempDirectory() + "/" + "NRLockfile.lock");
                
        // Setup text generation
        
        System.setProperty(TranslationModelFactoryCreator.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewTranslationModelFactory");
        System.setProperty(GeneratorEngineBuilder.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewGeneratorEngine");
        System.setProperty(TermHandlerFactory.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewTermHandler");
        System.setProperty(DerivedTermHandlerFactory.INSTANCE_PROPERTY, "medview.common.generator.DefaultMedViewDerivedTermHandler");
	
    }

    public static ApplicationManager getInstance() {
        if (theInstance == null)
            theInstance = new ApplicationManager();

        return theInstance;            
    }    
    
    
    public void setTool(int tool) {
        activeTool = tool;
        fireToolChanged();
    }
    
    public int getTool() {
        return activeTool;
    }
    
     
    public void addToolChangeListener(ToolChangeListener tcl) {
        toolChangeListeners.add(tcl);
    }
    
    public void removeToolChangeListener(ToolChangeListener tcl) {
        toolChangeListeners.remove(tcl);
    }
    
    
    /**
     * Sets whether to use MDI or free windows.
     * @param newWindowSystem Either WINDOWSYSTEM_MDI or WINDOWSYSTEM_FREE.
     */
    public void setWindowSystem(int newWindowSystem)
    {        
        int oldWindowSystem = settings.getWindowSystem();
        settings.setWindowSystem(newWindowSystem);
        
        if (oldWindowSystem != settings.getWindowSystem())
        {        
                fireWindowSystemChanged();
        }        
    }
    
    /**
     * Returns the current window system. Can be either WINDOWSYSTEM_MDI or WINDOWSYSTEM_FREE.
     * @return The current window system.
     */
    /* moved to settings
    public int getWindowSystem() {
        return windowSystem;
    }
    */
    /**
     * Adds a listener that will receive notifications when the window system has changed.
     * @param wscl The listener to add.
     */
    public void addWindowSystemChangeListener(WindowSystemChangeListener wscl) {
        windowSystemChangeListeners.add(wscl);
    }
    
    /**
     * Removes a WindowSystemChangeListener.
     * @param wscl The listener to remove.
     */
    public void removeWindowSystemChangeListener(WindowSystemChangeListener wscl) {
        windowSystemChangeListeners.remove(wscl);
    }
    
    /**
     * Notifies the ToolChangeListeners that the active tool has changed.
     */
    protected void fireToolChanged() {
        for(Iterator i = toolChangeListeners.iterator(); i.hasNext(); ) {
            ((ToolChangeListener)i.next()).toolChanged(new ToolChangeEvent(this));
        }
    }
    
    /**
     * Notifies the WindowSystemListeners that the window system has changed.
     */
    protected void fireWindowSystemChanged() {
        for(Iterator i = windowSystemChangeListeners.iterator(); i.hasNext(); ) {
            ((WindowSystemChangeListener)i.next()).windowSystemChanged(new WindowSystemChangeEvent(this,settings.getWindowSystem()));
        }
    }

    /**
     * Adds a ViewFrame to the list of ViewFrames.
     * @param viewFrame The ViewFrame to add to the list.
     */
    public void addViewFrame(ViewFrame viewFrame) {
        viewFrames.add(viewFrame);
        //System.out.println("Firing window list changed");
        fireWindowListChanged();
        
    }
    
    
    /**
     * Removes a ViewFrame from the list of ViewFrames.
     * @param viewFrame The ViewFrame to remove from the list.
     */
    public void removeViewFrame(ViewFrame viewFrame) {
        viewFrames.remove(viewFrame);
        fireWindowListChanged();
    }
    
    public void fireWindowListChanged() {
        for (Iterator it = windowListChangeListeners.iterator(); it.hasNext();) {
            Object o = it.next();
            if (o instanceof WindowListChangeListener) {
                ((WindowListChangeListener) o).windowListChanged(new WindowListChangeEvent(this));
            } else {
                errorMessage("ApplicationManager.fireWindowListChanged error: Contained non-windowlistchangelistener");
            }            
        }
    }
    
    
    
    /**
     * Returns an array with the ViewFrames of the application.
     * @return an array of ViewFrames.
     */
    public ViewFrame[] getViewFrames() {
        
        // Convert viewFrames vector to array and return it 
        ViewFrame[] frames = new ViewFrame[viewFrames.size()];        
        frames = (ViewFrame[]) viewFrames.toArray(frames);        
        return frames;
    }
     /**
     * Returns an array with the ViewFrames of the application.
     * @return an array of ViewFrames.
     */
    public Floater[] getToolFloaters() {
        
        Floater[] floaters = new InternalFloater[NUMBER_OF_TOOLS];        
        floaters[0] = messageFloater;
        floaters[1] = queryFloater;
        floaters[2] = dataGroupFloater;
        floaters[3] = toolBoxFloater;
        return floaters;
    }
    
    /**
     * Clears the list of ViewFrames.
     */
    public void clearViewFrames() {
        viewFrames.clear();
    }
    
    // Hack method to dispose all viewframes... probably should not be in this class...
    public void disposeAllViewFrames() {
        ViewFrame[] frames = getViewFrames();
        for (int i = 0; i < frames.length; i++) {
            ViewFrame vf = frames[i];
            vf.closeFrame();
            viewFrames.remove(vf); // If the vector does not contain the element, it is unchanged
        }
    }
    
    /**
     * Asks the Views in all the ViewFrames to check if the selection in them has changed, and if
     * so then regenerate the internal data structures and repaint the views.
     */
     public void validateViews() {
        for (Iterator i = viewFrames.iterator(); i.hasNext(); ) {
            ViewFrame viewFrame = (ViewFrame)i.next();
            View view = viewFrame.getView();
            view.validateView();
        }
        ApplicationFrame.getInstance().validateFrame(); // UGLY?
     }

     /**
      * Debug method that prints a debug message if debug == true.
      * @param message The message to print.
      */
     public static void debug(String message) {
         if (debug) {
            System.out.println(message);
        }
     }
     
     /**
      * Debug method that starts a timer.
      */
     public static void startTimer() {
        if (debug) {
            startDates.add(new Date());
        }
     }
     
     /**
      * Debug method that stops a timer and returns the number of milliseconds since it was started.
      * @return A string with the number of milliseconds since the timer was started.
      */
     public static String stopTimer() {
         if (debug) {
            Date stopDate = new Date();
            if (startDates.isEmpty()) {
                 return "Time: 0 (ms)";
            } else {
                Date startDate = (Date) startDates.remove(startDates.size() - 1);
                return "Time: " + (stopDate.getTime() - startDate.getTime()) + " (ms)";
             }
         } else {
             return "";
         }
     }          
     
     /**
      * Load an Icon from Visualizer's icon directory
      * @param name the name of the icon file
      * @return a new Icon
      * @throws IOException if the file corresponding to the icon could not be accessed
      */
     public javax.swing.ImageIcon loadVisualizerIcon(String name) throws java.io.IOException {
         return loadIcon(name, "/medview/visualizer/resources/icons/");
     }
     
     /**
      * Load an Icon from Medview's common icon resources directory
      * @param name the name of the icon file
      * @return a new Icon
      * @throws IOException if the file corresponding to the icon could not be accessed
      */
     public javax.swing.ImageIcon loadCommonIcon(String name) throws java.io.IOException {
         return loadIcon(name, "/medview/common/resources/icons/");
     }
     
     
     /**
      * Load an Icon from a file
      * @param name the file name (can also include directories, like openide/folder.png)
      * @param basePath the base path to the directory of icons
      * @return a new Icon
      * @throws IOException if the file corresponding to the icon could not be accessed
      */
     private javax.swing.ImageIcon loadIcon(String name, String basePath) throws java.io.IOException {
        
        // ImageReader pngReader = null;
         
        String resourceName = basePath + name;
        
        // System.out.println("Looking for resource " + resourceName);
         
        java.net.URL resourceURL = getClass().getResource(resourceName);
        // System.out.println("resource URL = " + resourceURL);
        if (resourceURL == null) {             
            // System.out.println("Resource not found!");              
             throw new java.io.IOException("Resource not found (" + resourceName + ")!");
        } else {
            // System.out.println("Resource found!");
            //return new javax.swing.ImageIcon(resource);                                 
            
            // debug routine to check for PNG format readers
            /* for (Iterator it = javax.imageio.ImageIO.getImageReadersByMIMEType("image/png"); it.hasNext(); ) {
                System.out.println("Got a PNG reader!");
                pngReader = (ImageReader) it.next();
                System.out.println(pngReader.getClass().getName());
            }
            */
            
            
            try {
                /*
                String path = resourceURL.getPath();                
                File imageFile = new File(path);
                java.awt.image.BufferedImage bimage = javax.imageio.ImageIO.read(imageFile);
                 */
                //System.err.println("Trying to load icon from " + resourceURL);
                java.awt.image.BufferedImage bimage = javax.imageio.ImageIO.read(resourceURL);
                if (bimage == null)
                    throw new IOException("No imageReader could read " + resourceURL);
                else
                    return new ImageIcon(bimage);
                
                /*
                    if (bimage == null) {                    
                        
                        // Try own handling with pngReader
                                                
                        
                            javax.imageio.stream.ImageInputStream imageInputStream = new javax.imageio.stream.FileImageInputStream(imageFile);
                            pngReader.setInput(imageInputStream);
                            bimage = pngReader.read(0);
                            
                            if (bimage == null) {
                                System.out.println("pngReader failed");
                                throw new IOException("pngReader could not read " + resourceURL);                                
                            } else {
                                System.out.println("pngReader success");
                                return new ImageIcon(bimage);
                            }
                        
                            
                    }
                    return new ImageIcon(bimage);
                } else {
                    throw new IOException("File " + path + " does not exist gaah");
                 */
                
                
                
            } catch (IOException ioe) {
                errorMessage("loadIcon: image reading failed: " + ioe.getMessage());                                        
                throw ioe;
            }
        }
     }     

     
     
     public void addWindowListChangeListener(WindowListChangeListener wlcl) {
        if ( ! windowListChangeListeners.contains(wlcl)) {
            windowListChangeListeners.add(wlcl);
        }
     }

    /**
     * Creates a JButton with an icon. If the icon does not exist then the fallback text is used.
     *
     * @param iconName The filename of the icon.
     * @param fallbackText The text to use if the icon isn't found.
     * @param size The preferred size if the icon is used.
     */
    public JButton createIconButton(String iconName, String fallbackText, Dimension size) {
        JButton button;
        try {
            ImageIcon icon = loadVisualizerIcon(iconName);
            button = new JButton(icon);                        
            button.setPreferredSize(size);
        } catch(IOException e) {
            button = new JButton(fallbackText);
        }
        return button;
    }


    
    public JToggleButton createIconToggleButton(String iconName, String fallbackText, Dimension size) {
        JToggleButton button;
        try {
            ImageIcon icon = loadVisualizerIcon(iconName);
            button = new JToggleButton(icon);                        
            button.setPreferredSize(size);
        } catch(IOException e) {
            button = new JToggleButton(fallbackText);
        }
        return button;
    }

    public JRadioButton createIconRadioButton(String iconName, String fallbackText, Dimension size) {
        JRadioButton button;
        try {
            ImageIcon icon = loadVisualizerIcon(iconName);
            button = new JRadioButton(icon);                        
            button.setPreferredSize(size);
        } catch(IOException e) {
            button = new JRadioButton(fallbackText);
        }
        return button;
    }       

    
    
    /**
     * Adds a TranslatorChangeListener.
     *
     * @param listener The listener to add.
     */
    public void addTranslatorChangeListener(TranslatorChangeListener listener) {
        translatorChangeListeners.add(listener);
    }
    
    /**
     * Removes a TranslatorChangeListener.
     *
     * @param listener The listener to remove.
     */
    public void removeTranslatorChangeListener(TranslatorChangeListener listener) {
        translatorChangeListeners.remove(listener);
    }
    
    /**
     * Notifies the TranslatorChangeListeners that the translator has been changed.
     */
    protected void fireTranslatorChanged() {
        for (Iterator i = translatorChangeListeners.iterator(); i.hasNext(); ) {
            ((TranslatorChangeListener) i.next()).translatorChanged(new TranslatorEvent(this));
        }
    }

    /**
     * Adds a TemplateChangeListener.
     *
     * @param listener The listener to add.
     */
    public void addTemplateChangeListener(TemplateChangeListener listener) {
        templateChangeListeners.add(listener);
    }
    
    /**
     * Removes a TemplateChangeListener.
     *
     * @param listener The listener to remove.
     */
    public void removeTemplateChangeListener(TemplateChangeListener listener) {
        templateChangeListeners.remove(listener);
    }
    
    /**
     * Notifies the TemplateChangeListeners that the template has been changed.
     */
    protected void fireTemplateChanged() {
        for (Iterator i = templateChangeListeners.iterator(); i.hasNext(); ) {
            ((TemplateChangeListener) i.next()).templateChanged(new TemplateEvent(this));
        }
    }
    
    /**
     * Sets the current template model.
     *
     * @param templateFilename The filename of the template model.
     */
    public void setTemplate(String templateFilename)
    {
        if (templateFilename != null)
        {
            try {
                templateModel = MedViewDataHandler.instance().loadTemplate(templateFilename);
                Settings.getInstance().setTemplateFilename(templateFilename);
                fireTemplateChanged();
            } catch(se.chalmers.cs.medview.docgen.misc.CouldNotLoadException exc)
            { 
                errorMessage("Could not set template - loadException: " + exc.getMessage());
            } /*catch(se.chalmers.cs.medview.docgen.misc.InvalidVersionException exc) { 
                ApplicationManager.errorMessage("Could not set template - invalidVersion: " + exc.getMessage());
            }*/
        }
    }
    
    /**
     * Sets the current translator model.
     *
     * @param translatorFilename The filename of the translator model.
     */
    public void setTranslator(String translatorFilename)
    {
        if (translatorFilename != null)
        {
            try 
            {
                translatorModel = MedViewDataHandler.instance().loadTranslator(translatorFilename);
                Settings.getInstance().setTranslatorFilename(translatorFilename);
                fireTranslatorChanged();
            } catch(se.chalmers.cs.medview.docgen.misc.CouldNotLoadException exc)
            { 
                ApplicationManager.errorMessage("Could not set/load translator: " + exc.getMessage());
            }
        }
    }
    
    /**
     * Returns the current translator model or null if none is selected.
     *
     * @return The current translator model or null if none is selected.
     */
    public TranslatorModel getTranslator()
    {
        if (translatorModel == null) {
            setTranslator(Settings.getInstance().getTranslatorFilename());
        }
        return translatorModel;
    }
    
    /**
     * Returns the current template model or null if none is selected.
     *
     * @return The current template model or null if none is selected.
     */
    public TemplateModel getTemplate()
    {
        if (templateModel == null) {
            setTemplate(Settings.getInstance().getTemplateFilename());
        }
        return templateModel;
    }
    
    /**
     * Adds a ProgressListener.
     *
     * @param listener The listener to add.
     */
    public void addProgressListener(ProgressListener listener)
    {
        progressListeners.add(listener);
    }
    
    /**
     * Removes a ProgressListener.
     *
     * @param listener The listener to remove.
     */
    public void removeProgressListener(ProgressListener listener)
    {
        progressListeners.remove(listener);
    }
    
    /**
     * Notifies progress listeners that progress in loading the data has been made.
     *
     * @param message The progress message.
     * @param note The progress note.
     * @param minimum The minimum of loaded patients.
     * @param now The current amount of loaded patients.
     * @param max The maximum number of patients to be loaded.
     */
    protected void fireProgressMade(String message, String note, int minimum, int now, int max)
    {
        for (Iterator it = progressListeners.iterator(); it.hasNext();) {
            ProgressListener nextListener = (ProgressListener) it.next();
            nextListener.progressMade(new ProgressEvent(message, note, this, minimum, now, max));
        }
    }
    
    /** 
     * Cancels the operation that generates ProgressEvents.
     */
    /*public void cancelProgressOperation() {
        keepLoading = false;
    }
    */
    public void setStatusBarText(String newText) {
        statusMessage(newText);
    }
    
    public void setStatusBarProgress(int min, int current, int max) {
        ApplicationFrame.getInstance().setStatusBarProgress(min,current,max);
    }
    
    public static void errorMessage(String message) {
        ApplicationFrame.getInstance().errorMessage(message);
    }
    
    public static void infoMessage(String message) {
        ApplicationFrame.getInstance().statusMessage(message);
    }
    
    public static void errorDialog(String message) {
        JOptionPane.showMessageDialog(ApplicationFrame.getInstance(),
                                      message,
                                      "Error",
                                      JOptionPane.ERROR_MESSAGE);            
    }
    
    
    public static void statusMessage(String message) {
        ApplicationFrame.getInstance().statusMessage(message);
    }                     
    
     /**
     * Loads the program session in a separate thread.
     *
     * @param sessionFile The file to load the session from.
     */
    public ProgressObject loadSession(File sessionFile) {
        ApplicationManager.debug("in loadSession method, creating thread");
        LoadSessionThread thread = new LoadSessionThread(sessionFile);
        ApplicationManager.debug("thread created, starting");
        thread.start();
        ApplicationManager.debug("thread started, returning now");
        return thread;
    }                        
    
    /**
     * Saves the program session. This operation is performed in a separate thread.
     * @return ProgressObject for the save session operation.
     * @param sessionFile The file to save the session to.
     */
    public ProgressObject saveSession(File sessionFile)
    {
        SaveSessionThread thread = new SaveSessionThread(sessionFile);
        thread.start();
        return thread;
    }                
                
    /**
     * Pops up a data group chooser. If OK, loading from the URI is started with a progress dialog.
     */
    public void chooseDataGroupThenStartLoading(java.net.URI uri)
    {
        ApplicationFrame appFrame = ApplicationFrame.getInstance();
        DataGroup dataGroup = appFrame.showDataGroupChooser();
        
        if (dataGroup != null) // Did not click cancel
        {  
            try
            {
                ApplicationManager.debug("Getting progress object");
                final ProgressObject progressObject = DataManager.getInstance().loadAllExaminations(uri, dataGroup);
                //System.out.println("Got progress object");
                appFrame.showProgressDialog(progressObject, "Loading examinations");                    
            } 
            catch (IOException e)
            {
                JOptionPane.showMessageDialog(appFrame,"Failed to load examination data because: IOEXCEPTION: " + e.getMessage(),"IOException",JOptionPane.ERROR_MESSAGE);            
            } 
            
            catch (InvalidDataLocationException idle)
            {
                JOptionPane.showMessageDialog(appFrame,"Failed to load examination data because: Invalid data location: " + idle.getMessage(),"InvalidDataLocationException",JOptionPane.ERROR_MESSAGE);
            }                
        }                     
    }   
    
    public JDesktopPane getDesktop()
    {
        return desktop;
    }

    public void setDesktop(JDesktopPane desktop)
    {
        this.desktop = desktop;
    }
    
   public Floater getDataGroupFloater()
   {
        return dataGroupFloater;
    }

    public void setDataGroupFloater(Floater dataGroupFloater)
    {
        this.dataGroupFloater = dataGroupFloater;
    }

    public Floater getMessageFloater()
    {
        return messageFloater;
    }

    public void setMessageFloater(Floater messageFloater)
    {
        this.messageFloater = messageFloater;
    }

    public Floater getQueryFloater()
    {
        return queryFloater;
    }

    public void setQueryFloater(Floater queryFloater)
    {
        this.queryFloater = queryFloater;
    }

    public Floater getToolBoxFloater()
    {
        return toolBoxFloater;
    }

    public void setToolBoxFloater(Floater toolBoxFloater)
    {
        this.toolBoxFloater = toolBoxFloater;
    }
    
}

