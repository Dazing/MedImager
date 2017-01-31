package medview.medrecords.plaqueIndex.plaqueIndexS;

import javax.swing.UIManager;
import java.awt.Dimension ;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import medview.medrecords.models.*;


public class RunTable extends javax.swing.JFrame {
    
    public RunTable() {
        initComponents();
        pack();
    }
    
    private void exitForm(java.awt.event.WindowEvent evt) {
        System.exit(0);
    }
        
    private void initComponents() {
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        }
        );        
        getContentPane().add(new PlaquePanel(), java.awt.BorderLayout.CENTER);
        
    }
     
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new RunTable().show();
        }
        catch(Exception e) {
            e.printStackTrace(); 
        }
    }
    
    
    
    
    
}