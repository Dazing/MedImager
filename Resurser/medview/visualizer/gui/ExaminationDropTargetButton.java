/*
 * $Id: ExaminationDropTargetButton.java,v 1.2 2004/10/12 09:58:19 erichson Exp $
 *
 * Created on October 12, 2004, 9:54 AM
 *
 * $Log: ExaminationDropTargetButton.java,v $
 * Revision 1.2  2004/10/12 09:58:19  erichson
 * removed a println
 *
 * Revision 1.1  2004/10/12 09:55:41  erichson
 * First check-in.
 *
 */

package medview.visualizer.gui;

import java.io.IOException;

import java.awt.event.*;

import javax.swing.*;

import medview.visualizer.data.*;
import medview.visualizer.dnd.*;

/**
 * Button that can have examinations dropped on it.
 *
 * @author Nils Erichson, Göran Zachrisson
 */
public abstract class ExaminationDropTargetButton extends JButton 
    implements ExaminationDropTarget, ActionListener
{
    
    public ExaminationDropTargetButton()
    {
        super();
        super.addActionListener(this);
    }
    
    public ExaminationDropTargetButton(String str)
    {
        super(str);
        super.addActionListener(this);
    }
    
    public ExaminationDropTargetButton(Icon icon)
    {
        super(icon);
        super.addActionListener(this);
    }
    
    /**
     * Both text and icon
     */
    public ExaminationDropTargetButton(String str, Icon icon)
    {
        super(str, icon);
        super.addActionListener(this);
    }
    
    /**
     * Preferably icon, but also fallback text
     */ 
    public ExaminationDropTargetButton(String fallbackText, String visualizerIconName)
    {        
        super();
        super.addActionListener(this);
        
        try {
            Icon icon = ApplicationManager.getInstance().loadVisualizerIcon(visualizerIconName);
            setIcon(icon);
        } catch (IOException ioe)
        {
            setText(fallbackText); // Fallback
        }
    }
    
    public void actionPerformed(ActionEvent ae) 
    {        
        buttonClicked();
    }
    
    // Action to take when the droptarget is clicked
    public abstract void buttonClicked();
}
