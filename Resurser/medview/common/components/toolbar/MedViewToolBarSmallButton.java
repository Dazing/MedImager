/*
 * @(#)MedViewToolBarNormalButton.java
 *
 * $Id: MedViewToolBarSmallButton.java,v 1.1 2004/12/08 14:46:29 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.components.toolbar;

import java.beans.*;

import javax.swing.*;

import misc.gui.constants.*;

/**
 * A simple JButton extension that is meant to
 * be used for toolbar buttons. This subclass
 * sets the size of the toolbar button to a
 * 'normal' Java-sized toolbar button, i.e.
 * with a dimension of 18x18 pixels. The graphics
 * contained in the button should be of size
 * 16x16 pixels.
 * @author Fredrik Lindahl
 */
public class MedViewToolBarSmallButton extends JButton
{
	private void configureButtonUI()
	{
		setText("");

		setPreferredSize(GUIConstants.BUTTON_SIZE_16x16);

		setMaximumSize(GUIConstants.BUTTON_SIZE_16x16); // the toolbar sometimes increases comp size (!)		
	}
	
	public MedViewToolBarSmallButton(Action action)
	{
		super(action); 
		
		configureButtonUI();
	}
}
