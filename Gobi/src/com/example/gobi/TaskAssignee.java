package com.example.gobi;

public class TaskAssignee {

	private String assigneeID;
	private String userEmail;
	private String username;
	private String taskID;

	//Constructor
	public TaskAssignee(String assigneeID, String userEmail, String username,
			String taskID) {
		super();
		this.assigneeID = assigneeID;
		this.userEmail = userEmail;
		this.username = username;
		this.taskID = taskID;
	}

	public String getAssigneeID() {
		return assigneeID;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getUsername() {
		return username;
	}

	public String getTaskID() {
		return taskID;
	}





}
