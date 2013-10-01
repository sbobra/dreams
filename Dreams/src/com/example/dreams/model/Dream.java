package com.example.dreams.model;

import com.stackmob.sdk.model.StackMobModel;

public class Dream extends StackMobModel {
  
    private String note;
  
    public Dream(String note) {
        super(Sleep.class);
        this.note = note;
    }
 
    public void setNote(String note) { this.note = note; }
    public String getNote() { return this.note; }
}