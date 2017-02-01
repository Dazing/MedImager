/*
 * $Id: SearchDialog.java,v 1.2 2004/10/19 12:47:25 erichson Exp $
 *
 * Created on den 11 oktober 2004, 12:51
 *
 * $Log: SearchDialog.java,v $
 * Revision 1.2  2004/10/19 12:47:25  erichson
 * 1. Färdig funktionalitet
 * 2. Bättre gui
 * 3. Lade till "single term" som möjlig sökmängd
 *
 * Revision 1.1  2004/10/19 06:35:59  erichson
 * First check-in. (From home, not finished)
 *
 */

package medview.visualizer.gui.dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import medview.common.components.aggregation.*;
import medview.datahandling.aggregation.*;

import medview.visualizer.data.DataManager; 


/**
 * Dialog for performing an examination search
 *
 * @author Nils Erichson
 */
public class SearchDialog extends JDialog
{
    
    private JButton okButton, cancelButton;
    
    private JRadioButton allTermsRadioButton, chosenTermsRadioButton, singleTermRadioButton;
    private JCheckBox aggregationCheckBox;
    private AggregationChooser aggregationChooser;
    private AggregationContainer aggregationContainer;
    private Frame parentFrame;
    private JComboBox singleTermComboBox;
    
    private int returnResult; // JoptionPane.OK_OPTION or CANCEL_OPTION. 
    
    private JTextField searchField;
    
    /** Creates a new instance of SearchDialog */
    public SearchDialog(Frame parentFrame, AggregationContainer aggregationContainer) {
        super(parentFrame, "Search", true); // modal        
        this.parentFrame = parentFrame;
        this.aggregationContainer = aggregationContainer;
        initComponents();
    }
    
    private void initComponents()
    {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        // Search label and text field
        
        JLabel searchLabel = new JLabel("Search:");
        searchField = new JTextField(30);
        searchField.addKeyListener(new KeyListener()
        {
            public void keyTyped(KeyEvent ke) {}            
              
            public void keyReleased(KeyEvent ke) {}
            
            public void keyPressed(KeyEvent ke)
            {
                if (ke.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    returnResult = JOptionPane.OK_OPTION;
                    dispose();
                }
            }
        });
        
        
        Box searchRow = new Box(BoxLayout.X_AXIS);
        searchRow.add(searchLabel);
        searchRow.add(searchField);
        
        // Choose terms                 
        
        singleTermComboBox = new JComboBox(DataManager.getInstance().getAllTerms());
        JPanel singleTermComboBoxPanel = new JPanel();
        singleTermComboBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        singleTermComboBoxPanel.add(singleTermComboBox);
        
        allTermsRadioButton = new JRadioButton("All terms");
        chosenTermsRadioButton = new JRadioButton("Chosen terms only");
        singleTermRadioButton = new JRadioButton("Single term:");
        
        JPanel singleTermRowPanel = new JPanel();
        singleTermRowPanel.setLayout(new BoxLayout(singleTermRowPanel,BoxLayout.X_AXIS));
        singleTermRowPanel.add(singleTermRadioButton);
        singleTermRowPanel.add(singleTermComboBoxPanel);
        
        JPanel termChoiceRadioButtonPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        //termChoiceRadioButtonPanel.setLayout(new BoxLayout(termChoiceRadioButtonPanel,BoxLayout.Y_AXIS));
        termChoiceRadioButtonPanel.setLayout(gbl);
        termChoiceRadioButtonPanel.add(allTermsRadioButton);
        termChoiceRadioButtonPanel.add(chosenTermsRadioButton);        
        termChoiceRadioButtonPanel.add(singleTermRowPanel);
        termChoiceRadioButtonPanel.setBorder(BorderFactory.createTitledBorder("Terms to search"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(allTermsRadioButton,gbc);
        gbl.setConstraints(chosenTermsRadioButton,gbc);
        gbl.setConstraints(singleTermRadioButton,gbc);
        
        //termChoiceRadioButtonPanel.setLayout(new GridLayout(3,1,0,0));
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(allTermsRadioButton);
        bg.add(chosenTermsRadioButton);
        bg.add(singleTermRadioButton);
        allTermsRadioButton.setSelected(true);
        
        // Aggregation?
        Box aggregationBox = new Box(BoxLayout.X_AXIS);
        //JLabel aggregationLabel = new JLabel("Use aggregation: ")
        aggregationChooser = new AggregationChooser(parentFrame,
                                                   aggregationContainer);
        aggregationCheckBox = new JCheckBox("Use aggregation");
        aggregationCheckBox.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent ae) {
                aggregationChooser.setEnabled(aggregationCheckBox.isSelected());
            }
        });
        aggregationChooser.setEnabled(aggregationCheckBox.isSelected());
        
        
        
         
        aggregationBox.add(aggregationCheckBox);
        aggregationBox.add(aggregationChooser);
        
        // mainPanel                
        
        
        JPanel mainPanel = new JPanel();
        
        //BoxLayout bl = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        //mainPanel.setLayout(bl);        
        //mainPanel.add(searchRow);        
        //mainPanel.add(termChoiceBox);
        //mainPanel.add(aggregationBox);
        
        BorderLayout bl = new BorderLayout();
        mainPanel.setLayout(bl);
        mainPanel.add(searchRow, BorderLayout.NORTH);        
        mainPanel.add(termChoiceRadioButtonPanel, BorderLayout.CENTER);
        mainPanel.add(aggregationBox, BorderLayout.SOUTH);
        
        
        // OK and cancel buttons
        
        JPanel buttonPanel = new JPanel();                
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");        
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                returnResult = JOptionPane.OK_OPTION;
                dispose();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                returnResult = JOptionPane.CANCEL_OPTION;
                dispose();
            }
        });
        
        
        
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        JPanel allPanel = new JPanel();
        allPanel.setLayout(new BorderLayout());
        allPanel.add(mainPanel,BorderLayout.CENTER);
        allPanel.add(buttonPanel,BorderLayout.SOUTH);
        
        Container contentPane = getContentPane();
        contentPane.add(allPanel);
        pack();
    }
    
    /**
     * Shows and waits for OK or cancel click.
     * @return // JoptionPane.OK_OPTION or CANCEL_OPTION
     */ 
    public int showDialog()
    {
        show();        
        // super.show does not return until hide or dispose has been called, since it is modal
        return returnResult;
    }
    
    public void show() {
        setLocationRelativeTo(getParent());
        returnResult = JOptionPane.CANCEL_OPTION; // cancel is default, in case the dialog is closed
        super.show();        
    }
    
    
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.show();
        SearchDialog dialog = new SearchDialog(frame, new AggregationContainer() {
            public Aggregation[] getAggregations() {
                return new Aggregation[0];
            }
            
            public void setAggregations(Aggregation[] aggs) { }
        });
        
        dialog.show();
    }
    
    public String getSearchText() 
    {
        return searchField.getText();
    }
    
    public Aggregation getAggregation() 
    {
        if (aggregationCheckBox.isSelected())
        {
            return aggregationChooser.getSelectedAggregation();
        }
        else
            return null;
    }
    
    public String[] getTerms()
    {
        if (allTermsRadioButton.isSelected())
        {
            return DataManager.getInstance().getAllTerms();
        }
        else if (chosenTermsRadioButton.isSelected())
        {
            return DataManager.getInstance().getChosenTerms();
        } 
        else { // single term 
            String[] termArray = new String[1];
            termArray[0] = (String) singleTermComboBox.getSelectedItem();
            return termArray;
        }
    }
    
}
