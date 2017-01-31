package medview.meduweb.datahandler;

import java.sql.*;	//JDBC


/**
*This class is a part of the datahandler for the mEduWeb system. 
*It contains the question object.
*@author Johanna
*@version 1.0
*/

public class Question {
    
    private int id;
    
    private String type;
    
    private String questId;
    
    private String question;
    
    private String answer;
    
    private Timestamp askDate;
    
    private Timestamp answerDate;
    
    
    Question(int id, String questId, String question) {
	this.id = id;
	this.questId = questId;
	this.question = question;
    }
    
    Question(int id, String question, String answer, Timestamp askDate) {
	this.id = id;
	this.question = question;
	this.answer = answer;
	this.askDate = askDate;
    }
 
    Question(int id, String question, String answer, Timestamp askDate, Timestamp answerDate) {
	this.id = id;
	this.question = question;
	this.answer = answer;
	this.askDate = askDate;
	this.answerDate = answerDate;
    }

    Question(int id, String questId, String question, String type, String answer) {
	this.id = id;
	this.type = type;
	this.questId = questId;
	this.question = question;
	this.answer = answer;
    }


    Question(int id, String questId, String question, String answer, 
	     Timestamp askDate, Timestamp answerDate) {
	this.id = id;
	this.questId = questId;
	this.question = question;
	this.answer = answer;
	this.askDate = askDate;
	this.answerDate = answerDate;
    }

        
    Question(int id, String type, String questId, String question, String answer, 
	     Timestamp askDate, Timestamp answerDate) {
	this.id = id;
	this.type = type;
	this.questId = questId;
	this.question = question;
	this.answer = answer;
	this.askDate = askDate;
	this.answerDate = answerDate;
    }
    
    public int getId() {
	return id;
    }
    
    public String getType() {
	return type;
    }
    
    public String getQuestId() {
	return questId;
    }
    
    public String getQuestion() {
	return question;
    }
    
    public String getAnswer() {
	return answer;
    }
    
    public Timestamp getAskDate() {
	return askDate;
    }	
    
    public Timestamp getAnswerDate() {
	return answerDate;
    }	
}

