package medview.meduweb.data;

import medview.datahandling.*;
import medview.medrecords.tools.ExtensionFileFilter;
import medview.datahandling.examination.*;
import medview.datahandling.examination.tree.*;
import medview.aggregator.*;
import medview.common.template.*;
import medview.common.translator.*;
import medview.common.generator.*;

import javax.swing.text.*;
import java.beans.*;
import java.io.*;
import java.util.*;

//Tests
import java.util.prefs.*;

/**
 * This class is responsible for handling all requests towards the MedView database.
 * It starts by reading all available patient files and storing some of the information into memory, in order to
 * reduce the waiting time when making a lookup for a patient.
 * Its main function lies in getting the text for a patient, using a template and a translator.
 * It also provides a way to search on attributes and values.
 * This class is implemented as a Singelton, so that it can be referenced from all classes within the JVM.
 * Credit goes to Fredrik Lindahl and Nils Erichson for their previous work with the MedView database.
 * @author Jonas Persson, Peter Kristensson, Fredrik Pettersson - project .::mEduWeb::.
 * @version 2.0
 */
public class DataLayer {

    /**
     * The Map containing the MedView data, indexed by term (attribute)
     */
    private HashMap dataTree = null;
    //Cache for examintations files
    private HashMap examinationMap = null;
    private HashMap categoryMap = null;
    //Cache for patient image paths
    private HashMap imageMap = null;
    //Cache for Examination value containers
    private HashMap evcCacheMap = null;
    private HashMap translatorMap = null;
    private HashMap templateMap = null;
    private HashMap removedAttributes = null;
    private HashMap pCodePrefixes = null;

    private File[] files = null;
    private String[] terms;
    
    private MeduwebDataHandler mVDH;
    private medview.meduweb.data.TreeFileHandler tFH;
    private static DataLayer instance = null;

    /** The scheduled time of updating */
    private static long UPDATE_HOUR = 3;

    private String filesep = System.getProperty("file.separator");

    private String treeFilePath = System.getProperty("forest.dir");
    
    private boolean hasRemovedAttributes = false;

	
    private DataLayer() {

	//clean up old stuff
	System.gc();
	mVDH = MeduwebDataHandler.instance();
	tFH = new medview.meduweb.data.TreeFileHandler(null);
	tFH.setExaminationDataLocation(treeFilePath);

	//initialise tree
	makeTree();

	//Start update-task
	Timer updateTimer = new Timer();
	TimerTask updateTask = new UpdateTask();
	long now = System.currentTimeMillis();
	long hour = (now / (1000 * 60 * 60)) % 24 ;
	long minute = (now / (1000 * 60)) % 60;
	if (hour > UPDATE_HOUR) {
	    hour = 27 - hour;
	}
	long timeForFirstUpdate = 120000;//(hour * 60 + minute) * (1000*60);
	long dayInMillis = 1000 * 60 * 60 * 24;
	//Kickstart the update thread
	updateTimer.scheduleAtFixedRate(updateTask,
					timeForFirstUpdate,
					dayInMillis);
    }

   /**
    * This method returns a reference to the DataLayer object.
    * It is a part of the Singleton pattern and should always be used to get reference to this object.
    * @return A reference to the DataLayer object.
    */
    public synchronized static DataLayer getInstance() {
	if (instance == null) {
	    //DataLayer hasn't been used before, it's empty and must be initialised.
	    instance = new DataLayer(); 
	}
	return instance;
    }
    
   /**
    * Gets all examinationValueContainers for the patient. 
    * Requiers that makeTree has been run, since it initialized the list of patient files.
    * @param pcode The pcode for the patient.
    * @return An array of ExaminatoinValueContainers
    */
    private ExaminationValueContainer[] getPatientContainers(String pcode) {
	MeduwebExaminationValueTable[] examinationValueContainers = null;

	ExaminationValueContainer[] outContainers = null;

	if (evcCacheMap == null) {
	    evcCacheMap = new HashMap();
	}

	if (evcCacheMap.get(pcode) == null)  {

	    System.out.println("Remaking containers for: " + pcode);

	    //if there exists files for this patient
	    if ((examinationMap.get(pcode)) != null) {
		//Get patient files
		File[] files = (File[]) examinationMap.get(pcode);
		
		examinationValueContainers = new MeduwebExaminationValueTable[files.length];
		
		for (int i=0; i<files.length; i++) {
			try {
			    examinationValueContainers[i] = new MeduwebExaminationValueTable(tFH.loadTreeFile(files[i]));
			} catch (java.io.IOException ioe) {
				System.err.println(ioe.getMessage());	
			}
		}

		//create one container which contains values form all containers
		//most recent value has precedence
		MeduwebExaminationValueTable[] mainEVCs = new MeduwebExaminationValueTable[1];
		mainEVCs[0] = new MeduwebExaminationValueTable();

		//sort containers
		ArrayList tmpValues = new ArrayList();
		for (int j=0; j<terms.length; j++) {
			tmpValues = new ArrayList();
			for (int i=0; i<examinationValueContainers.length; i++) {
				try {
					tmpValues.addAll(examinationValueContainers[i].getValueVector(terms[j]));
				} catch(NoSuchTermException nste) {
					System.out.println(nste.getMessage());	
				}
			}
			mainEVCs[0].addCollection(terms[j], tmpValues);
		}
		
		evcCacheMap.put(pcode,mainEVCs);
		outContainers = (ExaminationValueContainer[]) mainEVCs;
	    }
	} else {
	    //ExaminationValueContainer[] 
	    outContainers = (ExaminationValueContainer[])evcCacheMap.get(pcode);
	    //examinationValueContainers = new MeduwebExaminationValueTable[tmpContainers.length];
	    //System.arraycopy(tmpContainers,0,examinationValueContainers,0,tmpContainers.length);
	}

	return outContainers;
    }

    public synchronized void flushTemplateMap() {

	templateMap = null;

    }

    /**
     * Gets the text for the pcode, using the given template and translator.
     * @param pcode The patient who's text is to be generated
     * @param template The template to be used
     * @param translator The translator to use
     * @return A text containing the patient info
     */
    public String getPatientText(String pcode, String template, String translator) { 
	//throws NoSuchTemplateException, NoSuchTranslatorException{
	//long startTime = System.currentTimeMillis();
	if (translatorMap == null) {
	    translatorMap = new HashMap();
	}

	if (templateMap == null) {
	    templateMap = new HashMap();
	}


	String outstring = null;

	System.out.print("loading template and translator...");
	//long sTTe = System.currentTimeMillis();
	TemplateModel templateModel = null;
	//Set template to use
	try {
		if (templateMap.get(template) == null) {
			System.out.println("Trying to load: "+ System.getProperty("template.dir") + "\\" +
	    					template + ".xml");
	    		templateModel = mVDH.loadTemplate(System.getProperty("template.dir") + "\\" +
	    					template + ".xml");
	    		templateMap.put(template,templateModel);
		} else {
	    		templateModel = (TemplateModel)templateMap.get(template);
	    		
		}
	} catch(misc.foundation.InvalidVersionException ive) {
		System.out.println("InvalidVersionException: " + ive.getMessage());
	} catch(misc.foundation.io.CouldNotLoadException cnle) {
		System.out.println("CouldNotLoadException: " + cnle.getMessage());	
	}
	

	medview.meduweb.data.MeduwebTranslatorModel translatorModel = null;
	// retrieve the translator model to be used in the generation
	try {
		if (translatorMap.get(translator) == null) {
			System.out.println("Trying to load: "+System.getProperty("translator.dir") + "\\" +
	    			translator + ".xml");
	    		translatorModel = mVDH.loadTranslator(System.getProperty("translator.dir") + "\\" +
	    			translator + ".xml");
	    		translatorMap.put(translator,translatorModel);
	    		
		} else {
	    		translatorModel = (MeduwebTranslatorModel)translatorMap.get(translator);
		}
	} catch(misc.foundation.io.CouldNotLoadException cnle) {
		System.out.println("CouldNotLoadException: " + cnle.getMessage());	
	}
	//long eTTr = System.currentTimeMillis();	
	System.out.println("done");	
	/*System.out.println("Template loading: "+ (eTTe - sTTe));
	System.out.println("Translator loading: "+ (eTTr - sTTr));*/
	System.out.print("loading EVCs...");
	//create array of examination value containers
	try {
		ExaminationValueContainer[] examinationValueContainers = getPatientContainers(pcode);
		System.out.println("done, length was: " + examinationValueContainers.length);
	
		//Use the first examination and the first section to generate the first string
		//long sTsection = System.currentTimeMillis();
		String[] section1 = new String[1];

		section1 = templateModel.getAllContainedSections();

		String[] section2 = new String[1];
		section2[0] = templateModel.getSectionName(1);
		//long eTsection = System.currentTimeMillis();
		//System.out.println("Generate first string: " + (eTsection - sTsection));
		//ExaminationValueContainer[] firstContainer = new ExaminationValueContainer[1];
		//firstContainer[0] = examinationValueContainers[0];
		//examinationValueContainers[0] = null;

		long time = System.currentTimeMillis();
		System.out.print("generating text...");
		StyledDocument document;
		try {
		    //SummaryGenerator.setDebug(true);
		    	long sTsummary = System.currentTimeMillis();
		    	MeduwebGeneratorEngineBuilder gEB = new MeduwebGeneratorEngineBuilder();
		    	gEB.buildSections(section1); //section1 or section2 ???
		    	gEB.buildTemplateModel(templateModel);
		    	gEB.buildTranslatorModel(translatorModel);
		    	gEB.buildValueContainers(examinationValueContainers);
		    	//FULHACK
		    	ExaminationIdentifier[] eis = new ExaminationIdentifier[1];
		    	eis[0] = new MedViewExaminationIdentifier(pcode);
		    	gEB.buildIdentifiers(eis);
		        document = gEB.getEngine().generateDocument();
			long eTsummary = System.currentTimeMillis();
			//System.out.println("generateSummary: " + (eTsummary - sTsummary));
		    outstring = document.getText(0,document.getLength());
		    //System.out.println(outstring);
		} catch (BadLocationException e) {
		} catch (CouldNotGenerateException cnge) {
			System.err.println(cnge.getMessage());
		} catch (CouldNotBuildEngineException cnbee) {
			cnbee.printStackTrace();	
		} catch (FurtherElementsRequiredException fere) {
			fere.printStackTrace();	
		}
	
		System.out.println("done, it took: " + (System.currentTimeMillis() - time) + " ms");
	} catch (NullPointerException npe) {
		outstring = "This case coudln't be found. <br> Probably because of an old link or that the case has been removed from the database. <br><br>" +
				"(A NullPointerException occured in medview.meduweb.data.DataLayer).";	
	}
	if (outstring == null) {
	    outstring = "Det bidde fel nagonstans.";
	} else if(outstring.startsWith("$")) {
		outstring = outstring.substring(1);
	}
	
	return outstring;
    }

    private void makeTree() {
	String[] pcodes = null;

	//start by getting all terms
	System.out.println("Getting terms");
	long tid = System.currentTimeMillis();
	try {
		terms = mVDH.getTerms();
	} catch(DefinitionLocationNotFoundException dlnfe) {
		dlnfe.printStackTrace();	
	} catch(CouldNotParseException cnpe) {
		cnpe.printStackTrace();	
	}
	System.out.println("Det tog: " + (System.currentTimeMillis() - tid));

	System.out.println("Getting codes");
	tid = System.currentTimeMillis();
	pcodes = getPatients();
	System.out.println("Det tog: " + (System.currentTimeMillis() - tid));

	//construct a new tree
	dataTree = new HashMap();
	System.out.println("Making tree from files");
	tid = System.currentTimeMillis();
	for (int i=0; i<pcodes.length; i++) {
	    ExaminationValueContainer[] examinationValueContainers = getPatientContainers(pcodes[i]);
	    System.out.println("evc length:" + examinationValueContainers.length + " (pcode: " +
				pcodes[i] + ")");
	    //try {
		if(pcodes[i].equals("B00619480")) {
			System.out.println("Skipped insertion ...");
		} else {	
	    		insertContainers(examinationValueContainers);
		
		}
	    //} catch(InvalidPCodeException ipce) {
		//ipce.printStackTrace();
	    //}
	}
	System.out.println("Det tog: " + (System.currentTimeMillis() - tid));
	
    }
	
    private void insertContainers(ExaminationValueContainer[] examinationValueContainers) {
	//throws InvalidPCodeException {
	//long startTime = System.currentTimeMillis();
	try {
	//lets search the ExaminationValueContainer and put all the values in there in the tree
	if (examinationValueContainers != null) {
	    String pcode = "";
	    try {
	      	pcode = (examinationValueContainers[0].getValues("P-code"))[0];
	    } catch (NoSuchTermException nste) {
	    	System.out.println(nste.getMessage());	
	    }
	
	    //check if pcode has right size
	    if (pcode != null && pcode.length() == 9) {
		for (int ctr=0; ctr<examinationValueContainers.length; ctr++) {
			
		    HashMap tree;
		    //loop through all available attributes
		    for (int j=0; j<terms.length; j++) {
			String currentTerm = terms[j];
			//System.out.print(" Currentterm: " + terms[j]);
			
			//get the sub-tree matching the attribute
			tree = (HashMap) dataTree.get(currentTerm);
			//System.out.print(" Got term. ");
			
			//Get the patient's values for the attribute
			String[] values = new String[0];
			try {
				values = examinationValueContainers[ctr].getValues(currentTerm);
			} catch (NoSuchTermException nste) {
	    			System.out.println(nste.getMessage());	
	    		}
			//System.out.print(" Got values" );
			//if attribute doesn't exist, create it
			if (tree==null) {
			    //System.out.print(" tree = null ");
			    tree = new HashMap();
			    if (values!=null) {
				//System.out.print(" values != null ");
				//add all values for the patient
				for (int k=0; k<values.length; k++) {
				    ArrayList tmp = new ArrayList();
				    tmp.add(pcode);
				    tree.put(values[k], tmp);
				}
				
				//reinsert into main tree
				dataTree.put(currentTerm, tree);
				//System.out.print(" Reinserted into main tree. ");
			    }
			    
			} else {
			    if (values != null) {
				//System.out.print(" values != null (2) ");
				//add all values for the patient
				for (int k=0; k<values.length; k++) {	
				    
				    //Add patient at end of pcode-list
				    if (tree.get(values[k]) != null) {
					ArrayList tmpList = (ArrayList) tree.get(values[k]);
					if (! tmpList.contains(pcode)) {
					    tmpList.add(pcode);
					}
					tree.put(values[k],tmpList);
				    } else {
					ArrayList tmpList = new ArrayList();
					tmpList.add(pcode);
					tree.put(values[k], tmpList);
				    }
				}
	
			    }
			    
			    //reinsert into main tree
			    dataTree.put(currentTerm, tree);
				//System.out.print(" Reinserted into main tree. (2) ");
			}
		    }
			//System.out.println("Done!");
		}

		//insert special values, such as Gender and age
		if (dataTree.get("Gender") == null) {
		    dataTree.put("Gender", new HashMap());
		}
		HashMap genderMap = (HashMap)dataTree.get("Gender");
		String gender = "Male";
		if (pcode.charAt(8) == '0') {
		    gender = "Female";
		}
		if (genderMap.get(gender) == null) {
		    ArrayList list = new ArrayList();
		    list.add(pcode);
		    genderMap.put(gender, list);
		} else {
		    ArrayList list = (ArrayList)genderMap.get(gender);
		    if (! list.contains(pcode)) {
			list.add(pcode);
			genderMap.put(gender, list);
		    }
		}
		if (dataTree.get("Age") == null) {
		    dataTree.put("Age", new HashMap());
		}

		HashMap ageMap = (HashMap)dataTree.get("Age");
		String age = "0";
		try {
			age = String.valueOf(mVDH.getAge(pcode));
		} catch(InvalidPCodeException ipce) {
			System.out.println(ipce.getMessage());	
		}
		if (ageMap.get(age) == null) {
		    ArrayList ageList = new ArrayList();
		    ageList.add(pcode);
		    ageMap.put(age,ageList);
		} else {
		    ArrayList ageList = (ArrayList)ageMap.get(age);
		    if (! ageList.contains(pcode)) {
			ageList.add(pcode);
			ageMap.put(age,ageList);
		    }
		}
	    }
	}
	} catch (NullPointerException npe7) {
		System.out.println(npe7.getMessage());
	
	}
	//long endTime = System.currentTimeMillis();
	//System.out.println("insertContainers: " + (endTime - startTime));
    }


   /**
    * Searches for patients who have a mathing set of attribute and value.
    * @param attribute The attribute to search for.
    * @param value The value to match on.
    * @return ArrayList of strings, containing the pcodes which have the matching attribute-value.
    */
    public ArrayList searchAttribute(String attribute, String value) {
	ArrayList out = new ArrayList();

	//Check if value starts with a '-' sign
	//If so, remove it. Kinda risky...
	while (value != null &&
	    value.length() > 0 &&
	    value.charAt(0) == '-') {
	    value = value.substring(1,value.length()-1);
	}
	try {
		//Check if value is a category
		if (categoryMap != null && 
		    categoryMap.get(attribute) != null &&
		    ((HashMap)categoryMap.get(attribute)).get(value) != null) {
		    ArrayList subValues = (ArrayList)((HashMap)categoryMap.get(attribute)).get(value);
		    Iterator i = subValues.iterator();
		    while (i.hasNext()) {
			Iterator tmpPatientsI = searchAttribute(attribute, (String)i.next()).iterator();
			if(tmpPatientsI != null) {
				while (tmpPatientsI.hasNext()) {
			    		String tmpPat = (String)tmpPatientsI.next();
			    		if (! out.contains(tmpPat)) {
						out.add(tmpPat);
			    		}
				}
			}
		    }
		} else {
		    if (attribute != null &&
			value != null) {

			//hämta ut sub-trädet som matchar attributet
			HashMap subTree = (HashMap) dataTree.get(attribute);
				
			if (subTree != null) {
			    out = new ArrayList((ArrayList) subTree.get(value));
			}
		    }	
		}
	} catch (NullPointerException npe) {
	} 
	//System.out.println(out.size());

	return sort(out);
    }

    private void updateTree() {
	
	//Get all patient files, if length differs we've got new one's
	File[] currentFiles = listTreeFiles(treeFilePath);
	if (currentFiles.length != files.length) {
	    for (int i=0; i < currentFiles.length; i++) {
		int j;
		for (j=0; j < files.length && currentFiles[i].compareTo(files[j]) != 0; j++) {}
		if (j == files.length) {
		    try {
		    ExaminationValueContainer[] newContainer = new ExaminationValueContainer[1];
		    newContainer[0] = new MeduwebExaminationValueTable(tFH.loadTreeFile(currentFiles[i]));
		    String p_code = (newContainer[0].getValues("P-code"))[0];
		    addPatientFile(p_code, currentFiles[i]);
		    addPatientContainer(p_code, (MeduwebExaminationValueTable)newContainer[0]);
		    insertContainers(newContainer);
		    } catch (Exception e) {
			System.err.println("Got an exception during update, not good. ignorind data from this file.");
			e.printStackTrace();
			//If things go wrong here, we don't want the whole
			//Thread to go awry
		    }
		}
	    }
	    files = currentFiles;
	}
    }

   /**
    * Updates a category, creates a new mapping if there is no previous category with the corresponding name.
    * @param attribute The attribute for which the category is valid.
    * @param category The name of the category to update/create.
    * @param values ArrayList of Strings, the values to map to this category.
    */
    public void updateCategory(String attribute, String category, ArrayList values) {
	if (categoryMap == null) {
	    categoryMap = new HashMap();
	}
	HashMap tmpMap = new HashMap();
	//If mappings exist for this attribute
	if (categoryMap.get(attribute) != null) {
	    tmpMap = (HashMap)categoryMap.get(attribute);
	}
	tmpMap.put(category, new ArrayList(values));
	categoryMap.put(attribute, tmpMap);
    }

    /**
     * Removes a given category.
     * @param attribute the attribute to match on.
     * @param name the name of the category.
     */
    public void removeCategory(String attribute, String name) {
	if (categoryMap != null &&
	    categoryMap.get(attribute) != null) {
	    HashMap subMap = (HashMap)categoryMap.get(attribute);
	    Object o = subMap.remove(name);
	}
    }

    /**
     * Clears all categories.
     */
    public void clearCategories() {
	categoryMap = null;
    }

    public ArrayList listCategories(String attribute) {
    		ArrayList out = new ArrayList();
    		if(categoryMap != null &&
    			categoryMap.get(attribute) != null) {
    				HashMap categories = (HashMap)categoryMap.get(attribute);
	   			out = sort(categories.keySet());
	   	}
	   	return out;
    }	

   /**
    * Lists all values for an attribute.
    * Not all possible MedView values are returned here. 
    * Only the ones which have been used by at least one patient.
    * Arranged with first each Category, and their values (inlined with a '-'), 
    * and then all the values which haven't got a category.
    * @param attribute The attribute to list the values for.
    * @return ArrayList of Strings containing the values. 
    */
    public ArrayList listValues(String attribute) {

	ArrayList out = new ArrayList();
	ArrayList used = new ArrayList();

	//Check if we've got categories
	if (categoryMap != null &&
	    categoryMap.get(attribute) != null) {
	    HashMap categories = (HashMap)categoryMap.get(attribute);
	    ArrayList categoryList = sort(categories.keySet());
	    Iterator categoryI = categoryList.iterator();
	    while (categoryI.hasNext()) {
	        String category = (String)categoryI.next();
		out.add(category);
		ArrayList valueList = (ArrayList)categories.get(category);
		ArrayList sortedList = sort(valueList);
		Iterator valueI = sortedList.iterator();
		while (valueI.hasNext()) {
		    String tmp = (String)valueI.next();
		    out.add("-" + tmp);
		    used.add(tmp);
		}
	    }
	}

	HashMap subTree = null;
	subTree = (HashMap) dataTree.get(attribute);

	//Add normal values that aren't in any category
	if (subTree!=null) {
	    Iterator valueI = (sort(new ArrayList(subTree.keySet()))).iterator();
	    while (valueI.hasNext()) {
		String tmp = (String)valueI.next();
		if (! used.contains(tmp)) {
		    out.add(tmp);
		}
	    }
	}
	
	return out;
    }

   /**
    * Lists all values for an attribute.
    * Not all possible MedView values are returned here. 
    * Only the ones which have been used by at least one patient.
    * @param attribute The attribute to list the values for.
    * @return ArrayList of Strings containing the values. 
    */
    public ArrayList listValuesWithoutCategories(String attribute) {

	ArrayList out = new ArrayList();

	HashMap subTree = null;
	subTree = (HashMap) dataTree.get(attribute);

	if (subTree!=null) {
	    Iterator valueI = subTree.keySet().iterator();
	    while (valueI.hasNext()) {
		String tmp = (String)valueI.next();
		out.add(tmp);
	    }
	}
	
	return sort(out);
    }

    /** 
     * Checks whether a given pcode exists or not.
     * @param pcode the patient code to check.
     * @return boolean true if the pcode exists, fasle otherwise.
     */
    public boolean existingPCode(String pcode) {
	return (false || (evcCacheMap.get(pcode) != null));
    }

    /**
     * Removes a specified value for an attribute from the searchtree.
     * The value will no longer show up on any lists, nor will it be searchable.
     * It will however, show up in getPatientText if a template uses it.
     * @param attribute The attribute to remove the value for.
     * @param value The value to remove.
     */
    public void removeValueForAttribute(String attribute, String value) {
	if (dataTree.get(attribute) != null) {
	    Object o = ((HashMap)dataTree.get(attribute)).remove(value);
	}
    }

   /**
    * Lists all attributes.
    * Not all possible MedView attributes are returned here. 
    * Only the ones which have been used by at least one patient.
    * @return ArrayList of Strings containing the attributes.
    */
    public ArrayList listAttributes() {
	ArrayList out = new ArrayList();
	if (dataTree != null) {
	    out = new ArrayList(dataTree.keySet());
	}
	if(!hasRemovedAttributes) {
		Iterator attrI = out.iterator();
		while(attrI.hasNext()) {
			String attr = (String)attrI.next();
			if(listValuesWithoutCategories(attr).isEmpty()) {
				removeAttribute(attr);
			}	
		}
		out = new ArrayList(dataTree.keySet());
		hasRemovedAttributes = true;
	}
	return sort(out);
    }

    public ArrayList listPCodePrefixes() {
    	if(pCodePrefixes == null) {
    		initPCodePrefixes();	
    	}	
    	return new ArrayList(pCodePrefixes.keySet());
    }
    
    public void initPCodePrefixes() {
    	pCodePrefixes = new HashMap();
    	Iterator pCodesI = getPcodes().iterator();
    	String pcode = "";
    	String prefix = "";
    	ArrayList tmp = null;
    	while(pCodesI.hasNext()) {
    		try {
    			pcode = (String)pCodesI.next();
    			prefix = mVDH.getPrefix(pcode);
    			
    			if(pCodePrefixes.containsKey(prefix)) {
    				tmp = (ArrayList)pCodePrefixes.get(prefix);
    				tmp.add(pcode);
    				pCodePrefixes.put(prefix, tmp);
    			} else {
    				tmp = new ArrayList();
    				tmp.add(pcode);
    				pCodePrefixes.put(prefix, tmp);	
    			}
    		} catch (InvalidPCodeException ipce) {
    			System.out.println(ipce.getMessage());	
    		}
    	}
    		
    }

    public ArrayList searchPCodePrefix(String prefix) {
    	if(pCodePrefixes == null) {
    		initPCodePrefixes();	
    	}	
    	ArrayList out = (ArrayList)pCodePrefixes.get(prefix);
    	if(out == null)
    		out = new ArrayList();
    	return out;	
    }

    /**
     * Removes the selected attribute from the searchtree.
     * The attribute will no longer be searchable, but it will show up if a template contains it.
     * @param attribute The attribute to remove.
     */
    public void removeAttribute(String attribute) {
	if (removedAttributes == null) {
	    removedAttributes = new HashMap();
	}
	removedAttributes.put(attribute, dataTree.remove(attribute));
    }

    public void unRemoveAttribute(String attribute) {
	if (removedAttributes != null &&
	    removedAttributes.get(attribute) != null) {
	    if (dataTree == null) {
		dataTree = new HashMap();
	    }
	    dataTree.put(attribute, removedAttributes.remove(attribute));
	}
    }

   /**
    * Gets all images related to a patient.
    * @param pcode the patientcode for the patient.
    * @return ArrayList of strings, pointing to the images for the patient.
    */
    public ArrayList getPatientImages(String pcode) {
	if (imageMap == null) {
	    imageMap = new HashMap();
	}

	ArrayList images = new ArrayList();
	if (imageMap.get(pcode) == null) {
            if ((examinationMap.get(pcode)) != null) {
                //Get patient files
		
                //File[] files = (File[]) examinationMap.get(pcode);

                ExaminationValueContainer[] containers = getPatientContainers(pcode);
		    //new MeduwebExaminationValueTable[files.length];
		/*
                for (int i=0; i<files.length; i++) {
                    containers[i] = new MeduwebExaminationValueTable(EncodeTreeFile.makeTree(files[i]));
                }
		*/
		for (int i=0; i < containers.length; i++) {
		    if (containers[i] != null) {
		    	String[] photos = new String[0];
		    	try {
			 	photos = containers[i].getValues("Photo");
			} catch (NoSuchTermException nste) {
				System.out.println(nste.getMessage());	
			}
			//Only add if examination had photos in it
			if (photos != null) {
			    //Put old and new array together
			    for (int j=0; j<photos.length; j++) {
				String path = photos[j];
				//Tokenize path.
				StringTokenizer pathTokenizer = new StringTokenizer(path, "/");
				String last = "";
				String secondLast = "";
				//Remove all but the last two sections
				while (pathTokenizer.hasMoreTokens()) {
				    secondLast = new String(last);
				    last = pathTokenizer.nextToken();
				}
				String imageURL = new String(secondLast + "/" + last);
				if (! images.contains(imageURL)) {
				    images.add(imageURL);
				}
			    }
			}
		    } else {
			System.out.println("Container " + i + " was null, fuck!");
		    }
		}
		imageMap.put(pcode,images);
	    }
	} else {
	    images = (ArrayList)imageMap.get(pcode);
	}

	return images;//sort(images);
    }

   /**
    * Gets the pcodes for all patients in the database.
    * @return ArrayList of Strings containing pcodes.
    */
    public ArrayList getPcodes() {
	ArrayList out = new ArrayList();
	if (examinationMap != null) {
	    out = new ArrayList(examinationMap.keySet());
	}
	return sort(out);
    }

   //Things stolen from TreeFileHandler:
   /**
    * Gets the p-codes in the database. Also associates all p-codes with their examination-files.
    * Also saves the ExaminationValueContainers for each patient.
    * @return An array of strings containing the pcodes.
    */
    private String[] getPatients() {
        // Look through the treefilepaths for unique patients

        examinationMap = new HashMap();

        // List tree files
	System.out.println(treeFilePath);
        files = listTreeFiles(treeFilePath);
        
        Vector p_codes = new Vector();
        MeduwebExaminationValueTable evc = null;
	if (files != null) {
        for (int i = 0; i < files.length; i++) {
            
	    //mEduWeb version:
	    String p_code = "";
	    try {
		
		evc = new MeduwebExaminationValueTable(tFH.loadTreeFile(files[i]));

		p_code = evc.getValues("P-code")[0];
	    } catch (Exception e) {
		System.out.println("File: " + files[i].getName() + " generated an error");
		System.out.println(e.getMessage());
	    } catch (Error err) {
	    	System.out.println("File: " + files[i].getName() + " generated an error");	
	    	err.printStackTrace();
	    }

		addPatientFile(p_code, files[i]);
		addPatientContainer(p_code, evc);
		
		//Close file
		//fs.close();
            
            if (! (p_codes.contains(p_code))) {
                p_codes.add(p_code);
            }
            
        }
	}
        // Now we have all the p_codes in a vector, return as string array
        String[] codes = new String[p_codes.size()];
        codes = (String[]) p_codes.toArray(codes);
        
        return codes;
    }

    private void addPatientFile(String p_code, File file) {
	//Add this file to the examinationMap
	if (examinationMap.get(p_code) != null) {
	    //Get old files
	    File[] patientFiles = (File[]) examinationMap.get(p_code);
	    //Copy to a new array
	    File[] newPatientFiles = new File[patientFiles.length + 1];
	    System.arraycopy(patientFiles,0,newPatientFiles,0,patientFiles.length);
	    //Insert new file
	    newPatientFiles[patientFiles.length] = file;
	    //Reinsert into hashMap
	    examinationMap.put(p_code, newPatientFiles);
	} else {
	    //Patient doesn't have any previos examinations, add a new entry
	    File[] newPatientFiles = new File[1];
	    newPatientFiles[0] = file;
	    examinationMap.put(p_code, newPatientFiles);
	}
    }

    private void addPatientContainer(String p_code, MeduwebExaminationValueTable evc) {
	if (evcCacheMap == null) {
	    evcCacheMap = new HashMap();
	}

        //Add this file to the evcCacheMap
        if (evcCacheMap.get(p_code) != null) {
            //Get old files, just one file
            MeduwebExaminationValueTable[] oldEVCs = (MeduwebExaminationValueTable[]) evcCacheMap.get(p_code);
            //Add new data to old container
		for (int i=0; i<terms.length; i++) {
			Vector tmpList = new Vector();
			try {
				tmpList = evc.getValueVector(terms[i]);
			} catch (NoSuchTermException nste) {
				System.out.println(nste.getMessage());	
			}
			oldEVCs[0].addCollection(terms[i], tmpList);
		}
            evcCacheMap.put(p_code, oldEVCs);
        } else {
            //Patient doesn't have any previous examinations, add a new entry
            MeduwebExaminationValueTable[] newEVCs = new MeduwebExaminationValueTable[1];
            newEVCs[0] = evc;
            evcCacheMap.put(p_code, newEVCs);
        }
    }

    /**
     * Lists all patient files. 
     * @param path The path to where the .tree files are located.
     * @return An array of patient files.
     */
    private File[] listTreeFiles(String path) {
        
        Vector treeFiles = new Vector();
        
        String[] treeFileExtensions = { "tree","TREE","Tree" };
        ExtensionFileFilter treeFileFilter = new ExtensionFileFilter( treeFileExtensions);
        treeFileFilter.setDescription("Tree files (.tree)");
        
        File[] files = (new File(path)).listFiles(treeFileFilter);
        
        return files;
    }


    public static void destroyMe() {
	instance = null;
	System.gc();
    }

    /**
     * Sorts a given list (of Strings!), returns a new sorted list.
     * @param l The list to sort.
     * @return ArrayList of the same elements as l, sorted in natural order.
     */
    private ArrayList sort(Collection c) {
	ArrayList out = new ArrayList(c);
	Collections.sort(out);
	return out;
    }

    /**
     * This inner class is for updating pusposes, it runs a task scheduled
     * every night which ckecks the available files and insert new files
     * if any new has occured.
     */
    private class UpdateTask extends TimerTask {

	public void run() {
	    updateTree();
	}

    }

}
