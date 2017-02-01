/*
 * @(#)MedViewToolBar.java
 *
 * $Id: MedViewToolBar.java,v 1.1 2004/12/08 14:46:29 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.components.toolbar;

import java.awt.*;

import java.beans.*;

import javax.swing.*;

import medview.datahandling.*;

import misc.gui.components.*;
import misc.gui.constants.*;

/**
 * @author Fredrik Lindahl
 */
public class MedViewToolBar extends JToolBar implements GUIConstants
{
	private void configureUI()
	{		
		setFloatable(false);

		setBorderPainted(true);

		setMargin(new Insets(1,1,1,1));

		setOrientation(JToolBar.HORIZONTAL);

		setBorder(new ToolbarBorder(ToolbarBorder.ETCHED)); 
		
		putClientProperty(PROPERTY_LAF_ROLLOVER_CLIENT, Boolean.TRUE); 
		
		revalidate();
	}

	public MedViewToolBar(String titleLS)
	{
		super(MedViewDataHandler.instance().getLanguageString(titleLS));

		configureUI();
	}
}
