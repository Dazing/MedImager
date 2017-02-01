/*
 * $Id: DatahandlingHandler.java,v 1.6 2004/11/24 15:18:35 lindahlf Exp $
 *
 * Created on den 2 oktober 2002, 09:58
 *
 */

package medview.formeditor.data;

import java.awt.*;
import java.util.*;
import java.io.*;

import medview.common.data.*;

import medview.datahandling.*;
import medview.datahandling.examination.*;
import medview.datahandling.examination.tree.*;
import medview.datahandling.images.*;

import medview.formeditor.tools.*;
import medview.formeditor.models.*;

import misc.domain.*;
import misc.foundation.io.*;

/**
 *
 * The DatahandlingHandler acts as a cover for medview.datahandling.MedViewDataHandler.
 *
 * @author  nader
 *
 */

public class DatahandlingHandler implements MedViewLanguageConstants{

    private static DatahandlingHandler instance;

    private MedViewDataHandler mVDH;

    /** Creates a new instance of DatahandlingHandler */
    private DatahandlingHandler() {
	mVDH = MedViewDataHandler.instance();
    }

    public static DatahandlingHandler getInstance() {
	if (instance == null){
	    instance = new DatahandlingHandler();
	}
	return instance;
    }

    public void setProperty( String aKey, String aVal ){
	mVDH.setUserStringPreference( aKey, aVal, this.getClass() );
    }

    public String getProperty(String aKey){
	return mVDH.getUserStringPreference(aKey, null, this.getClass());
    }

    public void setTermDefinitionLocation(String aVal) {
	mVDH.setTermDefinitionLocation(aVal) ;
    }

    public void setTermValueLocation(String aVal) {
	mVDH.setTermValueLocation(aVal) ;
    }

    public String getTermDefinitionLocation() {
	return mVDH.getTermDefinitionLocation();
    }

    public String getTermValueLocation() {
	return mVDH.getTermValueLocation();
    }

    public void changeLanguage(String setLang) {
	try {
	    mVDH.changeLanguage(setLang);
	}
	catch (LanguageException e) {
	    System.err.print("Could not load specified language ");
	    System.err.print("(" + setLang + ") - will use default ");
	    System.err.print("language instead.");
	}
    }

    public String getLanguageString(String aKey) {
	return mVDH.getLanguageString(aKey);
    }

    public String[] getAvailableLanguages() {
	return mVDH.getAvailableLanguages();
    }

    public boolean checkPCode(String pCode){
	// Old P-code class checked for null
	if (pCode != null) {
	    return mVDH.validates(pCode);
	}
	return false;
    }

    public String[] getPresets(String termName) {

	try {
	    return mVDH.getValues(termName);
	}
	catch (Exception e) {
	    //Ut.prt("getPresets method in ExaminationIO raise exception " + e.getMessage());
	    return null;
	}
    }

    public void writeTerms(ExaminationModel model){
	/* Does not compile - =T 03-11-10

	CategoryModel[] categories = model.getCategories();

	for (int cat = 0; cat < categories.length; cat++) {
	    Ut.prt("Category " + cat + ": " + categories[cat].getTitle());

	    InputModel[] inputs = categories[cat].getInputs();
	    for (int input = 0; input < inputs.length; input++) {

		if( InputModel.INPUT_TYPE_TEXTAREA == (inputs[input].getType()) ) {
		    FieldModel fm = (FieldModel) inputs[input];
		    String termName = inputs[input].getName();
		    writeTerm(termName,fm.getFieldType());

		    String[] values = inputs[input].getValues();
		    // Ut.prt("Values: " + values.length);

		    for (int val = 0; val < values.length; val++) {
			writeValue(termName, values[val]);
		    }
		}
	    }
	}
	*/
    }

    public void writeValue(String pTerm,String pValue){
	try{
	    mVDH.addValue(pTerm, pValue);
	}
	catch(Exception e){
	    Ut.prt("Error in writeValue  " + e.getMessage());
	    e.printStackTrace();
	}
    }

    public void removeValue(String pTerm,String pValue){
	try{
	    mVDH.removeValue(pTerm, pValue);
	}
	catch(Exception e){
	    Ut.prt("Error in removeValue  " + e.getMessage());
	    e.printStackTrace();
	}
    }

    /*
     *package medview.datahandling;
     *public class ParsedTermDataHandler implements TermDataHandler
     *
     *public void addTerm(String term, int type) throws
		ValueLocationNotFoundException, DefinitionLocationNotFoundException,
		CouldNotParseException, InvalidTypeException{
     *
		if (!isValidTermType(type)) { throw new InvalidTypeException(type + ""); }
		if (termExists(term)) { removeTerm(term); }
     *
		HashMap termDefMap = readTermDefinitionHashMap();
		termDefMap.put(term, convertType(type));
		writeTermDefinitionHashMap(termDefMap);
	}
     */


    public void addTerm(String term, int newType) throws ValueLocationNotFoundException,
    DefinitionLocationNotFoundException, CouldNotParseException,  InvalidTypeException, IOException {

	int oldType = 0;

	if (mVDH.termExists(term)) {
	    try{
		oldType = mVDH.getType(term);
	    }
	    catch(NoSuchTermException e){
	    }
	    if(oldType == newType) return;


	    if ((newType == TermDataHandler.MULTIPLE_TYPE) && (oldType == TermDataHandler.REGULAR_TYPE)){
		// change type only
	    }
	    else if( (oldType == TermDataHandler.MULTIPLE_TYPE) && ( newType == TermDataHandler.REGULAR_TYPE)){
		// change type only
	    }
	    else{
		mVDH.addTerm( term, newType);
	    }
	}

	else{ // not exist
	    mVDH.addTerm( term, newType);
	}
    }

    public void writeTerm(String pTerm,int pType){
	/* Does not compile - OT 03-11-10
	String typeName = null;
	try{
	    switch (pType) {

		case FieldModel.FIELD_TYPE_INTERVAL:
		    typeName = "  min =  TYPE_INTERVAL  frd =  INTERVAL_TYPE   typenr = "  + pType + "  term = " + pTerm;
		    addTerm(pTerm, TermDataHandler.INTERVAL_TYPE);
		    break;
		case FieldModel.FIELD_TYPE_SINGLE:
		    typeName = "  min =  TYPE_SINGLE  frd =  REGULAR_TYPE   typenr = "  + pType + "  term = " + pTerm;
		    addTerm(pTerm, TermDataHandler.REGULAR_TYPE);
		    break;
		case FieldModel.FIELD_TYPE_QUESTION:
		    typeName = "  min =  TYPE_QUESTION  frd =  REGULAR_TYPE   typenr = "  + pType + "  term = " + pTerm;
		    addTerm(pTerm, TermDataHandler.REGULAR_TYPE);
		    break;
		case FieldModel.FIELD_TYPE_MULTI:
		    typeName = "  min =  TYPE_MULTI  frd =  MULTIPLE_TYPE   typenr = "  + pType + "  term = " + pTerm;
		    addTerm(pTerm, TermDataHandler.MULTIPLE_TYPE);
		    break;
		case FieldModel.FIELD_TYPE_NOTE:
		    typeName = "  min =  TYPE_NOTE  frd =  FREE_TYPE   typenr = "  + pType + "  term = " + pTerm;
		    addTerm(pTerm, TermDataHandler.FREE_TYPE);
		    break;
		default:
		    break;
	    }
	}
	catch(Exception e){
	    Ut.prt("Error in writeterm  " + typeName + " msg = " + e.getMessage());
	    e.printStackTrace();
	}
	*/
    }
    /*
    public void writeTermOld(String pTerm,int pType){

	String typeName = null;
	try{
	    switch (pType) {

		case FieldModel.FIELD_TYPE_INTERVAL:
		    typeName = "  min =  TYPE_INTERVAL  frd =  INTERVAL_TYPE   typenr = "  + pType + "  term = " + pTerm;
		    MedViewDataHandler.instance().addTerm(pTerm, TermDataHandler.INTERVAL_TYPE);
		    break;
		case FieldModel.FIELD_TYPE_SINGLE:
		    typeName = "  min =  TYPE_SINGLE  frd =  REGULAR_TYPE   typenr = "  + pType + "  term = " + pTerm;
		    MedViewDataHandler.instance().addTerm(pTerm, TermDataHandler.REGULAR_TYPE);
		    break;
		case FieldModel.FIELD_TYPE_QUESTION:
		    typeName = "  min =  TYPE_QUESTION  frd =  REGULAR_TYPE   typenr = "  + pType + "  term = " + pTerm;
		    MedViewDataHandler.instance().addTerm(pTerm, TermDataHandler.REGULAR_TYPE);
		    break;
		case FieldModel.FIELD_TYPE_MULTI:
		    typeName = "  min =  TYPE_MULTI  frd =  MULTIPLE_TYPE   typenr = "  + pType + "  term = " + pTerm;
		    MedViewDataHandler.instance().addTerm(pTerm, TermDataHandler.MULTIPLE_TYPE);
		    break;
		case FieldModel.FIELD_TYPE_NOTE:
		    typeName = "  min =  TYPE_NOTE  frd =  FREE_TYPE   typenr = "  + pType + "  term = " + pTerm;
		    MedViewDataHandler.instance().addTerm(pTerm, TermDataHandler.FREE_TYPE);
		    break;
		default:
		    break;
	    }
	}
	catch(Exception e){
	    Ut.prt("Error in writeterm  " + typeName + " msg = " + e.getMessage());
	    e.printStackTrace();
	}

    }
    */

    public void setExaminationDataLocation(String path) throws InvalidDataLocationException {
	// System.out.println("Datahandlinghandler: setting examinationdatalocation to " + path);
	// System.out.println("Currentdatahandler = " + mVDH.getClass());
	mVDH.setExaminationDataLocation(path);
    }

    public ExaminationIdentifier getExaminationIdentifier(Tree t) throws 
	    CouldNotParseDateException, CouldNotConstructIdentifierException {
	    return MedViewUtilities.constructExaminationIdentifier(t);
    }

    public ExaminationIdentifier createExaminationIdentifier(String pc, Date d) {
	return new MedViewExaminationIdentifier(pc, d);
    }

    public Tree createExaminationTree(String pc, Date d) {
	//return ExaminationTreeFactory.createExaminationTree(pc, d);

	return ExaminationTreeFactory.createExaminationTree(pc, d, true);	// Fredrik
    }
}
