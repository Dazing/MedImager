package medview.meduweb.visualizer;
import java.io.Serializable;
import java.util.*;

public class VisualBean implements Serializable {

    String[] vals1 = null;
    String[] vals2 = null;
    String[] vals3 = null;
    String attr1 = null;
    String attr2 = null;
    String attr3 = null;
    public ArrayList constraintAttributes = new ArrayList();
    public ArrayList constraintValues = new ArrayList();
    
    public void reset() {
	vals1 = null;
	vals2 = null;
	vals3 = null;
	attr1 = null;
	attr2 = null;
	attr3 = null;
	constraintAttributes.clear();
	constraintValues.clear();
    }
    public void setAttr1(String attr1) {
	this.attr1=attr1;
    }
    
    public void setAttr2(String attr2) {
	this.attr2=attr2;
    }
    
    public void setAttr3(String attr3) {
		this.attr3=attr3;
    }
    
    public String getAttr1() {
	return attr1;
    }

    public String getAttr2() {
	return attr2;
    }
    
    public String getAttr3() {
	return attr3;
    }
    
    public ArrayList getConstraintAttributes() {
	return constraintAttributes;
    }
    public ArrayList getConstraintValues() {
	return constraintValues;
    }

    public void setConstraintAttributes(ArrayList ca) {
	this.constraintAttributes = ca;
    }

    public void setConstraintValues(ArrayList cv) {
	this.constraintValues = cv;
    }
    

    public void setVals1(String[] vals) {
	this.vals1 = vals;
    }
    
    public void setVals2(String[] vals) {
	this.vals2 = vals;
    }

    public void setVals3(String[] vals) {
	this.vals3 = vals;
    }
    
    public String[] getVals1() {
	return vals1;
    }
    
    public String[] getVals2() {
	return vals2;
    }
    
    public String[] getVals3() {
	return vals3;
    }
}
