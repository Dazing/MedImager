/*
 * @(#)AddSectionDialog.java
 *
 * $Id: RenameSectionDialog.java,v 1.4 2006/04/24 14:17:02 lindahlf Exp $
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

import misc.gui.constants.*;

class RenameSectionDialog extends AbstractDialog implements MedViewLanguageConstants
{
	public JLabel currentLabel;

	public JLabel newDescLabel;

	public JTextField newTextField;

	public JLabel currentDescLabel;

	public RenameSectionDialog(Frame owner, String currName)
	{
		super(owner, true, new Object[] { currName });

		buttons[0].setEnabled(false);
	}

	public RenameSectionDialog(Dialog owner, String currName)
	{
		super(owner, true, new Object[] { currName });

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
			return newTextField.getText();
		}
	}

	protected JPanel createMainPanel()
	{
		GridBagLayout gbl = new GridBagLayout();

		GridBagConstraints gbc = new GridBagConstraints();

		JPanel retPanel = new JPanel(gbl);

		createCurrentDescLabel();

		createCurrentLabel();

		createNewDescLabel();

		createNewTextField();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0,0,5,5);
		retPanel.add(currentDescLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0,0,5,0);
		retPanel.add(currentLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0,0,0,5);
		retPanel.add(newDescLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0,0,0,0);
		retPanel.add(newTextField, gbc);

		return retPanel;
	}

	protected String[] getButtonFacesLS()
	{
		return new String[]
		{
			BUTTON_RENAME_LS_PROPERTY,

			BUTTON_CANCEL_TEXT_LS_PROPERTY
		};
	}

	protected String getTitleLS()
	{
		return TITLE_RENAME_SECTION_LS_PROPERTY;
	}

	protected void registerListeners()
	{
		buttons[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setDismissed(false);

				RenameSectionDialog.this.dispose();
			}
		});

		buttons[1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setDismissed(true);

				RenameSectionDialog.this.dispose();
			}
		});

		Document doc = newTextField.getDocument();

		doc.addDocumentListener(new DocumentListener()
		{
			public void changedUpdate(DocumentEvent e) {}

			public void insertUpdate(DocumentEvent e)
			{
				buttons[0].setEnabled(newTextField.getText().length() != 0);
			}

			public void removeUpdate(DocumentEvent e)
			{
				insertUpdate(e);
			}
		});
	}

	private void createCurrentDescLabel()
	{
		String lS = LABEL_EARLIER_SECTION_NAME_LS_PROPERTY;

		String text = mVDH.getLanguageString(lS);

		currentDescLabel = new JLabel(text);
	}

	private void createCurrentLabel()
	{
		String currName = (String) additionalData[0];

		currentLabel = new JLabel(currName);
	}

	private void createNewDescLabel()
	{
		String lS = LABEL_NEW_SECTION_NAME_LS_PROPERTY;

		String text = mVDH.getLanguageString(lS);

		newDescLabel = new JLabel(text);
	}

	private void createNewTextField()
	{
		newTextField = new JTextField();

		int height = GUIConstants.TEXTFIELD_HEIGHT_NORMAL;

		int width = GUIConstants.TEXTFIELD_WIDTH_NORMAL;

		newTextField.setPreferredSize(new Dimension(width, height));
	}

	public void show()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				newTextField.requestFocus();
			}
		});

		super.show();
	}
}
