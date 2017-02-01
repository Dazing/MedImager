/*
 * @(#)ChangeUserIntPreferenceCommand.java
 *
 * $Id: ChangeUserIntPreferenceCommand.java,v 1.1 2003/12/19 21:12:30 oloft Exp $
 *
 * --------------------------------
 * Original author: Olof Torgersson
 * --------------------------------
 */

package medview.common.dialogs.settings;

import medview.datahandling.*;

import misc.domain.*;

public class ChangeUserIntPreferenceCommand implements Command {
    
	public boolean shouldReplace(Command command) {
            if (!(command instanceof ChangeUserIntPreferenceCommand)) { return false; }

            return ((ChangeUserIntPreferenceCommand)command).prop.equals(this.prop);
	}

	public void execute() {
            MedViewDataHandler.instance().setUserIntPreference(prop, value,	prefClass);
	}


	/**
	 * Creates a change int command, that will place the
	 * int on a user preference node specified by the
	 * specified class. 	 
         * @param prop the preference name that will be set in
	 * the user preference node specified by the class.
	 * @param value the int value that the entry in the
	 * user preference node (identifier by prop) will be set
	 * to.
	 * @param cl the Class identifying the node in the user
	 * preference tree that should keep the entry.
	 */
	public ChangeUserIntPreferenceCommand(String prop, int value, Class cl) {
		this.prop = prop;

		this.value = value;

		this.prefClass = cl;
	}

	private String prop;

	private int value;

	private Class prefClass;
}