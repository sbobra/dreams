package com.example.dreams.view;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.dreams.R;
import com.example.dreams.controller.LoginController;
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
        StackMobAndroid.init(getApplicationContext(), 0, "2448d9d1-3e41-457a-bf29-6b239f5956a5");

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