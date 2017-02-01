/*
 * ViewJFrame.java
 *
 * Created on July 1, 2002, 3:59 PM
 *
 * $Id: ViewJFrame.java,v 1.8 2002/10/30 15:56:41 zachrisg Exp $
 *
 * $Log: ViewJFrame.java,v $
 * Revision 1.8  2002/10/30 15:56:41  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import javax.swing.*;
import java.awt.event.*;

import medview.visualizer.data.*;
import medview.visualizer.event.*;

/**
 * An implementation of the ViewFrame interface using a JFrame that can
 * contain a View.
 *
 * @author  G?ran Zachrisson
 */
public class ViewJFrame extends JFrame implements ViewFrame, TitleChangeListener {

    /** The View contained by this ViewFrame. */
    private View view = null;
    
    /** 
     * Creates new ViewJFrame.
     *
     * @param view The View to show in the frame.
     */
    public ViewJFrame(View view) {
        super(view.getTitle());
        this.view = view;
        view.addTitleChangeListener(this);
        getContentPane().add(view);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
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
            getContentPane().remove(this.view);
            view.removeTitleChangeListener(this);
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
    public void titleChanged(TitleChangeEvent event) {
        this.setTitle(event.getTitle());
    }
    
}
