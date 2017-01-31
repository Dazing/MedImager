/*
 * @(#)NewTranslatorDialog.java
 *
 * $Id: NewTranslatorDialog.java,v 1.4 2006/04/24 14:17:01 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import medview.datahandling.*;

public class NewTranslatorDialog extends AbstractDialog implements MedViewLanguageConstants
{
	private JLabel promptLabel;

	private JTextField nameField;

	public NewTranslatorDialog(Frame owner)
	{
		super(owner, true, null);

		buttons[0].setEnabled(false);
	}

	public NewTranslatorDialog(Dialog owner)
	{
		super(owner, true, null);

		buttons[0].setEnabled(false);
	}

	protected JPanel createMainPanel()
	{
		GridBagLayout gbl = new GridBagLayout();

		GridBagConstraints gbc = new GridBagConstraints();

		JPanel retPanel = new JPanel(gbl);

		initPromptLabel();

		initNameField();

		gbc.gridx = 0;

		gbc.gridy = 0;

		gbc.insets = new Insets(0,0,0,5);

		gbc.fill = GridBagConstraints.BOTH;

		retPanel.add(promptLabel, gbc);

		gbc.gridx = 1;

		gbc.gridy = 0;

		gbc.insets = new Insets(0,0,0,0);

		gbc.fill = GridBagConstraints.BOTH;

		retPanel.add(nameField, gbc);

		return retPanel;
	}

	protected void initPromptLabel()
	{
		String face = mVDH.getLanguageString(LABEL_NEW_TRANSLATOR_PROMPT_LS_PROPERTY);

		promptLabel = new JLabel(face);
	}

	protected void initNameField()
	{
		nameField = new JTextField(10);
	}

	protected String[] getButtonFacesLS()
	{
		return new String[]
		{
			BUTTON_OK_LS_PROPERTY, BUTTON_CANCEL_TEXT_LS_PROPERTY
		};
	}

	protected String getTitleLS()
	{
		return TITLE_NEW_TRANSLATOR_DIALOG_LS_PROPERTY;
	}

	protected void registerListeners()
	{
		nameField.getDocument().addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e) {}

			public void insertUpdate(DocumentEvent e)
			{
				if (e.getDocument().getLength() == 0)
				{
					buttons[0].setEnabled(false);
				}
				else
				{
					buttons[0].setEnabled(true);
				}
			}

			public void removeUpdate(DocumentEvent e)
			{
				insertUpdate(e);
			}
		});


		buttons[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setDismissed(false);

				NewTranslatorDialog.this.dispose();
			}
		});

		buttons[1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setDismissed(true);

				NewTranslatorDialog.this.dispose();
			}
		});
	}

	/**
	 * @return the entered name of the
	 * new translator, or null if the
	 * dialog was dismissed.
	 */
	public Object getObjectData()
	{
		if (!wasDismissed())
		{
			return nameField.getText();
		}
		else
		{
			return null;
		}
	}
}
