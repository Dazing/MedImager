package medview.medimager.view;

import java.awt.event.*;

import java.beans.*;

import javax.swing.*;

import misc.gui.actions.*;

/**
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class ActionShell extends ExtendedAbstractAction
{
	private int identification = -1;

	private Action pluggedAction = null;

	private PropertyChangeListener propList = null;

	public ActionShell(String desc, ImageIcon standardIcon, ImageIcon menuIcon, int id)
	{
		super(desc, standardIcon, menuIcon);

		this.identification = id;

		setEnabled(false); // at construction, a shell is disabled
	}

	public int getIdentification()
	{
		return this.identification;
	}

	public void actionPerformed(ActionEvent e)
	{
		if (pluggedAction != null)
		{
			pluggedAction.actionPerformed(e);
		}
	}

	public void setPluggedAction(Action a)
	{
		if (pluggedAction != null)
		{
			pluggedAction.removePropertyChangeListener(propList);
		}

		pluggedAction = a;

		propList = new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent evt)
			{
				setEnabled(pluggedAction.isEnabled());
			}
		};

		pluggedAction.addPropertyChangeListener(propList);

		setEnabled(pluggedAction.isEnabled());
	}
}
