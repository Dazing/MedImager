/*
 * 
 *
 * $Id: ExportDialog.java,v 1.7 2004/11/16 07:10:17 erichson Exp $
 *
 * $Log: ExportDialog.java,v $
 * Revision 1.7  2004/11/16 07:10:17  erichson
 * Thread naming
 *
 * Revision 1.6  2004/11/10 13:09:15  erichson
 * export destination checking now uses getExaminationCount()
 *
 * Revision 1.5  2004/11/03 13:53:23  erichson
 * Added count of existing examinations when choosing a directory that already exists.
 *
 * Revision 1.4  2004/11/03 12:46:21  erichson
 * Added Anonymize and Allow Partial options
 *
 * Revision 1.3  2004/10/25 08:22:08  erichson
 * Previous version uncompilable, now fixed
 *
 * Revision 1.2  2004/10/21 12:24:27  erichson
 * Made exporting into a thread, and added progress notification.
 *
 * Revision 1.1  2004/10/12 15:38:37  erichson
 * First check-in.
 *
 */

package medview.visualizer.gui.dialogs;

import java.io.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import medview.datahandling.*; // PatientIdentifier
import medview.datahandling.examination.*; 
import medview.datahandling.examination.filter.*; 
import medview.visualizer.data.*;



/**
 * Dialog for making choices related to MVD export
 * 
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class ExportDialog extends JDialog implements ActionListener {        
    
    private JPanel dialogComponent;
    
    private JPanel exportPanel;
    private JRadioButton exportPatientsRadioButton, 
            exportExaminationsRadioButton;
      
    private JLabel pathLabel;
    private JTextField pathTextField;
    private JButton choosePathButton;
        
    private JCheckBox allowPartialCheckBox, anonymizeCheckBox;
    
    private JButton okButton, cancelButton;
    
    private final JFileChooser fileChooser;
    private ExaminationDataElement[] dataElements;
    
    private PatientIdentifier[] patientIdentifiers;
    
    /** Creates a new instance of DatabaseDialog */
    public ExportDialog(Frame parentFrame, 
                        JFileChooser mvdfileChooser, 
                        ExaminationDataElement[] elements) {
        super(parentFrame, 
              "Export",
              true); // modal
        /*
        try {
            allElements = DataManager.getInstance().getCompletePatientExaminationSet(elements);            
        } catch (IOException ioe) {
            ApplicationManager.errorMessage("Cold not get complete examination set: IOException: " + ioe.getMessage());
            allElements = new ExaminationDataElement[0];
        }
        */
        this.fileChooser = mvdfileChooser;
        dataElements = elements;        
        
        
        String chosenExaminationsCountString;                 
        try {        
            int chosenExaminationsCount = DataManager.getUniqueExaminationIdentifiers(dataElements);
            chosenExaminationsCountString = new Integer(chosenExaminationsCount).toString();
        } catch (IOException ioe)
        {
            chosenExaminationsCountString = "IOException: " + ioe.getMessage();
        }
        
        String allExaminationsCountString;
        int patientCount = -1;
        try {
            patientIdentifiers = DataManager.getPatients(dataElements);
            patientCount = patientIdentifiers.length;
            int allExaminationsCount = DataManager.getInstance().getCompletePatientExaminationCount(patientIdentifiers);
            allExaminationsCountString = new Integer(allExaminationsCount).toString();
        } catch (IOException ioe)
        {
            allExaminationsCountString = "IOException: " + ioe.getMessage();
        }
        
        
        exportExaminationsRadioButton = new JRadioButton("Only export the examinations I have chosen. (" + chosenExaminationsCountString + " examinations)");
        exportPatientsRadioButton = new JRadioButton("Export all examinations for these " + patientCount + " patients. (" +  allExaminationsCountString + " examinations)");
        exportExaminationsRadioButton.setSelected(true);
                
        ButtonGroup exportTypeBG = new ButtonGroup();
        exportTypeBG.add(exportPatientsRadioButton);
        exportTypeBG.add(exportExaminationsRadioButton);        
        
        JPanel exportTypePanel = new JPanel();
        
        BoxLayout exportTypeLayout = new BoxLayout(exportTypePanel, BoxLayout.Y_AXIS);
        exportTypePanel.setLayout(exportTypeLayout);       
        exportTypePanel.add(exportExaminationsRadioButton);
        exportTypePanel.add(exportPatientsRadioButton);
                
        pathTextField = new JTextField(40);
        pathLabel = new JLabel("Target MVD: ");
        choosePathButton = new JButton("...");
        choosePathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)
            {
                showTheFileChooser(); // Choose where to store the MVD                        
            }
        });
        
        JPanel pathPanel = new JPanel();
        BoxLayout pathBoxLayout = new BoxLayout(pathPanel, BoxLayout.LINE_AXIS);
        pathPanel.setLayout(pathBoxLayout);
        
        pathPanel.add(pathLabel);
        pathPanel.add(pathTextField);
        pathPanel.add(choosePathButton);
        
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
        allowPartialCheckBox = new JCheckBox("Allow partial export");
        anonymizeCheckBox = new JCheckBox("Anonymize (remove PID)");
        checkBoxPanel.add(allowPartialCheckBox);
        checkBoxPanel.add(anonymizeCheckBox);
        
        exportPanel = new JPanel();
        exportPanel.setLayout(new BoxLayout(exportPanel, BoxLayout.Y_AXIS)); // rderLayout());
        exportPanel.add(pathPanel); //,BorderLayout.NORTH);
        exportPanel.add(exportTypePanel); //,BorderLayout.CENTER);
        exportPanel.add(checkBoxPanel);
        exportPanel.setBorder(BorderFactory.createTitledBorder("Export options"));                
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        okButton = new JButton("Export");                
        cancelButton = new JButton("Cancel");        
        okButton.setMnemonic('E');        
        cancelButton.setMnemonic('C');        
        okButton.addActionListener(this);        
        cancelButton.addActionListener(this);
        
        buttonPanel.add(okButton);        
        buttonPanel.add(cancelButton);
        
        dialogComponent = new JPanel();        
        dialogComponent.setLayout(new BorderLayout());                
        dialogComponent.add(exportPanel, BorderLayout.CENTER);
        dialogComponent.add(buttonPanel, BorderLayout.SOUTH);
                
        Container contentPane = getContentPane();
        contentPane.add(dialogComponent);
        
        pack();        
    }
    
    public void show() {    
        showTheFileChooser();
        setLocationRelativeTo(getParent());
        super.show();        
    }
    
    private void showTheFileChooser()
    {
        int result = fileChooser.showOpenDialog(getParent());
        if (result == JFileChooser.APPROVE_OPTION) 
        {
            pathTextField.setText(fileChooser.getSelectedFile().getPath());
        }
    }
    
    // TODO: Move export code out from ExportDialog
    // Handle actions from OK, apply and cancel buttons
    public void actionPerformed(ActionEvent ev) {        
        Object src = ev.getSource();
                
        if (src == cancelButton) {            
            dispose();
        }
        
        if (src == okButton) {            
            // OK Button clicked -> start exporting
            
            String selectedFilePath = pathTextField.getText();
            if (! (selectedFilePath.toUpperCase().endsWith(".MVD")))
            {
                // Selected file does not end in MVD
                selectedFilePath += ".MVD";
            }

            File selectedFile = new File(selectedFilePath);                        
            
            if (selectedFile.exists()) {
                //ApplicationManager.errorDialog("The directory " + selectedFile.getName() + " already exists!");

                int existingExaminations = 0;
                
                try 
                {
                    // Check examination amount in the directory. TODO: getExaminationCount() in EDH which could do this faster
                    MVDHandler mvdh = new MVDHandler();
                    mvdh.setExaminationDataLocation(selectedFilePath);
                    /*PatientIdentifier[] patients = mvdh.getPatients();
                    int examinationCount = 0;
                    for (int p = 0; p < patients.length; p++)
                    {
                        ExaminationIdentifier[] examinations = mvdh.getExaminations(patients[p]);
                        existingExaminations += examinations.length;
                    } 
                    */
                    existingExaminations = mvdh.getExaminationCount();
                }
                catch (IOException ioe)
                {
                    ApplicationManager.errorMessage("Could not list examinations in MVD " + selectedFilePath + ": " + ioe.getMessage());
                }
                
                
                // Ask whether to add/overwrite
                int overwrite_result = JOptionPane.showConfirmDialog(getParent(),
                                              "The directory " + selectedFile.getName() + " already exists! (Forest contains " + existingExaminations + " examinations)\n" +
                                                                                          "Is it OK to try to export to an existing MVD?\n" +
                                                                                          "(Existing examinations will not be overwritten)",
                                              "MVD Already exists", // title, 
                                              JOptionPane.OK_CANCEL_OPTION, // optiontype,
                                              JOptionPane.QUESTION_MESSAGE); // message type

                if (overwrite_result == JOptionPane.CANCEL_OPTION)                    
                {
                    return; // stop, don't dispose
                }
            }
            
            // OK, go ahead
            dispose(); // close down dialog    
            DefaultProgressObject progressObject = new DefaultProgressObject(1,100,1,true); // Start as indeterminate                                    
            
            TermFilter filter = null;            
            
            if (anonymizeCheckBox.isSelected())
            {
                String[] filteredTerms = { "PID" };
                filter = new TermFilter(filteredTerms);                
            }                        
            
            ExportThread exportThread = new ExportThread(selectedFilePath, 
                                                         exportPatientsRadioButton.isSelected(), // Export patients or not
                                                         progressObject,
                                                         filter,
                                                         allowPartialCheckBox.isSelected()); // allow partial
            exportThread.start();            
                                      
            medview.visualizer.gui.ApplicationFrame.getInstance().showProgressDialog(progressObject, "Exporting examinations");
        } // end of (if src = okbutton)
    } // end actionperformed
            
    private class ExportThread extends Thread
    {
        private AbstractProgressObject progressObject;
        private boolean exportPatients;
        private String selectedFilePath;
        private ExaminationContentFilter filter;
        private boolean allowPartial;
        
        public ExportThread(String selectedFilePath, 
                            boolean exportPatients, 
                            AbstractProgressObject progressObject,
                            ExaminationContentFilter filter,
                            boolean allowPartialExport)
        {
            super("ExportThread-"+selectedFilePath);
            this.progressObject = progressObject;
            this.exportPatients = exportPatients;
            this.selectedFilePath = selectedFilePath;
            this.filter = filter;
            this.allowPartial = allowPartialExport;
        }
        
        public void run() 
        {
            selectedFilePath += File.separatorChar; // Make sure path is a direcory, so it gets created
            File selectedFile = new File(selectedFilePath);                
            selectedFile.mkdirs();
                                             
            try 
            {
               int exportedAmount;
               if (exportPatients)
               {
                   exportedAmount = DataManager.getInstance().exportPatientExaminations(patientIdentifiers, selectedFile, progressObject, filter, allowPartial);
               } else { // Export only these examinations
                   exportedAmount = DataManager.getInstance().exportExaminations(dataElements, selectedFile, progressObject, filter, allowPartial);  
               }                              

               // Store the chosen export target in settings
               Settings.getInstance().setMVDFileChooserDir(selectedFile.getParent());
            } catch (IOException ioe)
            {
                progressObject.cancel();
                JOptionPane.showMessageDialog(getParent(),
                        "IOException: " + ioe.getMessage(),
                        "Export failed",
                        JOptionPane.ERROR_MESSAGE);                                
            }
        }    
    }        
    
    /**
     * Test method for the GUI
     */
    
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        new ExportDialog(frame,
                         new JFileChooser(),  
                         null).show();
        
    }
}
