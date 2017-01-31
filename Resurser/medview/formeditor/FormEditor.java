//
//  FormEditor.java
//  MedView
//
//  Created by Olof Torgersson on Mon Nov 10 2003.
//  $Id: FormEditor.java,v 1.2 2003/11/14 21:32:47 oloft Exp $
//

package medview.formeditor;

import javax.swing.*;
import medview.formeditor.data.*;

public class FormEditor {
    
    public static void main(String[] args) {
        // Create application controller
        AppController app = AppController.instance();

        // And show a new document
        app.newDocument(null);
    }

}
