package medview.common.dialogs;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import medview.datahandling.MedViewLanguageConstants;

import misc.gui.constants.*;

/**
 * A simple dialog for changing the name of
 * something. When constructed, takes the original
 * name to display, and will only enable 'apply'
 * button after the text is different from this
 * original and not of zero length.
 * @author Fredrik Lindahl
 */
class ChangeNameDialog extends AbstractDialog implements MedViewLanguageConstants
{
	private String origText;

	private String descText;

	private int allowedLength;

	private ContentPanel contentPanel;

	public ChangeNameDialog(Frame owner, String origText, String descText, int allowedLength)
	{
		super(owner, true, new Object[] { origText, descText, new Integer(allowedLength) });

		buttons[0].setEnabled(false);
	}

	public ChangeNameDialog(Dialog owner, String origText, String descText, int allowedLength)
	{
		super(owner, true, new Object[] { origText, descText, new Integer(allowedLength) });

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
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run() // cannot mutate text field on notification
					{
						String text = textField.getText();

						boolean tooLong = text.length() > allowedLength;

						if (tooLong)
						{
							Toolkit.getDefaultToolkit().beep();

							text = text.substring(0, allowedLength);

							textField.setText(text);
						}

						boolean isLengthZero = text.length() == 0;

						boolean isTextOriginal = text.equals(origText);

						if (!isLengthZero && !isTextOriginal)
						{
							buttons[0].setEnabled(true);
						}
						else
						{
							buttons[0].setEnabled(false);
						}
					}
				});
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

				ChangeNameDialog.this.dispose();
			}
		});

		buttons[1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setDismissed(true);

				ChangeNameDialog.this.dispose();
			}
		});
	}

	protected String getTitleLS()
	{
		return TITLE_CHANGE_NAME_LS_PROPERTY;
	}

	protected String[] getButtonFacesLS()
	{
		return new String[]
		{
			BUTTON_APPLY_LS_PROPERTY,

			BUTTON_CANCEL_TEXT_LS_PROPERTY
		};
	}

    @Override
	protected void initSubClassMembers()
	{
		this.origText = (String) additionalData[0];

		this.allowedLength = ((Integer)additionalData[2]).intValue();

		if (additionalData[1] != null)
		{
			descText = (String) additionalData[1];
		}
	}

	private class ContentPanel extends JPanel
	{
		public ContentPanel()
		{
			super();

			GridBagLayout gbl = new GridBagLayout();

			GridBagConstraints gbc = new GridBagConstraints();

			setLayout(gbl);

			// prompt

			int cCS = GUIConstants.COMPONENT_COMPONENT_SPACING;

			String prop = LABEL_NAME_PROMPT_LS_PROPERTY;

			String labelFace = mVDH.getLanguageString(prop);

			JLabel namePrompt = new JLabel(labelFace);

			gbc.gridx = 0;

			gbc.gridy = 0;

			gbc.insets = new Insets(0,0,cCS,cCS);

			gbc.fill = GridBagConstraints.NONE;

			gbc.gridwidth = 1;

			add(namePrompt ,gbc);

			// text field

			textField = new JTextField(25);

			Keymap km = textField.getKeymap();

			km.removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));

			gbc.gridx = 1;

			gbc.gridy = 0;

			gbc.weightx = 0;

			gbc.insets = new Insets(0,0,cCS,0);

			gbc.fill = GridBagConstraints.BOTH;

			gbc.gridwidth = 1;

			add(textField, gbc);

			// descriptive text (if non-null)

			if (descText != null)
			{
				int align = SwingConstants.CENTER;

				JLabel descLabel = new JLabel(descText, align);

				gbc.gridx = 0;

				gbc.gridy = 1;

				gbc.weightx = 0;

				gbc.insets = new Insets(0,0,0,0);

				gbc.anchor = GridBagConstraints.CENTER;

				gbc.fill = GridBagConstraints.BOTH;

				gbc.gridwidth = 2;

				add(descLabel, gbc);
			}
		}

		public JTextField textField;
	}

}
