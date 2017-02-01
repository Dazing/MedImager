/*
 * @(#)MedSummaryToolBarHandler.java
 *
 * $Id: MedSummaryToolBarHandler.java,v 1.31 2008/07/29 09:31:59 it2aran Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.view;

import medview.datahandling.*;

import medview.medsummary.model.*;
import medview.medsummary.model.exceptions.CouldNotSetException;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;

import medview.common.actions.*;
import medview.common.components.*;
import medview.common.components.toolbar.*;
import medview.common.data.*;
import medview.common.dialogs.*;
import medview.common.generator.*;
import medview.common.text.*;

import misc.gui.actions.*;
import misc.gui.constants.*;
import misc.gui.utilities.*;

/**
 * Handles everything related to toolbar-handling
 * in the medsummary application. The major method
 * is the one that returns the toolbars that should
 * be used in the application (getToolbars()).
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MedSummaryToolBarHandler implements
	MedSummaryActions, MedViewLanguageConstants, MedViewTextConstants,
	ActionContainer, MedSummaryUserProperties, MedSummaryFlagProperties,
	GUIConstants, MedViewMediaConstants, MedSummaryConstants
{

	// ACTION OBTAINING

	public Action getAction(String actionName)
	{
		return (Action)actions.get(actionName);
	}


	// TOOLBAR OBTAINING

	public JToolBar[] getToolbars()
	{
		return new JToolBar[]
		{
			createGeneratorToolBar(), createGraphicalToolBar(),

			createPatientToolBar(), createGeneratedToolBar()
		};

		/* NOTE: we create new ones, so that a look-and-feel
		   switch will work. It seems that Java, when the
		   look-and-feel changes, also forgets about the settings
		   made to the current toolbar(s), and resets everything. */
	}


	// TOOLBAR CREATION

	private JToolBar createGeneratorToolBar()
	{
		JToolBar generatorToolBar = new MedViewToolBar(TITLE_GENERATOR_TOOLBAR_LS_PROPERTY);

		// data location combo

		if (mVDH.getUserBooleanPreference(DISPLAY_DATA_LOCATION_COMBO_IN_TOOLBAR_PROPERTY,

			true, MedSummaryFlagProperties.class))
		{
			generatorToolBar.add(dataDescLabel);

			generatorToolBar.add(new MedViewToolBarLabelWrapper(dataLabel));

			generatorToolBar.add(dataMoreButton);
		}

		// component package combo

		generatorToolBar.addSeparator();

		generatorToolBar.add(packageDescLabel);

		MedViewToolBarComboBoxWrapper wrapper = new MedViewToolBarComboBoxWrapper(packageCombo);

		generatorToolBar.add(wrapper);

		// sections combo

		if (mVDH.getUserBooleanPreference(DISPLAY_SECTIONS_COMBO_IN_TOOLBAR_PROPERTY,

			true, MedSummaryFlagProperties.class))
		{
			generatorToolBar.addSeparator();

			generatorToolBar.add(sectionDescLabel);

			wrapper = new MedViewToolBarComboBoxWrapper(sectionCombo);

			generatorToolBar.add(wrapper);
		}

		// return toolbar

		return generatorToolBar;
	}

	private JToolBar createGraphicalToolBar()
	{
		JToolBar graphicalToolBar = new MedViewToolBar(TITLE_GRAPHICAL_TOOLBAR_LS_PROPERTY);

		// graphical template combo

		if (mVDH.getUserBooleanPreference(DISPLAY_GRAPHIC_TEMPLATE_COMBO_IN_TOOLBAR_PROPERTY,

			true, MedSummaryFlagProperties.class))
		{
			graphicalToolBar.add(gTemplateDescLabel);

			graphicalToolBar.add(new MedViewToolBarComboBoxWrapper(gTemplateCombo));
		}

		// return toolbar

		return graphicalToolBar;
	}

	private JToolBar createPatientToolBar()
	{
		JToolBar patientToolBar = new MedViewToolBar(TITLE_PATIENT_TOOLBAR_LS_PROPERTY);

		// refresh patients

		patientToolBar.add(new MedViewToolBarNormalButton(mS.getAction(REFRESH_PATIENTS_ACTION)));

		// new patient

		patientToolBar.add(new MedViewToolBarNormalButton(mS.getAction(NEW_PATIENT_ACTION)));

		// new daynote

		patientToolBar.add(new MedViewToolBarNormalButton(mS.getAction(NEW_DAYNOTE_ACTION)));

		// separator

		patientToolBar.addSeparator();

		// graph
		
		patientToolBar.add(new MedViewToolBarNormalButton(mS.getAction(SHOW_GRAPH_ACTION)));

		// invoke FASS

		patientToolBar.add(new MedViewToolBarNormalButton(mS.getAction(INVOKE_FASS_ACTION)));

		// pda export
		
		patientToolBar.add(new MedViewToolBarNormalButton(mS.getAction(PDA_EXPORT_ACTION)));
		
		// return toolbar

		return patientToolBar;
	}

	private JToolBar createGeneratedToolBar()
	{
		JToolBar generatedToolBar = new MedViewToolBar(TITLE_TEXT_TOOLBAR_LS_PROPERTY);

		generatedToolBar.add(new MedViewToolBarNormalButton(mS.getAction(SEK_CUT_ACTION)));

		generatedToolBar.add(new MedViewToolBarNormalButton(mS.getAction(SEK_COPY_ACTION)));

		generatedToolBar.add(new MedViewToolBarNormalButton(mS.getAction(SEK_PASTE_ACTION)));

		generatedToolBar.addSeparator();

		generatedToolBar.add(new MedViewToolBarNormalButton(mS.getAction(PRINT_JOURNAL_ACTION)));

		generatedToolBar.addSeparator();

		generatedToolBar.add(new MedViewToolBarNormalButton(mS.getAction(CHOOSE_COLOR_ACTION)));

		generatedToolBar.addSeparator();

		generatedToolBar.add(new MedViewToolBarToggleButton(mS.getAction(SEK_BOLD_ACTION)));

		generatedToolBar.add(new MedViewToolBarToggleButton(mS.getAction(SEK_ITALIC_ACTION)));

		generatedToolBar.add(new MedViewToolBarToggleButton(mS.getAction(SEK_UNDERLINE_ACTION)));

		generatedToolBar.addSeparator();

		generatedToolBar.add(new MedViewToolBarToggleButton(mS.getAction(TEXT_SUPERSCRIPT_ACTION)));

		generatedToolBar.add(new MedViewToolBarToggleButton(mS.getAction(TEXT_SUBSCRIPT_ACTION)));

		generatedToolBar.add(new MedViewToolBarToggleButton(mS.getAction(TEXT_STRIKETHROUGH_ACTION)));

		generatedToolBar.addSeparator();

		generatedToolBar.add(new MedViewToolBarToggleButton(mS.getAction(SEK_LEFT_JUSTIFY_ACTION)));

		generatedToolBar.add(new MedViewToolBarToggleButton(mS.getAction(SEK_CENTER_JUSTIFY_ACTION)));

		generatedToolBar.add(new MedViewToolBarToggleButton(mS.getAction(SEK_RIGHT_JUSTIFY_ACTION)));

		// return toolbar

		return generatedToolBar;
	}


	// UTILITY METHODS

	private void updateSections()
	{
		updatingSectionCombo = true;

		sectionCombo.removeAllItems();

		String[] sections = null;

		if (!model.containsSections())
		{
			sectionCombo.setEnabled(false);

			updateEngineBuilder();

			updatingSectionCombo = false;

			return; // ends method call
		}
		else
		{
			sections = model.getSections();
		}

		if ((sections != null) && (sections.length != 0))
		{
			sectionCombo.setEnabled(true);

			String allProp = COMBOBOX_ALL_SECTIONS_COMBO_CHOICE_LS_PROPERTY;

			String allChoice = mVDH.getLanguageString(allProp);

			sectionCombo.addItem(allChoice);

			for (int ctr = 0; ctr < sections.length; ctr++)
			{
				sectionCombo.addItem(sections[ctr]);
			}

			String lastProp = LAST_CHOSEN_SECTION_PROPERTY;

			Class userPropClass = MedSummaryUserProperties.class;

			if (mVDH.isUserPreferenceSet(lastProp, userPropClass))
			{
				sectionCombo.setSelectedItem(mVDH.getUserStringPreference(lastProp, "", userPropClass));
			}
		}
		else
		{
			sectionCombo.setEnabled(false);
		}

		updateEngineBuilder();

		updatingSectionCombo = false;
	}

	private void updatePackages()
	{
		updatingPackageCombo = true;

		// re-populate combo box list

		packageCombo.removeAllItems();

		DataComponentPackage[] packages = mS.getModel().getIncludedPackages(); // sorted

		for (int ctr=0; ctr<packages.length; ctr++)
		{
			packageCombo.addItem(packages[ctr]);
		}

		// if a package is current in model - try to have it selected in combo

		DataComponentPackage currModelPackage = mS.getModel().getCurrentPackage();

		if (currModelPackage != null)
		{
			DefaultComboBoxModel model = (DefaultComboBoxModel) packageCombo.getModel();

			int index = model.getIndexOf(currModelPackage);

			if (index != -1)
			{
				// the previous current is still in the list

				packageCombo.setSelectedIndex(index);
			}
		}

		// set current package

        if (packageCombo.getItemCount() > 0)
		{
			DataComponentPackage selectedPackage = (DataComponentPackage) packageCombo.getSelectedItem();

			if (!selectedPackage.equals(currModelPackage))
			{
				mS.getModel().setCurrentPackage(selectedPackage);

                mS.getModel().setLocalExaminationDataLocation(selectedPackage.getDatabaseLocation());
                try
				{
					mS.getModel().setTemplate(selectedPackage.getTemplateLocation());
				}
				catch (CouldNotSetException exc)
				{
					exc.printStackTrace();

					mVD.createAndShowErrorDialog(mS.getParentFrame(),

						mVDH.getLanguageString(ERROR_COULD_NOT_SET_DATA_COMPONENT_PACKAGE_LS_PROPERTY)); // PENDING MESSAGE
				}

				try
				{
					mS.getModel().setTranslator(selectedPackage.getTranslatorLocation());
				}
				catch (CouldNotSetException exc)
				{
					exc.printStackTrace();

					mVD.createAndShowErrorDialog(mS.getParentFrame(),

						mVDH.getLanguageString(ERROR_COULD_NOT_SET_DATA_COMPONENT_PACKAGE_LS_PROPERTY)); // PENDING MESSAGE
				}
			}
		}
        updatingPackageCombo = false;
	}

	private void updateEngineBuilder()
	{
		if (sectionCombo.getItemCount() != 0)
		{
			String allProp = COMBOBOX_ALL_SECTIONS_COMBO_CHOICE_LS_PROPERTY;

			String allChoice = mVDH.getLanguageString(allProp);

			String selected = (String)sectionCombo.getSelectedItem();

			if (selected.equals(allChoice))
			{
				String[] sections = new String[sectionCombo.getItemCount() - 1];

				int sectCtr = 0;

				String curr = null;

				for (int ctr = 0; ctr < sections.length + 1; ctr++)
				{
					curr = (String)sectionCombo.getItemAt(ctr);

					if (!curr.equals(allChoice))
					{
						sections[sectCtr++] = curr;
					}
				}

				engineBuilder.buildSections(sections);
			}
			else
			{
				engineBuilder.buildSections(new String[]
					{selected});
			}
		}
		else
		{
			engineBuilder.removeSections();
		}
	}

	/* NOTE: since the build() methods in the updateEngineBuilder() method
	 * takes place at the ends of the code, any eventual exception will not
	 * affect the appearance of the chosen section in the combo. */


	// CONSTRUCTOR

	public MedSummaryToolBarHandler(MedSummaryFrame mS)
	{
		this.mS = mS;

		// model reference

		model = mS.getModel();

		// engine builder

		engineBuilder = model.getGeneratorEngineBuilder();

		// create and register local actions

		newPatientAction = new NewPatientAction(); // new patient action

		changeDataLocationAction = new ChangeDataLocationAction(); // change data location action

		invokeFASSAction = new InvokeFASSAction(); // invoke FASS action

		pdaExportAction = new PDAExportAction();

		actions.put(NEW_PATIENT_ACTION, newPatientAction);

		actions.put(CHANGE_DATA_LOCATION_ACTION, changeDataLocationAction);

		actions.put(INVOKE_FASS_ACTION, invokeFASSAction);

		mS.registerAction(PDA_EXPORT_ACTION, pdaExportAction);

		mS.registerAction(NEW_PATIENT_ACTION, newPatientAction);

		mS.registerAction(CHANGE_DATA_LOCATION_ACTION, changeDataLocationAction);

		mS.registerAction(INVOKE_FASS_ACTION, invokeFASSAction);

		// infoke FASS extra setup (action is in misc.components)

		invokeFASSAction.setURL(mVDH.getUserStringPreference(FASS_URL_PROPERTY,

			DEFAULT_FASS_URL, MedSummaryUserProperties.class));

		// descriptive labels

		dataDescLabel = new JLabel(mVDH.getLanguageString(LABEL_DATA_LOCATION_LS_PROPERTY));

		packageDescLabel =  new JLabel(mVDH.getLanguageString(LABEL_PACKAGES_LS_PROPERTY));

		sectionDescLabel =  new JLabel(mVDH.getLanguageString(LABEL_SECTION_LS_PROPERTY));

		gTemplateDescLabel =  new JLabel(mVDH.getLanguageString(LABEL_GRAPHIC_TEMPLATES_LS_PROPERTY));

		// sunk labels

		dataLabel = new JLabel(model.getDataLocationID());

		dataLabel = GUIUtilities.createSunkLabel(model.getDataLocationID());

		GUIUtilities.applyLookAndFeelBackground(dataLabel, TYPE_LIGHTER_PANEL_BACKGROUND_COLOR); // lighten up

		dataLabel.setPreferredSize(new Dimension(LABEL_WIDTH_NORMAL, COMBOBOX_HEIGHT_NORMAL)); // same height as combos

		// combo box - packages

		packageCombo = new JComboBox();

		packageCombo.setPreferredSize(new Dimension(COMBOBOX_WIDTH_NORMAL, COMBOBOX_HEIGHT_NORMAL));

		packageCombo.addItemListener(new CheckListener());

		updatePackages(); // see utility methods...

		// combo box - sections

		sectionCombo = new JComboBox();

		sectionCombo.setPreferredSize(new Dimension(COMBOBOX_WIDTH_NORMAL, COMBOBOX_HEIGHT_NORMAL));

		sectionCombo.addItemListener(new CheckListener());

		updateSections(); // see utility methods...

		// combo box - graphical template

		String[] t = mS.getAvailableGraphicTemplates();

		String cT = mS.getGraphicTemplateInUse();

		gTemplateCombo = new JComboBox(t);

		gTemplateCombo.setPreferredSize(new Dimension(COMBOBOX_WIDTH_LARGE, COMBOBOX_HEIGHT_NORMAL));

		gTemplateCombo.setSelectedItem(cT);

		gTemplateCombo.addItemListener(new CheckListener());

		// more buttons

		dataMoreButton = new MedViewMoreButton(changeDataLocationAction);

		// listeners

		model.addMedSummaryModelListener(new ModelListener());

		mVDH.addMedViewPreferenceListener(new PreferenceListener());
	}

	// MEMBERS

	private MedSummaryFrame mS;

	private MedSummaryModel model;

	private MedViewGeneratorEngineBuilder engineBuilder;


	private HashMap actions = new HashMap();

	private MedViewDialogs mVD = MedViewDialogs.instance();

	private MedViewDataHandler mVDH = MedViewDataHandler.instance();


	private boolean updatingSectionCombo = false;

	private boolean updatingPackageCombo = false;


	// action members

	private Action newPatientAction;

	private Action changeDataLocationAction;

	private InvokeFASSAction invokeFASSAction;

	private Action pdaExportAction;


	// subcomponent members

	private JLabel dataDescLabel;

	private JLabel dataLabel;

	private JButton dataMoreButton;


	private JLabel packageDescLabel;

	private JComboBox packageCombo;


	private JLabel gTemplateDescLabel; // graphical template

	private JComboBox gTemplateCombo; // graphical template


	private JLabel sectionDescLabel;

	private JComboBox sectionCombo;


	// ACTIONS

	private class ChangeDataLocationAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			String chosenMVD = mVD.createAndShowLoadMVDDialog(mS.getParentFrame());

			if (chosenMVD != null)
			{
				model.setLocalExaminationDataLocation(chosenMVD);

				newPatientAction.setEnabled(true); // also in listener
			}
		}

		public ChangeDataLocationAction()
		{
			super(ACTION_CHANGE_DATA_LOCATION_LS_PROPERTY);

			setEnabled(!model.usesRemoteDataHandling());
		}
	}

	private class NewPatientAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			mS.initiateFeeder();
		}

		public NewPatientAction()
		{
			super(ACTION_NEW_PATIENT_LS_PROPERTY, NEW_PATIENT_IMAGE_ICON_24);

			setEnabled(model.isExaminationDataLocationSet());
		}
	}

	private class PDAExportAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			// open pda dialog
			PDAExportDialog pdaDialog = new PDAExportDialog(mS, model);
			pdaDialog.setVisible(true);
		}

		public PDAExportAction()
		{
			super(ACTION_PDA_EXPORT_LS_PROPERTY, PDA_IMAGE_ICON_24);
			setEnabled(true);
		}
	}

	// CHECKBOX LISTENER

	private class CheckListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			if (e.getSource() == sectionCombo)
			{
				if ((!updatingSectionCombo) && (e.getStateChange() == ItemEvent.SELECTED))
				{
					String cS = (String)sectionCombo.getSelectedItem();

					Class uPClass = MedSummaryUserProperties.class;

					mVDH.setUserStringPreference(LAST_CHOSEN_SECTION_PROPERTY, cS, uPClass);

					updateEngineBuilder();
				}
			}
			else if (e.getSource() == gTemplateCombo)
			{
				if (e.getStateChange() == ItemEvent.SELECTED)
				{
					String chosenGTemplate = (String)gTemplateCombo.getSelectedItem();

					mS.setGraphicTemplateToUse(chosenGTemplate);
				}
			}
			else if (e.getSource() == packageCombo)
			{
				if (!updatingPackageCombo && (e.getStateChange() == ItemEvent.SELECTED))
				{
					DataComponentPackage selPackage = (DataComponentPackage) packageCombo.getSelectedItem();

					mS.getModel().setCurrentPackage(selPackage);
                    
                    mS.getModel().setLocalExaminationDataLocation(selPackage.getDatabaseLocation());
                    
                    try
					{
						mS.getModel().setTemplate(selPackage.getTemplateLocation());
					}
					catch (CouldNotSetException exc)
					{
						exc.printStackTrace();

						mVD.createAndShowErrorDialog(mS.getParentFrame(),

							mVDH.getLanguageString(ERROR_COULD_NOT_SET_DATA_COMPONENT_PACKAGE_LS_PROPERTY)); // PENDING MESSAGE
					}

					try
					{
						mS.getModel().setTranslator(selPackage.getTranslatorLocation());
					}
					catch (CouldNotSetException exc)
					{
						exc.printStackTrace();

						mVD.createAndShowErrorDialog(mS.getParentFrame(),

							mVDH.getLanguageString(ERROR_COULD_NOT_SET_DATA_COMPONENT_PACKAGE_LS_PROPERTY)); // PENDING MESSAGE
					}
				}
			}
		}
	}


	// PREFERENCE LISTENER

	private class PreferenceListener implements MedViewPreferenceListener
	{
		public void systemPreferenceChanged(MedViewPreferenceEvent e)
		{
		}

		public void userPreferenceChanged(MedViewPreferenceEvent e)
		{
			if (e.getPreferenceName().equals(FASS_URL_PROPERTY))
			{
				invokeFASSAction.setURL(mVDH.getUserStringPreference(

					FASS_URL_PROPERTY, DEFAULT_FASS_URL, MedSummaryUserProperties.class));
			}
		}
	}


	// MODEL LISTENER

	private class ModelListener implements MedSummaryModelListener, MedSummaryUserProperties
	{
		public void dataLocationChanged(MedSummaryModelEvent e)
		{
			changeDataLocationAction.setEnabled(!model.usesRemoteDataHandling());

			newPatientAction.setEnabled(model.isExaminationDataLocationSet()); // also in action
		}

		public void dataLocationIDChanged(MedSummaryModelEvent e)
		{
			dataLabel.setText(model.getDataLocationID());
		}

		public void templateChanged(MedSummaryModelEvent e)
		{
			updateSections();
		}

		public void sectionsChanged(MedSummaryModelEvent e)
		{
			updateSections();
		}

		public void includedPackagesUpdated(MedSummaryModelEvent e)
		{
			updatePackages();
		}

		public void currentPackageChanged(MedSummaryModelEvent e)
		{
			updatePackages();
		}

		public void translatorChanged(MedSummaryModelEvent e)
		{
		}

		public void patientsChanged(MedSummaryModelEvent e)
		{
		}

		public void examinationAdded(MedSummaryModelEvent e)
		{
		}

		public void examinationUpdated(MedSummaryModelEvent e)
		{
		}

		public void templateIDChanged(MedSummaryModelEvent e)
		{
		}

		public void translatorIDChanged(MedSummaryModelEvent e)
		{
		}

		public void documentReplaced(MedSummaryModelEvent e)
		{
		}
	}
}
