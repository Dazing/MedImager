/*
 * TableFileFieldChooserDialog.java
 *
 * Created on den 13 september 2004, 17:02
 *
 * $Id: TableFileFieldChooserDialog.java,v 1.3 2005/06/09 08:57:03 erichson Exp $
 *
 * $Log: TableFileFieldChooserDialog.java,v $
 * Revision 1.3  2005/06/09 08:57:03  erichson
 * added getOkClicked()
 *
 * Revision 1.2  2004/10/11 10:02:29  erichson
 * Centering and more informative GUI
 *
 * Revision 1.1  2004/10/05 08:48:09  erichson
 * First check-in.
 *
 */

package medview.visualizer.gui;

import medview.datahandling.examination.tablefile.*;

import java.io.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Dialog to choose which fields are Löpnummer and Patient when loading an MHC table file
 *
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class TableFileFieldChooserDialog extends JDialog
{    
    
    private JComboBox pidBox, lopnummerBox;
    private MHCTableFileExaminationDataHandler mhcHandler;
    private boolean okClicked = false;
    
    /** Creates a new instance of TableFileFieldChooserDialog */
    public TableFileFieldChooserDialog(MHCTableFileExaminationDataHandler handler, Frame parentFrame)
    {
        super(parentFrame,
            "Field chooser for " + handler.getFile().getName(), // choose fields
            true); // Modal
        
        mhcHandler = handler;
        
        String[] fields = mhcHandler.getAvailableFields();
        
        System.err.println("tablefieldhooser: fields[0] = " + fields[0]);
        
        pidBox = new JComboBox(fields);
        lopnummerBox = new JComboBox(fields);
        
        
    
        JLabel pidLabel = new JLabel("PID field:");
        JLabel lopnrLabel = new JLabel("Löpnummer field:");
                        
        JPanel comboPanel = new JPanel();
        
        JLabel instructionsLabel = new JLabel("Choose fields for file " + handler.getFile().getName());
        
        GridBagLayout gbl = new GridBagLayout();
        comboPanel.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // left        
        gbc.anchor = GridBagConstraints.EAST;
        gbl.setConstraints(pidLabel, gbc);
        gbl.setConstraints(lopnrLabel, gbc);
        
        // right
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        gbl.setConstraints(pidBox, gbc);
        gbl.setConstraints(lopnummerBox, gbc);
        
        // Center and take up all the row
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(instructionsLabel, gbc);
        
        comboPanel.add(instructionsLabel);
        comboPanel.add(pidLabel);
        comboPanel.add(pidBox);
        comboPanel.add(lopnrLabel);
        comboPanel.add(lopnummerBox);
        
        /* OK and Cancel panel */
        
        JButton okButton = new JButton(new AbstractAction("OK") {
            public void actionPerformed(ActionEvent ae)
            {
                try {
                    updateHandlerFields();
                    okClicked = true;
                    dispose();
                } 
                catch (IOException ioe)
                {
                    JOptionPane.showMessageDialog(getParent(),"Could not update fields:\n" + ioe.getMessage(),"Could not update fields", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JButton cancelButton = new JButton(new AbstractAction("Cancel") {
            public void actionPerformed(ActionEvent ae)
            {                
                dispose();
            }
        });
        
        JPanel okCancelPanel = new JPanel();
        okCancelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        okCancelPanel.add(okButton);
        okCancelPanel.add(cancelButton);
        
        
        /* Put it all together */
        JPanel allPanel = new JPanel();
        allPanel.setLayout(new BorderLayout());
        allPanel.add(comboPanel,BorderLayout.CENTER);
        allPanel.add(okCancelPanel,BorderLayout.SOUTH);
        
        Container contentPane = getContentPane();
        contentPane.add(allPanel);
        
        pack();                
    }
    
    public void show()
    {
        okClicked = false;
        // Center on parent frame
        setLocationRelativeTo(getParent());        
        super.show();        
    }
    
    public boolean getOkClicked()
    {
        return okClicked;
    }
    
    /**
     * Updates the handler from the combo box selection
     */
    private void updateHandlerFields() throws IOException 
    {
        mhcHandler.setPidField( (String) pidBox.getSelectedItem());
        mhcHandler.setLopnummerField( (String) lopnummerBox.getSelectedItem());
        
    }
}
