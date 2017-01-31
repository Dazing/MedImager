package medview.medsummary.view.settings;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

import medview.common.components.*;
import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.medsummary.view.*;
import medview.medsummary.model.*;

import misc.domain.*;

import misc.gui.constants.*;

/**
 * @author Fredrik Lindahl
 */
public class MedSummaryExpertSCP extends SettingsContentPanel implements
	MedSummaryUserProperties, GUIConstants, MedViewLanguageConstants,
	MedSummaryConstants
{

	// MISC

	public String getTabLS()
	{
		return TAB_MEDSUMMARY_EXPERT_LS_PROPERTY;
	}

	public String getTabDescLS()
	{
		return TAB_MEDSUMMARY_EXPERT_DESCRIPTION_LS_PROPERTY;
	}

	protected void initSubMembers()
	{
		mediator = (MedSummaryFrame) subConstructorData[0];
	}

	protected void settingsShown()
	{
		ignoreEvents = true; // indicate that we are in a preparatory phase (disable all events)

		fassURLTextField.setText(mVDH.getUserStringPreference(

			FASS_URL_PROPERTY, DEFAULT_FASS_URL, MedSummaryUserProperties.class));

		ignoreEvents = false;
	}


	// LAYOUT PANEL (CALLED BY SUPERCLASS)

	protected void layoutPanel()
	{
		// prepare layout

		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();

		int cCS = COMPONENT_COMPONENT_SPACING;

		int cGS = COMPONENT_GROUP_SPACING;

		// FASS url

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0, 0, cCS, cCS);
		add(fassURLDescLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 100;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, cCS, 0);
		add(fassURLTextField, gbc);

		// push glue (pushes components 'up' in the panel)

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 100;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(0, 0, 0, 0);
		add(Box.createGlue(), gbc);
	}

	protected void createComponents()
	{
		listener = new TextListener();

		Dimension fieldDim = new Dimension(10, BUTTON_HEIGHT_NORMAL);

		// FASS url

		fassURLDescLabel = new MedViewLabel(LABEL_FASS_URL_LS_PROPERTY);

		fassURLDescLabel.setHorizontalAlignment(MedViewLabel.RIGHT);

		fassURLTextField = new JTextField();

		fassURLTextField.setBackground(Color.WHITE);

		fassURLTextField.setPreferredSize(fieldDim);

		fassURLTextField.getDocument().addDocumentListener(listener);
	}


	// CONSTRUCTOR

	public MedSummaryExpertSCP(CommandQueue queue, MedSummaryFrame mediator)
	{
		super(queue, new Object[] { mediator });

		// sc const: initSimpleMembers() -> initLayoutManager() -> initPanelBorder() -> initSubMembers() -> layoutPanel()
	}

	private boolean ignoreEvents;

	private MedSummaryFrame mediator;

	private JLabel fassURLDescLabel;

	private JTextField fassURLTextField;

	private TextListener listener;


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
			String text = null;

			if (e.getDocument() == fassURLTextField.getDocument())
			{
				text = fassURLTextField.getText();

				commandQueue.addToQueue(new ChangeFASSURLCommand(text));
			}

			return;
		}
	}


	// COMMANDS

	private class ChangeFASSURLCommand implements Command
	{
		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangeFASSURLCommand);
		}

		public void execute()
		{
			MedViewDataHandler mVDH = MedViewDataHandler.instance();

			mVDH.setUserStringPreference(FASS_URL_PROPERTY, url,

				MedSummaryUserProperties.class);
		}

		public ChangeFASSURLCommand(String url)
		{
			this.url = url;
		}

		private String url;
	}
}
