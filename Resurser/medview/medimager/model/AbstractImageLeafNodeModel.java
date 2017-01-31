/**
 * @(#) ImageLeafNodeModel.java
 */

package medview.medimager.model;

import java.awt.image.*;

import medview.medimager.foundation.*;

/**
 * A leaf node model which carries with it data about an image.
 * @author Fredrik Lindahl
 */
public abstract class AbstractImageLeafNodeModel extends LeafNodeModel implements ImageLeafNodeModel
{
	// PRIVATE MEMBERS

	private ImageDataObtainer imageDataObtainer = null;

	private String originalFileName = null;


	// CONSTRUCTOR(S)

	public AbstractImageLeafNodeModel()
	{
		super();
	}

	public AbstractImageLeafNodeModel( String description )
	{
		super(description);
	}


	// CLONING

	public Object clone()
	{
		AbstractImageLeafNodeModel clonedNodeModel = (AbstractImageLeafNodeModel) super.clone();

		clonedNodeModel.imageDataObtainer = this.imageDataObtainer; // this is ok

		clonedNodeModel.originalFileName = this.originalFileName; // this is ok

		return clonedNodeModel;
	}


	// IMAGE DATA OBTAINER

	/**
	 * Sets the image data obtainer used to collect the
	 * image data.
	 */
	public void setImageDataObtainer(ImageDataObtainer o)
	{
		this.imageDataObtainer = o;
	}

	/**
	 * Gets the currently set image data obtainer.
	 */
	public ImageDataObtainer getImageDataObtainer()
	{
		return imageDataObtainer;
	}


	// ORIGINAL IMAGE FILE NAME

	/**
	 * Returns the original file name of the image.
	 */
	public String getOriginalImageFileName()
	{
		return originalFileName;
	}

	/**
	 * Sets the original file name of the image.
	 */
	public void setOriginalImageFileName(String name)
	{
		this.originalFileName = name;
	}


	// IMAGES

	/**
	 * Obtains the full version of the kept image.
	 */
	public BufferedImage getFullImage( )
	{
		try
		{
			return imageDataObtainer.getFullImage();
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return null;
		}
	}

	/**
	 * Obtains the medium version of the kept image.
	 */
	public BufferedImage getMediumImage( )
	{
		try
		{
			return imageDataObtainer.getMediumImage();
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return null;
		}
	}

	/**
	 * Obtains the thumbnail version of the kept image.
	 */
	public BufferedImage getThumbImage( )
	{
		try
		{
			return imageDataObtainer.getThumbImage();
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return null;
		}
	}

	/**
	 * Obtains the full image raw byte data.
	 */
	public byte[] getFullImageByteArray()
	{
		try
		{
			return imageDataObtainer.getFullImageByteArray();
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return null;
		}
	}

	/**
	 * Obtains the medium image raw byte data.
	 */
	public byte[] getMediumImageByteArray()
	{
		try
		{
			return imageDataObtainer.getMediumImageByteArray();
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return null;
		}
	}

	/**
	 * Obtains the thumbnail image raw byte data.
	 */
	public byte[] getThumbImageByteArray()
	{
		try
		{
			return imageDataObtainer.getThumbImageByteArray();
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return null;
		}
	}

}
