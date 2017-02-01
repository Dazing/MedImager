/**
 * @(#) NoteLeafModel.java
 */

package medview.medimager.model;

/**
 * A node model containing a note.
 */
public interface NoteLeafNodeModel
{
	/**
	 * Obtain the note associated with the node.
	 */
	public abstract String getNote( );

	/**
	 * Set the note to associate with the node.
	 */
	public abstract void setNote( String note );


}
