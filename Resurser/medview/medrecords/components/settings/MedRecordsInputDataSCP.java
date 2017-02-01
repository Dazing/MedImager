//
//  MedRecordsInputDataSCP.java
//  MedViewX
//
//  Created by Olof Torgersson on Wed Dec 17 2003.
//  $Id: MedRecordsInputDataSCP.java,v 1.9 2005/06/14 15:13:17 erichson Exp $.
//

package medview.medrecords.components.settings;

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

import medview.medrecords.*;
import medview.medrecords.components.NumberField;

import medview.medrecords.data.*;

import misc.domain.*;

import misc.gui.constants.*;

public class MedRecordsInputDataSCP extends SettingsContentPanel implements
	MedViewLanguageConstants, GUIConstants
{

    public String getTabLS()
    {
        return TAB_MEDRECORDS_INPUT_LS_PROPERTY;
    }

    public String getTabDescLS()
    {
        return TAB_MEDRECORDS_INPUT_DESCRIPTION_LS_PROPERTY;
    }

    protected void initSubMembers()
    {
        ignoreEvents = false;
    }

    protected void settingsShown()
    {
        String text;

        String propText;

        ignoreEvents = true;

        // database

        text = databasePathTextField.getText(); // should be disabled when remote is on

        propText = prefs.getLocalDatabaseLocation();

        if (!text.equals(propText)) { databasePathTextField.setText(propText); }

        // images

        text = imagePathTextField.getText();

        propText = prefs.getImageInputLocation();

        if (!text.equals(propText)) { imagePathTextField.setText(propText); }

        text = imageNumTextField.getText();

        propText = ""+prefs.getImageSelectorCount();

        if (!text.equals(propText)) { imageNumTextField.setText(propText); }

        // graph template location
        
        text = graphTemplatePathTextField.getText();
        
        propText = prefs.getGraphTemplateLocation();
        
        if (!text.equals(propText)) { graphTemplatePathTextField.setText(propText); }        

        // use medform?
                
        useMedFormTouchscreenDataCheckBox.setSelected(prefs.getTouchScreenDataFromMedForm());
        setMedFormSettingsEnabled(prefs.getTouchScreenDataFromMedForm());
        
        // medform settings
        
        text = medFormMVDTextField.getText();       
        propText = prefs.getMedFormMVD();       
        if (!text.equals(propText)) { medFormMVDTextField.setText(propText); }        
        
        text = medFormUserTextField.getText();        
        propText = prefs.getMedFormUser();        
        if (!text.equals(propText)) { medFormUserTextField.setText(propText); }        
        
        text = medFormURLTextField.getText();        
        propText = prefs.getMedFormURL();        
        if (!text.equals(propText)) { medFormURLTextField.setText(propText); }                        
        
        // ..
        
        ignoreEvents = false;
    }

    protected void settingsHidden() {}

    protected void layoutPanel()
    {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        int cCS = COMPONENT_COMPONENT_SPACING;

        int cGS = COMPONENT_GROUP_SPACING;

        Component moreStrut = Box.createHorizontalStrut(databasePathMoreButton.getPreferredSize().width);


        // ***********************************
        // ---------- Database part ----------
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
        add(databasePathLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,0,cCS,cCS);
        add(databasePathTextField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,0,cCS,0);
        add(databasePathMoreButton, gbc);


        // ***********************************
        // ---------- Image path part -------
        // ***********************************

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0,0,cCS,cCS);
        add(imagePathLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 100;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,0,cCS,cCS);
        add(imagePathTextField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,0,cCS,0);
        add(imagePathMoreButton, gbc);

        // ***********************************
        // ---------- Image num part -------
        // ***********************************

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0,0,cCS,cCS);
        add(imageNumLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,0,cCS,cCS);
        add(imageNumTextField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,0,cCS,0);
        add(moreStrut, gbc);

        // ***********************************
        // --- Graph template location part --
        // ***********************************

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0,0,cCS,cCS);
        add(graphTemplatePathLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 100;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,0,cCS,cCS);
        add(graphTemplatePathTextField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,0,cCS,0);
        add(graphTemplatePathMoreButton, gbc);
        
        // ************************************
        // - Use touchscreendata from MedForm -
        // ************************************
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0,0,cCS,cCS);
        add(useMedFormLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 100;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,0,cCS,cCS);        
        add(useMedFormTouchscreenDataCheckBox, gbc);

        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,0,cCS,0);
        add(moreStrut, gbc);
        
        // ************************************
        // - MedForm URL -
        // ************************************
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0,0,cCS,cCS);
        add(medFormURLLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 100;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,0,cCS,cCS);        
        add(medFormURLTextField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,0,cCS,0);
        add(moreStrut, gbc);
        
        // ************************************
        // - MedForm user -
        // ************************************
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0,0,cCS,cCS);
        add(medFormUserLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weightx = 100;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,0,cCS,cCS);        
        add(medFormUserTextField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 7;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,0,cCS,0);
        add(moreStrut, gbc);
        
        // ************************************
        // - MedForm MVD -
        // ************************************
        
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0,0,cCS,cCS);
        add(medFormMVDLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.weightx = 100;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,0,cCS,cCS);        
        add(medFormMVDTextField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0,0,cCS,0);
        add(moreStrut, gbc);
        
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

        checkListener = new CheckListener();
        
        int fW = GUIConstants.TEXTFIELD_WIDTH_VERY_LARGE;
        int fH = GUIConstants.TEXTFIELD_HEIGHT_NORMAL;

        Dimension fDim = new Dimension(fW, fH);

        /* Database path */
        
        databasePathLabel = new MedViewLabel(LABEL_DEFAULT_DATABASE_LS_PROPERTY);

        databasePathTextField = new JTextField();
        databasePathTextField.setPreferredSize(fDim);
        databasePathTextField.getDocument().addDocumentListener(listener);

        databasePathMoreButton = new MedViewMoreButton(new DatabasePathMoreAction());

        /* Image path */
        
        imagePathLabel = new MedViewLabel(LABEL_IMAGE_INPUT_DIR_LS_PROPERTY);

        imagePathTextField = new JTextField();
        imagePathTextField.setPreferredSize(fDim);
        imagePathTextField.getDocument().addDocumentListener(listener);

        imagePathMoreButton = new MedViewMoreButton(new ImagePathMoreAction());

        /* Image number */
        
        imageNumLabel = new MedViewLabel(LABEL_IMAGE_MAX_THUMBNAILS_LS_PROPERTY);

        imageNumTextField = new NumberField(4, 0);
        imageNumTextField.setPreferredSize(fDim);
        imageNumTextField.getDocument().addDocumentListener(listener);
        
        /* Graph template path */
        
        graphTemplatePathLabel = new MedViewLabel(LABEL_GRAPH_TEMPLATE_LOCATION_LS_PROPERTY);
        
        graphTemplatePathTextField = new JTextField();
        graphTemplatePathTextField.setPreferredSize(fDim);
        graphTemplatePathTextField.getDocument().addDocumentListener(listener);

        graphTemplatePathMoreButton = new MedViewMoreButton(new GraphTemplatePathMoreAction());
        
        /* MedForm-related */
        
        useMedFormTouchscreenDataCheckBox = new MedViewCheckBox(CHECKBOX_USE_MEDFORM_TOUCHSCREEN_DATA_LS_PROPERTY, null, false); 

        useMedFormTouchscreenDataCheckBox.addItemListener(checkListener);

        useMedFormLabel = new MedViewLabel(LABEL_USE_MEDFORM_TOUCHSCREEN_DATA_LS_PROPERTY);
        medFormURLLabel = new MedViewLabel(LABEL_MEDFORM_URL_LS_PROPERTY);
        medFormUserLabel = new MedViewLabel(LABEL_MEDFORM_USER_LS_PROPERTY);
        medFormMVDLabel = new MedViewLabel(LABEL_MEDFORM_MVD_LS_PROPERTY);
        
        medFormURLTextField = new JTextField();
        medFormURLTextField.setPreferredSize(fDim);
        medFormURLTextField.getDocument().addDocumentListener(listener);
        
        medFormUserTextField = new JTextField();
        medFormUserTextField.setPreferredSize(fDim);
        medFormUserTextField.getDocument().addDocumentListener(listener);
        
        medFormMVDTextField = new JTextField();
        medFormMVDTextField.setPreferredSize(fDim);
        medFormMVDTextField.getDocument().addDocumentListener(listener);                
    }

    /**
     * Enables/disables medForm-related settings
     */
    private void setMedFormSettingsEnabled(boolean state)
    {
        
        medFormURLLabel.setEnabled(state);
        medFormUserLabel.setEnabled(state);
        medFormMVDLabel.setEnabled(state);       
        
        medFormURLTextField.setEnabled(state);
        medFormUserTextField.setEnabled(state);
        medFormMVDTextField.setEnabled(state);                       
    }
    
    public MedRecordsInputDataSCP(CommandQueue queue, Component parComp)
    {
        super(queue, new Object[0]);

        prefs = PreferencesModel.instance();                
    }

    private PreferencesModel prefs;

    private MedRecords mediator;

    private TextListener listener;
    
    private CheckListener checkListener;

    private JLabel databasePathLabel;
    private JTextField databasePathTextField;
    private JButton databasePathMoreButton;

    private JLabel imagePathLabel;
    private JTextField imagePathTextField;
    private JButton imagePathMoreButton;

    private JLabel imageNumLabel;
    private NumberField imageNumTextField;

    private JLabel graphTemplatePathLabel;
    private JTextField graphTemplatePathTextField;
    private JButton graphTemplatePathMoreButton;

    /* MedForm settings */
    
    private JLabel medFormUserLabel, medFormMVDLabel, medFormURLLabel, useMedFormLabel;
    
    private JTextField medFormUserTextField, medFormMVDTextField, medFormURLTextField;
    
    private JCheckBox useMedFormTouchscreenDataCheckBox;
    
    /* ---------------- */
    
    private boolean ignoreEvents;

    
    private class CheckListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			if (!ignoreEvents)
			{
				boolean sel = e.getStateChange() == ItemEvent.SELECTED;

				if (e.getSource() == useMedFormTouchscreenDataCheckBox)
				{
					String prop = PreferencesModel.GetTouchScreenDataFromMedForm;

					commandQueue.addToQueue(new ChangeFlagCommand(prop, sel, PreferencesModel.class));
				}				
                                
                                /* Enable / disable medform-related fields */
                                
                                setMedFormSettingsEnabled(sel);
			}
		}
	}

    
    
    
    
    private class DatabasePathMoreAction extends MedViewAction
    {
        public void actionPerformed(ActionEvent e)
        {
			MedViewDialogs mVD = MedViewDialogs.instance();

            String filePath = mVD.createAndShowLoadMVDDialog(null);

            if (filePath != null)
            {
                databasePathTextField.setText(filePath);
            }
        }

        public DatabasePathMoreAction()
        {
            super(ACTION_CHANGE_INPUT_DATA_LOCATION_LS_PROPERTY);
        }
    }

    private class ImagePathMoreAction extends MedViewAction
    {
        public void actionPerformed(ActionEvent e)
        {
			MedViewDialogs mVD = MedViewDialogs.instance();

            File dir = mVD.createAndShowChooseDirectoryDialog(null);

            if (dir != null)
            {
                imagePathTextField.setText(dir.getPath());
            }
        }

        public ImagePathMoreAction()
        {
            super(ACTION_CHANGE_INPUT_IMAGE_LOCATION_LS_PROPERTY);
        }
    }
    
    private class GraphTemplatePathMoreAction extends MedViewAction
    {
        public void actionPerformed(ActionEvent e)
        {
			MedViewDialogs mVD = MedViewDialogs.instance();

            String filePath = mVD.createAndShowChangeGraphTemplateDialog(null);

            if (filePath != null)
            {
                graphTemplatePathTextField.setText(filePath);
            }
        }

        public GraphTemplatePathMoreAction()
        {
            super(ACTION_CHANGE_INPUT_GRAPH_TEMPLATE_LOCATION_LS_PROPERTY);
        }
    }
    

    private class TextListener implements DocumentListener {

        public void changedUpdate(DocumentEvent e) {
            System.out.println("changedUpdate");

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

            if (e.getDocument() == databasePathTextField.getDocument()) { // protocol path
                prop = PreferencesModel.LocalDatabaseLocation;
                text = databasePathTextField.getText();
                commandQueue.addToQueue(new ChangeUserStringPropertyCommand(prop, text, PreferencesModel.class));
            }
            else if (e.getDocument() == imagePathTextField.getDocument()) { // template path
                prop = PreferencesModel.ImageInputLocation;
                text = imagePathTextField.getText();
                commandQueue.addToQueue(new ChangeUserStringPropertyCommand(prop, text, PreferencesModel.class));
            }
            else if (e.getDocument() == imageNumTextField.getDocument()) { // template path
                prop = PreferencesModel.ImageSelectorCount;
                //System.out.println("changing image num " + imageNumTextField.getValue());
                commandQueue.addToQueue(new ChangeUserIntPreferenceCommand(prop, imageNumTextField.getValue(), PreferencesModel.class));
            }
            else if (e.getDocument() == graphTemplatePathTextField.getDocument()) { // graph template path
                prop = PreferencesModel.GraphTemplateLocation;
                text = graphTemplatePathTextField.getText();
                commandQueue.addToQueue(new ChangeUserStringPropertyCommand(prop, text, PreferencesModel.class));
            }
            else if (e.getDocument() == medFormURLTextField.getDocument()) { // Medform URL
                prop = PreferencesModel.MedFormURL;
                text = medFormURLTextField.getText();
                commandQueue.addToQueue(new ChangeUserStringPropertyCommand(prop, text, PreferencesModel.class));
            }
            else if (e.getDocument() == medFormUserTextField.getDocument()) { // Medform user
                prop = PreferencesModel.MedFormUser;
                text = medFormUserTextField.getText();
                commandQueue.addToQueue(new ChangeUserStringPropertyCommand(prop, text, PreferencesModel.class));
            }
            else if (e.getDocument() == medFormMVDTextField.getDocument()) { // Medform MVD
                prop = PreferencesModel.MedFormMVD;
                text = medFormMVDTextField.getText();
                commandQueue.addToQueue(new ChangeUserStringPropertyCommand(prop, text, PreferencesModel.class));
            }                                                   
            else {
                return;
            }
        }

    }
}
