package com.example.dreams.controller;

import android.content.Intent;
import android.util.Log;

import com.example.dreams.model.State;
import com.example.dreams.model.User;
import com.example.dreams.view.HomeActivity;
import com.example.dreams.view.LoginActivity;
import com.parse.ParseUser;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

public class HomeController {
	private HomeActivity view;

	public HomeController(HomeActivity activity) {
		this.view = activity;

	}

	public void logout() {
		ParseUser.logOut();
		if (ParseUser.getCurrentUser() == null) {
			//TODO: logout on shared preferences
			Log.i("HomeController", "Successful logout!");
			view.startActivity(new Intent(view, LoginActivity.class));
			view.finish();
		} else {
			Log.i("HomeController", "failed to log out");
		}
	}

}