/*
 * @(#)MedSummaryFrame.java
 *
 * $Id: MedSummaryFrame.java,v 1.19 2008/07/29 09:31:59 it2aran Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.view;

import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;

import medview.medsummary.model.*;
import medview.medsummary.model.exceptions.CouldNotAddPatientException;
import medview.medsummary.view.settings.*;

import misc.domain.*;

import misc.foundation.*;

import misc.gui.actions.*;
import misc.gui.components.*;
import misc.gui.utilities.*;

import java.awt.*;
import java.awt.event.*;

import java.beans.*;

import java.util.*;

import javax.swing.*;

public class MedSummaryFrame extends MainShell implements
	MedViewLanguageConstants, MutableActionContainer, MedSummaryUserProperties,
	MedSummaryFlagProperties
{

	// MAINSHELL

	protected int getMSWidth()
	{
		return 1024;
	}

	protected int getMSHeight()
	{
		return 768;
	}

	protected boolean usesSplash()
	{
		return true;
	}

	protected boolean usesDotThread()
	{
		return true;
	}

	protected JToolBar[] getToolBars()
	{
		return toolbarHandler.getToolbars();
	}

	protected Image getSplashImage()
	{
		String prop = MedViewMediaConstants.SPLASH_MEDSUMMARY_IMAGE_ICON;

		return MedViewDataHandler.instance().getImage(prop);
	}

	protected String getDeveloperSplashText()
	{
		String version = MedSummaryConstants.VERSION_STRING;

		String lS = OTHER_ABOUT_MEDSUMMARY_TEXT_LS_PROPERTY;

		String a = "MedSummary " + version + "\n";

		return a + mVDH.getLanguageString(lS);
	}

	protected String getMSTitle()
	{
		//String post = " - MedSummary " + MedSummaryConstants.VERSION_STRING;
		String post = " - MedSummary";

		if (model == null)	// during initialization of the application
		{
			return ((MedSummaryModel)subClassData[0]).getDataLocationID() + post;
		}
		else
		{
			return model.getDataLocationID() + post;
		}
	}

	protected void afterShow()
	{
		super.afterShow();

		String prop = MEDSUMMARY_MAXIMIZED_PROPERTY;

		Class c = MedSummaryFlagProperties.class;

		if (mVDH.getUserBooleanPreference(prop, false, c))
		{
			this.setExtendedState(Frame.MAXIMIZED_BOTH);	// maximize
		}

		this.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				String prop = MEDSUMMARY_MAXIMIZED_PROPERTY;

				Class c = MedSummaryFlagProperties.class;

				boolean isMaximized = (getExtendedState() == Frame.MAXIMIZED_BOTH);

				mVDH.setUserBooleanPreference(prop, isMaximized, c);

				if (!isMaximized)
				{
					Dimension currSize = MedSummaryFrame.this.getSize();

					int height = currSize.height; int width = currSize.width;

					prop = MEDSUMMARY_SIZE_PROPERTY;

					c = MedSummaryUserProperties.class;

					mVDH.setUserStringPreference(prop, width + "x" + height, c);
				}
			}

			public void componentMoved(ComponentEvent e)
			{
				String prop = MEDSUMMARY_LOCATION_PROPERTY;

				Class c = MedSummaryUserProperties.class;

				Point location = MedSummaryFrame.this.getLocation();

				int x = location.x; int y = location.y;

				mVDH.setUserStringPreference(prop, x + "," + y, c);
			}
		});

		c = MedSummaryUserProperties.class;

		prop = DIVIDER_LOCATION_PROPERTY;

		int divLoc = mVDH.getUserIntPreference(prop, -1, c);

		if (divLoc != -1)
		{
			entirePane.setDividerLocation(divLoc);
		}

		patientPanel.requestFocus();
	}

	protected void onClose() // called when shutting down window (X)
	{
		tryShutDown();
	}

	protected void subclassInit() // since overridden methods use
	{
		mVDH = MedViewDataHandler.instance();
	}


	// APPLICATION SHUT-DOWN

	public void tryShutDown()
	{
		String lS = LABEL_EXIT_MEDSUMMARY_LS_PROPERTY;

		String message = mVDH.getLanguageString(lS);

		int type = MedViewDialogConstants.YES_NO;

		int choice = mVD.createAndShowQuestionDialog(this, type, message);

		if (choice == MedViewDialogConstants.YES)
		{
			if (feederAdapter.allowsShutDown())
			{
				mVDH.shuttingDown(); // notify data layer of shut-down

				System.exit(0);
			}
		}
	}

	// Graph Adapter
	
	public GraphAdapter getGraphAdapter()
	{
		return graphAdapter;
	}

	public void initiateGraph(final ExaminationIdentifier id) 
	{
		graphAdapter.initiateGraph(id, this);
	}
	
	// FEEDER ADAPTER

	public FeederAdapter getFeederAdapter()
	{
		return feederAdapter;
	}

	public void initiateFeeder()
	{
		feederAdapter.initiateFeeder(this);
	}

	/**
	 * Initiates the feeder application with the specified
	 * pid pre-filled. If no examination with the specified
	 * pid previously was contained in the tree model, will
	 * add the patient to the tree model according to the
	 * addPatient() method after an examination with the
	 * pid has been stored. This can be the case, for
	 * instance, when the application is invoked with flags
	 * and patient identifier information from outside.
	 * @param pid PatientIdentifier
	 */
	public void initiateFeeder(final PatientIdentifier pid)
	{
		feederAdapter.initiateFeeder(pid, this);

		if (!treeModel.contains(pid))
		{
			model.addMedSummaryModelListener(new MedSummaryModelAdapter()
			{
				public void examinationAdded(MedSummaryModelEvent e)
				{
					if (e.getExaminationIdentifier().getPID().equals(pid))
					{
						addPatient(pid); // adds to tree model

						model.removeMedSummaryModelListener(this);
					}
				}
			});
		}
	}


	// PATIENT ADDITION TO TREE

	/**
	 * Adds the specified patient to the tree model, which in
	 * turn will notify the tree GUI of this, which will update
	 * it's appearance.
	 * @param pid PatientIdentifier
	 */
	public void addPatient(final PatientIdentifier pid)
	{
		try
		{
			treeModel.addPatient(pid);
		}
		catch (final CouldNotAddPatientException e)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					mVD.createAndShowErrorDialog(getParentFrame(), e.getMessage());
				}
			});
		}
	}


	// SETTINGS DIALOG

	public void showSettingsDialog()
	{
		MedViewDialogs.instance().createAndShowSettingsDialog(getParentFrame(),

			TITLE_MEDSUMMARY_SETTINGS_LS_PROPERTY, settingsContentPanels);
	}


	// GRAPHIC TEMPLATE

	public void setGraphicTemplateToUse(String t)
	{
		try
		{
			summaryPanel.setGraphicTemplateToUse(t);
		}
		catch (InvalidGraphicTemplateException e)
		{
			e.printStackTrace();

			System.err.println(e.getMessage());
		}
	}

	public String getGraphicTemplateInUse()
	{
		return summaryPanel.getGraphicTemplateInUse();
	}

	public String[] getAvailableGraphicTemplates()
	{
		return summaryPanel.getAvailableGraphicTemplates();
	}


	// MODEL

	public MedSummaryModel getModel()
	{
		return model;
	}


	// ACTIONS

	public Action getAction(String actionID)
	{
		return (Action) actions.get(actionID);
	}

	public void registerAction(String actionID, Action action)
	{
		actions.put(actionID, action);
	}


	// IMAGES

	public void displayImages(ExaminationModel[] models)
	{
		imagePanel.displayImages(models);
	}

	public void clearImageDisplays()
	{
		imagePanel.clearImages();
	}


	// UTILITY METHODS

	public Component getParentComponent()
	{
		return (Component) this;
	}

	public Window getParentWindow()
	{
		return (Window) this;
	}

	public Frame getParentFrame()
	{
		return (Frame) this;
	}


	// METHODS CALLED FROM CONSTRUCTOR

	private void createTreePanel()
	{
		treePanel = new MedSummaryTreePanel(this);

		treePanel.setBorder(BorderFactory.createEmptyBorder(0,5,5,5));

		treePanel.setPreferredSize(treePanel.getMinimumSize());

		treePanel.setOpaque(false);
	}

	private void createPatientPanel()
	{
		String lS = OTHER_OBTAINING_PATIENTS_LS_PROPERTY;

		setLoadingSplashText(mVDH.getLanguageString(lS));

		ProgressNotifiable pN = new DefaultProgressNotifiable()
		{
			public void setCurrent(int c)
			{
				super.setCurrent(c);

				if (!((c % 10) == 0)) { return; }

				String desc = getDescription();

				int t = getTotal();

				setLoadingSplashText(desc + " (" + c + " / " + t + ")");
			}
		};

		patientPanel = new MedSummaryPatientPanel(this, pN);

		patientPanel.setBorder(BorderFactory.createEmptyBorder(0,5,5,5));

		patientPanel.setOpaque(false);
	}

	private void createImagePanel()
	{
		imagePanel = new MedSummaryImagePanel(this);

		imagePanel.setBorder(BorderFactory.createLoweredBevelBorder());

		imagePanel.setOpaque(true);
	}

	private void createSummaryPanel()
	{
		summaryPanel = new MedSummarySummaryPanel(this);

		summaryPanel.setBorder(BorderFactory.createEmptyBorder(0,0,5,5));

		summaryPanel.setMinimumSize(new Dimension(300,400));

		summaryPanel.setOpaque(false);
	}

	private void createNorthEastPane()
	{
		northEastPane = new JSplitPane();

		northEastPane.setOpaque(false);

		northEastPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

		northEastPane.setContinuousLayout(true);

		northEastPane.setOneTouchExpandable(false);

		northEastPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

		northEastPane.setTopComponent(treePanel);

		northEastPane.setBottomComponent(summaryPanel);

		GUIUtilities.retouchSplitPane(northEastPane);
	}

	private void createNorthPane()
	{
		northPane = new JSplitPane();

		northPane.setOpaque(false);

		northPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

		northPane.setContinuousLayout(true);

		northPane.setOneTouchExpandable(true);

		northPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

		northPane.setTopComponent(patientPanel);

		northPane.setBottomComponent(northEastPane);

		GUIUtilities.retouchSplitPane(northPane);
	}

	private void createEntirePane()
	{
		entirePane = new JSplitPane();

		entirePane.setOpaque(false);

		entirePane.setOrientation(JSplitPane.VERTICAL_SPLIT);

		entirePane.setContinuousLayout(true);

		entirePane.setOneTouchExpandable(true);

		entirePane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

		entirePane.setResizeWeight(1);

		entirePane.setTopComponent(northPane);

		entirePane.setBottomComponent(imagePanel);

		GUIUtilities.retouchSplitPane(entirePane);

		entirePane.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent e)
			{
				String divProp = JSplitPane.DIVIDER_LOCATION_PROPERTY;

				if (e.getPropertyName().equalsIgnoreCase(divProp))
				{
					Class c = MedSummaryUserProperties.class;

					String prop = DIVIDER_LOCATION_PROPERTY;

					int divLoc = entirePane.getDividerLocation();

					mVDH.setUserIntPreference(prop, divLoc, c);
				}
			}
		});
	}

	private void initSettings()
	{
		CommandQueue queue = mVD.getSettingsCommandQueue();

		settingsContentPanels = new SettingsContentPanel[]
		{
			new MedSummaryVisualSCP(queue, this),

			new MedSummaryJournalSCP(queue, this),

			new MedSummaryPictureTextSCP(queue, this),

			new MedSummaryDataHandlingSCP(queue, this),

			new MedSummaryExpertSCP(queue, this),

			new MedSummaryDataComponentSCP(queue, this)
		};
	}

	// CONSTRUCTOR

	public MedSummaryFrame(MedSummaryModel model)
	{
		super(new Object[] { model });

		this.model = model;

		treeModel = model.getTreeModel();

		mVD = MedViewDialogs.instance();

		actions = new HashMap();

		// panels and panes

		String lS = OTHER_INITIALIZING_COMPONENTS_LS_PROPERTY;

		setLoadingSplashText(mVDH.getLanguageString(lS));

		createTreePanel(); // registers actions

		createPatientPanel(); // registers actions

		createImagePanel(); // registers actions

		createSummaryPanel(); // registers actions

		createNorthEastPane(); // layout only

		createNorthPane(); // layout only

		createEntirePane(); // layout only

		setCenterComponent(entirePane);

		// toolbars and menus

		lS = OTHER_INITIALIZING_TOOLBARS_AND_MENUS_LS_PROPERTY;

		setLoadingSplashText(mVDH.getLanguageString(lS));

		toolbarHandler = new MedSummaryToolBarHandler(this);

		menuHandler = new MedSummaryMenuHandler(this);

		JMenu[] menus = menuHandler.getMenus();

		for (int ctr=0; ctr<menus.length; ctr++)
		{
			addToMenuBar(menus[ctr]);
		}

		// initial frame size and location

		String prop = MEDSUMMARY_SIZE_PROPERTY;

		Class c = MedSummaryUserProperties.class;

		String set = mVDH.getUserStringPreference(prop, "1024x768", c);

		StringTokenizer t = new StringTokenizer(set, "x");

		try
		{
			int width = Integer.parseInt(t.nextToken());

			int height = Integer.parseInt(t.nextToken());

			this.setSize(new Dimension(width, height));
		}
		catch (Exception e)
		{
			e.printStackTrace();	// should never happen
		}

		prop = MEDSUMMARY_LOCATION_PROPERTY;

		set = mVDH.getUserStringPreference(prop, null, c);

		if (set != null)	// if it is null, leave it up to the mainshell code for centering frame
		{
			t = new StringTokenizer(set, ",");

			int x = Integer.parseInt(t.nextToken());

			int y = Integer.parseInt(t.nextToken());

			this.setLocation(new Point(x,y));
		}

		// frame icon

		setIconImage(mVDH.getImage(MedViewMediaConstants.FRAME_IMAGE_ICON));

		// settings

		initSettings();

		// add listeners

		model.addMedSummaryModelListener(new ModelListener());

		// feeder adapter

		feederAdapter = new MedRecordsFeederAdapter(model);
		
		// graph adapter
		
		graphAdapter = new MedRecordsGraphAdapter(model);
	}

	// MEMBERS

	private FeederAdapter feederAdapter;

	private GraphAdapter graphAdapter;

	private TreeModel treeModel;

	private MedSummaryModel model;

	private MedViewDataHandler mVDH;

	private MedViewDialogs mVD;


	private MedSummaryTreePanel treePanel;

	private MedSummaryImagePanel imagePanel;

	private MedSummarySummaryPanel summaryPanel;

	private MedSummaryPatientPanel patientPanel;


	private SettingsContentPanel[] settingsContentPanels;


	private MedSummaryToolBarHandler toolbarHandler;

	private MedSummaryMenuHandler menuHandler;


	private JSplitPane northEastPane;

	private JSplitPane entirePane;

	private JSplitPane northPane;


	private HashMap actions;


	// INTERNAL MODEL LISTENER

	private class ModelListener implements MedSummaryModelListener
	{
		public void dataLocationIDChanged(MedSummaryModelEvent e)
		{
			MedSummaryFrame.this.setTitle(getMSTitle());
		}

		public void dataLocationChanged(MedSummaryModelEvent e)
		{
		}

		public void patientsChanged(MedSummaryModelEvent e)
		{
		}

		public void examinationAdded(MedSummaryModelEvent e)
		{
		}

		public void examinationUpdated(MedSummaryModelEvent e)
		{
		}

		public void sectionsChanged(MedSummaryModelEvent e)
		{
		}

		public void templateChanged(MedSummaryModelEvent e)
		{
		}

		public void templateIDChanged(MedSummaryModelEvent e)
		{
		}

		public void translatorChanged(MedSummaryModelEvent e)
		{
		}

		public void translatorIDChanged(MedSummaryModelEvent e)
		{
		}

		public void documentReplaced(MedSummaryModelEvent e)
		{
		}

		public void currentPackageChanged(MedSummaryModelEvent e)
		{
		}

		public void includedPackagesUpdated(MedSummaryModelEvent e)
		{
		}
	}

}
