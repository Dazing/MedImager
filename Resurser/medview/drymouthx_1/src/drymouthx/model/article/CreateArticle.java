/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package drymouthx.model.article;
import java.awt.Font;
import javax.swing.*;
import org.jdesktop.application.*;
/**
 *
 * @author Marie Lindgren
 */
public class CreateArticle {

    public CreateArticle(){}

    //Skall placera ut artiklar och dess grafiska utseende
    public JPanel drawArticle(){
        JPanel jp = new javax.swing.JPanel();
        jp.setAutoscrolls(true);
        jp.setMaximumSize(new java.awt.Dimension(510, 225));
        jp.setMinimumSize(new java.awt.Dimension(500, 125));
        jp.setName("jp"); //
        jp.setPreferredSize(new java.awt.Dimension(510, 185));

        JScrollPane sPane = new javax.swing.JScrollPane();
        sPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sPane.setMaximumSize(new java.awt.Dimension(500, 50));
        sPane.setMinimumSize(new java.awt.Dimension(500, 15));
        sPane.setName("sPane"); //

        JScrollPane sPane2 = new javax.swing.JScrollPane();
        sPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        sPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sPane2.setMaximumSize(new java.awt.Dimension(500, 50));
        sPane2.setMinimumSize(new java.awt.Dimension(500, 15));
        sPane2.setName("sPane2"); //

        JTextArea textA = new javax.swing.JTextArea();
        textA.setColumns(20);
        textA.setFont(Font.decode("Arial-12"));
        textA.setRows(5);
        textA.setTabSize(4);
        textA.setText("Här finns ett liknande patientfall där patienten hade ..... \n .... och även lichenoida förändringar vilka visar sig i ....");
        textA.setMaximumSize(new java.awt.Dimension(610, 50));
        textA.setMinimumSize(new java.awt.Dimension(500, 15));
        textA.setName("textA"); 
        textA.setPreferredSize(new java.awt.Dimension(610, 50));
        sPane.setViewportView(textA);

        JTextArea textB = new javax.swing.JTextArea();
        textB.setColumns(20);
        textB.setFont(Font.decode("Arial-12"));
        textB.setRows(5);
        textB.setTabSize(4);
        textB.setText("Här finns ytterligare ett liknande patientfall där patienten hade ..... \n .... och utan lichenoida förändringar");
        textB.setMaximumSize(new java.awt.Dimension(600, 50));
        textB.setMinimumSize(new java.awt.Dimension(500, 15));
        textB.setName("textB");
        textB.setPreferredSize(new java.awt.Dimension(600, 50));
        sPane2.setViewportView(textB);

        JLabel jLab1 = new javax.swing.JLabel();
        jLab1.setText("Source: ");
        jLab1.setName("jLab1");
        JLabel jLab2 = new javax.swing.JLabel();
        jLab2.setText("Reliable: ");
        jLab2.setName("jLab2");
        JLabel jLab3 = new javax.swing.JLabel();
        jLab3.setText("A, B");
        jLab3.setName("jLab3");

        JLabel jLab4 = new javax.swing.JLabel();
        jLab4.setText("Source: ");
        jLab4.setName("jLab4");
        JLabel jLab5 = new javax.swing.JLabel();
        jLab5.setText("Reliable: ");
        jLab5.setName("jLab5");
        JLabel jLab6 = new javax.swing.JLabel();
        jLab6.setText("A, B, C");
        jLab6.setName("jLab6");

        JButton jBut1 = new javax.swing.JButton();
        Icon icon = new ImageIcon("src\\drymouthx\\resources\\small_explorer.gif");
        jBut1.setIcon(icon);
        jBut1.setText("exp");
        jBut1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBut1.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jBut1.setMaximumSize(new java.awt.Dimension(51, 21));
        jBut1.setMinimumSize(new java.awt.Dimension(51, 21));
        jBut1.setName("jBut1"); 
        jBut1.setPreferredSize(new java.awt.Dimension(51, 21));

        JButton jBut2 = new javax.swing.JButton();
        icon = new ImageIcon("src\\drymouthx\\resources\\small_pdf.gif");
        jBut2.setIcon(icon);
        jBut2.setText("PDF");
        jBut2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBut2.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jBut2.setMaximumSize(new java.awt.Dimension(51, 21));
        jBut2.setMinimumSize(new java.awt.Dimension(51, 21));
        jBut2.setName("jBut2");
        jBut2.setPreferredSize(new java.awt.Dimension(51, 21));

        JButton jBut3 = new javax.swing.JButton();
        icon = new ImageIcon("src\\drymouthx\\resources\\small_ppt.gif");
        jBut3.setIcon(icon);
        jBut3.setText("ppt");
        jBut3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBut3.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jBut3.setMaximumSize(new java.awt.Dimension(51, 21));
        jBut3.setMinimumSize(new java.awt.Dimension(51, 21));
        jBut3.setName("jBut3");
        jBut3.setPreferredSize(new java.awt.Dimension(51, 21));

        JButton jBut4 = new javax.swing.JButton();
        icon = new ImageIcon("src\\drymouthx\\resources\\small_doc.gif");
        jBut4.setIcon(icon);
        jBut4.setText("doc");
        jBut4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jBut4.setMargin(new java.awt.Insets(2, 4, 2, 4));
        jBut4.setMaximumSize(new java.awt.Dimension(51, 21));
        jBut4.setMinimumSize(new java.awt.Dimension(51, 21));
        jBut4.setName("jBut4");
        jBut4.setPreferredSize(new java.awt.Dimension(51, 21));

        org.jdesktop.layout.GroupLayout jpLayout = new org.jdesktop.layout.GroupLayout(jp);
        jp.setLayout(jpLayout);
        jpLayout.setHorizontalGroup(
            jpLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpLayout.createSequentialGroup()
                .addContainerGap()
                .add(jpLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jpLayout.createSequentialGroup()
                        .add(jLab1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jBut1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBut2)
                        .add(12, 12, 12)
                        .add(jLab2)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLab3))
                    .add(jpLayout.createSequentialGroup()
                        .add(jLab4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jBut3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jBut4)
                        .add(12, 12, 12)
                        .add(jLab5)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jLab6)))
                .addContainerGap(764, Short.MAX_VALUE))
            .add(sPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1034, Short.MAX_VALUE)
            .add(sPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1034, Short.MAX_VALUE)
        );
        jpLayout.setVerticalGroup(
            jpLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpLayout.createSequentialGroup()
                .add(sPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jpLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLab1)
                    .add(jBut1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jBut2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLab2)
                    .add(jLab3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(sPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jpLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLab4)
                    .add(jBut4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jBut3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLab5)
                    .add(jLab6))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        return jp;
    }
    

}
