package com.example.gobi;

public class SharedWorkspace {
	private String shareID;
	private String workspaceID;
	private String userID;

	//Constructor
	public SharedWorkspace(String shareID, String workspaceID, String userID) {
		super();
		this.shareID = shareID;
		this.workspaceID = workspaceID;
		this.userID = userID;
	}

	public String getShareID() {
		return shareID;
	}

	public String getWorkspaceID() {
		return workspaceID;
	}

	public String getUserID() {
		return userID;
	}





}
