package com.MedImager.ExaminationServer;

public class User{
	
	private String id;
	private String username;
	private String userPermission;
	private String firstName;
	private String lastName;
	
	public User(){}
	
	public User(String id, String username, String userPermission, String firstName,
			String lastName){
		this.id = id;
		this.username = username;
		this.userPermission = userPermission;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getUserPermission(){
		return userPermission;
	}
	
	public void setUserPermission(String userPermission){
		this.userPermission = userPermission;
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	
	public String getLastName(){
		return lastName;
	}
	
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
}
