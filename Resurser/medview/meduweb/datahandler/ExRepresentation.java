package medview.meduweb.datahandler;
import medview.meduweb.datahandler.ExMotivation;
import java.sql.Date;
import java.util.*;

public class ExRepresentation {
 
    public int question = 0;   
    public ArrayList pics;
    public String exTitle;
    public String exText;
    public int noAnswers;
    public Date showDate;
    public boolean shown;
    public ArrayList motivations;
    public boolean isSaved = false;
    public ArrayList savedQuestions = new ArrayList();
    public String newExId = null;

    public ExRepresentation(ArrayList pics,
			    String exTitle,
			    String exText,
			    int noAnswers,
			    Date showDate,
			    boolean shown,
			    ArrayList motivations) {

	this.pics = new ArrayList(pics);     
	this.exTitle = exTitle;     
	this.exText = exText;     
	this.showDate = showDate;     
	this.noAnswers =  noAnswers;     
	this.shown = shown;     
	this.motivations = new ArrayList(motivations);
	isSaved = false;
	savedQuestions = new ArrayList();
    }
    
    public ExRepresentation() {
	pics = new ArrayList();
	exTitle = "";
	exText = "";
	noAnswers = 0;
	shown = false;
	motivations = new ArrayList();
	isSaved = false;
	savedQuestions = new ArrayList();
    }

    public void setSaved() {
	isSaved = true;
    }

    public boolean isSaved() {
	return isSaved;
    }

    public void clearSaved() {
	isSaved = false;
	savedQuestions = new ArrayList();
	newExId = null;
    }

}
