/*
 * @(#)ChangeFlagCommand.java
 *
 * $Id: ChangeFlagCommand.java,v 1.4 2003/04/10 01:49:15 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.common.dialogs.settings;

import medview.datahandling.*;

import misc.domain.*;

public class ChangeFlagCommand implements Command
{
	public boolean shouldReplace(Command command)
	{
		if (!(command instanceof ChangeFlagCommand)) { return false; }

		return ((ChangeFlagCommand)command).prop.equals(this.prop);
	}

	public void execute()
	{
		if (prefClass == null)
		{
			MedViewDataHandler.instance().setFlagProperty(prop, value);
		}
		else
		{
			MedViewDataHandler.instance().setUserBooleanPreference(prop, value,	prefClass);
		}

		/* NOTE: for backwards compatibility with
		 * earlier classes developed prior to the
		 * adaptation of 1.4, the 'flag property'
		 * method of dealing with preferences is
		 * kept. If the user specifies a class at
		 * construction of the command, both the
		 * flag property and a boolean user pref
		 * will be set when the command is executed.
		 */
	}


	/**
	 * Creates a change flag command, that will use the
	 * old way of placing flags between application runs
	 * (the way used prior to the 1.4 introduction of
	 * the Preferences API), by using the data handler's
	 * setFlagProperty() method.
	 * @param prop the property that will be set with the
	 * data handler's setFlagProperty() method.
	 * @param value the value that will be set for the
	 * specified property.
	 */
	public ChangeFlagCommand(String prop, boolean value)
	{
		this(prop, value, null);
	}

	/**
	 * Creates a change flag command, that will place the
	 * flag on a user preference node specified by the
	 * specified class. The old way (prior to adaptation of
	 * the 1.4 API) of setting flag properties will be used
	 * only if the other version of the constructor is used.
	 * @param prop the preference name that will be set in
	 * the user preference node specified by the class.
	 * @param value the boolean value that the entry in the
	 * user preference node (identifier by prop) will be set
	 * to.
	 * @param cl the Class identifying the node in the user
	 * preference tree that should keep the entry.
	 */
	public ChangeFlagCommand(String prop, boolean value, Class cl)
	{
		this.prop = prop;

		this.value = value;

		this.prefClass = cl;
	}

	private String prop;

	private boolean value;

	private Class prefClass;
}