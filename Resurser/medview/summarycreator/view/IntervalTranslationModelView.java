/*
 * @(#)IntervalTranslationModelView.java
 *
 * $Id: IntervalTranslationModelView.java,v 1.8 2006/04/24 14:16:43 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.summarycreator.view;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import javax.swing.*;

import medview.common.dialogs.*;

import medview.summarycreator.model.*;

public class IntervalTranslationModelView extends AbstractTableTermView
{
	protected String[] getTableColumnNamesLS( )
	{
		return new String[]
		{
			OTHER_PREVIEW_DESCRIPTION_LS_PROPERTY,

			OTHER_INTERVAL_VALUE_DESCRIPTION_LS_PROPERTY,

			OTHER_INTERVAL_TRANSLATION_DESCRIPTION_LS_PROPERTY
		};
	}





	protected Action getAddValueAction()
	{
		if (defaultIntervalAddAction == null)
		{
			defaultIntervalAddAction = new DefaultIntervalAddAction();
		}

		return defaultIntervalAddAction;
	}

	private Action defaultIntervalAddAction;





	protected class DefaultIntervalAddAction extends AbstractTermView.DefaultAddValueAction
	{
		public void actionPerformed(ActionEvent e)
		{
			Frame owner = null;

			Window windowAncestor = SwingUtilities.getWindowAncestor(IntervalTranslationModelView.this);

			if ((windowAncestor != null) && (windowAncestor instanceof Frame))
			{
				owner = (Frame) windowAncestor;
			}

			MedViewDialog mVD = MedViewDialogs.instance().createAddIntervalDialog(owner); mVD.show();

			if (!mVD.wasDismissed()) { currentModel.addValue(mVD.getObjectData(), new Date()); }
		}
	}
}
