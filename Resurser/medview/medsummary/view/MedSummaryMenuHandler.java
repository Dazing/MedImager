/*
 * @(#)MedSummaryMenuHandler.java
 *
 * $Id: MedSummaryMenuHandler.java,v 1.21 2006/11/15 22:34:55 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

import medview.common.actions.*;
import medview.common.components.menu.*;
import medview.common.dialogs.*;
import medview.common.text.*;

import medview.datahandling.*;
import medview.medsummary.model.*;

import misc.gui.actions.*;
import misc.gui.constants.*;

public class MedSummaryMenuHandler implements ActionContainer,
	MedViewLanguageConstants, MedSummaryActions, MedViewTextConstants,
	MedViewMediaConstants, MedSummaryFlagProperties, MedSummaryConstants,
	MedSummaryUserProperties
{
	// OBTAINING ACTIONS

	public Action getAction(String actionID)
	{
		return (Action) actions.get(actionID);
	}


	// OBTAINING (AND CREATION OF) MENUS

	public JMenu[] getMenus()
	{
		createFileMenu();

		createEditMenu();

		createViewMenu();

		createFormatMenu();

		createHelpMenu();

		return new JMenu[] { fileMenu, editMenu, viewMenu, formatMenu, helpMenu };
	}

	private JMenu createFileMenu()
	{
		fileMenu = new MedViewMenu(MENU_ARCHIVE_LS_PROPERTY, MNEMONIC_MENU_ARCHIVE_LS_PROPERTY);

		fileMenu.add(new MedViewMenuItem(changeDataLocationAction, MNEMONIC_MENU_ITEM_CHANGE_DATA_LOCATION_LS_PROPERTY));

		fileMenu.add(new MedViewMenuItem(closeGeneratedPageAction, MNEMONIC_MENU_ITEM_CLOSE_GENERATED_PAGE_LS_PROPERTY));

		fileMenu.addSeparator();

		fileMenu.add(new MedViewMenuItem(saveAsHTMLAction, MNEMONIC_MENU_ITEM_SAVE_AS_HTML_LS_PROPERTY));

		fileMenu.add(new MedViewMenuItem(saveAsRTFAction, MNEMONIC_MENU_ITEM_SAVE_AS_RTF_LS_PROPERTY));

		fileMenu.addSeparator();

		fileMenu.add(new MedViewMenuItem(refreshAction, MNEMONIC_MENU_ITEM_REFRESH_LS_PROPERTY));

		fileMenu.add(new MedViewMenuItem(newPatientAction, MNEMONIC_MENU_ITEM_NEW_PATIENT_LS_PROPERTY));

		fileMenu.add(new MedViewMenuItem(newDayNoteAction, MNEMONIC_MENU_ITEM_NEW_DAYNOTE_LS_PROPERTY));

		fileMenu.add(new MedViewMenuItem(pdaExportAction, MNEMONIC_MENU_ITEM_PDA_EXPORT_LS_PROPERTY));

		fileMenu.addSeparator();

		fileMenu.add(new MedViewMenuItem(printJournalAction, MNEMONIC_MENU_ITEM_PRINT_LS_PROPERTY));

		fileMenu.addSeparator();

		fileMenu.add(new MedViewMenuItem(showSettingsAction, MNEMONIC_MENU_ITEM_PREFERENCES_LS_PROPERTY));

		fileMenu.addSeparator();

		fileMenu.add(new MedViewMenuItem(exitMedSummaryAction, MNEMONIC_MENU_ITEM_EXIT_LS_PROPERTY));

		return fileMenu;
	}

	private JMenu createEditMenu()
	{
		// construct edit menu

		editMenu = new MedViewMenu(MENU_EDIT_LS_PROPERTY, MNEMONIC_MENU_EDIT_LS_PROPERTY);

		// add basic menu items

		editMenu.add(new MedViewMenuItem(cutAction, GUIConstants.CUT_KEYSTROKE, MNEMONIC_MENU_ITEM_CUT_LS_PROPERTY));

		editMenu.add(new MedViewMenuItem(copyAction, GUIConstants.COPY_KEYSTROKE, MNEMONIC_MENU_ITEM_COPY_LS_PROPERTY));

		editMenu.add(new MedViewMenuItem(pasteAction, GUIConstants.PASTE_KEYSTROKE, MNEMONIC_MENU_ITEM_PASTE_LS_PROPERTY));

		editMenu.addSeparator();

		editMenu.add(new MedViewMenuItem(generateSummaryAction, MNEMONIC_MENU_ITEM_GENERATE_SUMMARY_LS_PROPERTY));

		// return edit menu

		return editMenu;
	}

	private JMenu createViewMenu()
	{
		// create menu

		viewMenu = new MedViewMenu(MENU_VIEW_LS_PROPERTY, MNEMONIC_MENU_VIEW_LS_PROPERTY);

		// 'show chosen database'

		viewMenu.add(new MedViewCheckBoxMenuItem(CHECKBOX_MENU_ITEM_SHOW_DATA_LOCATION_LS_PROPERTY,

			DISPLAY_DATA_LOCATION_COMBO_IN_TOOLBAR_PROPERTY, true, MedSummaryFlagProperties.class)); // true = default

		// 'show available sections'

		viewMenu.add(new MedViewCheckBoxMenuItem(CHECKBOX_MENU_ITEM_SHOW_SECTIONS_LS_PROPERTY,

			DISPLAY_SECTIONS_COMBO_IN_TOOLBAR_PROPERTY, true, MedSummaryFlagProperties.class)); // true = default

		// viewMenu.addSeparator();

		// 'show surrounding templates'

		viewMenu.add(new MedViewCheckBoxMenuItem(CHECKBOX_MENU_ITEM_SHOW_GRAPHIC_TEMPLATE_LS_PROPERTY,

			DISPLAY_GRAPHIC_TEMPLATE_COMBO_IN_TOOLBAR_PROPERTY, true, MedSummaryFlagProperties.class)); // true = default

		viewMenu.addSeparator();
		
		// show fass menu
		viewMenu.add(new MedViewMenuItem(invokeFASSAction, MNEMONIC_MENU_ITEM_OPEN_FASS_LS_PROPERTY));

		// show graph menu
		viewMenu.add(new MedViewMenuItem(showGraphAction, MNEMONIC_MENU_ITEM_SHOW_GRAPH_LS_PROPERTY));

		viewMenu.addSeparator();

		// choose look-and-feel

		lookAndFeelSubMenu = new MedViewMenu(MENU_LOOK_AND_FEEL_LS_PROPERTY, MNEMONIC_MENU_LOOK_AND_FEEL_LS_PROPERTY);

		final UIManager.LookAndFeelInfo[] lAFs = UIManager.getInstalledLookAndFeels();

		ButtonGroup buttonGroup = new ButtonGroup();

		for (int ctr=0; ctr<lAFs.length; ctr++)
		{
			JCheckBoxMenuItem lAFMenuItem = new JCheckBoxMenuItem(lAFs[ctr].getName(),

				UIManager.getLookAndFeel().getClass().getName().equals(lAFs[ctr].getClassName()));

			final String className = lAFs[ctr].getClassName();

			lAFMenuItem.addItemListener(new ItemListener()
			{
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange() == ItemEvent.SELECTED)
					{
						try
						{
							UIManager.setLookAndFeel(className);

							mVDH.setUserStringPreference(LOOK_AND_FEEL_PROPERTY,

								className, MedSummaryUserProperties.class);
						}
						catch (Exception exc)
						{
							exc.printStackTrace();
						}
					}
				}
			});

			buttonGroup.add(lAFMenuItem);

			lookAndFeelSubMenu.add(lAFMenuItem);
		}

		viewMenu.add(lookAndFeelSubMenu);

		// return created menu

		return viewMenu;
	}

	private JMenu createFormatMenu()
	{
		formatMenu = new MedViewMenu(MENU_FORMAT_LS_PROPERTY, MNEMONIC_MENU_FORMAT_LS_PROPERTY);


		JMenu fontFamilyMenu = new MedViewMenu(MENU_FONT_FAMILY_LS_PROPERTY, MNEMONIC_MENU_FAMILY_LS_PROPERTY);

		JMenu fontStyleMenu = new MedViewMenu(MENU_FONT_STYLE_LS_PROPERTY, MNEMONIC_MENU_STYLE_LS_PROPERTY);

		JMenu fontSizeMenu = new MedViewMenu(MENU_FONT_SIZE_LS_PROPERTY, MNEMONIC_MENU_SIZE_LS_PROPERTY);

		JMenu alignmentMenu = new MedViewMenu(MENU_ALIGNMENT_LS_PROPERTY, MNEMONIC_MENU_ALIGNMENT_LS_PROPERTY);


		fontFamilyMenu.add(new MedViewToggleMenuItem(fFSansSerifAction, MNEMONIC_MENU_ITEM_SANSSERIF_LS_PROPERTY));

		fontFamilyMenu.add(new MedViewToggleMenuItem(fFMonoSpacedAction, MNEMONIC_MENU_ITEM_MONOSPACED_LS_PROPERTY));

		fontFamilyMenu.add(new MedViewToggleMenuItem(fFSerifAction, MNEMONIC_MENU_ITEM_SERIF_LS_PROPERTY));


		fontStyleMenu.add(new MedViewToggleMenuItem(fontBoldAction, MNEMONIC_MENU_ITEM_BOLD_LS_PROPERTY));

		fontStyleMenu.add(new MedViewToggleMenuItem(fontItalicAction, MNEMONIC_MENU_ITEM_ITALIC_LS_PROPERTY));

		fontStyleMenu.add(new MedViewToggleMenuItem(fontUnderlineAction, MNEMONIC_MENU_ITEM_UNDERLINE_LS_PROPERTY));

		fontStyleMenu.addSeparator();

		fontStyleMenu.add(new MedViewToggleMenuItem(fontSuperScriptAction, MNEMONIC_MENU_ITEM_SUPERSCRIPT_LS_PROPERTY));

		fontStyleMenu.add(new MedViewToggleMenuItem(fontSubScriptAction, MNEMONIC_MENU_ITEM_SUBSCRIPT_LS_PROPERTY));

		fontStyleMenu.add(new MedViewToggleMenuItem(fontStrikeThroughAction, MNEMONIC_MENU_ITEM_STRIKETHROUGH_LS_PROPERTY));

		fontStyleMenu.addSeparator();

		fontStyleMenu.add(new MedViewMenuItem(chooseColorAction, MNEMONIC_MENU_ITEM_CHOOSE_COLOR_LS_PROPERTY));


		fontSizeMenu.add(new MedViewToggleMenuItem(fontSize8Action, null));

		fontSizeMenu.add(new MedViewToggleMenuItem(fontSize10Action, null));

		fontSizeMenu.add(new MedViewToggleMenuItem(fontSize12Action, null));

		fontSizeMenu.add(new MedViewToggleMenuItem(fontSize14Action, null));

		fontSizeMenu.add(new MedViewToggleMenuItem(fontSize16Action, null));

		fontSizeMenu.add(new MedViewToggleMenuItem(fontSize18Action, null));

		fontSizeMenu.add(new MedViewToggleMenuItem(fontSize24Action, null));

		fontSizeMenu.add(new MedViewToggleMenuItem(fontSize36Action, null));

		fontSizeMenu.add(new MedViewToggleMenuItem(fontSize48Action, null));


		alignmentMenu.add(new MedViewToggleMenuItem(alignmentLeftAction, MNEMONIC_MENU_ITEM_LEFT_LS_PROPERTY));

		alignmentMenu.add(new MedViewToggleMenuItem(alignmentCenterAction, MNEMONIC_MENU_ITEM_CENTER_LS_PROPERTY));

		alignmentMenu.add(new MedViewToggleMenuItem(alignmentRightAction, MNEMONIC_MENU_ITEM_RIGHT_LS_PROPERTY));


		formatMenu.add(fontFamilyMenu);

		formatMenu.add(fontStyleMenu);

		formatMenu.add(fontSizeMenu);

		formatMenu.addSeparator();

		formatMenu.add(alignmentMenu);


		return formatMenu;
	}


	private JMenu createHelpMenu()
	{
		helpMenu = new MedViewMenu(MENU_HELP_LS_PROPERTY, MNEMONIC_MENU_HELP_LS_PROPERTY);

		helpMenu.add(new MedViewMenuItem(showAboutAction, MNEMONIC_MENU_ITEM_ABOUT_LS_PROPERTY));

		return helpMenu;
	}


	// CONSTRUCTOR AND RELATED METHODS

	private void initActions()
	{
		actions = new HashMap();

		showAboutAction = new ShowAboutAction();

		showSettingsAction = new ShowSettingsAction();

		exitMedSummaryAction = new ExitMedSummaryAction();

		actions.put(SHOW_ABOUT_ACTION, showAboutAction);

		actions.put(SHOW_SETTINGS_ACTION, showSettingsAction);

		actions.put(EXIT_MEDSUMMARY_ACTION, exitMedSummaryAction);

		medSummary.registerAction(SHOW_ABOUT_ACTION, showAboutAction);

		medSummary.registerAction(SHOW_SETTINGS_ACTION, showSettingsAction);

		medSummary.registerAction(EXIT_MEDSUMMARY_ACTION, exitMedSummaryAction);


		invokeFASSAction = medSummary.getAction(INVOKE_FASS_ACTION);

		pdaExportAction = medSummary.getAction(PDA_EXPORT_ACTION);

		showGraphAction = medSummary.getAction(SHOW_GRAPH_ACTION);
		
		changeDataLocationAction = medSummary.getAction(CHANGE_DATA_LOCATION_ACTION);

		closeGeneratedPageAction = medSummary.getAction(CLOSE_GENERATED_PAGE_ACTION);

		saveAsRTFAction = medSummary.getAction(SAVE_AS_RTF_ACTION);

		saveAsHTMLAction = medSummary.getAction(SAVE_AS_HTML_ACTION);

		newDayNoteAction = medSummary.getAction(NEW_DAYNOTE_ACTION);

		newPatientAction = medSummary.getAction(NEW_PATIENT_ACTION);

		refreshAction = medSummary.getAction(REFRESH_PATIENTS_ACTION);

		generateSummaryAction = medSummary.getAction(GENERATE_SUMMARY_ACTION);

		printJournalAction = medSummary.getAction(PRINT_JOURNAL_ACTION);

		chooseColorAction = medSummary.getAction(CHOOSE_COLOR_ACTION);


		cutAction = medSummary.getAction(SEK_CUT_ACTION);

		copyAction = medSummary.getAction(SEK_COPY_ACTION);

		pasteAction = medSummary.getAction(SEK_PASTE_ACTION);


		fFSansSerifAction = medSummary.getAction(SEK_FONT_FAMILY_SANSSERIF_ACTION);

		fFMonoSpacedAction = medSummary.getAction(SEK_FONT_FAMILY_MONOSPACED_ACTION);

		fFSerifAction = medSummary.getAction(SEK_FONT_FAMILY_SERIF_ACTION);


		fontBoldAction = medSummary.getAction(SEK_BOLD_ACTION);

		fontItalicAction = medSummary.getAction(SEK_ITALIC_ACTION);

		fontUnderlineAction = medSummary.getAction(SEK_UNDERLINE_ACTION);


		fontSuperScriptAction = medSummary.getAction(TEXT_SUPERSCRIPT_ACTION);

		fontSubScriptAction = medSummary.getAction(TEXT_SUBSCRIPT_ACTION);

		fontStrikeThroughAction = medSummary.getAction(TEXT_STRIKETHROUGH_ACTION);


		fontSize8Action = medSummary.getAction(SEK_FONT_SIZE_8_ACTION);

		fontSize10Action = medSummary.getAction(SEK_FONT_SIZE_10_ACTION);

		fontSize12Action = medSummary.getAction(SEK_FONT_SIZE_12_ACTION);

		fontSize14Action = medSummary.getAction(SEK_FONT_SIZE_14_ACTION);

		fontSize16Action = medSummary.getAction(SEK_FONT_SIZE_16_ACTION);

		fontSize18Action = medSummary.getAction(SEK_FONT_SIZE_18_ACTION);

		fontSize24Action = medSummary.getAction(SEK_FONT_SIZE_24_ACTION);

		fontSize36Action = medSummary.getAction(SEK_FONT_SIZE_36_ACTION);

		fontSize48Action = medSummary.getAction(SEK_FONT_SIZE_48_ACTION);


		alignmentLeftAction = medSummary.getAction(SEK_LEFT_JUSTIFY_ACTION);

		alignmentCenterAction = medSummary.getAction(SEK_CENTER_JUSTIFY_ACTION);

		alignmentRightAction = medSummary.getAction(SEK_RIGHT_JUSTIFY_ACTION);
	}

	public MedSummaryMenuHandler(MedSummaryFrame medSummary)
	{
		this.medSummary = medSummary;

		initActions();
	}

	private MedViewDataHandler mVDH = MedViewDataHandler.instance();

	private MedSummaryFrame medSummary;


	private JMenu fileMenu;

	private JMenu editMenu;

	private JMenu viewMenu;

	private JMenu formatMenu;

	private JMenu helpMenu;

	private JMenu lookAndFeelSubMenu;


	private HashMap actions;


	private Action invokeFASSAction;

	private Action pdaExportAction;

	private Action showGraphAction;

	private Action changeDataLocationAction;

	private Action closeGeneratedPageAction;

	private Action showAboutAction;

	private Action showSettingsAction;

	private Action exitMedSummaryAction;


	private Action saveAsRTFAction;

	private Action saveAsHTMLAction;

	private Action generateSummaryAction;

	private Action newDayNoteAction;
	
	private Action newPatientAction;

	private Action refreshAction;

	private Action printJournalAction;


	private Action cutAction;

	private Action copyAction;

	private Action pasteAction;

	private Action chooseColorAction;


	private Action fFSansSerifAction;

	private Action fFMonoSpacedAction;

	private Action fFSerifAction;


	private Action fontBoldAction;

	private Action fontItalicAction;

	private Action fontUnderlineAction;


	private Action fontSuperScriptAction;

	private Action fontSubScriptAction;

	private Action fontStrikeThroughAction;


	private Action fontSize8Action;

	private Action fontSize10Action;

	private Action fontSize12Action;

	private Action fontSize14Action;

	private Action fontSize16Action;

	private Action fontSize18Action;

	private Action fontSize24Action;

	private Action fontSize36Action;

	private Action fontSize48Action;


	private Action alignmentLeftAction;

	private Action alignmentRightAction;

	private Action alignmentCenterAction;


	// CUSTOM ACTIONS

	private class ShowSettingsAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			medSummary.showSettingsDialog();
		}

		public ShowSettingsAction()
		{
			super(ACTION_SETTINGS_LS_PROPERTY);
		}
	}

	private class ShowAboutAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			MedViewDialogs mVD = MedViewDialogs.instance();

			String titLS = TITLE_ABOUT_MEDSUMMARY_LS_PROPERTY;

			String txtLS = OTHER_ABOUT_MEDSUMMARY_TEXT_LS_PROPERTY;

			String version = MedSummaryConstants.VERSION_STRING;

			mVD.createAndShowAboutDialog(medSummary.getParentFrame(), titLS, "MedSummary", version, txtLS);
		}

		public ShowAboutAction()
		{
			super(ACTION_ABOUT_MEDSUMMARY_LS_PROPERTY);
		}
	}

	private class ExitMedSummaryAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			medSummary.tryShutDown();
		}

		public ExitMedSummaryAction()
		{
			super(ACTION_EXIT_LS_PROPERTY);
		}
	}

}
