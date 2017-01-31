package medview.meduweb.datahandler;

import java.io.File;

/**
 *This class extends medview.tools.ThumbnailMaker and is
 *specific to mEduWeb.
 *@author Fredrik Pettersson
 *@version 0.1
 */
public class ThumbnailMaker extends medview.tools.ThumbnailMaker {

	/** Directory to store thumbnails in. */
	public static String THUMB_DIR = System.getProperty("thumbnail.dir");
	/** Directory to read images from. */
	public static String IMAGE_DIR = System.getProperty("image.dir");
	/** URL to the get access to images. */
	public static String IMAGE_URL = System.getProperty("image.url");
	/** The file separator of this system. (typically slashes)*/
	private String FILE_SEP = System.getProperty("file.separator");

	/**
	 *Produces a thumbnail for a specific image. (This method is mEduWeb specific.)
	 *@param fall The case which the image belongs to.
	 *@param im The name of the image.
	 */
	public void makeThumbnail(String fall, String im, String inIm) {
		IMAGE_DIR = System.getProperty("image.dir");
		long startTime = System.currentTimeMillis();
		//boolean ret_value = false;
		//Used to see if thumbnail exists and to eventually create new directory
		File outfile = new File(THUMB_DIR + fall +  FILE_SEP + im);


		//If there's already a thumbnail, don't create one
		if(!outfile.exists()) {
			//System.out.println("Creating thumbnail.");
			//Is there a thumbnail directory for this case?
			//If not: create one in THUMB_DIR
			File outdir = new File(THUMB_DIR + fall +  FILE_SEP);
			if(!outdir.isDirectory()) {
				outdir.mkdir();
			}
			outdir = null;
			super.produceThumbnail(IMAGE_DIR + inIm, THUMB_DIR + fall +  FILE_SEP + im); 
			if(!outfile.exists())
				System.out.println("medview.meduweb.datahandler.ThumbnailMaker: " +
						   "Unable to create thumbnail (no file created)."); 	
		}else {
			//System.out.println("Found existing thumbnail.");
		}	
		outfile = null;
		

		//Make sure new image is released.
		//Perform garbage collection
		//long beforeGC = System.currentTimeMillis();
		//Runtime rt = Runtime.getRuntime();
		//rt.gc();
		long end = System.currentTimeMillis();
		System.out.println("Total time: " + (end - startTime));
		//System.out.println("GC time: " + (end - beforeGC));
		//return ret_value;
	}

}
