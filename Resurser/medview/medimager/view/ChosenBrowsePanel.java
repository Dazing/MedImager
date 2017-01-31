/**
 * @(#) ChosenBrowsePanel.java
 */

package medview.medimager.view;

import java.awt.*;

import java.beans.*;

import javax.swing.*;

import medview.datahandling.*;

import medview.medimager.foundation.*;
import medview.medimager.model.*;

import misc.gui.constants.*;

public class ChosenBrowsePanel extends JPanel implements GUIConstants, MedImagerConstants
{
	// the main panels ('leaf' GUI components - invariant to usability model layer changes)

	private BrowseTreePanel browseTreePanel = null;

	private NodeDetailPanel nodeDetailPanel = null;

	// various other members

	private MedImagerFrame frame;

	private MedImagerModel model;

	private UsabilityModel usabilityModel;


	// CONSTRUCTOR(S) AND RELATED METHODS

	public ChosenBrowsePanel(final MedImagerModel model, MedImagerFrame frame)
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
			}
		});
	}

	private void createGUILeafComponents()
	{
		NodeModel libraryRoot = model.getLibraryRoot();

		browseTreePanel = new BrowseTreePanel(new NodeModel[] { libraryRoot }, model, frame);

		nodeDetailPanel = new NodeDetailPanel(model, frame);

		// attach tree panel listener

		browseTreePanel.addBrowseTreeListener(new BrowseTreeListener()
		{
			public void nodeSelectionChanged(BrowseTreeEvent e)
			{
				nodeDetailPanel.displayNode(e.getLeadSelectedNode()); // might be null
			}
		});
	}

	private void layoutPanel()
	{
		// set up split pane component

		JSplitPane splitPane = new JSplitPane();

		splitPane.setLeftComponent(browseTreePanel);

		splitPane.setRightComponent(nodeDetailPanel);

		splitPane.setContinuousLayout(true);

		splitPane.setOneTouchExpandable(false);

		splitPane.setResizeWeight(0.0);

		// last divider location

		int divLoc = -1;

		if ((divLoc = MedViewDataHandler.instance().getUserIntPreference(

			LAST_CHOSEN_BROWSE_PANEL_DIVIDER_LOCATION_PROPERTY,

				divLoc, MedImagerConstants.class)) != -1)
		{
			splitPane.setDividerLocation(divLoc);
		}

		splitPane.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent e)
			{
				if (e.getPropertyName().equals(JSplitPane.DIVIDER_LOCATION_PROPERTY))
				{
					MedViewDataHandler.instance().setUserIntPreference(

						LAST_CHOSEN_BROWSE_PANEL_DIVIDER_LOCATION_PROPERTY,

							((Integer)e.getNewValue()).intValue(), MedImagerConstants.class);
				}
			}
		});

		// add split pane component

		add(splitPane, BorderLayout.CENTER);
	}
}
