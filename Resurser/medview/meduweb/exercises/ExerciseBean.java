package medview.meduweb.exercises;

import java.util.*;
import java.io.*;
import medview.meduweb.datahandler.ExMotivation;

public class ExerciseBean implements Serializable {
    private boolean saved = false;
    private ArrayList motivationList = new ArrayList();

    public ExerciseBean() {
	motivationList = new ArrayList();
	saved = false;
    }

    public void clear() {
	motivationList = new ArrayList();
	saved = false;
    }

    public void addMotivation(ExMotivation motivation) {
	motivationList.add(motivation);
    }

    public void setSaved() {
	saved = true;
    }

    public boolean isSaved() {
	return saved;
    }

    
}
