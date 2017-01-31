package medview.aggregator;

import java.awt.*;
import javax.swing.*;

/**
 * Title:        Aggregator
 * Description:  A medview tool for grouping of terms.
 * Copyright:    Copyright (c) 2002
 * Company:      G?teborg University
 * @author Nader Nazari
 * @version 1.0
 */

 /**
  * A help class for debuging or error handling.
  */
public class Ut {

    static final int No       = JOptionPane.NO_OPTION;
    static final int Yes      = JOptionPane.YES_OPTION;
	static final int Cancel   = JOptionPane.CANCEL_OPTION;

    public Ut() {
    }

	static String showOptions(String aTitle,String[] strs){

		if (strs.length == 0 ) return null;

		Object selectedValue = JOptionPane.showInputDialog(null, "Choose one",
				aTitle,JOptionPane.INFORMATION_MESSAGE, null, strs, strs[0]);

		return selectedValue.toString();
	}

	static String showInput(String aTitle,String aMessage){
		String ans = JOptionPane.showInputDialog(null,aMessage,
							    aTitle,JOptionPane.INFORMATION_MESSAGE );
		return ans;
	}

	static int yesNoCancelQuestion(String str){
		int ans =JOptionPane.showConfirmDialog(null,str , "Warning",
							JOptionPane.YES_NO_CANCEL_OPTION);
		return ans;
    }
    static int yesNoQuestion(String str){
		int ans =JOptionPane.showConfirmDialog(null,str , "Warning",
							JOptionPane.YES_NO_OPTION);
		return ans;
    }

    static void message(String str){
	    JOptionPane.showMessageDialog(null, str, "Messsage", JOptionPane.INFORMATION_MESSAGE);
    }

    static void warning(String str){
	    JOptionPane.showMessageDialog(null, str, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    static void error(String str){
	    JOptionPane.showMessageDialog(null, str, "Error", JOptionPane.ERROR_MESSAGE);
    }
    static void prt(String str){
	System.out.println(str);
    }
    static boolean errFalse(String str){
	System.out.println(str);
	return false;
    }

}