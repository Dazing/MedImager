/**
 * $Id: MVisualizerDataHandlingSCP.java,v 1.3 2006/03/30 20:56:33 erichson Exp $
 * 
 * $Log: MVisualizerDataHandlingSCP.java,v $
 * Revision 1.3  2006/03/30 20:56:33  erichson
 * Added MedView User ID settings.
 *
 * Revision 1.2  2005/06/30 10:55:42  erichson
 * Fix to filechooser (directories) and template/translator locations (applicatonmanager called now instead of settings directly)
 *
 * Revision 1.1  2005/06/30 09:12:05  erichson
 * First check-in.
 *
 */

package medview.visualizer.gui.settings;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import medview.datahandling.*;
import medview.common.components.MedViewMoreButton;

import medview.visualizer.data.Settings;
import medview.visualizer.data.ApplicationManager;

/**
 * Data handling settings for MVisualizer. adapted from MedRecordsDataHandlingSCP.java
 *
 * @author Nils Erichson
 */

public class MVisualizerDataHandlingSCP extends AbstractVisualizerSCP // implements MedViewLanguageConstants, GUIConstants
{

        private JTextField medServerTextField;            
        private JTextField templateTextField;
        private JTextField translatorTextField;
        private JTextField termDefTextField;
        private JTextField termValueTextField;
        private JTextField userIDTextField;
        
        private JLabel medServerLabel;
        private JLabel templateLabel;
        private JLabel translatorLabel;
        private JLabel termDefLabel;
        private JLabel termValueLabel;
        private JLabel userIDLabel;
    
        private MedViewMoreButton templateMoreButton;
        private MedViewMoreButton translatorMoreButton;
        private MedViewMoreButton termDefMoreButton;
        private MedViewMoreButton termValueMoreButton;
        
	public MVisualizerDataHandlingSCP() // CommandQueue queue, Component parComp)
	{
                super();            
	}

	public String getTabName()
	{
		return "Data";
	}

	public String getTabDescription()
	{
		return "MedServer and Summary generation settings";
	}       
        
	protected void settingsDialogShown()
	{
                medServerTextField.setText(settings.getMedServerHost());
            
                templateTextField.setText(settings.getTemplateFilename());
                
                translatorTextField.setText(settings.getTranslatorFilename());
                
                termDefTextField.setText(settings.getTermDefinitionLocation());
                
                termValueTextField.setText(settings.getTermValueLocation());                		

                String userID = MedViewDataHandler.instance().getUserID();
                if ((userID == null) || (userID.equals("")))
                {
                    userID = ApplicationManager.DEFAULT_MEDVIEW_USER_ID;
                }
                
                userIDTextField.setText(userID);
		// dataPanel.setUsesRemote(settings.usesRemoteDataHandling());
                                
	}

	protected void createComponents()
	{

            medServerLabel = new JLabel("MedServer host:");
            
            medServerTextField = new JTextField();
            medServerTextField.setPreferredSize(fDim);

            /* --- */
            
            templateLabel = new JLabel("Summary template:");            
            templateTextField = new JTextField();
            templateTextField.setPreferredSize(fDim);                        
            templateMoreButton = new MedViewMoreButton(new FileChooserAction(templateTextField, this, "Choose summary template", "xml"));
            
            translatorLabel = new JLabel("Summary translator:");            
            translatorTextField = new JTextField();
            translatorTextField.setPreferredSize(fDim);                        
            translatorMoreButton = new MedViewMoreButton(new FileChooserAction(translatorTextField, this, "Choose summary translator", "xml"));
            
            termDefLabel = new JLabel("Summary term defs:");            
            termDefTextField = new JTextField();
            termDefTextField.setPreferredSize(fDim);                        
            termDefMoreButton = new MedViewMoreButton(new FileChooserAction(termDefTextField, this, "Choose term definitions", "txt"));
            
            termValueLabel = new JLabel("Summary term values:");            
            termValueTextField = new JTextField();
            termValueTextField.setPreferredSize(fDim);                        
            termValueMoreButton = new MedViewMoreButton(new FileChooserAction(termValueTextField, this, "Choose term values", "txt"));
            
            userIDLabel = new JLabel("MedView User ID:");
            userIDTextField = new JTextField();
            userIDTextField.setPreferredSize(fDim);
            
            // old visualizer methods...
            //String termDefinitionLocation = MedViewDialogs.instance().createAndShowChangeTermDefinitionDialog(this);
            //String termValueLocation = MedViewDialogs.instance().createAndShowChangeTermValueDialog(this);
	}

	protected void layoutPanel()
	{
		gridBagAddTrio(medServerLabel, medServerTextField, moreStrut, 0);
                
                gridBagAddTrio(templateLabel, templateTextField, templateMoreButton, 2);
                gridBagAddTrio(translatorLabel, translatorTextField, translatorMoreButton, 3);
                gridBagAddTrio(termDefLabel, termDefTextField, termDefMoreButton, 4);
                gridBagAddTrio(termValueLabel, termValueTextField, termValueMoreButton, 5);                                
                
                gridBagAddTrio(userIDLabel, userIDTextField, moreStrut, 6);
                
                gridBagFinishingGlue(7);
	}

        public void applySettings()
        {
                settings.setMedServerHost(medServerTextField.getText().trim());
                
                //settings.setUseRemoteDataHandling(...);
                
                ApplicationManager.getInstance().setTemplate(templateTextField.getText().trim()); // Updates settings if location OK
                ApplicationManager.getInstance().setTranslator(translatorTextField.getText().trim()); // Updates settings if location OK
                
                String termDefs = termDefTextField.getText().trim();
                String termValues = termValueTextField.getText().trim();
                
                settings.setTermDefinitionLocation(termDefs);
                settings.setTermValueLocation(termValues);                                                                
                
                MedViewDataHandler.instance().setTermDefinitionLocation(termDefs);
                MedViewDataHandler.instance().setTermValueLocation(termValues);
                
                try
                {
                    MedViewDataHandler.instance().setUserID(userIDTextField.getText().trim().toUpperCase());
                } 
                catch (InvalidUserIDException e)
                {
                    JOptionPane.showMessageDialog(this, "Could not set MedView User ID because it was invalid, please re-set.", "Invalid User ID", JOptionPane.ERROR_MESSAGE);
                }
        }
    
    private class FileChooserFileFilter extends javax.swing.filechooser.FileFilter
    {
        private final String extension;
        
        public FileChooserFileFilter(String extension)
        {
            super();
            this.extension = extension;
        }
        
        public String getDescription() { return ('.' + extension + " files"); }
                                        
        public boolean accept(java.io.File f) 
        {
            if (f.isDirectory())
                return true;
            else
                return (f.getName().toLowerCase().endsWith("." + extension.toLowerCase()));
        }                
        
    }
        
    private class FileChooserAction extends AbstractAction 
    {
        private final JTextField textField;
        private final Component parent;
        private final String extension;        
        private final String title;                
 
        /** Constructor */
        public FileChooserAction(JTextField textField, Component parent, String title, String extension)
        {
            this.textField = textField;
            this.parent = parent;            
            this.title = title;
            this.extension = extension;
        }                         
        
        public void actionPerformed(ActionEvent e)
        {
            JFileChooser fileChooser = new JFileChooser(textField.getText()); // Get current path from the associated textfield
            fileChooser.setDialogTitle(title);
            fileChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileChooserFileFilter(extension));        

            // Show it
            int result = fileChooser.showDialog(parent, "Choose");
            
            if (result == JFileChooser.APPROVE_OPTION)
            {
                java.io.File chosenFile = fileChooser.getSelectedFile();
                if (chosenFile != null)
                {
                    if (!chosenFile.exists())
                    {
                        JOptionPane.showMessageDialog(parent, "Chosen file does not exist", "File doesn't exist", JOptionPane.ERROR_MESSAGE);
                    }
                    else if (!chosenFile.isFile())
                    {
                        JOptionPane.showMessageDialog(parent, "That choice is not a file", "Not a file", JOptionPane.ERROR_MESSAGE);
                    }
                    else
                    {
                       textField.setText(chosenFile.getPath());
                    }
                }
            }
        }
        
    } // end FileChooserAction class                       
}
