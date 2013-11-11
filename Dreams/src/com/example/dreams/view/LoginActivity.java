package com.example.dreams.view;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Views;

import com.example.dreams.R;
import com.example.dreams.db.DatabaseHelper;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.stackmob.android.sdk.common.StackMobAndroid;

public class LoginActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	@InjectView(R.id.loginButton)
	Button loginButton;
	@InjectView(R.id.nameLayout)
	LinearLayout nameLayout;
	@InjectView(R.id.nameTextBox)
	EditText nameTextBox;
	@InjectView(R.id.newAcctButton)
	Button newUserButton;
	@InjectView(R.id.passwordTextBox)
	EditText passwordTextBox;
	@InjectView(R.id.usernameTextBox)
	EditText usernameTextBox;

	public void checkLogin() {
		// TODO: do something?
	}

	public void getUserData(String username) {
		// TODO: do something?
	}
	
	@OnClick(R.id.loginButton)
	public void login() {
		if (nameLayout.getVisibility() == View.VISIBLE) {
			onNewUserPressed(nameTextBox.getText()
					.toString(), usernameTextBox.getText().toString(),
					passwordTextBox.getText().toString());
		} else {
			onLoginPressed(usernameTextBox.getText()
					.toString(), passwordTextBox.getText().toString());
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		Views.inject(this);
        StackMobAndroid.init(getApplicationContext(), 0, "2448d9d1-3e41-457a-bf29-6b239f5956a5");
        checkLogin();
	}

	public void onLoginPressed(final String username, String password) {
		// TODO: do something?
	}

	@OnClick(R.id.newAcctButton)
	public void onNewUserPressed() {
		nameLayout.setVisibility(View.VISIBLE);
		newUserButton.setVisibility(View.GONE);
	}

	public void onNewUserPressed(final String name, final String username,
			String password) {
		// TODO: do something?

	}
}