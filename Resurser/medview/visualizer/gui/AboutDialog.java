/*
 * AboutDialog.java
 *
 * Created on October 15, 2002, 11:09 AM
 *
 * $Id: AboutDialog.java,v 1.3 2002/11/26 15:07:08 zachrisg Exp $
 *
 * $Log: AboutDialog.java,v $
 * Revision 1.3  2002/11/26 15:07:08  zachrisg
 * Added an empty border around the text to make the dialog look much better.
 *
 * Revision 1.2  2002/10/15 09:36:52  erichson
 * Added title to the dialog
 *
 * Revision 1.1  2002/10/15 09:32:55  erichson
 * First check-in
 *
 */

package medview.visualizer.gui;

import java.awt.*; // FlowLayout
import java.awt.event.*; // ActionListener
import javax.swing.*; // JDialog
import javax.swing.border.*;


import medview.visualizer.data.ApplicationManager;

/**
 * About dialog for the visualizer application
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.0
 */
public class AboutDialog extends JDialog implements ActionListener {
    
    /**
     * Constructs a new AboutDialog.
     *
     * @param parent the parent frame
     * @param modal whether the dialog should be modal or not
     */
    public AboutDialog(java.awt.Frame parent, boolean modal) {
        super(parent, "About " + ApplicationManager.APPLICATION_NAME,modal);
        
        JLabel firstLabel = new JLabel(ApplicationManager.APPLICATION_NAME + " " + ApplicationManager.VERSION_STRING);
        JLabel secondLabel = new JLabel("Programmed by:");
        JLabel thirdLabel = new JLabel("Nils Erichson <d97nix@dtek.chalmers.se>");
        JLabel fourthLabel = new JLabel("G\u00f6ran Zachrisson <e8gz@etek.chalmers.se>");
        
        JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        firstRow.add(firstLabel);
        
        JPanel secondRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        secondRow.add(secondLabel);
        
        JPanel thirdRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        thirdRow.add(thirdLabel);
        
        JPanel fourthRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fourthRow.add(fourthLabel);
        
        JButton closeButton = new JButton("OK");
        closeButton.addActionListener(this);
        
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonRow.add(closeButton);
        
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.setBorder(new EmptyBorder(15,15,15,15));
        contents.add(firstRow);
        contents.add(secondRow);
        contents.add(thirdRow);
        contents.add(fourthRow);
        contents.add(buttonRow);
        
        getContentPane().add(contents);
        
        pack();
        
        setLocationRelativeTo(parent);
    }
    
    /** 
     * Handle action events (called when close button (OK) is pressed).
     *
     * @param ev the action event to handle
     */
    public void actionPerformed(ActionEvent ev) {
        // Close button pressed
        this.dispose();
    }
}



