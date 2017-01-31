/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package drymouthx.model.treetable;

import javax.swing.table.*;
//För XML och filhanteringen
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
/**
 *
 * @author Marie Lindgren
 */
public class CreateTreeTable {

    public CreateTreeTable(){}

/*    public JTreeTable drawTreeTable(){

     //Skapar en TreeTable för Symptoms and Signs och visar i interfacet
       try {
                File file = new File("C:\\Documents and Settings\\Marie Lindgren\\Mina dokument\\NetBeansProjects\\DryMouythX\\src\\drymouthx\\Model\\TreeTable\\MyXMLFile.xml");
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(file);
                doc.getDocumentElement().normalize();

                Element rotNod = doc.getDocumentElement();

                //Starta JTreeTable med ratt TreeModel och med startnoden far MyDataTree
                JTreeTable treeTable1 = new JTreeTable(new MyTreeModel(rotNod));
                TableColumn column = treeTable1.getColumnModel().getColumn(0);
                column.setMaxWidth(250);
                column.setMinWidth(250);
                column = treeTable1.getColumnModel().getColumn(1);
                column.setCellRenderer(new MultiRenderer());
                column.setCellEditor(new MultiEditor());
                column.setMaxWidth(100);
                column.setMinWidth(100);
                //treeTable1.setFillsViewportHeight(true); //Fyller ut treeTable i JDK 1.6
       //         jScrollPane1.getViewport().setBackground(Color.white);//Fyller ut treeTable i JDK 1.5
       //         jScrollPane1.setViewportView(treeTable1); //Visar TreeTabel i gränssnittet
        } catch (Exception e) {
                e.printStackTrace();
        }
        return treeTable1;   
    }*/

}
