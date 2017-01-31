//
//  DocumentListener.java
//  TETest
//
//  Created by Olof Torgersson on Sun Nov 30 2003.
//  $Id: TermEditDocumentListener.java,v 1.2 2003/12/16 00:11:31 oloft Exp $.
//

package medview.termedit.model;

import java.util.*;

public interface TermEditDocumentListener extends EventListener {

    public void termSelectionChanged(TermEditDocumentEvent evt);

    public void termAdded(TermEditDocumentEvent evt);

    public void termRemoved(TermEditDocumentEvent evt);

    public void termTypeChanged(TermEditDocumentEvent evt);

    public void valueAdded(TermEditDocumentEvent evt);

    public void valueRemoved(TermEditDocumentEvent evt);
    
    public void valueRenamed(TermEditDocumentEvent evt);

}
