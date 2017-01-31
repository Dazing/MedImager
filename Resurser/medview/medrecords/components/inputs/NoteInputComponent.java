/*
 * NoteInputComponent.java
 *
 * Created on den 4 augusti 2003, 13:53
 *
 * $Id: NoteInputComponent.java,v 1.9 2010/06/28 07:12:39 oloft Exp $
 *
 * $Log: NoteInputComponent.java,v $
 * Revision 1.9  2010/06/28 07:12:39  oloft
 * MR 4.5 dev
 *
 * Revision 1.8  2007/10/17 15:17:03  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.7  2005/07/06 09:18:29  erichson
 * Fix for Note fields not working with touchscreen import
 *
 * Revision 1.6  2005/02/17 10:25:38  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.5  2004/12/08 14:42:56  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.4  2004/06/01 10:36:12  lindahlf
 * no message
 *
 * Revision 1.3  2003/11/27 23:44:03  oloft
 * Field sizing
 *
 * Revision 1.2  2003/11/11 13:52:33  oloft
 * Switching mainbranch
 *
 * Revision 1.1.2.3  2003/10/18 14:50:46  oloft
 * Builds tree file with new file names
 *
 * Revision 1.1.2.2  2003/10/14 11:55:13  oloft
 * Enabled Shift-Tab
 *
 * Revision 1.1.2.1  2003/08/16 14:57:43  erichson
 * First check-in.
 *
 */

package medview.medrecords.components.inputs;

import medview.datahandling.examination.tree.*;

import medview.medrecords.events.*;
import medview.medrecords.models.*;
import medview.medrecords.components.TextActionProvider;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class NoteInputComponent extends ValueInputComponent implements FocusListener
{
	private String customValue;

    private static final int DEFAULT_COLUMNS = 30;

	private static final int DEFAULT_ROWS = 8;

	private final medview.medrecords.models.PresetModel presetModel;

	private JTextArea textArea;

	public NoteInputComponent(InputModel inputModel)
	{
		this(inputModel, DEFAULT_ROWS, DEFAULT_COLUMNS);
	}

	private NoteInputComponent(InputModel inputModel, int rows, int columns)
	{
		super(inputModel);

		presetModel = new medview.medrecords.models.PresetModel(getName());

		textArea = new JTextArea(rows, columns);
		
		textArea.setLineWrap(true);
		
		textArea.addKeyListener(new NoteTextAreaKeyAdapter());
		
		textArea.addFocusListener(this);

		textArea.getDocument().addDocumentListener(new NoteTextAreaDocumentListener());

		// disable consuming of TAB and SHIFT-TAB
		
		textArea.setFocusTraversalKeys(java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, java.util.Collections.EMPTY_SET);
		
		textArea.setFocusTraversalKeys(java.awt.KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, java.util.Collections.EMPTY_SET);

		JScrollPane textAreaScrollPane = new JScrollPane(textArea);
		
		//Add listener to the text area so the popup menu can come up.
		MouseListener popupListener = new PopupListener(TextActionProvider.getInstance().getPopupMenu());
		textArea.addMouseListener(popupListener);
		
		textAreaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		this.setLayout(new BorderLayout());

		add(textAreaScrollPane, BorderLayout.CENTER);
	}

	public void setEditable(boolean editable)
	{
		textArea.setEditable(editable);
	}

	public medview.medrecords.models.PresetModel getPresetModel()
	{
		// notes don't have presets, so just return an empty presetModel
		
		return presetModel;
	}

	public String[] getValues()
	{
		String[] stringArray = new String[1];
		
		stringArray[0] = textArea.getText();
		
		return stringArray;
	}

        // putvalue adds to the PresetListPanel
	public void putValue(String value) 
	{
		// empty implementation, since Notes don't have presets
	}

	public void putCustomPreset(String key, String value)
	{
		// empty implementation, since Notes don't have presets
	}
        // TouchScreenHack calls putpreset when importing
	public void putPreset(String value)
	{
		String currentText = textArea.getText();
                if (currentText.trim().equals(""))
                {
                    currentText = value;
                }
                else
                {
                    currentText += ("\n" + value);
                }
                
                textArea.setText(currentText);
	}

	public void clearContents()
	{
		textArea.setText("");
	}

	public void focusInput()
	{
		textArea.requestFocus();
		
		textArea.setCaretPosition(0);
	}

	private class NoteTextAreaKeyAdapter extends KeyAdapter
	{
		public void keyPressed(KeyEvent evt)
		{
			switch (evt.getKeyCode())
			{
				case KeyEvent.VK_TAB: // If this is not here, a tab press is inserted into the textfield

					evt.consume();
					
					if (evt.isShiftDown())
					{
						gotoPreviousInput();
					}
					else
					{
						gotoNextInput();
					}
					
					break;
			}
		}
	}

	private class NoteTextAreaDocumentListener implements DocumentListener
	{
		public void changedUpdate(DocumentEvent e)
		{
			fireInputValueChanged();
		}

		public void insertUpdate(DocumentEvent e)
		{
			fireInputValueChanged();
		}

		public void removeUpdate(DocumentEvent e)
		{
			fireInputValueChanged();
		}

	}

	public void verifyInput()
	{
		// do nothing
	}

	/**
	 * Called when focus is gained by the textArea
	 */
	public void focusGained(FocusEvent fe)
	{
		fireInputFocusChanged(new InputFocusEvent(this));
	}


	/**
	 * Called when focus is lost by the textArea
	 */
	public void focusLost(FocusEvent fe)
	{}

	public Tree getTreeRepresentation(Date date, String pCode)
	{
		return compileSingleTermTreeFromValues();
	}
	
	// PRIVATE CLASS FOR  POP-UP MENU
	
	private class PopupListener extends MouseAdapter {
        JPopupMenu popup;
		
        PopupListener(JPopupMenu popupMenu) {
            popup = popupMenu;
        }
		
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }
		
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }
		
        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(),
                           e.getX(), e.getY());
            }
        }
    }
	
}
