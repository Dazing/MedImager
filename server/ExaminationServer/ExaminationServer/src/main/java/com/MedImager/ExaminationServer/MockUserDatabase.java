package com.MedImager.ExaminationServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockUserDatabase{
	
	public static List<User> users = new ArrayList<>();
	
	// 
	static{
		users.add(new User("rune", "runepass", "admin", "runetoken"));
		users.add(new User("börje", "börjepass", "user", "börjetoken"));
		users.add(new User("arne", "arnepass", "user"));
	}
	
	public void registerUser(String username, String password){
		users.add(new User(username, password));
	}
	
	public void insertToken(String username, String token){
		for(User user : users){
			if(user.getUsername().equals(username)){
				user.setToken(token);
			}
		}
	}
	
	public void register(String username, String password) {
		// TODO Auto-generated method stub
		
	}
	
	public void login(String username, String password) {
		// TODO Auto-generated method stub
		
	}
	
	public static void authenticateUser(String username, String password) throws Exception{
		for(User user : users){
    		if(user.getUsername().equals(username) && user.getPassword().equals(password)){
    			return;
    		}
    	}
    	throw new Exception();
	}
	
	public static String issueToken(String username) {
    	// TODO: 
    	// Store token in database tied to user
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token
    	
    	// Mock
    	String token = username + "token";
    	for(User user : MockUserDatabase.users){
    		if(user.getUsername().equals(username)){
    			user.setToken(token);
    		}
    	}
    	return token;
    }
}
