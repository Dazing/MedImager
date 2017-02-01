package medview.medserver.view;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

import medview.common.components.*;
import medview.common.dialogs.*;

import medview.datahandling.*;

import medview.medserver.model.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

/**
 * A panel containing information about the distributed
 * terms.
 *
 * @author Fredrik Lindahl
 */
public class MedServerTermPanel extends JPanel implements
	MedViewLanguageConstants
{

// ---------------------------------------------------------------------
// ************* TERMS AND VALUES COUNTER UPDATING METHODS *************
// ---------------------------------------------------------------------

	private void setTotalTerms(int set)
	{
		totalTermsLabel.setText(set + "");
	}

	private void setTotalValues(int set)
	{
		totalValuesLabel.setText(set + "");
	}

	private void increaseTotalTerms(int delta)
	{
		try
		{
			int curr = Integer.parseInt(totalTermsLabel.getText());

			setTotalTerms(curr + delta);
		}
		catch (NumberFormatException exc) {} // never happens here
	}

	private void increaseTotalValues(int delta)
	{
		try
		{
			int curr = Integer.parseInt(totalValuesLabel.getText());

			setTotalValues(curr + delta);
		}
		catch (NumberFormatException exc) {} // never happens here
	}

	private void decreaseTotalTerms(int delta)
	{
		try
		{
			int curr = Integer.parseInt(totalTermsLabel.getText());

			setTotalTerms(curr - delta);
		}
		catch (NumberFormatException exc) {} // never happens here
	}

	private void decreaseTotalValues(int delta)
	{
		try
		{
			int curr = Integer.parseInt(totalValuesLabel.getText());

			setTotalValues(curr - delta);
		}
		catch (NumberFormatException exc) {} // never happens here
	}

	private int getTotalValueCount() // costly - might improve later
	{
		try
		{
			int valueCounter = 0;

			String[] terms = medServerModel.getTerms();

			for (int ctr=0; ctr<terms.length; ctr++)
			{
				valueCounter += medServerModel.getValues(terms[ctr]).length;
			}

			return valueCounter;
		}
		catch (CouldNotRetrieveTermsException e)
		{
			return 0;
		}
		catch (CouldNotRetrieveValuesException e)
		{
			return 0;
		}
	}

// ---------------------------------------------------------------------
// *********************************************************************
// ---------------------------------------------------------------------



// ---------------------------------------------------------------------
// ************************** UTILITY METHODS **************************
// ---------------------------------------------------------------------

	private void totallyRefreshTermSection()
	{
		try
		{
			int valueCounter = 0;

			availableTermsListModel.clear();

			String[] terms = medServerModel.getTerms(); // -> CouldNotRetrieveTermsException

			if (terms.length > 1) { Arrays.sort(terms); }

			for (int ctr=0; ctr<terms.length; ctr++)
			{
				availableTermsListModel.addElement(terms[ctr]);

				valueCounter += medServerModel.getValues(terms[ctr]).length; // -> CouldNotRetrieveValuesException
			}

			setTotalTerms(terms.length);

			setTotalValues(valueCounter);

			enableInnerTermPanel(true);

			enableSelectedTermPanel(false); // gets enabled at selection

			clearSelectedTermPanel();
		}
		catch (CouldNotRetrieveTermsException e)
		{
			clearAndDisableTermInfoPanels();
		}
		catch (CouldNotRetrieveValuesException e)
		{
			clearAndDisableTermInfoPanels();
		}
	}

	private void clearAndDisableTermInfoPanels()
	{
		clearInnerTermPanel();

		clearSelectedTermPanel();

		enableInnerTermPanel(false);

		enableSelectedTermPanel(false);
	}

	private void clearInnerTermPanel()
	{
		availableTermsListModel.clear();

		totalTermsLabel.setText("");

		totalValuesLabel.setText("");
	}

	private void clearSelectedTermPanel()
	{
		termValueListModel.clear();

		termNameLabel.setText("");

		termTypeLabel.setText("");

		termValueCountLabel.setText("");
	}

	private void enableInnerTermPanel(boolean enabled)
	{
		innerTermPanel.setEnabled(enabled);

		totalTermsDescLabel.setEnabled(enabled);

		totalTermsLabel.setEnabled(enabled);

		totalValuesDescLabel.setEnabled(enabled);

		totalValuesLabel.setEnabled(enabled);

		innerTermInnerPanel.setEnabled(enabled);

		availableTermsPanel.setEnabled(enabled);

		availableTermsList.setEnabled(enabled);
	}

	private void enableSelectedTermPanel(boolean enabled)
	{
		selectedTermPanel.setEnabled(enabled);

		termNameDescLabel.setEnabled(enabled);

		termNameLabel.setEnabled(enabled);

		termTypeDescLabel.setEnabled(enabled);

		termTypeLabel.setEnabled(enabled);

		termValueCountDescLabel.setEnabled(enabled);

		termValueCountLabel.setEnabled(enabled);

		termValueList.setEnabled(enabled);
	}

// ---------------------------------------------------------------------
// *********************************************************************
// ---------------------------------------------------------------------





	private void initializeComponents()
	{
		termDefLocLabel = new JLabel(mVDH.getLanguageString(

			LABEL_DISTRIBUTED_TERM_DEFINITION_LOCATION_LS_PROPERTY));

		termValLocLabel = new JLabel(mVDH.getLanguageString(

			LABEL_DISTRIBUTED_TERM_VALUE_LOCATION_LS_PROPERTY));

		String tvl = ""; String tdl = "";

		if (medServerModel.isTermValueLocationSet())
		{
			tvl = medServerModel.getTermValueLocation();
		}
		if (medServerModel.isTermDefinitionLocationSet())
		{
			tdl = medServerModel.getTermDefinitionLocation();
		}

		termDefLocField = new JTextField(tdl);

		termDefLocField.setBackground(Color.white);

		termDefLocField.setEditable(false);

		termValLocField = new JTextField(tvl);

		termValLocField.setBackground(Color.white);

		termValLocField.setEditable(false);

		changeTermDefAction = new ChangeTermDefinitionAction();

		changeTermValAction = new ChangeTermValueAction();

		termDefLocButton = new MedViewMoreButton(changeTermDefAction);

		termValLocButton = new MedViewMoreButton(changeTermValAction);

		innerTermPanel = new JPanel();

		totalTermsDescLabel = new JLabel(mVDH.getLanguageString(

			LABEL_TOTAL_NUMBER_OF_TERMS_LS_PROPERTY));

		totalValuesDescLabel = new JLabel(mVDH.getLanguageString(

			LABEL_TOTAL_NUMBER_OF_VALUES_LS_PROPERTY));

		totalTermsLabel = new JLabel("0");

		totalValuesLabel = new JLabel("0");

		innerTermInnerPanel = new JPanel();

		availableTermsPanel = new JPanel();

		availableTermsListModel = new DefaultListModel();

		availableTermsList = new JList(availableTermsListModel);

		availableTermsListListener = new AvailableTermsListListener();

		availableTermsList.addListSelectionListener(availableTermsListListener);

		selectedTermPanel = new JPanel();

		termNameDescLabel = new JLabel(mVDH.getLanguageString(

			LABEL_TERM_DESCRIPTOR_LS_PROPERTY));

		termTypeDescLabel = new JLabel(mVDH.getLanguageString(

			LABEL_TYPE_DESCRIPTOR_LS_PROPERTY));

		termValueCountDescLabel = new JLabel(mVDH.getLanguageString(

			LABEL_VALUES_DESCRIPTOR_LS_PROPERTY));

		termNameLabel = new JLabel("");

		termTypeLabel = new JLabel("");

		termValueCountLabel = new JLabel("");

		termValuesPanel = new JPanel();

		termValueListModel = new DefaultListModel();

		termValueList = new JList(termValueListModel);
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


		innerTermPanel.setLayout(new GridBagLayout());

		innerTermPanel.setBorder(BorderFactory.createEtchedBorder());


		innerTermInnerPanel.setLayout(new GridBagLayout());

		innerTermInnerPanel.setBorder(BorderFactory.createEmptyBorder(cgs,cgs,cgs,cgs));


		availableTermsPanel.setLayout(new BorderLayout());

		String titLS = TITLE_AVAILABLE_TERMS_LS_PROPERTY;

		String title = mVDH.getLanguageString(titLS);

		Insets i = new Insets(cgs, cgs, cgs, cgs);

		GUIUtilities.attachProperTitledBorder(availableTermsPanel, title, i);

		availableTermsPanel.setPreferredSize(new Dimension(GUIConstants.LIST_WIDTH_LARGE,0));

		JScrollPane availableTermsSP = new JScrollPane(availableTermsList);

		availableTermsPanel.add(availableTermsSP, BorderLayout.CENTER);


		selectedTermPanel.setLayout(new GridBagLayout());

		titLS = TITLE_SELECTED_TERM_LS_PROPERTY;

		title = mVDH.getLanguageString(titLS);

		i = new Insets(cgs, cgs, cgs, cgs);

		GUIUtilities.attachProperTitledBorder(selectedTermPanel, title, i);

		selectedTermPanel.setPreferredSize(new Dimension(0,0)); // see note below


		int hsbPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;

		int vsbPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;

		JScrollPane termValuesSP = new JScrollPane(termValueList, vsbPolicy, hsbPolicy);


		add(termDefLocLabel, this, 		0, 0, 1, 1, 0, 0, east, none, new Insets(cgs,cgs,ccs,ccs));

		add(termDefLocField, this, 		1, 0, 1, 1, 1, 0, east, both, new Insets(cgs,0,ccs,ccs));

		add(termDefLocButton, this, 	2, 0, 1, 1, 0, 0, east, both, new Insets(cgs,0,ccs,cgs));

		add(termValLocLabel, this, 		0, 1, 1, 1, 0, 0, east, none, new Insets(0,cgs,ccs,ccs));

		add(termValLocField, this, 		1, 1, 1, 1, 0, 0, east, both, new Insets(0,0,ccs,ccs));

		add(termValLocButton, this, 	2, 1, 1, 1, 0, 0, east, both, new Insets(0,0,ccs,cgs));

		add(innerTermPanel, this, 		0, 2, 3, 1, 0, 1, east, both, new Insets(0,cgs,cgs,cgs));


		add(totalTermsDescLabel, innerTermPanel, 		0, 0, 1, 1, 1, 0, east, none, new Insets(cgs,cgs,ccs,ccs));

		add(totalTermsLabel, innerTermPanel, 			1, 0, 1, 1, 0, 0, west, none, new Insets(cgs,0,ccs,ccs));

		add(totalValuesDescLabel, innerTermPanel, 		2, 0, 1, 1, 0, 0, east, none, new Insets(cgs,0,ccs,ccs));

		add(totalValuesLabel, innerTermPanel, 			3, 0, 1, 1, 1, 0, west, none, new Insets(cgs,0,ccs,cgs));

		add(innerTermInnerPanel, innerTermPanel, 		0, 1, 4, 1, 0, 1, west, both, new Insets(0,cgs,cgs,cgs));


		add(availableTermsPanel, innerTermInnerPanel,	0, 0, 1, 1, 0, 0, west, both, new Insets(0,0,0,ccs));

		add(selectedTermPanel, innerTermInnerPanel,		1, 0, 1, 1, 1, 1, east, both, new Insets(0,0,0,0));


		add(termNameDescLabel, selectedTermPanel,		0, 0, 1, 1, 0, 0, west, none, new Insets(0,0,ccs,ccs));

		add(termNameLabel, selectedTermPanel,			1, 0, 1, 1, 1, 0, west, none, new Insets(0,0,ccs,cgs));

		add(termTypeDescLabel, selectedTermPanel,		0, 1, 1, 1, 0, 0, west, none, new Insets(0,0,ccs,ccs));

		add(termTypeLabel, selectedTermPanel,			1, 1, 1, 1, 0, 0, west, none, new Insets(0,0,ccs,cgs));

		add(termValueCountDescLabel, selectedTermPanel,	0, 2, 1, 1, 0, 0, west, none, new Insets(0,0,ccs,ccs));

		add(termValueCountLabel, selectedTermPanel,		1, 2, 1, 1, 0, 0, west, none, new Insets(0,0,ccs,cgs));

		add(termValuesSP, selectedTermPanel,			0, 3, 2, 1, 0, 1, west, both, new Insets(0,0,0,0));

		/* NOTE: it seems that the inner term inner panel (with the
		 * two components availableTermsPanel and selectedTermPanel)
		 * right component (the selectedTermPanel) has some kind of
		 * default large size that makes it get all available width
		 * and presses the left component to minimum width even though
		 * you set the left component's preferred size. Since the
		 * right component is set to be the 'extra width taker' it
		 * seems like the grid bag layout decides that it takes
		 * precedence over the left component when it comes to the
		 * preferred sizes being larger than the available total
		 * width. Therefore, I force the right component to have a
		 * preferred width and height of 0, making the grid bag
		 * layout place the left component at its preferred width
		 * and then later extracting the right component to the
		 * remainder of the available space. */
	}

	private void add(Component comp, JPanel panel, int x, int y, int gw, int gh, int wx,
		int wy, int anchor, int fill, Insets insets)
	{
		if (gbc == null)
		{
			gbc = new GridBagConstraints(); // lazy initialization
		}

		gbc.gridx = x; gbc.gridy = y; gbc.gridwidth = gw; gbc.gridheight = gh;

		gbc.weightx = wx; gbc.weighty = wy; gbc.fill = fill; gbc.anchor = anchor;

		gbc.insets = insets; panel.add(comp, gbc);
	}

	public MedServerTermPanel(MedServerFrame medServerFrame)
	{
		this.medServerFrame = medServerFrame;

		this.medServerModel = medServerFrame.getModel();

		this.mVD = MedViewDialogs.instance();

		mVDH = MedViewDataHandler.instance();

		initializeComponents();

		createAndAttachListeners();

		layoutPanel();

		if (medServerModel.areTermLocationsSet())
		{
			totallyRefreshTermSection();

			enableInnerTermPanel(true);
		}
		else
		{
			enableInnerTermPanel(false);
		}
	}

	private MedViewDialogs mVD;

	private MedViewDataHandler mVDH;

	private MedServerFrame medServerFrame;

	private MedServerModel medServerModel;

	private GridBagConstraints gbc;

	private JLabel termDefLocLabel;

	private JLabel termValLocLabel;

	private JTextField termDefLocField;

	private JTextField termValLocField;

	private JButton termDefLocButton;

	private JButton termValLocButton;

	private Action changeTermDefAction;

	private Action changeTermValAction;

	private JPanel innerTermPanel;

	private JPanel innerTermInnerPanel;

	private JLabel totalTermsDescLabel;

	private JLabel totalTermsLabel;

	private JLabel totalValuesDescLabel;

	private JLabel totalValuesLabel;

	private JPanel availableTermsPanel;

	private JList availableTermsList;

	private JLabel termNameDescLabel;

	private JLabel termNameLabel;

	private JLabel termTypeDescLabel;

	private JLabel termTypeLabel;

	private JLabel termValueCountDescLabel;

	private JLabel termValueCountLabel;

	private JPanel termValuesPanel;

	private DefaultListModel termValueListModel;

	private JList termValueList;

	private DefaultListModel availableTermsListModel;

	private JPanel selectedTermPanel;

	private ModelListener modelListener;

	private AvailableTermsListListener availableTermsListListener;



// ----------------------------------------------------------------------------
// ***************** TERM VALUE AND DEFINITION CHANGE ACTIONS *****************
// ----------------------------------------------------------------------------

	private class ChangeTermDefinitionAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			String path = mVD.createAndShowChangeTermDefinitionDialog(medServerFrame);

			if (path != null) { medServerModel.setTermDefinitionLocation(path); }
		}
	}

	private class ChangeTermValueAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
			String path = mVD.createAndShowChangeTermValueDialog(medServerFrame);

			if (path != null) { medServerModel.setTermValueLocation(path); }
		}
	}

// ----------------------------------------------------------------------------
// ****************************************************************************
// ----------------------------------------------------------------------------



// ----------------------------------------------------------------------------
// ***************************** MODEL LISTENERS ******************************
// ----------------------------------------------------------------------------
	private class ModelListener implements MedServerModelListener
	{
		public void termAdded(MedServerModelEvent e) // should not get if already existant
		{
			String term = e.getTerm(); increaseTotalTerms(1);

			try
			{
				int termValCount = medServerModel.getValues(term).length;

				increaseTotalValues(termValCount);
			}
			catch (Exception exc) { exc.printStackTrace(); } // should not happen

			int ctr = 0; String currElem = null;

			Enumeration enm = availableTermsListModel.elements();

			while (enm.hasMoreElements())
			{
				currElem = (String) enm.nextElement();

				if (term.compareTo(currElem) < 0)
				{
					availableTermsListModel.add(ctr,term);

					return; // term added and counter increased - done
				}

				ctr++;
			}

			availableTermsListModel.addElement(term); // adds to end of list
		}

		public void termRemoved(MedServerModelEvent e) // should not get if non-existant
		{
			decreaseTotalTerms(1);

			setTotalValues(getTotalValueCount()); // costly - might improve later

			availableTermsListModel.removeElement(e.getTerm());
		}

		public void valueAdded(MedServerModelEvent e) // should not get if already existant
		{
			increaseTotalValues(1);

			if (!availableTermsList.isSelectionEmpty()) // check if term is displaying
			{
				String selTerm = (String) availableTermsList.getSelectedValue();

				if (selTerm.equals(e.getTerm()))
				{
					int ctr = 0; String value = (String) e.getValue();

					Enumeration enm = termValueListModel.elements();

					while (enm.hasMoreElements())
					{
						if (value.compareTo((String)enm.nextElement()) < 0)
						{
							termValueListModel.add(ctr,value);

							return;
						}

						ctr++;
					}

					termValueListModel.addElement(value); // adds to end of list
				}
			}
		}

		public void valueRemoved(MedServerModelEvent e) // should not get if non-existant
		{
			decreaseTotalValues(1);

			if (!availableTermsList.isSelectionEmpty())
			{
				String selTerm = (String) availableTermsList.getSelectedValue();

				if (selTerm.equals(e.getTerm()))
				{
					termValueListModel.removeElement(e.getValue());
				}
			}
		}

		public void termDefinitionLocationChanged(MedServerModelEvent e)
		{
			termDefLocField.setText(medServerModel.getTermDefinitionLocation());

			if (medServerModel.areTermLocationsSet()) { totallyRefreshTermSection(); }

			enableInnerTermPanel(medServerModel.areTermLocationsSet());

			medServerFrame.updateActivationState();
		}

		public void termValueLocationChanged(MedServerModelEvent e)
		{
			termValLocField.setText(medServerModel.getTermValueLocation());

			if (medServerModel.areTermLocationsSet()) { totallyRefreshTermSection(); }

			enableInnerTermPanel(medServerModel.areTermLocationsSet());

			medServerFrame.updateActivationState();
		}

		public void examinationAdded(MedServerModelEvent e) {}

		public void examinationUpdated(MedServerModelEvent e) {}

		public void examinationDataLocationIDChanged(MedServerModelEvent e) {}

		public void examinationDataLocationChanged(MedServerModelEvent e) { }

		public void termDataHandlerChanged(MedServerModelEvent e) {}

		public void examinationDataHandlerChanged(MedServerModelEvent e) {}

		public void tATDataHandlerChanged(MedServerModelEvent e) {}

		public void pCodePrefixChanged(MedServerModelEvent e) {}

		public void numberGeneratorLocationChanged(MedServerModelEvent e) {}

		public void pCodeGeneratorChanged(MedServerModelEvent e) {}
	}
// ----------------------------------------------------------------------------
// ****************************************************************************
// ----------------------------------------------------------------------------



// ----------------------------------------------------------------------------
// ***************************** JLIST LISTENERS ******************************
// ----------------------------------------------------------------------------

	private class AvailableTermsListListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			if (availableTermsList.isSelectionEmpty())
			{
				clearSelectedTermPanel();

				enableSelectedTermPanel(false);
			}
			else
			{
				try
				{
					String term = (String) availableTermsList.getSelectedValue();

					termTypeLabel.setText(medServerModel.getTypeDescription(term));

					termNameLabel.setText(term); termValueListModel.clear();

					Object[] values = medServerModel.getValues(term);

					for (int ctr=0; ctr<values.length; ctr++)
					{
						termValueListModel.addElement(values[ctr]);
					}

					termValueCountLabel.setText(values.length + "");

					enableSelectedTermPanel(true);
				}
				catch (Exception exc)
				{
					mVD.createAndShowErrorDialog(medServerFrame, exc.getMessage());
				}
			}
		}
	}

// ----------------------------------------------------------------------------
// ****************************************************************************
// ----------------------------------------------------------------------------

}
