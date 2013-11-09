package com.example.dreams.model;

import com.stackmob.sdk.model.StackMobModel;

public class Sleep extends StackMobModel implements Comparable<Sleep>{
  
    private Dream dream;
    private String end_time;
    private String start_time;
  
    public Sleep() {
    	super(Sleep.class);
    }
    
    public Sleep(String startTime, String endTime, Dream dream) {
        super(Sleep.class);
        this.start_time = startTime;
        this.end_time = endTime;
        this.dream = dream;
    }
 
    @Override
	public int compareTo(Sleep another) {
		long time1 = Long.valueOf(start_time);
		long time2 = Long.valueOf(another.getStartTime());
		if (time1 > time2)
			return -1;
		else if (time1 < time2)
			return 1;
		else return 0;
	}
    public Dream getDream() { return this.dream; }
    
    public String getEndTime() { return this.end_time; }
    public String getStartTime() { return this.start_time; }
    
    public void setDream(Dream dream) { this.dream = dream; }
    public void setEndTime(String endTime) { this.end_time = endTime; }

	public void setStartTime(String startTime) { this.start_time = startTime; }
}