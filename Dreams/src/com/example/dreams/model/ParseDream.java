package com.example.dreams.model;
import com.parse.ParseObject;
import com.parse.ParseClassName;

@ParseClassName("ParseDream")
public class ParseDream extends ParseObject {
	
	// date (string)
	// amount of sleep (int)
	public int getSleepTotal() {
	    return getInt("sleep");
	  }
	public void setSleepTotal(int value) {
	    put("sleep", value);
	}
	
	public void incrementSleepTotal(int value) {
	    increment("sleep", value);
	}
	
	public String getDate() {
	    return getString("date");
	  }
	public void setDate(String value) {
	    put("date", value);
	}
	
}
