package com.example.dreams.model;
import java.util.List;

import com.stackmob.sdk.model.StackMobUser;

public class User extends StackMobUser {
  
    private List<Sleep> sleepList;
    private String name;
  
    public User(String name, String username, String password) {
        super(User.class, username, password);
        this.name = name;
    }
    
    public void setName(String n) {
    	this.name = n;
    }
    
    public String getName() {
    	return this.name;
    }
  
    public List<Sleep> getSleepList() {
        return sleepList;
    }
  
}