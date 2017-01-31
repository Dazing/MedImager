/*
 * 
 *
 * $Id: AbstractVisualizerSCP.java,v 1.1 2005/06/30 09:12:04 erichson Exp $
 *
 * $Log: AbstractVisualizerSCP.java,v $
 * Revision 1.1  2005/06/30 09:12:04  erichson
 * First check-in.
 *
 */

package medview.visualizer.gui.settings;

import java.awt.*;

import misc.gui.constants.GUIConstants;

import javax.swing.*;
import javax.swing.border.*;

import medview.visualizer.data.Settings;

/**
 * Base class for MVisualizer settings
 *
 * Adapted from SettingsContentPanel in the common framework
 *
 * @author Nils Erichson
 *
 */
public abstract class AbstractVisualizerSCP extends JPanel
{
	protected GridBagLayout gbl;
	protected GridBagConstraints gbc;
    
        protected Settings settings;                

        protected Component moreStrut = Box.createHorizontalStrut(24); // databasePathMoreButton.getPreferredSize().width);
        
        // Tab names and descriptions

	public abstract String getTabName();
	public abstract String getTabDescription();

	// METHODS CALLED WHEN SETTINGS DIALOG IS SHOWN / HIDDEN

	// protected void settingsDialogHidden()	{}

	protected abstract void settingsDialogShown();
       
        // Called when the 'apply' button is clicked
        protected abstract void applySettings();

        protected abstract void createComponents();
        protected abstract void layoutPanel();
        
	// CONSTRUCTOR(S)

        // Preset field size
        protected int fW = GUIConstants.TEXTFIELD_WIDTH_VERY_LARGE;
        protected int fH = GUIConstants.TEXTFIELD_HEIGHT_NORMAL;
        protected Dimension fDim = new Dimension(fW, fH);
        
        // For layouting
        protected int cGS = GUIConstants.COMPONENT_GROUP_SPACING;
        protected int cCS = GUIConstants.COMPONENT_COMPONENT_SPACING;
        
	public AbstractVisualizerSCP()
	{
                super(); // JPanel constructor
                
		settings = Settings.getInstance();
            
		// layout init

		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		setLayout(gbl);

		// border	
		setBorder(new EmptyBorder(cGS + cCS, cGS + cCS, cGS + cCS, cGS + cCS));

                // These methods are implemented in subclasses
                createComponents();
                layoutPanel();
                				
	}                
        
        protected void gridBagAddTrio(Component leftComponent, Component centerComponent, Component rightComponent, int y)
        {
            gbc.gridx = 0;
            gbc.gridy = y;
            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.EAST;
            gbc.insets = new Insets(0,0,cCS,cCS);
            add(leftComponent, gbc);

            gbc.gridx = 1;
            gbc.gridy = y;
            gbc.weightx = 1;
            gbc.weighty = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(0,0,cCS,cCS);
            add(centerComponent, gbc);

            gbc.gridx = 2;
            gbc.gridy = y;
            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.gridwidth = 1;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(0,0,cCS,0);
            add(rightComponent, gbc);
        }
        
        protected void gridBagFinishingGlue(int y)
        {
            gbc.gridx = 0;
            gbc.gridy = y;
            gbc.weightx = 0;
            gbc.weighty = 1;
            gbc.gridwidth = 3;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(0,0,0,0);
            add(Box.createGlue(), gbc);
        }
                
}
