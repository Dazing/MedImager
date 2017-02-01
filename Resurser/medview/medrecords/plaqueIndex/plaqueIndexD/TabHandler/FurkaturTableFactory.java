/*
 * FurkaturTableFactory.java
 *
 * Created on den 24 mars 2005, 19:34
 *
 * $Id: FurkaturTableFactory.java,v 1.2 2005/04/26 13:31:16 erichson Exp $
 *
 * $Log: FurkaturTableFactory.java,v $
 * Revision 1.2  2005/04/26 13:31:16  erichson
 * Merged copenhagen_development
 *
 * Revision 1.1.2.1  2005/03/24 19:24:49  erichson
 * First check-in.
 *
 */

package medview.medrecords.plaqueIndex.plaqueIndexD.TabHandler;

/**
 *
 * @author Nils Erichson
 */

import javax.swing.JTable;

/**
 * Interface which allows me to do some OO magic to remove the duplicated code between
 * TableHandlerFurkaturer and TableHandlerFurkaturerUnder.
 */
public interface FurkaturTableFactory
{
    
    public JTable getOverTable(int j);
    public JTable getUnderTable(int j);
    
}
