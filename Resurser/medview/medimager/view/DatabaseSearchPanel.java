/**
 * @(#) DatabaseSearchPanel.java
 */

package medview.medimager.view;

import java.awt.*;

import java.beans.*;

import javax.swing.*;

import medview.datahandling.*;

import medview.medimager.foundation.*;
import medview.medimager.model.*;

import misc.gui.constants.*;

public class DatabaseSearchPanel extends JPanel implements GUIConstants, MedImagerConstants
{
	// the main panels ('leaf' GUI components - invariant to usability model layer changes)

	private SearchPanel searchPanel;

	private BrowseTreePanel browseTreePanel;

	// various other members

	private MedImagerFrame frame;

	private MedImagerModel model;

	private UsabilityModel usabilityModel;


	// CONSTRUCTOR(S) AND RELATED METHODS

	public DatabaseSearchPanel(final MedImagerModel model, MedImagerFrame frame)
	{
		this.model = model;

		this.frame = frame;

		// set direct usability model reference

		this.usabilityModel = model.getUsabilityModel();

		// set up panel layout common to all usability layers

		setLayout(new BorderLayout(CCS, CCS));

		setBorder(BorderFactory.createEmptyBorder(CCS, CCS, CCS, CCS));

		// leaf component creation

		createGUILeafComponents();

		// layout panel

		layoutPanel();

		// attach usability model listener

		usabilityModel.addUsabilityModelListener(new UsabilityModelListener()
		{
			public void functionalLayerStateChanged(UsabilityModelEvent e)
			{
				if (e.getFunctionalLayer() == UsabilityModel.LAYER_STORE_AWAY)
				{
					refreshPanelLayout();
				}
			}
		});
	}

	private void createGUILeafComponents()
	{
		searchPanel = new SearchPanel(model, frame);

		browseTreePanel = new BrowseTreePanel(new NodeModel[] { model.getLibraryRoot() }, model, frame);
	}

	private void layoutPanel()
	{
		if (usabilityModel.isFunctionalLayerActive(UsabilityModel.LAYER_STORE_AWAY))
		{
			// set up split pane component

			JSplitPane splitPane = new JSplitPane();

			splitPane.setLeftComponent(searchPanel);

			splitPane.setRightComponent(browseTreePanel);

			splitPane.setContinuousLayout(true);

			splitPane.setOneTouchExpandable(false);

			splitPane.setResizeWeight(1.0);

			// last divider location

			int divLoc = -1;

			if ((divLoc = MedViewDataHandler.instance().getUserIntPreference(

				LAST_DATABASE_SEARCH_PANEL_DIVIDER_LOCATION_PROPERTY, divLoc, MedImagerConstants.class)) != -1)
			{
				splitPane.setDividerLocation(divLoc);
			}

			splitPane.addPropertyChangeListener(new PropertyChangeListener()
			{
				public void propertyChange(PropertyChangeEvent evt)
				{
					if (evt.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY))
					{
						MedViewDataHandler.instance().setUserIntPreference(LAST_DATABASE_SEARCH_PANEL_DIVIDER_LOCATION_PROPERTY,

							((Integer)evt.getNewValue()).intValue(), MedImagerConstants.class);
					}
				}
			});

			add(splitPane, BorderLayout.CENTER);
		}
		else
		{
			add(searchPanel, BorderLayout.CENTER);
		}
	}

	private void refreshPanelLayout()
	{
		removeAll();

		layoutPanel();
	}
}
