/*
 * @(#)MedSummaryJournalSCP.java
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.view.settings;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import medview.common.actions.*;
import medview.common.components.*;
import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;
import medview.common.print.*;

import medview.datahandling.*;

import medview.medsummary.view.*;
import medview.medsummary.model.*;

import misc.domain.*;

import misc.gui.components.*;
import misc.gui.constants.*;

/**
 * The settings content panel that deals with settings
 * adhering to the surrounding journal template used in
 * the MedSummary application. This implementation provides
 * support for five address-lines as well as the path of
 * the logotype image, which will be scaled to a height of
 * 12 mm at rendering time.
 *
 * @author Fredrik Lindahl
 */
public class MedSummaryJournalSCP extends SettingsContentPanel implements
	MedViewPrintProperties, MedViewLanguageConstants, GUIConstants
{

	public String getTabLS()
	{
		return TAB_MEDSUMMARY_JOURNAL_LS_PROPERTY;
	}

	public String getTabDescLS()
	{
		return TAB_MEDSUMMARY_JOURNAL_DESCRIPTION_LS_PROPERTY;
	}


	private void setAddressSectionEnabled(boolean flag)
	{
		for (int ctr=0; ctr<addrLabels.length; ctr++)
		{
			addrLabels[ctr].setEnabled(flag);

			addrFields[ctr].setEnabled(flag);
		}
	}

	private void setLogotypeSectionEnabled(boolean flag)
	{
		logoPathLabel.setEnabled(flag);

		logoPathField.setEnabled(flag);

		logoPathMoreButton.setEnabled(flag);
	}


	protected void settingsShown()
	{
		ignoreEvents = true;

		if (mVDH.isPropertySet(SURROUNDING_JOURNAL_ADDRESS_LINE_1_PROPERTY)) // sync
		{
			String text = null; String propText = null;

			for (int ctr=0; ctr<addrProps.length; ctr++)
			{
				text = addrFields[ctr].getText();

				propText = mVDH.getUserProperty(addrProps[ctr]);

				if (!text.equals(propText)) { addrFields[ctr].setText(propText); }
			}

			addressButtonGroup.setSelected(useCustomAddressRadioButton.getModel(),true);
		}
		else
		{
			for (int ctr=0; ctr<addrFields.length; ctr++)
			{
				addrFields[ctr].setText(""); // make sure all fields blank if not set
			}

			addressButtonGroup.setSelected(useBuiltInAddressRadioButton.getModel(),true);
		}

		if (mVDH.isPropertySet(SURROUNDING_JOURNAL_LOGOTYPE_PATH_PROPERTY)) // sync
		{
			String text = logoPathField.getText();

			String propText = mVDH.getUserProperty(SURROUNDING_JOURNAL_LOGOTYPE_PATH_PROPERTY);

			if (!text.equals(propText)) { logoPathField.setText(propText); }

			logoButtonGroup.setSelected(useCustomLogoRadioButton.getModel(),true);
		}
		else
		{
			logoPathField.setText(""); // make sure field is blank if not set

			logoButtonGroup.setSelected(useBuiltInLogoRadioButton.getModel(),true);
		}

		ignoreEvents = false;
	}

	protected void settingsHidden()
	{
	}


	protected void initSubMembers()
	{
		ignoreEvents = false;

		medSummary = (MedSummaryFrame) subConstructorData[0];

		addrProps = new String[]
		{
			SURROUNDING_JOURNAL_ADDRESS_LINE_1_PROPERTY,

			SURROUNDING_JOURNAL_ADDRESS_LINE_2_PROPERTY,

			SURROUNDING_JOURNAL_ADDRESS_LINE_3_PROPERTY,

			SURROUNDING_JOURNAL_ADDRESS_LINE_4_PROPERTY,

			SURROUNDING_JOURNAL_ADDRESS_LINE_5_PROPERTY
		};
	}


	protected void createComponents()
	{
		initializeAddressFieldsAndLabels(); // listener is also initialized here

		logoPanel = createLogotypePathPanel();

		initializeRadioButtonsAndGroups();
	}

	protected void layoutPanel()
	{
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0,0,CCS,0);
		add(addrRadioPanel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0,0,CCS,CCS);
		add(addressLabel1, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 100;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0,0,CCS,0);
		add(addressField1, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0,0,CCS,CCS);
		add(addressLabel2, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0,0,CCS,0);
		add(addressField2, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0,0,CCS,CCS);
		add(addressLabel3, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0,0,CCS,0);
		add(addressField3, gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0,0,CCS,CCS);
		add(addressLabel4, gbc);

		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0,0,CCS,0);
		add(addressField4, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0,0,CGS,CCS);
		add(addressLabel5, gbc);

		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0,0,CGS,0);
		add(addressField5, gbc);

		// ***********************************
		// ---------- Logotype part ----------
		// ***********************************

		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0,0,CCS,0);
		add(logoRadioPanel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0,0,0,0);
		add(logoPanel, gbc);

		// ************************************
		// ------------------------------------
		// ************************************

		gbc.gridx = 0;
		gbc.gridy = 8;
		gbc.weightx = 0;
		gbc.weighty = 100;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0,0,0,0);
		add(Box.createGlue(), gbc);
	}

	private void initializeAddressFieldsAndLabels()
	{
		addressLabel1 = new MedViewLabel(LABEL_ADDRESS_LINE_1_LS_PROPERTY);
		addressLabel2 = new MedViewLabel(LABEL_ADDRESS_LINE_2_LS_PROPERTY);
		addressLabel3 = new MedViewLabel(LABEL_ADDRESS_LINE_3_LS_PROPERTY);
		addressLabel4 = new MedViewLabel(LABEL_ADDRESS_LINE_4_LS_PROPERTY);
		addressLabel5 = new MedViewLabel(LABEL_ADDRESS_LINE_5_LS_PROPERTY);

		addressField1 = new JTextField(); // if property set: text inserted at show time (settingsShown())
		addressField2 = new JTextField(); // if property set: text inserted at show time (settingsShown())
		addressField3 = new JTextField(); // if property set: text inserted at show time (settingsShown())
		addressField4 = new JTextField(); // if property set: text inserted at show time (settingsShown())
		addressField5 = new JTextField(); // if property set: text inserted at show time (settingsShown())

		addrLabels = new JLabel[]
		{
			addressLabel1,
			addressLabel2,
			addressLabel3,
			addressLabel4,
			addressLabel5
		};

		addrFields = new JTextField[]
		{
			addressField1,
			addressField2,
			addressField3,
			addressField4,
			addressField5
		};

		addrDocs = new Document[]
		{
			addressField1.getDocument(),
			addressField2.getDocument(),
			addressField3.getDocument(),
			addressField4.getDocument(),
			addressField5.getDocument()
		};

		listener = new Listener();

		for (int ctr=0; ctr<addrDocs.length; ctr++)
		{
			addrDocs[ctr].addDocumentListener(listener); // attach document listeners
		}
	}

	private JPanel createLogotypePathPanel()
	{
		JPanel retPanel = new JPanel(new BorderLayout(COMPONENT_COMPONENT_SPACING, 0)); // hgap, vgap

		logoPathLabel = new MedViewLabel(LABEL_LOGOTYPE_PATH_LS_PROPERTY);

		logoPathField = new JTextField(); // text inserted at show time (settingsShown())

		logoPathField.getDocument().addDocumentListener(listener); // attach document listener

		logoPathMoreButton = new MedViewMoreButton(new LogotypePathMoreAction());

		retPanel.add(logoPathLabel, BorderLayout.WEST);

		retPanel.add(logoPathField, BorderLayout.CENTER);

		retPanel.add(logoPathMoreButton, BorderLayout.EAST);

		return retPanel;
	}

	private void initializeRadioButtonsAndGroups()
	{
		int CCS = COMPONENT_COMPONENT_SPACING;

		addrRadioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, CCS, 0));

		addrRadioPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

		logoRadioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, CCS, 0));

		logoRadioPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

		String bIALS = RADIO_BUTTON_USE_BUILT_IN_ADDRESS_LS_PROPERTY;

		String cUALS = RADIO_BUTTON_USE_CUSTOM_ADDRESS_LS_PROPERTY;

		String bILLS = RADIO_BUTTON_USE_BUILT_IN_LOGOTYPE_LS_PROPERTY;

		String cULLS = RADIO_BUTTON_USE_CUSTOM_LOGOTYPE_LS_PROPERTY;

		useBuiltInAddressRadioButton = new MedViewRadioButton(bIALS);

		useCustomAddressRadioButton = new MedViewRadioButton(cUALS);

		useBuiltInLogoRadioButton = new MedViewRadioButton(bILLS);

		useCustomLogoRadioButton = new MedViewRadioButton(cULLS);

		addressButtonGroup = new ButtonGroup();

		addressButtonGroup.add(useBuiltInAddressRadioButton);

		addressButtonGroup.add(useCustomAddressRadioButton);

		logoButtonGroup = new ButtonGroup();

		logoButtonGroup.add(useBuiltInLogoRadioButton);

		logoButtonGroup.add(useCustomLogoRadioButton);

		ButtonModel bM = null;

		if (mVDH.isPropertySet(SURROUNDING_JOURNAL_ADDRESS_LINE_1_PROPERTY))
		{
			bM = useCustomAddressRadioButton.getModel();

			setAddressSectionEnabled(true);
		}
		else
		{
			bM = useBuiltInAddressRadioButton.getModel();

			setAddressSectionEnabled(false);
		}

		addressButtonGroup.setSelected(bM, true);

		if (mVDH.isPropertySet(SURROUNDING_JOURNAL_LOGOTYPE_PATH_PROPERTY))
		{
			bM = useCustomLogoRadioButton.getModel();

			setLogotypeSectionEnabled(true);
		}
		else
		{
			bM = useBuiltInLogoRadioButton.getModel();

			setLogotypeSectionEnabled(false);
		}

		logoButtonGroup.setSelected(bM, true);

		useBuiltInAddressRadioButton.addItemListener(listener);

		useCustomAddressRadioButton.addItemListener(listener);

		useBuiltInLogoRadioButton.addItemListener(listener);

		useCustomLogoRadioButton.addItemListener(listener);

		addrRadioPanel.add(useBuiltInAddressRadioButton);

		addrRadioPanel.add(useCustomAddressRadioButton);

		logoRadioPanel.add(useBuiltInLogoRadioButton);

		logoRadioPanel.add(useCustomLogoRadioButton);
	}





	public MedSummaryJournalSCP(CommandQueue queue, MedSummaryFrame medSummary)
	{
		super(queue, new Object[] { medSummary });
	}

	private MedSummaryFrame medSummary;

	private JTextField[] addrFields;

	private Document[] addrDocs;

	private JLabel[] addrLabels;

	private String[] addrProps;

	private Listener listener;

	private JPanel logoPanel;

	private JPanel addrRadioPanel;

	private JPanel logoRadioPanel;

	private JLabel addressLabel1;

	private JLabel addressLabel2;

	private JLabel addressLabel3;

	private JLabel addressLabel4;

	private JLabel addressLabel5;

	private JTextField addressField1;

	private JTextField addressField2;

	private JTextField addressField3;

	private JTextField addressField4;

	private JTextField addressField5;

	private JLabel logoPathLabel;

	private JTextField logoPathField;

	private JButton logoPathMoreButton;

	private JRadioButton useBuiltInAddressRadioButton;

	private JRadioButton useCustomAddressRadioButton;

	private JRadioButton useBuiltInLogoRadioButton;

	private JRadioButton useCustomLogoRadioButton;

	private ButtonGroup addressButtonGroup;

	private ButtonGroup logoButtonGroup;

	private boolean ignoreEvents;

	/* NOTE: the superclass protected variable
	 * 'subConstructorData' has the purpose of allowing
	 * the subclasses to pass some data that needs
	 * to be initialized before the other template
	 * methods are called. In this case, the
	 * mediator reference needs to be set up before
	 * the panel is laid out, since the various
	 * subpanels in use in the main panel needs to
	 * obtain information from the mediator. You
	 * cannot set the mediator reference after the
	 * superclass constructor has been called since
	 * the methods would then only have a null
	 * reference. */





	private class LogotypePathMoreAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			MedViewDialogs mVD = MedViewDialogs.instance();

			String filePath = mVD.createAndShowChooseImageDialog(medSummary);

			if (filePath != null) {	logoPathField.setText(filePath); } // only sets text
		}

		public LogotypePathMoreAction()
		{
			super(ACTION_CHANGE_JOURNAL_LOGOTYPE_LOCATION);
		}
	}

	private class Listener implements DocumentListener, ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			if (ignoreEvents) { return; }

			if (e.getStateChange() == ItemEvent.SELECTED)
			{
				if (e.getSource() == useBuiltInAddressRadioButton)
				{
					clearAddressSection(); // visually only, no commands

					setAddressSectionEnabled(false);

					commandQueue.addToQueue(new ClearAddressLinesCommand());

					/* NOTE: when adding the ClearAddr.. command
					 * above, any previous commands for setting
					 * the address line properties will be removed
					 * from the command queue. */
				}
				else if (e.getSource() == useCustomAddressRadioButton)
				{
					clearAddressSection(); // visually only, no commands

					for (int ctr=0; ctr<addrProps.length; ctr++)
					{
						commandQueue.addToQueue(new AddrChangeUserStringPropertyCommand(addrProps[ctr], ""));
					}

					setAddressSectionEnabled(true);

					/* NOTE: eventual existing previous address
					 * change commands will be removed from the
					 * command queue. Also, if any eventual
					 * clear address lines command is in the queue,
					 * this will also be removed. Dito for the
					 * logotype commands below. */
				}
				else if (e.getSource() == useBuiltInLogoRadioButton)
				{
					clearLogotypeSection(); // visually only, no commands

					setLogotypeSectionEnabled(false);

					commandQueue.addToQueue(new ClearLogotypeCommand());
				}
				else if (e.getSource() == useCustomLogoRadioButton)
				{
					clearLogotypeSection(); // visually only, no commands

					String p1 = SURROUNDING_JOURNAL_LOGOTYPE_PATH_PROPERTY;

					commandQueue.addToQueue(new LogoChangeUserStringPropertyCommand(p1, ""));

					setLogotypeSectionEnabled(true);
				}
			}
		}

		private void clearAddressSection()
		{
			for (int ctr=0; ctr<addrFields.length; ctr++)
			{
				addrFields[ctr].setText("");
			}
		}

		private void clearLogotypeSection()
		{
			logoPathField.setText("");
		}

		public void changedUpdate(DocumentEvent e)
		{
			if (ignoreEvents) { return; }
		}

		public void insertUpdate(DocumentEvent e)
		{
			if (ignoreEvents) { return; }

			checkInsertOrRemove(e);
		}

		public void removeUpdate(DocumentEvent e)
		{
			if (ignoreEvents) { return; }

			checkInsertOrRemove(e);
		}

		private void checkInsertOrRemove(DocumentEvent e)
		{
			if (e.getDocument() == logoPathField.getDocument()) // logo path
			{
				String prop = SURROUNDING_JOURNAL_LOGOTYPE_PATH_PROPERTY;

				String text = logoPathField.getText();

				commandQueue.addToQueue(new LogoChangeUserStringPropertyCommand(prop,text));
			}
			else // address field
			{
				int index = -1;

				for (int ctr=0; ctr<addrDocs.length; ctr++) // find out index of document source
				{
					if (addrDocs[ctr] == e.getDocument()) { index = ctr; break; }
				}

				String prop = addrProps[index]; // property

				String text = addrFields[index].getText(); // field content

				commandQueue.addToQueue(new AddrChangeUserStringPropertyCommand(prop,text));
			}
		}
	}



	private class AddrChangeUserStringPropertyCommand extends ChangeUserStringPropertyCommand
	{
		public boolean shouldReplace(Command command)
		{
			boolean b = super.shouldReplace(command); // checks property name

			return ((b)? true : command instanceof ClearAddressLinesCommand);
		}

		public AddrChangeUserStringPropertyCommand(String prop, String val)
		{
			super(prop, val);
		}
	}

	private class LogoChangeUserStringPropertyCommand extends ChangeUserStringPropertyCommand
	{
		public boolean shouldReplace(Command command)
		{
			boolean b = super.shouldReplace(command); // checks property name

			return ((b)? true : command instanceof ClearLogotypeCommand);
		}

		public LogoChangeUserStringPropertyCommand(String prop, String val)
		{
			super(prop, val);
		}
	}



	private class ClearAddressLinesCommand implements Command
	{
		public void execute()
		{
			for (int ctr=0; ctr<addrProps.length; ctr++)
			{
				mVDH.clearProperty(addrProps[ctr]);
			}
		}

		public boolean shouldReplace(Command command)
		{
			if (command instanceof ClearAddressLinesCommand) { return true; }

			if (command instanceof ChangeUserStringPropertyCommand)
			{
				temp = (ChangeUserStringPropertyCommand) command;

				for (int ctr=0; ctr<addrProps.length; ctr++)
				{
					if (addrProps[ctr].equals(temp.getProperty()))
					{
						return true; // removes eventual addr set command
					}
				}
			}

			return false;
		}

		private ChangeUserStringPropertyCommand temp;
	}

	private class ClearLogotypeCommand implements Command
	{
		public void execute()
		{
			mVDH.clearProperty(prop);
		}

		public boolean shouldReplace(Command command)
		{
			if (command instanceof ClearLogotypeCommand) { return true; }

			if (command instanceof ChangeUserStringPropertyCommand)
			{
				temp = (ChangeUserStringPropertyCommand) command;

				if (temp.getProperty().equals(prop)) { return true; }
			}

			return false;
		}

		private ChangeUserStringPropertyCommand temp;

		private String prop = SURROUNDING_JOURNAL_LOGOTYPE_PATH_PROPERTY;
	}
}
