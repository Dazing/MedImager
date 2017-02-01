/**
 * @(#) FolderNodeModel.java
 */

package medview.medimager.model;

/**
 * A folder node model.
 * @author Fredrik Lindahl
 */
public abstract class FolderNodeModel extends NodeModel
{
	// CONSTRUCTOR(S)

	/**
	 * Constructs a folder node model with a null
	 * parent and unspecified folder name.
	 */
	public FolderNodeModel()
	{
		this("Folder");
	}

	/**
	 * Constructs a folder node model with the
	 * specified name and a null parent.
	 */
	public FolderNodeModel( String name )
	{
		super(name);
	}


	// ABSTRACT IMPLEMENTATIONS

	public boolean isLeaf()
	{
		return false;
	}

	public boolean isBranch()
	{
		return true;
	}
}
