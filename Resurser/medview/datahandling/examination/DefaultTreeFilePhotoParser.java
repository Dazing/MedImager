/*
 * @(#)DefaultTreeFilePhotoParser.java
 *
 * $Id: DefaultTreeFilePhotoParser.java,v 1.10 2007/04/09 15:19:31 oloft Exp $
 *
 * --------------------------------
 * Original author: Fredrik Lindahl
 * --------------------------------
 */

package medview.datahandling.examination;

import java.io.*;

import medview.datahandling.*;

import misc.foundation.io.*;

public class DefaultTreeFilePhotoParser implements MedViewDataConstants, TreeFilePhotoParser, Serializable
{
	private String fileSepRegExp;
	
	/**
	* This parser extracts the image file name without the path,
	 * and then appends it to the pictures subdirectory name.
	 * @param nodePath String
	 * @param pictSubDirName String
	 * @return String
	 */
	
	public String extractRelativeLocation(String nodePath) {
		// The regExp thing (split) does not seem to handle backslash, therefore 
		// it is replaced by a slash
		String cleanedPath = nodePath.replace('\\', '/');
		
		// Based on making a regexp of all file separators and splitting at these		
		String [] pathComponents = cleanedPath.split(fileSepRegExp);
		int pathComponentsCount = pathComponents.length;
		
		//System.out.println(nodePath + " -> " + cleanedPath);
		
		// In a path /a/b/c/foo.jpg we wish to keep c/foo.jpg, that is the two last 
		// components
		switch (pathComponentsCount) {
			case 0:
				return "error"; // Should not happen
			case 1:
				return MVD_PICTURES_SUBDIRECTORY + File.separator + MVD_PICTURES_SUBDIRECTORY +
				File.separator + pathComponents[pathComponentsCount-1];
			default:
				return MVD_PICTURES_SUBDIRECTORY + File.separator + pathComponents[pathComponentsCount-2] +
				File.separator + pathComponents[pathComponentsCount-1];
		}
	}
	
	public DefaultTreeFilePhotoParser() {
		/* String[] recognizedFileSeparators = IOUtilities.getRecognizedFileSeparators();
		
		String fileSeparators = "";
		for (int i=0; i<recognizedFileSeparators.length; i++) {
			fileSeparators = fileSeparators + recognizedFileSeparators[i];
		}*/
		
		fileSepRegExp = "[" + "\\" + "/" + "]";
		
		//System.out.println("fileSepRegExp: " + fileSepRegExp);
	}
	
	
}
