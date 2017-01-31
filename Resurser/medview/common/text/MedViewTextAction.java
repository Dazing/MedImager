/*
 * @(#)MedViewTextAction.java
 *
 * $Id: MedViewTextAction.java,v 1.4 2004/12/08 14:46:39 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.text;

import javax.swing.*;
import javax.swing.text.*;

import medview.datahandling.*;

public abstract class MedViewTextAction extends StyledEditorKit.StyledTextAction
	implements MedViewLanguageConstants
{
	protected MedViewTextAction(String actionLS)
	{
		this(actionLS, null);
	}

	protected MedViewTextAction(String actionLS, String imageLS)
	{
		super(mVDH.getLanguageString(nPre + actionLS));

		this.post = actionLS;

		if (imageLS != null) { this.imIc = mVDH.getImageIcon(imageLS); }

		name = nPre + post;
		desc = dPre + post;

		putValue(Action.NAME, mVDH.getLanguageString(name));
		putValue(Action.SHORT_DESCRIPTION, mVDH.getLanguageString(desc));

		if (imageLS != null) { putValue(Action.SMALL_ICON, imIc); }
	}

	protected static final MedViewDataHandler mVDH = MedViewDataHandler.instance();

	protected static String dPre = ACTION_SHORT_DESCRIPTION_PREFIX_LS_PROPERTY;
	protected static String nPre = ACTION_NAME_PREFIX_LS_PROPERTY;

	protected String post;
	protected String name;
	protected String desc;

	protected ImageIcon imIc;
}
