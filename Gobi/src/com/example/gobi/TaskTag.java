package com.example.gobi;

public class TaskTag {

	private String tagID;
	private String tagText;
	private String taskID;

	//Constructor
	public TaskTag(String tagID, String tagText, String taskID) {
		super();
		this.tagID = tagID;
		this.tagText = tagText;
		this.taskID = taskID;
	}

	public String getTagID() {
		return tagID;
	}

	public String getTagText() {
		return tagText;
	}

	public String getTaskID() {
		return taskID;
	}




}
