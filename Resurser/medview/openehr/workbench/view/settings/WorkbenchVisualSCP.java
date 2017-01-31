//
//  WorkbenchVisualSCP.java
//
//  $Id: WorkbenchVisualSCP.java,v 1.2 2008/12/28 21:57:57 oloft Exp $.
//


package medview.openehr.workbench.view.settings;

import java.awt.*;

import javax.swing.*;

import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.openehr.workbench.model.*;
import medview.openehr.workbench.view.*;

import misc.domain.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

public class WorkbenchVisualSCP extends SettingsContentPanel
	implements MedViewLanguageConstants, GUIConstants {

	public String getTabLS() {
		return TAB_OEHR_VISUAL_LS_PROPERTY;
	}

	public String getTabDescLS() {
		return TAB_OEHR_VISUAL_DESCRIPTION_LS_PROPERTY;
	}

	protected void initSubMembers()
	{
		mediator = (WorkbenchFrame) subConstructorData[0];
	}

	protected void settingsShown()
	{
		visualPanel.settingsShown();
	}

	protected void createComponents()
	{
		visualPanel = new LanguageAndLAFSettingsPanel(commandQueue, Preferences.LookAndFeel,

			Preferences.SelectedLanguage, Preferences.class, Preferences.class);
	}

	protected void layoutPanel()
	{
		GUIUtilities.gridBagAdd(this, visualPanel,		0, 0, 1, 0, 1, 1, CENT, new Insets(0,0,0,0), BOTH);

		GUIUtilities.gridBagAdd(this, Box.createGlue(),		0, 1, 0, 1, 1, 1, CENT, new Insets(0,0,0,0), BOTH);
	}

	public WorkbenchVisualSCP(CommandQueue queue, WorkbenchFrame mediator)
	{
		super(queue, new Object[] { mediator });
	}

	private LanguageAndLAFSettingsPanel visualPanel;

	private WorkbenchFrame mediator;
}
