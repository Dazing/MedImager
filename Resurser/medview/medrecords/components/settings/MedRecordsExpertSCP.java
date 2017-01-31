//
//  MedRecordsExpertSCP.java
//  MedViewX
//
//  Created by Olof Torgersson on Sun Dec 21 2003.
//  $Id: MedRecordsExpertSCP.java,v 1.17 2008/09/01 11:07:00 it2aran Exp $.
//
// Kass

package medview.medrecords.components.settings;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import javax.swing.*;
import javax.swing.event.*;

import medview.common.actions.*;
import medview.common.components.*;
import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;
import medview.common.filefilter.*;

import medview.datahandling.*;

import medview.medrecords.*;
import medview.medrecords.components.*;
import medview.medrecords.data.*;

import misc.domain.*;

import misc.gui.constants.*;

public class MedRecordsExpertSCP extends SettingsContentPanel implements MedViewLanguageConstants, GUIConstants
{
	public String getTabLS()
	{
		return TAB_MEDRECORDS_EXPERT_LS_PROPERTY;
	}

	public String getTabDescLS()
	{
		return TAB_MEDRECORDS_EXPERT_DESCRIPTION_LS_PROPERTY;
	}

	protected void initSubMembers()
	{
		ignoreEvents = false;
	}

	protected void settingsShown()
	{
		ignoreEvents = true;

		String[] text = new String[]
		{
			imageCategoryNameTextField.getText(),

			userIDTextField.getText(),

			userNameTextField.getText(),

			lockFileTextField.getText(),

			fassURLTextField.getText(),

            cariesFileTextField.getText(),
        };

		String[] propText = new String[]
		{
			prefs.getImageCategoryName(),

			MedViewDataHandler.instance().getUserID(),

			MedViewDataHandler.instance().getUserName(),

			MedViewDataHandler.instance().getPCodeNRGeneratorLocation(),

			prefs.getFASSURL(),

            prefs.getCariesFileLocation()
        };

		JTextField[] textFields = new JTextField[]
		{
			imageCategoryNameTextField,

			userIDTextField,

			userNameTextField,

			lockFileTextField,

			fassURLTextField,

            cariesFileTextField
        };

		for (int ctr = 0; ctr < text.length; ctr++)
		{
			if (!text[ctr].equals(propText[ctr]))
			{
				textFields[ctr].setText(propText[ctr]);
			}
		}

		// use document mode (new window for each examination)

		useDocModelCheckBox.setSelected(prefs.getUseDocumentMode());

		// plaque Index

		usePlaqueCheckBox.setSelected(prefs.getUsePlaqueIndex());

        // mucositis

        useMucosCheckBox.setSelected(prefs.getUseMucos());

        // show translator at new value

		showTranslatorAtNewValueCheckBox.setSelected(prefs.getShowTranslatorAtNewValue());

        // start medsummary when medrecords starts from t4

		startMedsummaryCheckBox.setSelected(prefs.getStartMedSummary());
		ignoreEvents = false;
	}

	protected void settingsHidden()
	{}

	protected void layoutPanel()
	{
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		createComponents();

		int cCS = COMPONENT_COMPONENT_SPACING;

		int cGS = COMPONENT_GROUP_SPACING;

		// image category

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0, 0, cCS, cCS);
		add(imageCategoryNameLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, 0);
		add(imageCategoryNameTextField, gbc);

		// doc mode (window management)

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, cCS);
		add(useDocModelLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, 0);
		add(useDocModelCheckBox, gbc);

		// user id

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0, 0, cCS, cCS);
		add(userIDLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, 0);
		add(userIDTextField, gbc);

		// user name

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0, 0, cCS, cCS);
		add(userNameLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, 0);
		add(userNameTextField, gbc);

		// lock file

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0, 0, cCS, cCS);
		add(lockFileLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.weightx = 100;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, cCS);
		add(lockFileTextField, gbc);

		gbc.gridx = 2;
		gbc.gridy = 4;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, 0);
		add(lockFileMoreButton, gbc);

		// caries database settings file

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0, 0, cCS, cCS);
		add(cariesFileLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.weightx = 100;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, cCS);
		add(cariesFileTextField, gbc);

		gbc.gridx = 2;
		gbc.gridy = 5;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, 0);
		add(cariesFileMoreButton, gbc);

        // FASS url

		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0, 0, cCS, cCS);
		add(fassURLLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, 0);
		add(fassURLTextField, gbc);

        // plaque

		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, cCS);
		add(usePlaqueLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, 0);
		add(usePlaqueCheckBox, gbc);

        // mucositis

		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, cCS);
		add(useMucosLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 8;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, 0);
		add(useMucosCheckBox, gbc);


        // show translator at new values

		gbc.gridx = 0;
		gbc.gridy = 9;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, cCS);
		add(showTranslatorAtNewValueLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 9;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, 0);
		add(showTranslatorAtNewValueCheckBox, gbc);

        // start medsummary at new values

		gbc.gridx = 0;
		gbc.gridy = 10;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, cCS);
		add(startMedsummaryLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 10;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, 0);
		add(startMedsummaryCheckBox, gbc);
        
        // push glue (pushes components 'up' in the panel)

		gbc.gridx = 0;
		gbc.gridy = 11;
		gbc.weightx = 0;
		gbc.weighty = 100;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(Box.createGlue(), gbc);
	}

	protected void createComponents()
	{
		TextListener listener = new TextListener();

		CheckListener checkListener = new CheckListener();

		Dimension fieldDim = new Dimension(10, BUTTON_HEIGHT_NORMAL);

		// image selector tab

		imageCategoryNameLabel = new MedViewLabel(LABEL_IMAGE_CATEGORY_NAME_LS_PROPERTY);

		imageCategoryNameLabel.setHorizontalAlignment(MedViewLabel.RIGHT);

		imageCategoryNameTextField = new JTextField();

		imageCategoryNameTextField.setPreferredSize(fieldDim);

		imageCategoryNameTextField.getDocument().addDocumentListener(listener);

		// use document model (window management)

		useDocModelLabel = new MedViewLabel(LABEL_WINDOW_MANAGEMENT_LS_PROPERTY);

		useDocModelLabel.setHorizontalAlignment(MedViewLabel.RIGHT);

		useDocModelCheckBox = new MedViewCheckBox(CHECKBOX_ENABLE_DOCUMENT_MODE_LS_PROPERTY, null, true); // lite fult

		useDocModelCheckBox.addItemListener(checkListener);

		// user id

		userIDLabel = new MedViewLabel(LABEL_USER_ID_LS_PROPERTY);

		userIDLabel.setHorizontalAlignment(MedViewLabel.RIGHT);

		userIDTextField = new JTextField();

		userIDTextField.setPreferredSize(fieldDim);

		userIDTextField.getDocument().addDocumentListener(listener);

		// user name

		userNameLabel = new MedViewLabel(LABEL_USER_NAME_LS_PROPERTY);

		userNameLabel.setHorizontalAlignment(MedViewLabel.RIGHT);

		userNameTextField = new JTextField();

		userNameTextField.setPreferredSize(fieldDim);

		userNameTextField.getDocument().addDocumentListener(listener);

		// lock file

		lockFileLabel = new MedViewLabel(LABEL_LOCKFILE_LOCATION_LS_PROPERTY);

		lockFileLabel.setHorizontalAlignment(MedViewLabel.RIGHT);

		lockFileTextField = new JTextField();

		lockFileTextField.setEditable(false);

		lockFileTextField.setBackground(Color.WHITE);

		lockFileTextField.setPreferredSize(fieldDim);

		lockFileMoreButton = new MedViewMoreButton(new ChangeLockFileLocationAction());


		// caries file

		cariesFileLabel = new MedViewLabel(LABEL_CARIESFILE_LOCATION_LS_PROPERTY);

		cariesFileLabel.setHorizontalAlignment(MedViewLabel.RIGHT);

		cariesFileTextField = new JTextField();

		cariesFileTextField.setEditable(false);

		cariesFileTextField.setBackground(Color.WHITE);

		cariesFileTextField.setPreferredSize(fieldDim);

		cariesFileMoreButton = new MedViewMoreButton(new ChangeCariesFileLocationAction());


        // FASS url

		fassURLLabel = new MedViewLabel(LABEL_FASS_URL_LS_PROPERTY);

		fassURLLabel.setHorizontalAlignment(MedViewLabel.RIGHT);

		fassURLTextField = new JTextField();

		fassURLTextField.setBackground(Color.WHITE);

		fassURLTextField.setPreferredSize(fieldDim);

		fassURLTextField.getDocument().addDocumentListener(listener);

		// plaque

		usePlaqueLabel = new MedViewLabel(LABEL_USE_PLAQUE_INDEX_LS_PROPERTY);

		usePlaqueLabel.setHorizontalAlignment(MedViewLabel.RIGHT);

		usePlaqueCheckBox = new MedViewCheckBox(CHECKBOX_USE_PLAQUE_INDEX_LS_PROPERTY, null, true);

		usePlaqueCheckBox.addItemListener(checkListener);


		// Mucos

		useMucosLabel = new MedViewLabel(LABEL_USE_MUCOS_LS_PROPERTY);

		useMucosLabel.setHorizontalAlignment(MedViewLabel.RIGHT);

		useMucosCheckBox = new MedViewCheckBox(CHECKBOX_USE_MUCOS_LS_PROPERTY, null, true);

		useMucosCheckBox.addItemListener(checkListener);

        // translator at new value

		showTranslatorAtNewValueLabel = new MedViewLabel(LABEL_SHOW_TRANSLATOR_AT_NEW_VALUE_LS_PROPERTY);

		showTranslatorAtNewValueLabel.setHorizontalAlignment(MedViewLabel.RIGHT);

		showTranslatorAtNewValueCheckBox = new MedViewCheckBox(CHECKBOX_SHOW_TRANSLATOR_AT_NEW_VALUE_LS_PROPERTY, null, false);

		showTranslatorAtNewValueCheckBox.addItemListener(checkListener);
        
        // start medsummary

		startMedsummaryLabel = new MedViewLabel(LABEL_START_MEDSUMMARY_LS_PROPERTY);

		startMedsummaryLabel.setHorizontalAlignment(MedViewLabel.RIGHT);

		startMedsummaryCheckBox = new MedViewCheckBox(CHECKBOX_START_MEDSUMMARY_LS_PROPERTY, null, false);

		startMedsummaryCheckBox.addItemListener(checkListener);
    }

	public MedRecordsExpertSCP(CommandQueue queue, Frame owner)
	{
		super(queue, new Object[0]);

		this.owner = owner;

		// sc const: initSimpleMembers() -> initLayoutManager() -> initPanelBorder() -> initSubMembers() -> layoutPanel()
	}

	private boolean ignoreEvents;

	private Frame owner;

	private PreferencesModel prefs = PreferencesModel.instance();

	// buttons

	private JButton lockFileMoreButton;

    private JButton cariesFileMoreButton;

    // check boxes

	private MedViewCheckBox useDocModelCheckBox;

	private MedViewCheckBox usePlaqueCheckBox;

    private MedViewCheckBox useMucosCheckBox;

    private MedViewCheckBox showTranslatorAtNewValueCheckBox;
    
    private MedViewCheckBox startMedsummaryCheckBox;

    // descriptive labels

	private JLabel imageCategoryNameLabel;

	private JLabel useDocModelLabel;

	private JLabel userIDLabel;

	private JLabel userNameLabel;

	private JLabel lockFileLabel;

    private JLabel cariesFileLabel;

    private JLabel fassURLLabel;

	private JLabel usePlaqueLabel;

    private JLabel useMucosLabel;

    private JLabel showTranslatorAtNewValueLabel;
    
    private JLabel startMedsummaryLabel;

    // text fields

	private JTextField imageCategoryNameTextField;

	private JTextField userIDTextField;

	private JTextField userNameTextField;

	private JTextField lockFileTextField;

    private JTextField cariesFileTextField;

    private JTextField fassURLTextField;


	// LISTENERS

	private class TextListener implements DocumentListener
	{
		public void changedUpdate(DocumentEvent e)
		{
			if (ignoreEvents) // during settings shown setup
			{
				return;
			}
		}

		public void insertUpdate(DocumentEvent e)
		{
			if (ignoreEvents) // during settings shown setup
			{
				return;
			}

			checkInsertOrRemove(e);
		}

		public void removeUpdate(DocumentEvent e)
		{
			if (ignoreEvents) // during settings shown setup
			{
				return;
			}

			checkInsertOrRemove(e);
		}

		private void checkInsertOrRemove(DocumentEvent e)
		{
			String prop, text;

			if (e.getDocument() == imageCategoryNameTextField.getDocument())
			{
				prop = PreferencesModel.ImageCategoryName;

				text = imageCategoryNameTextField.getText();

				commandQueue.addToQueue(new ChangeUserStringPropertyCommand(prop, text, PreferencesModel.class));
			}

			if (e.getDocument() == userIDTextField.getDocument())
			{
				text = userIDTextField.getText();

				commandQueue.addToQueue(new ChangeUserIDCommand(text));
			}

			if (e.getDocument() == userNameTextField.getDocument())
			{
				text = userNameTextField.getText();

				commandQueue.addToQueue(new ChangeUserNameCommand(text));
			}

			if (e.getDocument() == fassURLTextField.getDocument())
			{
				text = fassURLTextField.getText();

				commandQueue.addToQueue(new ChangeFASSURLCommand(text));
			}

			return;
		}
	}

	private class CheckListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			if (!ignoreEvents)
			{
				boolean sel = e.getStateChange() == ItemEvent.SELECTED;

				if (e.getSource() == useDocModelCheckBox)
				{
					String prop = PreferencesModel.UseDocumentMode;

					commandQueue.addToQueue(new ChangeFlagCommand(prop, sel, PreferencesModel.class));
				}
				else if (e.getSource() == usePlaqueCheckBox)
				{
					String prop = PreferencesModel.UsePlaqueIndex;

					commandQueue.addToQueue(new ChangeFlagCommand(prop, sel, PreferencesModel.class));
				}
                else if (e.getSource() == useMucosCheckBox)
                {
                        String prop = PreferencesModel.UseMucos;

                        commandQueue.addToQueue(new ChangeFlagCommand(prop, sel, PreferencesModel.class));
                }
                else if (e.getSource() == startMedsummaryCheckBox)
                {
                        String prop = PreferencesModel.StartMedSummary;

                        commandQueue.addToQueue(new ChangeFlagCommand(prop, sel, PreferencesModel.class));
                }
                else if (e.getSource() == showTranslatorAtNewValueCheckBox)
				{
					String prop = PreferencesModel.ShowTranslatorAtNewValue;

					commandQueue.addToQueue(new ChangeFlagCommand(prop, sel, PreferencesModel.class));
			}
			}
		}
	}


	// COMMANDS

	private class ChangeLockFileLocationAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			String[] exts = new String[] {".lock"};

			String lS = FILEFILTER_LOCK_FILES_LS_PROPERTY;

			MedViewDialogs mVD = MedViewDialogs.instance();

			DialogFileFilter fF = new DialogFileFilter(exts, lS);

			File file = mVD.createAndShowChooseFileDialog(owner, fF);

			if (file != null)
			{
				lockFileTextField.setText(file.getPath());

				commandQueue.addToQueue(new ChangeLockFileLocationCommand(file.getPath()));
			}
		}

		public ChangeLockFileLocationAction()
		{
			super(ACTION_CHANGE_LOCK_FILE_LOCATION);
		}
	}


	private class ChangeCariesFileLocationAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			String[] exts = new String[] {".xml"};

			String lS = FILEFILTER_XML_FILES_LS_PROPERTY;

			MedViewDialogs mVD = MedViewDialogs.instance();

			DialogFileFilter fF = new DialogFileFilter(exts, lS);

			File file = mVD.createAndShowChooseFileDialog(owner, fF);

			if (file != null)
			{
				cariesFileTextField.setText(file.getPath());

				commandQueue.addToQueue(new ChangeCariesFileLocationCommand(file.getPath()));
			}
		}

		public ChangeCariesFileLocationAction()
		{
			super(ACTION_CHANGE_CARIES_FILE_LOCATION);
		}
	}

    private class ChangeUserIDCommand implements Command
	{
		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeUserIDCommand);
		}

		public void execute()
		{
			try
			{
				MedViewDataHandler.instance().setUserID(value);
			}
			catch (InvalidUserIDException e)
			{
				String m = e.getMessage();

				MedViewDialogs.instance().createAndShowErrorDialog(owner, m);
			}
		}

		public ChangeUserIDCommand(String value)
		{
			this.value = value;
		}

		private String value;
	}

	private class ChangeUserNameCommand implements Command
	{
		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeUserNameCommand);
		}

		public void execute()
		{
			MedViewDataHandler.instance().setUserName(value);
		}

		public ChangeUserNameCommand(String value)
		{
			this.value = value;
		}

		private String value;
	}

	private class ChangeLockFileLocationCommand implements Command
	{
		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeLockFileLocationCommand);
		}

		public void execute()
		{
			MedViewDataHandler.instance().setPCodeNRGeneratorLocation(value);

			prefs.setPCodeNRGeneratorLocation(value);
		}

		public ChangeLockFileLocationCommand(String value)
		{
			this.value = value;
		}

		private String value;
	}

	private class ChangeCariesFileLocationCommand implements Command
	{
		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeCariesFileLocationCommand);
		}

		public void execute()
		{
			prefs.setCariesDatabaseLocation(value);
		}

		public ChangeCariesFileLocationCommand(String value)
		{
			this.value = value;
		}

		private String value;
	}

    private class ChangeFASSURLCommand implements Command
	{
		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeFASSURLCommand);
		}

		public void execute()
		{
			prefs.setFASSURL(url);
		}

		public ChangeFASSURLCommand(String url)
		{
			this.url = url;
		}

		private String url;
	}
}
