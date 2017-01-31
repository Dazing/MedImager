/*
 * @(#)TemplateSettingsContentPanel.java
 *
 * $Id: TemplateSettingsContentPanel.java,v 1.5 2006/04/24 14:17:34 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view.settings;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;

import medview.common.actions.*;
import medview.common.components.*;
import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.summarycreator.model.*;
import medview.summarycreator.view.*;

import misc.domain.*;

import misc.gui.constants.*;
import misc.gui.utilities.*;

public class TemplateSettingsContentPanel extends SettingsContentPanel implements
	MedViewLanguageConstants, GUIConstants
{

// **********************************************************************************************
// ------------------------- VARIOUS OVERRIDDEN AND IMPLEMENTED METHODS -------------------------
// **********************************************************************************************

	public Action getAction(String name)
	{
		return (Action) actions.get(name);
	}

	public String getTabLS()
	{
		return TAB_SUMMARYCREATOR_TEMPLATE_LS_PROPERTY;
	}

	public String getTabDescLS()
	{
		return TAB_SUMMARYCREATOR_TEMPLATE_DESCRIPTION_LS_PROPERTY;
	}

	protected void initSubMembers()
	{
		actions = new HashMap();

		listener = new Listener();

		mediator = (SummaryCreatorFrame) subConstructorData[0];
	}

	protected void settingsShown()
	{
		Class userPropClass = SummaryCreatorUserProperties.class;

		Class flagPropClass = SummaryCreatorFlagProperties.class;

		String previewProp = SummaryCreatorUserProperties.PREVIEW_PCODE_PROPERTY;

		String askProp = SummaryCreatorFlagProperties.ASK_BEFORE_REMOVE_SECTION_PROPERTY;

		previewPCodeTextField.setText(mVDH.getUserStringPreference(previewProp, "", userPropClass));

		sectionAskCheckBox.setSelected(mVDH.getUserBooleanPreference(askProp, true, flagPropClass));
	}

// **********************************************************************************************
// ----------------------------------------------------------------------------------------------
// **********************************************************************************************





// **********************************************************************************************
// -------------------------------- LAYOUT AND CREATIONAL METHODS -------------------------------
// **********************************************************************************************

	protected void createComponents()
	{
		// preview pcode field

		String previewLabelLS = LABEL_PREVIEW_PCODE_LS_PROPERTY;

		String previewLabelText = mVDH.getLanguageString(previewLabelLS);

		previewPCodeDescLabel = new JLabel(previewLabelText, SwingConstants.RIGHT);

		int fieldWidth = GUIConstants.TEXTFIELD_WIDTH_VERY_LARGE;

		int fieldHeight = GUIConstants.TEXTFIELD_HEIGHT_NORMAL;

		Dimension dim = new Dimension(fieldWidth, fieldHeight);

		String initial = getInitialPCodeToDisplay();

		previewPCodeTextField = new JTextField(initial);

		previewPCodeTextField.setEditable(false);

		previewPCodeTextField.setPreferredSize(dim);

		previewPCodeTextField.setBackground(Color.white);

		previewPCodeAction = new PreviewPCodeAction();

		String actionName = SummaryCreatorActions.SET_PREVIEW_PCODE_ACTION;

		actions.put(actionName, previewPCodeAction);

		mediator.registerAction(actionName, previewPCodeAction);

		previewPCodeMoreButton = new MedViewMoreButton(previewPCodeAction);

		// ask before section removal

		Class flagPropClass = SummaryCreatorUserProperties.class;

		String askCheckBoxLS = CHECKBOX_ASK_BEFORE_SECTION_REMOVAL_LS_PROPERTY;

		String sProp = SummaryCreatorFlagProperties.ASK_BEFORE_REMOVE_SECTION_PROPERTY;

		sectionAskCheckBox = new MedViewCheckBox(askCheckBoxLS, sProp, true, flagPropClass);

		sectionAskCheckBox.addItemListener(new QueueFlagItemListener(sProp, flagPropClass));

		flagPanel = new JPanel(new GridLayout(3,2)); // rows, cols...

		flagPanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));

		flagPanel.add(sectionAskCheckBox);
	}

	protected void layoutPanel()
	{
		int cGS = GUIConstants.COMPONENT_GROUP_SPACING;

		int cCS = GUIConstants.COMPONENT_COMPONENT_SPACING;

		GUIUtilities.gridBagAdd(this, previewPCodeDescLabel,		0, 0, 1, 1, 1, 0, EAST, new Insets(0,0,cGS,cCS), BOTH);

		GUIUtilities.gridBagAdd(this, previewPCodeTextField,		1, 0, 1, 1, 0, 0, CENT, new Insets(0,0,cGS,0), BOTH);

		GUIUtilities.gridBagAdd(this, previewPCodeMoreButton,		2, 0, 1, 1, 0, 0, CENT, new Insets(0,0,cGS,0), BOTH);

		GUIUtilities.gridBagAdd(this, new JSeparator(),			0, 1, 3, 1, 0, 0, CENT, new Insets(0,0,cGS,0), BOTH);

		GUIUtilities.gridBagAdd(this, flagPanel,			0, 2, 3, 1, 0, 0, CENT, new Insets(0,0,0,0), BOTH);

		GUIUtilities.gridBagAdd(this, Box.createGlue(),			0, 3, 3, 1, 0, 1, CENT, new Insets(0,0,0,0), BOTH);
	}

// **********************************************************************************************
// ----------------------------------------------------------------------------------------------
// **********************************************************************************************





// **********************************************************************************************
// -------------------------------------- UTILITY METHODS ---------------------------------------
// **********************************************************************************************

	private String getInitialPCodeToDisplay()
	{
		Class userPropClass = SummaryCreatorUserProperties.class;

		String userProp = SummaryCreatorUserProperties.PREVIEW_PCODE_PROPERTY;

 		String ret = mVDH.getUserStringPreference(userProp, null, userPropClass);

		if (ret == null)
		{
			ret = mVDH.getExamplePCode();

			mVDH.setUserStringPreference(userProp, ret, userPropClass);
		}

		return ret;
	}

// **********************************************************************************************
// ----------------------------------------------------------------------------------------------
// **********************************************************************************************





	public TemplateSettingsContentPanel(CommandQueue queue, SummaryCreatorFrame mediator)
	{
		super(queue, new Object[] { mediator });

		mVDH.addMedViewPreferenceListener(listener);
	}

	private HashMap actions;

	private JPanel flagPanel;

	private Listener listener;

	private SummaryCreatorFrame mediator;

	private Action previewPCodeAction;

	private JLabel previewPCodeDescLabel;

	private JButton previewPCodeMoreButton;

	private JTextField previewPCodeTextField;

	private JCheckBox sectionAskCheckBox;





// **********************************************************************************************
// ---------------------------------------- INNER CLASSES ---------------------------------------
// **********************************************************************************************

	private class Listener implements MedViewPreferenceListener
	{
		public void userPreferenceChanged(MedViewPreferenceEvent e)
		{
			String propName = SummaryCreatorUserProperties.PREVIEW_PCODE_PROPERTY;

			if (e.getPreferenceName().equals(propName))
			{
				Class propClass = SummaryCreatorUserProperties.class;

				String setPCode = mVDH.getUserStringPreference(propName, null, propClass);

				previewPCodeTextField.setText(setPCode);
			}
		}

		public void systemPreferenceChanged(MedViewPreferenceEvent e) {}
	}

	private class PreviewPCodeAction extends MedViewAction
	{
		public void actionPerformed(ActionEvent e)
		{
			MedViewDialogs mVD = MedViewDialogs.instance();

			MedViewDialog d = mVD.createChangePCodeDialog(mediator.getParentFrame());

			d.show();

			if (!(d.wasDismissed()))
			{
				Class propClass = SummaryCreatorUserProperties.class;

				String propName = SummaryCreatorUserProperties.PREVIEW_PCODE_PROPERTY;

				String oldPCode = mVDH.getUserStringPreference(propName, "", propClass);

				String setPCode = (String) d.getObjectData();

				if (setPCode != null)
				{
					if (mVDH.validates(setPCode) && !(setPCode.equals(oldPCode)))
					{
						commandQueue.addToQueue(new ChangePreviewPCodeCommand(setPCode));

						previewPCodeTextField.setText(setPCode);
					}
				}
			}
		}

		public PreviewPCodeAction()
		{
			super(ACTION_SET_PREVIEW_PCODE_LS_PROPERTY);
		}
	}

	private class QueueFlagItemListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			JCheckBox source = (JCheckBox) e.getSource();

			boolean value = source.isSelected();

			commandQueue.addToQueue(new ChangeFlagCommand(prop, value, propClass));
		}

		public QueueFlagItemListener(String prop, Class propClass)
		{
			this.prop = prop;

			this.propClass = propClass;
		}

		private String prop;

		private Class propClass;
	}

	private class ChangePreviewPCodeCommand implements Command
	{
		public boolean shouldReplace(Command command)
		{
			return (command instanceof ChangePreviewPCodeCommand);
		}

		public void execute()
		{
			MedViewDataHandler mVDH = MedViewDataHandler.instance();

			String prop = SummaryCreatorUserProperties.PREVIEW_PCODE_PROPERTY;

			Class userPropClass = SummaryCreatorUserProperties.class;

			mVDH.setUserStringPreference(prop, pCode, userPropClass);
		}

		public ChangePreviewPCodeCommand(String newPCode)
		{
			this.pCode = newPCode;
		}

		private String pCode;
	}

// ***********************************************************************************
// -----------------------------------------------------------------------------------
// ***********************************************************************************

}
