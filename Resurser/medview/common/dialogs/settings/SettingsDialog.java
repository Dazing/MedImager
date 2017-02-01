/*
 * @(#)SettingsDialog.java
 *
 * $Id: SettingsDialog.java,v 1.9 2006/04/24 14:16:56 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs.settings;

import java.awt.*;
import java.awt.event.*;

import java.beans.*;

import javax.swing.*;

import medview.common.dialogs.*;
import medview.datahandling.*;

import misc.domain.*;

public class SettingsDialog extends AbstractDialog implements MedViewLanguageConstants, MedViewDataUserProperties
{
	// MEDVIEWDIALOG IMPLEMENTED METHODS

	public Object getObjectData() // necessary
	{
		return null;
	}

	// ABSTRACTDIALOG IMPLEMENTED / OVERRIDDEN METHODS

	public void show()
	{
		notifyPanelsOfShow();

		super.show();
	}

	public JPanel createMainPanel()
	{
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		Dimension retPanelPrefSize = new Dimension(500,350);

		JPanel retPanel = new JPanel(new BorderLayout());

		retPanel.add(tabbedPane, BorderLayout.CENTER);

		retPanel.setPreferredSize(retPanelPrefSize);

		return retPanel;
	}

	public String[] getButtonFacesLS()
	{
		return new String[]
		{
			BUTTON_APPLY_LS_PROPERTY,

			BUTTON_CLOSE_LS_PROPERTY
		};
	}

	public String getTitleLS()
	{
		return TITLE_SETTINGS_LS_PROPERTY;
	}

	public void registerListeners()
	{
		buttons[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				commandQueue.executeQueue();
			}
		});

		buttons[1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SettingsDialog.this.hide();

				commandQueue.clearQueue();

				notifyPanelsOfHide();
			}
		});

		commandQueue.addCommandQueueListener(new CommandQueueListener()
		{
			public void queueCleared(CommandQueueEvent e)
			{
				buttons[0].setEnabled(false);
			}

			public void commandAdded(CommandQueueEvent e)
			{
				if (!buttons[0].isEnabled())
				{
					buttons[0].setEnabled(true);
				}
			}
		});
	}

	protected int getDefaultButtonIndex()
	{
		return 1;
	}

	protected Dimension getButtonDimension()
	{
		return new Dimension(120,25);
	}

	// SETTINGSDIALOG SPECIFIC METHODS

	protected void notifyPanelsOfShow()
	{
		int tabs = tabbedPane.getTabCount();

		for (int ctr=0; ctr<tabs; ctr++)
		{
			((SettingsContentPanel)tabbedPane.getComponentAt(ctr)).settingsShown();
		}
	}

	protected void notifyPanelsOfHide()
	{
		int tabs = tabbedPane.getTabCount();

		for (int ctr=0; ctr<tabs; ctr++)
		{
			((SettingsContentPanel)tabbedPane.getComponentAt(ctr)).settingsHidden();
		}
	}

	public void addSettingsPanel(SettingsContentPanel panel)
	{
		String trans = mVDH.getLanguageString(panel.getTabLS());

		String desc = mVDH.getLanguageString(panel.getTabDescLS());

		tabbedPane.addTab(trans, null, panel, desc);
	}

	public void removeAllSettingsPanels()
	{
		tabbedPane.removeAll();
	}

	public void selectTabLS(String tabLS)
	{
		int indexOfTab = tabbedPane.indexOfTab(mVDH.getLanguageString(tabLS));

		if (indexOfTab != -1)
		{
			tabbedPane.setSelectedIndex(indexOfTab);
		}
	}

	// CONSTRUCTOR

	protected void initSubClassMembers() // called from superclass
	{
		commandQueue = (CommandQueue) additionalData[0]; // see registerListeners()
	}

	public SettingsDialog(Frame owner, CommandQueue commandQueue)
	{
		super(owner, true, new Object[] { commandQueue });

		buttons[0].setEnabled(false);
	}

	protected CommandQueue commandQueue;

	private JTabbedPane tabbedPane;
}
