/*
 * @(#)MedViewAction.java
 *
 * $Id: MedViewAction.java,v 1.5 2004/12/08 14:47:55 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.actions;

import javax.swing.*;

import medview.datahandling.*;

public abstract class MedViewAction extends AbstractAction implements 
	MedViewLanguageConstants
{

	protected MedViewAction(String lS)
	{
		this(lS, (ImageIcon) null);
	}


	protected MedViewAction(String lS, String imLS)
	{
		this(lS, mVDH.getImageIcon(imLS));
	}


	protected MedViewAction(String lS, ImageIcon im)
	{
		this.post = lS;

		this.imIc = im;

		name = nPre + lS;
		
		desc = dPre + lS;

		putValue(Action.NAME, mVDH.getLanguageString(name));

		putValue(Action.SHORT_DESCRIPTION, mVDH.getLanguageString(desc));

		if (im != null) 
		{ 
			putValue(Action.SMALL_ICON, im); 
		}
	}

	protected static final MedViewDataHandler mVDH;

	protected static final String dPre;
	
	protected static final String nPre;

	protected String post;
	
	protected String name;
	
	protected String desc;

	protected ImageIcon imIc;

	static
	{
		mVDH = MedViewDataHandler.instance();

		dPre = ACTION_SHORT_DESCRIPTION_PREFIX_LS_PROPERTY;

		nPre = ACTION_NAME_PREFIX_LS_PROPERTY;
	}
}
