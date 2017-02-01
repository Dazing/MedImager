//
//  TermEdit.java
//  TETest
//
//  Created by Olof Torgersson on Wed Nov 26 2003.
//  $Id: TermEdit.java,v 1.3 2008/11/13 20:38:19 oloft Exp $.
//

package medview.termedit;

import javax.swing.*;

import medview.datahandling.*;

import medview.termedit.model.*;
import medview.termedit.view.TermEditFrame;

public class TermEdit {

    public static void main(String[] args) {
        // common
        MedViewDataHandler mVDH = MedViewDataHandler.instance();
        Preferences prefs = Preferences.instance();

        // look and feel
        try {
            try {
                String userLAFClass = prefs.getLookAndFeel();

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

        // Set data location - Fake looks for location set by FormEditor!
        String termDefLoc = prefs.getTermDefinitionLocation();
        String termValsLoc = prefs.getTermValueLocation();

        mVDH.setTermDefinitionLocation(termDefLoc);
        mVDH.setTermValueLocation(termValsLoc);

        // System.out.println("mVDH.getTermDefinitionLocation(): " + mVDH.getTermDefinitionLocation());

        TermEditFrame frame = new TermEditFrame(new Document());

        frame.setIconImage(mVDH.getImage(MedViewMediaConstants.FRAME_IMAGE_ICON));

        frame.show();

    }

}
