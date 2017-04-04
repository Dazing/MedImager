package com.MedImager.ExaminationServer;

public interface IDatabase{
	
	public void register(String username, String password);
	public void login(String username, String password);
	public void insertToken(String username, String token);
	public void authenticateUser(String username, String password) throws Exception;
	
}
