/*
 * FileExaminationImage.java
 *
 * Created on November 20, 2002, 2:55 PM
 *
 * $Id: FileExaminationImage.java,v 1.10 2006/04/24 14:17:41 lindahlf Exp $
 *
 * $Log: FileExaminationImage.java,v $
 * Revision 1.10  2006/04/24 14:17:41  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.9  2005/09/09 15:40:52  lindahlf
 * Server cachning
 *
 * Revision 1.8  2005/06/03 15:49:18  lindahlf
 * <No Comment Entered>
 *
 * Revision 1.7  2003/09/09 17:22:20  erichson
 * Added field String imageName, initiated to examinationIdentifier in constructor.
 * Added method setName. Changed getName to return imageName.
 *
 * Revision 1.6  2003/08/19 16:03:16  lindahlf
 * See 030819_Release_Notes_DATAHANDLING_MISC_COMMON.pdf
 *
 * Revision 1.5  2003/07/02 16:16:20  erichson
 * Large rewrites which fixes bugzilla bug 006 (pictures in OM.MVD would not be read which locked the application)
 *
 * Revision 1.4  2003/07/02 10:41:53  erichson
 * Better error handling, step 1
 *
 * Revision 1.3  2002/11/22 16:47:58  erichson
 * Small fix to fixImagePaths(..) since it did not treat \ and / the same
 *
 * Revision 1.2  2002/11/21 16:16:56  erichson
 * removed try() and wait()
 *
 * Revision 1.1  2002/11/21 15:28:00  erichson
 * First check-in
 *
 */

package medview.datahandling.images;

import java.io.*;

import java.awt.image.*;

import javax.imageio.*;

import medview.datahandling.examination.*;

/**
 * Implementation of the ExaminationImage interface for files.
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: The MedView Project @ Chalmers University of Technology</p>
 *
 * @author Fredrik Lindahl, Nils Erichson
 * @version 1.0
 */
public class FileExaminationImage implements ExaminationImage
{

	// MEMBERS

	/**
	 * Cached thumbnail image.
	 */
	private BufferedImage cachedThumbImage;

	/**
	 * Name of image,
	 */
	private String imageName;

	/**
	 * Underlying File image object.
	 */
	private final File imageFile;

	/**
	 * Corresponding examination identifier.
	 */
	private final ExaminationIdentifier examinationIdentifier;


	// CONSTRUCTOR(S)

	/**
	 * Construct a file examination image, taken during the
	 * specified examination.
	 * @param examinationIdentifier ExaminationIdentifier
	 * @param imageFile File
	 */
	public FileExaminationImage(ExaminationIdentifier examinationIdentifier, File imageFile)
	{
		this.examinationIdentifier = examinationIdentifier;

		this.imageFile = imageFile;

		imageName = imageFile.getName();
	}


	// EXAMINATIONIMAGE INTERFACE IMPLEMENTATION

	/**
	 * Obtain the full image.
	 * @return BufferedImage
	 * @throws IOException
	 */
	public synchronized BufferedImage getFullImage() throws IOException
	{
		BufferedImage retImage = ImageIO.read(imageFile); // -> IOException, but might also return null if no reader (!)

		if (retImage == null)
		{
			throw new IOException(getExaminationIdentifier() + ": no reader for file " + getName());
		}
		else
		{
			return retImage;
		}
	}

	/**
	 * Obtain a thumbnail version of the full image.
	 * @return BufferedImage
	 * @throws IOException
	 */
	public BufferedImage getThumbnail() throws IOException
	{
		if (cachedThumbImage == null)
		{
			cachedThumbImage = createThumbImage(); // -> IOException
		}

		return cachedThumbImage;
	}

	/**
	 * Obtain the examination identifier identifying the examination
	 * associated with the image.
	 * @return ExaminationIdentifier
	 */
	public ExaminationIdentifier getExaminationIdentifier()
	{
		return examinationIdentifier;
	}

	/**
	 * Obtain an identifier for the image. For instance the filename.
	 * @return String
	 */
	public String getName()
	{
		return imageName;
	}

	/**
	 * Sets an identifier for the image. For instance the filename.
	 * @param newName String
	 */
	public void setName(String newName)
	{
		imageName = newName;
	}


	// UTILITY METHODS

	/**
	 * Creates a new thumbnail of this FileExaminationImage
	 * @param thumbnailFile the file to put the thumbnail in
	 */
	private synchronized BufferedImage createThumbImage() throws IOException
	{
		if (cachedThumbImage == null)
		{
			cachedThumbImage = new BufferedImage(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, getFullImage().getType()); // -> IOException

			cachedThumbImage.getGraphics().drawImage(getFullImage(), 0, 0, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, null);
		}

		return cachedThumbImage;
	}

	/**
	 * Returns an input stream from which the image can be
	 * read. Added by Fredrik 14 august 2003.
	 */
	public InputStream getInputStream() throws IOException
	{
		try
		{
			return new FileInputStream(imageFile); // -> IOException
		}
		catch (IOException exc)
		{
			throw new IOException(getExaminationIdentifier() + ": " + exc.getMessage());
		}
	}

	/**
	 * Returns the File identifying the image.
	 * @return File
	 */
	public File getFile()
	{
		return imageFile;
	}
}
