package com.example.dreams.view;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Views;

import com.example.dreams.R;
import com.example.dreams.Utils;
import com.example.dreams.model.State;
import com.example.dreams.model.User;
import com.stackmob.android.sdk.common.StackMobAndroid;
import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.stackmob.sdk.model.StackMobModel;
import com.stackmob.sdk.model.StackMobUser;

public class LoginActivity extends Activity {
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
		if(StackMob.getStackMob().isLoggedIn()) {
		    StackMobUser.getLoggedInUser(User.class, new StackMobQueryCallback<User>() {
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

	public void getUserData(String username) {
		String query = "user/" + username;
		StackMobQuery q = new StackMobQuery(query);
		StackMobModel.query(User.class, q,
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
						startActivity(new Intent(LoginActivity.this, HomeActivity.class));
						finish();
					}
				});
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
		password = Utils.sha256(password);
		User user = new User(username, password);
		user.login(new StackMobModelCallback() {

			@Override
			public void failure(StackMobException e) {
				Log.i("LoginController", e.getMessage());
			}

			@Override
			public void success() {
				Log.i("LoginController", "Successful login!");
				getUserData(username);
			}
		});

	}

	@OnClick(R.id.newAcctButton)
	public void onNewUserPressed() {
		nameLayout.setVisibility(View.VISIBLE);
		newUserButton.setVisibility(View.GONE);
	}

	public void onNewUserPressed(final String name, final String username,
			String password) {
		password = Utils.sha256(password);
		User user = new User(name, username, password);
		user.save(new StackMobModelCallback() {
			@Override
			public void failure(StackMobException e) {
				Toast.makeText(getApplicationContext(), "error!",
						Toast.LENGTH_SHORT).show();

				Log.i("LoginController", e.getMessage());
			}

			@Override
			public void success() {
				// TODO: save username and password for persistent login
				Log.i("LoginController", "Successful new user!");
				State.getInstance().setName(name);
				State.getInstance().setLoggedIn(true);

				startActivity(new Intent(LoginActivity.this, HomeActivity.class));
				finish();

			}
		});

	}
}