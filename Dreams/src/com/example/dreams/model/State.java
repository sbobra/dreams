package com.example.dreams.model;

public class State {
	private static State instance;
	public static State getInstance() {
		if (instance == null) {
            instance = new State();
        }
        return instance;
	}
	private boolean isLoggedIn = false;
	private String name = "";
	private String password = "";
	private String username = "";
	
	public String getName() {
    	return this.name;
    }
	
	public String getPassword() {
    	return this.password;
    }

    public String getUsername() {
    	return this.username;
    }
    
    public boolean isLoggedIn() {
		return this.isLoggedIn;
	}
    
    public void setLoggedIn(boolean b) {
		this.isLoggedIn = b;
	}
    
    public void setName(String n) {
    	this.name = n;
    }
    
    public void setPassword(String n) {
    	this.password = n;
    }
    
    public void setUsername(String n) {
    	this.username = n;
    }
}