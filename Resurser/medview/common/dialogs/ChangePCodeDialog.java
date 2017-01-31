/*
 * @(#)ChangePCodeDialog.java
 *
 * $Id: ChangePCodeDialog.java,v 1.6 2010/06/28 08:46:17 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs;

import medview.datahandling.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

class ChangePCodeDialog extends AbstractDialog implements MedViewLanguageConstants
{
	private ContentPanel contentPanel;

	public ChangePCodeDialog(Frame owner)
	{
		super(owner, true, null);

		buttons[0].setEnabled(false);
	}

	public ChangePCodeDialog(Dialog owner)
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
		return contentPanel = new ContentPanel();
	}

	protected String[] getButtonFacesLS()
	{
		return new String[]
		{
			BUTTON_NEW_PCODE_LS_PROPERTY,

			BUTTON_CANCEL_TEXT_LS_PROPERTY
		};
	}

	protected String getTitleLS()
	{
		return TITLE_CHANGE_PREVIEW_PCODE_LS_PROPERTY;
	}

	protected void registerListeners()
	{
		buttons[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setDismissed(false);

				ChangePCodeDialog.this.dispose();
			}
		});

		buttons[1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setDismissed(true);

				ChangePCodeDialog.this.dispose();
			}
		});



		final JTextField textField = contentPanel.textField;

		Document doc = textField.getDocument();

		doc.addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e) {}

			public void insertUpdate(DocumentEvent e)
			{
				String text = textField.getText();

				if (text.length() == 0)
				{
					buttons[0].setEnabled(false);
				}
				else
				{
					if (mVDH.validates(text))
					{
						buttons[0].setEnabled(true);
					}
					else
					{
						buttons[0].setEnabled(false);
					}
				}
			}

			public void removeUpdate(DocumentEvent e)
			{
				insertUpdate(e);
			}
		});
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
		public ContentPanel()
		{
			GridBagLayout gbl = new GridBagLayout();

			GridBagConstraints gbc = new GridBagConstraints();

			setLayout(gbl);


			// New PCode Label

			String prop = LABEL_NEW_PCODE_LS_PROPERTY;

			String label = mVDH.getLanguageString(prop);

			JLabel pCodeL = new JLabel(label);

			gbc.gridx = 0;

			gbc.gridy = 0;

			gbc.insets = new Insets(0,0,0,4);

			gbc.fill = GridBagConstraints.BOTH;

			add(pCodeL, gbc);


			// Textfield

			textField = new JTextField(15);

			gbc.gridx = 1;

			gbc.gridy = 0;

			gbc.insets = new Insets(0,0,0,0);

			gbc.fill = GridBagConstraints.BOTH;

			add(textField, gbc);

		}

		private JTextField textField;
	}
}
