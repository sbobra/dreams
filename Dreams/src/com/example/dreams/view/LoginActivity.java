package com.example.dreams.view;
import android.app.Activity;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseUser;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.dreams.R;
import com.example.dreams.controller.LoginController;
import com.example.dreams.model.Day;
import com.example.dreams.model.ParseDream;
import com.example.dreams.model.User;
import com.parse.ParseObject;
import com.stackmob.android.sdk.common.StackMobAndroid;

public class LoginActivity extends Activity {
	private LoginController controller;
	private Button loginButton;
	private Button newUserButton;
	private EditText usernameTextBox;
	private EditText passwordTextBox;
	private EditText nameTextBox;
	private LinearLayout nameLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
//        StackMobAndroid.init(getApplicationContext(), 0, "2448d9d1-3e41-457a-bf29-6b239f5956a5");

	    ParseObject.registerSubclass(Day.class);
	    ParseUser.registerSubclass(ParseDream.class);
	    ParseUser.registerSubclass(User.class);
        Parse.initialize(this, "iyhupL8h8uVQc189NEoSQmkoUnQYMk4vpUaAi5Pb", "JbNCCk31poMMa7m0FEaz1pjRbBio2AGS4X8azZlF"); 
        ParseAnalytics.trackAppOpened(getIntent());
        
        
		controller = new LoginController(this);
		controller.checkLogin();

		usernameTextBox = (EditText) findViewById(R.id.usernameTextBox);
		passwordTextBox = (EditText) findViewById(R.id.passwordTextBox);
		nameTextBox = (EditText) findViewById(R.id.nameTextBox);
		nameLayout = (LinearLayout) findViewById(R.id.nameLayout);

		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (nameLayout.getVisibility() == View.VISIBLE) {
					controller.onNewUserPressed(nameTextBox.getText()
							.toString(), usernameTextBox.getText().toString(),
							passwordTextBox.getText().toString());
				} else {
					controller.onLoginPressed(usernameTextBox.getText()
							.toString(), passwordTextBox.getText().toString());
				}
			}
		});
		newUserButton = (Button) findViewById(R.id.newAcctButton);
		newUserButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onNewUserPressed();
				newUserButton.setVisibility(View.GONE);
			}
		});

	}

	public void onNewUserPressed() {
		nameLayout.setVisibility(View.VISIBLE);
	}
}