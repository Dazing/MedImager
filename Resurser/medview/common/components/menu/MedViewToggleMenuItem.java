/*
 * @(#)MedViewToggleMenuItem.java
 *
 * $Id: MedViewToggleMenuItem.java,v 1.1 2004/12/08 14:46:36 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.components.menu;

import java.awt.event.*;
import java.beans.*;
import javax.swing.*;

import medview.datahandling.*;

import misc.gui.constants.*;

public class MedViewToggleMenuItem extends JCheckBoxMenuItem
{

	private void setupSelected()
	{
		Object sel = action.getValue(GUIConstants.PROPERTY_TOGGLE);

		if (sel == null)
		{
			setSelected(false);
		}
		else
		{
			setSelected(((Boolean)sel).booleanValue());
		}
	}

	private void setupMnemonic()
	{
		if (mneProp != null)
		{
			setMnemonic(mVDH.getLanguageString(mneProp).charAt(0));
		}
	}

	private void setupToggleListeners()
	{
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
	}



	public MedViewToggleMenuItem(Action action, String mneProp)
	{
		super(action);

		this.action = action;

		this.mneProp = mneProp;

		setupSelected();

		setupMnemonic();

		setupToggleListeners();
	}

	private static MedViewDataHandler mVDH;

	private Action action;

	private String mneProp;

	static
	{
		mVDH = MedViewDataHandler.instance();
	}

}
