/*
 * $Id: RowLimitedDocument.java,v 1.1 2003/11/10 23:23:55 oloft Exp $
 *
 * Created on July 9, 2001, 8:15 PM
 */

package medview.formeditor.tools;

//import java.awt.*;

import javax.swing.text.*;

/**
 *
 * @author  nils
 * @version 
 */
public class RowLimitedDocument extends PlainDocument {

    private int rowAmount;
    
    /** Creates new RowLimitedDocument */
    public RowLimitedDocument(int row_amount) {
        rowAmount = row_amount;
    }

   
    
    
    
    
    
    
    
    
    public void insertString(int offs, String str, AttributeSet a)  throws BadLocationException {
    
        // If null, do nothing
        if (str == null) {
            return;
        }

    
    
    }

}
