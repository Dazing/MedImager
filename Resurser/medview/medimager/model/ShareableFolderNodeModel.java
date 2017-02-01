/**
 * @(#) ShareableFolderNodeModel.java
 */

package medview.medimager.model;

/**
 * A folder node model which also can be shared.
 * @author Fredrik Lindahl
 */
public abstract class ShareableFolderNodeModel extends FolderNodeModel
{

	// MEMBER CONSTANTS

	/**
	 * Integer constant indicating a non-shared node.
	 */
	public static final int NOT_SHARED = 0;

	/**
	 * Integer constant indicating a globally shared folder node.
	 */
	public static final int GLOBALLY_SHARED = 1;

	/**
	 * Integer constant indicating a locally shared node.
	 */
	public static final int LOCALLY_SHARED = 2;

	/**
	 * Integer constant indicating a globally and locally shared node.
	 */
	public static final int GLOBALLY_AND_LOCALLY_SHARED = 3;


	// MEMBER VARIABLES

	private int shareType = NOT_SHARED;


	// CONSTRUCTOR(S)

	public ShareableFolderNodeModel()
	{
		super();
	}

	public ShareableFolderNodeModel(String name)
	{
		super(name);
	}


	// CLONING

	public Object clone()
	{
		ShareableFolderNodeModel clonedNodeModel = (ShareableFolderNodeModel) super.clone();

		clonedNodeModel.shareType = this.shareType;

		return clonedNodeModel;
	}


	// FOLDER SHARING

	/**
	 * Sets the share type of the node.
	 * @param shareType
	 */
	public void setShareType( int shareType )
	{
		this.shareType = shareType;
	}

	/**
	 * Returns the type of sharing of the node. If the
	 * node is not shared, it will return NOT_SHARED.
	 * @return
	 */
	public int getShareType( )
	{
		return shareType;
	}

	/**
	 * Whether or not the node model is shared.
	 * @return
	 */
	public boolean isShared( )
	{
		return (shareType != NOT_SHARED);
	}

}
