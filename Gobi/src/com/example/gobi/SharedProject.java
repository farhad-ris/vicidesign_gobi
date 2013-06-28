package com.example.gobi;

public class SharedProject {

	private String shareID;
	private String userID;
	private String projectID;

	//Construtor
	public SharedProject(String shareID, String userID, String projectID) {
		super();
		this.shareID = shareID;
		this.userID = userID;
		this.projectID = projectID;
	}

	public String getShareID() {
		return shareID;
	}

	public String getUserID() {
		return userID;
	}

	public String getProjectID() {
		return projectID;
	}




}
