package drymouthx.model.treetable;



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.EventObject;



public class MultiEditor implements TableCellEditor {
  private final static int      COMBO = 0;
  private final static int    BOOLEAN = 1;
  private final static int     STRING = 2;
  private final static int     PROGRESSBAR = 3;
  private final static int     RADIOCHOICE = 4;
  private final static int NUM_EDITOR = 5;
  DefaultCellEditor[] cellEditors;
  JComboBox comboBox;
  int flg;

  public MultiEditor() {
    cellEditors = new DefaultCellEditor[NUM_EDITOR];

    String[] combochoices = {"Yes","No"};
    comboBox = new JComboBox(combochoices);
    cellEditors[COMBO]   = new DefaultCellEditor(comboBox);

    JCheckBox checkBox   = new JCheckBox();
    checkBox.setHorizontalAlignment(JLabel.CENTER);
    cellEditors[BOOLEAN] = new DefaultCellEditor(checkBox);

    JTextField textField = new JTextField();
    cellEditors[STRING]  = new DefaultCellEditor(textField);

    JTextField textProgressBarField = new JTextField();
    cellEditors[PROGRESSBAR]  = new DefaultCellEditor(textProgressBarField);

    String[] choices = {"Yes","No"};
    cellEditors[RADIOCHOICE] = new RadioButtonEditor(new JCheckBox(), new RadioButtonPanel(choices));

    flg = 0;
  }

  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    if (value instanceof ProgressBar) {
      flg = PROGRESSBAR;
      return cellEditors[PROGRESSBAR].getTableCellEditorComponent(table, value,   isSelected, row, column);
    } else if (value instanceof RadioChoice) {
      flg = RADIOCHOICE;
      return cellEditors[RADIOCHOICE].getTableCellEditorComponent(table, value,   isSelected, row, column);
    } else if (value instanceof ComboString) {
      flg = COMBO;
      return cellEditors[COMBO].getTableCellEditorComponent(table, value,   isSelected, row, column);
    } else if (value instanceof Boolean) {
      flg = BOOLEAN;
      return cellEditors[BOOLEAN].getTableCellEditorComponent(table, value, isSelected, row, column);
    } else if (value instanceof String) {
      flg = STRING;
      return cellEditors[STRING].getTableCellEditorComponent(table, value, isSelected, row, column);
    }
    return null;
  }

  public Object getCellEditorValue() {
    switch (flg) {
      case   COMBO:
        String str = (String)comboBox.getSelectedItem();
        return new ComboString(str);
      case PROGRESSBAR:
        Integer n = 0;
        if (! (cellEditors[flg].getCellEditorValue() instanceof Integer)) {
            String tmpstr;
            if (cellEditors[flg].getCellEditorValue() instanceof String) {
                tmpstr = (String)cellEditors[flg].getCellEditorValue();
            } else {
                tmpstr = cellEditors[flg].getCellEditorValue().toString();
            }
            try {
                n = Integer.parseInt(tmpstr);
//                n = Integer.valueOf(tmpstr).intValue();
            } catch (NumberFormatException ex) {
            }
        } else {
            n = ((Integer)cellEditors[flg].getCellEditorValue()).intValue();
        }
        return new ProgressBar(n);
      case RADIOCHOICE:
      case BOOLEAN:
      case  STRING:
        return cellEditors[flg].getCellEditorValue();
      default:         return null;
    }
  }

  public Component getComponent() {
    return cellEditors[flg].getComponent();
  }
  public boolean stopCellEditing() {
    return cellEditors[flg].stopCellEditing();
  }
  public void cancelCellEditing() {
    cellEditors[flg].cancelCellEditing();
  }
  public boolean isCellEditable(EventObject anEvent) {
    return cellEditors[flg].isCellEditable(anEvent);
  }
  public boolean shouldSelectCell(EventObject anEvent) {
    return cellEditors[flg].shouldSelectCell(anEvent);
  }
  public void addCellEditorListener(CellEditorListener l) {
    cellEditors[flg].addCellEditorListener(l);
  }
  public void removeCellEditorListener(CellEditorListener l) {
    cellEditors[flg].removeCellEditorListener(l);
  }
  public void setClickCountToStart(int n) {
    cellEditors[flg].setClickCountToStart(n);
  }
  public int getClickCountToStart() {
    return cellEditors[flg].getClickCountToStart();
  }
}

//This is all about RadioButton Renderer and Editor--------------------

class RadioButtonEditor extends DefaultCellEditor implements ItemListener {
    RadioButtonPanel panel;

    public RadioButtonEditor(JCheckBox checkBox,RadioButtonPanel panel) {
        super(checkBox);
        this.panel = panel;
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton[] buttons  = panel.getButtons();
        for (int i=0; i<buttons.length; i++) {
            buttonGroup.add(buttons[i]);
            buttons[i].addItemListener(this);
        }
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof RadioChoice) {
            panel.setSelectedStr(((RadioChoice)value).toString());
        }
        return panel;
    }

    public Object getCellEditorValue() {
        return new RadioChoice(panel.getSelectedStr());
    }

    public void itemStateChanged(ItemEvent e) {
        super.fireEditingStopped();
    }
}
//  -----------------


