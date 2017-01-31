//
//  AppController.java
//  MedView
//
//  Created by Olof Torgersson on Thu Nov 13 2003.
//  Copyright (c) 2003 __MyCompanyName__. All rights reserved.
//

package medview.formeditor;

import java.util.*;
import java.awt.Component;
import javax.swing.*;

import medview.common.dialogs.*;

import medview.formeditor.data.*;
import medview.formeditor.tools.*;
import medview.formeditor.components.FormEditor;
import medview.formeditor.components.ConfigHandler;

public class AppController {

    private Vector documents;

    private DatahandlingHandler mDH;

    private static String version = "1.0 Beta 6";

    private static AppController instance;        

    private AppController() {
        initApplication();
    }
    
    public static AppController instance() {
        if (instance == null) {
            instance = new AppController();
        }

        return instance;
    }

    // Managing documents
    public void newDocument(Object sender) {
        // Dummy implementation for test purposes
        new FormEditor().show();
    }

    public void openDocument(Object sender) {
    }

    public void closeDocument(Object document) {
    }

    public void saveAllDocuments(Object sender) {
    }

    public List documents() {return null;}

    public boolean closeAllDocuments() {return false;}
    
    // Misc services
    public void showAboutPanel(Object sender) {
        MedViewDialogs mVD = MedViewDialogs.instance();

        String titLS = mDH.TITLE_ABOUT_FORMEDITOR_LS_PROPERTY;
        String txtLS = mDH.OTHER_ABOUT_FORMEDITOR_TEXT_LS_PROPERTY;

        mVD.createAndShowAboutDialog((Component)sender, titLS, "FormEditor", version, txtLS);        
    }

    public void showPreferences(Object sender) {
        //System.out.println("AppController showPreferences()");
        String[] tmpArray = Config.getValues();
        new ConfigHandler(tmpArray,true).setVisible(true);
        Ut.message(mDH.getLanguageString(mDH.LABEL_CHANGES_TAKE_EFFECT_LS_PROPERTY));
    }

    private void applicationDidFinishLaunching() {
    }
    
    // Managing shut down
    private boolean applicationShouldTerminate(Object sender) {return false;}

    // Should return false on mac
    private boolean applicationShouldTerminateAfterLastWindowClosed() {return true;}

    private void applicationWillTerminate() {}

    public void exitApplication(Object sender) {
        if (applicationShouldTerminate(sender)) {
            applicationWillTerminate();
            System.exit(0);
        }
    }

    private void initApplication() {
        System.out.println("AppController initApplication()");
        mDH = DatahandlingHandler.getInstance();
        documents = new Vector();
        Config.readConfigInfo();
        setLookAndFeel();
        setLanguage();
        applicationDidFinishLaunching();
    }

    private  void setLookAndFeel() {
        try {
            //  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            String  jVrn = System.getProperty("os.name");
            // Ut.prt( " name = " + jVrn);

            //system.property.(com.apple.mjr.verson) för apple
            if (false /*jVrn.startsWith("Windows")*/){
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            else{
                UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void setLanguage() {
        String setLang = Config.getLanguage();
        if (setLang != null) {
            DatahandlingHandler.getInstance().changeLanguage(setLang);
        }
    }    
}
