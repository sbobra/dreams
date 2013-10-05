package com.example.dreams.model;

import com.stackmob.sdk.model.StackMobModel;

public class Sleep extends StackMobModel {
  
    private int start_time;
    private int end_time;
    private int duration;
    private Dream dream;
  
    public Sleep(int startTime, int endTime, int duration, Dream dream) {
        super(Sleep.class);
        this.start_time = startTime;
        this.end_time = endTime;
        this.duration = duration;
        this.dream = dream;
    }
    
    public Sleep() {
    	super(Sleep.class);
    }
 
    public void setStartTime(int startTime) { this.start_time = startTime; }
    public int getStartTime() { return this.start_time; }
    
    public void setEndTime(int endTime) { this.end_time = endTime; }
    public int getEndTime() { return this.end_time; }
    
    public void setDuration(int duration) { this.duration = duration; }
    public int getDuration() { return this.duration; }
    
    public void setDream(Dream dream) { this.dream = dream; }
    public Dream getDream() { return this.dream; }
}