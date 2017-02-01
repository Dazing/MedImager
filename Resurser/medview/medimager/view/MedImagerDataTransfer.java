package medview.medimager.view;

import java.awt.datatransfer.*;

/**
 * A Singleton object grouping all datatransfer-related
 * functionality in the MedImager application.
 * @author Fredrik Lindahl
 */
public class MedImagerDataTransfer
{
	// PUBLIC MEMBERS

	public static final String DB_IMAGE_FLAVOR_MIME = DataFlavor.javaJVMLocalObjectMimeType + ";class=java.util.Vector";

	public static final String NODE_FLAVOR_MIME = DataFlavor.javaJVMLocalObjectMimeType + ";class=java.util.Vector";

	public static DataFlavor dbImageFlavor;

	public static DataFlavor nodeFlavor;


	// PRIVATE MEMBERS

	private final Clipboard localClipboard = new Clipboard("medImagerClipboard");

	private static MedImagerDataTransfer instance;


	// STATIC MEMBER DECLARATION BLOCK

	static
	{
		try
		{
			dbImageFlavor = new DataFlavor(DB_IMAGE_FLAVOR_MIME);

			nodeFlavor = new DataFlavor(NODE_FLAVOR_MIME);

			instance = new MedImagerDataTransfer();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}


	// CONSTRUCTOR(S)

	private MedImagerDataTransfer() {}	// not to be instantiated or subclassed


	// PUBLIC STATIC METHODS

	public static MedImagerDataTransfer instance()
	{
		if (instance == null)
		{
			instance = new MedImagerDataTransfer();
		}

		return instance;
	}


	// PUBLIC INSTANCE METHODS

	public Clipboard getLocalClipboard()
	{
		return localClipboard;
	}
}
