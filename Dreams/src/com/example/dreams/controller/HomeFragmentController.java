package com.example.dreams.controller;

import android.content.SharedPreferences;

import com.example.dreams.view.HomeFragment;

public class HomeFragmentController {
	private HomeFragment fragment;
	public static final String PREFS_NAME = "timeStamps";

	public HomeFragmentController(HomeFragment homeFragment) {
		this.fragment = homeFragment;

	}

	public void saveTime() {
		SharedPreferences settings = fragment.getActivity().getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("bedtime", ""+System.currentTimeMillis());

		// Commit the edits!
		editor.commit();
	}

	public void onRefresh() {
		// refresh
	}
}
