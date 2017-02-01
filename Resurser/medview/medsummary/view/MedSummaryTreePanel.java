/*
 * @(#)MedSummaryTreePanel.java
 *
 * $Id: MedSummaryTreePanel.java,v 1.28 2008/07/28 06:56:51 it2aran Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.view;

import medview.common.actions.*;
import medview.common.components.menu.*;
import medview.common.dialogs.*;
import medview.common.generator.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;

import medview.medsummary.model.*;

import misc.foundation.*;

import misc.gui.actions.*;
import misc.gui.constants.*;
import misc.gui.utilities.*;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.tree.*;

import se.chalmers.cs.medview.docgen.*;

public class MedSummaryTreePanel extends JPanel implements
	MedViewLanguageConstants, MedViewMediaConstants,
	MedSummaryActions, ActionContainer, GUIConstants
{

	public Action getAction(String actionName)
	{
		return (Action) actions.get(actionName);
	}



	private void layoutPanel()
	{
		setLayout(new BorderLayout());

		JPanel mainPanel = createMainPanel();

		JPanel eastPanel = createEastPanel();

		add(mainPanel, BorderLayout.CENTER);

		add(eastPanel, BorderLayout.EAST);

		setPreferredSize(new Dimension(230,230));

		setMinimumSize(new Dimension(210,210));
	}

	private JPanel createMainPanel()
	{
		JPanel mainPanel = new JPanel(new BorderLayout(0,0));

		JScrollPane jSP = new JScrollPane(tree);

		jSP.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));

		JButton clearPatientsButton = new JButton(clearPatientsAction);

		clearPatientsButton.setPreferredSize(new Dimension(0,BUTTON_HEIGHT_SMALL));

		mainPanel.add(jSP, BorderLayout.CENTER);

		mainPanel.add(clearPatientsButton, BorderLayout.SOUTH);

		return mainPanel;
	}

	private JPanel createEastPanel()
	{
		JPanel eastPanel = new JPanel();

		GridBagLayout gbl = new GridBagLayout();

		GridBagConstraints gbc = new GridBagConstraints();

		eastPanel.setLayout(gbl);

		eastPanel.setOpaque(false);

		eastPanel.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));

		generateButton = new JButton(generateSummaryAction);

		GUIUtilities.attachToolBarButtonSwingFix(generateButton);

		generateButton.setMargin(new Insets(5,3,5,3));

		generateButton.setText("");

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 1;
		eastPanel.add(Box.createGlue(), gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = 0;
		eastPanel.add(generateButton, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weighty = 1;
		eastPanel.add(Box.createGlue(), gbc);

		return eastPanel;
	}



	private void initSimpleMembers()
	{
		mVD = MedViewDialogs.instance();

		mVDH = MedViewDataHandler.instance();

		model = mediator.getModel();

		treeModel = model.getTreeModel();

		engineBuilder = model.getGeneratorEngineBuilder();
	}

	private void initActions()
	{
		actions = new HashMap();

		newDaynoteAction = new NewDaynoteAction();
		
		showGraphAction = new ShowGraphAction();

		removePatientAction = new RemovePatientAction();

		clearPatientsAction = new ClearPatientsAction();

		generateSummaryAction = new GenerateSummaryAction();

		actions.put(NEW_DAYNOTE_ACTION, newDaynoteAction);
		
		actions.put(SHOW_GRAPH_ACTION, showGraphAction);

		actions.put(REMOVE_PATIENT_ACTION, removePatientAction);

		actions.put(CLEAR_PATIENTS_ACTION, clearPatientsAction);

		actions.put(GENERATE_SUMMARY_ACTION, generateSummaryAction);

		mediator.registerAction(NEW_DAYNOTE_ACTION, newDaynoteAction);
		
		mediator.registerAction(SHOW_GRAPH_ACTION, showGraphAction);

		mediator.registerAction(REMOVE_PATIENT_ACTION, removePatientAction);

		mediator.registerAction(CLEAR_PATIENTS_ACTION, clearPatientsAction);

		mediator.registerAction(GENERATE_SUMMARY_ACTION, generateSummaryAction);
	}

	private void initTree()
	{
		swingRoot = new DefaultMutableTreeNode(TREE_ROOT_NAME);

		tree = new JTree(swingRoot);

		swingModel = (DefaultTreeModel) tree.getModel();

		selModel = tree.getSelectionModel();

		tree.setRootVisible(false);
	}

	private void initListeners()
	{
		listener = new Listener();

		treeModel.addTreeModelListener(listener);

		swingModel.addTreeModelListener(listener);

		tree.addTreeSelectionListener(listener);

		tree.addMouseListener(listener);

		engineBuilder.addGEBListener(listener);
	}

	private void initPopups()
	{
		/* NOTE: Something that caused me a lot of
		 * trouble was when I had another version of
		 * this code where I created four JMenuItems
		 * and then added them to the popup menus. I
		 * later figured out that I tried to add the
		 * same menu item (clear patients) to three
		 * different popupmenus - this cannot be done
		 * since Java only allows each component to
		 * have exactly one parent. Thus, when I added
		 * the menu item to another popup menu, the
		 * previous popup menu parent disassociated
		 * itself with the menu item prior to the
		 * addition. */

		String clearLS = MNEMONIC_MENU_ITEM_CLEAR_LS_PROPERTY;

		String generateLS = MNEMONIC_MENU_ITEM_GENERATE_SUMMARY_LS_PROPERTY;

		String newDaynoteLS = MNEMONIC_MENU_ITEM_NEW_DAYNOTE_LS_PROPERTY;

		String removePatientLS = MNEMONIC_MENU_ITEM_REMOVE_PATIENT_LS_PROPERTY;

		popupMenuPatient = new JPopupMenu();

		popupMenuExamination = new JPopupMenu();

		popupMenuPatient.add(new MedViewMenuItem(generateSummaryAction, generateLS));

		popupMenuPatient.addSeparator();

		popupMenuPatient.add(new MedViewMenuItem(newDaynoteAction, newDaynoteLS));

		popupMenuPatient.add(new MedViewMenuItem(removePatientAction, removePatientLS));

		popupMenuPatient.addSeparator();

		popupMenuPatient.add(new MedViewMenuItem(clearPatientsAction, clearLS));

		popupMenuExamination.add(new MedViewMenuItem(generateSummaryAction, generateLS));

		popupMenuExamination.addSeparator();

		popupMenuExamination.add(new MedViewMenuItem(clearPatientsAction, clearLS));
	}

	public MedSummaryTreePanel(MedSummaryFrame mediator)
	{
		this.mediator = mediator;

		initSimpleMembers();

		initActions();

		initTree();

		initListeners();

		initPopups();

		layoutPanel();
	}

	private JTree tree;

	private HashMap actions;

	private Listener listener;

	private MedViewDialogs mVD;

	private MedSummaryModel model;

	private JButton generateButton;

	private MedViewDataHandler mVDH;

	private MedSummaryFrame mediator;

	private DefaultTreeModel swingModel;

	private TreeSelectionModel selModel;

	private Action newDaynoteAction;
	
	private Action showGraphAction;

	private Action removePatientAction;

	private Action clearPatientsAction;

	private Action generateSummaryAction;

	private JPopupMenu popupMenuPatient;

	private JPopupMenu popupMenuExamination;

	private DefaultMutableTreeNode swingRoot;

	private MedViewGeneratorEngineBuilder engineBuilder;

	private medview.medsummary.model.TreeModel treeModel;

	private static final String TREE_ROOT_NAME = "root";








	private static class PatientTreeNode extends DefaultMutableTreeNode
	{
		public PatientTreeNode(Object userObject) { super(userObject); }

		public String toString()
		{
			PatientModel userObject = (PatientModel) getUserObject();

			return userObject.getPID().toString();
		}
	}

	private static class ExaminationTreeNode extends DefaultMutableTreeNode
	{
		public ExaminationTreeNode(Object userObject) { super(userObject); }

		public String toString()
		{
			ExaminationModel userObject = (ExaminationModel) getUserObject();

			return userObject.getDateString();
		}
	}


	// LISTENER

	private class Listener implements MouseListener,
		TreeSelectionListener, medview.medsummary.model.TreeModelListener,
		javax.swing.event.TreeModelListener, GeneratorEngineBuilderListener
	{

		// MEDSUMMARY TREE MODEL LISTENER

		public void patientAdded(final medview.medsummary.model.TreeModelEvent e)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					PatientModel patientModel = e.getPatientModel();

					PatientTreeNode pTN = new PatientTreeNode(patientModel);

					ExaminationModel[] eMS = patientModel.getModels();

					for (int ctr=0; ctr<eMS.length; ctr++) // add examination nodes
					{
						pTN.add(new ExaminationTreeNode(eMS[ctr]));
					}

					if (swingRoot.getChildCount() > 0) // children exists
					{
						Enumeration enm = swingRoot.children();

						PatientTreeNode currTN = null;

						PatientModel currM = null;

						while (enm.hasMoreElements())
						{
							currTN = (PatientTreeNode) enm.nextElement();

							currM = (PatientModel) currTN.getUserObject();

							if (patientModel.compareTo(currM) <= 0)
							{
								int ind = swingRoot.getIndex(currTN);

								swingModel.insertNodeInto(pTN, swingRoot, ind);

								swingModel.nodeStructureChanged(swingRoot);

								break;
							}
							else if (!enm.hasMoreElements()) // last child
							{
								int ind = swingRoot.getChildCount();

								swingModel.insertNodeInto(pTN, swingRoot, ind);

								swingModel.nodeStructureChanged(swingRoot);

								break;
							}
						}
					}
					else // children did not exist
					{
						swingModel.insertNodeInto(pTN, swingRoot, 0); // first child

						swingModel.nodeStructureChanged(swingRoot);
					}
				}
			});
		}

		public void patientRemoved(final medview.medsummary.model.TreeModelEvent e)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					Enumeration enm = swingRoot.children();

					DefaultMutableTreeNode currNode = null;

					PatientModel currUserObject = null;

					while(enm.hasMoreElements())
					{
						currNode = (DefaultMutableTreeNode) enm.nextElement();

						currUserObject = (PatientModel) currNode.getUserObject();

						if (currUserObject.equals(e.getPatientModel()))
						{
							swingModel.removeNodeFromParent(currNode);
						}
					}
				}
			});
		}

		public void patientsCleared(medview.medsummary.model.TreeModelEvent e)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					swingRoot.removeAllChildren();

					swingModel.nodeStructureChanged(swingRoot);
				}
			});
		}

		public void examinationAdded(final medview.medsummary.model.TreeModelEvent e)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					ExaminationModel eM = e.getExaminationModel();

					PatientModel pM = eM.getPatientModel();

					Enumeration enm = swingRoot.children();

					PatientTreeNode treeNode = null;

					while (enm.hasMoreElements())
					{
						treeNode = (PatientTreeNode) enm.nextElement();

						if (treeNode.getUserObject() == pM)
						{
							ExaminationTreeNode eTN = new ExaminationTreeNode(eM);

							int index = treeNode.getChildCount();

							swingModel.insertNodeInto(eTN, treeNode, index); // inserts at end

							TreePath tP = new TreePath(eTN.getPath());

							tree.scrollPathToVisible(tP);

							selModel.setSelectionPath(tP);

							return;
						}
					}
				}
			});
		}

		public void examinationRemoved(medview.medsummary.model.TreeModelEvent e)
		{
		}

		public void examinationUpdated(final medview.medsummary.model.TreeModelEvent e)
		{
			SwingUtilities.invokeLater(new Runnable()
			{
				public void run()
				{
					Enumeration enm = swingRoot.children();

					while (enm.hasMoreElements())
					{
						PatientTreeNode treeNode = (PatientTreeNode) enm.nextElement();

						if (treeNode.getUserObject() == e.getPatientModel())
						{
							Enumeration enm2 = treeNode.children();

							while (enm2.hasMoreElements())
							{
								ExaminationTreeNode examNode = (ExaminationTreeNode) enm2.nextElement();

								if (examNode.getUserObject() == e.getExaminationModel())
								{
									TreePath selPath = tree.getLeadSelectionPath();

									DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) selPath.getLastPathComponent();

									if (selNode.getUserObject() == e.getExaminationModel())
									{
										// the examination updated is selected - update images

										mediator.displayImages(new ExaminationModel[] {e.getExaminationModel()});
									}
								}
							}
						}
					}
				}
			});
		}

		// SWING TREE MODEL LISTENER

		public void treeNodesChanged(javax.swing.event.TreeModelEvent e) { checkClear(); }

		public void treeNodesInserted(javax.swing.event.TreeModelEvent e) { checkClear(); }

		public void treeNodesRemoved(javax.swing.event.TreeModelEvent e) { checkClear(); }

		public void treeStructureChanged(javax.swing.event.TreeModelEvent e) { checkClear(); }

		private void checkClear() {clearPatientsAction.setEnabled(swingRoot.getChildCount()>0);}

		// SWING TREE SELECTION MODEL LISTENER

		public void valueChanged(TreeSelectionEvent e)
		{
			if (tree.isSelectionEmpty())
			{
				newDaynoteAction.setEnabled(false);
				
				showGraphAction.setEnabled(false);

				removePatientAction.setEnabled(false);

				mediator.clearImageDisplays();

				engineBuilder.removeValueContainers();
			}
			else	// something is selected
			{
				removePatientAction.setEnabled(true);

				selPaths = tree.getSelectionPaths();

				toDisplayVector = new Vector();

				Vector assPatientModels = new Vector();		// patient models associated with selection

				for (int ctr=0; ctr<selPaths.length; ctr++)
				{
					currPath = selPaths[ctr];

					currNode = (DefaultMutableTreeNode) currPath.getLastPathComponent();

					if (currNode.getUserObject() instanceof PatientModel)
					{
						pModel = (PatientModel) currNode.getUserObject();

						boolean alreadyAssociated = false;

						for (int ctr2=0; ctr2<assPatientModels.size(); ctr2++)
						{
							if (assPatientModels.elementAt(ctr2).equals(pModel))
							{
								alreadyAssociated = true;
							}
						}

						if (!alreadyAssociated)
						{
							assPatientModels.add(pModel);
						}

						eModels = pModel.getModels();

						for (int ctr2=0; ctr2<eModels.length; ctr2++)
						{
							if (!toDisplayVector.contains(eModels[ctr2]))
							{
								toDisplayVector.add(eModels[ctr2]);
							}
						}
					}
					else if (currNode.getUserObject() instanceof ExaminationModel)
					{
						eModel = (ExaminationModel) currNode.getUserObject();

						pModel = eModel.getPatientModel();

						boolean alreadyAssociated = false;

						for (int ctr2=0; ctr2<assPatientModels.size(); ctr2++)
						{
							if (assPatientModels.elementAt(ctr2).equals(pModel))
							{
								alreadyAssociated = true;
							}
						}

						if (!alreadyAssociated)
						{
							assPatientModels.add(pModel);
						}

						if (!toDisplayVector.contains(eModel)) { toDisplayVector.add(eModel); }
					}
				}

				if (selPaths.length == 1) { // Exactly one examination should be selected to enable graph
					currNode = (DefaultMutableTreeNode) selPaths[0].getLastPathComponent();
					showGraphAction.setEnabled((currNode.getUserObject() instanceof ExaminationModel));
				}
				else {
					showGraphAction.setEnabled(false);
				}
				
				newDaynoteAction.setEnabled(assPatientModels.size() == 1);

				eModels = new ExaminationModel[toDisplayVector.size()];

				toDisplayVector.toArray(eModels);

				mediator.displayImages(eModels);

				addToEngine(eModels);
			}
		}

		private void addToEngine(ExaminationModel[] models)
		{
			
			Frame owner = mediator.getParentFrame();

			conts = new ValueContainer[models.length];

			ids = new ExaminationIdentifier[models.length];

			for (int ctr=0; ctr<models.length; ctr++)
			{
				currIdentifier = constructIdentifier(models[ctr]);

				try
				{
					final ExaminationValueContainer curr = mVDH.getExaminationValueContainer(currIdentifier,

						MedViewDataConstants.OPTIMIZE_FOR_EFFICIENCY);

					conts[ctr] = new ValueContainer() // wrapper
					{
						public String[] getValues(String term) throws se.chalmers.cs.medview.docgen.NoSuchTermException
						{
							try
							{
								return curr.getValues(term);
							}
							catch (medview.datahandling.NoSuchTermException exc)
							{
								throw new se.chalmers.cs.medview.docgen.NoSuchTermException(exc.getMessage());
							}
						}

						public String[] getTermsWithValues()
						{
							return curr.getTermsWithValues();
						}

					};

					ids[ctr] = currIdentifier;
				}
				catch (Exception e)
				{
					if (errorVector == null) { errorVector = new Vector(); }

					errorVector.add(currIdentifier);
				}
			}

			if ((errorVector != null) && (errorVector.size() != 0))
			{
				buffy = new StringBuffer();

				buffy.append("Warning: value containers could not be ");

				buffy.append(" obtained for the following examinations: ");

				enm = errorVector.elements();

				while (enm.hasMoreElements())
				{
					currIdentifier = (ExaminationIdentifier) enm.nextElement();

					buffy.append("[" + currIdentifier.getStringRepresentation() + "]");
				}

				mVD.createAndShowErrorDialog(owner, buffy.toString());

				errorVector.clear();
			}

			try
			{
				engineBuilder.buildValueContainers(conts);

				engineBuilder.buildIdentifiers(ids);
			}
			catch (Exception e)
			{
				Component parentComp = mediator.getParentComponent();

				mVD.createAndShowErrorDialog(owner, e.getMessage());
			}
		}

		private ExaminationIdentifier constructIdentifier(ExaminationModel model)
		{
			date = model.getDate();

			PatientIdentifier pid = model.getPatientModel().getPID();

			return new MedViewExaminationIdentifier(pid, date);
		}

		private Date date;

		private Enumeration enm;

		private Vector errorVector;

		private TreePath currPath;

		private TreePath[] selPaths;

		private String errorMessage;

		private StringBuffer buffy;

		private Vector toDisplayVector;

		private boolean foundPatientNode;

		private ExaminationIdentifier currIdentifier;

		private ValueContainer[] conts;

		private DefaultMutableTreeNode currNode;

		private ExaminationIdentifier[] ids;

		private ExaminationModel[] eModels;

		private ExaminationModel eModel;

		private PatientModel pModel;

		// GENERATOR ENGINE BUILDER LISTENER

		public void engineStatusChanged(GeneratorEngineBuilderEvent e)
		{
			generateSummaryAction.setEnabled(e.getEngineStatus() == e.ALL_ELEMENTS_BUILT);
		}

		// MOUSE LISTENER

		public void mouseClicked(MouseEvent e)
		{
			if ((e.getClickCount() == 2) && (e.getButton() == MouseEvent.BUTTON1))
			{
				Point loc = e.getPoint(); // point for double click left mb

				TreePath path = tree.getPathForLocation(loc.x, loc.y);

				if (path == null) { return; } // clicked in empty space of tree

				if (path.getLastPathComponent() instanceof ExaminationTreeNode)
				{
					if (generateSummaryAction.isEnabled())
					{
						generateSummaryAction.actionPerformed(null);
					}
				}
			}
		}

		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e)
		{
			checkForPopup(e);
		}

		public void mouseReleased(MouseEvent e)
		{
			checkForPopup(e);
		}

		private void checkForPopup(MouseEvent e)
		{
			if (e.isPopupTrigger())
			{
				Point loc = e.getPoint();

				TreePath path = tree.getPathForLocation(loc.x, loc.y); // null if none

				if (path != null)
				{
					tree.setSelectionPath(path);

					if (path.getLastPathComponent() instanceof PatientTreeNode)
					{
						popupMenuPatient.show(tree, loc.x, loc.y);
					}
					else // last component must be ExaminationTreeNode
					{
						popupMenuExamination.show(tree, loc.x, loc.y);
					}
				}
			}
		}
	}


	// REMOVE PATIENT ACTION

	private class RemovePatientAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			if (tree.isSelectionEmpty()) { return; }

			TreePath[] selPaths = tree.getSelectionPaths();

			TreePath currPath = null;

			DefaultMutableTreeNode currNode = null;

			for (int ctr=0; ctr<selPaths.length; ctr++)
			{
				currPath = selPaths[ctr];

				currNode = (DefaultMutableTreeNode) currPath.getLastPathComponent();

				if (currNode.getUserObject() instanceof PatientModel)
				{
					PatientModel patientModel = (PatientModel) currNode.getUserObject();

					treeModel.removePatient(patientModel.getPID()); // see above for remove listener
				}
				else if (currNode.getUserObject() instanceof ExaminationModel)
				{
					ExaminationModel eModel = (ExaminationModel) currNode.getUserObject();

					PatientModel patientModel = eModel.getPatientModel();

					treeModel.removePatient(patientModel.getPID()); // see above for remove listener
				}
			}
		}

		public RemovePatientAction()
		{
			super(ACTION_REMOVE_PATIENT_LS_PROPERTY, LEFT_ARROW_IMAGE_ICON);

			setEnabled(false);
		}
	}


	// CLEAR PATIENTS ACTION

	private class ClearPatientsAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			treeModel.clearPatients();
		}

		public ClearPatientsAction()
		{
			super(ACTION_CLEAR_PATIENTS_LS_PROPERTY);

			setEnabled(false);
		}
	}


	// NEW DAYNOTE ACTION

	private class NewDaynoteAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			if (selModel.getSelectionCount() <= 0) { return; } // nothing selected

			TreePath selPath = selModel.getSelectionPath();

			DefaultMutableTreeNode n = (DefaultMutableTreeNode) selPath.getLastPathComponent();

			PatientModel pModel = null;

			if (n.getUserObject() instanceof PatientModel)
			{
				pModel = (PatientModel) n.getUserObject();
			}
			else if (n.getUserObject() instanceof ExaminationModel)
			{
				ExaminationModel eModel = (ExaminationModel) n.getUserObject();

				pModel = eModel.getPatientModel();
			}

			mediator.initiateFeeder(pModel.getPID());
		}

		public NewDaynoteAction()
		{
			super(ACTION_NEW_DAYNOTE_LS_PROPERTY, NEW_DAYNOTE_IMAGE_ICON_24);

			setEnabled(false);
		}
	}

	// SHOW GRAPH ACTION
	
	private class ShowGraphAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			
			if (selModel.getSelectionCount() <= 0) { return; } // nothing selected

			TreePath selPath = selModel.getSelectionPath();

			DefaultMutableTreeNode n = (DefaultMutableTreeNode) selPath.getLastPathComponent();

			if (n.getUserObject() instanceof PatientModel)
			{
				// should not be called in this situation, but...
				return;
			}
			else if (n.getUserObject() instanceof ExaminationModel)
			{
				ExaminationModel eModel = (ExaminationModel) n.getUserObject();

				mediator.initiateGraph(eModel.getExaminationIdentifier());
			}

			return;
		}

		public ShowGraphAction()
		{
			super(ACTION_SHOW_GRAPH_LS_PROPERTY, SHOW_GRAPH_IMAGE_ICON_24);

			setEnabled(false);
		}
	}

	// GENERATE SUMMARY ACTION

	private class GenerateSummaryAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			
			Component treePanel = MedSummaryTreePanel.this;

			Frame owner = mediator.getParentFrame();

			try
			{
                //TODO since termLocation is in packages now, the code below needs to be rewritten
                /*boolean termLocationsValid = model.areTermLocationsValid();

				if (!termLocationsValid)
				{
					String lS = ERROR_TERM_LOCATIONS_INVALID;

					String err = mVDH.getLanguageString(lS);

					mVD.createAndShowErrorDialog(owner, err);

					mediator.showSettingsDialog();

					return; // the user will have to generate again
				}*/

				final GeneratorEngine engine = engineBuilder.getEngine();

				NotifyingRunnable runnable = new NotifyingRunnable()
				{
					public void run()
					{
						se.chalmers.cs.medview.docgen.misc.ProgressNotifiable wrappedNotifiable = new

							se.chalmers.cs.medview.docgen.misc.ProgressNotifiable()
						{
							public void setCurrent(int c)
							{
								getNotifiable().setCurrent(c);
							}

							public void setTotal(int t)
							{
								getNotifiable().setTotal(t);
							}

							public void setDescription(String d)
							{
								getNotifiable().setDescription(d);
							}

							public int getCurrent()
							{
								return getNotifiable().getCurrent();
							}

							public int getTotal()
							{
								return getNotifiable().getTotal();
							}

							public String getDescription()
							{
								return getNotifiable().getDescription();
							}

							public boolean isIndeterminate()
							{
								return getNotifiable().isIndeterminate();
							}

       							public void setIndeterminate(boolean indeterminate)
							{
								getNotifiable().setIndeterminate(indeterminate);
							}
						};

						try
						{
							StyledDocument doc = engine.generateDocument(wrappedNotifiable);

							model.setDocument(doc);
						}
						catch (CouldNotGenerateException exc)
						{
							exc.printStackTrace();
						}
					}
				};

				mVD.startProgressMonitoring(owner, runnable);
			}
			catch (FurtherElementsRequiredException exc)
			{
				mVD.createAndShowErrorDialog(owner, exc.getMessage());

				exc.printStackTrace();
			}
		}

		public GenerateSummaryAction()
		{
			super(ACTION_GENERATE_SUMMARY_LS_PROPERTY, TWO_RIGHT_ARROWS_IMAGE_ICON);

			setEnabled(false);
		}
	}

}
