/*
 * @(#)MedViewToolBarNormalButton.java
 *
 * $Id: MedViewToolBarNormalButton.java,v 1.1 2004/12/08 14:46:29 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.components.toolbar;

import javax.swing.*;

import misc.gui.constants.*;

import java.beans.*;

/**
 * A simple JButton extension that is meant to
 * be used for toolbar buttons. This subclass
 * sets the size of the toolbar button to a
 * 'normal' Java-sized toolbar button, i.e.
 * with a dimension of 24x24 pixels.
 * @author Fredrik Lindahl
 */
public class MedViewToolBarNormalButton extends JButton
{
	private void configureButtonUI()
	{
		setText("");

		setPreferredSize(GUIConstants.BUTTON_SIZE_24x24);

		setMaximumSize(GUIConstants.BUTTON_SIZE_24x24); // the toolbar sometimes increases comp size (!)		
	}
	
	public MedViewToolBarNormalButton(Action action)
	{
		super(action);
		
		configureButtonUI();
	}
}
