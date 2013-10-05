package com.example.dreams.controller;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.dreams.model.State;
import com.example.dreams.model.User;
import com.example.dreams.view.HomeActivity;
import com.example.dreams.view.LoginActivity;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;

public class LoginController {
	private LoginActivity view;

	public LoginController(LoginActivity activity) {
		this.view = activity;

	}
	
	public void checkLogin() {
		if(StackMob.getStackMob().isLoggedIn()) {
		    User.getLoggedInUser(User.class, new StackMobQueryCallback<User>() {
				@Override
				public void failure(StackMobException arg0) {
					// continue with login process
					Log.i("LoginController", "No logged in user found!");

				}

				@Override
				public void success(List<User> arg0) {
					// TODO Auto-generated method stub
					Log.i("LoginController", "User already logged in!");
		            User loggedInUser = arg0.get(0);
		            State.getInstance().setUsername(loggedInUser.getUsername());
		            getUserData(State.getInstance().getUsername());
				}
		    });
		}
	}

	public void onNewUserPressed(final String name, final String username,
			String password) {
		password = Utils.sha256(password);
		User user = new User(name, username, password);
		user.save(new StackMobModelCallback() {
			@Override
			public void success() {
				// TODO: save username and password for persistent login
				Log.i("LoginController", "Successful new user!");
				State.getInstance().setName(name);
				State.getInstance().setLoggedIn(true);

				view.startActivity(new Intent(view, HomeActivity.class));
				view.finish();

			}

			@Override
			public void failure(StackMobException e) {
				Toast.makeText(view.getApplicationContext(), "error!",
						Toast.LENGTH_SHORT).show();

				Log.i("LoginController", e.getMessage());
			}
		});

	}

	public void onLoginPressed(final String username, String password) {
		password = Utils.sha256(password);
		User user = new User(username, password);
		user.login(new StackMobModelCallback() {

			@Override
			public void success() {
				Log.i("LoginController", "Successful login!");
				getUserData(username);
			}

			@Override
			public void failure(StackMobException e) {
				Log.i("LoginController", e.getMessage());
			}
		});

	}

	public void getUserData(String username) {
		// TODO Auto-generated method stub
		String query = "user/" + username;
		StackMobQuery q = new StackMobQuery(query);
		User.query(User.class, q,
				StackMobOptions.selectedFields(Arrays.asList("name")),
				new StackMobQueryCallback<User>() {
					@Override
					public void failure(StackMobException e) {
						Log.i("LoginController", e.getMessage());
					}

					@Override
					public void success(List<User> arg0) {
						State.getInstance().setName(arg0.get(0).getName());
						Log.i("LoginController", "Welcome, "
								+ State.getInstance().getName());
						view.startActivity(new Intent(view, HomeActivity.class));
						view.finish();
					}
				});

	}

}