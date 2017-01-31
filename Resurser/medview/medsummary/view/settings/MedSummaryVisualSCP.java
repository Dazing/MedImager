package medview.medsummary.view.settings;

import java.awt.*;

import javax.swing.*;

import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.medsummary.model.*;
import medview.medsummary.view.*;

import misc.domain.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

public class MedSummaryVisualSCP extends SettingsContentPanel implements
	MedViewLanguageConstants, GUIConstants, MedSummaryUserProperties
{
	// OVERRIDDEN METHODS

	public String getTabLS()
	{
		return TAB_MEDSUMMARY_VISUAL_LS_PROPERTY;
	}

	public String getTabDescLS()
	{
		return TAB_MEDSUMMARY_VISUAL_DESCRIPTION_LS_PROPERTY;
	}

	protected void settingsShown()
	{
		visualPanel.settingsShown();
	}

	protected void createComponents()
	{
		visualPanel = new LanguageAndLAFSettingsPanel(commandQueue, MedSummaryUserProperties.LOOK_AND_FEEL_PROPERTY,

			MedSummaryUserProperties.LANGUAGE_PROPERTY, MedSummaryUserProperties.class, MedSummaryUserProperties.class);
	}

	protected void layoutPanel()
	{
		GUIUtilities.gridBagAdd(this, visualPanel,		0, 0, 1, 1, 1, 0, CENT, new Insets(0,0,0,0), BOTH);

		GUIUtilities.gridBagAdd(this, Box.createGlue(),		0, 1, 1, 1, 0, 1, CENT, new Insets(0,0,0,0), BOTH);
	}


	// CONSTRUCTOR

	public MedSummaryVisualSCP(CommandQueue queue, MedSummaryFrame mediator)
	{
		super(queue, new Object[] { mediator });
	}

	private LanguageAndLAFSettingsPanel visualPanel;
}
