/**
 * @(#) DefaultFolderNodeModel.java
 */

package medview.medimager.model;

/**
 * A shareable folder node model.
 * @author Fredrik Lindahl
 */
public class DefaultFolderNodeModel extends ShareableFolderNodeModel
{
	/**
	 * Constructs a default folder node model with
	 * no name and no parent (null parent).
	 */
	public DefaultFolderNodeModel()
	{
		super();
	}

	/**
	 * Constructs a default folder node model with
	 * the specified name and no parent (null parent).
	 */
	public DefaultFolderNodeModel(String name)
	{
		super(name);
	}
}
