/*
 * @(#)MedSummaryPatientPanel.java
 *
 * $Id: MedSummaryPatientPanel.java,v 1.24 2008/07/29 09:31:59 it2aran Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.util.*;

import medview.common.actions.*;
import medview.common.dialogs.*;

import medview.datahandling.*;

import medview.medsummary.model.*;
import medview.medsummary.model.exceptions.CouldNotRefreshExaminationsException;

import misc.foundation.*;

import misc.gui.actions.*;
import misc.gui.components.*;
import misc.gui.constants.*;
import misc.gui.utilities.*;

public class MedSummaryPatientPanel extends JPanel implements
	MedViewLanguageConstants, MedViewMediaConstants,
	MedSummaryActions, ActionContainer, GUIConstants
{

	public void select(PatientIdentifier pid)
	{
		listPanel.selectEntry(pid);
	}


	public Action getAction(String actionName)
	{
		return (Action) actions.get(actionName);
	}

	public void requestFocus()
	{
		listPanel.requestFocus();
	}



	private void createListPanel(ProgressNotifiable notifiable)
	{
		PatientIdentifier[] initialEntries = null;

		if (mediator.getModel().isExaminationDataLocationSet())
		{
			try
			{
				initialEntries = mediator.getModel().getPatients(notifiable);
			}
			catch (Exception exc)
			{
				mVD.createAndShowErrorDialog(mediator.getParentFrame(), exc.getMessage());

				initialEntries = new PatientIdentifier[0];
			}

			refreshAction.setEnabled(true);
		}
		else
		{
			initialEntries = new PatientIdentifier[0]; // model does not contain valid loc

			refreshAction.setEnabled(false);
		}

		final SearchableListModel listModel = new SearchableListModel(initialEntries);

		ImageIcon searchIcon = mVDH.getImageIcon(SEARCH_IMAGE_ICON_18);

		listPanel = new SearchableListPanel(listModel, searchIcon);

		listPanel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		listPanel.setPreferredSize(listPanel.getMinimumSize());

		listPanel.setOpaque(false);
	}

	private void createEastPanel()
	{
		createAddPatientButton();

		createRemovePatientButton();

		eastPanel = new JPanel(new GridBagLayout());

		eastPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();

		int cCS = COMPONENT_COMPONENT_SPACING;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 1;

		eastPanel.add(Box.createGlue(), gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = 0;
		gbc.insets = new Insets(0,cCS,cCS*2,0);

		eastPanel.add(addPatientButton, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weighty = 0;
		gbc.insets = new Insets(0,cCS,0,0);

		eastPanel.add(removePatientButton, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weighty = 1;

		eastPanel.add(Box.createGlue(), gbc);
	}

	private void createMainPanel()
	{
		mainPanel = new JPanel();

		mainPanel.setLayout(new BorderLayout(0,0));

		mainPanel.setOpaque(false);

		mainPanel.add(listPanel, BorderLayout.CENTER);

		JButton refreshButton = new JButton(refreshAction);

		refreshButton.setPreferredSize(new Dimension(0, BUTTON_HEIGHT_SMALL));

		refreshButton.setIcon(null);

		mainPanel.add(refreshButton, BorderLayout.SOUTH);
	}

	/**
	 * Creates the button used to add patient to tree.
	 */
	private void createAddPatientButton()
	{
		addPatientButton = new JButton(addPatientAction);

		GUIUtilities.attachToolBarButtonSwingFix(addPatientButton);

		addPatientButton.setMargin(new Insets(5,3,5,3));

		addPatientButton.setText("");
	}

	/**
	 * Creates the button used to remove patient from tree.
	 */
	private void createRemovePatientButton()
	{
		removePatientButton = new JButton(removePatientAction);

		GUIUtilities.attachToolBarButtonSwingFix(removePatientButton);

		removePatientButton.setMargin(new Insets(5,3,5,3));

		removePatientButton.setText("");
	}



	private void initActions()
	{
		actions = new HashMap();

		refreshAction = new RefreshAction();

		addPatientAction = new AddPatientAction();

		actions.put(REFRESH_PATIENTS_ACTION, refreshAction);

		actions.put(ADD_PATIENT_ACTION, addPatientAction);

		mediator.registerAction(REFRESH_PATIENTS_ACTION, refreshAction);

		mediator.registerAction(ADD_PATIENT_ACTION, addPatientAction);

		removePatientAction = mediator.getAction(REMOVE_PATIENT_ACTION); // from mediator
	}

	private void layoutPanel(ProgressNotifiable notifiable)
	{
		createListPanel(notifiable);

		createEastPanel();

		createMainPanel();

		setLayout(new BorderLayout());

		add(eastPanel, BorderLayout.EAST);

		add(mainPanel, BorderLayout.CENTER);
	}

	private void initDataListeners()
	{
	}

	private void initModelListeners()
	{
		final MedSummaryModel model = mediator.getModel();

		final SearchableListModel listModel = listPanel.getModel();

		model.addMedSummaryModelListener(new MedSummaryModelAdapter()
		{
			public void patientsChanged(MedSummaryModelEvent e)
			{
				NotifyingRunnable runnable = new NotifyingRunnable()
				{
					public void run()
					{
						try
						{
							PatientIdentifier[] patients = model.getPatients(getNotifiable());

							listModel.setEntries(patients);

							refreshAction.setEnabled(model.isExaminationDataLocationSet());
						}
						catch (Exception exc)
						{
							exc.printStackTrace(); // useful for debugging

							mVD.createAndShowErrorDialog(mediator, exc.getMessage());

							listModel.setEntries(new PatientIdentifier[0]);
						}
					}
				};

				mVD.startProgressMonitoring(mediator.getParentFrame(), runnable);
			}
		});
	}

	private void initListPanelListeners()
	{
		// System.out.println("DEBUG initListPanelListeners");

		listPanel.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e)
			{
				JList jList = (JList) e.getSource();

				boolean emptySelection = jList.isSelectionEmpty();

				addPatientAction.setEnabled(!emptySelection);
			}
		}); 

		listPanel.addListMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2) // double-click
				{
					
					mediator.addPatient((PatientIdentifier)listPanel.getSelectedEntry());
				}
			}

			public void mousePressed(MouseEvent e) { }

			public void mouseReleased(MouseEvent e) { }
		});
	}

	public MedSummaryPatientPanel(MedSummaryFrame mediator)
	{
		this(mediator, null);
	}

	public MedSummaryPatientPanel(MedSummaryFrame mediator, ProgressNotifiable notifiable)
	{
		this.mediator = mediator;

		mVD = MedViewDialogs.instance();

		mVDH = MedViewDataHandler.instance();

		initActions();

		layoutPanel(notifiable);

		initDataListeners();

		initModelListeners();

		initListPanelListeners();
	}

	private MedViewDialogs mVD;

	private MedViewDataHandler mVDH;

	private MedSummaryFrame mediator;

	private SearchableListPanel listPanel;

	private JButton addPatientButton;

	private JButton removePatientButton;

	private Action addPatientAction;

	private Action refreshAction;

	private Action removePatientAction; // from mediator...

	private JPanel eastPanel;

	private JPanel mainPanel;

	private HashMap actions;





	private class AddPatientAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			NotifyingRunnable runnable = new NotifyingRunnable()
			{
				public void run()
				{
					Object[] selEntries = listPanel.getSelectedEntries();

					getNotifiable().setTotal(selEntries.length);

					getNotifiable().setDescription("Adding patients");

					for (int ctr=0; ctr<selEntries.length; ctr++)
					{
						getNotifiable().setCurrent(ctr);

						mediator.addPatient((PatientIdentifier)selEntries[ctr]);
					}

					listPanel.clearSelection(); // clear selection after addition
				}
			};

			mVD.startProgressMonitoring(mediator.getParentFrame(), runnable);
		}

		public AddPatientAction()
		{
			super(ACTION_ADD_PATIENT_LS_PROPERTY, RIGHT_ARROW_IMAGE_ICON);

			setEnabled(false);
		}
	}

	private class RefreshAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			try
			{
				mediator.getModel().refreshExaminations();
			}
			catch (CouldNotRefreshExaminationsException exc)
			{
				mVD.createAndShowErrorDialog(mediator.getParentFrame(), exc.getMessage());
			}
		}

		public RefreshAction()
		{
			super(ACTION_REFRESH_PATIENT_LIST_LS_PROPERTY, REFRESH_IMAGE_ICON_24);

			setEnabled(false);
		}
	}

}
