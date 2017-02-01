/*
 * @(#)MedViewTextActionCollection.java
 *
 * $Id: MedViewTextActionCollection.java,v 1.11 2010/06/28 07:12:39 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.text;

import medview.common.*;
import medview.common.dialogs.*;
import medview.datahandling.*;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import misc.gui.constants.*;

public class MedViewTextActionCollection implements MedViewTextConstants, MedViewMediaConstants, MedViewLanguageConstants
{
	public MedViewTextActionCollection()
	{
		this(null);
	}

	public MedViewTextActionCollection(JTextPane textPane)
	{
		// initialize actions

		initializeActions();

		// attach text pane

		setAttachedTextPane(textPane);
	}

	private MedViewDataHandler mVDH;

	private HashMap actions = null;

	private Vector toggleVector = null;

	private JTextPane textPane = null;

	private CaretListener caretListener = null;

	private String[] allActions = new String[]
	{
			MedViewTextConstants.SEK_CUT_ACTION,
			MedViewTextConstants.SEK_COPY_ACTION,
			MedViewTextConstants.SEK_PASTE_ACTION,
			MedViewTextConstants.SEK_BOLD_ACTION,
			MedViewTextConstants.SEK_ITALIC_ACTION,
			MedViewTextConstants.SEK_UNDERLINE_ACTION,
			MedViewTextConstants.SEK_FONT_FAMILY_SANSSERIF_ACTION,
			MedViewTextConstants.SEK_FONT_FAMILY_MONOSPACED_ACTION,
			MedViewTextConstants.SEK_FONT_FAMILY_SERIF_ACTION,
			MedViewTextConstants.SEK_FONT_SIZE_8_ACTION,
			MedViewTextConstants.SEK_FONT_SIZE_10_ACTION,
			MedViewTextConstants.SEK_FONT_SIZE_11_ACTION,
			MedViewTextConstants.SEK_FONT_SIZE_12_ACTION,
			MedViewTextConstants.SEK_FONT_SIZE_14_ACTION,
			MedViewTextConstants.SEK_FONT_SIZE_16_ACTION,
			MedViewTextConstants.SEK_FONT_SIZE_18_ACTION,
			MedViewTextConstants.SEK_FONT_SIZE_24_ACTION,
			MedViewTextConstants.SEK_FONT_SIZE_36_ACTION,
			MedViewTextConstants.SEK_FONT_SIZE_48_ACTION,
			MedViewTextConstants.SEK_LEFT_JUSTIFY_ACTION,
			MedViewTextConstants.SEK_CENTER_JUSTIFY_ACTION,
			MedViewTextConstants.SEK_RIGHT_JUSTIFY_ACTION,
			MedViewTextConstants.TEXT_SUPERSCRIPT_ACTION,
			MedViewTextConstants.TEXT_SUBSCRIPT_ACTION,
			MedViewTextConstants.TEXT_STRIKETHROUGH_ACTION,
			MedViewTextConstants.CHOOSE_COLOR_ACTION
	};

	private String[] toggleActions = new String[]
	{
		MedViewTextConstants.SEK_BOLD_ACTION,
		MedViewTextConstants.SEK_ITALIC_ACTION,
		MedViewTextConstants.SEK_UNDERLINE_ACTION,
		MedViewTextConstants.SEK_FONT_FAMILY_SANSSERIF_ACTION,
		MedViewTextConstants.SEK_FONT_FAMILY_MONOSPACED_ACTION,
		MedViewTextConstants.SEK_FONT_FAMILY_SERIF_ACTION,
		MedViewTextConstants.SEK_FONT_SIZE_8_ACTION,
		MedViewTextConstants.SEK_FONT_SIZE_10_ACTION,
		MedViewTextConstants.SEK_FONT_SIZE_11_ACTION,
		MedViewTextConstants.SEK_FONT_SIZE_12_ACTION,
		MedViewTextConstants.SEK_FONT_SIZE_14_ACTION,
		MedViewTextConstants.SEK_FONT_SIZE_16_ACTION,
		MedViewTextConstants.SEK_FONT_SIZE_18_ACTION,
		MedViewTextConstants.SEK_FONT_SIZE_24_ACTION,
		MedViewTextConstants.SEK_FONT_SIZE_36_ACTION,
		MedViewTextConstants.SEK_FONT_SIZE_48_ACTION,
		MedViewTextConstants.SEK_LEFT_JUSTIFY_ACTION,
		MedViewTextConstants.SEK_CENTER_JUSTIFY_ACTION,
		MedViewTextConstants.SEK_RIGHT_JUSTIFY_ACTION,
		MedViewTextConstants.TEXT_SUPERSCRIPT_ACTION,
		MedViewTextConstants.TEXT_SUBSCRIPT_ACTION,
		MedViewTextConstants.TEXT_STRIKETHROUGH_ACTION
	};

	{
		mVDH = MedViewDataHandler.instance();
	}


	public Action getAction(String actionName)
	{
		return (Action) actions.get(actionName);
	}

	public Action[] getActions()
	{
		Action[] retArray = new Action[actions.size()];

		actions.values().toArray(retArray);

		return retArray;
	}

	public String[] getContainedActionNames()
	{
		return allActions;
	}

	public void enableActions()
	{
		for (Iterator iter = actions.values().iterator(); iter.hasNext();)
		{
			((Action)iter.next()).setEnabled(true);
		}
	}

	public void disableActions()
	{
		for (Iterator iter = actions.values().iterator(); iter.hasNext();)
		{
			((Action)iter.next()).setEnabled(false);
		}
	}

	public boolean isMedViewTextAction(String actionID)
	{
		for (int ctr=0; ctr<allActions.length; ctr++)
		{
			if (allActions[ctr].equals(actionID)) { return true; }
		}

		return false;
	}

	public void setAttachedTextPane(JTextPane tP)
	{
		if (textPane != null) { detachTPListener(); } // old text pane

		textPane = tP;

		switchTargetedActions();

		if (textPane == null)
		{
			disableActions();
		}
		else
		{
			attachTPListener();

			enableActions();
		}
	}

	protected void switchTargetedActions()
	{
		ChooseColorAction cCA = (ChooseColorAction) actions.get(CHOOSE_COLOR_ACTION);

		cCA.setTargetPane(textPane);
	}

	protected void attachTPListener()
	{
		caretListener = new CaretListener()
		{
			public void caretUpdate(CaretEvent e)
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						toggleActions(textPane.getInputAttributes());
					}
				});
			}
		};

		textPane.addCaretListener(caretListener);
	}

	protected void detachTPListener()
	{
		textPane.removeCaretListener(caretListener);
	}

	protected void toggleActions(AttributeSet attributes)
	{
		Boolean toggleStatus = null;

		Boolean oldToggleStatus = null;

		for (int ctr = 0; ctr<toggleVector.size(); ctr++)
		{
			Action currentAction = (Action) toggleVector.elementAt(ctr);

			if (toggleActions[ctr].equals(SEK_BOLD_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.isBold(attributes));
			}
			else if (toggleActions[ctr].equals(SEK_ITALIC_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.isItalic(attributes));
			}
			else if (toggleActions[ctr].equals(SEK_UNDERLINE_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.isUnderline(attributes));
			}
			else if (toggleActions[ctr].equals(TEXT_STRIKETHROUGH_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.isStrikeThrough(attributes));
			}
			else if (toggleActions[ctr].equals(TEXT_SUBSCRIPT_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.isSubscript(attributes));
			}
			else if (toggleActions[ctr].equals(TEXT_SUPERSCRIPT_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.isSuperscript(attributes));
			}
			else if (toggleActions[ctr].equals(SEK_LEFT_JUSTIFY_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.getAlignment(attributes) == StyleConstants.ALIGN_LEFT);
			}
			else if (toggleActions[ctr].equals(SEK_CENTER_JUSTIFY_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.getAlignment(attributes) == StyleConstants.ALIGN_CENTER);
			}
			else if (toggleActions[ctr].equals(SEK_RIGHT_JUSTIFY_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.getAlignment(attributes) == StyleConstants.ALIGN_RIGHT);
			}
			else if (toggleActions[ctr].equals(SEK_FONT_FAMILY_SANSSERIF_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.getFontFamily(attributes).equals(JAVA_SANSSERIF_STRING));
			}
			else if (toggleActions[ctr].equals(SEK_FONT_FAMILY_SERIF_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.getFontFamily(attributes).equals(JAVA_SERIF_STRING));
			}
			else if (toggleActions[ctr].equals(SEK_FONT_FAMILY_MONOSPACED_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.getFontFamily(attributes).equals(JAVA_MONOSPACED_STRING));
			}
			else if (toggleActions[ctr].equals(SEK_FONT_SIZE_8_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.getFontSize(attributes) == 8);
			}
			else if (toggleActions[ctr].equals(SEK_FONT_SIZE_10_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.getFontSize(attributes) == 10);
			}
			else if (toggleActions[ctr].equals(SEK_FONT_SIZE_11_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.getFontSize(attributes) == 11);
			}
			else if (toggleActions[ctr].equals(SEK_FONT_SIZE_12_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.getFontSize(attributes) == 12);
			}
			else if (toggleActions[ctr].equals(SEK_FONT_SIZE_14_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.getFontSize(attributes) == 14);
			}
			else if (toggleActions[ctr].equals(SEK_FONT_SIZE_16_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.getFontSize(attributes) == 16);
			}
			else if (toggleActions[ctr].equals(SEK_FONT_SIZE_18_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.getFontSize(attributes) == 18);
			}
			else if (toggleActions[ctr].equals(SEK_FONT_SIZE_24_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.getFontSize(attributes) == 24);
			}
			else if (toggleActions[ctr].equals(SEK_FONT_SIZE_36_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.getFontSize(attributes) == 36);
			}
			else if (toggleActions[ctr].equals(SEK_FONT_SIZE_48_ACTION))
			{
				toggleStatus = new Boolean(StyleConstants.getFontSize(attributes) == 48);
			}

			if (toggleStatus != null)
			{
				oldToggleStatus = (Boolean) currentAction.getValue(GUIConstants.PROPERTY_TOGGLE);

				if (!oldToggleStatus.equals(toggleStatus))
				{
					currentAction.putValue(GUIConstants.PROPERTY_TOGGLE, toggleStatus);
				}
			}
			else
			{
				System.out.print("WARNING: action not recognized: ");

				System.out.println(currentAction.getValue(Action.NAME));
			}
		}
	}

	private void initializeActions()
	{
		// actions = new Hashtable();
		actions = new HashMap();

		// actions not contained in the StyledEditorKit

		actions.put(TEXT_SUPERSCRIPT_ACTION, new TextSuperscriptAction());

		actions.put(TEXT_SUBSCRIPT_ACTION, new TextSubscriptAction());

		actions.put(TEXT_STRIKETHROUGH_ACTION, new TextStrikethroughAction());

		actions.put(CHOOSE_COLOR_ACTION, new ChooseColorAction());

		// actions contained in the StyledEditorKit (SEK)

		Hashtable styledActionTable = new Hashtable();

		Action[] styledEditorKitActions = new StyledEditorKit().getActions();

		for (int ctr=0; ctr<styledEditorKitActions.length; ctr++)
		{
			System.out.println("Action " + ctr + ", " + styledEditorKitActions[ctr].getValue(Action.NAME));
			styledActionTable.put(styledEditorKitActions[ctr].getValue(Action.NAME), styledEditorKitActions[ctr]);
		}

		// add action for setting font size to 11 points

		styledActionTable.put(SEK_FONT_SIZE_11_ACTION, new StyledEditorKit.FontSizeAction(SEK_FONT_SIZE_11_ACTION, 11));

		Object[] actionSetupArray = new Object[]
		{
			SEK_CUT_ACTION, ACTION_CUT_LS_PROPERTY, CUT_IMAGE_ICON_24,

			SEK_COPY_ACTION, ACTION_COPY_LS_PROPERTY, COPY_IMAGE_ICON_24,

			SEK_PASTE_ACTION, ACTION_PASTE_LS_PROPERTY, PASTE_IMAGE_ICON_24,

			SEK_BOLD_ACTION, ACTION_BOLD_LS_PROPERTY, BOLD_IMAGE_ICON_24,

			SEK_ITALIC_ACTION, ACTION_ITALIC_LS_PROPERTY, ITALIC_IMAGE_ICON_24,

			SEK_UNDERLINE_ACTION, ACTION_UNDERLINE_LS_PROPERTY, UNDERLINE_IMAGE_ICON_24,

			SEK_FONT_FAMILY_SANSSERIF_ACTION, ACTION_SANSSERIF_LS_PROPERTY, null,

			SEK_FONT_FAMILY_MONOSPACED_ACTION, ACTION_MONOSPACED_LS_PROPERTY, null,

			SEK_FONT_FAMILY_SERIF_ACTION, ACTION_SERIF_LS_PROPERTY, null,

			SEK_FONT_SIZE_8_ACTION, ACTION_FONT_SIZE_8_LS_PROPERTY, null,

			SEK_FONT_SIZE_10_ACTION, ACTION_FONT_SIZE_10_LS_PROPERTY, null,

			SEK_FONT_SIZE_11_ACTION, ACTION_FONT_SIZE_11_LS_PROPERTY, null,

			SEK_FONT_SIZE_12_ACTION, ACTION_FONT_SIZE_12_LS_PROPERTY, null,

			SEK_FONT_SIZE_14_ACTION, ACTION_FONT_SIZE_14_LS_PROPERTY, null,

			SEK_FONT_SIZE_16_ACTION, ACTION_FONT_SIZE_16_LS_PROPERTY, null,

			SEK_FONT_SIZE_18_ACTION, ACTION_FONT_SIZE_18_LS_PROPERTY, null,

			SEK_FONT_SIZE_24_ACTION, ACTION_FONT_SIZE_24_LS_PROPERTY, null,

			SEK_FONT_SIZE_36_ACTION, ACTION_FONT_SIZE_36_LS_PROPERTY, null,

			SEK_FONT_SIZE_48_ACTION, ACTION_FONT_SIZE_48_LS_PROPERTY, null,

			SEK_LEFT_JUSTIFY_ACTION, ACTION_LEFT_JUSTIFY_LS_PROPERTY, JUSTIFY_LEFT_IMAGE_ICON_24,

			SEK_RIGHT_JUSTIFY_ACTION, ACTION_RIGHT_JUSTIFY_LS_PROPERTY, JUSTIFY_RIGHT_IMAGE_ICON_24,

			SEK_CENTER_JUSTIFY_ACTION, ACTION_CENTER_JUSTIFY_LS_PROPERTY, JUSTIFY_CENTER_IMAGE_ICON_24
		};

		Action currentAction = null;

		String nP = ACTION_NAME_PREFIX_LS_PROPERTY;

		String dP = ACTION_SHORT_DESCRIPTION_PREFIX_LS_PROPERTY;

		for (int ctr=0; ctr<actionSetupArray.length; ctr++)
		{
			currentAction = (Action) styledActionTable.get(actionSetupArray[ctr]);

			System.out.println("1 >" + actionSetupArray[ctr] + "< " + currentAction);
			
			actions.put(actionSetupArray[ctr++], currentAction); // increments ctr

			System.out.println("2 " + actionSetupArray[ctr-1] + " " + currentAction);

			currentAction.putValue(Action.NAME, mVDH.getLanguageString(nP + actionSetupArray[ctr]));

			currentAction.putValue(Action.SHORT_DESCRIPTION, mVDH.getLanguageString(dP + actionSetupArray[ctr++]));

			if (actionSetupArray[ctr] != null)
			{
				currentAction.putValue(Action.SMALL_ICON, mVDH.getImageIcon((String)actionSetupArray[ctr]));
			}
		}

		toggleVector = new Vector();

		PropertyChangeListener toggleGroupListener = new ToggleGroupListener();

		for (int ctr=0; ctr<this.toggleActions.length; ctr++)
		{
			Action currentToggleAction = (Action) actions.get(toggleActions[ctr]);

			currentToggleAction.putValue(GUIConstants.PROPERTY_TOGGLE, new Boolean(false));

			currentToggleAction.addPropertyChangeListener(toggleGroupListener);

			toggleVector.add(currentToggleAction);
		}
	}

	private class ToggleGroupListener implements PropertyChangeListener
	{
		public void propertyChange(PropertyChangeEvent e)
		{
			String propName = e.getPropertyName();

			if (!propName.equals(GUIConstants.PROPERTY_TOGGLE)) { return; }


			Object newPropValue = e.getNewValue();

			if (!(newPropValue instanceof Boolean)) { return; }


			Boolean newValue = (Boolean) newPropValue;

			if (newValue.booleanValue())
			{
				if (isAlignmentAction((Action)e.getSource()))
				{
					toggleAlignments((Action)e.getSource());
				}
				else if (isFontSizeAction((Action)e.getSource()))
				{
					toggleFontSizes((Action)e.getSource());
				}
				else if (isFontFamilyAction((Action)e.getSource()))
				{
					toggleFontFamilies((Action)e.getSource());
				}
			}

			SwingUtilities.invokeLater(new Runnable()
			{
				public void run() { textPane.requestFocus(); }
			});
		}

		public boolean isAlignmentAction(Action action)
		{
			for (int ctr=0; ctr<alignmentActions.length; ctr++)
			{
				if (alignmentActions[ctr] == action) { return true; }
			}

			return false;
		}

		public void toggleAlignments(Action source)
		{
			for (int ctr=0; ctr<alignmentActions.length; ctr++)
			{
				if (!(alignmentActions[ctr] == source))
				{
					alignmentActions[ctr].putValue(GUIConstants.PROPERTY_TOGGLE, Boolean.FALSE);
				}
			}
		}

		public boolean isFontSizeAction(Action action)
		{
			for (int ctr=0; ctr<fontSizeActions.length; ctr++)
			{
				if (fontSizeActions[ctr] == action) { return true; }
			}

			return false;
		}

		public void toggleFontSizes(Action source)
		{
			for (int ctr=0; ctr<fontSizeActions.length; ctr++)
			{
				if (!(fontSizeActions[ctr] == source))
				{
					fontSizeActions[ctr].putValue(GUIConstants.PROPERTY_TOGGLE, Boolean.FALSE);
				}
			}
		}

		public boolean isFontFamilyAction(Action action)
		{
			for (int ctr=0; ctr<fontFamilyActions.length; ctr++)
			{
				if (fontFamilyActions[ctr] == action) { return true; }
			}

			return false;
		}

		public void toggleFontFamilies(Action source)
		{
			for (int ctr=0; ctr<fontFamilyActions.length; ctr++)
			{
				if (!(fontFamilyActions[ctr] == source))
				{
					fontFamilyActions[ctr].putValue(GUIConstants.PROPERTY_TOGGLE, Boolean.FALSE);
				}
			}
		}

		public ToggleGroupListener() {}

		private Action[] alignmentActions = new Action[]
		{
			getAction(SEK_LEFT_JUSTIFY_ACTION),
			getAction(SEK_CENTER_JUSTIFY_ACTION),
			getAction(SEK_RIGHT_JUSTIFY_ACTION)
		};

		private Action[] fontSizeActions = new Action[]
		{
			getAction(SEK_FONT_SIZE_8_ACTION),
			getAction(SEK_FONT_SIZE_10_ACTION),
			getAction(SEK_FONT_SIZE_11_ACTION),
			getAction(SEK_FONT_SIZE_12_ACTION),
			getAction(SEK_FONT_SIZE_14_ACTION),
			getAction(SEK_FONT_SIZE_16_ACTION),
			getAction(SEK_FONT_SIZE_18_ACTION),
			getAction(SEK_FONT_SIZE_24_ACTION),
			getAction(SEK_FONT_SIZE_36_ACTION),
			getAction(SEK_FONT_SIZE_48_ACTION),
		};

		private Action[] fontFamilyActions = new Action[]
		{
			getAction(SEK_FONT_FAMILY_SANSSERIF_ACTION),
			getAction(SEK_FONT_FAMILY_MONOSPACED_ACTION),
			getAction(SEK_FONT_FAMILY_SERIF_ACTION),
		};
	}

	public static class TextSuperscriptAction extends MedViewTextAction
	{
		public void actionPerformed(ActionEvent e)
		{
			JEditorPane editor = getEditor(e);

			if (editor != null)
			{
				StyledEditorKit kit = getStyledEditorKit(editor);

				MutableAttributeSet attr = kit.getInputAttributes();


				boolean superScript;

				if (StyleConstants.isSuperscript(attr))
				{
					superScript = false;
				}
				else
				{
					superScript = true;
				}


				SimpleAttributeSet sas = new SimpleAttributeSet();

				StyleConstants.setSuperscript(sas, superScript);

				setCharacterAttributes(editor, sas, false);


				editor.requestFocus();
			}
		}

		public TextSuperscriptAction()
		{
			super(ACTION_TEXT_SUPERSCRIPT_LS_PROPERTY, SUPERSCRIPT_IMAGE_ICON_24);
		}
	}

	public static class TextSubscriptAction extends MedViewTextAction
	{
		public void actionPerformed(ActionEvent e)
		{
			JEditorPane editor = getEditor(e);

			if (editor != null)
			{
				StyledEditorKit kit = getStyledEditorKit(editor);

				MutableAttributeSet attr = kit.getInputAttributes();


				boolean subScript;

				if (StyleConstants.isSubscript(attr))
				{
					subScript = false;
				}
				else
				{
					subScript = true;
				}


				SimpleAttributeSet sas = new SimpleAttributeSet();

				StyleConstants.setSubscript(sas, subScript);

				setCharacterAttributes(editor, sas, false);


				editor.requestFocus();
			}
		}

		public TextSubscriptAction()
		{
			super(ACTION_TEXT_SUBSCRIPT_LS_PROPERTY, SUBSCRIPT_IMAGE_ICON_24);
		}
	}

	public static class TextStrikethroughAction extends MedViewTextAction
	{
		public void actionPerformed(ActionEvent e)
		{
			JEditorPane editor = getEditor(e);

			if (editor != null)
			{
				StyledEditorKit kit = getStyledEditorKit(editor);

				MutableAttributeSet attr = kit.getInputAttributes();


				boolean strikeThrough;

				if (StyleConstants.isStrikeThrough(attr))
				{
					strikeThrough = false;
				}
				else
				{
					strikeThrough = true;
				}


				SimpleAttributeSet sas = new SimpleAttributeSet();

				StyleConstants.setStrikeThrough(sas, strikeThrough);

				setCharacterAttributes(editor, sas, false);


				editor.requestFocus();
			}
		}

		public TextStrikethroughAction()
		{
			super(ACTION_TEXT_STRIKETHROUGH_LS_PROPERTY, STRIKETHROUGH_IMAGE_ICON_24);
		}
	}

	public static class ChooseColorAction extends MedViewTextAction
	{
		public void setTargetPane(JTextPane textPane)
		{
			this.textPane = textPane;
		}

		public void actionPerformed(ActionEvent e)
		{
			MedViewDialogs mVD = MedViewDialogs.instance();

			Frame owner = null;

			Window windowAncestor = SwingUtilities.getWindowAncestor(textPane);

			if ((windowAncestor != null) && (windowAncestor instanceof Frame))
			{
				owner = (Frame) windowAncestor;
			}

			Color chosenColor = mVD.createAndShowChooseColorDialog(owner);

			if (chosenColor != null)
			{
				JEditorPane editor = getEditor(e);

				if (editor != null)
				{
					StyledEditorKit kit = getStyledEditorKit(editor);

					SimpleAttributeSet sas = new SimpleAttributeSet();

					StyleConstants.setForeground(sas, chosenColor);

					setCharacterAttributes(editor, sas, false);
				}
			}

			textPane.requestFocus();
		}

		public ChooseColorAction()
		{
			this(null);
		}

		public ChooseColorAction(JTextPane textPane)
		{
			super(ACTION_CHOOSE_COLOR_LS_PROPERTY, CHOOSE_COLOR_IMAGE_ICON_24);

			this.textPane = textPane;
		}

		private Component textPane;
	}
}
