/*
 * @(#)MedViewToolBarToggleButton.java
 *
 * $Id: MedViewToolBarToggleButton.java,v 1.1 2004/12/08 14:46:30 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.components.toolbar;

import java.awt.event.*;

import java.beans.*;

import javax.swing.*;

import misc.gui.constants.*;

public class MedViewToolBarToggleButton extends JToggleButton
{
	private void configureButtonUI()
	{
		setText("");
		
		setPreferredSize(GUIConstants.BUTTON_SIZE_24x24);
		
		setMaximumSize(GUIConstants.BUTTON_SIZE_24x24); // the toolbar sometimes increases comp size (!)	
	}

	public MedViewToolBarToggleButton(final Action action)
	{
		super(action);
		
		configureButtonUI();

		addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				Boolean b = new Boolean(e.getStateChange() == ItemEvent.SELECTED);

				if (!((Boolean)action.getValue(GUIConstants.PROPERTY_TOGGLE)).equals(b))
				{
					action.putValue(GUIConstants.PROPERTY_TOGGLE, b);
				}
			}
		});

		action.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent e)
			{
				if (e.getPropertyName().equals(GUIConstants.PROPERTY_TOGGLE))
				{
					boolean b = ((Boolean)e.getNewValue()).booleanValue();

					if (!(isSelected() == b)) { setSelected(b); }
				}
			}
		});
	}
}
