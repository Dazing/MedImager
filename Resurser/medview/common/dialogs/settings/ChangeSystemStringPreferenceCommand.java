//
// ChangeSystemStringPreferenceCommand.java
//
// $Id: ChangeSystemStringPreferenceCommand.java,v 1.1 2004/01/27 15:52:20 oloft Exp $
//

package medview.common.dialogs.settings;

import medview.datahandling.*;

import misc.domain.*;

public class ChangeSystemStringPreferenceCommand implements Command {
    
    public boolean shouldReplace(Command command) {
        if (!(command instanceof ChangeSystemStringPreferenceCommand)) { return false; }
        
        return ((ChangeSystemStringPreferenceCommand)command).prop.equals(this.prop);
    }
    
    public void execute() {
        MedViewDataHandler.instance().setSystemStringPreference(prop, value, prefClass);
    }
    
    
    /**
    * Creates a change string command, that will place the
     * int on a system preference node specified by the
     * specified class. 	 
     * @param prop the preference name that will be set in
     * the user preference node specified by the class.
     * @param value the int value that the entry in the
     * user preference node (identifier by prop) will be set
     * to.
     * @param cl the Class identifying the node in the user
     * preference tree that should keep the entry.
     */
    public ChangeSystemStringPreferenceCommand(String prop, String value, Class cl) {
        this.prop = prop;
        
        this.value = value;
        
        this.prefClass = cl;
    }
    
    private String prop;
    
    private String value;
    
    private Class prefClass;
}
