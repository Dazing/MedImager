/*
 * @(#)CaseCopier.java
 *
 * $Id: CaseCopier.java,v 1.3 2003/11/03 20:43:25 oloft Exp $
 *
 */
package medview.meduweb.data;

import java.util.*;
import java.io.*;
import misc.gui.ExtensionFileFilter;
import medview.meduweb.data.*;
import medview.meduweb.datahandler.Case;

public class CaseCopier {
	public String treeFilePath = System.getProperty("forest.dir");

	public CaseCopier() {
	}

	//DEBUG:
	public void test() {
		copyCase("G02559501", "E:\\meduweb\\WEB-INF\\classes\\medview\\data\\treefile\\TestData.mvd\\",
				"E:\\meduweb\\WEB-INF\\classes\\medview\\data\\treefile\\TestData2.mvd\\");


	}

	//END DEBUG

	/**
	 *Copies a case (treefiles and pictures) from one directory to another.
	 *@param pcode the pcode of the case
	 *@param fromdir the path to the directory to copy from
	 *@param todir the path to the directory to copy to
	 */
	public void copyCase(String pcode, String fromdir, String todir) {
		copyCaseText(pcode, fromdir, todir);
		copyCasePictures(pcode, fromdir, todir);

	}

	private void copyCaseText(String pcode, String fromdir, String todir) {
		File indir = new File(fromdir + "Forest.forest\\");
		File outdir = new File(todir + "Forest.forest\\");
		String[] filenames = null;
		try {
			FilenameFilter pff = (FilenameFilter)(new PrefixFileFilter(pcode));
			filenames = indir.list(pff);
		} catch(IncompatibleClassChangeError icce) {
			icce.printStackTrace();
		}
		if(!(filenames.length > 0)) {
			File[] files = getPatientTreeFiles(pcode);
			if(files.length < 1)
				files = getPatientTreeFiles(pcode.toLowerCase());
			filenames = new String[files.length];
			for(int i = 0; i < files.length; i++) {
				filenames[i] = files[i].getName();
			}

		}		

		if(!outdir.isDirectory()) {
			outdir.mkdir();
		}
		
		for(int i = 0; i < filenames.length; i++) {
			try {
				FileInputStream sourceFile = new FileInputStream(fromdir + "Forest.forest\\" + filenames[i]);
				FileOutputStream destFile = new FileOutputStream(todir + "Forest.forest\\" + filenames[i]);
				byte readFromFile[]=new byte[sourceFile.available()];
				sourceFile.read(readFromFile);
				destFile.write(readFromFile); 
				sourceFile.close();
				destFile.close();
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}
		}
		Runtime rt = Runtime.getRuntime();
		rt.gc();
			
	}

	private void copyCasePictures(String pcode, String fromdir, String todir) {
		Case thisPatient = new Case();
		ArrayList images = thisPatient.getPatientImages(pcode);
		Iterator iI = images.iterator();
		while(iI.hasNext()) {
			try {
				String filename = (String)iI.next();
				filename = filename.replace('/','\\');
				File outdir = new File(todir +  "Pictures\\" + filename.substring(0, filename.lastIndexOf('\\')));
				if(!outdir.isDirectory()) {
					outdir.mkdirs();
				}	
				FileInputStream sourceFile = new FileInputStream(fromdir + "Pictures\\" + filename);
				FileOutputStream destFile = new FileOutputStream(todir + "Pictures\\" + filename);
				byte readFromFile[]=new byte[sourceFile.available()];
				sourceFile.read(readFromFile);
				destFile.write(readFromFile); 
				sourceFile.close();
				destFile.close();
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}

		}
		//Manual Garbage Collection
		Runtime rt = Runtime.getRuntime();
		rt.gc();

	}

	public class PrefixFileFilter implements FilenameFilter {
		private String thePrefix = "";

		public PrefixFileFilter(String prefix) {
			thePrefix = prefix;
		}

		public boolean accept(File dir,String name) {
			return name.startsWith(thePrefix);
		}
	}

	//Stolen from medview.data.TreeFileHandler and then modified (that class isn't proper).
	public File[] getPatientTreeFiles(String patient_pcode) {
        	File[] files = listTreeFiles();
        
   	     	Vector matching = new Vector();
        
     	   	for (int i = 0; i < files.length; i++) {
       	   		String filename = files[i].getName();
        		String p_code = "";
	    		try {
				FileInputStream fs = new FileInputStream(files[i]);
				boolean pcodeNotFound = true;
				while (pcodeNotFound) {
		    			while (fs.read() != 'N' && fs.available() > 0){}
				    	if (fs.read() == 'P' && fs.read() == '-' && 
						fs.read() == 'c' && fs.read() == 'o' && 
						fs.read() == 'd' && fs.read() == 'e' &&
						fs.read() == '\n' && fs.read() == 'L') {
						//Now we're at the P-code section
						char c = (char) fs.read();
						while (c != '#'){
			    				p_code = p_code + c;
			    				c= (char) fs.read();
						}
						pcodeNotFound = false;
		    			}
				}
				fs.close();
	    		} catch (Exception e) {
				System.out.println("File: " + files[i].getName() + " generated an error");
	    		}
         		System.out.println(p_code);
            		if (p_code.equals(patient_pcode)) {
                		matching.add(files[i]);
           		}
            
            
            
        	}
        
        	files = new File[matching.size()];
        
        	files = (File[]) matching.toArray(files);
        
        	return files;
        
        
    	}

	//Stolen from medview.data.TreeFileHandler cause TFH isnt't compiling ...
	public File[] listTreeFiles() {
        
        	if (treeFilePath == null) {
            		System.err.println("TreeFileHandler.listTreeFiles: ERROR: treefilepath = null!");
        	} else {
            		System.out.println("Listing tree files in " + treeFilePath);
        	}
        
        	Vector treeFiles = new Vector();
        
        	String[] treeFileExtensions = { "tree","TREE","Tree" };
        	ExtensionFileFilter treeFileFilter = new ExtensionFileFilter( treeFileExtensions);
        	treeFileFilter.setDescription("Tree files (.tree)");
        
        	File[] files = (new File(treeFilePath)).listFiles(treeFileFilter);
        
        	return files;
    	}
}