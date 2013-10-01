package com.example.dreams.model;

public class State {
	private static State instance;
	private String name = "";
	private String username = "";
	private int id = 0;
	public static State getInstance() {
		if (instance == null) {
            instance = new State();
        }
        return instance;
	}

    public void setName(String n) {
    	this.name = n;
    }
    
    public String getName() {
    	return this.name;
    }
    
    public void setId(int id) {
    	this.id = id;
    }
    
    public int getId() {
    	return this.id;
    }
}