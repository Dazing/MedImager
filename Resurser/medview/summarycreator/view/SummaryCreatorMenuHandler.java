/*
 * @(#)SummaryCreatorMenuHandler.java
 *
 * $Id: SummaryCreatorMenuHandler.java,v 1.15 2006/04/24 14:16:44 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */
package medview.summarycreator.view;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;

import medview.common.actions.*;
import medview.common.components.menu.*;
import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;
import medview.common.text.*;

import medview.datahandling.*;

import medview.summarycreator.model.*;
import medview.summarycreator.view.settings.*;

import misc.domain.*;

import misc.gui.actions.*;
import misc.gui.constants.*;

public class SummaryCreatorMenuHandler implements
	SummaryCreatorActions, MedViewLanguageConstants, ActionContainer,
	MedViewTextConstants, MedViewMediaConstants
{

	public Action getAction(String name)
	{
		return (Action) actions.get(name);
	}





	public JMenu[] getMenus( )
	{
		JMenu fileMenu = createFileMenu();

		JMenu editMenu = createEditMenu();

		JMenu formatMenu = createFormatMenu();

		JMenu helpMenu = createHelpMenu();

		return new JMenu[] { fileMenu, editMenu, formatMenu, helpMenu };
	}

	private JMenu createFileMenu()
	{
		String fileMenuLS = MENU_ARCHIVE_LS_PROPERTY;

		String fileMenuMneLS = MNEMONIC_MENU_ARCHIVE_LS_PROPERTY;

		String newTemplateMneLS = MNEMONIC_MENU_ITEM_NEW_TEMPLATE_LS_PROPERTY;

		String loadTemplateMneLS = MNEMONIC_MENU_ITEM_OPEN_TEMPLATE_LS_PROPERTY;

		String saveTemplateMneLS = MNEMONIC_MENU_ITEM_SAVE_TEMPLATE_LS_PROPERTY;

		String saveTemplateAsMneLS = MNEMONIC_MENU_ITEM_SAVE_TEMPLATE_AS_LS_PROPERTY;

		String closeTemplateMneLS = MNEMONIC_MENU_ITEM_CLOSE_TEMPLATE_LS_PROPERTY;

		String assTranslatorMneLS = MNEMONIC_MENU_ITEM_ASSOCIATE_TRANSLATOR_LS_PROPERTY;

		String newTranslatorMneLS = MNEMONIC_MENU_ITEM_NEW_TRANSLATOR_LS_PROPERTY;

		String loadTranslatorMneLS = MNEMONIC_MENU_ITEM_CHANGE_TRANSLATOR_LS_PROPERTY;

		String saveTranslatorMneLS = MNEMONIC_MENU_ITEM_SAVE_TRANSLATOR_LS_PROPERTY;

		String saveTranslatorAsMneLS = MNEMONIC_MENU_ITEM_SAVE_TRANSLATOR_AS_LS_PROPERTY;

		String closeTranslatorMneLS = MNEMONIC_MENU_ITEM_CLOSE_TRANSLATOR_LS_PROPERTY;

		String showSettingsMneLS = MNEMONIC_MENU_ITEM_PREFERENCES_LS_PROPERTY;

		String exitSummaryCreatorMneLS = MNEMONIC_MENU_ITEM_EXIT_LS_PROPERTY;


		JMenu fileMenu = new MedViewMenu(fileMenuLS, fileMenuMneLS);

		JMenuItem newTemplateMI = new MedViewMenuItem(newTemplateAction, GUIConstants.NEW_PRIMARY_KEYSTROKE, newTemplateMneLS);

		JMenuItem loadTemplateMI = new MedViewMenuItem(loadTemplateAction, GUIConstants.LOAD_PRIMARY_KEYSTROKE, loadTemplateMneLS);

		JMenuItem saveTemplateMI = new MedViewMenuItem(saveTemplateAction, GUIConstants.SAVE_PRIMARY_KEYSTROKE, saveTemplateMneLS);

		JMenuItem saveTemplateAsMI = new MedViewMenuItem(saveTemplateAsAction, null, saveTemplateAsMneLS);

		JMenuItem closeTemplateMI = new MedViewMenuItem(closeTemplateAction, null, closeTemplateMneLS);

		JMenuItem associateMI = new MedViewMenuItem(associateAction, null, assTranslatorMneLS);

		JMenuItem newTranslatorMI = new MedViewMenuItem(newTranslatorAction, GUIConstants.NEW_SECONDARY_KEYSTROKE, newTranslatorMneLS);

		JMenuItem loadTranslatorMI = new MedViewMenuItem(loadTranslatorAction, GUIConstants.LOAD_SECONDARY_KEYSTROKE, loadTranslatorMneLS);

		JMenuItem saveTranslatorMI = new MedViewMenuItem(saveTranslatorAction, GUIConstants.SAVE_SECONDARY_KEYSTROKE, saveTranslatorMneLS);

		JMenuItem saveTranslatorAsMI = new MedViewMenuItem(saveTranslatorAsAction, null, saveTranslatorAsMneLS);

		JMenuItem closeTranslatorMI = new MedViewMenuItem(closeTranslatorAction, null, closeTranslatorMneLS);

		JMenuItem showSettingsMI = new MedViewMenuItem(showSettingsAction, null, showSettingsMneLS);

		JMenuItem exitSummaryCreatorMI = new MedViewMenuItem(exitSummaryCreatorAction, null, exitSummaryCreatorMneLS);


		fileMenu.add(newTemplateMI);

		fileMenu.add(loadTemplateMI);

		fileMenu.add(saveTemplateMI);

		fileMenu.add(saveTemplateAsMI);

		fileMenu.add(closeTemplateMI);

		fileMenu.add(associateMI);

		fileMenu.addSeparator();

		fileMenu.add(newTranslatorMI);

		fileMenu.add(loadTranslatorMI);

		fileMenu.add(saveTranslatorMI);

		fileMenu.add(saveTranslatorAsMI);

		fileMenu.add(closeTranslatorMI);

		fileMenu.addSeparator();

		fileMenu.add(showSettingsMI);

		fileMenu.addSeparator();

		fileMenu.add(exitSummaryCreatorMI);


		return fileMenu;
	}

	private JMenu createEditMenu()
	{
		JMenu editMenu = new MedViewMenu(MENU_EDIT_LS_PROPERTY, MNEMONIC_MENU_EDIT_LS_PROPERTY);


		editMenu.add(new MedViewMenuItem(cutAction, GUIConstants.CUT_KEYSTROKE, MNEMONIC_MENU_ITEM_CUT_LS_PROPERTY));

		editMenu.add(new MedViewMenuItem(copyAction, GUIConstants.COPY_KEYSTROKE, MNEMONIC_MENU_ITEM_COPY_LS_PROPERTY));

		editMenu.add(new MedViewMenuItem(pasteAction, GUIConstants.PASTE_KEYSTROKE, MNEMONIC_MENU_ITEM_PASTE_LS_PROPERTY));

		editMenu.addSeparator();

		editMenu.add(new MedViewMenuItem(addSectionAction, null, MNEMONIC_MENU_ITEM_NEW_SECTION_LS_PROPERTY));

		editMenu.add(new MedViewMenuItem(renameSectionAction, null, MNEMONIC_MENU_ITEM_RENAME_SECTION_LS_PROPERTY));

		editMenu.add(new MedViewMenuItem(removeSectionAction, null, MNEMONIC_MENU_ITEM_REMOVE_SECTION_LS_PROPERTY));


		return editMenu;
	}


	private JMenu createFormatMenu()
	{
		JMenu formatMenu = new MedViewMenu(MENU_FORMAT_LS_PROPERTY, MNEMONIC_MENU_FORMAT_LS_PROPERTY);


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

		fontStyleMenu.add(new MedViewMenuItem(chooseColorAction, null, MNEMONIC_MENU_ITEM_CHOOSE_COLOR_LS_PROPERTY));


		fontSizeMenu.add(new MedViewToggleMenuItem(fontSize8Action, null));

		fontSizeMenu.add(new MedViewToggleMenuItem(fontSize10Action, null));

		fontSizeMenu.add(new MedViewToggleMenuItem(fontSize11Action, null));

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
		JMenu helpMenu = new MedViewMenu(MENU_HELP_LS_PROPERTY, MNEMONIC_MENU_HELP_LS_PROPERTY);

		helpMenu.add(new MedViewMenuItem(showAboutAction, MNEMONIC_MENU_ITEM_ABOUT_LS_PROPERTY));

		return helpMenu;
	}








	private void initActions()
	{
		actions = new HashMap();


		// create and register local actions

		showAboutAction = new ShowAboutAction();

		showSettingsAction = new ShowSettingsAction();

		exitSummaryCreatorAction = new ExitSummaryCreatorAction();

		actions.put(SHOW_ABOUT_ACTION, showAboutAction);

		actions.put(SHOW_SETTINGS_ACTION, showSettingsAction);

		actions.put(EXIT_SUMMARYCREATOR_ACTION, exitSummaryCreatorAction);

		summaryCreator.registerAction(SHOW_ABOUT_ACTION, showAboutAction);

		summaryCreator.registerAction(SHOW_SETTINGS_ACTION, showSettingsAction);

		summaryCreator.registerAction(EXIT_SUMMARYCREATOR_ACTION, exitSummaryCreatorAction);


		// obtain text edit actions from summaryCreator

		cutAction = summaryCreator.getAction(SEK_CUT_ACTION);

		copyAction = summaryCreator.getAction(SEK_COPY_ACTION);

		pasteAction = summaryCreator.getAction(SEK_PASTE_ACTION);

		chooseColorAction = summaryCreator.getAction(CHOOSE_COLOR_ACTION);


		addSectionAction = summaryCreator.getAction(ADD_SECTION_ACTION);

		removeSectionAction = summaryCreator.getAction(REMOVE_SECTION_ACTION);

		renameSectionAction = summaryCreator.getAction(RENAME_SECTION_ACTION);


		fFSansSerifAction = summaryCreator.getAction(SEK_FONT_FAMILY_SANSSERIF_ACTION);

		fFMonoSpacedAction = summaryCreator.getAction(SEK_FONT_FAMILY_MONOSPACED_ACTION);

		fFSerifAction = summaryCreator.getAction(SEK_FONT_FAMILY_SERIF_ACTION);


		fontBoldAction = summaryCreator.getAction(SEK_BOLD_ACTION);

		fontItalicAction = summaryCreator.getAction(SEK_ITALIC_ACTION);

		fontUnderlineAction = summaryCreator.getAction(SEK_UNDERLINE_ACTION);


		fontSuperScriptAction = summaryCreator.getAction(TEXT_SUPERSCRIPT_ACTION);

		fontSubScriptAction = summaryCreator.getAction(TEXT_SUBSCRIPT_ACTION);

		fontStrikeThroughAction = summaryCreator.getAction(TEXT_STRIKETHROUGH_ACTION);


		fontSize8Action = summaryCreator.getAction(SEK_FONT_SIZE_8_ACTION);

		fontSize10Action = summaryCreator.getAction(SEK_FONT_SIZE_10_ACTION);

		fontSize11Action = summaryCreator.getAction(SEK_FONT_SIZE_11_ACTION);

		fontSize12Action = summaryCreator.getAction(SEK_FONT_SIZE_12_ACTION);

		fontSize14Action = summaryCreator.getAction(SEK_FONT_SIZE_14_ACTION);

		fontSize16Action = summaryCreator.getAction(SEK_FONT_SIZE_16_ACTION);

		fontSize18Action = summaryCreator.getAction(SEK_FONT_SIZE_18_ACTION);

		fontSize24Action = summaryCreator.getAction(SEK_FONT_SIZE_24_ACTION);

		fontSize36Action = summaryCreator.getAction(SEK_FONT_SIZE_36_ACTION);

		fontSize48Action = summaryCreator.getAction(SEK_FONT_SIZE_48_ACTION);


		alignmentLeftAction = summaryCreator.getAction(SEK_LEFT_JUSTIFY_ACTION);

		alignmentCenterAction = summaryCreator.getAction(SEK_CENTER_JUSTIFY_ACTION);

		alignmentRightAction = summaryCreator.getAction(SEK_RIGHT_JUSTIFY_ACTION);


		// obtain other actions from summaryCreator

		newTemplateAction = summaryCreator.getAction(NEW_TEMPLATE_ACTION);

		loadTemplateAction = summaryCreator.getAction(LOAD_TEMPLATE_ACTION);

		saveTemplateAction = summaryCreator.getAction(SAVE_TEMPLATE_ACTION);

		saveTemplateAsAction = summaryCreator.getAction(SAVE_TEMPLATE_AS_ACTION);

		closeTemplateAction = summaryCreator.getAction(CLOSE_TEMPLATE_ACTION);

		associateAction = summaryCreator.getAction(ASSOCIATE_TRANSLATOR_ACTION);

		newTranslatorAction = summaryCreator.getAction(NEW_TRANSLATOR_ACTION);

		loadTranslatorAction = summaryCreator.getAction(LOAD_TRANSLATOR_ACTION);

		saveTranslatorAction = summaryCreator.getAction(SAVE_TRANSLATOR_ACTION);

		saveTranslatorAsAction = summaryCreator.getAction(SAVE_TRANSLATOR_AS_ACTION);

		closeTranslatorAction = summaryCreator.getAction(CLOSE_TRANSLATOR_ACTION);
	}

	private void initSettings()
	{
		// settings

		MedViewDialogs mVD = MedViewDialogs.instance();

		CommandQueue cQ = mVD.getSettingsCommandQueue();

		settingsContentPanels = new SettingsContentPanel[]
		{
			new TemplateSettingsContentPanel(cQ, summaryCreator),

			new VisualSettingsContentPanel(cQ, summaryCreator),

			new DataLocationSettingsContentPanel(cQ, summaryCreator)
		};
	}

	public SummaryCreatorMenuHandler( SummaryCreatorFrame summaryCreator )
	{
		this.summaryCreator = summaryCreator;

		initActions();

		initSettings();
	}

	private SummaryCreatorFrame summaryCreator;

	private SettingsContentPanel[] settingsContentPanels;

	private Action newTemplateAction;

	private Action loadTemplateAction;

	private Action saveTemplateAction;

	private Action saveTemplateAsAction;

	private Action closeTemplateAction;

	private Action associateAction;

	private Action newTranslatorAction;

	private Action loadTranslatorAction;

	private Action saveTranslatorAction;

	private Action saveTranslatorAsAction;

	private Action closeTranslatorAction;

	private Action showAboutAction;

	private Action showSettingsAction;

	private Action exitSummaryCreatorAction;

	private Action chooseColorAction;

	private Action cutAction;

	private Action copyAction;

	private Action pasteAction;

	private Action addSectionAction;

	private Action removeSectionAction;

	private Action renameSectionAction;

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

	private Action fontSize11Action;

	private Action fontSize12Action;

	private Action fontSize14Action;

	private Action fontSize16Action;

	private Action fontSize18Action;

	private Action fontSize24Action;

	private Action fontSize36Action;

	private Action fontSize48Action;

	private Action alignmentLeftAction;

	private Action alignmentCenterAction;

	private Action alignmentRightAction;

	private HashMap actions;








	private class ShowSettingsAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			MedViewDialogs mVD = MedViewDialogs.instance();

			String lS = TITLE_SUMMARYCREATOR_SETTINGS_LS_PROPERTY;

			mVD.createAndShowSettingsDialog(summaryCreator.getParentFrame(), lS, settingsContentPanels);
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

			String titLS = TITLE_ABOUT_SUMMARYCREATOR_LS_PROPERTY;

			String txtLS = OTHER_ABOUT_SUMMARYCREATOR_TEXT_LS_PROPERTY;

			String version = SummaryCreatorConstants.VERSION_STRING;

			mVD.createAndShowAboutDialog(summaryCreator.getParentFrame(), titLS, "SummaryCreator", version, txtLS);
		}

		public ShowAboutAction()
		{
			super(ACTION_ABOUT_SUMMARYCREATOR_LS_PROPERTY);
		}
	}

	private class ExitSummaryCreatorAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			String lS = LABEL_EXIT_SUMMARYCREATOR_LS_PROPERTY;

			String message = this.mVDH.getLanguageString(lS);

			int type = MedViewDialogConstants.YES_NO;

			MedViewDialogs mVD = MedViewDialogs.instance();

			int choice = mVD.createAndShowQuestionDialog(summaryCreator.getParentFrame(), type, message);

			if (choice == MedViewDialogConstants.YES)
			{
				if (summaryCreator.allowsExit()) { System.exit(0); }
			}
		}

		public ExitSummaryCreatorAction()
		{
			super(ACTION_EXIT_LS_PROPERTY);
		}
	}

}
