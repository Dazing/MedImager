/*
 * @(#)AbstractTermView.java
 *
 * $Id: AbstractTermView.java,v 1.12 2005/02/24 16:32:56 lindahlf Exp $
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
import javax.swing.border.*;
import javax.swing.event.*;

import medview.common.actions.*;

import medview.datahandling.*;

import misc.gui.actions.*;
import misc.gui.utilities.*;

import se.chalmers.cs.medview.docgen.*;

public abstract class AbstractTermView extends JPanel implements
	MedViewLanguageConstants, ActionContainer, SummaryCreatorActions
{
	// MISC

	public Action getAction(String actionName)
	{
		return (Action)actions.get(actionName);
	}

	protected void disableAllButtonComponents()
	{
		addValueAction.setEnabled(false);

		removeValueAction.setEnabled(false);
	}

	protected void disableAllSeparatorComponents()
	{
		ntlSepField.setEnabled(false);

		sepField.setEnabled(false);

		ntlSepFieldLabel.setEnabled(false);

		sepFieldLabel.setEnabled(false);
	}

	protected void disableAllDescriptiveComponents()
	{
		currentTermDescLabel.setEnabled(false);

		currentTermLabel.setEnabled(false);
	}

	protected void disableAllVGComponents()
	{
		autoVGCheckBox.setEnabled(false);

		gemenVGCheckBox.setEnabled(false);

		versalVGCheckBox.setEnabled(false);
	}


	// SUBCLASS INTEREST METHODS

	protected void updateVG()
	{}

	protected void updateButtons()
	{}

	protected void updateTopPanel()
	{}

	protected void updateSeparators()
	{}

	protected abstract JPanel getTopPanel();

	protected boolean usesAutoVG()
	{
		return false;
	}

	protected boolean usesSeparators()
	{
		return false;
	}

	protected boolean usesValueButtons()
	{
		return false;
	}

	protected boolean usesTermDescription()
	{
		return false;
	}

	protected Action getAddValueAction()
	{
		return new DefaultAddValueAction();
	}

	protected Action getRemoveValueAction()
	{
		return new DefaultRemoveValueAction();
	}

	protected ItemListener getAutoVGItemListener()
	{
		return null;
	}

	protected ItemListener getGemenItemListener()
	{
		return null;
	}

	protected ItemListener getVersalItemListener()
	{
		return null;
	}

	protected DocumentListener getSeparatorDocumentListener()
	{
		return null;
	}

	protected DocumentListener getNTLSeparatorDocumentListener()
	{
		return null;
	}


	// MISC

	public final void setTerm(String term)
	{
		this.term = term;

		updateView();
	}

	public boolean isDerived()
	{
		if (term == null)
		{
			return false;
		}
		else
		{
			return DerivedTermHandlerFactory.getDerivedTermHandler().isDerivedTerm(term);
		}
	}

	protected final void updateView()
	{
		updateVG();

		updateButtons();

		updateTopPanel();

		updateSeparators();

		updateDescription();
	}

	protected void updateDescription()
	{
		if (term != null)
		{
			try
			{
				updateDescription(TermHandlerFactory.getTermHandler().getTypeDescriptor(term));
			}
			catch (Exception e)
			{
				updateDescription("(unknown type)");
			}
		}
		else
		{
			currentTermLabel.setText("");
		}
	}

	protected void updateDescription(String typeDesc)
	{
		if (isDerived())
		{
			currentTermLabel.setText(term + " / " + typeDesc + " (derived)");
		}
		else
		{
			currentTermLabel.setText(term + " / " + typeDesc);
		}
	}


	private void layoutBottomPanel()
	{
		GridBagLayout gbl = new GridBagLayout();

		GridBagConstraints gbc = new GridBagConstraints();

		bottomPanel.setLayout(gbl);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;

		bottomPanel.add(descPanel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;

		bottomPanel.add(addValueButton, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;

		bottomPanel.add(removeValueButton, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;

		bottomPanel.add(sepPanel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0;
		gbc.weighty = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;

		bottomPanel.add(vGPanel, gbc);
	}

	private void layoutDescriptivePanel()
	{
		GridBagLayout gbl = new GridBagLayout();

		GridBagConstraints gbc = new GridBagConstraints();

		descPanel.setLayout(gbl);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.insets = new Insets(0, 5, 0, 5);
		gbc.fill = GridBagConstraints.BOTH;

		descPanel.add(currentTermDescLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;

		descPanel.add(currentTermLabel, gbc);
	}

	private void layoutSeparatorPanel()
	{
		GridBagLayout gbl = new GridBagLayout();

		GridBagConstraints gbc = new GridBagConstraints();

		sepPanel.setLayout(gbl);

		gbc.gridx = 0;
		gbc.weightx = 0;
		gbc.insets = new Insets(0, 5, 0, 5);
		gbc.fill = GridBagConstraints.BOTH;

		sepPanel.add(sepFieldLabel, gbc);

		gbc.gridx = 1;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;

		sepPanel.add(sepField, gbc);

		gbc.gridx = 2;
		gbc.weightx = 0;
		gbc.insets = new Insets(0, 5, 0, 5);
		gbc.fill = GridBagConstraints.BOTH;

		sepPanel.add(ntlSepFieldLabel, gbc);

		gbc.gridx = 3;
		gbc.weightx = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		gbc.fill = GridBagConstraints.BOTH;

		sepPanel.add(ntlSepField, gbc);
	}

	private void layoutVGPanel()
	{
		GridBagLayout gbl = new GridBagLayout();

		GridBagConstraints gbc = new GridBagConstraints();

		vGPanel.setLayout(gbl);

		gbc.gridx = 0;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.CENTER;

		vGPanel.add(autoVGCheckBox, gbc);

		gbc.gridx = 1;
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.EAST;

		vGPanel.add(gemenVGCheckBox, gbc);

		gbc.gridx = 2;
		gbc.weightx = 1;
		gbc.anchor = GridBagConstraints.WEST;
		vGPanel.add(versalVGCheckBox, gbc);
	}


	private void createTopPanel()
	{
		topPanel = getTopPanel();

		topPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
	}

	private void createAddValueAction()
	{
		addValueAction = getAddValueAction();
	}

	private void createRemoveValueAction()
	{
		removeValueAction = getRemoveValueAction();
	}

	private void createAddValueButton()
	{
		addValueButton = new JButton(addValueAction);
	}

	private void createRemoveValueButton()
	{
		removeValueButton = new JButton(removeValueAction);
	}

	private void createAutoVGCheckBox()
	{
		String lS = CHECKBOX_PERFORM_AUTOMATIC_VG_LS_PROPERTY;

		String autoText = mVDH.getLanguageString(lS);

		autoVGCheckBox = new JCheckBox(autoText, false);

		autoVGCheckBox.addItemListener(new AutoVGSelectListener());

		autoVGCheckBox.setEnabled(usesAutoVG());

		ItemListener aVGList = getAutoVGItemListener();

		if (usesAutoVG() && (aVGList != null))
		{
			autoVGCheckBox.addItemListener(getAutoVGItemListener());
		}
	}

	private void createGemenCheckBox()
	{
		String lS = CHECKBOX_GEMEN_LS_PROPERTY;

		String gemenText = mVDH.getLanguageString(lS);

		gemenVGCheckBox = new JCheckBox(gemenText, false);

		gemenVGCheckBox.setEnabled(false);

		ItemListener gVGList = getGemenItemListener();

		if (usesAutoVG() && (gVGList != null))
		{
			gemenVGCheckBox.addItemListener(getGemenItemListener());
		}
	}

	private void createVersalCheckBox()
	{
		String lS = CHECKBOX_VERSAL_LS_PROPERTY;

		String versalText = mVDH.getLanguageString(lS);

		versalVGCheckBox = new JCheckBox(versalText, false);

		versalVGCheckBox.setEnabled(false);

		ItemListener vVGList = getVersalItemListener();

		if (usesAutoVG() && (vVGList != null))
		{
			versalVGCheckBox.addItemListener(getVersalItemListener());
		}
	}

	private void groupCheckBoxes()
	{
		ButtonGroup group = new ButtonGroup();

		group.add(gemenVGCheckBox);

		group.add(versalVGCheckBox);
	}

	private void createTermDescriptiveLabel()
	{
		String lS = LABEL_TERM_DESCRIPTOR_LS_PROPERTY;

		String descText = mVDH.getLanguageString(lS);

		currentTermDescLabel = new JLabel(descText);

		currentTermDescLabel.setEnabled(usesTermDescription());
	}

	private void createTermLabel()
	{
		currentTermLabel = GUIUtilities.createSunkLabel("");

		GUIUtilities.applyLookAndFeelBackground(currentTermLabel, GUIUtilities.TYPE_LIGHTER_PANEL_BACKGROUND_COLOR);

		currentTermLabel.setEnabled(usesTermDescription());
	}

	private void createSepFieldLabel()
	{
		String sepLS = LABEL_SEPARATOR_LS_PROPERTY;

		String sepText = mVDH.getLanguageString(sepLS);

		sepFieldLabel = new JLabel(sepText);

		sepFieldLabel.setEnabled(usesSeparators());
	}

	private void createSepField()
	{
		sepField = new JTextField();

		sepField.setEnabled(usesSeparators());

		DocumentListener sepList = getSeparatorDocumentListener();

		if (usesSeparators() && (sepList != null))
		{
			sepField.getDocument().addDocumentListener(sepList);
		}
	}

	private void createNTLSepFieldLabel()
	{
		String ntlLS = LABEL_NTL_SEPARATOR_LS_PROPERTY;

		String ntlText = mVDH.getLanguageString(ntlLS);

		ntlSepFieldLabel = new JLabel(ntlText);

		ntlSepFieldLabel.setEnabled(usesSeparators());
	}

	private void createNTLSepField()
	{
		ntlSepField = new JTextField();

		ntlSepField.setEnabled(usesSeparators());

		DocumentListener ntlList = getNTLSeparatorDocumentListener();

		if (usesSeparators() && (ntlList != null))
		{
			ntlSepField.getDocument().addDocumentListener(ntlList);
		}
	}

	private void createDescriptivePanel()
	{
		descPanel = new JPanel();

		layoutDescriptivePanel();
	}

	private void createSeparatorPanel()
	{
		sepPanel = new JPanel();

		layoutSeparatorPanel();
	}

	private void createVGPanel()
	{
		vGPanel = new JPanel();

		layoutVGPanel();
	}

	private void createBottomPanel()
	{
		bottomPanel = new JPanel();

		layoutBottomPanel();
	}


	private void initSimpleMembers()
	{
		actions = new HashMap();

		mVDH = MedViewDataHandler.instance();
	}

	private void initGUIComponents()
	{
		createAddValueAction();

		createRemoveValueAction();

		createAddValueButton();

		createRemoveValueButton();

		createAutoVGCheckBox();

		createGemenCheckBox();

		createVersalCheckBox();

		groupCheckBoxes();

		createTermDescriptiveLabel();

		createTermLabel();

		createSepFieldLabel();

		createSepField();

		createNTLSepFieldLabel();

		createNTLSepField();

		createDescriptivePanel();

		createSeparatorPanel();

		createVGPanel();

		createBottomPanel();

		createTopPanel();
	}

	private void layoutTermView()
	{
		setLayout(new BorderLayout());

		add(topPanel, BorderLayout.CENTER);

		add(bottomPanel, BorderLayout.SOUTH);
	}


	// CONSTRUCTOR

	protected AbstractTermView()
	{
		initSimpleMembers();

		initGUIComponents();

		layoutTermView();
	}


	// MEMBERS

	protected String term;

	protected HashMap actions;

	protected MedViewDataHandler mVDH;


	protected JButton addValueButton;

	protected JButton removeValueButton;


	protected JTextField sepField;

	protected JTextField ntlSepField;


	protected JCheckBox autoVGCheckBox;

	protected JCheckBox versalVGCheckBox;

	protected JCheckBox gemenVGCheckBox;


	protected JLabel currentTermLabel;

	protected JLabel currentTermDescLabel;

	protected JLabel ntlSepFieldLabel;

	protected JLabel sepFieldLabel;


	protected Action addValueAction;

	protected Action removeValueAction;


	private JPanel topPanel;

	private JPanel bottomPanel;

	private JPanel descPanel;

	private JPanel sepPanel;

	private JPanel vGPanel;


	// VG SELECT CHECKBOX LISTENER

	private class AutoVGSelectListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			gemenVGCheckBox.setEnabled(autoVGCheckBox.isSelected());

			versalVGCheckBox.setEnabled(autoVGCheckBox.isSelected());
		}
	}

	// ITEM LISTENER

	protected class ItemAdapter implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{}
	}

	// DOCUMENT LISTENER

	protected class DocumentAdapter implements DocumentListener
	{
		public void changedUpdate(DocumentEvent e)
		{}

		public void insertUpdate(DocumentEvent e)
		{}

		public void removeUpdate(DocumentEvent e)
		{}
	}

	// ADD VALUE

	protected class DefaultAddValueAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{}

		public DefaultAddValueAction()
		{
			super(ACTION_ADD_NEW_VALUE_LS_PROPERTY);

			setEnabled(usesValueButtons());
		}
	}

	// REMOVE VALUE

	protected class DefaultRemoveValueAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{}

		public DefaultRemoveValueAction()
		{
			super(ACTION_REMOVE_VALUE_LS_PROPERTY);

			setEnabled(usesValueButtons());
		}
	}

	/* NOTE: subclasses that want to add specific action
	 * handling to the 'add value' and 'remove value'
	 * buttons should subclass the DefaultXXXValueAction
	 * protected inner classes since they then get the
	 * same face in all term views. The only thing the
	 * subclassed actions need to do is to override the
	 * actionPerformed() method. */


	// MAIN METHOD

	public static void main(String[] args)
	{
		AbstractTermView view = new AbstractTermView()
		{
			public boolean usesAutoVG()
			{
				return true;
			}

			public boolean usesSeparators()
			{
				return true;
			}

			public boolean usesValueButtons()
			{
				return true;
			}

			public boolean usesTermDescription()
			{
				return true;
			}

			protected JPanel getTopPanel()
			{
				return new JPanel();
			}
		};

		JFrame frame = new JFrame();

		Container contentPane = frame.getContentPane();

		contentPane.add(view);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setVisible(true);

		frame.pack();
	}

}
