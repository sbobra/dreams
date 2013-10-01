package com.example.dreams.controller;

import java.security.MessageDigest;

import com.example.dreams.model.User;
import com.example.dreams.view.LoginActivity;

public class LoginController {
	private LoginActivity view;

	public LoginController(LoginActivity activity) {
		this.view = activity;

	}

	public void onNewUserPressed(String name, String username, String password) {
		password = Utils.sha256(password);
		User user = new User(name, username, password);
		user.save();
		//State.getInstance().setName(name);

	}

	public void onLoginPressed(String username, String password) {
		
	}

}