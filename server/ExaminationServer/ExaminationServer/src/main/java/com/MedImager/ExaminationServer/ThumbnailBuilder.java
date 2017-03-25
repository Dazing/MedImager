package com.MedImager.ExaminationServer;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ThumbnailBuilder {
	public ThumbnailBuilder(){
		new File("thumbnail").mkdir();
	}
	public File getThumbnail(String path, String examinationID, int index){
		File thumbnail;
		BufferedImage originalBufferedImage = null;
		try {
		    originalBufferedImage = ImageIO.read(new File(path));
		}   
		catch(IOException ioe) {
		}
		int thumbnailWidth = 300;
		int thumbnailHeight = 210;
		BufferedImage resizedImage = new BufferedImage(thumbnailWidth, thumbnailHeight, originalBufferedImage.getType());
		Graphics2D g = resizedImage.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawImage(originalBufferedImage, 0, 0, thumbnailWidth, thumbnailHeight, null);
		g.dispose();
		thumbnail = new File("thumbnail/" + index + "-" + examinationID + ".jpg");
		try {
		    ImageIO.write(resizedImage, "JPG", thumbnail);
		}
		catch (IOException ioe) {
		}
		return thumbnail;
	}
}
