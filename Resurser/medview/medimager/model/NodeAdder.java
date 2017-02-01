/**
 * @(#) NodeAdder.java
 */

package medview.medimager.model;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

import java.io.*;

import javax.imageio.*;

import medview.medimager.foundation.*;

import misc.gui.utilities.*;

/**
 * A class responsible for adding data in various source
 * formats to node models.
 * @author Fredrik Lindahl
 */
public class NodeAdder implements MedImagerConstants
{
	/**
	 * Adds an array of data base search results to the specified node model.
	 * If the specified node model is not of branch type, nothing will
	 * happen.
	 */
	public void addToNode( final DatabaseImageSearchResult[] results, NodeModel node )
	{
		if (node.isBranch())
		{
			try
			{
				for (int ctr=0; ctr<results.length; ctr++)
				{
					final DatabaseImageSearchResult currResult = results[ctr];

					ImageDataObtainer obtainer = new ImageDataObtainer()
					{
						public BufferedImage getFullImage( ) throws IOException
						{
							return currResult.getFullImage();
						}

						public BufferedImage getMediumImage( ) throws IOException
						{
							int mediumWidth = MEDIUM_IMAGE_DIMENSION.width;

							int mediumHeight = MEDIUM_IMAGE_DIMENSION.height;

							BufferedImage fullImage = getFullImage();

							GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

							GraphicsDevice gd = ge.getDefaultScreenDevice();

							GraphicsConfiguration gc = gd.getDefaultConfiguration();

							int transparency = fullImage.getColorModel().getTransparency();

							BufferedImage scaled = gc.createCompatibleImage(mediumWidth, mediumHeight, transparency);

							Graphics2D g2d = scaled.createGraphics();

							double scaleX = (double) scaled.getWidth() / fullImage.getWidth();

							double scaleY = (double) scaled.getHeight() / fullImage.getHeight();

							AffineTransform xform = AffineTransform.getScaleInstance(scaleX, scaleY);

							g2d.drawRenderedImage(fullImage, xform);

							g2d.dispose();

							return scaled;
						}

						public BufferedImage getThumbImage( ) throws IOException
						{
							return currResult.getThumbImage();
						}

						public byte[] getFullImageByteArray( ) throws IOException
						{
							return GUIUtilities.convertBufferedImageToByteArray(getFullImage(), "jpg"); // TWEAK LATER
						}

						public byte[] getMediumImageByteArray( ) throws IOException
						{
							return GUIUtilities.convertBufferedImageToByteArray(getMediumImage(), "jpg"); // TWEAK LATER
						}

						public byte[] getThumbImageByteArray( ) throws IOException
						{
							return GUIUtilities.convertBufferedImageToByteArray(getThumbImage(), "jpg"); // TWEAK LATER
						}
					};

					final DefaultLeafNodeModel curr = new DefaultLeafNodeModel("En bild");

					curr.setOriginalImageFileName(currResult.getImageName());

					curr.setImageDataObtainer(obtainer);

					curr.setEID(currResult.getEID());

					curr.setPID(currResult.getPID());

					node.add(curr);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Adds an array of files containing image data to the specified node
	 * model. If the specified node model is not of branch type, nothing will
	 * happen. If a file cannot be identified as an image file by the ImageIO
	 * subsystem, it will not be added to the node.
	 */
	public void addToNode(File[] files, NodeModel node)
	{
		if (node.isBranch())
		{
			try
			{
				for (int ctr=0; ctr<files.length; ctr++)
				{
					final File currFile = files[ctr];

					// check that the file is an image file (otherwise we dont add it)

					if (ImageIO.read(currFile) != null)
					{
						ImageDataObtainer obtainer = new ImageDataObtainer()
						{
							public BufferedImage getFullImage( ) throws IOException
							{
								return ImageIO.read(currFile);
							}

							public BufferedImage getMediumImage( ) throws IOException
							{
								int mediumWidth = MEDIUM_IMAGE_DIMENSION.width;

								int mediumHeight = MEDIUM_IMAGE_DIMENSION.height;

								BufferedImage fullImage = getFullImage();

								GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

								GraphicsDevice gd = ge.getDefaultScreenDevice();

								GraphicsConfiguration gc = gd.getDefaultConfiguration();

								int transparency = fullImage.getColorModel().getTransparency();

								BufferedImage scaled = gc.createCompatibleImage(mediumWidth, mediumHeight, transparency);

								Graphics2D g2d = scaled.createGraphics();

								double scaleX = (double) scaled.getWidth() / fullImage.getWidth();

								double scaleY = (double) scaled.getHeight() / fullImage.getHeight();

								AffineTransform xform = AffineTransform.getScaleInstance(scaleX, scaleY);

								g2d.drawRenderedImage(fullImage, xform);

								g2d.dispose();

								return scaled;
							}

							public BufferedImage getThumbImage( ) throws IOException
							{
								int thumbWidth = THUMB_IMAGE_DIMENSION.width;

								int thumbHeight = THUMB_IMAGE_DIMENSION.height;

								BufferedImage fullImage = getFullImage();

								GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

								GraphicsDevice gd = ge.getDefaultScreenDevice();

								GraphicsConfiguration gc = gd.getDefaultConfiguration();

								int transparency = fullImage.getColorModel().getTransparency();

								BufferedImage scaled = gc.createCompatibleImage(thumbWidth, thumbHeight, transparency);

								Graphics2D g2d = scaled.createGraphics();

								double scaleX = (double) scaled.getWidth() / fullImage.getWidth();

								double scaleY = (double) scaled.getHeight() / fullImage.getHeight();

								AffineTransform xform = AffineTransform.getScaleInstance(scaleX, scaleY);

								g2d.drawRenderedImage(fullImage, xform);

								g2d.dispose();

								return scaled;
							}

							public byte[] getFullImageByteArray( ) throws IOException
							{
								return GUIUtilities.convertBufferedImageToByteArray(getFullImage(), "jpg");		// TWEAK LATER
							}

							public byte[] getMediumImageByteArray( ) throws IOException
							{
								return GUIUtilities.convertBufferedImageToByteArray(getMediumImage(), "jpg");	// TWEAK LATER
							}

							public byte[] getThumbImageByteArray( ) throws IOException
							{
								return GUIUtilities.convertBufferedImageToByteArray(getThumbImage(), "jpg");	// TWEAK LATER
							}
						};

						final DefaultLeafNodeModel curr = new DefaultLeafNodeModel(IMPORTED_IMAGE_FILE_STRING);

						curr.setOriginalImageFileName(currFile.getName());

						curr.setImageDataObtainer(obtainer);

						curr.setEID(NOT_APPLICABLE_EID);

						curr.setPID(NOT_APPLICABLE_PID);

						node.add(curr);
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Adds an array of images to the specified node model. If
	 * the specified node model is not of branch type, nothing
	 * will happen.
	 */
	public void addToNode(BufferedImage[] images, NodeModel node)
	{
		if (node.isBranch())
		{
			try
			{
				for (int ctr=0; ctr<images.length; ctr++)
				{
					final BufferedImage currImage = images[ctr];

					ImageDataObtainer obtainer = new ImageDataObtainer()
					{
						public BufferedImage getFullImage( ) throws IOException
						{
							return currImage;
						}

						public BufferedImage getMediumImage( ) throws IOException
						{
							int mediumWidth = MEDIUM_IMAGE_DIMENSION.width;

							int mediumHeight = MEDIUM_IMAGE_DIMENSION.height;

							BufferedImage fullImage = getFullImage();

							GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

							GraphicsDevice gd = ge.getDefaultScreenDevice();

							GraphicsConfiguration gc = gd.getDefaultConfiguration();

							int transparency = fullImage.getColorModel().getTransparency();

							BufferedImage scaled = gc.createCompatibleImage(mediumWidth, mediumHeight, transparency);

							Graphics2D g2d = scaled.createGraphics();

							double scaleX = (double) scaled.getWidth() / fullImage.getWidth();

							double scaleY = (double) scaled.getHeight() / fullImage.getHeight();

							AffineTransform xform = AffineTransform.getScaleInstance(scaleX, scaleY);

							g2d.drawRenderedImage(fullImage, xform);

							g2d.dispose();

							return scaled;
						}

						public BufferedImage getThumbImage( ) throws IOException
						{
							int thumbWidth = THUMB_IMAGE_DIMENSION.width;

							int thumbHeight = THUMB_IMAGE_DIMENSION.height;

							BufferedImage fullImage = getFullImage();

							GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

							GraphicsDevice gd = ge.getDefaultScreenDevice();

							GraphicsConfiguration gc = gd.getDefaultConfiguration();

							int transparency = fullImage.getColorModel().getTransparency();

							BufferedImage scaled = gc.createCompatibleImage(thumbWidth, thumbHeight, transparency);

							Graphics2D g2d = scaled.createGraphics();

							double scaleX = (double) scaled.getWidth() / fullImage.getWidth();

							double scaleY = (double) scaled.getHeight() / fullImage.getHeight();

							AffineTransform xform = AffineTransform.getScaleInstance(scaleX, scaleY);

							g2d.drawRenderedImage(fullImage, xform);

							g2d.dispose();

							return scaled;
						}

						public byte[] getFullImageByteArray( ) throws IOException
						{
							return GUIUtilities.convertBufferedImageToByteArray(getFullImage(), "jpg");		// TWEAK LATER
						}

						public byte[] getMediumImageByteArray( ) throws IOException
						{
							return GUIUtilities.convertBufferedImageToByteArray(getMediumImage(), "jpg");	// TWEAK LATER
						}

						public byte[] getThumbImageByteArray( ) throws IOException
						{
							return GUIUtilities.convertBufferedImageToByteArray(getThumbImage(), "jpg");	// TWEAK LATER
						}
					};

					final DefaultLeafNodeModel curr = new DefaultLeafNodeModel(IMPORTED_IMAGE_FILE_STRING);

					curr.setOriginalImageFileName(UNKNOWN_STRING);

					curr.setImageDataObtainer(obtainer);

					curr.setEID(NOT_APPLICABLE_EID);

					curr.setPID(NOT_APPLICABLE_PID);

					node.add(curr);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
