/*
 * PIDWizard.java
 *
 * Created on den 30 januari 2005, 11:22
 *
 * $Id: PIDWizard.java,v 1.2 2010/06/28 07:56:42 oloft Exp $
 *
 * $Log: PIDWizard.java,v $
 * Revision 1.2  2010/06/28 07:56:42  oloft
 * Changes for looks-2.1.3
 *
 * Revision 1.1  2005/01/31 09:50:30  erichson
 * First check-in.
 *
 */

package medview.tools.pidwizard;

import java.awt.*;
import javax.swing.*;


/**
 *
 * @author erichson
 */
public class PIDWizard extends JFrame 
{
    
    private static Class plasticClass;
    public static final String VERSION = "1.0";
    
    /** Creates a new instance of PIDWizard */
    public PIDWizard() 
    {                                
        super("PIDWizard version " + VERSION);
        
        setLookAndFeel(plasticClass.getName());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container c = getContentPane();
        //JPanel panel = new JPanel();
        //panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        //panel.add(new PIDWizardComponent());
        //c.add(panel);
        PIDWizardComponent wiz = new PIDWizardComponent();
        c.add(wiz);
        pack();
    }
    
    /**
     * Sets the Look And Feel of the application.
     *
     * @param LAFClassName The Look And Feel class name.
     */
    public void setLookAndFeel(String LAFClassName) {
        try {
            UIManager.setLookAndFeel(LAFClassName);// Add your handling code here:
            
        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(this,"Class not found: " + LAFClassName,"ClassNotFoundException", JOptionPane.ERROR_MESSAGE);
        } catch (InstantiationException ie) {
            JOptionPane.showMessageDialog(this,"Could not instantiate class " + LAFClassName +". More info: " + ie.getMessage(),"InstantiationException", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalAccessException iae) {
            JOptionPane.showMessageDialog(this,"IllegalAccessException: " + iae.getMessage(),"IllegalAccessException", JOptionPane.ERROR_MESSAGE);
        } catch (UnsupportedLookAndFeelException ulafe) {
            JOptionPane.showMessageDialog(this,"Look and feel " + LAFClassName + " not supported.","Unsuppoerted Look and feel exception", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args)
    {
        plasticClass = com.jgoodies.looks.plastic.PlasticLookAndFeel.class;
        UIManager.installLookAndFeel("Plastic", plasticClass.getName());        
        
        new PIDWizard().show();
    }
}
