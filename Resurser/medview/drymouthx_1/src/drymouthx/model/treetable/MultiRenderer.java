package drymouthx.model.treetable;



import java.awt.*;
//import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
//import javax.swing.event.*;
//import java.util.EventObject;


public class MultiRenderer extends DefaultTableCellRenderer {
    JComboBox comboBox;
    JCheckBox checkBox;
    ProgressBarRenderer progressBarRenderer;
    RadioButtonRenderer radioButtonRenderer;

  public MultiRenderer () {
      String[] combochoices = {"Yes","No"};
      comboBox = new JComboBox(combochoices);
      checkBox = new JCheckBox();
      progressBarRenderer = new ProgressBarRenderer();
      String[] choices = {"Yes","No"};
      radioButtonRenderer = new RadioButtonRenderer(choices);
  }

  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    if (value instanceof Boolean) {
      checkBox.setSelected(((Boolean)value).booleanValue());
      checkBox.setHorizontalAlignment(JLabel.CENTER);
      return checkBox;
    }
    if (value instanceof ProgressBar) {
        return progressBarRenderer.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
    }
    if (value instanceof RadioChoice) {
      return radioButtonRenderer.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
    }
    if (value instanceof ComboString) {
      comboBox.setSelectedItem(((ComboString)value).toString());
      return comboBox;
    }
    String str = (value == null) ? "" : value.toString();
    return super.getTableCellRendererComponent(table,str,isSelected,hasFocus,row,column);
  }
}


//This is all about Progressbar Renderer (No Editor)--------------------
class ProgressBarRenderer extends JProgressBar implements TableCellRenderer {
    public ProgressBarRenderer() {
        super(JProgressBar.HORIZONTAL);
        
    }

    //Renderar Progressbarerna i treetable
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof ProgressBar) {
          setStringPainted(true) ;
          this.setFont(Font.decode("Arial-10"));
          setBackground(Color.DARK_GRAY);
          setBorderPainted(true);

          if (((ProgressBar)value).i <34) {
               setForeground(Color.lightGray);
              //setForeground(Color.white);
          } else if (((ProgressBar)value).i >66) {
              setForeground(Color.orange);
          } else {
              setForeground(Color.green);
          }
          setValue(((ProgressBar)value).i);
        }
        return this;
    }
  }

//  -----------------
//This is all about RadioButton Renderer and Editor--------------------
class RadioButtonRenderer extends RadioButtonPanel implements TableCellRenderer {
    RadioButtonRenderer(String[] strs) {
      super(strs);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      if (value instanceof RadioChoice) {
        setSelectedStr(((RadioChoice)value).toString());
      }

      return this;
    }
  }
