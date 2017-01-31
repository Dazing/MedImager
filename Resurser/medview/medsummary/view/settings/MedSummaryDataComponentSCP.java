package medview.medsummary.view.settings;

import java.awt.*;

import java.util.*;

import medview.common.components.datapackage.*;
import medview.common.data.*;
import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.medsummary.model.*;
import medview.medsummary.view.*;

import misc.domain.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class MedSummaryDataComponentSCP extends SettingsContentPanel implements
	MedViewLanguageConstants, DataComponentPackageConstants, MedSummaryConstants
{
	// MEMBERS

	private DataComponentPackagePanel mainPanel;

	// CONSTRUCTOR

	public MedSummaryDataComponentSCP(CommandQueue queue, Frame owner)
	{
		super(queue, new Object[] { owner });
	}


	// TAB DESCRIPTIONS

	public String getTabDescLS()
	{
		return TAB_COMPONENT_PACKAGES_DESCRIPTION_LS_PROPERTY;
	}

	public String getTabLS()
	{
		return TAB_COMPONENT_PACKAGES_LS_PROPERTY;
	}


	// SETTINGS SHOWN

	protected void settingsShown()
	{
		synchronizePanel();
	}


	// INITIALIZATIONS

	protected void initSubMembers()
	{
	}

	protected void createComponents()
	{
	}

	protected void layoutPanel()
	{
		this.setLayout(new BorderLayout());

		synchronizePanel();
	}


	// UTILITY METHODS

	/**
	 * Utility method that makes sure that the lists in
	 * the main panel show what is currently set in the
	 * preferences.
	 */
	private void synchronizePanel()
	{
		// global packages

		DataComponentPackage[] globalPackages = DataComponentPackageUtilities.getGlobalPackages();

		// build included packages

		DataComponentPackage[] includedPackages = DataComponentPackageUtilities.obtainIncludedPackages(

			MedSummaryConstants.class);

		// construct (if not already) and set global packages and included packages in main panel

		if (mainPanel == null)
		{
			mainPanel = new DataComponentPackagePanel(globalPackages, includedPackages);

			mainPanel.addDataComponentPackagePanelListener(new Listener());

			add(mainPanel, BorderLayout.CENTER);
		}
		else
		{
			mainPanel.setGlobalPackages(globalPackages);

			mainPanel.setIncludedPackages(includedPackages);
		}
	}

	/**
	 * Utility method that makes sure that the preferences
	 * contain the same information as list shown to the user.
	 */
	private void synchronizePreferences(DataComponentPackage[] globalPackages, DataComponentPackage[] includedPackages)
	{
		// synchronize global

		DataComponentPackageUtilities.synchronizeGlobalPackages(globalPackages);

		// synchronize included

		DataComponentPackageUtilities.synchronizeIncludedPackages(includedPackages, MedSummaryConstants.class);
	}


	// INTERNAL LISTENER

	private class Listener implements DataComponentPackagePanelListener
	{
		public void includedPackageAdded(DataComponentPackagePanelEvent event) // does not affect global packages
		{
			commandQueue.addToQueue(new SyncCommand());
		}

		public void includedPackageRemoved(DataComponentPackagePanelEvent event) // does not affect global packages
		{
			commandQueue.addToQueue(new SyncCommand());
		}

		public void includedPackageNewDefault(DataComponentPackagePanelEvent event) // does not affect global packages
		{
		}

		public void globalPackageAdded(DataComponentPackagePanelEvent event)
		{
			commandQueue.addToQueue(new SyncCommand());
		}

		public void globalPackageRemoved(DataComponentPackagePanelEvent event)
		{
			commandQueue.addToQueue(new SyncCommand());
		}

		public void globalPackageEdited(DataComponentPackagePanelEvent event)
		{
			commandQueue.addToQueue(new SyncCommand());
		}

	}

	private class SyncCommand implements Command
	{
		public void execute()
		{
			synchronizePreferences(mainPanel.getGlobalPackages(), mainPanel.getIncludedPackages());
		}

		public boolean shouldReplace(Command command)
		{
			return (command instanceof SyncCommand);
		}

	}
}
