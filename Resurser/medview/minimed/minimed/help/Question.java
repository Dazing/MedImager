package minimed.help;


/**
 * Represents a question (article) in the help system. 
 * 
 * The title is a question and the text is an answer to 
 * that question. Both are given in plain text format.  
 */
public class Question {
	private String title;
	private String text;
	
	/**
	 * Constructs a new instance of this class.
	 * 
	 * @param pTitle the title in pure text format.
	 * @param pText the text in pure text format.
	 */
	public Question(String pTitle, String pText) {
		this.title = pTitle;
		this.text = pText;
	}
	
	/**
	 * Returns the question to be answered.
	 * 
	 * @return the question to be answered.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Returns the answer to the question.
	 * 
	 * @return the answer to the question.
	 */
	public String getText() {
		return text;
	}
}
