package minimed.help;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Vector;

import minimed.MinimedModel;
import minimed.core.MinimedConstants;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Represents a collection of questions and answers accesible thru the Minimed
 * help system. All questions are divided into categories. 
 * 
 * @author Andreas Johansson, Jonas Hadin
 */
public class Help {
	private Category[] categories;
	private Question[] questions;

	/**
	 * Constructs a new instance of this class.
	 *  
	 * @throws Exception
	 */
	public Help(MinimedModel pModel) throws Exception {

		String helpFilePath = pModel.getHelpFilePath();
		File writeFile = new File(helpFilePath);
		
		/* Tests whether the file exists. */
		if (!writeFile.exists()) {
			throw new FileNotFoundException();
		}
		
		/* Creates a FileInputStream. */
		FileInputStream fos = new FileInputStream(writeFile);
		InputStreamReader osw = new InputStreamReader(fos, MinimedConstants.LATIN_ENC);
		
		/* Creates a XML parser instance. */
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
		
        /* Creates temporary vectors. */
		Vector questionVector = new Vector(); 
		Vector categoryVector = new Vector();
		Vector allQuestions = new Vector();
				
		/* Creates temporary strings. */
		String text = "";
		String CategoryTitle = ""; 
		String QuestionTitle = "";
				
		/* 
		 * Parses the XML file and builds an array of categories, each with an 
		 * array of questions. Each question is also added to the array of all questions.
		 */
		xpp.setInput(osw);
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
        	if (eventType == XmlPullParser.START_DOCUMENT) { 
        		/* Do nothing. */
        	} else if (eventType == XmlPullParser.END_DOCUMENT) { 
        		/* Do nothing. */
        	} else if (eventType == XmlPullParser.START_TAG) {
        		if (xpp.getName().trim().compareToIgnoreCase("question") == 0) {
        			QuestionTitle = xpp.getAttributeValue(0);
             	} else if (xpp.getName().trim().compareToIgnoreCase("category") == 0) {
        			CategoryTitle = xpp.getAttributeValue(0);       			 
        		}
        	} else if (eventType == XmlPullParser.END_TAG) {	
        		if (xpp.getName().trim().compareToIgnoreCase("question") == 0){
        			Question question = new Question(QuestionTitle,text);
        			questionVector.add(question);
        			allQuestions.add(question);
        			text = "";
        		} else if (xpp.getName().trim().compareToIgnoreCase("category") == 0) {
        			Question[] questArray = (Question[])questionVector.toArray(new Question[0]);
        			Category category = new Category(CategoryTitle, questArray);
        			categoryVector.add(category);
        			questionVector.removeAllElements(); //new Category, new Questionlist
        		}
        	} else if (eventType == XmlPullParser.TEXT) {
        		text += xpp.getText().trim();
        	}
        	eventType = xpp.next();
        }

        questions = (Question[]) allQuestions.toArray(new Question[0]);
		categories = (Category[]) categoryVector.toArray(new Category[0]);
	}
	
	/**
	 * Returns all categories. 
	 * 
	 * @return all categories.
	 */
	public Category[] getCategories() {
		return categories; 
	}
	
	/**
	 * Returns all questions.
	 * 
	 * @return all questions.
	 */
	public Question[] getQuestions() {
		return questions;
	}
}
