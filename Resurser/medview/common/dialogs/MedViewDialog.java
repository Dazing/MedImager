/*
 * @(#)MedViewDialog.java
 *
 * $Id: MedViewDialog.java,v 1.5 2006/04/24 14:17:01 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs;

import java.awt.*;

import javax.swing.*;

/**
 * A dialog in the MedView context.
 *
 * @author Fredrik Lindahl
 */
public abstract class MedViewDialog extends JDialog
{
	public abstract Object getObjectData();

	public abstract boolean wasDismissed();

	public MedViewDialog(Dialog owner, String title, boolean modal)
	{
		super(owner, title, modal);
	}

	public MedViewDialog(Frame owner, String title, boolean modal)
	{
		super(owner, title, modal);
	}
}
