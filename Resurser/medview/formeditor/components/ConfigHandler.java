/*
 *
 * $Id: ConfigHandler.java,v 1.4 2004/11/04 12:04:39 lindahlf Exp $
 *
 */

package medview.formeditor.components;

import medview.formeditor.data.*;
import java.lang.String;
import java.awt.*;
import javax.swing.*;
import javax.swing.UIManager;
import medview.formeditor.tools.*;

/**
 * Editor component for MedRecords Config
 *
 * @author  nader
 */
public class ConfigHandler extends javax.swing.JDialog {

    private DatahandlingHandler mDH = DatahandlingHandler.getInstance();

    private  String[] mListOfValuesNames = {
        mDH.getLanguageString(mDH.LABEL_PREVIEW_TRANSLATOR_LS_PROPERTY), //"Translator File",
        mDH.getLanguageString(mDH.LABEL_PREVIEW_TEMPLATE_LS_PROPERTY), //"Preview Template File",

        mDH.getLanguageString(mDH.LABEL_TERM_DEFINITION_LOCATION_LS_PROPERTY), //"Terms Path",
        mDH.getLanguageString(mDH.LABEL_TERM_VALUE_LOCATION_LS_PROPERTY), //"Values Path",

        mDH.getLanguageString(mDH.LABEL_IMAGE_CATEGORY_NAME_LS_PROPERTY), //"Picture Category's Name",  // not file
        mDH.getLanguageString(mDH.LABEL_IMAGE_TERM_NAME_LS_PROPERTY), //"Picture Node's Name",
        mDH.getLanguageString(mDH.LABEL_MR_LANGUAGE_LS_PROPERTY), //"Language"
        mDH.getLanguageString(mDH.LABEL_WINDOW_MANAGEMENT_LS_PROPERTY), //"Window Management:",
        mDH.getLanguageString(mDH.LABEL_USE_PLAQUE_INDEX_LS_PROPERTY) //"Use Plaque"
    };

        private int mNuOfFile = 4;

        /** Creates new form ConfigHandler */
        public ConfigHandler(String[] pValues) {
            super(new JFrame(),true); // Modal // NE
            initComponents();
            mConfigList = pValues;
            init();

        }

        public ConfigHandler(String[] pValues,boolean outsider) {
            this(pValues);
            mStartOutSide = outsider;
        }

        private void initRowComponents(){
            GridBagConstraints  gbConstraints   = null;

            for(int i=0; i< mConfigList.length; i++){
                String aRow     = mConfigList[i];
                int indx        = aRow.indexOf('=');

                String aKeyShow = mListOfValuesNames[i];
                String aKey     = aRow.substring(0,indx);
                String aVal     = aRow.substring(indx+1);


                JLabel aLab     = new JLabel(aKeyShow);
                //aLab.setHorizontalAlignment(SwingConstants.CENTER); OT
                aLab.setHorizontalAlignment(SwingConstants.RIGHT);
                aLab.setVerticalAlignment(SwingConstants.CENTER);
                aLab.setFont(aLab.getFont().deriveFont(Font.BOLD));
                //aLab.setBorder(new javax.swing.border.EtchedBorder()); OT

                JComponent anInput = null;
                JComponent aButton;

                if(i <  mNuOfFile){
                    anInput       = new JTextField(aVal);
                    aButton    = new JButton(mDH.getLanguageString(mDH.OTHER_BROWSE_LS_PROPERTY));
                    anInput.setFont(anInput.getFont().deriveFont(12));

                    if (aKey.compareTo(Config.Translator) == 0 )
                        this.setBrowseFileButton((JButton)aButton,anInput,true);

                    else if (aKey.compareTo(Config.Preview_Template) == 0 )
                        this.setBrowseFileButton((JButton)aButton,anInput,true);

                    else if (aKey.compareTo(Config.TermDefLocation) == 0 )
                        this.setBrowseFileButton((JButton)aButton,anInput,false);

                    else if (aKey.compareTo(Config.ValueDefLocation) == 0 )
                        this.setBrowseFileButton((JButton)aButton,anInput,false);

                    else
                        this.setBrowseButton((JButton)aButton,anInput);
                }
                else{
                    aButton = new JLabel();

                    if( aKey.compareTo(Config.UsePlaque) == 0 ){  // plaque cat
                        anInput       = new JCheckBox(mDH.getLanguageString(mDH.CHECKBOX_USE_PLAQUE_INDEX_LS_PROPERTY),
                                                   (aVal.compareTo("true") == 0? true:false));
                    }
                    else if( aKey.equals(Config.IsDocumentModeOn)){  // ues document model
                        anInput       = new JCheckBox(mDH.getLanguageString(mDH.CHECKBOX_ENABLE_DOCUMENT_MODE_LS_PROPERTY),
                                                      (aVal.compareTo("true") == 0? true:false));
                    }
                    else if( aKey.equals(Config.Language)){  // language cat
                        JComboBox langCombo = new JComboBox(mDH.getAvailableLanguages());
                        langCombo.setSelectedItem(aVal);
                        anInput       = langCombo;
                    }
                    else{
                        anInput       = new JTextField(aVal);  // photocat/ photo nod
                    }
                }
                mInputComponent[i]   = anInput;
                setARow(i,aButton,aLab,anInput);
            }
        }

        private void setARow(int pIndx,JComponent aButton,JLabel aLab,JComponent anInput){

            GridBagConstraints gbConstraints = new GridBagConstraints();
            gbConstraints.gridx = 0;
            gbConstraints.gridy = GridBagConstraints.RELATIVE;
            gbConstraints.gridwidth = 1;
            gbConstraints.gridheight = 1;
            gbConstraints.fill = GridBagConstraints.HORIZONTAL;
            gbConstraints.weightx = .1;
           // gbConstraints.weighty = 1;

            gbLayout.setConstraints(aLab,gbConstraints);
            configPanel.add(aLab);

            gbConstraints = new GridBagConstraints();
            gbConstraints.gridx = 1;
            gbConstraints.gridy = GridBagConstraints.RELATIVE;
            gbConstraints.gridwidth = GridBagConstraints.RELATIVE;
            gbConstraints.gridheight = 1;
            gbConstraints.fill = GridBagConstraints.HORIZONTAL;
            gbConstraints.weightx = .8;
           // gbConstraints.weighty = 1;

            gbLayout.setConstraints(anInput,gbConstraints);
            configPanel.add(anInput);

            gbConstraints = new GridBagConstraints();
            gbConstraints.gridx = 2;
            gbConstraints.gridy = GridBagConstraints.RELATIVE;
            gbConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gbConstraints.gridheight = 1;
            gbConstraints.fill = GridBagConstraints.HORIZONTAL;
            gbConstraints.weightx = .1;
           // gbConstraints.weighty = 1;

            gbLayout.setConstraints(aButton,gbConstraints);
            configPanel.add(aButton);
        }

        private void init(){
            gbLayout            = new GridBagLayout();
            mInputComponent     = new JComponent[mConfigList.length];
            mBrowseButtons      = new JButton[mConfigList.length-4];
            configPanel.setLayout(gbLayout);

            initRowComponents();

            buttonPanel.add(new javax.swing.JLabel());
            buttonPanel.add(new javax.swing.JLabel());

            mCancelButton   = new JButton(mDH.getLanguageString(mDH.BUTTON_CLOSE_LS_PROPERTY));
            mSaveButton     = new JButton(mDH.getLanguageString(mDH.BUTTON_SAVE_TEXT_LS_PROPERTY));
            buttonPanel.add(mCancelButton);
            buttonPanel.add(mSaveButton);

            setButtonsClick();

            pack();
            this.repaint();

        }


        private void setBrowseFileButton(JButton aButton,final JComponent pTxt,final boolean isXml){

            aButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    browseToFile((JTextField)pTxt,isXml);
                }
            });
        }

        private void browseToFile(JTextField pTxt,boolean isXml){
            String aPath = pTxt.getText();
            JFileChooser chooser = new JFileChooser(aPath);

            chooser.setFileSelectionMode(chooser.FILES_AND_DIRECTORIES);
            chooser.setApproveButtonText(mDH.getLanguageString(mDH.BUTTON_OPEN_LS_PROPERTY));
            chooser.setName("V\u00E4lj ett bibliotek");

            String[] extensions;
            if(isXml){
                String[] tmp = { "xml", "Xml", "XML"};
                extensions= tmp;
            }
            else{
                String[] tmp = { "txt", "Txt", "TXT"};
                extensions = tmp;
            }

            misc.gui.ExtensionFileFilter aFilter = new misc.gui.ExtensionFileFilter(extensions, true);
            aFilter.setIncludeDirectories(true);

            aFilter.setDescription((isXml?
                                    mDH.getLanguageString(mDH.FILEFILTER_XML_FILES_LS_PROPERTY):
                                    mDH.getLanguageString(mDH.FILEFILTER_TXT_FILES_LS_PROPERTY)));
            chooser.setFileFilter(aFilter);
            //chooser.setFileFilter(new FileFilter(
            int result = chooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File chosenFile = chooser.getSelectedFile(); // Clicked OK
                try {
                    if (chosenFile.canRead()) {
                        // Get examination handler, use it to read examination model
                        pTxt.setText(chosenFile.getAbsolutePath());

                    } else {
                        JOptionPane.showMessageDialog(this,"Error: File " + chosenFile.getPath() + "is not readable!",
                                                      "Not readable",JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch (Exception e) {
                    JOptionPane.showMessageDialog(this,"Error: Could not read file " + chosenFile.getName() + "!",
                                                  "Read error",JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        }

        private void setBrowseButton(JButton aButton,final JComponent  pTxt){

            aButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    browseForm((JTextField)pTxt);
                }
            });
        }

        private void browseForm(JTextField pTxt){
            String aPath = pTxt.getText();
            JFileChooser chooser = new JFileChooser(aPath);

            chooser.setFileSelectionMode(chooser.DIRECTORIES_ONLY);
            chooser.setApproveButtonText(mDH.getLanguageString(mDH.BUTTON_OPEN_LS_PROPERTY));
            chooser.setName("V\u00E4lj ett bibliotek");
            int result = chooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File chosenFile = chooser.getSelectedFile(); // Clicked OK
                try {
                    if (chosenFile.canRead()) {
                        // Get examination handler, use it to read examination model
                        pTxt.setText(chosenFile.getAbsolutePath());

                    } else {
                        JOptionPane.showMessageDialog(this,"Error: File " + chosenFile.getPath() + "is not readable!",
                                                      "Not readable",JOptionPane.ERROR_MESSAGE);
                    }
                }
                catch (Exception e) {
                    JOptionPane.showMessageDialog(this,"Error: Could not read file " + chosenFile.getName() + "!","Read error",
                                                  JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
            // Ut.prt(pTxt.getText());
        }

        private void setButtonsClick(){
            mCancelButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    cancelClicked(evt);
                }
            });

            mSaveButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    saveClicked(evt);
                }
            });
        }

        private void cancelClicked(java.awt.event.ActionEvent evt){
            if(mStartOutSide)
                this.dispose();
            else
                System.exit(0);

        }

        private void saveClicked(java.awt.event.ActionEvent evt){
            for(int i=0; i< mConfigList.length; i++){
                String aRow     = mConfigList[i];
                int indx        = aRow.indexOf('=');
                String aKey     = aRow.substring(0,indx);
                String aVal     = aRow.substring(indx+1);
                JComponent aCom =  mInputComponent[i];
                if(aCom instanceof JTextField){
                    mConfigList[i]  = aKey + "=" +((JTextField)aCom).getText();
                }
                if(aCom instanceof JCheckBox){
                    mConfigList[i]  = aKey + "=" +((JCheckBox)aCom).isSelected();
                }
                if(aCom instanceof JComboBox){
                    mConfigList[i]  = aKey + "=" +((JComboBox)aCom).getSelectedItem();
                }
            }
            Config.save(mConfigList);
            //this.dispose();
        }

    private void initComponents() {//GEN-BEGIN:initComponents
        mainPanel = new javax.swing.JPanel();
        configPanel = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(mDH.getLanguageString(mDH.TITLE_PREFERENCES_LS_PROPERTY));
        setFont(new java.awt.Font("Times New Roman", 0, 10));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        mainPanel.setLayout(new java.awt.BorderLayout());

        mainPanel.setPreferredSize(new java.awt.Dimension(750, 450));
        configPanel.setLayout(null);

        configPanel.setPreferredSize(new java.awt.Dimension(700, 400));
        mainPanel.add(configPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setLayout(new java.awt.GridLayout(2, 2, 2, 0));

        buttonPanel.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(5, 1, 5, 1)));
        buttonPanel.setToolTipText("");
        mainPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        if(mStartOutSide)
                this.dispose();
            else
                System.exit(0);
    }//GEN-LAST:event_exitForm

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            String  jVrn = System.getProperty("os.name");
            //Ut.prt( " name = " + jVrn);
            if (jVrn.startsWith("Windows")){
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            else{

                UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        //Config.setConfigFile( args[0]);

        Config.readConfigInfo();
        String[] tmpArray = Config.getValues();
       new ConfigHandler(tmpArray).setVisible(true);
      // Config.showValues();

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel configPanel;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables
    private String[] mConfigList;
    private JButton  mCancelButton;
    private JButton  mSaveButton;
    private JComponent[]  mInputComponent;
    private JButton[] mBrowseButtons;
    private GridBagLayout   gbLayout;
    private boolean mStartOutSide = false;

}
