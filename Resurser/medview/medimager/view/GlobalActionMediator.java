package medview.medimager.view;

import misc.gui.actions.*;

/**
 * A single point of access for actions that are global (i.e.
 * non-action-shell-contained) in the MedImager application.
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl
 * @version 1.0
 */
public class GlobalActionMediator
{
	// ACTION CONSTANTS

	public static final int ABOUT_ACTION = 0;

	public static final int EXIT_ACTION = 1;

	public static final int NEW_ALBUM_ACTION = 2;

	public static final int PLAY_ACTION = 3;

	public static final int PREFERENCES_ACTION = 4;

	public static final int RECORD_ACTION = 5;

	public static final int SEARCH_ACTION = 6;

	public static final int STOP_ACTION = 7;


	// PRIVATE MEMBERS

	private ExtendedAbstractAction[] registeredActions = new ExtendedAbstractAction[7];

	private static GlobalActionMediator instance;


	// PRIVATE CONSTRUCTOR

	private GlobalActionMediator() { super(); }


	// SINGLETON ACCESSOR

	public static GlobalActionMediator instance()
	{
		if (instance == null)
		{
			instance = new GlobalActionMediator();
		}

		return instance;
	}


	// ACTION REGISTRATION

	public void registerAction(int id, ExtendedAbstractAction action)
	{
		registeredActions[id] = action;
	}

	public ExtendedAbstractAction getAction(int id)
	{
		return registeredActions[id];
	}
}
