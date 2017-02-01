/*
 * $Id: InputContainerPanel.java,v 1.19 2008/01/31 13:23:26 it2aran Exp $
 *
 * Created on August 1, 2001, 12:56 PM
 *
 * $Log: InputContainerPanel.java,v $
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
import java.awt.event.*;
import java.awt.Font.*;

import java.beans.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import medview.medrecords.components.inputs.*;

/**
 * @author  nils
 * @version
 */
public class InputContainerPanel extends JPanel
{	
	private TabPanel parentTab;

	private ValueInputComponent valueInputComponent;

	public InputContainerPanel(ValueInputComponent component)
	{
		setInputComponent(component);
	}

	public void setTabPanel(TabPanel aTab)
	{
		parentTab = aTab;
	}

	public void setInputComponent(ValueInputComponent component)
	{
		valueInputComponent = component;

		if (!(component instanceof JComponent))
		{
			JOptionPane.showMessageDialog(parentTab, "FATAL ERROR: Could not cast " +
			
				"ValueInputComponent to JComponent!");
			
			return;
		}
		
		this.removeAll();
		
		this.setLayout(new BorderLayout());

		String description = component.getDescription();
		
		JLabel descLabel = new JLabel("<description> " + description );
		descLabel.setAlignmentY(0);
		descLabel.setAlignmentX(0);
    	descLabel.setAutoscrolls(true);
		descLabel.setBackground(new JLabel().getBackground());
        Font font = new JLabel().getFont().deriveFont(Font.BOLD,12);
        descLabel.setForeground(new Color(0,100,180));

        descLabel.setFont(font);
        JPanel descAndComm = new JPanel();
        descAndComm.setLayout(new GridLayout(1,1));
        descAndComm.add(descLabel); // usually a question

        //add comment if it's not empty
        String comment = component.getComment();
        if(!comment.equals("") && !comment.equals("NULL"))
        {
            descAndComm.setLayout(new GridLayout(2,1));
            JLabel commentLabel = new JLabel("<comment> "+comment);
            commentLabel.setOpaque(false);
            commentLabel.setFont(new JLabel().getFont().deriveFont(Font.PLAIN));
            descAndComm.add(commentLabel);
        }

        this.add(descAndComm, BorderLayout.NORTH);

        this.add(valueInputComponent, BorderLayout.CENTER); // it can be a picture or textField

        String borderText = valueInputComponent.getName(); // attrib name

        //only show a border if there is a text (class infoText doesn't have)
        if(!borderText.equals(""))
        {
            Font borderFont = new JLabel().getFont().deriveFont(Font.PLAIN,9);
            this.setBorder(BorderFactory.createTitledBorder(null, "<name> " +borderText, TitledBorder.LEFT,TitledBorder.TOP,borderFont,Color.black));
        }
        
	}

	public void selectComponent()
	{
		valueInputComponent.focusInput();
	}

	public void mouseclicked(java.awt.event.MouseEvent evt)
	{
		parentTab.scrollToPanel(this);
	}
}
