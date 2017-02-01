package medview.meduweb.datahandler;

import java.sql.*;                     //JDBC
import java.util.*;
import medview.meduweb.datahandler.Datahandler;
import medview.meduweb.datahandler.Question;
import medview.meduweb.datahandler.Mailer;
import medview.meduweb.datahandler.Administration;
import medview.meduweb.datahandler.StringStringId;

/**
*This class is a part of the datahandler for the mEduWeb system. 
*It contains methods used for handling questions.
*@author Johanna
*@version 1.0
*/

public class Questionhandler {

    public class QuestAnsw {
	public String question;
	public String answer;
	QuestAnsw(String question, String answer) {
	    this.question = question;
	    this.answer = answer;
	}

	public String getQuest() {
	    return question;
	}

	public String getAnsw() {
	    return answer;
	}
    }

    /**
     *This attribute contains a reference to a new Datahandler object.
     */
    private Datahandler dh = new Datahandler();
    
    
    /**
     *Returns a list of questions belonging to a case, excersise or visualisation. 
     *@param type type of question.
     *@param questId question ID; user, pcode, excercise ID or visualisation path.
     *@return questions, an ArrayList of questions with id, question, answer, askDate,answerDate.
     *@throws java.sql.SQLException has to be caught elsewhere.
     */
    public ArrayList getQuestion(String type, String questId) throws SQLException {
	
	ArrayList questions = new ArrayList();
	Statement dbstmt = dh.connect();
	ResultSet rs;
	
	String sql = "SELECT id, question, answer, askdate, answerdate FROM questionstab WHERE type = '" + type + "' AND questid = '" + questId + "' ORDER BY askdate";
	rs = dbstmt.executeQuery(sql);
	
	while (rs.next()) {
	    questions.add(new Question(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4), rs.getTimestamp(5)));
	}
	
	dh.disconnect();
	
	return questions;
    }
    
    /**
     *Returns a list of questions of a certain type, asked by a specific user. 
     *@param user The name of the user
     *@param type The type of question; case, ex, vis, userid
     *@return questions, an ArrayList of questions with id, type, questId, question, answer, askDate, answerDate.
     *@throws java.sql.SQLException has to be caught elsewhere.
     */
    public ArrayList getQuestionByUser(String user, String type) throws SQLException {
	
	ArrayList questions = new ArrayList();
	Statement dbstmt = dh.connect();
	ResultSet rs;
	
	String sql = "SELECT id, questid, question, answer, askdate, answerdate FROM questionstab WHERE userid = '" + user + "' AND type='" + type + "'";
	rs = dbstmt.executeQuery(sql);
	
	while (rs.next()) {
	    questions.add(new Question(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getTimestamp(5), rs.getTimestamp(6)));
	}
	
	dh.disconnect();
	
	return questions;
    }

    /**
     *Returns a QuestAnsw object with a questiontext and answertext to a specific id. 
     *@param id specific for the question.
     *@return questionanswer, a QuestAnsw with questiontext and answertext.
     *@throws java.sql.SQLException has to be caught elsewhere.
     */
    public QuestAnsw getSpecificQuestion(String id) throws SQLException {
	
	Statement dbstmt = dh.connect();
	ResultSet rs;
	
	String sql = "SELECT question, answer FROM questionstab WHERE id = '" + id + "'";
	rs = dbstmt.executeQuery(sql);
	rs.next();
	QuestAnsw questionanswer = new QuestAnsw(rs.getString(1), rs.getString(2));
    
        dh.disconnect();
	
	return questionanswer;
    }
    
    /**
     *Returns a list of all unanswered questions belonging to a case, excersise or visualisation. 
     *@return questions, an ArrayList of questions with id, questid, question, askdate.
     *@throws java.sql.SQLException has to be caught elsewhere.
     */
    public ArrayList getNewQuestions() throws SQLException {
	
	ArrayList questions = new ArrayList();		
	Statement dbstmt = null;
	ResultSet rs;
	
	dbstmt = dh.connect();
	
	String sql = "SELECT id, questid, question, askdate FROM questionstab WHERE answer IS NULL ORDER BY askdate";
	rs = dbstmt.executeQuery(sql);
	
	while(rs.next()) {
	    questions.add(new Question(rs.getInt(1), 
				       rs.getString(2), rs.getString(3), rs.getTimestamp(4)));
	}
	
	dh.disconnect();
	return questions;
    }
    
    /**
     *Returns a list of all newly answered questions asked by the user. 
     *@param user The current user
     *@param lastLogin The time for the users last login
     *@return questions, an ArrayList of questions with id, questid, question, answer.
     *@throws java.sql.SQLException has to be caught elsewhere.
     */
    public ArrayList getNewQandA(String user, Timestamp lastLogin) throws SQLException {
	
	ArrayList questions = new ArrayList();		
	Statement dbstmt = null;
	ResultSet rs;
	
	dbstmt = dh.connect();
	
	String sql = "SELECT id, questid, question, type, answer FROM questionstab WHERE answer IS NOT NULL AND userid='" + user + "' AND answerdate > '" + lastLogin + "'";
	rs = dbstmt.executeQuery(sql);
	
	while(rs.next()) {
	    questions.add(new Question(rs.getInt(1), 
				       rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
	}
	
	dh.disconnect();
	return questions;
    }
    
    /**
     *Returns the number of unanswered questions belonging to a case. 
     *@param number String with questid for the case.
     *@return number, an int containing the number of unanswered questions conected with a case.
     *@throws java.sql.SQLException has to be caught elsewhere.
     */
    public int getNoNewCaseQuestions(String questid) throws SQLException {
	
	int number = 0;
	Statement dbstmt = null;
	ResultSet rs;
	
	dbstmt = dh.connect();
	
	String sql = "SELECT id FROM questionstab WHERE questid='" + questid + "' and answerdate IS NULL";
	rs = dbstmt.executeQuery(sql);
	
	while(rs.next()) {
	    number++;
	}
	
	dh.disconnect();
	return number;
    }
	
    /**
     *Saves a question and uses the sendMail method in Mailer.java to send notifications
     *by email to all addresses given by the getEmailAddresses method in Administration.java
     *@param type type of question.
     *@param questId question ID; pcode, excercise ID or visualisation path.
     *@param question the question text. 
     *@param userId the username.
     *@throws java.sql.SQLException has to be caught elsewhere.
     */
    public void saveQuestion(String type, String questId, String question, String userId) throws SQLException {
	Mailer mailer = new Mailer();
	Statement dbstmt = dh.connect();
	ResultSet rs = null;
	String sql = "INSERT INTO questionstab (type, questid, question, userid) VALUES('" + type + "', '" + questId + "', '" + question + "','" + userId + "')";
	dbstmt.executeUpdate(sql);
	
	dh.disconnect();
	String temp = "";
	if(type.equals("vis")) {
	    temp = "Fråga på visualisering./n";
	}else{ if(type.equals("ordinary")) {
	    temp = "";
	}else{
	    temp = "Fråga på " + type + "./n"; 
	}}
	ArrayList emailList =  new Administration().getEmailAddresses();
	Iterator k = emailList.iterator();
	while (k.hasNext()) {
	    StringStringId mail = (StringStringId) k.next();

	    String[] addrs = {mail.s2};
	    String header = "Ny frågra från mEduWeb";
	    String mess = temp+ "/n/n" + question + 
		"/n  / Hälsningar " + userId;
	    mailer.sendMail(addrs, header, mess);
	}
    }
    
    /**
     *Returns the first 30 letter from a questiontext. 
     *@param questiontext unique question id.
     *@return String with the 30 first letters in questiontext.
     */
    public static String shortQuestion(String questiontext) {
	
	if (questiontext.length() < 30) {
	    return questiontext;
	} else {
	    return questiontext.substring(0,30) +"...";
	}
    }
    
    /**
     *Returns a date and a time from a Timestamp as a String. 
     *@param Timestamp.
     *@return String with the date and the time from Timestamp..
     */
    public String getDateTime(Timestamp timestamp) {
	
	String dateTime = timestamp.toString();
	return dateTime.substring(0,dateTime.length() - 14);
    }
    
    /**
     *Deletes a question. 
     *@param id unique question id.
     *@throws java.sql.SQLException has to be caught elsewhere.
     */
    public void deleteQuestion(String id) throws SQLException {
	
	Statement dbstmt = dh.connect();
	
	String sql = "DELETE FROM questionstab WHERE id = '" + id + "'";
	dbstmt.executeUpdate(sql);
	
	dh.disconnect();
    }
    
    /**
     *Returns a list of all questions. 
     *@return questions, an ArrayList of all questions with id, type, questid, question, answer, askdate, answerdate.
     *@throws java.sql.SQLException has to be caught elsewhere.
     */
    public ArrayList getAllQuestions() throws SQLException {
	
	ArrayList questions = new ArrayList();
	int i = 0;
	Statement dbstmt = dh.connect();
	ResultSet rs;
	
	String sql = "SELECT id, type, questid, question, answer, askdate, answerdate FROM questionstab ORDER BY askdate";
	rs = dbstmt.executeQuery(sql);
	
	while (rs.next()) {
	    questions.add(new Question(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getTimestamp(6), rs.getTimestamp(7)));
	}
	
	dh.disconnect();
	
	return questions;
    }
    
    /**
     *Saves the answer to a question. 
     *@param id the question id.
     *@param answer the answer text.
     *@throws java.sql.SQLException has to be caught elsewhere.
     */
    public void saveAnswer(String id, String answer) throws SQLException {
	
	Statement dbstmt = dh.connect();
	
	String sql = "UPDATE questionstab SET answer = '" + answer + "',answerdate=CURRENT_TIMESTAMP WHERE id = " + id;
	dbstmt.executeUpdate(sql);
	
	dh.disconnect();
	
    }
    
}

