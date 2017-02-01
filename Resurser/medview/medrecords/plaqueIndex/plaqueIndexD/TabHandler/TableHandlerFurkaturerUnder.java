/*
 * TableHandlerFurkaturerUnder.java
 *
 * Created on den 10 februari 2003, 11:55
 *
 * $Id: TableHandlerFurkaturerUnder.java,v 1.7 2005/04/26 12:57:13 erichson Exp $
 *
 * $Log: TableHandlerFurkaturerUnder.java,v $
 * Revision 1.7  2005/04/26 12:57:13  erichson
 * Merging COPENHAGEN_DEVELOPMENT branch.
 *
 * Revision 1.6.2.3  2005/04/26 10:39:41  erichson
 * Code cleanup while finishing up
 *
 * Revision 1.6.2.2  2005/03/24 19:23:47  erichson
 * Utrensad från allt överlapp med TableHandlerFurkaturer.
 *
 * Revision 1.6.2.1  2005/03/24 18:07:09  erichson
 * Uppdaterad till ny PlaqueHandler och upprensad. Checkar in inför att uppdatera
 * den till att fungera som den uppdaterade TableHandlerFurkaturer.
 *
 */

package medview.medrecords.plaqueIndex.plaqueIndexD.TabHandler;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.*;
import medview.medrecords.plaqueIndex.plaqueIndexD.ThreeColTab.*;


/**
 *
 * @author  nader
 */
public class TableHandlerFurkaturerUnder extends TableHandlerFurkaturer
    implements FurkaturTableFactory
{

    public TableHandlerFurkaturerUnder(PlaquePanelD pPanel)
    {
        super(pPanel, false); // don't call initcomponents in super class, do it here instead
        
        tableFactory = this;
        initComponents(); // Use this class for creating tables        
    }

    public JTable getOverTable(int j)
    {       
        ThreeColTabModel aMd = new ThreeColTabModel(getMaxInput(),j,true,true,null); // isNumeric was true

        int tabNr = PqDFunConst.calculateTabNr(j);

        aMd.addRow();

        ThreeColTable aTb = new ThreeColTable(aMd);

        aTb.setHandler(this);

        if(tabNr <=5)
        {
            aTb.permDisableAllColumns();
        }
        else
        {
            aTb.permDisableColumn(0);

            aTb.permDisableColumn(2);
        }

        mModels.add(aMd);

        mTables.add(aTb);

        //aTb.addMouseListener(mParent);

        return aTb;
    }

    public JTable getUnderTable(int j)
    {
        String [] header ={"","",""};

        ThreeColTabModel aMd = new ThreeColTabModel(getMaxInput(),j,true,false,header); // isNumeric was true

        int tabNr = PqDFunConst.calculateTabNr(j);

        aMd.addRow();

        ThreeColTable aTb = new ThreeColTable(aMd);

        aTb.setHandler(this);

        if(tabNr <=5)
        {
            aTb.permDisableAllColumns();
        }
        else
        {
            aTb.permDisableColumn(0);
            aTb.permDisableColumn(2);
        }

        mModelsU.add(aMd);
        mTablesU.add(aTb);

        //aTb.addMouseListener(mParent);

        return aTb;
    }
}
