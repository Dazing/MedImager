/*
 * FieldInputComponent.java
 *
 * Created on den 4 augusti 2003, 14:07
 *
 * $Id: FieldInputComponent.java,v 1.14 2010/06/28 07:12:39 oloft Exp $
 *
 * $Log: FieldInputComponent.java,v $
 * Revision 1.14  2010/06/28 07:12:39  oloft
 * MR 4.5 dev
 *
 * Revision 1.13  2007/10/17 15:17:03  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.12  2005/02/17 10:25:38  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.11  2004/12/08 14:42:56  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.10  2004/11/19 12:33:57  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.9  2004/06/01 10:36:12  lindahlf
 * no message
 *
 * Revision 1.8  2004/05/18 18:21:05  lindahlf
 * Åtgärdade fel med bild-filnamn skapade utan datum
 *
 * Revision 1.7  2004/03/18 16:37:14  lindahlf
 * Ordnade till PID-format bugg
 *
 * Revision 1.6  2004/02/20 12:12:40  lindahlf
 * Valbart visa översättare vid inmatning av nytt värde
 *
 * Revision 1.5  2004/02/19 18:21:28  lindahlf
 * Major update patch 1
 *
 * Revision 1.4  2003/12/21 21:54:12  oloft
 * Changed settings, removed DataHandlingHandler
 *
 * Revision 1.3  2003/11/27 23:44:02  oloft
 * Field sizing
 *
 * Revision 1.2  2003/11/11 13:52:33  oloft
 * Switching mainbranch
 *
 * Revision 1.1.2.12  2003/10/20 14:43:02  oloft
 * File transfer
 *
 * Revision 1.1.2.11  2003/10/18 14:50:46  oloft
 * Builds tree file with new file names
 *
 * Revision 1.1.2.10  2003/10/14 11:55:13  oloft
 * Enabled Shift-Tab
 *
 * Revision 1.1.2.9  2003/10/13 05:53:12  oloft
 * Completion manamgement
 *
 * Revision 1.1.2.8  2003/10/10 14:49:34  oloft
 * Return forces movement to next component
 *
 * Revision 1.1.2.7  2003/10/08 18:34:26  oloft
 * Uses Config instead of Prefs
 *
 * Revision 1.1.2.6  2003/10/06 09:15:41  oloft
 * Uses DatahandlingHandler to check P-code
 *
 * Revision 1.1.2.5  2003/09/09 17:10:35  erichson
 * Bugfix for previous check-in.
 *
 * Revision 1.1.2.4  2003/09/09 10:24:35  erichson
 * Now checks Prefs whether to keep the value in the textfield or not
 *
 * Revision 1.1.2.3  2003/09/05 21:33:11  erichson
 * Now first letter in manually inputted values is always capitalized. (Bugzilla bug 165)
 *
 * Revision 1.1.2.2  2003/09/05 20:51:43  erichson
 * Fixed: Value list background, caret reseting for ? type values, showing of translator editor
 *
 * Revision 1.1.2.1  2003/08/16 14:57:48  erichson
 * First check-in.
 *
 */

package medview.medrecords.components.inputs;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import medview.datahandling.examination.tree.*;
import medview.datahandling.*;

import medview.medrecords.*;
import medview.medrecords.components.*;
import medview.medrecords.data.*;
import medview.medrecords.events.*;
import medview.medrecords.exceptions.*;
import medview.medrecords.models.*;

/**
 *  ValueInputComponent for all types of field except note.
 *
 * @author Nils Erichson, Nader Nazari
 */
public class FieldInputComponent extends ValueInputComponent implements DocumentListener
	{
		private HashMap customValueMap;
		
		private JList list;
		
		private JTextField textField;
		
		private DefaultListModel listModel;
		
		private MedViewDataHandler mDH = MedViewDataHandler.instance();
		
		private static int completeKey;
		
		static
		{
			String os = System.getProperty("os.name").toLowerCase();
			
			if (os.startsWith("mac"))
			{
				completeKey = KeyEvent.VK_F5;
			}
			else
			{
				completeKey = KeyEvent.VK_F2;
			}
		}
		
		public FieldInputComponent(FieldModel fieldModel)
		{
			super(fieldModel);
			customValueMap = new HashMap();
			textField = new JTextField(40);
			
			textField.addKeyListener(new TextFieldKeyAdapter());
			
			textField.addFocusListener(new TextFieldFocusListener());
			
			textField.getDocument().addDocumentListener(this);
			
			textField.setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, java.util.Collections.EMPTY_SET);
			
			textField.setFocusTraversalKeys(java.awt.KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, java.util.Collections.EMPTY_SET);
			
			listModel = new DefaultListModel();
			
			list = new JList(listModel);
			
			// -------------------------------------->	FREDRIK 040212
			
			Class c = PreferencesModel.class;
			
			String key = PreferencesModel.InputListBackgroundColor;
			
			String defColString = PreferencesModel.DefaultInputListBackgroundColorString;
			
			String colString = mDH.getUserStringPreference(key, defColString, c);
			
			StringTokenizer t = new StringTokenizer(colString, ",");
			
			int r = Integer.parseInt(t.nextToken());
			
			int g = Integer.parseInt(t.nextToken());
			
			int b = Integer.parseInt(t.nextToken());
			
			list.setBackground(new Color(r, g, b));
			
			// <------------------------------------------------------
			
			list.addMouseListener(new ValueListMouseListener());
			
			list.addFocusListener(new ListFocusListener());
			
			list.setBorder(BorderFactory.createLoweredBevelBorder());
			
			JPanel contentPanel = new JPanel();
			
			contentPanel.setLayout(new BorderLayout());
			
			contentPanel.add(textField, BorderLayout.CENTER);
			
			contentPanel.add(list, BorderLayout.SOUTH);
			
			this.setLayout(new BorderLayout()); // OT greatly improves sizing
			
			this.add(contentPanel, BorderLayout.CENTER); // OT
		}
		
		/**
		 * Verify that the input in this text area is valid 
		 * (according to the model). Returns true if the input is 
		 * valid, otherwise throws an exception with an explanation.
		 * @throws ValueInputException if the input is invalid
		 */
		public void verifyInput() throws ValueInputException
		{
			final FieldModel fieldModel = getFieldModel();
			
			Object[] listObjects = listModel.toArray();
			
			String[] rows = new String[listObjects.length];
			
			for (int i = 0; i < listObjects.length; i++)
			{
				rows[i] = (String)listObjects[i];
			}
			
			// Check that the input line is empty! If it isn't, we have made some input but not confirmed it.
			
			String inputLine = textField.getText().trim();
			
			if (inputLine.length() > 0)
			{
				throw new ValueInputException("Value has been input, but not confirmed. Press return, or delete the first line");
			}
			
			// If the field is an identification field, verify the p_code
			
			if (fieldModel.getFieldType() == FieldModel.FIELD_TYPE_IDENTIFICATION)
			{
				if (rows.length < 1)
				{
					throw new ValueInputException("Patient identifier field is not filled in");
				}
				
				String aRow = rows[0];
				
				if (aRow == null)
				{
					throw new ValueInputException("Program error (aRow == null, which should never happen. Case: First check)");
				}
				
				validateValue(aRow); // throws appropriate exception
			}
			
			// If this field is "required" to be filled in, check that a value is entered
			
			if (fieldModel.isRequired())
			{
				if (rows.length < 1)
				{
					throw new ValueInputException("The template requires this field to be filled in");
				}
				
				String aRow = rows[0]; // Get first row
				
				if (aRow == null)
				{
					throw new ValueInputException("Program error (aRow == null, which should never happen. Case: isRequired)");
				}
				
				if (aRow.length() < 1)
				{
					throw new ValueInputException("The template requires this field to be filled in");
				}
			}
			
			return; // detected nothing wrong with the input
		}
		
		/**
		 * Returns all the values and checks if there are some custom values using ||
		 */
		public String[] getValues()
		{
			Object[] objArray = listModel.toArray();
			
			String[] stringArray = new String[objArray.length];
			
			String value;
			String customValue;
			for (int i = 0; i < objArray.length; i++)
			{
				value = (String)objArray[i];
				customValue = (String)customValueMap.get(value);
				if(customValue==null)
				{
					stringArray[i] = value;
				}
				else
				{
					stringArray[i] = customValue;
				}
			}
			
			return stringArray;
		}
		
		/**
		 * Check whether it is legal to add a value to the list or not. 
		 * For fields of type IDENTIFICATION, the p_code is verified for 
		 * validity. For locked fields (isEditable false), the value is 
		 * checked to be in the preset list.
		 */
		private void validateValue(String value) throws ValueInputException
		{
			FieldModel fieldModel = getFieldModel();
			
			int fieldType = fieldModel.getFieldType();
			
			if (!fieldModel.isEditable())
			{
				if (!fieldModel.getPresetModel().containsPreset(value))
				{
					// throw new ValueInputException("Term " + getName() + " is locked, only preset values are allowed"); 
					throw new ValueInputException(mDH.getLanguageString(MedViewLanguageConstants.ERROR_ONLY_PRESET_VALUES_ALLOWED_LS_PROPERTY));
				}
			}
			
			if (fieldType == FieldModel.FIELD_TYPE_IDENTIFICATION)
			{
				if (!mDH.validates(value))
				{
					throw new ValueInputException(mDH.getLanguageString(MedViewLanguageConstants.ERROR_INVALID_PCODE_PREFIX_LS_PROPERTY) + 
												  " '" + value + "' " + mDH.getLanguageString(MedViewLanguageConstants.ERROR_INVALID_PCODE_SUFFIX_LS_PROPERTY));
				}
			}
		}
		
		/**
		 * Called when a preset is clicked. Behaves differenty with 
		 * regards to type: most values are put into the list, but 
		 * "?" values are put into the input field.
		 */
		public void putPreset(String value)
		{
			if (value.startsWith("?")) // TEMPORARY HARDCODING. REMOVE THIS LATER.
			{
				textField.setText(value.substring(1)); // skip the "?"
				
				textField.setCaretPosition(0);
			}
			else
			{
				putValue(value);
			}
		}
		
		public void putCustomPreset(String key, String value)
		{
			customValueMap.put(key,value);
		}
		
		private String makeFirstCharacterUpperCase(String value)
		{
			if (value == null)
			{
				return null;
			}
			
			if (value.length() < 1)
			{
				return value;
			}
			
			char[] array = value.toCharArray();
			
			array[0] = Character.toUpperCase(array[0]);
			
			return new String(array);
		}
		
		/**
		 * Add a value to the list. Does NOT validate the value first.
		 * Clears the textfield if that is set in the prefs.
		 */
		public void putValue(String value)
		{
			if (!listModel.contains(value))
			{
				// --------------------------------------------------------> Fredrik 040416
				
				boolean fieldIsMulti = getFieldModel().getFieldType() == FieldModel.FIELD_TYPE_MULTI;
				
				boolean fieldIsQuest = getFieldModel().getFieldType() == FieldModel.FIELD_TYPE_QUESTION;
				
				if (!fieldIsMulti && !fieldIsQuest)
				{
					listModel.clear();
				}
				
				// <---------------------------------------------------------
				
				// --------------------------------------------------------> Fredrik 040317
				
				if (inputModel.isIdentification())
				{
					MedViewDataHandler mVDH = MedViewDataHandler.instance();
					
					try
					{
						listModel.addElement(mVDH.normalizePID(value));
					}
					catch (Exception e)
					{
						e.printStackTrace();
						
						listModel.addElement(value);
					}
				}
				else
				{
					listModel.addElement(value); // Add the value last
				}
				
				// <---------------------------------------------------------
				
				fireInputValueChanged();
			}
			
			if (!PreferencesModel.instance().getKeepPrefix())
			{
				textField.setText("");
			}
			
		}
		
		/**
		 * The KeyListener for the TextField
		 */
		private class TextFieldKeyAdapter extends KeyAdapter
			{
				public void keyPressed(KeyEvent evt)
				{
					int keyCode = evt.getKeyCode();
					
					switch (keyCode)
					{
						case KeyEvent.VK_ENTER:
							putValueFromField();
							
							
							// --------------------------------------------------------> Fredrik 040416
							
							boolean fieldIsMulti = getFieldModel().getFieldType() == FieldModel.FIELD_TYPE_MULTI;
							
							boolean fieldIsQuest = getFieldModel().getFieldType() == FieldModel.FIELD_TYPE_QUESTION;
							
							if (!fieldIsMulti && !fieldIsQuest)
							{
								gotoNextInput();
							}
							
							
							// <--------------------------------------------------------
							break;
						case KeyEvent.VK_TAB: // If this is not here, a tab press is inserted into the textfield
							evt.consume();
							if (evt.isShiftDown())
							{
								//System.out.println("Got Shift Tab");
								gotoPreviousInput();
							}
							else
							{
								//System.out.println("Got Tab");
								gotoNextInput();
							}
							break;
					}
					if (keyCode == completeKey)
					{
						evt.consume();
						
						handleComplete();
					}
				}
			}
		
		
		/**
		 * Put the value in the field into the list.
		 * If the value only contains whitespace, nothing happens.
		 * Makes sure the first character is uppercase. If the value 
		 * is translateable and does not exist as preset (and isn't 
		 * a ? type), a dialog asks whether to add the new value as preset.
		 */
		protected void putValueFromField()
		{
			String newVal = textField.getText();
			
			newVal = makeFirstCharacterUpperCase(newVal);
			
			PresetModel presetModel = inputModel.getPresetModel();
			
			FieldModel fieldModel = (FieldModel)inputModel;
			
			if (!(newVal.trim().equals("")))
			{
				if ((!inputModel.isTranslateAble()) || (fieldModel.getFieldType() == FieldModel.FIELD_TYPE_QUESTION)
					
					|| (presetModel.containsPreset(newVal)))
				{
					// do nothing
				}
				else
				{
					
					// ask whether to translate, but only if the preset list is open
					if (inputModel.isEditable()) {
						
						// System.out.println("Model editable, " + newVal);
						
						String[] optionArray = {mDH.getLanguageString(MedViewLanguageConstants.BUTTON_ADD_TO_LS_PROPERTY),
							
						mDH.getLanguageString(MedViewLanguageConstants.BUTTON_ADD_LOCALLY_LS_PROPERTY)};
						
						String defaultOption = optionArray[0];
						
						int result = JOptionPane.showOptionDialog(this, newVal + " " + mDH.getLanguageString(
																											 
																											 MedViewLanguageConstants.QUESTION_SHOULD_ADD_VALUE_LS_PROPERTY), mDH.getLanguageString(
																																																	
																																																	MedViewLanguageConstants.TITLE_NEW_VALUE_ALERT_LS_PROPERTY), JOptionPane.YES_NO_OPTION,
																  
																  JOptionPane.QUESTION_MESSAGE, null, optionArray, defaultOption);
						
						switch (result)
						{
							case JOptionPane.YES_OPTION:
								showTranslationEditor(presetModel, newVal);
								
								break;
								
							case JOptionPane.NO_OPTION:
							case JOptionPane.CLOSED_OPTION:
							default:
								break;
						}
					}
				}
				
				try
				{
					validateValue(newVal);
					
					putValue(newVal);
				}
				catch (ValueInputException vie)
				{
					JOptionPane.showMessageDialog(this, vie.getMessage(), 
												  mDH.getLanguageString(MedViewLanguageConstants.TITLE_INVALID_INPUT_LS_PROPERTY), JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		/**
		 * Shows a term editor.
		 */
		private void showTranslationEditor(PresetModel presetModel, String newValue)
		{
			ApplicationFrame appFrame = getParentTab().getParentPane().getApplicationFrame();
			
			appFrame.showTranslationEditor(presetModel, newValue);
			
		}
		
		public Tree getTreeRepresentation(Date date, String pCode)
		{
			return compileSingleTermTreeFromValues();
		}
		
		/**
		 * Update the preset list mask with the contents of the current row
		 */
		protected void setMask(String newMask)
		{
			TabPanel parentTab = getParentTab();
			
			ValueTabbedPane parentPane = parentTab.getParentPane();
			
			PresetListPanel presetListPanel = parentPane.getPresetListPanel();
			
			if (presetListPanel == null)
			{
				System.err.println("Error: Could not set mask: presetListPanel == null");
			}
			else
			{
				presetListPanel.setMask(newMask);
			}
		}
		
		private FieldModel getFieldModel()
		{
			return (FieldModel)inputModel;
		}
		
		public void clearContents()
		{
			textField.setText("");
			
			listModel.clear();
		}
		
		public void setEditable(boolean editable)
		{
		}
		
		public void focusInput()
		{
			textField.requestFocus();
			
			textField.setCaretPosition(0);
		}
		
		public void requestFocus()
		{
			focusInput(); // when the field input component wants focus, it asks the text field to request focus	
		}
		
		public void changedUpdate(DocumentEvent e)
		{
			documentChanged();
		}
		
		public void insertUpdate(DocumentEvent e)
		{
			documentChanged();
		}
		
		public void removeUpdate(DocumentEvent e)
		{
			documentChanged();
		}
		
		private void documentChanged()
		{
			setMask(textField.getText());
		}
		
		private void handleComplete()
		{
			ValueTabbedPane vtb = getParentTab().getParentPane();
			
			PresetListPanel psp = vtb.getPresetListPanel();
			
			psp.addFirstValue();
		}
		
		
		private class ListFocusListener implements FocusListener
			{
				public void focusGained(FocusEvent e)
				{
				}
				
				public void focusLost(FocusEvent e)
				{
					if (e.getOppositeComponent() != textField)
					{
						list.clearSelection(); // the focus went to other component than list or text field
					}
				}
			}
		
		private class TextFieldFocusListener implements FocusListener
			{
				public void focusGained(FocusEvent e)
				{
					fireInputFocusChanged(new InputFocusEvent(FieldInputComponent.this));
				}
				
				public void focusLost(FocusEvent e)
				{
					// reclaim focus if the JList steals it
					
					if (e.getOppositeComponent() == list)
					{
						textField.requestFocus(); // the focus went to the list
					}
					else
					{
						list.clearSelection(); // the focus went to other component than list or text field
					}
				}
			}
		
		private class ValueListMouseListener extends MouseAdapter
			{
				public void mousePressed(MouseEvent me)
				{
					maybeShowPopup(me);
				}
				
				public void mouseReleased(MouseEvent me)
				{
					maybeShowPopup(me);
				}
				
				private void maybeShowPopup(MouseEvent me)
				{
					if (me.isPopupTrigger())
					{
						Object clickedObject = listModel.getElementAt(list.locationToIndex(me.getPoint()));
						
						JPopupMenu popupMenu = new JPopupMenu();
						
						JMenuItem removeItem = new JMenuItem(mDH.getLanguageString(MedViewLanguageConstants.
																				   
																				   MENU_ITEM_REMOVE_VALUE_LS_PROPERTY) + " " + clickedObject);
						
						removeItem.setMnemonic(mDH.getLanguageString(MedViewLanguageConstants.
																	 
																	 MNEMONIC_MENU_ITEM_REMOVE_VALUE_LS_PROPERTY).charAt(0));
						
						removeItem.addActionListener(new PopupActionListener(clickedObject));
						
						popupMenu.add(removeItem);
						
						popupMenu.show(me.getComponent(), me.getX(), me.getY());
					}
				}
			}
		
		private class PopupActionListener implements ActionListener
			{
				private Object objectToRemove;
				
				public PopupActionListener(Object objectToRemove)
				{
					this.objectToRemove = objectToRemove;
				}
				
				public void actionPerformed(ActionEvent e)
				{
					listModel.removeElement(objectToRemove);
					
					fireInputValueChanged();
				}
			}
	}
