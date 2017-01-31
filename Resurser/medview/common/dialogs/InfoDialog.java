/*
 * @(#)InfoDialog.java
 *
 * $Id: InfoDialog.java,v 1.4 2010/06/28 08:46:17 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import medview.datahandling.*;

public class InfoDialog extends AbstractDialog implements MedViewMediaConstants, MedViewLanguageConstants
{
	public InfoDialog(Frame owner, String message)
	{
		super(owner, true, new Object[] { message });
	}

	public InfoDialog(Dialog owner, String message)
	{
		super(owner, true, new Object[] { message });
	}

	public Object getObjectData()
	{
		return null;
	}

	protected String getTitleLS()
	{
		return TITLE_INFO_LS_PROPERTY;
	}

	protected String[] getButtonFacesLS()
	{
		return new String[]
		{
			BUTTON_OK_LS_PROPERTY
		};
	}

	protected JPanel createMainPanel()
	{
		JPanel retPanel = new JPanel();

		JLabel errorImage = new JLabel(mVDH.getImageIcon(INFORMATION_IMAGE_ICON_40));

		retPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));

		retPanel.add(errorImage);

		String message = (String) additionalData[0];

		if (message.length() > 50) // use text area
		{
			JTextArea area = new JTextArea(message);

			area.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

			area.setWrapStyleWord(true); area.setEditable(false);

			area.setLineWrap(true); area.setOpaque(false);

			area.setSize(new Dimension(300,1)); // force pref size calc

			area.setFont(UIManager.getFont("Label.font")); // look like label

			retPanel.add(area);
		}
		else // use label
		{
			JLabel mLabel = new JLabel(message);

			retPanel.add(mLabel);
		}

		return retPanel;
	}

	protected void registerListeners()
	{
		buttons[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				InfoDialog.this.dispose();
			}
		});
	}
}
