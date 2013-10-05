package com.example.dreams.model;

public class State {
	private static State instance;
	private String name = "";
	private String username = "";
	private String password = "";
	private boolean isLoggedIn = false;
	public static State getInstance() {
		if (instance == null) {
            instance = new State();
        }
        return instance;
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
    
    public String getName() {
    	return this.name;
    }
    
    public void setUsername(String n) {
    	this.username = n;
    }
    
    public String getUsername() {
    	return this.username;
    }
    
    public void setPassword(String n) {
    	this.password = n;
    }
    
    public String getPassword() {
    	return this.password;
    }
}