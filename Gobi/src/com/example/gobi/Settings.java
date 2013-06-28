package com.example.gobi;

public class Settings {

	private String userID;
	private boolean autohide;
	private int theme;
	//check the datatype for theme.
	private String privacy;

	//Constructor
	public Settings(String userID, boolean autohide, int theme, String privacy) {
		super();
		this.userID = userID;
		this.autohide = autohide;
		this.theme = theme;
		this.privacy = privacy;
	}

	public String getUserID() {
		return userID;
	}

	public boolean isAutohide() {
		return autohide;
	}

	public int getTheme() {
		return theme;
	}

	public String getPrivacy() {
		return privacy;
	}



}
