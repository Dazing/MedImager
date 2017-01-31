//
//  TextActionProvider.java
//
//  Created by Olof Torgersson on 2010-05-27.
//
//  $Id: TextActionProvider.java,v 1.4 2010/07/02 11:40:26 oloft Exp $
//
package medview.medrecords.components;

import java.util.*;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;

import misc.gui.constants.*;

import medview.datahandling.*;

// Singleton class that provides easy acces to the most common text actions and a popup menu.
// The actions come from a DefaultEditorKit
// The motivation for this class is to not have to repeat code to access the actions
// Originally the intention was to use MedViewTextActionCollection but it does not really work
// ToDo: What's the problem with MedViewTextActionCollection?
// ToDo: Does not set mnemonic since it doesn't work with the localization system.
// Might be possible to fix in 1.7 using KeyEvent.getExtendedKeyCodeForChar(mnemonic)


public class TextActionProvider implements MedViewLanguageConstants {

	private static TextActionProvider instance = null;
		
	private Action cutAction;
	private Action copyAction;
	private Action pasteAction;
	private Action selectAllAction;
	
	private JPopupMenu popup;
	
	private HashMap<Object, Action> actions;
	
	private MedViewDataHandler mVDH;
	
    protected TextActionProvider() {
        // Exists only to defeat instantiation.
    }
	
    public static TextActionProvider getInstance() {
        if (instance == null) {
            instance = new TextActionProvider();
			instance.init();
        }
        return instance;
    }
	
    private void init() {
		mVDH = MedViewDataHandler.instance();
		
		actions = createActionTable();
		
		// ToDo: This was abandonded since it clashes with MedViewTextActionCollection used by MedSummary
		// cutAction = getActionByName(DefaultEditorKit.cutAction);
		cutAction = new DefaultEditorKit.CutAction();
		
		copyAction = new DefaultEditorKit.CopyAction();
		
		pasteAction = new DefaultEditorKit.PasteAction();
		
		// ToDo: This only works since there's no selectAllAction in the MedViewTextActionCollection class
		selectAllAction = getActionByName(DefaultEditorKit.selectAllAction);
		
		// Set title mnemonics and menu shortcut
		cutAction.putValue(Action.NAME, mVDH.getLanguageString(ACTION_NAME_PREFIX_LS_PROPERTY+ACTION_CUT_LS_PROPERTY));
                cutAction.putValue(Action.SHORT_DESCRIPTION, mVDH.getLanguageString(ACTION_SHORT_DESCRIPTION_PREFIX_LS_PROPERTY+ACTION_CUT_LS_PROPERTY));
		cutAction.putValue(Action.ACCELERATOR_KEY, GUIConstants.CUT_KEYSTROKE);
		
		// int mnemonic = mVDH.getLanguageString(MNEMONIC_MENU_ITEM_CUT_LS_PROPERTY).charAt(0);
		// cutAction.putValue(Action.MNEMONIC_KEY, KeyEvent.getExtendedKeyCodeForChar(mnemonic));

		copyAction.putValue(Action.NAME, mVDH.getLanguageString(ACTION_NAME_PREFIX_LS_PROPERTY+ACTION_COPY_LS_PROPERTY));
                copyAction.putValue(Action.SHORT_DESCRIPTION, mVDH.getLanguageString(ACTION_SHORT_DESCRIPTION_PREFIX_LS_PROPERTY+ACTION_COPY_LS_PROPERTY));
		copyAction.putValue(Action.ACCELERATOR_KEY, GUIConstants.COPY_KEYSTROKE);
		// copyAction.putValue(Action.MNEMONIC_KEY, mVDH.getLanguageString(MNEMONIC_MENU_ITEM_COPY_LS_PROPERTY).charAt(0));

		pasteAction.putValue(Action.NAME, mVDH.getLanguageString(ACTION_NAME_PREFIX_LS_PROPERTY+ACTION_PASTE_LS_PROPERTY));
                pasteAction.putValue(Action.SHORT_DESCRIPTION, mVDH.getLanguageString(ACTION_SHORT_DESCRIPTION_PREFIX_LS_PROPERTY+ACTION_PASTE_LS_PROPERTY));
		pasteAction.putValue(Action.ACCELERATOR_KEY, GUIConstants.PASTE_KEYSTROKE);
		// pasteAction.putValue(Action.MNEMONIC_KEY, mVDH.getLanguageString(MNEMONIC_MENU_ITEM_PASTE_LS_PROPERTY).charAt(0));

		selectAllAction.putValue(Action.NAME, mVDH.getLanguageString(ACTION_NAME_PREFIX_LS_PROPERTY+ACTION_SELECT_ALL_LS_PROPERTY));
		selectAllAction.putValue(Action.ACCELERATOR_KEY, GUIConstants.SELECT_ALL_KEYSTROKE);
		// selectAllAction.putValue(Action.MNEMONIC_KEY, mVDH.getLanguageString(MNEMONIC_MENU_ITEM_SELECT_ALL_LS_PROPERTY).charAt(0));

		//Create  popup menu.
		JMenuItem menuItem;
		Action menuAction;

		popup = new JPopupMenu();

		menuAction = cutAction;
		menuItem = new JMenuItem(menuAction);
		popup.add(menuItem);
		
		menuAction = copyAction;
		menuItem = new JMenuItem(menuAction);
		popup.add(menuItem);
		
		menuAction = pasteAction;
		menuItem = new JMenuItem(menuAction);
		popup.add(menuItem);
		
		popup.addSeparator();
		
		menuAction = selectAllAction;
		menuItem = new JMenuItem(menuAction);
		popup.add(menuItem);
		
	}
	
	//The following two methods allow us to find an
	//action provided by the editor kit by its name.
	private HashMap<Object, Action> createActionTable() {
		HashMap<Object, Action> acts = new HashMap<Object, Action>();
		Action[] actionsArray = new DefaultEditorKit().getActions();
		for (int i = 0; i < actionsArray.length; i++) {
			
			Action a = actionsArray[i];
			acts.put(a.getValue(Action.NAME), a);
		}
		return acts;
	}
	
	private Action getActionByName(String name) {
		return actions.get(name);
	}
	
	
	public Action getCutAction() {
		return cutAction;
	}
	
	public Action getCopyAction() {
		return copyAction;
	}
	
	public Action getPasteAction() {
		return pasteAction;
	}
	
	public Action getSelectAllAction() {
		return selectAllAction;
	}
	
	public JPopupMenu getPopupMenu() {
		return popup;
	}
}
