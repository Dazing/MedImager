/*
 * $Id: InputContainerPanel.java,v 1.22 2010/07/01 19:31:28 oloft Exp $
 *
 * Created on August 1, 2001, 12:56 PM
 *
 * $Log: InputContainerPanel.java,v $
 * Revision 1.22  2010/07/01 19:31:28  oloft
 * Added * for required items
 *
 * Revision 1.21  2010/07/01 07:42:27  oloft
 * MR 4.5, minor edits
 *
 * Revision 1.20  2010/06/22 15:27:44  oloft
 * Tries to avoid horizontal scrolls by using several lines for description and comment when needed
 *
 * Revision 1.19  2008/01/31 13:23:26  it2aran
 * Cariesdata handler that retrieves caries data from an external database
 * Some bugfixes
 *
 * Revision 1.18  2007/10/17 15:17:03  it2aran
 * Medrecords 4.3 Beta 1
 *
 * Revision 1.17  2004/12/08 14:42:51  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.16  2003/12/21 21:54:11  oloft
 * Changed settings, removed DataHandlingHandler
 *
 * Revision 1.15  2003/11/11 13:42:02  oloft
 * Switching mainbranch
 *
 * Revision 1.14.2.1  2003/08/16 14:59:58  erichson
 * Cleanup (removed instanceof-type coding etc)
 *
 * Revision 1.14  2003/07/22 16:57:15  erichson
 * checkInputValues() changed to verifyInputValues(), which now throws ValueInputExceptions containing explanations why a value is considered invalid
 *
 */
package medview.medrecords.components;

import java.awt.*;
import java.awt.Font.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import medview.datahandling.*;
import misc.gui.components.MultiLineLabel;

import medview.medrecords.components.inputs.*;
import medview.medrecords.data.MRConst;

/**
 * @author  nils
 * @version
 */
public class InputContainerPanel extends JPanel {

    private TabPanel parentTab;
    private ValueInputComponent valueInputComponent;
    private MedViewDataHandler mVDH = MedViewDataHandler.instance();

    public InputContainerPanel(ValueInputComponent component) {
        setInputComponent(component);
    }

    public void setTabPanel(TabPanel aTab) {
        parentTab = aTab;
    }

    public void setInputComponent(ValueInputComponent component) {
        valueInputComponent = component;

        if (!(component instanceof JComponent)) {
            JOptionPane.showMessageDialog(parentTab, "FATAL ERROR: Could not cast "
                    + "ValueInputComponent to JComponent!");

            return;
        }

        this.removeAll();

        this.setLayout(new BorderLayout());

        String description = component.getDescription();
        // Add '(locked)' if the term list is locked
        if (!component.isEditable()) {
            description = description + " (" + mVDH.getLanguageString(MedViewLanguageConstants.LABEL_TERMS_LOCKED_LS_PROPERTY) + ")";
        }

		if (component.isRequired()) {
			description = description + " *";
		}
		
        Font labelFont = UIManager.getFont("Label.font");
        
        // Tries to find an appropriate number of lines using a default as heuristic
        MultiLineLabel descLabel = new MultiLineLabel(/*"<description> " + */description, (description.length() / MRConst.BOLD_LINE_WIDTH) + 1, 0);

        descLabel.setAlignmentY(0);
        descLabel.setAlignmentX(0);
        descLabel.setAutoscrolls(true);
        descLabel.setBackground(new JLabel().getBackground());
        descLabel.setForeground(new Color(0, 100, 180));
        descLabel.setFont(labelFont.deriveFont(Font.BOLD, 12));

        JPanel descAndComm = new JPanel();
        descAndComm.setLayout(new GridLayout(1, 1));
        descAndComm.add(descLabel); // usually a question

        //add comment if it's not empty
        String comment = component.getComment();
        if (!comment.equals("") && !comment.equals("NULL")) {
            descAndComm.setLayout(new GridLayout(2, 1));

            //
            int nbrOfLines = (comment.length() / MRConst.LINE_WIDTH) + 1; // if text is shorter than line, the result should be 1
            MultiLineLabel commentLabel = new MultiLineLabel(/*"<comment> "+*/comment, nbrOfLines, 0);
            commentLabel.setOpaque(false);
            commentLabel.setFont(labelFont.deriveFont(Font.PLAIN));
            descAndComm.add(commentLabel);
        }

        // debug
	/* descAndComm.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.red),
        descAndComm.getBorder()));*/
        this.add(descAndComm, BorderLayout.NORTH);

        this.add(valueInputComponent, BorderLayout.CENTER); // it can be a picture or textField

        String borderText = valueInputComponent.getName(); // attrib name

        //only show a border if there is a text (class infoText doesn't have)
        if (!borderText.equals("")) {
            Font borderFont = labelFont.deriveFont(Font.PLAIN, 9);
            this.setBorder(BorderFactory.createTitledBorder(null, /* "<name> " + */ borderText, TitledBorder.LEFT, TitledBorder.TOP, borderFont, Color.black));
        }

    }

    public void selectComponent() {
        valueInputComponent.focusInput();
    }

    public void mouseclicked(java.awt.event.MouseEvent evt) {
        parentTab.scrollToPanel(this);
    }
}
