package medview.meduweb.search;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchBean implements Serializable {

    //Things used by simple search
    private ArrayList attributeList = new ArrayList();
    private ArrayList valueList = new ArrayList();
    private ArrayList resultList = new ArrayList();
    private String searchType;
    private String savedSearchString = "";
    private String valueListType = "both";

    //Things used by advanced search
    private ArrayList parameterList = new ArrayList();
    private ArrayList allParameterList = new ArrayList();

    /**
     * Used to reset all valus for simple search.
     */
    public void reset() {
	this.attributeList = new ArrayList();
	this.valueList = new ArrayList();
	this.resultList = new ArrayList();
    }

    /**
     * Adds an attribute and a value to the attribute and value list.
     * Used when in simple-search-mode.
     * If the attribute already exists, it's value is overwritten.
     * @param attribute the attribute to add.
     * @param value the value to add
     */
    public void addAttributeValue(String attribute, String value) {
	//if attribute already exists, replace it
	int index = attributeList.indexOf(attribute);
	if (index != -1) {
	    valueList.set(index, value);
	} else {
	    attributeList.add(attribute);
	    valueList.add(value);
	}
    }

    /**
     * Removes an attribute, and it's associated value.
     * Used when in simple-search-mode.
     * @param attribute the attribute to remove.
     */
    public void removeAttributeValue(String attribute, String value) {
	//each attribute appears only one time, delete the first instance
	int index = attributeList.indexOf(attribute);
	if (index != -1) {
	    Object o = attributeList.remove(index);
	    o = valueList.remove(index);
	}
    }

	public void addSearchString(String searchString) {
		savedSearchString = searchString;	
	}
	
	public String getSearchString() {
		return savedSearchString;	
	}

    /**
     * Returns all attributes saved.
     * @return ArrayList containing Strings, the attributes.
     */
    public ArrayList getAttributeList() {
	return attributeList;
    }

    /**
     * Returns all values, with same index as their corresponding attributes.
     * @return ArrayList of Strings, the values.
     */
    public ArrayList getValueList() {
	return valueList;
    }

    /**
     * Adds a result list with pcodes.
     * @param sesultList the list of strings to add.
     */
    public void addResultList(ArrayList resultList) {
	this.resultList = new ArrayList(resultList);
    }

    /**
     * Returns a sub-section of the saved resultList.
     * For use when displaying X results at a time.
     * @param from The index to start at.
     * @param to The index to stop at.
     * @return ArrayList containing pcodes (as Strings), if index was out of bounds
     * an empty ArrayList is returned.
     */
    public ArrayList getResultInterval(int from, int to) {
	ArrayList out;
	try {
	    out = new ArrayList(resultList.subList(from, to));
	} catch (IndexOutOfBoundsException ioobe) {
	    out = new ArrayList();
	}
	return out;
    }

    /**
     * Gets the size of the saved result (no of pcodes).
     * @return the number of patients in the result.
     */
    public int getResultSize() {
	return resultList.size();
    }

    /**
     * Gets the type of search performed (simple, advanced, pcode, visualization).
     * @return A Strring containing the saved type.
     */
    public String getSearchType() {
	return searchType;
    }

    /**
     * Sets the search type.
     */
    public void setSearchType(String searchType) {
	this.searchType = searchType;
    }

    public String getValueListType() {
    	return valueListType;	
    }

    public void setValueListType(String type) {
    	valueListType = type;	
    }
    
    
    /**
     * Adds a parameter for the advanced search.
     * Only adds it if it doesn't already exist.
     * @param parameter The searchexpression to add.
     */
    public void advancedAddParameter(String parameter) {
	if (! parameterList.contains(parameter)) {
	    parameterList.add(parameter);
	}
	if (! allParameterList.contains(parameter)) {
	    allParameterList.add(parameter);
	}
    }

    /**
     * Removes a parameter from the advanced parameter list.
     * Saves it in the backup-list for restore possibilities.
     * @param parameter The parameter to remove.
     */
    public void advancedRemoveParameter(String parameter) {
	if (parameterList.contains(parameter)) {
	    Object o = parameterList.remove(parameterList.indexOf(parameter));
	}
    }

    /**
     * Restores the advanced parameter-list from the backup-list.
     */
    public void advancedRestoreParameters() {
	parameterList = new ArrayList(allParameterList);
    }
    
    /**
     * Gets the saved list of parameters.
     * @return ArrayList of Strings, the saved parameters.
     */
    public ArrayList advancedGetParameterList() {
	return parameterList;
    }

    /**
     * Resets the advanced lists, the parameter list and the backup list.
     */
    public void advancedReset() {
	parameterList = new ArrayList();
	allParameterList = new ArrayList();
    }

}
