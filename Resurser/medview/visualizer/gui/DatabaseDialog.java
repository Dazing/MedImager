/*
 * DatabaseDialog.java
 * 
 * $Id: DatabaseDialog.java,v 1.6 2004/10/11 10:00:46 erichson Exp $
 *
 * $Log: DatabaseDialog.java,v $
 * Revision 1.6  2004/10/11 10:00:46  erichson
 * Fixed show so that the dialog gets centered
 *
 * Revision 1.5  2004/07/26 14:54:24  erichson
 * Cleaner GUI (separated into two parts); added authentication type choices.
 *
 * Revision 1.4  2004/04/12 20:21:33  erichson
 * Cleanup and moved data functionality to ApplicationManager (should not be in GUI class)
 *
 * Revision 1.3  2004/03/28 18:00:24  erichson
 * pidField, examination identifier field, table field, examination identifier type radio buttons added
 *
 * Revision 1.2  2004/02/23 12:17:52  erichson
 * Better DB N/A error message
 *
 * Revision 1.1  2004/02/23 12:16:04  erichson
 * First check-in
 * 
 */

package medview.visualizer.gui;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.*;

import medview.datahandling.examination.InvalidDataLocationException;
import medview.datahandling.examination.SQLExaminationDataHandler; // ExaminationIdentifierType constants
import medview.visualizer.data.*;

/**
 * Dialog for inputting the parameters used to connect to an SQL database.
 * 
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public class DatabaseDialog extends JDialog implements ActionListener {
    
    private Settings settings = Settings.getInstance();
    
    private JPanel dialogComponent;
    
    private JPanel serverAuthTypePanel;
    private JRadioButton serverAuthTypeSQLButton, serverAuthTypeWindowsButton;
    private JTextField serverWindowsDomainField;
    private JTextField serverAddressField;
    private JTextField serverPortField;
    private JTextField serverUserField;
    private JPasswordField serverPasswordField;
    private JTextField serverCatalogField;
    private JTextField serverTableField;
    
    // -----
    private JTextField pidFieldField;
    private JTextField examinationIdentifierFieldField;
    private JRadioButton dateExaminationIdentifierRadioButton, 
                         stringExaminationIdentifierRadioButton;
    
    private JButton okButton, applyButton, cancelButton;
    
    /** Creates a new instance of DatabaseDialog */
    public DatabaseDialog(Frame parentFrame) {
        super(parentFrame, 
              "Database connection settings",
              true); // modal
        
        initComponents();
    }
    
    private void initComponents() {
    
        
        serverAuthTypeSQLButton = new JRadioButton("SQL");
        serverAuthTypeWindowsButton = new JRadioButton("Windows domain:");
        ButtonGroup authButtonBG = new ButtonGroup();
        authButtonBG.add(serverAuthTypeSQLButton);
        authButtonBG.add(serverAuthTypeWindowsButton);
        serverAuthTypeSQLButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ev)
            {
                serverWindowsDomainField.setEnabled(false);
            }
        });
        
        serverAuthTypeWindowsButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent ev)
            {
                serverWindowsDomainField.setEnabled(true);
            }
        });
        
        
        serverWindowsDomainField = new JTextField(20);
        serverAuthTypePanel = new JPanel();    
        serverAuthTypePanel.setLayout(new BoxLayout(serverAuthTypePanel, BoxLayout.LINE_AXIS));
        serverAuthTypePanel.add(serverAuthTypeSQLButton);
        serverAuthTypePanel.add(serverAuthTypeWindowsButton);
        serverAuthTypePanel.add(serverWindowsDomainField);
        
        serverAddressField = new JTextField(40);
        serverPortField = new JTextField(5);
        serverUserField = new JTextField(20);
        serverPasswordField = new JPasswordField(20);
        serverCatalogField = new JTextField(30);
        serverTableField = new JTextField(20);
        
        examinationIdentifierFieldField = new JTextField(20);
        pidFieldField = new JTextField(20);
        
        dateExaminationIdentifierRadioButton = new JRadioButton("Date", true);
        stringExaminationIdentifierRadioButton = new JRadioButton("String", false);
        ButtonGroup bg = new ButtonGroup();
        bg.add(dateExaminationIdentifierRadioButton);
        bg.add(stringExaminationIdentifierRadioButton);
        
        JPanel examinationIdentifierTypePanel = new JPanel();
        examinationIdentifierTypePanel.add(dateExaminationIdentifierRadioButton);
        examinationIdentifierTypePanel.add(stringExaminationIdentifierRadioButton);
        
        GridBagLayout servergbl = new GridBagLayout();
        GridBagLayout dbgbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        
        dialogComponent = new JPanel();        
        dialogComponent.setLayout(new BorderLayout());
             
        JPanel serverInfoPanel = new JPanel();
        serverInfoPanel.setLayout(servergbl);
        serverInfoPanel.setBorder(BorderFactory.createTitledBorder("DB Server settings"));
        
        JPanel dbInfoPanel = new JPanel();
        dbInfoPanel.setLayout(dbgbl);
        dbInfoPanel.setBorder(BorderFactory.createTitledBorder("Database structure settings"));
        
        JLabel serverAuthTypeLabel = new JLabel("Authentication type:");
        JLabel serverWindowsDomainLabel = new JLabel("Windows domain:");
        JLabel serverAddressLabel = new JLabel("Server address:");
        JLabel serverPortLabel = new JLabel("Server port:");
        JLabel serverUserLabel = new JLabel("User name:");
        JLabel serverPasswordLabel = new JLabel("Password:");
        JLabel serverCatalogLabel = new JLabel("Catalog:");                        
        JLabel serverTableLabel = new JLabel("Database table:");
        
        JLabel pidFieldLabel = new JLabel("Patient identifier field:");
        JLabel examinationIdentifierFieldLabel = new JLabel("Examination identifier field:");
        JLabel examinationIdentifierTypeLabel = new JLabel("Examination identifier type:");
        
        
        
        gbc.anchor = GridBagConstraints.EAST;
        servergbl.setConstraints(serverAuthTypeLabel, gbc);
        servergbl.setConstraints(serverWindowsDomainLabel, gbc);
        servergbl.setConstraints(serverAddressLabel, gbc);
        servergbl.setConstraints(serverPortLabel, gbc);
        servergbl.setConstraints(serverUserLabel, gbc);
        servergbl.setConstraints(serverPasswordLabel, gbc);
        
        dbgbl.setConstraints(serverCatalogLabel, gbc);
        dbgbl.setConstraints(serverTableLabel,gbc);
        
        dbgbl.setConstraints(pidFieldLabel, gbc);
        dbgbl.setConstraints(examinationIdentifierFieldLabel, gbc);
        dbgbl.setConstraints(examinationIdentifierTypeLabel, gbc);
        
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;
        servergbl.setConstraints(serverAuthTypePanel, gbc);
        servergbl.setConstraints(serverWindowsDomainField, gbc);
        servergbl.setConstraints(serverAddressField, gbc);
        servergbl.setConstraints(serverPortField, gbc);
        servergbl.setConstraints(serverUserField, gbc);
        servergbl.setConstraints(serverPasswordField, gbc);
        
        dbgbl.setConstraints(serverCatalogField, gbc);
        dbgbl.setConstraints(serverTableField, gbc);
        
        dbgbl.setConstraints(examinationIdentifierFieldField, gbc);
        dbgbl.setConstraints(pidFieldField, gbc);
        dbgbl.setConstraints(examinationIdentifierTypePanel, gbc);
        
        serverInfoPanel.add(serverAuthTypeLabel);
        serverInfoPanel.add(serverAuthTypePanel);
        
        serverInfoPanel.add(serverAddressLabel);
        serverInfoPanel.add(serverAddressField);
        
        serverInfoPanel.add(serverPortLabel);
        serverInfoPanel.add(serverPortField);
        
        serverInfoPanel.add(serverUserLabel);
        serverInfoPanel.add(serverUserField);
        
        serverInfoPanel.add(serverPasswordLabel);                                
        serverInfoPanel.add(serverPasswordField);
        
        dbInfoPanel.add(serverCatalogLabel);                                
        dbInfoPanel.add(serverCatalogField);
        
        dbInfoPanel.add(serverTableLabel);
        dbInfoPanel.add(serverTableField);
        
        // pid, exId, exIdType                
        
        dbInfoPanel.add(pidFieldLabel);
        dbInfoPanel.add(pidFieldField);
        
        dbInfoPanel.add(examinationIdentifierFieldLabel);
        dbInfoPanel.add(examinationIdentifierFieldField);
        
        dbInfoPanel.add(examinationIdentifierTypeLabel);
        dbInfoPanel.add(examinationIdentifierTypePanel);
        
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        okButton = new JButton("OK (Load)");        
        applyButton = new JButton("Apply");
        cancelButton = new JButton("Cancel");
        
        okButton.setMnemonic('O');
        applyButton.setMnemonic('A');
        cancelButton.setMnemonic('C');
        
        okButton.addActionListener(this);
        applyButton.addActionListener(this);
        cancelButton.addActionListener(this);
        
        buttonPanel.add(okButton);
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);
        
        dialogComponent.add(serverInfoPanel, BorderLayout.NORTH);
        dialogComponent.add(dbInfoPanel, BorderLayout.CENTER);
        dialogComponent.add(buttonPanel, BorderLayout.SOUTH);
                
        Container contentPane = getContentPane();
        contentPane.add(dialogComponent);
        
        pack();        
    }
    
    /** 
     * Update fields from Settings
     */
    private void loadSettings() {
        Integer portNumber = new Integer(settings.getDatabaseServerPort());
        
        serverAddressField.setText(settings.getDatabaseServerAddress());
        serverPortField.setText( portNumber.toString());
        serverUserField.setText(settings.getDatabaseUser());
        serverPasswordField.setText(settings.getDatabasePassword()); 
        serverCatalogField.setText(settings.getDatabaseCatalog());
        serverTableField.setText(settings.getDatabaseTable());
        serverWindowsDomainField.setText(settings.getDatabaseWindowsDomain());
        
        // pidfield, exIdfield, exIdType
        pidFieldField.setText(settings.getDatabasePIDField());
        examinationIdentifierFieldField.setText(settings.getDatabaseExaminationIdentifierField());
        
        switch (settings.getDatabaseExaminationIdentifierType())
        {
            case SQLExaminationDataHandler.EXAMINATIONIDENTIFIER_DATE:
                dateExaminationIdentifierRadioButton.setSelected(true);
                break;
            case SQLExaminationDataHandler.EXAMINATIONIDENTIFIER_STRING:
                stringExaminationIdentifierRadioButton.setSelected(true);
                break;
        }
        switch (settings.getDatabaseAuthenticationType())
        {
            case SQLExaminationDataHandler.AUTHENTICATION_SQL:
                serverAuthTypeSQLButton.setSelected(true);
                serverWindowsDomainField.setEnabled(false);
                break;
            case SQLExaminationDataHandler.AUTHENTICATION_WINDOWS:
                serverAuthTypeWindowsButton.setSelected(true);
                serverWindowsDomainField.setEnabled(true);
                break;
        }
        
    }
        
    /**
     * Store contents of fields in Settings
     */ 
    private void storeSettings() throws NumberFormatException {
        int portNumber = Integer.parseInt(serverPortField.getText().trim());
        settings.setDatabaseServerPort(portNumber); // Throws exception                            
        settings.setDatabaseServerAddress(serverAddressField.getText().trim());        
        settings.setDatabaseUser(serverUserField.getText().trim());
        settings.setDatabasePassword(serverPasswordField.getText());
        settings.setDatabaseCatalog(serverCatalogField.getText().trim());                
        settings.setDatabaseTable(serverTableField.getText().trim());
        settings.setDatabaseWindowsDomain(serverWindowsDomainField.getText().trim());
        
        settings.setDatabaseExaminationIdentifierField( examinationIdentifierFieldField.getText().trim());
        settings.setDatabasePIDField(pidFieldField.getText().trim());
        
        if (stringExaminationIdentifierRadioButton.isSelected())
            settings.setDatabaseExaminationIdentifierType(SQLExaminationDataHandler.EXAMINATIONIDENTIFIER_STRING);
        else
            settings.setDatabaseExaminationIdentifierType(SQLExaminationDataHandler.EXAMINATIONIDENTIFIER_DATE);
        
        if (serverAuthTypeWindowsButton.isSelected())
            settings.setDatabaseAuthenticationType(SQLExaminationDataHandler.AUTHENTICATION_WINDOWS);
        else
            settings.setDatabaseAuthenticationType(SQLExaminationDataHandler.AUTHENTICATION_SQL);
        
        // pidfield, exIdField, exIdType
    }
    
    public void show() {
        loadSettings();
        setLocationRelativeTo(getParent());
        super.show();
    }
    
    // Handle actions from OK, apply and cancel buttons
    public void actionPerformed(ActionEvent ev) {        
        Object src = ev.getSource();
        
        if ((src == okButton) || (src == applyButton)) {            
            storeSettings();
        }
        
        if ((src == okButton) || (src == cancelButton)) {            
            dispose();
        }
        
        if (src == okButton) {            
            // OK Button clicked -> start loading data
            try 
            {
                ApplicationManager.getInstance().chooseDataGroupThenStartLoading(DataManager.createDBURI());            
            } 
            
            catch (java.net.URISyntaxException urise)
            {
                ApplicationManager.errorMessage("Could not start loading from DB: Database URI has a syntax error: " + urise.getMessage());
            }
        }
    
    }        
}
