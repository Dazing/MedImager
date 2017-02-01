//
//  TermEditVisualSCP.java
//
//  $Id: TermEditVisualSCP.java,v 1.2 2005/07/12 09:39:15 lindahlf Exp $.
//


package medview.termedit.view.settings;

import java.awt.*;

import javax.swing.*;

import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.termedit.model.*;
import medview.termedit.view.*;

import misc.domain.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

public class TermEditVisualSCP extends SettingsContentPanel
	implements MedViewLanguageConstants, GUIConstants {

	public String getTabLS() {
		return TAB_MEDSERVER_VISUAL_LS_PROPERTY;
	}

	public String getTabDescLS() {
		return TAB_MEDSERVER_VISUAL_DESCRIPTION_LS_PROPERTY;
	}

	protected void initSubMembers()
	{
		mediator = (TermEditFrame) subConstructorData[0];
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

	public TermEditVisualSCP(CommandQueue queue, TermEditFrame mediator)
	{
		super(queue, new Object[] { mediator });
	}

	private LanguageAndLAFSettingsPanel visualPanel;

	private TermEditFrame mediator;
}
