//
//  MedRecordsDataHandlingSCP.java
//  MedView
//
//  Created by Olof Torgersson on Fri Dec 05 2003.
//  $Id: MedRecordsDataHandlingSCP.java,v 1.8 2006/05/29 18:32:49 limpan Exp $.
//

package medview.medrecords.components.settings;

import java.awt.*;

import javax.swing.*;

import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.medrecords.*;
import medview.medrecords.components.*;
import medview.medrecords.data.*;

import misc.domain.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;
import medview.common.dialogs.settings.DataLocationSettingsPanel.DataLocationSettingsEvent;

public class MedRecordsDataHandlingSCP extends SettingsContentPanel implements MedViewLanguageConstants, GUIConstants
{
	private Frame owner;

	private PreferencesModel prefs;

	private DataLocationSettingsPanel dataPanel;

	public MedRecordsDataHandlingSCP(CommandQueue queue, Frame owner)
	{
		super(queue, new Object[0]);

		this.owner = owner;

		prefs = PreferencesModel.instance();
	}

	public String getTabLS()
	{
		return TAB_DATA_LOCATIONS_LS_PROPERTY;
	}

	public String getTabDescLS()
	{
		return TAB_DATA_LOCATIONS_DESCRIPTION_LS_PROPERTY;
	}

	protected void initSubMembers()
	{
	}

	protected void settingsShown()
	{
		dataPanel.setLocalLocationText(prefs.getLocalDatabaseLocation());

		dataPanel.setTermDefinitionLocationText(prefs.getTermDefinitionLocation());

		dataPanel.setTermValueLocationText(prefs.getTermValueLocation());

		dataPanel.setServerLocationText(prefs.getServerLocation());

		dataPanel.setUsesRemote(prefs.usesRemoteDataHandling());

		dataPanel.setPDALocationText(prefs.getPDADatabaseLocation());
	}

	protected void createComponents()
	{
		// the data locations panel

		dataPanel = new DataLocationSettingsPanel(commandQueue, owner);

		dataPanel.addDataPanelListener(new Listener());
	}

	protected void layoutPanel()
	{
		GUIUtilities.gridBagAdd(this, dataPanel, 	0, 0, 1, 1, 1, 0, CENT, new Insets(0, 0, 0, 0), BOTH);

		GUIUtilities.gridBagAdd(this, Box.createGlue(), 0, 1, 1, 1, 0, 1, CENT, new Insets(0, 0, 0, 0), BOTH);
	}

	private class Listener implements DataLocationSettingsPanel.DataLocationSettingsListener
	{
		public void localLocationChangeRequested(DataLocationSettingsEvent e)
		{
			prefs.setLocalDatabaseLocation(e.getRequestedLocation());
		}

		public void serverLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		{
			prefs.setServerLocation(e.getRequestedLocation());
		}

		public void useRemoteDataHandlingChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		{
			prefs.setUseRemoteDataHandling(e.getRequestedUseFlag());
		}

		public void termDefinitionLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		{
			prefs.setTermDefinitionLocation(e.getRequestedLocation());
		}

		public void termValueLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		{
			prefs.setTermValueLocation(e.getRequestedLocation());
		}

		public void pdaDataLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		{
			prefs.setPDADatabaseLocation(e.getRequestedLocation());
		}
	}
}
