/*
 * @(#)MedViewConstants.java
 *
 * $Id: MedViewConstants.java,v 1.6 2010/07/02 11:23:41 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.constants;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public interface MedViewConstants
{
	public static final int BUTTON_HEIGHT_NORMAL = 25;

	public static final int BUTTON_HEIGHT_SMALL = 20;


	public static final int COMBOBOX_HEIGHT_NORMAL = 20;

	public static final int COMBOBOX_WIDTH_SMALL = 80;

	public static final int COMBOBOX_WIDTH_NORMAL = 130;

	public static final int COMBOBOX_WIDTH_LARGE = 180;

	
	public static final KeyStroke NEW_TRANSLATOR_KEYSTROKE = KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + InputEvent.ALT_MASK);

	public static final KeyStroke LOAD_TRANSLATOR_KEYSTROKE = KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + InputEvent.ALT_MASK);

	public static final KeyStroke SAVE_TRANSLATOR_KEYSTROKE = KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() + InputEvent.ALT_MASK);


	public static final String TOGGLE_PROPERTY = "toggle";
}