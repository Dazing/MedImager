package medview.meduweb.datahandler;

/**
*This class contains a part of the datahandler for the mEduWeb system.
*It contains an object with two string and an integer, designed for use
*in Material.java, Messages.java and Links.java.
*/     

public class StringStringId {
	public String s1;	
	public String s2;
	public int id;
	StringStringId(String s1, String s2, int id) {
	    this.s1 = s1;
	    this.s2 = s2;
	    this.id = id;
	}
	public String toString() {
	    return s1 + " " + s2 + " " + id;
	}
    }
