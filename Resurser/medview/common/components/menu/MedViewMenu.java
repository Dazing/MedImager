/*
 * @(#)MedViewMenu.java
 *
 * $Id: MedViewMenu.java,v 1.1 2004/12/08 14:46:36 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.components.menu;

import javax.swing.*;

import medview.datahandling.*;

public class MedViewMenu extends JMenu
{
	public MedViewMenu(String menuProp, String mneProp)
	{
		super(mVDH.getLanguageString(menuProp));

		if (mneProp != null) 
		{ 
			setMnemonic(mVDH.getLanguageString(mneProp).charAt(0)); 
		}
	}

	private static MedViewDataHandler mVDH = MedViewDataHandler.instance();
}
