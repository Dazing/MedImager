/*
 * FloaterFactory.java
 *
 * Created on July 23, 2002, 10:15 AM
 *
 * $Id: FloaterFactory.java,v 1.7 2008/09/01 13:18:48 it2aran Exp $
 *
 * $Log: FloaterFactory.java,v $
 * Revision 1.7  2008/09/01 13:18:48  it2aran
 * Visualizer: The desktop gets scrollbars if a window is outaide the main window to prevent windows from disappearing (594)
 * Changed version numbers
 *
 * Revision 1.6  2003/07/02 00:50:39  erichson
 * Went from interface to abstract class.
 * Deprecated individual methods, added general createFloater methods.
 * Added methods getFloaterType(), getFloaterTitle() and isFloaterTypeResizable().
 *
 * Revision 1.5  2002/10/16 12:53:07  zachrisg
 * Added createQueryFloater().
 *
 * Revision 1.4  2002/10/14 08:53:38  zachrisg
 * Fixed mistake in javadoc.
 *
 * Revision 1.3  2002/10/14 08:50:59  zachrisg
 * Javadoc clean up.
 *
 */

package medview.visualizer.gui;

import medview.visualizer.data.*;

import javax.swing.*;

/**
 * An interface for floater factories.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public abstract class FloaterFactory {
    
     
    
    public static final String getFloaterTypeString(int floaterType) {
        switch(floaterType) {
            case Floater.FLOATER_TYPE_MESSAGES:
                return "messages";                
            case Floater.FLOATER_TYPE_DATAGROUPS:
                return "datagroups";                
            case Floater.FLOATER_TYPE_QUERY:
                return "query";                
            case Floater.FLOATER_TYPE_TOOLBOX:
                return "toolbox";                
            default:
                return "unknown_floater_type (" + floaterType + ")";                
        }        
    }
    
    public static final String getFloaterTitle(int floaterType) {
        switch(floaterType) {
            case Floater.FLOATER_TYPE_MESSAGES:
                return "Messages";
            case Floater.FLOATER_TYPE_DATAGROUPS:
                return "Data groups";
            case Floater.FLOATER_TYPE_QUERY:
                return "Query";
            case Floater.FLOATER_TYPE_TOOLBOX:
                return "Tools";
            default:
                return "Unknown floater type (" + floaterType + ")";
        }
    }
    
    public static final boolean isFloaterTypeResizable(int floaterType) {
        switch(floaterType) {
            case Floater.FLOATER_TYPE_MESSAGES:
            case Floater.FLOATER_TYPE_QUERY:
                return true;
            case Floater.FLOATER_TYPE_DATAGROUPS:                                            
            case Floater.FLOATER_TYPE_TOOLBOX:
                return false;
            default:
                return false;
        }
    }
    
    
    public Floater createFloater(FloaterComponent component) {
        return createFloater(component, new java.awt.Point(0,0));
    }
    
    /***
     * The method to create the floater. Extend this for example Free floater or InternalFloater
     */
    public abstract Floater createFloater(FloaterComponent component, java.awt.Point location);                    
        
    
    
    /** 
     * Creates a data group floater.
     *
     * @param dataGroupPanel The data group panel.
     * @param location The location of the data group floater.
     * @deprecated generalized to createFloater()
     */    
     public abstract Floater createDataGroupFloater(DataGroupPanel dataGroupPanel, java.awt.Point location);
    
    /** 
     * Creates a toolbox floater.
     *
     * @param toolBox The toolbox.
     * @param location The location of the toolbox floater.
     * @deprecated generalized to createFloater()
     */    
    public abstract Floater createToolBoxFloater(ToolBoxComponent toolBox, java.awt.Point location);
    
    /**
     * Creates a query floater.
     *
     * @param queryPanel The query panel.
     * @param location The location of the query floater.
     * @deprecated generalized to createFloater()
     */
    public abstract Floater createQueryFloater(QueryPanel queryPanel, java.awt.Point location);
    
    /** 
     * @deprecated generalized to createFloater()
     */
    public abstract Floater createMessageFloater(MessagePanel messagePanel, java.awt.Point location);
}
