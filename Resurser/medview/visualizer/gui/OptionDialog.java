/*
 * OptionDialog.java
 *
 * Created on August 26, 2002, 9:53 AM
 *
 * $Id: OptionDialog.java,v 1.3 2002/11/07 16:37:37 erichson Exp $
 *
 * $Log: OptionDialog.java,v $
 * Revision 1.3  2002/11/07 16:37:37  erichson
 * now supports Components, also there is another show method which doesnt take position but centers the dialog on the parent frame instead
 *
 * Revision 1.2  2002/10/30 15:56:31  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A modal dialog that lets the user select one of a couple of options.
 *
 * @author G?ran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class OptionDialog extends JDialog implements ActionListener {
    
    /** The buttons. */
    private JButton[] buttons;

    /** The index to return. */
    private int returnIndex = -1;
    
    private Component component;
    
    /** 
     * Creates a new instance of OptionDialog.
     *
     * @param owner The owner Frame.
     * @param title The title of the dialog.
     * @param content The content of the dialog (Component, String or something that can be converted to string)
     * @param buttonStrings An array of the strings that will be shown as buttons.
     */
    public OptionDialog(Frame owner, String title, Object content, String[] buttonStrings) {
        super(owner,title,true);        
        
        if (content instanceof Component)
            component = (Component) content;
        else 
            component = new JLabel(content.toString());
                
        // create the buttons and the button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttons = new JButton[buttonStrings.length];
        for (int i = 0; i < buttonStrings.length; i++) {
            buttons[i] = new JButton(buttonStrings[i]);
            buttons[i].addActionListener(this);
            buttonPanel.add(buttons[i]);
        }                        
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(component, BorderLayout.CENTER);        
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // set the default button
        if (buttons.length > 0) {
            getRootPane().setDefaultButton(buttons[0]);
        }
        
        // set the size
        pack();
        Dimension size = getSize();
        size.height *= 1.5;
        setSize(size);
    }

    
    /**
     * Returns the index of the selected button or -1 if the dialog was closed.
     *
     * @return The index of the selected button.
     */
    public int getSelectedIndex() {
        return returnIndex;
    }
    
    /**
     * Displays an OptionDialog and returns the selected index.
     *
     * @param owner The owner Frame.
     * @param position The position of the dialog. (if null, dialog will be centered on parent frame)
     * @param title The title of the dialog.
     * @param content The content of the dialog (Component, String or something that can be converted to string)
     * @param buttonStrings An array of the strings that will be shown as buttons.
     * @return The selected index.
     */    
    public static int showOptionDialog(Frame owner, Point position, String title, Object content, String[] buttonStrings) {
        OptionDialog dialog = new OptionDialog(owner,title,content,buttonStrings);
        if (position == null) {
            dialog.setLocationRelativeTo(owner);
        } else {
            dialog.setLocation(position);
        }
        dialog.setVisible(true);
        return dialog.getSelectedIndex();
    }
    
    /** 
     * Displays an OptionDialog (centered on the parent frame) and returns the selected index.
     * @param owner The owner Frame.     
     * @param title The title of the dialog.
     * @param content The content of the dialog (Component, String or something that can be converted to string)
     * @param buttonStrings An array of the strings that will be shown as buttons.
     * @return The selected index.
     */
    public static int showOptionDialog(Frame owner, String title, Object content, String[] buttonStrings) {
        return showOptionDialog(owner, null, title, content, buttonStrings);
    }
        
    /**
     * Handles button clicks.
     *
     * @param event The event object.
     */
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        for (int i = 0; i < buttons.length; i++) {
            if (source == buttons[i]) {
                returnIndex = i;
                setVisible(false);
            }
        }
    }
    
}
