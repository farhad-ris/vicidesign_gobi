package com.example.gobi;

public class ProjectComment {
	private String commentID;
	private String projectCommentText;
	private String projectID;

	//Constructor
	public ProjectComment(String commentID, String projectCommentText,
			String projectID) {
		super();
		this.commentID = commentID;
		this.projectCommentText = projectCommentText;
		this.projectID = projectID;
	}

	public String getCommentID() {
		return commentID;
	}

	public String getProjectCommentText() {
		return projectCommentText;
	}

	public String getProjectID() {
		return projectID;
	}




}
