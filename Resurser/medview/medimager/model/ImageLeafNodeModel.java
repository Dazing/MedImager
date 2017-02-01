/**
 * @(#) ImageLeafNodeModel.java
 */

package medview.medimager.model;

import java.awt.image.*;

import medview.medimager.foundation.*;

public interface ImageLeafNodeModel
{
	/**
	 * Returns the full image.
	 * @return BufferedImage the full image.
	 */
	public abstract BufferedImage getFullImage();

	/**
	 * Returns the full image as a byte array.
	 * @return byte[] the full image as a byte array.
	 */
	public abstract byte[] getFullImageByteArray( );

	/**
	 * Returns the ImageDataObtainer used to obtain the
	 * image data.
	 * @return ImageDataObtainer the ImageDataObtainer used
	 * to obtain the image data.
	 */
	public abstract ImageDataObtainer getImageDataObtainer( );

	/**
	 * Returns the medium image.
	 * @return BufferedImage the medium image.
	 */
	public abstract BufferedImage getMediumImage( );

	/**
	 * Obtains the medium image raw byte data.
	 * @return byte[] the medium image raw byte data.
	 */
	public abstract byte[] getMediumImageByteArray( );

	/**
	 * Returns the original file name of the image.
	 * @return String the original file name of the image.
	 */
	public abstract String getOriginalImageFileName( );

	/**
	 * Obtains the thumbnail version of the kept image.
	 * @return BufferedImage the thumbnail version of the
	 * kept image.
	 */
	public abstract BufferedImage getThumbImage( );

	/**
	 * Obtains the thumbnail image raw byte data.
	 * @return byte[] the thumbnail image raw byte data.
	 */
	public abstract byte[] getThumbImageByteArray( );

	/**
	 * Sets the image data obtainer used to collect the
	 * image data.
	 * @param o ImageDataObtainer the image data obtainer
	 * used to collect the image data.
	 */
	public abstract void setImageDataObtainer( ImageDataObtainer o );

	/**
	 * Sets the original file name of the image.
	 * @param name String the original file name of the image.
	 */
	public abstract void setOriginalImageFileName( String name );
}
