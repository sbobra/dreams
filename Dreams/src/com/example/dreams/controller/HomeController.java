package com.example.dreams.controller;

import android.content.Intent;
import android.util.Log;

import com.example.dreams.model.State;
import com.example.dreams.model.User;
import com.example.dreams.view.HomeActivity;
import com.example.dreams.view.LoginActivity;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

public class HomeController {
	private HomeActivity view;

	public HomeController(HomeActivity activity) {
		this.view = activity;

	}

	public void logout() {
		User user = new User(State.getInstance().getUsername(), State
				.getInstance().getPassword());
		user.logout(new StackMobModelCallback() {
			@Override
			public void success() {
				//TODO: logout on shared preferences
				Log.i("HomeController", "Successful logout!");
				view.startActivity(new Intent(view, LoginActivity.class));
				view.finish();
			}

			@Override
			public void failure(StackMobException arg0) {
				Log.i("HomeController", arg0.getMessage());
			}
		});
	}

}