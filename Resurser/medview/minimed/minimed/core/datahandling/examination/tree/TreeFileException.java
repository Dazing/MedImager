
/**
 *
 * $Id: TreeFileException.java,v 1.1 2006/05/29 18:32:53 limpan Exp $
 *
 * $Log: TreeFileException.java,v $
 * Revision 1.1  2006/05/29 18:32:53  limpan
 * Added minimed code
 * ----------------------------------------------------------------------
 *
 * Revision 1.2  2002/10/22 17:13:53  erichson
 * Javadoc, Id and Log tags
 *
 */

package minimed.core.datahandling.examination.tree;

/**
 * This exception is thrown when there is a problem when parsing a tree file.
 * @author Nils Erichson <d97nix@dtek.chalmers.se> 
 *
 * @version 1.0
 * 
 */

public class TreeFileException extends Exception {
    
/** 
 * Create a new TreeFileException.
 * @param reason The reason the exception was thrown
 */    
  TreeFileException(String reason) {
    super(reason);
  }
}
