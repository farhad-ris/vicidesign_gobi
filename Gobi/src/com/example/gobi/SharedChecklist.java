package com.example.gobi;

public class SharedChecklist {

	private String shareID;
	private String userID;
	private String checklistID;

	//Constructor
	public SharedChecklist(String shareID, String userID, String checklistID) {
		super();
		this.shareID = shareID;
		this.userID = userID;
		this.checklistID = checklistID;
	}

	public String getShareID() {
		return shareID;
	}

	public String getUserID() {
		return userID;
	}

	public String getChecklistID() {
		return checklistID;
	}



}
