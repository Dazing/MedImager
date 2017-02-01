/*
 * @(#)MedSummaryPictureTextSCP.java
 *
 * $id$
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.medsummary.view.settings;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import medview.common.components.*;
import medview.common.dialogs.*;
import medview.common.dialogs.settings.*;

import medview.datahandling.*;

import medview.medsummary.view.*;
import medview.medsummary.model.*;

import misc.domain.*;

import misc.gui.components.*;
import misc.gui.constants.*;
import misc.gui.print.*;

/**
 * Contains settings for the summary panel containing
 * the generated text's drop image support. Note that
 * this settings content panel will be constructed after
 * the summary panel, which means that all the relevant
 * properties have been set to their default values by
 * the summary panel constructor. Therefore, there is
 * no need to check whether the properties has yet been
 * set.
 *
 * @author Fredrik Lindahl
 */
public class MedSummaryPictureTextSCP extends SettingsContentPanel implements
	MedViewLanguageConstants, GUIConstants, MedSummaryUserProperties,
	MedSummaryConstants, MedSummaryFlagProperties
{

	public String getTabLS()
	{
		return TAB_INSERTED_IMAGES_IN_TEXT_LS_PROPERTY;
	}

	public String getTabDescLS()
	{
		return TAB_INSERTED_IMAGES_IN_TEXT_DESCRIPTION_LS_PROPERTY;
	}


	protected void settingsShown()
	{
		prepPhase = true; // indicate that we are in a preparatory phase (disable all events)

		Class userPropClass = MedSummaryUserProperties.class;

		Class flagPropClass = MedSummaryFlagProperties.class;

		String heightProp = MAXIMUM_HEIGHT_OF_INSERTED_IMAGES_PROPERTY;

		String padProp = PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_PROPERTY;

		String padVProp = PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_VERTICAL_PROPERTY;

		String padHProp = PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_HORIZONTAL_PROPERTY;

		String borderProp = USE_BORDER_AROUND_INSERTED_IMAGES_PROPERTY;

		String borderTypeProp = BORDER_TYPE_AROUND_INSERTED_IMAGES_PROPERTY;

		int defInsertHeight = DEFAULT_HEIGHT_OF_INSERTED_IMAGES;

		int defPadV = DEFAULT_EMPTY_SPACE_AROUND_INSERTED_IMAGES_VERTICAL;

		int defPadH = DEFAULT_EMPTY_SPACE_AROUND_INSERTED_IMAGES_HORIZONTAL;

		maxHeightField.setText(mVDH.getUserIntPreference(heightProp, defInsertHeight, userPropClass) + "");

		padEmptyCheckBox.setSelected(mVDH.getUserBooleanPreference(padProp, true, flagPropClass)); // true - default

		setPadSectionEnabled(padEmptyCheckBox.isSelected());

		useBorderCheckBox.setSelected(mVDH.getUserBooleanPreference(borderProp, true, flagPropClass)); // true - default

		setBorderSectionEnabled(useBorderCheckBox.isSelected());

		verticalField.setText(mVDH.getUserIntPreference(padVProp, defPadV, userPropClass) + "");

		horizontalField.setText(mVDH.getUserIntPreference(padHProp, defPadH, userPropClass) + "");

		switch (mVDH.getUserIntPreference(borderTypeProp, PageRenderer.IMAGE_BORDER_BLACK, userPropClass))
		{
			case PageRenderer.IMAGE_BORDER_BLACK:
			{
				borderRadioGroup.setSelected(blackRadioButton.getModel(), true);

				break;
			}
			case PageRenderer.IMAGE_BORDER_SHADOW:
			{
				borderRadioGroup.setSelected(shadowRadioButton.getModel(), true);

				break;
			}
			default:
			{
				// the radio buttons are disabled, doesn't matter...
			}
		}

		prepPhase = false;
	}


	protected void initSubMembers()
	{
		medSummary = (MedSummaryFrame) subConstructorData[0];
	}

	protected void createComponents()
	{
		String lS = null;

		int cCS = GUIConstants.COMPONENT_COMPONENT_SPACING;


		// labels

		lS = LABEL_MAX_HEIGHT_FOR_INSERTED_PICTURE_LS_PROPERTY;

		maxHeightLabel = new MedViewLabel(lS);

		lS = LABEL_PIXELS_LS_PROPERTY;

		pixelLabel1 = new MedViewLabel(lS); // only one parent

		pixelLabel2 = new MedViewLabel(lS);

		pixelLabel3 = new MedViewLabel(lS);

		lS = LABEL_OBSERVANDUM_PICTURE_TEXT_LS_PROPERTY;

		observandumLabel = new MedViewMultiLineLabel(lS);

		lS = LABEL_VERTICAL_LS_PROPERTY;

		verticalLabel = new MedViewLabel(lS);

		lS = LABEL_HORIZONTAL_LS_PROPERTY;

		horizontalLabel = new MedViewLabel(lS);


		// text fields

		verticalField = new JTextField(4);

		maxHeightField = new JTextField(4);

		horizontalField = new JTextField(4);

		verticalField.setHorizontalAlignment(JTextField.RIGHT);

		maxHeightField.setHorizontalAlignment(JTextField.RIGHT);

		horizontalField.setHorizontalAlignment(JTextField.RIGHT);


		// check boxes

		lS = CHECKBOX_PAD_EMPTY_AROUND_PICTURE_LS_PROPERTY;

		padEmptyCheckBox = new MedViewCheckBox(lS, null, true); // lite fult

		lS = CHECKBOX_USE_BORDER_AROUND_PICTURE_LS_PROPERTY;

		useBorderCheckBox = new MedViewCheckBox(lS, null, true); // lite fult


		// radio buttons

		lS = RADIO_BUTTON_BLACK_LS_PROPERTY;

		blackRadioButton = new MedViewRadioButton(lS);

		lS = RADIO_BUTTON_SHADOW_LS_PROPERTY;

		shadowRadioButton = new MedViewRadioButton(lS);


		// button groups

		borderRadioGroup = new ButtonGroup();

		borderRadioGroup.add(blackRadioButton);

		borderRadioGroup.add(shadowRadioButton);


		// panels

		maxHeightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,cCS,0)); // hgap, vgap

		useBorderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,cCS,0));

		padEmptyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,cCS,0));

		maxHeightPanel.add(maxHeightLabel);

		maxHeightPanel.add(maxHeightField);

		maxHeightPanel.add(pixelLabel1);

		padEmptyPanel.add(verticalLabel);

		padEmptyPanel.add(verticalField);

		padEmptyPanel.add(pixelLabel2);

		padEmptyPanel.add(horizontalLabel);

		padEmptyPanel.add(horizontalField);

		padEmptyPanel.add(pixelLabel3);

		useBorderPanel.add(blackRadioButton);

		useBorderPanel.add(shadowRadioButton);


		// listeners

		listener = new Listener();

		padEmptyCheckBox.addItemListener(listener);

		useBorderCheckBox.addItemListener(listener);

		blackRadioButton.addItemListener(listener);

		shadowRadioButton.addItemListener(listener);

		verticalField.getDocument().addDocumentListener(listener);

		horizontalField.getDocument().addDocumentListener(listener);

		maxHeightField.getDocument().addDocumentListener(listener);
	}

	protected void layoutPanel()
	{
		setLayout(new GridBagLayout());

		int both = GridBagConstraints.BOTH;

		int none = GridBagConstraints.NONE;

		int west = GridBagConstraints.WEST;

		int east = GridBagConstraints.EAST;

		int cent = GridBagConstraints.CENTER;

		int cGS = GUIConstants.COMPONENT_GROUP_SPACING;

		int cCS = GUIConstants.COMPONENT_COMPONENT_SPACING;

		add(maxHeightPanel,0,0,0,0,5,new Insets(0,0,cCS,0),both,cent);

		add(observandumLabel,0,1,100,0,5,new Insets(0,0,cGS,0),both,cent);

		add(new JSeparator(),0,2,0,0,5,new Insets(0,0,cGS,0),both,cent);

		add(padEmptyCheckBox,0,3,0,0,1,new Insets(0,0,cGS,cCS),none,west);

		add(padEmptyPanel,1,3,0,0,4,new Insets(0,0,cGS,0),both,cent);

		add(useBorderCheckBox,0,4,0,0,1,new Insets(0,0,cGS,cCS),none,west);

		add(useBorderPanel,1,4,0,0,4,new Insets(0,0,cGS,0),both,cent);

		add(Box.createGlue(),0,5,0,100,5,new Insets(0,0,0,0),both,cent);
	}

	private void add(Component comp, int x, int y, int weightx, int weighty,
		int width, Insets insets, int fill, int anchor)
	{
		if (gbc == null)
		{
			gbc = new GridBagConstraints();
		}

		gbc.gridx = x;

		gbc.gridy = y;

		gbc.fill = fill;

		gbc.weightx = weightx;

		gbc.weighty = weighty;

		gbc.gridwidth = width;

		gbc.anchor = anchor;

		gbc.insets = insets;

		add(comp, gbc);
	}

	private void setPadSectionEnabled(boolean enabled)
	{
		verticalLabel.setEnabled(enabled);

		verticalField.setEnabled(enabled);

		horizontalLabel.setEnabled(enabled);

		horizontalField.setEnabled(enabled);

		pixelLabel2.setEnabled(enabled);

		pixelLabel3.setEnabled(enabled);
	}

	private void setBorderSectionEnabled(boolean enabled)
	{
		blackRadioButton.setEnabled(enabled);

		shadowRadioButton.setEnabled(enabled);
	}



	public MedSummaryPictureTextSCP(CommandQueue queue, MedSummaryFrame medSummary)
	{
		super(queue, new Object[] { medSummary });
	}

	private boolean prepPhase;

	private MedSummaryFrame medSummary;

	private GridBagConstraints gbc;

	private JPanel useBorderPanel;

	private JPanel padEmptyPanel;

	private JPanel maxHeightPanel;

	private JLabel pixelLabel1;

	private JLabel pixelLabel2;

	private JLabel pixelLabel3;

	private JLabel maxHeightLabel;

	private JLabel verticalLabel;

	private JLabel horizontalLabel;

	private JTextField verticalField;

	private JTextField horizontalField;

	private JTextField maxHeightField;

	private JCheckBox padEmptyCheckBox;

	private JCheckBox useBorderCheckBox;

	private JRadioButton blackRadioButton;

	private JRadioButton shadowRadioButton;

	private MultiLineLabel observandumLabel;

	private ButtonGroup borderRadioGroup;

	private Listener listener;


	/* NOTE: the superclass protected variable
	 * 'subConstructorData' has the purpose of allowing
	 * the subclasses to pass some data that needs
	 * to be initialized before the other template
	 * methods are called. In this case, the
	 * mediator reference needs to be set up before
	 * the panel is laid out, since the various
	 * subpanels in use in the main panel needs to
	 * obtain information from the mediator. You
	 * cannot set the mediator reference after the
	 * superclass constructor has been called since
	 * the methods would then only have a null
	 * reference. */




	private class Listener implements ItemListener, DocumentListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			if (prepPhase) { return; } // ignore events during 'settingsShown()' setup

			Class userPropClass = MedSummaryUserProperties.class;

			Class flagPropClass = MedSummaryFlagProperties.class;

			boolean sel = e.getStateChange() == ItemEvent.SELECTED;

			if (e.getSource() == padEmptyCheckBox)
			{
				setPadSectionEnabled(sel);

				String prop = PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_PROPERTY;

				commandQueue.addToQueue(new ChangeFlagCommand(prop, sel, flagPropClass));
			}
			else if (e.getSource() == useBorderCheckBox)
			{
				setBorderSectionEnabled(sel);

				String prop = USE_BORDER_AROUND_INSERTED_IMAGES_PROPERTY;

				commandQueue.addToQueue(new ChangeFlagCommand(prop, sel, flagPropClass));
			}
			else if ((e.getSource() == blackRadioButton) && sel) // button group (1 sel - 1 desel)
			{
				String prop = BORDER_TYPE_AROUND_INSERTED_IMAGES_PROPERTY;

				String type = PageRenderer.IMAGE_BORDER_BLACK + "";

				commandQueue.addToQueue(new ChangeUserStringPropertyCommand(prop, type, userPropClass));
			}
			else if ((e.getSource() == shadowRadioButton) && sel) // button group (1 sel - 1 desel)
			{
				String prop = BORDER_TYPE_AROUND_INSERTED_IMAGES_PROPERTY;

				String type = PageRenderer.IMAGE_BORDER_SHADOW + "";

				commandQueue.addToQueue(new ChangeUserStringPropertyCommand(prop, type, userPropClass));
			}
		}

		public void changedUpdate(DocumentEvent e)
 		{
			// attributes not used in any way here...
		}

		public void insertUpdate(DocumentEvent e)
 		{
			if (prepPhase) { return; }

			checkDocument(e.getDocument());
		}

		public void removeUpdate(DocumentEvent e)
		{
			if (prepPhase) { return; }

			checkDocument(e.getDocument());
		}

		private void checkDocument(javax.swing.text.Document doc)
		{
			Class userPropClass = MedSummaryUserProperties.class;

			if (doc == maxHeightField.getDocument())
			{
				String height = maxHeightField.getText();

				String prop = MAXIMUM_HEIGHT_OF_INSERTED_IMAGES_PROPERTY;

				commandQueue.addToQueue(new ChangeUserStringPropertyCommand(prop, height, userPropClass));
			}
			else if (doc == horizontalField.getDocument())
			{
				String pad = horizontalField.getText();

				String prop = PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_HORIZONTAL_PROPERTY;

				commandQueue.addToQueue(new ChangeUserStringPropertyCommand(prop, pad, userPropClass));
			}
			else if (doc == verticalField.getDocument())
			{
				String pad = verticalField.getText();

				String prop = PAD_EMPTY_SPACE_AROUND_INSERTED_IMAGES_VERTICAL_PROPERTY;

				commandQueue.addToQueue(new ChangeUserStringPropertyCommand(prop, pad, userPropClass));
			}
		}
	}
}
