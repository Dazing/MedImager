/*
 * TextInputDialog.java
 *
 * Created on July 23, 2002, 2:25 PM
 *
 * $Id: TextInputDialog.java,v 1.7 2004/07/23 13:25:39 erichson Exp $
 *
 * $Log: TextInputDialog.java,v $
 * Revision 1.7  2004/07/23 13:25:39  erichson
 * Ok --> OK (BZ #137)
 *
 * Revision 1.6  2002/12/03 15:09:46  zachrisg
 * Added some space around the textfield.
 *
 * Revision 1.5  2002/12/03 15:04:42  zachrisg
 * Changed "Avbryt" to "Cancel".
 *
 * Revision 1.4  2002/12/03 15:03:53  zachrisg
 * Now the text is preselected.
 *
 * Revision 1.3  2002/10/30 15:56:36  zachrisg
 * Added Id and Log tags and updated javadoc.
 *
 */

package medview.visualizer.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;

/**
 * A modal text input dialog.
 *
 * @author Göran Zachrisson <zachrisg@mdstud.chalmers.se>
 */
public class TextInputDialog extends JDialog implements ActionListener {
    
    /** The textfield allowing the user to enter text. */
    private JTextField textField;
    
    /** The label displaying the message. */
    private JLabel messageLabel;    
    
    /** The ok button. */
    private JButton okButton;
    
    /** The cancel button. */
    private JButton cancelButton;
    
    /** The text string to return. */
    private String text = null;
    
    /** 
     * Creates a modal text input dialog. 
     *
     * @param owner The owner frame of the dialog.
     * @param title The title of the dialog.
     * @param message The message to display.
     * @param initialValue The initial value of the textfield.
     */
    public TextInputDialog(Frame owner, String title, String message, String initialValue) {
        super(owner, title, true);
        
        // create text components
        textField = new JTextField(initialValue);
        textField.setCaretPosition(0);
        textField.moveCaretPosition(textField.getDocument().getLength());
        messageLabel = new JLabel(message);
        
        // add text components to a panel
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(new EmptyBorder(15,15,15,15));
        textPanel.add(messageLabel, BorderLayout.WEST);
        textPanel.add(textField, BorderLayout.CENTER);
        
        // create the buttons
        okButton = new JButton("OK");
        okButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        
        // add the buttons to a panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        // add the panels to the dialog
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(textPanel,BorderLayout.NORTH);
        getContentPane().add(buttonPanel,BorderLayout.SOUTH);

        // set the default button
        getRootPane().setDefaultButton(okButton);
    
        pack();
        Dimension size = getSize();
        size.width *= 2;
        setSize(size);
    }
        
    /**
     * Handles action events.
     *
     * @param event The event to handle.
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == okButton) {
            text = textField.getText();
            setVisible(false);
        } else if (event.getSource() == cancelButton) {
            text = null;
            setVisible(false);
        }
    }
    
    /**
     * Displays a modal text input dialog.
     *
     * @param owner The owner frame of the dialog.
     * @param title The title of the dialog.
     * @param message The message to display.
     * @param initialValue The initial value of the textfield.
     * @return The entered string if "ok" is pressed, null if "cancel" or the close button is pressed.
     */
    public static String showTextInputDialog(Frame owner, Point location, String title, String message, String initialValue) {
        TextInputDialog dialog = new TextInputDialog(owner,title,message,initialValue);
        dialog.setLocation(location);
        dialog.setVisible(true);
        return dialog.text;
    }
}
