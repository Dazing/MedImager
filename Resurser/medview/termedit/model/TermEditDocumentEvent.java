//
//  TermEditDocumentEvent.java
//  TETest
//
//  Created by Olof Torgersson on Sun Nov 30 2003.
//  $Id: TermEditDocumentEvent.java,v 1.2 2003/12/15 14:15:33 oloft Exp $.
//
package medview.termedit.model;

import java.util.*;

public class TermEditDocumentEvent extends EventObject {

    private String term = null;

    private String value = null;
    
    /**
    * Constructs a DocumentEvent with
     * the specified source.
     */
    public TermEditDocumentEvent(Object source)	{
        super(source);
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String t) {
        term = t;
    }
    
    public String getValue() {
        return value;
    }
    public void setValue(String v) {
        value = v;
    }
}
