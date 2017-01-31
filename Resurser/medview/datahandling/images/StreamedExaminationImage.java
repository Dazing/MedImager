/**
 * $Id: StreamedExaminationImage.java,v 1.5 2006/04/24 14:17:41 lindahlf Exp $
 *
 * $Log: StreamedExaminationImage.java,v $
 * Revision 1.5  2006/04/24 14:17:41  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.4  2005/09/09 15:40:52  lindahlf
 * Server cachning
 *
 * Revision 1.3  2005/06/03 15:49:18  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.2  2003/09/09 17:23:14  erichson
 * Added field String imageName, initiated to examinationIdentifier in constructor.
 * Added method setName. Changed getName to return imageName.
 *
 *
 */

package medview.datahandling.images;

import java.awt.image.*;

import javax.imageio.*;

import java.io.*;

import medview.datahandling.examination.*;

/**
 * A class that implements the ExaminationImage interface by
 * reading an image from a supplied InputStream. The
 * image is read at construction, if it could not be read,
 * the exception thrown at construction is kept and rethrown
 * when a client tries to obtain the image from the class.
 *
 * @author Fredrik Lindahl
 */
public class StreamedExaminationImage implements ExaminationImage
{
	/**
	 * Obtains a thumbnail version of the image,
	 * or throws the exception that was thrown
	 * when the object tried to construct the
	 * image at construction.
	 */
	public BufferedImage getThumbnail() throws IOException
	{
		if (!couldCreateImage())
		{
			throw thrownException;
		}
		else
		{
			BufferedImage thumbImage = new BufferedImage(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, getFullImage().getType());

			thumbImage.getGraphics().drawImage(getFullImage(), 0, 0, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, null);

			return thumbImage;
		}
	}

	/**
	 * Obtains the image constructed from the
	 * specified InputStream, or throws
	 * the exception that was thrown when the
	 * object tried to construct the image at
	 * construction.
	 */
	public BufferedImage getFullImage() throws IOException
	{
		if (!couldCreateImage())
		{
			throw thrownException;
		}
		else
		{
			return image;
		}
	}

	/**
	 * Utility method that checks whether or
	 * not the image could be created during
	 * construction.
	 */
	private boolean couldCreateImage()
	{
		return thrownException == null;
	}


	/**
	 * Obtains the ExaminationIdentifier that
	 * represents the examination during which
	 * the image was taken.
	 */
	public ExaminationIdentifier getExaminationIdentifier()
	{
		return examinationIdentifier;
	}

	/**
	 * Returns a name for the image.
	 */
	public String getName()
	{
		return imageName; // Changed by NE 030909
	}

	public void setName(String newName) // Added by NE 030909
	{
		imageName = newName;
	}

	public InputStream getInputStream() throws IOException
	{
		return inputStream;
	}


	private BufferedImage createImage(InputStream input)
	{
		try
		{
			BufferedImage image = ImageIO.read(input);

			if (image == null)
			{
				throw new IOException(getExaminationIdentifier() + ": no reader for file " + getName());
			}

			return image;
		}
		catch (IOException exc)
		{
			thrownException = exc;
		}

		return null;
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
	 * Constructs an image from the specified InputStream.
	 * The examination during which the image was taken is also
	 * specified. The image is created from the stream during
	 * construction, and any exception thrown is kept and re-
	 * thrown when a client tries to obtain the image.
	 */
	public StreamedExaminationImage(InputStream input, ExaminationIdentifier id)
	{
		this.inputStream = input;

		this.imageName = examinationIdentifier.toString(); // Added by NE 030909

		this.image = createImage(input);

		this.examinationIdentifier = id;
	}

	private ExaminationIdentifier examinationIdentifier = null;

	private IOException thrownException = null;

	private InputStream inputStream = null;

	private BufferedImage image = null;

	private String imageName;
}
