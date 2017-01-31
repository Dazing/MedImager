package medview.medserver.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import medview.common.actions.*;
import medview.common.components.menu.*;
import medview.common.components.toolbar.*;
import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.medserver.model.*;
import medview.medserver.view.settings.*;

import misc.domain.*;

import misc.foundation.*;

import misc.gui.components.*;
import misc.gui.constants.*;

public class MedServerFrame extends MainShell implements
	MedViewLanguageConstants, MedViewMediaConstants, MedServerModelConstants
{

// --------------------------------------------------------------
// **************** MAINSHELL OVERRIDDEN METHODS ****************
// --------------------------------------------------------------

	protected int getMSHeight()
	{
		return 600;
	}

	protected int getMSWidth()
	{
		return 700;
	}

	protected String getMSTitle()
	{
		return "MedServer " + VERSION_STRING;
	}

	protected JToolBar[] getToolBars()
	{
		serverToolbar = new MedViewToolBar(TITLE_SERVER_TOOLBAR_LS_PROPERTY);

		serverToolbar.add(connectButton);

		serverToolbar.add(disconnectButton);

		serverToolbar.addSeparator();

		serverToolbar.add(settingsButton);

		return new JToolBar[] { serverToolbar };
	}

	protected boolean usesSplash()
	{
		return true;
	}

	protected boolean usesDotThread()
	{
		return false;
	}

	protected boolean usesToolBars()
	{
		return true;
	}

	protected int getToolBarHeight()
	{
		return GUIConstants.TOOLBAR_HEIGHT_SMALL;
	}

	protected Insets getPadding()
	{
		return new Insets(3,3,3,3);
	}

	protected Image getSplashImage()
	{
		String prop = SPLASH_MEDSERVER_IMAGE_ICON;

		return mVDH.getImage(prop);
	}

	protected String getDeveloperSplashText()
	{
		String nL = "\n";

		String a = "MedServer " + VERSION_STRING;

		String b = "Developed by Fredrik Lindahl";

		return a + nL + b;
	}

	protected void onClose()
	{
		String lS = LABEL_EXIT_MEDSERVER_LS_PROPERTY;

		String message = mVDH.getLanguageString(lS);

		int type = MedViewDialogConstants.YES_NO;

		int choice = mVD.createAndShowQuestionDialog(this, type, message);

		if (choice == MedViewDialogConstants.YES) { System.exit(0); }
	}

// --------------------------------------------------------------
// **************************************************************
// --------------------------------------------------------------





	public void updateActivationState()
	{
		boolean tLS = model.areTermLocationsSet();

		boolean eLS = model.isExaminationDataLocationSet();

		boolean isC = model.isServerActive();

		activateAction.setEnabled(tLS && eLS && !isC);

		deactivateAction.setEnabled(isC);

		setStatusText();
	}



	private void setTabTitlesText()
	{
		String tLS = TAB_TERMS_LS_PROPERTY;

		String eLS = TAB_EXAMINATION_DATA_LS_PROPERTY;

		String sLS = TAB_SERVER_LS_PROPERTY;

		tabbedPane.setTitleAt(0, mVDH.getLanguageString(sLS));

		tabbedPane.setTitleAt(1, mVDH.getLanguageString(tLS));

		tabbedPane.setTitleAt(2, mVDH.getLanguageString(eLS));
	}

	private void setStatusText()
	{
		String sLS = OTHER_STATUS_LS_PROPERTY;

		String aLS = OTHER_ACTIVATED_LS_PROPERTY;

		String dLS = OTHER_DEACTIVATED_LS_PROPERTY;

		String sT = mVDH.getLanguageString(sLS);

		String aT = mVDH.getLanguageString(aLS);

		String dT = mVDH.getLanguageString(dLS);

		if (model.isServerActive())
		{
			setStatusText(sT + ": " + aT);
		}
		else
		{
			setStatusText(sT + ": " + dT);
		}
	}





	public MedServerModel getModel()
	{
		return model;
	}


	protected void subclassInit()
	{
		mVD = MedViewDialogs.instance();

		mVDH = MedViewDataHandler.instance();
	}

	public MedServerFrame(MedServerModel model)
	{
		super();

		this.model = model;

		tabbedPane = new JTabbedPane();

		ProgressNotifiable pN = new DefaultProgressNotifiable()
		{
			public void setCurrent(int c)
			{
				super.setCurrent(c);

				if (!((c % 10) == 0)) { return; }

				String desc = getDescription(); int t = getTotal();

				setLoadingSplashText(desc + " (" + c + " / " + t + ")");
			}
		};

		serverPanel = new MedServerServerPanel(this);

		termPanel = new MedServerTermPanel(this);

		examPanel = new MedServerExaminationPanel(this, pN);

		tabbedPane.add(serverPanel);

		tabbedPane.add(termPanel);

		tabbedPane.add(examPanel);

		setTabTitlesText();

		tabbedPane.setOpaque(false);

		tabbedPane.setBorder(new EmptyBorder(0,0,0,0));

		setCenterComponent(tabbedPane);


		// menus

		String archiveLS = MENU_ARCHIVE_LS_PROPERTY; // menu - don't have action for name

		String helpLS = MENU_HELP_LS_PROPERTY; // menu - don't have action for name

		String exitLS = MENU_ITEM_FILE_EXIT_LS_PROPERTY; // menu - don't have action for name


		String archiveMneLS = MNEMONIC_MENU_ARCHIVE_LS_PROPERTY;

		String helpMneLS = MNEMONIC_MENU_HELP_LS_PROPERTY;

		String aboutMneLS = MNEMONIC_MENU_ITEM_ABOUT_LS_PROPERTY; // item - have action for name

		String activateMneLS = MNEMONIC_MENU_ITEM_ACTIVATE_LS_PROPERTY; // item - have action for name

		String deactivateMneLS = MNEMONIC_MENU_ITEM_DEACTIVATE_LS_PROPERTY; // item - have action for name

		String settingsMneLS = MNEMONIC_MENU_ITEM_PREFERENCES_LS_PROPERTY; // item - have action for name

		String exitMneLS = MNEMONIC_MENU_ITEM_EXIT_LS_PROPERTY; // item - have action for name


		activateAction = new ActivateAction();

		deactivateAction = new DeactivateAction();

		settingsAction = new ShowSettingsAction();

		exitAction = new ExitMedServerAction();

		aboutAction = new ShowAboutAction();


		archiveMenu = new MedViewMenu(archiveLS, archiveMneLS);

		helpMenu = new MedViewMenu(helpLS, helpMneLS);


		activateMenuItem = new MedViewMenuItem(activateAction, activateMneLS);

		deactivateMenuItem = new MedViewMenuItem(deactivateAction, deactivateMneLS);

		settingsMenuItem = new MedViewMenuItem(settingsAction, settingsMneLS);

		exitMenuItem = new MedViewMenuItem(exitAction, exitMneLS);

		aboutMenuItem = new MedViewMenuItem(aboutAction, aboutMneLS);


		archiveMenu.add(activateMenuItem);

		archiveMenu.add(deactivateMenuItem);

		archiveMenu.addSeparator();

		archiveMenu.add(settingsMenuItem);

		archiveMenu.addSeparator();

		archiveMenu.add(exitMenuItem);


		helpMenu.add(aboutMenuItem);


		addToMenuBar(archiveMenu);

		addToMenuBar(helpMenu);


		// toolbar buttons

		connectButton = new MedViewToolBarSmallButton(activateAction);

		disconnectButton = new MedViewToolBarSmallButton(deactivateAction);

		settingsButton = new MedViewToolBarSmallButton(settingsAction);


		// settings

		CommandQueue queue = mVD.getSettingsCommandQueue();

		medServerVisualSCP = new MedServerVisualSCP(queue, this);


		// status text

		setStatusText();


		// initial action enable/disable

		updateActivationState();
	}

	private MedServerModel model;

	private JTabbedPane tabbedPane;

	private MedServerTermPanel termPanel;

	private MedServerServerPanel serverPanel;

	private MedServerExaminationPanel examPanel;

	private SettingsContentPanel medServerVisualSCP;

	private MedViewDialogs mVD;

	private MedViewDataHandler mVDH;

	private Action settingsAction;

	private Action aboutAction;

	private Action exitAction;

	private Action activateAction;

	private Action deactivateAction;

	private JMenu archiveMenu;

	private JMenu helpMenu;

	private JMenuItem exitMenuItem;

	private JMenuItem aboutMenuItem;

	private JMenuItem settingsMenuItem;

	private JMenuItem activateMenuItem;

	private JMenuItem deactivateMenuItem;

	private JButton connectButton;

	private JButton disconnectButton;

	private JButton settingsButton;

	private JToolBar serverToolbar;






	private class ShowSettingsAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			String lS = TITLE_MEDSERVER_SETTINGS_LS_PROPERTY;

			mVD.createAndShowSettingsDialog(MedServerFrame.this, lS,

				new SettingsContentPanel[] { medServerVisualSCP }, null);
		}

		public ShowSettingsAction()
		{
			super(ACTION_SETTINGS_LS_PROPERTY, PREFERENCES_IMAGE_ICON_16);
		}
	}

	private class ShowAboutAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			String titLS = TITLE_ABOUT_MEDSERVER_LS_PROPERTY;

			String txtLS = OTHER_ABOUT_MEDSERVER_TEXT_LS_PROPERTY;

			String v = VERSION_STRING;

			mVD.createAndShowAboutDialog(MedServerFrame.this, titLS, "MedServer", v, txtLS);
		}

		public ShowAboutAction()
		{
			super(ACTION_ABOUT_MEDSERVER_LS_PROPERTY);
		}
	}

	private class ExitMedServerAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e) { onClose(); }

		public ExitMedServerAction()
		{
			super(ACTION_EXIT_LS_PROPERTY);
		}
	}

	private class ActivateAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				model.activateServer();

				updateActivationState();
			}
			catch (CouldNotActivateException exc)
			{
				mVD.createAndShowErrorDialog(MedServerFrame.this, exc.getMessage());
			}
		}

		public ActivateAction()
		{
			super(ACTION_ACTIVATE_LS_PROPERTY, CONNECT_IMAGE_ICON_16);
		}
	}

	private class DeactivateAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				model.deactivateServer();

				updateActivationState();
			}
			catch (CouldNotDeactivateException exc)
			{
				mVD.createAndShowErrorDialog(MedServerFrame.this, exc.getMessage());
			}
		}

		public DeactivateAction()
		{
			super(ACTION_DEACTIVATE_LS_PROPERTY, DISCONNECT_IMAGE_ICON_16);
		}
	}

}
