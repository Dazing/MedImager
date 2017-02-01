/*
 * @(#)AddIntervalDialog.java
 *
 * $Id: AddIntervalDialog.java,v 1.6 2010/06/28 08:48:50 oloft Exp $
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

import medview.datahandling.*;

import misc.domain.*;

class AddIntervalDialog extends AbstractDialog implements MedViewLanguageConstants
{
	private ContentPanel contentPanel;

	public AddIntervalDialog(Frame owner)
	{
		super(owner, true, null);

		buttons[0].setEnabled(false);
	}

	public AddIntervalDialog(Dialog owner)
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
			String minS = contentPanel.minTextField.getText();

			String maxS = contentPanel.maxTextField.getText();

			float min = Float.parseFloat(minS);

			float max = Float.parseFloat(maxS);

			return new Interval(min, max);
		}
	}

	protected JPanel createMainPanel()
	{
		return (contentPanel = new ContentPanel());
	}

	protected void registerListeners()
	{
		final JTextField minTextField = contentPanel.minTextField;

		final JTextField maxTextField = contentPanel.maxTextField;

			class LocalListener implements DocumentListener
			{
				public void changedUpdate(DocumentEvent e) {}

				public void insertUpdate(DocumentEvent e)
				{
					if ((minTextField.getText().length() != 0) &&

						(maxTextField.getText().length() != 0))
					{
						try
						{
							float minFloat = Float.parseFloat(minTextField.getText());

							float maxFloat = Float.parseFloat(maxTextField.getText());

							if (minFloat < maxFloat)
							{
								buttons[0].setEnabled(true);
							}
							else
							{
								buttons[0].setEnabled(false);
							}
						}
						catch (NumberFormatException nFE)
						{
							buttons[0].setEnabled(false);
						}
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
			}

		DocumentListener lL = new LocalListener();

		minTextField.getDocument().addDocumentListener(lL);

		maxTextField.getDocument().addDocumentListener(lL);

		buttons[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setDismissed(false);

				AddIntervalDialog.this.dispose();
			}
		});

		buttons[1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setDismissed(true);

				AddIntervalDialog.this.dispose();
			}
		});
	}

	protected String[] getButtonFacesLS()
	{
		return new String[]
		{
			BUTTON_ADD_TO_LS_PROPERTY,

			BUTTON_CANCEL_TEXT_LS_PROPERTY
		};
	}

	protected String getTitleLS()
	{
		return TITLE_ADD_NEW_INTERVAL_LS_PROPERTY;
	}

    @Override
	public void show()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				contentPanel.minTextField.requestFocus();
			}
		});

		super.show();
	}

	private class ContentPanel extends JPanel
	{
		private void layoutContent()
		{
			GridBagLayout gbl = new GridBagLayout();

			GridBagConstraints gbc = new GridBagConstraints();

			setLayout(gbl);

			// New Interval Label

			String prop = LABEL_NEW_INTERVAL_PROMPT_LS_PROPERTY;

			String label = mVDH.getLanguageString(prop);

			JLabel newLabel = new JLabel(label);

			gbc.gridx = 0;

			gbc.gridy = 0;

			gbc.insets = new Insets(0,0,0,4);

			gbc.fill = GridBagConstraints.NONE;

			add(newLabel, gbc);

			// Minimum Text Field

			minTextField = new JTextField(5);

			Keymap keymap = minTextField.getKeymap();

			KeyStroke eK = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);

			keymap.removeKeyStrokeBinding(eK);

			gbc.gridx = 1;

			gbc.gridy = 0;

			gbc.insets = new Insets(0,0,0,2);

			gbc.fill = GridBagConstraints.HORIZONTAL;

			add(minTextField, gbc);

			// Separator label

			gbc.gridx = 2;

			gbc.gridy = 0;

			gbc.insets = new Insets(0,0,0,2);

			gbc.fill = GridBagConstraints.NONE;

			add(new JLabel(" - "), gbc);

			// Maximum Text Field

			maxTextField = new JTextField(5);

			keymap = maxTextField.getKeymap();

			eK = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);

			keymap.removeKeyStrokeBinding(eK);

			gbc.gridx = 3;

			gbc.gridy = 0;

			gbc.insets = new Insets(0,0,0,0);

			gbc.fill = GridBagConstraints.HORIZONTAL;

			add(maxTextField, gbc);
		}

		public ContentPanel()
		{
			layoutContent();
		}

		public JTextField minTextField;

		public JTextField maxTextField;
	}
}
