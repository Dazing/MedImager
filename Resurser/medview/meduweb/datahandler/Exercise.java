package medview.meduweb.datahandler;

import medview.meduweb.datahandler.*;
import java.sql.*;
import medview.meduweb.datahandler.ExListItem;
import medview.meduweb.datahandler.ExRepresentation;
import medview.meduweb.datahandler.ExMotivation;
import medview.meduweb.datahandler.ExTextTitle;
import java.util.*;                                  //ArrayList

/**
 *This class contains methods used in the connection between 
 *the Exercise part of the system and the databases. Special 
 *classes, imported above, is used to represent exercises.
 *@author Figge
 *@version 1.0
 */
public class Exercise {

    

    /**
     *Deletes a specific exercise from the database.
     *@param exId A valid exercise id.
     */
    public void deleteEx(int exId) throws SQLException {
    	Datahandler dh = new Datahandler();
	Statement stmt = null;
	stmt = dh.connect();
	stmt.executeUpdate("DELETE FROM exercisetab " +
			   "WHERE exerciseid=" + 
			   (new Integer(exId)).toString());
	dh.disconnect();

	//Remove all questions associated with this exercise
	Questionhandler questionH = new Questionhandler();
	ArrayList questionList = questionH.getQuestion("exercise", String.valueOf(exId));
	Iterator questionI = questionList.iterator();
	while (questionI.hasNext()) {
	    questionH.deleteQuestion(String.valueOf(
				     ((Question)questionI.next()).getId()));
	}
    }

    public void deleteQuestion(int exId,int question) throws SQLException {
    	Datahandler dh = new Datahandler();
	Statement stmt = dh.connect();
	stmt.executeUpdate("DELETE FROM exercisequestionstab " +
			"WHERE exerciseid=" + String.valueOf(exId) +
			" AND question=" + String.valueOf(question));
	stmt.executeUpdate("DELETE FROM exercisemotivationstab " +
			"WHERE exerciseid=" + String.valueOf(exId) +
			" AND question=" + String.valueOf(question));
	stmt.executeUpdate("DELETE FROM exerciseimagestab " +
			"WHERE exerciseid=" + String.valueOf(exId) +
			" AND question=" + String.valueOf(question));
	dh.disconnect();
    }

    /**
     *Lists all available exercises.
     *@return ArrayList containing the available exercises as ExListItem objects.
     */
    public ArrayList getAllEx() throws SQLException {
    	Datahandler dh = new Datahandler();
	ArrayList tmpExercises = new ArrayList();
	Statement stmt = null;
	ResultSet rs = null;
	int i = 0;
	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT * FROM exercisetab");
	while(rs.next()) {
	    tmpExercises.add(new ExListItem(rs.getInt("exerciseid"),
					    rs.getString("title"),
					    rs.getDate("created"),
					    rs.getDate("startatdate"),
					    rs.getBoolean("shown"),
						rs.getInt("questions")));
	}
	dh.disconnect();
	return tmpExercises;
    }

    /**
     *Gets the current exercises.
     *@return ArrayList containing ExListItems, the exercises currently shown.
     */
    public ArrayList getCurrentEx() throws SQLException { 
    	Datahandler dh = new Datahandler();  
	ArrayList tmpExercises = new ArrayList();
	Statement stmt = null;
	ResultSet rs = null;
	int i = 0;
	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT * FROM exercisetab WHERE shown=true ORDER BY startatdate");
	while(rs.next()) {
	    tmpExercises.add(new ExListItem(rs.getInt("exerciseid"),
					    rs.getString("title"),
					    rs.getDate("created"),
					    rs.getDate("startatdate"),
					    rs.getBoolean("shown"),
					    rs.getInt("questions")));
	}
	dh.disconnect();
	 
	return tmpExercises;
    }

    public int getNumberOfQuestions(int exId) throws SQLException {
    	Datahandler dh = new Datahandler();
	int numberOfQuestions = 0;
	Statement stmt = dh.connect();
	ResultSet rs = stmt.executeQuery("SELECT questions FROM exercisetab WHERE " +
					"exerciseid=" + String.valueOf(exId));
	dh.disconnect();
	if(rs.next()) {
		numberOfQuestions = rs.getInt("questions");
	} 
	return numberOfQuestions;
    }

    /**
     *Gets all new exercises for a specific user.
     *@param user The current username
     *@return ArrayList containing ExListItems, the exercises that are new to the user.
     */
    public ArrayList getUnviewedEx(String user) throws SQLException {  
    	Datahandler dh = new Datahandler(); 
	ArrayList tmpExercises = new ArrayList();
	Statement stmt = null;
	ResultSet rs = null;

	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT * FROM exercisetab WHERE shown=true AND exerciseid NOT IN (SELECT exerciseid from completedexercisestab WHERE userid='" + user + "')");
	while(rs.next()) {
	    tmpExercises.add(new ExListItem(rs.getInt("exerciseid"),
					    rs.getString("title"),
					    rs.getDate("created"),
					    rs.getDate("startatdate"),
					    rs.getBoolean("shown"),
					    rs.getInt("questions")));
	}
	dh.disconnect();
	 
	return tmpExercises;
    }

    /**
     * Mars an exercise for a user so that it doesn't appear on that
     * users startpage anymore.
     * @param userid the id of the user
     * @param exid the id of the exercise to flag
     */
    public void markExerciseAsDone(String userid, String exid) {
	try {
		Datahandler dh = new Datahandler();
	    Statement stmt = dh.connect();
	
	    int i = stmt.executeUpdate("INSERT INTO completedexercisestab VALUES" +
				   " ('" + userid + "', '" + exid + "')");
	    dh.disconnect();
	} catch (SQLException e) {
	}
    }

    /**
     * Checks whether a specific exercise has been marked as done
     * by a specific user.
     * @param userid the user to check against.
     * @param exid the exercise to check.
     * @return boolean true is exercise has been done, false otherwise.
     */
    public boolean isFinishedExercise(String userid, String exid) {
	boolean isDone = false;
	try {
		Datahandler dh = new Datahandler();
	    Statement stmt = dh.connect();
	    
	    ResultSet rs = stmt.executeQuery("SELECT userid FROM completedexercisestab WHERE userid='" + userid + "' AND exerciseid='" + exid + "'");
	    dh.disconnect();
	    if (rs != null && rs.next()) {
		isDone = true;
	    }
	} catch (SQLException e) {
	}
	return isDone;
    }

    /**
     *Gets a specific exercise from the database.
     *@param exId A valid exercise id.
     *@return The exercise as an ExRepresentation object.
     *@throws SQLException
     */
    public ExRepresentation getEx(int exId,int question) throws SQLException {
	ExRepresentation er = null;
	Statement stmt = null;

	ResultSet rs = null;
	String text = "";
	Datahandler dh2 = new Datahandler();
	stmt = dh2.connect();

	rs = stmt.executeQuery("SELECT * FROM exercisequestionstab WHERE " +
				"exerciseid=" + exId + " AND question=" + String.valueOf(question));
	if(rs.next()) {
		text = rs.getString("exercisetext");
	}

	rs = stmt.executeQuery("SELECT * FROM exercisetab WHERE " +
			       "exerciseid=" + exId);
	
	ArrayList motivations = this.getExMotivations(exId,question);
	if(rs.next()) {
	    er = new ExRepresentation(this.getExPics(exId, question),
				     rs.getString("title"),
				     text,
				     motivations.size(),
				     rs.getDate("startatdate"),
				     rs.getBoolean("shown"),
				     motivations);
	   
	}
	dh2.disconnect();

	return er;
    }

    /**
     *Gets the title and text for a specific exercise.
     *@param exId A valid exercise id.
     *@return the title for the given exerciseId.
     *@throws SQLException
     */
    public ExTextTitle getExTextTitle(int exId) throws SQLException { 
	ExTextTitle ett = null;
	Statement stmt = null;
	ResultSet rs = null;
	Datahandler dh = new Datahandler();
	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT * FROM exercisetab WHERE " +
			       "exerciseid=" + (new Integer(exId)).toString());
	if(rs.next()) {
	    ett = new ExTextTitle(rs.getString("exercisetext"),
				  rs.getString("title"));
	}
	dh.disconnect();
	return ett;
    }

    public String getExTitle(int exId) throws SQLException { 
	String exTitle = "";
	Statement stmt = null;
	ResultSet rs = null;
	Datahandler dh = new Datahandler();
	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT title FROM exercisetab WHERE " +
			       "exerciseid=" + (new Integer(exId)).toString());
	if(rs.next()) {
		exTitle = rs.getString("title");
	}
	dh.disconnect();
	return exTitle;

    } 

    public String getQuestionText(int exId, int question) throws SQLException { 
	String qText = "";
	Statement stmt = null;
	ResultSet rs = null;
	Datahandler dh = new Datahandler();
	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT exercisetext FROM exercisequestionstab WHERE " +
				"exerciseid=" +(new Integer(exId)).toString() + " AND " +
				"question=" + (new Integer(question)).toString());
	if(rs.next()) {
		qText = rs.getString("exercisetext");
	}
	dh.disconnect();
	return qText;
    } 
				 

    public ArrayList getExMotivations(int exId, int question) throws SQLException {

	ArrayList tmpMotivations = new ArrayList();
	Statement stmt = null;
	ResultSet rs = null;
	int i = 0;
	Datahandler dh = new Datahandler();
	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT * FROM exercisemotivationstab " +
			       "WHERE exerciseid=" + exId + " AND " +
				"question=" + String.valueOf(question));
	while(rs.next()) {
	    tmpMotivations.add(new ExMotivation(rs.getString("answer"),
						rs.getString("motivation"),
						rs.getBoolean("correct")));
	}
	dh.disconnect();

	return tmpMotivations;
    }
                                  
    /**
     * Gets the pictures associated with an exercise.
     * @param exID The Id of the exercise to get the pictures for.
     * @return ArrayList of strings, containing the requested exercisepictures.
     */
    public ArrayList getExPics(int exId, int question) throws SQLException {

	ArrayList tmpPics = new ArrayList();
	try {
		Statement stmt = null;
		ResultSet rs = null;
		int i = 0;
		Datahandler dh = new Datahandler();
		stmt = dh.connect();
		rs = stmt.executeQuery("SELECT * FROM exerciseimagestab " +
				       "WHERE exerciseid=" + exId + " AND question=" + String.valueOf(question));
		while(rs.next()) {
		    tmpPics.add(rs.getString("imagelocation"));
		}
		dh.disconnect();
	} catch (NullPointerException npe) {			
	}
	return tmpPics;
    }

    /**
     *Saves a new exercise in the database.
     *@param exId A new valid exercise id.
     *@param pcodes The patients associated with this exercise.
     *@param pics The pictures associated with this exercise.
     *@param exTitle The title of the exercise.
     *@param exText The text for the exercise.
     *@param noOfAnswers The number of answers available for the exercise.
     *@param showDate The date that the exercise becomes available.
     *@param current If the exercise is current.
     *@param motivations The motivations for the Answers.
     */
    public void saveExQuestion(int exId, int question, 
			String[] pcodes, String[] pics,
		       	String exTitle, String exText,int questions ,
			int noOfAnswers,
		       	java.sql.Date showDate, boolean current,
		       	ExMotivation[] motivations) throws SQLException {
	Statement stmt = null;
	ResultSet rs = null;
	Datahandler dh = new Datahandler();
	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT * FROM exercisetab WHERE exerciseid=" + String.valueOf(exId));
	if(rs.next()) {

	} else {
		stmt.executeUpdate("INSERT INTO exercisetab VALUES(" +
			   exId + ",'" + exTitle + "','" + 
			   new java.sql.Date(System.currentTimeMillis()) + "','" +
			   showDate.toString() + "'," +
			   (new Boolean(current)).toString() + 
				"," + String.valueOf(questions) + ")");
	}
	stmt.executeUpdate("INSERT INTO exercisequestionstab VALUES(" + String.valueOf(exId) + "," +
				String.valueOf(question) + ",'" + exText + "')");
	for(int i = 0; i < pics.length; i++) {
	    stmt.executeUpdate("INSERT INTO exerciseimagestab " +
			       "VALUES(" + String.valueOf(exId) + "," + 
				String.valueOf(question) + ",'" + 
			       	pics[i] + "')");
	}
	for(int j = 0; j < motivations.length; j++) {
	    stmt.executeUpdate("INSERT INTO exercisemotivationstab " +
			       "VALUES(" + String.valueOf(exId) + "," + 
				String.valueOf(question) + "," +
			       	(new Integer(j)).toString() + ",'" +
			       	motivations[j].exAnswer + "','" +
			       	motivations[j].exMotivation + "'," +
			       	String.valueOf(motivations[j].correct) + 
			       ")");
	}
	dh.disconnect();
    }

    public void setShown(int exId, boolean shown) throws SQLException {
    	Datahandler dh = new Datahandler();
	Statement stmt = dh.connect();
	stmt.executeUpdate("UPDATE exercisetab SET shown=" + 
		String.valueOf(shown) + " WHERE exerciseid=" + 
		String.valueOf(exId));
	dh.disconnect();
    }

    public void updateShowDate(int exId, java.sql.Date date) throws SQLException {
    	Datahandler dh = new Datahandler();
	Statement stmt = dh.connect();
	stmt.executeUpdate("UPDATE exercisetab SET startatdate='" +
			date.toString() + "' WHERE exerciseid=" +
			String.valueOf(exId));
	dh.disconnect();
    }
  
    public void updateTitle(int exId, String newTitle) throws SQLException {
    	Datahandler dh = new Datahandler();
	Statement stmt = dh.connect();
	stmt.executeUpdate("UPDATE exercisetab SET title ='" + newTitle +
			"' WHERE exerciseid=" + String.valueOf(exId));
	dh.disconnect();

    }


    /**
     * Returns the highest exercise id in the database, good for inserting new exercises.
     * @return int, the highest exId value, 0 if an error uccured.
     * @throws SQLException
     */
    public int getMaxId() throws SQLException{
        Datahandler dh = new Datahandler();
	Statement stmt = null;
	ResultSet rs = null;
	int id = 0;
	stmt = dh.connect();
	rs = stmt.executeQuery("SELECT max(exerciseid) FROM exercisetab");
	if (rs.next()) {
	    id = rs.getInt(1);
	}
	dh.disconnect();
	return id;
    }

}
