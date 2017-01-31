package medview.summarycreator.view.settings;

import java.awt.*;

import javax.swing.*;

import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.summarycreator.view.*;

import misc.domain.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

public class DataLocationSettingsContentPanel extends SettingsContentPanel implements
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

	protected void initSubMembers()
	{
		mediator = (SummaryCreatorFrame) subConstructorData[0];

		listener = new Listener();
	}

	protected void settingsShown()
	{
		dataPanel.setLocalLocationText("används ej");

		dataPanel.setTermDefinitionLocationText(mediator.getModel().getTermDefinitionLocation());

		dataPanel.setTermValueLocationText(mediator.getModel().getTermValueLocation());

		dataPanel.setServerLocationText(mediator.getModel().getServerLocation());

		dataPanel.setUsesRemote(mediator.getModel().usesRemoteDataHandling());
	}

	protected void createComponents()
	{
		// the data locations panel

		dataPanel = new DataLocationSettingsPanel(commandQueue, mediator);

		dataPanel.addDataPanelListener(listener);
	}

	protected void layoutPanel()
	{
		// layout the panel

		GUIUtilities.gridBagAdd(this, dataPanel,		0, 0, 1, 1, 1, 0, CENT, new Insets(0,0,0,0), BOTH);

		GUIUtilities.gridBagAdd(this, Box.createGlue(),		0, 1, 1, 1, 0, 1, CENT, new Insets(0,0,0,0), BOTH);
	}

	public DataLocationSettingsContentPanel(CommandQueue queue, SummaryCreatorFrame mediator)
	{
		super(queue, new Object[] { mediator });
	}

	private Listener listener;

	private SummaryCreatorFrame mediator;

	private DataLocationSettingsPanel dataPanel;



	private class Listener implements DataLocationSettingsPanel.DataLocationSettingsListener
	{
		public void localLocationChangeRequested(DataLocationSettingsPanel.DataLocationSettingsEvent e)
		{
			// TODO: this option should not be displayed in SC
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
			// TODO: should not be here
		}
	}
}
