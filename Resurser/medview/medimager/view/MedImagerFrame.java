/**
 * @(#) MedImagerFrame.java
 */

package medview.medimager.view;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.util.*;

import javax.swing.*;
import javax.swing.text.*;

import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.medimager.foundation.*;
import medview.medimager.model.*;
import medview.medimager.view.settings.*;

import misc.domain.*;

import misc.foundation.*;
import misc.foundation.io.*;

import misc.gui.actions.*;
import misc.gui.components.*;
import misc.gui.constants.*;

/**
 * The MedImager application main frame.
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MedImagerFrame extends MainShell implements GUIConstants,

	MedViewMediaConstants, MedImagerActionConstants, MedViewLanguageConstants, ShellOwner, MedImagerConstants
{

	/*
	 * NOTE: some components are 'leaf' components, while others
	 * are pure layout components. The 'leaf' components are created
	 * at construction and do not change when the usability layer
	 * changes. The layout components depend on the usability layer
	 * setting.
	 */

	// actions not plugged into shells

	private ExtendedAbstractAction aboutAction = new AboutAction(); // always available

	private ExtendedAbstractAction exitAction = new ExitAction(); // always available

	private ExtendedAbstractAction newAlbumAction = new NewAlbumAction(); // always available

	private ExtendedAbstractAction preferencesAction = new PreferencesAction(); // always available

	// action shells

	private ActionShell[] shells = new ActionShell[] // indexed according to ShellState constants
	{
		new NewFolderActionShell(), new RemoveActionShell(), new CutActionShell(),

		new CopyActionShell(), new PasteActionShell(), new JournalActionShell(),

		new EnlargeImageActionShell(), new InformationActionShell(), new ShareLocalActionShell(),

		new ShareGlobalActionShell() // enable / disable / meaning depends on current focus
	};

	// the main panels ('leaf' GUI components - invariant to usability model layer changes)

	private ChosenBrowsePanel chosenBrowsePanel = null;

	private DatabaseSearchPanel databaseSearchPanel = null;

	// the toolbar buttons ('leaf' GUI components - invariant to usability model layer changes)

	private JButton newAlbumToolBarButton, newFolderToolBarButton, removeToolBarButton, cutToolBarButton,

			copyToolBarButton, pasteToolBarButton, journalToolBarButton, enlargeImageToolBarButton,

			informationToolBarButton, shareLocalToolBarButton, shareGlobalToolBarButton;

	// menus ('leaf' GUI components - invariant to usability model layer changes)

	private JMenu fileMenu, editMenu, helpMenu;

	// menu items ('leaf' GUI components - invariant to usability model layer changes)

	private JMenuItem newAlbumMenuItem, newFolderMenuItem, removeMenuItem, journalMenuItem, exitMenuItem,

			enlargeImageMenuItem, informationMenuItem, shareLocalMenuItem, shareGlobalMenuItem,

			cutMenuItem, copyMenuItem, pasteMenuItem, preferencesMenuItem, aboutMenuItem;

	// various other members

	private MedImagerModel model = null;

	private ShellState currentState = null;

	private UsabilityModel usabilityModel = null;

	private Vector ownerComponentVector = new Vector();

	private SettingsContentPanel[] settingsContentPanels;


	// CONSTRUCTOR(S) AND RELATED METHODS

	public MedImagerFrame( MedImagerModel model )
	{
		super();

		this.model = model;

		// frame icon

		setIconImage(MedViewDataHandler.instance().getImage(MedViewMediaConstants.FRAME_IMAGE_ICON));

		// set direct usability model reference

		this.usabilityModel = model.getUsabilityModel();

		// register global actions

		GlobalActionMediator.instance().registerAction(GlobalActionMediator.ABOUT_ACTION, aboutAction);

		GlobalActionMediator.instance().registerAction(GlobalActionMediator.EXIT_ACTION, exitAction);

		GlobalActionMediator.instance().registerAction(GlobalActionMediator.NEW_ALBUM_ACTION, newAlbumAction);

		GlobalActionMediator.instance().registerAction(GlobalActionMediator.PREFERENCES_ACTION, preferencesAction);

		// add the root pane as an owner component

		ownerComponentVector.add(this.getRootPane());

		// leaf component creation

		createGUILeafComponents();

		// layout frame and menus

		layoutFrame();

		addMenusAndItems();

		// attach usability model listener

		usabilityModel.addUsabilityModelListener(new UsabilityModelListener()
		{
			public void functionalLayerStateChanged(UsabilityModelEvent e)
			{
				if (e.getFunctionalLayer() == UsabilityModel.LAYER_STORE_AWAY)
				{
					refreshFrameLayout();
				}

				refreshMenus();

				refreshToolBars();
			}
		});

		// settings / preferences

		initSettings();
	}

	protected void layoutFrame()
	{
		if (usabilityModel.isFunctionalLayerActive(UsabilityModel.LAYER_STORE_AWAY))
		{
			JTabbedPane tabbedPane = new JTabbedPane();

			tabbedPane.add(databaseSearchPanel, "Sök bland alla bilder");

			tabbedPane.add(chosenBrowsePanel, "Granska valda bilder");

			setCenterComponent(tabbedPane);
		}
		else
		{
			setCenterComponent(databaseSearchPanel);
		}
	}

	protected void refreshFrameLayout()
	{
		layoutFrame();
	}

	protected void createGUILeafComponents()
	{
		// create main panels

		databaseSearchPanel = new DatabaseSearchPanel(model, this);

		chosenBrowsePanel = new ChosenBrowsePanel(model, this);

		// create toolbar buttons

		newAlbumToolBarButton = new JButton(newAlbumAction);

		newAlbumToolBarButton.setText("");

		ownerComponentVector.add(newAlbumToolBarButton);

		newFolderToolBarButton = new JButton(shells[NEW_FOLDER_ACTION]);

		newFolderToolBarButton.setText("");

		ownerComponentVector.add(newFolderToolBarButton);

		removeToolBarButton = new JButton(shells[REMOVE_ACTION]);

		removeToolBarButton.setText("");

		ownerComponentVector.add(removeToolBarButton);

		cutToolBarButton = new JButton(shells[CUT_ACTION]);

		cutToolBarButton.setText("");

		ownerComponentVector.add(cutToolBarButton);

		copyToolBarButton = new JButton(shells[COPY_ACTION]);

		copyToolBarButton.setText("");

		ownerComponentVector.add(copyToolBarButton);

		pasteToolBarButton = new JButton(shells[PASTE_ACTION]);

		pasteToolBarButton.setText("");

		ownerComponentVector.add(pasteToolBarButton);

		journalToolBarButton = new JButton(shells[JOURNAL_ACTION]);

		journalToolBarButton.setText("");

		ownerComponentVector.add(journalToolBarButton);

		enlargeImageToolBarButton = new JButton(shells[ENLARGE_IMAGE_ACTION]);

		enlargeImageToolBarButton.setText("");

		ownerComponentVector.add(enlargeImageToolBarButton);

		informationToolBarButton = new JButton(shells[INFORMATION_ACTION]);

		informationToolBarButton.setText("");

		ownerComponentVector.add(informationToolBarButton);

		shareLocalToolBarButton = new JButton(shells[SHARE_LOCAL_ACTION]);

		shareLocalToolBarButton.setText("");

		ownerComponentVector.add(shareLocalToolBarButton);

		shareGlobalToolBarButton = new JButton(shells[SHARE_GLOBAL_ACTION]);

		shareGlobalToolBarButton.setText("");

		ownerComponentVector.add(shareGlobalToolBarButton);

		// create menus

		fileMenu = new JMenu("Arkiv");

		ownerComponentVector.add(fileMenu);

		editMenu = new JMenu("Redigera");

		ownerComponentVector.add(editMenu);

		helpMenu = new JMenu("Hjälp");

		ownerComponentVector.add(helpMenu);

		// create menu items

		newAlbumMenuItem = new JMenuItem(newAlbumAction);

		newAlbumMenuItem.setIcon((Icon)newAlbumAction.getValue(ExtendedAbstractAction.SMALL_MENU_ICON));

		ownerComponentVector.add(newAlbumMenuItem);

		newFolderMenuItem = new JMenuItem(shells[NEW_FOLDER_ACTION]);

		newFolderMenuItem.setIcon((Icon)shells[NEW_FOLDER_ACTION].getValue(ExtendedAbstractAction.SMALL_MENU_ICON));

		ownerComponentVector.add(newFolderMenuItem);

		removeMenuItem = new JMenuItem(shells[REMOVE_ACTION]);

		removeMenuItem.setIcon((Icon)shells[REMOVE_ACTION].getValue(ExtendedAbstractAction.SMALL_MENU_ICON));

		ownerComponentVector.add(removeMenuItem);

		journalMenuItem = new JMenuItem(shells[JOURNAL_ACTION]);

		journalMenuItem.setIcon((Icon)shells[JOURNAL_ACTION].getValue(ExtendedAbstractAction.SMALL_MENU_ICON));

		ownerComponentVector.add(journalMenuItem);

		enlargeImageMenuItem = new JMenuItem(shells[ENLARGE_IMAGE_ACTION]);

		enlargeImageMenuItem.setIcon((Icon)shells[ENLARGE_IMAGE_ACTION].getValue(ExtendedAbstractAction.SMALL_MENU_ICON));

		ownerComponentVector.add(enlargeImageMenuItem);

		informationMenuItem = new JMenuItem(shells[INFORMATION_ACTION]);

		informationMenuItem.setIcon((Icon)shells[INFORMATION_ACTION].getValue(ExtendedAbstractAction.SMALL_MENU_ICON));

		ownerComponentVector.add(informationMenuItem);

		shareLocalMenuItem = new JMenuItem(shells[SHARE_LOCAL_ACTION]);

		shareLocalMenuItem.setIcon((Icon)shells[SHARE_LOCAL_ACTION].getValue(ExtendedAbstractAction.SMALL_MENU_ICON));

		ownerComponentVector.add(shareLocalMenuItem);

		shareGlobalMenuItem = new JMenuItem(shells[SHARE_GLOBAL_ACTION]);

		shareGlobalMenuItem.setIcon((Icon)shells[SHARE_GLOBAL_ACTION].getValue(ExtendedAbstractAction.SMALL_MENU_ICON));

		ownerComponentVector.add(shareGlobalMenuItem);

		exitMenuItem = new JMenuItem(exitAction);

		ownerComponentVector.add(exitMenuItem);

		preferencesMenuItem = new JMenuItem(preferencesAction);

		ownerComponentVector.add(preferencesMenuItem);

		cutMenuItem = new JMenuItem(shells[CUT_ACTION]);

		cutMenuItem.setIcon((Icon)shells[CUT_ACTION].getValue(ExtendedAbstractAction.SMALL_MENU_ICON));

		ownerComponentVector.add(cutMenuItem);

		copyMenuItem = new JMenuItem(shells[COPY_ACTION]);

		copyMenuItem.setIcon((Icon)shells[COPY_ACTION].getValue(ExtendedAbstractAction.SMALL_MENU_ICON));

		ownerComponentVector.add(copyMenuItem);

		pasteMenuItem = new JMenuItem(shells[PASTE_ACTION]);

		pasteMenuItem.setIcon((Icon)shells[PASTE_ACTION].getValue(ExtendedAbstractAction.SMALL_MENU_ICON));

		ownerComponentVector.add(pasteMenuItem);

		aboutMenuItem = new JMenuItem(aboutAction);

		ownerComponentVector.add(aboutMenuItem);
	}

	protected JToolBar[] getToolBars()
	{
		JToolBar toolBar = new JToolBar();

		toolBar.setRollover(true);

		if (usabilityModel.isFunctionalLayerActive(UsabilityModel.LAYER_ORGANIZE)) // if this layer is active...
		{
			toolBar.add(newAlbumToolBarButton);

			toolBar.addSeparator();

			toolBar.add(newFolderToolBarButton);
		}

		if (usabilityModel.isFunctionalLayerActive(UsabilityModel.LAYER_STORE_AWAY)) // ... then this layer is active
		{
			toolBar.add(removeToolBarButton);

			toolBar.addSeparator();
		}

		if (usabilityModel.isFunctionalLayerActive(UsabilityModel.LAYER_STORE_AWAY))
		{
			toolBar.add(cutToolBarButton);
		}

		toolBar.add(copyToolBarButton);

		if (usabilityModel.isFunctionalLayerActive(UsabilityModel.LAYER_STORE_AWAY))
		{
			toolBar.add(pasteToolBarButton);
		}

		toolBar.addSeparator();

		toolBar.add(journalToolBarButton);

		toolBar.add(enlargeImageToolBarButton);

		if (usabilityModel.isFunctionalLayerActive(UsabilityModel.LAYER_STORE_AWAY))
		{
			toolBar.add(informationToolBarButton);
		}

		if (usabilityModel.isFunctionalLayerActive(UsabilityModel.LAYER_SHARE))
		{
			toolBar.addSeparator();

			toolBar.add(shareLocalToolBarButton);

			toolBar.add(shareGlobalToolBarButton);
		}

		toolBar.add(Box.createGlue()); // fills out rest of toolbar space

		return new JToolBar[] { toolBar };
	}

	protected void addMenusAndItems()
	{
		if (usabilityModel.isFunctionalLayerActive(UsabilityModel.LAYER_ORGANIZE)) // if this layer is active...
		{
			fileMenu.add(newAlbumMenuItem);

			fileMenu.addSeparator();

			fileMenu.add(newFolderMenuItem);
		}

		if (usabilityModel.isFunctionalLayerActive(UsabilityModel.LAYER_STORE_AWAY)) // ... then this layer is active
		{
			fileMenu.add(removeMenuItem);

			fileMenu.addSeparator();
		}

		fileMenu.add(journalMenuItem);

		fileMenu.add(enlargeImageMenuItem);

		if (usabilityModel.isFunctionalLayerActive(UsabilityModel.LAYER_STORE_AWAY))
		{
			fileMenu.add(informationMenuItem);
		}

		fileMenu.addSeparator();

		if (usabilityModel.isFunctionalLayerActive(UsabilityModel.LAYER_SHARE))
		{
			fileMenu.add(shareLocalMenuItem);

			fileMenu.add(shareGlobalMenuItem);

			fileMenu.addSeparator();
		}

		fileMenu.add(preferencesMenuItem);

		fileMenu.addSeparator();

		fileMenu.add(exitMenuItem);

		if (usabilityModel.isFunctionalLayerActive(UsabilityModel.LAYER_STORE_AWAY))
		{
			editMenu.add(cutMenuItem);
		}

		editMenu.add(copyMenuItem);

		if (usabilityModel.isFunctionalLayerActive(UsabilityModel.LAYER_STORE_AWAY))
		{
			editMenu.add(pasteMenuItem);
		}

		helpMenu.add(aboutMenuItem);

		addToMenuBar(fileMenu);

		addToMenuBar(editMenu);

		addToMenuBar(helpMenu);
	}

	protected void refreshMenus()
	{
		clearMenuBar();

		fileMenu.removeAll();

		editMenu.removeAll();

		addMenusAndItems();
	}

	private void initSettings()
	{
		CommandQueue queue = MedViewDialogs.instance().getSettingsCommandQueue();

		settingsContentPanels = new SettingsContentPanel[]
		{
			new MedImagerJournalSCP(queue, this),

			new MedImagerDataHandlingSCP(queue, this)
		};
	}


	// SETTINGS DIALOG

	public void showSettingsDialog()
	{
		MedViewDialogs.instance().createAndShowSettingsDialog(this,

			MedViewLanguageConstants.TITLE_MEDIMAGER_SETTINGS_LS_PROPERTY, settingsContentPanels);
	}


	// other methods

	public ActionShell getActionShell(int id)
	{
		return shells[id];
	}

	public ActionShell[] getActionShells()
	{
		return shells;
	}

	public ShellState getCurrentState()
	{
		return currentState;
	}

	public ActionShell getShellForID(int id)
	{
		for (int ctr=0; ctr<shells.length; ctr++)
		{
			if (shells[ctr].getIdentification() == id)
			{
				return shells[ctr];
			}
		}

		return null;
	}

	/**
	 * Note that all actions set to null will be disabled.
	 */
	public void pluginState(ShellState state)
	{
		currentState = state;	// keep track of current state

		Action[] actions = state.getActions();	// some might be null

		for (int ctr=0; ctr<actions.length; ctr++)
		{
			if (actions[ctr] != null)
			{
				shells[ctr].setPluggedAction(actions[ctr]);

				shells[ctr].setEnabled(actions[ctr].isEnabled());
			}
			else
			{
				shells[ctr].setEnabled(false);
			}
		}
	}

	public boolean isOwnerComponent(Component comp)
	{
		return ownerComponentVector.contains(comp);
	}


	protected Insets getPadding()
	{
		return new Insets(CCS,CCS,CCS,CCS);
	}

	protected int getMSHeight()
	{
		return 768;
	}

	protected int getMSWidth()
	{
		return 1024;
	}

	protected String getMSTitle()
	{
		return "MedImager " + MedImagerConstants.VERSION_STRING;
	}

	protected Image getSplashImage()
	{
		return MedViewDataHandler.instance().getImage(SPLASH_MEDIMAGER_IMAGE_ICON);
	}

	protected String getInitialLoadText()
	{
		return "Laddar MedImager...";
	}

	protected boolean usesSplash()
	{
		return true;
	}

	protected boolean usesToolBars()
	{
		return true;
	}

	public MedImagerModel getModel( )
	{
		return model;
	}


	// PATIENT JOURNALS

	private StyledDocument[] docs; // used only in getJournals()

	/**
	 * Due to progress monitoring, this thread may not be called
	 * from the event dispatch thread.
	 *
	 * @param pids PatientIdentifier[]
	 * @return StyledDocument[]
	 */
	public StyledDocument[] getJournals(final PatientIdentifier[] pids)
	{
		if (SwingUtilities.isEventDispatchThread())
		{
			throw new AWTError("This method cannot be called from the EDT"); // unchecked
		}

		NotifyingRunnable runnable = new NotifyingRunnable()
		{
			public void run()
			{
				// this code will be placed on a thread other than the event dispatch thread

				Vector documentVector = new Vector(pids.length);

				for (int ctr=0; ctr<pids.length; ctr++)
				{
					try
					{
						StyledDocument journal = model.getJournal(pids[ctr], getNotifiable());

						documentVector.add(journal);
					}
					catch (final CouldNotGenerateJournalException exc)
					{
						SwingUtilities.invokeLater(new Runnable()
						{
							public void run()
							{
								JOptionPane.showMessageDialog(MedImagerFrame.this,

									"Det gick inte att generera journal:\n" + exc.getMessage(),

										"Fel vid generering av journal", JOptionPane.ERROR_MESSAGE);
							}
						});
					}
				}

				docs = new StyledDocument[documentVector.size()];

				documentVector.toArray(docs);
			}
		};

		Thread runnerThread = MedViewDialogs.instance().startProgressMonitoring(MedImagerFrame.this, runnable);

		try
		{
			runnerThread.join(); // wait until thread has finished execution (i.e. until docs populated)

			/* THIS IS WHY THE THREAD CALLING THIS METHOD CANNOT BE THE EDT */
		}
		catch (InterruptedException exc)
		{
			exc.printStackTrace();

			System.exit(1); // fatal error if this occurs (should never occur)
		}

		return docs;
	}

	public void showJournals(final PatientIdentifier[] pids)
	{
		final String[] titles = new String[pids.length];

		for (int ctr=0; ctr<titles.length; ctr++)
		{
			titles[ctr] = pids[ctr] + "";
		}

		new Thread(new Runnable()
		{
			public void run()
			{
				final StyledDocument[] journals = getJournals(pids);

				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						MedViewDialogs.instance().createAndShowTextDialogs(

							MedImagerFrame.this, journals, titles, false);
					}
				});
			}

		}).start();
	}


	// ACTION SHELLS

	public static class NewFolderActionShell extends ActionShell
	{
		public NewFolderActionShell()
		{
			super("Ny mapp", MedViewDataHandler.instance().getImageIcon(NEW_FOLDER_IMAGE_ICON_24),

				MedViewDataHandler.instance().getImageIcon(NEW_FOLDER_IMAGE_ICON_16), NEW_FOLDER_ACTION);

			putValue(Action.SHORT_DESCRIPTION, "Skapar ny mapp");
		}
	}

	public static class RemoveActionShell extends ActionShell
	{
		public RemoveActionShell()
		{
			super("Ta bort", MedViewDataHandler.instance().getImageIcon(REMOVE_IMAGE_ICON_24),

				MedViewDataHandler.instance().getImageIcon(REMOVE_IMAGE_ICON_16), REMOVE_ACTION);

			putValue(Action.SHORT_DESCRIPTION, "Tar bort valt objekt");
		}
	}

	public static class JournalActionShell extends ActionShell
	{
		public JournalActionShell()
		{
			super("Se journal", MedViewDataHandler.instance().getImageIcon(JOURNAL_IMAGE_ICON_24),

				MedViewDataHandler.instance().getImageIcon(JOURNAL_IMAGE_ICON_16), JOURNAL_ACTION);

			putValue(Action.SHORT_DESCRIPTION, "Visar tillhörande patientjournal till valt objekt");
		}
	}

	public static class EnlargeImageActionShell extends ActionShell
	{
		public EnlargeImageActionShell()
		{
			super("Se bild i fullstorlek", MedViewDataHandler.instance().getImageIcon(ENLARGE_IMAGE_ICON_24),

				MedViewDataHandler.instance().getImageIcon(ENLARGE_IMAGE_ICON_16), ENLARGE_IMAGE_ACTION);

			putValue(Action.SHORT_DESCRIPTION, "Visar vald bild i fullstorlek");
		}
	}

	public static class InformationActionShell extends ActionShell
	{
		public InformationActionShell()
		{
			super("Information om bild", MedViewDataHandler.instance().getImageIcon(INFORMATION_IMAGE_ICON_24),

				MedViewDataHandler.instance().getImageIcon(INFORMATION_IMAGE_ICON_16), INFORMATION_ACTION);

			putValue(Action.SHORT_DESCRIPTION, "Visar ytterligare information om vald bild");
		}
	}

	public static class CutActionShell extends ActionShell
	{
		public CutActionShell()
		{
			super("Klipp", MedViewDataHandler.instance().getImageIcon(CUT_IMAGE_ICON_24),

				MedViewDataHandler.instance().getImageIcon(CUT_IMAGE_ICON_16), CUT_ACTION);

			putValue(Action.SHORT_DESCRIPTION, "Klipp");
		}
	}

	public static class CopyActionShell extends ActionShell
	{
		public CopyActionShell()
		{
			super("Kopiera", MedViewDataHandler.instance().getImageIcon(COPY_IMAGE_ICON_24),

				MedViewDataHandler.instance().getImageIcon(COPY_IMAGE_ICON_16), COPY_ACTION);

			putValue(Action.SHORT_DESCRIPTION, "Kopiera");
		}
	}

	public static class PasteActionShell extends ActionShell
	{
		public PasteActionShell()
		{
			super("Klistra", MedViewDataHandler.instance().getImageIcon(PASTE_IMAGE_ICON_24),

				MedViewDataHandler.instance().getImageIcon(PASTE_IMAGE_ICON_16), PASTE_ACTION);

			putValue(Action.SHORT_DESCRIPTION, "Klistra");
		}
	}

	public static class ShareLocalActionShell extends ActionShell
	{
		public ShareLocalActionShell()
		{
			super("Dela ut lokalt", MedViewDataHandler.instance().getImageIcon(SHARE_FOLDER_IMAGE_ICON_24),

				MedViewDataHandler.instance().getImageIcon(SHARE_FOLDER_IMAGE_ICON_16), SHARE_LOCAL_ACTION);

			putValue(Action.SHORT_DESCRIPTION, "Delar ut valt objekt lokalt (över lokala nätverket)");
		}
	}

	public static class ShareGlobalActionShell extends ActionShell
	{
		public ShareGlobalActionShell()
		{
			super("Dela ut globalt", MedViewDataHandler.instance().getImageIcon(SHARE_FOLDER_GLOBALLY_IMAGE_ICON_24),

				MedViewDataHandler.instance().getImageIcon(SHARE_FOLDER_GLOBALLY_IMAGE_ICON_16), SHARE_GLOBAL_ACTION);

			putValue(Action.SHORT_DESCRIPTION, "Delar ut valt objekt globalt (över internet)");
		}
	}


	// SHOW INFORMATION ABOUT THE APPLICATION

	private class AboutAction extends ExtendedAbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			MedViewDialogs.instance().createAndShowAboutDialog(MedImagerFrame.this,

				TITLE_ABOUT_MEDIMAGER_LS_PROPERTY, "MedImager", MedImagerConstants.VERSION_STRING,

				OTHER_ABOUT_MEDIMAGER_TEXT_LS_PROPERTY);
		}

		public AboutAction()
		{
			super("Om MedImager...");

			putValue(Action.SHORT_DESCRIPTION, "Visar information om MedImager applikationen");
		}
	}

	// EXIT THE APPLICATION

	private class ExitAction extends ExtendedAbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			Component par = MedImagerFrame.this;

			Object mess = "Vill du avsluta?";

			String tit = "Vill du avsluta?";

			int optType = JOptionPane.YES_NO_OPTION;

			int choice = JOptionPane.showConfirmDialog(par, mess, tit, optType);

			if (choice == JOptionPane.YES_OPTION)
			{
				System.exit(0);
			}
		}

		public ExitAction()
		{
			super("Avsluta programmet");

			putValue(Action.SHORT_DESCRIPTION, "Frågar om du vill avsluta programmet");
		}
	}

	// SHOW PREFERENCES

	private class PreferencesAction extends ExtendedAbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			showSettingsDialog();
		}

		public PreferencesAction()
		{
			super("Inställningar...");

			putValue(Action.SHORT_DESCRIPTION, "Inställningar för MedImager");
		}
	}


	// ALBUM STORAGE, RETRIEVAL, AND CLOSAGE

	private class NewAlbumAction extends ExtendedAbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			String name = JOptionPane.showInputDialog(MedImagerFrame.this, "Namn på nytt album:");

			if (name != null)
			{
				model.createNewAlbum(name);
			}
		}

		public NewAlbumAction()
		{
			super("Nytt album", MedViewDataHandler.instance().getImageIcon(NEW_IMAGE_ICON_24),

				MedViewDataHandler.instance().getImageIcon(NEW_IMAGE_ICON_16));

			putValue(Action.SHORT_DESCRIPTION, "Skapar ett nytt album");
		}
	}


	// UTILITY CLASSES

	private class MedImagerAlbumFileFilter extends javax.swing.filechooser.FileFilter
	{
		public boolean accept(File f)
		{
			if (f.isDirectory())
			{
				return true;
			}

			String extension = IOUtilities.getFileExtension(f);

			if (extension != null)
			{
				return extension.equals(MEDIMAGER_ALBUM_FILE_EXTENSION);
			}
			else
			{
				return false;
			}
		}

		public String getDescription()
		{
			return "MedImager Album (" + MEDIMAGER_ALBUM_FILE_EXTENSION + ")";
		}
	}
}
