/**
 * $Id: Ut.java,v 1.1 2003/11/10 23:23:56 oloft Exp $
 * Copyright:    Copyright (c) 2002
 * Company:      Göteborg University
 * @author Nader Nazari
 * @version 1.0
 */

package medview.formeditor.tools;

import java.awt.*;
import javax.swing.*;
/**
 * A help class for debuging or error handling.
 */
public class Ut {
    
    static final public int No       = JOptionPane.NO_OPTION;
    static final public int Yes      = JOptionPane.YES_OPTION;
    static final public int Cancel   = JOptionPane.CANCEL_OPTION; 
    
    public Ut() {
    }
    
    public static String showOptions(String aTitle,String[] strs){
        
        if (strs.length == 0 ) return null;
        
        Object selectedValue = JOptionPane.showInputDialog(null, "Choose one",
        aTitle,JOptionPane.INFORMATION_MESSAGE, null, strs, strs[0]);
        
        return selectedValue.toString();
    }
    
    public static String showInput(String aTitle,String aMessage){
        String ans = JOptionPane.showInputDialog(null,aMessage,
        aTitle,JOptionPane.INFORMATION_MESSAGE );
        return ans;
    }
    
    public static int yesNoCancelQuestion(String str){
        int ans =JOptionPane.showConfirmDialog(null,str , "Warning",
        JOptionPane.YES_NO_CANCEL_OPTION);
        return ans;
    }
    public static int yesNoQuestion(String str){
        int ans =JOptionPane.showConfirmDialog(null,str , "Warning",
        JOptionPane.YES_NO_OPTION);
        return ans;
    }
    
    public static void message(String str){
        JOptionPane.showMessageDialog(null, str, "Messsage", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void warning(String str){
        JOptionPane.showMessageDialog(null, str, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    public static void error(String str){
        JOptionPane.showMessageDialog(null, str, "Error", JOptionPane.ERROR_MESSAGE);
    }
    public static void prt(String str){
        System.out.println(str);
    }
    public static boolean errFalse(String str){
        System.out.println(str);
        return false;
    }
    
}