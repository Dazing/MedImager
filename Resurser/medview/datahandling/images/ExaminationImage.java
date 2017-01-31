/*
 * ExaminationImage.java
 *
 * Created on November 20, 2002, 2:54 PM
 *
 * $Id: ExaminationImage.java,v 1.5 2006/04/24 14:17:41 lindahlf Exp $
 *
 * $Log: ExaminationImage.java,v $
 * Revision 1.5  2006/04/24 14:17:41  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.4  2005/06/03 15:49:18  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.3  2003/09/09 17:20:53  erichson
 * added setName method.
 *
 */

package medview.datahandling.images;

import java.io.InputStream;

import medview.datahandling.examination.ExaminationIdentifier;

/**
 * Class which represents an ExaminationImage and the methods
 * required to access the image data. Due to memory restrictions,
 * the methods returning images should return images on-demand, i.e.
 * the implementations should not cache the images. It is up to the
 * user classes to cache images if necessary.
 *
 * @authors Nils Erichson <d97nix@dtek.chalmers.se>, Fredrik Lindahl <lindahlf@cs.chalmers.se>
 * @version 1.1
 */
public interface ExaminationImage
{

	// CONSTANTS

	/**
	 * The standard height for thumbnails returned by implementations
	 * of this interface.
	 */
	public static final int THUMBNAIL_HEIGHT = 90;

	/**
	 * The standard width for thumbnails returned by implementations
	 * of this interface.
	 */
	public static final int THUMBNAIL_WIDTH = 120;


	// METHODS

	/**
	 * Obtain a thumbnail version of the image.
	 * @throws IOException If the image data could not be read
	 * @return a thumbnail Image.
	 */
	public java.awt.image.BufferedImage getThumbnail() throws java.io.IOException;

	/**
	 * Obtain the full image.
	 * @throws IOException if the image data could not be read
	 * @return the Image data of the full image.
	 */
	public java.awt.image.BufferedImage getFullImage() throws java.io.IOException;

	/**
	 * Get the examination identifier for the examination this
	 * image belongs to.
	 * @return The examinationIdentifier
	 */
	public ExaminationIdentifier getExaminationIdentifier();

	/**
	 * Get a 'name' for the image, i.e. a string that identifies
	 * the image. Can be the file name or something like that.
	 * @return
	 */
	public String getName();

	/**
	 * Set a new identifying name for the image. Mainly used when
	 * saving examinations, since the pictures' names will be changed
	 * to (for example) A00019611_date_-1, -2.jpg etc
	 */
	public void setName(String newName);

	/**
	 * Returns the image file, if applicable. Not all implementations
	 * may use files for the images, if such an implementation is used
	 * and the method is called on it, it will throw an exception.
	 * @return File
	 */
	public java.io.File getFile() throws misc.foundation.MethodNotSupportedException;

	/**
	 * Returns an input stream from which the image can be
	 * read. Added by Fredrik 14 august 2003.
	 */
	public InputStream getInputStream() throws java.io.IOException;

}
