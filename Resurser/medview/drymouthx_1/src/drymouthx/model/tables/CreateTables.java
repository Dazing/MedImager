/* */

package drymouthx.model.tables;
// Imports

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

/**
 *
 * @author Marie Lindgren
 */
public class CreateTables {
    public CreateTables(){}
    
    
    
    //funktion som returnerar DrugTable
    public JTable drawDrugTable() {

        String columnNames[] = { " ", "Start Date", "Drug Details", " ", "Ordination", "Status", "Reason", "Review Date" };

        String myicohigh = "src\\drymouthx\\resources\\OrangeRound.gif";
        String myiconorm = "src\\drymouthx\\resources\\GreenRound.gif";
        String myicolow = "src\\drymouthx\\resources\\WhiteRound.gif";
        String[] mycboxchoices = {"Started", "Suspended", "Deleted"};
        String myjbuttonicostr = "src\\drymouthx\\resources\\small_explorer.gif";
        Icon myjbuttonico = new ImageIcon(myjbuttonicostr);
        JButton myjbutt1 = new JButton("Fass", myjbuttonico);
        JButton myjbutt2 = new JButton("Fass", myjbuttonico);
        JButton myjbutt3 = new JButton("Fass", myjbuttonico);
        JButton myjbutt4 = new JButton("Fass", myjbuttonico);

        myjbutt1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        myjbutt1.setMargin(new java.awt.Insets(2, 4, 2, 4));
        myjbutt1.setPreferredSize(new java.awt.Dimension(51, 21));
        myjbutt2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        myjbutt2.setMargin(new java.awt.Insets(2, 4, 2, 4));
        myjbutt2.setPreferredSize(new java.awt.Dimension(51, 21));
        myjbutt3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        myjbutt3.setMargin(new java.awt.Insets(2, 4, 2, 4));
        myjbutt3.setPreferredSize(new java.awt.Dimension(51, 21));
        myjbutt4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        myjbutt4.setMargin(new java.awt.Insets(2, 4, 2, 4));
        myjbutt4.setPreferredSize(new java.awt.Dimension(51, 21));


        Object[][] data = {
            {new ImageIcon(myicohigh), new Date(), "Lithium 100% as required", myjbutt1, "x2-120mg-7days", "Treatment of febrile convulsions",new JComboBox(mycboxchoices), new Date()},
            {new ImageIcon(myiconorm), new Date(), "ABcd as required",         myjbutt2, "x2-120mg-7days", "Treatment of febrile convulsions",new JComboBox(mycboxchoices), new Date()},
            {new ImageIcon(myiconorm), new Date(), "ABcd as required",         myjbutt3, "x2-120mg-7days", "Treatment of febrile convulsions",new JComboBox(mycboxchoices), new Date()},
            {new ImageIcon(myicohigh), new Date(), "Lithium 100% as required", myjbutt4, "x2-120mg-7days", "Treatment of febrile convulsions",new JComboBox(mycboxchoices), new Date()}
        };


        JTable table = new JTable( data, columnNames )
        {
            public TableCellRenderer getCellRenderer(int row, int column) {
                TableColumn tableColumn = getColumnModel().getColumn(column);
                TableCellRenderer renderer = tableColumn.getCellRenderer();
                if (renderer == null) {
                    Class c = getColumnClass(column);
                    if( c.equals(Object.class) )
                    {
                        Object o = getValueAt(row,column);
                        if( o != null )
                            c = getValueAt(row,column).getClass();
                    }
                    renderer = getDefaultRenderer(c);
                }
                return renderer;
            }

            public TableCellEditor getCellEditor(int row, int column) {
                TableColumn tableColumn = getColumnModel().getColumn(column);
                TableCellEditor editor = tableColumn.getCellEditor();
                if (editor == null) {
                    Class c = getColumnClass(column);
                    if( c.equals(Object.class) )
                    {
                        Object o = getValueAt(row,column);
                        if( o != null )
                        c = getValueAt(row,column).getClass();
                    }
                    editor = getDefaultEditor(c);
                }
                return editor;
            }
        };

        TableColumn column = table.getColumnModel().getColumn(0);
        column.setMaxWidth(24);
        column.setMinWidth(24);
        column = table.getColumnModel().getColumn(1);
        column.setMaxWidth(80);
        column.setMinWidth(80);
        column = table.getColumnModel().getColumn(2);
        column.setMaxWidth(200);
        column.setMinWidth(200);
        column = table.getColumnModel().getColumn(3);
        column.setMaxWidth(55);
        column.setMinWidth(55);

        table.setRowHeight(22);

        table.setDefaultRenderer( JComponent.class, new JComponentCellRenderer() );
        table.setDefaultEditor( JComponent.class, new JComponentCellEditor() );

        table.setShowGrid(false);
        return table;
    }

    public JTable drawLabValueTable() {
        String columnNames[] = { " ", "Start Date", "Lab test", "Ordination", "Value", "Status", "Reason" };

        String myicohigh = "src\\drymouthx\\resources\\OrangeRound.gif";
        String myiconorm = "src\\drymouthx\\resources\\GreenRound.gif";
        String myicolow = "src\\drymouthx\\resources\\WhiteRound.gif";
        String[] mycboxchoices = {"Saliva-Sialometri-Stimulated", "Saliva-Sialometri-Unstimulated", "Tears-Schrimerr", "Blood-Anti-SS-A"};

        Object[][] data = {
            {new ImageIcon(myicohigh), new Date(), new JComboBox(mycboxchoices), "x2-120mg-7days", "3mg / 6mg","High", "Treatment of febrile convulsion"},
            {new ImageIcon(myiconorm), new Date(), new JComboBox(mycboxchoices), "x2-120mg-7days", "6mg / 6mg", "Normal", "Treatment of febrile convulsion"},
            {new ImageIcon(myicolow), new Date(), new JComboBox(mycboxchoices), "x2-120mg-7days", "1.5mg", "Low", "Treatment of febrile convulsion"},
            {new ImageIcon(myiconorm), new Date(), new JComboBox(mycboxchoices), "x2-120mg-7days", "3ml /3ml", "Normal", "Treatment of febrile convulsion"}
        };

        
        JTable table = new JTable( data, columnNames )
        {
            public TableCellRenderer getCellRenderer(int row, int column) {
                TableColumn tableColumn = getColumnModel().getColumn(column);
                TableCellRenderer renderer = tableColumn.getCellRenderer();
                if (renderer == null) {
                    Class c = getColumnClass(column);
                    if( c.equals(Object.class) )
                    {
                        Object o = getValueAt(row,column);
                        if( o != null )
                            c = getValueAt(row,column).getClass();
                    }
                    renderer = getDefaultRenderer(c);
                }
                return renderer;
            }

            public TableCellEditor getCellEditor(int row, int column) {
                TableColumn tableColumn = getColumnModel().getColumn(column);
                TableCellEditor editor = tableColumn.getCellEditor();
                if (editor == null) {
                    Class c = getColumnClass(column);
                    if( c.equals(Object.class) )
                    {
                        Object o = getValueAt(row,column);
                        if( o != null )
                        c = getValueAt(row,column).getClass();
                    }
                    editor = getDefaultEditor(c);
                }
                return editor;
            }
        };

        TableColumn column = table.getColumnModel().getColumn(0);
        column.setMaxWidth(24);
        column.setMinWidth(24);
        column = table.getColumnModel().getColumn(1);
        column.setMaxWidth(80);
        column.setMinWidth(80);
        column = table.getColumnModel().getColumn(2);
        column.setMaxWidth(200);
        column.setMinWidth(200);

        table.setRowHeight(22);

        table.setShowGrid(false);

        table.setDefaultRenderer( JComponent.class, new JComponentCellRenderer() );
        table.setDefaultEditor( JComponent.class, new JComponentCellEditor() );

        return table;
    }


   


   class JComponentCellRenderer implements TableCellRenderer
{
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
    return (JComponent)value;
    }
}


}//Slut Klass createTables