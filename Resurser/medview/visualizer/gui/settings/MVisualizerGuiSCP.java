/*
 * MVisualizerGuiSCP.java
 *
 * Created on den 20 juni 2005, 13:02
 *
 * $Id: MVisualizerGuiSCP.java,v 1.3 2005/06/30 10:56:16 erichson Exp $
 *
 * $Log: MVisualizerGuiSCP.java,v $
 * Revision 1.3  2005/06/30 10:56:16  erichson
 * update to restartNeeded
 *
 * Revision 1.2  2005/06/30 09:23:37  erichson
 * Added notification for when gui options change
 *
 * Revision 1.1  2005/06/30 09:12:05  erichson
 * First check-in.
 *
 */

package medview.visualizer.gui.settings;


import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import misc.gui.constants.GUIConstants;

import medview.datahandling.*;

import medview.visualizer.data.ApplicationManager;
import medview.visualizer.data.Settings;

public class MVisualizerGuiSCP extends AbstractVisualizerSCP 
{        
    
    private TextListener listener;

    private JLabel mdiLabel;
    private JCheckBox mdiCheckBox;
    
    private JLabel lafLabel;
    private LAFComboBox lafComboBox;           

    private boolean ignoreEvents;
    
    public MVisualizerGuiSCP()
    {
        super();               
    }
       
    public String getTabName()
    {
        return "GUI";
    }

    public String getTabDescription()
    {
        return "GUI and Look&Feel settings";
    }

    protected void initSubMembers()
    {
        ignoreEvents = false;
    }

    protected void settingsDialogShown()
    {        
        ignoreEvents = true;

        // use MDI? (Desktop-type)
        
        boolean useMDI = (settings.getWindowSystem() == settings.WINDOWSYSTEM_MDI);

        mdiCheckBox.setSelected(useMDI);
                
        // Look and feel

        String selectedLAF = lafComboBox.getSelectedLAFClassName();

        String settingsLAF = settings.getLookAndFeelClassName();
        
        if (!selectedLAF.equals(settingsLAF))
        { 
            lafComboBox.setSelectedLAFClassName(settingsLAF);            
        }

        ignoreEvents = false;
    }

    protected void settingsHidden() {}

    protected void layoutPanel()
    {                
        // MDI Checkbox
        
        gridBagAddTrio(mdiLabel,mdiCheckBox,moreStrut,0);
        
        // LAF Checkbox
                
        gridBagAddTrio(lafLabel,lafComboBox,moreStrut,2);
        
        // Finish up
                
        gridBagFinishingGlue(3);
        
    }


    protected void createComponents()
    {                

        mdiLabel = new JLabel("Window system:");
        mdiCheckBox = new JCheckBox("Desktop-based (MDI)");
        
        lafLabel = new JLabel("Look and feel:");
        lafComboBox = new LAFComboBox();
             
    }

    public void applySettings()
    {
        boolean restartNeeded = false;
        
        // MDI checkbox
        
        int newWindowSystem;
        if (mdiCheckBox.isSelected())
            newWindowSystem = Settings.WINDOWSYSTEM_MDI;
        else
            newWindowSystem = Settings.WINDOWSYSTEM_FREE;
        
        if (newWindowSystem != settings.getWindowSystem())
        {        
            settings.setWindowSystem(newWindowSystem);
            // ApplicationManager.getInstance().setWindowSystem(newWindowSystem);
            restartNeeded = true;
        }
        
        // LAF
        
        String newLAF = lafComboBox.getSelectedLAFClassName();
        if (! (newLAF.equals(settings.getLookAndFeelClassName())))
        {
            settings.setLookAndFeelClassName(newLAF);
            restartNeeded = true;
        }
                        
        if (restartNeeded)
        {
            JOptionPane.showMessageDialog(this, "You must restart mVisualizer for the GUI options to apply.", "GUI options changed", JOptionPane.INFORMATION_MESSAGE);
        }
        
    }

    private class LAFComboBox extends JComboBox
    {
        private UIManager.LookAndFeelInfo[] LAFinfos;
        private String[] LAFnames; // Look and feel names
        
        public LAFComboBox()
        {                
            super();            
            
            // Get chosen look and feel
        
            String currentLAFclass = settings.getLookAndFeelClassName();
            int currentLAFIndex = 0;
            
            // Get all look and feels
            LAFinfos = UIManager.getInstalledLookAndFeels();                        

            LAFnames = new String[LAFinfos.length];
            for (int i = 0; i < LAFinfos.length; i++)
            {
                LAFnames[i] = LAFinfos[i].getName();
                if (LAFinfos[i].getClassName().equals(currentLAFclass))
                {
                    currentLAFIndex = i;
                }
            }
            
            // Add LAFs to the combobox
            setModel(new DefaultComboBoxModel(LAFnames));
            setSelectedIndex(currentLAFIndex);                                                
        }
        
        public String getSelectedLAFClassName()
        {
            int i = getSelectedIndex();
            return LAFinfos[i].getClassName();
        }
    
        public void setSelectedLAFClassName(String lafClassName)
        {
            // Search for this lafclass
            for (int i = 0; i < LAFinfos.length; i++)
            {                
                if (LAFinfos[i].getClassName().equals(lafClassName))
                {
                    setSelectedIndex(i);
                    return;
                }
            }                                    
        }                
    }
}
