/*
 * @(#)MedViewMenuItem.java
 *
 * $Id: MedViewMenuItem.java,v 1.1 2004/12/08 14:46:36 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.components.menu;

import javax.swing.*;

import medview.datahandling.*;

public class MedViewMenuItem extends JMenuItem
{
	protected void setupAppearance()
	{
		setIcon(null);
	}

	protected void setupAccelerator()
	{
		if (accKS != null) { setAccelerator(accKS); }
	}

	protected void setupMnemonic()
	{
		if (mneProp != null)
		{
			setMnemonic(mVDH.getLanguageString(mneProp).charAt(0));
		}
	}

	public MedViewMenuItem(Action action, String mneProp)
	{
		this(action, null, mneProp);
	}

	public MedViewMenuItem(Action action, KeyStroke accKS, String mneProp)
	{
		super(action);

		this.accKS = accKS;

		this.action = action;

		this.mneProp = mneProp;

		setupAppearance();

		setupAccelerator();

		setupMnemonic();
	}

	protected static MedViewDataHandler mVDH;

	protected Action action;

	protected KeyStroke accKS;

	protected String mneProp;

	static
	{
		mVDH = MedViewDataHandler.instance();
	}
}
