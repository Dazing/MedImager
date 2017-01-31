package medview.aggregator;

import javax.swing.UIManager;
import java.awt.*;

/*
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * Original author: Nader Nazari
 *
 * $Id: StartAggregator.java,v 1.5 2003/07/01 17:29:40 erichson Exp $
 *
 * $Log: StartAggregator.java,v $
 * Revision 1.5  2003/07/01 17:29:40  erichson
 * re-added startAggregator method since other applications use it.
 *
 * Revision 1.4  2003/06/24 13:29:13  erichson
 * Moved most of the code to AggregatorFrame (method fitToScreen) // NE
 *
 * Revision 1.3  2002/10/14 10:34:33  erichson
 * Small javadoc addition
 *
 * Revision 1.2  2002/10/14 10:25:50  erichson
 * Changed starting code to static method instead of class constructor
 *
 */

 /**
  * A class with a static method to start a new aggregator
  *
  * @author Nader Nazari, some modifications by Nils Erichson <d97nix@dtek.chalmers.se>
  * @version 1.2
  */

public class StartAggregator {
         
    /**
     * Main method
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        startAggregator();
    }
    
    public static void startAggregator() {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        
        AggregatorFrame frame = new AggregatorFrame();                
        frame.fitToScreen();
        frame.setVisible(true);
    }        
}