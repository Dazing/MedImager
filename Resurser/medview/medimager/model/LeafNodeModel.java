/**
 * @(#) DefaultFolderNodeModel.java
 */

package medview.medimager.model;

/**
 * A branch node model (leaf).
 * @author Fredrik Lindahl
 */
public abstract class LeafNodeModel extends NodeModel
{
	// CONSTRUCTOR(S)

	/**
	 * Constructs a leaf node model with a null
	 * parent and unspecified description.
	 */
	public LeafNodeModel()
	{
		this("Leaf");
	}

	/**
	 * Constructs a leaf node model with the
	 * specified description and a null parent.
	 */
	public LeafNodeModel(String description)
	{
		super(description);
	}


	// ABSTRACT IMPLEMENTATIONS

	public boolean isLeaf()
	{
		return true;
	}

	public boolean isBranch()
	{
		return false;
	}
}
