/*
 * @(#)QuestionDialog.java
 *
 * $Id: QuestionDialog.java,v 1.6 2006/04/24 14:17:01 lindahlf Exp $
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

/**
 * A dialog displaying a question mark along with
 * a question. Short questions will be displayed in
 * one line (less than 30 characters), while longer
 * questions will be displayed over several lines.
 *
 * @author Fredrik Lindahl
 */
class QuestionDialog extends AbstractDialog implements MedViewLanguageConstants, MedViewMediaConstants, MedViewDialogConstants
{
	private int lastChoice;

	public QuestionDialog(Frame owner, int type, String mess)
	{
		super(owner, true, new Object[] {new Integer(type), mess});

		this.lastChoice = -1;
	}

	public QuestionDialog(Dialog owner, int type, String mess)
	{
		super(owner, true, new Object[] {new Integer(type), mess});

		this.lastChoice = -1;
	}

	public Object getObjectData()
	{
		return new Integer(lastChoice);
	}

	protected JPanel createMainPanel()
	{
		return new ContentPanel();
	}

	protected String[] getButtonFacesLS()
	{
		int type = ((Integer) additionalData[0]).intValue();

		switch (type)
		{
			case YES_NO:
			{
				return new String[]
				{
					BUTTON_YES_LS_PROPERTY, BUTTON_NO_LS_PROPERTY
				};
			}

			default:
			{
				return new String[]
				{
					BUTTON_YES_LS_PROPERTY, BUTTON_NO_LS_PROPERTY, BUTTON_CANCEL_TEXT_LS_PROPERTY
				};
			}
		}
	}

	protected String getTitleLS()
	{
		return TITLE_QUESTION_LS_PROPERTY;
	}

	protected void registerListeners()
	{
		buttons[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				lastChoice = YES;

				QuestionDialog.this.dispose();
			}
		});

		buttons[1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				lastChoice = NO;

				QuestionDialog.this.dispose();
			}
		});


		int type = ((Integer) additionalData[0]).intValue();

		if (type == YES_NO_CANCEL)
		{
			buttons[2].addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					lastChoice = CANCEL;

					QuestionDialog.this.dispose();
				}
			});
		}
	}

	private class ContentPanel extends JPanel
	{
		public ContentPanel()
		{
			setLayout(new GridBagLayout());

			GridBagConstraints gbc = new GridBagConstraints();

			ImageIcon questionIcon = mVDH.getImageIcon(QUESTION_IMAGE_ICON_40);

			JLabel label = new JLabel(questionIcon);

			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 0;
			gbc.insets = new Insets(0,0,0,12);
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.WEST;
			add(label, gbc);

			String message = (String) additionalData[1];

			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.weightx = 0;
			gbc.insets = new Insets(0,0,0,0);
			gbc.fill = GridBagConstraints.NONE;
			gbc.anchor = GridBagConstraints.CENTER;

			if (message.length() > 50) // use text area
			{
				JTextArea area = new JTextArea(message);

				area.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

				area.setWrapStyleWord(true); area.setEditable(false);

				area.setLineWrap(true); area.setOpaque(false);

				area.setSize(new Dimension(300,1)); // force pref size calc

				area.setFont(UIManager.getFont("Label.font")); // look like label

				add(area, gbc);
			}
			else // use label
			{
				JLabel mLabel = new JLabel(message);

				add(mLabel, gbc);
			}
		}
	}
}
