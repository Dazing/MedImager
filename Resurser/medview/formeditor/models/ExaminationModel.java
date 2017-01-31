/*
 * $Id: ExaminationModel.java,v 1.2 2003/11/18 00:43:41 oloft Exp $
 *
 * Created on June 15, 2001, 12:18 PM
 *
 */

package medview.formeditor.models;

import java.util.*;
import javax.swing.event.*;

import medview.formeditor.components.*;
import misc.foundation.HashVector;

/**
 *
 * Keeps track of all mCategories of an examination
 *
 * @author  nils
 * @version
 */

public class ExaminationModel extends Object implements ChangeListener{
    // implements ChangeListener att 5/6 2003 to catch change value to inform medrecordFrame
    
    private Vector          mChangeListeners;
    private HashVector      mCategories; // Maps category name to category model
    private String          mTreeFile = null;
    //private MedRecordsFrame mMedRecordsFrame = null;
    private FormEditor      mFormEditor = null;
    
    /** Creates new ValueTabbedPaneModel */
    public ExaminationModel() {
        super();
        // name = new String(in_name);
        mCategories = new HashVector();
        mChangeListeners = new Vector();
        mTreeFile = null;
    }
    public void setTreeFile(String pFileName){
        mTreeFile = pFileName;
    }
    public String  getTreeFile(){
        return mTreeFile;
    }
    
    public boolean containsCategory(String name) {
        return mCategories.containsKey(name);
    }
    
    public CategoryModel getCategory(String name) {
        return (CategoryModel) mCategories.get(name);
    }

    // May not be smart, but it does work
    public String[] getCategoryNames() {
        int cnt = mCategories.size();
        String[] names = new String[cnt];
        CategoryModel[] cats = getCategories();

        for (int i=0; i<cnt; i++) {
            names[i]=cats[i].getTitle();
        }

        return names;
        //String[] names = new String[mCategories.size()];
        //names = (String[]) mCategories.getKeyArray();
        //return names;
    }
    
    public CategoryModel[] getCategories() {
        CategoryModel[] cats = new CategoryModel[mCategories.size()];
        
        if(mCategories != null){
            //Ut.prt("mCategories != null ");
            cats = (CategoryModel[]) mCategories.getValueArray(cats);
            return cats;
        }
        return null;
    }
    /**
     * Add a category
     */
    public void addCategory(CategoryModel catModel) {
        mCategories.put(catModel.getTitle(),catModel);
        catModel.addChangeListener(this);
        fireStateChanged();
    }
    
    /**
     * Reorder a category
     *
     */
    public void moveCategory(CategoryModel catModel, int relativeAdjustment) {
        //System.out.println("Moving category [" + catModel.getTitle() + "]: " + relativeAdjustment);
        mCategories.moveRelative(catModel,relativeAdjustment);
        catModel.removeChangeListener(this);
        fireStateChanged();
    }
    
    /**
     * Remove a category
     */
    public void removeCategory(CategoryModel catModel) {
        mCategories.removeByKey(catModel.getTitle());
        fireStateChanged();
    }
    
    public void clearValues() {
        // Iterate through all mCategories to change all  instances
        mTreeFile = null;
        for (Iterator catIterator =mCategories.iterator(); catIterator.hasNext();) {
            CategoryModel nextCat = (CategoryModel) catIterator.next();
            InputModel[] inputs = nextCat.getInputs();
            
            for (int i = 0; i < inputs.length; i++) {
                InputModel thisInput = inputs[i];
                // if( !(thisInput instanceof PictueChooserModel) )
                thisInput.clear();
            }
        }
    }
    
    /**
     * Set the id_code
     */
    public void setIdcode(String new_pcode) {
        // Iterate through all mCategories to change all  instances
        for (Iterator catIterator = mCategories.iterator(); catIterator.hasNext();) {
            CategoryModel nextCat = (CategoryModel) catIterator.next();
            InputModel[] inputs = nextCat.getInputs();
            
            for (int i = 0; i < inputs.length; i++) {
                InputModel thisInput = inputs[i];
                if(thisInput instanceof FieldModel){
                    FieldModel afModel = (FieldModel)thisInput;
                    
                    if(afModel.getFieldType() == FieldModel.TYPE_IDENTIFICATION){
                        afModel.putIdCode(new_pcode);
                        // System.out.println("id set = " + new_pcode + " cat T = " + nextCat.getTitle() );
                    }
                }
            }
        }
    }
    
    public String getIdcode() {
        // Iterate through all mCategories to change all p_code instances
        for (Iterator catIterator =mCategories.iterator(); catIterator.hasNext();) {
            CategoryModel nextCat = (CategoryModel) catIterator.next();
            InputModel[] inputs = nextCat.getInputs();
            //System.out.println("getidcode cat= " + nextCat.getTitle() );
            for (int i = 0; i < inputs.length; i++) {
                InputModel thisInput = inputs[i];
                if(thisInput instanceof FieldModel){
                    FieldModel afModel = (FieldModel)thisInput;
                    // System.out.println("fildmodel =  " +  afModel.getName() );
                    //if(afModel.getType() == FieldModel.TYPE_IDENTIFICATION){
                    if(afModel.getFieldType() == FieldModel.TYPE_IDENTIFICATION){
                        //System.out.println("find id type");
                        return thisInput.getValueText();
                    }
                }
            }
        }
        return null;
    }
    
    public HashMap getTremsHashTable() {
        HashMap aTable = new HashMap();
        
        for (Iterator catIterator =mCategories.iterator(); catIterator.hasNext();) {
            CategoryModel nextCat = (CategoryModel) catIterator.next();
            InputModel[] inputs = nextCat.getInputs();
            
            for (int i = 0; i < inputs.length; i++) {
                InputModel  thisInput   = inputs[i];
                String      termKey     = thisInput.getName();
                String[]    values      = thisInput.getValues();
                if(termKey != null && termKey.length() > 0){
                    if(values == null || values.length == 0)
                        aTable.put(termKey,null);
                    else{
                        Vector vecValues = new Vector(values.length);
                        for(int j = 0; j < values.length; j++){
                            vecValues.add(values[j]);
                            aTable.put(termKey,vecValues);
                        }
                    }
                }
            }
        }
        if(aTable.isEmpty()) return null;
        return aTable;
    }
    
    public void addChangeListener(ChangeListener cl) {      
        if (! mChangeListeners.contains(cl))
            mChangeListeners.add(cl);
    }
    
    public void removeChangeListener(ChangeListener cl) {
        
        mChangeListeners.remove(cl);
    }
    
    public void fireStateChanged() {
        for (int i = 0; i < mChangeListeners.size(); i++) {
            ChangeListener c = (ChangeListener) mChangeListeners.get(i);
            c.stateChanged(new ChangeEvent(this));
        }
    }
    // ADD att 5/6 2003 to catch change value to inform medrecordFrame
    public void stateChanged(javax.swing.event.ChangeEvent changeEvent) {
        /*
        if(mMedRecordsFrame != null)
            mMedRecordsFrame.setValueChanged(true);
        if(mFormEditor != null)
            mFormEditor.valueChanged();
          OT 03-11-10 */
    }

    /*
    public void setMedRecordsFrame(MedRecordsFrame pFrame){
        mMedRecordsFrame = pFrame;
    }
     */
     public void setFormEditor(FormEditor pFrame){
        mFormEditor = pFrame;
    }
    
    
    
}



/**
 * Get the p_code
 */
 /*   public String getPcode() {
        // Iterate through all mCategories to change all p_code instances
        for (Iterator catIterator =mCategories.iterator(); catIterator.hasNext();) {
            CategoryModel nextCat = (CategoryModel) catIterator.next();
            InputModel[] inputs = nextCat.getInputs();
  
            for (int i = 0; i < inputs.length; i++) {
                InputModel thisInput = inputs[i];
                String inputName = thisInput.getName();
                if ( (inputName.equalsIgnoreCase("pcode")) ||
                        (inputName.equalsIgnoreCase("p-code")) ||
                            (inputName.equalsIgnoreCase("p_code")) ){
                                return thisInput.getValueText();
                }
            }
        }
        return null;
    }*/

/**
 * Set the p_code
 */
 /*   public void setPcode(String new_pcode) {
  
        // Iterate through all mCategories to change all p_code instances
        for (Iterator catIterator =mCategories.iterator(); catIterator.hasNext();) {
            CategoryModel nextCat = (CategoryModel) catIterator.next();
  
            // Change all inputs in this CategoryModel
  
            InputModel[] inputs = nextCat.getInputs();
  
            for (int i = 0; i < inputs.length; i++) {
  
                InputModel thisInput = inputs[i];
                String inputName = thisInput.getName();
  
                if (
                    (inputName.equalsIgnoreCase("pcode"))
                    ||
                    (inputName.equalsIgnoreCase("p-code"))
                    ||
                    (inputName.equalsIgnoreCase("p_code"))
                    ) {
  
                        // A match, so change the field content
                        thisInput.putValue(new_pcode);
                }
  
  
            }
  
        }
  
    }*/


