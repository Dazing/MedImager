/**
 *
 * $Id: ByteArrayExaminationImage.java,v 1.9 2006/04/24 14:17:41 lindahlf Exp $
 *
 * $Log: ByteArrayExaminationImage.java,v $
 * Revision 1.9  2006/04/24 14:17:41  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.8  2005/09/09 15:40:52  lindahlf
 * Server cachning
 *
 * Revision 1.7  2005/06/03 15:49:18  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.6  2004/12/08 14:47:53  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.5  2004/11/09 21:14:25  lindahlf
 * Datalayer upgrade in connection with server development
 *
 * Revision 1.4  2004/10/19 21:40:19  lindahlf
 * Lade tillbaka @deprecation eftersom det faktiskt hjälper om man har en bra IDE
 *
 * Revision 1.3  2003/09/09 17:23:15  erichson
 * Added field String imageName, initiated to examinationIdentifier in constructor.
 * Added method setName. Changed getName to return imageName.
 *
 *
 */

package medview.datahandling.images;

import java.io.*;

import java.awt.image.*;

import javax.imageio.*;

import medview.datahandling.examination.*;

/**
 * A class that implements the ExaminationImage interface by
 * reading an image from a supplied byte array. In order to
 * minimize memory usage, the byte array specified to the
 * constructor is stored as a member variable, and used to
 * construct an image each time the image is requested.
 * @author Fredrik Lindahl
 */
public class ByteArrayExaminationImage implements ExaminationImage
{
	/**
	 * Obtains a thumbnail version of the image.
	 */
	public BufferedImage getThumbnail() throws IOException
	{
		BufferedImage thumbImage = new BufferedImage(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, getFullImage().getType()); // -> IOException

		thumbImage.getGraphics().drawImage(getFullImage(), 0, 0, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, null);

		return thumbImage;
	}

	/**
	 * Obtains the full version of the image.
	 */
	public BufferedImage getFullImage() throws IOException
	{
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(byteArray)); // -> IOException, but might also return null if no reader (!)

		if (image == null)
		{
			throw new IOException(getExaminationIdentifier() + ": no reader for file " + getName());
		}
		else
		{
			return image;
		}
	}

	/**
	 * Returns the name for the image.
	 */
	public String getName()
	{
		return imageName; // Changed by NE 030909
	}

	/**
	 * Sets the identifying name for the image. Does not change the backing store.
	 */
	public void setName(String newName)
	{
		imageName = newName;
	}

	/**
	 * Returns an inputstream that provides raw image bytes.
	 */
	public InputStream getInputStream()
	{
		return new ByteArrayInputStream(byteArray);
	}

	/**
	 * Returns the examination id of the image's examination.
	 */
	public ExaminationIdentifier getExaminationIdentifier()
	{
		return examinationIdentifier;
	}

	/**
	 * This method is not supported in this implementation.
	 * @return File
	 */
	public File getFile() throws misc.foundation.MethodNotSupportedException
	{
		throw new misc.foundation.MethodNotSupportedException();
	}

	/**
	 * Constructs an image from the specified byte array. As the
	 * name for the image, the string representation of the
	 * examination identifier will be used.
	 */
	public ByteArrayExaminationImage(byte[] input, ExaminationIdentifier id)
	{
		this(input, id, id.toString());
	}

	/**
	 * Constructs an image from the specified byte array. This
	 * version of the method allows you to specify the name of
	 * the image as well.
	 */
	public ByteArrayExaminationImage(byte[] input, ExaminationIdentifier id, String name)
	{
		this.examinationIdentifier = id;

		this.byteArray = input;

		this.imageName = name;
	}

	private ExaminationIdentifier examinationIdentifier = null;

	private byte[] byteArray = null;

	private String imageName;
}
