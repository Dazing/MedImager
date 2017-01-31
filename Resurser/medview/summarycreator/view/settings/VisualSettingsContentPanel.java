package medview.summarycreator.view.settings;

import java.awt.*;

import javax.swing.*;

import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.summarycreator.model.*;
import medview.summarycreator.view.*;

import misc.domain.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

public class VisualSettingsContentPanel extends SettingsContentPanel
	implements MedViewLanguageConstants, GUIConstants
{

// **********************************************************************************************
// ------------------------- VARIOUS OVERRIDDEN AND IMPLEMENTED METHODS -------------------------
// **********************************************************************************************

	public String getTabLS()
	{
		return TAB_SUMMARYCREATOR_VISUAL_LS_PROPERTY;
	}

	public String getTabDescLS()
	{
		return TAB_SUMMARYCREATOR_VISUAL_DESCRIPTION_LS_PROPERTY;
	}

	protected void initSubMembers()
	{
		mediator = (SummaryCreatorFrame) subConstructorData[0];
	}

	protected void settingsShown()
	{
		visualPanel.settingsShown();
	}

	protected void createComponents()
	{
		Class langPC = SummaryCreatorUserProperties.class;

		Class lafPC = SummaryCreatorUserProperties.class;

		String langP = SummaryCreatorUserProperties.LANGUAGE_PROPERTY;

		String lafP = SummaryCreatorUserProperties.LOOK_AND_FEEL_PROPERTY;

		visualPanel = new LanguageAndLAFSettingsPanel(commandQueue, lafP, langP, lafPC, langPC);
	}

	protected void layoutPanel()
	{
		GUIUtilities.gridBagAdd(this, visualPanel,		0, 0, 1, 1, 1, 0, CENT, new Insets(0,0,0,0), BOTH);

		GUIUtilities.gridBagAdd(this, Box.createGlue(),		0, 1, 1, 1, 0, 1, CENT, new Insets(0,0,0,0), BOTH);
	}

// **********************************************************************************************
// ----------------------------------------------------------------------------------------------
// **********************************************************************************************





	public VisualSettingsContentPanel(CommandQueue queue, SummaryCreatorFrame mediator)
	{
		super(queue, new Object[] { mediator });
	}

	private LanguageAndLAFSettingsPanel visualPanel;

	private SummaryCreatorFrame mediator;





// **********************************************************************************************
// ---------------------------------------- INNER CLASSES ---------------------------------------
// **********************************************************************************************

// **********************************************************************************************
// ----------------------------------------------------------------------------------------------
// **********************************************************************************************

}
