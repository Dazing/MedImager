package medview.medserver.view;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.util.*;

import javax.swing.*;

import medview.common.components.*;
import medview.common.dialogs.*;

import medview.datahandling.*;

import medview.medserver.model.*;

import misc.foundation.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

/**
 * A panel containing information about the distributed
 * examination data.
 *
 * @author Fredrik Lindahl
 */
public class MedServerExaminationPanel extends JPanel implements
	MedViewLanguageConstants
{

	private void updatePatientList(final ProgressNotifiable pN)
	{
		if (!medServerModel.isExaminationDataLocationSet())
		{
			distExamField.setText("");

			clearInnerExamPanel();

			enableInnerExamPanel(false);
		}
		else
		{
			try
			{
				new Thread(new Runnable()
				{
					private PatientIdentifier[] patients;

					public void run()
					{
						try
						{
							if (pN == null)
							{
								patients = obtainPatientsWithDefaultProgress();
							}
							else
							{
								patients = medServerModel.getPatients(pN); // -> CouldNotRetrievePatientsException
							}

							SwingUtilities.invokeLater(new Runnable()
							{
								public void run()
								{
									try
									{
										int examinationCounter = 0;

										availablePatientsListModel.clear();

										if (patients.length > 1)
										{
											Arrays.sort(patients);
										}

										for (int ctr = 0; ctr < patients.length; ctr++)
										{
											availablePatientsListModel.addElement(patients[ctr]);

											examinationCounter += medServerModel.getExaminations(patients[ctr]).length; // -> CouldNotRetrieveExaminationsException
										}

										totalExaminationsLabel.setText(examinationCounter + "");

										totalPatientsLabel.setText(patients.length + "");

										enableInnerExamPanel(true);
									}
									catch (final CouldNotRetrieveExaminationsException exc)
									{
										exc.printStackTrace();

										SwingUtilities.invokeLater(new Runnable()
										{
											public void run()
											{
												mVD.createAndShowErrorDialog(medServerFrame, exc.getMessage());
											}
										});
									}
								}
							});
						}
						catch (final CouldNotRetrievePatientsException exc)
						{
							exc.printStackTrace();

							SwingUtilities.invokeLater(new Runnable()
							{
								public void run()
								{
									mVD.createAndShowErrorDialog(medServerFrame, exc.getMessage());
								}
							});
						}
					}
				}).start();


				PatientIdentifier[] patients;

				if (pN == null)
				{
					patients = obtainPatientsWithDefaultProgress();
				}
				else
				{
					patients = medServerModel.getPatients(pN);
				}

				int examinationCounter = 0;

				availablePatientsListModel.clear();

				if (patients.length > 1) { Arrays.sort(patients); }

				for (int ctr=0; ctr<patients.length; ctr++)
				{
					availablePatientsListModel.addElement(patients[ctr]);

					examinationCounter += medServerModel.getExaminations(patients[ctr]).length;
				}

				totalExaminationsLabel.setText(examinationCounter + "");

				totalPatientsLabel.setText(patients.length + "");

				enableInnerExamPanel(true);
			}
			catch (Exception e)
			{
				mVD.createAndShowErrorDialog(medServerFrame, e.getMessage());
			}
		}
	}

	private PatientIdentifier[] obtainPatientsWithDefaultProgress()
	{
		class PatientNotifyingRunnable extends NotifyingRunnable
		{
			public void run()
			{
				try
				{
					patients = medServerModel.getPatients(getNotifiable());
				}
				catch (final Exception exc)
				{
					SwingUtilities.invokeLater(new Runnable()
					{
						public void run()
						{
							mVD.createAndShowErrorDialog(medServerFrame, exc.getMessage());
						}
					});
				}
			}

			public PatientIdentifier[] patients = null;
		}

		PatientNotifyingRunnable runnable = new PatientNotifyingRunnable();

		Thread t = MedViewDialogs.instance().startProgressMonitoring(medServerFrame, runnable);

		try
		{
			t.join(); // -> InterruptedException, this is why this method cannot be called from the EDT
		}
		catch (InterruptedException exc)
		{
			exc.printStackTrace();

			return null;
		}

		return runnable.patients;
	}



	private void clearInnerExamPanel()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				availablePatientsListModel.clear();

				totalPatientsLabel.setText("0");

				totalExaminationsLabel.setText("0");

				enableInnerExamPanel(false);
			}
		});
	}

	private void enableInnerExamPanel(boolean enabled)
	{
		innerExamPanel.setEnabled(enabled);

		totalPatientsDescLabel.setEnabled(enabled);

		totalPatientsLabel.setEnabled(enabled);

		totalExaminationsDescLabel.setEnabled(enabled);

		totalExaminationsLabel.setEnabled(enabled);

		innerExamInnerPanel.setEnabled(enabled);

		availablePatientsPanel.setEnabled(enabled);

		availablePatientsList.setEnabled(enabled);

		selectedPatientPanel.setEnabled(enabled);
	}



	private void initializeComponents()
	{
		// distributed examinations

		distExamDescLabel = new JLabel(mVDH.getLanguageString(

			LABEL_DISTRIBUTED_EXAMINATIONS_LS_PROPERTY));

		String del = "";

		if (medServerModel.isExaminationDataLocationSet())
		{
			del = medServerModel.getExaminationDataLocation();
		}

		distExamField = new JTextField(del);

		distExamField.setBackground(Color.white);

		distExamField.setEditable(false);

		changeExamLocAction = new ChangeExaminationDataLocationAction();

		distExamMoreButton = new MedViewMoreButton(changeExamLocAction);

		// pcode number generator

		nrGenLabel = new JLabel(mVDH.getLanguageString(

			LABEL_LOCKFILE_LOCATION_LS_PROPERTY));

		String nGL = "";

		if (medServerModel.isNumberGeneratorLocationSet())
		{
			nGL = medServerModel.getNumberGeneratorLocation();
		}

		nrGenField = new JTextField(nGL);

		nrGenField.setBackground(Color.white);

		nrGenField.setEditable(false);

		changeNrGenAction = new ChangeNumberGeneratorLocationAction();

		nrGenMoreButton = new MedViewMoreButton(changeNrGenAction);

		// inner examination panel

		innerExamPanel = new JPanel();

		// total numbers information

		totalPatientsDescLabel = new JLabel(mVDH.getLanguageString(

			LABEL_TOTAL_NUMBER_OF_PATIENTS_LS_PROPERTY));

		totalExaminationsDescLabel = new JLabel(mVDH.getLanguageString(

			LABEL_TOTAL_NUMBER_OF_EXAMINATIONS_LS_PROPERTY));

		totalPatientsLabel = new JLabel("0");

		totalExaminationsLabel = new JLabel("0");

		// available patients panel

		innerExamInnerPanel = new JPanel();

		availablePatientsPanel = new JPanel();

		availablePatientsListModel = new DefaultListModel();

		availablePatientsList = new JList(availablePatientsListModel);

		selectedPatientPanel = new JPanel();
	}

	private void createAndAttachListeners()
	{
		modelListener = new ModelListener();

		medServerModel.addMedServerModelListener(modelListener);
	}

	private void layoutPanel()
	{
		this.setLayout(new GridBagLayout());

		int west = GridBagConstraints.WEST;

		int east = GridBagConstraints.EAST;

		int none = GridBagConstraints.NONE;

		int both = GridBagConstraints.BOTH;

		final int cgs = GUIConstants.COMPONENT_GROUP_SPACING;

		final int ccs = GUIConstants.COMPONENT_COMPONENT_SPACING;

		innerExamPanel.setLayout(new GridBagLayout());

		innerExamPanel.setBorder(BorderFactory.createEtchedBorder());

		innerExamInnerPanel.setLayout(new BorderLayout(ccs,ccs)); // hgap, vgap

		innerExamInnerPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

		innerExamInnerPanel.add(availablePatientsPanel, BorderLayout.WEST);

		innerExamInnerPanel.add(selectedPatientPanel, BorderLayout.CENTER);

		availablePatientsPanel.setLayout(new BorderLayout());

		String titLS = TITLE_AVAILABLE_PATIENTS_LS_PROPERTY;

		String title = mVDH.getLanguageString(titLS);

		Insets i = new Insets(cgs, cgs, cgs, cgs);

		GUIUtilities.attachProperTitledBorder(availablePatientsPanel, title, i);

		availablePatientsPanel.add(new JScrollPane(availablePatientsList), BorderLayout.CENTER);

		availablePatientsPanel.setPreferredSize(new Dimension(GUIConstants.LIST_WIDTH_NORMAL,0));

		titLS = TITLE_SELECTED_PATIENT_LS_PROPERTY;

		title = mVDH.getLanguageString(titLS);

		i = new Insets(cgs, cgs, cgs, cgs);

		GUIUtilities.attachProperTitledBorder(selectedPatientPanel, title, i);

		add(distExamDescLabel, this,			0, 0, 1, 1, 0, 0, east, none, new Insets(cgs,cgs,cgs,ccs));

		add(distExamField, this, 			1, 0, 1, 1, 1, 0, east, both, new Insets(cgs,0,cgs,ccs));

		add(distExamMoreButton, this, 			2, 0, 1, 1, 0, 0, east, both, new Insets(cgs,0,cgs,cgs));

		add(nrGenLabel, this,				0, 1, 1, 1, 0, 0, east, none, new Insets(0,cgs,cgs,ccs));

		add(nrGenField, this,				1, 1, 1, 1, 0, 0, east, both, new Insets(0,0,cgs,ccs));

		add(nrGenMoreButton, this,			2, 1, 1, 1, 0, 0, east, both, new Insets(0,0,cgs,cgs));

		add(innerExamPanel, this, 			0, 2, 3, 1, 0, 1, east, both, new Insets(0,cgs,cgs,cgs));

		add(totalPatientsDescLabel, innerExamPanel,	0, 0, 1, 1, 1, 0, east, none, new Insets(cgs,cgs,ccs,ccs));

		add(totalPatientsLabel, innerExamPanel, 	1, 0, 1, 1, 0, 0, west, none, new Insets(cgs,0,ccs,ccs));

		add(totalExaminationsDescLabel, innerExamPanel,	2, 0, 1, 1, 0, 0, east, none, new Insets(cgs,0,ccs,ccs));

		add(totalExaminationsLabel, innerExamPanel, 	3, 0, 1, 1, 1, 0, west, none, new Insets(cgs,0,ccs,cgs));

		add(innerExamInnerPanel, innerExamPanel, 	0, 1, 4, 1, 0, 1, west, both, new Insets(0,cgs,cgs,cgs));
	}

	private void add(Component comp, JPanel panel, int x, int y, int gw, int gh, int wx,
		int wy, int anchor, int fill, Insets insets)
	{
		if (gbc == null) { gbc = new GridBagConstraints(); } // lazy

		gbc.gridx = x; gbc.gridy = y; gbc.gridwidth = gw; gbc.gridheight = gh;

		gbc.weightx = wx; gbc.weighty = wy; gbc.fill = fill; gbc.anchor = anchor;

		gbc.insets = insets; panel.add(comp, gbc);
	}

	public MedServerExaminationPanel(MedServerFrame medServerFrame, ProgressNotifiable pN)
	{
		this.medServerFrame = medServerFrame;

		this.medServerModel = medServerFrame.getModel();

		this.mVD = MedViewDialogs.instance();

		mVDH = MedViewDataHandler.instance();

		initializeComponents();

		createAndAttachListeners();

		layoutPanel();

		updatePatientList(pN);

		enableInnerExamPanel(medServerModel.isExaminationDataLocationSet());
	}

	private MedViewDialogs mVD;

	private GridBagConstraints gbc;

	private MedViewDataHandler mVDH;

	private MedServerFrame medServerFrame;

	private MedServerModel medServerModel;

	private ModelListener modelListener;

	private JLabel distExamDescLabel;

	private JLabel nrGenLabel;

	private JTextField distExamField;

	private JTextField nrGenField;

	private Action changeExamLocAction;

	private Action changeNrGenAction;

	private JButton distExamMoreButton;

	private JButton nrGenMoreButton;

	private JPanel innerExamPanel;

	private JLabel totalPatientsDescLabel;

	private JLabel totalExaminationsDescLabel;

	private JLabel totalPatientsLabel;

	private JLabel totalExaminationsLabel;

	private JPanel innerExamInnerPanel;

	private JPanel availablePatientsPanel;

	private DefaultListModel availablePatientsListModel;

	private JList availablePatientsList;

	private JPanel selectedPatientPanel;


	// TERM VALUE AND DEFINITION CHANGE ACTIONS

	private class ChangeExaminationDataLocationAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			String path = mVD.createAndShowChangeExaminationDirectoryDialog(medServerFrame);

			if (path != null) { medServerModel.setExaminationDataLocation(path); }
		}
	}

	private class ChangeNumberGeneratorLocationAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			File file = mVD.createAndShowChooseFileDialog(medServerFrame);

			if (file != null) { medServerModel.setNumberGeneratorLocation(file.getPath()); }
		}
	}


	// MODEL LISTENERS

	private class ModelListener implements MedServerModelListener
	{
		public void examinationAdded(MedServerModelEvent e)
		{
			PatientIdentifier patient = e.getExamination().getPID();

			Enumeration enm = availablePatientsListModel.elements();

			for (int ctr=0; enm.hasMoreElements(); ctr++)
			{
				PatientIdentifier curr = (PatientIdentifier) enm.nextElement();

				if (patient.compareTo(curr) < 0)
				{
					availablePatientsListModel.add(ctr, patient);

					return; // insert patient into list at right place -> end traversal
				}
				else if (patient.compareTo(curr) == 0)
				{
					return; // patient already in list -> end traversal
				}
			}

			availablePatientsListModel.addElement(patient); // add to end of list if we get here
		}

		public void examinationUpdated(MedServerModelEvent e)
		{
		}

		public void examinationDataLocationChanged(MedServerModelEvent e)
		{
			distExamField.setText(medServerModel.getExaminationDataLocation());

			enableInnerExamPanel(medServerModel.isExaminationDataLocationSet());

			medServerFrame.updateActivationState();

			NotifyingRunnable runnable = new NotifyingRunnable()
			{
				public void run()
				{
					// this code will be placed on a thread other than the event dispatch thread

					updatePatientList(getNotifiable());
				}
			};

			MedViewDialogs.instance().startProgressMonitoring(medServerFrame, runnable);
		}

		public void numberGeneratorLocationChanged(MedServerModelEvent e)
		{
			nrGenField.setText(medServerModel.getNumberGeneratorLocation());
		}

		public void examinationDataLocationIDChanged(MedServerModelEvent e) {}

		public void termAdded(MedServerModelEvent e) {}

		public void termRemoved(MedServerModelEvent e) {}

		public void valueAdded(MedServerModelEvent e) {}

		public void valueRemoved(MedServerModelEvent e) {}

		public void termDefinitionLocationChanged(MedServerModelEvent e) {}

		public void termValueLocationChanged(MedServerModelEvent e) {}

		public void termDataHandlerChanged(MedServerModelEvent e) {}

		public void examinationDataHandlerChanged(MedServerModelEvent e) {}

		public void tATDataHandlerChanged(MedServerModelEvent e) {}

		public void pCodePrefixChanged(MedServerModelEvent e) {}

		public void pCodeGeneratorChanged(MedServerModelEvent e) {}
	}

}
