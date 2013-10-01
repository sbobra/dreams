package com.example.dreams.model;

import com.stackmob.sdk.model.StackMobModel;

public class Sleep extends StackMobModel {
  
    private int startTime;
    private int endTime;
    private int duration;
  
    public Sleep(int startTime, int endTime, int duration) {
        super(Sleep.class);
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }
 
    public void setStartTime(int startTime) { this.startTime = startTime; }
    public int getStartTime() { return this.startTime; }
    
    public void setEndTime(int endTime) { this.endTime = endTime; }
    public int getEndTime() { return this.endTime; }
    
    public void setDuration(int duration) { this.duration = duration; }
    public int getDuration() { return this.duration; }
}