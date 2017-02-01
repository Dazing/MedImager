/**
 * $Id: RunTableD.java,v 1.5 2010/06/28 07:51:28 oloft Exp $
 *
 * $Log: RunTableD.java,v $
 * Revision 1.5  2010/06/28 07:51:28  oloft
 * Changes for looks-2.1.3
 *
 * Revision 1.4  2005/04/26 12:57:12  erichson
 * Merging COPENHAGEN_DEVELOPMENT branch.
 *
 * Revision 1.3.2.6  2005/04/26 10:39:40  erichson
 * Code cleanup while finishing up
 *
 * Revision 1.3.2.5  2005/04/24 10:30:27  erichson
 * Can now show the built tree again (for testing purposes)
 *
 * Revision 1.3.2.4  2005/03/29 08:09:53  erichson
 * Use Plastic (same LAF as medrecords)
 *
 * Revision 1.3.2.3  2005/03/25 13:36:50  erichson
 * added super call to constructor.
 *
 * Revision 1.3.2.2  2005/03/04 08:28:17  erichson
 * exit_on_close, title for the frame
 *
 * Revision 1.3.2.1  2005/02/24 12:12:11  erichson
 * comment
 *
 * Revision 1.3  2003/11/11 14:49:52  oloft
 * Switching main-branch
 *
 * Revision 1.2.2.1  2003/08/16 14:11:56  erichson
 * Disabled "test tree" since it is dependent on the (right now) broken ExaminationIO
 *
 */

package medview.medrecords.plaqueIndex.plaqueIndexD;

import java.awt.Dimension;
import javax.swing.*;



public class RunTableD extends javax.swing.JFrame {
    
    // Self reference for inner classes that may confuse 'this' // NE
    private javax.swing.JFrame thisFrame = this;
    
    private StartPlaque mSP;
    
    public RunTableD() {
        super();
        initComponents();
        pack();
    }
    
    private void exitForm(java.awt.event.WindowEvent evt) {
        System.exit(0);
    }
    
    private void initComponents() {
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        }
        );
        // getContentPane().add(new PlaquePanelD("OVERKÄKEN",true), java.awt.BorderLayout.CENTER);
        mSP   = new StartPlaque();
        getContentPane().add(mSP, java.awt.BorderLayout.CENTER);
                        
        JButton aButton = new JButton("TEST TREE");
        aButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                buildAndShowTree();                
            }
        }
        );        
        getContentPane().add(aButton, java.awt.BorderLayout.SOUTH);
        
    }
    
    private void buildAndShowTree()
    {
        medview.datahandling.examination.tree.MRTree aTree = mSP.buildTree();
        String treeString = aTree.toString();
        
        JTextPane pane = new JTextPane();
        pane.setText(treeString);
        JScrollPane jSP = new JScrollPane(pane);
        jSP.setPreferredSize(new Dimension(800,600));
        jSP.getViewport().scrollRectToVisible(new java.awt.Rectangle(10,10));
        JOptionPane.showMessageDialog(this,jSP);
        
    }
            
    public static void main(String[] args) {
        
        try {
            // look-and-feel setup

            // PlasticLookAndFeel.setMyCurrentTheme(new DesertBlue()); not working well since Java 1.5

            UIManager.installLookAndFeel("JGoodies Plastic 3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");

            // look-and-feel instantiation

            try
            {
                UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.Plastic3DLookAndFeel");                
            }
            catch (Exception e)
            {
                String a = "Warning: could not set plastic look and feel";

                String crossLAFClass = UIManager.getCrossPlatformLookAndFeelClassName();

                UIManager.setLookAndFeel(crossLAFClass);
            }                                    
            
            RunTableD tD = new RunTableD();
            tD.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            tD.setTitle("RunTableD");
            tD.show();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }    
}