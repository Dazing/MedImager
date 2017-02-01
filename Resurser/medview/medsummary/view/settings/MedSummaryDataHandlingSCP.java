package medview.medsummary.view.settings;

import java.awt.*;

import javax.swing.*;

import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.medsummary.view.*;

import misc.domain.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;
import medview.common.dialogs.settings.DataLocationSettingsPanel.DataLocationSettingsEvent;

public class MedSummaryDataHandlingSCP extends SettingsContentPanel implements
	MedViewLanguageConstants, GUIConstants
{
	public String getTabLS()
	{
		return TAB_DATA_LOCATIONS_LS_PROPERTY;
	}

	public String getTabDescLS()
	{
		return TAB_DATA_LOCATIONS_DESCRIPTION_LS_PROPERTY;
	}


	protected void settingsShown()
	{
		dataPanel.setLocalLocationText(mediator.getModel().getLocalExaminationDataLocation());

		dataPanel.setTermDefinitionLocationText(mediator.getModel().getLocalTermDefinitionLocation());

		dataPanel.setTermValueLocationText(mediator.getModel().getLocalTermValueLocation());

		dataPanel.setServerLocationText(mediator.getModel().getServerLocation());

		dataPanel.setUsesRemote(mediator.getModel().usesRemoteDataHandling());

		dataPanel.setPDALocationText(mediator.getModel().getLocalPDADataLocation());
	}

	protected void initSubMembers()
	{
		mediator = (MedSummaryFrame) subConstructorData[0];
	}

	protected void createComponents()
	{
		dataPanel = new DataLocationSettingsPanel(commandQueue, mediator);

		dataPanel.addDataPanelListener(new Listener());
	}

	protected void layoutPanel()
	{
		// layout the panel

		GUIUtilities.gridBagAdd(this, dataPanel,	0, 0, 1, 0, 1, 1, CENT, new Insets(0,0,0,0), BOTH);

		GUIUtilities.gridBagAdd(this, Box.createGlue(),	0, 1, 0, 1, 1, 1, CENT, new Insets(0,0,0,0), BOTH);
	}

	public MedSummaryDataHandlingSCP(CommandQueue queue, MedSummaryFrame mediator)
	{
		super(queue, new Object[] { mediator });
	}

	private MedSummaryFrame mediator;

	private DataLocationSettingsPanel dataPanel;



	private class Listener implements DataLocationSettingsPanel.DataLocationSettingsListener
	{
		public void localLocationChangeRequested(DataLocationSettingsEvent e)
		{
			mediator.getModel().setLocalExaminationDataLocation(e.getRequestedLocation());
		}

		public void serverLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		{
			mediator.getModel().setServerLocation(e.getRequestedLocation());
		}

		public void useRemoteDataHandlingChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		{
			mediator.getModel().setUseRemoteDataHandling(e.getRequestedUseFlag());
		}

		public void termDefinitionLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		{
			mediator.getModel().setLocalTermDefinitionLocation(e.getRequestedLocation());
		}

		public void termValueLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		{
			mediator.getModel().setLocalTermValueLocation(e.getRequestedLocation());
		}
		public void pdaDataLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		{
			mediator.getModel().setLocalPDADataLocation(e.getRequestedLocation());
		}
	}
}
