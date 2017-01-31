/*
 * FreeFloaterFactory.java
 *
 * Created on July 23, 2002, 10:19 AM
 *
 * $Id: FreeFloaterFactory.java,v 1.10 2003/07/02 00:47:52 erichson Exp $
 *
 * $Log: FreeFloaterFactory.java,v $
 * Revision 1.10  2003/07/02 00:47:52  erichson
 * Large rewrite! deprecated individual methods and added general methods createFloater().
 * Added type constants.
 *
 * Revision 1.9  2002/10/31 14:50:57  zachrisg
 * Minor changes needed for settings.
 *
 * Revision 1.8  2002/10/29 10:18:04  zachrisg
 * Changed the datagroup floater title to english.
 *
 * Revision 1.7  2002/10/17 14:40:07  zachrisg
 * Changed floater constructors to take "resizable" as a parameter.
 *
 * Revision 1.6  2002/10/16 12:53:07  zachrisg
 * Added createQueryFloater().
 *
 * Revision 1.5  2002/10/14 08:47:54  zachrisg
 * Updated javadoc.
 *
 */

package medview.visualizer.gui;

import java.awt.*;

import medview.visualizer.data.*;

/**
 * A floater factory for free floaters.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class FreeFloaterFactory extends FloaterFactory {
    
    /** 
     * Creates a new instance of FreeFloaterFactory.
     */
    public FreeFloaterFactory() {
        super();
    }
    
    public Floater createFloater(FloaterComponent component, Point location) {
        int type = component.getFloaterType();
        Floater floater = new FreeFloater(getFloaterTypeString(type),
                                            getFloaterTitle(type),
                                            location,
                                            isFloaterTypeResizable(type)); // not resizable
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
    public Floater createDataGroupFloater(DataGroupPanel dataGroupPanel, Point location) {        
        // location.translate(300,100);
        Floater floater = new FreeFloater("datagroups", "Groups", location, false);
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
        //location.translate(10,100);
        Floater floater = new FreeFloater("toolbox", "", location, false);
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
    public Floater createQueryFloater(QueryPanel queryPanel, java.awt.Point location) {
        Floater floater = new FreeFloater("query", "Query", location, true);
        floater.setComponent(queryPanel);
        floater.pack();
        return floater;
    }
    
    /**
     * @deprecated generalized to createFloater()
     */
    
    public Floater createMessageFloater(MessagePanel messagePanel, java.awt.Point location) {
        Floater floater = new FreeFloater("messages", "Messages", location, true);
        floater.setComponent(messagePanel);
        floater.pack();
        return floater;
    }
    
    public Floater createFloater(javax.swing.JComponent component, int type, java.awt.Point location) {
        Floater floater = new FreeFloater(FloaterFactory.getFloaterTypeString(type), FloaterFactory.getFloaterTitle(type), location, true);
        floater.setComponent(component);
        floater.pack();
        return floater;
    }
    
}
