/*
 * ViewJInternalFrame.java
 *
 * Created on June 26, 2002, 2:56 PM
 *
 * $Id: ViewJInternalFrame.java,v 1.9 2008/09/01 13:18:48 it2aran Exp $
 *
 * $Log: ViewJInternalFrame.java,v $
 * Revision 1.9  2008/09/01 13:18:48  it2aran
 * Visualizer: The desktop gets scrollbars if a window is outaide the main window to prevent windows from disappearing (594)
 * Changed version numbers
 *
 * Revision 1.8  2002/11/27 13:53:31  zachrisg
 * Added icon.
 *
 * Revision 1.7  2002/10/30 15:56:42  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import java.awt.event.*;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;

import medview.visualizer.data.*;
import medview.visualizer.event.*;

/**
 * A ViewFrame implementation using JInternalFrame.
 *
 * @author  Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class ViewJInternalFrame extends JInternalFrame implements ViewFrame, TitleChangeListener {

    /** The View contained by this ViewFrame. */
    private View view = null;
    
    /** 
     * Creates new ViewJInternalFrame.
     *
     * @param view The View to show in the frame.
     */
    public ViewJInternalFrame(View view) {
        super(view.getTitle(), 
            true, //resizable
            true, //closable
            true, //maximizable
            true);//iconifiable
        this.view = view;

        view.addTitleChangeListener(this);

        try {
            setFrameIcon(ApplicationManager.getInstance().loadVisualizerIcon("medview16.gif"));
        } catch (IOException exc) {
            System.err.println("ViewJInternalFrame could not load icon.");
        }        
        
        getContentPane().add(view);
        
        addComponentListener(new ComponentAdapter(){
 
        public void componentMoved(ComponentEvent evt)
        {
            //we must go through all opened frames to see if this frame is the
            //one farthest out, if it is we must shrink the desktop
            ViewFrame[] viewFrames = ApplicationManager.getInstance().getViewFrames();
            int x = 0;
            int y = 0;
            for(ViewFrame view : viewFrames)
            {
                x = Math.max(x,view.getLocation().x+view.getSize().width);
                y = Math.max(y,view.getLocation().y+view.getSize().height);
            }

            for(Floater floater : ApplicationManager.getInstance().getToolFloaters())
            {
                x = Math.max(x,floater.getLocation().x+floater.getSize().width);
                y = Math.max(y,floater.getLocation().y+floater.getSize().height);
            }
            ApplicationManager.getInstance().getDesktop().setPreferredSize(new Dimension(x,y));
            ApplicationManager.getInstance().getDesktop().updateUI();
            }
    });
        addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                frameClosing();
            }});
        setLocation(30,30);
        setSize(200,100);
    }
    /**
     * Sets the View of the frame.
     *
     * @param view The new View.
     */
    public void setView(View view) {
        if (this.view != null) {
            this.view.removeTitleChangeListener(this);
            getContentPane().remove(this.view);
        }
        this.view = view;
        view.addTitleChangeListener(this);
        getContentPane().add(view);
    }

    /**
     * Returns the View contained in the frame.
     *
     * @return The View contained in the frame.
     */
    public View getView() {
        return view;
    }    

    /**
     * Close the frame and dispose all of its belongings.
     */
    public void closeFrame() {
        getContentPane().remove(view);
        view.removeTitleChangeListener(this);
        view = null;        
        setVisible(false);
        dispose();
    }
    
    /**
     * Called when the user clicks on the close button of the ViewFrame.
     * Removes the ViewFrame from the ApplicationManager's list of ViewFrames.
     */
    private void frameClosing() {
        view.cleanUp();
        ApplicationManager.getInstance().removeViewFrame(this);
    }

    /** 
     * Called when the title of a View has been changed.
     *
     * @param event The object containing information about the event.
     */
    public void titleChanged(TitleChangeEvent event)
    {
        this.setTitle(event.getTitle());
    }
}
