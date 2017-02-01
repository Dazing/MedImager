/*
 * TextTableFile.java
 *
 * Created on den 29 juli 2004, 18:44
 *
 * $Id: AbstractTableFile.java,v 1.1 2004/09/09 10:31:51 erichson Exp $
 *
 * $Log: AbstractTableFile.java,v $
 * Revision 1.1  2004/09/09 10:31:51  erichson
 * First check-in.
 *
 */

package medview.datahandling.examination.tablefile;

import java.io.*;

/**
 * Abstract base class for table file implementations
 * @author Nils Erichson <d97nix@dtek.chalmers.se>
 */
public abstract class AbstractTableFile implements TableFile  {
    
    protected File theFile;
    
    /** Creates a new instance of TextTableFile */
    public AbstractTableFile(File file)
    {
        theFile = file;
        // Read the text file into a proper data structure to perform the TableFile operations
    }
    
    public String getName()
    {
        return theFile.getName();
    }
    
    public boolean exists()
    {
        return theFile.exists();
    }
    
    public String getPath()
    {
        return theFile.getPath();
    }
}
