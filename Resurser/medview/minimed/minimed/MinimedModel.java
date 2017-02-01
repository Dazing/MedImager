package minimed;

import java.io.*;
import java.util.*;

import minimed.core.MinimedConstants;
import minimed.core.datahandling.DataHandlerFactory;
import minimed.core.datahandling.examination.tree.*;
import minimed.core.datahandling.template.InvalidTemplateException;
import minimed.core.datahandling.term.TermDataHandler;
import minimed.core.models.ExaminationModel;
import minimed.core.properties.PropertiesHandler;
import minimed.core.datahandling.template.PullXmlMedformTemplateReader;
import minimed.core.datahandling.examination.ExaminationValueContainer;
import ch.ubique.inieditor.IniEditor;

/**
 * This is the MiniMed model which is the MiniMed GUI's interface for the
 * core-functionalities.
 * <p>
 * For information about Dependencies, look att the following methods in the class
 * InputModel: notifyDependees() setNotVisible() hasDependees()
 *  
 * @author Andreas Nilsson, Jonas Hadin
 * @version 0.04
 */
public class MinimedModel {

	private TreeFileHandler saveTreeFileHandler = null;
	private TreeFileHandler databaseTreeFileHandler = null;
	private PropertiesHandler propHandler;
	private PullXmlMedformTemplateReader templateReader;
	private TermDataHandler termHandler;
	private ExaminationModel currentExaminationModel;
	private Date date = new Date();
	private File treeFile = null;
	private ExaminationValueContainer currentExaminationValueContainer;
	private IniEditor settings, language;
	
	
	/**
	 * Creates a <code>MinimedModel</code>.
	 */
	public MinimedModel() {

		/* Load settings file */
		settings = new IniEditor();
		try {
			settings.load(MinimedConstants.SETTINGS_FILE);
		} catch (IOException ioe) {
			System.out.println("Settings file not found: " + MinimedConstants.SETTINGS_FILE);
			System.exit(1);
		}
		
		/* Load language file */
		language = new IniEditor();
		try {
			language.load(MinimedConstants.LANGUAGE_FILE);
		} catch (IOException ioe) {
			System.out.println("Language file not found: " + MinimedConstants.LANGUAGE_FILE);
			System.exit(1);
		}
				
		propHandler = new PropertiesHandler(settings, language);
		termHandler = DataHandlerFactory.instance().getDefaultTermDataHandler();
		
		/* Create an ExaminationModel */
		try {
			getExamination();
		}
		catch (Exception e){}
	}
	
	public ExaminationModel getExamination() throws IOException, 
		InvalidTemplateException {
		
		/* If model hasn't been fetched before */
		if (currentExaminationModel == null) {
			File formTemplateFile = new File(propHandler.getFormTemplatePath());
			
			termHandler.setTermValueLocation(propHandler.getTermValuesPath());
			
			termHandler.setTermDefinitionLocation(propHandler.getTermDefinitionsPath());
			
			templateReader = new PullXmlMedformTemplateReader();
			currentExaminationModel = templateReader.readXMLExamination(formTemplateFile);
				
		}
		return currentExaminationModel;
	}
	
	public void setExaminationValueContainer(ExaminationValueContainer cont){
		currentExaminationValueContainer = cont;
	}
	
	public ExaminationValueContainer getExaminationValueContainer(){
		return currentExaminationValueContainer;
	}
	
	public void setCurrentTreeFile(File file){
		treeFile = file;
	}
	
	/* ***************************************************************
	 *  Setter methods for Properties module
	 * ***************************************************************/
	
	/** 
	 * Sets the path to the term values file.
	 * 
	 * @param pPath The path to the term values file
	 */
	public void setTermValuesPath(String pPath) {
		if (!pPath.equalsIgnoreCase(propHandler.getTermValuesPath())) {

			/* Update property handler */
			propHandler.setTermValuesPath(pPath);
			
			/* Current ExaminationModel is no longer valid since a 
			 * new term value file is used.
			 */
			currentExaminationModel = null;
		}
		
	}
	
	/** 
	 * Sets the path to the term definitions file.
	 * 
	 * @param  pPath The path to the term definitions file
	 */
	public void setTermDefinitionsPath(String pPath) {
		if (!pPath.equalsIgnoreCase(propHandler.getTermDefinitionsPath())) {

			/* Update property handler */
			propHandler.setTermDefinitionsPath(pPath);
			
			/* Current ExaminationModel is no longer valid since a new 
			 * term definition file is used.
			 */
			currentExaminationModel = null;
		}
	}
	
	/** 
	 * Sets the path to the form template file.
	 * 
	 * @param pPath The path to the form template file
	 */	
	public void setFormTemplatePath(String pPath) {
		if (!pPath.equalsIgnoreCase(propHandler.getFormTemplatePath())) {
			
			/* Update property handler */
			propHandler.setFormTemplatePath(pPath);
			
			
			/* Current ExaminationModel is no longer valid since a new 
			 * form template file is used.
			 */
			currentExaminationModel = null;
		}
		
	}
	
	/** 
	 * Sets the path to the help file.
	 * 
	 * @param pPath The path to the help file
	 */	
	public void setHelpFilePath(String pPath) {
		if (!pPath.equalsIgnoreCase(propHandler.getHelpFilePath())) {

			/* Update property handler */
			propHandler.setHelpFilePath(pPath);
		}
	}
	
	/** 
	 * Sets whether to show the mucos form in the examination view.
	 * 
	 * @param pShow Whether or not to show the mucos form
	 */	
	public void setShowMucos(String pShow) {
		/* Update property handler */
		propHandler.setShowMucos(pShow);
	}
	
	/** 
	 * Sets what language to use in MiniMed.
	 * 
	 * @param pLanguage What language to use in MiniMed
	 */	
	public void setLanguage(String pLanguage) {
		propHandler.setLanguage(pLanguage);
	}
	
	
	/* ***************************************************************
	 *  Getter methods for Properties module
	 * ***************************************************************/
	
	/** 
	 * Gets the path to the form template file.
	 * 
	 * @return The path to the form template file
	 */	
	public String getFormTemplatePath() {
		return propHandler.getFormTemplatePath();
	}

	/** 
	 * Gets the path to the term definitions file.
	 * 
	 * @return The path to the term definitions file
	 */	
	public String getTermDefinitionsPath() {
		return propHandler.getTermDefinitionsPath();
	}
	
	/** 
	 * Gets the path to the term values file.
	 * 
	 * @return The path to the term values file
	 */	
	public String getTermValuesPath() {
		return propHandler.getTermValuesPath();
	}
	
	/** 
	 * Gets the path to the examination save directory.
	 * 
	 * @return The path to the exam save dir
	 */	
	public String getExamSaveDir() {
		return propHandler.getExamSaveDir();
	}
	
	/** 
	 * Gets the path to the examination database directory.
	 * 
	 * @return The path to the exam database dir
	 */	
	public String getExamDatabaseDir() {
		return propHandler.getExamDatabaseDir();
	}
	
	/** 
	 * Gets the path to the inbox dir.
	 * 
	 * @return The path to the inbox dir
	 */	
	public String getInboxDir() {
		return propHandler.getInboxDir();
	}
	
	/** 
	 * Gets the path to the help file.
	 * 
	 * @return The path to the help file
	 */	
	public String getHelpFilePath() {
		return propHandler.getHelpFilePath();
	}
	
	/** 
	 * Gets the setting for whether or not to show the mucos form in the examination view.
	 * 
	 * @return Whether or not the mucos form should be visible in the exam view
	 */	
	public String getShowMucos() {
		return propHandler.getShowMucos();
	}
	
	/** 
	 * Gets the revision of MiniMed being used.
	 * 
	 * @return The revision of MiniMed being used
	 */		
	public String getMinimedRevision() {
		return propHandler.getMinimedRevision();
	}
	
	/** 
	 * Gets the language currently being used in MiniMed.
	 * 
	 * @return The language currently being used in MiniMed
	 */	
	public String getLanguage() {
		return propHandler.getLanguage();
	}
	
	/** 
	 * Gets all the languages available in MiniMed.
	 * 
	 * @return All languages available in MiniMed
	 */	
	public String[] getAllLanguages() {
		return propHandler.getAllLanguages();
	}
	
	/**
	 * Provides internationalization support for MiniMed. The method
	 * takes an english character string as argument and returns the 
	 * corresponding string for the active language. If a matching string
	 * for the active language can not be found, the english string
	 * is returned and used instead. 
	 * 
	 * @param pStr The string to translate
	 * @return The translated version of pStr
	 */	
	public String i18n(String pStr) {
		return propHandler.i18n(pStr);
	}
	
	/**
	 * Inits the TreeFileHandler and sets working directory to the examSave directory 
	 * @throws IOException
	 */
	public TreeFileHandler getSaveTreeFileHandler() throws IOException{
		if ( getExamSaveDir() == null  || !(new File(getExamSaveDir())).isDirectory()) {
			throw new IOException("No save dir set");
		}
		if ( saveTreeFileHandler == null ) {
			saveTreeFileHandler = new TreeFileHandler(getExamSaveDir());
		}
		return saveTreeFileHandler;
	}
	
	/**
	 * Inits the TreeFileHandler and sets working directory to the examDatabase directory 
	 * @throws IOException
	 */
	public TreeFileHandler getDatabaseTreeFileHandler() throws IOException{
		if ( getExamDatabaseDir() == null  || !(new File(getExamDatabaseDir())).isDirectory()) {
			throw new IOException("No database dir set!");
		}
		if ( databaseTreeFileHandler == null ) {
			databaseTreeFileHandler = new TreeFileHandler(getExamDatabaseDir());
		}
		return databaseTreeFileHandler;
	}
	
	/**
	 * Inits the TreeFileHandler and sets working directory to the inbox directory
	 * @throws IOException
	 */
	public TreeFileHandler getHtmlTreeFileHandler() throws IOException{
		if ( !(new File(getInboxDir())).isDirectory() || getInboxDir() == null ) {
			throw new IOException("No inbox dir set!");
		}
		return new TreeFileHandler(getInboxDir());
	}
	
	/**
	 * A function used by the "edit old examination as new"-functionality, 
	 * which enters term values from an old examination file with the 
	 * terms specified in the settings file.
	 * @param file the old examination file, fileHandler a treeFileHandler
	 * @return A Tree object with the PID, NAME, PCODE and place of birth.
	 */
	public ExaminationValueContainer getExaminationFromFile(File file, TreeFileHandler fileHandler)
				throws IOException{
		ExaminationValueContainer examValues = new ExaminationValueTable();	
		Tree tree = fileHandler.loadTreeFile(file);
		
		String[] values;
		String term = "";
		String[] termList = propHandler.getTermsFromIni();
		
		for ( int i = 0; i< termList.length ; i++ ) {
			term = termList[i];
			values = tree.getValuesOfNodesNamed(term);
			if(values.length > 0){
				examValues.addValue(term, values[0]);
			}
			
		}
				
		return examValues;
	}
	
	/**
	 * Disposes the current TreeFileHandler for the save path, by setting it to null, and running
	 * the garbage collector. For this method to work as intended, all references to objects obtained
	 * with getSaveTreeFileHandler() must be nulled prior to calling this function.
	 *
	 */
	public void disposeSaveTreeFileHandler(){
		saveTreeFileHandler = null;
		System.gc();
	}
	/**
	 * Disposes the current TreeFileHandler for the database path, by setting it to null, and running
	 * the garbage collector. For this method to work as intended, all references to objects obtained
	 * with getDatabaseTreeFileHandler() must be nulled prior to calling this function.
	 *
	 */
	public void disposeDatabaseTreeFileHandler(){
		saveTreeFileHandler = null;
		System.gc();
	}

	/**
	 * Saves the examination that´s with values stored in a tree structure
	 * @param saveTree
	 * @throws IOException
	 */
	public void saveExamination(Tree saveTree) throws IOException  {
		/* Security checks */
		File saveDir = new File(getExamSaveDir());
		if ( !saveDir.exists() && !saveDir.isDirectory() ) {
			throw new IOException("No default path set");
		}
		/* Init the treefilehandler */
		getSaveTreeFileHandler();
		/* Inits */
		File writeFile;
		date = new Date();
		String shortDate = MinimedConstants.TREEFILENAME_DATE_FORMAT.format(date);;
		String longDate;
		String treeFileName;
		String pCode = "";
		String constructedPCode = "";
		Vector pCodeList = new Vector();
		
		/* Get the pid from the input tree */
		String[] pids = saveTree.getValuesOfNodesNamed(MinimedConstants.PID_TERM_NAME);
		String pid;
		if ( pids.length > 0 ) {
			pid = pids[0];
		}
		else{
			throw new IOException("No PID term in the tree");
		}
				
		/* If new examination */
		if( treeFile == null ) {
			/* Init new values */
			longDate = MinimedConstants.TREEFILE_DATUM_FIELD_DATE_FORMAT.format(date);
			treeFileName = MinimedConstants.SAVEFILE_PREFIX + shortDate + MinimedConstants.MVD_TREE_FILE_ENDER;
			writeFile = new File(getExamSaveDir(),treeFileName);
			constructedPCode = shortDate;
			
			/*List all the tree files in Save dir*/
			TreeFileFilter treeFileFilter = new TreeFileFilter();
			File[] files = (new File(getExamSaveDir())).listFiles(treeFileFilter);
			
			/* Check if there is any other saved examinations containing this examinations pid,
			 * if so use that pcode in this examination too.
			 */
			Tree otherTree = new TreeBranch("other");
			String otherpid = "";
			String otherpcode = "";
			for ( int i = 0 ; i<files.length ; i++ ) {
				otherTree = saveTreeFileHandler.loadTreeFile(files[i]);
				try {
					otherpid = otherTree.getValuesOfNodesNamed(MinimedConstants.PID_TERM_NAME)[0];
					otherpcode = otherTree.getValuesOfNodesNamed(MinimedConstants.PCODE_TERM_NAME)[0];
				} catch(Exception e){};
				pCodeList.add(otherpcode);
				if ( otherpid.equals(pid) ) {
					pCode = otherpcode;
				}
			}
			/* If no pcode found in the other tree file */
			if ( pCode == "" ) {
				pCode = constructedPCode;
				/*Check if pCode allready exists*/
				Iterator pCodeIterator = pCodeList.listIterator();
				while ( pCodeIterator.hasNext() ) {
					if ( shortDate == pCodeIterator.next() ) {
						/* The pCode needs to be unique */
						throw new IOException("The constructed P-Code already exists");
					}
				}
			}
		}
		else {
			/* Use stored values */
			writeFile = treeFile;
			Tree oldTree = saveTreeFileHandler.loadTreeFile(treeFile);
			shortDate = oldTree.getValue();
			try {
				longDate = oldTree.getValuesOfNodesNamed(MinimedConstants.DATE_TERM_NAME)[0];
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new IOException("No date in the existing file");
			}
			treeFileName = writeFile.getName();
			try {
				pCode = oldTree.getValuesOfNodesNamed(MinimedConstants.PCODE_TERM_NAME)[0];
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new IOException("No PID in the existing file");
			}
		}
				
		/* Standard valueing */		
		saveTree.setValue(shortDate);
		
		/* P-Code node */
		TreeBranch pCodeBranch = new TreeBranch(MinimedConstants.PCODE_TERM_NAME);
		TreeLeaf pCodeLeaf = new TreeLeaf(pCode);
		pCodeBranch.addChild(pCodeLeaf);
		
		/* Concrete identification */
		TreeBranch konkretIdentifikationBranch = new TreeBranch(MinimedConstants.CONCRETE_ID_TERM_NAME);
		TreeLeaf fileNameLeaf = new TreeLeaf(treeFileName);
		konkretIdentifikationBranch.addChild(fileNameLeaf);
		
		/* Date node */
		TreeBranch datumBranch = new TreeBranch(MinimedConstants.DATE_TERM_NAME);
		datumBranch.addChild(new TreeLeaf(longDate));
		
		/* Add to the root node */
		saveTree.addChild(konkretIdentifikationBranch);
		saveTree.addChild(datumBranch);	
		saveTree.addChild(pCodeBranch);
		
		/* Standard outputstream init*/		
		FileOutputStream fos = new FileOutputStream(writeFile);
		OutputStreamWriter osw = new OutputStreamWriter(fos, MinimedConstants.LATIN_ENC);
		
		/* Write the examination tree to file */
		osw.write(saveTree.toString()); 
		osw.flush();
		osw.close();
		
		/* If written successfully then set current working file to the written file */
		treeFile = writeFile;
	}
	
	
	/**
	 * Saves set properties to disk in a file name determined by
	 * MinimedConstants.PROPERTIES_FILE).
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void saveProperties() throws FileNotFoundException, IOException {
		propHandler.saveProperties();
	}

	/**
	 * Must be called before program is closed so that the program can
	 * do a final cleanup (atm, saving properties to disk).
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void shutdown() throws FileNotFoundException, IOException {
		propHandler.saveProperties();
	}
	
	/**
	 * A reset function, seting the current used treefile to none.
	 */
	public void flush(){
		treeFile = null;
		currentExaminationValueContainer = null;
	}
	
	/**
	 * This class is needed for filtering tree files when listing
	 */
	private class TreeFileFilter implements FilenameFilter {
    	public boolean accept(File dir,String name) {
    		String a = "tree";
    		return name.toLowerCase().endsWith(a.toLowerCase());
    	}
    }
		
}
