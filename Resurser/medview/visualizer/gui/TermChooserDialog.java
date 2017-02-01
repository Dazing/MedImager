/*
 * TermChooserDialog.java
 *
 * Created on August 23, 2002, 4:22 PM
 *
 * $Id: TermChooserDialog.java,v 1.11 2008/08/25 09:23:17 it2aran Exp $
 *
 * $Log: TermChooserDialog.java,v $
 * Revision 1.11  2008/08/25 09:23:17  it2aran
 * T4 Server updates so it loads the mvdlocation from the package
 * Visualizer: Chosen terms doesn't have to be sorted alpabetically
 * Visualizer: Can load and save chosen terms
 * Updated the release notes
 *
 * Revision 1.10  2008/08/22 08:20:49  it2aran
 * New feature: save and load terms that shoud be visible in grids etc.
 *
 * Revision 1.9  2005/06/15 11:59:41  erichson
 * Fixed layout to allow the dialog contents to resize properly (Bug 469)
 *
 * Revision 1.8  2005/05/20 08:20:08  erichson
 * No real changes, just re-generated
 *
 * Revision 1.7  2005/01/26 12:53:29  erichson
 * Updated after term handling overhaul.
 *
 * Revision 1.6  2004/12/17 11:49:03  erichson
 * Added link to TermChooser instead of hardcoding changes to the DataManager.
 *
 * Revision 1.5  2002/10/14 10:44:29  erichson
 * Added "term chooser" title to the frame, added javadoc // NE
 *
 */

package medview.visualizer.gui;

import medview.visualizer.event.*; // Listeners

import medview.visualizer.data.*;
import medview.datahandling.MedViewDataHandler;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

import misc.gui.ExtensionFileFilter; // DataManager

/**
 * Modal dialog which contains a TermChooserPanel.
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 * @version 1.0
 */
public class TermChooserDialog extends javax.swing.JDialog  {
    
    private TermChooserPanel termChooserPanel;
    private ChosenTermsContainer externalTermContainer; // The term container to update when OK is clicked
    
    private final static DataManager DM = DataManager.getInstance();
    
    /*public TermChooserDialog(java.awt.Frame parent, TermContainer termContainer, boolean modal)
    {
        this(parent, termContainer, "Term chooser", modal);
    }
    */
    
    /** Creates new form TermChooserDialog
     * @param parent the frame which is to be the parent of this dialog
     * @param modal whether this dialog should be modal or not
     * @param termContainer the term container to update if OK is clicked
     */
    public TermChooserDialog(java.awt.Frame parent, ChosenTermsContainer termContainer, String title, boolean modal)
    {
        super(parent, title, modal);        
        this.externalTermContainer = termContainer;
        initComponents();
        
        String[] chosenTerms = termContainer.getChosenTerms();
        String[] allTerms = DataManager.getInstance().getAllTerms();
        
        java.util.HashSet termSet = new java.util.HashSet(chosenTerms.length);
        
        for (int i = 0; i < allTerms.length; i++)
        {
            termSet.add(allTerms[i]);
        }
        
        for (int i = 0; i < chosenTerms.length; i++)
        {
            termSet.remove(chosenTerms[i]);
        }
        
        //String[] unChosenTerms = termContainer.getUnchosenTerms();
        String[] unChosenTerms = new String[termSet.size()];
        unChosenTerms = (String[]) termSet.toArray(unChosenTerms);
        
        System.out.println("TermChooserDialog: chosenterms="+chosenTerms.length+",unchosenterms="+unChosenTerms.length);
        
        termChooserPanel = new TermChooserPanel(chosenTerms, unChosenTerms); 
        centerPanel.add(termChooserPanel);
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        centerPanel = new javax.swing.JPanel();
        southPanel = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        buttonPanel1 = new javax.swing.JPanel();
        buttonButtonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        loadButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        centerPanel.setLayout(new java.awt.BorderLayout());

        getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setLayout(new java.awt.GridLayout(1, 0));

        southPanel.add(buttonPanel);

        buttonButtonPanel.setLayout(new java.awt.GridLayout(1, 0));

        loadButton.setText("Load...");
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });

        buttonButtonPanel.add(loadButton);
        
        saveButton.setText("Save...");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        buttonButtonPanel.add(saveButton);
        
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        buttonButtonPanel.add(okButton);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        buttonButtonPanel.add(cancelButton);

        buttonPanel1.add(buttonButtonPanel);

        southPanel.add(buttonPanel1);

        getContentPane().add(southPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);        
        externalTermContainer.setChosenTerms(termChooserPanel.getChosenTerms());
        
    }

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        JFileChooser fc = new JFileChooser(MedViewDataHandler.instance().getUserHomeDirectory());
        fc.setFileFilter(new ExtensionFileFilter("tsf", "Term Selection File (.tsf)",false));
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            ArrayList<String> terms = new ArrayList<String>();
            try
            {
                  BufferedReader input =  new BufferedReader(new FileReader(file));
                  try
                  {
                    String line = null;
                    while (( line = input.readLine()) != null)
                    {
                        terms.add(line);
                    }
                  }
                  finally
                  {
                    input.close();
                  }
                  termChooserPanel.setChosenTerms(terms.toArray(new String [terms.size()]));
            }
            catch (IOException ex)
            {
                System.err.println(ex);
                JOptionPane.showMessageDialog(this,"Couldn't load terms: " + ex.getMessage(),"Error loading",JOptionPane.ERROR_MESSAGE);
            }
            
        }
        else
        {
            //Open cancelled by user
        }
    }
    
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
       JFileChooser fc = new JFileChooser(MedViewDataHandler.instance().getUserHomeDirectory());
        fc.setFileFilter(new ExtensionFileFilter("tsf", "Term Selection File (.tsf)",false));
        int returnVal = fc.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            
            if (!file.getPath().toLowerCase().endsWith(".tsf")) 
            {
                file = new File(file.getPath() + ".tsf");
            }
            
            if (file.exists())
            {
                int ok_to_overwrite = JOptionPane.showConfirmDialog(this,
                                              "File " + file.getName() + " exists! Overwrite?",
                                              "File exists",
                                              JOptionPane.YES_NO_OPTION,
                                              JOptionPane.ERROR_MESSAGE);
                if (ok_to_overwrite == JOptionPane.NO_OPTION)
                    return;
                
                // if not "No" option, continue saving (and overwrite!);
                               
            }
            
            
            
            String[] terms = termChooserPanel.getChosenTerms();

            try
            {
                FileWriter fw = new FileWriter(file);
                for(String s :terms)
                {
                    fw.write(s + '\n');
                }
                fw.close();
            }
            catch (IOException ex)
            {
                System.err.println(ex);
                JOptionPane.showMessageDialog(this,"Couldn't save terms: " + ex.getMessage(),"Error saving",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
    }
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible(false);
        //dispose();
    }
    
    /*public int showTermChooserDialog()
    {
        show(); 
    }*/
    
    /** Main method for stand-alone testing
     * @param args the command line arguments
     */
    /*public static void main(String args[]) {
        new TermChooserDialog(new javax.swing.JFrame(), true).show();
    }
    */
    

    private javax.swing.JPanel buttonButtonPanel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel buttonPanel1;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JButton okButton;
    private javax.swing.JButton loadButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JPanel southPanel;

    /*
    public void termsChanged(TermsChangeEvent tce) {
        termChooserPanel.updateTerms();
    }
    */
}
