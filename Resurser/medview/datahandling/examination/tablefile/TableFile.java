/*
 * TableFile.java
 *
 * Created on den 6 september 2004, 16:33
 *
 * $Id: TableFile.java,v 1.1 2004/09/08 12:45:22 erichson Exp $
 *
 * $Log: TableFile.java,v $
 * Revision 1.1  2004/09/08 12:45:22  erichson
 * First check-in.
 *
 */

package medview.datahandling.examination.tablefile;

import java.io.*;

import misc.foundation.util.*;

/**
 * Base class representing Table files (from which a data matrix can be derived)
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public interface TableFile 
{
    
    public abstract SparseMatrix getDataMatrix() throws IOException;
    
    public String getName();
    public String getPath();
    
    public boolean exists();            
}
