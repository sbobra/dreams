package com.example.dreams.controller;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.net.ParseException;
import android.util.Log;
import android.widget.Toast;

import com.example.dreams.model.State;
import com.example.dreams.model.User;
import com.example.dreams.view.HomeActivity;
import com.example.dreams.view.LoginActivity;
import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginController {
	private LoginActivity view;

	public LoginController(LoginActivity activity) {
		this.view = activity;

	}
	
	public void checkLogin() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
		  // do stuff with the user
			Log.i("LoginController", "User found! Welcome " + currentUser.getString("name"));
			view.startActivity(new Intent(view, HomeActivity.class));
			view.finish();
		} else {
		  // show the signup or login screen
			Log.i("LoginController", "No logged in user found!");
		}
		
//		if(StackMob.getStackMob().isLoggedIn()) {
//		    User.getLoggedInUser(User.class, new StackMobQueryCallback<User>() {
//				@Override
//				public void failure(StackMobException arg0) {
//					// continue with login process
//					Log.i("LoginController", "No logged in user found!");
//
//				}
//
//				@Override
//				public void success(List<User> arg0) {
//					// TODO Auto-generated method stub
//					Log.i("LoginController", "User already logged in!");
//		            User loggedInUser = arg0.get(0);
//		            State.getInstance().setUsername(loggedInUser.getUsername());
//		            getUserData(State.getInstance().getUsername());
//				}
//		    });
//		}
	}

	public void onNewUserPressed(final String name, final String username,
			String password) {
		password = Utils.sha256(password);
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(username);
		user.setName(name);
		
		user.signUpInBackground(new SignUpCallback() {
			@Override
			public void done(com.parse.ParseException arg0) {
				// TODO Auto-generated method stub
				if (arg0 == null) {
				      // Hooray! Let them use the app now.
						Log.i("LoginController", "Successful new user!");
						State.getInstance().setName(name);
						State.getInstance().setLoggedIn(true);
	
						view.startActivity(new Intent(view, HomeActivity.class));
						view.finish();
				    } else {
				      // Sign up didn't succeed. Look at the ParseException
				      // to figure out what went wrong
				    	Toast.makeText(view.getApplicationContext(), "error!",
								Toast.LENGTH_SHORT).show();
				    	Log.i("LoginController", arg0.getMessage());
				    }
			}
			});
//		
//		User user = new User(name, username, password);
//		user.save(new StackMobModelCallback() {
//			@Override
//			public void success() {
//				// TODO: save username and password for persistent login
//				Log.i("LoginController", "Successful new user!");
//				State.getInstance().setName(name);
//				State.getInstance().setLoggedIn(true);
//
//				view.startActivity(new Intent(view, HomeActivity.class));
//				view.finish();
//
//			}
//
//			@Override
//			public void failure(StackMobException e) {
//				Toast.makeText(view.getApplicationContext(), "error!",
//						Toast.LENGTH_SHORT).show();
//
//				Log.i("LoginController", e.getMessage());
//			}
//		});

	}

	public void onLoginPressed(final String username, String password) {
		password = Utils.sha256(password);
		
		User.logInInBackground(username, password, new LogInCallback() {
			@Override
			public void done(ParseUser arg0, com.parse.ParseException arg1) {
				// TODO Auto-generated method stub
				if (arg0 != null) {
					Log.i("LoginController", "Login successful! Welcome " + arg0.getString("name"));
					// Hooray! The user is logged in.
					view.startActivity(new Intent(view, HomeActivity.class));
					view.finish();
				} else {
					// Signup failed. Look at the ParseException to see what happened.
					Log.i("LoginController", arg1.getMessage());
				}
				
			}
			});
		
//		User user = new User(username, password);
//		user.login(new StackMobModelCallback() {
//
//			@Override
//			public void success() {
//				Log.i("LoginController", "Successful login!");
//				getUserData(username);
//			}
//
//			@Override
//			public void failure(StackMobException e) {
//				Log.i("LoginController", e.getMessage());
//			}
//		});

	}
//
//	public void getUserData(String username) {
//		// TODO Auto-generated method stub
//		String query = "user/" + username;
//		StackMobQuery q = new StackMobQuery(query);
//		User.query(User.class, q,
//				StackMobOptions.selectedFields(Arrays.asList("name")),
//				new StackMobQueryCallback<User>() {
//					@Override
//					public void failure(StackMobException e) {
//						Log.i("LoginController", e.getMessage());
//					}
//
//					@Override
//					public void success(List<User> arg0) {
//						State.getInstance().setName(arg0.get(0).getName());
//						Log.i("LoginController", "Welcome, "
//								+ State.getInstance().getName());
//						view.startActivity(new Intent(view, HomeActivity.class));
//						view.finish();
//					}
//				});
//
//	}

}