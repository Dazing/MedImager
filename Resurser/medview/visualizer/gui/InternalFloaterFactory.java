/*
 * InternalFloaterFactory.java
 *
 * Created on July 23, 2002, 10:30 AM
 *
 * $Id: InternalFloaterFactory.java,v 1.11 2008/09/01 13:18:48 it2aran Exp $
 *
 * $Log: InternalFloaterFactory.java,v $
 * Revision 1.11  2008/09/01 13:18:48  it2aran
 * Visualizer: The desktop gets scrollbars if a window is outaide the main window to prevent windows from disappearing (594)
 * Changed version numbers
 *
 * Revision 1.10  2003/07/02 00:48:33  erichson
 * Large rewrite! deprecated individual methods and added general methods createFloater().
 * FloaterFactory went from interface to class, so this class was updated accordingly.
 *
 * Revision 1.9  2002/10/31 14:50:56  zachrisg
 * Minor changes needed for settings.
 *
 * Revision 1.8  2002/10/29 10:18:04  zachrisg
 * Changed the datagroup floater title to english.
 *
 * Revision 1.7  2002/10/17 14:40:07  zachrisg
 * Changed floater constructors to take "resizable" as a parameter.
 *
 * Revision 1.6  2002/10/16 12:53:06  zachrisg
 * Added createQueryFloater().
 *
 * Revision 1.5  2002/10/14 08:53:01  zachrisg
 * Updated javadoc.
 *
 */

package medview.visualizer.gui;

import java.awt.Point;

import medview.visualizer.data.*;

/**
 * A floater factory for internal floaters.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class InternalFloaterFactory extends FloaterFactory {
    
    /**
     * Creates a new instance of InternalFloaterFactory.
     */
    public InternalFloaterFactory() {
        super();
    }
    
    public Floater createFloater(FloaterComponent component, java.awt.Point location) {
        int type = component.getFloaterType();
        Floater floater = new InternalFloater(getFloaterTypeString(type),
                                getFloaterTitle(type),
                                location,
                                isFloaterTypeResizable(type)); // Not resizable
        floater.setComponent(component);
        floater.pack();
        return floater;        
    }
    
    /** 
     * Creates a data group floater.
     *
     * @param dataGroupPanel The data group panel.
     * @param location The location of the data group floater.
     * @deprecated generalized to createFloater()
     */    
    public Floater createDataGroupFloater(DataGroupPanel dataGroupPanel, Point location ) {        
        Floater floater = new InternalFloater("datagroups", "Groups", location, false);
        floater.setComponent(dataGroupPanel);
        floater.pack();
        return floater;
    }
    
    /** 
     * Creates a toolbox floater.
     *
     * @param toolBox The toolbox.
     * @param location The location of the toolbox floater.
     * @deprecated generalized to createFloater()
     */        
    public Floater createToolBoxFloater(ToolBoxComponent toolBox, Point location) {
        Floater floater = new InternalFloater("toolbox", "", location, false);
        floater.setComponent(toolBox);
        floater.pack();
        return floater;
    }
    
    /** 
     * Creates a query floater.
     *
     * @param queryPanel The query panel.
     * @param location The location of the query floater.
     * @deprecated generalized to createFloater()
     */
    public Floater createQueryFloater(QueryPanel queryPanel, Point location) {
        Floater floater = new InternalFloater("query", "Query", location, true);
        floater.setComponent(queryPanel);
        floater.pack();
        return floater;
    }
    
    /**
     * @deprecated generalized to createFloater()
     */
    public Floater createMessageFloater(MessagePanel messagePanel, java.awt.Point location) {
        Floater floater = new InternalFloater("messages", "Messages", location, true);
        floater.setComponent(messagePanel);
        floater.pack();
        return floater;    
    }    
    
    
}
