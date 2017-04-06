package com.MedImager.ExaminationServer;

public class User {
	
	private String username;
	private String password;
	private String token;
	
	// Default value for now
	private String role = "user";
	
	public User(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public User(String username, String password, String role){
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	public User(String username, String password, String role, String token){
		this.username = username;
		this.password = password;
		this.role = role;
		this.token = token;
	}
	
	public User (int id, String username, String email, String firstname, String lastname, String userPermission){
		
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
