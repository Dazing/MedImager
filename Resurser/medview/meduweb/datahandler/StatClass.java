package medview.meduweb.datahandler;

public class StatClass {
	public String loggedtime;
	public int numlogins;
	public int numsearches;
	public int numadminexercises;
	public int numownexercises;
	StatClass(String loggedtime,
		       int numlogins, 
		       int numsearches,
		       int numadminexercises,
		       int numownexercises) {
	    this.loggedtime = loggedtime;
	    this.numlogins = numlogins;
	    this.numsearches = numsearches;
	    this.numadminexercises = numadminexercises;
	    this.numownexercises = numownexercises;
	}
	public StatClass(){}
	
	public String toString() {
	    return loggedtime + " " + numlogins + " " + numsearches + " " + numadminexercises + " " + numownexercises;

	}
    }
