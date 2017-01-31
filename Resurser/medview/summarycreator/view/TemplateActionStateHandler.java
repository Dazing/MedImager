/*
 * @(#)TemplateActionStateHandler.java
 *
 * $Id: TemplateActionStateHandler.java,v 1.11 2005/06/03 15:42:18 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import java.awt.event.*;

import java.util.*;

import javax.swing.*;

import medview.common.actions.*;
import medview.common.dialogs.*;
import medview.common.text.*;

import medview.datahandling.*;

import medview.summarycreator.model.*;

import misc.foundation.io.*;

import misc.gui.actions.*;

import se.chalmers.cs.medview.docgen.template.*;

public class TemplateActionStateHandler implements
	MedViewMediaConstants, MedViewLanguageConstants,
	SummaryCreatorActions, SummaryCreatorFlagProperties,
	ActionContainer
{

	public Action getAction(String actionName)
	{
		if (tAC.isMedViewTextAction(actionName))
		{
			return tAC.getAction(actionName);
		}
		else
		{
			return (Action) actions.get(actionName);
		}
	}

	public void setActionEnabledStatus(boolean e)
	{
		if (!e)
		{
			addSectionAction.setEnabled(false);

			removeSectionAction.setEnabled(false);

			closeTemplateAction.setEnabled(false);

			loadTemplateAction.setEnabled(false);

			newTemplateAction.setEnabled(false);

			saveTemplateAction.setEnabled(false);

			saveTemplateAsAction.setEnabled(false);
		}
		else
		{
			templateState.enterState();
		}
	}

	public boolean allowsExit()
	{
		return templateState.allowsCloseTemplate();
	}

	public void contentChanged()
	{
		templateState.contentChanged();
	}

	public void basePaneChanged(JTextPane basePane)
	{
		if (basePane != null)
		{
			tAC.setAttachedTextPane(basePane);

			tAC.enableActions();
		}
		else
		{
			tAC.disableActions();
		}
	}


	private void setTemplateState(TemplateState state)
	{
		templateState = state;

		templateState.enterState();
	}

	private boolean trySaveAs()
	{
		try
		{
			String path = mVD.createAndShowSaveTemplateDialog(mediator);

			if (path != null) // null -> dismissed
			{
				keeper.saveTemplateModel(path);

				return true;
			}
			else
			{
				return false;
			}
		}
		catch (se.chalmers.cs.medview.docgen.misc.CouldNotSaveException e)
		{
			mVD.createAndShowErrorDialog(mediator, e.getMessage());

			return false;
		}
	}

	private void assignBasePaneFocus()
	{
		wrapper.getBasePane().requestFocus();
	}


	private void initSimpleMembers()
	{
		actions = new HashMap();

		mVD = MedViewDialogs.instance();

		mediator = wrapper.getMediator();

		mVDH = MedViewDataHandler.instance();

		tAC = new MedViewTextActionCollection();

		keeper = mediator.getModel();
	}

	private void initActions()
	{
		addSectionAction = new AddSectionAction();

		removeSectionAction = new RemoveSectionAction();

		renameSectionAction = new RenameSectionAction();

		newTemplateAction = new NewTemplateAction();

		loadTemplateAction = new LoadTemplateAction();

		saveTemplateAction = new SaveTemplateAction();

		saveTemplateAsAction = new SaveTemplateAsAction();

		closeTemplateAction = new CloseTemplateAction();


		actions.put(ADD_SECTION_ACTION, addSectionAction);

		actions.put(REMOVE_SECTION_ACTION, removeSectionAction);

		actions.put(RENAME_SECTION_ACTION, renameSectionAction);

		actions.put(NEW_TEMPLATE_ACTION, newTemplateAction);

		actions.put(LOAD_TEMPLATE_ACTION, loadTemplateAction);

		actions.put(SAVE_TEMPLATE_ACTION, saveTemplateAction);

		actions.put(SAVE_TEMPLATE_AS_ACTION, saveTemplateAsAction);

		actions.put(CLOSE_TEMPLATE_ACTION, closeTemplateAction);


		mediator.registerAction(ADD_SECTION_ACTION, addSectionAction);

		mediator.registerAction(REMOVE_SECTION_ACTION, removeSectionAction);

		mediator.registerAction(RENAME_SECTION_ACTION, renameSectionAction);

		mediator.registerAction(NEW_TEMPLATE_ACTION, newTemplateAction);

		mediator.registerAction(LOAD_TEMPLATE_ACTION, loadTemplateAction);

		mediator.registerAction(SAVE_TEMPLATE_ACTION, saveTemplateAction);

		mediator.registerAction(SAVE_TEMPLATE_AS_ACTION, saveTemplateAsAction);

		mediator.registerAction(CLOSE_TEMPLATE_ACTION, closeTemplateAction);


		String[] actionNames = tAC.getContainedActionNames();

		for (int ctr=0; ctr<actionNames.length; ctr++)
		{
			Action currentAction = tAC.getAction(actionNames[ctr]);

			mediator.registerAction(actionNames[ctr], currentAction);
		}
	}

	private void initStates()
	{
		noTemplateState = new NoTemplateState();

		noSaveContainsSectionsState = new NoSaveContainsSectionsState();

		noSaveNoSectionsState = new NoSaveNoSectionsState();

		saveContainsSectionsState = new SaveContainsSectionsState();

		saveNoSectionsState = new SaveNoSectionsState();

		setTemplateState(noTemplateState);
	}

	public TemplateActionStateHandler(TemplateViewWrapper wrapper)
	{
		this.wrapper = wrapper;

		initSimpleMembers();

		initActions();

		initStates();
	}


	private TemplateModelKeeper keeper;

	private TemplateViewWrapper wrapper;

	private SummaryCreatorFrame mediator;

	private MedViewDataHandler mVDH;

	private MedViewDialogs mVD;


	private HashMap actions;

	private Action addSectionAction;

	private Action removeSectionAction;

	private Action renameSectionAction;

	private Action newTemplateAction;

	private Action loadTemplateAction;

	private Action saveTemplateAction;

	private Action saveTemplateAsAction;

	private Action closeTemplateAction;

	private MedViewTextActionCollection tAC;


	private TemplateState noTemplateState;

	private TemplateState noSaveContainsSectionsState;

	private TemplateState noSaveNoSectionsState;

	private TemplateState saveContainsSectionsState;

	private TemplateState saveNoSectionsState;

	private TemplateState templateState;


	private class AddSectionAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			TemplateModel model = wrapper.getModel();

			String[] currSections = model.getAllContainedSections();

			MedViewDialog dialog = mVD.createAddSectionDialog(mediator, currSections);

			dialog.show();

			if (!dialog.wasDismissed())
			{
				Object data = dialog.getObjectData();

				Object[] dataArr = (Object[]) data;

				String after = mVDH.getLanguageString(LABEL_AFTER_LS_PROPERTY);

				boolean isAfter = ((String)dataArr[1]).equalsIgnoreCase(after);

				try
				{
					int sectStart = model.addSection((String)dataArr[0], isAfter, (String)dataArr[2]);

					wrapper.getBasePane().setCaretPosition(sectStart);

					templateState.addSectionPerformed();
				}
				catch (InvalidSectionException exc)
				{
					mVD.createAndShowErrorDialog(mediator, exc.getMessage());
				}
			}
		}

		public AddSectionAction()
		{
			super(ACTION_NEW_SECTION_LS_PROPERTY, NEW_SECTION_IMAGE_ICON_24);
		}
	}

	private class RemoveSectionAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			TemplateModel model = wrapper.getModel();

			Class propClass = SummaryCreatorFlagProperties.class;

			String propName = SummaryCreatorFlagProperties.ASK_BEFORE_REMOVE_SECTION_PROPERTY;

			if (mVDH.getUserBooleanPreference(propName, true, propClass))
			{
				int type = MedViewDialogConstants.YES_NO;

				String askLS =  QUESTION_SHOULD_REMOVE_SECTION_LS_PROPERTY;

				String askString = mVDH.getLanguageString(askLS);

				int choice = mVD.createAndShowQuestionDialog(mediator, type, askString);

				if (choice != MedViewDialogConstants.YES) { return; }
			}

			model.removeSection(wrapper.getBasePane().getCaretPosition());

			templateState.removeSectionPerformed();
		}

		public RemoveSectionAction()
		{
			super(ACTION_REMOVE_SECTION_LS_PROPERTY, REMOVE_SECTION_IMAGE_ICON_24);
		}
	}

	private class RenameSectionAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			TemplateModel model = wrapper.getModel();

			int currOffs = wrapper.getBasePane().getCaretPosition();

			String currSect = model.getSectionName(currOffs);

			MedViewDialog d = mVD.createRenameSectionDialog(mediator, currSect);

			d.show();

			if (!d.wasDismissed())
			{
				String newName = (String) d.getObjectData();

				model.renameSectionAtOffset(currOffs, newName);
			}
		}

		public RenameSectionAction()
		{
			super(ACTION_RENAME_SECTION_LS_PROPERTY);
		}
	}

	private class NewTemplateAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			if (templateState.allowsNewTemplate())
			{
				keeper.newTemplateModel();

				templateState.newTemplatePerformed();
			}
		}

		public NewTemplateAction()
		{
			super(ACTION_NEW_TEMPLATE_LS_PROPERTY, NEW_IMAGE_ICON_24);
		}
	}

	private class LoadTemplateAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			if (templateState.allowsLoadTemplate())
			{
				try
				{
					String filePath = mVD.createAndShowLoadTemplateDialog(mediator);

					if (filePath != null)
					{
						TemplateModelWrapper wrapper = keeper.loadTemplateModel(filePath);

						SwingUtilities.invokeLater(new Runnable()
						{
							public void run()
							{
								templateState.loadTemplatePerformed();

								assignBasePaneFocus();
							}
						});

						if (wrapper.isAssociatedWithTranslator())
						{
							String lSProp = QUESTION_LOAD_ASSOCIATED_TRANSLATOR_LS_PROPERTY;

							int type = MedViewDialogConstants.YES_NO;

							String mess = mVDH.getLanguageString(lSProp);

							int ret = mVD.createAndShowQuestionDialog(mediator, type, mess);

							if (ret == MedViewDialogConstants.YES)
							{
								mediator.requestUseTranslator(wrapper.getAssociatedTranslatorLocation());
							}
						}
					}
				}
				catch (se.chalmers.cs.medview.docgen.misc.CouldNotLoadException exc)
				{
					String m = exc.getMessage();

					mVD.createAndShowErrorDialog(mediator, m);
				}
			}
		}

		public LoadTemplateAction()
		{
			super(ACTION_LOAD_TEMPLATE_LS_PROPERTY, OPEN_IMAGE_ICON_24);
		}
	}

	private class SaveTemplateAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			String tP = keeper.getTemplateModelLocation();

			if (tP == null)
			{
				saveTemplateAsAction.actionPerformed(e);
			}
			else
			{
				try
				{
					keeper.saveTemplateModel(tP);

					templateState.saveTemplatePerformed();

					assignBasePaneFocus();
				}
				catch (se.chalmers.cs.medview.docgen.misc.CouldNotSaveException exc)
				{
					String m = exc.getMessage();

					mVD.createAndShowErrorDialog(mediator, m);
				}
			}
		}

		public SaveTemplateAction()
		{
			super(ACTION_SAVE_TEMPLATE_LS_PROPERTY, SAVE_IMAGE_ICON_24);
		}
	}

	private class SaveTemplateAsAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			if (trySaveAs())
			{
				templateState.saveTemplateAsPerformed();

				assignBasePaneFocus();
			}
		}

		public SaveTemplateAsAction()
		{
			super(ACTION_SAVE_TEMPLATE_AS_LS_PROPERTY);
		}
	}

	private class CloseTemplateAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			if (templateState.allowsCloseTemplate())
			{
				keeper.clearTemplateModel();

				templateState.closeTemplatePerformed();
			}
		}

		public CloseTemplateAction()
		{
			super(ACTION_CLOSE_TEMPLATE_LS_PROPERTY);
		}
	}


	private abstract class TemplateState
	{
		public void enterState() {}

		public void contentChanged() {}


		public void newTemplatePerformed() {}

		public void loadTemplatePerformed() {}

		public void saveTemplatePerformed() {}

		public void saveTemplateAsPerformed() {}

		public void addSectionPerformed() {}

		public void removeSectionPerformed() {}

		public void renameSectionPerformed() {}

		public void closeTemplatePerformed() {}


		public boolean allowsNewTemplate() { return true; }

		public boolean allowsLoadTemplate() { return true; }

		public boolean allowsCloseTemplate() { return true; }


		protected boolean allowsAfterSaveDialog()
		{
			int mType = MedViewDialogConstants.YES_NO_CANCEL;

			String mess = mVDH.getLanguageString(LABEL_SHOULD_SAVE_TEMPLATE_LS_PROPERTY);

			int ret = mVD.createAndShowQuestionDialog(mediator, mType, mess);

			if (ret == MedViewDialogConstants.YES) { return trySaveAs(); }

			if (ret == MedViewDialogConstants.NO) { return true; }

			return false;
		}

		protected boolean hasSections()
		{
			TemplateModel model = wrapper.getModel();

			if (model == null) { return false; }

			return (model.getSectionCount() != 0);
		}

		/* NOTE: the 'allowsXXX()' methods above
		 * are called whenever a transition occurs
		 * that might cause loss of template information,
		 * and that is important that the user first
		 * is asked if he is sure / alternatively if
		 * he wants to save first. Note also that the
		 * state concept in this context is in regard
		 * to what states the template model can be in.
		 * The actions are disabled / enabled and may
		 * or may not be allowed depending on the state
		 * of the model. The actions that do not cause
		 * any state transitions (for example, the
		 * print action) do not notify the state that
		 * they have been performed. */
	}

	private class NoTemplateState extends TemplateState
	{
		public void enterState()
		{
			newTemplateAction.setEnabled(true);

			loadTemplateAction.setEnabled(true);

			saveTemplateAction.setEnabled(false);

			saveTemplateAsAction.setEnabled(false);

			addSectionAction.setEnabled(false);

			removeSectionAction.setEnabled(false);

			renameSectionAction.setEnabled(false);

			closeTemplateAction.setEnabled(false);
		}

		public void newTemplatePerformed()
		{
			setTemplateState(noSaveNoSectionsState);
		}

		public void loadTemplatePerformed()
		{
			if (hasSections())
			{
				setTemplateState(noSaveContainsSectionsState);
			}
			else
			{
				setTemplateState(noSaveNoSectionsState);
			}
		}
	}

	private class NoSaveContainsSectionsState extends TemplateState
	{
		public void enterState()
		{
			newTemplateAction.setEnabled(true);

			loadTemplateAction.setEnabled(true);

			saveTemplateAction.setEnabled(false);

			saveTemplateAsAction.setEnabled(true);

			addSectionAction.setEnabled(true);

			removeSectionAction.setEnabled(true);

			renameSectionAction.setEnabled(true);

			closeTemplateAction.setEnabled(true);
		}

		public void contentChanged()
		{
			setTemplateState(saveContainsSectionsState);
		}

		public void newTemplatePerformed()
		{
			setTemplateState(noSaveNoSectionsState);
		}

		public void loadTemplatePerformed()
		{
			if (!hasSections())
			{
				setTemplateState(noSaveNoSectionsState);
			}
		}

		public void saveTemplateAsPerformed() { }

		public void closeTemplatePerformed()
		{
			setTemplateState(noTemplateState);
		}

		public void addSectionPerformed() { }

		public void removeSectionPerformed()
		{
			if (!hasSections())
			{
				setTemplateState(saveNoSectionsState);
			}
			else
			{
				setTemplateState(saveContainsSectionsState);
			}
		}

		public void renameSectionPerformed()
		{
			setTemplateState(saveContainsSectionsState);
		}
	}

	private class NoSaveNoSectionsState extends TemplateState
	{
		public void enterState()
		{
			newTemplateAction.setEnabled(true);

			loadTemplateAction.setEnabled(true);

			saveTemplateAction.setEnabled(false);

			saveTemplateAsAction.setEnabled(true);

			addSectionAction.setEnabled(true);

			removeSectionAction.setEnabled(false);

			renameSectionAction.setEnabled(false);

			closeTemplateAction.setEnabled(true);
		}

		public void contentChanged()
		{
			setTemplateState(saveNoSectionsState);
		}

		public void newTemplatePerformed() { }

		public void loadTemplatePerformed()
		{
			if (hasSections())
			{
				setTemplateState(noSaveContainsSectionsState);
			}
		}

		public void saveTemplateAsPerformed() { }

		public void closeTemplatePerformed()
		{
			setTemplateState(noTemplateState);
		}

		public void addSectionPerformed()
		{
			setTemplateState(saveContainsSectionsState);
		}
	}

	private class SaveContainsSectionsState extends TemplateState
	{
		public void enterState()
		{
			newTemplateAction.setEnabled(true);

			loadTemplateAction.setEnabled(true);

			saveTemplateAction.setEnabled(true);

			saveTemplateAsAction.setEnabled(true);

			addSectionAction.setEnabled(true);

			removeSectionAction.setEnabled(true);

			renameSectionAction.setEnabled(true);

			closeTemplateAction.setEnabled(true);
		}

		public void newTemplatePerformed()
		{
			setTemplateState(noSaveNoSectionsState);
		}

		public void loadTemplatePerformed()
		{
			if (hasSections())
			{
				setTemplateState(noSaveContainsSectionsState);
			}
			else
			{
				setTemplateState(noSaveNoSectionsState);
			}
		}

		public void saveTemplatePerformed()
		{
			setTemplateState(noSaveContainsSectionsState);
		}

		public void saveTemplateAsPerformed()
		{
			setTemplateState(noSaveContainsSectionsState);
		}

		public void closeTemplatePerformed()
		{
			setTemplateState(noTemplateState);
		}

		public void addSectionPerformed() { }

		public void removeSectionPerformed()
		{
			if (!hasSections())
			{
				setTemplateState(saveNoSectionsState);
			}
		}

		public boolean allowsNewTemplate() { return allowsAfterSaveDialog(); }

		public boolean allowsLoadTemplate() { return allowsAfterSaveDialog(); }

		public boolean allowsCloseTemplate() { return allowsAfterSaveDialog(); }
	}

	private class SaveNoSectionsState extends TemplateState
	{
		public void enterState()
		{
			newTemplateAction.setEnabled(true);

			loadTemplateAction.setEnabled(true);

			saveTemplateAction.setEnabled(true);

			saveTemplateAsAction.setEnabled(true);

			addSectionAction.setEnabled(true);

			removeSectionAction.setEnabled(false);

			renameSectionAction.setEnabled(false);

			closeTemplateAction.setEnabled(true);
		}

		public void newTemplatePerformed()
		{
			setTemplateState(noSaveNoSectionsState);
		}

		public void loadTemplatePerformed()
		{
			if (hasSections())
			{
				setTemplateState(noSaveContainsSectionsState);
			}
			else
			{
				setTemplateState(noSaveNoSectionsState);
			}
		}

		public void saveTemplatePerformed()
		{
			setTemplateState(noSaveNoSectionsState);
		}

		public void saveTemplateAsPerformed()
		{
			setTemplateState(noSaveNoSectionsState);
		}

		public void closeTemplatePerformed()
		{
			setTemplateState(noTemplateState);
		}

		public void addSectionPerformed()
		{
			setTemplateState(saveContainsSectionsState);
		}

		public boolean allowsNewTemplate() { return allowsAfterSaveDialog(); }

		public boolean allowsLoadTemplate() { return allowsAfterSaveDialog(); }

		public boolean allowsCloseTemplate() { return allowsAfterSaveDialog(); }
	}

}
