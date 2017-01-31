package medview.tools;

// $Id: ThumbnailMaker.java,v 1.4 2003/11/03 13:59:26 oloft Exp $

// Note: javax.media.jai and the body of produceThumbnail(String, String) have been commented
// out in order to enable compilation in the absence of Java Advanced Imaging (JAI).
// To actually use this class put them back in again (and, of course, install JAI).

import java.io.File;
//import javax.media.jai.*; //Image manipulation. - Remove comment to enable
import java.awt.image.renderable.*; //ParameterBlock

/**
 *This class is used to produce thumbnails (JPEG) for existing pictures (BMP, JPEG, GIF, PNG).
 *It only holds one method (makeThumbnail).
 *The following System properties has to bes set to use this class (or manually): file.separator 
 *(typically '/' or '\'). 
 *Java Advanced Imaging (JAI) is used to manipulate images. JAI can be downloaded for free
 *from java.sun.com
 *@author Fredrik Pettersson, MedView/mEduWeb
 *@version 0.3
 *@see javax.media.jai
 */ 
public class ThumbnailMaker {

	private int width = 0;
	private int height = 0;
	private boolean widthSet = false;
	private boolean heightSet = false;

	/** The file separator of this system. (typically slashes)*/
	private String FILE_SEP = System.getProperty("file.separator");
	/** Scaling parameter */
	private double scale = 0.3;
	/** X scaling */
	private double xScale = 0.3;
	/** Y scaling */
	private double yScale = 0.3;
	/** Type of image as output */
	private String outType = "JPEG";
	/** Decides whether to automatically create directories or not */
	private boolean makeDirs = true;
	/** Decides whether to overwrite existing files or not */
	private boolean overWrite = false;
	/**Used to hold options for new image */
	// private ParameterBlock pb = null; - OT: Remove comment to enable 
	/**The images */
	//private RenderedOp snk = null; - OT: Remove comment to enable
	//private RenderedOp src = null; - OT: Remove comment to enable

	/** 
	 *Produces a thumbnail from a specified image.
	 *@param inImage The full path of the image to produce a thumbnail for including filename.
	 *@param outImage The full path of the produced thumbnail including filename.
	 *@return True if a thumbnail was created, false otherwise.
	 *@throws IllegalArgumentException
	 *@see javax.media.jai
	 */
	public boolean produceThumbnail(String inImage, String outImage) throws IllegalArgumentException { /*
		boolean thumbMade = false;
		File outdir = null;

		//Does the InImage exist?
		if(!(new File(inImage)).isFile()) {
			throw new IllegalArgumentException("medview.tools.ThumbnailMaker.produceThumbnail" +
							"(java.lang.String,java.lang.String):" +
							"  Nu such file: " + inImage + ".");

		}

		if(FILE_SEP != null) {
			if(outImage.lastIndexOf(FILE_SEP) > -1) {
				outdir = new File(outImage.substring(0, outImage.lastIndexOf(FILE_SEP)));
			} else {
				throw new IllegalArgumentException("medview.tools.ThumbnailMaker.produceThumbnail" +
								"(java.lang.String,java.lang.String):" +
								" Illegal write directory (" + outImage + ").");
			}
		} else if(!makeDirs){
			if(outImage.indexOf('/') > -1) {
				outdir = new File(outImage.substring(0, outImage.lastIndexOf('/')));
			} else if (outImage.indexOf('\\')> -1) {
				outdir = new File(outImage.substring(0, outImage.lastIndexOf('\\')));
			} else {
				throw new IllegalArgumentException("medview.tools.ThumbnailMaker.produceThumbnail" +
								"(java.lang.String,java.lang.String):" +
								" Illegal write directory (" + outImage + ").");
			}
		}

		if(!makeDirs && !outdir.isFile()) {
			throw new IllegalArgumentException("medview.tools.ThumbnailMaker.produceThumbnail" +
							"(java.lang.String,java.lang.String):" +
							" Illegal write directory (" + outImage + ").");
		} else if(makeDirs && !outdir.exists()) {
			boolean madeDir = outdir.mkdir();
		}

		File outIm = new File(outImage);
		if(overWrite || !outIm.exists()) {

			//Load image (using Java Advanced Imaging)
			src = JAI.create("fileload", inImage);

			//ParameterBlock with desired values.
			pb = new ParameterBlock();
          		pb.addSource(src);                   // The source image
          		pb.add(new Float(xScale));            // The xScale
          		pb.add(new Float(yScale));           // The yScale
          		pb.add(0.0F);                       // The x translation
          		pb.add(0.0F);                       // The y translation
          		pb.add(new InterpolationNearest()); // The interpolation

			thumbMade = true;
			snk = JAI.create("scale", pb, null);

			//Store thumbnail(using Java Advanced Imaging) 
			JAI.create("filestore", snk, outImage, outType, null);

			//Release source and sink (new image).
			src.dispose();
			snk.dispose();
			snk = null;
			src = null;
			
			thumbMade = true;
		}

		//Garbage Collection to make sure files are released.
		outIm = null;
		outdir = null;
		//Slowed things down toooooo much 
		//Runtime rt = Runtime.getRuntime();
		//rt.gc();

		return thumbMade;*/
         return false;

	}


	/**
	 *Gets the relative scaling currently used by this object.
	 *@return A double telling current scaling.
	 */
	public double getScale() {
		return scale;
	}

	/**
	 *Sets the relative scaling to be used by this object.<BR>
	 *Scaling >1 enlarges the image. <BR>
	 *Scaling <1 shrinks the image. <BR>
	 *@param newScale The new scaling to use.
	 */
	public void setScale(double newScale) {
		scale = newScale;
		xScale = newScale;
		yScale = newScale;
	}

	/**
	 *Sets x scaling (width).
	 *@param newScale < 1 for shrinking, > 1 for enlarging.
	 */
	public void setXScale(double newScale) {
		xScale = newScale;
	}

	/**
	 *Sets y scaling (height).
	 *@param newScale < 1 for shrinking, > 1 for enlarging.
	 */
	public void setYScale(double newScale) {
		yScale = newScale;
	}
	
	

	/**
	 *Used to set the systemspecific file separator manually.
	 *@param separator A String holding the separator.
	 */
	public void setFileSeparator(String separator) {
		FILE_SEP = separator;
	}

	/**
	 *Sets the wanted height (in pixels) for the new image. <BR>
	 *This causes the scaling to be set automatically.
	 *@param height Wanted height in pixels.
	 */
	public void setHeight(int height) {
		this.height = height;
		this.heightSet = true;
	}

	/**
	 *If set to true directories will be made automatically.
         *Default is to make dirs (true).
	 *@param makeDirs True to make directories, false otherwise.
         */
	public void setMakeDirs(boolean makeDirs) {
		this.makeDirs = makeDirs;
	}

	/**
         *Sets whether to overwrite files or not.
         *Default is not to overwrite existing files (false).
         *@param overWrite If set to true files will be written over, else not.
         */
	public void setOverWrite(boolean overWrite) {
		this.overWrite = overWrite;
	}

	/**
         *Sets the type of image to produce.
         *Default i JPEG ("JPEG").
	 *@param imtype String with the name of the type, these are specified in JAI.
	 *@see javax.media.jai
         */
	public void setOutType(String imtype) {

		this.outType = imtype;
	}
	
	/**
	 *Sets the wanted width (in pixels) for the new image. <BR>
	 *This causes the scaling to be set automatically.
	 *@param width Wanted width in pixels.
	 */
	public void setWidth(int width) {
		this.width = width;
		this.widthSet = true;
	}
	
}