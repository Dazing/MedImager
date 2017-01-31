/*
 * $Id: ImageButton.java,v 1.6 2004/12/08 14:42:51 lindahlf Exp $
 *
 * Created on July 5, 2001, 10:50 PM
 */

package medview.medrecords.components;

import java.io.*;
import java.awt.*;

import javax.swing.*;

import medview.medrecords.data.*;
import medview.medrecords.tools.*;

import misc.foundation.io.*;

/**
 * @author  nils, modified by Fredrik Lindahl 041202
 * @version
 */
public class ImageButton extends JButton implements Comparable
{
	private String imagePath; // contains original image path (SHOULD NOT BE USED FOR CONSTRUCTION)
	
	private byte[] imageData; // contains the raw image data
	
	private String caption; // the text to be shown on the button

	private Image thumbnail; // the image to be shown on the button

	/**
	 * Once an image button is constructed, the file used to
	 * construct it can be discarded, since the image byte
	 * data is stored internally at construction.
	 * @param in_path String
	 */
	public ImageButton(String in_path) 
	{
		super();
		
		this.imagePath = in_path;
		
		File inputFile = new File(in_path);

		caption = StringTools.stripExtension(inputFile.getName());

		thumbnail = ThumbnailCache.getInstance().getImage(in_path); // get image via thumbnail cache
		
		try
		{
			FileInputStream fIS = new FileInputStream(new File(in_path));
			
			imageData = IOUtilities.getBytesFromInputStream(fIS);
			
			fIS.close();
		}
		catch (FileNotFoundException exc)
		{
			exc.printStackTrace();
			
			imageData = null;
		}
		catch (IOException exc)
		{
			exc.printStackTrace();
			
			imageData = null;
		}
		
		setIcon(new ImageIcon(thumbnail)); // sets the button graphic
		
		setText(caption); // sets text displayed on button graphic
		
		setPreferredSize(new Dimension(ThumbnailCache.BUTTON_WIDTH, ThumbnailCache.BUTTON_HEIGHT));
		
		setHorizontalTextPosition(SwingConstants.CENTER);
		
		setVerticalTextPosition(SwingConstants.BOTTOM);
	}

	public String getCaption()
	{
		return new String(caption);
	}
	
	public Image getThumbnail()
	{
		return this.thumbnail;
	}
	
	public Image getFullImage() // built on-the-fly from cached byte array
	{
		return Toolkit.getDefaultToolkit().createImage(imageData);
	}
	
	public byte[] getImageData()
	{
		return this.imageData;
	}
	
	public String getImagePath()
	{
		return this.imagePath;
	}
	
	public int compareTo(Object other)
	{
		return getImagePath().compareTo(((ImageButton)other).getImagePath());
	}
	
	public boolean equals(Object other)
	{
		if (other instanceof ImageButton)
		{
			return getImagePath().equalsIgnoreCase(((ImageButton)other).getImagePath());
		}
		else
		{
			return false;
		}
	}
}

