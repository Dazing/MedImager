/*
 * @(#)MedViewMoreButton.java
 *
 * $Id: MedViewMoreButton.java,v 1.9 2005/06/03 15:42:56 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.components;

import java.awt.*;

import java.beans.*;

import javax.swing.*;

import misc.gui.constants.*;

/**
 * A simple 'more' button, i.e. a button with the face text '...'.
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Main Project: The MedView Project @ Chalmers University of Technology</p>
 *
 * <p>Sub Project: none</p>
 *
 * <p>Project Web http://www.cs.chalmers.se/proj/medview/website/medview/</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class MedViewMoreButton extends JButton
{
	public MedViewMoreButton()
	{
		this(null);
	}

	public MedViewMoreButton(Action action)
	{
		if (action != null)
		{
			setAction(action);

			action.addPropertyChangeListener(new PropertyChangeListener()
			{
				public void propertyChange(PropertyChangeEvent e)
				{
					setText(FACE_TEXT); // makes sure face is always "..."
				}
			});
		}

		setIcon(null);

		setText(FACE_TEXT);

		setPreferredSize(new Dimension(GUIConstants.BUTTON_WIDTH_MORE,

			GUIConstants.BUTTON_HEIGHT_MORE));
	}

	private static final String FACE_TEXT = "...";
}
