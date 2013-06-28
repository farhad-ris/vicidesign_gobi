package com.example.gobi;

public class TaskComment {

	private String commentID;
	private String taskCommentText;
	private String taskID;

	//Constructor
	public TaskComment(String commentID, String taskCommentText, String taskID) {
		super();
		this.commentID = commentID;
		this.taskCommentText = taskCommentText;
		this.taskID = taskID;
	}

	public String getCommentID() {
		return commentID;
	}

	public String getTaskCommentText() {
		return taskCommentText;
	}

	public String getTaskID() {
		return taskID;
	}






}
