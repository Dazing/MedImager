package minimed.help;


/**
 * Represents a category of questions (articles) in the help system. 
 * 
 * @author Jens Hultberg
 */
public class Category {
	private String title;
	private Question[] questions;
	
	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param pTitle the name of the category. 
	 * @param pQuestions the questions for the category. 
	 */
	public Category(String pTitle, Question[] pQuestions) {
		this.title = pTitle;
		this.questions = pQuestions;
	}
	
	/**
	 * Returns all questions in the category.
	 * 
	 * @return all questions in the category. 
	 */
	public Question[] getQuestions() {
		return questions;
	}
	
	/**
	 * Returns the name of the category. 
	 * 
	 * @return the name of the category. 
	 */
	public String getTitle() {
		return title;
	}
}
