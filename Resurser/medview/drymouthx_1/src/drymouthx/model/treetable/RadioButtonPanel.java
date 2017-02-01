/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package drymouthx.model.treetable;

/**
 *
 * @author Marie Lindgren
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.EventObject;

public class RadioButtonPanel extends JPanel {
    JRadioButton[] buttons;

    public RadioButtonPanel(String[] str) {
      setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
      buttons = new JRadioButton[str.length];
      for (int i=0; i<buttons.length; i++) {
        buttons[i] = new JRadioButton(str[i]);
        buttons[i].setFocusPainted(false);
        buttons[i].setBackground(Color.white);
        add(buttons[i]);
      }
      this.setBackground(Color.white);

    }

    public void setSelectedStr(String str) {
      for (int i=0;i<buttons.length;i++) {
        buttons[i].setSelected( str.equals(buttons[i].getText()) );
      }
    }

    public String getSelectedStr() {
      for (int i=0; i<buttons.length; i++) {
        if (buttons[i].isSelected()) {
          return buttons[i].getText();
        }
      }
      return "";
    }

    public JRadioButton[] getButtons() {
      return buttons;
    }
  }