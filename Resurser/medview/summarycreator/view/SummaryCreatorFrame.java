/*
 * @(#)SummaryCreator.java
 *
 * $Id: SummaryCreatorFrame.java,v 1.10 2007/04/08 15:57:49 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import java.awt.*;
import java.awt.event.*;

import java.util.*;
import java.io.File;

import javax.swing.*;
import javax.swing.text.*;

import medview.common.actions.*;
import medview.common.dialogs.*;

import medview.datahandling.*;

import medview.summarycreator.model.*;

import misc.gui.components.*;
import misc.gui.utilities.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.template.*;
import se.chalmers.cs.medview.docgen.translator.*;

public class SummaryCreatorFrame extends MainShell implements
	MedViewMediaConstants, MedViewLanguageConstants, TranslatorViewMediator,
	SummaryCreatorUserProperties, SummaryCreatorFlagProperties
{

// ------------------------------------------------------------------------
// ********************* MAINSHELL OVERRIDDEN METHODS *********************
// ------------------------------------------------------------------------

	protected int getMSWidth()
	{
		return 1024;
	}

	protected int getMSHeight()
	{
		return 768;
	}

	protected boolean usesDotThread()
	{
		return true;
	}

	protected int getDotRate()
	{
		return 500;
	}

	protected JToolBar[] getToolBars()
	{
		return toolbarHandler.getToolbars();
	}

	protected boolean usesSplash()
	{
		return true;
	}

	protected Image getSplashImage()
	{
		String prop = MedViewMediaConstants.SPLASH_SUMMARYCREATOR_IMAGE_ICON;

		return MedViewDataHandler.instance().getImage(prop);
	}

	protected String getDeveloperSplashText()
	{
		String version = SummaryCreatorConstants.VERSION_STRING;

		String lS = OTHER_ABOUT_SUMMARYCREATOR_TEXT_LS_PROPERTY;

		String a = "SummaryCreator " + version + "\n";

		return a + mVDH.getLanguageString(lS);
	}

	protected void onClose()
	{
		if (allowsExit())
		{
			mVDH.shuttingDown();

			System.exit(0);
		}
	}

	protected String getMSTitle()
	{
		return "SummaryCreator";
	}

	protected void subclassInit()
	{
		mVDH = MedViewDataHandler.instance(); // used in factory method imps
	}

	protected void afterShow()
	{
		String prop = SUMMARYCREATOR_MAXIMIZED_PROPERTY;

		Class c = SummaryCreatorFlagProperties.class;

		if (mVDH.getUserBooleanPreference(prop, false, c))
		{
			this.setExtendedState(Frame.MAXIMIZED_BOTH);	// maximize
		}

		this.addComponentListener(new ComponentAdapter()
		{
			public void componentResized(ComponentEvent e)
			{
				String prop = SUMMARYCREATOR_MAXIMIZED_PROPERTY;

				Class c = SummaryCreatorFlagProperties.class;

				boolean isMaximized = (getExtendedState() == Frame.MAXIMIZED_BOTH);

				mVDH.setUserBooleanPreference(prop, isMaximized, c);

				if (!isMaximized)
				{
					Dimension currSize = SummaryCreatorFrame.this.getSize();

					int height = currSize.height; int width = currSize.width;

					prop = SUMMARYCREATOR_SIZE_PROPERTY;

					c = SummaryCreatorUserProperties.class;

					mVDH.setUserStringPreference(prop, width + "x" + height, c);
				}
			}

			public void componentMoved(ComponentEvent e)
			{
				String prop = SUMMARYCREATOR_LOCATION_PROPERTY;

				Class c = SummaryCreatorUserProperties.class;

				Point location = SummaryCreatorFrame.this.getLocation();

				int x = location.x; int y = location.y;

				mVDH.setUserStringPreference(prop, x + "," + y, c);
			}
		});
	}

// ------------------------------------------------------------------------
// ************************************************************************
// ------------------------------------------------------------------------



// ------------------------------------------------------------------------
// *************************** MEDIATOR METHODS ***************************
// ------------------------------------------------------------------------

	public Action getAction(String actionName)
	{
		return (Action) actions.get(actionName);
	}

	public void registerAction(String actionName, Action action)
	{
		actions.put(actionName, action);
	}

	public SummaryCreatorModel getModel()
	{
		return model;
	}

	public TranslatorModelKeeper getTranslatorModelKeeper()
	{
		return model;
	}

	public Frame getParentFrame()
	{
		return this;
	}

	public void requestUseTranslator(String location)
	{
		translatorView.requestUseTranslator(location);
	}

	public void templateLocationChanged(String name)
	{
		String displayName = name;
		
		try {
			File f = new File(name);
			displayName = f.getName();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		this.setTitle(displayName + " - " + "SummaryCreator");
	}

	public void termChosenInTemplate(String term)
	{
		termListView.selectTerm(term);
	}

	public boolean allowsExit()
	{
		if (!templateViewWrapper.allowsExit())
		{
			return false;
		}

		if (!translatorView.allowsExit())
		{
			return false;
		}

		return true;
	}

	public void displayTerm(String term)
	{
		translatorView.setCurrentTerm(term);
	}

	public void addTerm(String term)
	{
		templateViewWrapper.addTerm(term);
	}

// ------------------------------------------------------------------------
// ************************************************************************
// ------------------------------------------------------------------------





	public SummaryCreatorFrame( SummaryCreatorModel model )
	{
		super(); // mainshell constructor called here


		// initial member initializations (mVDH initialized above due to Factory Methods)

		this.model = model;

		actions = new HashMap();

		String lS = OTHER_INITIALIZING_COMPONENTS_LS_PROPERTY;

		setLoadingSplashText(mVDH.getLanguageString(lS));

		modelListener = new ModelListener();

		previewAction = new PreviewAction();

		associateAction = new AssociateTranslatorAction();

		templateViewWrapper = new TemplateViewWrapper(this);

		translatorView = new TranslatorView(this);

		lS = OTHER_INITIALIZING_LIST_OF_TERMS_LS_PROPERTY;

		setLoadingSplashText(mVDH.getLanguageString(lS));

		termListView = new TermListView(this);


		// register kept actions

		registerAction(SummaryCreatorActions.PREVIEW_JOURNAL_ACTION, previewAction);

		registerAction(SummaryCreatorActions.ASSOCIATE_TRANSLATOR_ACTION, associateAction);


		// toolbars and menus (after actions)

		menuHandler = new SummaryCreatorMenuHandler(this);

		toolbarHandler = new SummaryCreatorToolbarHandler(this);


		// attach various listeners

		mVDH.addMedViewPropertyListener(new PropertyListener());

		model.addTemplateModelKeeperListener(modelListener);

		model.addTranslatorModelKeeperListener(modelListener);

		model.addSummaryCreatorModelListener(modelListener);

		modelListener.checkPreviewAndLinkageAction();


		// add menus and toolbars

		JMenu[] menus = menuHandler.getMenus();

		for (int ctr=0; ctr<menus.length; ctr++)
		{
			addToMenuBar(menus[ctr]);
		}


		// layout shell

		JPanel termPanel = new JPanel(new BorderLayout());

		termPanel.add(termListView, BorderLayout.CENTER);


		JSplitPane leftPane = new JSplitPane();

		leftPane.setOneTouchExpandable(false);

		leftPane.setContinuousLayout(true);

		leftPane.setResizeWeight(1);

		leftPane.setRightComponent(termPanel);

		leftPane.setLeftComponent(templateViewWrapper.getPaneContainer());

		GUIUtilities.retouchSplitPane(leftPane);


		JSplitPane mainSplitPane = new JSplitPane();

		mainSplitPane.setOneTouchExpandable(true);

		mainSplitPane.setContinuousLayout(true);

		mainSplitPane.setResizeWeight(1);

		mainSplitPane.setRightComponent(translatorView);

		mainSplitPane.setLeftComponent(leftPane);

		GUIUtilities.retouchSplitPane(mainSplitPane);


		JPanel mainSPContainer = new JPanel(new BorderLayout());

		mainSPContainer.add(mainSplitPane, BorderLayout.CENTER);

		setCenterComponent(mainSPContainer);


		// initial frame size and location

		String prop = SUMMARYCREATOR_SIZE_PROPERTY;

		Class c = SummaryCreatorUserProperties.class;

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

		prop = SUMMARYCREATOR_LOCATION_PROPERTY;

		set = mVDH.getUserStringPreference(prop, null, c);

		if (set != null)	// if it is null, leave it up to the mainshell code for centering frame
		{
			t = new StringTokenizer(set, ",");

			int x = Integer.parseInt(t.nextToken());

			int y = Integer.parseInt(t.nextToken());

			this.setLocation(new Point(x,y));
		}


		// initial term locations validity check

		lS = OTHER_CHECKING_TERM_VALIDITY_LS_PROPERTY;

		setLoadingSplashText(mVDH.getLanguageString(lS));

		if (!model.areTermLocationsValid())
		{
			termListView.setEnabled(false);

			translatorView.setEnabled(false);

			templateViewWrapper.setEnabled(false);
		}
	}

	private MedViewDataHandler mVDH;

	private SummaryCreatorModel model;

	private TermListView termListView;

	private TranslatorView translatorView;

	private TemplateViewWrapper templateViewWrapper;

	private SummaryCreatorMenuHandler menuHandler;

	private SummaryCreatorToolbarHandler toolbarHandler;

	private ModelListener modelListener;

	private Action associateAction;

	private Action previewAction;

	private HashMap actions;








// ---------------------------------------------------------------
// *************************** ACTIONS ***************************
// ---------------------------------------------------------------

	private class PreviewAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			MedViewDialogs mVD = MedViewDialogs.instance();

			try
			{
				StyledDocument document = model.generatePreviewDocument();

				String title = mVDH.getLanguageString(TITLE_PREVIEW_TEMPLATE_LS_PROPERTY);

				mVD.createAndShowTextDialog(SummaryCreatorFrame.this, document, title);
			}
			catch (CouldNotGenerateException exc)
			{
				mVD.createAndShowErrorDialog(SummaryCreatorFrame.this, exc.getMessage());
			}
		}

		public PreviewAction()
		{
			super(ACTION_PREVIEW_JOURNAL_LS_PROPERTY, PREVIEW_IMAGE_ICON_24);

			setEnabled(false); // initial state at startup
		}
	}

	private class AssociateTranslatorAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			MedViewDialogs mVD = MedViewDialogs.instance();

			String lS = QUESTION_SHOULD_ASSOCIATE_TRANSLATOR_LS_PROPERTY;

			String message = mVDH.getLanguageString(lS);

			int type = MedViewDialogConstants.YES_NO;

			int ans = mVD.createAndShowQuestionDialog(SummaryCreatorFrame.this, type, message);

			if (ans == MedViewDialogConstants.YES)
			{
				String tempLoc = model.getTemplateModelLocation();

				String transLoc = model.getTranslatorModelLocation();

				try
				{
					model.saveTemplateModel(tempLoc, transLoc);
				}
				catch (Exception exc)
				{
					mVD.createAndShowErrorDialog(SummaryCreatorFrame.this, exc.getMessage());
				}
			}
		}

		public AssociateTranslatorAction()
		{
			super(ACTION_ASSOCIATE_TRANSLATOR_LS_PROPERTY, LINK_IMAGE_ICON_24);

			setEnabled(false); // initial state at startup
		}
	}

// ---------------------------------------------------------------
// ***************************************************************
// ---------------------------------------------------------------



// ---------------------------------------------------------------
// ************************** LISTENERS **************************
// ---------------------------------------------------------------

	private class ModelListener implements TemplateModelKeeperListener,
		TranslatorModelKeeperListener, SummaryCreatorModelListener
	{
		public void templateModelChanged(TemplateModelKeeperEvent e)
		{
			checkPreviewAndLinkageAction();
		}

		public void translatorModelChanged(TranslatorModelKeeperEvent e)
		{
			checkPreviewAndLinkageAction();
		}

		public void checkPreviewAndLinkageAction()
		{
			boolean containsTemplate = model.containsTemplateModel();

			boolean containsTranslator = model.containsTranslatorModel();

			associateAction.setEnabled(containsTemplate && containsTranslator);

			previewAction.setEnabled(containsTemplate && containsTranslator);
		}

		public void translatorModelLocationChanged(TranslatorModelKeeperEvent e) {}

		public void templateModelLocationChanged(TemplateModelKeeperEvent e) {}

		public void termListingChanged(SummaryCreatorModelEvent e)
		{
			boolean valid = model.areTermLocationsValid();

			translatorView.setEnabled(valid);

			templateViewWrapper.setEnabled(valid);

			termListView.setEnabled(valid);
		}
	}

	private class PropertyListener extends MedViewPropertyAdapter
	{
		public void userPropertyChanged(MedViewPropertyEvent e)
		{
			String lafProp = SummaryCreatorUserProperties.LOOK_AND_FEEL_PROPERTY;

			if (e.getPropertyName().equals(lafProp))
			{
				Class lafPropClass = SummaryCreatorUserProperties.class;

				String className = mVDH.getUserStringPreference(lafProp, "", lafPropClass);

				String setClassName = UIManager.getLookAndFeel().getClass().getName();

				if (!className.equalsIgnoreCase(setClassName))
				{
					try
					{
						UIManager.setLookAndFeel(className); // triggers update (MainShell)
					}
					catch (Exception exc)
					{
						exc.printStackTrace(); // should not happen

						System.exit(1);
					}
				}
			}
		}
	}

// ---------------------------------------------------------------
// ***************************************************************
// ---------------------------------------------------------------

}
