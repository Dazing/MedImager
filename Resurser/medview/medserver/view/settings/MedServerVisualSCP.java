package medview.medserver.view.settings;

import java.awt.*;

import javax.swing.*;

import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.medserver.model.*;
import medview.medserver.view.*;

import misc.domain.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

public class MedServerVisualSCP extends SettingsContentPanel
	implements MedViewLanguageConstants, GUIConstants
{
	public String getTabLS()
	{
		return TAB_MEDSERVER_VISUAL_LS_PROPERTY;
	}

	public String getTabDescLS()
	{
		return TAB_MEDSERVER_VISUAL_DESCRIPTION_LS_PROPERTY;
	}

	protected void initSubMembers()
	{
		mediator = (MedServerFrame) subConstructorData[0];
	}

	protected void settingsShown()
	{
		visualPanel.settingsShown();
	}

	protected void createComponents()
	{
		Class langPC = MedServerModelUserProperties.class;

		Class lafPC = MedServerModelUserProperties.class;

		String langP = MedServerModelUserProperties.LANGUAGE_PROPERTY;

		String lafP = MedServerModelUserProperties.LOOK_AND_FEEL_PROPERTY;

		visualPanel = new LanguageAndLAFSettingsPanel(commandQueue, lafP, langP, lafPC, langPC);
	}

	protected void layoutPanel()
	{
		GUIUtilities.gridBagAdd(this, visualPanel,		0, 0, 1, 1, 1, 0, CENT, new Insets(0,0,0,0), BOTH);

		GUIUtilities.gridBagAdd(this, Box.createGlue(),		0, 1, 1, 1, 0, 1, CENT, new Insets(0,0,0,0), BOTH);
	}

	public MedServerVisualSCP(CommandQueue queue, MedServerFrame mediator)
	{
		super(queue, new Object[] { mediator });
	}

	private LanguageAndLAFSettingsPanel visualPanel;

	private MedServerFrame mediator;
}
