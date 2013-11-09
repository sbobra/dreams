package com.example.dreams.model;
import com.parse.ParseUser;
import com.parse.ParseClassName;

@ParseClassName("DreamUser")
public class User extends ParseUser {
	
//	public int getDayList() {
//	    return getInt("sleep");
//	  }
	public void addToDayList(Day d) {
	    add("days", d);
	}
	
	public void setName(String value) {
		put("name", value);
	}
	
	public String getName() {
		return getString("name");
	}
	
  
//    private List<Sleep> sleep_list;
//    private List<Sleep> dream_list;
//    private String name;
//  
//    public User(String name, String username, String password) {
//        super(User.class, username, password);
//        this.name = name;
//        this.sleep_list = new ArrayList<Sleep>();
//        this.dream_list = new ArrayList<Sleep>();
//    }
//    
//    public User(String username, String password) {
//        super(User.class, username, password);
//        this.sleep_list = new ArrayList<Sleep>();
//        this.dream_list = new ArrayList<Sleep>();
//    }
//    
//    public void setName(String n) {
//    	this.name = n;
//    }
//    
//    public String getName() {
//    	return this.name;
//    }
//  
//    public List<Sleep> getSleepList() {
//        return sleep_list;
//    }
//    
//    public List<Sleep> getDreamList() {
//        return dream_list;
//    }
  
}