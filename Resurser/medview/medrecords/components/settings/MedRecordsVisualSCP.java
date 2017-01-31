//
//  MedRecordsVisualSCP.java
//  MedView
//
//  Created by Olof Torgersson on Thu Dec 04 2003.
//  $Id: MedRecordsVisualSCP.java,v 1.8 2005/06/03 15:47:13 lindahlf Exp $.
//

package medview.medrecords.components.settings;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import medview.common.actions.*;
import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.medrecords.*;
import medview.medrecords.data.*;

import misc.domain.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

public class MedRecordsVisualSCP extends SettingsContentPanel implements MedViewLanguageConstants, GUIConstants
{
	public String getTabLS()
	{
		return TAB_MEDRECORDS_VISUAL_LS_PROPERTY;
	}

	public String getTabDescLS()
	{
		return TAB_MEDRECORDS_VISUAL_DESCRIPTION_LS_PROPERTY;
	}

	protected void initSubMembers()
	{
	}

	protected void settingsShown()
	{
		visualPanel.settingsShown();
	}

	protected void createComponents()
	{
		visualPanel = new LanguageAndLAFSettingsPanel(commandQueue, PreferencesModel.LookAndFeel,

			PreferencesModel.SelectedLanguage, PreferencesModel.class, PreferencesModel.class);

		inputValueBGPanel = new InputValueBGSettingsPanel();
	}

	protected void layoutPanel()
	{
		GUIUtilities.gridBagAdd(this, visualPanel, 		0, 0, 1, 1, 1, 0, CENT, new Insets(0, 0, 0, 0), BOTH);

		GUIUtilities.gridBagAdd(this, inputValueBGPanel, 	0, 1, 1, 1, 0, 0, CENT, new Insets(0, 0, 0, 0), BOTH);

		GUIUtilities.gridBagAdd(this, Box.createGlue(),		0, 2, 1, 1, 0, 1, CENT, new Insets(0, 0, 0, 0), BOTH);
	}

	public MedRecordsVisualSCP(CommandQueue queue, Component parComp)
	{
		super(queue, new Object[0]);
	}

	private LanguageAndLAFSettingsPanel visualPanel;

	private InputValueBGSettingsPanel inputValueBGPanel;


	private class InputValueBGSettingsPanel extends JPanel
	{
		public InputValueBGSettingsPanel()
		{
			int cCS = GUIConstants.COMPONENT_COMPONENT_SPACING;

			setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

			setLayout(new BorderLayout(cCS, cCS));

			descLabel = new JLabel();

			exampleLabel = new JLabel();

			exampleLabel.setHorizontalAlignment(SwingConstants.CENTER);

			exampleLabel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));

			exampleLabel.setOpaque(true);

			setExampleColorAccordingToPreference();

			changeButton = new JButton(new ChangeColorAction());

			add(descLabel, BorderLayout.WEST);

			add(exampleLabel, BorderLayout.CENTER);

			add(changeButton, BorderLayout.EAST);
		}

		private void setExampleColorAccordingToPreference()
		{
			Class c = PreferencesModel.class;

			String key = PreferencesModel.InputListBackgroundColor;

			String colorString = mVDH.getUserStringPreference(key, "220,220,220", c);

			StringTokenizer t = new StringTokenizer(colorString, ",");

			int r = Integer.parseInt(t.nextToken());

			int g = Integer.parseInt(t.nextToken());

			int b = Integer.parseInt(t.nextToken());

			exampleLabel.setBackground(new Color(r, g, b));
		}

		private JLabel descLabel;

		private JLabel exampleLabel;

		private JButton changeButton;

		private class ChangeColorAction extends MedViewAction
		{
			public void actionPerformed(ActionEvent e)
			{
				MedViewDialogs mVD = MedViewDialogs.instance();

				Color c = mVD.createAndShowChooseColorDialog(new JFrame());

				if (c != null) // null -> cancelled
				{
					String r = c.getRed() + "";

					String g = c.getGreen() + "";

					String b = c.getBlue() + "";

					Class pC = PreferencesModel.class;

					String key = PreferencesModel.InputListBackgroundColor;

					String val = r + "," + g + "," + b;

					commandQueue.addToQueue(new InfoChangePropertyCommand(key, val, pC));

					exampleLabel.setBackground(c);
				}
			}

			public ChangeColorAction()
			{
				super(ACTION_CHOOSE_COLOR_LS_PROPERTY);
			}
		}

		private class InfoChangePropertyCommand extends ChangeUserStringPropertyCommand
		{
			public void execute()
			{
				super.execute();

				MedViewDialogs mVD = MedViewDialogs.instance();

				String lSProp = OTHER_SETTINGS_TAKE_EFFECT_NEXT_TIME_LS_PROPERTY;

				mVD.createAndShowInfoDialog(new JFrame(), mVDH.getLanguageString(lSProp));
			}

			public InfoChangePropertyCommand(String key, String val, Class c)
			{
				super(key, val, c);
			}
		}
	}
}
