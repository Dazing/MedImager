/*
 * @(#)ChangeUserStringPropertyCommand.java
 *
 * $Id: ChangeUserStringPropertyCommand.java,v 1.2 2003/08/19 17:56:34 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs.settings;

import medview.datahandling.*;

import misc.domain.*;

/**
 * This is an old class that has been mutated in order
 * to support the new preferences api used in the data
 * handling layer as well as the old property api. You
 * choose which api to use by using the appropriate
 * constructor version.
 * @author Fredrik Lindahl
 */
public class ChangeUserStringPropertyCommand implements Command
{
	public String getPreference()
	{
		return propOrPref;
	}

	public String getProperty()
	{
		return propOrPref;
	}

	public String getValue()
	{
		return val;
	}

	public boolean shouldReplace(Command command)
	{
		if (command instanceof ChangeUserStringPropertyCommand)
		{
			return (propOrPref.equals(((ChangeUserStringPropertyCommand)command).propOrPref));
		}

		return false;
	}

	public void execute()
	{
		if (userClass == null) // property api
		{
			mVDH.setUserProperty(propOrPref, val);
		}
		else // preference api
		{
			mVDH.setUserStringPreference(propOrPref, val, userClass);
		}
	}

	/**
	 * Use this version of the constructor to create a change
	 * command for changing property values. This is deprecated,
	 * and you should use the other version of the constructor
	 * instead.
	 */
	public ChangeUserStringPropertyCommand(String prop, String val)
	{
		this(prop, val, null);
	}

	/**
	 * Use this version of the constructor to create a change
	 * command for changing preference values. This is preferred
	 * since we are trying to leave the old property api.
	 */
	public ChangeUserStringPropertyCommand(String pref, String val, Class c)
	{
		this.val = val;

		this.userClass = c;

		this.propOrPref = pref;

		mVDH = MedViewDataHandler.instance();
	}

	private MedViewDataHandler mVDH;

	private String propOrPref;

	private Class userClass;

	private String val;
}