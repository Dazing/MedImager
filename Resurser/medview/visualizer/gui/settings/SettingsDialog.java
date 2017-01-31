/*
 * SettingsDialog.java
 *
 * Created on den 30 juni 2005, 10:35
 *
 * $Id: SettingsDialog.java,v 1.3 2005/06/30 10:39:34 erichson Exp $
 *
 * $Log: SettingsDialog.java,v $
 * Revision 1.3  2005/06/30 10:39:34  erichson
 * Added: Can select initial tab with show() call
 *
 * Revision 1.2  2005/06/30 09:23:20  erichson
 * Changed default size
 *
 * Revision 1.1  2005/06/30 09:12:06  erichson
 * First check-in.
 *
 */

package medview.visualizer.gui.settings;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;


/**
 *
 * @author Nils Erichson
 */
public class SettingsDialog 
{   
    
    public static final int GUI_TAB = 0;
    public static final int DATA_TAB = 1;
    
    private final JDialog theDialog;    
    
    private final JTabbedPane centerPane;
    
    private static AbstractVisualizerSCP[] settingsPanelArray;
    
    private final Frame owner;
    
    /** Creates a new instance of SettingsDialog */
    public SettingsDialog(Frame owner) 
    {                
        this.owner = owner;
        
        /* Center: The settings tabbedPane */
        settingsPanelArray = new AbstractVisualizerSCP[2];
        settingsPanelArray[GUI_TAB] = new MVisualizerGuiSCP();
        settingsPanelArray[DATA_TAB] = new MVisualizerDataHandlingSCP();
        
        centerPane = new JTabbedPane();
        centerPane.addTab(settingsPanelArray[0].getTabName(), settingsPanelArray[0]);
        centerPane.addTab(settingsPanelArray[1].getTabName(), settingsPanelArray[1]);                
        
        /* South: Buttons panel */
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                for(int i = 0; i < settingsPanelArray.length; i++)
                {
                    settingsPanelArray[i].applySettings();
                }
                
                theDialog.setVisible(false);
            }            
        });
                        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                theDialog.setVisible(false);
            }
            
        });
        
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
        
        /* Put it all together */
        
        
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BorderLayout());        
        
        totalPanel.add(centerPane, BorderLayout.CENTER);
        totalPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        
        theDialog = new JDialog(owner, 
                                "Preferences", 
                                true); // modal
        
        Container contentPane = theDialog.getContentPane();
        contentPane.add(totalPanel);
        
        // theDialog.pack();
        theDialog.setSize(600, 400);
        
    }

    public void show(int initialTab)
    {
        for(int i = 0; i < settingsPanelArray.length; i++)
        {
            settingsPanelArray[i].settingsDialogShown();
        }
        
        if ((initialTab >= 0) && (initialTab < settingsPanelArray.length))
        {
            centerPane.setSelectedIndex(initialTab);
        }
        
        theDialog.setLocationRelativeTo(owner);
        theDialog.show();        
    }    
}
