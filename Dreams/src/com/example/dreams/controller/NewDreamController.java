package com.example.dreams.controller;

import android.content.SharedPreferences;

import com.example.dreams.view.NewDreamActivity;

public class NewDreamController {

	private NewDreamActivity view;
	public static final String PREFS_NAME = "timeStamps";

	public NewDreamController(NewDreamActivity activity) {
		this.view = activity;

	}

	public String getLastBedTime() {
		// Restore preferences
		SharedPreferences settings = view.getSharedPreferences(PREFS_NAME, 0);
		return (settings.getString("bedtime", ""));
	}

}
