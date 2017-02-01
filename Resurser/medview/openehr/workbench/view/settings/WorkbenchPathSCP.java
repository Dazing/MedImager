//
//  WorkbenchPathSCP.java
//  WorkBench-GUI
//
//  Created by Olof Torgersson on 2008-11-25.
//
//  $Id: WorkbenchPathSCP.java,v 1.3 2008/12/28 11:08:57 oloft Exp $
//

package medview.openehr.workbench.view.settings;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import medview.common.actions.*;
import medview.common.components.*;
import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.openehr.workbench.model.*;
import medview.openehr.workbench.view.*;

import misc.domain.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

public class WorkbenchPathSCP extends SettingsContentPanel  implements
	MedViewLanguageConstants, GUIConstants
	{
		
		public String getTabLS()
		{
			return TAB_OEHR_REPOSITORY_LS_PROPERTY;
		}
		
		public String getTabDescLS()
		{
			return TAB_OEHR_REPOSITORY_DESCRIPTION_LS_PROPERTY;
		}
		
		protected void initSubMembers()
		{
			// mediator = (WorkbenchFrame) subConstructorData[0];
		}
		
	    protected void settingsShown()
		{
			String text;
			
			String propText;
			
			ignoreEvents = true;
			
			// archetypes
			
			text = archetypesPathTextField.getText(); 
			
			propText = prefs.getArchetypesLocation();
			
			if (!text.equals(propText)) { archetypesPathTextField.setText(propText); }
			
			// templates
			
			text = templatesPathTextField.getText();
			
			propText = prefs.getTemplatesLocation();
			
			if (!text.equals(propText)) { templatesPathTextField.setText(propText); }
			
			ignoreEvents = false;
		}
		
		protected void settingsHidden() {}
		
		protected void layoutPanel()
		{
			setLayout(new GridBagLayout());
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			int cCS = COMPONENT_COMPONENT_SPACING;
			
			int cGS = COMPONENT_GROUP_SPACING;
			
			Component moreStrut = Box.createHorizontalStrut(archetypesPathMoreButton.getPreferredSize().width);
			
			
			// ***********************************
			// ---------- Archetypes part --------
			// ***********************************
			
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.EAST;
			gbc.insets = new Insets(0,0,cCS,cCS);
			add(archetypesPathLabel, gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.weightx = 1;
			gbc.weighty = 0;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.insets = new Insets(0,0,cCS,cCS);
			add(archetypesPathTextField, gbc);
			
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.insets = new Insets(0,0,cCS,0);
			add(archetypesPathMoreButton, gbc);
			
			
			// ***********************************
			// ---------- Templates part ---------
			// ***********************************
			
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.EAST;
			gbc.insets = new Insets(0,0,cCS,cCS);
			add(templatesPathLabel, gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 1;
			gbc.weightx = 100;
			gbc.weighty = 0;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.insets = new Insets(0,0,cCS,cCS);
			add(templatesPathTextField, gbc);
			
			gbc.gridx = 2;
			gbc.gridy = 1;
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.insets = new Insets(0,0,cCS,0);
			add(templatesPathMoreButton, gbc);
			
			// ************************************
			// ---- Fill out with empty space ----
			// ************************************
			
			gbc.gridx = 0;
			gbc.gridy = 9;
			gbc.weightx = 0;
			gbc.weighty = 1;
			gbc.gridwidth = 3;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.insets = new Insets(0,0,0,0);
			add(Box.createGlue(), gbc);
		}
		
		protected void createComponents()
		{
			listener = new TextListener();
			
			int fW = GUIConstants.TEXTFIELD_WIDTH_VERY_LARGE;
			int fH = GUIConstants.TEXTFIELD_HEIGHT_NORMAL;
			
			Dimension fDim = new Dimension(fW, fH);
			
			/* Archetypes path */
			
			archetypesPathLabel = new MedViewLabel(LABEL_OEHR_ARCHETYPES_LOCATION_LS_PROPERTY);
			
			archetypesPathTextField = new JTextField();
			archetypesPathTextField.setPreferredSize(fDim);
			archetypesPathTextField.getDocument().addDocumentListener(listener);
			
			archetypesPathMoreButton = new MedViewMoreButton(new ArchetypesPathMoreAction());
			
			/* Templates path */
			
			templatesPathLabel = new MedViewLabel(LABEL_OEHR_TEMPLATES_LOCATION_LS_PROPERTY);
			
			templatesPathTextField = new JTextField();
			templatesPathTextField.setPreferredSize(fDim);
			templatesPathTextField.getDocument().addDocumentListener(listener);
			
			templatesPathMoreButton = new MedViewMoreButton(new TemplatesPathMoreAction());
			
		}
		
		public WorkbenchPathSCP(CommandQueue queue, Component parComp)
		{
			super(queue, new Object[0]);
			
			prefs = Preferences.instance();                
		}
		
		private Preferences prefs;
		
		private JLabel archetypesPathLabel;
		private JTextField archetypesPathTextField;
		private JButton archetypesPathMoreButton;
		
		private JLabel templatesPathLabel;
		private JTextField templatesPathTextField;
		private JButton templatesPathMoreButton;
		
		private WorkbenchFrame mediator;
		
		private TextListener listener;

		private boolean ignoreEvents;

		private class ArchetypesPathMoreAction extends MedViewAction
			{
				public void actionPerformed(ActionEvent e)
				{
					MedViewDialogs mVD = MedViewDialogs.instance();
					
					File dir = mVD.createAndShowChooseDirectoryDialog(null);
					
					if (dir != null)
					{
						archetypesPathTextField.setText(dir.getPath());
					}
				}
				
				public ArchetypesPathMoreAction()
				{
					super(ACTION_CHANGE_INPUT_IMAGE_LOCATION_LS_PROPERTY);
				}
			}
		
		private class TemplatesPathMoreAction extends MedViewAction
			{
				public void actionPerformed(ActionEvent e)
				{
					MedViewDialogs mVD = MedViewDialogs.instance();
					
					File dir = mVD.createAndShowChooseDirectoryDialog(null);
					
					if (dir != null)
					{
						templatesPathTextField.setText(dir.getPath());
					}
				}
				
				public TemplatesPathMoreAction()
				{
					super(ACTION_CHANGE_INPUT_IMAGE_LOCATION_LS_PROPERTY);
				}
			}
		
		private class TextListener implements DocumentListener {
			
			public void changedUpdate(DocumentEvent e) {
				// System.out.println("changedUpdate");
				
				//if (ignoreEvents) { return; } // koko
			}
			
			public void insertUpdate(DocumentEvent e) {
				if (ignoreEvents) { return; }
				
				//System.out.println("insertUpdate");
				
				checkInsertOrRemove(e);
			}
			
			public void removeUpdate(DocumentEvent e) {
				if (ignoreEvents) { return; }
				
				//System.out.println("removeUpdate");
				
				checkInsertOrRemove(e);
			}
			
			private void checkInsertOrRemove(DocumentEvent e) {
				String prop;
				String text;
				
				if (e.getDocument() == archetypesPathTextField.getDocument()) { // protocol path
					prop = Preferences.ArchetypesLocation;
					text = archetypesPathTextField.getText();
					commandQueue.addToQueue(new ChangeUserStringPropertyCommand(prop, text, Preferences.class));
				}
				else if (e.getDocument() == templatesPathTextField.getDocument()) { // template path
					prop = Preferences.TemplatesLocation;
					text = templatesPathTextField.getText();
					commandQueue.addToQueue(new ChangeUserStringPropertyCommand(prop, text, Preferences.class));
				}
				else {
					return;
				}
			}
			
		}
		
	}
