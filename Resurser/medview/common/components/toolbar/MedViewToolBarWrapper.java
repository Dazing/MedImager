/*
 * @(#)MedViewToolBar.java
 *
 * $Id: MedViewToolBarWrapper.java,v 1.1 2004/12/08 14:46:30 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.components.toolbar;

import java.awt.*;

import javax.swing.*;

import medview.datahandling.*;

import misc.gui.components.*;

/**
 * @author Fredrik Lindahl
 */
public class MedViewToolBarWrapper extends JPanel
{
	public MedViewToolBarWrapper(JToolBar toolBar)
	{
		this(toolBar, true, true);
	}

	public MedViewToolBarWrapper(JToolBar toolBar, boolean isRollover)
	{
		this(toolBar, true, true);
	}

	public MedViewToolBarWrapper(JToolBar toolBar, boolean isRollover, boolean isEtched)
	{	
		// layout
		
		this.setLayout(new BorderLayout());
		
		this.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		
		this.add(toolBar, BorderLayout.WEST);
		
		if (isEtched) { this.setBorder(new ToolbarBorder(ToolbarBorder.ETCHED)); }

		if (!isEtched) { this.setBorder(new ToolbarBorder(ToolbarBorder.NO_ETCH)); }

		if (isRollover) { putClientProperty(ROLLOVER_CLIENT_PROPERTY, Boolean.TRUE); }

		if (!isRollover) { putClientProperty(ROLLOVER_CLIENT_PROPERTY, Boolean.FALSE); }
	}

	private static MedViewDataHandler mVDH = MedViewDataHandler.instance();

	private static final String ROLLOVER_CLIENT_PROPERTY = "JToolBar.isRollover";
}
