// $Id: AboutDialog.java,v 1.6 2010/06/28 08:48:07 oloft Exp $

package medview.common.dialogs;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import medview.datahandling.MedViewLanguageConstants;
import medview.datahandling.MedViewMediaConstants;

/**
 * A dialog class for displaying typical 'about'
 * dialogs within the MedView context.
 *
 * @author Fredrik Lindahl
 */
public class AboutDialog extends AbstractDialog implements MedViewLanguageConstants, MedViewMediaConstants
{
	public AboutDialog(Frame owner, String titLS, String aN, String vN, String txtLS)
	{
		super(owner, true, new Object[] { titLS, aN, vN, txtLS });

		setDismissed(true);
	}

	public AboutDialog(Dialog owner, String titLS, String aN, String vN, String txtLS)
	{
		super(owner, true, new Object[] { titLS, aN, vN, txtLS });

		setDismissed(true);
	}

	public Object getObjectData() { return null; }

	protected int getMainPanelHorizontalSpacing() { return 0; }

	public JPanel createMainPanel()
	{
		GridBagLayout gbl = new GridBagLayout();

		GridBagConstraints gbc = new GridBagConstraints();

		JPanel retPanel = new JPanel(gbl);

		JLabel imageLabel = createImageLabel();

		JLabel appNameLabel = createAppNameLabel();

		JLabel versionLabel = createVersionLabel();

		JPanel textPanel = createTextPanel();

		int bHS = getBarHSpacing();
		int bVS = getBarVSpacing();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0,bHS,0,bHS);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		retPanel.add(imageLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0,bHS,0,bHS);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		retPanel.add(appNameLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0,bHS,bVS,bHS);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		retPanel.add(versionLabel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0,bHS,bVS,bHS);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		retPanel.add(new JSeparator(), gbc);

		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0,bHS,0,bHS);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		retPanel.add(textPanel, gbc);

		return retPanel;
	}

	private JLabel createImageLabel()
	{
		ImageIcon i = mVDH.getImageIcon(ABOUT_MEDVIEW_IMAGE_ICON);

		return new JLabel(i);
	}

	private JLabel createAppNameLabel()
	{
		String appName = (String) additionalData[1];

		JLabel retLabel = new JLabel(appName, JLabel.CENTER);

		Font labelFont = UIManager.getFont("Label.font"); // l&f font style

		String fontName = labelFont.getName(); int style = Font.BOLD;

		int size = labelFont.getSize() + 5; // very large size

		retLabel.setFont(new Font(fontName, style, size));

		return retLabel;
	}

	private JLabel createVersionLabel()
	{
		String lS = OTHER_VERSION_LS_PROPERTY;

		String v = mVDH.getLanguageString(lS);

		String n = (String) additionalData[2];

		JLabel retLabel = new JLabel(v + " " + n, JLabel.CENTER);

		Font labelFont = UIManager.getFont("Label.font"); // l&f font style

		String fontName = labelFont.getName(); int style = labelFont.getStyle();

		int size = labelFont.getSize() + 2; // large size

		retLabel.setFont(new Font(fontName, style, size));

		return retLabel;
	}

	private JPanel createTextPanel()
	{
		JPanel retPanel = new JPanel(new BorderLayout());

		String textLS = (String) additionalData[3];

		String text = mVDH.getLanguageString(textLS);

		String ventureLS = OTHER_ABOUT_MEDVIEW_VENTURE_TEXT_LS_PROPERTY;

		text = text + mVDH.getLanguageString(ventureLS);

		JTextArea area = new JTextArea(text);

		area.setFont(UIManager.getFont("Label.font"));

		area.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

		area.setWrapStyleWord(true); area.setEditable(false);

		area.setLineWrap(true); area.setOpaque(false);

		ImageIcon i = mVDH.getImageIcon(ABOUT_MEDVIEW_IMAGE_ICON);

		area.setSize(new Dimension(i.getIconWidth(),1)); // forces height calculation

		retPanel.add(area, BorderLayout.CENTER);

		retPanel.setPreferredSize(area.getPreferredSize());

		return retPanel;
	}

	public String[] getButtonFacesLS()
	{
		return new String[] { BUTTON_CLOSE_LS_PROPERTY };
	}

	public String getTitleLS()
	{
		return ((String) additionalData[0]);
	}

	public void registerListeners()
	{
		buttons[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				AboutDialog.this.dispose();
			}
		});
	}
}
