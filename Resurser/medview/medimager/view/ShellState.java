package medview.medimager.view;

import javax.swing.*;

/**
 * A utility class for enabling each part of the
 * application to maintain it's separate state
 * information of the common actions.
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class ShellState implements MedImagerActionConstants
{
	private Action[] actions = new Action[]
	{
		null, null, null, null, null, null, null, null, null, null
	};

	public void setAction(int index, Action action)
	{
		this.actions[index] = action;
	}

	public Action getAction(int index)
	{
		return actions[index];
	}

	public Action[] getActions() // OBS - might return some null
	{
		return actions;
	}
}
