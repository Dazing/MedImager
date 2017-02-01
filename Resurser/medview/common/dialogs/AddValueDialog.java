/*
 * @(#)AddValueDialog.java
 *
 * $Id: AddValueDialog.java,v 1.6 2010/06/28 08:46:17 oloft Exp $
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
import javax.swing.text.*;

import medview.datahandling.MedViewLanguageConstants;

class AddValueDialog extends AbstractDialog implements MedViewLanguageConstants
{
	private ContentPanel contentPanel;

	public AddValueDialog(Frame owner)
	{
		super(owner, true, null);

		buttons[0].setEnabled(false);
	}

	public AddValueDialog(Dialog owner)
	{
		super(owner, true, null);

		buttons[0].setEnabled(false);
	}

	public Object getObjectData()
	{
		if (wasDismissed())
		{
			return null;
		}
		else
		{
			return contentPanel.textField.getText();
		}
	}

	protected JPanel createMainPanel()
	{
		return (contentPanel = new ContentPanel());
	}

	protected void registerListeners()
	{

		final JTextField textField = contentPanel.textField;

		DocumentListener docListener = new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e)
			{
			}

			public void insertUpdate(DocumentEvent e)
			{
				if (textField.getText().length() != 0)
				{
					buttons[0].setEnabled(true);
				}
				else
				{
					buttons[0].setEnabled(false);
				}
			}

			public void removeUpdate(DocumentEvent e)
			{
				insertUpdate(null);
			}
		};

		textField.getDocument().addDocumentListener(docListener);

		buttons[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setDismissed(false);

				AddValueDialog.this.dispose();
			}
		});

		buttons[1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setDismissed(true);

				AddValueDialog.this.dispose();
			}
		});
	}

	protected String getTitleLS()
	{
		return TITLE_ADD_NEW_VALUE_LS_PROPERTY;
	}

	protected String[] getButtonFacesLS()
	{
		return new String[]
		{
			BUTTON_ADD_TO_LS_PROPERTY,

			BUTTON_CANCEL_TEXT_LS_PROPERTY
		};
	}

    @Override
	public void show()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				contentPanel.textField.requestFocus();
			}
		});

		super.show();
	}

	private class ContentPanel extends JPanel
	{
		private void layoutPanel()
		{
			GridBagLayout gbl = new GridBagLayout();

			GridBagConstraints gbc = new GridBagConstraints();

			setLayout(gbl);

			// New Value label

			String prop = LABEL_NEW_VALUE_PROMPT_LS_PROPERTY;

			String labelFace = mVDH.getLanguageString(prop);

			JLabel newValue = new JLabel(labelFace);

			gbc.gridx = 0;

			gbc.gridy = 0;

			gbc.insets = new Insets(0,0,0,4);

			gbc.fill = GridBagConstraints.NONE;

			add(newValue ,gbc);

			// Text Field

			textField = new JTextField(20);

			Keymap km = textField.getKeymap();

			km.removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));

			gbc.gridx = 1;

			gbc.gridy = 0;

			gbc.insets = new Insets(0,0,0,0);

			add(textField, gbc);
		}

		public ContentPanel()
		{
			super();

			layoutPanel();
		}

		public JTextField textField;
	}
}
