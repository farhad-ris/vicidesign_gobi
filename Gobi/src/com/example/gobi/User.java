package com.example.gobi;

public class User {

	private String userID;
	private String username;
	//private photo;
	private String password;
	private String email;

	//Constructor
	public User(String userID, String username, String password, String email) {
		super();
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.email = email;
	}

	public String getUserID() {
		return userID;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}





}
