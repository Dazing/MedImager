package medview.meduweb.datahandler;

import java.sql.*;              //JDBC stuff
import java.util.*;             //ArrayList...
import medview.meduweb.datahandler.Datahandler;
import medview.meduweb.data.*;

/**
 *This class is a part of the datahandler for the mEduWeb 
 *system. It contains methods used for administration purposes.
 *It extends the class Datahandler which contains methods used
 *to manage the database connection(s). 
 *@author Frans
 *@version 1.00001
 */
public class Category {


    public class CategoryObj {
	public int id;
	public String name;
	public String attribute;
	public ArrayList values;

	CategoryObj(int id, String name, String attribute, ArrayList values) {
	    this.id = id;
	    this.name = name;
	    this.attribute = attribute;
	    this.values = new ArrayList(values);
	}

	public int getId() {
	    return id;
	}
	public String getName() {
	    return name;
	}
	public String getAttribute() {
	    return attribute;
	}	
	public ArrayList getValues() {
	    return values;
	}
    }

    /**
     *This attribute contains a reference to a new
     *Datahandler object.
     */
    private Datahandler dh = new Datahandler();
    private Datahandler dh2 = new Datahandler();
    private DataLayer dl = DataLayer.getInstance();

    /**
     *Gets all links, title and ids from the database.
     *@return ArrayList containing CategoryObj with id, categoryname, attribute 
     *and an arraylist of values.
     */
    public ArrayList getAllCategories() {
	ArrayList catList = new ArrayList();
	ArrayList valueList = new ArrayList();
	Statement stmt = null;
	Statement stmt2 = null;
	ResultSet rs = null;
	ResultSet rs2 = null;

	try {
	    
	    stmt = dh.connect();
	    stmt2 = dh2.connect();
	    
	    rs = stmt.executeQuery("SELECT id, categoryname, attribute FROM categoriestab "+
					   "ORDER BY attribute, categoryname");
	    while ((rs != null) && (rs.next())) {
		valueList = new ArrayList();
		int id = Integer.parseInt(rs.getString("id"));
		String name =  rs.getString("categoryname");
		String attribute = rs.getString("attribute");

		rs2 = stmt2.executeQuery("SELECT value FROM categoryvaluestab WHERE id='" + id + "'");
		while ((rs2 != null) && (rs2.next())) {
		    String value = rs2.getString("value");
		    valueList.add(value);
		}

	       	catList.add(new CategoryObj(id, name, attribute, valueList));
	    }
	    dh.disconnect();
	    dh2.disconnect();
	}catch(SQLException sqle){
	}

	return catList;
    }
    
    /**
     *Saves a category in the database.
     *@param name the categoryname.
     *@param attribute the attribute categorised.
     *@param valueList the values in the category.
     */
    public void saveCategory(String name, String attribute, ArrayList valueList) {
	Statement stmt = null;
	ResultSet rs;
	try {
	    stmt = dh.connect();
	    stmt.executeUpdate("INSERT INTO categoriestab (categoryname, attribute) VALUES('" + name + "','" + attribute + "')");
	    rs = stmt.executeQuery("SELECT max(id) FROM categoriestab");
	    rs.next();
	    String id = rs.getString(1);
	    Iterator j = valueList.iterator();
	    while (j.hasNext()) {
		String value = (String) j.next();
		stmt.executeUpdate("INSERT INTO categoryvaluestab VALUES('" + id + "','" + value + "')");		    
	    }
	    dh.disconnect();

	    dl.updateCategory(attribute, name, valueList);

	}catch(SQLException sqle){
	}
    }
    
    /**
     * Inserts all categories into the DataLayer.
     */
    public void remakeCategories() {
	ArrayList categoryList = getAllCategories();
	Iterator categoryI = categoryList.iterator();
	while (categoryI.hasNext()) {
	    CategoryObj category = (CategoryObj)categoryI.next();
	    dl.updateCategory(category.getAttribute(), category.getName(), category.getValues());
	}
    }	

    /**
     *Gets name, attribute and list of values for a given category
     *@param id the id of the category to list
     *@return category a CategoryObj with name, attribute and list of values
     */
    public CategoryObj getCategory(int id) {
	Statement stmt = null;
	Statement stmt2 = null;
	ResultSet rs = null;
	ResultSet rs2 = null;
	ArrayList valueList = new ArrayList();
	CategoryObj category = null;
	String attribute = null;
	String name = null;

	try {
	    stmt = dh.connect();
	    stmt2 = dh2.connect();
	    rs = stmt.executeQuery("SELECT categoryname, attribute FROM categoriestab WHERE id='" + id + "'");
	    while (rs.next()) {
		name = rs.getString("categoryname");
		attribute = rs.getString("attribute");
		rs2 = stmt2.executeQuery("SELECT value FROM categoryvaluestab WHERE id='" + id + "'");
		while (rs2.next()) {
		    String value = rs2.getString("value");
		    valueList.add(value);
		}
	    category = new CategoryObj(id, name, attribute, valueList);
	    }
	    dh.disconnect();
	    dh2.disconnect();
	}catch(SQLException sqle){
	}

	return category;
    }	
    
    /**
     *Removes a category from the database.
     *@param id the categoryname.
    */
    public void deleteCategory(int id) {
	Statement stmt = null;
	
	try {

	    CategoryObj cat = getCategory(id);

	    stmt = dh.connect();

	    stmt.executeUpdate("DELETE FROM categoriestab WHERE id='" + id + "'");

	    dl.removeCategory(cat.attribute, cat.name);

	    dh.disconnect();
	}catch(SQLException sqle){
	}
    }

    /**
     *Gets the attribute ignorelist
     *@return ignorelist an ArrayList with attributes
     */
    public ArrayList getAttributeIgnoreList() {
	Statement stmt = null;
	ResultSet rs = null;
	ArrayList ignoreList = new ArrayList();
	
	try {
	    stmt = dh.connect();
	    rs = stmt.executeQuery("SELECT attribute FROM attributeignoretab " +
					   "ORDER BY attribute");
	    while (rs.next()) {
		String attribute = rs.getString("attribute");
		ignoreList.add(attribute);
	    }
	    
	    dh.disconnect();
	    
	}catch(SQLException sqle){
	}

	return ignoreList;
    }
	
    /**
     *Removes an attribute from the ignorelist.
     *@param attribute the attribute to unignore.
    */
    public void unignoreAttribute(String attribute) {
	Statement stmt = null;
	
	try {
	    stmt = dh.connect();
	    stmt.executeUpdate("DELETE FROM attributeignoretab WHERE attribute ='" + attribute + "'");
	    
	    dl.unRemoveAttribute(attribute);

	    dh.disconnect();
	}catch(SQLException sqle){
	}
    }

    /**
     * Adds an attribute to the ignorelist.
     *@param attribute the attribute to ignore.
     */
    public void ignoreAttribute(String attribute) {
	Statement stmt = null;
	ResultSet rs;
	try {
	    stmt = dh.connect();
	    stmt.executeUpdate("INSERT INTO attributeignoretab VALUES('" + attribute + "')");
	 
	    dh.disconnect();

	    dl.removeAttribute(attribute);

	}catch(SQLException sqle){
	}
    }

    /**
     * Reloads all attributeas that are to be ignored, and pushes this information to the DataLayer.
     */
    public void remakeIgnoredAttributes() {
	ArrayList attributesToIgnore = getAttributeIgnoreList();
	Iterator ignoreI = attributesToIgnore.iterator();
	while (ignoreI.hasNext()) {
	    dl.removeAttribute((String)ignoreI.next());
	}
    }

    public ArrayList getAttributeCategories(String attribute) {
	Statement stmt = null;
	ResultSet rs;
	ArrayList nameList = new ArrayList();

	try{
	    stmt=dh.connect();
	    rs = stmt.executeQuery("SELECT categoryname FROM categoriestab WHERE attribute='" + attribute + "'");

	    while (rs.next()) {
		String categoryName = rs.getString("categoryname");
		nameList.add(categoryName);
	    }
	    

	    dh.disconnect();

	}
	catch(SQLException sqle) {}

	return nameList;
    }

}
