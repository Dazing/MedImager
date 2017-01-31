/*
 * WorkBenchGUI.java
 *
 * Created 2008-11-17.
 *
 * $Id: WorkBenchGUI.java,v 1.8 2009/12/28 14:20:33 oloft Exp $
 *
 */

package medview.openehr.workbench;

import javax.swing.*;

import com.jgoodies.plaf.plastic.*;

import medview.datahandling.*;

import medview.openehr.workbench.model.*;
import medview.openehr.workbench.view.WorkbenchFrame;

public class WorkBenchGUI {
	
    public static void main(String[] args) {
        
		//Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
											   public void run() {
													startUp(); 
											   }
											   });
    }
    
	private static void startUp() {
		
		WorkbenchModel m = new WorkbenchModel();
        
		MedViewDataHandler mVDH = MedViewDataHandler.instance();
        Preferences prefs = Preferences.instance();
		
        // look and feel
		UIManager.installLookAndFeel("JGoodies Plastic 3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
		UIManager.installLookAndFeel("Quaqua", "ch.randelshofer.quaqua.QuaquaLookAndFeel");
		
        try {
            try {
                String userLAFClass = prefs.getLookAndFeel();
				
				System.out.println("LAF: " + userLAFClass);
				
                if (userLAFClass != null) { // the L&F has been set by the user
                    UIManager.setLookAndFeel(userLAFClass);
                }
                else {// no L&F set by the user, try to set system L&F
					
                    String sysLAFClass = UIManager.getSystemLookAndFeelClassName();
                    UIManager.setLookAndFeel(sysLAFClass);
                }
            }
            catch (Exception e) {
                System.out.println(
								   
                                   "Warning: could not set either the user set " +
                                   "look and feel or the look and feel for the " +
                                   "current system, will resort to using the " +
                                   "cross platform look and feel.");
				
                String crossLAFClass = UIManager.getCrossPlatformLookAndFeelClassName();
                UIManager.setLookAndFeel(crossLAFClass);
            }
        }
        catch (Exception e) {
            System.err.println(
							   
                               "FATAL ERROR: Could not set any of the following " +
                               "look and feels: user, system, or cross platform. " +
                               "Cannot run the program - exiting with error code 1.");
			
            System.exit(1);
        }
		
        // language
        String setLang = prefs.getSelectedLanguage();
		
        if (setLang != null) {
            try {
                mVDH.changeLanguage(setLang);
            }
            catch (LanguageException e) {
                System.err.print("Could not load specified language ");
				
                System.err.print("(" + setLang + ") - will use default ");
				
                System.err.print("language instead.");
            }
        }
        
        JFrame frame = new WorkbenchFrame(m);        
		
        frame.setVisible(true);
		
	}
	
    public static void shutDown() {
		// MedViewDataHandler.instance().shuttingDown();
        System.exit(0);
    }
    
}
