package medview.meduweb.datahandler;

//
import medview.meduweb.datahandler.Datahandler;
import java.sql.*;	//JDBC
import java.util.*;       //ArrayList

/**
*This class is a part of the datahandler for the mEduWeb system. 
*It contains methods used for handling comments to a case.
*@author Johanna
*@version 1.0
*/
public class Comment {

	/**
	*This attribute contains a reference to a new Datahandler object.
	*/
	private Datahandler dh = new Datahandler();
	
	/**
	*Returns a list of image locations belonging to a certain comment. 
	*@param pcode the PCode of the case to which the comment belongs.
	*@return ArrayList containing Strings, the locations of the edited images belonging to the specific comment.
	*@throws SQLEXception has to be caught elsewhere.
	*/
	public ArrayList getEditedImages(String pcode) throws SQLException{
	
		ArrayList tmpLocations = new ArrayList();
		int i = 0;		

		Statement dbstmt = null;
		ResultSet rs;
				
		//get image locations from database
		dbstmt = dh.connect();
		String sql = "SELECT imagelocation FROM editedimagestab WHERE pcode = '" + pcode + "'";
		rs = dbstmt.executeQuery(sql);
		
		while (rs.next()) {
			tmpLocations.add(rs.getString(1));
		}

		dh.disconnect();	
		
		return tmpLocations;
	}
	
	/**
	*Returns the text associated with a omment. 
	*@param pcode the PCode of the case to which the comment belongs.
	*@return commentText the comment text
	*@throws java.sql.SQLException has to be caught elsewhere.
	*/
	public String getCommentText(String pcode) throws SQLException{

		String commentText = null;	//if no comment text, return null
	
		Statement dbstmt = null;
		ResultSet rs;
	
		dbstmt = dh.connect();
		
		//Get comment text from database
		String sql = "SELECT comment FROM commenttab where pcode = '" + pcode + "'";
		rs =  dbstmt.executeQuery(sql);
		
		if (rs.next()) {
			commentText = rs.getString(1);	
		}

		dh.disconnect();

		return commentText;
		
	}
	
	/**
	*Saves a comment and stores it in the mEduWeb database. 
	*@param pcode the PCode of the case to which the comment belongs.
	*@param commentText the comment text.
	*@param editedImageLocations A list of locations of the edited images.
	*/	
	public void saveComment(String pcode, String commentText, String[] editedImageLocations) throws SQLException {
		
		Statement dbstmt = null;
		ResultSet rs;

		dbstmt = dh.connect();
	
		String sql = "DELETE FROM commenttab WHERE pcode = '" + pcode + "'";
		String imageSQL;
		int j;
		//delete old comment, catch this exception, since we don't want to throw it
		try {
		    j = dbstmt.executeUpdate(sql);
		} catch (SQLException e) {}

		sql = "INSERT INTO commenttab VALUES ('" + pcode + "', '" + commentText + "')";
		j = dbstmt.executeUpdate(sql);

		for (int i = 0; i < editedImageLocations.length; i++) {
			imageSQL = "INSERT INTO editedimagestab VALUES ('" + pcode + "', '" + editedImageLocations[i] + "')";
			j = dbstmt.executeUpdate(imageSQL);
		}	
		
		dh.disconnect();
	}
	
	/**
	*Deletes a comment.
	*@param pcode the PCode of the case to which the comment belongs.
	*/
	public void deleteComment(String pcode) throws SQLException {
	
		Statement dbstmt = dh.connect();

		String sql = "DELETE FROM commenttab WHERE pcode = '" + pcode + "'";
		dbstmt.executeUpdate(sql);
		
		dh.disconnect();		
		
	}
	
	/**
	*Returns the PCode of every commented case. 
	*@return ArrayList containing the PCode of every commented case, as String.
	*/
	public ArrayList getAllComments() throws SQLException {
		
		ArrayList tmpCases = new ArrayList();
		int i = 0;	
		Statement dbstmt;
		ResultSet rs;
		
		//get PCodes from database
		dbstmt = dh.connect();
		String sql = "SELECT pcode FROM commenttab";
		rs = dbstmt.executeQuery(sql);

		while (rs.next()) {
		    tmpCases.add(rs.getString(1));
		}
			
		dh.disconnect();
		
		return tmpCases;
	}
	
	
}
