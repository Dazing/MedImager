/*
 * PIDWizardComponent.java
 *
 * Created on den 30 januari 2005, 11:24
 *
 * $Id: PIDWizardComponent.java,v 1.1 2005/01/31 09:50:31 erichson Exp $
 *
 * $Log: PIDWizardComponent.java,v $
 * Revision 1.1  2005/01/31 09:50:31  erichson
 * First check-in.
 *
 */

package medview.tools.pidwizard;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

// import ise.java.awt.*;

/**
 *
 * @author erichson
 */
public class PIDWizardComponent extends JPanel 
{
    
    /* Gap around and between components */
    private int GAP = 5;
    
    private JLabel pidLabel, examinationsLabel;
    private JTextField pidField;
    private JList examinationsList;
    private JTextArea examinationContentArea;
    
    /** Creates a new instance of PIDWizardComponent */
    public PIDWizardComponent() 
    {
        super();
        initComponents();
    }
    
    private void initComponents()
    {
        //setLayout(new KappaLayout());
        
        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setHgap(GAP);
        borderLayout.setVgap(GAP);
        
        setLayout(borderLayout);
        
	pidLabel = new JLabel("Personnummer:");
        pidField = new JTextField(13);
        Box pidBox = new Box(BoxLayout.X_AXIS);
        pidBox.add(pidLabel);
        pidBox.add(pidField);
        
        examinationsLabel = new JLabel("Examinations:");
        examinationsList = new JList();
        JScrollPane listSP = new JScrollPane(examinationsList);
        
        JPanel listPanel = new JPanel();
        borderLayout = new BorderLayout();
        borderLayout.setHgap(GAP);
        borderLayout.setVgap(GAP);
        listPanel.setLayout(borderLayout);
        listPanel.add(examinationsLabel, BorderLayout.NORTH);
        listPanel.add(listSP, BorderLayout.CENTER);
        
        examinationContentArea = new JTextArea();
        
        JScrollPane contentSP = new JScrollPane(examinationContentArea);
        
        JPanel leftPanel = new JPanel();
        borderLayout = new BorderLayout();
        borderLayout.setHgap(GAP);
        borderLayout.setVgap(GAP);
        leftPanel.setLayout(borderLayout);
        leftPanel.add(listPanel, BorderLayout.CENTER);
        leftPanel.add(pidBox, BorderLayout.NORTH);
                
        add(contentSP, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);
        
        setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        
        //add("0,0,1,1,,,0", pidBox);
        //add("0,1,1,1,,,0", listPanel);
        //add("1,0,1,2,,,0", contentSP);
        
    }
    
}
