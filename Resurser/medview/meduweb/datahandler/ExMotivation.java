package medview.meduweb.datahandler;

public class ExMotivation {
    public String exAnswer;
    public String exMotivation;
    public boolean correct;

    public ExMotivation(String exAnswer,
			String exMotivation,
			boolean correct) {
	this.exAnswer = exAnswer;
	this.exMotivation = exMotivation;
	this.correct = correct;
    }
}
