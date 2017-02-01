/*
 * @(#)TranslatorView.java
 *
 * $Id: TranslatorView.java,v 1.15 2006/04/24 14:16:44 lindahlf Exp $
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
import medview.common.dialogs.*;

import medview.datahandling.*;

import misc.gui.actions.*;
import misc.gui.constants.*;
import misc.gui.utilities.*;

import misc.foundation.io.*;

import se.chalmers.cs.medview.docgen.*;
import se.chalmers.cs.medview.docgen.translator.*;

public class TranslatorView extends JPanel implements
	MedViewLanguageConstants, SummaryCreatorActions, ActionContainer,
	GUIConstants
{

	public Action getAction(String name)
	{
		return (Action) actions.get(name);
	}

	public void displayCard( String identifier )
	{
		cardLayout.show(cardPanel, identifier);
	}

	public void setEnabled(boolean e)
	{
		super.setEnabled(e);

		descriptionLabel.setEnabled(e);

		descriptionPreLabel.setEnabled(e);

		if (!e)
		{
			newTranslatorAction.setEnabled(false);

			loadTranslatorAction.setEnabled(false);

			saveTranslatorAction.setEnabled(false);

			saveTranslatorAsAction.setEnabled(false);

			closeTranslatorAction.setEnabled(false);
		}
		else
		{
			actionState.enterState();
		}
	}

	public TranslatorModelKeeper getModelKeeper( )
	{
		return keeper;
	}

	public void requestUseTranslator(String location)
	{
		if (actionState.allowsLoadTranslator())
		{
			if (location != null)
			{
				try
				{
					keeper.loadTranslatorModel(location);

					actionState.loadTranslatorPerformed();
				}
				catch (se.chalmers.cs.medview.docgen.misc.CouldNotLoadException exc)
				{
					MedViewDialogs.instance().createAndShowErrorDialog(mediator.getParentFrame(), exc.getMessage());
				}
			}
		}
	}


	public boolean allowsExit()
	{
		return actionState.allowsCloseTranslator();
	}


	protected TranslatorState getStateForType(String typeDesc)
	{
		TranslatorState state = (TranslatorState) typeStateMap.get(typeDesc);

		if (state == null)
		{
			return emptyState;
		}

		return state;
	}

	protected TranslatorState getStateForTerm( String term )
	{
		try
		{
			if (DerivedTermHandlerFactory.getDerivedTermHandler().isDerivedTerm(term))
			{
				return getStateForType(DerivedTermHandlerFactory.getDerivedTermHandler().getDerivedTermTypeDescriptor(term));
			}
			else
			{
				return getStateForType(TermHandlerFactory.getTermHandler().getTypeDescriptor(term));

			}
		}
		catch (Exception exc)
		{
			exc.printStackTrace();

			MedViewDialogs.instance().createAndShowErrorDialog(mediator.getParentFrame(), exc.getMessage());

			return emptyState;
		}
	}


	public void setCurrentTerm( String term )
	{
		lastRequestedTerm = term;

		if (keeper.containsTranslatorModel())
		{
			if (term != null)
			{
				this.term = term;

				getStateForTerm(term).displayTerm(term);

				return; // breaks
			}
		}

		this.term = null;

		state = emptyState;

		state.displayTerm(null);

		/* NOTE: so if the translator model
		 * keeper currently does not contain
		 * a translator model, or if the
		 * specified term is null, the panel
		 * to display is the 'empty' panel,
		 * and the term description field is
		 * blanked by the displayTerm() method
		 * when receiving a null argument. */
	}

	public String getCurrentTerm( )
	{
		return term;
	}



	/**
	 * Will try to save the current translator
	 * by showing a save dialog, where the user
	 * is asked to choose the full path of where
	 * to save the current translator model. Will
	 * return false if the user discards the
	 * save dialog.
	 */
	private boolean trySaveAs()
	{
		MedViewDialogs mVD = MedViewDialogs.instance();

		try
		{
			String path = mVD.createAndShowSaveTranslatorDialog(mediator.getParentFrame());

			if (path != null) // null -> dismissed
			{
				keeper.saveTranslatorModel(path);

				return true;
			}
			else
			{
				return false;
			}
		}
		catch (se.chalmers.cs.medview.docgen.misc.CouldNotSaveException e)
		{
			MedViewDialogs.instance().createAndShowErrorDialog(mediator.getParentFrame(), e.getMessage());

			return false;
		}
	}





	private void layoutView( )
	{
		setLayout(new BorderLayout());

		setMinimumSize(new Dimension(365, 365));

		setPreferredSize(new Dimension(440, 440));

		JPanel topPanel = createTopPanel();

		JPanel cardPanel = createCardPanel();

		add(topPanel, BorderLayout.NORTH);

		add(cardPanel, BorderLayout.CENTER);
	}

	private JPanel createTopPanel( )
	{
		GridBagLayout gbl = new GridBagLayout();

		GridBagConstraints gbc = new GridBagConstraints();

		JPanel topPanel = new JPanel(gbl);

		JPanel descPanel = createDescPanel();

		JButton loadButton = createLoadButton();

		JButton newButton = createNewButton();

		JButton saveButton = createSaveButton();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.gridwidth = 3;
		gbc.insets = new Insets(0,5,0,0);
		gbc.fill = GridBagConstraints.BOTH;
		topPanel.add(descPanel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0,0,0,0);
		gbc.fill = GridBagConstraints.BOTH;
		topPanel.add(newButton, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0,0,0,0);
		gbc.fill = GridBagConstraints.BOTH;
		topPanel.add(loadButton, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0,0,0,0);
		gbc.fill = GridBagConstraints.BOTH;
		topPanel.add(saveButton, gbc);

		return topPanel;
	}

	private JPanel createDescPanel()
	{
		JPanel descPanel = new JPanel(new BorderLayout(5,0));

		JLabel descPreLabel = createDescPreLabel();

		JLabel descLabel = createDescLabel();

		descPanel.add(descPreLabel, BorderLayout.WEST);

		descPanel.add(descLabel, BorderLayout.CENTER);

		return descPanel;
	}

	private JPanel createCardPanel()
	{
		cardLayout = new CardLayout();

		cardPanel = new JPanel(cardLayout);

		emptyState.addToCardPanel(cardPanel);

		Enumeration enm = typeStates.elements();

		TranslatorState currState = null;

		while (enm.hasMoreElements())
		{
			currState = (TranslatorState) enm.nextElement();

			currState.addToCardPanel(cardPanel);

			/* NOTE: each state is responsible for
			 * adding it's own view to the card
			 * panel, so that the state subsequently
			 * can ask the panel to display this
			 * view later when a term of the type
			 * the state represents is to be
			 * displayed. */
		}

		return cardPanel;
	}

	private JButton createNewButton()
	{
		String lS = OTHER_NEW_LS_PROPERTY;

		newTranslatorButton = new JButton(newTranslatorAction);

		newTranslatorButton.setText(mVDH.getLanguageString(lS));

		return newTranslatorButton;
	}

	private JButton createLoadButton()
	{
		String lS = OTHER_LOAD_LS_PROPERTY;

		loadTranslatorButton = new JButton(loadTranslatorAction);

		loadTranslatorButton.setText(mVDH.getLanguageString(lS));

		return loadTranslatorButton;
	}

	private JButton createSaveButton()
	{
		String lS = OTHER_SAVE_LS_PROPERTY;

		saveTranslatorButton = new JButton(saveTranslatorAction);

		saveTranslatorButton.setText(mVDH.getLanguageString(lS));

		return saveTranslatorButton;
	}

	private JLabel createDescPreLabel()
	{
		descriptionPreLabel = new JLabel();

		String lS = LABEL_CURRENT_TRANSLATOR_LS_PROPERTY;

		descriptionPreLabel.setText(mVDH.getLanguageString(lS));

		return descriptionPreLabel;
	}

	private JLabel createDescLabel()
	{
		descriptionLabel = GUIUtilities.createSunkLabel("");

		int colorType = TYPE_LIGHTER_PANEL_BACKGROUND_COLOR;

		GUIUtilities.applyLookAndFeelBackground(descriptionLabel, colorType);

		return descriptionLabel;
	}


	private void updateModelDescription(String desc)
	{
		descriptionLabel.setText(desc);
	}

	private void updateModelListener(TranslatorModel oldModel)
	{
		if (oldModel != null)
		{
			oldModel.removeTranslatorModelListener(listener);
		}

		if (model != null)
		{
			model.addTranslatorModelListener(listener);
		}
	}

	/* NOTE: the translator model listener needs
	 * to be updated whenever the model keeper
	 * signals that the translator model has
	 * changed. It is important that the old model's
	 * eventually attached listeners is detached first
	 * though, and the listeners that this class is
	 * responsible for is detached by the method above. */


	/**
	 * The translator view is always in one of
	 * three states - namely 1) 'no translator'
	 * state when no model is being shown by
	 * the view, 2) 'save needed' state when
	 * a translator is shown and it is in need
	 * of save if no information is to be lost
	 * when a state transition occurs that may
	 * cause loss of information in the
	 * translator, and finally 3) 'no save
	 * needed' state when a translator is shown
	 * and it is in no need of save (all changes
	 * have either been saved permanently or the
	 * translator has just been created by means
	 * of the 'new' action). These states are called
	 * 'action' states, which represent a different
	 * concept than the 'translator' states. See the
	 * getAllStates() method description for further
	 * information.
	 */
	private void setActionState(ActionState state)
	{
		actionState = state;

		actionState.enterState();
	}


	private void initSimpleMembers()
	{
		listener = new Listener();

		mVDH = MedViewDataHandler.instance();

		keeper = mediator.getTranslatorModelKeeper();
	}

	private void initModel()
	{
		boolean keepsModel = keeper.containsTranslatorModel();

		model = ((keepsModel) ? keeper.getTranslatorModel() : null);
	}

	private void initActions()
	{
		actions = new HashMap();

		loadTranslatorAction = new LoadTranslatorAction();

		newTranslatorAction = new NewTranslatorAction();

		saveTranslatorAction = new SaveTranslatorAction();

		saveTranslatorAsAction = new SaveTranslatorAsAction();

		closeTranslatorAction = new CloseTranslatorAction();

		actions.put(NEW_TRANSLATOR_ACTION, newTranslatorAction);

		actions.put(LOAD_TRANSLATOR_ACTION, loadTranslatorAction);

		actions.put(SAVE_TRANSLATOR_ACTION, saveTranslatorAction);

		actions.put(SAVE_TRANSLATOR_AS_ACTION, saveTranslatorAsAction);

		actions.put(CLOSE_TRANSLATOR_ACTION, closeTranslatorAction);

		mediator.registerAction(NEW_TRANSLATOR_ACTION, newTranslatorAction);

		mediator.registerAction(LOAD_TRANSLATOR_ACTION, loadTranslatorAction);

		mediator.registerAction(SAVE_TRANSLATOR_ACTION, saveTranslatorAction);

		mediator.registerAction(SAVE_TRANSLATOR_AS_ACTION, saveTranslatorAsAction);

		mediator.registerAction(CLOSE_TRANSLATOR_ACTION, closeTranslatorAction);
	}

	private void initStates()
	{
		initViewStates();

		initActionStates();
	}

	private void initViewStates()
	{
		emptyState = new EmptyState(this);

		typeStates = new Vector();

		typeStates.add(new RegularState(this));

		typeStates.add(new MultipleState(this));

		typeStates.add(new IntervalState(this));

		typeStates.add(new FreeState(this));

		initMapAndInitial();
	}

	private void initMapAndInitial()
	{
		typeStateMap = new HashMap(typeStates.size());

		Enumeration enm = typeStates.elements();

		TranslatorState currState = null;

		while (enm.hasMoreElements())
		{
			currState = (TranslatorState) enm.nextElement();

			currState.addToStateHashMap(typeStateMap);
		}

		state = emptyState; // initial state
	}

	private void initActionStates()
	{
		noTranslatorState = new NoTranslatorState();

		saveNeededState = new SaveNeededState();

		noSaveNeededState = new NoSaveNeededState();

		setActionState(noTranslatorState);
	}

	private void attachListeners()
	{
		keeper.addTranslatorModelKeeperListener(listener);

		if (model != null) { model.addTranslatorModelListener(listener); }
	}

	public TranslatorView( TranslatorViewMediator mediator )
	{
		this.mediator = mediator;

		initSimpleMembers();

		initModel();

		initActions();

		initStates();

		attachListeners();

		layoutView();
	}

	private Listener listener;

	private TranslatorModel model;

	private MedViewDataHandler mVDH;

	private String lastRequestedTerm;

	private TranslatorViewMediator mediator;

	private TranslatorModelKeeper keeper;

	private JButton loadTranslatorButton;

	private JButton newTranslatorButton;

	private JButton saveTranslatorButton;

	private JLabel descriptionPreLabel;

	private JLabel descriptionLabel;

	private TranslatorState state;

	private CardLayout cardLayout;

	private HashMap typeStateMap;

	private JPanel cardPanel;

	private HashMap actions;

	private Vector typeStates;

	private String term;


	private ActionState actionState;

	private ActionState noTranslatorState;

	private ActionState saveNeededState;

	private ActionState noSaveNeededState;


	private Action loadTranslatorAction;

	private Action newTranslatorAction;

	private Action saveTranslatorAction;

	private Action saveTranslatorAsAction;

	private Action closeTranslatorAction;


	private TranslatorState emptyState; // does not map to a type


	private class Listener implements TranslatorModelKeeperListener, TranslatorModelListener
	{
		public void translatorModelChanged(TranslatorModelKeeperEvent e)
		{
			TranslatorModel oldModel = model;

			model = keeper.getTranslatorModel();

			updateModelListener(oldModel); // if null -> simply removes old model listeners

			setCurrentTerm(lastRequestedTerm);

			state.translatorChanged(); // OBS: not the action state
		}

		public void translatorModelLocationChanged(TranslatorModelKeeperEvent e)
		{
			if (!keeper.containsTranslatorModel())
			{
				updateModelDescription("");
			}
			else if (keeper.containsNewTranslatorModel())
			{
				String lS = OTHER_NEW_TRANSLATOR_LS_PROPERTY;

				updateModelDescription(mVDH.getLanguageString(lS));
			}
			else
			{
				String loc = keeper.getTranslatorModelLocation();

				updateModelDescription(mVDH.parseTATDHLocation(loc));

				/* NOTE: each template and translator data
				 * handler implementation has it's own format
				 * for the location of data. Thus, it should
				 * also have it's own way of modifying a location
				 * to a more readable form, which is used when
				 * displaying to the user the currently set
				 * location. The parseTATDHLocation() method is
				 * simply delegated by the data handler to the
				 * current TATDH implementation, and then it in
				 * turn formats the string to what it sees
				 * appropriate. */
			}
		}

		public void translationModelAdded(TranslatorModelEvent e) {}

		public void translationModelRemoved(TranslatorModelEvent e)	{}

		public void translatorModelUpdated(TranslatorModelEvent e)
		{
			actionState.contentChanged();
		}
	}

	public class NewTranslatorAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			if (actionState.allowsNewTranslator())
			{
				try
				{
					keeper.newTranslatorModel();

					actionState.newTranslatorPerformed();
				}
				catch (CouldNotCreateException exc)
				{
					MedViewDialogs.instance().createAndShowErrorDialog(mediator.getParentFrame(), exc.getMessage());
				}
			}
		}

		public NewTranslatorAction()
		{
			super(ACTION_NEW_TRANSLATOR_LS_PROPERTY);
		}
	}

	public class LoadTranslatorAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			if (actionState.allowsLoadTranslator())
			{
				String path = MedViewDialogs.instance().createAndShowLoadTranslatorDialog(mediator.getParentFrame());

				if (path != null)
				{
					try
					{
						keeper.loadTranslatorModel(path);

						actionState.loadTranslatorPerformed();
					}
					catch (se.chalmers.cs.medview.docgen.misc.CouldNotLoadException exc)
					{
						MedViewDialogs.instance().createAndShowErrorDialog(mediator.getParentFrame(), exc.getMessage());
					}
				}
			}
		}

		public LoadTranslatorAction()
		{
			super(ACTION_LOAD_TRANSLATOR_LS_PROPERTY);
		}
	}

	public class SaveTranslatorAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			String tP = keeper.getTranslatorModelLocation();

			if (tP == null)
			{
				saveTranslatorAsAction.actionPerformed(e);
			}
			else
			{
				try
				{
					keeper.saveTranslatorModel(tP);

					actionState.saveTranslatorPerformed();
				}
				catch (se.chalmers.cs.medview.docgen.misc.CouldNotSaveException exc)
				{
					MedViewDialogs.instance().createAndShowErrorDialog(mediator.getParentFrame(), exc.getMessage());
				}
			}
		}

		public SaveTranslatorAction()
		{
			super(ACTION_SAVE_TRANSLATOR_LS_PROPERTY);
		}
	}

	public class SaveTranslatorAsAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			if (trySaveAs()) { actionState.saveTranslatorPerformed(); }
		}

		public SaveTranslatorAsAction()
		{
			super(ACTION_SAVE_TRANSLATOR_AS_LS_PROPERTY);
		}
	}

	public class CloseTranslatorAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			if (actionState.allowsCloseTranslator())
			{
				keeper.clearTranslatorModel(); // will result in notify (see listener)

				actionState.closeTranslatorPerformed(); // switches action state to closed
			}
		}

		public CloseTranslatorAction()
		{
			super(ACTION_CLOSE_TRANSLATOR_LS_PROPERTY);
		}
	}

	private class ActionState
	{
		public void enterState() {}

		public void contentChanged() {}

		public void newTranslatorPerformed() {}

		public void loadTranslatorPerformed() {}

		public void closeTranslatorPerformed() {}

		public void saveTranslatorPerformed() {}

		public void saveTranslatorAsPerformed() {}

		public boolean allowsNewTranslator() { return true; }

		public boolean allowsLoadTranslator() { return true; }

		public boolean allowsCloseTranslator() { return true; }

		protected boolean allowsAfterAsk()
		{
			MedViewDialogs mVD = MedViewDialogs.instance();

			String mess = mVDH.getLanguageString(LABEL_SHOULD_SAVE_TRANSLATOR_LS_PROPERTY);

			int mType = MedViewDialogConstants.YES_NO_CANCEL;

			int ret = mVD.createAndShowQuestionDialog(mediator.getParentFrame(), mType, mess);

			if (ret == MedViewDialogConstants.YES) { return trySaveAs(); }

			if (ret == MedViewDialogConstants.NO) { return true; }

			return false;
		}
	}

	private class NoTranslatorState extends ActionState
	{
		public void enterState()
		{
			newTranslatorAction.setEnabled(true);

			loadTranslatorAction.setEnabled(true);

			saveTranslatorAction.setEnabled(false);

			saveTranslatorAsAction.setEnabled(false);

			closeTranslatorAction.setEnabled(false);
		}

		public void newTranslatorPerformed() { setActionState(saveNeededState); }

		public void loadTranslatorPerformed() { setActionState(noSaveNeededState); }
	}

	private class SaveNeededState extends ActionState
	{
		public void enterState()
		{
			newTranslatorAction.setEnabled(true);

			loadTranslatorAction.setEnabled(true);

			saveTranslatorAction.setEnabled(true);

			saveTranslatorAsAction.setEnabled(true);

			closeTranslatorAction.setEnabled(true);
		}

		public void closeTranslatorPerformed() { setActionState(noTranslatorState); }

		public void loadTranslatorPerformed() { setActionState(noSaveNeededState); }

		public void saveTranslatorPerformed() { setActionState(noSaveNeededState); }

		public void saveTranslatorAsPerformed() { setActionState(noSaveNeededState); }

		public boolean allowsNewTranslator() { return allowsAfterAsk(); }

		public boolean allowsLoadTranslator() { return allowsAfterAsk(); }

		public boolean allowsCloseTranslator() { return allowsAfterAsk(); }
	}

	private class NoSaveNeededState extends ActionState
	{
		public void enterState()
		{
			newTranslatorAction.setEnabled(true);

			loadTranslatorAction.setEnabled(true);

			saveTranslatorAction.setEnabled(false);

			saveTranslatorAsAction.setEnabled(true);

			closeTranslatorAction.setEnabled(true);
		}

		public void newTranslatorPerformed() { setActionState(saveNeededState); }

		public void closeTranslatorPerformed() { setActionState(noTranslatorState); }

		public void contentChanged() { setActionState(saveNeededState); }
	}
}
