/*
 * @(#)TreeFilePhotoParser.java
 *
 * $Id: TreeFilePhotoParser.java,v 1.4 2005/12/22 19:10:23 lindahlf Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling.examination;

/**
 * Specifies what it is to be a
 * tree file photo parser, in other
 * words that an implementation of this
 * class is responsible for extracting
 * the relative location of a photo
 * from an absolute path specified in
 * a tree file, which in most cases
 * points to an incorrect file. This
 * occurs since the tree files are
 * moved around a lot, but the paths
 * are not.
 */
public interface TreeFilePhotoParser
{
	/**
	 * Extracts the relative location (relative to the MVD
	 * structure root directory). As the time of this writing,
	 * the implementation classes needs to consider that there
	 * might be two different formats of tree files, where the
	 * location of the photos differ depending on which format
	 * is in use for the current node. No matter, the users of
	 * this interface does not need to consider this, but this
	 * is more internal details for the implementation class.
	 *
	 * @param nodePath the exact path of the photo file as
	 * it is specified in the tree file.
	 */
	public String extractRelativeLocation(String nodePath);
}
