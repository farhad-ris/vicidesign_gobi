package com.example.gobi;

public class ChecklistComment {

	private String commentID;
	private String commentText;
	private String checklistID;

	//Constructor
	public ChecklistComment(String commentID, String commentText,
			String checklistID) {
		super();
		this.commentID = commentID;
		this.commentText = commentText;
		this.checklistID = checklistID;
	}

	public String getCommentID() {
		return commentID;
	}

	public String getCommentText() {
		return commentText;
	}

	public String getChecklistID() {
		return checklistID;
	}








}
