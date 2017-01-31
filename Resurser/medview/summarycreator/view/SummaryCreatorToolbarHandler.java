/*
 * @(#)SummaryCreatorToolbarHandler.java
 *
 * $Id: SummaryCreatorToolbarHandler.java,v 1.8 2004/12/08 14:46:26 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import java.util.*;

import javax.swing.*;

import medview.common.components.toolbar.*;
import medview.common.text.*;

import medview.datahandling.*;

import misc.gui.actions.*;
import misc.gui.constants.*;
import misc.gui.utilities.*;

public class SummaryCreatorToolbarHandler implements
	ActionContainer, SummaryCreatorActions, MedViewLanguageConstants,
	MedViewTextConstants
{

	public Action getAction(String actionName)
	{
		return (Action) actions.get(actionName);
	}








	public JToolBar[] getToolbars()
	{
		JToolBar templateToolbar = createTemplateToolbar();

		JToolBar textEditToolbar = createTextEditToolbar();

		GUIUtilities.adjustToolbarHeight(templateToolbar, GUIConstants.TOOLBAR_HEIGHT_NORMAL);

		GUIUtilities.adjustToolbarHeight(textEditToolbar, GUIConstants.TOOLBAR_HEIGHT_NORMAL);

		return new JToolBar[] { templateToolbar, textEditToolbar };
	}

	private JToolBar createTemplateToolbar()
	{
		JToolBar templateToolbar = new MedViewToolBar(TITLE_TEMPLATE_TOOLBAR_LS_PROPERTY);

		templateToolbar.add(new MedViewToolBarNormalButton(newTemplateAction));

		templateToolbar.add(new MedViewToolBarNormalButton(loadTemplateAction));

		templateToolbar.add(new MedViewToolBarNormalButton(saveTemplateAction));

		templateToolbar.addSeparator();

		templateToolbar.add(new MedViewToolBarNormalButton(associateAction));

		templateToolbar.addSeparator();

		templateToolbar.add(new MedViewToolBarNormalButton(addSectionAction));

		templateToolbar.add(new MedViewToolBarNormalButton(removeSectionAction));

		templateToolbar.addSeparator();

		templateToolbar.add(new MedViewToolBarNormalButton(previewJournalAction));

		return templateToolbar;
	}

	private JToolBar createTextEditToolbar()
	{
		JToolBar textEditToolbar = new MedViewToolBar(TITLE_TEXT_TOOLBAR_LS_PROPERTY);

		textEditToolbar.add(new MedViewToolBarNormalButton(summaryCreator.getAction(SEK_CUT_ACTION)));

		textEditToolbar.add(new MedViewToolBarNormalButton(summaryCreator.getAction(SEK_COPY_ACTION)));

		textEditToolbar.add(new MedViewToolBarNormalButton(summaryCreator.getAction(SEK_PASTE_ACTION)));

		textEditToolbar.addSeparator();

		textEditToolbar.add(new MedViewToolBarNormalButton(summaryCreator.getAction(CHOOSE_COLOR_ACTION)));

		textEditToolbar.addSeparator();

		textEditToolbar.add(new MedViewToolBarToggleButton(summaryCreator.getAction(SEK_BOLD_ACTION)));

		textEditToolbar.add(new MedViewToolBarToggleButton(summaryCreator.getAction(SEK_ITALIC_ACTION)));

		textEditToolbar.add(new MedViewToolBarToggleButton(summaryCreator.getAction(SEK_UNDERLINE_ACTION)));

		textEditToolbar.addSeparator();

		textEditToolbar.add(new MedViewToolBarToggleButton(summaryCreator.getAction(TEXT_SUPERSCRIPT_ACTION)));

		textEditToolbar.add(new MedViewToolBarToggleButton(summaryCreator.getAction(TEXT_SUBSCRIPT_ACTION)));

		textEditToolbar.add(new MedViewToolBarToggleButton(summaryCreator.getAction(TEXT_STRIKETHROUGH_ACTION)));

		textEditToolbar.addSeparator();

		textEditToolbar.add(new MedViewToolBarToggleButton(summaryCreator.getAction(SEK_LEFT_JUSTIFY_ACTION)));

		textEditToolbar.add(new MedViewToolBarToggleButton(summaryCreator.getAction(SEK_CENTER_JUSTIFY_ACTION)));

		textEditToolbar.add(new MedViewToolBarToggleButton(summaryCreator.getAction(SEK_RIGHT_JUSTIFY_ACTION)));

		return textEditToolbar;
	}





	private void initSimpleMembers()
	{
		mVDH = MedViewDataHandler.instance();
	}

	private void initActions()
	{
		actions = new HashMap();

		newTemplateAction = summaryCreator.getAction(NEW_TEMPLATE_ACTION);

		loadTemplateAction = summaryCreator.getAction(LOAD_TEMPLATE_ACTION);

		saveTemplateAction = summaryCreator.getAction(SAVE_TEMPLATE_ACTION);

		associateAction = summaryCreator.getAction(ASSOCIATE_TRANSLATOR_ACTION);

		previewJournalAction = summaryCreator.getAction(PREVIEW_JOURNAL_ACTION);

		addSectionAction = summaryCreator.getAction(ADD_SECTION_ACTION);

		removeSectionAction = summaryCreator.getAction(REMOVE_SECTION_ACTION);
	}

	public SummaryCreatorToolbarHandler(SummaryCreatorFrame summaryCreator)
	{
		this.summaryCreator = summaryCreator;

		initSimpleMembers();

		initActions();
	}

	private HashMap actions;

	private MedViewDataHandler mVDH;

	private SummaryCreatorFrame summaryCreator;

	private Action associateAction;

	private Action newTemplateAction;

	private Action loadTemplateAction;

	private Action saveTemplateAction;

	private Action previewJournalAction;

	private Action addSectionAction;

	private Action removeSectionAction;

}
